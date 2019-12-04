package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import helperClassesA.SkystoneAutonMode;

@Autonomous
public class testCamera extends SkystoneAutonMode {

    @Override
    public void runOpMode()
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
