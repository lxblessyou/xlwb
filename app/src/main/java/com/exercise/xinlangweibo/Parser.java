package com.exercise.xinlangweibo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

public class Parser {

	public static void weiboJsonParse(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray statusesJsonArray = jsonObject.optJSONArray("statuses");
			Log.i(Constants.TAG, "statusesJsonArray");
			for (int i = 0; i < statusesJsonArray.length(); i++) {
				JSONObject jsonObject2 = statusesJsonArray.getJSONObject(i);
				statusParse(jsonObject2);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void statusParse(JSONObject jsonObject2) {
		Status status = new Status();
		status.text = jsonObject2.optString("text");
		status.source = jsonObject2.optString("source");
		status.created_at = jsonObject2.optString("created_at");
		status.thumbnail_pic = jsonObject2.optString("thumbnail_pic");
		status.idstr = jsonObject2.optString("idstr");
		status.reposts_count = jsonObject2.optInt("reposts_count");
		status.comments_count = jsonObject2.optInt("comments_count");
		status.attitudes_count = jsonObject2.optInt("attitudes_count");
		JSONObject jsonObject = jsonObject2.optJSONObject("user");
		userParse(status, jsonObject);

		Constants.statusesList.add(status);
		// Log.i(Constants.TAG, "status.text--" + status.text);
		// Log.i(Constants.TAG, "获取微博数据完成");
	}

	public static void userParse(Status status, JSONObject jsonObject) {
		status.user = new User();
		status.user.screen_name = jsonObject.optString("screen_name");
		status.user.profile_image_url = jsonObject
				.optString("profile_image_url");
		status.user.avatar_hd = jsonObject.optString("avatar_hd");
		status.user.avatar_large = jsonObject.optString("avatar_large");
	}

	/**
	 * xml解析类
	 * 
	 * @param source
	 * @return
	 */
	public static String xmlParse(String source) {
		try {
			Reader reader = new StringReader(source);
			XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory
					.newInstance();
			XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
			xmlPullParser.setInput(reader);
			xmlPullParser.next();
			int event = xmlPullParser.getEventType();
			// Log.i(Constants.TAG, "进入XML解析");
			while (event != XmlPullParser.END_DOCUMENT) {
				// Log.i(Constants.TAG, source);
				source = xmlPullParser.nextText();
				// Log.i(Constants.TAG, source);
				xmlPullParser.next();
				event = xmlPullParser.getEventType();
			}
		} catch (XmlPullParserException e) {
			Log.i(Constants.TAG, e + "");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			Log.i(Constants.TAG, e + "");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(Constants.TAG, e + "");
			e.printStackTrace();
		}
		return source;
	}

}
