package com.qjm3662.cloud_u_pan.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.easybar.EasyBar;
import com.example.easybar.ImageCircleButton;
import com.example.easybar.OnImageCircleButtonClickedListener;
import com.example.easybar.RoundRectButton;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.FileManager.FileManager;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.DialogUtils;
import com.qjm3662.cloud_u_pan.Tool.NetworkUtils;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.WifiDirect.TransMain;


public class MainActivity extends BaseActivity implements OnImageCircleButtonClickedListener {

    private RoundRectButton btn_upload;
    private RoundRectButton btn_download;
    private ImageCircleButton btn_wifi;
    private ImageCircleButton btn_more;
    private ImageCircleButton btn_my;
    public static final int FILE_SELECT_CODE = 1;
    public static final String FILE_SELECT = "file select";
    public static final int requestCode_selectFile = 6;
    public static final int resultCode = 9;
    public static final String PATH = "path";
    private Context context = null;

    private Dialog dialog = null;
    private static final int REQUEST_CODE_GALLERY = 253;
    public static String FILE_PATH_TO_BE_UPLOAD = "the file is going to be upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
    }


    private void initView() {
        btn_upload = (RoundRectButton) findViewById(R.id.btn_upload);
        btn_download = (RoundRectButton) findViewById(R.id.btn_download);
        btn_wifi = (ImageCircleButton) findViewById(R.id.btn_wifi);
        btn_more = (ImageCircleButton) findViewById(R.id.btn_more);
        btn_my = (ImageCircleButton) findViewById(R.id.btn_my);
        ((EasyBar)findViewById(R.id.easyBar)).setLeftIconVisibility(false);

        btn_upload.setOnImageCircleButtonClickedListener(this);
        btn_download.setOnImageCircleButtonClickedListener(this);
        btn_wifi.setOnImageCircleButtonClickedListener(this);
        btn_more.setOnImageCircleButtonClickedListener(this);
        btn_my.setOnImageCircleButtonClickedListener(this);
    }


    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
        switch (v.getId()) {
            case R.id.btn_upload:
                NetworkUtils.doAfterJudgeNetworkState(MainActivity.this, new NetworkUtils.NeworkCallBack() {
                    @Override
                    public void connected() {
                        Intent intent = new Intent(context, FileManager.class);
                        intent.putExtra(FILE_SELECT, FILE_SELECT_CODE);
                        startActivityForResult(intent, requestCode_selectFile);
                        App.startAnim(MainActivity.this);
                    }
                });
                break;
            case R.id.btn_download:
                startActivity(new Intent(this, DownloadUi.class));
                App.startAnim(MainActivity.this);
                break;
            case R.id.btn_wifi:
                startActivity(new Intent(this, TransMain.class));
                App.startAnim(MainActivity.this);
                break;
            case R.id.btn_more:
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_cancel:
                                dialog.cancel();
                                System.out.println("dialog cancel!!");
                                break;
                            case R.id.img_upload_record:
                                startActivity(new Intent(MainActivity.this, RecordTabsActivity.class));
                                App.startAnim(MainActivity.this);
                                dialog.cancel();
                                break;
                            case R.id.img_download_record:
                                startActivity(new Intent(MainActivity.this, BaiduSearchActivity.class));
                                App.startAnim(MainActivity.this);

                                dialog.cancel();
                                break;
                            case R.id.img_share_center:
                                NetworkUtils.doAfterJudgeNetworkState(MainActivity.this, new NetworkUtils.NeworkCallBack() {
                                    @Override
                                    public void connected() {
                                        startActivity(new Intent(MainActivity.this, ShareCenter.class));
                                        App.startAnim(MainActivity.this);
                                        dialog.cancel();
                                    }
                                });
                                break;
                        }
                    }
                };
                if(dialog == null){
                    dialog = new Dialog(this, R.style.common_dialog);
                    DialogUtils.showSelectDialog(this, dialog, listener);
                    return;
                }
                if(!dialog.isShowing()) {
                    dialog = new Dialog(this, R.style.common_dialog);
                    DialogUtils.showSelectDialog(this, dialog, listener);
                }
                break;
            case R.id.btn_my:
                startActivity(new Intent(this, UserMain.class));
                App.startAnim(MainActivity.this);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case requestCode_selectFile:
                if (data != null) {
                    System.out.println(data.getStringExtra(PATH));
                    Intent intent = new Intent(this, UploadUi.class);
                    intent.putExtra(FILE_PATH_TO_BE_UPLOAD, data.getStringExtra(PATH));
                    this.startActivity(intent);
                    App.startAnim(MainActivity.this);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
