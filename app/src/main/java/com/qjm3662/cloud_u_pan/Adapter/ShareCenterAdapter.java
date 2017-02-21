package com.qjm3662.cloud_u_pan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.R;

import java.util.List;

/**
 * Created by tanshunwang on 2016/10/2 0002.
 */

public class ShareCenterAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int size;
    private List<FileInformation> list;

    public ShareCenterAdapter(Context context, List<FileInformation> list) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    class ViewHolder {
        private ImageView img_head;
        private TextView tv_fileName;
        private TextView tv_time;
        private TextView tv_size;
        private TextView tv_downCount;
    }

    @Override
    public int getCount() {
        size = list.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return list.get(size - 1 - position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.share_center_item, null);
            viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
            viewHolder.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            viewHolder.tv_downCount = (TextView) convertView.findViewById(R.id.tv_downCount);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FileInformation fileInformation = list.get(size - 1 - position);
        viewHolder.img_head.setImageBitmap(fileInformation.getBitmap_type());
        viewHolder.tv_fileName.setText(fileInformation.getFileName());
        viewHolder.tv_time.setText(fileInformation.getDownTimeString());
        viewHolder.tv_downCount.setText("下载次数：" + fileInformation.getDownloadCount() + "");
        viewHolder.tv_size.setText("文件大小：" + fileInformation.getFileSize() + "M");
        return convertView;
    }
}
