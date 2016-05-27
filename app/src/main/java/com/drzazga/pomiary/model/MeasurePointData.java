package com.drzazga.pomiary.model;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drzazga.pomiary.utils.MathExtra;
import com.drzazga.pomiary.view.MeasureSurface;

public class MeasurePointData extends MeasureDataElement {

    public final static float DETECTION_CIRCLE_R = (float) (MeasureSurface.POINT_CIRCLE_R * 2.0);

    public int x, y;
    public MeasurePointData relativeTo;

    public MeasurePointData(int x, int y, String name, MeasurePointData relativeTo) {
        super(name);
        this.x = x;
        this.y = y;
        this.relativeTo = relativeTo;
    }

    public PointF getRelativePos() {
        return new PointF(
                x + (relativeTo != null ? relativeTo.x : MeasureSurface.POINT_START.x),
                y + (relativeTo != null ? relativeTo.y : MeasureSurface.POINT_START.y)
        );
    }

    @Override
    public String toString() {
        return name + "(" + x + ", " + y + ")";
    }

    @Override
    public boolean isSelected(PointF p) {
        PointF circle = getRelativePos();
        return MathExtra.pow2(p.x - circle.x) + MathExtra.pow2(p.y - circle.y) <= MathExtra.pow2(DETECTION_CIRCLE_R);
    }

    @Override
    public void performSelectedActon() {
        super.performSelectedActon();
        Log.i("touching", "point " + toString() + " is selected");
    }

    @Override
    public PointF getNamePosition() {
        return null;
    }

    @NonNull
    @Override
    public PointF getStringValuePosition() {
        return getRelativePos();
    }
}
