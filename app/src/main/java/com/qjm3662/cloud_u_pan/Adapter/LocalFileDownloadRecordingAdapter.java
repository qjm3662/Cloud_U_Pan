package com.qjm3662.cloud_u_pan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.R;

/**
 * Created by qjm3662 on 2016/9/27 0027.
 */

public class LocalFileDownloadRecordingAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    public LocalFileDownloadRecordingAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder{
        private ImageView img_head;
        private TextView tv_fileName;
        private TextView tv_time;
    }

    @Override
    public int getCount() {
        return App.Public_List_Local_File_Download.size();
    }

    @Override
    public Object getItem(int i) {
        return App.Public_List_Local_File_Download.get(App.Public_List_Local_File_Download.size() - 1 - i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LocalFileDownloadRecordingAdapter.ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new LocalFileDownloadRecordingAdapter.ViewHolder();
            view = inflater.inflate(R.layout.local_file_recording_item, null);
            viewHolder.img_head = (ImageView) view.findViewById(R.id.img_head);
            viewHolder.tv_fileName = (TextView) view.findViewById(R.id.tv_fileName);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        }else{
            viewHolder = (LocalFileDownloadRecordingAdapter.ViewHolder) view.getTag();
        }

        LocalFile localFile = App.Public_List_Local_File_Download.get(App.Public_List_Local_File_Download.size() - 1 - i);
        if(localFile.isFlag_is_DateNode()){

        }else{
            setImgHead(viewHolder.img_head, localFile.getType(), localFile.getPath());
            viewHolder.tv_fileName.setText(localFile.getName());
            viewHolder.tv_time.setText(localFile.getDownTimeString());
        }

        return view;
    }

    private void setImgHead(ImageView img_head, String type, String path) {
        switch (type){
            case LocalFile.DOC:
                img_head.setImageResource(R.drawable.file_small);
                break;
            case LocalFile.HTML:
                img_head.setImageResource(R.drawable.html);
                break;
            case LocalFile.MUSIC:
                img_head.setImageResource(R.drawable.music);
                break;
            case LocalFile.PHOTO:
                Bitmap bm = BitmapFactory.decodeFile(path);
                img_head.setImageBitmap(bm);
                break;
            case LocalFile.PPT_PDF:
                img_head.setImageResource(R.drawable.pptpdf);
                break;
            case LocalFile.VIDEO:
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
                img_head.setImageBitmap(bitmap);
                break;
            case LocalFile.ZIP:
                img_head.setImageResource(R.drawable.zip);
                break;
            default:
                img_head.setImageResource(R.drawable.file_small);
        }
    }
}