package de.tommy13.sugar.publisher_observer;

import java.util.List;
import de.tommy13.sugar.database.FoodItem;

/**
 * Created by tommy on 07.03.2017.
 * Interface for an publisher of FoodData.
 * Sends changes in FoodData to its observers.
 */

public interface FoodDataPublisher {

    void registerObserver(FoodDataObserver observer);
    void removeObserver(FoodDataObserver observer);

    void publishNewFoodItem(FoodItem foodItem);
    void publishChangedFoodItem(FoodItem foodItem);
    void publishDeletedFoodItem(FoodItem foodItem);
    void publishDeletedFoodItems(List<FoodItem> foodItems);

}
