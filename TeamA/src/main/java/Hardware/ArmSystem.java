package Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class ArmSystem {
    DcMotor hinge;
    DcMotor extension;

    Servo leftGrabber;
    Servo rightGrabber;

    public ArmSystem(DcMotor hinge, DcMotor extension, Servo leftGrabber, Servo rightGrabber)
    {
        hinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.hinge = hinge;
        this.extension = extension;
        this.leftGrabber = leftGrabber;
        this.rightGrabber = rightGrabber;
    }

}
