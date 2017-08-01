package com.hbut_cc.maptools;

import com.hbut_cc.towerdefense.R;

public class TowerData
{
	public static final int TOWER1 = 1;
	public static final int TOWER2 = 2;
	public static final int TOWER3 = 3;
	public static final int TOWER4 = 4;

	public static final int[] TowerId1 = new int[] { R.drawable.bunker1,
			R.drawable.bunker2, R.drawable.bunker3 };

	public static final int[] TowerId2 = new int[] { R.drawable.cannontower1,
			R.drawable.cannontower2, R.drawable.cannontower3 };

	public static final int[] TowerId3 = new int[] { R.drawable.poisontower1,
			R.drawable.poisontower2, R.drawable.poisontower3,
			R.drawable.poisontower_special_1, R.drawable.poisontower_special_2,
			R.drawable.poisontower_special_3 };

	public static final int[] TowerId4 = new int[] { R.drawable.tesla1,
			R.drawable.tesla2, R.drawable.tesla3, R.drawable.tesla_special_1,
			R.drawable.tesla_special_2, R.drawable.tesla_special_3 };

	protected SkillsValue getInitSkillsValue(int towerType)
	{
		SkillsValue skillsValue = new SkillsValue(towerType);

		switch (towerType)
		{
		case TOWER1:
			/* 使防御塔1具有火和毒的能力 */
			skillsValue.getFirePower();
			skillsValue.getToxinPower();
			break;
		case TOWER2:
			/* 使防御塔2具有火和冰的能力 */
			skillsValue.getFirePower();
			skillsValue.getIcePower();
			break;
		case TOWER3:
			/* 使防御塔3具有特殊塔 */
			skillsValue.getSpecialPower();
			break;
		case TOWER4:
			/* 使防御塔4具有特殊塔 */
			skillsValue.getSpecialPower();
			break;

		default:
			break;
		}

		return skillsValue;
	}

	/**
	 * 获得攻击范围除以FrameW所得的倍数
	 * 
	 * @param sValue
	 * @return FrameW的倍数
	 */
	protected float getAttackRFrameWTimes(SkillsValue sValue)
	{
		int towerType = sValue.getTowerType();
		int attackLevel = sValue.getAttackLevel();

		if (towerType == TowerData.TOWER1)
		{
			if (attackLevel == 1)
			{
				return (float) 1.6;
			}
			else if (attackLevel == 2)
			{
				return (float) 1.8;
			}
			else
			{
				return (float) 2.2;
			}
		}
		else if (towerType == TowerData.TOWER2)
		{
			if (attackLevel == 1)
			{
				return (float) 2.0;
			}
			else if (attackLevel == 2)
			{
				return (float) 2.4;
			}
			else
			{
				return (float) 2.8;
			}
		}
		else if (towerType == TowerData.TOWER3)
		{
			if (attackLevel == 1)
			{
				return (float) 1.6;
			}
			else if (attackLevel == 2)
			{
				return (float) 1.8;
			}
			else
			{
				return (float) 2.0;
			}
		}
		else
		{
			if (attackLevel == 1)
			{
				return (float) 4.0;
			}
			else if (attackLevel == 2)
			{
				return (float) 4.5;
			}
			else
			{
				return (float) 5.0;
			}
		}
	}
	
	/**
	 * 获得防御塔攻击时间间隔
	 * @param towerType 防御塔种类
	 * @return 防御塔攻击时间间隔
	 */
	protected int getAttackTime(int towerType)
	{
		if(towerType == TowerData.TOWER1)
		{
			return 150;
		}
		else if(towerType == TowerData.TOWER2)
		{
			return 1000;
		}
		else if(towerType == TowerData.TOWER3)
		{
			return 500;
		}
		else 
		{
			return 3000;
		}
			
	}

}
