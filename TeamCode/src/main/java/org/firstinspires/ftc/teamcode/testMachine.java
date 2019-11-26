package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class testMachine {


   private DcMotor motor;
   private final LinearOpMode m_opMode;

   public testMachine(LinearOpMode op)
   {
       m_opMode = op;
   }

    public void run()
    {
        while(m_opMode.opModeIsActive())
        {
            motor = m_opMode.hardwareMap.get(DcMotor.class, "testMotor");
            m_opMode.telemetry.addLine("It's working");
            m_opMode.telemetry.update();
        }
    }
}
