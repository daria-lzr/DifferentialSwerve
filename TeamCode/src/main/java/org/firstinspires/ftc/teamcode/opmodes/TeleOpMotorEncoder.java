package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.controller.PIDController;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.hardware.DriveTrain;


@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp Motor Encoder", group = "OpModes")
public class TeleOpMotorEncoder extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    public static double currentPosition = 0;
    public static double targetPosition = 0;

    public static double dashInput = 0;

    public PIDController rotationPID;
    public static double p_rotation = 0.007, i_rotation = 0.001, d_rotation = 0.0001;
    private double loopTime = 0;

    public double extra_left, extra_right;
    public double positionLeft, positionRight;



    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        CommandScheduler.getInstance().reset();
        gamepadEx = new GamepadEx(gamepad1);
        driveTrain.initializeHardware(hardwareMap);
        driveTrain.initialize();


        rotationPID = new PIDController(p_rotation, i_rotation, d_rotation);
        rotationPID.reset();
//        currentPosition = driveTrain.throughBoreEncoder.getCurrentPosition();


    }


    @Override
    public void run(){
        CommandScheduler.getInstance().run();
        driveTrain.bulkRead();


        positionLeft = driveTrain.frontRight.getCurrentPosition();
        positionRight = driveTrain.backRight.getCurrentPosition();
        currentPosition = (positionLeft - positionRight);

        dashInput = -gamepad1.left_stick_x * 90;
        targetPosition = dashInput * 3.3334;
        rotationPID.setPID(p_rotation, i_rotation, d_rotation);
        double difference = rotationPID.calculate(currentPosition, targetPosition);
//        if(Math.abs(gamepad1.right_stick_y + difference/2) > 1){
//             extra_left = 1 - (gamepad1.right_stick_y + difference/2);
//        }
//        else extra_left = 0;
//        if(Math.abs(gamepad1.right_stick_y - difference/2) > 1){
//            extra_right = 1 - (gamepad1.right_stick_y - difference/2);
//        }
//        else extra_right = 0;
        driveTrain.frontRight.setPower(gamepad1.right_stick_y + difference/2);
        driveTrain.backRight.setPower(gamepad1.right_stick_y - difference/2);



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
