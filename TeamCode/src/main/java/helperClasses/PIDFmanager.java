package helperClasses;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public abstract class PIDFmanager {

    private static double maxVel = 4480;
    private static double F = 32767/maxVel;//32767 / maxVelocity of motor
    private static double P = 0.1 * F;
    private static double D = 0.0; //DO NOT CHANGE
    private static double I = 0.1 * P;
    private static double position = 5.0; //DO NOT CHANGE
    private static PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);

    public static void setPIDF(DcMotorEx ex)
    {
        ex.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        ex.setVelocityPIDFCoefficients(P, I, D, F);
        ex.setPositionPIDFCoefficients(position);
    }

    public static void setPIDF(DcMotor mtr)
    {
        DcMotorEx ex = (DcMotorEx)mtr;
        ex.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        ex.setVelocityPIDFCoefficients(P, I, D, F);
        ex.setPositionPIDFCoefficients(position);
    }

}
