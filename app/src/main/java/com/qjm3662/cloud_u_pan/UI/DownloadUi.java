package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.example.easybar.EasyBar;
import com.example.easybar.OnImageCircleButtonClickedListener;
import com.example.easybar.RoundRectButton;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasyButton;
import com.qjm3662.cloud_u_pan.Widget.EditTextWithDivLine;

public class DownloadUi extends BaseActivity {

    private EditTextWithDivLine editTextWithDivLine;
    private RoundRectButton btn_sure;
    private boolean flag_is_input_finish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_ui);
        initView();
    }

    FileInformation.callBack callBack = new FileInformation.callBack() {
        @Override
        public void call() {
            Intent intent = new Intent(DownloadUi.this, DownloadUi2.class);
            intent.putExtra("code", editTextWithDivLine.getPwdText());
            startActivity(intent);
            editTextWithDivLine.clearText();
            flag_is_input_finish = false;
        }
    };

    private void initView() {
        EasyBarUtils.justSetTitleAndBack("提取文件", this, 1);
        editTextWithDivLine = (EditTextWithDivLine) findViewById(R.id.et_code);
        btn_sure = (RoundRectButton) findViewById(R.id.btn_sure);
        btn_sure.setOnImageCircleButtonClickedListener(new OnImageCircleButtonClickedListener() {
            @Override
            public void onClick(View view) {
                if(flag_is_input_finish){
                    NetWorkOperator.GetFileInformation(DownloadUi.this, editTextWithDivLine.getPwdText(), App.fileInformation, callBack);
                }
            }
        });

        editTextWithDivLine.initStyle(R.drawable.code_input_type, 6, 1, R.color.gray, R.color.blue, 24);
        editTextWithDivLine.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        editTextWithDivLine.setOnTextFinishListener(new EditTextWithDivLine.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                flag_is_input_finish = true;
            }
        });
    }


}
