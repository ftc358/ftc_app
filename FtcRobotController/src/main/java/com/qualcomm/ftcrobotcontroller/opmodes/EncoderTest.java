package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by jamesliu on 11/30/15.
 */
public class EncoderTest extends OpMode{

    public EncoderTest() {
    }

    DcMotor motL,motR, motD, motA;

    FWD fwd;

    ButtonState controller1;
    ButtonState controller2;

    double inchesTraveledL;
    double inchesTraveledR;

    @Override
    public void init() {

        setupMotors();

        try{
            fwd = new FWD(motL,motR);
            fwd.setEnableAcc(false);
            telemetry.addData("GGWP", "fwd created");
        }catch (Exception e){
            telemetry.addData("ERROR",e.toString());
        }
        controller1 = new ButtonState();
        controller2 = new ButtonState();

    }

    @Override
    public void loop() {
        controller1.update(gamepad1);
        controller2.update(gamepad2);

        double rStick = (double)gamepad1.right_stick_y;
        double lStick = (double)gamepad1.left_stick_y;

        double rStick2 = (double)gamepad2.right_stick_y;
        double lStick2 = (double)gamepad2.left_stick_y;

        //Writing the throttle values to the motors.
        fwd.setPower(lStick, rStick);
        telemetry.addData("LftStick: ", lStick + "RhtStick: " + rStick);
        telemetry.addData("LftStick2: ", lStick2 + "RhtStick2: " + rStick2);
        telemetry.addData("lEnc", motL.getCurrentPosition());
        telemetry.addData("rEnc: ", motR.getCurrentPosition());

        if(controller1.x_press()) {
            fwd.invertDirections();
        }

        controller1.pushButtonHistory();
        controller2.pushButtonHistory();

        if(gamepad1.a){
            inchesTraveledL = fwd.getValInches(motL);
            inchesTraveledR = fwd.getValInches(motR);
        }

        telemetry.addData("LInTraveled", inchesTraveledL + "  RInTraveled" + inchesTraveledR);

    }


    public void setupMotors(){
        try {
            motL=hardwareMap.dcMotor.get("l");
            telemetry.addData("GGWP", "MOTOR L");
        }catch (Exception e){
            telemetry.addData("ERROR",e.toString());
        }
        try {
            motR=hardwareMap.dcMotor.get("r");
            telemetry.addData("GGWP", "MOTOR R");
        }catch (Exception e){
            telemetry.addData("ERROR",e.toString());
        }
    }

}