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
	 * 创建地图表;
	 * <p>
	 * mapname："map1"~"map6"<br>
	 * gamemode：0,1<br>
	 * gamedifficulty：0,1,2<br>
	 * monsterWave：0~n<br>
	 * mapid：年月日时分（各占两位,如：1705081545,17年5月8号15时45分）
	 * residuallife：剩余生命数
	 * monsterWave:1,2,3...
	 */
	private static final String CreateTableMap = "create table map(mapid character(20) primary key not null,"
			+ "mapname character(20) not null,gamemode integer no null,"
			+ "gamedifficulty integer not null,monsterWave integer not null,"
			+ "score integer not null,residuallife integer not null,"
			+ "money integer not null)";

	/**
	 * 创建防御塔表;
	 * <p>
	 * coordinateX为防御塔横坐标除以frameW<br>
	 * skill：分两位，第一位是什么技能（毒火冰特，1234）;第二位是技能的等级<br>
	 * towername：tower1,tower2<br>
	 * grade：1,2,3
	 * mapid：年月日时分（各占两位,如：1705081545,17年5月8号15时45分）
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
	 * 构造函数，会创建一个数据库。
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            数据库名字（要以.db结尾）
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
