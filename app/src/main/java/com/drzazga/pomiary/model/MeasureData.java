package com.drzazga.pomiary.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;

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
    private ContentResolver resolver;

    public MeasureData(Context context) {
        this.resolver = context.getContentResolver();
        loseFocus();
    }

    public void clear() {
        points.clear();
        lines.clear();
        angles.clear();
    }

    public void load(int measureId) {
        int id, p0, p1, p2, x, y, relativeTo;
        String name;
        Cursor c = resolver.query(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/point"), null, null, null, DatabaseContract.MeasurePoints._ID + " ASC");
        assert c != null;
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints._ID));
                x = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_X));
                y = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_Y));
                name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_NAME));
                relativeTo = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_RELATIVE_TO));
                points.put(id, new MeasurePointData(x, y, name, points.get(relativeTo)));
            } while (c.moveToNext());
        }
        c = resolver.query(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/line"), null, null, null, null);
        assert c != null;
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureLines._ID));
                p1 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureLines.COLUMN_POINT_1_ID));
                p2 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureLines.COLUMN_POINT_2_ID));
                name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.MeasureLines.COLUMN_NAME));
                lines.put(id, new MeasureLineData(points.get(p1), points.get(p2), name));
            } while (c.moveToNext());
        }
        c = resolver.query(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/angle"), null, null, null, null);
        assert c != null;
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles._ID));
                p0 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_0_ID));
                p1 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_1_ID));
                p2 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_2_ID));
                name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_NAME));
                angles.put(id, new MeasureAngleData(points.get(p0), points.get(p1), points.get(p2), name));
            } while (c.moveToNext());
        }
        c.close();
    }

    public boolean isSthSelected() {
        return sthSelected;
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
