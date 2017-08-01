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
		 * ȷ��activity��ֱ��ʾ
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/**
		 * handler ����MenuActivity�����ӳ� INTRO_LENGTH �ر�firstActivity
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
