package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name="Cwab 1.0", group="Iterative1 OpMode")

public class CWAB extends OpMode
{
    // Declare OpMode members.
   private Servo armServo = null;
   private Servo leftServo = null;
   private Servo rightServo = null;
   private DcMotor leftfront = null;
   private DcMotor rightfront = null;
   private DcMotor leftrear = null;
   private DcMotor rightrear = null;
   
   private boolean buttonPressed = false;
   
   private double armServoPos = .0;   
   private double rightServoPos = .0; 
   private double leftServoPos = .0; //

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        
        telemetry.addData("Status", "Initializing C.W.A.B");
        leftfront = hardwareMap.get(DcMotor.class, "leftfront");
        rightfront = hardwareMap.get(DcMotor.class, "rightfront");
        leftrear = hardwareMap.get(DcMotor.class, "leftrear");
        rightrear = hardwareMap.get(DcMotor.class, "rightrear");
        armServo = hardwareMap.get(Servo.class, "armservo");
        armServo.setPosition(armServoPos);
        leftServo = hardwareMap.get(Servo.class, "leftclawservo");
        leftServo.setPosition(leftServoPos);
        rightServo = hardwareMap.get(Servo.class, "rightclawservo");
        rightServo.setPosition(rightServoPos);
        
        

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialization of CW.AB Complete");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }
    
    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if(gamepad1.a){
            armServo.setPosition(0.65);
            buttonPressed = true;
        } else if(gamepad1.b){
            armServo.setPosition(0.55);
            buttonPressed = true;
        } else if(gamepad1.y){
            armServo.setPosition(0.45);
            buttonPressed = true;
        } else if(gamepad1.x){
            armServo.setPosition(0.35);
            buttonPressed = false;
            
        }
            
      //  telemetry.addData("Claw Servo Position:", "%.2f", clawPos);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}