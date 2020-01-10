package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class display2EncoderCount extends LinearOpMode {

    private DcMotor motor1;
    private DcMotor motor2;
    @Override
    public void runOpMode()
    {
        motor1 = hardwareMap.get(DcMotor.class, "armHingeMotor");
        motor1 = hardwareMap.get(DcMotor.class, "armExtensionMotor");
        waitForStart();

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(!opModeIsActive())
        {
            telemetry.addData("Encoder 1 count: ", motor1.getCurrentPosition());
            telemetry.addData("Encoder 2 count: ", motor2.getCurrentPosition());
        }

    }

}
