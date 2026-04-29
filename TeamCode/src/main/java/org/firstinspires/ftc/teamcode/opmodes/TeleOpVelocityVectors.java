package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.hardware.DriveTrain;
import org.firstinspires.ftc.teamcode.utils.Vector2D;


@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpVelocityVectors➡️", group = "OpModes")
public class TeleOpVelocityVectors extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    private double loopTime = 0;

    Vector2D strafeVector = new Vector2D();



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

        strafeVector.set(
                gamepad1.right_stick_x,
                -gamepad1.right_stick_y
        );



        double magnitude = strafeVector.magnitude();
        double angle = strafeVector.angle();
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
        telemetry.addData("x:", strafeVector.x);
        telemetry.addData("y:", strafeVector.y);

        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
