package com.hbut_cc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPreferences
{
	public static final String TAG = "TowerDefense";
	
	private Context context;
	/** ���ڱ��� �Ƿ��ܼ�����Ϸ */
	private static final String NAME_RESUME = "resumes";
	/** ���ڱ��� ѡ��ҳ���״̬ */
	private static final String NAME_OPTIONS = "options";

	/** ��ΪSTART_GAMEʱ����û�д������Ϸ����ʼ�µ���Ϸ */
	public static final int START_GAME = 0;
	public static final int RESUME = 1;

	/** ѡ��ҳ�����һ����ʾ״̬����ΪfalseʱΪ����ʾ */
	public static final boolean NEXTLEVEL_OFF = false;
	public static final boolean NEXTLEVEL_ON = true;

	public MySharedPreferences(Context context)
	{
		this.context = context;
	}

	public void saveOption(boolean nextLevel)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				NAME_OPTIONS, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();

		editor.putBoolean("nextLevel", nextLevel);

		editor.commit();
		
	}

	public boolean getOption()
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				NAME_OPTIONS, Context.MODE_PRIVATE);

		return sharedPreferences.getBoolean("nextLevel", false);
		
	}

	public void saveGameState(int state)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				NAME_RESUME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();

		editor.putInt("state", state);

		editor.commit();
	}

	public int getGameState()
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				NAME_RESUME, Context.MODE_PRIVATE);

		return sharedPreferences.getInt("state", START_GAME);
	}

}
