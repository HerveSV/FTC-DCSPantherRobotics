package org.firstinspires.ftc.teamC;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * mutated and weird mechanum drive, built the wrong way around to allow for more convenient motor placement
 */

@TeleOp
public class InverseMechanumDrive extends LinearOpMode {



    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    @Override
    public void runOpMode(){

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

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


        String wheelMode = "";

        boolean slowMove = false;
        double bToggleTime = 0;

        waitForStart();
        while (opModeIsActive()) {



            /* gamepad1 control
            everything here has to do with movement
             */


            boolean B_button1 = this.gamepad1.b;
            if(B_button1 && slowMove && (this.getRuntime()-bToggleTime)>= toggleDelay) {
                slowMove = false;
                bToggleTime = this.getRuntime();
            }
            else if(B_button1 && (this.getRuntime()-bToggleTime)>= toggleDelay) {
                slowMove = true;
                bToggleTime = this.getRuntime();
            }


            //standard concept for holonomic movement

            /**double lateral = -this.gamepad1.right_stick_x;
            double vertical = -this.gamepad1.right_stick_y;
            double turn = -this.gamepad1.left_stick_x;



            double leftFrontPwr = lateral - vertical - turn;
            double leftBackPwr =  lateral + vertical - turn;
            double rightFrontPwr = lateral + vertical + turn;
            double rightBackPwr = lateral - vertical + turn;**/

            double x = -this.gamepad1.right_stick_x;
            double y = -this.gamepad1.right_stick_y;
            double turn = -this.gamepad1.left_stick_x;


            double leftFrontPwr = x-y-turn;
            double leftBackPwr =  x+y-turn;
            double rightFrontPwr = x+y+turn;
            double rightBackPwr = x-y+turn;


            if(!slowMove) {
                leftFront.setPower(Range.clip(leftFrontPwr, -1, 1)); //clip here is just a safety measure
                leftBack.setPower(Range.clip(leftBackPwr, -1, 1));
                rightFront.setPower(Range.clip(rightFrontPwr, -1, 1));
                rightBack.setPower(Range.clip(leftBackPwr, -1, 1));
            }
            else {
                leftFront.setPower(Range.clip(leftFrontPwr, -1, 1)/4); //clip here is just a safety measure
                leftBack.setPower(Range.clip(leftBackPwr, -1, 1)/4);
                rightFront.setPower(Range.clip(rightFrontPwr, -1, 1)/4);
                rightBack.setPower(Range.clip(rightBackPwr, -1, 1)/4);
            }


            wheelMode = "D-pad Movement";

            /*boolean leftStrafe = this.gamepad1.dpad_down;
            boolean rightStrafe = this.gamepad1.dpad_up;
            boolean backStrafe = this.gamepad1.dpad_right;
            boolean frontStrafe = this.gamepad1.dpad_left;*/

            boolean backStrafe = this.gamepad1.dpad_down;
            boolean frontStrafe = this.gamepad1.dpad_up;
            boolean rightStrafe = this.gamepad1.dpad_right;
            boolean leftStrafe = this.gamepad1.dpad_left;


            if(!slowMove) {
                if (leftStrafe) {  // Left strafe
                    leftFront.setPower(forwards);
                    leftBack.setPower(backwards);
                    rightFront.setPower(backwards);
                    rightBack.setPower(forwards);
                }
                else if (rightStrafe) {// Right strafe
                    leftFront.setPower(backwards);
                    leftBack.setPower(forwards);
                    rightFront.setPower(forwards);
                    rightBack.setPower(backwards);
                }
                else if (frontStrafe) {
                    leftFront.setPower(forwards);
                    leftBack.setPower(forwards);
                    rightFront.setPower(forwards);
                    rightBack.setPower(forwards);
                }
                else if (backStrafe) {
                    leftFront.setPower(backwards);
                    leftBack.setPower(backwards);
                    rightFront.setPower(backwards);
                    rightBack.setPower(backwards);
                }
            }
            else if(slowMove)
            {
                if (leftStrafe) {  // Left strafe
                    leftFront.setPower(forwards/2);
                    leftBack.setPower(backwards/2);
                    rightFront.setPower(backwards/2);
                    rightBack.setPower(forwards/2);
                }
                else if (rightStrafe) {  // Right strafe
                    leftFront.setPower(backwards/2);
                    leftBack.setPower(forwards/2);
                    rightFront.setPower(forwards/2);
                    rightBack.setPower(backwards/2);
                }
                else if (frontStrafe) {
                    leftFront.setPower(forwards/2);
                    leftBack.setPower(forwards/2);
                    rightFront.setPower(forwards/2);
                    rightBack.setPower(forwards/2);
                }
                else if (backStrafe) {
                    leftFront.setPower(backwards/2);
                    leftBack.setPower(backwards/2);
                    rightFront.setPower(backwards/2);
                    rightBack.setPower(backwards/2);
                }
            }


            telemetry.addData("LeftFront Power", leftFront.getPower());
            telemetry.addData("LeftBack Power", leftBack.getPower());
            telemetry.addData("RightFront Power", rightFront.getPower());
            telemetry.addData("RightBack Power", rightBack.getPower());
            telemetry.addData("Wheel Mode", wheelMode);
            if(slowMove)
            {
                telemetry.addData("Slow Move", "ON");
            }

            telemetry.addData("Status", "Running");
            telemetry.update();




            idle();
        }
    }
}
