package Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

public class IntakeSystem
{
    DcMotor leftIntake;
    DcMotor rightIntake;

    public IntakeSystem(DcMotor li, DcMotor ri)
    {
        li.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ri.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.leftIntake = li;
        this.rightIntake = ri;
    }


}
