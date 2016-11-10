package com.qjm3662.cloud_u_pan.WifiDirect;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.R;

import java.util.List;

/**
 * Created by qjm3662 on 2016/10/25 0025.
 */

public class PeersListAdapter extends BaseAdapter{
    private List<WifiP2pDevice> peers;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private qjm_WifiP2pManager manager;

    public void setPeers(List<WifiP2pDevice> peers) {
        this.peers = peers;
    }

    public PeersListAdapter(Context context, List<WifiP2pDevice> peers, qjm_WifiP2pManager manager) {
        this.peers = peers;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.manager = manager;
    }

    class ViewHolder{
        private TextView tv_name;
//        private TextView tv_information;
        private ImageView img_disconnected;
    }

    @Override
    public int getCount() {
        System.out.println("getSize");
        return peers.size();
    }

    @Override
    public Object getItem(int position) {
        return peers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_deceive, null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.deceive_name);
//            viewHolder.tv_information = (TextView) convertView.findViewById(R.id.deceive_details);
            viewHolder.img_disconnected = (ImageView) convertView.findViewById(R.id.img_disconnected);
            viewHolder.img_disconnected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.DisConnect();
                    viewHolder.img_disconnected.setVisibility(View.INVISIBLE);
                }
            });
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        WifiP2pDevice device = peers.get(position);
        viewHolder.tv_name.setText(device.deviceName);
        viewHolder.img_disconnected.setVisibility(View.INVISIBLE);
        if(device.status == WifiP2pDevice.CONNECTED){
            viewHolder.img_disconnected.setVisibility(View.VISIBLE);
        }
//        viewHolder.tv_information.setText(getDeceiveStatus(device.status));
        return convertView;
    }

    private static String getDeceiveStatus(int status) {
        Log.d("STATE", "Peer status : " + status);
        switch (status){
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";
        }
    }
}
