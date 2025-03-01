
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Model C", group="Iterative1 OpMode")

public class MotorCrab extends OpMode
{
    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;   // goBilda 5302 537.7
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.08;
    static final double     TURN_SPEED              = 0.2;

    // Declare OpMode members.
    private Servo leftServo = null;
    private Servo rightServo = null;
    private DcMotor leftfront = null;
    private DcMotor leftrear = null;
    private DcMotor rightfront = null;
    //private DcMotor rightrear = null;
    private DcMotor armMotor = null;

    private boolean singlePlayer = false;
    private boolean buttonPressed = false;

    private int armPos = 0;
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
        // The port is now used by the arm motor which inherited the name "rightrear"
        //rightrear = hardwareMap.get(DcMotor.class, "rightrear");
        armMotor = hardwareMap.get(DcMotor.class, "rightrear");

        leftServo = hardwareMap.get(Servo.class, "leftclawservo");
        leftServo.setPosition(leftServoPos);
        rightServo = hardwareMap.get(Servo.class, "rightclawservo");
        rightServo.setDirection(Servo.Direction.REVERSE);
        rightServo.setPosition(rightServoPos);

        // Arm servo is replaced by arm motor
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(armPos);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(DRIVE_SPEED);
        armMotor.setDirection(DcMotor.Direction.REVERSE);


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
            armPos = 5;
            armMotor.setTargetPosition(armPos);
            buttonPressed = true;
        } else if(gamepad1.b) {
            armPos = 30;
            armMotor.setTargetPosition(armPos);
            buttonPressed = true;
        } else if(gamepad1.y) {
            armPos = 80;
            armMotor.setTargetPosition(armPos);
            buttonPressed = true;
        } else if(gamepad1.x) {
            armPos = 100;
            armMotor.setTargetPosition(armPos);
            buttonPressed = false;
        } else if(!buttonPressed){
            double sticky = singlePlayer? gamepad1.right_stick_y : gamepad1.left_stick_y;
            if(sticky > 0) {
                armPos += 1;
                if(armPos > 150) armPos = 150;
            } else if(sticky < 0) {
                armPos -= 1;
                if(armPos < 10) armPos = 10;
            }
            armMotor.setTargetPosition(armPos);
        }

        double clawPos = gamepad1.right_trigger > .45 ? .45 : gamepad1.right_trigger;
        leftServo.setPosition(clawPos);
        rightServo.setPosition(clawPos);

        if(singlePlayer){
            leftfront.setPower(gamepad1.left_stick_y);
            leftrear.setPower(gamepad1.left_stick_y);
            // Remember: left side and right side motors rotate in opposite directions!
            rightfront.setPower(-gamepad1.left_stick_y);
            //rightrear.setPower(-gamepad1.left_stick_y);
            if (gamepad1.left_stick_y < 0) {
                leftfront.setPower(-gamepad1.left_stick_x);
                leftrear.setPower(-gamepad1.left_stick_x);
                rightfront.setPower(-gamepad1.left_stick_x);
                //rightrear.setPower(-gamepad1.left_stick_x);
            } else {
                leftfront.setPower(gamepad1.left_stick_x);
                leftrear.setPower(gamepad1.left_stick_x);
                rightfront.setPower(gamepad1.left_stick_x);
                //rightrear.setPower(gamepad1.left_stick_x);
            }
        } else {
            // Drive in tank mode
            leftfront.setPower(gamepad2.left_stick_y);
            leftrear.setPower(gamepad2.left_stick_y);
            rightfront.setPower(-gamepad2.right_stick_y);
        }
        // Code below adds power values to DS screen
        telemetry.addData("Single Player", singlePlayer);
        telemetry.addData("Target Power", gamepad1.left_stick_y);
        telemetry.addData("Motor Power LF", leftfront.getPower());
        telemetry.addData("Motor Power RF", rightfront.getPower());

        telemetry.addData("Left Stick Y:", "%.2f", gamepad1.left_stick_y);
        telemetry.addData("Arm Motor Position:", "%d", armPos);
        telemetry.addData("Claw Servo Position:", "%.2f", clawPos);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
