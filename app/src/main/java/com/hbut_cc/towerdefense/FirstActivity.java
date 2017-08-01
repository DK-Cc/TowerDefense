package com.hbut_cc.towerdefense;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

public class FirstActivity extends Activity
{

	private final int INTRO_LENGTH = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);

		/**
		 * 确保activity垂直显示
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/**
		 * handler 开启MenuActivity，并延迟 INTRO_LENGTH 关闭firstActivity
		 */
		new Handler().postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				Intent intent = new Intent(FirstActivity.this,
						MenuActivity.class);
				startActivity(intent);
				finish();

				overridePendingTransition(R.anim.mainfadein,
						R.anim.introfadeout);
			}

		}, INTRO_LENGTH);

	}

}
