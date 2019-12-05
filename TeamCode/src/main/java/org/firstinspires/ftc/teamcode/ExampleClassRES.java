package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class ExampleClassRES extends LinearOpMode {


    private DcMotor motor; //motor variable used to control an actual motor
    private Servo servo; //servo variable used to control an actual servo

    @Override //Have this in front of the runOpMode method
    public void runOpMode() //This is what gets run when you press INIT on the phone
    {
        motor = hardwareMap.get(DcMotor.class, "testMotor");
        //the motor variable is mapped to the physical one using it's name on the phone config

        servo = hardwareMap.get(Servo.class, "testServo");
        //the servo variable is mapped to the physical one using it's name on the phone config


        waitForStart();
        //Wait till the final run button is pressed

        while(opModeIsActive()) //game loop, will run until program is closed
        {
            double motorPower = this.gamepad1.right_stick_y;
            //variable equal to joystick value on controller (range of -1 to 1)

            boolean moveServo180 = this.gamepad1.right_bumper;
            //if the right bumper is pressed, the variable is true, if not - false;

            motor.setPower(Range.clip(-1 , 1, motorPower));
            //motor is given power value: a negative power means backwards movement, 0 means stop, positive means forwards


            if(moveServo180)
            {
                servo.setPosition(1);
                //set value is a fraction of the servo's full rotation, i.e. for an 180 degree servo, 0.5 = 90 degrees
            }
            else
            {
                servo.setPosition(0);
                //moves back to starting position
            }


            telemetry.addData("Motor Power: ", motorPower);
            telemetry.addData("Servo in position: ", moveServo180);
            telemetry.addLine("Hey, its a line");
            //telemetry data to be sent to the driver phone on telemetry.update

            telemetry.update();
            //updates to send telemetry data to driver phone

            idle(); //cleanup routine at the end of every iteration
        }

    }

}
