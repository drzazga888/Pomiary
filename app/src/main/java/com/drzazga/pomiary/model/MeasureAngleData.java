package com.drzazga.pomiary.model;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drzazga.pomiary.utils.MathExtra;
import com.drzazga.pomiary.view.MeasureSurface;

import java.util.Locale;

public class MeasureAngleData extends MeasureDataElement {

    public final static float DETECTION_ANGLE_R = (float) (MeasureSurface.ANGLE_CIRCLE_R * 1.5);

    public MeasurePointData p0, p1, p2;

    public MeasureAngleData(MeasurePointData p0, MeasurePointData p1, MeasurePointData p2, String name) {
        super(name);
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
    }

    private double getStartAngle() {
        PointF pos0 = p0.getRelativePos();
        PointF pos1 = p1.getRelativePos();
        return Math.atan2(pos1.y - pos0.y, pos1.x - pos0.x);
    }

    private double getEndAngle() {
        PointF pos0 = p0.getRelativePos();
        PointF pos2 = p2.getRelativePos();
        return Math.atan2(pos2.y - pos0.y, pos2.x - pos0.x);
    }

    public float getStartAngleDeg() {
        return (float) (getStartAngle() * 180.0 / Math.PI);
    }

    public float getSweepAngleDeg() {
        return (float) ((getEndAngle() - getStartAngle()) * 180.0 / Math.PI) ;
    }

    @NonNull
    @Override
    public String getStringValue() {
        return String.format(
                Locale.getDefault(),
                "%.2f" + (char) 0x00B0,
                Math.abs(getSweepAngleDeg())
        );
    }

    @NonNull
    public RectF getOval() {
        PointF pos0 = p0.getRelativePos();
        return new RectF(
                pos0.x - MeasureSurface.ANGLE_CIRCLE_R,
                pos0.y - MeasureSurface.ANGLE_CIRCLE_R,
                pos0.x + MeasureSurface.ANGLE_CIRCLE_R,
                pos0.y + MeasureSurface.ANGLE_CIRCLE_R
        );
    }

    @Override
    public String toString() {
        return name + "(" + p0 + ", " + p1 + ", " + p2 + ")";
    }

    @Override
    public boolean isSelected(PointF p) {
        MathExtra.PolarCoordinates polarP = new MathExtra.PolarCoordinates(p0.getRelativePos(), p);
        return MathExtra.between(polarP.theta, getStartAngle(), getEndAngle()) && polarP.r <= DETECTION_ANGLE_R;
    }

    @Override
    public void performSelectedActon() {
        super.performSelectedActon();
        Log.i("touching", "angle " + toString() + " is selected");
    }

    @Override
    public PointF getNamePosition() {
        return null;
    }

    @NonNull
    @Override
    public PointF getStringValuePosition() {
        double angle = (getStartAngle() + getEndAngle()) * 0.5;
        PointF pos0 = p0.getRelativePos();
        return new PointF(
                (float) (pos0.x + MeasureSurface.ANGLE_CIRCLE_R * Math.cos(angle)),
                (float) (pos0.y + MeasureSurface.ANGLE_CIRCLE_R * Math.sin(angle))
        );
    }
}
