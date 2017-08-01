package com.hbut_cc.maptools;

import com.hbut_cc.towerdefense.R;

public class SkillsValue
{
	/**
	 * �������ĸ����������Ӧ�����������軨��<br>
	 * ��Ϊ-1ʱ�����������˼���
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
	 * ���������1�Ķ��˺�
	 */
	private int[] tower1ToxinDamage = { 50, 200, 300, 450, 600 };

	/**
	 * ���������1�Ļ��˺�
	 */
	private int[] tower1FireDamage = { 50, 200, 300, 450, 600 };

	/**
	 * ���������2�Ļ��˺�
	 */
	private int[] tower2FireDamage = { 50, 250, 400, 500, 800 };

	/**
	 * ���������2�����˺�
	 */
	private int[] tower2IceDamage = { 50, 100, 150, 220, 400 };

	/**
	 * ���������3�������˺�
	 */
	private int[] tower3SelfToxinDamage = { 150, 300, 1000 };

	/**
	 * ���������3�������˺�
	 */
	private int tower3SpecialDamage = 10000;

	/**
	 * ���������4�������˺�
	 */
	private int tower4SpecialDamage = 50000;

	/**
	 * �������ĸ���������Ӧ�ȼ��µĹ�����
	 * <p>
	 * �ȼ�1���ȼ�2���ȼ�3���ȼ�4
	 */
	private int[][] towerAttackValue = { { 10, 60, 150 }, { 80, 200, 500 },
			{ 5, 20, 50 }, { 600, 2000, 9000 } };

	/**
	 * �����ų�ʼ������������Ҫ���ѵĽ�Ǯ
	 */
	public static final int[] InitialCost = { 20, 45, 100, 200 };

	/**
	 * �����������࣬ȡֵΪ1,2,3,4
	 * */
	private int towerType;

	/**
	 * ��¼������ӵ�иü��ܵĵȼ�
	 * <p>
	 * �����ȼ�,��ȡֵֻ��Ϊ1,2,3<br>
	 * ��ʼ״̬ʱ��Ϊ1,��һ��ʼ�Ϳ�������Ϊ�ȼ�1
	 */
	private int attackLevel = 1;

	/**
	 * ��¼������ӵ�иü��ܵĵȼ�
	 * <p>
	 * �÷��������������ȼ�,ֻ��ȡֵ-1,0,1 ;<br>
	 * -1�����߱�������,0����߱�������
	 */
	private int specialLevel = -1;

	/**
	 * ��¼������ӵ�иü��ܵĵȼ�
	 * <p>
	 * ���ĵȼ�,ȡֵ-1~5;<br>
	 * ��Ϊ-1ʱ����˷��������߱���������,0����߱�������;<br>
	 * 1����ȼ�Ϊ1
	 */
	private int toxinLevel = -1;

	/**
	 * ��¼������ӵ�иü��ܵĵȼ�
	 * <p>
	 * ��ĵȼ���ͬ��
	 */
	private int fireLevel = -1;

	/**
	 * ��¼������ӵ�иü��ܵĵȼ�
	 * <p>
	 * ���ĵȼ���ͬ��
	 */
	private int iceLevel = -1;

	/**
	 * ���������ܼ�ֵ
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
	 * ��ӵ�ж�������ʱ,��ȡ��ǰ���������Ķ��ĵȼ�ͼ����R�ļ��е�id;<br>
	 * ��û�ж�������ʱ,����-1;��ʾ�������� ��attackLevelΪ1ʱҲ����-1
	 * 
	 * @return -1 or �����ڿ��������ĵȼ�ͼ����R�ļ��е�id
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
	 * ��ӵ�л������ʱ,��ȡ��ǰ���������Ļ�ĵȼ�ͼ����R�ļ��е�id;<br>
	 * ��û�л������ʱ,����-1;��ʾ������������ ��attackLevelΪ1ʱҲ����-1
	 * 
	 * @return -1 or �����ڿ��������ĵȼ�ͼ����R�ļ��е�id
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
	 * ��ӵ�б�������ʱ,��ȡ��ǰ���������ı��ĵȼ�ͼ����R�ļ��е�id;<br>
	 * ��û�б�������ʱ,����-1;��ʾ������������ ��attackLevelΪ1ʱҲ����-1
	 * 
	 * @return -1 or �����ڿ��������ĵȼ�ͼ����R�ļ��е�id
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
	 * ��ȡ��ǰ���������� ���� �ĵȼ�ͼ����R�ļ��е�id;<br>
	 * ����-1;��ʾ������������
	 * 
	 * @return -1 or �������ڿ��������ĵȼ�ͼ��id
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
	 * ��ӵ��������ʱ,��ȡ��ǰ������������������ͼ����R�ļ��е�id;<br>
	 * ��û��������ʱ,����-1;��ʾ�������� ��attackLevelΪ1ʱҲ����-1
	 * 
	 * @return -1 or ���������ڿ���������ͼ����R�ļ��е�id
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
	 * �������ĵȼ�,��Ϊ������1ʱ�Ҷ�������Ϊ1ʱ,ȡ���������
	 * <p>
	 * �������˶��������Ͳ����л������
	 */
	public void increaseToxinLevel()
	{
		if (toxinLevel > -1 && toxinLevel < 5)
		{
			toxinLevel++;

			if (towerType == Tower.TOWER1 && toxinLevel == 1)
			{
				/* ȡ��������������� */
				fireLevel = -1;
			}

			totalValue = totalValue
					+ upgradeCost[towerType - 1][toxinLevel + 2];
		}
	}

	/**
	 * �������ĵȼ�,��Ϊ������2ʱ�ұ�������Ϊ1ʱ,ȡ���������
	 * <p>
	 * �������˱�������,������2�Ͳ����л������
	 */
	public void increaseIceLevel()
	{
		if (iceLevel > -1 && iceLevel < 5)
		{
			iceLevel++;

			if (towerType == Tower.TOWER2 && iceLevel == 1)
			{
				/* ȡ��������� */
				fireLevel = -1;
			}

			totalValue = totalValue + upgradeCost[towerType - 1][iceLevel + 12];
		}
	}

	/**
	 * ������ĵȼ�,��Ϊ������1ʱ�һ������Ϊ1ʱ,ȡ����������<br>
	 * ��Ϊ������2ʱ�һ������Ϊ1ʱ,ȡ����������
	 * <p>
	 * �������� �� ������ ������1 �Ͳ����ж�������<br>
	 * �������� �� ������ ������2 �Ͳ����б�������
	 */
	public void increaseFireLevel()
	{
		if (fireLevel > -1 && fireLevel < 5)
		{
			fireLevel++;
			if (towerType == Tower.TOWER1 && fireLevel == 1)
			{
				/* ȡ���������� */
				toxinLevel = -1;
			}
			else if (towerType == Tower.TOWER2 && fireLevel == 1)
			{
				/* ȡ���������� */
				iceLevel = -1;
			}

			totalValue = totalValue + upgradeCost[towerType - 1][fireLevel + 7];
		}
	}

	/**
	 * ��ȡ����attack����Ľ����<br>
	 * ��Ϊ-1ʱ��������������
	 * 
	 * @return -1 or ��������
	 */
	public int upgradeAttackMoney()
	{
		if (attackLevel < 3)
		{
			/* attackLevel == 1,������������2������ */
			return upgradeCost[towerType - 1][attackLevel - 1];
		}
		return -1;
	}

	/**
	 * ��ȡ����special����Ľ����<br>
	 * ��Ϊ-1ʱ��������������
	 * 
	 * @return -1 or ��������
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
	 * ��ȡ����toxin����Ľ����<br>
	 * ��Ϊ-1ʱ��������������
	 * 
	 * @return -1 or ��������
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
	 * ��ȡ����fire����Ľ����<br>
	 * ��Ϊ-1ʱ��������������
	 * 
	 * @return -1 or ��������
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
	 * ��ȡ����ice����Ľ����<br>
	 * ��Ϊ-1ʱ��������������
	 * 
	 * @return -1 or ��������
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
	 * ��õ�ǰattackLevel�ȼ��µ�bitmap��id
	 * 
	 * @return bitmap��R�ļ��е�id
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
	 * ���ϵͳ�趨����Ӧ�ȼ��·������Ĺ�����
	 * 
	 * @return ��Ӧ�ȼ��µķ������Ĺ�����
	 */
	public int getTowerAttackValue()
	{
		return towerAttackValue[towerType - 1][attackLevel - 1];
	}

	/**
	 * ��ó��۸÷������Ľ�Ǯ��,��ֵΪ�ܼ�ֵ��80%
	 * 
	 * @return
	 */
	public int getSellMoney()
	{
		return (int) (totalValue * 0.8);
	}

	/**
	 * ��ȡ������ЧͼƬ��R�ļ���id
	 * 
	 * @return ��ЧͼƬ��R�ļ��е�id
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
	 * ��ö��˺���ֵ
	 * 
	 * @return 0 or ���˺�ֵ
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
	 * ��û��˺���ֵ
	 * 
	 * @return 0 or ���˺�ֵ
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
	 * ��ñ��˺���ֵ
	 * 
	 * @return 0 or ���˺�ֵ
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
	 * ��������˺���ֵ
	 * 
	 * @return 0 or �����˺�ֵ
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
