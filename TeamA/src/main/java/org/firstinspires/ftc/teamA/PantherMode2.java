package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.hardware.DcMotor;

import helperClassesA.SkystonePantherMode;

public class PantherMode2 extends SkystonePantherMode {

    private double RUN_SPEED = 0.5;

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

        initRevImu();
        waitForStart();
        initVuforia();
        initColourSensor();
        targetsSkyStone.activate();

        RelativePos pos = locateStoneTarget(3);

        switch(pos)
        {
            case LEFT:
                pattern = SkystonePattern.A;
                runState(State.LEFTSTRAFE, 200, RUN_SPEED);
                runState(State.FORWARDS, 700, RUN_SPEED);
                runState(State.STOP,0);

            case CENTER:
                pattern = SkystonePattern.B;
                runState(State.FORWARDS, 700, RUN_SPEED);
                runState(State.STOP,0);

            case RIGHT:
                pattern = SkystonePattern.C;
                runState(State.RIGHTSTRAFE, 200, RUN_SPEED);
                runState(State.FORWARDS, 700, RUN_SPEED);
                runState(State.STOP,0);
        }

        //Get Stone

        runState(State.BACKWARDS, 200, RUN_SPEED);
        runState(State.RIGHTSTRAFE, RUN_SPEED);
        while(!isBlue(getColourReadings())){}
        runState(State.STOP, 0);
        //Do stuff




    }

    public boolean isBlue(float[] hsvValues)
    {
        if((hsvValues[0] >= 180/2 && hsvValues[0] <= 300/2) &&
                (hsvValues[1] >= 50 && hsvValues[1] <= 255) &&
                (hsvValues[2] >= 150 && hsvValues[2] <= 255))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isRed(float[] hsvValues)
    {
        if((hsvValues[0] >= 60/2 && hsvValues[0] <= 300/2) &&
                (hsvValues[1] >= 50 && hsvValues[1] <= 255) &&
                (hsvValues[2] >= 150 && hsvValues[2] <= 255))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
