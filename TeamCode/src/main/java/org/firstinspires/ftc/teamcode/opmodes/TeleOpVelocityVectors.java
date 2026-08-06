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

    Vector2D rotationDirectionLeft = new Vector2D(0, 36.2).normalize();
    Vector2D rotationDirectionRight = new Vector2D(0, -36.2).normalize();
    Vector2D rotationVectorLeft = new Vector2D();
    Vector2D rotationVectorRight = new Vector2D();

    Vector2D finalVectorLeft = new Vector2D();
    Vector2D finalVectorRight = new Vector2D();

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


        double rotationInput = gamepad1.left_stick_x;
        if(gamepad1.left_stick_x == 0) {
            rotationInput = 0;
        }

        rotationVectorLeft = rotationDirectionLeft.scale(rotationInput);
        finalVectorLeft = strafeVector.add(rotationVectorLeft);

        rotationVectorRight = rotationDirectionRight.scale(rotationInput);
        finalVectorRight = strafeVector.add(rotationVectorRight);




        double magnitudeLeft = finalVectorLeft.magnitude();
        double magnitudeRight = -finalVectorRight.magnitude();


        double newAngleLeft = finalVectorLeft.angle();
        double newAngleRight = finalVectorRight.angle();

        //shortest angular difference
        double deltaLeft = Math.atan2(
                Math.sin(newAngleLeft - previousAngleLeft),
                Math.cos(newAngleLeft - previousAngleLeft)
        );

        double deltaRight = Math.atan2(
                Math.sin(newAngleRight - previousAngleRight),
                Math.cos(newAngleRight - previousAngleRight)
        );

        //accumulate
        continuousAngleLeft += deltaLeft;
        continuousAngleRight += deltaRight;

        previousAngleLeft = newAngleLeft;
        previousAngleRight = newAngleRight;

        driveTrain.loop(magnitudeLeft, magnitudeRight, continuousAngleLeft, continuousAngleRight);



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
        telemetry.addData("Angle Stafe Vector", Math.toDegrees(strafeVector.angle()));
        telemetry.addData("x:", strafeVector.x);
        telemetry.addData("y:", strafeVector.y);

        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
