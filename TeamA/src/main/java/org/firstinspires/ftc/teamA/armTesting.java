package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import helperClassesA.PIDFmanager;

@TeleOp
public class armTesting extends LinearOpMode {

    private DcMotorEx armHingeMotor;

    private static final int ARM_H_COUNT_LOWER_LIMIT = 0;
    private static final int ARM_H_COUNT_UPPER_LIMIT = 1500;
    private static final int ARM_H_180_COUNTS = ARM_H_COUNT_UPPER_LIMIT - ARM_H_COUNT_LOWER_LIMIT;

    @Override
    public void runOpMode()
    {
        armHingeMotor = hardwareMap.get(DcMotorEx.class, "armHingeMotor");
        PIDFmanager.setPIDF(armHingeMotor);
        armHingeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armHingeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double armPow = 0;
        while(opModeIsActive())
        {
            if(this.gamepad1.dpad_up)
            {
                armPow += 0.1;
            }
        }
    }

    private double getArmAngle180()
    {
        int pos = armHingeMotor.getCurrentPosition();
        int difFromOrigin = pos - ARM_H_COUNT_LOWER_LIMIT;

        double angle = (difFromOrigin/ARM_H_180_COUNTS)*180;
        return angle;
    }


}
