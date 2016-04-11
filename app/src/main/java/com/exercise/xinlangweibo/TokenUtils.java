package com.exercise.xinlangweibo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class TokenUtils {
	/**
	 * 从sharedpreference中读取令牌
	 * 
	 * @param mainActivity
	 * @return
	 */
	public static Oauth2AccessToken readToken(MainActivity mainActivity) {
		Oauth2AccessToken token = null;
		SharedPreferences sharedPreferences = mainActivity
				.getSharedPreferences(Constants.TOKENNAME,
						mainActivity.MODE_PRIVATE);
		String uid = sharedPreferences.getString("uid", "");
		String access_token = sharedPreferences.getString("access_token", "");
		long expires_in = sharedPreferences.getLong("expires_in", 0);
		token = new Oauth2AccessToken(access_token, expires_in + "");
		token.setUid(uid);
		Log.i(Constants.TAG, "令牌读取完成");
		return token;
	}

	/**
	 * 把令牌写入本地sharedpreference
	 * 
	 * @param mainActivity
	 * @param bundle
	 */
	public static void writeToken(MainActivity mainActivity, Bundle bundle) {
		String access_token = bundle.getString("access_token");
		String uid = bundle.getString("uid");
		long expires_in = bundle.getLong("expires_in");

		Constants.token = new Oauth2AccessToken(access_token, expires_in + "");
		SharedPreferences sharedPreferences = mainActivity
				.getSharedPreferences(Constants.TOKENNAME,
						mainActivity.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putString("access_token", access_token);
		edit.putString("uid", uid);
		edit.putLong("expires_in", expires_in);
		edit.commit();
		Log.i(Constants.TAG, "令牌写入完成");
	}

}
