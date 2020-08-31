package de.tommy13.sugar.page_overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import de.tommy13.sugar.general.DayOfWeek;
import de.tommy13.sugar.input_dialog.InputDialogFragment;
import de.tommy13.sugar.input_dialog.InputDialogType;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.general.NutrientType;
import de.tommy13.sugar.R;
import de.tommy13.sugar.page_foodlist.FoodItemDialogFragment;
import de.tommy13.sugar.publisher_observer.InputDialogReceiver;
import de.tommy13.sugar.menu.AppPreferences;

/**
 * Created by tommy on 05.03.2017.
 * Dialog to the the user preferences.
 */

public class SettingsGoalDialogFragment extends DialogFragment
        implements View.OnClickListener, InputDialogReceiver, CompoundButton.OnCheckedChangeListener {

    private static String LOG_TAG = SettingsGoalDialogFragment.class.getSimpleName();

    public static final String TITLE  = "title";


    private enum Field_Type {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, ALLDAY
    }

    private Field_Type   fieldType;
    private MainActivity mainActivity;

    private NutrientType nutrientType;

    private Button btSave;
    private Button btCancel;
    private Button btMonday, btTuesday, btWednesday, btThursday, btFriday, btSaturday, btSunday;
    private Button btAllday;
    private Switch daySwitch;


    public SettingsGoalDialogFragment() {}


    public static SettingsGoalDialogFragment newInstance(String title, MainActivity mainActivity,
                                                         NutrientType nutrientType) {
        SettingsGoalDialogFragment fragment = new SettingsGoalDialogFragment();
        fragment.mainActivity = mainActivity;
        fragment.nutrientType = nutrientType;
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        Bundle bundle = new Bundle();
        bundle.putString(FoodItemDialogFragment.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_goals, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);

        btSave   = (Button) view.findViewById(R.id.settings_save);
        btSave.setOnClickListener(this);
        btCancel = (Button) view.findViewById(R.id.settings_cancel);
        btCancel.setOnClickListener(this);

        btMonday = (Button) view.findViewById(R.id.goals_monday);
        btMonday.setOnClickListener(this);
        btTuesday = (Button) view.findViewById(R.id.goals_tuesday);
        btTuesday.setOnClickListener(this);
        btWednesday = (Button) view.findViewById(R.id.goals_wednesday);
        btWednesday.setOnClickListener(this);
        btThursday = (Button) view.findViewById(R.id.goals_thursday);
        btThursday.setOnClickListener(this);
        btFriday = (Button) view.findViewById(R.id.goals_friday);
        btFriday.setOnClickListener(this);
        btSaturday = (Button) view.findViewById(R.id.goals_saturday);
        btSaturday.setOnClickListener(this);
        btSunday = (Button) view.findViewById(R.id.goals_sunday);
        btSunday.setOnClickListener(this);
        btAllday = (Button) view.findViewById(R.id.goals_everyday);
        btAllday.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        getDialog().setTitle(bundle.getString(SettingsGoalDialogFragment.TITLE));

        AppPreferences preferences = mainActivity.getPreferences();
        btAllday.setText(String.valueOf(preferences.getGoal(nutrientType, DayOfWeek.MONDAY)));
        btMonday.setText(String.valueOf(preferences.getGoal(nutrientType, DayOfWeek.MONDAY)));
        btTuesday.setText(String.valueOf(preferences.getGoal(nutrientType, DayOfWeek.TUESDAY)));
        btWednesday.setText(String.valueOf(preferences.getGoal(nutrientType, DayOfWeek.WEDNESDAY)));
        btThursday.setText(String.valueOf(preferences.getGoal(nutrientType, DayOfWeek.THURSDAY)));
        btFriday.setText(String.valueOf(preferences.getGoal(nutrientType, DayOfWeek.FRIDAY)));
        btSaturday.setText(String.valueOf(preferences.getGoal(nutrientType, DayOfWeek.SATURDAY)));
        btSunday.setText(String.valueOf(preferences.getGoal(nutrientType, DayOfWeek.SUNDAY)));

        boolean isGoalGlobalChecked = preferences.isGoalGlobal(nutrientType);

        daySwitch = (Switch) view.findViewById(R.id.goals_everyday_switch);
        daySwitch.setChecked(isGoalGlobalChecked);
        daySwitch.setOnCheckedChangeListener(this);

        enableButtons(isGoalGlobalChecked);
    }

    private void enableButtons(boolean isGoalGlobalChecked) {
        btAllday.setEnabled(isGoalGlobalChecked);
        btMonday.setEnabled(!isGoalGlobalChecked);
        btTuesday.setEnabled(!isGoalGlobalChecked);
        btWednesday.setEnabled(!isGoalGlobalChecked);
        btThursday.setEnabled(!isGoalGlobalChecked);
        btFriday.setEnabled(!isGoalGlobalChecked);
        btSaturday.setEnabled(!isGoalGlobalChecked);
        btSunday.setEnabled(!isGoalGlobalChecked);

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
        else if (id == btAllday.getId()) {
            fieldType = Field_Type.ALLDAY;
            showInputDialog(btAllday.getText().toString());
        }
        else if (id == btMonday.getId()) {
            fieldType = Field_Type.MONDAY;
            showInputDialog(btMonday.getText().toString());
        }
        else if (id == btTuesday.getId()) {
            fieldType = Field_Type.TUESDAY;
            showInputDialog(btTuesday.getText().toString());
        }
        else if (id == btWednesday.getId()) {
            fieldType = Field_Type.WEDNESDAY;
            showInputDialog(btWednesday.getText().toString());
        }
        else if (id == btThursday.getId()) {
            fieldType = Field_Type.THURSDAY;
            showInputDialog(btThursday.getText().toString());
        }
        else if (id == btFriday.getId()) {
            fieldType = Field_Type.FRIDAY;
            showInputDialog(btFriday.getText().toString());
        }
        else if (id == btSaturday.getId()) {
            fieldType = Field_Type.SATURDAY;
            showInputDialog(btSaturday.getText().toString());
        }
        else if (id == btSunday.getId()) {
            fieldType = Field_Type.SUNDAY;
            showInputDialog(btSunday.getText().toString());
        }
    }

    private void showInputDialog(String fieldValue) {

        AppPreferences prefs = mainActivity.getPreferences();
        int            nr    = 1;

        switch (nutrientType) {
            case ENERGY: nr = 1; break;
            case SUGAR:  nr = 2; break;
            case FAT:    nr = 3; break;
        }

        String dialogTitle = prefs.getNameNutrient(nr) + " " + getResources().getString(R.string.bracket_open)
                + prefs.getUnitNutrient(nr) + getResources().getString(R.string.bracket_close);

        InputDialogFragment dialog = InputDialogFragment.newInstance(
                InputDialogType.DEZIMAL, dialogTitle, this, fieldValue);
        dialog.show(getFragmentManager(),"InputDialog");
    }

    private float parseFloat(String floatString) {
        float result = 0f;
        try {
            result = Float.parseFloat(floatString);
            // round to one place after comma
            result = Math.round(10 * result) / 10.0f;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Parsing float error! Number set to zero.");
        }
        return result;
    }


    private void onSaveClicked() {
        boolean isGoalGlobal = daySwitch.isChecked();

        String strMo = btMonday.getText().toString().trim();
        String strTu = btTuesday.getText().toString().trim();
        String strWe = btWednesday.getText().toString().trim();
        String strTh = btThursday.getText().toString().trim();
        String strFr = btFriday.getText().toString().trim();
        String strSa = btSaturday.getText().toString().trim();
        String strSu = btSunday.getText().toString().trim();

        // parse strings
        float valMo = parseFloat(strMo);
        float valTu = parseFloat(strTu);
        float valWe = parseFloat(strWe);
        float valTh = parseFloat(strTh);
        float valFr = parseFloat(strFr);
        float valSa = parseFloat(strSa);
        float valSu = parseFloat(strSu);

        // set preferences
        AppPreferences preferences = mainActivity.getPreferences();
        preferences.setGoals(nutrientType, isGoalGlobal,
                valMo, valTu, valWe, valTh, valFr, valSa, valSu);

        dismiss();
    }


    @Override
    public void onInputDialogAnswer(String answer) {
        switch (fieldType) {
            case ALLDAY:
                setAllInputButtons(answer);
                break;
            case MONDAY:
                btMonday.setText(answer);
                break;
            case TUESDAY:
                btTuesday.setText(answer);
                break;
            case WEDNESDAY:
                btWednesday.setText(answer);
                break;
            case THURSDAY:
                btThursday.setText(answer);
                break;
            case FRIDAY:
                btFriday.setText(answer);
                break;
            case SATURDAY:
                btSaturday.setText(answer);
                break;
            case SUNDAY:
                btSunday.setText(answer);
                break;
        }
    }

    private void setAllInputButtons(String text) {
        btAllday.setText(text);
        btMonday.setText(text);
        btTuesday.setText(text);
        btWednesday.setText(text);
        btThursday.setText(text);
        btFriday.setText(text);
        btSaturday.setText(text);
        btSunday.setText(text);
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == daySwitch.getId()) {
            setAllInputButtons(btAllday.getText().toString());
            enableButtons(isChecked);
        }
    }
}
