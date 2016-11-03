package com.qjm3662.cloud_u_pan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.qjm3662.cloud_u_pan.FileManager.FileManagerAdapter;
import com.qjm3662.cloud_u_pan.R;

import java.util.List;

/**
 * Created by qjm3662 on 2016/11/1 0001.
 */

public class DCIMGridViewAdapter extends BaseAdapter{
    private List<String> img_list;
    private LayoutInflater inflater;


    // 配置图片加载及显示选项（还有一些其他的配置，查阅doc文档吧）
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.img_share)// 在ImageView加载过程中显示图片
            .showImageForEmptyUri(R.color.img_share) // image连接地址为空时
            .showImageOnFail(R.color.img_share) // image加载失败
            .cacheInMemory(true) // 加载图片时会在内存中加载缓存
            .imageScaleType(ImageScaleType.EXACTLY)
            .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
            .cacheOnDisk(true) // 加载图片时会在磁盘中加载缓存
            .displayer(new SimpleBitmapDisplayer()) // 设置用户加载图片task(这里是圆角图片显示)
            .build();

    class ViewHolder{
        ImageView imgView;
    }

    public DCIMGridViewAdapter(Context context, List<String> img_list) {
        this.img_list = img_list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<String> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<String> img_list) {
        this.img_list = img_list;
    }

    @Override
    public int getCount() {
        return img_list.size();
    }

    @Override
    public Object getItem(int position) {
        return img_list.get(position);
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
            convertView = inflater.inflate(R.layout.grid_item, null);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.img_grid_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage("file://" + img_list.get(position), viewHolder.imgView, options, new FileManagerAdapter.AnimateFirstDisplayListener());
        return convertView;
    }
}
