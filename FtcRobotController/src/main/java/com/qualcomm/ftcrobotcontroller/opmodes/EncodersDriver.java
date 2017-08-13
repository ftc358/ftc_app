package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Timer;


/**
 * Created by David Hou on 01/14/16.
 */

/*
 * @Todo
 * LEGEND: <Feature> [completed][Tested][Interfaced]
 *
 */

public class EncodersDriver extends OpMode {

    DcMotor mot;
    public int val;

    //Wheel Data
    double diameterIn = 6;
    double pulses = 520;
    double circum;
    double pulsesPerDegree;
    double degreesPerPulse;


    @Override
    public void init() {
        double circum = diameterIn * Math.PI;
        double pulsesPerDegree = 520/360;
        double degreesPerPulse = 360/520;
    }

    @Override
    public void loop() {
    }

    public int getValPulses() {
        return val;
    }

    public double getValAngle() {
        return val * degreesPerPulse;
    }

    public double getValInches() {
        double d;
        d = val * degreesPerPulse;
        d = d / 360;
        return d * circum;
    }

}
