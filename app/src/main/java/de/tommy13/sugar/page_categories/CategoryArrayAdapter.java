package de.tommy13.sugar.page_categories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.tommy13.sugar.R;
import de.tommy13.sugar.database.CategoryItem;
import de.tommy13.sugar.menu.AppPreferences;

/**
 * Created by tommy on 06.03.2017.
 * Array Adapter for a CategoryItem.
 */

class CategoryArrayAdapter extends ArrayAdapter<CategoryItem> {

    private AppPreferences prefs;

    CategoryArrayAdapter(Context context, ArrayList<CategoryItem> categories, AppPreferences prefs) {
        super(context, 0, categories);
        this.prefs = prefs;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        CategoryItem categoryItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    getContext()).inflate(R.layout.row_category_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName     = (TextView) convertView.findViewById(R.id.page_categories_tvName);
        TextView tvAmountPP = (TextView) convertView.findViewById(R.id.page_categories_tvAmountPerPiece);
        TextView tvDetails  = (TextView) convertView.findViewById(R.id.page_categories_tvDetails);

        // Populate the data into the template view using the data object
        String label;
        if (categoryItem != null) {
            tvName.setText(categoryItem.getName());

            label = convertView.getResources().getString(R.string.amount_per_piece) + ": " +
                    categoryItem.getAmountPerPiece() + " " + prefs.getUnitFoods() +
                    convertView.getResources().getString(R.string.divider) + prefs.getUnitDrinks();
            tvAmountPP.setText(label);

            label = prefs.getNameNutrient(1) + ": " +
                    categoryItem.getKcal100() + " " + prefs.getUnitNutrient(1) +
                    ", " + prefs.getNameNutrient(2) + ": " +
                    categoryItem.getSugar100() + " " + prefs.getUnitNutrient(2) +
                    ", " + prefs.getNameNutrient(3) + ": " +
                    categoryItem.getFat100() + " " + prefs.getUnitNutrient(3);
            tvDetails.setText(label);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
