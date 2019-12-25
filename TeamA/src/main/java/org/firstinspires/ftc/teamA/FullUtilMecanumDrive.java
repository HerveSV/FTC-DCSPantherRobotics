package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import helperClassesA.SkystoneAutonMode;

@TeleOp
public class FullUtilMecanumDrive extends LinearOpMode {
    static final double COUNTS_PER_MOTOR_REV = 1478.4;

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    private DcMotor leftTraction;
    private DcMotor rightTraction;

    private DcMotor armHingeMotor;
    private DcMotor armExtensionMotor;
    private static final float ARM_H_MAXPOWER = 1.0f; //ARM_H values are used for movement of the arm hinge motor
    private static final float ARM_H_THRESHOLD = 100.0f;
    private static final int ARM_H_STEP = 1;
    private static final int ARM_H_OFFSET_BOUND = 500;
    private static final int ARM_H_ACTIVATION_TARGET = 800;
    private static final int ARM_H_COUNT_BUFFER = 50;
    private static final int ARM_H_COUNT_UPPER_LIMIT = 1500;
    private static final int ARM_H_COUNT_LOWER_LIMIT = 0;

    private static final int MAX_STONE_REACH = 10;
    private int armHingeOffset = 0;
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
    @Override
    public void runOpMode()
    {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        leftTraction = hardwareMap.get(DcMotor.class, "leftTraction");
        rightTraction = hardwareMap.get(DcMotor.class, "rightTraction");

        armHingeMotor = hardwareMap.get(DcMotor.class, "armHingeMotor");
        armExtensionMotor = hardwareMap.get(DcMotor.class, "armExtensionMotor");

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftTraction.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightTraction.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armHingeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armHingeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armExtensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtensionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        /* Wait for the game to start (driver presses PLAY) waitForStart(); */
        //leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        //armHingeMotor.setDirection(DcMotorSimple.Direction.REVERSE); //positive power needs to equal the armHinge moving up.



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
            Dpad Up: Increments stone target
            Dpad Down: Decrements stone target
            Dpad Left: Retracts arm extension
            Dpad Right: Increases arm extension
            A: Toggles stone intake system
            X: Toggles arm into active position
            Y: Refreshes arm position to fit the stone target


         */

        final double toggleDelay = 0.25;

        /* run until the end of the match (driver presses STOP) */
        double forwards = 1.0;
        double backwards = -1.0;


        String wheelMode = "";

        boolean slowMove = false;
        double b1ToggleTime = 0;
        double speedMod = 1;


        boolean moveTraction = false;
        double a2ToggleTime = 0;



        boolean keepArmHingePos = false;
        int armHingeTarget = 0;
        boolean armHingeActivating = false;
        boolean armHingeActive = false;
        double x2ToggleTime = 0;


        int armExtensionTarget = 0;

        int targetStoneNum = 0;
        double upDown2ToggleTime = 0;
        boolean refreshingArmHeight = false;


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


            wheelMode = "D-pad Movement";

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
             * Stone intake system
             */

            boolean A_button2 = this.gamepad1.right_bumper;
            if(A_button2 && moveTraction && (this.getRuntime()-a2ToggleTime)>= toggleDelay) {
                moveTraction = false;
                a2ToggleTime = this.getRuntime();
            }
            else if(A_button2 && (this.getRuntime()-a2ToggleTime)>= toggleDelay) {
                moveTraction = true;
                a2ToggleTime = this.getRuntime();
            }

            if(moveTraction)
            {
                leftTraction.setPower(-1);
                rightTraction.setPower(1);
            }
            else
            {
                leftTraction.setPower(0);
                rightTraction.setPower(0);
            }


            /**
             * ARM
             */
            boolean X_button2 = this.gamepad1.x;
            if(X_button2 && armHingeActive && (this.getRuntime()-x2ToggleTime)>= toggleDelay) {
                armHingeActive = false;
                x2ToggleTime = this.getRuntime();
            }
            else if(X_button2 && (this.getRuntime()-x2ToggleTime)>= toggleDelay && !armHingeActivating) {
                armHingeActivating = true;
                x2ToggleTime = this.getRuntime();
            }


            if(armHingeActivating)
            {
                if(armHingeMotor.getCurrentPosition() < ARM_H_ACTIVATION_TARGET)
                {
                    armHingeMotor.setPower(0.5);
                }
                else
                {
                    armHingeMotor.setPower(0);
                    armHingeActivating = false;
                    armHingeActive = true;
                }
            }

            //angle and extension of arm can be changed to presets
            //that allow for a certain stone height to be achieved
            if((this.gamepad2.dpad_up || this.gamepad2.dpad_down) && (this.getRuntime()-upDown2ToggleTime)>= toggleDelay) {
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

            if(this.gamepad2.y && !refreshingArmHeight && !armHingeActivating)
            {
                refreshingArmHeight = true;
            }



            if(refreshingArmHeight)
            {
                int partsDone = 0;

                if(armHingeMotor.getCurrentPosition() < ARM_STONE_STAGES[targetStoneNum-1][0] - ARM_H_COUNT_BUFFER)
                {
                    armHingeMotor.setPower(0.5);
                }
                else if(armHingeMotor.getCurrentPosition() > ARM_STONE_STAGES[targetStoneNum-1][0] + ARM_H_COUNT_BUFFER)
                {
                    armHingeMotor.setPower(0.5);
                }
                else
                {
                    ++partsDone;
                    armHingeMotor.setPower(0);
                }

                if(armExtensionMotor.getCurrentPosition() < ARM_STONE_STAGES[targetStoneNum-1][1] - ARM_X_COUNT_BUFFER)
                {
                    armExtensionMotor.setPower(0.5);
                }
                else if(armExtensionMotor.getCurrentPosition() > ARM_STONE_STAGES[targetStoneNum-1][1] + ARM_X_COUNT_BUFFER)
                {
                    armExtensionMotor.setPower(0.5);
                }
                else
                {
                    ++partsDone;
                    armExtensionMotor.setPower(0);
                }

                if(partsDone == 2)
                {
                    refreshingArmHeight = false;
                }
            }
            else if(armHingeActive)
            {
                double armHingePower = this.gamepad2.right_trigger - this.gamepad2.left_trigger;

                if(armHingeMotor.getCurrentPosition() < ARM_H_ACTIVATION_TARGET && armHingePower < 0)
                {
                    armHingePower = 0;
                }
                else if(armHingeMotor.getCurrentPosition() > ARM_H_COUNT_UPPER_LIMIT - ARM_H_COUNT_BUFFER)
                {
                    armHingePower = 0;
                }


                if(!keepArmHingePos && armHingePower == 0)
                {
                    keepArmHingePos = true;
                    armHingeTarget = armHingeMotor.getCurrentPosition();
                }
                else if(keepArmHingePos && armHingePower != 0)
                {
                    keepArmHingePos = false;
                }


                if(keepArmHingePos)
                {
                    armHingeMotor.setPower(1.0f * getArmHingePower(armHingeTarget, armHingeMotor.getCurrentPosition()));
                }
                else
                {
                    armHingeMotor.setPower(armHingePower);
                }


                if(this.gamepad2.dpad_right)
                {
                    armExtensionTarget += ARM_X_STEP;
                }
                else if(this.gamepad2.dpad_left && armExtensionTarget >= ARM_X_STEP)
                {
                    armExtensionTarget -= ARM_X_STEP;
                }
                if(armExtensionMotor.getCurrentPosition() < armExtensionTarget - ARM_H_COUNT_BUFFER)
                {
                    armExtensionMotor.setPower(0.5);
                }
                else if(armExtensionMotor.getCurrentPosition() > armExtensionTarget + ARM_H_COUNT_BUFFER)
                {
                    armExtensionMotor.setPower(-0.5);
                }
            }
            else if(!armHingeActive)
            {
                if(armHingeMotor.getCurrentPosition() > ARM_H_COUNT_LOWER_LIMIT + ARM_H_COUNT_BUFFER)
                {
                    armHingeMotor.setPower(-0.5);
                }
                else
                {
                    armHingeMotor.setPower(0);
                }
            }



            //EXTENSION LIMITER












            telemetry.addData("LeftFront Power", leftFront.getPower());
            telemetry.addData("LeftBack Power", leftBack.getPower());
            telemetry.addData("RightFront Power", rightFront.getPower());
            telemetry.addData("RightBack Power", rightBack.getPower());
            telemetry.addData("Wheel Mode", wheelMode);
            telemetry.addData("Slow mode", slowMove);
            telemetry.addData("Intake on", moveTraction);
            telemetry.addData("Target Stone", targetStoneNum);

            telemetry.addData("Status", "Running");
            telemetry.update();




            idle();
        }
    }

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
