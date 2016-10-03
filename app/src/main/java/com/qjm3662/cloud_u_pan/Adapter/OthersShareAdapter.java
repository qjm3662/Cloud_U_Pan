package com.qjm3662.cloud_u_pan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.R;

import java.util.List;

/**
 * Created by tanshunwang on 2016/10/3 0003.
 */

public class OthersShareAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<FileInformation> list;

    public OthersShareAdapter(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public OthersShareAdapter(Context context, List<FileInformation> list) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    class ViewHolder{
        private ImageView img_head;
        private TextView tv_fileName;
        private TextView tv_time;
    }

    @Override
    public int getCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(list.size() - 1 - position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.local_file_recording_item, null);
            viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
            viewHolder.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FileInformation fileInformation = list.get(list.size() - 1 - position);
        viewHolder.img_head.setImageBitmap(fileInformation.getBitmap_type());
        viewHolder.tv_fileName.setText(fileInformation.getName());
        viewHolder.tv_time.setText(fileInformation.getDownTimeString());
        return convertView;
    }
}
