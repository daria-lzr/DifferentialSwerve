package org.firstinspires.ftc.teamcode.utils;

public class Vector2D {

    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0, 0);
    }

    public void set(double x, double y) {
        this.x = (x == 0.0) ? 0.0 : x;
        this.y = (y == 0.0) ? 0.0 : y;
    }



    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D scale(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public Vector2D divide(double scalar) {
        if (scalar == 0) throw new ArithmeticException("Division by zero");
        return new Vector2D(this.x / scalar, this.y / scalar);
    }


    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public double magnitudeSquared() {
        return x * x + y * y;
    }

    public Vector2D normalize() {
        double mag = magnitude();
        if (mag == 0) return new Vector2D(0, 0);
        return divide(mag);
    }

    public Vector2D setMagnitude(double newMag) {
        return normalize().scale(newMag);
    }

    /**
     * in radians
     * @return
     */
    public double angle() {
        return Math.atan2(y, x);
    }




    public double angleDegrees() {
        return Math.toDegrees(angle());
    }


    public Vector2D rotate(double angleRadians) {
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);

        return new Vector2D(
                x * cos - y * sin,
                x * sin + y * cos
        );
    }


    public double dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }


    public double cross(Vector2D other) {
        return this.x * other.y - this.y * other.x;
    }


    public double distanceTo(Vector2D other) {
        return Math.sqrt(
                (other.x - this.x) * (other.x - this.x) +
                        (other.y - this.y) * (other.y - this.y)
        );
    }

    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    public static Vector2D fromAngle(double angleRadians) {
        return new Vector2D(Math.cos(angleRadians), Math.sin(angleRadians));
    }

    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }


    public Vector2D clamp(double maxMagnitude) {
        if (magnitude() > maxMagnitude) {
            return normalize().scale(maxMagnitude);
        }
        return this.copy();
    }


    @Override
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }
}
