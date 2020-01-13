package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import helperClassesA.PantherOpMode;
@TeleOp
public class displayArmAngle extends PantherOpMode {




    private DcMotor armHingeMotor;

    private static final int ARM_H_COUNT_LOWER_LIMIT = 0;
    private static final int ARM_H_COUNT_UPPER_LIMIT = 1700;
    private static final int ARM_H_180_COUNTS = ARM_H_COUNT_UPPER_LIMIT - ARM_H_COUNT_LOWER_LIMIT;

    @Override
    public void runOpMode()
    {
        armHingeMotor = hardwareMap.get(DcMotor.class, "armHingeMotor");
        waitForStart();

        armHingeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armHingeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(opModeIsActive())
        {
            telemetry.addData("Encoder count: ", getArmAngle180());
            telemetry.update();
        }

    }

    private double getArmAngle180()
    {
        int pos = armHingeMotor.getCurrentPosition();
        double difFromOrigin = pos - ARM_H_COUNT_LOWER_LIMIT;

        double angle = Range.clip((difFromOrigin/ARM_H_180_COUNTS)*180, 0, 180);
        return angle;
    }

    private int getEncoderCount180(double angle)
    {
        double diffFromOrigin = (angle/180)*ARM_H_180_COUNTS;
        int pos = (int) (diffFromOrigin + ARM_H_COUNT_LOWER_LIMIT);
        return pos;
    }


}
