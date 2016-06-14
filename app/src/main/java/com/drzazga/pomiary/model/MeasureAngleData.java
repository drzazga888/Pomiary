package com.drzazga.pomiary.model;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.utils.MathExtra;
import com.drzazga.pomiary.view.MeasureSurface;

import java.util.Locale;

public class MeasureAngleData extends MeasureDataElement {

    public final static float DETECTION_ANGLE_R = (float) (MeasureSurface.ANGLE_CIRCLE_R * 1.5);

    public MeasurePointData p0, p1, p2;

    public MeasureAngleData(Integer id, MeasurePointData p0, MeasurePointData p1, MeasurePointData p2, String name) {
        super(id, name);
        this.p0 = p0 != null ? p0 : MeasureData.startPoint.get(0);
        this.p1 = p1 != null ? p1 : MeasureData.startPoint.get(0);
        this.p2 = p2 != null ? p2 : MeasureData.startPoint.get(0);
    }

    private double getStartAngle() {
        Point pos0 = p0.getRelativePos();
        Point pos1 = p1.getRelativePos();
        return Math.atan2(pos1.y - pos0.y, pos1.x - pos0.x);
    }

    private double getEndAngle() {
        Point pos0 = p0.getRelativePos();
        Point pos2 = p2.getRelativePos();
        return Math.atan2(pos2.y - pos0.y, pos2.x - pos0.x);
    }

    public float getStartAngleDeg() {
        return (float) (getStartAngle() * 180.0 / Math.PI);
    }

    public float getSweepAngleDeg() {
        return (float) (MathExtra.normalizeAngle(getEndAngle() - getStartAngle()) * 180.0 / Math.PI) ;
    }

    @NonNull
    @Override
    public String getStringValue() {
        return String.format(
                Locale.getDefault(),
                "%.2f" + (char) 0x00B0,
                getSweepAngleDeg()
        );
    }

    @NonNull
    public RectF getOval() {
        Point pos0 = p0.getRelativePos();
        return new RectF(
                pos0.x - MeasureSurface.ANGLE_CIRCLE_R,
                pos0.y - MeasureSurface.ANGLE_CIRCLE_R,
                pos0.x + MeasureSurface.ANGLE_CIRCLE_R,
                pos0.y + MeasureSurface.ANGLE_CIRCLE_R
        );
    }

    @Override
    public String toString() {
        return super.toString() + "(" + p0 + ", " + p1 + ", " + p2 + ")";
    }

    @Override
    public boolean isSelected(PointF p) {
        Point p0i = p0.getRelativePos();
        MathExtra.PolarCoordinates polarP = new MathExtra.PolarCoordinates(new PointF(p0i.x, p0i.y), p);
        return MathExtra.betweenAngles(polarP.theta, getStartAngle(), getEndAngle()) && polarP.r <= DETECTION_ANGLE_R;
    }

    @Override
    public int getBarRes() {
        return R.layout.measure_element;
    }

    @NonNull
    @Override
    public PointF getStringValuePosition() {
        double angle = MathExtra.getMiddleAngle(getStartAngle(), getEndAngle());
        Point pos0 = p0.getRelativePos();
        return new PointF(
                (float) (pos0.x + MeasureSurface.ANGLE_CIRCLE_R * Math.cos(angle)),
                (float) (pos0.y + MeasureSurface.ANGLE_CIRCLE_R * Math.sin(angle))
        );
    }
}
