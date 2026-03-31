package org.firstinspires.ftc.teamcode.utils;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

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

    }

    public void bulkRead(){
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }



}
