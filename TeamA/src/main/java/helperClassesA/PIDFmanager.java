package helperClassesA;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public abstract class PIDFmanager {

    private static double maxVel = 4480;
    private static double F = 32767/maxVel;//32767 / maxVelocity of motor
    private static double P = 0.15 * F;
    private static double D = 1.0; //DO NOT CHANGE
    private static double I = 0.1 * P;
    private static double position = 5.0; //DO NOT CHANGE
    private static PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
    private static PIDCoefficients pid = new PIDCoefficients(P, I, D);

    public static void setPIDF(DcMotorEx ex)
    {
        DcMotorControllerEx motorControllerEx = (DcMotorControllerEx)ex.getController();
        // get the port number of our configured motor.
        int motorIndex = (ex).getPortNumber();
        motorControllerEx.setPIDCoefficients(motorIndex, DcMotor.RunMode.RUN_USING_ENCODER, pid);
        motorControllerEx.setPIDCoefficients(motorIndex, DcMotor.RunMode.RUN_TO_POSITION, pid);
        /*PIDCoefficients pid = new PIDCoefficients(P, I, D);
        ex.setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pid);*/
        //ex.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        //ex.setPositionPIDFCoefficients(P);
        //ex.setVelocityPIDFCoefficients(P, I, D, F);
        //ex.setPositionPIDFCoefficients(position);
    }

    public static void setPIDF(DcMotor mtr)
    {
        //PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        DcMotorEx ex = (DcMotorEx)mtr;
        //PIDCoefficients pid = new PIDCoefficients(P, I, D);
        //ex.setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pid);

        DcMotorControllerEx motorControllerEx = (DcMotorControllerEx)ex.getController();
        // get the port number of our configured motor.
        int motorIndex = (ex).getPortNumber();
        motorControllerEx.setPIDCoefficients(motorIndex, DcMotor.RunMode.RUN_USING_ENCODER, pid);
        motorControllerEx.setPIDCoefficients(motorIndex, DcMotor.RunMode.RUN_TO_POSITION, pid);
        //ex.setPositionPIDFCoefficients(P);
        //ex.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        //ex.setVelocityPIDFCoefficients(P, I, D, F);
        //ex.setPositionPIDFCoefficients(position);
    }

    public static double getP()
    {
        return P;
    }
    public static double getI()
    {
        return I;
    }
    public static double getD()
    {
        return D;
    }
    public static double getF()
    {
        return F;
    }

}
