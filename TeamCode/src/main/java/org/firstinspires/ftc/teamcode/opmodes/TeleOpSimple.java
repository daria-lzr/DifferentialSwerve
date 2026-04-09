package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.utils.DriveTrain;



@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpSimple", group = "OpModes")
public class TeleOpSimple extends CommandOpMode {
    private final DriveTrain driveTrain = DriveTrain.getInstance();
    public GamepadEx gamepadEx;
    private double loopTime = 0;

    public static double powerLeft = 0;
    public static double powerRight = 0;


    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        CommandScheduler.getInstance().reset();
        gamepadEx = new GamepadEx(gamepad1);
        driveTrain.initializeHardware(hardwareMap);
        driveTrain.initialize();

        powerLeft = 0;
        powerRight = 0;
    }


    @Override
    public void run(){
        CommandScheduler.getInstance().run();
        driveTrain.bulkRead();


//        driveTrain.leftMotor.setPower(powerLeft);
//        driveTrain.rightMotor.setPower(powerRight);

        telemetry.addData("PowerLeft", powerLeft);
        telemetry.addData("PowerRight", powerRight);
        double loop = System.nanoTime();
        telemetry.addData("Hz", 1000000000 / (loop - loopTime));
        loopTime = loop;
        telemetry.update();
    }
}
