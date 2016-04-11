package com.exercise.xinlangweibo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RefreshListView extends ListView {
	private Context context;
	private LayoutInflater layoutInflater;
	private View headView;

	private int width;
	private int height;
	private int widthParam;
	private int heightParam;

	public RefreshListView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	private void init() {
		layoutInflater = LayoutInflater.from(context);
		headView = layoutInflater.inflate(R.layout.layout_listrefresh_head,
				null);
		// 在父布局申明出控件详细信息（默认是填充的布局）
		notifyParentLayout(headView);
		// 设置控件尺寸
		setViewMeasure();

		this.addHeaderView(headView);
	}

	/**
	 * 设置控件尺寸
	 */
	private void setViewMeasure() {
		headView.setPadding(headView.getPaddingLeft(), -height,
				headView.getPaddingRight(), headView.getPaddingBottom());
		// 不使用此方法不知为什么也可以
		headView.invalidate();
	}

	/**
	 * 在父布局申明出控件详细信息
	 * 
	 * @param headView2
	 */
	private void notifyParentLayout(View view) {
		// 获取view布局参数
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		// 如果布局参数为空则实例化
		if (layoutParams == null) {
			Log.i(Constants.TAG, "layoutParams=" + layoutParams);
			layoutParams = new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		width = ViewGroup.getChildMeasureSpec(0, 0, layoutParams.width);
		heightParam = layoutParams.height;
		if (heightParam != -1 || heightParam != -2) {
			height = MeasureSpec.makeMeasureSpec(heightParam,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		Log.i(Constants.TAG, "width=" + widthParam + ",height=" + heightParam);
	}

}
