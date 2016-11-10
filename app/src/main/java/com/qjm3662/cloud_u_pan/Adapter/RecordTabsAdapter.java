package com.qjm3662.cloud_u_pan.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by qjm3662 on 2016/11/7 0007.
 */

public class RecordTabsAdapter extends FragmentPagerAdapter{
    String[] titles = {"下载", "上传", "直连"};
    private List<Fragment> fragmentList = null;
    public RecordTabsAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
