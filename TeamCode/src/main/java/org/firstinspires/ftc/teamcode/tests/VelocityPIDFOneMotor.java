package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.utils.DriveTrain;


@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "tryAgain", group = "OpModes")
public class VelocityPIDFOneMotor extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    public static double currentVelocity_Left = 0;
    public static double targetVelocity = 0;

    public PIDFController velocityPIDF_Left;
    public static double p_velocity = 0, i_velocity = 0, d_velocity = 0, f_velocity = 0;
    private double loopTime = 0;


    public double currentPositionLeft = 0;

    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        CommandScheduler.getInstance().reset();
        gamepadEx = new GamepadEx(gamepad1);
        driveTrain.initializeHardware(hardwareMap);
        driveTrain.initialize();


        velocityPIDF_Left = new PIDFController(p_velocity, i_velocity, d_velocity, f_velocity);
        velocityPIDF_Left.reset();

        currentPositionLeft = driveTrain.leftMotor.getCurrentPosition();
    }


    @Override
    public void run(){
        CommandScheduler.getInstance().run();
        driveTrain.bulkRead();


        currentVelocity_Left = driveTrain.leftMotor.getVelocity();


        velocityPIDF_Left.setPIDF(p_velocity, i_velocity, d_velocity, f_velocity);
        double powerLeft = velocityPIDF_Left.calculate(currentVelocity_Left, targetVelocity);
        powerLeft = Math.max(-1, Math.min(1, powerLeft));
        driveTrain.leftMotor.setPower(powerLeft);


        telemetry.addData("CurrentVelocityLeft:", currentVelocity_Left);
        telemetry.addData("Target Velocity:", targetVelocity);


        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
