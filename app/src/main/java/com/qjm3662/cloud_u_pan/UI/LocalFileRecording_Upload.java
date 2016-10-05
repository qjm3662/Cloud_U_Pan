package com.qjm3662.cloud_u_pan.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Adapter.LocalFileUploadRecordingAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;

public class LocalFileRecording_Upload extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ListView listView;
    private LocalFileUploadRecordingAdapter adapter;
    private TextView tv_empty_view;
    private TextView tv_bar;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_file_recording__upload);
        initListView();
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.list_localFile);
        adapter = new LocalFileUploadRecordingAdapter(this);
        listView.setAdapter(adapter);
        tv_empty_view = (TextView) findViewById(R.id.list_empty_view);
        listView.setEmptyView(tv_empty_view);
        listView.setOnItemClickListener(this);
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_bar.setText("上传历史");

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
        LocalFile localFile = App.Public_List_Local_File_Upload.get(App.Public_List_Local_File_Upload.size() - 1 - i);
        if(!localFile.isFlag_is_DateNode()){
            FileUtils.OpenFile(this, localFile.getPath(), localFile.getName());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
