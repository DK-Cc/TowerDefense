package com.hbut_cc.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class DBTower implements Parcelable
{

	private int coordinateX;

	private int coordinateY;

	private String mapid;

	private String towername;

	private int grade;

	private int skill;

	private int recoveryprice;

	public DBTower()
	{
	}

	public DBTower(Parcel in)
	{
		coordinateX = in.readInt();
		coordinateY = in.readInt();
		mapid = in.readString();
		towername = in.readString();
		grade = in.readInt();
		skill = in.readInt();
		recoveryprice = in.readInt();
	}

	public int getCoordinateX()
	{
		return coordinateX;
	}

	public void setCoordinateX(int coordinateX)
	{
		this.coordinateX = coordinateX;
	}

	public int getCoordinateY()
	{
		return coordinateY;
	}

	public void setCoordinateY(int coordinateY)
	{
		this.coordinateY = coordinateY;
	}

	public String getMapid()
	{
		return mapid;
	}

	public void setMapid(String mapid)
	{
		this.mapid = mapid;
	}

	public String getTowername()
	{
		return towername;
	}

	public void setTowername(String towername)
	{
		this.towername = towername;
	}

	public int getGrade()
	{
		return grade;
	}

	public void setGrade(int grade)
	{
		this.grade = grade;
	}

	public int getSkill()
	{
		return skill;
	}

	public void setSkill(int skill)
	{
		this.skill = skill;
	}

	public int getRecoveryprice()
	{
		return recoveryprice;
	}

	public void setRecoveryprice(int recoveryprice)
	{
		this.recoveryprice = recoveryprice;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(coordinateX);
		dest.writeInt(coordinateY);
		dest.writeString(mapid);
		dest.writeString(towername);
		dest.writeInt(grade);
		dest.writeInt(skill);
		dest.writeInt(recoveryprice);
	}

	public static final Parcelable.Creator<DBTower> CREATOR = new Creator<DBTower>()
	{
		@Override
		public DBTower[] newArray(int size)
		{
			return new DBTower[size];
		}

		@Override
		public DBTower createFromParcel(Parcel in)
		{
			return new DBTower(in);
		}
	};

}
