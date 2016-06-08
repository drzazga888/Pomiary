package com.drzazga.pomiary.model;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.utils.MathExtra;
import com.drzazga.pomiary.view.MeasureSurface;

public class MeasurePointData extends MeasureDataElement {

    public final static float DETECTION_CIRCLE_R = (float) (MeasureSurface.POINT_CIRCLE_R * 2.0);

    public int x, y;
    public MeasurePointData relativeTo;

    public MeasurePointData(Integer id, int x, int y, String name, MeasurePointData relativeTo) {
        super(id, name);
        this.x = x;
        this.y = y;
        this.relativeTo = this instanceof MeasureStartPointData || relativeTo != null ? relativeTo : MeasureData.startPoint.get(0);
    }

    public Point getRelativePos() {
        Point relativeToAbsolutePos = relativeTo.getRelativePos();
        return new Point(x + relativeToAbsolutePos.x, y + relativeToAbsolutePos.y);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + x + ", " + y + ")";
    }

    @Override
    public boolean isSelected(PointF p) {
        Point circle = getRelativePos();
        return MathExtra.pow2(p.x - circle.x) + MathExtra.pow2(p.y - circle.y) <= MathExtra.pow2(DETECTION_CIRCLE_R);
    }

    @Override
    public int getBarRes() {
        return R.layout.measure_point;
    }

    @Override
    public void performSelectedActon() {
        super.performSelectedActon();
        Log.i("touching", "point " + toString() + " is selected");

    }

    @NonNull
    @Override
    public String getStringValue() {
        Point relPos = getRelativePos();
        return "(" + relPos.x + ", " + relPos.y + ")";
    }

    @NonNull
    @Override
    public PointF getStringValuePosition() {
        Point pos = getRelativePos();
        return new PointF(pos.x, pos.y);
    }
}
