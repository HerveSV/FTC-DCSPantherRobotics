package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import helperClassesA.PantherOpMode;
import helperClassesA.PIDFmanager;

@Autonomous
public class Panther1StScrim extends PantherOpMode {

    @Override
    public void runOpMode()
    {
        rebaseDriveTrain(DriveTrainType.MECANUM);

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftBack.setDirection(DcMotor.Direction.REVERSE);

        PIDFmanager.setPIDF(leftFront);
        PIDFmanager.setPIDF(rightFront);
        PIDFmanager.setPIDF(leftBack);
        PIDFmanager.setPIDF(rightBack);
        waitForStart();

        runState(State.FORWARDS, 500, 0.5);
    }

}
