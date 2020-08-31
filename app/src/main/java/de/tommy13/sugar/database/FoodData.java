package de.tommy13.sugar.database;

import java.util.ArrayList;
import java.util.List;

import de.tommy13.sugar.general.Nutrient;
import de.tommy13.sugar.publisher_observer.FoodDataObserver;
import de.tommy13.sugar.publisher_observer.FoodDataPublisher;

/**
 * Created by tommy on 19.03.2017.
 * Class to organize the FoodData.
 */

public class FoodData implements FoodDataPublisher {


    /*-------------------------------------------------------------------------*/
    /*-------------------------------- OBSERVER -------------------------------*/
    /*-------------------------------------------------------------------------*/
    private List<FoodDataObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(FoodDataObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(FoodDataObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void publishNewFoodItem(FoodItem foodItem) {
        for (FoodDataObserver observer : observers) {
            observer.updateOnNewFoodItem(foodItem);
        }
    }

    @Override
    public void publishChangedFoodItem(FoodItem foodItem) {
        for (FoodDataObserver observer : observers) {
            observer.updateOnChangedFoodItem(foodItem);
        }
    }

    @Override
    public void publishDeletedFoodItem(FoodItem foodItem) {
        for (FoodDataObserver observer : observers) {
            observer.updateOnDeletedFoodItem(foodItem);
        }
    }

    @Override
    public void publishDeletedFoodItems(List<FoodItem> foodItems) {
        for (FoodDataObserver observer : observers) {
            observer.updateOnDeletedFoodItems(foodItems);
        }
    }








    /*-------------------------------------------------------------------------*/
    /*-------------------------------- CREATION -------------------------------*/
    /*-------------------------------------------------------------------------*/
    private SugarDataSource     dataSource;

    public FoodData(SugarDataSource dataSource) {
        this.dataSource = dataSource;
    }









    /*-------------------------------------------------------------------------*/
    /*---------------------------- GETTER AND SETTER --------------------------*/
    /*-------------------------------------------------------------------------*/
    public ArrayList<FoodItem> getFoodItems(int year, int month, int day) {
        return dataSource.getFoodItems(year,month,day);
    }

    public Nutrient getNutrientSums(int year, int month, int day) {
        float energySum = 0.0f;
        float sugarSum  = 0.0f;
        float fatSum    = 0.0f;

        ArrayList<FoodItem> items = getFoodItems(year,month,day);
        for (FoodItem item : items) {
            energySum += item.getKcal();
            sugarSum  += item.getSugar();
            fatSum    += item.getFat();
        }

        return new Nutrient(round(energySum), round(sugarSum), round(fatSum));
    }


    private float round(float value) {
        return Math.round(10 * value) / 10.0f;
    }






    /*-------------------------------------------------------------------------*/
    /*---------------- CREATE / CHANGE /DELETE FOOD ITEM ----------------------*/
    /*-------------------------------------------------------------------------*/

    public FoodItem createNewFoodItem(int year, int month, int day, String category, String note,
                                   float amount, float kcal, float sugar, float fat, float pieces) {
        FoodItem newItem = dataSource.createFoodItem(year, month, day, category, note, amount, kcal, sugar, fat, pieces);
        publishNewFoodItem(newItem);
        return newItem;
    }


    public FoodItem changeFoodItem(long id, int year, int month, int day, String category, String note,
                                   float amount, float kcal, float sugar, float fat, float pieces) {
        FoodItem newItem = dataSource.updateFoodItem(id, year, month, day, category, note, amount, kcal, sugar, fat, pieces);
        publishChangedFoodItem(newItem);
        return newItem;
    }


    public void deleteFoodItem(FoodItem item) {
        dataSource.deleteFoodItem(item);
        publishDeletedFoodItem(item);
    }

    public void deleteFoodItemsUntil(int year, int month, int day) {
        dataSource.deleteFoodItemsUntil(year, month, day);
        publishDeletedFoodItems(null);
    }


}
