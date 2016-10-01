package com.qjm3662.cloud_u_pan.FileManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by qjm3662 on 2016/9/28 0028.
 */

public class FileManagerAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Bitmap directory, file_img;
    private File file;
    //存储文件名称
    private ArrayList<String> names = null;
    //存储文件路径
    private ArrayList<String> paths = null;

    public FileManagerAdapter(Context context, ArrayList<String> paths, ArrayList<String> names) {
        this.paths = paths;
        this.names = names;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        directory = BitmapFactory.decodeResource(context.getResources(), R.drawable.directy);
        file_img = BitmapFactory.decodeResource(context.getResources(), R.drawable.file_small);
        //缩小图片
        directory = small(directory,0.16f);
        file_img = small(file_img,0.1f);
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.local_file_recording_item, null);
            holder.tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            holder.img_icon = (ImageView) view.findViewById(R.id.img_head);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        File f = new File(paths.get(i));
        if (names.get(i).equals("@1")){
            holder.tv_fileName.setText("/");
            holder.img_icon.setImageBitmap(directory);
        }
        else if (names.get(i).equals("@2")){
            holder.tv_fileName.setText("..");
            holder.img_icon.setImageBitmap(directory);
        }
        else{
            holder.tv_fileName.setText(f.getName());
            if (f.isDirectory()){
                holder.img_icon.setImageBitmap(directory);
            }
            else if (f.isFile()){
                file = new File(paths.get(i));
//                holder.img_icon.setImageBitmap(file_img);
                FileUtils.setImgHead(holder.img_icon, FileUtils.getMIMEType(file), paths.get(i));
            }
            else{
                System.out.println(f.getName());
            }
        }
        return view;
    }

    class ViewHolder{
        private ImageView img_icon;
        private TextView tv_fileName;
    }


    /**
     * 缩小图片
     * @param map
     * @param num
     * @return
     */
    private Bitmap small(Bitmap map,float num){
        Matrix matrix = new Matrix();
        matrix.postScale(num, num);
        return Bitmap.createBitmap(map,0,0,map.getWidth(),map.getHeight(),matrix,true);
    }
}
