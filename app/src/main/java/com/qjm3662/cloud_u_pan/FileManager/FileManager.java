package com.qjm3662.cloud_u_pan.FileManager;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qjm3662.cloud_u_pan.Data.LocalFile;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.FileUtils;
import com.qjm3662.cloud_u_pan.UI.MainActivity;
import com.qjm3662.cloud_u_pan.UI.UserMain;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

import java.io.File;
import java.util.ArrayList;

public class FileManager extends ListActivity implements View.OnClickListener {
    private static final String ROOT_PATH = "/";
    //存储文件名称
    private ArrayList<String> names = null;
    //存储文件路径
    private ArrayList<String> paths = null;
    private ImageView img_back;
    private TextView tv_bar;

    private int selectPosition = 0;
    private Intent intent;
    private boolean flag_is_select_file;
    private String currentListPath;
    private String parentPath;
    private int where = 0;             // 0-上传文件       1-选择头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        intent = getIntent();
        if (intent.getIntExtra(MainActivity.FILE_SELECT, 0) == MainActivity.FILE_SELECT_CODE) {
            flag_is_select_file = true;
            ShowFileDir(ROOT_PATH, selectPosition);
        }else if(intent.getIntExtra(String.valueOf("SELECT_PHOTO"), 0) == 1){
            ShowPhotoDir(ROOT_PATH, selectPosition);
            where = 1;
        }
        initView();
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_bar = (TextView) findViewById(R.id.bar);

        img_back.setOnClickListener(this);
        tv_bar.setText("选择文件");
    }

    /**
     * 显示所有文件
     * @param path
     * @param selectPosition
     */
    private void ShowFileDir(String path, int selectPosition) {
        names = new ArrayList<String>();
        paths = new ArrayList<String>();

        currentListPath = path;

        File file = new File(path);
        File[] files = file.listFiles();
        parentPath = file.getParent();
        //如果当前目录不是根目录
        if (!ROOT_PATH.equals(path)) {
            //根目录
            names.add("@1");
            paths.add(ROOT_PATH);

            //返回上一级
            names.add("@2");
            paths.add(file.getParent());
        }

        //添加所有文件
        for (File f : files) {
            names.add(f.getName());
            paths.add(f.getPath());
        }
        this.setSelection(selectPosition);
        this.setListAdapter(new FileManagerAdapter(this, paths, names));
    }


    /**
     * 显示图片文件
     * @param path
     * @param selectPosition
     */
    private void ShowPhotoDir(String path, int selectPosition){
        names = new ArrayList<String>();
        paths = new ArrayList<String>();

        currentListPath = path;
        File file = new File(path);
        File[] files = file.listFiles();
        parentPath = file.getParent();
        //如果当前目录不是根目录
        if (!ROOT_PATH.equals(path)) {
            //根目录
            names.add("@1");
            paths.add(ROOT_PATH);

            //返回上一级
            names.add("@2");
            paths.add(file.getParent());
        }
        //添加所有文件
        for (File f : files) {
            if(FileUtils.getMIMEType(f).equals(LocalFile.PHOTO)){
                names.add(f.getName());
                paths.add(f.getPath());
            }
        }
        this.setSelection(selectPosition);
        this.setListAdapter(new FileManagerAdapter(this, paths, names));
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String path = paths.get(position);
        File file = new File(path);
        //文件存在并可读
        if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                //显示子目录及其文件
                if(where == 0){
                    ShowFileDir(path, 0);
                }else if(where == 1){
                    ShowPhotoDir(path, 0);
                }
                selectPosition = position;
            } else {
                if (flag_is_select_file) {
                    intent = new Intent();
                    intent.putExtra(MainActivity.PATH, path);
                    this.setResult(MainActivity.resultCode, intent);
                    this.finish();
                }else if(where == 1){
                    intent = new Intent();
                    intent.putExtra(UserMain.PATH, path);
                    this.setResult(UserMain.SELECT_PHTOT_RESULT_CODE, intent);
                    this.finish();
                }else{
                    //处理文件
                    fileHandle(file);
                }
            }
        } else {//没有权限
            EasySweetAlertDialog.ShowTip(this, "没有获得权限");
        }
        super.onListItemClick(l, v, position, id);
    }


    //对文件进行增删改
    private void fileHandle(final File file) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 打开文件
                if (which == 0) {
                    FileUtils.OpenFile(FileManager.this, file.getPath(), file.getName());
                }
                //删除文件
                else {
                    new AlertDialog.Builder(FileManager.this)
                            .setTitle("注意!")
                            .setMessage("确定要删除此文件吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (file.delete()) {
                                        //更新文件列表
                                        ShowFileDir(file.getParent(), selectPosition);
                                        displayToast("删除成功！");
                                    } else {
                                        displayToast("删除失败！");
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        };
        //选择文件时，弹出增删该操作选项对话框
        String[] menu = {"打开文件", "重命名", "删除文件"};
        new AlertDialog.Builder(FileManager.this)
                .setTitle("请选择要进行的操作!")
                .setItems(menu, listener)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


//    //打开文件
//    private void openFile(File file){
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//
//        String type = getMIMEType(file);
//        intent.setDataAndType(Uri.fromFile(file), type);
//        startActivity(intent);
//    }

    private void displayToast(String message) {
        Toast.makeText(FileManager.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        System.out.println("finish\n" + currentListPath + "\n" + parentPath);
        System.out.println(ROOT_PATH);
        if (currentListPath.equals(ROOT_PATH)) {
            finish();
        } else {
            System.out.println("ShowDIR " + parentPath);
            ShowFileDir(parentPath, selectPosition);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
