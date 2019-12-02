package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.hardware.DcMotor;

import helperClassesA.AutonStateMachine;
import helperClassesA.DriveStateMachine;
import helperClassesA.SkystoneAutonMode;

public class TestAutonMode extends SkystoneAutonMode {

    @Override
    public void runOpMode()
    {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        AutonStateMachine robotState = new AutonStateMachine(leftFront, rightFront, leftBack, rightBack, this, DriveStateMachine.DriveTrain.DRIVE_TRAIN_MECANUM);

        waitForStart();
        initVuforia();
        targetsSkyStone.activate();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        resetStartTime();
        while(!(targetVisible || stoneLastLocation == null) && getRuntime() < 3)
        {
            locateStoneTarget();
        }
        robotState.navigateToSkystone(stoneLastTranslate, 15, 75, 0.5 );
        /*if(targetVisible && stoneLastLocation != null)
        {
            double x = stoneLastTranslate.get(0);
            double y = stoneLastTranslate.get(1);


        }*/



    }

    public TestAutonMode()
    {
        CAMERA_FORWARD_DISPLACEMENT = 0.0f;
        CAMERA_LEFT_DISPLACEMENT = 0.0f;
        CAMERA_VERTICAL_DISPLACEMENT = 0.0f;
    }


}
