package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import helperClassesA.SkystonePantherMode;

@Autonomous
public class testCamera extends SkystonePantherMode {

    @Override
    public void runOpMode() throws InterruptedException
    {
        waitForStart();
        initVuforia();
        targetsSkyStone.activate();
        while(opModeIsActive())
        {
            locateStoneTarget();
        }
        targetsSkyStone.deactivate();
    }


}
