package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.utils.DriveTrain;


@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpMotorEncoderVelocityPID", group = "OpModes")
public class TeleOpMotorEncoderVelocityPID extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    public static double currentVelocity_Left = 0;
    public static double targetVelocity = 0;

    public PIDController velocityPID_Left;
    public static double p_velocity = 0, i_velocity = 0, d_velocity = 0;
    private double loopTime = 0;

    public double currentPositionLeft = 0;
    public double previousPositionLeft = 0;

    public double previousTime = 0;
    public ElapsedTime currentTime = new ElapsedTime();

    public int counter = 0;


    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        CommandScheduler.getInstance().reset();
        gamepadEx = new GamepadEx(gamepad1);
        driveTrain.initializeHardware(hardwareMap);
        driveTrain.initialize();


        velocityPID_Left = new PIDController(p_velocity, i_velocity, d_velocity);
        velocityPID_Left.reset();

        currentPositionLeft = driveTrain.leftMotor.getCurrentPosition();
        currentTime.reset();

        counter = 0;
        previousTime = 0;

    }


    @Override
    public void run(){
        counter++;
        CommandScheduler.getInstance().run();
        driveTrain.bulkRead();

        currentPositionLeft = driveTrain.leftMotor.getCurrentPosition();


        if(counter == 10) {
            currentVelocity_Left = (currentPositionLeft - previousPositionLeft) / (currentTime.milliseconds() - previousTime);
            previousTime = currentTime.milliseconds();
            previousPositionLeft = currentPositionLeft;

            velocityPID_Left.setPID(p_velocity, i_velocity, d_velocity);
            double powerLeft = velocityPID_Left.calculate(currentVelocity_Left, targetVelocity);
            driveTrain.leftMotor.setPower(powerLeft);

            counter = 0;
        }


        telemetry.addData("VelocityLeft:", currentVelocity_Left);
        telemetry.addData("Current Time:", currentTime.milliseconds());
        telemetry.addData("Previous Time:", previousTime);
        telemetry.addData("Current Position Left:", currentPositionLeft);
        telemetry.addData("Previous Position Left:", previousPositionLeft);
        telemetry.addData("Counter", counter);
        telemetry.addData("Target Velocity:", targetVelocity);


        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
