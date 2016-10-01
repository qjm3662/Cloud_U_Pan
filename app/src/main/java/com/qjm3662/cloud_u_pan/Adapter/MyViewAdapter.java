package com.qjm3662.cloud_u_pan.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by qjm3662 on 2016/9/29 0029.
 */

public class MyViewAdapter extends PagerAdapter {
    private List<View> mListView;

    public MyViewAdapter(List<View> mListView) {
        this.mListView = mListView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public int getCount() {
        return mListView.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.removeView(mListView.get(position % mListView.size()));
        container.addView(mListView.get(position % mListView.size()), 0);
        //对ViewPager页号求模取出View列表中要显示的项
        position %= mListView.size();
        if (position<0){
            position = mListView.size()+position;
        }
        View view = mListView.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        //add listeners here if necessary
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;//官方提示这样写
    }
}
