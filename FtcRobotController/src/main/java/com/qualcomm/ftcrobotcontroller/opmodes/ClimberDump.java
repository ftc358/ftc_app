package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by David Hou on 12/04/15.
 */

/*
 * @Todo
 * LEGEND: <Feature> [completed][Tested][Interfaced]
 * <Dump Climber Function>    [X][X][ ]
 */

public class ClimberDump extends OpMode {

    //Initialize ClimberDump Class
    public ClimberDump(Servo servoo) {
        dumpServo=servoo;
        telemetry.addData("DumpDrive", "SETUP");
    }

    Servo dumpServo;

    //Do not change
    private Timer dumpTimer;

    @Override
    public void init() {
    }

    @Override
    public void loop() {
    }

    public void dumpClimbers(){
        //Swing the Servo to dump position
        dumpServo.setPosition(1.0);
        //Begin timer for return of Servo, and call return servo task
        dumpTimer = new Timer("armTimer",true);
        dumpTimer.schedule(new servoReturnTask(), 1500);
    }

    private class servoReturnTask extends TimerTask {
        @Override
        public void run() {
//<<<<<<< Updated upstream
//=======
            //dumpServo.setDirection(Servo.Direction.REVERSE);
//>>>>>>> Stashed changes
            dumpServo.setPosition(0.0);
            telemetry.addData("DUMP", "COMPLETE");
        }
    }
}
