package com.qjm3662.cloud_u_pan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qjm3662.cloud_u_pan.FileManager.FileManagerAdapter;
import com.qjm3662.cloud_u_pan.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by qjm3662 on 2016/11/2 0002.
 */

public class DCIMDirectryAdapter extends BaseAdapter{

    private HashMap<String, List<String>> mDCIM;
    private String[] names;
    private LayoutInflater inflater;

    public DCIMDirectryAdapter(Context context, HashMap<String, List<String>> mDCIM) {
        this.mDCIM = mDCIM;
        names = mDCIM.keySet().toArray(new String[]{});
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public HashMap<String, List<String>> getmDCIM() {
        return mDCIM;
    }

    public void setmDCIM(HashMap<String, List<String>> mDCIM) {
        this.mDCIM = mDCIM;
        this.names = mDCIM.keySet().toArray(new String[]{});
    }

    class ViewHolder{
        ImageView img_per;
        TextView tv_directory_name;
    }
    @Override
    public int getCount() {
        return mDCIM.size();
    }

    @Override
    public Object getItem(int position) {
        return mDCIM.get(position);
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
            convertView = inflater.inflate(R.layout.grid_item_directy, null);
            viewHolder.img_per = (ImageView) convertView.findViewById(R.id.img_grid_item);
            viewHolder.tv_directory_name = (TextView) convertView.findViewById(R.id.tv_directory_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_directory_name.setText(names[position]);
        ImageLoader.getInstance().displayImage("file://" + mDCIM.get(names[position]).get(0), viewHolder.img_per, DCIMGridViewAdapter.options, new FileManagerAdapter.AnimateFirstDisplayListener());
        return convertView;
    }
}
