package helperClassesA;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class AutonStateMachine extends DriveStateMachine{


    protected final DcMotor m_left1; //These cannot be changed into other motors once assigned in the constructor
    protected final DcMotor m_left2;
    protected final DcMotor m_right1;
    protected final DcMotor m_right2;
    protected final DcMotor m_hMotor;

    protected final DriveTrain driveTrain;

    public AutonStateMachine(DcMotor leftFront, DcMotor rightFront, DcMotor leftBack, DcMotor rightBack, SkystoneAutonMode opMode, DriveTrain train) { //the constructor

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

    public AutonStateMachine(DcMotor leftSide, DcMotor rightSide, DcMotor hMotor, SkystoneAutonMode opMode, DriveTrain train) {
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

    public void navigateToSkystone(VectorF translation, double xOffset, double yOffset, double speed) //the robot will try its best to get right in front of skystone
    {
        double x = translation.get(0) - xOffset;
        double y = translation.get(2) - Math.abs(yOffset);

        runState(State.STRAFE, x, speed);
        runState(State.FORWARDS, y, speed);
    }



}
