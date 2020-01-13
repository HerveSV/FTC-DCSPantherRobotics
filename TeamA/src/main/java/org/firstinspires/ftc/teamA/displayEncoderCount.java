package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class displayEncoderCount extends LinearOpMode {

    private DcMotor testMotor;
    @Override
    public void runOpMode()
    {
        testMotor = hardwareMap.get(DcMotor.class, "armHingeMotor");
        waitForStart();

        testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(opModeIsActive())
        {
            telemetry.addData("Encoder count: ", testMotor.getCurrentPosition());
        }

    }

}
