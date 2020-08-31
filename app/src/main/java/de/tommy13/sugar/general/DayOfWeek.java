package de.tommy13.sugar.general;

/**
 * Created by tommy on 25.03.2017.
 * Enum for the days of the week.
 */

public enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    public static final String STR_MO = "monday";
    public static final String STR_TU = "tuesday";
    public static final String STR_WE = "wednesday";
    public static final String STR_TH = "thursday";
    public static final String STR_FR = "friday";
    public static final String STR_SA = "saturday";
    public static final String STR_SU = "sunday";


    public static String getString(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:    return STR_MO;
            case TUESDAY:   return STR_TU;
            case WEDNESDAY: return STR_WE;
            case THURSDAY:  return STR_TH;
            case FRIDAY:    return STR_FR;
            case SATURDAY:  return STR_SA;
            case SUNDAY:    return STR_SU;
        }
        return STR_MO;
    }
}
