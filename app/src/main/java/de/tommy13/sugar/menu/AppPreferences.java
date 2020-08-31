package de.tommy13.sugar.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.tommy13.sugar.general.DayOfWeek;
import de.tommy13.sugar.R;
import de.tommy13.sugar.general.NutrientType;
import de.tommy13.sugar.publisher_observer.PreferenceObserver;
import de.tommy13.sugar.publisher_observer.PreferencePublisher;

/**
 * Created by tommy on 10.03.2017.
 * Class for saving and loading preferences for this app.
 */

public class AppPreferences implements PreferencePublisher {

    /*----------------------------------------------------------------------------*/
    /*------------------------------ OBSERVER ------------------------------------*/
    /*----------------------------------------------------------------------------*/
    private List<PreferenceObserver> observers = new ArrayList<>();

    @Override
    public void register(PreferenceObserver observer) {
        observers.add(observer);
    }

    @Override
    public void remove(PreferenceObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void publishChangedNutrientLabels() {
        for (PreferenceObserver observer : observers) {
            observer.updateOnChangedNutrientLabels();
        }
    }

    @Override
    public void publishChangedGoals(NutrientType nutrientType) {
        for (PreferenceObserver observer : observers) {
            observer.updateOnChangedGoals(nutrientType);
        }
    }

    @Override
    public void publishChangedDate(int year, int month, int day, String dateString) {
        for (PreferenceObserver observer : observers) {
            observer.updateOnChangedDate(year, month, day, dateString);
        }
    }

    @Override
    public void publishChangedTime() {
        for (PreferenceObserver observer : observers) {
            observer.updateOnChangedTime();
        }
    }




    /*----------------------------------------------------------------------------*/
    /*------------------------------- GENERAL ------------------------------------*/
    /*----------------------------------------------------------------------------*/
    public static final int MAX_PERCENTAGE    = 100; // 100 %
    public static final int YELLOW_PERCENTAGE = 75;  //  75 %



    private final String  CHECKED             = "_checked";
    private final String  GOAL                = "_goal_";

    private final boolean DEFAULT_CHECKED      = true;
    private final float   DEFAULT_ENERGY_GOAL  = 2200f;
    private final float   DEFAULT_SUGAR_GOAL   = 60f;
    private final float   DEFAULT_FAT_GOAL     = 90f;

    private final String  UNIT_DRINKS    = "unit_drinks";
    private final String  UNIT_FOODS     = "unit_foods";

    private final String  NUTRIENT1_NAME = "nutrient1_name";
    private final String  NUTRIENT2_NAME = "nutrient2_name";
    private final String  NUTRIENT3_NAME = "nutrient3_name";

    private final String  NUTRIENT1_UNIT = "nutrient1_unit";
    private final String  NUTRIENT2_UNIT = "nutrient2_unit";
    private final String  NUTRIENT3_UNIT = "nutrient3_unit";

    private final String  HISTORY_TIME   = "history_time";

    public static final int DEFAULT_HISTORY_DAYS = 10;
    public static final int MIN_HISTORY_DAYS     = 5;
    public static final int MAX_HISTORY_DAYS     = 30;


    private Context           context;
    private SharedPreferences prefs;
    private Calendar          currentDate;



    public AppPreferences(Context context) {
        this.context     = context;
        this.prefs       = PreferenceManager.getDefaultSharedPreferences(context);
        this.currentDate = Calendar.getInstance();
    }


    public boolean isFirstStart() {
        final String IS_FIRST_START = "is_first_start";
        boolean isFirstStart = prefs.getBoolean(IS_FIRST_START, true);
        if (isFirstStart) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(IS_FIRST_START, false);
            editor.apply();
            return true;
        }
        else {
            return false;
        }
    }








    /*----------------------------------------------------------------------------*/
    /*----------------------- SETTER AND GETTER ----------------------------------*/
    /*----------------------------------------------------------------------------*/
    public void setGoals(NutrientType type, boolean isGeneralGoal,
                         float goalMonday, float goalTuesday, float goalWednesday, float goalThursday,
                         float goalFriday, float goalSaturday, float goalSunday) {
        String strType = NutrientType.getString(type);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(strType + CHECKED, isGeneralGoal);
        editor.putFloat(strType + GOAL + DayOfWeek.getString(DayOfWeek.MONDAY), goalMonday);
        editor.putFloat(strType + GOAL + DayOfWeek.getString(DayOfWeek.TUESDAY), goalTuesday);
        editor.putFloat(strType + GOAL + DayOfWeek.getString(DayOfWeek.WEDNESDAY), goalWednesday);
        editor.putFloat(strType + GOAL + DayOfWeek.getString(DayOfWeek.THURSDAY), goalThursday);
        editor.putFloat(strType + GOAL + DayOfWeek.getString(DayOfWeek.FRIDAY), goalFriday);
        editor.putFloat(strType + GOAL + DayOfWeek.getString(DayOfWeek.SATURDAY), goalSaturday);
        editor.putFloat(strType + GOAL + DayOfWeek.getString(DayOfWeek.SUNDAY), goalSunday);
        editor.apply();

        publishChangedGoals(type);
    }

    public float getGoal(NutrientType type, DayOfWeek dayOfWeek) {
        String key = NutrientType.getString(type) + GOAL + DayOfWeek.getString(dayOfWeek);
        float defaultVal = DEFAULT_ENERGY_GOAL;
        switch (type) {
            case ENERGY: defaultVal = DEFAULT_ENERGY_GOAL; break;
            case SUGAR:  defaultVal = DEFAULT_SUGAR_GOAL;  break;
            case FAT:    defaultVal = DEFAULT_FAT_GOAL;    break;
        }
        return prefs.getFloat(key, defaultVal);
    }

    public boolean isGoalGlobal(NutrientType type) {
        String key = NutrientType.getString(type) + CHECKED;
        return prefs.getBoolean(key, DEFAULT_CHECKED);
    }

    void setToDefaultGoals() {
        setGoals(NutrientType.ENERGY, DEFAULT_CHECKED,
                DEFAULT_ENERGY_GOAL, DEFAULT_ENERGY_GOAL, DEFAULT_ENERGY_GOAL, DEFAULT_ENERGY_GOAL,
                DEFAULT_ENERGY_GOAL, DEFAULT_ENERGY_GOAL, DEFAULT_ENERGY_GOAL);
        setGoals(NutrientType.SUGAR, DEFAULT_CHECKED,
                DEFAULT_SUGAR_GOAL, DEFAULT_SUGAR_GOAL, DEFAULT_SUGAR_GOAL, DEFAULT_SUGAR_GOAL,
                DEFAULT_SUGAR_GOAL, DEFAULT_SUGAR_GOAL, DEFAULT_SUGAR_GOAL);
        setGoals(NutrientType.FAT, DEFAULT_CHECKED,
                DEFAULT_FAT_GOAL, DEFAULT_FAT_GOAL, DEFAULT_FAT_GOAL, DEFAULT_FAT_GOAL,
                DEFAULT_FAT_GOAL, DEFAULT_FAT_GOAL, DEFAULT_FAT_GOAL);
    }


    void setNutrientsAndUnits(String unitFoods, String unitDrinks,
                              String nutrientName1, String nutrientUnit1,
                              String nutrientName2, String nutrientUnit2,
                              String nutrientName3, String nutrientUnit3) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(UNIT_FOODS, unitFoods);
        editor.putString(UNIT_DRINKS, unitDrinks);
        editor.putString(NUTRIENT1_NAME, nutrientName1);
        editor.putString(NUTRIENT2_NAME, nutrientName2);
        editor.putString(NUTRIENT3_NAME, nutrientName3);
        editor.putString(NUTRIENT1_UNIT, nutrientUnit1);
        editor.putString(NUTRIENT2_UNIT, nutrientUnit2);
        editor.putString(NUTRIENT3_UNIT, nutrientUnit3);
        editor.apply();

        publishChangedNutrientLabels();
    }

    public String getUnitFoods() {
        return prefs.getString(UNIT_FOODS, context.getResources().getString(R.string.default_unit_foods));
    }

    public String getUnitDrinks() {
        return prefs.getString(UNIT_DRINKS, context.getResources().getString(R.string.default_unit_drinks));
    }

    public String getUnitNutrient(int nr) {
        switch (nr) {
            case 1:
                return prefs.getString(NUTRIENT1_UNIT, context.getResources().getString(R.string.default_unit_one));
            case 2:
                return prefs.getString(NUTRIENT2_UNIT, context.getResources().getString(R.string.default_unit_two));
            case 3: default:
                return prefs.getString(NUTRIENT3_UNIT, context.getResources().getString(R.string.default_unit_three));
        }
    }
    public String getNameNutrient(int nr) {
        switch (nr) {
            case 1:
                return prefs.getString(NUTRIENT1_NAME, context.getResources().getString(R.string.default_nutrient_one));
            case 2:
                return prefs.getString(NUTRIENT2_NAME, context.getResources().getString(R.string.default_nutrient_two));
            case 3: default:
                return prefs.getString(NUTRIENT3_NAME, context.getResources().getString(R.string.default_nutrient_three));
        }
    }


    void resetAllFields() {
        setNutrientsAndUnits(
                context.getResources().getString(R.string.default_unit_foods),
                context.getResources().getString(R.string.default_unit_drinks),
                context.getResources().getString(R.string.default_nutrient_one),
                context.getResources().getString(R.string.default_unit_one),
                context.getResources().getString(R.string.default_nutrient_two),
                context.getResources().getString(R.string.default_unit_two),
                context.getResources().getString(R.string.default_nutrient_three),
                context.getResources().getString(R.string.default_unit_three)
        );
    }


    public void setTime(int days) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(HISTORY_TIME, days);
        editor.apply();
        publishChangedTime();
    }

    public int getTime() {
        return prefs.getInt(HISTORY_TIME, DEFAULT_HISTORY_DAYS);
    }








    /*---------------------------------------------------------------------------------*/
    /*---------------------------- DATE -----------------------------------------------*/
    /*---------------------------------------------------------------------------------*/

    public int getDay()   {return getDay(currentDate);}
    public int getMonth() {return getMonth(currentDate);}
    public int getYear()  {return getYear(currentDate);}

    private int getDay(Calendar calendar)   {return calendar.get(Calendar.DAY_OF_MONTH);}
    private int getMonth(Calendar calendar) {return calendar.get(Calendar.MONTH);}
    private int getYear(Calendar calendar)  {return calendar.get(Calendar.YEAR);}

    public void setDate(int year, int month, int day) {
        currentDate      = getCalender(year, month, day);
        publishChangedDate(year, month, day, getDateString());
    }

    private String getDateString() {
        String date = DateFormat.getDateInstance().format(currentDate.getTime());
        date = getDayOfWeek(currentDate) + ", " + date;
        return date;
    }

    public String getShortDateString() {
        return DateFormat.getDateInstance().format(currentDate.getTime());
    }

    private Calendar getCalender(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }


    public DayOfWeek getDayOfWeek() {
        int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
        DayOfWeek dow = DayOfWeek.MONDAY;
        if (dayOfWeek == Calendar.MONDAY) {
            dow = DayOfWeek.MONDAY;
        }
        else if (dayOfWeek == Calendar.TUESDAY) {
            dow = DayOfWeek.TUESDAY;
        }
        else if (dayOfWeek == Calendar.WEDNESDAY) {
            dow = DayOfWeek.WEDNESDAY;
        }
        else if (dayOfWeek == Calendar.THURSDAY) {
            dow = DayOfWeek.THURSDAY;
        }
        else if (dayOfWeek == Calendar.FRIDAY) {
            dow = DayOfWeek.FRIDAY;
        }
        else if (dayOfWeek == Calendar.SATURDAY) {
            dow = DayOfWeek.SATURDAY;
        }
        else if (dayOfWeek == Calendar.SUNDAY) {
            dow = DayOfWeek.SUNDAY;
        }
        return dow;
    }

    private String getDayOfWeek(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String strDayOfWeek = context.getResources().getString(R.string.monday);
        if (dayOfWeek == Calendar.MONDAY) {
            strDayOfWeek = context.getResources().getString(R.string.monday);
        }
        else if (dayOfWeek == Calendar.TUESDAY) {
            strDayOfWeek = context.getResources().getString(R.string.tuesday);
        }
        else if (dayOfWeek == Calendar.WEDNESDAY) {
            strDayOfWeek = context.getResources().getString(R.string.wednesday);
        }
        else if (dayOfWeek == Calendar.THURSDAY) {
            strDayOfWeek = context.getResources().getString(R.string.thursday);
        }
        else if (dayOfWeek == Calendar.FRIDAY) {
            strDayOfWeek = context.getResources().getString(R.string.friday);
        }
        else if (dayOfWeek == Calendar.SATURDAY) {
            strDayOfWeek = context.getResources().getString(R.string.saturday);
        }
        else if (dayOfWeek == Calendar.SUNDAY) {
            strDayOfWeek = context.getResources().getString(R.string.sunday);
        }
        return strDayOfWeek;
    }



    public DayOfWeek getDayOfWeekFromCalendar(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        DayOfWeek dow = DayOfWeek.MONDAY;
        if (dayOfWeek == Calendar.MONDAY) {
            dow = DayOfWeek.MONDAY;
        }
        else if (dayOfWeek == Calendar.TUESDAY) {
            dow = DayOfWeek.TUESDAY;
        }
        else if (dayOfWeek == Calendar.WEDNESDAY) {
            dow = DayOfWeek.WEDNESDAY;
        }
        else if (dayOfWeek == Calendar.THURSDAY) {
            dow = DayOfWeek.THURSDAY;
        }
        else if (dayOfWeek == Calendar.FRIDAY) {
            dow = DayOfWeek.FRIDAY;
        }
        else if (dayOfWeek == Calendar.SATURDAY) {
            dow = DayOfWeek.SATURDAY;
        }
        else if (dayOfWeek == Calendar.SUNDAY) {
            dow = DayOfWeek.SUNDAY;
        }
        return dow;
    }

}
