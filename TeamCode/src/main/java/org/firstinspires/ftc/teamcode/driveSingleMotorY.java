package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class driveSingleMotorY extends LinearOpMode {

    private DcMotor m_motor;

    @Override
    public void runOpMode() {
        waitForStart();
        m_motor = hardwareMap.get(DcMotor.class, "testMotor");
        m_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(opModeIsActive())
        {
            double pwr = this.gamepad1.right_stick_y;
            m_motor.setPower(Range.clip(pwr, -1.0, 1.0));
            telemetry.addData("Motor Power:", m_motor.getPower());
            telemetry.addData("Theoretical pwr:", pwr);
            telemetry.addData("encoder count", m_motor.getCurrentPosition());
            telemetry.update();

            idle();
        }



    }
}
