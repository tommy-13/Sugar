package de.tommy13.sugar.input_dialog;

/**
 * Created by tommy on 14.03.2017.
 * Types for the InputDialog.
 */

public enum InputDialogType {

    TEXT, BIG_DEZIMAL, DEZIMAL, NUTRIENT_NAME, NUTRIENT_UNIT, FILENAME;


    private static final int MAX_LEN_DEZIMAL     = 4;
    private static final int MAX_LEN_BIG_DEZIMAL = 6;
    private static final int MAX_LEN_TEXT        = 40;
    private static final int MAX_LEN_NAME        = 15;
    private static final int MAX_LEN_UNIT        = 4;
    private static final int MAX_LEN_FNAME       = 32;

    public static int getMaxLength(InputDialogType type) {
        switch (type) {
            case DEZIMAL:       return MAX_LEN_DEZIMAL;
            case BIG_DEZIMAL:   return MAX_LEN_BIG_DEZIMAL;
            case NUTRIENT_UNIT: return MAX_LEN_UNIT;
            case NUTRIENT_NAME: return MAX_LEN_NAME;
            case FILENAME:      return MAX_LEN_FNAME;
            case TEXT: default: return MAX_LEN_TEXT;
        }
    }
}
