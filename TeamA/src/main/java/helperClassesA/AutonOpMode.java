package helperClassesA;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Disabled
public class AutonOpMode extends LinearOpMode {

    protected DcMotor leftFront;
    protected DcMotor leftBack;
    protected DcMotor rightFront;
    protected DcMotor rightBack;
    protected DcMotor hMotor;
    protected BNO055IMU revImu;

    protected DriveTrain driveTrain = DriveTrain.MECANUM;

    private Orientation lastAngles = new Orientation();
    private double globalAngle;

    static final double COUNTS_PER_MOTOR_REV = 1478.4;    // Number of ticks for every full revolution/rotation of the motor shaft
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // Depends on gearing ratio between motor and wheel
    static final double WHEEL_DIAMETER_MM = 78.0;     // For figuring circumference
    static final double COUNTS_PER_MM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_MM * 3.1415);  //This is the amount of ticks we have every cm travelled by the wheel

    public enum DriveTrain {
        MECANUM,
        INVERSE_MECANUM,
        HDRIVE,
        PUSHBOT,
    }

    public enum State {
        INITIALISE,
        FORWARDS,
        BACKWARDS,
        LEFTSTRAFE,
        RIGHTSTRAFE,
        STRAFE,
        TURNLEFT,
        TURNRIGHT,
        TURN,
        ARMUP,
        ARMDOWN,
        FLIPSERVO,
        BOTDOWN,
        CHECK,
        STOP
    };

    @Override
    public void runOpMode() throws InterruptedException
    {

    }


    public void runState(DriveStateMachine.State currState, double distanceMm, double speed) {
        //this function is called
        if(distanceMm == 0)
        {
            return;
        }
        switch (driveTrain) {

            case MECANUM:
                switch (currState) {
                    case FORWARDS:
                        encoderDrive(speed, distanceMm, distanceMm, distanceMm, distanceMm);
                        break;
                    case BACKWARDS:
                        encoderDrive(speed, -distanceMm, -distanceMm, -distanceMm, -distanceMm);
                        break;
                    case LEFTSTRAFE:
                        encoderDrive(speed, distanceMm, -distanceMm, -distanceMm, distanceMm);
                        break;
                    case RIGHTSTRAFE:
                        encoderDrive(speed, -distanceMm, distanceMm, distanceMm, -distanceMm);
                        break;
                    case TURNLEFT:
                        //encoderDrive(speed, -distanceMm, -distanceMm, distanceMm, distanceMm);
                        encoderRotate(speed, -distanceMm);
                        break;
                    case TURNRIGHT:
                        //encoderDrive(speed, distanceMm, distanceMm, -distanceMm, -distanceMm);
                        encoderRotate(speed, distanceMm);
                        break;
                    case TURN:
                        encoderRotate(speed, distanceMm);
                        break;
                    case STRAFE:
                        if(distanceMm < 0)
                        {
                            runState(DriveStateMachine.State.LEFTSTRAFE, distanceMm, speed);
                        }
                        else if(distanceMm > 0)
                        {
                            runState(DriveStateMachine.State.RIGHTSTRAFE, distanceMm, speed);
                        }
                        break;
                    default:
                        break;
                }
            case INVERSE_MECANUM:
                switch (currState) {
                    case LEFTSTRAFE:
                        encoderDrive(speed, distanceMm, distanceMm, distanceMm, distanceMm);
                        break;
                    case RIGHTSTRAFE:
                        encoderDrive(speed, -distanceMm, -distanceMm, -distanceMm, -distanceMm);
                        break;
                    case FORWARDS:
                        encoderDrive(speed, distanceMm, -distanceMm, -distanceMm, distanceMm);
                        break;
                    case BACKWARDS:
                        encoderDrive(speed, -distanceMm, distanceMm, distanceMm, -distanceMm);
                        break;
                    case TURNLEFT:
                        //encoderDrive(speed, -distanceMm, -distanceMm, distanceMm, distanceMm);
                        encoderRotate(speed, -distanceMm);
                        break;
                    case TURNRIGHT:
                        //encoderDrive(speed, distanceMm, distanceMm, -distanceMm, -distanceMm);
                        encoderRotate(speed, distanceMm);
                        break;
                    case TURN:
                        encoderRotate(speed, distanceMm);
                        break;
                    case STRAFE:
                        if(distanceMm < 0)
                        {
                            runState(DriveStateMachine.State.LEFTSTRAFE, distanceMm, speed);
                        }
                        else if(distanceMm > 0)
                        {
                            runState(DriveStateMachine.State.RIGHTSTRAFE, distanceMm, speed);
                        }
                        break;

                    default:
                        break;
                }
            case HDRIVE:
                switch (currState) {
                    case FORWARDS:
                        encoderDrive(speed, distanceMm, distanceMm);
                        break;
                    case BACKWARDS:
                        encoderDrive(speed, -distanceMm, -distanceMm);
                        break;
                    case LEFTSTRAFE:
                        encoderDrive(speed, distanceMm, hMotor);
                        break;
                    case RIGHTSTRAFE:
                        encoderDrive(speed, -distanceMm, hMotor);
                        break;
                    case TURNLEFT:
                        //encoderDrive(speed, -distanceMm, -distanceMm, distanceMm, distanceMm);
                        encoderRotate(speed, -distanceMm);
                        break;
                    case TURNRIGHT:
                        //encoderDrive(speed, distanceMm, distanceMm, -distanceMm, -distanceMm);
                        encoderRotate(speed, distanceMm);
                        break;
                    case TURN:
                        encoderRotate(speed, distanceMm);
                        break;
                    case STRAFE:
                        if(distanceMm < 0)
                        {
                            runState(DriveStateMachine.State.LEFTSTRAFE, distanceMm, speed);
                        }
                        else if(distanceMm > 0)
                        {
                            runState(DriveStateMachine.State.RIGHTSTRAFE, distanceMm, speed);
                        }
                        break;
                    default:
                        break;
                }

            case PUSHBOT:
                switch (currState) {
                    case FORWARDS:
                        encoderDrive(speed, distanceMm, distanceMm);
                        break;
                    case BACKWARDS:
                        encoderDrive(speed, -distanceMm, -distanceMm);
                        break;
                    case TURNLEFT:
                        //encoderDrive(speed, -distanceMm, -distanceMm, distanceMm, distanceMm);
                        encoderRotate(speed, -distanceMm);
                        break;
                    case TURNRIGHT:
                        //encoderDrive(speed, distanceMm, distanceMm, -distanceMm, -distanceMm);
                        encoderRotate(speed, distanceMm);
                        break;
                    case TURN:
                        encoderRotate(speed, distanceMm);
                        break;

                    default:
                        break;
                }
        }
    }


    private void encoderDrive(double speed,
                              double leftFrontMm, double leftBackMm, double rightFrontMm, double rightBackMm) { //for mecanum drive
        int newLeftFrontTarget;
        int newLeftBackTarget;
        int newRightFrontTarget;
        int newRightBackTarget;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = leftFront.getCurrentPosition() + (int) (leftFrontMm * COUNTS_PER_MM);
            newLeftBackTarget = leftBack.getCurrentPosition() + (int) (leftBackMm * COUNTS_PER_MM);
            newRightFrontTarget = rightFront.getCurrentPosition() + (int) (rightFrontMm * COUNTS_PER_MM);
            newRightBackTarget = rightBack.getCurrentPosition() + (int) (rightBackMm * COUNTS_PER_MM);

            leftFront.setTargetPosition(newLeftFrontTarget);
            leftBack.setTargetPosition(newLeftBackTarget);
            rightFront.setTargetPosition(newRightFrontTarget);
            rightBack.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion
            leftFront.setPower(Math.abs(speed));
            leftBack.setPower(Math.abs(speed));
            rightFront.setPower(Math.abs(speed));
            rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and both motors are still running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            while (opModeIsActive() &&
                    (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to :", newLeftFrontTarget, newLeftBackTarget, newRightFrontTarget, newRightBackTarget);
                telemetry.addData("Path2", "Running at :",
                        leftFront.getCurrentPosition(),
                        leftBack.getCurrentPosition(),
                        rightFront.getCurrentPosition(),
                        leftBack.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;

            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //opMode.sleep(250);   // optional pause after each move
        }
    }

    private void encoderDrive(double speed, double leftDriveMm, double rightDriveMm) { //for dual motor drive
        int newLeftDriveTarget;
        int newRightDriveTarget;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftDriveTarget = leftFront.getCurrentPosition() + (int) (leftDriveMm * COUNTS_PER_MM);
            newRightDriveTarget = rightFront.getCurrentPosition() + (int) (rightDriveMm * COUNTS_PER_MM);

            leftFront.setTargetPosition(newLeftDriveTarget);
            rightFront.setTargetPosition(newRightDriveTarget);

            // Turn On RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion
            leftFront.setPower(Math.abs(speed));
            rightFront.setPower(Math.abs(speed));

            // keep looping while we are still active, and both motors are still running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            while (opModeIsActive() &&
                    (leftFront.isBusy() && rightFront.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to :", newLeftDriveTarget, newRightDriveTarget);
                telemetry.addData("Path2", "Running at :",
                        leftFront.getCurrentPosition(),
                        rightFront.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;

            leftFront.setPower(0);
            rightFront.setPower(0);

            // Turn off RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //opMode.sleep(250);   // optional pause after each move
        }
    }

    private void encoderDrive(double speed, double driveMm, DcMotor motor) { //for single motor drive (e.g, the H drive)
        int newDriveTarget;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newDriveTarget = motor.getCurrentPosition() + (int) (driveMm * COUNTS_PER_MM);

            motor.setTargetPosition(newDriveTarget);

            // Turn On RUN_TO_POSITION
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion
            motor.setPower(Math.abs(speed));

            // keep looping while we are still active, and both motors are still running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            while (opModeIsActive() &&
                    (leftFront.isBusy() && rightFront.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to :", newDriveTarget);
                telemetry.addData("Path2", "Running at :",
                        motor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;

            motor.setPower(0);

            // Turn off RUN_TO_POSITION
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //opMode.sleep(250);   // optional pause after each move
        }
    }


    private void encoderRotate(double speed, double degrees)
    {
        double  leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        degrees = -degrees;
        /**
         * The original system has + set as left, and - as right
         *
         * Now, it is + for right and - for left
         */
        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0)
        {   // turn right.
            leftPower = speed;
            rightPower = -speed;
        }
        else if (degrees > 0)
        {   // turn left.
            leftPower = -speed;
            rightPower = speed;
        }

        else return;

        // set power to rotate.
        if(driveTrain == DriveTrain.MECANUM || driveTrain == DriveTrain.INVERSE_MECANUM)
        {
            leftFront.setPower(leftPower);
            leftBack.setPower(leftPower);

            rightFront.setPower(rightPower);
            rightBack.setPower(rightPower);
        }
        else if(driveTrain == DriveTrain.HDRIVE || driveTrain == DriveTrain.PUSHBOT)
        {
            leftFront.setPower(leftPower);
            rightFront.setPower(rightPower);
        }


        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {}

            while (opModeIsActive() && getAngle() > degrees) {}
        }
        else    // left turn.
        {
            while (opModeIsActive() && getAngle() < degrees) {}
        }

        // turn the motors off.

        if(driveTrain == DriveTrain.MECANUM || driveTrain == DriveTrain.INVERSE_MECANUM)
        {
            leftFront.setPower(0);
            leftBack.setPower(0);

            rightFront.setPower(0);
            rightBack.setPower(0);

        }
        else if(driveTrain == DriveTrain.HDRIVE || driveTrain == DriveTrain.PUSHBOT)
        {
            leftFront.setPower(0);
            rightFront.setPower(0);
        }



        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle()
    {
        lastAngles = revImu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAngle()
    {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = revImu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }


    protected void initRevImu()
    {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        revImu = hardwareMap.get(BNO055IMU.class, "imu");

        revImu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !revImu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", revImu.getCalibrationStatus().toString());
        telemetry.update();
    }

}
