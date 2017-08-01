package com.hbut_cc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPreferences
{
	public static final String TAG = "TowerDefense";
	
	private Context context;
	/** 用于保存 是否能继续游戏 */
	private static final String NAME_RESUME = "resumes";
	/** 用于保存 选择页面的状态 */
	private static final String NAME_OPTIONS = "options";

	/** 当为START_GAME时代表没有储存的游戏，开始新的游戏 */
	public static final int START_GAME = 0;
	public static final int RESUME = 1;

	/** 选择页面的下一波提示状态，当为false时为无提示 */
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
