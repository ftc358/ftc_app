package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by David Hou on 12/03/15.
 */

/*
 * @Todo
 * LEGEND: <Feature> [completed][Tested][Interfaced]
 * <Extend Arm Function>  [X][ ][ ]
 * <Stop Arm Function>    [X][ ][ ]
 * <Retract Arm Function> [X][ ][ ]
 */

public class ArmDrive extends OpMode {

    public ArmDrive(DcMotor motOne, DcMotor motTwo, Servo tServo) {
        mot1=motOne;
        mot2=motTwo;
        mot2.setDirection(DcMotor.Direction.REVERSE);
        tiltServo=tServo;
        DcMotor[] motorList = {mot1, mot2};
        telemetry.addData("ArmDrive", "SETUP");
    }
    DcMotor mot1, mot2;
    DcMotor[] motorList;
    Servo tiltServo;
    private double motSpeed = 0.5;
    private Timer armTimer;

    @Override
    public void init() {
    }

    @Override
    public void loop() {
    }

    public void ExtendArm() {
        for (DcMotor m : motorList) {
            m.setPower(motSpeed);
        }

        armTimer = new Timer("armTimer",true);
        armTimer.schedule(new motorStopTask(), 2);
    }

    public void RetractArm() {
        for (DcMotor m : motorList) {
            m.setPower(-motSpeed);
        }

        armTimer = new Timer("armTimer",true);
        armTimer.schedule(new motorStopTask(), 2300);
    }

    public void TiltArm(Servo.Direction dir){
        tiltServo.setDirection(dir);
        tiltServo.setPosition(0.95);
    }

    public void ControlExtension(boolean down){
         if(!down) {
             for (DcMotor m : motorList) {
                 m.setPower(motSpeed);
             }
         }else{
             for (DcMotor m : motorList) {
                 m.setPower(-motSpeed);
             }
         }
    }

    private class motorStopTask extends TimerTask {
        @Override
        public void run() {
            for (DcMotor m : motorList) {
                m.setPower(0.0);
            }
        }
    }
}