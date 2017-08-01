package com.hbut_cc.maptools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hbut_cc.utils.BitmapUtils;

public class Monster extends MonsterData implements Runnable
{
	/* ��ÿ��ֳɶ��ٲ���ȥ�� */
	private static final int Multiple = 16;

	private static final int sleepTime = 100;

	/* ����ֵ */
	private int hp;
	/* ������ֵ */
	private int totalHP;

	/**
	 * ����,����,��,�￹
	 */
	private boolean iceR, toxinR, fireR;

	/**
	 * ɱ��������õĽ��,ɱ��������õķ���
	 */
	private int killMoney, killScore;

	private int physicsR;

	/**
	 * ����
	 */
	private float moveSpeed;

	/**
	 * �����ٶȵı���,һ��ʼΪ1
	 */
	private float reduceSpeedN;

	/**
	 * ��Ϸ���٣�ȡֵΪ1,2����Ϊ2��ʱ��������.��ʼֵΪ1
	 */
	private int accelerate;

	/**
	 * ���ٵ�ʱ��
	 */
	private long reduceSpeedTime;

	private Bitmap monsterMap;
	private Bitmap monsterMove;
	private Bitmap monsterDead;

	private Context context;

	/**
	 * �������ͼƬ�����Ͻǵ�����
	 */
	private float monsterX, monsterY;

	/**
	 * �����Ź�������ͼƬ�����Ͻǵ�����
	 */
	private float deathX, deathY;

	/* ��¼�����ߵ���һ�� */
	private int moveI;

	/**
	 * ��¼�����Ƿ����
	 */
	private boolean isAlive = true;

	/* �̱߳�� */
	private boolean threadFlag;

	/* ��¼ͼƬ���Ƶ���һ�� */
	private int monsterMoveIndex;

	private static GameMap gameMap;

	/**
	 * �������ײ�뾶,Ĭ�Ϲ������ײ����ΪԲ��
	 */
	private float collisionRadius;

	/* �������ߵ�·������ */
	private static float[][] monsterRoute;

	/**
	 * ע�⣺��������ʱ,Ҫ��ʵ����gameMap��̬��Ա����
	 * 
	 * @param context
	 *            ������
	 * @param id
	 *            �Ѷȼ�
	 * @param number
	 *            ��¼�ǵڼ��ֹ���(0��ʼ,���Գ���13)
	 */
	public Monster(Context context, int difficulty, int number)
	{
		this.context = context;

		totalHP = getHPValue(difficulty, number);
		hp = totalHP;

		killMoney = getEachWaveMoney(number);
		killScore = getEachWaveScore(number);
		physicsR = getPhysicsResistance(number);
		
		number = number % 14;
		
		iceR = getIceResistance(number);
		fireR = getFireResistance(number);
		toxinR = getToxinResistance(number);
		moveSpeed = getMoveSpeed(number);
		reduceSpeedN = 1;
		accelerate = 1;

		try
		{
			if (gameMap == null)
			{
				throw new Exception("Monster���е�gameMap��̬��Ա����ûʵ����~");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		initBitmap(number);

		collisionRadius = gameMap.getFrameW() / 8;
	}

	private void initBitmap(int number)
	{
		monsterMap = BitmapFactory.decodeResource(context.getResources(),
				monsterMapValue[number]);
		monsterMove = BitmapFactory.decodeResource(context.getResources(),
				monsterMoveValue[number]);
		monsterDead = BitmapFactory.decodeResource(context.getResources(),
				monsterDeadpValue[number]);

		if (gameMap != null)
		{
			/*
			 * monsterMap = BitmapUtils.changeBitmapSize(monsterMap, frameW,
			 * frameH);
			 */
			monsterMove = BitmapUtils.changeBitmapSize(monsterMove,
					gameMap.getFrameW() * 2, gameMap.getFrameH());
			monsterDead = BitmapUtils.changeBitmapSize(monsterDead,
					gameMap.getFrameW(), gameMap.getFrameH());

		}

	}

	/**
	 * ������Ϸ����,��������ƶ��ٶȿ���һ��
	 * 
	 * @param a
	 *            true�������
	 */
	public void isAccelerate(boolean a)
	{
		if (a)
		{
			accelerate = 2;
		}
		else
		{
			accelerate = 1;
		}
	}

	/**
	 * �����ײ�뾶
	 * 
	 * @return
	 */
	public float getCollisionRadius()
	{
		return collisionRadius;
	}

	/**
	 * ��û�ɱ���,ֻ�ܻ�ȡһ��,�˺�Ͱѻ�ɱ�������Ϊ0
	 * 
	 * @return
	 */
	public int getKillMoney()
	{
		if (killMoney == 0)
		{
			return 0;
		}
		else
		{
			int kMoney = killMoney;
			killMoney = 0;
			return kMoney;
		}

	}

	/**
	 * ��û�ɱ����,ֻ�ܻ�ȡһ��,�˺�Ͱѻ�ɱ��������Ϊ0
	 * 
	 * @return
	 */
	public int getKillScore()
	{
		if (killScore == 0)
		{
			return 0;
		}
		else
		{
			int kScore = killScore;
			killScore = 0;
			return kScore;
		}

	}

	public int getHp()
	{
		return hp;
	}

	/**
	 * ���ʣ��Ѫ������Ѫ���ı�ֵ,ȡֵ0~1
	 * 
	 * @return
	 */
	public float getSurplusHpRatio()
	{
		return (float) ((hp * 1.0) / totalHP);
	}

	/**
	 * ���ù��������ֵ<br>
	 * 
	 * @param damage
	 *            �������ܵĹ����˺�ֵ
	 */
	public void setHpInAttack(int damage)
	{
		if (damage > 0)
		{
			hp = hp - damage + physicsR;

			isMonsterDead();
		}
	}

	/**
	 * ���ù��������ֵ<br>
	 * 
	 * @param damage
	 *            �������ܵ� �� �˺�ֵ
	 */
	public void setHpInToxin(int damage)
	{
		if (!toxinR && damage > 0)
		{
			hp = hp - damage;
			isMonsterDead();
		}

	}

	/**
	 * ���ù��������ֵ<br>
	 * 
	 * @param damage
	 *            �������ܵ� �� �˺�ֵ
	 */
	public void setHpInFire(int damage)
	{
		if (!fireR && damage > 0)
		{
			hp = hp - damage;
			isMonsterDead();
		}

	}

	/**
	 * ���ù��������ֵ<br>
	 * 
	 * @param damage
	 *            �������ܵĹ����˺�ֵ
	 */
	public void setHpInIce(int damage, int iceLevel)
	{
		if (!iceR && damage > 0)
		{
			hp = hp - damage;
			isMonsterDead();

		}

		if (!iceR && iceLevel > 0)
		{
			reduceSpeedN = iceLevel / 2 + 2;
			reduceSpeedTime = System.currentTimeMillis();
		}

	}

	/**
	 * ���ù��������ֵ<br>
	 * 
	 * @param damage
	 *            �������ܵ� ���� �˺�ֵ
	 */
	public void setHpInSpecial(int damage)
	{
		if (damage > 0)
		{
			hp = hp - damage;
			isMonsterDead();
		}
	}

	private void isMonsterDead()
	{
		if (hp <= 0)
		{
			isAlive = false;

			/* ��¼������λ��,��������ͼƬ�����ƶ� */
			deathX = monsterX;
			deathY = monsterY;
		}
	}

	public float getDeathX()
	{
		return deathX;
	}

	public float getDeathY()
	{
		return deathY;
	}

	public boolean isAlive()
	{
		return isAlive;
	}

	public Bitmap getMonsterMap()
	{
		return monsterMap;
	}

	public Bitmap getMonsterMove()
	{
		return monsterMove;
	}

	public Bitmap getMonsterDead()
	{
		return monsterDead;
	}

	public void setThreadFlag(boolean threadFlag)
	{
		this.threadFlag = threadFlag;
	}

	/**
	 * �����߳���while���ı�־,���Ա��ഴ��һ���߳�,�������߳�
	 * 
	 * @param threadFlag
	 *            while����������־
	 */
	public void startThread(boolean threadFlag)
	{
		this.threadFlag = threadFlag;

		Thread thread = new Thread(this);
		thread.start();
	}

	public float getMonsterX()
	{
		return monsterX;
	}

	public float getMonsterY()
	{
		return monsterY;
	}

	/**
	 * ʵ����GameMap����ʵ����monsterRoute
	 * 
	 * @param gameMap
	 */
	public static void setGameMap(GameMap gameMap)
	{
		Monster.gameMap = gameMap;
		Monster.monsterRoute = GameMap.getRoute(gameMap.getMapId(),
				gameMap.getFrameW(), gameMap.getFrameH());
	}

	public int getMonsterMoveIndex()
	{
		if (monsterMoveIndex == 19)
		{
			monsterMoveIndex = 0;
		}
		else
		{
			monsterMoveIndex++;
		}

		return -monsterMoveIndex / 10;
	}

	@Override
	public void run()
	{
		float lengthW = gameMap.getFrameW() / Multiple;
		float lengthH = gameMap.getFrameH() / Multiple;

		while (threadFlag)
		{
			float differX = monsterRoute[moveI][0] - monsterRoute[moveI + 1][0];
			float differY = monsterRoute[moveI][1] - monsterRoute[moveI + 1][1];

			int times = 0;

			if (differX == 0)
			{
				times = (int) (Math.abs(differY) / lengthH);
			}
			else
			{
				times = (int) (Math.abs(differX) / lengthW);
			}

			for (int i = 0; i < times; i++)
			{
				monsterX = monsterRoute[moveI][0] - (differX / times) * i;
				monsterY = monsterRoute[moveI][1] - (differY / times) * i;

				long time = System.currentTimeMillis();

				if (time - reduceSpeedTime > 3000)
				{
					reduceSpeedN = 1;
				}

				try
				{
					Thread.sleep((long) (sleepTime / (moveSpeed / (reduceSpeedN / accelerate))));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			if (moveI < monsterRoute.length - 2)
			{
				moveI++;
			}

		}
	}

}
