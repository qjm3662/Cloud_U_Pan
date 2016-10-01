package com.qjm3662.cloud_u_pan.Tool;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;

/**
 * Created by tanshunwang on 2016/10/1 0001.
 */

public class DialogUtils {
    public static void showDialog(Context context, Dialog dialog, View.OnClickListener listener){
        Log.d("TAG", "showShareDialog");
        View view = LayoutInflater.from(context).inflate(R.layout.more_dialog, null);
        // 设置style 控制默认dialog带来的边距问题
        dialog.setContentView(view);
        dialog.show();

        ImageView img_upload_record = (ImageView) view.findViewById(R.id.img_upload_record);
        ImageView img_download_record = (ImageView) view.findViewById(R.id.img_download_record);
        ImageView img_share_center = (ImageView) view.findViewById(R.id.img_share_center);
        EasyButton btn_cancel = (EasyButton) view.findViewById(R.id.btn_cancel);

        img_download_record.setOnClickListener(listener);
        img_share_center.setOnClickListener(listener);
        img_upload_record.setOnClickListener(listener);
        btn_cancel.setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DensityUtil.dip2px(context, (float) 250.3);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }
}
