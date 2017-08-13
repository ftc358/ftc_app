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

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class TwoWheelChassisBAK extends OpMode {

	/*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
	// TETRIX VALUES.
	final static double ARM_MIN_RANGE  = 0.01;
	final static double ARM_MAX_RANGE  = 0.99;

	// position of the arm servo.
	double armPosition = 0.5;

	// amount to change the arm servo position.
	double armDelta = 0.05;

	DcMotor motorRF;
	DcMotor motorLF;
	Servo arm;

	MotorCtrl motorCtrl;

	/**
	 * Constructor
	 */
	public TwoWheelChassisBAK() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */

	@Override
	public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		motorRF = hardwareMap.dcMotor.get("motorRF");
		motorLF = hardwareMap.dcMotor.get("motorLF");
		motorLF.setDirection(DcMotor.Direction.REVERSE);
		
		arm = hardwareMap.servo.get("servo_1");
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		//Taking in input from Joystick and clipping between -1 and 1.
		float rThrottle = Range.clip(gamepad1.right_stick_y, -1, 1);
		float lThrottle = Range.clip(gamepad1.left_stick_y, -1, 1);

		//Writing the throttle values to the motors.
		motorRF.setPower(rThrottle);
		motorLF.setPower(lThrottle);

		//Updating the target position of servo.
		if(gamepad1.left_bumper){
			armPosition -= armDelta;
		}else if(gamepad1.right_bumper){
			armPosition += armDelta;
		}

        //Clipping the target position and updating actual position of servo.
        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
		arm.setPosition(armPosition);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Servo Target Pos: ", armPosition);
		telemetry.addData("Servo Current Pos: ", arm.getPosition());
		telemetry.addData("LftStick: ", gamepad1.left_stick_y + "RhtStick: " + gamepad1.right_stick_y);

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}
}
