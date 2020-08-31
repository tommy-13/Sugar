package de.tommy13.sugar.page_categories;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.tommy13.sugar.general.NutrientType;
import de.tommy13.sugar.publisher_observer.CategoryDataObserver;
import de.tommy13.sugar.general.MainActivity;
import de.tommy13.sugar.R;
import de.tommy13.sugar.database.CategoryItem;
import de.tommy13.sugar.publisher_observer.PreferenceObserver;

/**
 * Created by tommy on 01.03.2017.
 * Page with the List of all Categories.
 */

public class PageFragmentCategories extends Fragment implements View.OnClickListener, CategoryDataObserver, PreferenceObserver{

    private static final String ARG_PAGE_NUMBER = "page_number";

    private MainActivity         mainActivity;
    private FloatingActionButton btAddCategory;
    private CategoryArrayAdapter categoryItemArrayAdapter;





    /*------------------------------------------------------------------------------*/
    /*-------------------------------- CREATION ------------------------------------*/
    /*------------------------------------------------------------------------------*/

    public PageFragmentCategories() {}

    public static PageFragmentCategories newInstance(MainActivity mainActivity, int page) {
        PageFragmentCategories fragment = new PageFragmentCategories();
        fragment.mainActivity = mainActivity;

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mainActivity == null) {
            mainActivity = (MainActivity) getActivity();
        }

        btAddCategory = (FloatingActionButton) view.findViewById(R.id.page_categories_add_item);
        btAddCategory.setOnClickListener(this);

        ArrayList<CategoryItem> categoryItems = mainActivity.getCategoryData().getCategories();
        categoryItemArrayAdapter = new CategoryArrayAdapter(getContext(), categoryItems, mainActivity.getPreferences());
        ListView categoryItemListView = (ListView) view.findViewById(R.id.page_categories_itemlist);
        categoryItemListView.setAdapter(categoryItemArrayAdapter);
        categoryItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CategoryItem categoryItem = (CategoryItem) adapterView.getItemAtPosition(position);
                changeCategory(categoryItem);
            }
        });
        categoryItemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                CategoryItem categoryItem = (CategoryItem) adapterView.getItemAtPosition(position);
                deleteCategory(categoryItem);
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == btAddCategory.getId()) {
            addNewCategory();
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        mainActivity.getCategoryData().removeObserver(this);
        mainActivity.getPreferences().remove(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.getCategoryData().registerObserver(this);
        mainActivity.getPreferences().register(this);
        showAllCategories();
    }





    /*------------------------------------------------------------------------------*/
    /*-------------------------------- LISTENER ------------------------------------*/
    /*------------------------------------------------------------------------------*/

    @Override
    public void updateOnCategoriesChanged() {
        showAllCategories();
    }


    @Override
    public void updateOnChangedNutrientLabels() {
        showAllCategories();
    }

    @Override
    public void updateOnChangedGoals(NutrientType nutrientType) {
        // do nothing
    }

    @Override
    public void updateOnChangedDate(int year, int month, int day, String dayOfWeek) {
        // do nothing
    }

    @Override
    public void updateOnChangedTime() {
        // do nothing
    }








    /*------------------------------------------------------------------------------*/
    /*------------------------------- CATEGORIES -----------------------------------*/
    /*------------------------------------------------------------------------------*/
    public void showAllCategories() {
        ArrayList<CategoryItem> categoryItems = mainActivity.getCategoryData().getCategories();
        categoryItemArrayAdapter.clear();
        categoryItemArrayAdapter.addAll(categoryItems);
    }


    private void addNewCategory() {
        String title = getResources().getString(R.string.category_dialog_title_new);
        CategoryDialogFragment dialogFragment = CategoryDialogFragment.newInstance(title, mainActivity);
        dialogFragment.show(mainActivity.getSupportFragmentManager(), "DialogNewCategoryItem");
    }


    private void changeCategory(CategoryItem categoryItem) {
        String title = getResources().getString(R.string.category_dialog_title_edit);
        CategoryDialogFragment dialogFragment = CategoryDialogFragment.newInstance(
                title, categoryItem, mainActivity);
        dialogFragment.show(mainActivity.getSupportFragmentManager(), "DialogChangeCategoryItem");
    }

    private void deleteCategory(final CategoryItem categoryItem) {
        Resources resources = mainActivity.getResources();
        final AlertDialog alertDialog = new AlertDialog.Builder(mainActivity)
                .setTitle(resources.getString(R.string.delete_category_dialog_title) + categoryItem.getName())
                .setMessage(resources.getString(R.string.delete_category_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(resources.getString(R.string.button_yes),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mainActivity.getCategoryData().deleteCategory(categoryItem);
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
