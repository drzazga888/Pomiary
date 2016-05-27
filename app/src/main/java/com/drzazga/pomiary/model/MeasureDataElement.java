package com.drzazga.pomiary.model;

import android.graphics.PointF;
import android.support.annotation.NonNull;

public abstract class MeasureDataElement {

    private boolean focused = false;
    public String name;

    MeasureDataElement(String name) {
        this.name = name;
    }

    public void performSelectedActon() {
        focused = true;
    }

    public boolean isFocused() {
        return focused;
    }

    public void loseFocus() {
        focused = false;
    }

    public String getName() {
        return name;
    }

    @NonNull public String getStringValue() {
        return "";
    }

    public abstract PointF getNamePosition();
    @NonNull public abstract PointF getStringValuePosition();
    public abstract boolean isSelected(PointF p);
}
