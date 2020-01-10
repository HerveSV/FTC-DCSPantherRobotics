package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class testGrabbers extends LinearOpMode {

    private Servo leftGrabber;
    private Servo rightGrabber;

    @Override
    public void runOpMode()
    {
       if(this.gamepad1.left_bumper)
       {
           leftGrabber.setPosition(0.5);
       }
       else
       {
           leftGrabber.setPosition(0);
       }

       if(this.gamepad1.right_bumper)
       {
           rightGrabber.setPosition(0.5);
       }
       else
       {
           rightGrabber.setPosition(0);
       }

       telemetry.addData("lg pos", leftGrabber.getPosition());
       telemetry.addData("rg pos", rightGrabber.getPosition());
       telemetry.update();
    }

}
