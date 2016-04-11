package com.exercise.xinlangweibo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Status;

/**
 * statuses微博列表适配器
 * 
 * @author admin
 * 
 */
class StatusesAdapter extends BaseAdapter {
	private StatusesActivity statusesActivity;
	private DownloadStatusIcon downloadStatusIcon = null;
	private Handler handler;
	private ViewHold viewHold = null;

	public StatusesAdapter(StatusesActivity statusesActivity, Handler handler) {
		super();
		this.statusesActivity = statusesActivity;
		this.handler = handler;
		downloadStatusIcon = new DownloadStatusIcon();
	}

	@Override
	public int getCount() {
		return Constants.statusesList.size();
	}

	@Override
	public Object getItem(int position) {
		return Constants.statusesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Status status = (Status) getItem(position);
		LayoutInflater layoutInflater = statusesActivity.getLayoutInflater();
		View view = null;
		if (convertView != null) {
			view = convertView;
			viewHold = (ViewHold) view.getTag();
		} else {
			view = layoutInflater.inflate(R.layout.layout_statuses_item, null);
			viewHold = new ViewHold();

			viewHold.img_profile_image = (ImageView) view
					.findViewById(R.id.img_profile_image_url);
			viewHold.tv_screen_name = (TextView) view
					.findViewById(R.id.tv_screen_name);
			viewHold.tv_source = (TextView) view.findViewById(R.id.tv_source);
			viewHold.tv_created_at = (TextView) view
					.findViewById(R.id.tv_created_at);
			viewHold.tv_text = (TextView) view.findViewById(R.id.tv_text);
			viewHold.tv_reposts_count = (TextView) view
					.findViewById(R.id.tv_reposts_count);
			viewHold.tv_comments_count = (TextView) view
					.findViewById(R.id.tv_comments_count);
			viewHold.tv_attitudes_count = (TextView) view
					.findViewById(R.id.tv_attitudes_count);

			view.setTag(viewHold);
		}
		// 设置微博文本内容
		viewHold.tv_text.setText(status.text);
		// 设置发博者用户名
		viewHold.tv_screen_name.setText(status.user.screen_name);
		// 设置转发数
		setReposts_count(status);
		// 设置评论数
		setComments_count(status);
		// 设置表态数
		setAttitudes_count(status);
		// 设置微博创建时间
		setCreatedAt(status);
		// 设置微博发送端（来源）
		setSource(status);
		// 设置头像
		setProfile_image(status);

		return view;
	}

	/**
	 * 设置表态数
	 * 
	 * @param view
	 * @param status
	 */
	private void setAttitudes_count(Status status) {
		String html = "<img src='toolbar_icon_like_meizu'/><font>  "
				+ status.attitudes_count + "</font>";
		CharSequence charSequence = Html.fromHtml(html, new MyImageGetter(
				statusesActivity), null);
		viewHold.tv_attitudes_count.setText(charSequence);
	}

	/**
	 * 设置评论数
	 * 
	 * @param view
	 * @param status
	 */
	private void setComments_count(Status status) {
		String html = "<img src='toolbar_comment_icon_nor_meizu'/><font>  "
				+ status.comments_count + "</font>";
		CharSequence charSequence = Html.fromHtml(html, new MyImageGetter(
				statusesActivity), null);
		viewHold.tv_comments_count.setText(charSequence);
	}

	/**
	 * 设置转发数
	 * 
	 * @param view
	 * @param status
	 */
	private void setReposts_count(Status status) {
		String html = "<img src='toolbar_forward_icon_nor_meizu'/><font>  "
				+ status.reposts_count + "</font>";
		CharSequence charSequence = Html.fromHtml(html, new MyImageGetter(
				statusesActivity), null);
		viewHold.tv_reposts_count.setText(charSequence);
	}

	/**
	 * 设置头像
	 * 
	 * @param view
	 * @param status
	 * @param position
	 */
	public void setProfile_image(Status status) {
		String profile_image_url = status.user.avatar_large;
		Bitmap bitmap = StatusesActivity.profile_image_url_cache
				.get(profile_image_url);
		if (bitmap == null) {
			downloadStatusIcon.download(profile_image_url, handler);
		} else {
			viewHold.img_profile_image.setImageBitmap(bitmap);
		}
	}

	/**
	 * 设置微博发送端（来源）
	 * 
	 * @param view
	 * @param status
	 */
	public void setSource(Status status) {
		// Log.i(Constants.TAG, "设置source");
		String source = Parser.xmlParse(status.source);
		viewHold.tv_source.setText(source);
	}

	/**
	 * 设置微博创建时间
	 * 
	 * @param view
	 * @param status
	 */
	public void setCreatedAt(Status status) {
		String created_at = status.created_at;
		String timeString = null;
		try {
			Date date = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"EEE MMM dd hh:mm:ss Z yyyy", Locale.ENGLISH);
			date = simpleDateFormat.parse(created_at);

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			timeString = format.format(date);
		} catch (ParseException e) {
			Log.i(Constants.TAG, "异常" + e);
			e.printStackTrace();
		}
		viewHold.tv_created_at.setText(timeString);
	}

	class ViewHold {
		TextView tv_text, tv_created_at, tv_source, tv_screen_name,
				tv_reposts_count, tv_comments_count, tv_attitudes_count;
		ImageView img_profile_image;
	}
}
