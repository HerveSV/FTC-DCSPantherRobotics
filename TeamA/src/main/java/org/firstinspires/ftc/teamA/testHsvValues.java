package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import helperClassesA.SkystonePantherMode;

@TeleOp
public class testHsvValues extends SkystonePantherMode
{
    @Override
    public void runOpMode()
    {
        initColourSensor();

        waitForStart();
        float[] hsvValues = new float[3];
        while(opModeIsActive())
        {
            hsvValues = getColourReadings();

            telemetry.addLine()
                    .addData("H", "%.3f", hsvValues[0])
                    .addData("S", "%.3f", hsvValues[1])
                    .addData("V", "%.3f", hsvValues[2]);
        }

    }

}
