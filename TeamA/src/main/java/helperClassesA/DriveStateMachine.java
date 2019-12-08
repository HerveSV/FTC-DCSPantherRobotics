package helperClassesA;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class DriveStateMachine {

    protected final DcMotor left1; //These cannot be changed into other motors once assigned in the constructor
    protected final DcMotor left2;
    protected final DcMotor right1;
    protected final DcMotor right2;
    protected final DcMotor hMotor;
    protected final BNO055IMU IMU;


    private Orientation lastAngles = new Orientation();
    private double globalAngle;
    //private double power = .30;
    private double correction;

    public enum DriveTrain {
        DRIVE_TRAIN_MECANUM,
        DRIVE_TRAIN_INVERSE_MECANUM,
        DRIVE_TRAIN_HDRIVE,
        DRIVE_TRAIN_PUSHBOT,
    }

    protected final DriveTrain driveTrain;


    protected LinearOpMode opMode;

    static final double COUNTS_PER_MOTOR_REV = 1478.4;    // Number of ticks for every full revolution/rotation of the motor shaft
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // Depends on gearing ratio between motor and wheel
    static final double WHEEL_DIAMETER_MM = 4.0;     // For figuring circumference
    static final double COUNTS_PER_MM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_MM * 3.1415);  //This is the amount of ticks we have every cm travelled by the wheel
    //static final double DRIVE_SPEED = 0.6;
    //static final double TURN_SPEED = 0.5;


    protected DriveStateMachine()
    {
        left1 = null;
        right1 = null;
        left2 = null;
        right2 = null;
        hMotor = null;
        driveTrain = null;
        IMU = null;
    }

    public DriveStateMachine(DcMotor leftFront, DcMotor rightFront, DcMotor leftBack, DcMotor rightBack, BNO055IMU imu,LinearOpMode opMode, DriveTrain train) { //the constructor

        //the motors are assigned
        left1 = leftFront;
        left2 = leftBack;
        right1 = rightFront;
        right2 = rightBack;
        if (train == DriveTrain.DRIVE_TRAIN_HDRIVE) {
            train = DriveTrain.DRIVE_TRAIN_MECANUM;
        }
        driveTrain = train;
        hMotor = null;
        PIDFmanager.setPIDF((DcMotorEx) left1);
        PIDFmanager.setPIDF((DcMotorEx) left2);
        PIDFmanager.setPIDF((DcMotorEx) right1);
        PIDFmanager.setPIDF((DcMotorEx) right2);
        PIDFmanager.setPIDF((DcMotorEx) hMotor);

        IMU = imu;


        opMode = opMode;

    }

    public DriveStateMachine(DcMotor leftSide, DcMotor rightSide, DcMotor hDrive, BNO055IMU imu, LinearOpMode opMode, DriveTrain train) {
        if (train != DriveTrain.DRIVE_TRAIN_HDRIVE) {
            train = DriveTrain.DRIVE_TRAIN_HDRIVE;
        }
        left1 = leftSide;
        right1 = rightSide;
        hMotor = hDrive;


        driveTrain = train;

        left2 = null;
        right2 = null;

        PIDFmanager.setPIDF((DcMotorEx) left1);
        PIDFmanager.setPIDF((DcMotorEx) left2);
        PIDFmanager.setPIDF((DcMotorEx) right1);
        PIDFmanager.setPIDF((DcMotorEx) right2);
        PIDFmanager.setPIDF((DcMotorEx) hMotor);

        IMU = imu;

        opMode = opMode;

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
        ARMUP,
        ARMDOWN,
        FLIPSERVO,
        BOTDOWN,
        CHECK,
        STOP
    };

    public void runState(State currState, double distanceMm, double speed) {
        //this function is called
        if(distanceMm == 0)
        {
            return;
        }
        switch (driveTrain) {

            case DRIVE_TRAIN_MECANUM:
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
                        encoderDrive(speed, -distanceMm, -distanceMm, distanceMm, distanceMm);
                        break;
                    case TURNRIGHT:
                        encoderDrive(speed, distanceMm, distanceMm, -distanceMm, -distanceMm);
                        break;
                    case STRAFE:
                        if(distanceMm < 0)
                        {
                            runState(State.LEFTSTRAFE, distanceMm, speed);
                        }
                        else if(distanceMm > 0)
                        {
                            runState(State.RIGHTSTRAFE, distanceMm, speed);
                        }

                    default:
                        break;
                }
            case DRIVE_TRAIN_INVERSE_MECANUM:
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
                        encoderDrive(speed, -distanceMm, -distanceMm, distanceMm, distanceMm);
                        break;
                    case TURNRIGHT:
                        encoderDrive(speed, distanceMm, distanceMm, -distanceMm, -distanceMm);
                        break;
                    case STRAFE:
                        if(distanceMm < 0)
                        {
                            runState(State.LEFTSTRAFE, distanceMm, speed);
                        }
                        else if(distanceMm > 0)
                        {
                            runState(State.RIGHTSTRAFE, distanceMm, speed);
                        }

                    default:
                        break;
                }
            case DRIVE_TRAIN_HDRIVE:
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
                        encoderDrive(speed, -distanceMm, distanceMm);
                        break;
                    case TURNRIGHT:
                        encoderDrive(speed, distanceMm, -distanceMm);
                        break;
                    case STRAFE:
                        if(distanceMm < 0)
                        {
                            runState(State.LEFTSTRAFE, distanceMm, speed);
                        }
                        else if(distanceMm > 0)
                        {
                            runState(State.RIGHTSTRAFE, distanceMm, speed);
                        }

                    default:
                        break;
                }

            case DRIVE_TRAIN_PUSHBOT:
                switch (currState) {
                    case FORWARDS:
                        encoderDrive(speed, distanceMm, distanceMm);
                        break;
                    case BACKWARDS:
                        encoderDrive(speed, -distanceMm, -distanceMm);
                        break;
                    case TURNLEFT:
                        encoderDrive(speed, -distanceMm, distanceMm);
                        break;
                    case TURNRIGHT:
                        encoderDrive(speed, distanceMm, -distanceMm);
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
        if (opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = left1.getCurrentPosition() + (int) (leftFrontMm * COUNTS_PER_MM);
            newLeftBackTarget = left2.getCurrentPosition() + (int) (leftBackMm * COUNTS_PER_MM);
            newRightFrontTarget = right1.getCurrentPosition() + (int) (rightFrontMm * COUNTS_PER_MM);
            newRightBackTarget = right2.getCurrentPosition() + (int) (rightBackMm * COUNTS_PER_MM);

            left1.setTargetPosition(newLeftFrontTarget);
            left2.setTargetPosition(newLeftBackTarget);
            right1.setTargetPosition(newRightFrontTarget);
            right2.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            left1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion
            left1.setPower(Math.abs(speed));
            left2.setPower(Math.abs(speed));
            right1.setPower(Math.abs(speed));
            right2.setPower(Math.abs(speed));

            // keep looping while we are still active, and both motors are still running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            while (opMode.opModeIsActive() &&
                    (left1.isBusy() && left2.isBusy() && right1.isBusy() && right2.isBusy())) {

                // Display it for the driver.
                opMode.telemetry.addData("Path1", "Running to :", newLeftFrontTarget, newLeftBackTarget, newRightFrontTarget, newRightBackTarget);
                opMode.telemetry.addData("Path2", "Running at :",
                        left1.getCurrentPosition(),
                        left2.getCurrentPosition(),
                        right1.getCurrentPosition(),
                        left2.getCurrentPosition());
                opMode.telemetry.update();
            }

            // Stop all motion;

            left1.setPower(0);
            left2.setPower(0);
            right1.setPower(0);
            right2.setPower(0);

            // Turn off RUN_TO_POSITION
            left1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            left2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //opMode.sleep(250);   // optional pause after each move
        }
    }

    private void encoderDrive(double speed, double leftDriveMm, double rightDriveMm) { //for dual motor drive
        int newLeftDriveTarget;
        int newRightDriveTarget;


        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftDriveTarget = left1.getCurrentPosition() + (int) (leftDriveMm * COUNTS_PER_MM);
            newRightDriveTarget = right1.getCurrentPosition() + (int) (rightDriveMm * COUNTS_PER_MM);

            left1.setTargetPosition(newLeftDriveTarget);
            right1.setTargetPosition(newRightDriveTarget);

            // Turn On RUN_TO_POSITION
            left1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion
            left1.setPower(Math.abs(speed));
            right1.setPower(Math.abs(speed));

            // keep looping while we are still active, and both motors are still running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            while (opMode.opModeIsActive() &&
                    (left1.isBusy() && right1.isBusy())) {

                // Display it for the driver.
                opMode.telemetry.addData("Path1", "Running to :", newLeftDriveTarget, newRightDriveTarget);
                opMode.telemetry.addData("Path2", "Running at :",
                        left1.getCurrentPosition(),
                        right1.getCurrentPosition());
                opMode.telemetry.update();
            }

            // Stop all motion;

            left1.setPower(0);
            right1.setPower(0);

            // Turn off RUN_TO_POSITION
            left1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //opMode.sleep(250);   // optional pause after each move
        }
    }

    private void encoderDrive(double speed, double driveMm, DcMotor motor) { //for single motor drive (e.g, the H drive)
        int newDriveTarget;


        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

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
            while (opMode.opModeIsActive() &&
                    (left1.isBusy() && right1.isBusy())) {

                // Display it for the driver.
                opMode.telemetry.addData("Path1", "Running to :", newDriveTarget);
                opMode.telemetry.addData("Path2", "Running at :",
                        motor.getCurrentPosition());
                opMode.telemetry.update();
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
        if(driveTrain == DriveTrain.DRIVE_TRAIN_MECANUM || driveTrain == DriveTrain.DRIVE_TRAIN_INVERSE_MECANUM)
        {
            left1.setPower(leftPower);
            left2.setPower(leftPower);

            right1.setPower(rightPower);
            right2.setPower(rightPower);
        }
        else if(driveTrain == DriveTrain.DRIVE_TRAIN_HDRIVE || driveTrain == DriveTrain.DRIVE_TRAIN_PUSHBOT)
        {
            left1.setPower(leftPower);
            right1.setPower(rightPower);
        }


        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opMode.opModeIsActive() && getAngle() == 0) {}

            while (opMode.opModeIsActive() && getAngle() > degrees) {}
        }
        else    // left turn.
        {
            while (opMode.opModeIsActive() && getAngle() < degrees) {}
        }

        // turn the motors off.
        if(driveTrain == DriveTrain.DRIVE_TRAIN_HDRIVE || driveTrain == DriveTrain.DRIVE_TRAIN_PUSHBOT)
        {
            left1.setPower(0);
            right1.setPower(0);
        }
        else if(driveTrain == DriveTrain.DRIVE_TRAIN_MECANUM || driveTrain == DriveTrain.DRIVE_TRAIN_INVERSE_MECANUM)
        {
            left1.setPower(0);
            left2.setPower(0);

            right1.setPower(0);
            right2.setPower(0);

        }


        // wait for rotation to stop.
        opMode.sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }



    protected double encoderLimit = 0;

    public void setRangeLimit(double rangeMm)
    {
        if(driveTrain == DriveTrain.DRIVE_TRAIN_MECANUM || driveTrain == DriveTrain.DRIVE_TRAIN_INVERSE_MECANUM)
        {
            left1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            left2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        encoderLimit = rangeMm * COUNTS_PER_MM;
    }

    public void updateRange()
    {

    }

    private void initImu(BNO055IMU imu)
    {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        opMode.telemetry.addData("Mode", "calibrating...");
        opMode.telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!opMode.isStopRequested() && !imu.isGyroCalibrated())
        {
            opMode.sleep(50);
            opMode.idle();
        }

        opMode.telemetry.addData("Mode", "waiting for start");
        opMode.telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
        opMode.telemetry.update();
    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle()
    {
        lastAngles = IMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

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

        Orientation angles = IMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

}


