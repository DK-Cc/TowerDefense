package com.hbut_cc.maptools;

import com.hbut_cc.towerdefense.R;

public class MonsterData
{
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_NORMAL = 1;
	public static final int DIFFICULTY_HARD = 2;

	protected int[] monsterMapValue = new int[] { R.drawable.mrbabyrabbit,
			R.drawable.mrcat, R.drawable.mrrabbit, R.drawable.mrevilrabbit,
			R.drawable.mrbear, R.drawable.misspiggy, R.drawable.mrmouse,
			R.drawable.mrelephant, R.drawable.mrbully, R.drawable.mrlion,
			R.drawable.mrpingu, R.drawable.mrghostrabbit, R.drawable.mrmonkey,
			R.drawable.mrsakib };

	protected int[] monsterMoveValue = new int[] {
			R.drawable.mrbabyrabbit_animate, R.drawable.mrcat_animate,
			R.drawable.mrrabbit_animate, R.drawable.mrevilrabbit_animate,
			R.drawable.mrbear_animate, R.drawable.misspiggy_animate,
			R.drawable.mrmouse_animate, R.drawable.mrelephant_animate,
			R.drawable.mrbully_animate, R.drawable.mrlion_animate,
			R.drawable.mrpingu_animate, R.drawable.mrghostrabbit_animate,
			R.drawable.mrmonkey_animate, R.drawable.mrsakib_animate };

	protected int[] monsterDeadpValue = new int[] {
			R.drawable.mrbabyrabbitdead, R.drawable.mrcatdead,
			R.drawable.mrrabbitdead, R.drawable.mrevilrabbitdead,
			R.drawable.mrbeardead, R.drawable.misspiggydead,
			R.drawable.mrmousedead, R.drawable.mrelephantdead,
			R.drawable.mrbullydead, R.drawable.mrliondead,
			R.drawable.mrpingudead, R.drawable.mrghostrabbitdead,
			R.drawable.mrmonkeydead, R.drawable.mrsakibdead };

	public int[] getMonsterMapValue()
	{
		return monsterMapValue;
	}

	/**
	 * ��ȡ��Ӧ�ѶȵĹ���(Ѫ��)����
	 * 
	 * @param difficulty
	 *            ��Ϸ�Ѷ�,0Ϊ��,1Ϊһ��,2Ϊ����
	 * @param number
	 *            �ڼ��ֹ���,���Գ���13
	 * @return �����HP
	 */
	public int getHPValue(int difficulty, int number)
	{
		int[] hps = new int[] { 50, 100, 500, 1000, 2000, 5000, 8000, 30000,
				12000, 15000, 20000, 30000, 50000, 200000 };

		if (difficulty == DIFFICULTY_EASY)
		{

		}
		else if (difficulty == DIFFICULTY_NORMAL)
		{
			for (int i = 0; i < hps.length; i++)
			{
				hps[i] = (int) (hps[i] * 1.5);
			}
		}
		else
		{
			for (int i = 0; i < hps.length; i++)
			{
				hps[i] = hps[i] * 3;
			}
		}

		int times = number / hps.length;

		return (int) (hps[number % hps.length] * (times + 1) + hps[hps.length - 1]
				* times);
	}

	/**
	 * ��ù�����ٶ�
	 * 
	 * @param level
	 *            �ڼ�������0��ʼ������ΪN��
	 * @return
	 */
	public float getMoveSpeed(int level)
	{
		float[] speeds = { 1, 1, 1, 2, 1, 1, 3, (float) 0.5, 1, 1, 1, 2, 1,
				(float) 0.5 };
		return speeds[level % speeds.length];
	}

	/**
	 * ��ȡ����� �� ��,true�����п���
	 * 
	 * @param level
	 *            �ڼ�������0��ʼ,����ΪN��
	 * @return
	 */
	public boolean getIceResistance(int level)
	{
		boolean[] iceResistance = { false, false, false, false, false, false,
				false, false, false, false, true, true, false, false };
		return iceResistance[level % iceResistance.length];
	}

	/**
	 * ��ȡ����� �� ��,true�����п���
	 * 
	 * @param level
	 *            �ڼ�������0��ʼ,����ΪN��
	 * @return
	 */
	public boolean getFireResistance(int level)
	{
		boolean[] fireResistance = { false, false, false, false, false, false,
				false, false, true, false, false, false, false, false };
		return fireResistance[level % fireResistance.length];
	}

	/**
	 * ��ȡ����� �� ��,true�����п���
	 * 
	 * @param level
	 *            �ڼ�������0��ʼ,����ΪN��
	 * @return
	 */
	public boolean getToxinResistance(int level)
	{
		boolean[] toxinResistance = { false, false, false, true, false, false,
				true, false, false, false, false, true, false, false };
		return toxinResistance[level % toxinResistance.length];
	}

	/**
	 * ���ÿ������ɱ�������õķ���
	 * 
	 * @param level
	 *            �ڼ�������0��ʼ,����ΪN��
	 * @return
	 */
	public int getEachWaveScore(int level)
	{
		int[] scores = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120,
				130, 200 };

		int times = level / scores.length;

		return scores[level % scores.length] * (times + 1)
				+ scores[scores.length - 1] * times;
	}

	/**
	 * ���ÿ������ɱ�������õĽ��
	 * 
	 * @param level
	 *            �ڼ�������0��ʼ,n��
	 * @return
	 */
	public int getEachWaveMoney(int level)
	{
		int[] moneys = { 5, 7, 10, 15, 20, 30, 70, 50, 60, 60, 100, 200, 100,
				500 };

		int times = level / moneys.length;

		return moneys[level % moneys.length] * (times + 1)
				+ moneys[moneys.length - 1] * times;

	}

	/**
	 * ���ÿ�������������
	 * 
	 * @param level
	 *            �ڼ�������0��ʼ,���Ե�N��
	 * @return
	 */
	public int getPhysicsResistance(int level)
	{
		int[] physics = { 0, 0, 10, 0, 50, 30, 0, 100, 0, 40, 160, 1000, 50,
				100 };

		int times = level / physics.length;

		return physics[level % physics.length] * (times + 1)
				+ physics[physics.length - 1] * times;

	}

}
