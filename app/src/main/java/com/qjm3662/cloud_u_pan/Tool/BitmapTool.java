package com.qjm3662.cloud_u_pan.Tool;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.qjm3662.cloud_u_pan.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by qjm3662 on 2016/11/1 0001.
 */

public class BitmapTool {
    /**
     * 重新设置图片的大小
     *
     * @param b
     * @param x
     * @param y
     * @return
     */
    public static String resize_bitmap(Bitmap b, float x, float y) {
        //大小压缩
        int w = b.getWidth();
        int h = b.getHeight();
        float sx = (float) x / w;//要强制转换，不转换我的在这总是死掉。
        float sy = (float) y / h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
                h, matrix, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        resizeBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 5;
            System.out.println("options : " + options);
            resizeBmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return saveByteArrayAsFile(baos.toByteArray());
    }

    /**
     * byte数组存为文件，并返回路径
     * @param bytes 输出流.toByteArray()
     * @return 保存文件的路径
     */
    public static String saveByteArrayAsFile(byte[] bytes){
        String path = App.currentSavePath + "/header/" + System.currentTimeMillis() + ".png";
        File file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("ByteToFile : ", file.length() + "");
        return path;
    }
}
