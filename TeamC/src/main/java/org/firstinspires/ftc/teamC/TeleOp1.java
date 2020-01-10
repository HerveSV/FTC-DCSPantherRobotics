package org.firstinspires.ftc.teamC;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp

public class TeleOp1 extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    private DcMotor armHinge;
    private Servo armBase;
    private Servo armJoint;

    @Override
    public void runOpMode(){

        leftFront = hardwareMap.get(DcMotor.class, "lf");
        leftBack = hardwareMap.get(DcMotor.class, "lb");
        rightFront = hardwareMap.get(DcMotor.class, "rf");
        rightBack = hardwareMap.get(DcMotor.class, "rb");

        armHinge = hardwareMap.get(DcMotor.class, "hinge");
        //armBase = hardwareMap.get(DcMotor.class, "hinge");
        //armHinge = hardwareMap.get(DcMotor.class, "hinge");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        /* Wait for the game to start (driver presses PLAY) waitForStart(); */

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        //leftFront.setDirection(DcMotor.Direction.REVERSE);
        //leftBack.setDirection(DcMotor.Direction.REVERSE);



        /*
        Robot wheel mapping:
            X FRONT X
          X           X
        X  LF       RF  X
                X
               XXX
                X
        X  LB       RB  X
          X           X
            X       X
        */

        final double toggleDelay = 0.25;

        /* run until the end of the match (driver presses STOP) */
        double forwards = -1.0;
        double backwards = 1.0;

        boolean slowMove = false;
        double b1ToggleTime = 0;
        double speedMod = 1;

        boolean keepArmHingePos = false;
        int hingeTarget = 0;

        waitForStart();
        while (opModeIsActive()) {



            /* gamepad1 control
            everything here has to do with movement
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

            /**double lateral = -this.gamepad1.right_stick_x;
             double vertical = -this.gamepad1.right_stick_y;
             double turn = -this.gamepad1.left_stick_x;



             double leftFrontPwr = lateral - vertical - turn;
             double leftBackPwr =  lateral + vertical - turn;
             double rightFrontPwr = lateral + vertical + turn;
             double rightBackPwr = lateral - vertical + turn;**/

            double y = -this.gamepad1.right_stick_x;
            double x = -this.gamepad1.right_stick_y;
            double turn = -this.gamepad1.left_stick_x;


            double leftFrontPwr = y-x+turn;//y+x+turn;//
            double leftBackPwr =  y+x+turn;//y-x+turn;//
            double rightFrontPwr = y+x-turn;//y+x-turn;//
            double rightBackPwr = y-x-turn;//y+x-turn;//


            leftFront.setPower(Range.clip(leftFrontPwr * speedMod, -1, 1)); //clip here is just a safety measure
            leftBack.setPower(Range.clip(leftBackPwr * speedMod, -1, 1));
            rightFront.setPower(Range.clip(rightFrontPwr * speedMod, -1, 1));
            rightBack.setPower(Range.clip(rightBackPwr * speedMod, -1, 1));


            /*boolean leftStrafe = this.gamepad1.dpad_down;
            boolean rightStrafe = this.gamepad1.dpad_up;
            boolean backStrafe = this.gamepad1.dpad_right;
            boolean frontStrafe = this.gamepad1.dpad_left;*/

            boolean backStrafe = this.gamepad1.dpad_down;
            boolean frontStrafe = this.gamepad1.dpad_up;
            boolean rightStrafe = this.gamepad1.dpad_right;
            boolean leftStrafe = this.gamepad1.dpad_left;


            if (leftStrafe) {  // Left strafe


                leftFront.setPower(backwards * speedMod);
                leftBack.setPower(forwards * speedMod);
                rightFront.setPower(forwards * speedMod);
                rightBack.setPower(backwards * speedMod);
            }
            else if (rightStrafe) {// Right strafe
                leftFront.setPower(forwards * speedMod);
                leftBack.setPower(backwards * speedMod);
                rightFront.setPower(backwards * speedMod);
                rightBack.setPower(forwards * speedMod);
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

            double hingePower = this.gamepad1.right_trigger - this.gamepad1.left_trigger/2;

            if(!keepArmHingePos && hingePower == 0)
            {
                keepArmHingePos = true;
                hingeTarget = armHinge.getCurrentPosition();
            }
            else if(keepArmHingePos && hingePower != 0)
            {
                keepArmHingePos = false;
            }

            //HINGE MOVEMENT
            if(keepArmHingePos)
            {
                armHinge.setPower(1.0f * getPower(hingeTarget, armHinge.getCurrentPosition()));
            }
            else
            {
                armHinge.setPower(hingePower);
            }

            if(this.gamepad1.a)
            {
                this.armHinge.setPower(0);
            }


            telemetry.addData("LeftFront Power", leftFront.getPower());
            telemetry.addData("LeftBack Power", leftBack.getPower());
            telemetry.addData("RightFront Power", rightFront.getPower());
            telemetry.addData("RightBack Power", rightBack.getPower());
            if(slowMove)
            {
                telemetry.addData("Slow Move", "ON");
            }

            telemetry.addData("Status", "Running");
            telemetry.update();




            idle();
        }
    }

    private static final float MAXPOWER = 1.0f;
    private static final float THRESHOLD = 100.0f;
    private static final int STEP = 1;
    private static final int OFFSET_BOUND = 500;
    private int offset = 0;

    private float getPower(int target, int current) {
        int diff = target - current;

        if (diff > 0) {
            offset += STEP;
        }
        if (diff < 0) {
            offset -= STEP;
        }
        if (offset > OFFSET_BOUND) {
            offset = OFFSET_BOUND;
        } else if (offset < - OFFSET_BOUND) {
            offset = - OFFSET_BOUND;
        }

        diff = target + offset - current;

        float val = (MAXPOWER / THRESHOLD) * diff;
        if (Math.abs(diff) < THRESHOLD) {
            return val;
        } else {
            if (val > 0) {
                return MAXPOWER;
            } else {
                return - MAXPOWER;
            }
        }
    }
}
