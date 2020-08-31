package de.tommy13.sugar.page_categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.tommy13.sugar.input_dialog.InputDialogFragment;
import de.tommy13.sugar.input_dialog.InputDialogType;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.R;
import de.tommy13.sugar.database.CategoryItem;
import de.tommy13.sugar.page_foodlist.FoodItemDialogFragment;
import de.tommy13.sugar.publisher_observer.InputDialogReceiver;
import de.tommy13.sugar.menu.AppPreferences;

/**
 * Created by tommy on 05.03.2017.
 * Dialog for creating/changing a category.
 */

public class CategoryDialogFragment extends DialogFragment
        implements View.OnClickListener, InputDialogReceiver {

    private static String LOG_TAG = CategoryDialogFragment.class.getSimpleName();

    public static final String TITLE  = "title";
    public static final String ID     = "oldId";
    public static final String NAME   = "name";
    public static final String ENERGY = "energy";
    public static final String SUGAR  = "sugar";
    public static final String FAT    = "fat";
    public static final String AMOUNT_PER_PIECE = "amount_per_piece";


    private enum Field_Type {
        NAME, ENERGY, SUGAR, FAT, AMOUNT_PER_PIECE
    }

    private Field_Type   fieldType;
    private MainActivity mainActivity;

    private Button btSave;
    private Button btCancel;
    private Button btName;
    private Button btEnergy;
    private Button btSugar;
    private Button btFat;
    private Button btAmountPerPiece;

    private boolean isNewCategory;
    private long    oldId;

    private String errorEmptyName, errorDuplicateName;



    public CategoryDialogFragment() {}


    public static CategoryDialogFragment newInstance(String title, MainActivity mainActivity) {
        CategoryDialogFragment fragment = new CategoryDialogFragment();
        fragment.isNewCategory = true;
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
        fragment.mainActivity  = mainActivity;

        Bundle bundle = new Bundle();
        bundle.putString(FoodItemDialogFragment.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static CategoryDialogFragment newInstance(String title, CategoryItem categoryItem, MainActivity mainActivity) {
        CategoryDialogFragment fragment = newInstance(title, mainActivity);
        fragment.isNewCategory = false;
        fragment.oldId = categoryItem.getId();

        Bundle bundle = fragment.getArguments();
        bundle.putString(CategoryDialogFragment.TITLE, title);
        bundle.putLong(CategoryDialogFragment.ID, categoryItem.getId());
        bundle.putString(CategoryDialogFragment.NAME, categoryItem.getName());
        bundle.putString(CategoryDialogFragment.ENERGY, categoryItem.getKcal100() + "");
        bundle.putString(CategoryDialogFragment.SUGAR, categoryItem.getSugar100() + "");
        bundle.putString(CategoryDialogFragment.FAT, categoryItem.getFat100() + "");
        bundle.putString(CategoryDialogFragment.AMOUNT_PER_PIECE, categoryItem.getAmountPerPiece() + "");

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_category, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);

        setTextFields(view);
        errorEmptyName = view.getResources().getString(R.string.category_dialog_error_empty_name);
        errorDuplicateName = view.getResources().getString(R.string.category_dialog_error_duplicate_name);

        btSave   = (Button) view.findViewById(R.id.category_dialog_save);
        btSave.setOnClickListener(this);
        btCancel = (Button) view.findViewById(R.id.category_dialog_cancel);
        btCancel.setOnClickListener(this);

        btName   = (Button) view.findViewById(R.id.category_dialog_name);
        btName.setOnClickListener(this);
        btEnergy = (Button) view.findViewById(R.id.category_dialog_calories);
        btEnergy.setOnClickListener(this);
        btSugar  = (Button) view.findViewById(R.id.category_dialog_sugar);
        btSugar.setOnClickListener(this);
        btFat    = (Button) view.findViewById(R.id.category_dialog_fat);
        btFat.setOnClickListener(this);
        btAmountPerPiece = (Button) view.findViewById(R.id.category_dialog_amount_per_piece);
        btAmountPerPiece.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        getDialog().setTitle(bundle.getString(CategoryDialogFragment.TITLE));
        if (!isNewCategory) {
            btName.setText(bundle.getString(NAME, ""));
            btEnergy.setText(bundle.getString(ENERGY, ""));
            btSugar.setText(bundle.getString(SUGAR, ""));
            btFat.setText(bundle.getString(FAT, ""));
            btAmountPerPiece.setText(bundle.getString(AMOUNT_PER_PIECE, ""));
        }
    }

    public void setTextFields(View view) {
        AppPreferences prefs = mainActivity.getPreferences();

        TextView nutrients = (TextView) view.findViewById(R.id.category_dialog_tvNutrients);
        String label = getResources().getString(R.string.label_nutrients) + " " +
                getResources().getString(R.string.per) + " 100 " +
                prefs.getUnitFoods() + getResources().getString(R.string.divider) +
                prefs.getUnitDrinks();
        nutrients.setText(label);

        TextView energy = (TextView) view.findViewById(R.id.category_dialog_tvEnergy);
        energy.setText(getLabel(prefs, 1));
        TextView sugar = (TextView) view.findViewById(R.id.category_dialog_tvSugar);
        sugar.setText(getLabel(prefs, 2));
        TextView fat = (TextView) view.findViewById(R.id.category_dialog_tvFat);
        fat.setText(getLabel(prefs, 3));
        TextView amountPP = (TextView) view.findViewById(R.id.category_dialog_tvamount_per_piece);
        amountPP.setText(getLabelAmountPP(prefs));
    }

    private String getLabel(AppPreferences prefs, int nr) {
        return prefs.getNameNutrient(nr) + " " + getResources().getString(R.string.bracket_open) +
                prefs.getUnitNutrient(nr) + getResources().getString(R.string.bracket_close);
    }

    private String getLabelAmountPP(AppPreferences prefs) {
        return getResources().getString(R.string.label_amount_blank) + " " +
                getResources().getString(R.string.per) + " " +
                getResources().getString(R.string.piece) + " " +
                getResources().getString(R.string.bracket_open) +
                prefs.getUnitFoods() + getResources().getString(R.string.divider) +
                prefs.getUnitDrinks() + getResources().getString(R.string.bracket_close);
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
        AppPreferences prefs = mainActivity.getPreferences();

        if (id == btSave.getId()) {
            onSaveClicked();
        }
        else if (id == btCancel.getId()) {
            dismiss();
        }
        else if (id == btName.getId()) {
            InputDialogFragment dialog = InputDialogFragment.newInstance(
                    InputDialogType.TEXT,
                    getResources().getString(R.string.label_category_name),
                    this,
                    btName.getText().toString());
            fieldType = Field_Type.NAME;
            dialog.show(getFragmentManager(),"CategoryNameInputDialog");
        }
        else if (id == btEnergy.getId()) {
            fieldType = Field_Type.ENERGY;
            showInputDialogDezimal(prefs.getNameNutrient(1), prefs.getUnitNutrient(1), btEnergy);
        }
        else if (id == btSugar.getId()) {
            fieldType = Field_Type.SUGAR;
            showInputDialogDezimal(prefs.getNameNutrient(2), prefs.getUnitNutrient(2), btSugar);
        }
        else if (id == btFat.getId()) {
            fieldType = Field_Type.FAT;
            showInputDialogDezimal(prefs.getNameNutrient(3), prefs.getUnitNutrient(3), btFat);
        }
        else if (id == btAmountPerPiece.getId()) {
            fieldType = Field_Type.AMOUNT_PER_PIECE;
            showInputDialogAmountPP(prefs, btAmountPerPiece);
        }
    }

    private void showInputDialogAmountPP(AppPreferences prefs, Button button) {
        InputDialogFragment dialog = InputDialogFragment.newInstance(
                InputDialogType.DEZIMAL, getLabelAmountPP(prefs), this, button.getText().toString());
        dialog.show(getFragmentManager(),"InputDialog");
    }

    private void showInputDialogDezimal(String label, String unit, Button button) {
        AppPreferences prefs = mainActivity.getPreferences();
        String title = label + " " + getResources().getString(R.string.bracket_open) +
                unit + getResources().getString(R.string.bracket_close) + " " +
                getResources().getString(R.string.per) + " 100 " +
                prefs.getUnitFoods() + getResources().getString(R.string.divider) +
                prefs.getUnitDrinks();

        InputDialogFragment dialog = InputDialogFragment.newInstance(
                InputDialogType.DEZIMAL, title, this, button.getText().toString());
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

        String name     = btName.getText().toString().trim();
        String strKal   = btEnergy.getText().toString().trim();
        String strSugar = btSugar.getText().toString().trim();
        String strFat   = btFat.getText().toString().trim();
        String strAmountPP = btAmountPerPiece.getText().toString().trim();

        if (name.isEmpty()) {
            Toast toast = Toast.makeText(mainActivity, errorEmptyName, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (!isCategoryNameAllowed(name)) {
            Toast toast = Toast.makeText(mainActivity, errorDuplicateName, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // no field is empty, parse strings
        float kal = parseFloat(strKal);
        float sug = parseFloat(strSugar);
        float fat = parseFloat(strFat);
        float amountPP = parseFloat(strAmountPP);

        // return values to MainActivity
        saveCategory(name, kal, sug, fat, amountPP);
        dismiss();
    }


    private void saveCategory(String name, float calories, float sugar, float fat, float amountPP) {
        if (isNewCategory) {
            mainActivity.getCategoryData().createNewCategory(name, calories, sugar, fat, amountPP);
        }
        else {
            mainActivity.getCategoryData().changeCategory(oldId, name, calories, sugar, fat, amountPP);
        }
    }

    public boolean isCategoryNameAllowed(String categoryName) {
        return isNewCategory ?
                mainActivity.getCategoryData().isCategoryNameAllowed(categoryName) :
                mainActivity.getCategoryData().isCategoryNameAllowed(categoryName, oldId);
    }


    @Override
    public void onInputDialogAnswer(String answer) {
        switch (fieldType) {
            case NAME:
                btName.setText(answer);
                break;
            case ENERGY:
                btEnergy.setText(answer);
                break;
            case SUGAR:
                btSugar.setText(answer);
                break;
            case FAT:
                btFat.setText(answer);
                break;
            case AMOUNT_PER_PIECE:
                btAmountPerPiece.setText(answer);
                break;
        }
    }
}
