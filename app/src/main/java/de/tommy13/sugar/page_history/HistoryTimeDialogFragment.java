package de.tommy13.sugar.page_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.tommy13.sugar.R;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.input_dialog.InputDialogFragment;
import de.tommy13.sugar.input_dialog.InputDialogType;
import de.tommy13.sugar.menu.AppPreferences;
import de.tommy13.sugar.page_foodlist.FoodItemDialogFragment;
import de.tommy13.sugar.publisher_observer.InputDialogReceiver;

/**
 * Created by tommy on 05.03.2017.
 * Dialog to the the user preferences.
 */

public class HistoryTimeDialogFragment extends DialogFragment implements View.OnClickListener, InputDialogReceiver {

    public static final String TITLE  = "title";

    private MainActivity mainActivity;

    private Button btSave, btCancel, btTime;
    private Button btMin, btNormal, btMax;
    private int    time;


    public HistoryTimeDialogFragment() {}


    public static HistoryTimeDialogFragment newInstance(String title, MainActivity mainActivity) {
        HistoryTimeDialogFragment fragment = new HistoryTimeDialogFragment();
        fragment.mainActivity = mainActivity;
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        Bundle bundle = new Bundle();
        bundle.putString(FoodItemDialogFragment.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_history_time, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);

        btSave   = (Button) view.findViewById(R.id.dialog_history_save);
        btSave.setOnClickListener(this);
        btCancel = (Button) view.findViewById(R.id.dialog_history_cancel);
        btCancel.setOnClickListener(this);

        btTime   = (Button) view.findViewById(R.id.history_dialog_button_time) ;
        btTime.setOnClickListener(this);

        btMin    = (Button) view.findViewById(R.id.dialog_history_button_min);
        btMin.setOnClickListener(this);
        btNormal = (Button) view.findViewById(R.id.dialog_history_button_standard);
        btNormal.setOnClickListener(this);
        btMax    = (Button) view.findViewById(R.id.dialog_history_button_max);
        btMax.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        getDialog().setTitle(bundle.getString(HistoryTimeDialogFragment.TITLE));

        AppPreferences preferences = mainActivity.getPreferences();
        time = preferences.getTime();
        btTime.setText(String.valueOf(time));
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
        else if (id == btTime.getId()) {
            InputDialogFragment dialog = InputDialogFragment.newInstance(
                    InputDialogType.DEZIMAL,
                    getResources().getString(R.string.label_history_time),
                    this,
                    btTime.getText().toString());
            dialog.show(getFragmentManager(),"InputDialog");
        }
        else if (id == btMin.getId()) {
            time = AppPreferences.MIN_HISTORY_DAYS;
            btTime.setText(String.valueOf(AppPreferences.MIN_HISTORY_DAYS));
        }
        else if (id == btNormal.getId()) {
            time = AppPreferences.DEFAULT_HISTORY_DAYS;
            btTime.setText(String.valueOf(AppPreferences.DEFAULT_HISTORY_DAYS));
        }
        else if (id == btMax.getId()) {
            time = AppPreferences.MAX_HISTORY_DAYS;
            btTime.setText(String.valueOf(AppPreferences.MAX_HISTORY_DAYS));
        }
    }


    private void onSaveClicked() {
        // set preferences
        AppPreferences preferences = mainActivity.getPreferences();
        preferences.setTime(time);
        dismiss();
    }


    @Override
    public void onInputDialogAnswer(String answer) {
        time = parseInt(answer);
        btTime.setText(String.valueOf(time));
    }



    private int parseInt(String intString) {
        int result = 0;
        try {
            result = Integer.parseInt(intString);
        } catch (Exception e) {
            // do nothing
        }

        if (result < AppPreferences.MIN_HISTORY_DAYS) {
            result = AppPreferences.MIN_HISTORY_DAYS;
        }
        if (result > AppPreferences.MAX_HISTORY_DAYS) {
            result = AppPreferences.MAX_HISTORY_DAYS;
        }

        return result;
    }
}
