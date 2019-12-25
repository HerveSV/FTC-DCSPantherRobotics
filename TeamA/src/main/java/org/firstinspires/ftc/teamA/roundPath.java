package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import helperClassesA.DriveStateMachine;
import helperClassesA.SkystoneAutonMode;

@Autonomous
public class roundPath extends SkystoneAutonMode {

    @Override
    public void runOpMode()
    {
        initRevImu();
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        driveTrain = DriveTrain.MECANUM;


        waitForStart();
        runState(State.TURNRIGHT, 180, 0.5);
        sleep(500);
        runState(State.TURN, 180, 0.5);
    }
}
