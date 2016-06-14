package com.drzazga.pomiary.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MeasureData implements Iterable<MeasureDataElement> {

    public static final List<MeasureStartPointData> startPoint = Collections.singletonList(new MeasureStartPointData());
    public HashMap<Integer, MeasurePointData> points = new HashMap<>();
    public HashMap<Integer, MeasureLineData> lines = new HashMap<>();
    public HashMap<Integer, MeasureAngleData> angles = new HashMap<>();
    private ContentResolver resolver;
    private int measureId;

    public MeasureData(Context context, int id) {
        this.measureId = id;
        this.resolver = context.getContentResolver();
    }

    public void load() {
        int id, p0, p1, p2, x, y, relativeTo;
        String name;
        points.clear();
        lines.clear();
        angles.clear();
        Cursor c = resolver.query(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/point"), null, null, null, DatabaseContract.MeasurePoints._ID + " ASC");
        assert c != null;
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints._ID));
                x = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_X));
                y = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_Y));
                name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_NAME));
                relativeTo = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_RELATIVE_TO));
                points.put(id, new MeasurePointData(id, x, y, name, points.get(relativeTo)));
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
                lines.put(id, new MeasureLineData(id, points.get(p1), points.get(p2), name));
            } while (c.moveToNext());
        }
        c = resolver.query(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/angle"), null, null, null, null);
        assert c != null;
        Log.i("db_test", DatabaseUtils.dumpCursorToString(c));
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles._ID));
                p0 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_0_ID));
                p1 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_1_ID));
                p2 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_2_ID));
                name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_NAME));
                angles.put(id, new MeasureAngleData(id, points.get(p0), points.get(p1), points.get(p2), name));
            } while (c.moveToNext());
        }
        c.close();
    }

    public int addPoint(Integer relativeTo) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MeasurePoints.COLUMN_X, 100);
        values.put(DatabaseContract.MeasurePoints.COLUMN_Y, 100);
        values.put(DatabaseContract.MeasurePoints.COLUMN_RELATIVE_TO, relativeTo);
        Uri uri = resolver.insert(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/point"), values);
        assert uri != null;
        return Integer.parseInt(uri.getPathSegments().get(3));
    }

    public int addLine(Integer p1, Integer p2) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MeasureLines.COLUMN_POINT_1_ID, p1);
        values.put(DatabaseContract.MeasureLines.COLUMN_POINT_2_ID, p2);
        Uri uri = resolver.insert(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/line"), values);
        assert uri != null;
        return Integer.parseInt(uri.getPathSegments().get(3));
    }

    public int addAngle(Integer p0, Integer p1, Integer p2) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MeasureAngles.COLUMN_POINT_0_ID, p0);
        values.put(DatabaseContract.MeasureAngles.COLUMN_POINT_1_ID, p1);
        values.put(DatabaseContract.MeasureAngles.COLUMN_POINT_2_ID, p2);
        Uri uri = resolver.insert(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/angle"), values);
        assert uri != null;
        return Integer.parseInt(uri.getPathSegments().get(3));
    }

    public void update(MeasureDataElement selectedItem) {
        ContentValues values = new ContentValues();
        String elementClass = "";
        values.put(DatabaseContract.MeasurePoints.COLUMN_NAME, selectedItem.name);
        if (selectedItem instanceof MeasurePointData) {
            MeasurePointData casted = (MeasurePointData) selectedItem;
            values.put(DatabaseContract.MeasurePoints.COLUMN_X, casted.x);
            values.put(DatabaseContract.MeasurePoints.COLUMN_Y, casted.y);
            elementClass = "point";
        } else if (selectedItem instanceof MeasureLineData)
            elementClass = "line";
        else if (selectedItem instanceof MeasureAngleData) {
            MeasureAngleData casted = (MeasureAngleData) selectedItem;
            values.put(DatabaseContract.MeasureAngles.COLUMN_POINT_1_ID, casted.p1.id);
            values.put(DatabaseContract.MeasureAngles.COLUMN_POINT_2_ID, casted.p2.id);
            elementClass = "angle";
        }
        resolver.update(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/" + elementClass + "/" + selectedItem.id), values, null, null);
    }

    public void delete(MeasureDataElement selectedItem) {
        String elementClass = "";
        if (selectedItem instanceof MeasurePointData) {
            elementClass = "point";
        } else if (selectedItem instanceof MeasureLineData)
            elementClass = "line";
        else if (selectedItem instanceof MeasureAngleData)
            elementClass = "angle";
        resolver.delete(Uri.withAppendedPath(MeasureProvider.CONTENT_URI, "measure/" + measureId + "/" + elementClass + "/" + selectedItem.id), null, null);
    }

    @Override
    public String toString() {
        return "points = " + points + ", lines = " + lines + ", angles = " + angles + "";
    }

    @Override
    public Iterator<MeasureDataElement> iterator() {
        return new Iterator<MeasureDataElement>() {

            private int state = 0;
            private Iterator<MeasurePointData> pointsIterator = points.values().iterator();
            private Iterator<MeasureStartPointData> nullPointIterator = startPoint.iterator();
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
