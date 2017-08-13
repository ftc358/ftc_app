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
//import com.qualcomm.ftcrobotcontroller.opmodes.FWD;
import com.qualcomm.ftcrobotcontroller.opmodes.ArmDrive;
import com.qualcomm.ftcrobotcontroller.opmodes.ClimberDump;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class ArmAndDumpTest extends OpMode {

	/*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
	// TETRIX VALUES.
	final static double ARM_MIN_RANGE  = 0.01;
	final static double ARM_MAX_RANGE  = 0.99;

	FWD fwd;
	ClimberDump dumpCtrl;
	//ArmDrive armDriver;

	DcMotor motOne,motTwo;
	Servo dumpServo,tiltServo,flapServo;

	/**
	 * Constructor
	 */
	public ArmAndDumpTest() {
		super();
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

		/*try {
			motOne=hardwareMap.dcMotor.get("l");
			telemetry.addData("GGWP", "MOTOR L");
		}catch (Exception e){
			telemetry.addData("ERROR",e.toString());
		}
		try {
			motTwo=hardwareMap.dcMotor.get("r");
			telemetry.addData("GGWP", "MOTOR R");
		}catch (Exception e){
			telemetry.addData("ERROR",e.toString());
		}
		try {
			tiltServo=hardwareMap.servo.get("tilt");
			telemetry.addData("GGWP", "MOTOR R");
		}catch (Exception e){
			telemetry.addData("ERROR",e.toString());
		}
		try {
			armDriver = new ArmDrive(motOne, motTwo, tiltServo);
			telemetry.addData("GGWP", "ArmDriver Established");
		}catch (Exception e){
			telemetry.addData("ERROR",e.toString());
		}*/

		try {
			dumpServo = hardwareMap.servo.get("dump");
			telemetry.addData("GGWP", "DUMP SERVO");
		}catch (Exception e) {
			telemetry.addData("ERROR",e.toString());
		}
		try {
			dumpCtrl = new ClimberDump(dumpServo);

			telemetry.addData("GGWP", "DumpCtrl Established");
		}catch (Exception e){
			telemetry.addData("ERROR",e.toString());
		}

	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		//Taking in input from Joystick and clipping between -1 and 1.
		float rStick = gamepad1.right_stick_y;
		float lStick = gamepad1.left_stick_y;

		if(gamepad2.b){
			dumpCtrl.dumpClimbers();
		}

		if(gamepad2.left_bumper){
			flapServo.setPosition(0.1);
		}else if(gamepad2.right_bumper){
			flapServo.setPosition(1.0);
		}


		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");

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
