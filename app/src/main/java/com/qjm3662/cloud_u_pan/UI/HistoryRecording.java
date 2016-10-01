package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Adapter.LocalFileDownloadRecordingAdapter;
import com.qjm3662.cloud_u_pan.Adapter.LocalFileUploadRecordingAdapter;
import com.qjm3662.cloud_u_pan.Adapter.MyViewAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecording extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewAdapter adapterViewPager;
    private View view_down;
    private View view_upload;
    private List<View> list;
    private LayoutInflater inflater;

    private ListView lv_upload;
    private ListView lv_download;
    private TextView tv_empty_view;
    private TextView tv_bar;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histry_recording);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        list = new ArrayList<View>();
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        view_down = inflater.inflate(R.layout.activity_local_file_recording__download, null);
        view_upload = inflater.inflate(R.layout.activity_local_file_recording__upload, null);
        initListView(lv_download, view_down, new LocalFileDownloadRecordingAdapter(this), 1);
        initListView(lv_upload, view_upload, new LocalFileUploadRecordingAdapter(this), 2);

        list.add(view_down);
        list.add(view_upload);
        adapterViewPager = new MyViewAdapter(list);
        viewPager.setAdapter(adapterViewPager);

        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("历史纪录");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     *
     * @param listView
     * @param view
     * @param adapter
     * @param flag  1-download     2-upload
     */
    private void initListView(ListView listView, View view, ListAdapter adapter, final int flag) {
        listView = (ListView) view.findViewById(R.id.list_localFile);
        TextView textView = new TextView(this);
        if(flag == 1){
            textView.setText("下载");
        }else{
            textView.setText("上传");
        }
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(24);
        listView.addHeaderView(textView, null, false);
        listView.setAdapter(adapter);
        tv_empty_view = (TextView) view.findViewById(R.id.list_empty_view);
        listView.setEmptyView(tv_empty_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LocalFile localFile = (flag == 1)?App.Public_List_Local_File_Download.get(App.Public_List_Local_File_Download.size() - i) : App.Public_List_Local_File_Upload.get(App.Public_List_Local_File_Upload.size() - i);
                if(!localFile.isFlag_is_DateNode()){
                    FileUtils.OpenFile(HistoryRecording.this, localFile.getPath(), localFile.getName());
                }
            }
        });
    }
}
