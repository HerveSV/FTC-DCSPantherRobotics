package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import helperClassesA.PIDController;
import helperClassesA.PIDFmanager;

@TeleOp
public class TeleOp2 extends LinearOpMode {

    static final double COUNTS_PER_MOTOR_REV = 1478.4;

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    private DcMotor leftIntake;
    private DcMotor rightIntake;

    private Servo leftGrabber;
    private Servo rightGrabber;

    private Servo servoHinge;

    private DcMotorEx armHinge;
    private DcMotor armExtension;

    private static final int ARM_H_ACTIVATION_TARGET = 800;
    private static final int ARM_H_COUNT_BUFFER = 50;
    private static final int ARM_H_COUNT_LOWER_LIMIT = 0;
    private static final int ARM_H_COUNT_UPPER_LIMIT = 1500;
    private static final int ARM_H_180_COUNTS = ARM_H_COUNT_UPPER_LIMIT - ARM_H_COUNT_LOWER_LIMIT;
    private static final int ARM_H_MAX_ANGLE = 180;
    private static final int ARM_H_MIN_ANGLE = 0;
    private static final int ARM_H_GEAR_RATIO = 6;


    private static final int MAX_STONE_REACH = 10;

    private static final int[][] ARM_STONE_STAGES =
            {
                    {1, 2}, //val one is hinge count and val 2 is extension count to achieve required height for n amount of blocks height
                    {5, 3},
                    {4, 2},
                    {},
                    {},
                    {},
                    {},
                    {},
                    {},
                    {},
            };


    private static final int ARM_X_COUNTS_PER_MM = (int) COUNTS_PER_MOTOR_REV/5;
    private static final int ARM_X_STEP = ARM_X_COUNTS_PER_MM * 5;
    private static final int ARM_X_COUNT_BUFFER = 50;
    private static final int ARM_X_COUNT_UPPER_LIMIT = 10000;
    private static final int ARM_X_COUNT_LOWER_LIMIT= 0;

    PIDController hingePID = new PIDController(PIDFmanager.getP(), PIDFmanager.getI(), PIDFmanager.getD());

    @Override
    public void runOpMode()
    {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        leftIntake = hardwareMap.get(DcMotor.class, "leftIntake");
        rightIntake = hardwareMap.get(DcMotor.class, "rightIntake");

        leftGrabber = hardwareMap.get(Servo.class, "leftGrabber");
        rightGrabber = hardwareMap.get(Servo.class, "rightGrabber");

        servoHinge = hardwareMap.get(Servo.class, "servoHinge");

        armHinge = hardwareMap.get(DcMotorEx.class, "armHingeMotor");
        armExtension = hardwareMap.get(DcMotor.class, "armExtensionMotor");

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        /* Wait for the game to start (driver presses PLAY) waitForStart(); */
        //leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        leftGrabber.setDirection(Servo.Direction.REVERSE);
        //armHingeMotor.setDirection(DcMotorSimple.Direction.REVERSE); //positive power needs to equal the armHinge moving up.

        //hingePID.setContinuous(true);
        hingePID.setInputRange(0, 180);
        hingePID.setOutputRange(-1, 1);
        //hingePID.setTolerance();

        PIDFmanager.setPIDF(armHinge);
        PIDFmanager.setPIDF(armExtension);
        /**
         Robot wheel mapping:
                     X
                   X   X
                 X FRONT X
               X           X
             X  LF       RF  X
           X                   X
         X                       X            Mecanum need to be aligned to create a diamond-square shape
           X                   X
             X  LB       RB  X
               X           X
                 X       X
                   X   X
                     X

         */

        /**
         GAMEPAD 1:
         Left stick x: Controls robot turn
         Right stick x: Controls robot strafe
         Right stick y: Controls robot tank drive
         B: Toggles Slow Move mode (4x slower)

         GAMEPAD 2:
         Left trigger: Moves arm hinge counter-clockwise
         Right trigger: Moves arm hinge clockwise
         Left bumper: Toggles stone grabbers
         Right bumper: Toggles stone intake system
         Dpad Up: Increments stone target
         Dpad Down: Decrements stone target
         Dpad Left: Retracts arm extension
         Dpad Right: Increases arm extension
         X: Toggles arm into intake and active state
         Y: Refreshes arm position to fit the stone target


         */

        final double toggleDelay = 0.25;

        /* run until the end of the match (driver presses STOP) */
        double forwards = 1.0;
        double backwards = -1.0;


        boolean slowMove = false;
        double b1ToggleTime = 0;
        double speedMod = 1;


        boolean moveIntake = false;
        double a2ToggleTime = 0;

        boolean closeGrabbers = false;

        boolean keepArmHingePos = false;
        int armHingeTarget = 0;
        int hingeTargetAngle = 0;
        boolean armHingeActive = true;
        double x2ToggleTime = 0;


        int armExtensionTarget = 0;

        int targetStoneNum = 0;
        double upDown2ToggleTime = 0;
        boolean refreshingHinge = false;
        boolean refreshingExtension = false;
        //boolean hingeLargerThanStoneTarget = false;


        waitForStart();

        while (opModeIsActive()) {


            /**
             * Gamepad 1
             */


            boolean B_button1 = this.gamepad1.b;
            if(B_button1 && slowMove && (this.getRuntime()-b1ToggleTime)>= toggleDelay) {
                slowMove = false;
                speedMod = 1;
                b1ToggleTime = this.getRuntime();
            }
            else if(B_button1 && (this.getRuntime()-b1ToggleTime)>= toggleDelay) {
                slowMove = true;
                speedMod = 0.25;
                b1ToggleTime = this.getRuntime();
            }


            //standard concept for holonomic movement

            double x = this.gamepad1.right_stick_x;
            double y = -this.gamepad1.right_stick_y;
            double turn = this.gamepad1.left_stick_x;


            double leftFrontPwr = y-x+turn;//y+x+turn;//
            double leftBackPwr =  y+x+turn;//y-x+turn;//
            double rightFrontPwr = y+x-turn;//y+x-turn;//
            double rightBackPwr = y-x-turn;//y+x-turn;//


            leftFront.setPower(Range.clip(leftFrontPwr * speedMod, -1, 1)); //clip here is just a safety measure
            leftBack.setPower(Range.clip(leftBackPwr * speedMod, -1, 1));
            rightFront.setPower(Range.clip(rightFrontPwr * speedMod, -1, 1));
            rightBack.setPower(Range.clip(rightBackPwr * speedMod, -1, 1));




            boolean leftStrafe = this.gamepad1.dpad_left;
            boolean rightStrafe = this.gamepad1.dpad_right;
            boolean backStrafe = this.gamepad1.dpad_up;
            boolean frontStrafe = this.gamepad1.dpad_down;


            if (leftStrafe) {  // Left strafe
                leftFront.setPower(forwards * speedMod);
                leftBack.setPower(backwards * speedMod);
                rightFront.setPower(backwards * speedMod);
                rightBack.setPower(forwards * speedMod);
            }
            else if (rightStrafe) {// Right strafe
                leftFront.setPower(backwards * speedMod);
                leftBack.setPower(forwards * speedMod);
                rightFront.setPower(forwards * speedMod);
                rightBack.setPower(backwards * speedMod);
            }
            else if (frontStrafe) {
                leftFront.setPower(forwards * speedMod);
                leftBack.setPower(forwards * speedMod);
                rightFront.setPower(forwards * speedMod);
                rightBack.setPower(forwards * speedMod);
            }
            else if (backStrafe) {
                leftFront.setPower(backwards * speedMod);
                leftBack.setPower(backwards * speedMod);
                rightFront.setPower(backwards * speedMod);
                rightBack.setPower(backwards * speedMod);
            }


            /**
             * Gamepad 2
             */


            /**
             * Stone grabbers
             */
            boolean left_bumper2 = this.gamepad2.left_bumper;
            if(left_bumper2 && closeGrabbers && (this.getRuntime()-a2ToggleTime)>= toggleDelay) {
                closeGrabbers = false;
                a2ToggleTime = this.getRuntime();
            }
            else if(left_bumper2 && (this.getRuntime()-a2ToggleTime)>= toggleDelay) {
                closeGrabbers = true;
                a2ToggleTime = this.getRuntime();
            }

            if(closeGrabbers)
            {
                leftGrabber.setPosition(0.2);
                rightGrabber.setPosition(0.2);
            }
            else
            {
                leftGrabber.setPosition(0);
                rightGrabber.setPosition(0);
            }


            /**
             * Stone intake system
             */
            boolean right_bumper1 = this.gamepad1.right_bumper;
            if(right_bumper1 && moveIntake && (this.getRuntime()-a2ToggleTime)>= toggleDelay) {
                moveIntake = false;
                a2ToggleTime = this.getRuntime();
            }
            else if(right_bumper1 && (this.getRuntime()-a2ToggleTime)>= toggleDelay) {
                moveIntake = true;
                a2ToggleTime = this.getRuntime();
            }

            if(moveIntake)
            {
                leftIntake.setPower(-1);
                rightIntake.setPower(1);
            }
            else
            {
                leftIntake.setPower(0);
                rightIntake.setPower(0);
            }


            /**
             * ARM ACTIVE
             */
            boolean X_button2 = this.gamepad2.x;
            if(X_button2 && armHingeActive && (this.getRuntime()-x2ToggleTime)>= toggleDelay) {
                armHingeActive = false;
                x2ToggleTime = this.getRuntime();
            }
            else if(X_button2 && (this.getRuntime()-x2ToggleTime)>= toggleDelay && !armHingeActive) {
                armHingeActive = true;
                x2ToggleTime = this.getRuntime();
            }



            //angle and extension of arm can be changed to presets
            //that allow for a certain stone height to be achieved
            if((this.gamepad2.dpad_up || this.gamepad2.dpad_down) && (this.getRuntime()-upDown2ToggleTime)>= toggleDelay)
            {
                if(this.gamepad2.dpad_up)
                {
                    if(targetStoneNum < MAX_STONE_REACH)
                    {
                        ++targetStoneNum;
                    }
                }
                else
                {
                    if(targetStoneNum > 0)
                    {
                        --targetStoneNum;
                    }
                }
                upDown2ToggleTime = this.getRuntime();
            }

            if(this.gamepad2.y && !refreshingHinge && !refreshingExtension && armHingeActive)
            {
                refreshingHinge = true;
                refreshingExtension = true;
                armHinge.setTargetPosition(ARM_STONE_STAGES[targetStoneNum-1][0]);
                armHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armHinge.setPower(0.5);
                armExtension.setTargetPosition(ARM_STONE_STAGES[targetStoneNum-1][1]);
                armExtension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armExtension.setPower(0);
            }


            if(!armHingeActive)//if armHingeActive == false
            {
                if(armHinge.getCurrentPosition() > ARM_H_COUNT_LOWER_LIMIT + ARM_H_COUNT_BUFFER)
                {
                    hingePID.setSetpoint(0);
                    armHinge.setPower(hingePID.performPID(armHinge.getCurrentPosition()));
                }
                else
                {
                    armHinge.setPower(0);
                }
            }
            /*else if(refreshingHinge)
            {
                if(!armHingeMotor.isBusy())
                {
                    armHingeMotor.setPower(0);
                    refreshingHinge = false;

                    armExtensionMotor.setPower(0.5);
                }
            }
            else if(refreshingExtension)
            {
                if(!armExtensionMotor.isBusy())
                {
                    armExtensionMotor.setPower(0);
                    refreshingExtension = false;
                }
            }*/

            /**
             * ARM NORMAL
             */
            else
            {

                if(this.gamepad1.right_bumper)
                {
                    hingeTargetAngle += 10;
                    //armHinge.setTargetPosition(hingeTargetAngle);
                    hingePID.setSetpoint(hingeTargetAngle);
                }
                else if(this.gamepad1.left_bumper)
                {
                    hingeTargetAngle -= 10;
                    hingePID.setSetpoint(hingeTargetAngle);
                }
                hingeTargetAngle = Range.clip(hingeTargetAngle, 0, 180);



                armHinge.setPower(hingePID.performPID(getArmAngle180()));




                /*if(!keepArmHingePos && armHingePower == 0 && armHingeMotor.getCurrentPosition() > ARM_H_COUNT_LOWER_LIMIT +ARM_H_COUNT_LOWER_LIMIT)
                {
                    keepArmHingePos = true;
                    armHingeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    armHingeTarget = armHingeMotor.getCurrentPosition();
                    hingePID.enable();
                    hingePID.setSetpoint(armHingeTarget);
                }
                else if(keepArmHingePos && armHingePower != 0)
                {
                    //armHingeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    armHingeTarget = 0;
                    keepArmHingePos = false;
                }*/



                //HINGE MOVEMENT
                /*if(keepArmHingePos)
                {
                    armHingeMotor.setPower(hingePID.performPID(armHingeMotor.getCurrentPosition()));
                    //armHingeMotor.setPower(1.0f * getArmHingePower(armHingeTarget, armHingeMotor.getCurrentPosition()));
                }
                else if(!keepArmHingePos)
                {
                    armHingeMotor.setPower(armHingePower);
                    //armHingeMotor.setPower(armHingePower + hingePID.performPID());
                }
                else
                {

                    armHingeMotor.setPower(0);
                }*/


                double armExtensionPower = 0;
                if(this.gamepad2.dpad_up)
                {
                    armExtensionPower = 1;
                }
                else if(this.gamepad2.dpad_down)
                {
                    armExtensionPower = -1;
                }
                armExtension.setPower(armExtensionPower);

                //double hinge2Pos = 0;

                if(this.gamepad2.right_bumper){
                    if(servoHinge.getPosition() <= 0.9)
                    {
                        servoHinge.setPosition(servoHinge.getPosition()+0.1);
                    }
                }

                else if(this.gamepad2.left_bumper)
                {
                    if(servoHinge.getPosition() >= 0.1)
                    {
                        servoHinge.setPosition(servoHinge.getPosition()-0.1);
                    }

                }

            }

            telemetry.addData("LeftFront Power", leftFront.getPower());
            telemetry.addData("LeftBack Power", leftBack.getPower());
            telemetry.addData("RightFront Power", rightFront.getPower());
            telemetry.addData("RightBack Power", rightBack.getPower());

            telemetry.addLine("/n");

            telemetry.addData("ArmHinge Power", armHinge.getPower());
            telemetry.addData("ArmHinge Angle", getArmAngle180());
            telemetry.addData("ArmHinge Target", hingeTargetAngle);
            telemetry.addData("Arm Active", armHingeActive);

            telemetry.addLine("/n");

            telemetry.addData("Refreshing hinge", refreshingHinge);
            telemetry.addData("Refrshing extension", refreshingExtension);

            telemetry.addLine("/n");

            telemetry.addData("Slow mode", slowMove);
            telemetry.addData("Closing Grabbers", closeGrabbers);
            telemetry.addData("Intake on", moveIntake);
            telemetry.addData("Grabbers closed", closeGrabbers);
            telemetry.addData("Target Stone", targetStoneNum);

            telemetry.addData("Status", "Running");
            telemetry.update();




            idle();
        }
    }


    private double getArmAngle180()
    {
        int pos = armHinge.getCurrentPosition();
        int difFromOrigin = pos - ARM_H_COUNT_LOWER_LIMIT;

        double angle = (difFromOrigin/ARM_H_180_COUNTS)*180;
        return angle;
    }

    private int angleToCount(double angle)
    {
        int diffFromOrigin = (int) (angle/180)*ARM_H_180_COUNTS;
        int count = diffFromOrigin + ARM_H_COUNT_LOWER_LIMIT;

        return count;

    }


    private static final float ARM_H_MAXPOWER = 0.5f; //ARM_H values are used for movement of the arm hinge motor
    private static final float ARM_H_THRESHOLD = 300.0f;
    private static final int ARM_H_STEP = 1;
    private static final int ARM_H_OFFSET_BOUND = 800;
    private int armHingeOffset = 0;

    private float getArmHingePower(int target, int current) {
        int diff = target - current;

        if (diff > 0) {
            armHingeOffset += ARM_H_STEP;
        }
        if (diff < 0) {
            armHingeOffset -= ARM_H_STEP;
        }
        if (armHingeOffset > ARM_H_OFFSET_BOUND) {
            armHingeOffset = ARM_H_OFFSET_BOUND;
        } else if (armHingeOffset < - ARM_H_OFFSET_BOUND) {
            armHingeOffset = - ARM_H_OFFSET_BOUND;
        }

        diff = target + armHingeOffset - current;

        float val = ARM_H_MAXPOWER / ARM_H_THRESHOLD * diff;
        if (Math.abs(diff) < ARM_H_THRESHOLD) {
            return val;
        } else {
            if (val > 0) {
                return ARM_H_MAXPOWER;
            } else {
                return - ARM_H_MAXPOWER;
            }
        }
    }

}
