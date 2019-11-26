package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class BasicHDrive extends LinearOpMode {


    private DcMotor leftDrive;
    //private DcMotor leftBack;
    private DcMotor rightDrive;
    private DcMotor centreDrive;
    //private DcMotor rightBack;

    @Override
    public void runOpMode(){



        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        centreDrive = hardwareMap.get(DcMotor.class, "centreDrive");


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        /* Wait for the game to start (driver presses PLAY) waitForStart(); */

        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        //rightFront.setDirection(DcMotor.Direction.REVERSE);
        //rightBack.setDirection(DcMotor.Direction.REVERSE);



        /*
        Robot wheel mapping:
            X FRONT X
          X           X
        X               X

          LD   XCX   RD
                D
        X               X
          X           X
            X       X
        */

        final double toggleDelay = 0.25;

        /* run until the end of the match (driver presses STOP) */
        double forwards = -1.0;
        double backwards = 1.0;
        double leftwards = -1.0;
        double rightwards = 1.0;


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

            double lateral = -this.gamepad1.right_stick_x;
            double vertical = -this.gamepad1.right_stick_y;
            double turn = -this.gamepad1.left_stick_x;


            double leftPwr = vertical-turn;
            double rightPwr = vertical+turn;
            double centrePwr = lateral;
            //double leftFrontPwr = vertical-lateral-turn;
            //double leftBackPwr =  vertical+lateral-turn;
            //double rightFrontPwr = vertical+lateral+turn;
            //double rightBackPwr = vertical-lateral+turn;


            if(!slowMove) {
                leftDrive.setPower(Range.clip(leftPwr, -1, 1));
                rightDrive.setPower(Range.clip(rightPwr, -1, 1));
                centreDrive.setPower(Range.clip(centrePwr, -1, 1));
                /*leftFront.setPower(Range.clip(leftFrontPwr, -1, 1)); //clip here is just a safety measure
                leftBack.setPower(Range.clip(leftBackPwr, -1, 1));
                rightFront.setPower(Range.clip(rightFrontPwr, -1, 1));
                rightBack.setPower(Range.clip(leftBackPwr, -1, 1));*/
            }
            else {
                leftDrive.setPower(Range.clip(leftPwr/4, -1, 1));
                rightDrive.setPower(Range.clip(rightPwr/4, -1, 1));
                centreDrive.setPower(Range.clip(centrePwr/4, -1, 1));
                /*leftFront.setPower(Range.clip(leftFrontPwr, -1, 1)/4); //clip here is just a safety measure
                leftBack.setPower(Range.clip(leftBackPwr, -1, 1)/4);
                rightFront.setPower(Range.clip(rightFrontPwr, -1, 1)/4);
                rightBack.setPower(Range.clip(rightBackPwr, -1, 1)/4);*/
            }


            wheelMode = "D-pad Movement";

            boolean leftStrafe = this.gamepad1.dpad_left;
            boolean rightStrafe = this.gamepad1.dpad_right;
            boolean backStrafe = this.gamepad1.dpad_up;
            boolean frontStrafe = this.gamepad1.dpad_down;


            if(!slowMove) {
                if (leftStrafe) {  // Left strafe
                    leftDrive.setPower(0);
                    rightDrive.setPower(0);
                    centreDrive.setPower(leftwards);
                    /*leftFront.setPower(forwards);
                    leftBack.setPower(backwards);
                    rightFront.setPower(backwards);
                    rightBack.setPower(forwards);*/
                }
                else if (rightStrafe) {// Right strafe
                    leftDrive.setPower(0);
                    rightDrive.setPower(0);
                    centreDrive.setPower(rightwards);
                    /*leftFront.setPower(backwards);
                    leftBack.setPower(forwards);
                    rightFront.setPower(forwards);
                    rightBack.setPower(backwards);*/
                }
                else if (frontStrafe) {
                    leftDrive.setPower(forwards);
                    rightDrive.setPower(forwards);
                    centreDrive.setPower(0);
                    /*leftFront.setPower(forwards);
                    leftBack.setPower(forwards);
                    rightFront.setPower(forwards);
                    rightBack.setPower(forwards);*/
                }
                else if (backStrafe) {
                    leftDrive.setPower(backwards);
                    rightDrive.setPower(backwards);
                    centreDrive.setPower(0);
                    /*leftFront.setPower(backwards);
                    leftBack.setPower(backwards);
                    rightFront.setPower(backwards);
                    rightBack.setPower(backwards);*/
                }
            }
            else if(slowMove)
            {
                if (leftStrafe) {  // Left strafe
                    leftDrive.setPower(0);
                    rightDrive.setPower(0);
                    centreDrive.setPower(leftwards/2);
                    /*leftFront.setPower(forwards/2);
                    leftBack.setPower(backwards/2);
                    rightFront.setPower(backwards/2);
                    rightBack.setPower(forwards/2);*/
                }
                else if (rightStrafe) {  // Right strafe
                    leftDrive.setPower(0);
                    rightDrive.setPower(0);
                    centreDrive.setPower(rightwards/2);
                    /*leftFront.setPower(backwards/2);
                    leftBack.setPower(forwards/2);
                    rightFront.setPower(forwards/2);
                    rightBack.setPower(backwards/2);*/
                }
                else if (frontStrafe) {
                    leftDrive.setPower(forwards/2);
                    rightDrive.setPower(forwards/2);
                    centreDrive.setPower(0);
                    /*leftFront.setPower(forwards/2);
                    leftBack.setPower(forwards/2);
                    rightFront.setPower(forwards/2);
                    rightBack.setPower(forwards/2);*/
                }
                else if (backStrafe) {
                    leftDrive.setPower(backwards/2);
                    rightDrive.setPower(backwards/2);
                    centreDrive.setPower(0);
                    /*leftFront.setPower(backwards/2);
                    leftBack.setPower(backwards/2);
                    rightFront.setPower(backwards/2);
                    rightBack.setPower(backwards/2);*/
                }
            }


            telemetry.addData("LeftFront Power", leftDrive.getPower());
            //telemetry.addData("LeftBack Power", leftBack.getPower());
            telemetry.addData("RightFront Power", rightDrive.getPower());
            telemetry.addData("RightBack Power", centreDrive.getPower());
            //telemetry.addData("Wheel Mode", wheelMode);
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
