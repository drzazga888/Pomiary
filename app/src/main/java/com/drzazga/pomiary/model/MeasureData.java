package com.drzazga.pomiary.model;

import android.graphics.PointF;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MeasureData implements Iterable<MeasureDataElement> {

    public static final List<MeasurePointData> startPoint = Collections.singletonList(new MeasurePointData(0, 0, null, null));
    public HashMap<Integer, MeasurePointData> points = new HashMap<>();
    public HashMap<Integer, MeasureLineData> lines = new HashMap<>();
    public HashMap<Integer, MeasureAngleData> angles = new HashMap<>();
    private boolean sthSelected = false;

    public boolean isSthSelected() {
        return sthSelected;
    }

    public MeasureData() {
        loseFocus();
    }

    @Override
    public String toString() {
        return "points = " + points + ", lines = " + lines + ", angles = " + angles + "";
    }

    private void loseFocus() {
        for (MeasureDataElement item : this) {
            if (item.isFocused()) {
                item.loseFocus();
                sthSelected = false;
                return;
            }
        }
    }

    public boolean resolveSelected(PointF p) {
        loseFocus();
        for (MeasureDataElement item : this) {
            if (item.isSelected(p)) {
                item.performSelectedActon();
                sthSelected = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<MeasureDataElement> iterator() {
        return new Iterator<MeasureDataElement>() {

            private int state = 0;
            private Iterator<MeasurePointData> pointsIterator = points.values().iterator();
            private Iterator<MeasurePointData> nullPointIterator = startPoint.iterator();
            private Iterator<MeasureLineData> linesIterator = lines.values().iterator();
            private Iterator<MeasureAngleData> anglesIterator = angles.values().iterator();

            @Override
            public boolean hasNext() {
                switch (state) {
                    case 0:
                        if (pointsIterator.hasNext())
                            return true;
                        else {
                            ++state;
                            return hasNext();
                        }
                    case 1:
                        if (nullPointIterator.hasNext())
                            return true;
                        else {
                            ++state;
                            return hasNext();
                        }
                    case 2:
                        if (linesIterator.hasNext())
                            return true;
                        else {
                            ++state;
                            return hasNext();
                        }
                    case 3:
                        return anglesIterator.hasNext();
                }
                return true;
            }

            @Override
            public MeasureDataElement next() {
                switch (state) {
                    case 0:
                        return pointsIterator.next();
                    case 1:
                        return nullPointIterator.next();
                    case 2:
                        return linesIterator.next();
                    case 3:
                        return anglesIterator.next();
                }
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
