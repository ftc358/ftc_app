package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by David Hou on 1/20/16
 */
public class Megautonomous extends LinearOpMode {

    DcMotor motL, motR;
<<<<<<< Updated upstream
    FWD fwd;

    private Timer moveTimer;
    long delayMs = 4000;

=======
    Servo flapServo;
    FWD fwd;

    private Timer nextTimer;
    long initWaitDelay = 10000;
    long moveFwdTime = 3800;

    double flapDownPos = 0.95;
>>>>>>> Stashed changes
    double lPwr = 0.5;
    double rPwr = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////// INIT ////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        try {
            motL=hardwareMap.dcMotor.get("l");
            telemetry.addData("GGWP", "MOTOR L");
            motR=hardwareMap.dcMotor.get("r");
            telemetry.addData("GGWP", "MOTOR R");
        }catch (Exception e){
            telemetry.addData("ERROR",e.toString());
            telemetry.addData("SHIT","Motors not mapped! OpMode unrunnable!!!");
        }
<<<<<<< Updated upstream
=======
        try{
            flapServo = hardwareMap.servo.get("flap");
            telemetry.addData("GGWP", "FLAP SERVO");
        }catch (Exception e) {
            telemetry.addData("ERROR",e.toString());
        }
>>>>>>> Stashed changes

        try{
            fwd = new FWD(motL,motR);
            telemetry.addData("GGWP", "fwd created");
        }catch (Exception e){
            telemetry.addData("ERROR",e.toString());
            telemetry.addData("SHIT","FWD not initiated! OpMode unrunnable!!!");
        }

<<<<<<< Updated upstream
        telemetry.addData("NOW", "Waiting For Start...");
=======
>>>>>>> Stashed changes
        waitForStart();

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////// PROCEDURE /////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

<<<<<<< Updated upstream
        motL.setPower(lPwr);
        motR.setPower(rPwr);

        moveTimer = new Timer("moveTimer", true);
        moveTimer.schedule(new StopMovingTask(), delayMs);
=======
        //Initialize Autonomous
        motL.setPower(0);
        motR.setPower(0);
        flapServo.setPosition(flapDownPos);

        //Initial wait, then move forward.
        nextTimer = new Timer("initWaitTimer", true);
        nextTimer.schedule(new StartMovingTask(), initWaitDelay);

        //Move forward for <moveFwdTime> ms and stop again.
        nextTimer = new Timer("moveFwdTimer", true);
        nextTimer.schedule(new StopMovingTask(), moveFwdTime);
>>>>>>> Stashed changes
    }

    private class StopMovingTask extends TimerTask {
        @Override
        public void run() {
            motL.setPower(0);
            motR.setPower(0);
        }
    }
<<<<<<< Updated upstream
=======

    private class StartMovingTask extends TimerTask {
        @Override
        public void run() {
            motL.setPower(lPwr);
            motR.setPower(rPwr);
        }
    }
>>>>>>> Stashed changes
}