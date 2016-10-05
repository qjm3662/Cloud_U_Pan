package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.AvatarUtils;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.QRCodeUtil;
import com.qjm3662.cloud_u_pan.Widget.CaptureActivityAnyOrientation;

import static com.qjm3662.cloud_u_pan.R.id.imageView;

public class ZXingAddFriend extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_bar;
    private ImageView img_back;
    private ImageView img_camera;
    private ImageView img_ZXing;
    private TextView tv_name;
    private Bitmap bitmap_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_add_friend);
        initView();
        if(!App.Flag_IsLogin){
            finish();
        }else {
            bitmap_icon = User.getInstance().getBitmap();
            System.out.println(bitmap_icon);
            if(bitmap_icon == null || bitmap_icon.getHeight() == 0 || bitmap_icon.getWidth() == 0){
                User.getInstance().getAvatar();
                AvatarUtils.AvatarCallBack callBack = new AvatarUtils.AvatarCallBack() {
                    @Override
                    public void callback(Bitmap bitmap) {
                        ShowZXing(User.getInstance().getName(), bitmap);
                    }

                    @Override
                    public void callBack_2(User u, Bitmap bitmap, int position) {

                    }
                };
                AvatarUtils.getBitmapByUrl(User.getInstance().getAvatar(), callBack);
            }else{
//                img_ZXing.setImageBitmap(bitmap_icon);
                ShowZXing(User.getInstance().getName(), bitmap_icon);
            }
        }
    }

    /**
     * 显示二维码
     * @param text
     * @param bitmap_icon
     */
    private void ShowZXing(String text, Bitmap bitmap_icon){
        String path = FileUtils.getSDPath() + "mdzz.jpg";
        QRCodeUtil.createQRImage(text, 500, 500, bitmap_icon, path);
        Bitmap bitmap1 = BitmapFactory.decodeFile(path);
        img_ZXing.setImageBitmap(bitmap1);
    }

    private void initView() {
        tv_bar = (TextView) findViewById(R.id.bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_ZXing = (ImageView) findViewById(R.id.img_ZXing);
        tv_name = (TextView) findViewById(R.id.tv_name);

        tv_bar.setText("扫一扫加关注");
        tv_name.setText("我的二维码");
        img_back.setOnClickListener(this);
        img_camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_camera:
                startCamera();
                break;
        }
    }

    private void startCamera() {
        //扫描操作
        IntentIntegrator integrator = new IntentIntegrator(ZXingAddFriend.this);
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        System.out.println("mdzz");
        if (scanResult != null) {
            System.out.println("woc, 怎么还有");
            String result = scanResult.getContents();
            NetWorkOperator.getOtherUserInfoByName(this, result.trim(), false);
        }else{
            System.out.println("没有信息");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
