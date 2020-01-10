package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import helperClassesA.SkystonePantherMode;

@TeleOp
public class vufConstTest extends SkystonePantherMode {

    @Override
    public void runOpMode()
    {
        initVuforia();
        double leftVar = 0;
        double rightVar = 0;
        String relativPos = "Centre";

        waitForStart();

        while(opModeIsActive())
        {
            if(this.gamepad1.dpad_up)
            {
                leftVar += 0.5;
            }
            else if(this.gamepad1.dpad_down)
            {
                leftVar -= 0.5;
            }

            if(this.gamepad1.dpad_left)
            {
                rightVar += 0.5;
            }
            else if(this.gamepad1.dpad_right)
            {
                rightVar -= 0.5;
            }


            updateStoneTargetPosition();

            if(stoneLastTranslate != null && stoneLastTranslate.get(0) > leftVar)
            {
                relativPos = "Left";
            }
            else if(stoneLastTranslate != null && stoneLastTranslate.get(0) < rightVar)
            {
                relativPos = "Right";
            }
            else
            {
                relativPos = "Centre";
            }

            telemetry.addData("leftVar ", leftVar);
            telemetry.addData("rightVar ", rightVar);
            telemetry.addData("Relative Position ", relativPos);
            telemetry.update();
        }
    }

}
