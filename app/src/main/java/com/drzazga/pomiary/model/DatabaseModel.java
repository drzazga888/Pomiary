package com.drzazga.pomiary.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

public class DatabaseModel {

    private DatabaseDbHelper dbHelper;

    public DatabaseModel(Context context) {
        dbHelper = new DatabaseDbHelper(context);
    }

    public void populate(int measureId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MeasurePoints.COLUMN_MEASURE_ID, measureId);
        values.put(DatabaseContract.MeasurePoints.COLUMN_X, -130);
        values.put(DatabaseContract.MeasurePoints.COLUMN_Y, 120);
        long point1Id = db.insert(DatabaseContract.MeasurePoints.TABLE_NAME, null, values);
        Log.i("db_test", "Wstawiono nowy rekord do punktów, id = " + point1Id);

        values.put(DatabaseContract.MeasurePoints.COLUMN_NAME, "nazwany punkt");
        values.put(DatabaseContract.MeasurePoints.COLUMN_X, 171);
        values.put(DatabaseContract.MeasurePoints.COLUMN_Y, 152);
        long point2Id = db.insert(DatabaseContract.MeasurePoints.TABLE_NAME, null, values);
        Log.i("db_test", "Wstawiono nowy rekord do punktów, id = " + point2Id);

        values.put(DatabaseContract.MeasurePoints.COLUMN_NAME, "kolejny pkt");
        values.put(DatabaseContract.MeasurePoints.COLUMN_X, 100);
        values.put(DatabaseContract.MeasurePoints.COLUMN_Y, -71);
        long point3Id = db.insert(DatabaseContract.MeasurePoints.TABLE_NAME, null, values);
        Log.i("db_test", "Wstawiono nowy rekord do punktów, id = " + point3Id);

        values = new ContentValues();
        values.put(DatabaseContract.MeasureLines.COLUMN_MEASURE_ID, measureId);
        values.put(DatabaseContract.MeasureLines.COLUMN_POINT_1_ID, point1Id);
        values.put(DatabaseContract.MeasureLines.COLUMN_POINT_2_ID, point2Id);
        long line1Id = db.insert(DatabaseContract.MeasureLines.TABLE_NAME, null, values);
        Log.i("db_test", "Wstawiono nowy rekord do linii, id = " + line1Id);

        values.put(DatabaseContract.MeasureLines.COLUMN_NAME, "jakaś linia");
        values.put(DatabaseContract.MeasureLines.COLUMN_MEASURE_ID, measureId);
        values.put(DatabaseContract.MeasureLines.COLUMN_POINT_1_ID, point1Id);
        values.put(DatabaseContract.MeasureLines.COLUMN_POINT_2_ID, point3Id);
        long line2Id = db.insert(DatabaseContract.MeasureLines.TABLE_NAME, null, values);
        Log.i("db_test", "Wstawiono nowy rekord do linii, id = " + line2Id);

        values = new ContentValues();
        values.put(DatabaseContract.MeasureAngles.COLUMN_MEASURE_ID, measureId);
        values.put(DatabaseContract.MeasureAngles.COLUMN_NAME, "kąt");
        values.put(DatabaseContract.MeasureAngles.COLUMN_POINT_0_ID, point1Id);
        values.put(DatabaseContract.MeasureAngles.COLUMN_POINT_1_ID, point2Id);
        values.put(DatabaseContract.MeasureAngles.COLUMN_POINT_2_ID, point3Id);
        long angleId = db.insert(DatabaseContract.MeasureAngles.TABLE_NAME, null, values);
        Log.i("db_test", "Wstawiono nowy rekord do kątów, id = " + angleId);
    }

    @NonNull
    private Cursor getMeasurePointCursor(int measureId) {
        return dbHelper.getReadableDatabase().query(
                DatabaseContract.MeasurePoints.TABLE_NAME,
                null,
                DatabaseContract.MeasurePoints.COLUMN_MEASURE_ID + " = " + measureId,
                null,
                null,
                null,
                DatabaseContract.MeasurePoints.COLUMN_RELATIVE_TO + " ASC"
        );
    }

    @NonNull
    private Cursor getMeasureLineCursor(int measureId) {
        return dbHelper.getReadableDatabase().query(
                DatabaseContract.MeasureLines.TABLE_NAME,
                null,
                DatabaseContract.MeasureLines.COLUMN_MEASURE_ID + " = " + measureId,
                null,
                null,
                null,
                null
        );
    }

    @NonNull
    private Cursor getMeasureAngleCursor(int measureId) {
        return dbHelper.getReadableDatabase().query(
                DatabaseContract.MeasureAngles.TABLE_NAME,
                null,
                DatabaseContract.MeasureAngles.COLUMN_MEASURE_ID + " = " + measureId,
                null,
                null,
                null,
                null
        );
    }

    public MeasureData getMeasureData(int measureId) {
        int id, p0, p1, p2, x, y, relativeTo;
        String name;
        MeasureData data = new MeasureData();
        Cursor c = getMeasurePointCursor(measureId);
        Log.i("db_test", String.valueOf(c.getCount()));
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints._ID));
                x = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_X));
                y = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_Y));
                name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_NAME));
                relativeTo = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasurePoints.COLUMN_RELATIVE_TO));
                data.points.put(id, new MeasurePointData(x, y, name, data.points.get(relativeTo)));
            } while (c.moveToNext());
        }
        c = getMeasureLineCursor(measureId);
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureLines._ID));
                p1 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureLines.COLUMN_POINT_1_ID));
                p2 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureLines.COLUMN_POINT_2_ID));
                name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.MeasureLines.COLUMN_NAME));
                data.lines.put(id, new MeasureLineData(data.points.get(p1), data.points.get(p2), name));
            } while (c.moveToNext());
        }
        c = getMeasureAngleCursor(measureId);
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles._ID));
                p0 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_0_ID));
                p1 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_1_ID));
                p2 = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_POINT_2_ID));
                name = c.getString(c.getColumnIndexOrThrow(DatabaseContract.MeasureAngles.COLUMN_NAME));
                data.angles.put(id, new MeasureAngleData(data.points.get(p0), data.points.get(p1), data.points.get(p2), name));
            } while (c.moveToNext());
        }
        return data;
    }

    @NonNull
    public Cursor getMeasureCursor() {
        return dbHelper.getReadableDatabase().query(
                DatabaseContract.ViewMeasures.TABLE_NAME, null, null, null, null, null, null
        );
    }

    @NonNull
    public Cursor getMeasureCursor(int id) {
        return dbHelper.getReadableDatabase().query(
                DatabaseContract.ViewMeasures.TABLE_NAME,
                null,
                DatabaseContract.ViewMeasures._ID + " = " + id,
                null,
                null,
                null,
                null
        );
    }

    @NonNull
    public Cursor getCategoryCursor() {
        return dbHelper.getReadableDatabase().query(
                DatabaseContract.Categories.TABLE_NAME, null, null, null, null, null, null
        );
    }

    public long addMeasure(String name, Integer categoryId) throws SQLiteConstraintException {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Measures.COLUMN_NAME, name);
        values.put(DatabaseContract.Measures.COLUMN_CATEGORY_ID, categoryId);
        long id = dbHelper.getWritableDatabase().insert(DatabaseContract.Measures.TABLE_NAME, null, values);
        populate((int) id);
        return id;
    }

    public void removeMeasure(int id) {
        Log.i("db_test", "Usuwanie pomiaru, id = " + id);
    }

    public void changeCategory(int measureId, Integer categoryId) {
        Log.i("db_test", "Zmiana kategorii, measureId = " + measureId + ", categoryId = " + categoryId);
    }

}
