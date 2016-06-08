package com.drzazga.pomiary.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.drzazga.pomiary.utils.StringExtra;

public class MeasureProvider extends ContentProvider {

    private SQLiteOpenHelper dbHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String AUTHORITY = "com.drzazga.pomiary.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final int CODE_MEASURE = 0;
    public static final int CODE_MEASURE_SINGLE = 1;
    public static final int CODE_CATEGORY = 2;
    public static final int CODE_CATEGORY_SINGLE = 3;
    public static final int CODE_MEASURE_POINT = 4;
    public static final int CODE_MEASURE_POINT_SINGLE = 5;
    public static final int CODE_MEASURE_LINE = 6;
    public static final int CODE_MEASURE_LINE_SINGLE = 7;
    public static final int CODE_MEASURE_ANGLE = 8;
    public static final int CODE_MEASURE_ANGLE_SINGLE = 9;

    static {
        uriMatcher.addURI(AUTHORITY, "measure", CODE_MEASURE);
        uriMatcher.addURI(AUTHORITY, "measure/#", CODE_MEASURE_SINGLE);
        uriMatcher.addURI(AUTHORITY, "category", CODE_CATEGORY);
        uriMatcher.addURI(AUTHORITY, "category/#", CODE_CATEGORY_SINGLE);
        uriMatcher.addURI(AUTHORITY, "measure/#/point", CODE_MEASURE_POINT);
        uriMatcher.addURI(AUTHORITY, "measure/#/point/#", CODE_MEASURE_POINT_SINGLE);
        uriMatcher.addURI(AUTHORITY, "measure/#/line", CODE_MEASURE_LINE);
        uriMatcher.addURI(AUTHORITY, "measure/#/line/#", CODE_MEASURE_LINE_SINGLE);
        uriMatcher.addURI(AUTHORITY, "measure/#/angle", CODE_MEASURE_ANGLE);
        uriMatcher.addURI(AUTHORITY, "measure/#/angle/#", CODE_MEASURE_ANGLE_SINGLE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case CODE_MEASURE:
                queryBuilder.setTables(DatabaseContract.ViewMeasures.TABLE_NAME);
                break;
            case CODE_MEASURE_SINGLE:
                queryBuilder.setTables(DatabaseContract.ViewMeasures.TABLE_NAME);
                queryBuilder.appendWhere(DatabaseContract.ViewMeasures._ID + " = " + uri.getLastPathSegment());
                break;
            case CODE_CATEGORY:
                queryBuilder.setTables(DatabaseContract.Categories.TABLE_NAME);
                break;
            case CODE_CATEGORY_SINGLE:
                queryBuilder.setTables(DatabaseContract.Categories.TABLE_NAME);
                queryBuilder.appendWhere(DatabaseContract.Categories._ID + " = " + uri.getLastPathSegment());
                break;
            case CODE_MEASURE_POINT:
                queryBuilder.setTables(DatabaseContract.MeasurePoints.TABLE_NAME);
                queryBuilder.appendWhere(DatabaseContract.MeasurePoints.COLUMN_MEASURE_ID + " = " + uri.getPathSegments().get(1));
                break;
            case CODE_MEASURE_LINE:
                queryBuilder.setTables(DatabaseContract.MeasureLines.TABLE_NAME);
                queryBuilder.appendWhere(DatabaseContract.MeasureLines.COLUMN_MEASURE_ID + " = " + uri.getPathSegments().get(1));
                break;
            case CODE_MEASURE_ANGLE:
                queryBuilder.setTables(DatabaseContract.MeasureAngles.TABLE_NAME);
                queryBuilder.appendWhere(DatabaseContract.MeasureAngles.COLUMN_MEASURE_ID + " = " + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return queryBuilder.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private ContentValues simplifyNameNullable(ContentValues values, String column) {
        String name = values.getAsString(column);
        if (name != null)
            values.put(column, StringExtra.simplify(name));
        return values;
    }

    private ContentValues simplifyName(ContentValues values, String column) {
        values = simplifyNameNullable(values, column);
        if (values.get(column) == null)
            throw new IllegalArgumentException();
        return values;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id, measureId;
        switch (uriMatcher.match(uri)) {
            case CODE_MEASURE:
                id = db.insertOrThrow(DatabaseContract.Measures.TABLE_NAME, null, simplifyName(
                        values, DatabaseContract.Measures.COLUMN_NAME
                ));
                return Uri.withAppendedPath(CONTENT_URI, "measure/" + String.valueOf(id));
            case CODE_CATEGORY:
                id = db.insertOrThrow(DatabaseContract.Categories.TABLE_NAME, null, simplifyName(
                        values, DatabaseContract.Categories.COLUMN_NAME
                ));
                return Uri.withAppendedPath(CONTENT_URI, "category/" + String.valueOf(id));
            case CODE_MEASURE_POINT:
                measureId = Integer.parseInt(uri.getPathSegments().get(1));
                values.put(DatabaseContract.MeasurePoints.COLUMN_MEASURE_ID, measureId);
                id = db.insertOrThrow(DatabaseContract.MeasurePoints.TABLE_NAME, null, values);
                return Uri.withAppendedPath(CONTENT_URI, "measure/" + measureId + "/point/" + id);
            case CODE_MEASURE_LINE:
                measureId = Integer.parseInt(uri.getPathSegments().get(1));
                values.put(DatabaseContract.MeasureLines.COLUMN_MEASURE_ID, measureId);
                id = db.insertOrThrow(DatabaseContract.MeasureLines.TABLE_NAME, null, values);
                return Uri.withAppendedPath(CONTENT_URI, "measure/" + measureId + "/line/" + id);
            case CODE_MEASURE_ANGLE:
                measureId = Integer.parseInt(uri.getPathSegments().get(1));
                values.put(DatabaseContract.MeasureAngles.COLUMN_MEASURE_ID, measureId);
                id = db.insertOrThrow(DatabaseContract.MeasureAngles.TABLE_NAME, null, values);
                return Uri.withAppendedPath(CONTENT_URI, "measure/" + measureId + "/angle/" + id);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CODE_MEASURE_SINGLE:
                selection = DatabaseContract.Measures._ID + " = " + uri.getLastPathSegment();
                return db.delete(DatabaseContract.Measures.TABLE_NAME, selection, null);
            case CODE_CATEGORY_SINGLE:
                selection = DatabaseContract.Categories._ID + " = " + uri.getLastPathSegment();
                return db.delete(DatabaseContract.Categories.TABLE_NAME, selection, null);
            case CODE_MEASURE_POINT_SINGLE:
                selection = DatabaseContract.MeasurePoints.COLUMN_MEASURE_ID + " = " + uri.getPathSegments().get(1)
                        + " AND " + DatabaseContract.MeasurePoints._ID + " = " + uri.getLastPathSegment();
                return db.delete(DatabaseContract.MeasurePoints.TABLE_NAME, selection, null);
            case CODE_MEASURE_LINE_SINGLE:
                selection = DatabaseContract.MeasureLines.COLUMN_MEASURE_ID + " = " + uri.getPathSegments().get(1)
                        + " AND " + DatabaseContract.MeasureLines._ID + " = " + uri.getLastPathSegment();
                return db.delete(DatabaseContract.MeasureLines.TABLE_NAME, selection, null);
            case CODE_MEASURE_ANGLE_SINGLE:
                selection = DatabaseContract.MeasureAngles.COLUMN_MEASURE_ID + " = " + uri.getPathSegments().get(1)
                        + " AND " + DatabaseContract.MeasureAngles._ID + " = " + uri.getLastPathSegment();
                return db.delete(DatabaseContract.MeasureAngles.TABLE_NAME, selection, null);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CODE_MEASURE_SINGLE:
                selection = DatabaseContract.Measures._ID + " = " + uri.getLastPathSegment();
                return db.update(DatabaseContract.Measures.TABLE_NAME, simplifyName(
                        values, DatabaseContract.Measures.COLUMN_NAME
                ), selection, null);
            case CODE_CATEGORY_SINGLE:
                selection = DatabaseContract.Categories._ID + " = " + uri.getLastPathSegment();
                return db.update(DatabaseContract.Categories.TABLE_NAME, simplifyName(
                        values, DatabaseContract.Categories.COLUMN_NAME
                ), selection, null);
            case CODE_MEASURE_POINT_SINGLE:
                selection = DatabaseContract.MeasurePoints.COLUMN_MEASURE_ID + " = "  + uri.getPathSegments().get(1)
                    + " AND " +  DatabaseContract.MeasurePoints._ID + " = " + uri.getLastPathSegment();
                return db.update(DatabaseContract.MeasurePoints.TABLE_NAME, simplifyNameNullable(
                        values, DatabaseContract.MeasurePoints.COLUMN_NAME
                ), selection, null);
            case CODE_MEASURE_LINE_SINGLE:
                selection = DatabaseContract.MeasureLines.COLUMN_MEASURE_ID + " = "  + uri.getPathSegments().get(1)
                        + " AND " +  DatabaseContract.MeasureLines._ID + " = " + uri.getLastPathSegment();
                return db.update(DatabaseContract.MeasureLines.TABLE_NAME, simplifyNameNullable(
                        values, DatabaseContract.MeasureLines.COLUMN_NAME
                ), selection, null);
            case CODE_MEASURE_ANGLE_SINGLE:
                selection = DatabaseContract.MeasureAngles.COLUMN_MEASURE_ID + " = "  + uri.getPathSegments().get(1)
                        + " AND " +  DatabaseContract.MeasureAngles._ID + " = " + uri.getLastPathSegment();
                return db.update(DatabaseContract.MeasureAngles.TABLE_NAME, simplifyNameNullable(
                        values, DatabaseContract.MeasureAngles.COLUMN_NAME
                ), selection, null);
            default:
                throw new IllegalArgumentException();
        }
    }
}
