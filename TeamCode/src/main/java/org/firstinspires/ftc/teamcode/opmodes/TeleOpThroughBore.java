package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.utils.DriveTrain;


@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpThroughBore", group = "OpModes")
public class TeleOpThroughBore extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    public static double currentPosition = 0;
    public static double targetPosition = 0;

    public static double dashInput = 0;

    public PIDController rotation_pid;
    public static double p = 0.0009, i = 0.00001, d = 0.000015;
    private double loopTime = 0;

    public double extra_left, extra_right;


    public enum DriveState{
        PID,
        MANUAL
    }

    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        CommandScheduler.getInstance().reset();
        gamepadEx = new GamepadEx(gamepad1);
        driveTrain.initializeHardware(hardwareMap);
        driveTrain.initialize();


        rotation_pid = new PIDController(p, i, d);
        rotation_pid.reset();
        currentPosition = driveTrain.throughBoreEncoder.getCurrentPosition();


    }


    @Override
    public void run(){
        CommandScheduler.getInstance().run();
        driveTrain.bulkRead();


//        driveTrain.leftMotor.setPower(gamepad1.left_stick_y);
//        driveTrain.rightMotor.setPower(gamepad1.left_stick_y);
//        double currentPowerLeft = driveTrain.leftMotor.getPower();
//        double currentPowerRight = driveTrain.rightMotor.getPower();


        currentPosition = driveTrain.throughBoreEncoder.getCurrentPosition();
        dashInput = -gamepad1.left_stick_x * 90;
        targetPosition = dashInput * 22.756;
        rotation_pid.setPID(p, i, d);
        double difference = rotation_pid.calculate(currentPosition, targetPosition);
//        if(Math.abs(gamepad1.right_stick_y + difference/2) > 1){
//             extra_left = 1 - (gamepad1.right_stick_y + difference/2);
//        }
//        else extra_left = 0;
//        if(Math.abs(gamepad1.right_stick_y - difference/2) > 1){
//            extra_right = 1 - (gamepad1.right_stick_y - difference/2);
//        }
//        else extra_right = 0;
        driveTrain.leftMotor.setPower(gamepad1.right_stick_y + difference/2);
        driveTrain.rightMotor.setPower(gamepad1.right_stick_y - difference/2);



        telemetry.addData("CurrentPosition:", currentPosition);
        telemetry.addData("TargetPosition:", targetPosition);
        telemetry.addData("Error:", Math.abs(currentPosition - targetPosition));
        telemetry.addData("Stick_value:", gamepad1.left_stick_y);

        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
