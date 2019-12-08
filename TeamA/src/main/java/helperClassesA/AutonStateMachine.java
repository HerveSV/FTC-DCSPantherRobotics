package helperClassesA;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class AutonStateMachine extends DriveStateMachine{


    protected final DcMotor left1; //These cannot be changed into other motors once assigned in the constructor
    protected final DcMotor left2;
    protected final DcMotor right1;
    protected final DcMotor right2;
    protected final DcMotor hMotor;
    protected final BNO055IMU IMU;



    protected final DriveTrain driveTrain;

    public AutonStateMachine(DcMotor leftFront, DcMotor rightFront, DcMotor leftBack, DcMotor rightBack, BNO055IMU imu, SkystoneAutonMode opMode, DriveTrain train) { //the constructor

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

    public AutonStateMachine(DcMotor leftSide, DcMotor rightSide, DcMotor hDrive, BNO055IMU imu, SkystoneAutonMode opMode, DriveTrain train) {
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

    public void navigateToSkystone(VectorF translation, double xOffsetMm, double yOffsetMm, double speed) //the robot will try its best to get right in front of skystone
    {
        double x = translation.get(0) - xOffsetMm;
        double y = translation.get(2) - Math.abs(yOffsetMm);

        runState(State.STRAFE, x, speed);
        runState(State.FORWARDS, y, speed);
    }



}
