package com.qjm3662.cloud_u_pan.Tool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.qjm3662.cloud_u_pan.App;
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
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  targetURl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ImageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, Appname);
        tencent.shareToQQ((Activity) context, params,listener);
    }
}
