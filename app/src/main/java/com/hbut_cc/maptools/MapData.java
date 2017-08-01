package com.hbut_cc.maptools;

import com.hbut_cc.towerdefense.R;

public class MapData
{

	/**
	 * ��ȡ���Է��÷�������λ�ã���Ϊ0��ʱ������ܷţ�1�����ܷ��÷�����;<br>
	 * 11~19�����λ�÷��õ��ǵ�һ�ַ�������<br>
	 * 21~29�����λ�÷��õ��ǵڶ��ַ�������<br>
	 * 31~35�����λ�÷��õ��ǵ����ַ�������36~39�����λ�÷��õ��ǵ���������ķ�������<br>
	 * 41~45�����λ�÷��õ��ǵ����ַ�������46~49�����λ�÷��õ��ǵ���������ķ�������
	 * 
	 * @param mapId
	 *            ��ͼ��R�ļ��е�id
	 * @return
	 */
	public int[][] getTowerPosition(int mapId)
	{
		int[][] towerPosition = null;

		if (mapId == R.drawable.map1)
		{
			towerPosition = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 1, 1, 1, 0 },
					{ 0, 1, 0, 0, 1, 0, 1, 0 }, { 1, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 1, 0, 0, 0, 0, 0, 1 }, { 0, 1, 0, 0, 1, 1, 1, 0 },
					{ 0, 1, 0, 0, 1, 0, 1, 0 }, { 0, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 0, 0, 0, 1, 1 } };
		}
		else if (mapId == R.drawable.map2)
		{
			towerPosition = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 1, 1, 1, 0, 0, 0, 0 }, { 0, 1, 0, 1, 1, 1, 0, 0 },
					{ 0, 1, 0, 1, 0, 1, 0, 0 }, { 0, 1, 0, 1, 0, 1, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 0, 0 }, { 0, 1, 0, 1, 1, 0, 0, 0 },
					{ 1, 1, 1, 1, 0, 1, 1, 1 }, { 1, 0, 0, 0, 0, 1, 0, 1 },
					{ 1, 0, 0, 0, 0, 1, 0, 0 }, { 1, 1, 1, 1, 1, 1, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0 } };
		}
		else if (mapId == R.drawable.map3)
		{
			towerPosition = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 1, 0, 0, 1 },
					{ 0, 1, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 1, 1, 1, 0, 0 },
					{ 0, 0, 1, 1, 0, 0, 0, 0 }, { 0, 0, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 1, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 1, 1, 0 },
					{ 0, 0, 1, 1, 1, 1, 1, 0 }, { 1, 1, 1, 0, 0, 0, 0, 0 },
					{ 1, 0, 0, 0, 0, 0, 0, 0 } };
		}
		else if (mapId == R.drawable.map4)
		{
			towerPosition = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 1, 1, 1, 0, 1, 0 },
					{ 0, 1, 1, 0, 1, 0, 1, 0 }, { 1, 1, 1, 0, 1, 1, 1, 1 },
					{ 1, 0, 0, 0, 0, 0, 0, 0 }, { 1, 1, 1, 1, 1, 1, 1, 0 },
					{ 1, 0, 0, 0, 0, 0, 1, 0 }, { 0, 1, 1, 1, 0, 1, 1, 0 },
					{ 0, 1, 0, 1, 0, 0, 1, 0 }, { 1, 1, 1, 1, 1, 1, 1, 0 },
					{ 1, 0, 0, 0, 0, 0, 0, 0 } };
		}
		else if (mapId == R.drawable.map5)
		{
			towerPosition = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 0, 0, 0, 0, 1, 1, 0 }, { 0, 0, 1, 1, 1, 0, 1, 0 },
					{ 0, 0, 1, 0, 1, 0, 1, 0 }, { 0, 0, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 0, 0, 0 },
					{ 0, 1, 1, 1, 1, 1, 1, 0 }, { 0, 1, 0, 1, 1, 0, 1, 1 },
					{ 0, 1, 1, 1, 1, 1, 1, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0 },
					{ 0, 0, 1, 1, 0, 0, 0, 0 } };
		}
		else
		{
			towerPosition = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1, 0, 1, 0 },
					{ 0, 1, 0, 0, 0, 0, 1, 0 }, { 0, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 1, 0, 1, 0, 0, 0, 0 }, { 0, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 1, 1, 1, 1, 1, 1, 0 },
					{ 0, 1, 1, 0, 0, 0, 0, 1 }, { 0, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 0, 0, 0, 0, 0, 0, 0 } };
		}

		return towerPosition;
	}

	/**
	 * ��ȡ·�ߵĻ�Ԫ��
	 * 
	 * @param mapId
	 *            ��ͼ��R�ļ��е�id
	 * @param frameW
	 * @param frameH
	 * @return
	 */
	public static float[][] getRoute(int mapId, float frameW, float frameH)
	{
		float deviation = (float) (0.23 * frameH);
		float[][] routeXY = null;

		if (mapId == R.drawable.map1)
		{
			/*-0.268*/
			routeXY = new float[][] {
					{ 1 * frameW, (float) (-0.200 * frameH) },
					{ 1 * frameW, 4 * frameH - deviation },
					{ 4 * frameW, 4 * frameH - deviation },
					{ 4 * frameW, 2 * frameH - deviation },
					{ 6 * frameW, 2 * frameH - deviation },
					{ 6 * frameW, 6 * frameH - deviation },
					{ 1 * frameW, 6 * frameH - deviation },
					{ 1 * frameW, 10 * frameH - deviation },
					{ 4 * frameW, 10 * frameH - deviation },
					{ 4 * frameW, 8 * frameH - deviation },
					{ 6 * frameW, 8 * frameH - deviation },
					{ 6 * frameW, 13 * frameH - deviation },
					{ 6 * frameW, 14 * frameH } };
		}
		else if (mapId == R.drawable.map2)
		{
			/*-0.268*/
			routeXY = new float[][] {
					{ 1 * frameW, (float) (-0.200 * frameH) },
					{ 1 * frameW, 5 * frameH - deviation },
					{ 5 * frameW, 5 * frameH - deviation },
					{ 5 * frameW, 2 * frameH - deviation },
					{ 3 * frameW, 2 * frameH - deviation },
					{ 3 * frameW, 7 * frameH - deviation },
					{ 0 * frameW, 7 * frameH - deviation },
					{ 0 * frameW, 10 * frameH - deviation },
					{ 5 * frameW, 10 * frameH - deviation },
					{ 5 * frameW, 7 * frameH - deviation },
					{ 9 * frameW, 7 * frameH - deviation } };
		}
		else if (mapId == R.drawable.map3)
		{
			/*-0.268*/
			routeXY = new float[][] {
					{ 1 * frameW, (float) (-0.200 * frameH) },
					{ 1 * frameW, 2 * frameH - deviation },
					{ 4 * frameW, 2 * frameH - deviation },
					{ 4 * frameW, 4 * frameH - deviation },
					{ 2 * frameW, 4 * frameH - deviation },
					{ 2 * frameW, 6 * frameH - deviation },
					{ 6 * frameW, 6 * frameH - deviation },
					{ 6 * frameW, 9 * frameH - deviation },
					{ 2 * frameW, 9 * frameH - deviation },
					{ 2 * frameW, 10 * frameH - deviation },
					{ -2 * frameW, 10 * frameH - deviation } };
		}
		else if (mapId == R.drawable.map4)
		{
			/*-0.268*/
			routeXY = new float[][] {
					{ 6 * frameW, (float) (-0.200 * frameH) },
					{ 6 * frameW, 4 * frameH - deviation },
					{ 4 * frameW, 4 * frameH - deviation },
					{ 4 * frameW, 2 * frameH - deviation },
					{ 2 * frameW, 2 * frameH - deviation },
					{ 2 * frameW, 4 * frameH - deviation },
					{ 0 * frameW, 4 * frameH - deviation },
					{ 0 * frameW, 6 * frameH - deviation },
					{ 6 * frameW, 6 * frameH - deviation },
					{ 6 * frameW, 10 * frameH - deviation },
					{ 3 * frameW, 10 * frameH - deviation },
					{ 3 * frameW, 8 * frameH - deviation },
					{ 1 * frameW, 8 * frameH - deviation },
					{ 1 * frameW, 10 * frameH - deviation },
					{ -2 * frameW, 10 * frameH - deviation } };
		}
		else if (mapId == R.drawable.map5)
		{
			/*-0.268*/
			routeXY = new float[][] {
					{ 6 * frameW, (float) (-0.200 * frameH) },
					{ 6 * frameW, 4 * frameH - deviation },
					{ 2 * frameW, 4 * frameH - deviation },
					{ 2 * frameW, 2 * frameH - deviation },
					{ 4 * frameW, 2 * frameH - deviation },
					{ 4 * frameW, 7 * frameH - deviation },
					{ 6 * frameW, 7 * frameH - deviation },
					{ 6 * frameW, 9 * frameH - deviation },
					{ 1 * frameW, 9 * frameH - deviation },
					{ 1 * frameW, 7 * frameH - deviation },
					{ 3 * frameW, 7 * frameH - deviation },
					{ 3 * frameW, 13 * frameH - deviation } };
		}
		else
		{
			/*-0.268*/
			routeXY = new float[][] {
					{ 6 * frameW, (float) (-0.200 * frameH) },
					{ 6 * frameW, 4 * frameH - deviation },
					{ 1 * frameW, 4 * frameH - deviation },
					{ 1 * frameW, 6 * frameH - deviation },
					{ 6 * frameW, 6 * frameH - deviation },
					{ 6 * frameW, 8 * frameH - deviation },
					{ 1 * frameW, 8 * frameH - deviation },
					{ 1 * frameW, 10 * frameH - deviation },
					{ 9 * frameW, 10 * frameH - deviation } };
		}
		return routeXY;
	}

	/**
	 * ��ù������ܵ�λ�õĺ�����
	 * 
	 * @param mapId
	 *            ��ͼͼƬ��R�ļ��е�id
	 * @return ���������frameW���õ�����
	 */
	public int getMonsterEscapeX(int mapId)
	{
		if (mapId == R.drawable.map1)
		{
			return 6;
		}
		else if (mapId == R.drawable.map2)
		{
			return 8;
		}
		else if (mapId == R.drawable.map3)
		{
			return -1;
		}
		else if (mapId == R.drawable.map4)
		{
			return -1;
		}
		else if (mapId == R.drawable.map5)
		{
			return 3;
		}
		else
		{
			return 8;
		}
	}

	/**
	 * ��ù������ܵ�λ�õ�������
	 * 
	 * @param mapId
	 *            ��ͼͼƬ��R�ļ��е�id
	 * @return ���������frameH���õ�����
	 */
	public int getMonsterEscapeY(int mapId)
	{
		if (mapId == R.drawable.map1)
		{
			return 12;
		}
		else if (mapId == R.drawable.map2)
		{
			return 7;
		}
		else if (mapId == R.drawable.map3)
		{
			return 10;
		}
		else if (mapId == R.drawable.map4)
		{
			return 10;
		}
		else if (mapId == R.drawable.map5)
		{
			return 12;
		}
		else
		{
			return 10;
		}
	}

}
