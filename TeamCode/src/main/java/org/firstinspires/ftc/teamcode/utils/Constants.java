package org.firstinspires.ftc.teamcode.utils;

public class Constants {

    public static double GEAR_RATIO_ROTATION_LEFT = 4.631578947368421;
    public static double GEAR_RATIO_ROTATION_RIGHT = 4.5625;


    public static double MOTOR_PPR = 145.1; //the encoder resolution

    public static double TICKS_PER_REV_RIGHT = GEAR_RATIO_ROTATION_RIGHT * 2 * MOTOR_PPR;
    public static double TICKS_PER_REV_LEFT = GEAR_RATIO_ROTATION_LEFT * 2 * MOTOR_PPR;


    public static double DEGREES_TO_TICKS_CONSTANT_RIGHT = TICKS_PER_REV_RIGHT / (2*Math.PI);
    public static double DEGREES_TO_TICKS_CONSTANT_LEFT = TICKS_PER_REV_LEFT / (2*Math.PI);
    public static double MAX_VELOCITY = 2781;

}
