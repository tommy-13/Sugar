package de.tommy13.sugar.page_foodlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.tommy13.sugar.R;
import de.tommy13.sugar.database.FoodItem;
import de.tommy13.sugar.menu.AppPreferences;


/**
 * Created by tommy on 06.03.2017.
 * Array Adapter for a food item.
 */

class FoodArrayAdapter extends ArrayAdapter<FoodItem> {

    private static final String LOG = FoodArrayAdapter.class.getSimpleName();


    private AppPreferences prefs;

    FoodArrayAdapter(Context context, ArrayList<FoodItem> foodItems, AppPreferences prefs) {
        super(context, 0, foodItems);
        this.prefs = prefs;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        FoodItem foodItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    getContext()).inflate(R.layout.row_food_item, parent, false);
        }

        // Lookup view for data population
        TextView tvCategory = (TextView) convertView.findViewById(R.id.page_food_tvCategory);
        TextView tvNote     = (TextView) convertView.findViewById(R.id.page_food_tvNote);
        TextView tvDetails  = (TextView) convertView.findViewById(R.id.page_food_tvDetails);

        // Populate the data into the template view using the data object
        if (foodItem == null) {
            Log.e(LOG, "No Food Item available");
        }
        else {
            tvCategory.setText(foodItem.getCategory());
            tvNote.setText(foodItem.getNote());

            String label = prefs.getNameNutrient(1) + ": ";
            label += foodItem.getKcal() + " ";
            label += prefs.getUnitNutrient(1) + ", ";
            label += prefs.getNameNutrient(2) + ": ";
            label += foodItem.getSugar() + " ";
            label += prefs.getUnitNutrient(2) + ", ";
            label += prefs.getNameNutrient(3) + ": ";
            label += foodItem.getFat() + " ";
            label += prefs.getUnitNutrient(3);
            tvDetails.setText(label);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
