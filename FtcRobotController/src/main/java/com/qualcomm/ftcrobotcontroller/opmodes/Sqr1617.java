package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by LockonS on 9/6/16.
 */
public class Sqr1617 extends OpMode {

    DcMotor motR, motL;
    FWD fwd;
    ButtonState controller1, controller2;

    public void init() {
        setupMotors();

        try {
            fwd = new FWD(motL, motR);
            fwd.setEnableAcc(false);
            telemetry.addData("YES", "fwd created successfully");
        } catch (Exception e) {
            telemetry.addData("NO", e.toString());
        }

        controller1 = new ButtonState();
        controller2 = new ButtonState();
    }

    public void loop(){
        controller1.update(gamepad1);
        controller2.update(gamepad2);

        double lStick = (double)gamepad1.left_stick_y;
        double rStick = (double)gamepad1.right_stick_y;

        fwd.setPower(lStick, rStick);
        telemetry.addData("Left Stick", lStick);
        telemetry.addData("Right Stick", rStick);

        controller1.pushButtonHistory();
        controller2.pushButtonHistory();
    }

    void setupMotors(){
        try {
            motL = hardwareMap.dcMotor.get("l");
            telemetry.addData("YES", "Left Motor get successful");
        }catch (Exception e){
            telemetry.addData("NO", e.toString());
        }
        try {
            motR = hardwareMap.dcMotor.get("r");
            telemetry.addData("YES", "Right Motor get successful");
        }catch (Exception e){
            telemetry.addData("NO", e.toString());
        }
    }
}
