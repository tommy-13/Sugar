package de.tommy13.sugar.page_foodlist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.general.NutrientType;
import de.tommy13.sugar.R;
import de.tommy13.sugar.database.FoodItem;
import de.tommy13.sugar.publisher_observer.FoodDataObserver;
import de.tommy13.sugar.publisher_observer.PreferenceObserver;
import de.tommy13.sugar.menu.AppPreferences;

/**
 * Created by tommy on 01.03.2017.
 * Shows the list of all food items for a certain day.
 */

public class PageFragmentList extends Fragment
        implements View.OnClickListener, FoodDataObserver, PreferenceObserver {

    private static final String ARG_PAGE_NUMBER = "page_number";

    private MainActivity mainActivity;

    private FloatingActionButton btAddItem;
    private FoodArrayAdapter     foodItemArrayAdapter;






    /*---------------------------------------------------------------------------------*/
    /*------------------------ CREATION -----------------------------------------------*/
    /*---------------------------------------------------------------------------------*/

    public PageFragmentList() {
    }

    public static PageFragmentList newInstance(MainActivity mainActivity, int page) {
        PageFragmentList fragment = new PageFragmentList();
        fragment.mainActivity = mainActivity;

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_fragment_list, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mainActivity == null) {
            mainActivity = (MainActivity) getActivity();
        }

        btAddItem        = (FloatingActionButton) view.findViewById(R.id.page_list_add_item);
        btAddItem.setOnClickListener(this);

        ArrayList<FoodItem> foodItems = new ArrayList<>();
        foodItemArrayAdapter = new FoodArrayAdapter(getContext(), foodItems, mainActivity.getPreferences());
        ListView foodItemListView = (ListView) view.findViewById(R.id.page_food_foodlist);
        foodItemListView.setAdapter(foodItemArrayAdapter);
        foodItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FoodItem foodItem = (FoodItem) adapterView.getItemAtPosition(position);
                changeFoodItem(foodItem);
            }
        });
        foodItemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                FoodItem foodItem = (FoodItem) adapterView.getItemAtPosition(position);
                deleteFoodItem(foodItem);
                return true;
            }
        });
        updateFoodList();
    }


    @Override
    public void onPause() {
        super.onPause();
        mainActivity.getFoodData().removeObserver(this);
        mainActivity.getPreferences().remove(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.getFoodData().registerObserver(this);
        mainActivity.getPreferences().register(this);
        updateFoodList();
    }






/*---------------------------------------------------------------------------------*/
    /*------------------------ FOOD ITEMS ---------------------------------------------*/
    /*---------------------------------------------------------------------------------*/

    public void updateFoodList() {
        AppPreferences preferences = mainActivity.getPreferences();

        ArrayList<FoodItem> foodItems = mainActivity.getFoodData().getFoodItems(
                preferences.getYear(), preferences.getMonth(), preferences.getDay());
        foodItemArrayAdapter.clear();
        foodItemArrayAdapter.addAll(foodItems);
    }


    private void addNewFoodItem() {
        String title = getResources().getString(R.string.food_item_dialog_title_new);
        FoodItemDialogFragment dialogFragment = FoodItemDialogFragment.newInstance(
                title, mainActivity);
        dialogFragment.show(mainActivity.getSupportFragmentManager(), "DialogNewFoodItem");
    }

    private void changeFoodItem(FoodItem foodItem) {
        String title = getResources().getString(R.string.food_item_dialog_title_edit);
        FoodItemDialogFragment dialogFragment = FoodItemDialogFragment.newInstance(
                title, mainActivity, foodItem);
        dialogFragment.show(mainActivity.getSupportFragmentManager(), "DialogChangeFoodItem");
    }

    private void deleteFoodItem(final FoodItem foodItem) {
        final AlertDialog alertDialog = new AlertDialog.Builder(mainActivity)
                .setTitle(mainActivity.getResources().getString(R.string.food_item_delete_dialog_title) + foodItem.getCategory())
                .setMessage(mainActivity.getResources().getString(R.string.food_item_delete_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(mainActivity.getResources().getString(R.string.food_item_delete_dialog_btpositiv),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainActivity.getFoodData().deleteFoodItem(foodItem);
                                dialogInterface.cancel();
                            }
                        })
                .setNegativeButton(mainActivity.getResources().getString(R.string.food_item_delete_dialog_btnegativ),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                .setCancelable(true)
                .create();
        alertDialog.show();
    }















    /*---------------------------------------------------------------------------------*/
    /*------------------------ LISTENER -----------------------------------------------*/
    /*---------------------------------------------------------------------------------*/

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == btAddItem.getId()) {
            addNewFoodItem();
        }
    }




    @Override
    public void updateOnNewFoodItem(FoodItem foodItem) {
        updateFoodList();
    }

    @Override
    public void updateOnChangedFoodItem(FoodItem foodItem) {
        updateFoodList();
    }

    @Override
    public void updateOnDeletedFoodItem(FoodItem foodItem) {
        updateFoodList();
    }

    @Override
    public void updateOnDeletedFoodItems(List<FoodItem> foodItems) {
        updateFoodList();
    }



    @Override
    public void updateOnChangedNutrientLabels() {
        updateFoodList();
    }

    @Override
    public void updateOnChangedGoals(NutrientType nutrientType) {
        // do nothing
    }

    @Override
    public void updateOnChangedDate(int year, int month, int day, String dayOfWeek) {
        updateFoodList();
    }

    @Override
    public void updateOnChangedTime() {
        // do nothing
    }
}
