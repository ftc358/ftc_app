package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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

public class FlapDrive extends OpMode {

    public FlapDrive(Servo servoo) {
        flapServo=servoo;
        telemetry.addData("DumpDrive", "SETUP");
    }

    Servo flapServo;
    private Timer dumpTimer;

    @Override
    public void init() {
    }

    @Override
    public void loop() {
    }

    public void dropFlap(){
        flapServo.setPosition(1.0);
    }

    public void raiseFlap(){

    }
}
