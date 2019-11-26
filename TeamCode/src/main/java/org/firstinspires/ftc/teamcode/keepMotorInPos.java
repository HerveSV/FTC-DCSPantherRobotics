package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import helperClasses.PIDFmanager;

@TeleOp
public class keepMotorInPos extends LinearOpMode {
    private DcMotor motor;
    private static final float MAXPOWER = 1;
    private static final float THRESHOLD = 500;

    @Override
    public void runOpMode() {
        waitForStart();
        motor = hardwareMap.get(DcMotor.class, "testMotor");
        PIDFmanager.setPIDF((DcMotorEx) motor);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(opModeIsActive())
        {
            telemetry.addData("Encoder counts:", motor.getCurrentPosition());
            motor.setPower(1.0f * getPower(0, motor.getCurrentPosition()));
        }
    }
    public float getPower(int target, int current) {
        int diff = target - current;
        float val = MAXPOWER / THRESHOLD * diff;
        if (Math.abs(diff) < THRESHOLD) {
            return val;
        } else {
            if (val > 0) {
                return MAXPOWER;
            } else {
                return - MAXPOWER;
            }
        }
    }
}
