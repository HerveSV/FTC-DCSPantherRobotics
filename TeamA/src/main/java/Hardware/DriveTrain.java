package Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import helperClassesA.PIDFmanager;

public class DriveTrain {
    DcMotor lf;
    DcMotor rf;
    DcMotor lb;
    DcMotor rb;

    public DriveTrain(DcMotor lf, DcMotor rf, DcMotor lb, DcMotor rb)
    {
        PIDFmanager.setPIDF(lf);
        PIDFmanager.setPIDF(rf);
        PIDFmanager.setPIDF(lb);
        PIDFmanager.setPIDF(rb);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lb.setDirection(DcMotor.Direction.REVERSE);

        this.lf = lf;
        this.rf = rf;
        this.lb = lb;
        this.rb = rb;
    }

    public void encodersOn()
    {
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encodersOff()
    {
        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void applyPower(double lfpower, double rfpower, double lbpower, double rbpower){
        lf.setPower(lfpower);
        rf.setPower(rfpower);
        lb.setPower(lbpower);
        rb.setPower(rbpower);
    }


}
