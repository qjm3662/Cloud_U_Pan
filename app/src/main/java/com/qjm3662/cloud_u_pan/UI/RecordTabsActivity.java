package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import com.astuetz.PagerSlidingTabStrip;
import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.Adapter.RecordTabsAdapter;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.Fragment.DownloadFragment;
import com.qjm3662.cloud_u_pan.Fragment.UploadRecordFragment;
import com.qjm3662.cloud_u_pan.Fragment.WifiDirectRecordFragment;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class RecordTabsActivity extends FragmentActivity {

    private ViewPager pager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private LayoutInflater inflater;
    private List<Fragment> list = new ArrayList<Fragment>();
    private Fragment v_upload;
    private Fragment v_download;
    private Fragment v_wifi_trans;
    private RecordTabsAdapter adapter;
    private EasyBar easyBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_tabs);
        initView();
    }

    private void initView() {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        pager = (ViewPager) findViewById(R.id.viewpager);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        v_upload = new UploadRecordFragment();
        v_download = new DownloadFragment();
        v_wifi_trans = new WifiDirectRecordFragment();
        list.add(v_download);
        list.add(v_upload);
        list.add(v_wifi_trans);
        adapter = new RecordTabsAdapter(this.getSupportFragmentManager(), list);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);

        EasyBarUtils.justSetTitleAndBack("传输记录", this, 2);

        pagerSlidingTabStrip.setShouldExpand(true);
        pagerSlidingTabStrip.setTextSize(DensityUtil.dip2px(this, 18));
//        pagerSlidingTabStrip.setTextColor(Color.WHITE);
        try{
            pagerSlidingTabStrip.setViewPager(pager);
        }catch (Exception e){
            System.out.println("error : " + e.toString());
        }

    }
}
