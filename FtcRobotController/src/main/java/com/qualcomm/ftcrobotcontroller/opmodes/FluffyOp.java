package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jamesliu on 11/30/15.
 */
public class FluffyOp extends OpMode{

    public FluffyOp() {

    }

    DcMotor motL,motR, motD, motA;
    Servo dumpServo, flapServo, swayServoL, swayServoR;

    FWD fwd;
    ClimberDump dumpCtrl;
    TapeArm tapeArm;

    ButtonState controller1;
    ButtonState controller2;

    double flapPos;
    static double swayMin = 0.02, swayMax = 0.78;

    @Override
    public void init() {

        setupMotors();
        setupTapeMotors();

        try{
            fwd = new FWD(motL,motR);
            fwd.setEnableAcc(false);
            telemetry.addData("GGWP", "fwd created");
        }catch (Exception e){
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
            tapeArm = new TapeArm(motD, motA);
            tapeArm.setExponent(4.5);
            tapeArm.setEnableAcc(false);
            telemetry.addData("GGWP", "DumpCtrl Established");
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
            swayServoR = hardwareMap.servo.get("swayR");
            swayServoL = hardwareMap.servo.get("swayL");
            telemetry.addData("GGWP", "SWAY SERVO");
        }catch (Exception e) {
            telemetry.addData("ERROR",e.toString());
        }

        controller1 = new ButtonState();
        controller2 = new ButtonState();

        flapPos = 0.2;

        double swayDefaultPos = 0;

        try {
            dumpServo.setPosition(0.99);
            telemetry.addData("done", "dumpServo.setPosition(0.99)");
            swayServoL.setDirection(Servo.Direction.REVERSE);
            telemetry.addData("done", "swayServoL.setDirection(Servo.Direction.REVERSE)");
            swayServoL.setPosition(swayDefaultPos);
            telemetry.addData("done", "swayServoL.setPosition(swayDefaultPos)");
            swayServoR.setPosition(swayDefaultPos);
            telemetry.addData("done", "swayServoR.setPosition(swayDefaultPos)");
        }catch (Exception e){
            telemetry.addData("u dun goofed","servo problemo");
        }
    }
    double tapeVel;

    @Override
    public void loop() {
        controller1.update(gamepad1);
        controller2.update(gamepad2);

        double rStick = (double)gamepad1.right_stick_y;
        double lStick = (double)gamepad1.left_stick_y;

        double rStick2 = (double)gamepad2.right_stick_y;
        double lStick2 = (double)gamepad2.left_stick_y;

        double rTrig2 = (double)gamepad2.right_trigger;
        double lTrig2 = (double)gamepad2.left_trigger;
        rTrig2=(rTrig2+1.0)/2.0;
        lTrig2=(lTrig2+1.0)/2.0;
        rStick2=(rStick2+1.0)/2.0;
        lStick2/=1.2;

        //Writing the throttle values to the motors.
        fwd.setPower(lStick, rStick);
        telemetry.addData("LftStick: ", lStick);
        telemetry.addData("RhtStick: ", rStick);
        tapeVel=0.0;
        if(controller2.y){
            tapeVel=1.0;
        }else if(controller2.x){
            tapeVel=-1.0;
        }

        tapeArm.setPower(tapeVel,-lStick2);

        telemetry.addData("LftStick2: ", lStick2);
        telemetry.addData("RhtStick2: " , rStick2);
        telemetry.addData("LftTrigger2: ", lTrig2 );
        telemetry.addData("RhtTrigger2: " , rTrig2);

        if(controller1.x_press()) {
            fwd.invertDirections();
        }

        if(controller1.b_press()) {
            dumpCtrl.dumpClimbers();
        }

        if(controller2.b_press()){
            ToggleServoExtreme(swayServoR, swayMin, swayMax);
        }

        if(controller2.a_press()){
            ToggleServoExtreme(swayServoL, swayMin, swayMax);
        }

        if(controller2.left_bumper_press()){
            if(flapPos <= 0.9) {
                flapPos += 0.1;
            }
        }
        if(controller2.right_bumper_press()){
            if(flapPos >= 0.1) {
                flapPos -= 0.1;
            }
        }

        flapServo.setPosition(flapPos);

        controller1.pushButtonHistory();
        controller2.pushButtonHistory();

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

    public void setupTapeMotors(){
        try {
            motD=hardwareMap.dcMotor.get("d");
            telemetry.addData("GGWP", "MOTOR D");
        }catch (Exception e){
            telemetry.addData("ERROR",e.toString());
        }
        try {
            motA=hardwareMap.dcMotor.get("a");
            telemetry.addData("GGWP", "MOTOR A");
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