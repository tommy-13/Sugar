package de.tommy13.sugar.general;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import de.tommy13.sugar.R;
import de.tommy13.sugar.database.CategoryData;
import de.tommy13.sugar.database.FoodData;
import de.tommy13.sugar.database.SugarDataSource;
import de.tommy13.sugar.menu.AppPreferences;
import de.tommy13.sugar.menu.DeleteDialogFragment;
import de.tommy13.sugar.menu.ImExportDialogFragment;
import de.tommy13.sugar.menu.SettingsDialogFragment;

/**
 * Created by tommy on 01.03.2017.
 * The main activity of this app.
 */

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private AppPreferences   preferences;
    private CategoryData     categoryData;
    private FoodData         foodData;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SugarDataSource dataSource = SugarDataSource.newInstance(this);
        categoryData = new CategoryData(dataSource);
        foodData     = new FoodData(dataSource);
        preferences  = new AppPreferences(this);

        if (savedInstanceState != null) {
            int year  = savedInstanceState.getInt(YEAR, preferences.getYear());
            int month = savedInstanceState.getInt(MONTH, preferences.getMonth());
            int day   = savedInstanceState.getInt(DAY, preferences.getDay());
            preferences.setDate(year, month, day);
        }

        if (preferences.isFirstStart()) {
            dataSource.setCategoriesOnFirstUse();
        }

        setContentView(R.layout.activity_main);
        setTitle(preferences.getShortDateString());

        TabLayout tabs   = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager  = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this);

        pager.setAdapter(tabsPagerAdapter);
        tabs.setupWithViewPager(pager);

        verifyStoragePermission();
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    private static final String YEAR  = "year";
    private static final String MONTH = "month";
    private static final String DAY   = "day";
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(YEAR, preferences.getYear());
        outState.putInt(MONTH, preferences.getMonth());
        outState.putInt(DAY, preferences.getDay());
    }








    /*-------------------------------------------------------------------------*/
    /*----------------------------- PERMISSIONS -------------------------------*/
    /*-------------------------------------------------------------------------*/
    private final String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private void verifyStoragePermission() {
        int permission = ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_EXTERNAL_STORAGE = 1;
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }










    /*-------------------------------------------------------------------------*/
    /*----------------------------- OPTIONS MENU ------------------------------*/
    /*-------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            String title = getResources().getString(R.string.settings);
            SettingsDialogFragment settingsFragment = SettingsDialogFragment.newInstance(title,this);
            settingsFragment.show(getSupportFragmentManager(), "DialogSettings");
            return true;
        }
        else if (id == R.id.menu_delete) {
            String title = getResources().getString(R.string.delete_title);
            DeleteDialogFragment deleteFragment = DeleteDialogFragment.newInstance(title,this);
            deleteFragment.show(getSupportFragmentManager(), "DialogDelete");
            return true;
        }
        else if (id == R.id.menu_calendar) {
            showDatePickerDialog();
            return true;
        }
        else if (id == R.id.menu_imexport) {
            showImExportDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showDatePickerDialog() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setDateListener(this);
        datePicker.show(getSupportFragmentManager(), "DatePicker");
    }

    private void showImExportDialog() {
        ImExportDialogFragment dialog = ImExportDialogFragment.newInstance(
                getResources().getString(R.string.imexport_title), this);
        dialog.show(getSupportFragmentManager(), "ImExportDialog");
    }







    /*-------------------------------------------------------------------------*/
    /*-------------------------- SETTER AND GETTER ----------------------------*/
    /*-------------------------------------------------------------------------*/
    public CategoryData getCategoryData() {
        return categoryData;
    }
    public FoodData getFoodData() {
        return foodData;
    }
    public AppPreferences getPreferences() {
        return preferences;
    }









    /*-------------------------------------------------------------------------*/
    /*-------------------------- DATE -----------------------------------------*/
    /*-------------------------------------------------------------------------*/
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        preferences.setDate(year, month, dayOfMonth);
        setTitle(preferences.getShortDateString());
    }
}
