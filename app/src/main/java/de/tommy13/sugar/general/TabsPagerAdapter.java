package de.tommy13.sugar.general;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.tommy13.sugar.R;
import de.tommy13.sugar.page_categories.PageFragmentCategories;
import de.tommy13.sugar.page_history.PageFragmentHistory;
import de.tommy13.sugar.page_foodlist.PageFragmentList;
import de.tommy13.sugar.page_overview.PageFragmentOverview;

/**
 * Created by tommy on 01.03.2017.
 * Class to create the tabs.
 */

class TabsPagerAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_PAGES = 4;

    private MainActivity           mainActivity;


    TabsPagerAdapter(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
        this.mainActivity = mainActivity;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: default:
                PageFragmentOverview pageOverview = PageFragmentOverview.newInstance(mainActivity, 0);
                mainActivity.getPreferences().register(pageOverview);
                mainActivity.getFoodData().registerObserver(pageOverview);
                return pageOverview;
            case 1:
                PageFragmentList pageList = PageFragmentList.newInstance(mainActivity, 1);
                mainActivity.getPreferences().register(pageList);
                mainActivity.getFoodData().registerObserver(pageList);
                return pageList;
            case 2:
                PageFragmentHistory pageHistory = PageFragmentHistory.newInstance(mainActivity, 2);
                mainActivity.getPreferences().register(pageHistory);
                mainActivity.getFoodData().registerObserver(pageHistory);
                return pageHistory;
            case 3:
                PageFragmentCategories pageCategories = PageFragmentCategories.newInstance(mainActivity, 3);
                mainActivity.getPreferences().register(pageCategories);
                mainActivity.getCategoryData().registerObserver(pageCategories);
                return pageCategories;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: default:
                return mainActivity.getResources().getString(R.string.tabTitleOverview);
            case 1:
                return mainActivity.getResources().getString(R.string.tabTitleList);
            case 2:
                return mainActivity.getResources().getString(R.string.tabTitleHistory);
            case 3:
                return mainActivity.getResources().getString(R.string.tabTitleCategories);
        }
    }

}
