package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Adapter.RecordAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;

public class LocalFileRecording_Download extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private RecordAdapter adapter;
    private TextView tv_empty_view;
    private TextView tv_bar;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_file_recording__download);
        initListView();
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.list_localFile);
        adapter = new RecordAdapter(this, App.Public_List_Local_File_Download);
        listView.setAdapter(adapter);
        tv_empty_view = (TextView) findViewById(R.id.list_empty_view);
        listView.setEmptyView(tv_empty_view);
        listView.setOnItemClickListener(this);
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("下载历史");

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LocalFile localFile = App.Public_List_Local_File_Download.get(App.Public_List_Local_File_Download.size() - 1 - i);
        if(!localFile.isFlag_is_DateNode()){
            FileUtils.OpenFile(this, localFile.getPath(), localFile.getName());
        }
    }
}
