package helperClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import helperClasses.PIDFmanager;

public class DriveStateMachine {

    private final DcMotor m_left1; //These cannot be changed into other motors once assigned in the constructor
    private final DcMotor m_left2;
    private final DcMotor m_right1;
    private final DcMotor m_right2;
    private final DcMotor m_hMotor;

    public enum DriveTrain {
        DRIVE_TRAIN_MECANUM,
        DRIVE_TRAIN_INVERSE_MECANUM,
        DRIVE_TRAIN_HDRIVE,
        DRIVE_TRAIN_PUSHBOT,
    }

    private final DriveTrain driveTrain;


    private LinearOpMode m_opMode;

    static final double COUNTS_PER_MOTOR_REV = 1478.4;    // Number of ticks for every full revolution/rotation of the motor shaft
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // Depends on gearing ratio between motor and wheel
    static final double WHEEL_DIAMETER_CM = 4.0;     // For figuring circumference
    static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * 3.1415);  //This is the amount of ticks we have every cm travelled by the wheel
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;


    public DriveStateMachine(DcMotor leftFront, DcMotor rightFront, DcMotor leftBack, DcMotor rightBack, LinearOpMode opMode, DriveTrain train) { //the constructor

        //the motors are assigned
        m_left1 = leftFront;
        m_left2 = leftBack;
        m_right1 = rightFront;
        m_right2 = rightBack;
        if (train == DriveTrain.DRIVE_TRAIN_HDRIVE) {
            train = DriveTrain.DRIVE_TRAIN_MECANUM;
        }
        driveTrain = train;
        m_hMotor = null;
        PIDFmanager.setPIDF((DcMotorEx) m_left1);
        PIDFmanager.setPIDF((DcMotorEx) m_left2);
        PIDFmanager.setPIDF((DcMotorEx) m_right1);
        PIDFmanager.setPIDF((DcMotorEx) m_right2);
        PIDFmanager.setPIDF((DcMotorEx) m_hMotor);


        m_opMode = opMode;

    }

    public DriveStateMachine(DcMotor leftSide, DcMotor rightSide, DcMotor hMotor, LinearOpMode opMode, DriveTrain train) {
        if (train != DriveTrain.DRIVE_TRAIN_HDRIVE) {
            train = DriveTrain.DRIVE_TRAIN_HDRIVE;
        }
        m_left1 = leftSide;
        m_right1 = rightSide;
        m_hMotor = hMotor;


        driveTrain = train;

        m_left2 = null;
        m_right2 = null;

        PIDFmanager.setPIDF((DcMotorEx) m_left1);
        PIDFmanager.setPIDF((DcMotorEx) m_left2);
        PIDFmanager.setPIDF((DcMotorEx) m_right1);
        PIDFmanager.setPIDF((DcMotorEx) m_right2);
        PIDFmanager.setPIDF((DcMotorEx) m_hMotor);

        m_opMode = opMode;

    }



    public enum State {
        INITIALISE,
        FORWARDS,
        BACKWARDS,
        LEFTSTRAFE,
        RIGHTSTRAFE,
        TURNLEFT,
        TURNRIGHT,
        ARMUP,
        ARMDOWN,
        FLIPSERVO,
        BOTDOWN,
        CHECK,
        STOP
    }

    ;

    public void runState(State currState, double distanceCm) {
        //this function is called
        switch (driveTrain) {

            case DRIVE_TRAIN_MECANUM:
                switch (currState) {
                    case FORWARDS:
                        encoderDrive(DRIVE_SPEED, distanceCm, distanceCm, distanceCm, distanceCm);
                        break;
                    case BACKWARDS:
                        encoderDrive(DRIVE_SPEED, -distanceCm, -distanceCm, -distanceCm, -distanceCm);
                        break;
                    case LEFTSTRAFE:
                        encoderDrive(DRIVE_SPEED, distanceCm, -distanceCm, -distanceCm, distanceCm);
                        break;
                    case RIGHTSTRAFE:
                        encoderDrive(DRIVE_SPEED, -distanceCm, distanceCm, distanceCm, -distanceCm);
                        break;
                    case TURNLEFT:
                        encoderDrive(TURN_SPEED, -distanceCm, -distanceCm, distanceCm, distanceCm);
                        break;
                    case TURNRIGHT:
                        encoderDrive(TURN_SPEED, distanceCm, distanceCm, -distanceCm, -distanceCm);
                        break;

                    default:
                        break;
                }
            case DRIVE_TRAIN_INVERSE_MECANUM:
                switch (currState) {
                    case LEFTSTRAFE:
                        encoderDrive(DRIVE_SPEED, distanceCm, distanceCm, distanceCm, distanceCm);
                        break;
                    case RIGHTSTRAFE:
                        encoderDrive(DRIVE_SPEED, -distanceCm, -distanceCm, -distanceCm, -distanceCm);
                        break;
                    case FORWARDS:
                        encoderDrive(DRIVE_SPEED, distanceCm, -distanceCm, -distanceCm, distanceCm);
                        break;
                    case BACKWARDS:
                        encoderDrive(DRIVE_SPEED, -distanceCm, distanceCm, distanceCm, -distanceCm);
                        break;
                    case TURNLEFT:
                        encoderDrive(TURN_SPEED, -distanceCm, -distanceCm, distanceCm, distanceCm);
                        break;
                    case TURNRIGHT:
                        encoderDrive(TURN_SPEED, distanceCm, distanceCm, -distanceCm, -distanceCm);
                        break;

                    default:
                        break;
                }
            case DRIVE_TRAIN_HDRIVE:
                switch (currState) {
                    case FORWARDS:
                        encoderDrive(DRIVE_SPEED, distanceCm, distanceCm);
                        break;
                    case BACKWARDS:
                        encoderDrive(DRIVE_SPEED, -distanceCm, -distanceCm);
                        break;
                    case LEFTSTRAFE:
                        encoderDrive(DRIVE_SPEED, distanceCm, m_hMotor);
                        break;
                    case RIGHTSTRAFE:
                        encoderDrive(DRIVE_SPEED, -distanceCm, m_hMotor);
                        break;
                    case TURNLEFT:
                        encoderDrive(TURN_SPEED, -distanceCm, distanceCm);
                        break;
                    case TURNRIGHT:
                        encoderDrive(TURN_SPEED, distanceCm, -distanceCm);
                        break;

                    default:
                        break;
                }

            case DRIVE_TRAIN_PUSHBOT:
                switch (currState) {
                    case FORWARDS:
                        encoderDrive(DRIVE_SPEED, distanceCm, distanceCm);
                        break;
                    case BACKWARDS:
                        encoderDrive(DRIVE_SPEED, -distanceCm, -distanceCm);
                        break;
                    case TURNLEFT:
                        encoderDrive(TURN_SPEED, -distanceCm, distanceCm);
                        break;
                    case TURNRIGHT:
                        encoderDrive(TURN_SPEED, distanceCm, -distanceCm);
                        break;

                    default:
                        break;
                }
        }
    }


    private void encoderDrive(double speed,
                              double leftFrontCm, double leftBackCm, double rightFrontCm, double rightBackCm) {
        int newLeftFrontTarget;
        int newLeftBackTarget;
        int newRightFrontTarget;
        int newRightBackTarget;


        // Ensure that the opmode is still active
        if (m_opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = m_left1.getCurrentPosition() + (int) (leftFrontCm * COUNTS_PER_CM);
            newLeftBackTarget = m_left2.getCurrentPosition() + (int) (leftBackCm * COUNTS_PER_CM);
            newRightFrontTarget = m_right1.getCurrentPosition() + (int) (rightFrontCm * COUNTS_PER_CM);
            newRightBackTarget = m_right2.getCurrentPosition() + (int) (rightBackCm * COUNTS_PER_CM);

            m_left1.setTargetPosition(newLeftFrontTarget);
            m_left2.setTargetPosition(newLeftBackTarget);
            m_right1.setTargetPosition(newRightFrontTarget);
            m_right2.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            m_left1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m_left2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m_right1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m_right2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion
            m_left1.setPower(Math.abs(speed));
            m_left2.setPower(Math.abs(speed));
            m_right1.setPower(Math.abs(speed));
            m_right2.setPower(Math.abs(speed));

            // keep looping while we are still active, and both motors are still running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            while (m_opMode.opModeIsActive() &&
                    (m_left1.isBusy() && m_left2.isBusy() && m_right1.isBusy() && m_right2.isBusy())) {

                // Display it for the driver.
                m_opMode.telemetry.addData("Path1", "Running to :", newLeftFrontTarget, newLeftBackTarget, newRightFrontTarget, newRightBackTarget);
                m_opMode.telemetry.addData("Path2", "Running at :",
                        m_left1.getCurrentPosition(),
                        m_left2.getCurrentPosition(),
                        m_right1.getCurrentPosition(),
                        m_left2.getCurrentPosition());
                m_opMode.telemetry.update();
            }

            // Stop all motion;

            m_left1.setPower(0);
            m_left2.setPower(0);
            m_right1.setPower(0);
            m_right2.setPower(0);

            // Turn off RUN_TO_POSITION
            m_left1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m_left2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m_right1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m_right2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            m_opMode.sleep(250);   // optional pause after each move
        }
    }

    private void encoderDrive(double speed, double leftDriveCm, double rightDriveCm) {
        int newLeftDriveTarget;
        int newRightDriveTarget;


        // Ensure that the opmode is still active
        if (m_opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftDriveTarget = m_left1.getCurrentPosition() + (int) (leftDriveCm * COUNTS_PER_CM);
            newRightDriveTarget = m_right1.getCurrentPosition() + (int) (rightDriveCm * COUNTS_PER_CM);

            m_left1.setTargetPosition(newLeftDriveTarget);
            m_right1.setTargetPosition(newRightDriveTarget);

            // Turn On RUN_TO_POSITION
            m_left1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m_right1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion
            m_left1.setPower(Math.abs(speed));
            m_right1.setPower(Math.abs(speed));

            // keep looping while we are still active, and both motors are still running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            while (m_opMode.opModeIsActive() &&
                    (m_left1.isBusy() && m_right1.isBusy())) {

                // Display it for the driver.
                m_opMode.telemetry.addData("Path1", "Running to :", newLeftDriveTarget, newRightDriveTarget);
                m_opMode.telemetry.addData("Path2", "Running at :",
                        m_left1.getCurrentPosition(),
                        m_right1.getCurrentPosition());
                m_opMode.telemetry.update();
            }

            // Stop all motion;

            m_left1.setPower(0);
            m_right1.setPower(0);

            // Turn off RUN_TO_POSITION
            m_left1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            m_right1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            m_opMode.sleep(250);   // optional pause after each move
        }
    }

    private void encoderDrive(double speed, double driveCm, DcMotor motor) {
        int newDriveTarget;


        // Ensure that the opmode is still active
        if (m_opMode.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newDriveTarget = motor.getCurrentPosition() + (int) (driveCm * COUNTS_PER_CM);

            motor.setTargetPosition(newDriveTarget);

            // Turn On RUN_TO_POSITION
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion
            motor.setPower(Math.abs(speed));

            // keep looping while we are still active, and both motors are still running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            while (m_opMode.opModeIsActive() &&
                    (m_left1.isBusy() && m_right1.isBusy())) {

                // Display it for the driver.
                m_opMode.telemetry.addData("Path1", "Running to :", newDriveTarget);
                m_opMode.telemetry.addData("Path2", "Running at :",
                        motor.getCurrentPosition());
                m_opMode.telemetry.update();
            }

            // Stop all motion;

            motor.setPower(0);

            // Turn off RUN_TO_POSITION
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            m_opMode.sleep(250);   // optional pause after each move
        }
    }
}


