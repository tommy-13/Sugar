package de.tommy13.sugar.publisher_observer;

import de.tommy13.sugar.general.NutrientType;

/**
 * Created by tommy on 19.03.2017.
 * An publisher for app preferences.
 */

public interface PreferencePublisher {

    void register(PreferenceObserver observer);
    void remove(PreferenceObserver observer);

    void publishChangedNutrientLabels();
    void publishChangedGoals(NutrientType nutrientType);
    void publishChangedDate(int year, int month, int day, String dayOfWeek);
    void publishChangedTime();

}
