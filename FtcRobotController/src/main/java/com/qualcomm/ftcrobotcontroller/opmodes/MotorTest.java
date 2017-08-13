/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import java.lang.String;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class MotorTest extends OpMode {

	/*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
    // TETRIX VALUES.

    //Defining Motor Variables to use:
    //L2,R2 = Left, Right Triggers
    //L1,R1 = Left, Right Bumpers
    DcMotor motorL2;
    DcMotor motorL1;
    DcMotor motorR1;
    DcMotor motorR2;


    /**
     * Constructor
     */

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the Prototypenbot we assume the following,
		 *   There are four motors "motor_1" through "motor_4"
		 *   "motor_1" and "motor_3" are on the left side of the bot.
		 *   "motor_2" and "motor_4" are on the right side of the bot.
		 */

        //motorL2 = hardwareMap.dcMotor.get("motor_1");
        //motorL1 = hardwareMap.dcMotor.get("motor_2");
        //motorR1 = hardwareMap.dcMotor.get("motor_3");
        //motorR2 = hardwareMap.dcMotor.get("motor_4");
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        //booleans for whether each are activated
        boolean OnL2 = false;
        boolean OnL1 = false;
        boolean OnR1 = false;
        boolean OnR2 = false;

        //Telemetry for Motor Activations
        int TelL2 = 0;
        int TelL1 = 0;
        int TelR1 = 0;
        int TelR2 = 0;


        //Mapping throttle to Left Stick
        float throttle = gamepad1.left_stick_y;


        //CONTROLS: activating and mapping motors with Triggers and Bumpers
        //Left Trigger -> L2
        if(gamepad1.left_trigger != 0){
            OnL2 = true;
            TelL2 = 1;
            motorL2 = hardwareMap.dcMotor.get("motor_1");
        }

        //Left Bumper -> L1
        if(gamepad1.left_bumper){
            OnL1 = true;
            TelL1 = 1;
            motorL1 = hardwareMap.dcMotor.get("motor_2");
        }

        //Right Trigger
        if(gamepad1.right_trigger != 0){
            OnR2 = true;
            TelR2 = 1;
            motorR2 = hardwareMap.dcMotor.get("motor_4");
        }

        //Right Bumper
        if(gamepad1.left_bumper){
            OnR1 = true;
            TelR2 = 1;
            motorR1 = hardwareMap.dcMotor.get("motor_3");
        }


        if(throttle != 0) {
            //Set motor power with throttle if the motor is activated
            double scaledThrottle = scaleInput((double) throttle);
            if(OnL2) {
                motorL2.setPower(scaledThrottle);
            }
            if(OnL1) {
                motorL1.setPower(scaledThrottle);
            }
            if(OnR1) {
                motorR1.setPower(scaledThrottle);
            }
            if(OnR2) {
                motorR2.setPower(scaledThrottle);
            }
        }else {
            //Set power of all activated motors to 0
            if(OnL2) {
                motorL2.setPower(0);
            }
            if(OnL1) {
                motorL1.setPower(0);
            }
            if(OnR1) {
                motorR1.setPower(0);
            }
            if(OnR2) {
                motorR2.setPower(0);
            }
        }

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        //TELEMETRY
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("joystick raw","left: "+String.format("%.2f",throttle));
        telemetry.addData("Motor Status","(1, 2, 3, 4): "+String.format("%2d %2d %2d %2d", TelL2, TelL1, TelR1, TelR2));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
