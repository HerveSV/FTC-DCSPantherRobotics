package org.firstinspires.ftc.teamA;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import helperClassesA.SkystonePantherMode;


@TeleOp
public class TestPantherMode extends SkystonePantherMode {

    @Override
    public void runOpMode()
    {

        waitForStart();
        initVuforia();


        while(opModeIsActive())
        {
            targetsSkyStone.activate();
            updateStoneTargetPosition();
        }
        targetsSkyStone.deactivate();




    }



}
