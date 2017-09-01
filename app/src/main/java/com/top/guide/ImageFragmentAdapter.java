package com.top.guide;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ImageFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragment;

    public ImageFragmentAdapter(FragmentManager fm, List<Fragment> mFragment) {
        super(fm);
        this.mFragment = mFragment;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragment.get(arg0);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

}
