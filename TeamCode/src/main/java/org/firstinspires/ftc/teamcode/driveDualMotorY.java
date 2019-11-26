package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class driveDualMotorY extends LinearOpMode {
    private DcMotor motor1;
    private DcMotor motor2;

    @Override
    public void runOpMode() {

        waitForStart();
        motor1 = hardwareMap.get(DcMotor.class, "testMotor1");
        motor2 = hardwareMap.get(DcMotor.class, "testMotor2");
        while(opModeIsActive())
        {
            double pwr1 = this.gamepad1.left_stick_y;
            double pwr2 = this.gamepad1.right_stick_y;
            motor1.setPower(Range.clip(pwr1, -1.0, 1.0));
            motor2.setPower(Range.clip(pwr2, -1.0, 1.0));
            telemetry.addData("Motor1 Power:", motor1.getPower());
            telemetry.addData("Theo. pwr1:", pwr1);
            telemetry.addData("Motor2 Power:", motor2.getPower());
            telemetry.addData("Theo. pwr2:", pwr2);

            telemetry.update();

            idle();
        }



    }
}
