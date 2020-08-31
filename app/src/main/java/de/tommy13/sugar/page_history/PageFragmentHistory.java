package de.tommy13.sugar.page_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import de.tommy13.sugar.general.DayOfWeek;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.general.NutrientType;
import de.tommy13.sugar.R;
import de.tommy13.sugar.database.FoodData;
import de.tommy13.sugar.database.FoodItem;
import de.tommy13.sugar.general.Nutrient;
import de.tommy13.sugar.publisher_observer.FoodDataObserver;
import de.tommy13.sugar.publisher_observer.PreferenceObserver;
import de.tommy13.sugar.menu.AppPreferences;

/**
 * Created by tommy on 01.03.2017.
 * Fragment for printing the history of the nutrients.
 */

public class PageFragmentHistory extends Fragment implements PreferenceObserver, FoodDataObserver, View.OnClickListener {

    private static final String ARG_PAGE_NUMBER = "page_number";

    private MainActivity mainActivity;

    private LinearLayout panelEnergy, panelSugar, panelFat;
    private TextView     labelEnergy, labelSugar, labelFat;
    private HistoryView  hvEnergy, hvSugar, hvFat;
    private TextView     tvEnergyAverage, tvSugarAverage, tvFatAverage;


    public PageFragmentHistory() {
    }

    public static PageFragmentHistory newInstance(MainActivity mainActivity, int page) {
        PageFragmentHistory fragment = new PageFragmentHistory();
        fragment.mainActivity = mainActivity;

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mainActivity == null) {
            mainActivity = (MainActivity) getActivity();
        }

        panelEnergy = (LinearLayout) view.findViewById(R.id.history_panel_energy);
        panelSugar  = (LinearLayout) view.findViewById(R.id.history_panel_sugar);
        panelFat    = (LinearLayout) view.findViewById(R.id.history_panel_fat);
        panelEnergy.setOnClickListener(this);
        panelSugar.setOnClickListener(this);
        panelFat.setOnClickListener(this);

        labelEnergy     = (TextView) view.findViewById(R.id.label_energy);
        labelSugar      = (TextView) view.findViewById(R.id.label_sugar);
        labelFat        = (TextView) view.findViewById(R.id.label_fat);

        hvEnergy = (HistoryView) view.findViewById(R.id.history_energy);
        tvEnergyAverage = (TextView) view.findViewById(R.id.history_energy_average);
        hvSugar = (HistoryView) view.findViewById(R.id.history_sugar);
        tvSugarAverage  = (TextView) view.findViewById(R.id.history_sugar_average);
        hvFat = (HistoryView) view.findViewById(R.id.history_fat);
        tvFatAverage    = (TextView) view.findViewById(R.id.history_fat_average);

        update();
    }

    private void update() {
        FoodData       foodData    = mainActivity.getFoodData();
        AppPreferences preferences = mainActivity.getPreferences();

        // days to show
        int timeMax       = preferences.getTime();

        float energyAverage = 0.0f;
        float sugarAverage  = 0.0f;
        float fatAverage    = 0.0f;

        HistoryDataSet dataSetEnergy = new HistoryDataSet(preferences.getUnitNutrient(1));
        HistoryDataSet dataSetSugar  = new HistoryDataSet(preferences.getUnitNutrient(2));
        HistoryDataSet dataSetFat    = new HistoryDataSet(preferences.getUnitNutrient(3));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, preferences.getYear());
        calendar.set(Calendar.MONTH, preferences.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, preferences.getDay());
        calendar.add(Calendar.DATE, -timeMax + 1);

        for (int i=0; i<timeMax; i++) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            DayOfWeek dayOfWeek = preferences.getDayOfWeekFromCalendar(calendar);

            Nutrient nutrientSum = foodData.getNutrientSums(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            setSingleData(preferences, NutrientType.ENERGY, dayOfWeek, nutrientSum.getEnergy(), dayOfMonth, dataSetEnergy);
            setSingleData(preferences, NutrientType.SUGAR, dayOfWeek, nutrientSum.getSugar(), dayOfMonth, dataSetSugar);
            setSingleData(preferences, NutrientType.FAT, dayOfWeek, nutrientSum.getFat(), dayOfMonth, dataSetFat);

            energyAverage += nutrientSum.getEnergy();
            sugarAverage  += nutrientSum.getSugar();
            fatAverage += nutrientSum.getFat();

            calendar.add(Calendar.DATE, 1);
        }

        // labels
        labelEnergy.setText(preferences.getNameNutrient(1));
        labelSugar.setText(preferences.getNameNutrient(2));
        labelFat.setText(preferences.getNameNutrient(3));

        energyAverage = round(energyAverage / timeMax);
        sugarAverage  = round(sugarAverage / timeMax);
        fatAverage    = round(fatAverage / timeMax);

        String tvText = getContext().getResources().getString(R.string.average) + ": " + energyAverage +
                " " + preferences.getUnitNutrient(1);
        tvEnergyAverage.setText(tvText);
        hvEnergy.setData(dataSetEnergy);

        tvText = getContext().getResources().getString(R.string.average) + ": " + sugarAverage +
                " " + preferences.getUnitNutrient(2);
        tvSugarAverage.setText(tvText);
        hvSugar.setData(dataSetSugar);

        tvText = getContext().getResources().getString(R.string.average) + ": " + fatAverage +
                " " + preferences.getUnitNutrient(3);
        tvFatAverage.setText(tvText);
        hvFat.setData(dataSetFat);

    }

    private void setSingleData(AppPreferences prefs, NutrientType nutrientType, DayOfWeek dayOfWeek, float nutrientSum,
                               int dayOfMonth, HistoryDataSet dataSet) {
        float goal = prefs.getGoal(nutrientType, dayOfWeek);
        float perCentF = round(AppPreferences.MAX_PERCENTAGE * nutrientSum / goal);
        int   perCentI = (int) perCentF;
        dataSet.add(String.valueOf(dayOfMonth), nutrientSum, getClassification(perCentI));
    }

    private Classification getClassification(int perCent) {
        if (perCent < AppPreferences.YELLOW_PERCENTAGE) {
            return Classification.GOOD;
        } else if (perCent < AppPreferences.MAX_PERCENTAGE) {
            return Classification.ATTENTION;
        } else {
            return Classification.BAD;
        }
    }

    private float round(float perCent) {
        return Math.round(10 * perCent) / 10.0f;
    }


    @Override
    public void updateOnChangedNutrientLabels() {
        update();
    }

    @Override
    public void updateOnChangedGoals(NutrientType nutrientType) {
        update();
    }

    @Override
    public void updateOnChangedDate(int year, int month, int day, String dayOfWeek) {
        update();
    }

    @Override
    public void updateOnChangedTime() {
        update();
    }

    @Override
    public void updateOnNewFoodItem(FoodItem foodItem) {
        update();
    }

    @Override
    public void updateOnChangedFoodItem(FoodItem foodItem) {
        update();
    }

    @Override
    public void updateOnDeletedFoodItem(FoodItem foodItem) {
        update();
    }

    @Override
    public void updateOnDeletedFoodItems(List<FoodItem> foodItems) {
        update();
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
        update();
    }




    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == panelEnergy.getId() || id == panelSugar.getId() || id == panelFat.getId()) {
            HistoryTimeDialogFragment dialog = HistoryTimeDialogFragment.newInstance(
                    getResources().getString(R.string.history_time_dialog_title),
                    mainActivity
            );
            dialog.show(mainActivity.getSupportFragmentManager(), "HistoryTimeDialog");
        }
    }
}
