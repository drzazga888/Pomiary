package com.drzazga.pomiary.model;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.view.ActionMode;
import android.view.Menu;

public abstract class MeasureDataElement {

    private boolean focused = false;
    public Integer id;
    public String name;

    MeasureDataElement(Integer id, String name) {
        this.id = id;
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

    @NonNull public abstract String getStringValue();

    public PointF getNamePosition() {
        PointF pos = getStringValuePosition();
        pos.y -= 30;
        return pos;
    }

    @NonNull
    public abstract PointF getStringValuePosition();

    public abstract boolean isSelected(PointF p);

    @LayoutRes
    public abstract int getBarRes();

    @Override
    public String toString() {
        return name + "#" + id;
    }
}
