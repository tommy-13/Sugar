package de.tommy13.sugar.publisher_observer;

import java.util.List;

import de.tommy13.sugar.database.FoodItem;

/**
 * Created by tommy on 07.03.2017.
 * Interface for an observer of the FoodData class.
 */

public interface FoodDataObserver {

    void updateOnNewFoodItem(FoodItem foodItem);
    void updateOnChangedFoodItem(FoodItem foodItem);
    void updateOnDeletedFoodItem(FoodItem foodItem);
    void updateOnDeletedFoodItems(List<FoodItem> foodItems);

}
