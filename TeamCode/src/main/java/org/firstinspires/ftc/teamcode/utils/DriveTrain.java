package org.firstinspires.ftc.teamcode.utils;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.controller.PIDFController;

import java.util.List;


@Config
public class DriveTrain {
    private static DriveTrain instance = null;
    private HardwareMap hardwareMap;
    private List<LynxModule> allHubs;
    public DcMotorEx leftMotor, rightMotor, throughBoreEncoder;

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }

    public final double MAX_VELOCITY = 2781;


    public PIDFController velocityPIDF_Left;
    public PIDFController velocityPIDF_Right;
    public static double p_velocity = 0, i_velocity = 0, d_velocity = 0, f_velocity = 0.00039;
    public PIDController rotationPID;
    public static double p_rotation = 0, i_rotation = 0, d_rotation = 0;

    public static double currentPosition = 0;
    public static double targetPosition = 0;

    public double currentPositionLeft = 0;
    public double currentPositionRight = 0;

    public static double currentVelocity = 0;
    public static double targetVelocityLeft = 0, targetVelocityRight = 0;
    public static double currentVelocityLeft = 0;
    public static double currentVelocityRight = 0;


    public static double dashInput = 0;


    public void initializeHardware(final HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        leftMotor = hardwareMap.get(DcMotorEx.class, "leftMotor");
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightMotor = hardwareMap.get(DcMotorEx.class, "rightMotor");
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        throughBoreEncoder = hardwareMap.get(DcMotorEx.class, "throughBore");
        throughBoreEncoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        throughBoreEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        throughBoreEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

    }

    public void initialize(){
        velocityPIDF_Left = new PIDFController(p_velocity, i_velocity, d_velocity, f_velocity);
        velocityPIDF_Left.reset();
        velocityPIDF_Right = new PIDFController(p_velocity, i_velocity, d_velocity, f_velocity);
        velocityPIDF_Right.reset();
        rotationPID = new PIDController(p_rotation, i_rotation, d_rotation);
        rotationPID.reset();

        currentPositionLeft = leftMotor.getCurrentPosition();
        currentPositionRight = rightMotor.getCurrentPosition();
        currentPosition = 0;
        targetPosition = 0;

        currentVelocityLeft = leftMotor.getVelocity();
        currentVelocityRight = rightMotor.getVelocity();
        targetVelocityLeft = 0;
        targetVelocityRight = 0;
    }


    public void loop(double left_stick_x, double right_stick_y){
        targetPosition = dashInput * 90 * 3.3334;
        currentPositionLeft = leftMotor.getCurrentPosition();
        currentPositionRight = rightMotor.getCurrentPosition();
        currentPosition = (currentPositionLeft - currentPositionRight);

        rotationPID.setPID(p_rotation, i_rotation, d_rotation);
        double velocityDifference = rotationPID.calculate(currentPosition, targetPosition) * MAX_VELOCITY;

        targetVelocityLeft = right_stick_y * MAX_VELOCITY + velocityDifference;
        targetVelocityRight = right_stick_y * MAX_VELOCITY - velocityDifference;


        currentVelocityLeft = leftMotor.getVelocity();
        velocityPIDF_Left.setPIDF(p_velocity, i_velocity, d_velocity, f_velocity);
        double powerLeft = velocityPIDF_Left.calculate(currentVelocityLeft, targetVelocityLeft);

        currentVelocityRight = rightMotor.getVelocity();
        velocityPIDF_Right.setPIDF(p_velocity, i_velocity, d_velocity, f_velocity);
        double powerRight = velocityPIDF_Left.calculate(currentVelocityRight, targetVelocityRight);


        leftMotor.setPower(powerLeft);
        rightMotor.setPower(powerRight);


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
        return currentVelocityLeft;
    }
    public double getTargetVelocityLeft(){
        return targetVelocityLeft;
    }



    public double getCurrentVelocityRight(){
        return currentVelocityRight;
    }
    public double getTargetVelocityRight(){
        return targetVelocityRight;
    }



    public double getCurrentVelocityWheel(){
        return (currentVelocityLeft + currentVelocityRight)/2;
    }

    public double getTargetVelocityWheel(){
        return targetVelocityLeft;
    }


    public double getCurrentPositionWheel(){
        return currentPosition;
    }

    public double getTargetPositionWheel(){
        return targetPosition;
    }



}
