package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import helperClassesA.DriveStateMachine;
import helperClassesA.SkystoneAutonMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous
public class AutonMode1 extends SkystoneAutonMode {


    @Override
    public void runOpMode() throws InterruptedException
    {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        initRevImu();
        waitForStart();
        initVuforia();
        targetsSkyStone.activate();

        runState(State.FORWARDS, 500, 0.5);


    }


    public AutonMode1()
    {
        CAMERA_FORWARD_DISPLACEMENT = 0.0f;
        CAMERA_LEFT_DISPLACEMENT = 0.0f;
        CAMERA_VERTICAL_DISPLACEMENT = 0.0f;
    }

}
