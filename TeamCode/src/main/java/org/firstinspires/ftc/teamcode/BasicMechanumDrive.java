package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class BasicMechanumDrive extends LinearOpMode {


    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    //private DcMotor leftTraction;
    //private DcMotor rightTraction;

    @Override
    public void runOpMode(){

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        /*leftTraction = hardwareMap.get(DcMotor.class, "leftTraction");
        rightTraction = hardwareMap.get(DcMotor.class, "rightTraction");*/

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        /* Wait for the game to start (driver presses PLAY) waitForStart(); */
        //leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        /*
        Robot wheel mapping:
                X
              X   X
            X FRONT X
          X           X
        X  LF       RF  X
      X        X          X
    X           XXX         X            Mecanum need to be aligned to create a diamond-square shape
      X          X        X
        X  LB       RB  X
          X           X
            X       X
              X   X
                X

       */

        final double toggleDelay = 0.25;

        /* run until the end of the match (driver presses STOP) */
        double forwards = 1.0;
        double backwards = -1.0;


        String wheelMode = "";

        boolean slowMove = false;
        double bToggleTime = 0;
        double speedMod = 1;

       // boolean moveTraction = false;
       // double rbToggleTime = 0;

        waitForStart();
        while (opModeIsActive()) {



            /* gamepad1 control
            everything here has to do with movement
             */


            boolean B_button1 = this.gamepad1.b;
            if(B_button1 && slowMove && (this.getRuntime()-bToggleTime)>= toggleDelay) {
                slowMove = false;
                speedMod = 1;
                bToggleTime = this.getRuntime();
            }
            else if(B_button1 && (this.getRuntime()-bToggleTime)>= toggleDelay) {
                slowMove = true;
                speedMod = 0.25;
                bToggleTime = this.getRuntime();
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







            /*boolean right_bumper1 = this.gamepad1.right_bumper;
            if(right_bumper1 && slowMove && (this.getRuntime()-rbToggleTime)>= toggleDelay) {
                moveTraction = false;
                rbToggleTime = this.getRuntime();
            }
            else if(right_bumper1 && (this.getRuntime()-rbToggleTime)>= toggleDelay) {
                moveTraction = true;
                rbToggleTime = this.getRuntime();
            }

            if(moveTraction)
            {
                leftTraction.setPower(-1);
                rightTraction.setPower(1);
            }*/


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
