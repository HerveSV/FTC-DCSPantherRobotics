package org.firstinspires.ftc.teamA;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import helperClassesA.AutonOpMode;
import helperClassesA.DriveStateMachine;

@Autonomous
public class AutonStraightPath extends AutonOpMode {

    @Override
    public void runOpMode() throws InterruptedException
    {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        initRevImu();
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        runState(DriveStateMachine.State.FORWARDS, 500, 0.5);
        telemetry.addLine("Forwards success");
        telemetry.update();
        sleep(5000);
        runState(DriveStateMachine.State.BACKWARDS, 500, 0.5);
        telemetry.addLine("Backwards success");
        telemetry.update();
    }


}
