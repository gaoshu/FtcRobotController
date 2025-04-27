
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Model D", group="Iterative1 OpMode")

public class MotorCrabTest extends OpMode
{
    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;   // goBilda 5302 537.7
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.2;
    static final double     TURN_SPEED              = 0.2;
    
    // Declare OpMode members.
    private Servo leftServo = null;
    private Servo rightServo = null;
    private DcMotor leftfront = null;
    private DcMotor leftrear = null;
    private DcMotor rightfront = null;
    private DcMotor rightrear = null;
    private Servo armMotor = null;
    
    private boolean singlePlayer = false;
    private boolean buttonPressed = false;
    private double armPos = 0;
    private double leftServoPos = 0.0;
    private double rightServoPos = 0.0;

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
        armMotor = hardwareMap.get(Servo.class, "armMotor");
        //armMotor.setDirection(CRServo.Direction.REVERSE);
        
        
        leftServo = hardwareMap.get(Servo.class, "leftclawservo");
        leftServo.setPosition(leftServoPos);
        rightServo = hardwareMap.get(Servo.class, "rightclawservo");
        rightServo.setDirection(Servo.Direction.REVERSE);
        rightServo.setPosition(rightServoPos);
        
        // Arm servo is replaced by arm motor
        //armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //armMotor.setTargetPosition(armPos);
        //armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //armMotor.setPower(DRIVE_SPEED);
        
        //armMotor.setPower(.1);
        

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
            armPos = 0.0;
            armMotor.setPosition(armPos);
            buttonPressed = true;
        } else if(gamepad1.b) {
            armPos = .50;
            armMotor.setPosition(armPos);
            buttonPressed = true;
        } else if(gamepad1.y) {
            armPos = .70;
            armMotor.setPosition(armPos);
            buttonPressed = true;
        } else if(gamepad1.x) {
            armPos = .90;
            armMotor.setPosition(armPos);
            buttonPressed = false;
        } /*else if(!buttonPressed){
            if(gamepad1.right_stick_y < 0) {
                armPos += 1;
                if(armPos > 300) armPos = 300;
            } else if(gamepad1.right_stick_y > 0) {
                armPos -= 1;
                if(armPos < 50) armPos = 50;
            }
            armMotor.setTargetPosition(armPos);
        }
        */
        /*
        if(gamepad1.right_stick_y <= 0) {
            double spd = gamepad1.right_stick_y;
            spd = spd > 0.5? 0.5:spd;
            armMotor.setPower(spd);
        } else if(gamepad1.right_stick_y > 0) {
            double spd = gamepad1.right_stick_y;
            spd = spd < -0.5? -0.5:spd;
            armMotor.setPower(spd);
        } //else (gamepad1.right_stick_y = 0) { 
            //armMotor.setPower(0);
        //}
        */
        if(!buttonPressed) {
            if(gamepad1.right_stick_y < 0) {
                armPos += .0008;
                if(armPos > .7) armPos = .7;
            } else if(gamepad1.right_stick_y > 0) {
                armPos -= .0006;
                if(armPos < .35) armPos = .35;
            }
            armMotor.setPosition(armPos);
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
        //rightrear.setPower(-tgtPower);
        if (tgtPower < 0) {
          leftfront.setPower(turnPower);
          leftrear.setPower(turnPower);
          rightfront.setPower(turnPower);
          //rightrear.setPower(-turnPower);
        } else {
          leftfront.setPower(-turnPower);
          leftrear.setPower(-turnPower);
          rightfront.setPower(-turnPower);
          //rightrear.setPower(turnPower);
        }
        // Code below adds power values to DS screen
        telemetry.addData("Single Player", singlePlayer);
        telemetry.addData("Target Power", tgtPower);
        telemetry.addData("Motor Power LF", leftfront.getPower());
        telemetry.addData("Motor Power RF", rightfront.getPower());
        
        telemetry.addData("Left Stick Y:", "%.2f", gamepad1.left_stick_y);
        //telemetry.addData("Arm Motor Position:", "%d", armPos);
        telemetry.addData("Claw Servo Position:", "%.2f", clawPos);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
