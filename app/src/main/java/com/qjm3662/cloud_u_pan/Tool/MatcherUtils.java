package com.qjm3662.cloud_u_pan.Tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tanshunwang on 2016/10/4 0004.
 */

public class MatcherUtils {
    public static boolean isMobilePhone(String phone) {//
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
