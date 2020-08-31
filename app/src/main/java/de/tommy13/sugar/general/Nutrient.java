package de.tommy13.sugar.general;

/**
 * Created by tommy on 22.03.2017.
 * Class to save all nutrients.
 */

public class Nutrient {

    private float energy;
    private float sugar;
    private float fat;

    public Nutrient(float energy, float sugar, float fat) {
        this.energy = energy;
        this.sugar  = sugar;
        this.fat    = fat;
    }



    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
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
}
