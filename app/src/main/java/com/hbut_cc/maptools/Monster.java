package com.hbut_cc.maptools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hbut_cc.utils.BitmapUtils;

public class Monster extends MonsterData implements Runnable
{
	/* 把每格分成多少步数去走 */
	private static final int Multiple = 16;

	private static final int sleepTime = 100;

	/* 生命值 */
	private int hp;
	/* 总生命值 */
	private int totalHP;

	/**
	 * 冰抗,毒抗,火抗,物抗
	 */
	private boolean iceR, toxinR, fireR;

	/**
	 * 杀死后所获得的金币,杀死后所获得的分数
	 */
	private int killMoney, killScore;

	private int physicsR;

	/**
	 * 移速
	 */
	private float moveSpeed;

	/**
	 * 减少速度的倍数,一开始为1
	 */
	private float reduceSpeedN;

	/**
	 * 游戏加速，取值为1,2；当为2的时候代表加速.初始值为1
	 */
	private int accelerate;

	/**
	 * 减速的时间
	 */
	private long reduceSpeedTime;

	private Bitmap monsterMap;
	private Bitmap monsterMove;
	private Bitmap monsterDead;

	private Context context;

	/**
	 * 保存怪物图片的左上角的坐标
	 */
	private float monsterX, monsterY;

	/**
	 * 保存着怪物死亡图片的左上角的坐标
	 */
	private float deathX, deathY;

	/* 记录怪物走到哪一步 */
	private int moveI;

	/**
	 * 记录怪物是否活着
	 */
	private boolean isAlive = true;

	/* 线程标记 */
	private boolean threadFlag;

	/* 记录图片是移到哪一个 */
	private int monsterMoveIndex;

	private static GameMap gameMap;

	/**
	 * 怪物的碰撞半径,默认怪物的碰撞区域为圆形
	 */
	private float collisionRadius;

	/* 怪物行走的路径数组 */
	private static float[][] monsterRoute;

	/**
	 * 注意：创建本类时,要先实例化gameMap静态成员变量
	 * 
	 * @param context
	 *            上下文
	 * @param id
	 *            难度几
	 * @param number
	 *            记录是第几种怪物(0开始,可以超过13)
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
				throw new Exception("Monster类中的gameMap静态成员变量没实例化~");
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
	 * 进行游戏加速,即怪物的移动速度快了一倍
	 * 
	 * @param a
	 *            true代表加速
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
	 * 获得碰撞半径
	 * 
	 * @return
	 */
	public float getCollisionRadius()
	{
		return collisionRadius;
	}

	/**
	 * 获得击杀金币,只能获取一次,此后就把击杀金币设置为0
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
	 * 获得击杀分数,只能获取一次,此后就把击杀分数设置为0
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
	 * 获得剩余血量和总血量的比值,取值0~1
	 * 
	 * @return
	 */
	public float getSurplusHpRatio()
	{
		return (float) ((hp * 1.0) / totalHP);
	}

	/**
	 * 设置怪物的生命值<br>
	 * 
	 * @param damage
	 *            怪物所受的攻击伤害值
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
	 * 设置怪物的生命值<br>
	 * 
	 * @param damage
	 *            怪物所受的 毒 伤害值
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
	 * 设置怪物的生命值<br>
	 * 
	 * @param damage
	 *            怪物所受的 火 伤害值
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
	 * 设置怪物的生命值<br>
	 * 
	 * @param damage
	 *            怪物所受的攻击伤害值
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
	 * 设置怪物的生命值<br>
	 * 
	 * @param damage
	 *            怪物所受的 特殊 伤害值
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

			/* 记录死亡的位置,让死亡的图片不再移动 */
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
	 * 传入线程中while语句的标志,并以本类创建一个线程,并启动线程
	 * 
	 * @param threadFlag
	 *            while语句的条件标志
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
	 * 实例化GameMap，并实例化monsterRoute
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
