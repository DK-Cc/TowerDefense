package com.hbut_cc.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

	public static final String DB_NAME = "towerdefense.db";
	public static final int DB_VERSION = 1;
	public static final String DB_TABLE_NAME_MAP = "map";
	public static final String DB_TABLE_NAME_TOWER = "tower";

	/**
	 * ������ͼ��;
	 * <p>
	 * mapname��"map1"~"map6"<br>
	 * gamemode��0,1<br>
	 * gamedifficulty��0,1,2<br>
	 * monsterWave��0~n<br>
	 * mapid��������ʱ�֣���ռ��λ,�磺1705081545,17��5��8��15ʱ45�֣�
	 * residuallife��ʣ��������
	 * monsterWave:1,2,3...
	 */
	private static final String CreateTableMap = "create table map(mapid character(20) primary key not null,"
			+ "mapname character(20) not null,gamemode integer no null,"
			+ "gamedifficulty integer not null,monsterWave integer not null,"
			+ "score integer not null,residuallife integer not null,"
			+ "money integer not null)";

	/**
	 * ������������;
	 * <p>
	 * coordinateXΪ���������������frameW<br>
	 * skill������λ����һλ��ʲô���ܣ�������أ�1234��;�ڶ�λ�Ǽ��ܵĵȼ�<br>
	 * towername��tower1,tower2<br>
	 * grade��1,2,3
	 * mapid��������ʱ�֣���ռ��λ,�磺1705081545,17��5��8��15ʱ45�֣�
	 */
	private static final String CreateTableTower = "create table tower(coordinateX integer not null,"
			+ "coordinateY integer not null,mapid character(20) not null,"
			+ "towername character(20) not null,grade integer not null,"
			+ "skill integer not null,recoveryprice integer not null,"
			+ "primary key(coordinateX,coordinateY,mapid))";

	public DBHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * ���캯�����ᴴ��һ�����ݿ⡣
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param name
	 *            ���ݿ����֣�Ҫ��.db��β��
	 * @param factory
	 * @param version
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CreateTableMap);
		db.execSQL(CreateTableTower);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

}
