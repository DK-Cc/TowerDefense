package com.hbut_cc.utils;

import java.util.ArrayList;

import android.content.Context;

public class DBThread extends Thread
{

	private DBManager dbManager;

	private ArrayList<DBMap> dbMaps;
	private ArrayList<ArrayList<DBTower>> dbTowerss;

	public DBThread(Context context)
	{
		dbManager = new DBManager(context);
	}

	public ArrayList<DBMap> getDbMaps()
	{
		return dbMaps;
	}

	public ArrayList<ArrayList<DBTower>> getDbTowerss()
	{
		return dbTowerss;
	}

	@Override
	public void run()
	{
		dbManager.queryAll();
		dbMaps = dbManager.getDbMaps();
		dbTowerss = dbManager.getDbTowerss();
	}
}
