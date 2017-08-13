package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
/**
 * Created by jamesliu on 12/3/15.
 */
public class Fluffynomous extends LinearOpMode {

    DcMotor motL, motR;
    FWD fwd;

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

        try{
            fwd = new FWD(motL,motR);
            telemetry.addData("GGWP", "fwd created");
        }catch (Exception e){
            telemetry.addData("ERROR",e.toString());
            telemetry.addData("SHIT","FWD not initiated! OpMode unrunnable!!!");
        }

        waitForStart();

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////// PROCEDURE /////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

//      telemetry.addData("encoder", fwd.getMotFR().getCurrentPosition());
//      fwd.testEncoders(50);
        //fwd.goCmBySpeed(100); //this used to work a little
//      telemetry.addData("encoder2", fwd.getMotFR().getCurrentPosition());

    }
}