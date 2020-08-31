package de.tommy13.sugar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tommy on 05.03.2017.
 * Access class to the database.
 */

public class SugarDataSource {


    /*-------------------------------------------------------------------------------*/
    /*--------------------- GENERAL DATABASE ----------------------------------------*/
    /*-------------------------------------------------------------------------------*/
    private static final String LOG_TAG = SugarDataSource.class.getSimpleName();

    private Context        context;
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;


    public static SugarDataSource newInstance(Context context) {
        SugarDataSource dataSource = new SugarDataSource();
        dataSource.context         = context;
        dataSource.databaseHelper  = new DatabaseHelper(context);
        Log.d(LOG_TAG, "Helper for the database is created.");
        return dataSource;
    }









    /*-------------------------------------------------------------------------------*/
    /*--------------------- CATEGORIES ----------------------------------------------*/
    /*-------------------------------------------------------------------------------*/
    private String[] columnsCategory = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_CAT_NAME,
            DatabaseHelper.COLUMN_KCAL100,
            DatabaseHelper.COLUMN_SUGAR100,
            DatabaseHelper.COLUMN_FAT100,
            DatabaseHelper.COLUMN_AMOUNT_PER_PIECE
    };


    CategoryItem createCategoryItem(String categoryName, float kcal100, float sugar100, float fat100, float amountPerPiece) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CAT_NAME, categoryName);
        values.put(DatabaseHelper.COLUMN_KCAL100, kcal100);
        values.put(DatabaseHelper.COLUMN_SUGAR100, sugar100);
        values.put(DatabaseHelper.COLUMN_FAT100, fat100);
        values.put(DatabaseHelper.COLUMN_AMOUNT_PER_PIECE, amountPerPiece);

        database = databaseHelper.getWritableDatabase();
        long insertId = database.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_CATEGORIES, columnsCategory,
                DatabaseHelper.COLUMN_ID + "=" + insertId, null, null, null, null);

        cursor.moveToFirst();
        CategoryItem categoryItem = cursorToCategoryItem(cursor);
        cursor.close();
        databaseHelper.close();

        return categoryItem;
    }

    private CategoryItem cursorToCategoryItem(Cursor cursor) {
        int idIndex    = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int idCatName  = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAT_NAME);
        int idKcal100  = cursor.getColumnIndex(DatabaseHelper.COLUMN_KCAL100);
        int idSugar100 = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUGAR100);
        int idFat100   = cursor.getColumnIndex(DatabaseHelper.COLUMN_FAT100);
        int idAmountPP = cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT_PER_PIECE);

        long id        = cursor.getLong(idIndex);
        String catName = cursor.getString(idCatName);
        float kcal100  = cursor.getFloat(idKcal100);
        float sugar100 = cursor.getFloat(idSugar100);
        float fat100   = cursor.getFloat(idFat100);
        float amountPP = cursor.getFloat(idAmountPP);

        return new CategoryItem(id, catName, kcal100, sugar100, fat100, amountPP);
    }

    ArrayList<CategoryItem> getAllCategories() {
        ArrayList<CategoryItem> categories = new ArrayList<>();

        database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CATEGORIES, columnsCategory,
                null, null, null, null, null);
        cursor.moveToFirst();

        CategoryItem categoryItem;
        while (!cursor.isAfterLast()) {
            categoryItem = cursorToCategoryItem(cursor);
            categories.add(categoryItem);
            cursor.moveToNext();
        }
        cursor.close();
        databaseHelper.close();

        return categories;
    }


    CategoryItem updateCategoryItem(long id, String categoryName, float kcal100, float sugar100, float fat100, float amountPerPiece) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CAT_NAME, categoryName);
        values.put(DatabaseHelper.COLUMN_KCAL100, kcal100);
        values.put(DatabaseHelper.COLUMN_SUGAR100, sugar100);
        values.put(DatabaseHelper.COLUMN_FAT100, fat100);
        values.put(DatabaseHelper.COLUMN_AMOUNT_PER_PIECE, amountPerPiece);

        database = databaseHelper.getWritableDatabase();
        database.update(DatabaseHelper.TABLE_CATEGORIES, values, DatabaseHelper.COLUMN_ID + "=" + id, null);

        Cursor cursor = database.query(DatabaseHelper.TABLE_CATEGORIES, columnsCategory,
                DatabaseHelper.COLUMN_ID + "=" + id, null, null, null, null);

        cursor.moveToFirst();
        CategoryItem categoryItem = cursorToCategoryItem(cursor);
        cursor.close();
        databaseHelper.close();

        return categoryItem;
    }


    void deleteCategoryItem(CategoryItem item) {
        long id = item.getId();

        database = databaseHelper.getWritableDatabase();
        database.delete(DatabaseHelper.TABLE_CATEGORIES, DatabaseHelper.COLUMN_ID + "=" + id,null);
        databaseHelper.close();

        Log.d(LOG_TAG, "Deleted entry with name " + item.getName());
    }


    void deleteAllCategories() {
        database = databaseHelper.getReadableDatabase();
        database.delete(DatabaseHelper.TABLE_CATEGORIES, null, null);
        databaseHelper.close();
    }

    void resetCategories() {
        database = databaseHelper.getWritableDatabase();
        database.delete(DatabaseHelper.TABLE_CATEGORIES, null, null);

        ArrayList<CategoryItem> categoryItems = StandardCategories.get(context);
        for (CategoryItem categoryItem : categoryItems) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_CAT_NAME, categoryItem.getName());
            values.put(DatabaseHelper.COLUMN_KCAL100, categoryItem.getKcal100());
            values.put(DatabaseHelper.COLUMN_SUGAR100, categoryItem.getSugar100());
            values.put(DatabaseHelper.COLUMN_FAT100, categoryItem.getFat100());
            values.put(DatabaseHelper.COLUMN_AMOUNT_PER_PIECE, categoryItem.getAmountPerPiece());
            database.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
        }

        databaseHelper.close();
    }


    public void setCategoriesOnFirstUse() {
        database = databaseHelper.getWritableDatabase();

        ArrayList<CategoryItem> categoryItems = StandardCategories.get(context);
        for (CategoryItem categoryItem : categoryItems) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_CAT_NAME, categoryItem.getName());
            values.put(DatabaseHelper.COLUMN_KCAL100, categoryItem.getKcal100());
            values.put(DatabaseHelper.COLUMN_SUGAR100, categoryItem.getSugar100());
            values.put(DatabaseHelper.COLUMN_FAT100, categoryItem.getFat100());
            values.put(DatabaseHelper.COLUMN_AMOUNT_PER_PIECE, categoryItem.getAmountPerPiece());
            database.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
        }

        databaseHelper.close();
    }











    /*-------------------------------------------------------------------------------*/
    /*--------------------- FOOD ----------------------------------------------------*/
    /*-------------------------------------------------------------------------------*/
    private String[] columnsFoodHistory = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_YEAR,
            DatabaseHelper.COLUMN_MONTH,
            DatabaseHelper.COLUMN_DAY,
            DatabaseHelper.COLUMN_CATEGORY,
            DatabaseHelper.COLUMN_NOTE,
            DatabaseHelper.COLUMN_AMOUNT,
            DatabaseHelper.COLUMN_ENERGY,
            DatabaseHelper.COLUMN_SUGAR,
            DatabaseHelper.COLUMN_FAT,
            DatabaseHelper.COLUMN_PIECES
    };


    FoodItem createFoodItem(int year, int month, int day, String category, String note,
                            float amount, float kcal, float sugar, float fat, float pieces) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_YEAR, year);
        values.put(DatabaseHelper.COLUMN_MONTH, month);
        values.put(DatabaseHelper.COLUMN_DAY, day);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_NOTE, note);
        values.put(DatabaseHelper.COLUMN_AMOUNT, amount);
        values.put(DatabaseHelper.COLUMN_ENERGY, kcal);
        values.put(DatabaseHelper.COLUMN_SUGAR, sugar);
        values.put(DatabaseHelper.COLUMN_FAT, fat);
        values.put(DatabaseHelper.COLUMN_PIECES, pieces);

        database = databaseHelper.getWritableDatabase();
        long insertId = database.insert(DatabaseHelper.TABLE_FOOD_HISTORY, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_FOOD_HISTORY, columnsFoodHistory,
                DatabaseHelper.COLUMN_ID + "=" + insertId, null, null, null, null);

        cursor.moveToFirst();
        FoodItem foodItem = cursorToFoodItem(cursor);
        cursor.close();
        databaseHelper.close();

        return foodItem;
    }

    private FoodItem cursorToFoodItem(Cursor cursor) {
        int idIndex    = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int idYear     = cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR);
        int idMonth    = cursor.getColumnIndex(DatabaseHelper.COLUMN_MONTH);
        int idDay      = cursor.getColumnIndex(DatabaseHelper.COLUMN_DAY);
        int idCategory = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);
        int idNote     = cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE);
        int idAmount   = cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT);
        int idKcal     = cursor.getColumnIndex(DatabaseHelper.COLUMN_ENERGY);
        int idSugar    = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUGAR);
        int idFat      = cursor.getColumnIndex(DatabaseHelper.COLUMN_FAT);
        int idPieces   = cursor.getColumnIndex(DatabaseHelper.COLUMN_PIECES);

        long   id       = cursor.getLong(idIndex);
        int    year     = cursor.getInt(idYear);
        int    month    = cursor.getInt(idMonth);
        int    day      = cursor.getInt(idDay);
        String category = cursor.getString(idCategory);
        String note     = cursor.getString(idNote);
        float  amount   = cursor.getFloat(idAmount);
        float  kcal     = cursor.getFloat(idKcal);
        float  sugar    = cursor.getFloat(idSugar);
        float  fat      = cursor.getFloat(idFat);
        float  pieces   = cursor.getFloat(idPieces);

        return new FoodItem(id, year, month, day, category, note, amount, kcal, sugar, fat, pieces);
    }



    ArrayList<FoodItem> getFoodItems(int year, int month, int day) {
        ArrayList<FoodItem> foodItems = new ArrayList<>();

        database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_FOOD_HISTORY,
                columnsFoodHistory,
                DatabaseHelper.COLUMN_YEAR + "=" + year + " AND " +
                        DatabaseHelper.COLUMN_MONTH + "=" + month + " AND " +
                        DatabaseHelper.COLUMN_DAY + "=" + day,
                null, null, null, null);
        cursor.moveToFirst();

        FoodItem foodItem;
        while (!cursor.isAfterLast()) {
            foodItem = cursorToFoodItem(cursor);
            foodItems.add(foodItem);
            cursor.moveToNext();
        }
        cursor.close();
        databaseHelper.close();

        return foodItems;
    }




    FoodItem updateFoodItem(long id, int year, int month, int day, String category, String note,
                            float amount, float kcal, float sugar, float fat, float pieces) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_YEAR, year);
        values.put(DatabaseHelper.COLUMN_MONTH, month);
        values.put(DatabaseHelper.COLUMN_DAY, day);
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);
        values.put(DatabaseHelper.COLUMN_NOTE, note);
        values.put(DatabaseHelper.COLUMN_AMOUNT, amount);
        values.put(DatabaseHelper.COLUMN_ENERGY, kcal);
        values.put(DatabaseHelper.COLUMN_SUGAR, sugar);
        values.put(DatabaseHelper.COLUMN_FAT, fat);
        values.put(DatabaseHelper.COLUMN_PIECES, pieces);

        database = databaseHelper.getWritableDatabase();
        database.update(DatabaseHelper.TABLE_FOOD_HISTORY, values, DatabaseHelper.COLUMN_ID + "=" + id, null);

        Cursor cursor = database.query(DatabaseHelper.TABLE_FOOD_HISTORY, columnsFoodHistory,
                DatabaseHelper.COLUMN_ID + "=" + id, null, null, null, null);

        cursor.moveToFirst();
        FoodItem foodItem = cursorToFoodItem(cursor);
        cursor.close();
        databaseHelper.close();

        return foodItem;
    }


    void deleteFoodItem(FoodItem item) {
        long id = item.getId();

        database = databaseHelper.getWritableDatabase();
        database.delete(DatabaseHelper.TABLE_FOOD_HISTORY, DatabaseHelper.COLUMN_ID + "=" + id,null);
        databaseHelper.close();

        Log.d(LOG_TAG, "Deleted entry with id " + item.getId());
    }


    void deleteFoodItemsUntil(int year, int month, int day) {

        String whereClause = DatabaseHelper.COLUMN_YEAR + "<=" + year + " AND " +
                DatabaseHelper.COLUMN_MONTH + "<=" + month + " AND " +
                DatabaseHelper.COLUMN_DAY + "<=" + day;
        database = databaseHelper.getWritableDatabase();
        database.delete(DatabaseHelper.TABLE_FOOD_HISTORY, whereClause, null);
        databaseHelper.close();

        Log.d(LOG_TAG, "Deleted items");
    }

}
