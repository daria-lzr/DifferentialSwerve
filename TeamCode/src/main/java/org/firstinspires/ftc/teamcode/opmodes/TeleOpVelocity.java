package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.hardware.DriveTrain;


@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpVelocity", group = "OpModes")
public class TeleOpVelocity extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    private double loopTime = 0;




    @Override
    public void initialize() {
        gamepad1.setLedColor(128, 0, 128, Gamepad.LED_DURATION_CONTINUOUS);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        CommandScheduler.getInstance().reset();
        gamepadEx = new GamepadEx(gamepad1);
        driveTrain.initializeHardware(hardwareMap);
        driveTrain.initialize();

    }


    @Override
    public void run(){
        CommandScheduler.getInstance().run();
        driveTrain.bulkRead();

        double x = gamepad1.right_stick_x;
        double y = -gamepad1.right_stick_y;

        if(gamepad1.right_stick_y == 0){
            y = 0;
        }

        if(gamepad1.right_stick_x == 0){
            x = 0;
        }

        double magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double angle = Math.toDegrees(Math.atan2(x, y));
        driveTrain.loop(magnitude, angle);


//        telemetry.addData("Current VELOCITY LEFT:", driveTrain.getCurrentVelocityLeft());
//        telemetry.addData("Target VELOCITY LEFT:", driveTrain.getTargetVelocityLeft());
//
//        telemetry.addData("Current VELOCITY RIGHT:", driveTrain.getCurrentVelocityRight());
//        telemetry.addData("Target VELOCITY RIGHT:", driveTrain.getTargetVelocityRight());
//
//
//        telemetry.addData("Current VELOCITY WHEEL:", driveTrain.getCurrentVelocityWheel());
//        telemetry.addData("Target VELOCITY WHEEL:", driveTrain.getTargetVelocityWheel());


        telemetry.addData("Current POSITION WHEEL:", driveTrain.getCurrentPositionWheel());
        telemetry.addData("Target POSITION WHEEL:", driveTrain.getTargetPositionWheel());

        telemetry.addData("TAN:", angle);
        telemetry.addData("x:", x);
        telemetry.addData("y:", y);

        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
