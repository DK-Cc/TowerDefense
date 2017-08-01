package com.hbut_cc.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class DBMap implements Parcelable
{

	private String mapid;

	private String mapname;

	private int gamemode;

	private int gamedifficulty;

	private int monsterWave;

	private int score;

	private int residuallife;

	private int money;

	public DBMap()
	{
	}

	public DBMap(Parcel in)
	{
		mapid = in.readString();
		mapname = in.readString();
		gamemode = in.readInt();
		gamedifficulty = in.readInt();
		monsterWave = in.readInt();
		score = in.readInt();
		residuallife = in.readInt();
		money = in.readInt();
	}

	public String getMapid()
	{
		return mapid;
	}

	public void setMapid(String mapid)
	{
		this.mapid = mapid;
	}

	public String getMapname()
	{
		return mapname;
	}

	public void setMapname(String mapname)
	{
		this.mapname = mapname;
	}

	public int getGamemode()
	{
		return gamemode;
	}

	public void setGamemode(int gamemode)
	{
		this.gamemode = gamemode;
	}

	public int getGamedifficulty()
	{
		return gamedifficulty;
	}

	public void setGamedifficulty(int gamedifficulty)
	{
		this.gamedifficulty = gamedifficulty;
	}

	public int getMonsterWave()
	{
		return monsterWave;
	}

	public void setMonsterWave(int monsterWave)
	{
		this.monsterWave = monsterWave;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}

	public int getResiduallife()
	{
		return residuallife;
	}

	public void setResiduallife(int residuallife)
	{
		this.residuallife = residuallife;
	}

	public int getMoney()
	{
		return money;
	}

	public void setMoney(int money)
	{
		this.money = money;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(mapid);
		dest.writeString(mapname);
		dest.writeInt(gamemode);
		dest.writeInt(gamedifficulty);
		dest.writeInt(monsterWave);
		dest.writeInt(score);
		dest.writeInt(residuallife);
		dest.writeInt(money);
	}

	public static final Parcelable.Creator<DBMap> CREATOR = new Creator<DBMap>()
	{
		@Override
		public DBMap[] newArray(int size)
		{
			return new DBMap[size];
		}

		@Override
		public DBMap createFromParcel(Parcel in)
		{
			return new DBMap(in);
		}
	};

}
