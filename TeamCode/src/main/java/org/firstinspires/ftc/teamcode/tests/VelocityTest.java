package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.hardware.DriveTrain;


@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "VelocityTest", group = "OpModes")
public class VelocityTest extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    public static double currentVelocity = 0;
    public static double targetVelocity = 0;
    public PIDController velocityPID_Left;
    public PIDController velocityPID_Right;


    public double previousPositionLeft = 0;
    public double currentPositionLeft = 0;


    public double previousTime = 0;
    public ElapsedTime currentTime = new ElapsedTime();

    public double velocityLeft, velocityRight;

//    public static double dashInput = 0;



    public static double p_velocity = 0, i_velocity = 0, d_velocity = 0;
    private double loopTime = 0;
    public int counter = 0;
    public static int limit = 10;


    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        CommandScheduler.getInstance().reset();
        gamepadEx = new GamepadEx(gamepad1);
        driveTrain.initializeHardware(hardwareMap);
        driveTrain.initialize();

        counter = 0;
        previousTime = 0;



//        velocityPID_Left = new PIDController(p_velocity, i_velocity, d_velocity);
//        velocityPID_Right = new PIDController(p_velocity, i_velocity, d_velocity);

//        velocityPID_Left.reset();
//        velocityPID_Right.reset();

//        currentPositionLeft = driveTrain.leftMotor.getCurrentPosition();
        currentTime.reset();
    }


    @Override
    public void run(){
        counter++;
        CommandScheduler.getInstance().run();
        driveTrain.bulkRead();


//        currentPositionLeft = driveTrain.leftMotor.getCurrentPosition();
//        velocityRight = driveTrain.rightMotor.getVelocity();
//        currentVelocity = (velocityLeft - velocityRight)/2;

//        dashInput = -gamepad1.left_stick_x * 90;
//        targetPosition = dashInput * 3.3334;


//        velocityPID_Left.setPID(p_velocity, i_velocity, d_velocity);
//        velocityPID_Right.setPID(p, i, d);

//        double powerLeft = velocityPID_Left.calculate(velocityLeft, targetVelocity);
//        driveTrain.leftMotor.setPower(powerLeft);
//        double powerRight = velocityPID_Right.calculate(velocityRight, targetVelocity);

//        double difference = rotation_pid.calculate(currentPosition, targetPosition);

//        driveTrain.leftMotor.setPower(gamepad1.right_stick_y + difference/2);
//        driveTrain.rightMotor.setPower(gamepad1.right_stick_y - difference/2);
//        telemetry.addData("CurrentPosition:", currentPosition);
//        telemetry.addData("TargetPosition:", targetPosition);
//        telemetry.addData("Error:", Math.abs(currentPosition - targetPosition));

//        telemetry.addData("CurrentVelocity:", currentVelocity);




        if(counter >= limit) {
            velocityLeft = (currentPositionLeft - previousPositionLeft)/(currentTime.milliseconds() - previousTime);
            previousTime = currentTime.milliseconds();
            previousPositionLeft = currentPositionLeft;
            counter = 0;
        }


        telemetry.addData("VelocityLeft:", velocityLeft);
        telemetry.addData("Current Time:", currentTime.milliseconds());
        telemetry.addData("Previous Time:", previousTime);
        telemetry.addData("Current Position Left:", currentPositionLeft);
        telemetry.addData("Previous Position Left:", previousPositionLeft);
        telemetry.addData("Counter", counter);


//        telemetry.addData("VelocityRight:", velocityRight);
//        telemetry.addData("TargetVelocity:", targetVelocity);


        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
