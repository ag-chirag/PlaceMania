package com.place.mania.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.place.mania.SearchActivity;

import java.util.List;

public class SearchFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<SearchActivity.SearchResultPage> pages;

    public SearchFragmentPagerAdapter(FragmentManager fm, List<SearchActivity.SearchResultPage> pages) {
        super(fm);
        this.pages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).getTitle();
    }

    //******************************* Global Constants & Properties ********************************//
//********************************** Public Getters & Setters **********************************//
//*************************************** Constructor(s) ***************************************//
//********************************** Public Methods ********************************************//

//************************************* Interfaces *********************************************//
//************************************* Private Methods ****************************************//
//************************************* Implemented Methods ************************************//
}
