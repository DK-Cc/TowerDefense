package com.hbut_cc.maptools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hbut_cc.utils.BitmapUtils;

/**
 * 
 * 地图类
 * 
 * @author c.c
 *
 */
public class GameMap extends MapData
{
	/* 放背景地图图片 */
	private Bitmap gameMap;

	/*private Context context;*/

	private int mapId;

	/* 屏幕的宽和高 */
	private int screenW, screenH;

	/* 游戏中每格的宽和高 */
	private float frameW, frameH;

	/* 初始出现点 */
	private float beginningPointX, beginningPointY;

	/**
	 * 
	 * @param context
	 * @param mapId
	 *            本地地图在R文件中id,例如R.drawable.map1
	 */
	public GameMap(Context context, int mapId)
	{
		/*this.context = context;*/
		this.mapId = mapId;

		gameMap = BitmapFactory.decodeResource(context.getResources(), mapId);
	}

	public Bitmap getGameMap()
	{
		return gameMap;
	}

	public int getScreenW()
	{
		return screenW;
	}

	public int getScreenH()
	{
		return screenH;
	}

	/**
	 * 设置屏幕的宽和高、设置frameW和frameH，并改变地图的大小
	 * 
	 * @param screenW
	 *            屏幕的宽
	 * @param screenH
	 *            屏幕的高
	 */
	public void setScreenWHAndFramenWH(int screenW, int screenH,float frameW,float frameH)
	{
		this.screenW = screenW;
		this.screenH = screenH;

		this.frameW = frameW;
		this.frameH = frameH;

		float[] beginingPoint = getRoute(mapId, frameW, frameH)[0];
		beginningPointX = beginingPoint[0];
		beginningPointY = beginingPoint[1];

		gameMap = BitmapUtils.changeBitmapSize(gameMap, screenW, screenH);
	}

	public float getBeginningPointX()
	{
		return beginningPointX;
	}

	public float getBeginningPointY()
	{
		return beginningPointY;
	}

	public float getFrameW()
	{
		return frameW;
	}

	public float getFrameH()
	{
		return frameH;
	}

	public int getMapId()
	{
		return mapId;
	}

}
