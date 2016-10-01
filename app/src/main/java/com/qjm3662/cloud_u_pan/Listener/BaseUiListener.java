package com.qjm3662.cloud_u_pan.Listener;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by qjm3662 on 2016/9/30 0030.
 */

public class BaseUiListener implements IUiListener {

    protected void doComplete(JSONObject values) {

    }
    @Override
    public void onComplete(Object o) {

    }

    @Override

    public void onError(UiError e) {

//        showResult("onError:", "code:" + e.errorCode + ", msg:"
//
//                + e.errorMessage + ", detail:" + e.errorDetail);

    }

    @Override

    public void onCancel() {

//        showResult("onCancel", "");

    }

}
