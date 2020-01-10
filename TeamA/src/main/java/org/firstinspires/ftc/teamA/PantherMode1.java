package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import helperClassesA.SkystonePantherMode;

import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous
public class PantherMode1 extends SkystonePantherMode {


    @Override
    public void runOpMode() throws InterruptedException
    {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        initRevImu();
        waitForStart();
        initVuforia();
        initColourSensor();
        targetsSkyStone.activate();

        runState(State.FORWARDS, 500, 0.5);
        boolean stoneCentered = false;

        while(opModeIsActive())
        {
            if(locateStoneTarget() == RelativePos.CENTER)
            {
                stoneCentered = true;
            }

            while(!stoneCentered)
            {
                runState(State.RIGHTSTRAFE, 0.1);

                if(locateStoneTarget() == RelativePos.CENTER)
                {
                    stoneCentered = true;
                }
            }
            runState(State.STOP, 0);

            RelativePos posCheck = locateStoneTarget(2);
            if(posCheck == RelativePos.CENTER)
            {
                break;
            }
            else
            {
                continue;
            }
        }




    }


    public PantherMode1()
    {
        CAMERA_FORWARD_DISPLACEMENT = 0.0f;
        CAMERA_LEFT_DISPLACEMENT = 0.0f;
        CAMERA_VERTICAL_DISPLACEMENT = 0.0f;
    }

}
