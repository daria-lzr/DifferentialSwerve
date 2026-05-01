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

    private double previousAngleLeft = 0.0, previousAngleRight = 0.0;
    private double continuousAngleLeft = 0.0, continuousAngleRight = 0.0;

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
                gamepad1.right_stick_y
        );



        double magnitudeLeft = strafeVector.magnitude();
        double magnitudeRight = -strafeVector.magnitude();


        double newAngleLeft = strafeVector.angle(); //-90
        double newAngleRight = strafeVector.angle();//+90

        //shortest angular difference
        double deltaLeft = Math.toDegrees(Math.atan2(
                Math.sin(Math.toRadians(newAngleLeft - previousAngleLeft)),
                Math.cos(Math.toRadians(newAngleLeft - previousAngleLeft))
        ));

        double deltaRight = Math.toDegrees(Math.atan2(
                Math.sin(Math.toRadians(newAngleRight - previousAngleRight)),
                Math.cos(Math.toRadians(newAngleRight - previousAngleRight))
        ));

        //accumulate
        continuousAngleLeft += deltaLeft;
        continuousAngleRight += deltaRight;

        previousAngleLeft = newAngleLeft;
        previousAngleRight = newAngleRight;

        driveTrain.loop(magnitudeLeft, magnitudeRight, continuousAngleLeft, continuousAngleRight);


//        double currentAngleLeft = driveTrain.getCurrentAngleLeftWheel();
//        double currentAngleRight = driveTrain.getCurrentAngleRightWheel();
//
//        double angleErrorLeft = Math.toRadians(targetAngleLeft - currentAngleLeft);
//        double angleErrorRight = Math.toRadians(targetAngleRight - currentAngleRight);
//
//        double normalizedAngleLeft = Math.toDegrees(Math.atan2(Math.cos(angleErrorLeft), Math.sin(angleErrorLeft))) - 90;
//        double normalizedAngleRight = Math.toDegrees(Math.atan2(Math.cos(angleErrorRight), Math.sin(angleErrorRight))) + 90;
//        driveTrain.loop(magnitude, normalizedAngleLeft, normalizedAngleRight);


//        telemetry.addData("Current VELOCITY LEFT:", driveTrain.getCurrentVelocityLeft());
//        telemetry.addData("Target VELOCITY LEFT:", driveTrain.getTargetVelocityLeft());
//
//        telemetry.addData("Current VELOCITY RIGHT:", driveTrain.getCurrentVelocityRight());
//        telemetry.addData("Target VELOCITY RIGHT:", driveTrain.getTargetVelocityRight());
//
//
//        telemetry.addData("Current VELOCITY WHEEL:", driveTrain.getCurrentVelocityWheel());
//        telemetry.addData("Target VELOCITY WHEEL:", driveTrain.getTargetVelocityWheel());


//        telemetry.addData("Current POSITION WHEEL:", driveTrain.getCurrentPositionRightWheel());
//        telemetry.addData("Target POSITION WHEEL:", driveTrain.getTargetPositionWheel());

        telemetry.addData("Current ANGLE RIGHT WHEEL:", driveTrain.getCurrentAngleRightWheel());
        telemetry.addData("Current ANGLE LEFT WHEEL:", driveTrain.getCurrentAngleLeftWheel());
//        telemetry.addData("Target ANGLE LEFT WHEEL:", driveTrain.getCurrentAngleLeftWheel());




//        telemetry.addData("TAN:", angle);
        telemetry.addData("x:", strafeVector.x);
        telemetry.addData("y:", strafeVector.y);

        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
