package com.qjm3662.cloud_u_pan.Tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qjm3662.cloud_u_pan.Data.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tanshunwang on 2016/10/3 0003.
 */

public class AvatarUtils {

    public interface AvatarCallBack{
        void callback(Bitmap bitmap);
        void callBack_2(User u, Bitmap bitmap, int position);
    }

    public static void getBitmapByUrl(final String url, final AvatarCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = returnBitmap(url);
                callBack.callback(bitmap);
            }
        }).start();
    }

    public static void getBitmapByUrl(final String url, final AvatarCallBack callBack, final User user, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = returnBitmap(url);
                callBack.callBack_2(user, bitmap, position);
            }
        }).start();
    }
    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        System.out.println("begin getBitmap");
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            assert fileUrl != null;
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            return null;
        }
        System.out.println("return Bitmap end in!!!!");
        return bitmap;
    }
}
