package de.tommy13.sugar.page_foodlist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import de.tommy13.sugar.input_dialog.AutoCompleteInputDialogFragment;
import de.tommy13.sugar.general.DatePickerFragment;
import de.tommy13.sugar.input_dialog.InputDialogFragment;
import de.tommy13.sugar.input_dialog.InputDialogType;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.R;
import de.tommy13.sugar.database.CategoryData;
import de.tommy13.sugar.database.CategoryItem;
import de.tommy13.sugar.database.FoodItem;
import de.tommy13.sugar.publisher_observer.InputDialogReceiver;
import de.tommy13.sugar.menu.AppPreferences;

/**
 * Created by tommy on 05.03.2017.
 * Dialog for creating/changing a food item.
 */

public class FoodItemDialogFragment extends DialogFragment
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener, InputDialogReceiver {

    private static String LOG_TAG = de.tommy13.sugar.page_foodlist.FoodItemDialogFragment.class.getSimpleName();

    public static final String TITLE    = "title";
    public static final String ID       = "oldId";
    public static final String YEAR     = "year";
    public static final String MONTH    = "month";
    public static final String DAY      = "day";
    public static final String CATEGORY = "category";
    public static final String NOTE     = "note";
    public static final String AMOUNT   = "amount";
    public static final String KCAL     = "kcal";
    public static final String SUGAR    = "sugar";
    public static final String FAT      = "fat";
    public static final String PIECES   = "pieces";


    private enum Field_Type {
        CATEGORY, NOTE, AMOUNT, ENERGY, SUGAR, FAT, PIECES
    }

    private Field_Type   fieldType;
    private MainActivity mainActivity;


    private Button   btSave;
    private Button   btCancel;
    private Button   btCalcPieces;
    private Button   btCalcAmount;

    private int      currentYear;
    private int      currentMonth;
    private int      currentDay;

    private Button btDate;
    private Button btCategory;
    private Button btNote;
    private Button btAmount;
    private Button btEnergy;
    private Button btSugar;
    private Button btFat;
    private Button btPieces;


    private boolean isNewFoodItem;
    private long    oldId;

    private List<CategoryItem> categories;
    private String[]           categoryNames;

    private String errorUnknownCategory, errorEmptyName;







    /*---------------------------------------------------------------------------*/
    /*--------------------------- CREATION --------------------------------------*/
    /*---------------------------------------------------------------------------*/

    public FoodItemDialogFragment() {}

    public static FoodItemDialogFragment newInstance(String title, MainActivity mainActivity) {
        FoodItemDialogFragment fragment = new FoodItemDialogFragment();
        fragment.isNewFoodItem = true;
        fragment.mainActivity  = mainActivity;
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
        fragment.setCategories(mainActivity.getCategoryData());

        Bundle bundle = new Bundle();
        bundle.putString(FoodItemDialogFragment.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static FoodItemDialogFragment newInstance(String title, MainActivity mainActivity, FoodItem foodItem) {
        FoodItemDialogFragment fragment = newInstance(title, mainActivity);
        fragment.isNewFoodItem = false;
        fragment.oldId = foodItem.getId();

        Bundle bundle = fragment.getArguments();
        bundle.putString(FoodItemDialogFragment.TITLE, title);
        bundle.putLong(FoodItemDialogFragment.ID, foodItem.getId());
        bundle.putInt(FoodItemDialogFragment.YEAR, foodItem.getYear());
        bundle.putInt(FoodItemDialogFragment.MONTH, foodItem.getMonth());
        bundle.putInt(FoodItemDialogFragment.DAY, foodItem.getDay());
        bundle.putString(FoodItemDialogFragment.CATEGORY, foodItem.getCategory());
        bundle.putString(FoodItemDialogFragment.NOTE, foodItem.getNote());
        bundle.putString(FoodItemDialogFragment.AMOUNT, foodItem.getAmount() + "");
        bundle.putString(FoodItemDialogFragment.KCAL, foodItem.getKcal() + "");
        bundle.putString(FoodItemDialogFragment.SUGAR, foodItem.getSugar() + "");
        bundle.putString(FoodItemDialogFragment.FAT, foodItem.getFat() + "");
        bundle.putString(FoodItemDialogFragment.PIECES, foodItem.getPieces() + "");

        return fragment;
    }

    public void setCategories(CategoryData categoryData) {
        categories = categoryData.getCategories();
        categoryNames = new String[categories.size()];
        for (int i=0; i<categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_food_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);

        setTextFields(view);
        errorUnknownCategory = view.getResources().getString(R.string.food_item_dialog_error_unknown_category);
        errorEmptyName       = view.getResources().getString(R.string.food_item_dialog_error_empty_name);

        btSave   = (Button) view.findViewById(R.id.food_item_dialog_save);
        btSave.setOnClickListener(this);
        btCancel = (Button) view.findViewById(R.id.food_item_dialog_cancel);
        btCancel.setOnClickListener(this);
        btCalcPieces = (Button) view.findViewById(R.id.food_item_dialog_btCalcPieces);
        btCalcPieces.setOnClickListener(this);
        btCalcAmount = (Button) view.findViewById(R.id.food_item_dialog_btCalcAmount);
        btCalcAmount.setOnClickListener(this);

        btDate = (Button) view.findViewById(R.id.food_item_dialog_btdate);
        btDate.setOnClickListener(this);

        btCategory = (Button) view.findViewById(R.id.food_item_dialog_category);
        btCategory.setOnClickListener(this);
        btNote     = (Button) view.findViewById(R.id.food_item_dialog_note);
        btNote.setOnClickListener(this);
        btAmount   = (Button) view.findViewById(R.id.food_item_dialog_amount);
        btAmount.setOnClickListener(this);
        btEnergy   = (Button) view.findViewById(R.id.food_item_dialog_energy);
        btEnergy.setOnClickListener(this);
        btSugar    = (Button) view.findViewById(R.id.food_item_dialog_sugar);
        btSugar.setOnClickListener(this);
        btFat      = (Button) view.findViewById(R.id.food_item_dialog_fat);
        btFat.setOnClickListener(this);
        btPieces   = (Button) view.findViewById(R.id.food_item_dialog_pieces);
        btPieces.setOnClickListener(this);


        Bundle bundle = this.getArguments();
        getDialog().setTitle(bundle.getString(FoodItemDialogFragment.TITLE));

        if (isNewFoodItem) {
            Calendar calendar = Calendar.getInstance();
            setDate(calendar);
        }
        else {
            Calendar calendar = Calendar.getInstance();
            int year  = bundle.getInt(YEAR, getYear(calendar));
            int month = bundle.getInt(MONTH, getMonth(calendar));
            int day   = bundle.getInt(DAY, getDay(calendar));
            setDate(year, month, day);

            btCategory.setText(bundle.getString(CATEGORY, ""));

            String note = bundle.getString(NOTE, "");
            String defaultNote = getResources().getString(R.string.default_note);
            if (!note.equals(defaultNote)) {
                btNote.setText(bundle.getString(NOTE, ""));
            }

            btAmount.setText(bundle.getString(AMOUNT, ""));
            btEnergy.setText(bundle.getString(KCAL, ""));
            btSugar.setText(bundle.getString(SUGAR, ""));
            btFat.setText(bundle.getString(FAT, ""));
            btPieces.setText(bundle.getString(PIECES, ""));
        }

    }


    private void setTextFields(View view) {
        AppPreferences prefs = mainActivity.getPreferences();

        TextView tvAmount = (TextView) view.findViewById(R.id.food_item_dialog_tvAmount);
        String label = getResources().getString(R.string.label_amount_blank) +
                " " + getResources().getString(R.string.bracket_open) +
                prefs.getUnitFoods() + getResources().getString(R.string.divider) +
                prefs.getUnitDrinks() + getResources().getString(R.string.bracket_close);
        tvAmount.setText(label);

        TextView tvEnergy = (TextView) view.findViewById(R.id.food_item_dialog_tvEnergy);
        tvEnergy.setText(getLabel(prefs, 1));
        TextView tvSugar = (TextView) view.findViewById(R.id.food_item_dialog_tvSugar);
        tvSugar.setText(getLabel(prefs, 2));
        TextView tvFat = (TextView) view.findViewById(R.id.food_item_dialog_tvFat);
        tvFat.setText(getLabel(prefs, 3));
    }

    private String getLabel(AppPreferences prefs, int nr) {
        return prefs.getNameNutrient(nr) + " " + getResources().getString(R.string.bracket_open) +
                prefs.getUnitNutrient(nr) + getResources().getString(R.string.bracket_close);

    }




    @Override
    public void onPause() {
        super.onPause();
        mainActivity = null;
        dismiss();
    }









    /*-------------------------------------------------------------------------------*/
    /*------------------------- DATE ------------------------------------------------*/
    /*-------------------------------------------------------------------------------*/

    public int getDay(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }
    public int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public void setDate(Calendar calendar) {
        String strDayOfWeek = getDayOfWeek(calendar);
        String strDate = DateFormat.getDateInstance().format(calendar.getTime());
        btDate.setText(strDayOfWeek + ", " + strDate);

        currentYear  = getYear(calendar);
        currentMonth = getMonth(calendar);
        currentDay   = getDay(calendar);
    }
    public void setDate(int year, int month, int day) {
        // set the date text field
        Calendar calendar = getCalender(year, month, day);
        setDate(calendar);
    }

    public Calendar getCalender(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    public String getDayOfWeek(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String strDayOfWeek = getResources().getString(R.string.monday);
        if (dayOfWeek == Calendar.MONDAY) {
            strDayOfWeek = getResources().getString(R.string.monday);
        }
        else if (dayOfWeek == Calendar.TUESDAY) {
            strDayOfWeek = getResources().getString(R.string.tuesday);
        }
        else if (dayOfWeek == Calendar.WEDNESDAY) {
            strDayOfWeek = getResources().getString(R.string.wednesday);
        }
        else if (dayOfWeek == Calendar.THURSDAY) {
            strDayOfWeek = getResources().getString(R.string.thursday);
        }
        else if (dayOfWeek == Calendar.FRIDAY) {
            strDayOfWeek = getResources().getString(R.string.friday);
        }
        else if (dayOfWeek == Calendar.SATURDAY) {
            strDayOfWeek = getResources().getString(R.string.saturday);
        }
        else if (dayOfWeek == Calendar.SUNDAY) {
            strDayOfWeek = getResources().getString(R.string.sunday);
        }
        return strDayOfWeek;
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setDateListener(this);
        datePicker.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        setDate(year, month, day);
    }










    /*-------------------------------------------------------------------------------*/
    /*------------------------- CLICK -----------------------------------------------*/
    /*-------------------------------------------------------------------------------*/

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == btSave.getId()) {
            onButtonSaveClick();
        }
        else if (id == btCancel.getId()) {
            dismiss();
        }
        else if (id == btCalcPieces.getId()) {
            onButtonCalculateClick(true);
        }
        else if (id == btCalcAmount.getId()) {
            onButtonCalculateClick(false);
        }
        else if (id == btDate.getId()) {
            showDatePickerDialog();
        }
        else if (id == btCategory.getId()) {
            if (categoryNames == null) {
                return;
            }
            AutoCompleteInputDialogFragment dialog = AutoCompleteInputDialogFragment.newInstance(
                    getResources().getString(R.string.label_category),
                    categoryNames,
                    this,
                    btCategory.getText().toString());
            fieldType = Field_Type.CATEGORY;
            dialog.show(getFragmentManager(),"CategoryInputDialog");
        }
        else if (id == btNote.getId()) {
            InputDialogFragment dialog = InputDialogFragment.newInstance(
                    InputDialogType.TEXT,
                    getResources().getString(R.string.label_food_note),
                    this,
                    btNote.getText().toString());
            fieldType = Field_Type.NOTE;
            dialog.show(getFragmentManager(),"NoteInputDialog");
        }
        else if (id == btAmount.getId()) {
            fieldType = Field_Type.AMOUNT;
            String title = getResources().getString(R.string.label_amount_blank) + " " +
                    getResources().getString(R.string.bracket_open) +
                    mainActivity.getPreferences().getUnitFoods() +
                    getResources().getString(R.string.divider) +
                    mainActivity.getPreferences().getUnitDrinks() +
                    getResources().getString(R.string.bracket_close);
            InputDialogFragment dialog = InputDialogFragment.newInstance(
                    InputDialogType.BIG_DEZIMAL, title, this, btAmount.getText().toString());
            dialog.show(getFragmentManager(),"AmountInputDialog");
        }
        else if (id == btPieces.getId()) {
            fieldType = Field_Type.PIECES;
            String title = getResources().getString(R.string.pieces);
            InputDialogFragment dialog = InputDialogFragment.newInstance(
                    InputDialogType.DEZIMAL, title, this, btPieces.getText().toString());
            dialog.show(getFragmentManager(),"AmountInputDialog");
        }
        else if (id == btEnergy.getId()) {
            fieldType = Field_Type.ENERGY;
            showInputDialogDezimal(mainActivity.getPreferences(), 1, btEnergy);
        }
        else if (id == btSugar.getId()) {
            fieldType = Field_Type.SUGAR;
            showInputDialogDezimal(mainActivity.getPreferences(), 2, btSugar);
        }
        else if (id == btFat.getId()) {
            fieldType = Field_Type.FAT;
            showInputDialogDezimal(mainActivity.getPreferences(), 3, btFat);
        }
        else if (id == btFat.getId()) {
            fieldType = Field_Type.PIECES;
            String title = getResources().getString(R.string.pieces);
            InputDialogFragment dialog = InputDialogFragment.newInstance(
                    InputDialogType.DEZIMAL, title, this, btAmount.getText().toString());
            dialog.show(getFragmentManager(),"PiecesInputDialog");
        }
    }

    private void showInputDialogDezimal(AppPreferences prefs, int nr, Button button) {
        String title = prefs.getNameNutrient(nr) + " " + getResources().getString(R.string.bracket_open);
        title       += prefs.getUnitNutrient(nr) + getResources().getString(R.string.bracket_close);
        InputDialogFragment dialog = InputDialogFragment.newInstance(
                InputDialogType.DEZIMAL,
                title,
                this,
                button.getText().toString());
        dialog.show(getFragmentManager(),"FoodInputDialog");
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



    private void onButtonCalculateClick(boolean isPiecesButton) {
        String strCat    = btCategory.getText().toString().trim();
        CategoryItem categoryItem = getCategoryItem(strCat);

        if (categoryItem == null) {
            Toast toast = Toast.makeText(mainActivity, errorUnknownCategory, Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            float amount;
            if (isPiecesButton) {
                String strPieces = btPieces.getText().toString().trim();
                float  pieces    = parseFloat(strPieces);
                amount           = round(pieces * categoryItem.getAmountPerPiece());
                btAmount.setText(String.valueOf(amount));
            }
            else {
                String strAmount = btAmount.getText().toString().trim();
                amount           = parseFloat(strAmount);
            }

            float energy = calculateAbsolut(amount, categoryItem.getKcal100());
            float sugar  = calculateAbsolut(amount, categoryItem.getSugar100());
            float fat    = calculateAbsolut(amount, categoryItem.getFat100());

            btEnergy.setText(String.valueOf(energy));
            btSugar.setText(String.valueOf(sugar));
            btFat.setText(String.valueOf(fat));
        }
    }

    private float round(float value) {
        return Math.round(value * 10) / 10.0f;
    }

    private float calculateAbsolut(float amount, float percent) {
        float result = amount * percent / 100.0f;
        return round(result);
    }


    private void onButtonSaveClick() {
        String strCat    = btCategory.getText().toString();
        String strNote   = btNote.getText().toString();
        String strAmount = btAmount.getText().toString();
        String strEnergy = btEnergy.getText().toString();
        String strSugar  = btSugar.getText().toString();
        String strFat    = btFat.getText().toString();
        String strPieces = btPieces.getText().toString();

        if (strCat.isEmpty()) {
            Toast toast = Toast.makeText(mainActivity, errorEmptyName, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (strNote.isEmpty()) {
            strNote = getResources().getString(R.string.default_note);
        }

        // no field is empty, parse strings
        float amount = parseFloat(strAmount);
        float kal    = parseFloat(strEnergy);
        float sug    = parseFloat(strSugar);
        float fat    = parseFloat(strFat);
        float pieces = parseFloat(strPieces);

        // return values to MainActivity
        saveFoodItem(strCat, strNote, amount, kal, sug, fat, pieces);
        dismiss();
    }

    private void saveFoodItem(String category, String note, float amount, float calories, float sugar, float fat, float pieces) {
        if (isNewFoodItem) {
            FoodItem foodItem = mainActivity.getFoodData().createNewFoodItem(
                    currentYear, currentMonth, currentDay, category, note, amount, calories, sugar, fat, pieces);
            Log.d(LOG_TAG, "Written new Food Item to Database. Id: " + foodItem.getId());
        }
        else {
            FoodItem foodItem = mainActivity.getFoodData().changeFoodItem(oldId,
                    currentYear, currentMonth, currentDay, category, note, amount, calories, sugar, fat, pieces);
            Log.d(LOG_TAG, "Changed Food Item with id " + foodItem.getId());
        }
    }


    private CategoryItem getCategoryItem(String catName) {
        if (catName == null || catName.isEmpty()) {
            return null;
        }

        CategoryItem categoryItem = null;
        for (CategoryItem c : categories) {
            if (catName.equals(c.getName())) {
                categoryItem = c;
                break;
            }
        }

        return categoryItem;
    }


    @Override
    public void onInputDialogAnswer(String answer) {
        switch (fieldType) {
            case CATEGORY:
                btCategory.setText(answer);
                break;
            case NOTE:
                btNote.setText(answer);
                break;
            case AMOUNT:
                btAmount.setText(answer);
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
            case PIECES:
                btPieces.setText(answer);
                break;
        }
    }

}
