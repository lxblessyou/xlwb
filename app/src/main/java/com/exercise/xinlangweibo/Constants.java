package com.exercise.xinlangweibo;

import java.util.ArrayList;
import java.util.List;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Status;

public class Constants {
    public static Oauth2AccessToken token = new Oauth2AccessToken();
    public static List<Status> statusesList = new ArrayList<Status>();
    public static final String TAG = "Constants";
    public static final String TOKENNAME = "token";

    public static final String APP_KEY = "326077836";  // 应用的APP_KEY
    public static final String REDIRECT_URL = "http://www.sina.com";// 应用的回调页
    public static final String SCOPE = // 应用申请的 高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
}
