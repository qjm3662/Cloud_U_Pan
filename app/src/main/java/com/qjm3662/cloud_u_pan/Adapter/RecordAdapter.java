package com.qjm3662.cloud_u_pan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.FileManager.FileManagerAdapter;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;

import java.util.List;

/**
 * Created by qjm3662 on 2016/9/26 0026.
 */

public class RecordAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<LocalFile> list;
    public RecordAdapter(Context context, List<LocalFile> list) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    class ViewHolder {
        private ImageView img_head;
        private TextView tv_fileName;
        private TextView tv_time;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(list.size() - 1 - i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.local_file_recording_item, null);
            viewHolder.img_head = (ImageView) view.findViewById(R.id.img_head);
            viewHolder.tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        LocalFile localFile = list.get(list.size() - 1 - i);
        if (localFile.getType().equals(LocalFile.PHOTO) || localFile.getType().equals(LocalFile.VIDEO)) {
            ImageLoader.getInstance().displayImage("file://" + localFile.getPath(), viewHolder.img_head, FileManagerAdapter.options, new FileManagerAdapter.AnimateFirstDisplayListener());
        } else {
            viewHolder.img_head.setImageBitmap(FileUtils.getImgHead_not_down(localFile.getType()));
        }
        viewHolder.tv_fileName.setText(localFile.getName());
        viewHolder.tv_time.setText(localFile.getDownTimeString());
        return view;
    }
}
