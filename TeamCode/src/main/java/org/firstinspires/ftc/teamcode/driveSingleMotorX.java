package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class driveSingleMotorX extends LinearOpMode {

    private DcMotor m_motor;

    @Override
    public void runOpMode() {

        waitForStart();
        m_motor = hardwareMap.get(DcMotor.class, "testMotor");
        while(opModeIsActive())
        {
            double pwr = this.gamepad1.right_stick_x;
            m_motor.setPower(Range.clip(pwr, -1.0, 1.0));
            telemetry.addData("Motor Power:", m_motor.getPower());
            telemetry.addData("Theoretical pwr:", pwr);
            telemetry.update();

            idle();
        }



    }
}
