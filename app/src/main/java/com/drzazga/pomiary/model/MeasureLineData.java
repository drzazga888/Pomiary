package com.drzazga.pomiary.model;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drzazga.pomiary.utils.MathExtra;
import com.drzazga.pomiary.view.MeasureSurface;

import java.util.Locale;

public class MeasureLineData extends MeasureDataElement {

    public final static float DETECTION_LINE_R = (float) (MeasureSurface.STROKE_WIDTH * 3.0);

    public MeasurePointData p1, p2;

    public MeasureLineData(MeasurePointData p1, MeasurePointData p2, String name) {
        super(name);
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String toString() {
        return name + "(" + p1 + ", " + p2 + ")";
    }

    @Override
    public boolean isSelected(PointF p) {
        Matrix matrix = new Matrix();
        PointF pos1 = p1.getRelativePos();
        PointF pos2 = p2.getRelativePos();
        matrix.setRotate((float) -MathExtra.getAngleFromLinePoints(pos1, pos2));
        float[] mapped = new float[]{p.x, p.y, pos1.x, pos1.y, pos2.x, pos2.y};
        matrix.mapPoints(mapped);
        return (
                (MathExtra.between(mapped[0], mapped[2], mapped[4])) &&
                        (mapped[1] >= mapped[3] - DETECTION_LINE_R) &&
                        (mapped[1] <= mapped[3] + DETECTION_LINE_R)
        );
    }

    @Override
    public void performSelectedActon() {
        super.performSelectedActon();
        Log.i("touching", "line " + toString() + " is selected");
    }

    @Override
    public PointF getNamePosition() {
        return null;
    }

    @NonNull
    @Override
    public PointF getStringValuePosition() {
        PointF pos1 = p1.getRelativePos();
        PointF pos2 = p2.getRelativePos();
        return new PointF(
                (float) ((pos1.x + pos2.x) * 0.5),
                (float) ((pos1.y + pos2.y) * 0.5)
        );
    }

    @NonNull
    @Override
    public String getStringValue() {
        return String.format(
                Locale.getDefault(),
                "%.2f",
                MathExtra.distance(p1.getRelativePos(), p2.getRelativePos())
        );
    }
}
