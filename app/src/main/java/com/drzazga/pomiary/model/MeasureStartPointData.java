package com.drzazga.pomiary.model;

import android.graphics.Point;
import android.support.annotation.NonNull;

public class MeasureStartPointData extends MeasurePointData {

    public MeasureStartPointData() {
        super(null, 0, 0, null, null);
    }

    @Override
    public Point getRelativePos() {
        return new Point();
    }

    @NonNull
    @Override
    public String getStringValue() {
        return "";
    }

}
