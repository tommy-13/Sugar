package de.tommy13.sugar.database;

/**
 * Created by tommy on 05.03.2017.
 * Class to save a food item.
 */

public class FoodItem {

    private long   id;
    private int    year;
    private int    month;
    private int    day;
    private String category;
    private String note;
    private float  amount;
    private float  kcal;
    private float  sugar;
    private float  fat;
    private float  pieces;


    public FoodItem(long id, int year, int month, int day, String category, String note,
                    float amount, float kcal, float sugar, float fat, float pieces) {
        this.id       = id;
        this.year     = year;
        this.month    = month;
        this.day      = day;
        this.category = category;
        this.note     = note;
        this.amount   = amount;
        this.kcal     = kcal;
        this.sugar    = sugar;
        this.fat      = fat;
        this.pieces   = pieces;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    public float getAmount() {
        return amount;
    }

    public float getKcal() {
        return kcal;
    }

    public float getSugar() {
        return sugar;
    }

    public void setSugar(float sugar) {
        this.sugar = sugar;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getPieces() {
        return pieces;
    }

    public void setPieces(float pieces) {
        this.pieces = pieces;
    }
}
