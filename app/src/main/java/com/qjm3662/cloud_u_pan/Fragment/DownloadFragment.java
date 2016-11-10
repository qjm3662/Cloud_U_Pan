package com.qjm3662.cloud_u_pan.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Adapter.RecordAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;

/**
 * Created by qjm3662 on 2016/11/7 0007.
 */

public class DownloadFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private RecordAdapter adapter;
    private TextView tv_empty_view;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_local_file_recording__upload, container, false);
        context = view.getContext();
        initListView(view);
        return view;
    }

    private void initListView(View view) {
        listView = (ListView) view.findViewById(R.id.list_localFile);
        adapter = new RecordAdapter(context, App.Public_List_Local_File_Download);
        listView.setAdapter(adapter);
        tv_empty_view = (TextView) view.findViewById(R.id.list_empty_view);
        listView.setEmptyView(tv_empty_view);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LocalFile localFile = App.Public_List_Local_File_Download.get(App.Public_List_Local_File_Download.size() - 1 - i);
        if(!localFile.isFlag_is_DateNode()){
            FileUtils.OpenFile(context, localFile.getPath(), localFile.getName());
        }
    }
}
