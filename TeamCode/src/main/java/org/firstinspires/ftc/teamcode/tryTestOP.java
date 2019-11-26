package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class tryTestOP extends LinearOpMode {

    @Override
    public void runOpMode()
    {
        waitForStart();
        testMachine test = new testMachine(this);
        test.run();
    }
}
