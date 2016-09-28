package com.qjm3662.cloud_u_pan.UI;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qjm3662.cloud_u_pan.App;
import com.qjm3662.cloud_u_pan.R;

/**
 * Created by qjm3662 on 2016/9/25 0025.
 */
public class WiFiPeerListAdapter extends BaseAdapter{

    private LayoutInflater inflater = null;


    private class ViewHolder{
        private TextView tv_device;
        private TextView deviceAddress;
        private TextView primary_type;
        private TextView secondary_type;
        private TextView wps;
        private TextView grpcapab;
        private TextView devcapab;
        private TextView status;
        private TextView wfdInfo;
        private TextView enabled;
        private TextView WFDDeviceInfo;
        private TextView WFDCtrlPort;
        private TextView WFDMaxThroughput;
        private TextView WFDExtendedCapability;
        private TextView deviceIP;
        private TextView goIface;
        private TextView oobRoleIndication;
    }
    public WiFiPeerListAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return App.peers.size();
    }

    @Override
    public Object getItem(int i) {
        return App.peers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.wifideceive_item, null);
            viewHolder.tv_device = (TextView) view.findViewById(R.id.tv_devise);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        WifiP2pDevice wifiP2pDevice = App.peers.get(i);
        viewHolder.tv_device.setText(wifiP2pDevice.deviceName);
        return view;
    }

}
