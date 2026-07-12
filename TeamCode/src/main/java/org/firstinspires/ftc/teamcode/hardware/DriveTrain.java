package org.firstinspires.ftc.teamcode.hardware;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.teamcode.utils.Constants;

import java.util.List;


@Config
public class DriveTrain {
    private static DriveTrain instance = null;
    private HardwareMap hardwareMap;
    private List<LynxModule> allHubs;

    public DcMotorEx frontLeft, backLeft, frontRight, backRight;

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }




    public PIDFController velocityPIDF_BackRight;
    public PIDFController velocityPIDF_FrontRight;
    public double p_velocity = 0, i_velocity = 0, d_velocity = 0, f_velocity = 0.000348;
    public PIDController rotationPID_RightModule;
    public static double p_rotation_BIG = 0.009, i_rotation_BIG = 0.001, d_rotation_BIG = 0.0001;
    public static double p_rotation_SMOL = 0.0115, i_rotation_SMOL = 0.01, d_rotation_SMOL = 0;

    public static double currentPositionRightModule = 0;
    public static double targetPositionRightModule = 0;

    public double currentPositionBackRight = 0;
    public double currentPositionFrontRight = 0;

    public static double currentVelocityRightModule = 0;
    public static double targetVelocityBackRight = 0, targetVelocityFrontRight = 0;
    public static double currentVelocityBackRight = 0, currentVelocityFrontRight = 0;







    public PIDFController velocityPIDF_BackLeft;
    public PIDFController velocityPIDF_FrontLeft;

    public PIDController rotationPID_LeftModule;


    public static double currentPositionLeftModule = 0;
    public static double targetPositionLeftModule = 0;

    public double currentPositionBackLeft = 0;
    public double currentPositionFrontLeft = 0;

    public static double currentVelocityLeftModule = 0;
    public static double targetVelocityBackLeft = 0, targetVelocityFrontLeft = 0;
    public static double currentVelocityBackLeft = 0, currentVelocityFrontLeft = 0;



    public static double dashInput = 0;


    public void initializeHardware(final HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

    }

    public void initialize(){
        velocityPIDF_BackRight = new PIDFController(p_velocity, i_velocity, d_velocity, f_velocity);
        velocityPIDF_BackRight.reset();
        velocityPIDF_FrontRight = new PIDFController(p_velocity, i_velocity, d_velocity, f_velocity);
        velocityPIDF_FrontRight.reset();
        rotationPID_RightModule = new PIDController(p_rotation_BIG, i_rotation_BIG, d_rotation_BIG);
        rotationPID_RightModule.reset();

        currentPositionBackRight = backRight.getCurrentPosition();
        currentPositionFrontRight = frontRight.getCurrentPosition();
        currentPositionRightModule = 0;
        targetPositionRightModule = 0;

        currentVelocityBackRight = backRight.getVelocity();
        currentVelocityFrontRight = frontRight.getVelocity();
        targetVelocityBackRight = 0;
        targetVelocityFrontRight = 0;




        velocityPIDF_BackLeft = new PIDFController(p_velocity, i_velocity, d_velocity, f_velocity);
        velocityPIDF_BackLeft.reset();
        velocityPIDF_FrontLeft = new PIDFController(p_velocity, i_velocity, d_velocity, f_velocity);
        velocityPIDF_FrontLeft.reset();
        rotationPID_LeftModule = new PIDController(p_rotation_BIG, i_rotation_BIG, d_rotation_BIG);
        rotationPID_LeftModule.reset();

        currentPositionBackLeft = backLeft.getCurrentPosition();
        currentPositionFrontLeft = frontLeft.getCurrentPosition();
        currentPositionLeftModule = 0;
        targetPositionLeftModule = 0;

        currentVelocityBackLeft = backLeft.getVelocity();
        currentVelocityFrontLeft = frontLeft.getVelocity();
        targetVelocityBackLeft = 0;
        targetVelocityFrontLeft = 0;
    }


    public void loopRight(double magnitude, double angle){
        targetPositionRightModule = angle * Constants.DEGREES_TO_TICKS_CONSTANT_RIGHT;
        currentPositionBackRight = backRight.getCurrentPosition();
        currentPositionFrontRight = frontRight.getCurrentPosition();
        currentPositionRightModule = (currentPositionBackRight - currentPositionFrontRight);


        if(Math.abs(currentPositionRightModule - targetPositionRightModule) <= Math.PI/6)
            rotationPID_RightModule.setPID(p_rotation_SMOL, i_rotation_SMOL, d_rotation_SMOL);
        else rotationPID_RightModule.setPID(p_rotation_BIG, i_rotation_BIG, d_rotation_BIG);

        double velocityDifference = rotationPID_RightModule.calculate(currentPositionRightModule, targetPositionRightModule) * Constants.MAX_VELOCITY;



        targetVelocityBackRight = magnitude * Constants.MAX_VELOCITY + velocityDifference;
        targetVelocityFrontRight = magnitude * Constants.MAX_VELOCITY - velocityDifference;


        currentVelocityBackRight = backRight.getVelocity();
        velocityPIDF_BackRight.setPIDF(p_velocity, i_velocity, d_velocity, f_velocity);
        double powerBackRight = velocityPIDF_BackRight.calculate(currentVelocityBackRight, targetVelocityBackRight);

        currentVelocityFrontRight = frontRight.getVelocity();
        velocityPIDF_FrontRight.setPIDF(p_velocity, i_velocity, d_velocity, f_velocity);
        double powerFrontRight = velocityPIDF_FrontRight.calculate(currentVelocityFrontRight, targetVelocityFrontRight);


        backRight.setPower(powerBackRight);
        frontRight.setPower(powerFrontRight);
    }


    public void loopLeft(double magnitude, double angle){
        targetPositionLeftModule = angle * Constants.DEGREES_TO_TICKS_CONSTANT_LEFT;
        currentPositionBackLeft = backLeft.getCurrentPosition();
        currentPositionFrontLeft = frontLeft.getCurrentPosition();
        currentPositionLeftModule = (currentPositionBackLeft - currentPositionFrontLeft);


        if(Math.abs(currentPositionLeftModule - targetPositionLeftModule) <= Math.PI/6)
            rotationPID_LeftModule.setPID(p_rotation_SMOL, i_rotation_SMOL, d_rotation_SMOL);
        else rotationPID_LeftModule.setPID(p_rotation_BIG, i_rotation_BIG, d_rotation_BIG);

        double velocityDifference = rotationPID_LeftModule.calculate(currentPositionLeftModule, targetPositionLeftModule) * Constants.MAX_VELOCITY;



        targetVelocityBackLeft = -magnitude * Constants.MAX_VELOCITY + velocityDifference;
        targetVelocityFrontLeft = -magnitude * Constants.MAX_VELOCITY - velocityDifference;


        currentVelocityBackLeft = backLeft.getVelocity();
        velocityPIDF_BackLeft.setPIDF(p_velocity, i_velocity, d_velocity, f_velocity);
        double powerBackLeft = velocityPIDF_BackLeft.calculate(currentVelocityBackLeft, targetVelocityBackLeft);

        currentVelocityFrontLeft = frontLeft.getVelocity();
        velocityPIDF_FrontLeft.setPIDF(p_velocity, i_velocity, d_velocity, f_velocity);
        double powerFrontLeft = velocityPIDF_FrontLeft.calculate(currentVelocityFrontLeft, targetVelocityFrontLeft);


        backLeft.setPower(powerBackLeft);
        frontLeft.setPower(powerFrontLeft);
    }

    public void loop(double magnitudeLeft, double magnitudeRight, double angleLeft, double angleRight){
        loopLeft(magnitudeLeft, angleLeft);
        loopRight(magnitudeRight, angleRight);
    }






    public void bulkRead(){
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

//    public double getTranslationVelocity(){
//        double velocityLeft = leftMotor.getVelocity();
//        double velocityRight = rightMotor.getVelocity();
//        return (velocityLeft + velocityRight)/2;
//    }
//
//    public double getRotationVelocity(){
//        double velocityLeft = leftMotor.getVelocity();
//        double velocityRight = rightMotor.getVelocity();
//        return (velocityLeft - velocityRight)/2;
//    }


    public double getCurrentVelocityLeft(){
        return currentVelocityBackRight;
    }
    public double getTargetVelocityLeft(){
        return targetVelocityBackRight;
    }



    public double getCurrentVelocityRight(){
        return currentVelocityFrontRight;
    }
    public double getTargetVelocityRight(){
        return targetVelocityFrontRight;
    }



    public double getCurrentVelocityWheel(){
        return (currentVelocityBackRight + currentVelocityFrontRight)/2;
    }

    public double getTargetVelocityWheel(){
        return targetVelocityBackRight;
    }


    public double getCurrentPositionRightWheel(){
        return currentPositionRightModule;//in ticks
    }

    public double getCurrentPositionLeftWheel(){
        return currentPositionLeftModule;//in ticks
    }


    public double getCurrentAngleRightWheel(){
        return (Math.toDegrees(currentPositionRightModule) * 360) / 1324.2;

    }

    public double getCurrentAngleLeftWheel(){
        return (Math.toDegrees(currentPositionLeftModule) * 360) / 1344.2;
    }

    public double getTargetPositionWheel(){
        return targetPositionRightModule;
    }



}
