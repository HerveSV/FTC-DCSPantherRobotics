package org.firstinspires.ftc.teamA;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import helperClassesA.SkystonePantherMode;


@Autonomous
public class ExamplePantherPath extends SkystonePantherMode {

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;
    @Override
    public void runOpMode() throws InterruptedException
    {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        initRevImu();
        waitForStart();

        //this completes a complete square shaped path
        while(!this.gamepad1.left_bumper)
        {
            telemetry.addLine("Strafe Square");
            telemetry.addLine("Awaiting start (left bumper)");
            telemetry.update();
        }

        while(!this.gamepad1.left_bumper)
        {
            telemetry.addLine("Turn Square");
            telemetry.addLine("Awaiting start (left bumper)");
            telemetry.update();
        }

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

}
