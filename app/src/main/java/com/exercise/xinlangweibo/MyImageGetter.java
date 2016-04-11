package com.exercise.xinlangweibo;

import java.lang.reflect.Field;

import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

public class MyImageGetter implements ImageGetter {

	private StatusesActivity statusesActivity;

	public MyImageGetter(StatusesActivity statusesActivity) {
		super();
		this.statusesActivity = statusesActivity;
	}

	@Override
	public Drawable getDrawable(String source) {
		Drawable drawable = null;
		Field field;
		try {
			field = R.drawable.class.getField(source);
			int id = Integer.parseInt(field.get(null).toString());
			drawable = statusesActivity.getResources().getDrawable(id);
			drawable.setBounds(0, 0, 50, 50);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drawable;
	}
}
