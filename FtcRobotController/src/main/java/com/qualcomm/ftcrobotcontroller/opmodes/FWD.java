package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
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

public class FWD extends OpMode {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// START OF USER SETTINGS ///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private double exponent=3.5; //curviness of exponential curve
    private int speedUpdatePeriod = 33; //in milliseconds
    private double rawAcceleration = 0.1; //delta power per period
    private double wheelDiameter = 6.5; //in inches
    private double vehicleWidth = 50.0; //in centimeters
    private double MaxSpeedForward = 183; //forward driving speed at 100% power, in cm per second
    private double MaxSpeedPivot = 259; //angular speed at 100% power, in degrees per second
    private double encoderResolution = 520; //encoder ticks per rotation

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// END OF USER SETTINGS ////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// VARIABLES //////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //basic stuff
    private boolean enableExp,enableAcc,enable4wd;
    DcMotor motFL,motFR,motRL,motRR;
    private double speedFL=0.0,speedFR=0.0,speedRL=0.0,speedRR=0.0;
    private double targetFL=0.0,targetFR=0.0,targetRL=0.0,targetRR=0.0;

    //loops
    private Timer speedUpdater,runstopper;

    //autonomous
    int offset;
    double circumference=wheelDiameter*2*Math.PI;//inch
    double tickPerDegree=encoderResolution/360.0;
    double tickPerInch=encoderResolution/circumference;
    long updatePeriod = 2; //millis

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// CORE METHODS ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }

    public FWD(DcMotor motL, DcMotor motR) {
        motFL=motL;
        motFR=motR;
        enable4wd=false;
        telemetry.addData("FWD", "SETUP");
        setupDefaults();
    }

    public FWD(boolean isAutonomous, DcMotor motL, DcMotor motR) {
        motFL=motL;
        motFR=motR;
        enable4wd=false;
        telemetry.addData("FWD", "SETUP");
        setupDefaults();
        runstopper=new Timer();
    }

    public FWD(DcMotor motfl, DcMotor motfr,DcMotor motrl, DcMotor motrr) {
        setupDefaults();
        motFL=motfl;
        motFR=motfr;
        motRL=motrl;
        motRR=motrr;
        enable4wd=true;
        telemetry.addData("FWD", "SETUP");
        setupDefaults();
        motRL.setDirection(DcMotor.Direction.REVERSE);
        motRR.setDirection(DcMotor.Direction.REVERSE);
    }

    private void setupDefaults() {
        enableExp=true;
        enableAcc=false;
        enable4wd=false;
        speedUpdater = new Timer("speedUpdater",true);
        speedUpdater.scheduleAtFixedRate(new speedUpdateTask(),0,speedUpdatePeriod);
        motFL.setDirection(DcMotor.Direction.REVERSE);
        motFR.setDirection(DcMotor.Direction.REVERSE);

        double circum = wheelDiameter * Math.PI;
        double pulsesPerDegree = 520/360;
        double degreesPerPulse = 360/520;

        motFL.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motFR.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motFL.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motFR.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }

    public void setRawPower(double FL, double FR, double RL, double RR){
        if(!enable4wd){
            telemetry.addData("uDunGüfd","Do you count 4 motors bruh?");
            return;
        }
        speedFL=Range.clip(FL, -1, 1);
        speedFR=Range.clip(FR, -1, 1);
        speedRL=Range.clip(RL, -1, 1);
        speedRR=Range.clip(RR, -1, 1);
        updateMotorPower();
    }

    public void setRawPower(double L, double R) {
        speedFL=Range.clip(L, -1, 1);
        speedFR=Range.clip(R, -1, 1);
        if(enable4wd){
            speedRL=Range.clip(L, -1, 1);
            speedRR=Range.clip(R, -1, 1);
        }
        updateMotorPower();
    }

    public void setPower(double FL, double FR, double RL, double RR){
        if(enableExp) {
            FL = exponentiate(FL, exponent, 1);
            FR = exponentiate(FR, exponent, 1);
            RL = exponentiate(RL, exponent, 1);
            RR = exponentiate(RR, exponent, 1);
        }
        targetFL=FL;
        targetFR=FR;
        targetRL=RL;
        targetRR=RR;
        if(!enableAcc) {
            setRawPower(FL, FR, RL, RR); //setRawPower sets speedXX variables.
        }
    }

    public void setPower(double L, double R){
        if(enableExp) {
            L = exponentiate(L, exponent, 1);
            R = exponentiate(R, exponent, 1);
        }
        if(Math.abs(L)<0.01)L=0.0;
        if(Math.abs(R)<0.01)R=0.0;
        targetFL=L;
        targetFR=R;
        if(!enableAcc) {
            setRawPower(L, R); //setRawPower sets speedXX variables.
        }
    }

    private void updateMotorPower(){
        try {
            motFL.setPower(speedFL);
            telemetry.addData("motFL", speedFL);
            motFR.setPower(speedFR);
            telemetry.addData("motFR", speedFR);
            if(enable4wd){
                motRL.setPower(speedRL);
                telemetry.addData("motRL", speedRL);
                motRR.setPower(speedRR);
                telemetry.addData("motRR", speedRR);
            }
            telemetry.addData("GGWPP", "MOTORS OPERATIONAL");
        }catch (Exception e){
            telemetry.addData("ERROR", e.toString());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// TELEOP ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private class speedUpdateTask extends TimerTask{
        @Override
        public void run() {
            if(enableAcc){
                if(speedFL < targetFL){
                    speedFL += rawAcceleration;
                    if(speedFL > targetFL){
                        speedFL = targetFL;
                    }
                }else if(speedFL > targetFL){
                    speedFL -= rawAcceleration;
                    if(speedFL < targetFL){
                        speedFL = targetFL;
                    }
                }
                if(speedFR < targetFR){
                    speedFR += rawAcceleration;
                    if(speedFR > targetFR){
                        speedFR = targetFR;
                    }
                }else if(speedFR > targetFR){
                    speedFR -= rawAcceleration;
                    if(speedFR < targetFR){
                        speedFR = targetFR;
                    }
                }
                if(enable4wd) {
                    if (speedRL < targetRL) {
                        speedRL += rawAcceleration;
                        if (speedRL > targetRL) {
                            speedRL = targetRL;
                        }
                    } else if (speedRL > targetRL) {
                        speedRL -= rawAcceleration;
                        if (speedRL < targetRL) {
                            speedRL = targetRL;
                        }
                    }
                    if (speedRR < targetRR) {
                        speedRR += rawAcceleration;
                        if (speedRR > targetRR) {
                            speedRR = targetRR;
                        }
                    } else if (speedRR > targetRR) {
                        speedRR -= rawAcceleration;
                        if (speedRR < targetRR) {
                            speedRR = targetRR;
                        }
                    }
                }
            }
            updateMotorPower();
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

    public void invertDirections(){
        if(motFR.getDirection()==DcMotor.Direction.FORWARD){
            motFR.setDirection(DcMotor.Direction.REVERSE);
        }else{
            motFR.setDirection(DcMotor.Direction.FORWARD);
        }
        if(motFL.getDirection()==DcMotor.Direction.FORWARD){
            motFL.setDirection(DcMotor.Direction.REVERSE);
        }else{
            motFL.setDirection(DcMotor.Direction.FORWARD);
        }
        DcMotor tempMot = motFL;
        motFL=motFR;
        motFR=tempMot;
        if(enable4wd) {
            if (motRR.getDirection() == DcMotor.Direction.FORWARD) {
                motRR.setDirection(DcMotor.Direction.REVERSE);
            } else {
                motRR.setDirection(DcMotor.Direction.FORWARD);
            }
            if (motRL.getDirection() == DcMotor.Direction.FORWARD) {
                motRL.setDirection(DcMotor.Direction.REVERSE);
            } else {
                motRL.setDirection(DcMotor.Direction.FORWARD);
            }
            tempMot=motRL;
            motRL=motRR;
            motRR=tempMot;
        }
    }

    public void drive(int ticks){

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// AUTONOMOUS //////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public class EncoderCheckTask extends TimerTask{ //stop robot after set ticks
        int targetL,targetR,initL,initR,l,r; //encoder ticks x6, sign
        boolean dir;//true for forward
        public EncoderCheckTask(int ticks){ //go a set num of ticks
            initL=motFL.getCurrentPosition();
            initR=motFR.getCurrentPosition();
            targetL=initL+ticks;
            targetR=initR+ticks;
            dir=(ticks<0) ? false : true;
        }
        @Override
        public void run(){
            l=motFL.getCurrentPosition();
            r=motFR.getCurrentPosition();
            if(dir) {
                if (l >= targetL && r >= targetR) {
                    setRawPower(0, 0);
                    this.cancel();
                } else if (l - initL > r - initR) {
                    setRawPower(speedFL - 0.01, speedFR + 0.01);
                } else if (l - initL < r - initR) {
                    setRawPower(speedFL + 0.01, speedFR - 0.01);
                } else {
                    setRawPower(speedFL + 0.01, speedFR + 0.01);
                }
            }else{
                if (l <= targetL && r <= targetR) {
                    setRawPower(0, 0);
                    this.cancel();
                } else if ( initL - l > initR - r) {
                    setRawPower(speedFL + 0.01, speedFR - 0.01);
                } else if ( initL - l < initR - r) {
                    setRawPower(speedFL - 0.01, speedFR + 0.01);
                } else {
                    setRawPower(speedFL - 0.01, speedFR - 0.01);
                }
            }
        }
    }

    public TimerTask goForward(int ticks){ //gives caller a chance to retain a reference to task created
        TimerTask goForwardTask = new EncoderCheckTask(ticks);
        runstopper.scheduleAtFixedRate(goForwardTask,0,updatePeriod);
        return goForwardTask;
    }

    public TimerTask goForward(double inches){ //gives caller a chance to retain a reference to task created
        return goForward(inches*tickPerInch);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// UTILS ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public double intocm(double in){
        return in*2.54;
    }
    public double intocm(float in){
        return in*2.54;
    }
    public double intocm(int in){
        return in*2.54;
    }

    public double cmtoin(double cm){
        return cm/2.54;
    }
    public double cmtoin(float cm){
        return cm/2.54;
    }
    public double cmtoin(int cm){
        return cm/2.54;
    }

    public int getValPulses(DcMotor motT) {
        return motT.getCurrentPosition();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// GETTERS AND SETTERS /////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public DcMotor getMotFL() {
        return motFL;
    }
    public void setMotFL(DcMotor motFL) {
        this.motFL = motFL;
    }
    public DcMotor getMotFR() {
        return motFR;
    }
    public void setMotFR(DcMotor motFR) {
        this.motFR = motFR;
    }
    public DcMotor getMotRL() {
        return motRL;
    }
    public void setMotRL(DcMotor motRL) {
        this.motRL = motRL;
    }
    public DcMotor getMotRR() {
        return motRR;
    }
    public void setMotRR(DcMotor motRR) {
        this.motRR = motRR;
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
    public boolean isEnable4wd() {
        return enable4wd;
    }
    public void setEnable4wd(boolean enable4wd) {
        this.enable4wd = enable4wd;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// GRAVEYARD //////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

//    public void blockingGoByDistance(double distance){
//        int initialTheta = motFL.getCurrentPosition();
//        int targetTheta = initialTheta+(int)(distance/((wheelDiameter*Math.PI*2.0)*(double)encoderResolution));
//        //setPower(1.0,1.0);
//        setPower(0.5,0.5);
//        while(!(speedFR==targetFR && speedFL==targetFL)){}
//        int accelerationTheta = motFL.getCurrentPosition()-initialTheta;
//        while(motFL.getCurrentPosition()<(targetTheta-accelerationTheta)){
//        }
//        setPower(0.0, 0.0);
//    }

//    public void testEncoders(int rotations){
//        setRawPower(0.7, 0.7);
//        int pos = motFR.getCurrentPosition();
//        target = pos + (50*encoderResolution);
//        runStopper = new Timer("runStopper",true);
//        runStopper.scheduleAtFixedRate(new runStopTask(), 0, 20);
//    }

//    public void testEncoders(int rotations){
//        motFL.setTargetPosition(rotations*encoderResolution);
//        motFR.setTargetPosition(rotations*encoderResolution);
//        setRawPower(0.7, 0.7);
//    }

//    private class countEncoderTask extends TimerTask{
//
//        @Override
//        public void run() {
//            motFR.setPower(((double)motFL.getCurrentPosition()/1000.0));
//        }
//    }

//    public void blockingTurnByDegrees(double degrees){
//        int initialTheta = motFL.getCurrentPosition();
//        int targetTheta = initialTheta+(int)(degrees/((wheelDiameter*Math.PI*2.0)*(double)encoderResolution));
//        setPower(1.0,-1.0);
//        while(!(speedFR==targetFR && speedFL==targetFL)){}
//        int accelerationTheta = motFL.getCurrentPosition()-initialTheta;
//        while(motFL.getCurrentPosition()<(targetTheta-accelerationTheta)){
//        }
//        setPower(0.0,0.0);
//    }

//    public double getValAngle(DcMotor motT) {
//        return motT.getCurrentPosition() * degreesPerPulse;
//    }
//
//    public double getValInches(DcMotor motT) {
//        double d;
//        d = motT.getCurrentPosition() * degreesPerPulse;
//        d = d / 360;
//        return d * circumference;
//    }

//    public void goCmBySpeed(int cms){
//        int dir=(cms<0)?-1:1;
//        cms=Math.abs(cms);
//        int millis = cms*1000/(int)MaxSpeedForward;
//        setRawPower(1*dir,1*dir);
//        waitMillis(millis);
//        setRawPower(0, 0);
//    }

//    public void turnDegreesBySpeed(int deg){ //clockwise is positive
//        int dir=(deg<0)?-1:1;
//        deg=Math.abs(deg);
//        int millis = deg*1000/(int)MaxSpeedPivot;
//        setRawPower(1*dir,-1*dir);
//        waitMillis(millis);
//        setRawPower(0,0);
//    }

//    public void waitMillis(int millis){
//        ElapsedTime sTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
//        sTime.reset();
//        while(sTime.time() <= millis) {
//
//        }
//    }

}