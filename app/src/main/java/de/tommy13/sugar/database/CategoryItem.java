package de.tommy13.sugar.database;

/**
 * Created by tommy on 05.03.2017.
 * Class to save a category.
 */

public class CategoryItem {

    private long   id;
    private String name;
    private float  kcal100;
    private float  sugar100;
    private float  fat100;
    private float  amountPerPiece;


    public CategoryItem(long id, String name, float kcal100, float sugar100, float fat100, float amountPerPiece) {
        this.id       = id;
        this.name     = name;
        this.kcal100  = kcal100;
        this.sugar100 = sugar100;
        this.fat100   = fat100;
        this.amountPerPiece = amountPerPiece;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getKcal100() {
        return kcal100;
    }

    public float getSugar100() {
        return sugar100;
    }

    public float getFat100() {
        return fat100;
    }

    public float getAmountPerPiece() {
        return amountPerPiece;
    }
}
