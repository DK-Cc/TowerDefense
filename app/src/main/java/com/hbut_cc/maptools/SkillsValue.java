package com.hbut_cc.maptools;

import com.hbut_cc.towerdefense.R;

public class SkillsValue
{
	/**
	 * 保存着四个防御塔相对应升级技能所需花费<br>
	 * 当为-1时代表不能升级此技能
	 * <p>
	 * attack2 attack3 special toxin1~toxin5 fire1~fire5 ice1~ice5
	 */
	private int[][] upgradeCost = {
			{ 50, 100, -1, 50, 150, 300, 450, 600, 50, 150, 300, 450, 600, -1,
					-1, -1, -1, -1 },
			{ 80, 180, -1, -1, -1, -1, -1, -1, 50, 150, 300, 500, 700, 50, 150,
					300, 400, 500 },
			{ 160, 300, 2000, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1 },
			{ 500, 900, 5000, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
					-1, -1, -1 } };

	/**
	 * 保存防御塔1的毒伤害
	 */
	private int[] tower1ToxinDamage = { 50, 200, 300, 450, 600 };

	/**
	 * 保存防御塔1的火伤害
	 */
	private int[] tower1FireDamage = { 50, 200, 300, 450, 600 };

	/**
	 * 保存防御塔2的火伤害
	 */
	private int[] tower2FireDamage = { 50, 250, 400, 500, 800 };

	/**
	 * 保存防御塔2冰的伤害
	 */
	private int[] tower2IceDamage = { 50, 100, 150, 220, 400 };

	/**
	 * 保存防御塔3自身毒的伤害
	 */
	private int[] tower3SelfToxinDamage = { 150, 300, 1000 };

	/**
	 * 保存防御塔3的特殊伤害
	 */
	private int tower3SpecialDamage = 10000;

	/**
	 * 保存防御塔4的特殊伤害
	 */
	private int tower4SpecialDamage = 50000;

	/**
	 * 保存着四个防御塔对应等级下的攻击力
	 * <p>
	 * 等级1，等级2，等级3，等级4
	 */
	private int[][] towerAttackValue = { { 10, 60, 150 }, { 80, 200, 500 },
			{ 5, 20, 50 }, { 600, 2000, 9000 } };

	/**
	 * 保存着初始防御塔建造需要花费的金钱
	 */
	public static final int[] InitialCost = { 20, 45, 100, 200 };

	/**
	 * 防御塔的种类，取值为1,2,3,4
	 * */
	private int towerType;

	/**
	 * 记录防御塔拥有该技能的等级
	 * <p>
	 * 攻击等级,其取值只能为1,2,3<br>
	 * 初始状态时它为1,即一开始就可以升级为等级1
	 */
	private int attackLevel = 1;

	/**
	 * 记录防御塔拥有该技能的等级
	 * <p>
	 * 该防御塔的特殊塔等级,只能取值-1,0,1 ;<br>
	 * -1代表不具备特殊塔,0代表具备特殊塔
	 */
	private int specialLevel = -1;

	/**
	 * 记录防御塔拥有该技能的等级
	 * <p>
	 * 毒的等级,取值-1~5;<br>
	 * 当为-1时代表此防御塔不具备毒的能力,0代表具备此能力;<br>
	 * 1代表等级为1
	 */
	private int toxinLevel = -1;

	/**
	 * 记录防御塔拥有该技能的等级
	 * <p>
	 * 火的等级，同毒
	 */
	private int fireLevel = -1;

	/**
	 * 记录防御塔拥有该技能的等级
	 * <p>
	 * 冰的等级，同毒
	 */
	private int iceLevel = -1;

	/**
	 * 防御塔的总价值
	 * */
	private int totalValue;

	public SkillsValue(int towerType)
	{
		this.towerType = towerType;

		totalValue = InitialCost[towerType - 1];
	}

	public int getTowerType()
	{
		return towerType;
	}

	/**
	 * 当拥有毒的能力时,获取当前可以升级的毒的等级图标在R文件中的id;<br>
	 * 当没有毒的能力时,返回-1;表示不能升级 当attackLevel为1时也返回-1
	 * 
	 * @return -1 or 毒现在可以升级的等级图标在R文件中的id
	 */
	public int getUpgradeToxinBitmapId()
	{

		if (attackLevel == 1)
		{
			return -1;
		}

		if (toxinLevel == 0)
		{
			return R.drawable.xml_upgrade_poison1;
		}
		else if (toxinLevel == 1)
		{
			return R.drawable.xml_upgrade_poison2;
		}
		else if (toxinLevel == 2)
		{
			return R.drawable.xml_upgrade_poison3;
		}
		else if (toxinLevel == 3)
		{
			return R.drawable.xml_upgrade_poison4;
		}
		else if (toxinLevel == 4)
		{
			return R.drawable.xml_upgrade_poison5;
		}

		return -1;
	}

	/**
	 * 当拥有火的能力时,获取当前可以升级的火的等级图标在R文件中的id;<br>
	 * 当没有火的能力时,返回-1;表示不能再升级了 当attackLevel为1时也返回-1
	 * 
	 * @return -1 or 火现在可以升级的等级图标在R文件中的id
	 */
	public int getUpgradeFireBitmapId()
	{
		if (attackLevel == 1)
		{
			return -1;
		}

		if (fireLevel == 0)
		{
			return R.drawable.xml_upgrade_fire1;
		}
		else if (fireLevel == 1)
		{
			return R.drawable.xml_upgrade_fire2;
		}
		else if (fireLevel == 2)
		{
			return R.drawable.xml_upgrade_fire3;
		}
		else if (fireLevel == 3)
		{
			return R.drawable.xml_upgrade_fire4;
		}
		else if (fireLevel == 4)
		{
			return R.drawable.xml_upgrade_fire5;
		}

		return -1;
	}

	/**
	 * 当拥有冰的能力时,获取当前可以升级的冰的等级图标在R文件中的id;<br>
	 * 当没有冰的能力时,返回-1;表示不能再升级了 当attackLevel为1时也返回-1
	 * 
	 * @return -1 or 冰现在可以升级的等级图标在R文件中的id
	 */
	public int getUpgradeIceBitmapId()
	{
		if (attackLevel == 1)
		{
			return -1;
		}

		if (iceLevel == 0)
		{
			return R.drawable.xml_upgrade_frost1;
		}
		else if (iceLevel == 1)
		{
			return R.drawable.xml_upgrade_frost2;
		}
		else if (iceLevel == 2)
		{
			return R.drawable.xml_upgrade_frost3;
		}
		else if (iceLevel == 3)
		{
			return R.drawable.xml_upgrade_frost4;
		}
		else if (iceLevel == 4)
		{
			return R.drawable.xml_upgrade_frost5;
		}

		return -1;
	}

	/**
	 * 获取当前可以升级的 攻击 的等级图标在R文件中的id;<br>
	 * 返回-1;表示不能再升级了
	 * 
	 * @return -1 or 攻击现在可以升级的等级图标id
	 */
	public int getUpgradeAttackBitmapId()
	{
		if (attackLevel == 1)
		{
			return R.drawable.xml_upgrade_lvl2;
		}
		else if (attackLevel == 2)
		{
			return R.drawable.xml_upgrade_lvl3;
		}

		return -1;
	}

	/**
	 * 当拥有特殊塔时,获取当前可以升级的特殊塔的图标在R文件中的id;<br>
	 * 当没有特殊塔时,返回-1;表示不能升级 当attackLevel为1时也返回-1
	 * 
	 * @return -1 or 特殊塔现在可以升级的图标在R文件中的id
	 */
	public int getUpgradeSpecialBitmapId()
	{
		if (attackLevel == 1)
		{
			return -1;
		}

		if (specialLevel == 0)
		{
			return R.drawable.xml_upgrade_special;
		}

		return -1;
	}

	public int getToxinLevel()
	{
		return toxinLevel;
	}

	public int getAttackLevel()
	{
		return attackLevel;
	}

	public int getFireLevel()
	{
		return fireLevel;
	}

	public int getIceLevel()
	{
		return iceLevel;
	}

	public int getSpecialLevel()
	{
		return specialLevel;
	}

	public void getSpecialPower()
	{
		specialLevel = 0;
	}

	public void getToxinPower()
	{
		toxinLevel = 0;
	}

	public void getIcePower()
	{
		iceLevel = 0;
	}

	public void getFirePower()
	{
		fireLevel = 0;
	}

	public void increaseAttackLevel()
	{
		if (attackLevel < 3)
		{
			attackLevel++;
			totalValue = totalValue
					+ upgradeCost[towerType - 1][attackLevel - 2];
		}
	}

	public void increaseSpecialLevel()
	{
		if (specialLevel == 0)
		{
			specialLevel = 1;
			totalValue = totalValue + upgradeCost[towerType - 1][2];
		}
	}

	/**
	 * 升级毒的等级,当为防御塔1时且毒的能力为1时,取消火的能力
	 * <p>
	 * 即升级了毒的能力就不能有火的能力
	 */
	public void increaseToxinLevel()
	{
		if (toxinLevel > -1 && toxinLevel < 5)
		{
			toxinLevel++;

			if (towerType == Tower.TOWER1 && toxinLevel == 1)
			{
				/* 取消防御塔火的能力 */
				fireLevel = -1;
			}

			totalValue = totalValue
					+ upgradeCost[towerType - 1][toxinLevel + 2];
		}
	}

	/**
	 * 升级冰的等级,当为防御塔2时且冰的能力为1时,取消火的能力
	 * <p>
	 * 即升级了冰的能力,防御塔2就不能有火的能力
	 */
	public void increaseIceLevel()
	{
		if (iceLevel > -1 && iceLevel < 5)
		{
			iceLevel++;

			if (towerType == Tower.TOWER2 && iceLevel == 1)
			{
				/* 取消火的能力 */
				fireLevel = -1;
			}

			totalValue = totalValue + upgradeCost[towerType - 1][iceLevel + 12];
		}
	}

	/**
	 * 升级火的等级,当为防御塔1时且火的能力为1时,取消毒的能力<br>
	 * 当为防御塔2时且火的能力为1时,取消冰的能力
	 * <p>
	 * 即升级了 火 的能力 防御塔1 就不能有毒的能力<br>
	 * 或升级了 火 的能力 防御塔2 就不能有冰的能力
	 */
	public void increaseFireLevel()
	{
		if (fireLevel > -1 && fireLevel < 5)
		{
			fireLevel++;
			if (towerType == Tower.TOWER1 && fireLevel == 1)
			{
				/* 取消毒的能力 */
				toxinLevel = -1;
			}
			else if (towerType == Tower.TOWER2 && fireLevel == 1)
			{
				/* 取消冰的能力 */
				iceLevel = -1;
			}

			totalValue = totalValue + upgradeCost[towerType - 1][fireLevel + 7];
		}
	}

	/**
	 * 获取升级attack所需的金币数<br>
	 * 当为-1时代表不能再升级了
	 * 
	 * @return -1 or 所需金币数
	 */
	public int upgradeAttackMoney()
	{
		if (attackLevel < 3)
		{
			/* attackLevel == 1,代表升级攻击2所需金币 */
			return upgradeCost[towerType - 1][attackLevel - 1];
		}
		return -1;
	}

	/**
	 * 获取升级special所需的金币数<br>
	 * 当为-1时代表不能再升级了
	 * 
	 * @return -1 or 所需金币数
	 */
	public int upgradeSpecialMoney()
	{
		if (specialLevel == 0)
		{
			return upgradeCost[towerType - 1][2];
		}
		return -1;
	}

	/**
	 * 获取升级toxin所需的金币数<br>
	 * 当为-1时代表不能再升级了
	 * 
	 * @return -1 or 所需金币数
	 */
	public int upgradeToxinMoney()
	{
		if (toxinLevel > -1 && toxinLevel < 5)
		{
			return upgradeCost[towerType - 1][toxinLevel + 3];
		}
		return -1;
	}

	/**
	 * 获取升级fire所需的金币数<br>
	 * 当为-1时代表不能再升级了
	 * 
	 * @return -1 or 所需金币数
	 */
	public int upgradeFireMoney()
	{
		if (fireLevel > -1 && fireLevel < 5)
		{
			return upgradeCost[towerType - 1][fireLevel + 8];
		}
		return -1;
	}

	/**
	 * 获取升级ice所需的金币数<br>
	 * 当为-1时代表不能再升级了
	 * 
	 * @return -1 or 所需金币数
	 */
	public int upgradeIceMoney()
	{
		if (iceLevel > -1 && iceLevel < 5)
		{
			return upgradeCost[towerType - 1][iceLevel + 13];
		}
		return -1;
	}

	/**
	 * 获得当前attackLevel等级下的bitmap的id
	 * 
	 * @return bitmap在R文件中的id
	 */
	public int getTowerBitmapId()
	{
		if (towerType == Tower.TOWER1)
		{
			return Tower.TowerId1[attackLevel - 1];
		}
		else if (towerType == Tower.TOWER2)
		{
			return Tower.TowerId2[attackLevel - 1];
		}
		else if (towerType == Tower.TOWER3)
		{
			if (specialLevel == 1)
			{
				return Tower.TowerId3[attackLevel + 2];
			}
			else
			{
				return Tower.TowerId3[attackLevel - 1];
			}
		}
		else
		{
			if (specialLevel == 1)
			{
				return Tower.TowerId4[attackLevel + 2];
			}
			else
			{
				return Tower.TowerId4[attackLevel - 1];
			}
		}
	}

	/**
	 * 获得系统设定的相应等级下防御塔的攻击力
	 * 
	 * @return 相应等级下的防御塔的攻击力
	 */
	public int getTowerAttackValue()
	{
		return towerAttackValue[towerType - 1][attackLevel - 1];
	}

	/**
	 * 获得出售该防御塔的金钱数,其值为总价值的80%
	 * 
	 * @return
	 */
	public int getSellMoney()
	{
		return (int) (totalValue * 0.8);
	}

	/**
	 * 获取攻击特效图片在R文件中id
	 * 
	 * @return 特效图片在R文件中的id
	 */
	public int getAttackBitmapId()
	{
		if (towerType == TowerData.TOWER1)
		{
			return R.drawable.throwingstar;
		}
		else if (towerType == TowerData.TOWER2)
		{
			return R.drawable.cannonshot;

		}
		else if (towerType == TowerData.TOWER3)
		{
			return R.drawable.poisoncloud;
		}
		else
		{
			return R.drawable.lightbolt;
		}
	}

	/**
	 * 获得毒伤害的值
	 * 
	 * @return 0 or 毒伤害值
	 */
	public int getToxinDmage()
	{
		if (towerType == TowerData.TOWER1)
		{
			if (toxinLevel < 1 || toxinLevel > 5)
			{
				return 0;
			}
			else
			{
				return tower1ToxinDamage[toxinLevel - 1];
			}
		}
		else if (towerType == TowerData.TOWER3)
		{
			if (attackLevel < 1 || attackLevel > 3)
			{
				return 0;
			}
			else
			{
				return tower3SelfToxinDamage[attackLevel - 1];
			}
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 获得火伤害的值
	 * 
	 * @return 0 or 火伤害值
	 */
	public int getFireDamage()
	{
		if (towerType == TowerData.TOWER1)
		{
			if (fireLevel < 1 || fireLevel > 5)
			{
				return 0;
			}
			else
			{
				return tower1FireDamage[fireLevel - 1];
			}
		}
		else if (towerType == TowerData.TOWER2)
		{
			if (fireLevel < 1 || fireLevel > 5)
			{
				return 0;
			}
			else
			{
				return tower2FireDamage[fireLevel - 1];
			}
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 获得冰伤害的值
	 * 
	 * @return 0 or 冰伤害值
	 */
	public int getIceDamage()
	{
		if (towerType == TowerData.TOWER2 && iceLevel > 0 && iceLevel < 6)
		{
			return tower2IceDamage[iceLevel - 1];
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 获得特殊伤害的值
	 * 
	 * @return 0 or 特殊伤害值
	 */
	public int getSpecialDamage()
	{
		if (towerType == TowerData.TOWER3)
		{
			if (specialLevel == 1)
			{
				return tower3SpecialDamage;
			}
			else
			{
				return 0;
			}
		}
		else if (towerType == TowerData.TOWER4)
		{
			if (specialLevel == 1)
			{
				return tower4SpecialDamage;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}

}
