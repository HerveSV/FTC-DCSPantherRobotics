package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import helperClasses.AutonOpMode;


@Autonomous
public class ExampleAutonPath extends AutonOpMode {

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;
    @Override
    public void runOpMode() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();

        //this completes a complete square shaped path
        runState(State.FORWARDS, 30, 0.5); //each side of this square path measures 30cm
        runState(State.LEFTSTRAFE, 30, 0.5);
        runState(State.BACKWARDS, 30, 0.5);
        runState(State.RIGHTSTRAFE, 30, 0.5);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

}
