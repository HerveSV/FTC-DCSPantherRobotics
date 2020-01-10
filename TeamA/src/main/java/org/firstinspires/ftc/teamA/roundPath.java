package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import com.qualcomm.robotcore.hardware.DcMotor;

import helperClassesA.SkystonePantherMode;

@Autonomous
public class roundPath extends SkystonePantherMode {

    @Override
    public void runOpMode()
    {
        initRevImu();
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        rebaseDriveTrain(DriveTrainType.MECANUM);


        waitForStart();
        runState(State.TURNRIGHT, 180, 0.5);
        sleep(500);
        runState(State.TURN, 180, 0.5);
    }
}
