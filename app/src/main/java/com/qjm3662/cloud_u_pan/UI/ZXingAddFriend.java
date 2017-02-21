package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easybar.EasyBar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.AvatarUtils;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.QRCodeUtil;
import com.qjm3662.cloud_u_pan.Widget.CaptureActivityAnyOrientation;

import static com.qjm3662.cloud_u_pan.R.id.img_back;

public class ZXingAddFriend extends BaseActivity {

    private EasyBar easyBar;
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
                        ShowZXing(User.getInstance().getUsername(), bitmap);
                    }

                    @Override
                    public void callBack_2(User u, Bitmap bitmap, int position) {

                    }
                };
                AvatarUtils.getBitmapByUrl(User.getInstance().getAvatar(), callBack);
            }else{
//                img_ZXing.setImageBitmap(bitmap_icon);
                ShowZXing(User.getInstance().getUsername(), bitmap_icon);
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
        easyBar = (EasyBar) findViewById(R.id.easyBar);
        easyBar.setTitle("扫一扫加关注");
        easyBar.setRightIcon(BitmapFactory.decodeResource(getResources(), R.drawable.saoyisao));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick() {
                finish();
            }

            @Override
            public void onRightIconClick() {
                startCamera();
            }
        });
        img_ZXing = (ImageView) findViewById(R.id.img_ZXing);
        tv_name = (TextView) findViewById(R.id.tv_name);

        tv_name.setText("我的二维码");
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
        try {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            System.out.println("mdzz");
            if (scanResult != null) {
                System.out.println("woc, 怎么还有");
                String result = scanResult.getContents();
                NetWorkOperator.getOtherUserInfoByUsername(this, result.trim(), false);
            }else{
                System.out.println("没有信息");
            }
        }catch (Exception e){
            System.out.println( "what:" + e.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
