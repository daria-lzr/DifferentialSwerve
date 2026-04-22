package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.hardware.DriveTrain;


@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Position Test")
public class PositionTest extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    public double currentPositionRight = 0, currentPositionLeft = 0;
    private double loopTime = 0;

    public double positionFrontLeft, positionBackLeft;
    public double positionFrontRight, positionBackRight;



    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        CommandScheduler.getInstance().reset();
        gamepadEx = new GamepadEx(gamepad1);
        driveTrain.initializeHardware(hardwareMap);
        driveTrain.initialize();

//        currentPosition = driveTrain.throughBoreEncoder.getCurrentPosition();
    }


    @Override
    public void run(){
        CommandScheduler.getInstance().run();
        driveTrain.bulkRead();

        positionBackLeft = driveTrain.backLeft.getCurrentPosition();
        positionFrontLeft = driveTrain.frontLeft.getCurrentPosition();

        positionBackRight = driveTrain.backRight.getCurrentPosition();
        positionFrontRight = driveTrain.frontRight.getCurrentPosition();


        currentPositionRight = positionBackRight - positionFrontRight;
        currentPositionLeft = positionBackLeft - positionFrontLeft;

        telemetry.addData("CurrentPosition RIGHT:", currentPositionRight);
        telemetry.addData("CurrentPosition LEFT:", currentPositionLeft);

        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
