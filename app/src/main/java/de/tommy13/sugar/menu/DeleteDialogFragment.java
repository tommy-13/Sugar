package de.tommy13.sugar.menu;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.Calendar;

import de.tommy13.sugar.general.DatePickerFragment;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.R;
import de.tommy13.sugar.page_foodlist.FoodItemDialogFragment;

/**
 * Created by tommy on 05.03.2017.
 * Dialog to the the user preferences.
 */

public class DeleteDialogFragment extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String TITLE  = "title";

    private MainActivity mainActivity;

    private Button btOK;

    private Button btDate;
    private Button btDeleteData;
    private Button btResetGoals;
    private Button btDeleteCategories;
    private Button btResetCategories;

    private int      currentYear;
    private int      currentMonth;
    private int      currentDay;


    public DeleteDialogFragment() {}


    public static DeleteDialogFragment newInstance(String title, MainActivity mainActivity) {
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        fragment.mainActivity = mainActivity;
        fragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        Bundle bundle = new Bundle();
        bundle.putString(FoodItemDialogFragment.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_delete, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);

        btOK   = (Button) view.findViewById(R.id.delete_ok);
        btOK.setOnClickListener(this);

        btDate       = (Button) view.findViewById(R.id.delete_data_date);
        btDate.setOnClickListener(this);
        btDeleteData = (Button) view.findViewById(R.id.button_delete_data);
        btDeleteData.setOnClickListener(this);

        btResetGoals       = (Button) view.findViewById(R.id.delete_reset_values);
        btResetGoals.setOnClickListener(this);
        btDeleteCategories = (Button) view.findViewById(R.id.delete_delete_categories);
        btResetCategories  = (Button) view.findViewById(R.id.delete_reset_categories);
        btDeleteCategories.setOnClickListener(this);
        btResetCategories.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        getDialog().setTitle(bundle.getString(DeleteDialogFragment.TITLE));

        setFields();
    }

    private void setFields() {
        Calendar calendar = Calendar.getInstance();
        setDate(calendar);
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

        if (id == btOK.getId()) {
            dismiss();
        }
        else if (id == btDate.getId()) {
            showDatePickerDialog();
        }
        else if (id == btDeleteData.getId()) {
            deleteData();
        }
        else if (id == btDeleteCategories.getId()) {
            deleteAllCategories();
        }
        else if (id == btResetCategories.getId()) {
            resetCategories();
        }
        else if (id == btResetGoals.getId()) {
            resetGoals();
        }
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







    private void deleteData() {
        Resources resources = getResources();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(resources.getString(R.string.delete_food_items_dialog_title))
                .setMessage(resources.getString(R.string.delete_food_items_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(resources.getString(R.string.button_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainActivity.getFoodData().deleteFoodItemsUntil(currentYear, currentMonth, currentDay);
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



    private void deleteAllCategories() {
        Resources resources = getResources();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(resources.getString(R.string.delete_all_categories_dialog_title))
                .setMessage(resources.getString(R.string.delete_all_categories_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(resources.getString(R.string.button_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainActivity.getCategoryData().deleteAllCategories();
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

    private void resetCategories() {
        Resources resources = getResources();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(resources.getString(R.string.reset_categories_dialog_title))
                .setMessage(resources.getString(R.string.reset_categories_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(resources.getString(R.string.button_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainActivity.getCategoryData().resetCategories();
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


    private void resetGoals() {
        Resources resources = getResources();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(resources.getString(R.string.reset_goals_dialog_title))
                .setMessage(resources.getString(R.string.reset_goals_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(resources.getString(R.string.button_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainActivity.getPreferences().setToDefaultGoals();
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
