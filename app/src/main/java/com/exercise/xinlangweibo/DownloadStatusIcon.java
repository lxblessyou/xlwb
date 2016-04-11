package com.exercise.xinlangweibo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

public class DownloadStatusIcon {
	public void download(final String profile_image_url, final Handler handler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(profile_image_url);
					InputStream inputStream = url.openStream();
					BufferedInputStream bufferedInputStream = new BufferedInputStream(
							inputStream);
					Bitmap bitmap = BitmapFactory
							.decodeStream(bufferedInputStream);
					writeToCache(profile_image_url, bitmap);
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected void writeToCache(String profile_image_url, Bitmap bitmap) {
		if (readCache(profile_image_url) == null) {
			StatusesActivity.profile_image_url_cache.put(profile_image_url,
					bitmap);
		}
	}

	private Bitmap readCache(String profile_image_url) {
		Bitmap bitmap = StatusesActivity.profile_image_url_cache
				.get(profile_image_url);
		return bitmap;
	}
}
