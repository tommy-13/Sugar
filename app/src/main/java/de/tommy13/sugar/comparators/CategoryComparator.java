package de.tommy13.sugar.comparators;

import java.util.Comparator;

import de.tommy13.sugar.database.CategoryItem;

/**
 * Created by tommy on 19.03.2017.
 */

public class CategoryComparator implements Comparator<CategoryItem> {
    @Override
    public int compare(CategoryItem item1, CategoryItem item2) {
        return item1.getName().compareTo(item2.getName());
    }
}
