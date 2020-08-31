package de.tommy13.sugar.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tommy13.sugar.comparators.CategoryComparator;
import de.tommy13.sugar.publisher_observer.CategoryDataObserver;
import de.tommy13.sugar.publisher_observer.CategoryDataPublisher;

/**
 * Created by tommy on 19.03.2017.
 * Class to save all data belonging to categories for this session.
 */

public class CategoryData implements CategoryDataPublisher {


    /*-------------------------------------------------------------------------*/
    /*-------------------------------- OBSERVER -------------------------------*/
    /*-------------------------------------------------------------------------*/
    private List<CategoryDataObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(CategoryDataObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(CategoryDataObserver observer) {
        observers.remove(observer);
    }


    @Override
    public void publishCategoriesChanged() {
        for (CategoryDataObserver observer : observers) {
            observer.updateOnCategoriesChanged();
        }
    }







    /*-------------------------------------------------------------------------*/
    /*-------------------------------- CREATION -------------------------------*/
    /*-------------------------------------------------------------------------*/
    private SugarDataSource         dataSource;

    public CategoryData(SugarDataSource dataSource) {
        this.dataSource = dataSource;
    }







    /*-------------------------------------------------------------------------*/
    /*--------------------------- GETTER AND SETTER ---------------------------*/
    /*-------------------------------------------------------------------------*/
    public ArrayList<CategoryItem> getCategories() {
        ArrayList<CategoryItem> categories = dataSource.getAllCategories();
        Collections.sort(categories, new CategoryComparator());
        return categories;
    }











    /*-------------------------------------------------------------------------*/
    /*-------------------- NEW / CHANGE / DEL CATEGORY ------------------------*/
    /*-------------------------------------------------------------------------*/
    public void createNewCategory(String categoryName, float energy, float sugar, float fat, float amountPerPiece) {
        dataSource.createCategoryItem(categoryName, energy, sugar, fat, amountPerPiece);
        publishCategoriesChanged();
    }

    public void changeCategory(long id, String name, float energy, float sugar, float fat, float amountPerPiece) {
        dataSource.updateCategoryItem(id, name, energy, sugar, fat, amountPerPiece);
        publishCategoriesChanged();
    }

    public void addAll(ArrayList<CategoryItem> categories, boolean overwrite) {
        ArrayList<CategoryItem> savedCategories = getCategories();

        for (CategoryItem item : categories) {
            if (isCategoryNameAllowed(item.getName())) {
                dataSource.createCategoryItem(
                        item.getName(),
                        item.getKcal100(),
                        item.getSugar100(),
                        item.getFat100(),
                        item.getAmountPerPiece());
            }
            else {
                if (overwrite) {
                    long id = getIdByName(savedCategories, item.getName());
                    dataSource.updateCategoryItem(
                            id, item.getName(),
                            item.getKcal100(),
                            item.getSugar100(),
                            item.getFat100(),
                            item.getAmountPerPiece());
                }
            }
        }

        publishCategoriesChanged();
    }

    private long getIdByName(ArrayList<CategoryItem> categoryItems, String name) {
        for (CategoryItem item : categoryItems) {
            if (item.getName().equals(name)) {
                return item.getId();
            }
        }
        return -1;
    }

    public void deleteCategory(CategoryItem item) {
        dataSource.deleteCategoryItem(item);
        publishCategoriesChanged();
    }

    public void deleteAllCategories() {
        dataSource.deleteAllCategories();
        publishCategoriesChanged();
    }

    public void resetCategories() {
        dataSource.resetCategories();
        publishCategoriesChanged();
    }












    /*-------------------------------------------------------------------------*/
    /*--------------------------------- METHODS -------------------------------*/
    /*-------------------------------------------------------------------------*/

    // A category name is not allowed, if it is already in use and if it is not the dataset with the current id.
    public boolean isCategoryNameAllowed(String name, long currentId) {
        ArrayList<CategoryItem> categories = dataSource.getAllCategories();
        for (CategoryItem item : categories) {
            if (name.equals(item.getName()) && currentId != item.getId()) {
                return false;
            }
        }
        return true;
    }

    public boolean isCategoryNameAllowed(String name) {
        ArrayList<CategoryItem> categories = dataSource.getAllCategories();
        for (CategoryItem item : categories) {
            if (name.equals(item.getName())) {
                return false;
            }
        }
        return true;
    }

}
