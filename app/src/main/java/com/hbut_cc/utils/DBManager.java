package com.hbut_cc.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 用于数据库操作
 * 
 * @author c.c
 *
 */
public class DBManager
{

	/**
	 * 最大储存量
	 */
	public static final int MAX_SAVE = 20;
	
	private DBHelper helper;
	private SQLiteDatabase db;

	private ArrayList<DBMap> dbMaps;
	private ArrayList<ArrayList<DBTower>> dbTowerss;

	public DBManager(Context context)
	{
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 获取时要判断是否为null如果为null,那么运行方法queryAll()
	 * @return
	 */
	public ArrayList<DBMap> getDbMaps()
	{
		return dbMaps;
	}

	/**
	 * 获取时要判断是否为null如果为null,那么运行方法queryAll()
	 * @return
	 */
	public ArrayList<ArrayList<DBTower>> getDbTowerss()
	{
		return dbTowerss;
	}

	/**
	 * 添加一行（元组）
	 * 
	 * @param dbMap
	 * @param dbTowers
	 */
	public void addTuple(DBMap dbMap, ArrayList<DBTower> dbTowers)
	{
		/* 开始事务 */
		db.beginTransaction();
		try
		{
			db.execSQL("INSERT INTO map VALUES(?,?,?,?,?,?,?,?)", new Object[] {
					dbMap.getMapid(), dbMap.getMapname(), dbMap.getGamemode(),
					dbMap.getGamedifficulty(), dbMap.getMonsterWave(),
					dbMap.getScore(),dbMap.getResiduallife(),dbMap.getMoney()});

			for (DBTower dbTower : dbTowers)
			{
				db.execSQL(
						"INSERT INTO tower VALUES(?,?,?,?,?,?,?)",
						new Object[] { dbTower.getCoordinateX(),
								dbTower.getCoordinateY(), dbTower.getMapid(),
								dbTower.getTowername(), dbTower.getGrade(),
								dbTower.getSkill(), dbTower.getRecoveryprice() });
			}
			/* 事务成功 */
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			/* 结束事务 */
			db.endTransaction();
		}

	}

	/**
	 * 删除一行
	 * 
	 * @param mapid
	 */
	public void deleteTuple(String mapid)
	{
		db.delete(DBHelper.DB_TABLE_NAME_MAP, "mapid=?", new String[] { mapid });

		db.delete(DBHelper.DB_TABLE_NAME_TOWER, "mapid=?",
				new String[] { mapid });
	}
	
	/**
	 * 删除所有
	 */
	public void clearAll()
	{
		try
		{
			db.execSQL("DELETE FROM " + DBHelper.DB_TABLE_NAME_MAP);
			
			db.execSQL("DELETE FROM " + DBHelper.DB_TABLE_NAME_TOWER);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * 查询所有的数据,把它们放在dbMaps和dbTowerss中
	 */
	public void queryAll()
	{
		dbMaps = new ArrayList<DBMap>();
		dbTowerss = new ArrayList<ArrayList<DBTower>>();

		Cursor cursor = db.rawQuery("SELECT * FROM "
				+ DBHelper.DB_TABLE_NAME_MAP, null);
		while (cursor.moveToNext())
		{
			DBMap dbMap = new DBMap();

			String mapid = cursor.getString(cursor.getColumnIndex("mapid"));

			dbMap.setMapid(mapid);
			dbMap.setMapname(cursor.getString(cursor.getColumnIndex("mapname")));
			dbMap.setGamemode(cursor.getInt(cursor.getColumnIndex("gamemode")));
			dbMap.setGamedifficulty(cursor.getInt(cursor
					.getColumnIndex("gamedifficulty")));
			dbMap.setMonsterWave(cursor.getInt(cursor
					.getColumnIndex("monsterWave")));
			dbMap.setScore(cursor.getInt(cursor
					.getColumnIndex("score")));
			dbMap.setResiduallife(cursor.getInt(cursor
					.getColumnIndex("residuallife")));
			dbMap.setMoney(cursor.getInt(cursor
					.getColumnIndex("money")));

			ExecSQLForDBTower("SELECT * FROM " + DBHelper.DB_TABLE_NAME_TOWER
					+ " where mapid=" + "'" + mapid + "'");
			
			dbMaps.add(dbMap);


		}
		cursor.close();
	}

	private void ExecSQLForDBTower(String sql)
	{
		ArrayList<DBTower> dbTowers = new ArrayList<DBTower>();

		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext())
		{
			DBTower dbTower = new DBTower();

			dbTower.setCoordinateX(cursor.getInt(cursor
					.getColumnIndex("coordinateX")));
			dbTower.setCoordinateY(cursor.getInt(cursor
					.getColumnIndex("coordinateY")));
			dbTower.setMapid(cursor.getString(cursor.getColumnIndex("mapid")));
			dbTower.setTowername(cursor.getString(cursor
					.getColumnIndex("towername")));
			dbTower.setGrade(cursor.getInt(cursor.getColumnIndex("grade")));
			dbTower.setSkill(cursor.getInt(cursor.getColumnIndex("skill")));
			dbTower.setRecoveryprice(cursor.getInt(cursor
					.getColumnIndex("recoveryprice")));

			dbTowers.add(dbTower);
		}
		cursor.close();

		dbTowerss.add(dbTowers);
	}
}
