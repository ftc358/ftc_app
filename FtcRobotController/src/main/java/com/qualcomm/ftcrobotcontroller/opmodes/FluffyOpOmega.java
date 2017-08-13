package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jamesliu on 11/30/15.
 */
public class FluffyOpOmega extends OpMode{

    public FluffyOpOmega() {
    }

    DcMotor motL,motR;
    Servo dumpServo, flapServo, switchServo;

    FWD fwd;
    ClimberDump dumpCtrl;

    boolean lastBack=false;

    @Override
    public void init() {

        setupMotors();

        try{
            fwd = new FWD(motL,motR);

            telemetry.addData("GGWP", "fwd created");
        }catch (Exception e){
            telemetry.addData("ERROR",e.toString());
        }

        try{
            flapServo = hardwareMap.servo.get("flap");
            telemetry.addData("GGWP", "FLAP SERVO");
        }catch (Exception e) {
            telemetry.addData("ERROR",e.toString());
        }
        try{
            flapServo = hardwareMap.servo.get("switch");
            telemetry.addData("GGWP", "SWITCH SERVO");
        }catch (Exception e) {
            telemetry.addData("ERROR",e.toString());
        }

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

    @Override
    public void loop() {

        double rStick = (double)gamepad1.right_stick_y;
        double lStick = (double)gamepad1.left_stick_y;

        //Writing the throttle values to the motors.
        fwd.setPower(lStick, rStick);
        telemetry.addData("LftStick: ", lStick + "RhtStick: " + rStick);

        if(gamepad1.x && !lastBack) {
            fwd.invertDirections();
        }

        if(gamepad1.b){
            dumpCtrl.dumpClimbers();
        }

        if(gamepad1.left_stick_button){
            ToggleServoExtreme(flapServo, 0.0, 1.0);
        }
        if(gamepad1.left_stick_button){
            ToggleServoExtreme(switchServo, 0.0, 1.0);
        }

        lastBack=gamepad1.x;
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

    void ToggleServoExtreme(Servo servoToToggle, double min, double max){
        if(servoToToggle.getPosition() < min + 0.01){
            servoToToggle.setPosition(max);
        }else{
            servoToToggle.setPosition(min);
        }
    }
}