package de.tommy13.sugar.menu;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import de.tommy13.sugar.input_dialog.InputDialogFragment;
import de.tommy13.sugar.input_dialog.InputDialogType;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.R;
import de.tommy13.sugar.page_foodlist.FoodItemDialogFragment;
import de.tommy13.sugar.publisher_observer.InputDialogReceiver;

/**
 * Created by tommy on 05.03.2017.
 * Dialog to the the user preferences.
 */

public class SettingsDialogFragment extends DialogFragment
        implements View.OnClickListener, InputDialogReceiver {

    public static final String TITLE  = "title";


    private enum Field_Type {
        UNIT_FOODS, UNIT_DRINKS, UNIT_NUTRIENT1, UNIT_NUTRIENT2, UNIT_NUTRIENT3,
        NAME_NUTRIENT1, NAME_NUTRIENT2, NAME_NUTRIENT3
    }

    private Field_Type   fieldType;
    private MainActivity mainActivity;

    private Button btSave;
    private Button btCancel;

    private Button btFoods;
    private Button btDrinks;
    private Button btNutrientName1;
    private Button btNutrientName2;
    private Button btNutrientName3;
    private Button btUnit1;
    private Button btUnit2;
    private Button btUnit3;

    private Button btResetSettings;


    public SettingsDialogFragment() {}


    public static SettingsDialogFragment newInstance(String title, MainActivity mainActivity) {
        SettingsDialogFragment fragment = new SettingsDialogFragment();
        fragment.mainActivity = mainActivity;
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        Bundle bundle = new Bundle();
        bundle.putString(FoodItemDialogFragment.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_settings, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);

        btSave   = (Button) view.findViewById(R.id.settings_save);
        btCancel = (Button) view.findViewById(R.id.settings_cancel);
        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        btFoods         = (Button) view.findViewById(R.id.settings_unit_foods);
        btDrinks        = (Button) view.findViewById(R.id.settings_unit_drinks);
        btUnit1         = (Button) view.findViewById(R.id.settings_unit_one);
        btUnit2         = (Button) view.findViewById(R.id.settings_unit_two);
        btUnit3         = (Button) view.findViewById(R.id.settings_unit_three);
        btNutrientName1 = (Button) view.findViewById(R.id.settings_nutrient_name_one);
        btNutrientName2 = (Button) view.findViewById(R.id.settings_nutrient_name_two);
        btNutrientName3 = (Button) view.findViewById(R.id.settings_nutrient_name_three);
        btFoods.setOnClickListener(this);
        btDrinks.setOnClickListener(this);
        btUnit1.setOnClickListener(this);
        btUnit2.setOnClickListener(this);
        btUnit3.setOnClickListener(this);
        btNutrientName1.setOnClickListener(this);
        btNutrientName2.setOnClickListener(this);
        btNutrientName3.setOnClickListener(this);

        btResetSettings = (Button) view.findViewById(R.id.settings_reset_units);
        btResetSettings.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        getDialog().setTitle(bundle.getString(SettingsDialogFragment.TITLE));

        setFields();
    }

    private void setFields() {
        AppPreferences preferences = mainActivity.getPreferences();
        btFoods.setText(preferences.getUnitFoods());
        btDrinks.setText(preferences.getUnitDrinks());

        btUnit1.setText(preferences.getUnitNutrient(1));
        btUnit2.setText(preferences.getUnitNutrient(2));
        btUnit3.setText(preferences.getUnitNutrient(3));

        btNutrientName1.setText(preferences.getNameNutrient(1));
        btNutrientName2.setText(preferences.getNameNutrient(2));
        btNutrientName3.setText(preferences.getNameNutrient(3));
    }



    @Override
    public void onPause() {
        super.onPause();
        mainActivity = null;
        dismiss();
    }



    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == btSave.getId()) {
            onSaveClicked();
        }
        else if (id == btCancel.getId()) {
            dismiss();
        }
        else if (id == btUnit1.getId()) {
            fieldType = Field_Type.UNIT_NUTRIENT1;
            String title = getResources().getString(R.string.label_unit) + " " +
                    getResources().getString(R.string.settings_nutrient_one_label);
            showInputDialogUnit(btUnit1, title);
        }
        else if (id == btUnit2.getId()) {
            fieldType = Field_Type.UNIT_NUTRIENT2;
            String title = getResources().getString(R.string.label_unit) + " " +
                    getResources().getString(R.string.settings_nutrient_two_label);
            showInputDialogUnit(btUnit2, title);
        }
        else if (id == btUnit3.getId()) {
            fieldType = Field_Type.UNIT_NUTRIENT3;
            String title = getResources().getString(R.string.label_unit) + " " +
                    getResources().getString(R.string.settings_nutrient_three_label);
            showInputDialogUnit(btUnit3, title);
        }
        else if (id == btNutrientName1.getId()) {
            fieldType = Field_Type.NAME_NUTRIENT1;
            String title = getResources().getString(R.string.label_name) + " " +
                    getResources().getString(R.string.settings_nutrient_one_label);
            showInputDialogName(btNutrientName1, title);
        }
        else if (id == btNutrientName2.getId()) {
            fieldType = Field_Type.NAME_NUTRIENT2;
            String title = getResources().getString(R.string.label_name) + " " +
                    getResources().getString(R.string.settings_nutrient_two_label);
            showInputDialogName(btNutrientName1, title);
        }
        else if (id == btNutrientName3.getId()) {
            fieldType = Field_Type.NAME_NUTRIENT3;
            String title = getResources().getString(R.string.label_name) + " " +
                    getResources().getString(R.string.settings_nutrient_three_label);
            showInputDialogName(btNutrientName3, title);
        }
        else if (id == btFoods.getId()) {
            fieldType = Field_Type.UNIT_FOODS;
            showInputDialogUnit(btFoods, getResources().getString(R.string.label_foods));
        }
        else if (id == btDrinks.getId()) {
            fieldType = Field_Type.UNIT_DRINKS;
            showInputDialogUnit(btDrinks, getResources().getString(R.string.label_drinks));
        }
        else if (id == btResetSettings.getId()) {
            resetSettings();
        }
    }

    private void showInputDialogUnit(Button button, String title) {
        InputDialogFragment dialog = InputDialogFragment.newInstance(
                InputDialogType.NUTRIENT_UNIT, title, this, button.getText().toString());
        dialog.show(getFragmentManager(),"UnitInputDialog");
    }

    private void showInputDialogName(Button button, String title) {
        InputDialogFragment dialog = InputDialogFragment.newInstance(
                InputDialogType.NUTRIENT_NAME, title, this, button.getText().toString());
        dialog.show(getFragmentManager(),"UnitInputDialog");
    }

    private void onSaveClicked() {
        String strFoods  = btFoods.getText().toString().trim();
        String strDrinks = btDrinks.getText().toString().trim();

        String strName1 = btNutrientName1.getText().toString().trim();
        String strName2 = btNutrientName2.getText().toString().trim();
        String strName3 = btNutrientName3.getText().toString().trim();
        String strUnit1 = btUnit1.getText().toString().trim();
        String strUnit2 = btUnit2.getText().toString().trim();
        String strUnit3 = btUnit3.getText().toString().trim();

        // no field is allowed to be empty
        if (strFoods.isEmpty() || strDrinks.isEmpty() || strName1.isEmpty() || strName2.isEmpty() ||
                strName3.isEmpty() || strUnit1.isEmpty() || strUnit2.isEmpty() || strUnit3.isEmpty()) {
            Toast toast = Toast.makeText(
                    mainActivity,
                    getResources().getString(R.string.settings_text_empty_field),
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        AppPreferences prefs = mainActivity.getPreferences();
        prefs.setNutrientsAndUnits(
                strFoods, strDrinks,
                strName1, strUnit1,
                strName2, strUnit2,
                strName3, strUnit3);
        dismiss();
    }



    @Override
    public void onInputDialogAnswer(String answer) {
        switch (fieldType) {
            case UNIT_FOODS:
                btFoods.setText(answer);
                break;
            case UNIT_DRINKS:
                btDrinks.setText(answer);
                break;
            case UNIT_NUTRIENT1:
                btUnit1.setText(answer);
                break;
            case UNIT_NUTRIENT2:
                btUnit2.setText(answer);
                break;
            case UNIT_NUTRIENT3:
                btUnit3.setText(answer);
                break;
            case NAME_NUTRIENT1:
                btNutrientName1.setText(answer);
                break;
            case NAME_NUTRIENT2:
                btNutrientName2.setText(answer);
                break;
            case NAME_NUTRIENT3:
                btNutrientName3.setText(answer);
                break;
        }
    }

    private void resetSettings() {
        Resources resources = getResources();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(resources.getString(R.string.settings_reset_all_fields_dialog_title))
                .setMessage(resources.getString(R.string.settings_reset_all_fields_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(resources.getString(R.string.button_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainActivity.getPreferences().resetAllFields();
                                setFields();
                                dialogInterface.cancel();
                            }
                        })
                .setNegativeButton(resources.getString(R.string.button_no),
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
}
