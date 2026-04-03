package org.firstinspires.ftc.teamcode.utils;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.controller.PIDFController;

import java.util.List;


@Config
public class DriveTrain {
    private static DriveTrain instance = null;
    private HardwareMap hardwareMap;
    private List<LynxModule> allHubs;
    public DcMotorEx leftMotor, rightMotor;
//    public DcMotorEx throughBoreEncoder;

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }
        return instance;
    }

    public static double MAX_VELOCITY = 2781;


    public PIDFController velocityPIDF_Left;
    public PIDFController velocityPIDF_Right;
    public double p_velocity = 0, i_velocity = 0, d_velocity = 0, f_velocity = 0.000348;
    public PIDController rotationPID;
    public static double p_rotation_BIG = 0.009, i_rotation_BIG = 0.001, d_rotation_BIG = 0.0001;
    public static double p_rotation_SMOL = 0.0115, i_rotation_SMOL = 0.01, d_rotation_SMOL = 0;

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


//        throughBoreEncoder = hardwareMap.get(DcMotorEx.class, "throughBore");
//        throughBoreEncoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        throughBoreEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        throughBoreEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


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
        rotationPID = new PIDController(p_rotation_BIG, i_rotation_BIG, d_rotation_BIG);
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
        targetPosition = left_stick_x * 90 * 3.7361;
        currentPositionLeft = leftMotor.getCurrentPosition();
        currentPositionRight = rightMotor.getCurrentPosition();
        currentPosition = (currentPositionLeft - currentPositionRight);


        if(Math.abs(currentPosition - targetPosition) <= 30)
            rotationPID.setPID(p_rotation_SMOL, i_rotation_SMOL, d_rotation_SMOL);
        else rotationPID.setPID(p_rotation_BIG, i_rotation_BIG, d_rotation_BIG);

        double velocityDifference = rotationPID.calculate(currentPosition, targetPosition) * MAX_VELOCITY;



//        if(Math.abs(currentVelocityRight + velocityDifferenceRight) > MAX_VELOCITY){
//            if(currentVelocityRight + velocityDifferenceRight > 0){
//                velocityDifferenceLeft += (Math.abs(currentVelocityRight + velocityDifferenceRight) - MAX_VELOCITY);
//                velocityDifferenceRight -= (Math.abs(currentVelocityRight + velocityDifferenceRight) - MAX_VELOCITY);
//            }
//            else{
//                velocityDifferenceLeft -= (Math.abs(currentVelocityRight + velocityDifferenceRight) - MAX_VELOCITY);
//                velocityDifferenceRight += (Math.abs(currentVelocityRight + velocityDifferenceRight) - MAX_VELOCITY);
//            }
//        }
//
//
//        if(Math.abs(currentVelocityLeft + velocityDifferenceLeft) > MAX_VELOCITY){
//            if(currentVelocityLeft + velocityDifferenceLeft > 0){
//                velocityDifferenceLeft -= (Math.abs(currentVelocityLeft + velocityDifferenceLeft) - MAX_VELOCITY);
//                velocityDifferenceRight += (Math.abs(currentVelocityLeft + velocityDifferenceLeft) - MAX_VELOCITY);
//            }
//            else{
//                velocityDifferenceLeft += (Math.abs(currentVelocityLeft + velocityDifferenceLeft) - MAX_VELOCITY);
//                velocityDifferenceRight -= (Math.abs(currentVelocityLeft + velocityDifferenceLeft) - MAX_VELOCITY);
//            }
//
//        }




        targetVelocityLeft = right_stick_y * MAX_VELOCITY + velocityDifference;
        targetVelocityRight = right_stick_y * MAX_VELOCITY - velocityDifference;

//        if(Math.abs(targetVelocityLeft) > MAX_VELOCITY){
//            double difference = Math.abs(targetVelocityLeft) - MAX_VELOCITY;
//            if(targetVelocityRight > 0)
//                targetVelocityRight -= difference;
//            else targetVelocityRight += difference;
//        }
//
//        if(Math.abs(targetVelocityRight) > MAX_VELOCITY){
//            double difference = Math.abs(targetVelocityRight) - MAX_VELOCITY;
//            if(targetVelocityLeft > 0){
//                targetVelocityLeft -= difference;
//            }
//            else targetVelocityLeft += difference;
//        }

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
