package com.hbut_cc.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * �������ݿ����
 * 
 * @author c.c
 *
 */
public class DBManager
{

	/**
	 * ��󴢴���
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
	 * ��ȡʱҪ�ж��Ƿ�Ϊnull���Ϊnull,��ô���з���queryAll()
	 * @return
	 */
	public ArrayList<DBMap> getDbMaps()
	{
		return dbMaps;
	}

	/**
	 * ��ȡʱҪ�ж��Ƿ�Ϊnull���Ϊnull,��ô���з���queryAll()
	 * @return
	 */
	public ArrayList<ArrayList<DBTower>> getDbTowerss()
	{
		return dbTowerss;
	}

	/**
	 * ���һ�У�Ԫ�飩
	 * 
	 * @param dbMap
	 * @param dbTowers
	 */
	public void addTuple(DBMap dbMap, ArrayList<DBTower> dbTowers)
	{
		/* ��ʼ���� */
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
			/* ����ɹ� */
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			/* �������� */
			db.endTransaction();
		}

	}

	/**
	 * ɾ��һ��
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
	 * ɾ������
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
	 * ��ѯ���е�����,�����Ƿ���dbMaps��dbTowerss��
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
