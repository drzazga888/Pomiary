package com.drzazga.pomiary.utils;

import android.graphics.Point;
import android.graphics.PointF;

import org.jetbrains.annotations.Contract;

public class MathExtra {

    public static double normalizeAngle(double angle) {
        while (angle < 0.0)
            angle += 2.0 * Math.PI;
        while (angle >= 2.0 * Math.PI)
            angle -= 2.0 * Math.PI;
        return angle;
    }

    public static boolean betweenAngles(double theta, double startAngle, double endAngle) {
        if (startAngle <= endAngle)
            return startAngle < theta && endAngle > theta;
        else
            return !(startAngle < theta && endAngle > theta);
    }

    public static double getMiddleAngle(double startAngle, double endAngle) {
        double avg = (startAngle + endAngle) * 0.5;
        if (startAngle > endAngle)
            avg = normalizeAngle(avg + Math.PI);
        return avg;
    }

    public static class PolarCoordinates {

        public double r, theta;

        public PolarCoordinates(PointF translation, PointF point) {
            this.r = MathExtra.distance(point, translation);
            this.theta = Math.atan2(point.y - translation.y, point.x - translation.x);
        }
    }

    @Contract(pure = true)
    public static double pow2(double x) {
        return x * x;
    }

    @Contract(pure = true)
    public static boolean between(double val, double end1, double end2) {
        double min, max;
        if (end1 > end2) {
            min = end2;
            max = end1;
        } else {
            min = end1;
            max = end2;
        }
        return val >= min && val <= max;
    }

    public static double getAngleFromLinePoints(Point p1, Point p2) {
        return Math.atan2(p2.y - p1.y, p2.x - p1.x) * 180 / Math.PI;
    }

    public static double distance(PointF p1, PointF p2) {
        return Math.sqrt(pow2(p2.x - p1.x) + pow2(p2.y - p1.y));
    }
}
