package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by jamesliu on 11/16/15.
 */

/**
 * TODO
 * LEGEND: <Feature> [Completed][Tested][Interfaced]
 *
 * Motor takeover                   √√√
 * Exponential                      √√√
 * Acceleration                     √√√
 * Asynchronous speed interpolation √-√
 * Go by encoder                    √-√ <!--Screw encoders-->
 * Turn by angle                    -xx
 * Accelerometer cross-check        xxx
 * Gyro for turn                    xxx
 * Magnetometer for turn            xxx
 */

public class TapeArm extends OpMode {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// START OF USER-SERVICEABLE CODE ///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private double exponent =3.5; //curviness of exponential curve
    private int speedUpdatePeriod = 33; //in milliseconds
    private double rawAcceleration = 0.1; //delta power per period
    private double wheelDiameter = 17.02; //in centimeters
    private double vehicleWidth = 50.0; //in centimeters
    private double MaxSpeedForward = 183; //forward driving speed at 100% power, in cm per second
    private double MaxSpeedPivot = 259; //angular speed at 100% power, in degrees per second
//    private int encoderResolution = 1024; //encoder ticks per rotation

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////// END OF USER-SERVICEABLE CODE ////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public TapeArm(DcMotor Motd, DcMotor mota) { //distance motor, angle motor
        motD=Motd;
        motA=mota;
        //telemetry.addData("TPRM", "SETUP");
        setupDefaults();
    }

    private boolean enableExp;

    private boolean enableAcc;
    DcMotor motD,motA;
    private double speedD=0.0,speedA=0.0,targetD=0.0,targetA=0.0;
    private Timer speedUpdater,runStopper;

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }

    private void setupDefaults() {
        enableExp=true;
        motD.setDirection(DcMotor.Direction.FORWARD);
        motA.setDirection(DcMotor.Direction.FORWARD);
        speedUpdater = new Timer("speedUpdater",true);
        speedUpdater.scheduleAtFixedRate(new speedUpdateTask(), 0, speedUpdatePeriod);
//        motD.setMode(DcMotorController.RunMode.RESET_ENCODERS);
//        motA.setMode(DcMotorController.RunMode.RESET_ENCODERS);
//        motD.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
//        motA.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void waitMillis(int millis){
        ElapsedTime sTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        sTime.reset();
        while(sTime.time() <= millis) {

        }
    }

    public void goCmBySpeed(int cms){
        int dir=(cms<0)?-1:1;
        cms=Math.abs(cms);
        int millis = cms*1000/(int)MaxSpeedForward;
        waitMillis(millis);
        setRawPower(0,0);
    }

    public void turnDegreesBySpeed(int deg){ //clockwise is positive
        int dir=(deg<0)?-1:1;
        deg=Math.abs(deg);
        int millis = deg*1000/(int)MaxSpeedPivot;
        setRawPower(1*dir,-1*dir);
        waitMillis(millis);
        setRawPower(0, 0);
    }

    private class runStopTask extends TimerTask{

        @Override
        public void run() {
            setRawPower(0.0,0.0);
        }
    }

    public void setRawPower(double L, double R) {
        speedD=Range.clip(L, -1, 1);
        speedA=Range.clip(R, -1, 1);
        speedD /= 3.2;
        speedA /= 3.2;
        updateMotorPower();
    }

    public void setPower(double L, double R){
        if(enableExp) {
            L = exponentiate(L, exponent, 1);
            R = exponentiate(R, exponent, 1);
        }
        if(Math.abs(L)<0.01)L=0.0;
        if(Math.abs(R)<0.01)R=0.0;
        targetD=L;
        targetA=R;
        if(!enableAcc) {
            setRawPower(L, R); //setRawPower sets speedXX variables.
        }
    }

    private void updateMotorPower(){
        try {
            motD.setPower(speedD);
            telemetry.addData("motD", speedD);
            motA.setPower(speedA);
            telemetry.addData("motA", speedA);
            telemetry.addData("GGWPP", "MOTORS OPERATIONAL");
        }catch (Exception e){
            telemetry.addData("ERROR", e.toString());
        }
    }

    private double exponentiate(double input, double power, double max){
        double polarity; //separate
        if(input<0.0){
            polarity=-1.0;
        }else{
            polarity=1.0;
        }
        input = Math.abs(input);
        double res = Math.pow(input, power);
        res = res * (max);
        res = res / (Math.pow(max, power));
        res = res*polarity;
        return(res);
    }

    private class speedUpdateTask extends TimerTask{
        @Override
        public void run() {
            if(enableAcc){
                if(speedD < targetD){
                    speedD += rawAcceleration;
                    if(speedD > targetD){
                        speedD = targetD;
                    }
                }else if(speedD > targetD){
                    speedD -= rawAcceleration;
                    if(speedD < targetD){
                        speedD = targetD;
                    }
                }
                if(speedA < targetA){
                    speedA += rawAcceleration;
                    if(speedA > targetA){
                        speedA = targetA;
                    }
                }else if(speedA > targetA){
                    speedA -= rawAcceleration;
                    if(speedA < targetA){
                        speedA = targetA;
                    }
                }
            }
            updateMotorPower();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// GETTERS AND SETTERS /////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public DcMotor getMotFL() {
        return motD;
    }

    public void setMotFL(DcMotor motD) {
        this.motD = motD;
    }

    public DcMotor getMotRL() {
        return motA;
    }

    public void setMotRL(DcMotor motA) {
        this.motA = motA;
    }

    public double getExponent() {
        return exponent;
    }

    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    public int getSpeedUpdatePeriod() {
        return speedUpdatePeriod;
    }

    public void setSpeedUpdatePeriod(int speedUpdatePeriod) {
        this.speedUpdatePeriod = speedUpdatePeriod;
    }

    public double getRawAcceleration() {
        return rawAcceleration;
    }

    public void setRawAcceleration(double rawAcceleration) {
        this.rawAcceleration = rawAcceleration;
    }

    public double getVehicleWidth() {
        return vehicleWidth;
    }

    public void setVehicleWidth(double vehicleWidth) {
        this.vehicleWidth = vehicleWidth;
    }

    public double getMaxSpeedForward() {
        return MaxSpeedForward;
    }

    public void setMaxSpeedForward(double maxSpeedForward) {
        MaxSpeedForward = maxSpeedForward;
    }

    public double getMaxSpeedPivot() {
        return MaxSpeedPivot;
    }

    public void setMaxSpeedPivot(double maxSpeedPivot) {
        MaxSpeedPivot = maxSpeedPivot;
    }

    public boolean isEnableExp() {
        return enableExp;
    }

    public void setEnableExp(boolean enableExp) {
        this.enableExp = enableExp;
    }

    public boolean isEnableAcc() {
        return enableAcc;
    }

    public void setEnableAcc(boolean enableAcc) {
        this.enableAcc = enableAcc;
    }
}