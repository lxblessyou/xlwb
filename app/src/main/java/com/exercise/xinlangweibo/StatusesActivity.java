package com.exercise.xinlangweibo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;

public class StatusesActivity extends Activity {
	private RefreshListView listView;
	public static LruCache<String, Bitmap> profile_image_url_cache = null;
	private Spinner usernameSpinner;
	private Spinner radarSpinner;

	public static BaseAdapter adapterStatuses;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adapterStatuses.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statuses);
		initStatusesUserIconCache();
		initStatusesData();
		initView();
	}

	private void initStatusesUserIconCache() {
		long maxMemory = Runtime.getRuntime().maxMemory();
		int maxSize = (int) (maxMemory / 4);
		profile_image_url_cache = new LruCache<String, Bitmap>(maxSize) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getByteCount();
			}

		};
	}

	/**
	 * 初始化view
	 */
	public void initView() {
		listView = (RefreshListView) findViewById(R.id.lv_statuses);
		usernameSpinner = (Spinner) findViewById(R.id.spinner_username);
		initUsernameSpinner();
		radarSpinner = (Spinner) findViewById(R.id.spinner_radar);
	}

	private void initUsernameSpinner() {
		int uid = Integer.parseInt(Constants.token.getUid());
		UsersAPI usersAPI = new UsersAPI(this, Constants.APP_KEY, Constants.token);
	}

	/**
	 * 初始化微博列表数据
	 */
	private void initStatusesData() {
		Log.i(Constants.TAG, Constants.token + "--statusesActivity");
		StatusesAPI statusesAPI = new StatusesAPI(this, Constants.APP_KEY,
				Constants.token);
		statusesAPI.friendsTimeline(0, 0, 20, 1, false, 0, false,
				new MyRequstListener());
	}

	/**
	 * statuses微博数据监听器
	 */
	class MyRequstListener implements RequestListener {
		@Override
		public void onWeiboException(WeiboException arg0) {
			Toast.makeText(StatusesActivity.this, "授权失败、网络或服务器异常",
					Toast.LENGTH_LONG).show();
			Log.i(Constants.TAG, "获取微博数据异常");
		}

		@Override
		public void onComplete(String json) {
			Log.i(Constants.TAG, "获取微博数据成功");
			if (json.startsWith("{\"statuses\":")) {
				Parser.weiboJsonParse(json);
				adapterStatuses = new StatusesAdapter(StatusesActivity.this,
						handler);
				if (listView != null) {
					listView.setAdapter(adapterStatuses);
				}
			}
		}
	}

}
