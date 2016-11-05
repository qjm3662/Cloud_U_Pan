package com.qjm3662.cloud_u_pan.Tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.qjm3662.cloud_u_pan.Widget.EasySweetAlertDialog;

/**
 * Created by qjm3662 on 2016/9/29 0029.
 */

public class NetworkUtils {

//    public static final int NETWORK_TYPE_UNKNOWN
////            (不知道网络类型)
//    public static final int NETWORK_TYPE_GPRS
////            (2.5G）移动和联通
//                    public static final int NETWORK_TYPE_EDGE
////                    (2.75G)2.5G到3G的过渡    移动和联通
//    public static final int NETWORK_TYPE_UMTS
////            (3G)联通
//    public static final int NETWORK_TYPE_CDMA
////            (2G 电信)
//    public static final int NETWORK_TYPE_EVDO_0
////            ( 3G )电信
//    public static final int NETWORK_TYPE_EVDO_A
////            (3.5G) 属于3G过渡
//    public static final int NETWORK_TYPE_1xRTT
////            ( 2G )
//    public static final int NETWORK_TYPE_HSDPA
////            (3.5G )
//    public static final int NETWORK_TYPE_HSUPA
////            ( 3.5G )
//    public static final int NETWORK_TYPE_HSPA
////            ( 3G )联通
//    public static final int NETWORK_TYPE_IDEN
////            (2G )
//    public static final int NETWORK_TYPE_EVDO_B
////    3G-3.5G
//    public static final int NETWORK_TYPE_LTE
////            (4G)
//    public static final int NETWORK_TYPE_EHRPD
////    3G(3G到4G的升级产物)
//
//    public static final int NETWORK_TYPE_HSPAP
////            ( 3G )
    /**
     原文链接：http://www.jianshu.com/p/10ed9ae02775
     著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。
     */

    public static final int NETWORK_FLAG_NOT_CONNECT = -1;
    public static final int NETWORK_FLAG_WIFI = 1;
    public static final int NETWORK_FLAG_MOBILE = 2;
    public static final int NETWORK_FLAG_VPN = 3;

    /**
     * 判断网络连接状态
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context){
        if(context != null){
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络信息
            NetworkInfo info = cm.getActiveNetworkInfo();
            //判断网络信息是否为空
            if(info != null){
                return info.isAvailable();
            }
        }
        return false;
    }


    public static boolean judgeIsNetworkConnected(Context context){
        return !(getNetworkFlag(context) == -1);
    }

    public static void doAfterJudgeNetworkState(Context context, NeworkCallBack callBack){
        if(judgeIsNetworkConnected(context)){
            callBack.connected();
        }else{
            EasySweetAlertDialog.ShowTip(context, "tip", "请检查您的网络连接");
        }
    }

    public interface NeworkCallBack{
        void connected();
    }
    /**
     * 获取网络标记
     * @param context
     * @return  -1（没有网络连接）     1(WIFI连接)    2（数据流量连接）     3(VPN)
     */
    public static int getNetworkFlag(Context context){
        if(!isNetworkConnected(context)){
            return -1;
        }else{
            switch (getTheConnectType(context)){
                case ConnectivityManager.TYPE_MOBILE:
                    return 2;
                case ConnectivityManager.TYPE_BLUETOOTH:
                    break;
                case ConnectivityManager.TYPE_DUMMY:
                    break;
                case ConnectivityManager.TYPE_ETHERNET:
                    break;
                case ConnectivityManager.TYPE_MOBILE_DUN:
                    return 2;
                case ConnectivityManager.TYPE_VPN:
                    return 3;
                case ConnectivityManager.TYPE_WIFI:
                    return 1;
                default:
                    return -1;
            }
        }
        return -1;
    }

    /**
     * 获取网络类型
     * @param context
     * @return
     */
    public static int getTheConnectType(Context context){
        if(context != null){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info != null){
                return info.getType();
            }
        }
        return -1;
    }



}
