package com.qjm3662.cloud_u_pan.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.easybar.EasyBar;
import com.qjm3662.cloud_u_pan.Adapter.OthersShareAdapter;
import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.Data.FileInformation;
import com.qjm3662.cloud_u_pan.Data.User;
import com.qjm3662.cloud_u_pan.EasyBarUtils;
import com.qjm3662.cloud_u_pan.NetWorkOperator;
import com.qjm3662.cloud_u_pan.R;
import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

import java.util.List;

public class OthersMain extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EasyBar easyBar;
    private ListView listView;
    private TextView tv_empty_view;
    private OthersShareAdapter adapter;
    private View header;
    private LayoutInflater inflater;
    private ImageView img_avatar;
    private TextView tv_name;
    private ImageView img_add;
    private TextView tv_concren_info;
    private int flag_where;             //0-other   1-self      3-shareCenter
    private int position;
    private User user;
    private boolean isConcern = false;
    private List<FileInformation> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_main);
        Intent intent = getIntent();
        flag_where = intent.getIntExtra("WHERE", 0);
        position = intent.getIntExtra("POSITION", 0);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_others_share);
        EasyBarUtils.justSetTitleAndBack("个人主页", this, 1);
        tv_empty_view = (TextView) findViewById(R.id.list_empty_view);
        tv_empty_view.setVisibility(View.INVISIBLE);
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        header = inflater.inflate(R.layout.others_main_listview_header, null);
        img_avatar = (ImageView) header.findViewById(R.id.img_avatar);
        tv_name = (TextView) header.findViewById(R.id.tv_name);
        img_add = (ImageView) header.findViewById(R.id.img_edit_nickname);
        tv_concren_info = (TextView) header.findViewById(R.id.tv_concern_info);
        if (flag_where == 1) {
            user = User.getInstance();
            img_add.setVisibility(View.INVISIBLE);
            tv_concren_info.setVisibility(View.INVISIBLE);
            adapter = new OthersShareAdapter(this, user.getShares_list());
        } else if (flag_where == 3) {
            user = App.user_temp;
            isConcern = user.isRelative();
            if(isConcern){
                img_add.setImageResource(R.drawable.guanzhu);
                tv_concren_info.setText("已关注");
            }
            adapter = new OthersShareAdapter(this, user.getShares_list());
        } else {
            user = App.Public_Following_Info.get(position);
            System.out.println("here, size" + App.Public_Following_Info.size());
            adapter = new OthersShareAdapter(this, user.getShares_list());
        }
        list = user.getShares_list();

        listView.setAdapter(adapter);
        listView.addHeaderView(header);
        listView.setOnItemClickListener(this);
        img_avatar.setOnClickListener(this);
        img_add.setOnClickListener(this);

        tv_name.setText(user.getNickname());
        img_avatar.setImageBitmap(user.getBitmap());
    }

    public class ViewHolder{
        private ImageView img_add;
        private TextView tv_concern_info;

        public void set(boolean isConcern) {
            if(isConcern){
                this.img_add.setImageResource(R.drawable.guanzhu);
                this.tv_concern_info.setText("已关注");
            }else{
                this.img_add.setImageResource(R.drawable.jiahao);
                this.tv_concern_info.setText("关注");
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_avatar:

                break;
            case R.id.img_edit_nickname:
                if (App.Flag_IsLogin) {
                    ViewHolder viewHolder = new ViewHolder();
                    viewHolder.img_add = img_add;
                    viewHolder.tv_concern_info = tv_concren_info;
                    if(isConcern){
                        NetWorkOperator.UnFollowSB(this, user.getUsername(), viewHolder);
                        isConcern = false;
                        user.setRelative(false);
                    }else{
                        NetWorkOperator.FollowSB(this, user.getUsername(), viewHolder);
                        isConcern = true;
                        user.setRelative(true);
                    }
                } else {
                    EasySweetAlertDialog.ShowTip(this, "tip", "请先登录！");
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            final String code = list.get(list.size() - position).getIdentifyCode();
            FileInformation.callBack callBack = new FileInformation.callBack() {
                @Override
                public void call() {
                    Intent intent = new Intent(OthersMain.this, DownloadUi2.class);
                    intent.putExtra("code", code);
                    intent.putExtra("WHERE", 1);
                    startActivity(intent);
                    App.startAnim(OthersMain.this);
                }
            };
            NetWorkOperator.GetFileInformation(this, code, App.fileInformation, callBack);
        }catch (Exception e){
            System.out.println("ERROR");
            System.out.println(list.size());
            System.out.println(position);
        }
    }
}
