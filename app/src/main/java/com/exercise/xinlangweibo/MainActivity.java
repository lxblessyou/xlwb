package com.exercise.xinlangweibo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class MainActivity extends Activity {
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView = (ImageView) findViewById(R.id.img_welcome_android_slogan);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.i(Constants.TAG, "动画结束");
				startAuth();
			}
		});
		imageView.setAnimation(animation);
	}

	/**
	 * 开始认证
	 */
	protected void startAuth() {
		// 检查网络
		boolean isConnect = checkNetwork();
		if (isConnect) {
			Log.i(Constants.TAG, "网络已连接");
			// 判断令牌是否存在和过期
			Constants.token = TokenUtils.readToken(this);
			boolean sessionValid = Constants.token.isSessionValid();
			if (!sessionValid) {
				// 认证
				auth();
			} else {
				startActivity(new Intent(MainActivity.this,
						StatusesActivity.class));
				MainActivity.this.finish();
			}
		} else {
			Log.i(Constants.TAG, "网络未连接");
			setNetwork();
		}
	}

	/**
	 * 检查网络
	 * 
	 * @return
	 */
	private boolean checkNetwork() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null) {
			return true;
		}
		return false;
	}

	/**
	 * 设置网络
	 */
	private void setNetwork() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("网络设置");
		builder.setMessage("请设置网络");
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				MainActivity.this.finish();
			}
		});
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction("android.settings.SETTINGS");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
			}
		});
		builder.create().show();
	}

	private AuthInfo authInfo;
	private SsoHandler ssoHandler;

	/**
	 * 认证
	 */
	private void auth() {
		Log.i(Constants.TAG, "开始认证");
		authInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL,
				Constants.SCOPE);
		ssoHandler = new SsoHandler(this, authInfo);
		ssoHandler.authorize(new MyWeiboAuthListener());
	}

	/**
	 * 认证监听器
	 * 
	 * @author admin
	 * 
	 */
	class MyWeiboAuthListener implements WeiboAuthListener {
		public MyWeiboAuthListener() {
			Log.i(Constants.TAG, "认证监听");
		}

		@Override
		public void onCancel() {
			Log.i(Constants.TAG, "取消认证");
			MainActivity.this.finish();
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Log.i(Constants.TAG, "认证异常");
			Toast.makeText(MainActivity.this, "网络或服务器异常", Toast.LENGTH_LONG)
					.show();
			MainActivity.this.finish();
		}

		@Override
		public void onComplete(Bundle bundle) {
			Log.i(Constants.TAG, "完成认证");
			// 保存令牌
			TokenUtils.writeToken(MainActivity.this, bundle);
			// 认证完成，跳转页面
			startActivity(new Intent(MainActivity.this, StatusesActivity.class));
			MainActivity.this.finish();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (ssoHandler != null) {
			Log.i(Constants.TAG, "通过客户端完成认证后activity回调方法");
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
