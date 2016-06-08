package com.drzazga.pomiary.model;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.utils.MathExtra;
import com.drzazga.pomiary.view.MeasureSurface;

import java.util.Locale;

public class MeasureLineData extends MeasureDataElement {

    public final static float DETECTION_LINE_R = (float) (MeasureSurface.STROKE_WIDTH * 3.0);

    public MeasurePointData p1, p2;

    public MeasureLineData(Integer id, MeasurePointData p1, MeasurePointData p2, String name) {
        super(id, name);
        this.p1 = p1 != null ? p1 : MeasureData.startPoint.get(0);
        this.p2 = p2 != null ? p2 : MeasureData.startPoint.get(0);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + p1 + ", " + p2 + ")";
    }

    @Override
    public boolean isSelected(PointF p) {
        Matrix matrix = new Matrix();
        Point pos1 = p1.getRelativePos();
        Point pos2 = p2.getRelativePos();
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
    public int getBarRes() {
        return R.layout.measure_element;
    }

    @Override
    public void performSelectedActon() {
        super.performSelectedActon();
        Log.i("touching", "line " + toString() + " is selected");
    }

    @NonNull
    @Override
    public PointF getStringValuePosition() {
        Point pos1 = p1.getRelativePos();
        Point pos2 = p2.getRelativePos();
        return new PointF(
                (float) ((pos1.x + pos2.x) * 0.5),
                (float) ((pos1.y + pos2.y) * 0.5)
        );
    }

    @NonNull
    @Override
    public String getStringValue() {
        Point p1i = p1.getRelativePos();
        Point p2i = p2.getRelativePos();
        return String.format(
                Locale.getDefault(),
                "%.2f",
                MathExtra.distance(new PointF(p1i.x, p1i.y), new PointF(p2i.x, p2i.y))
        );
    }
}
