package de.tommy13.sugar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tommy on 05.03.2017.
 * Helper to manage a SQLite Database.
 */

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private static final String DB_NAME    = "food.db";
    private static final int    DB_VERSION = 1;

    static final String TABLE_CATEGORIES   = "tbl_categories";
    static final String TABLE_FOOD_HISTORY = "tbl_food_history";

    static final String COLUMN_ID       = "_id";

    static final String COLUMN_CAT_NAME = "name";
    static final String COLUMN_KCAL100  = "kcal100";
    static final String COLUMN_SUGAR100 = "sugar100";
    static final String COLUMN_FAT100   = "fat100";
    static final String COLUMN_AMOUNT_PER_PIECE = "amount_per_piece";

    static final String COLUMN_YEAR     = "year";
    static final String COLUMN_MONTH    = "month";
    static final String COLUMN_DAY      = "day";
    static final String COLUMN_CATEGORY = "category";
    static final String COLUMN_NOTE     = "note";
    static final String COLUMN_AMOUNT   = "amount";
    static final String COLUMN_ENERGY   = "energy";
    static final String COLUMN_SUGAR    = "sugar";
    static final String COLUMN_FAT      = "fat";
    static final String COLUMN_PIECES   = "pieces";

    private static final String SQL_CREATE_TBL_CATEGORIES =
            "CREATE TABLE " + TABLE_CATEGORIES + "(" +
                    COLUMN_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CAT_NAME + " TEXT NOT NULL, " +
                    COLUMN_KCAL100  + " FLOAT NOT NULL, " +
                    COLUMN_SUGAR100 + " FLOAT NOT NULL, " +
                    COLUMN_FAT100   + " FLOAT NOT NULL, " +
                    COLUMN_AMOUNT_PER_PIECE   + " FLOAT NOT NULL);";

    private static final String SQL_CREATE_TBL_FOOD_HISTORY =
            "CREATE TABLE " + TABLE_FOOD_HISTORY + "(" +
                    COLUMN_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_YEAR     + " INTEGER NOT NULL, " +
                    COLUMN_MONTH    + " INTEGER NOT NULL, " +
                    COLUMN_DAY      + " INTEGER NOT NULL, " +
                    COLUMN_CATEGORY + " TEXT NOT NULL, " +
                    COLUMN_NOTE     + " TEXT NOT NULL, " +
                    COLUMN_AMOUNT   + " FLOAT NOT NULL, " +
                    COLUMN_ENERGY   + " FLOAT NOT NULL, " +
                    COLUMN_SUGAR    + " FLOAT NOT NULL, " +
                    COLUMN_FAT      + " FLOAT NOT NULL, " +
                    COLUMN_PIECES   + " FLOAT NOT NULL);";


    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DatabaseHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(SQL_CREATE_TBL_CATEGORIES);
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: Table " + TABLE_CATEGORIES + " not created successfully.");
            e.printStackTrace();
        }

        try {
            sqLiteDatabase.execSQL(SQL_CREATE_TBL_FOOD_HISTORY);
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: Table " + TABLE_FOOD_HISTORY + " not created successfully.");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
