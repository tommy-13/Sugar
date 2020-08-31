package de.tommy13.sugar.publisher_observer;

/**
 * Created by tommy on 07.03.2017.
 * Interface for an publisher of FoodData.
 * Sends changes in FoodData to its observers.
 */

public interface CategoryDataPublisher {

    void registerObserver(CategoryDataObserver observer);
    void removeObserver(CategoryDataObserver observer);
    void publishCategoriesChanged();

}
