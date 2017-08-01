package com.hbut_cc.maptools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hbut_cc.utils.BitmapUtils;

/**
 * 
 * ��ͼ��
 * 
 * @author c.c
 *
 */
public class GameMap extends MapData
{
	/* �ű�����ͼͼƬ */
	private Bitmap gameMap;

	/*private Context context;*/

	private int mapId;

	/* ��Ļ�Ŀ�͸� */
	private int screenW, screenH;

	/* ��Ϸ��ÿ��Ŀ�͸� */
	private float frameW, frameH;

	/* ��ʼ���ֵ� */
	private float beginningPointX, beginningPointY;

	/**
	 * 
	 * @param context
	 * @param mapId
	 *            ���ص�ͼ��R�ļ���id,����R.drawable.map1
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
	 * ������Ļ�Ŀ�͸ߡ�����frameW��frameH�����ı��ͼ�Ĵ�С
	 * 
	 * @param screenW
	 *            ��Ļ�Ŀ�
	 * @param screenH
	 *            ��Ļ�ĸ�
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
