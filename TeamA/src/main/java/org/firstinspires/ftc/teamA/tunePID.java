package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import helperClassesA.PIDController;
import helperClassesA.PIDFmanager;


@TeleOp
public class tunePID extends LinearOpMode {
    private DcMotorEx armHingeMotor;
    private PIDController hingeController = new PIDController(0,0, 0);

    private static final int ARM_H_COUNT_LOWER_LIMIT = 0;
    private static final int ARM_H_COUNT_UPPER_LIMIT = 1500;
    private static final int ARM_H_180_COUNTS = ARM_H_COUNT_UPPER_LIMIT - ARM_H_COUNT_LOWER_LIMIT;

    private double setpoint = 0;

    @Override
    public void runOpMode()
    {
        waitForStart();

        armHingeMotor = hardwareMap.get(DcMotorEx.class, "armHingeMotor");
        PIDFmanager.setPIDF(armHingeMotor);
        armHingeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armHingeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hingeController.enable();
        setpoint = getEncoderCount180(90);
        armHingeMotor.setPower(0);

        double armPow = 0;
        while(opModeIsActive())
        {


            armHingeMotor.setPower(hingeController.performPID(armHingeMotor.getCurrentPosition()));

        }
    }


    private double getArmAngle180()
    {
        int pos = armHingeMotor.getCurrentPosition();
        int difFromOrigin = pos - ARM_H_COUNT_LOWER_LIMIT;

        double angle = (difFromOrigin/ARM_H_180_COUNTS)*180;
        return angle;
    }

    private int getEncoderCount180(double angle)
    {
        double diffFromOrigin = (angle/180)*ARM_H_180_COUNTS;
        int pos = (int) (diffFromOrigin + ARM_H_COUNT_LOWER_LIMIT);
        return pos;
    }



}

