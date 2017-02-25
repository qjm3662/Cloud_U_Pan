package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.Adapter.DCIMDirectryAdapter;
import com.qjm3662.cloud_u_pan.Adapter.DCIMGridViewAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.Tool.LocalDCIMUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DCIMGirdActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private BaseAdapter adapter_directory;
    private final HashMap<String, List<String>> mGroupMap = new HashMap<String, List<String>>();
    private String[] DCIMset = null;
    private boolean isDirect = true;
    private BaseAdapter adapter_photo;
    private List<String> currentPaths = null;
    private EasyBar easyBar;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 5;
    private File tempFile = new File(FileUtils.getPath(), "aa_temp.jpg");
    private ImageView img_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcimgird);
        initImagePath();
        initView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("item click");
        if(isDirect){
            currentPaths = mGroupMap.get(DCIMset[position]);
            adapter_photo = new DCIMGridViewAdapter(this, currentPaths);
            gridView.setAdapter(adapter_photo);
            isDirect = false;
        }else{
            Uri uri = Uri.parse("file://" + currentPaths.get(position));
            File file = new File(FileUtils.getPath(), "aa.jpg");
            Uri desUri = Uri.fromFile(file);
            UCrop.of(uri, desUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(300, 300)
                    .start(this);
            System.out.println(desUri);
        }
    }


    class MyGetImageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LocalDCIMUtils.SCAN_OK:
                    DCIMset = mGroupMap.keySet().toArray(new String[]{});
                    System.out.println(Arrays.toString(mGroupMap.keySet().toArray()));
                    System.out.println(mGroupMap.toString());
                    ((DCIMDirectryAdapter)adapter_directory).setmDCIM(mGroupMap);
                    adapter_directory.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void initImagePath() {
        LocalDCIMUtils.getImages(this, new MyGetImageHandler(), mGroupMap);
    }

    private void initView() {
        EasyBar easyBar = (EasyBar) findViewById(R.id.easyBar);
        easyBar.setRightIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_gf_camera));
        easyBar.setTitle("选择一张图片");
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick() {
                onBackPressed();
            }

            @Override
            public void onRightIconClick() {
                getCamera();
            }
        });
        gridView = (GridView) findViewById(R.id.my_DCIM_grid);
        adapter_directory = new DCIMDirectryAdapter(this, new HashMap<String, List<String>>());
        gridView.setAdapter(adapter_directory);
        gridView.setOnItemClickListener(this);
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


    @Override
    public void onBackPressed() {
        if(!isDirect){
            gridView.setAdapter(adapter_directory);
            isDirect = true;
        }else{
            super.onBackPressed();
            App.finishAnim(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_REQUEST_TAKEPHOTO){
            if(data != null){
                System.out.println("dd*****");
                return;
            }
            Uri uri = Uri.fromFile(tempFile);
            File file = new File(FileUtils.getPath(), "aa.jpg");
            Uri desUri = Uri.fromFile(file);
            UCrop.of(uri, desUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(300, 300)
                    .start(this);
            System.out.println(desUri);
        }
        if (resultCode == RESULT_OK) {
            //裁切成功
            if (requestCode == UCrop.REQUEST_CROP) {
                Uri croppedFileUri = UCrop.getOutput(data);
                assert croppedFileUri != null;
                String path = croppedFileUri.getPath();
                Intent intent = new Intent();
                intent.putExtra("PATH", path);
                setResult(UserMain.REQUEST_CODE_MY_DCIM, intent);
                finish();
            }
        }
        //裁切失败
        if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "裁切图片失败", Toast.LENGTH_SHORT).show();
        }
    }

}
