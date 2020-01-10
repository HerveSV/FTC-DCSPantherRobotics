package Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveTrain {
    DcMotor ld;
    DcMotor rd;
    DcMotor hd;

    public DriveTrain(DcMotor ld, DcMotor rd, DcMotor hd)
    {
        ld.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ld.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.ld = ld;
        this.rd = rd;
        this.hd = hd;
    }

    public void encodersOn()
    {
        ld.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encodersOff()
    {
        ld.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void applyPower(double ldpower, double rdpower, double hdpower){
        ld.setPower(ldpower);
        rd.setPower(rdpower);
        hd.setPower(hdpower);
    }


}
