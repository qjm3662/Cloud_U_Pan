package com.qjm3662.cloud_u_pan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.R;

import java.util.List;

/**
 * Created by tanshunwang on 2016/10/3 0003.
 */

public class FollowingsAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<User> list;

    public FollowingsAdapter(Context context, List<User> list) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    class ViewHolder{
        private ImageView img_avatar;
        private TextView tv_name;
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
            convertView = inflater.inflate(R.layout.followings_item, null);
            viewHolder.img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User user = list.get(list.size() - 1 - position);
        viewHolder.img_avatar.setImageBitmap(user.getBitmap());
        viewHolder.tv_name.setText(user.getNickname());
        return convertView;
    }
}
