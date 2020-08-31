package de.tommy13.sugar.publisher_observer;

import de.tommy13.sugar.general.NutrientType;

/**
 * Created by tommy on 19.03.2017.
 * An observer for the preferences of the app.
 */

public interface PreferenceObserver {

    void updateOnChangedNutrientLabels();
    void updateOnChangedGoals(NutrientType nutrientType);
    void updateOnChangedDate(int year, int month, int day, String dayOfWeek);
    void updateOnChangedTime();

}
