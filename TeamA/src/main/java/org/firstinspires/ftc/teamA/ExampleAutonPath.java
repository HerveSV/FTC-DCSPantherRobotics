package org.firstinspires.ftc.teamA;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import helperClassesA.DriveStateMachine;
import helperClassesA.SkystoneAutonMode;


@Autonomous
public class ExampleAutonPath extends SkystoneAutonMode {

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
        DriveStateMachine robotState = new DriveStateMachine(leftFront, leftBack, rightFront, rightBack, revImu, this, DriveStateMachine.DriveTrain.DRIVE_TRAIN_MECANUM);
        waitForStart();

        //this completes a complete square shaped path
        while(!this.gamepad1.left_bumper)
        {
            telemetry.addLine("Strafe Square");
            telemetry.addLine("Awaiting start (left bumper)");
            telemetry.update();
        }
        robotState.runState(DriveStateMachine.State.FORWARDS, 300, 0.5); //each side of this square path measures 30cm
        robotState.runState(DriveStateMachine.State.LEFTSTRAFE, 300, 0.5);
        robotState.runState(DriveStateMachine.State.BACKWARDS, 300, 0.5);
        robotState.runState(DriveStateMachine.State.RIGHTSTRAFE, 300, 0.5);

        while(!this.gamepad1.left_bumper)
        {
            telemetry.addLine("Turn Square");
            telemetry.addLine("Awaiting start (left bumper)");
            telemetry.update();
        }
        robotState.runState(DriveStateMachine.State.FORWARDS, 30, 0.5);
        robotState.runState(DriveStateMachine.State.TURNLEFT, 90,  0.5);
        robotState.runState(DriveStateMachine.State.FORWARDS, 30, 0.5);
        robotState.runState(DriveStateMachine.State.TURNLEFT, 90, 0.5);
        robotState.runState(DriveStateMachine.State.FORWARDS, 30, 0.5);
        robotState.runState(DriveStateMachine.State.TURNLEFT, 90, 0.5);
        robotState.runState(DriveStateMachine.State.FORWARDS, 30, 0.5);
        robotState.runState(DriveStateMachine.State.TURNLEFT, 90, 0.5);
        robotState.runState(DriveStateMachine.State.FORWARDS, 30, 0.5);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

}
