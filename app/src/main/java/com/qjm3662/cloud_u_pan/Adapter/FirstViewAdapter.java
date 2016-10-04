package com.qjm3662.cloud_u_pan.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by tanshunwang on 2016/10/4 0004.
 */

public class FirstViewAdapter extends PagerAdapter {
    private List<View> list;

    public FirstViewAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.removeView(list.get(position));
        container.addView(list.get(position), 0);
        //对ViewPager页号求模取出View列表中要显示的项
        if (position<0){
            position = list.size()+position;
        }
        View view = list.get(position);
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
}
