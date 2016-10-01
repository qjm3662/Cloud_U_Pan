package com.qjm3662.cloud_u_pan.Tool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

/**
 * Created by qjm3662 on 2016/10/1 0001.
 */

public class TencentOperator {

    /**
     * 分享图文消息到QQ
     * @param context
     * @param tencent
     * @param listener
     * @param title
     * @param summary
     * @param targetURl
     * @param ImageUrl
     * @param Appname
     */
    public static void shareToQQ(Context context, Tencent tencent, IUiListener listener, String title, String summary, String targetURl, String ImageUrl, String Appname){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "测试应用 222222");
        tencent.shareToQQ((Activity) context, params,listener);
    }
}
