package com.qjm3662.cloud_u_pan.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.DialogUtils;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserMain extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_edit_nickname;
    private RoundedImageView img_head;
    private TextView tv_name;
    private TextView tv_following;
    private TextView tv_upload;
    private TextView tv_down_in_wifi;
    private TextView tv_save_path;
    private TextView tv_callback;
    private TextView tv_bar;
    private ImageView img_back;
    private TextView tv_exit;
    private TextView tv_current_save_path;
    private Button btn_switch;
    private BroadcastReceiver receiver;
    public static final String ACTION_GET_USER_INFO_SUCCESS = "get userInfo success";
    private User user;

    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果

    public static final String PATH = "path";
    public static final int SELECT_PHTOT_RESULT_CODE = 6;

    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        initView();
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GET_USER_INFO_SUCCESS);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                user = User.getInstance();
                System.out.println(user.getBitmap());
                img_head.setImageBitmap(user.getBitmap());
                tv_name.setText(user.getUsername());
            }
        };
        registerReceiver(receiver, intentFilter);

    }

    private void initView() {
        img_edit_nickname = (ImageView) findViewById(R.id.img_edit_nickname);
        img_head = (RoundedImageView) findViewById(R.id.img_avatar);
        tv_callback = (TextView) findViewById(R.id.tv_callback);
        tv_down_in_wifi = (TextView) findViewById(R.id.tv_down_in_wifi);
        tv_following = (TextView) findViewById(R.id.tv_following);
        tv_save_path = (TextView) findViewById(R.id.tv_save_path);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_bar = (TextView) findViewById(R.id.bar);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        btn_switch = (Button) findViewById(R.id.my_switch_button);
        tv_current_save_path = (TextView) findViewById(R.id.tv_current_save_path);
        tv_current_save_path.setText(App.currentSavePath);
        tv_bar.setText("关于我的");
        img_back = (ImageView) findViewById(R.id.img_back);

        img_edit_nickname.setOnClickListener(this);
        tv_callback.setOnClickListener(this);
        tv_down_in_wifi.setOnClickListener(this);
        tv_following.setOnClickListener(this);
        tv_save_path.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        btn_switch.setOnClickListener(this);

        img_head.setOnClickListener(this);

        if(App.Flag_IsLogin){
            user = User.getInstance();
            img_head.setImageBitmap(user.getBitmap());
            tv_name.setText(user.getUsername());
            NetWorkOperator.getUserInfo(this, User.getInstance().getName(), 3);
        }
        initSwitch();
    }

    private void initSwitch() {
        if(!App.Down_In_Wifi_Switch_State){
            btn_switch.setBackgroundResource(R.drawable.img_switch);
        }else{
            btn_switch.setBackgroundResource(R.drawable.img_switch_choose);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_edit_nickname:
                DialogUtils.ShowDialog(this, user.getUsername());
                break;
            case R.id.img_avatar:
                System.out.println("clicasdf asfa sk");
//                getAlbum();
                getCamera();
                break;
            case R.id.tv_callback:

                break;
            case R.id.tv_down_in_wifi:

                break;
            case R.id.tv_following:
                NetWorkOperator.GetFollowingInformation(this, App.Public_Following_Info);
                break;
            case R.id.tv_save_path:

                break;
            case R.id.tv_upload:
                Intent intent = new Intent(UserMain.this, OthersMain.class);
                intent.putExtra("WHERE", 1);
                startActivity(intent);
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_exit:
                User.deleteUser();
                App.Flag_IsLogin = false;
                //删除SharedPreferences的记录
                App.deleteUserInfo(this);
                this.startActivity(new Intent(this, Login.class));
                finish();
                break;
            case R.id.my_switch_button:
                SharedPreferences sp = this.getSharedPreferences("SWITCH", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if(!App.Down_In_Wifi_Switch_State){
                    btn_switch.setBackgroundResource(R.drawable.img_switch_choose);
                    App.Down_In_Wifi_Switch_State = true;
                }else{
                    btn_switch.setBackgroundResource(R.drawable.img_switch);
                    App.Down_In_Wifi_Switch_State = false;
                }
                editor.putBoolean("SWITCH_WIFI", App.Down_In_Wifi_Switch_State);
                editor.apply();
                break;
        }
    }



    // 使用系统当前日期加以调整作为照片的名称

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private void getCamera() {
        Intent cameraintent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(tempFile));
        startActivityForResult(cameraintent,
                PHOTO_REQUEST_TAKEPHOTO);
    }

    //从相册获取图片并裁剪
    private void getAlbum() {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 调用系统裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        System.out.println("22================");
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                startPhotoZoom(Uri.fromFile(tempFile));
                break;
            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null) {
                    System.out.println("11================");
                    startPhotoZoom(data.getData());
                } else {
                    System.out.println("================");
                }
                break;
            case PHOTO_REQUEST_CUT:// 返回的结果
                if (data != null) {
                    //获取文件的绝对路径
                    Bitmap bm = data.getParcelableExtra("data");
                    if (bm != null) {
                        System.out.println("bm is valued");
                        System.out.println(data);
                        //img.setImageBitmap(bm);
                    }
                    try {
//                        NWO_2.UPFILE(getContext(), NWO_2.saveFile(bm, "dog.jpg"), "dog.jpg", true);
                        NetWorkOperator.ModifyUserAvatar(this, FileUtils.saveFile(bm, "header.jpg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
