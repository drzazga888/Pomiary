package com.drzazga.pomiary.model;

import android.provider.BaseColumns;

public final class DatabaseContract {

    public static abstract class Categories implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOR = "color";
    }

    public static abstract class Measures implements BaseColumns {
        public static final String TABLE_NAME = "measures";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_CREATION_DATE = "creation_date";
    }

    public static abstract class MeasurePoints implements BaseColumns {
        public static final String TABLE_NAME = "measure_points";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MEASURE_ID = "measure_id";
        public static final String COLUMN_X = "x";
        public static final String COLUMN_Y = "y";
        public static final String COLUMN_RELATIVE_TO = "relative_to";
    }

    public static abstract class MeasureLines implements BaseColumns {
        public static final String TABLE_NAME = "measure_lines";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MEASURE_ID = "measure_id";
        public static final String COLUMN_POINT_1_ID = "point_1_id";
        public static final String COLUMN_POINT_2_ID = "point_2_id";
    }

    public static abstract class MeasureAngles implements BaseColumns {
        public static final String TABLE_NAME = "measure_angles";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MEASURE_ID = "measure_id";
        public static final String COLUMN_POINT_0_ID = "point_0_id";
        public static final String COLUMN_POINT_1_ID = "point_1_id";
        public static final String COLUMN_POINT_2_ID = "point_2_id";
    }

    public static abstract class ViewMeasures implements BaseColumns {
        public static final String TABLE_NAME = "measures_view";
        public static final String COLUMN_NAME = Measures.COLUMN_NAME;
        public static final String COLUMN_CATEGORY_NAME = "category_name";
        public static final String COLUMN_CATEGORY_COLOR = "category_color";
        public static final String COLUMN_CREATION_DATE = Measures.COLUMN_CREATION_DATE;
    }

    public static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + Categories.TABLE_NAME + " (" +
                    Categories._ID + " INTEGER PRIMARY KEY," +
                    Categories.COLUMN_NAME + " TEXT UNIQUE NOT NULL," +
                    Categories.COLUMN_COLOR + " TEXT NOT NULL)";

    public static final String SQL_CREATE_MEASURES =
            "CREATE TABLE " + Measures.TABLE_NAME + " (" +
                    Measures._ID + " INTEGER PRIMARY KEY," +
                    Measures.COLUMN_NAME + " TEXT UNIQUE NOT NULL," +
                    Measures.COLUMN_CATEGORY_ID + " REFERENCES " + Categories.TABLE_NAME + "(" + Categories._ID + ") ON DELETE CASCADE, " +
                    Measures.COLUMN_CREATION_DATE + " INTEGER DEFAULT CURRENT_TIMESTAMP NOT NULL)";

    public static final String SQL_CREATE_MEASURE_POINTS =
            "CREATE TABLE " + MeasurePoints.TABLE_NAME + " (" +
                    MeasurePoints._ID + " INTEGER PRIMARY KEY," +
                    MeasurePoints.COLUMN_MEASURE_ID + " NOT NULL REFERENCES " + Measures.TABLE_NAME + "(" + Measures._ID + ") ON DELETE CASCADE, " +
                    MeasurePoints.COLUMN_NAME + " TEXT," +
                    MeasurePoints.COLUMN_X + " INTEGER NOT NULL," +
                    MeasurePoints.COLUMN_Y + " INTEGER NOT NULL," +
                    MeasurePoints.COLUMN_RELATIVE_TO + " REFERENCES " + MeasurePoints.TABLE_NAME + "(" + MeasurePoints._ID + ") ON DELETE CASCADE)";

    public static final String SQL_CREATE_MEASURE_LINES =
            "CREATE TABLE " + MeasureLines.TABLE_NAME + " (" +
                    MeasureLines._ID + " INTEGER PRIMARY KEY," +
                    MeasureLines.COLUMN_MEASURE_ID + " NOT NULL REFERENCES " + Measures.TABLE_NAME + "(" + Measures._ID + ") ON DELETE CASCADE, " +
                    MeasureLines.COLUMN_POINT_1_ID + " REFERENCES " + MeasurePoints.TABLE_NAME + "(" + MeasureLines._ID + ") ON DELETE CASCADE, " +
                    MeasureLines.COLUMN_POINT_2_ID + " REFERENCES " + MeasurePoints.TABLE_NAME + "(" + MeasureLines._ID + ") ON DELETE CASCADE, " +
                    MeasureLines.COLUMN_NAME + " TEXT)";

    public static final String SQL_CREATE_MEASURE_ANGLES =
            "CREATE TABLE " + MeasureAngles.TABLE_NAME + " (" +
                    MeasureAngles._ID + " INTEGER PRIMARY KEY," +
                    MeasureAngles.COLUMN_MEASURE_ID + " NOT NULL REFERENCES " + Measures.TABLE_NAME + "(" + Measures._ID + ") ON DELETE CASCADE, " +
                    MeasureAngles.COLUMN_POINT_0_ID + " REFERENCES " + MeasurePoints.TABLE_NAME + "(" + MeasureLines._ID + ") ON DELETE CASCADE, " +
                    MeasureAngles.COLUMN_POINT_1_ID + " REFERENCES " + MeasurePoints.TABLE_NAME + "(" + MeasureLines._ID + ") ON DELETE CASCADE, " +
                    MeasureAngles.COLUMN_POINT_2_ID + " REFERENCES " + MeasurePoints.TABLE_NAME + "(" + MeasureLines._ID + ") ON DELETE CASCADE, " +
                    MeasureAngles.COLUMN_NAME + " TEXT)";

    public static final String SQL_CREATE_VIEW_MEASURES =
            "CREATE VIEW " + ViewMeasures.TABLE_NAME + " AS SELECT " +
                    Measures.TABLE_NAME + "." + Measures._ID + " AS " + ViewMeasures._ID + ", " +
                    Measures.TABLE_NAME + "." + Measures.COLUMN_NAME + " AS " + ViewMeasures.COLUMN_NAME + ", " +
                    Categories.TABLE_NAME + "." + Categories.COLUMN_NAME + " AS " + ViewMeasures.COLUMN_CATEGORY_NAME + ", " +
                    Categories.TABLE_NAME + "." + Categories.COLUMN_COLOR + " AS " + ViewMeasures.COLUMN_CATEGORY_COLOR + ", " +
                    "datetime(" + Measures.TABLE_NAME + "." + Measures.COLUMN_CREATION_DATE + ") AS " + ViewMeasures.COLUMN_CREATION_DATE +
                    " FROM " + Measures.TABLE_NAME + " LEFT JOIN " + Categories.TABLE_NAME +
                    " ON " + Measures.TABLE_NAME + "." + Measures.COLUMN_CATEGORY_ID +
                    " = " + Categories.TABLE_NAME + "." + Categories._ID;

    public static final String SQL_DROP_VIEW_MEASURES = "DROP VIEW IF EXISTS " + ViewMeasures.TABLE_NAME;
    public static final String SQL_DROP_CATEGORIES = "DROP TABLE IF EXISTS " + Categories.TABLE_NAME;
    public static final String SQL_DROP_MEASURES = "DROP TABLE IF EXISTS " + Measures.TABLE_NAME;
    public static final String SQL_DROP_MEASURE_POINTS = "DROP TABLE IF EXISTS " + MeasurePoints.TABLE_NAME;
    public static final String SQL_DROP_MEASURE_LINES = "DROP TABLE IF EXISTS " + MeasureLines.TABLE_NAME;
    public static final String SQL_DROP_MEASURE_ANGLES = "DROP TABLE IF EXISTS " + MeasureAngles.TABLE_NAME;
}