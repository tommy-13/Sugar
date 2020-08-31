package de.tommy13.sugar.general;

/**
 * Created by tommy on 25.03.2017.
 * Enum for the type of nutrient.
 */

public enum NutrientType {
    ENERGY, SUGAR, FAT;

    public static final String STR_ENERGY = "energy";
    public static final String STR_SUGAR  = "sugar";
    public static final String STR_FAT    = "fat";

    public static String getString(NutrientType type) {
        switch (type) {
            case ENERGY: return STR_ENERGY;
            case SUGAR:  return STR_SUGAR;
            case FAT:    return STR_FAT;
        }
        return STR_ENERGY;
    }
}
