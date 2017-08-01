package com.hbut_cc.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hbut_cc.towerdefense.R;

public class ToastUtils
{

	/**
	 * 显示自定义Toast，显示时间为Toast.LENGTH_SHORT
	 * @param context 上下文本
	 * @param toastString 要显示的语句
	 */
	@SuppressLint("InflateParams")
	public static void showToast(Context context,String toastString)
	{
		LayoutInflater lInflater = LayoutInflater.from(context);
		View toastRoot = lInflater.inflate(R.layout.toast, null);
		TextView tv_toast = (TextView) toastRoot.findViewById(R.id.tv_toast);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/hanyixue.ttf");
		tv_toast.setTypeface(face);
		tv_toast.setText(toastString);
		
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}
	
}
