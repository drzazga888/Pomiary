package com.drzazga.pomiary.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.fragment.CategoryListFragment;
import com.drzazga.pomiary.fragment.MeasureListFragment;
import com.drzazga.pomiary.fragment.MultiChoiceListFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public static final int MEASURE_LIST_FRAGMENT_ID = 0;
    public static final int CATEGORY_LIST_FRAGMENT_ID = 1;
    private MultiChoiceListFragment measureListFragment = new MeasureListFragment();
    private MultiChoiceListFragment categoryListFragment = new CategoryListFragment();
    private Context context;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public MultiChoiceListFragment getSpecificItem(int position) {
        switch (position) {
            case MEASURE_LIST_FRAGMENT_ID:
                return measureListFragment;
            case CATEGORY_LIST_FRAGMENT_ID:
                return categoryListFragment;
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return getSpecificItem(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case MEASURE_LIST_FRAGMENT_ID:
                return context.getString(R.string.app_name);
            case CATEGORY_LIST_FRAGMENT_ID:
                return context.getString(R.string.categories);
        }
        return super.getPageTitle(position);
    }
}
