package de.tommy13.sugar.page_overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.tommy13.sugar.general.DayOfWeek;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.general.NutrientType;
import de.tommy13.sugar.R;
import de.tommy13.sugar.database.FoodItem;
import de.tommy13.sugar.general.Nutrient;
import de.tommy13.sugar.publisher_observer.FoodDataObserver;
import de.tommy13.sugar.publisher_observer.PreferenceObserver;
import de.tommy13.sugar.menu.AppPreferences;

/**
 * Created by tommy on 01.03.2017.
 * Page to show an overview: energy, sugar, fat of the day.
 */

public class PageFragmentOverview extends Fragment implements FoodDataObserver, View.OnClickListener, PreferenceObserver {

    private static final String ARG_PAGE_NUMBER = "page_number";

    private MainActivity mainActivity;

    private LinearLayout panelEnergy, panelSugar, panelFat;

    private TextView tvEnergyLabel, tvSugarLabel, tvFatLabel;
    private TextView tvEnergySum, tvSugarSum, tvFatSum;
    private GoalView pbEnergySum, pbSugarSum, pbFatSum;



    public PageFragmentOverview() {
    }

    public static PageFragmentOverview newInstance(MainActivity mainActivity, int page) {
        PageFragmentOverview fragment = new PageFragmentOverview();
        fragment.mainActivity = mainActivity;

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mainActivity == null) {
            mainActivity = (MainActivity) getActivity();
        }

        panelEnergy = (LinearLayout) view.findViewById(R.id.overview_energy);
        panelEnergy.setOnClickListener(this);
        panelSugar  = (LinearLayout) view.findViewById(R.id.overview_sugar);
        panelSugar.setOnClickListener(this);
        panelFat    = (LinearLayout) view.findViewById(R.id.overview_fat);
        panelFat.setOnClickListener(this);

        tvEnergyLabel = (TextView) view.findViewById(R.id.overview_energy_label);
        tvSugarLabel  = (TextView) view.findViewById(R.id.overview_sugar_label);
        tvFatLabel    = (TextView) view.findViewById(R.id.overview_fat_label);

        tvEnergySum = (TextView) view.findViewById(R.id.overview_energy_sum);
        tvSugarSum  = (TextView) view.findViewById(R.id.overview_sugar_sum);
        tvFatSum    = (TextView) view.findViewById(R.id.overview_fat_sum);

        pbEnergySum = (GoalView) view.findViewById(R.id.overview_progress_energy);
        pbSugarSum  = (GoalView) view.findViewById(R.id.overview_progress_sugar);
        pbFatSum    = (GoalView) view.findViewById(R.id.overview_progress_fat);

        updateOverview();
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
        updateOverview();
    }





    public void updateOverview() {
        AppPreferences preferences = mainActivity.getPreferences();
        Nutrient nutrientSums = mainActivity.getFoodData().getNutrientSums(
                preferences.getYear(), preferences.getMonth(), preferences.getDay());
        DayOfWeek dayOfWeek   = preferences.getDayOfWeek();

        tvEnergyLabel.setText(preferences.getNameNutrient(1));
        tvSugarLabel.setText(preferences.getNameNutrient(2));
        tvFatLabel.setText(preferences.getNameNutrient(3));

        // energy
        setNutrientProgress(
                pbEnergySum,
                tvEnergySum,
                preferences.getGoal(NutrientType.ENERGY, dayOfWeek),
                nutrientSums.getEnergy(),
                preferences.getUnitNutrient(1));

        // sugar
        setNutrientProgress(
                pbSugarSum,
                tvSugarSum,
                preferences.getGoal(NutrientType.SUGAR, dayOfWeek),
                nutrientSums.getSugar(),
                preferences.getUnitNutrient(2));

        // fat
        setNutrientProgress(
                pbFatSum,
                tvFatSum,
                preferences.getGoal(NutrientType.FAT, dayOfWeek),
                nutrientSums.getFat(),
                preferences.getUnitNutrient(3));
    }

    private void setNutrientProgress(GoalView goalView, TextView tvInfo,
                                     float nutrientGoal, float nutrientSum, String nutrientUnit) {
        // set progress
        goalView.setData(nutrientGoal, nutrientSum);

        // set info
        float perCentF = round(AppPreferences.MAX_PERCENTAGE * nutrientSum / nutrientGoal);
        String info = nutrientSum + " / " + nutrientGoal + " " + nutrientUnit + " (" + perCentF + " %)";
        tvInfo.setText(info);
    }

    private float round(float perCent) {
        return Math.round(10 * perCent) / 10.0f;
    }




    @Override
    public void updateOnNewFoodItem(FoodItem foodItem) {
        updateOverview();
    }

    @Override
    public void updateOnChangedFoodItem(FoodItem foodItem) {
        updateOverview();

    }

    @Override
    public void updateOnDeletedFoodItem(FoodItem foodItem) {
        updateOverview();
    }

    @Override
    public void updateOnDeletedFoodItems(List<FoodItem> foodItems) {
        updateOverview();
    }







    @Override
    public void onClick(View view) {
        int            id    = view.getId();
        AppPreferences prefs = mainActivity.getPreferences();

        if (id == panelEnergy.getId()) {
            showGoalsDialog(prefs, 1, NutrientType.ENERGY);
        }
        else if (id == panelSugar.getId()) {
            showGoalsDialog(prefs, 2, NutrientType.SUGAR);
        }
        else if (id == panelFat.getId()) {
            showGoalsDialog(prefs, 3, NutrientType.FAT);
        }
    }

    private void showGoalsDialog(AppPreferences prefs, int nr, NutrientType nutrientType) {
        String dialogTitle = getResources().getString(R.string.label_goal) + ": ";
        dialogTitle += prefs.getNameNutrient(nr) + " ";
        dialogTitle += getResources().getString(R.string.bracket_open);
        dialogTitle += prefs.getUnitNutrient(nr);
        dialogTitle += getResources().getString(R.string.bracket_close);

        SettingsGoalDialogFragment goalDialog = SettingsGoalDialogFragment.newInstance(
                dialogTitle, mainActivity, nutrientType);
        goalDialog.show(mainActivity.getSupportFragmentManager(), "GoalsDialog");
    }




    @Override
    public void updateOnChangedNutrientLabels() {
        updateOverview();
    }

    @Override
    public void updateOnChangedGoals(NutrientType nutrientType) {
        updateOverview();
    }

    @Override
    public void updateOnChangedDate(int year, int month, int day, String dayOfWeek) {
        updateOverview();
    }

    @Override
    public void updateOnChangedTime() {
        // do nothing
    }
}
