package com.qjm3662.cloud_u_pan.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Tool.DensityUtil;


/**
 * Created by qjm3662 on 2016/6/30 0030.
 */
public class MyDialog extends Dialog {
    private Context context;
    private String content;
    private String title;
    private String hint;
    public EditText dialog_et;
    public TextView tv_title;
    public TextView tv_exit;
    private ClickListenerInterface clickListenerInterface;

    private int flag = 0;

    public MyDialog(Context context, String content, String title, String hint) {
        super(context);
        this.context = context;
        this.content = content;
        this.title = title;
        this.hint = hint;
    }

    public MyDialog(Context context, int themeResId, String content, String title, String hint, int flag) {
        super(context, themeResId);
        this.context = context;
        this.content = content;
        this.title = title;
        this.hint = hint;
        this.flag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public EditText getDialog_et() {
        return dialog_et;
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_dialog, null);
        setContentView(view);

        dialog_et = (EditText) view.findViewById(R.id.dialog_et);
        tv_title = (TextView) view.findViewById(R.id.dialog_title);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        Button btn_yes = (Button) view.findViewById(R.id.dialog_yes);
        Button btn_no = (Button) view.findViewById(R.id.dialog_no);


        if(flag == 1){
            dialog_et.setVisibility(View.INVISIBLE);
            btn_yes.setText("公开");
            btn_no.setText("私密");
        }else{
            tv_exit.setVisibility(View.INVISIBLE);
        }
        dialog_et.setText(content);
        dialog_et.setHint(hint);
        tv_title.setText(title);


        btn_yes.setOnClickListener(new clickListener());
        btn_no.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = DensityUtil.dip2px(getContext(), 245); // 高度设置为屏幕的0.6
        lp.height = DensityUtil.dip2px(getContext(), 126);
        dialogWindow.setAttributes(lp);
    }

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.dialog_yes:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.dialog_no:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }
}
