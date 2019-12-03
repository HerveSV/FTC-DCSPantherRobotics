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
    public void runOpMode(){

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        DriveStateMachine robotState = new DriveStateMachine(leftFront, rightFront, leftBack, rightBack, this, DriveStateMachine.DriveTrain.DRIVE_TRAIN_MECANUM);


        waitForStart();
        initVuforia();
        targetsSkyStone.activate();

        /**
         *
         * DO STUFF
         *
         */

    }


    public AutonMode1()
    {
        CAMERA_FORWARD_DISPLACEMENT = 0.0f;
        CAMERA_LEFT_DISPLACEMENT = 0.0f;
        CAMERA_VERTICAL_DISPLACEMENT = 0.0f;
    }

}
