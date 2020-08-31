package de.tommy13.sugar.input_dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import de.tommy13.sugar.R;
import de.tommy13.sugar.publisher_observer.InputDialogReceiver;

/**
 * Created by tommy on 14.03.2017.
 * Class for user input.
 */

public class InputDialogFragment extends DialogFragment implements View.OnClickListener {

    private InputDialogType     type;
    private String              title;
    private InputDialogReceiver receiver;
    private String              startStr;

    private Button   btOk;
    private Button   btCancel;
    private EditText tvInput;

    public InputDialogFragment() {}

    public static InputDialogFragment newInstance(InputDialogType type, String title,
                                                  InputDialogReceiver receiver, String startStr) {
        InputDialogFragment fragment = new InputDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
        fragment.type                = type;
        fragment.title               = title;
        fragment.receiver            = receiver;
        fragment.startStr            = startStr;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_input, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(title);

        tvInput = (EditText) view.findViewById(R.id.input_dialog_input);
        tvInput.setText(startStr);

        int maxLength;
        if (type == InputDialogType.DEZIMAL) {
            tvInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            maxLength = InputDialogType.getMaxLength(InputDialogType.DEZIMAL);
        }
        else if (type == InputDialogType.BIG_DEZIMAL) {
            tvInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            maxLength = InputDialogType.getMaxLength(InputDialogType.BIG_DEZIMAL);
        }
        else if (type == InputDialogType.NUTRIENT_NAME){
            tvInput.setInputType(InputType.TYPE_CLASS_TEXT);
            maxLength = InputDialogType.getMaxLength(InputDialogType.NUTRIENT_NAME);
        }
        else if (type == InputDialogType.NUTRIENT_UNIT){
            tvInput.setInputType(InputType.TYPE_CLASS_TEXT);
            maxLength = InputDialogType.getMaxLength(InputDialogType.NUTRIENT_UNIT);
        }
        else {
            tvInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            maxLength = InputDialogType.getMaxLength(InputDialogType.TEXT);
        }
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(maxLength);
        tvInput.setFilters(inputFilters);

        btOk = (Button) view.findViewById(R.id.input_dialog_ok);
        btOk.setOnClickListener(this);
        btCancel = (Button) view.findViewById(R.id.input_dialog_cancel);
        btCancel.setOnClickListener(this);

        tvInput.requestFocus();
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == btCancel.getId()) {
            dismiss();
        } else if (id == btOk.getId()) {
            String answer = tvInput.getText().toString().trim();
            receiver.onInputDialogAnswer(answer);
            dismiss();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        receiver = null;
        dismiss();
    }
}
