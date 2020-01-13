package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import helperClassesA.PIDController;
import helperClassesA.PIDFmanager;


@TeleOp
public class tunePID extends LinearOpMode {
    private DcMotorEx armHingeMotor;
    double P = 0;
    private PIDController hingeController = new PIDController(P,0, 0);

    private static final int ARM_H_COUNT_LOWER_LIMIT = 0;
    private static final int ARM_H_COUNT_UPPER_LIMIT = 1700;
    private static final int ARM_H_180_COUNTS = ARM_H_COUNT_UPPER_LIMIT - ARM_H_COUNT_LOWER_LIMIT;

    private double setpoint = 0;
    private boolean upLastState = false;
    private boolean downLastState = false;



    @Override
    public void runOpMode()
    {
        waitForStart();


        armHingeMotor = hardwareMap.get(DcMotorEx.class, "armHingeMotor");
        PIDFmanager.setPIDF(armHingeMotor);
        armHingeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armHingeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hingeController.enable();
        setpoint = 90;
        hingeController.setSetpoint(setpoint);
        armHingeMotor.setPower(0);

        resetStartTime();
        double armPow = 0;
        while(opModeIsActive())
        {
            if(this.gamepad1.dpad_up)
            {
                if (!upLastState)
                {
                    P += 0.05;
                    hingeController.setPID(P, 0, 0);
                    upLastState = true;
                }
            } else {
                upLastState = false;
            }
            if(this.gamepad1.dpad_down)
            {
                if(!downLastState)
                {
                    P -= 0.05;
                    hingeController.setPID(P, 0, 0);
                    downLastState = true;
                }

            }
            else {
                downLastState = false;
            }


            armPow = hingeController.performPID(getArmAngle180(), getRuntime());
            armHingeMotor.setPower(armPow);
            telemetry.addData("Motor pwr", armHingeMotor.getPower());
            telemetry.addData("Arm pwr", armPow);
            telemetry.addData("Arm angle", getArmAngle180());
            telemetry.addData("Total error", hingeController.getTotalError());
            telemetry.addData("P", hingeController.getP());
            telemetry.addData("I", hingeController.getI());
            telemetry.addData("D", hingeController.getD());
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

