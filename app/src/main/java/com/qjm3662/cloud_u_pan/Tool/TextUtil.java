package com.qjm3662.cloud_u_pan.Tool;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by qjm3662 on 2016/9/25 0025.
 */
public class TextUtil {

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static void copy(String content, Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("Label", content));
    }
    /**
     * 实现粘贴功能
     * add by wangqianzhou
     * @param context
     * @return
     */
    public static String paste(Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
