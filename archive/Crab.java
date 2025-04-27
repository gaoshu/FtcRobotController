
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Crab 1.0", group="Iterative1 OpMode")

public class Crab extends OpMode
{
    // Declare OpMode members.
    private Servo armServo = null;
    private Servo leftServo = null;
    private Servo rightServo = null;
    private DcMotor leftfront = null;
    private DcMotor leftrear = null;
    private DcMotor rightfront = null;
    private DcMotor rightrear = null;
    
    private boolean singlePlayer = false;
    private boolean buttonPressed = false;
    private double armServoPos = 0.7;
    private double leftServoPos = 0.0;
    private double rightServoPos = 0.0;

    private void slowMotion(double startPos, double endPos){
        int steps = (int)((endPos - startPos) / 0.01);
        try{
            for(int i = 0; i < steps; ++i) {
                armServo.setPosition(startPos + i * 0.01);
                Thread.sleep(i*3); //// Increase the magic number to make it slower
            }
            armServo.setPosition(endPos);
        } catch (Exception e) {
            // do something
        }
    }

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        // Initialize the hardware variables. Note that the strings used here as parameters 
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftfront = hardwareMap.get(DcMotor.class, "leftfront");
        leftrear = hardwareMap.get(DcMotor.class, "leftrear");
        rightfront = hardwareMap.get(DcMotor.class, "rightfront");
        rightrear = hardwareMap.get(DcMotor.class, "rightrear");
        
        armServo = hardwareMap.get(Servo.class, "armservo");
        armServo.setPosition(armServoPos);
        leftServo = hardwareMap.get(Servo.class, "leftclawservo");
        leftServo.setPosition(leftServoPos);
        rightServo = hardwareMap.get(Servo.class, "rightclawservo");
        rightServo.setDirection(Servo.Direction.REVERSE);
        rightServo.setPosition(rightServoPos);
        

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialization Complete");
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
        // Switch between double players and single player
        if(gamepad1.left_bumper && gamepad1.right_bumper) {
            singlePlayer = !singlePlayer;
        }
        
        if(gamepad1.a) {
            armServoPos = .7; // lowest allowed position
            //armServo.setPosition(armServoPos);
            slowMotion(armServo.getPosition(), armServoPos);
            buttonPressed = true;
        } else if(gamepad1.b) {
            armServoPos = .6;
            //armServo.setPosition(armServoPos);
            slowMotion(armServo.getPosition(), armServoPos);
            buttonPressed = true;
        } else if(gamepad1.y) {
            armServoPos = .45;
            armServo.setPosition(armServoPos);
            //slowMotion(armServo.getPosition(), armServoPos);
            buttonPressed = true;
        } else if(gamepad1.x) {
            armServoPos = .35; // highest allowed position
            armServo.setPosition(armServoPos);
           // slowMotion(armServo.getPosition(), armServoPos);
            buttonPressed = false;
        } else if(!buttonPressed){
            if(gamepad1.right_stick_y < 0) {
                armServoPos += .0008;
                if(armServoPos > .7) armServoPos = .7;
            } else if(gamepad1.right_stick_y > 0) {
                armServoPos -= .0006;
                if(armServoPos < .35) armServoPos = .35;
            }
            armServo.setPosition(armServoPos);
        }
        
        double clawPos = gamepad1.right_trigger > .45 ? .45 : gamepad1.right_trigger;
        leftServo.setPosition(clawPos);
        rightServo.setPosition(clawPos);
        
        double turnPower = singlePlayer ? gamepad1.left_stick_x : gamepad2.left_stick_x;
        double tgtPower = singlePlayer ? gamepad1.left_stick_y :gamepad2.left_stick_y;
        leftfront.setPower(tgtPower);
        leftrear.setPower(tgtPower);
        // Remember: left side and right side motors rotate in opposite directions!
        rightfront.setPower(-tgtPower);
        rightrear.setPower(-tgtPower);
        if (tgtPower < 0) {
          leftfront.setPower(-turnPower);
          leftrear.setPower(-turnPower);
          rightfront.setPower(-turnPower);
          rightrear.setPower(-turnPower);
        } else {
          leftfront.setPower(turnPower);
          leftrear.setPower(turnPower);
          rightfront.setPower(turnPower);
          rightrear.setPower(turnPower);
        }
        // Code below adds power values to DS screen
        telemetry.addData("Single Player", singlePlayer);
        telemetry.addData("Target Power", tgtPower);
        telemetry.addData("Motor Power LF", leftfront.getPower());
        telemetry.addData("Motor Power RF", rightfront.getPower());
        
        telemetry.addData("Left Stick Y:", "%.2f", gamepad1.left_stick_y);
        telemetry.addData("Arm Servo Position:", "%.2f", armServoPos);
        telemetry.addData("Claw Servo Position:", "%.2f", clawPos);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
