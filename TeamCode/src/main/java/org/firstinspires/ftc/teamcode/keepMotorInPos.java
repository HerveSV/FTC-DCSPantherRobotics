package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import helperClasses.PIDFmanager;

@TeleOp
public class keepMotorInPos extends LinearOpMode {
    private static final float MAXPOWER = 1.0f;
    private static final float THRESHOLD = 100.0f;
    private static final int STEP = 1;
    private static final int OFFSET_BOUND = 500;

    private int offset = 0;

    @Override
    public void runOpMode() {
        waitForStart();

        DcMotor motor = hardwareMap.get(DcMotor.class, "testMotor");
        PIDFmanager.setPIDF((DcMotorEx) motor);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(opModeIsActive())
        {
            telemetry.addData("Encoder counts:", motor.getCurrentPosition());
            telemetry.addData("Power: ", getPower(0, motor.getCurrentPosition()));
            telemetry.addData("Offset: ", offset);
            motor.setPower(1.0f * getPower(0, motor.getCurrentPosition()));
            telemetry.update();
        }
    }
    private float getPower(int target, int current) {
        int diff = target - current;

        if (diff > 0) {
            offset += STEP;
        }
        if (diff < 0) {
            offset -= STEP;
        }
        if (offset > OFFSET_BOUND) {
            offset = OFFSET_BOUND;
        } else if (offset < - OFFSET_BOUND) {
            offset = - OFFSET_BOUND;
        }

        diff = target + offset - current;

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
