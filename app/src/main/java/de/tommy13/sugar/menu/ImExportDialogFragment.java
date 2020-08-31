package de.tommy13.sugar.menu;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.tommy13.sugar.R;
import de.tommy13.sugar.database.CategoryData;
import de.tommy13.sugar.database.CategoryItem;
import de.tommy13.sugar.general.DayOfWeek;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.general.NutrientType;
import de.tommy13.sugar.input_dialog.InputDialogFragment;
import de.tommy13.sugar.input_dialog.InputDialogType;
import de.tommy13.sugar.page_foodlist.FoodItemDialogFragment;
import de.tommy13.sugar.publisher_observer.InputDialogReceiver;

/**
 * Created by tommy on 05.03.2017.
 * Dialog to the the user preferences.
 */

public class ImExportDialogFragment extends DialogFragment implements View.OnClickListener, InputDialogReceiver {

    private enum Field_Type {
        SETTING_FILENAME, CATEGORY_FILENAME
    }

    private final NutrientType[] nutrientTypes = {
            NutrientType.ENERGY, NutrientType.SUGAR, NutrientType.FAT};
    private final DayOfWeek[]    daysOfWeek    = {
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};

    public static final String TITLE  = "title";

    private MainActivity mainActivity;

    private Button btOK;
    private Button settingsFilename, settingsImport, settingsExport;
    private Button categoriesFilename, categoriesImport, categoriesExport;
    private Switch overwriteCategories;

    private Field_Type fieldType;


    public ImExportDialogFragment() {}


    public static ImExportDialogFragment newInstance(String title, MainActivity mainActivity) {
        ImExportDialogFragment fragment = new ImExportDialogFragment();
        fragment.mainActivity = mainActivity;
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        Bundle bundle = new Bundle();
        bundle.putString(FoodItemDialogFragment.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_imexport, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);

        btOK   = (Button) view.findViewById(R.id.delete_ok);
        btOK.setOnClickListener(this);

        settingsFilename = (Button) view.findViewById(R.id.imexport_setting_file);
        settingsFilename.setOnClickListener(this);
        settingsImport   = (Button) view.findViewById(R.id.button_import_settings);
        settingsImport.setOnClickListener(this);
        settingsExport   = (Button) view.findViewById(R.id.button_export_settings);
        settingsExport.setOnClickListener(this);

        categoriesFilename = (Button) view.findViewById(R.id.imexport_category_file);
        categoriesFilename.setOnClickListener(this);
        categoriesImport   = (Button) view.findViewById(R.id.button_import_categories);
        categoriesImport.setOnClickListener(this);
        categoriesExport   = (Button) view.findViewById(R.id.button_export_categories);
        categoriesExport.setOnClickListener(this);

        overwriteCategories = (Switch) view.findViewById(R.id.imexport_overwrite_categories);

        Bundle bundle = this.getArguments();
        getDialog().setTitle(bundle.getString(ImExportDialogFragment.TITLE));

        setFields();
    }

    private void setFields() {
        settingsFilename.setText(getResources().getString(R.string.default_setting_filename));
        categoriesFilename.setText(getResources().getString(R.string.default_category_filename));
    }

































    /*-------------------------------------------------------------------------*/
    /*----------------------------- FLOW --------------------------------------*/
    /*-------------------------------------------------------------------------*/


    @Override
    public void onPause() {
        super.onPause();
        mainActivity = null;
        dismiss();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == btOK.getId()) {
            dismiss();
        }
        else if (id == settingsFilename.getId()) {
            fieldType = Field_Type.SETTING_FILENAME;
            showInputDialog(settingsFilename);
        }
        else if (id == settingsImport.getId()) {
            importSettings();
        }
        else if (id == settingsExport.getId()) {
            exportSettings();
        }
        else if (id == categoriesFilename.getId()) {
            fieldType = Field_Type.CATEGORY_FILENAME;
            showInputDialog(categoriesFilename);
        }
        else if (id == categoriesImport.getId()) {
            importCategories();
        }
        else if (id == categoriesExport.getId()) {
            exportCategories();
        }
    }







    /*-------------------------------------------------------------------------*/
    /*----------------- HELPER FOR EXPORT/IMPORT ------------------------------*/
    /*-------------------------------------------------------------------------*/

    private void showToast(String message) {
        Toast toast = Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean checkFileName(String fileName) {
        if (fileName.isEmpty()) {
            showToast(getResources().getString(R.string.error_filename_empty));
            return false;
        }
        return true;
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean checkExternalStorageReadable() {
        if (!isExternalStorageReadable()) {
            showToast(getResources().getString(R.string.storage_not_readable));
            return false;
        }
        return true;
    }

    private boolean checkExternalStorageWriteable() {
        if (!isExternalStorageWriteable()) {
            showToast(getResources().getString(R.string.storage_not_writeable));
            return false;
        }
        return true;
    }

    private File getDirectory() {
        String folder = getResources().getString(R.string.savings_folder);
        File dir = new File(Environment.getExternalStorageDirectory(), folder);
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        return dir;
    }

    private boolean checkFileExistenceForImport(File file) {
        if (!file.exists()) {
            showToast(getResources().getString(R.string.file_not_available));
            return false;
        }
        return true;
    }

    private boolean checkFileExistenceForExport(File file) {
        if (file.exists()) {
            showToast(getResources().getString(R.string.file_already_exists));
            return true;
        }
        return false;
    }

    private void tellFileNotImportable() {
        showToast(getResources().getString(R.string.file_not_importable));
    }

    private void tellFileNotCreatable() {
        showToast(getResources().getString(R.string.file_not_createable));
    }

    private void tellImportSuccess(boolean settings) {
        if (settings) {
            showToast(getResources().getString(R.string.import_success_settings));
        }
        else {
            showToast(getResources().getString(R.string.import_success_categories));
        }
    }

    private void tellExportSuccess(boolean settings) {
        if (settings) {
            showToast(getResources().getString(R.string.export_success_settings));
        }
        else {
            showToast(getResources().getString(R.string.export_success_categories));
        }
    }






    /*-------------------------------------------------------------------------*/
    /*----------------- EXPORT/IMPORT -----------------------------------------*/
    /*-------------------------------------------------------------------------*/

    private void exportSettings() {
        AppPreferences prefs = mainActivity.getPreferences();

        String fileName = settingsFilename.getText().toString().trim();
        if (!checkFileName(fileName) || !checkExternalStorageWriteable()) {
            return;
        }

        File dir  = getDirectory();
        File file = new File(dir, fileName);
        if (checkFileExistenceForExport(file)) {
            return;
        }

        try {
            if (file.createNewFile()) {
                tellFileNotCreatable();
                return;
            }

            // file is created, save data
            FileWriter writer = new FileWriter(file);

            String str = prefs.getUnitFoods() + "\n" +
                    prefs.getUnitDrinks() + "\n" +
                    prefs.getNameNutrient(1) + "\n" +
                    prefs.getUnitNutrient(1) + "\n" +
                    prefs.getNameNutrient(2) + "\n" +
                    prefs.getUnitNutrient(2) + "\n" +
                    prefs.getNameNutrient(3) + "\n" +
                    prefs.getUnitNutrient(3) + "\n";

            for (NutrientType type : nutrientTypes) {
                for (DayOfWeek day : daysOfWeek) {
                    str += prefs.getGoal(type, day) + "\n";
                }
            }

            writer.write(str);
            writer.close();

            tellExportSuccess(true);

        } catch (IOException e) {
            tellFileNotCreatable();
        }
    }


    private void exportCategories() {
        CategoryData categoryData = mainActivity.getCategoryData();

        String fileName = categoriesFilename.getText().toString().trim();
        if (!checkFileName(fileName) || !checkExternalStorageWriteable()) {
            return;
        }

        File dir  = getDirectory();
        File file = new File(dir, fileName);
        if (checkFileExistenceForExport(file)) {
            return;
        }


        try {
            if (!file.createNewFile()) {
                tellFileNotCreatable();
                return;
            }

            // file is created, save data
            FileWriter writer = new FileWriter(file);

            String str = "";

            ArrayList<CategoryItem> categoryItems = categoryData.getCategories();

            for (CategoryItem item : categoryItems) {
                str += item.getName() + "\n";
                str += item.getKcal100() + "\n";
                str += item.getSugar100() + "\n";
                str += item.getFat100() + "\n";
                str += item.getAmountPerPiece() + "\n";
            }

            writer.write(str);
            writer.close();

            tellExportSuccess(false);

        } catch (IOException e) {
            tellFileNotCreatable();
        }

    }


    private void importSettings() {
        AppPreferences prefs = mainActivity.getPreferences();

        String fileName = settingsFilename.getText().toString().trim();
        if (!checkFileName(fileName) || !checkExternalStorageReadable()) {
            return;
        }

        File dir  = getDirectory();
        File file = new File(dir, fileName);
        if (!checkFileExistenceForImport(file)) {
            return;
        }

        String[]       lines  = new String[29];
        try {
            // load data
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line = reader.readLine();
            for (int i=0; i<lines.length; i++) {
                lines[i] = "";
                if (line != null) {
                    lines[i] = line;
                    line     = reader.readLine();
                }
            }

            if (lines[0].isEmpty()) {
                lines[0] = prefs.getUnitFoods();
            }
            if (lines[1].isEmpty()) {
                lines[1] = prefs.getUnitDrinks();
            }
            if (lines[2].isEmpty()) {
                lines[2] = prefs.getNameNutrient(1);
            }
            if (lines[3].isEmpty()) {
                lines[3] = prefs.getUnitNutrient(1);
            }
            if (lines[4].isEmpty()) {
                lines[4] = prefs.getNameNutrient(2);
            }
            if (lines[5].isEmpty()) {
                lines[5] = prefs.getUnitNutrient(2);
            }
            if (lines[6].isEmpty()) {
                lines[6] = prefs.getNameNutrient(3);
            }
            if (lines[7].isEmpty()) {
                lines[7] = prefs.getUnitNutrient(3);
            }

            prefs.setNutrientsAndUnits(
                    lines[0], lines[1], lines[2], lines[3], lines[4], lines[5], lines[6], lines[7]);

            int pos = 8;
            for (NutrientType type : nutrientTypes) {
                prefs.setGoals(type, false, parseFloat(lines[pos]), parseFloat(lines[pos+1]),
                        parseFloat(lines[pos+2]), parseFloat(lines[pos+3]), parseFloat(lines[pos+4]),
                        parseFloat(lines[pos+5]), parseFloat(lines[pos+6]));
                pos += 7;
            }

            reader.close();

            tellImportSuccess(true);

        } catch (IOException e) {
            tellFileNotImportable();
        }
    }


    private void importCategories() {
        CategoryData categoryData = mainActivity.getCategoryData();

        String fileName = categoriesFilename.getText().toString().trim();
        if (!checkFileName(fileName) || !checkExternalStorageReadable()) {
            return;
        }

        File dir  = getDirectory();
        File file = new File(dir, fileName);
        if (!checkFileExistenceForImport(file)) {
            return;
        }

        ArrayList<CategoryItem> categoryItems = new ArrayList<>();
        try {
            // load data
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));

            String name = reader.readLine();

            while (name != null && !name.isEmpty()) {
                String nutrient1 = reader.readLine();
                if (nutrient1 == null || nutrient1.isEmpty()) {
                    break;
                }

                String nutrient2 = reader.readLine();
                if (nutrient2 == null || nutrient2.isEmpty()) {
                    break;
                }

                String nutrient3 = reader.readLine();
                if (nutrient3 == null || nutrient3.isEmpty()) {
                    break;
                }

                String amountPP = reader.readLine();
                if (amountPP == null || amountPP.isEmpty()) {
                    break;
                }

                CategoryItem item = new CategoryItem(
                        -1, name, parseFloat(nutrient1), parseFloat(nutrient2), parseFloat(nutrient3), parseFloat(amountPP));
                categoryItems.add(item);

                name = reader.readLine();
            }

            reader.close();

            tellImportSuccess(false);

        } catch (IOException e) {
            tellFileNotImportable();
        }

        // add categories
        boolean overwrite = overwriteCategories.isChecked();
        categoryData.addAll(categoryItems, overwrite);
    }






    /*-------------------------------------------------------------------------*/
    /*----------------- MORE HELPER -------------------------------------------*/
    /*-------------------------------------------------------------------------*/

    private float parseFloat(String floatString) {
        float result = 0f;
        try {
            result = Float.parseFloat(floatString);
            // round to one place after comma
            result = Math.round(10 * result) / 10.0f;
        } catch (Exception e) {
            // ignore
        }
        return result;
    }

    private void showInputDialog(Button button) {
        InputDialogFragment dialog = InputDialogFragment.newInstance(
                InputDialogType.FILENAME,
                getResources().getString(R.string.label_filename),
                this,
                button.getText().toString());
        dialog.show(getFragmentManager(),"FilenameInputDialog");
    }

    @Override
    public void onInputDialogAnswer(String answer) {
        switch (fieldType) {
            case SETTING_FILENAME:
                settingsFilename.setText(answer);
                break;
            default:
                categoriesFilename.setText(answer);
                break;
        }
    }

}
