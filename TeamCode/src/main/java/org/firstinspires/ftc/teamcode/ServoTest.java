
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ServoTest", group="Iterative OpMode")

public class ServoTest extends OpMode
{
    // Declare OpMode members.
    private Servo servo = null;             // servo object
    private boolean buttonPressed = false;  // On gamepad buttons override sticks
    private double servoPos = .0;           // Current servo position, controlled by gamepad
    private double lastPos = .0;            // Servo position in last loop

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        // "testservo" is the servo name in the configuration on the driver hub.
        // It can be "armservo", "handservo_left", etc.
        servo  = hardwareMap.get(Servo.class, "testservo");
        servo.setPosition(servoPos);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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
        // Buttons A, B, Y, X set servo to preset positions
        // X sets servo position to 0, essentially reset the servo position,
        // and reset 'buttonPressed' variable, so the stick can take control.
        if(gamepad1.a) {
            servoPos = .75;
            servo.setPosition(servoPos);
            buttonPressed = true;
        } else if(gamepad1.b) {
            servoPos = .5;
            servo.setPosition(servoPos);
            buttonPressed = true;
        } else if(gamepad1.y) {
            servoPos = .25;
            servo.setPosition(servoPos);
            buttonPressed = true;
        } else if(gamepad1.x) {
            servoPos = .0;
            servo.setPosition(servoPos);
            buttonPressed = false;
        } else if(!buttonPressed){
            servoPos = -gamepad1.left_stick_y;
            if(servoPos > 0 && servoPos > lastPos) {
                servo.setPosition(servoPos);
                lastPos = servoPos;
            } else if(servoPos < 0 && servoPos < lastPos) {
                servo.setPosition(1+servoPos);
                lastPos = servoPos;
            }

        } /*else if(!buttonPressed) {
            servo.setPosition(gamepad1.right_trigger);
        }*/
        telemetry.addData("Left Stick Y:", "%.2f", gamepad1.left_stick_y);
        telemetry.addData("Servo Postion:", "%.2f", servoPos);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        servo.setPosition(0.7); // This doesn't work?
    }

}
