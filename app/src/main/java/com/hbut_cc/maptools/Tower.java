package com.hbut_cc.maptools;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hbut_cc.utils.BitmapUtils;

/**
 * 防御塔类
 * 
 * @author c.c
 *
 */
public class Tower extends TowerData implements Runnable
{
	/**
	 * 目前所有的怪物集合
	 */
	private static ArrayList<Monster> monsters;

	/**
	 * 保存着本防御塔可以攻击到的怪物
	 * <p>
	 */
	private ArrayList<Monster> canAMonsters;

	/**
	 * 是否要画攻击特效。<br>
	 * 每次获取在攻击范围内的怪物时,把它设为true;<br>
	 * 每次画了攻击特效后把它设为false
	 */
	private boolean isPaintBP;

	/* 存放防御塔图片 */
	private Bitmap towerBitmap;

	private Context context;

	/* 防御塔左上角的坐标 */
	private float towerX, towerY;

	private float frameW, frameH;

	/* 攻击力 */
	private int attackValue;

	/**
	 * 攻击时间间隔
	 */
	private int attackTime;

	/**
	 * 游戏加速，取值为1,2；当为2的时候代表加速.初始值为1
	 */
	private int accelerate;

	/* 主动攻击范围 */
	private float attackRange;

	/* run()方法中的while语句的条件 */
	private boolean threadFlag;

	/**
	 * 攻击图片
	 */
	private Bitmap attackBitmap;

	/**
	 * 是否画攻击范围,在GameMapView中调用
	 */
	private boolean isPaintAttackR;

	/**
	 * 攻击特效index,用来轮换一张图片上的位置
	 */
	private int buffPlacerIndex;

	/**
	 * 攻击特效Clip区域的index,从4开始
	 */
	private int BPClipIndex = 4;

	/* 保存技能等级数据 */
	private SkillsValue skillsValue;

	public Tower(Context context, int pictureId, int towerType)
	{
		this.context = context;
		towerBitmap = BitmapFactory.decodeResource(context.getResources(),
				pictureId);

		this.skillsValue = getInitSkillsValue(towerType);

		attackBitmap = BitmapFactory.decodeResource(context.getResources(),
				skillsValue.getAttackBitmapId());

		attackTime = getAttackTime(towerType);
		accelerate = 1;

		canAMonsters = new ArrayList<Monster>();

		setAttackValue();
	}

	/**
	 * 设置frameW和frameH，并改变防御塔图片的大小为frameW×frameH<br>
	 * 并设置attackRange的大小,和改变attackRBitmap的大小<br>
	 * 并改变attackBitmap的大小
	 * 
	 * @param frameW
	 * @param frameH
	 */
	public void setFrameWAndFrameH(float frameW, float frameH)
	{
		this.frameW = frameW;
		this.frameH = frameH;

		towerBitmap = BitmapUtils.changeBitmapSize(towerBitmap, frameW, frameH);

		attackRange = getAttackRFrameWTimes(skillsValue) * frameW;

		if (skillsValue.getTowerType() == Tower.TOWER3)
		{
			attackBitmap = BitmapUtils.changeBitmapSize(attackBitmap,
					attackRange * 2 * 8, attackRange * 2);
		}
		else
		{
			attackBitmap = BitmapUtils.changeBitmapSize(attackBitmap,
					frameW * 4, frameH / 2);
		}
	}

	/**
	 * 获得该防御塔能攻击到的怪物 的List
	 * 
	 * @return
	 */
	public ArrayList<Monster> getCanAMonsters()
	{
		return canAMonsters;
	}

	public static void setMonsters(ArrayList<Monster> monsters)
	{
		Tower.monsters = monsters;
	}

	public boolean isPaintBP()
	{
		return isPaintBP;
	}

	public void setPaintBP(boolean isPaintBP)
	{
		this.isPaintBP = isPaintBP;
	}

	public SkillsValue getSkillsValue()
	{
		return skillsValue;
	}
	
	public float getTowerX()
	{
		return towerX;
	}

	public void setTowerX(float towerX)
	{
		this.towerX = towerX;
	}

	public float getTowerY()
	{
		return towerY;
	}

	public void setTowerY(float towerY)
	{
		this.towerY = towerY;
	}

	public float getAttackRange()
	{
		return attackRange;
	}

	public boolean isPaintAttackR()
	{
		return isPaintAttackR;
	}

	public void setPaintAttackR(boolean isPaintAttackR)
	{
		this.isPaintAttackR = isPaintAttackR;
	}

	public Bitmap getAttackBitmap()
	{
		return attackBitmap;
	}

	public void setThreadFlag(boolean threadFlag)
	{
		this.threadFlag = threadFlag;
	}

	/**
	 * 游戏是否加速，加速即攻击时间间隔减半
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

	/**
	 * 从SkillsValue获得该防御塔的攻击力,并加上其10%的random值
	 * 
	 */
	private void setAttackValue()
	{
		int attack = skillsValue.getTowerAttackValue();
		Random random = new Random();

		if (attack / 10 > 0)
		{
			attackValue = attack + random.nextInt(attack / 10) + 1;
		}

	}

	public int getAttackValue()
	{
		return attackValue;
	}

	/**
	 * 设置防御塔的图片,并修改图片的大小
	 * 
	 * @param pictureId
	 */
	public void setTowerBitmap(int pictureId)
	{
		towerBitmap = BitmapFactory.decodeResource(context.getResources(),
				pictureId);
		towerBitmap = BitmapUtils.changeBitmapSize(towerBitmap, frameW, frameH);
	}

	public Bitmap getTowerBitmap()
	{
		return towerBitmap;
	}

	/**
	 * 升级attack的等级,并把tower的bitmap换成相应的图片<br>
	 * 并升级攻击范围,调整攻击范围图片的大小<br>
	 * 并升级 防御塔3 攻击特效 范围<br>
	 * 升级防御塔的攻击力
	 */
	public void upgradeAttackLevel()
	{
		skillsValue.increaseAttackLevel();
		setTowerBitmap(skillsValue.getTowerBitmapId());

		attackRange = getAttackRFrameWTimes(skillsValue) * frameW;

		if (skillsValue.getTowerType() == Tower.TOWER3)
		{
			attackBitmap = BitmapUtils.changeBitmapSize(attackBitmap,
					attackRange * 2 * 8, attackRange * 2);
		}

		setAttackValue();
	}

	/**
	 * 升级SpecialLevel,并把tower的bitmap换成相应的图片
	 */
	public void upgradeSpecialLevel()
	{
		skillsValue.increaseSpecialLevel();
		setTowerBitmap(skillsValue.getTowerBitmapId());

	}

	/**
	 * 获取防御塔的攻击特效显示指示<br>
	 * 减少受到攻击怪物的血量
	 * 
	 * @return 攻击特效显示指示(0~7)
	 */
	public int getBuffPlacerIndex()
	{
		if (buffPlacerIndex == 15)
		{
			/* 当一次攻击完成后,把值恢复到初始值 */
			buffPlacerIndex = 0;
			BPClipIndex = 4;

			/* 一次攻击特效完成后不再继续画，等待下一次判断是否有怪物在攻击范围内 */
			isPaintBP = false;

			if (skillsValue.getTowerType() != Tower.TOWER3)
			{
				canAMonsters.get(0).setHpInAttack(attackValue);
				canAMonsters.get(0).setHpInFire(skillsValue.getFireDamage());
				canAMonsters.get(0).setHpInIce(skillsValue.getIceDamage(),
						skillsValue.getIceLevel());
				canAMonsters.get(0).setHpInSpecial(
						skillsValue.getSpecialDamage());
				canAMonsters.get(0).setHpInToxin(skillsValue.getToxinDmage());
			}
			else
			{
				for (int i = 0; i < canAMonsters.size(); i++)
				{
					/* 减少受到伤害的怪物的血量 */
					canAMonsters.get(i).setHpInAttack(attackValue);
					canAMonsters.get(i).setHpInToxin(
							skillsValue.getToxinDmage());
					canAMonsters.get(i).setHpInSpecial(
							skillsValue.getSpecialDamage());
				}
			}
		}
		else
		{
			buffPlacerIndex++;
		}

		return -buffPlacerIndex / 2;
	}

	/**
	 * 判断是否有怪物进入了该防御塔的攻击范围,有则往canAMonsters中添加该怪物塔对象
	 */
	private void isInAttackRange()
	{
		canAMonsters.clear();

		for (int i = 0; i < monsters.size(); i++)
		{
			/* 怪物图片左上角的坐标 */
			float mX = monsters.get(i).getMonsterX();
			float mY = monsters.get(i).getMonsterY();

			/* 当怪物没死的时候 */
			if (monsters.get(i).getHp() > 0)
			{
				/* 如果怪物图片左上角与防御塔左上角的距离 小于 防御塔与怪物的碰撞半径的和 */
				if (Math.sqrt((towerX - mX) * (towerX - mX) + (towerY - mY)
						* (towerY - mY)) < (attackRange + monsters.get(i)
						.getCollisionRadius()))
				{
					canAMonsters.add(monsters.get(i));
				}
			}

		}

	}

	/**
	 * 获得攻击特效在x轴方向上每次移动的距离
	 * <p>
	 * 获得的值为xM-xT所得(有正负)
	 * 
	 * @param xT
	 *            防御塔图片左上角的x坐标
	 * @param xM
	 *            怪物图片左上角的x坐标
	 * @return
	 */
	public float getBPMoveInXAxis(float xT, float xM)
	{
		return ((xM - xT) / 5) * getBPClipIndex();
	}

	/**
	 * 获得攻击特效在y轴方向上每次移动的距离
	 * <p>
	 * 获得的值为yM-yT所得(有正负)
	 * 
	 * @param yT
	 *            防御塔图片左上角的x坐标
	 * @param yM
	 *            怪物图片左上角的x坐标
	 * @return
	 */
	public float getBPMoveInYAxis(float yT, float yM)
	{
		return ((yM - yT) / 5) * getBPClipIndex();
	}

	private int getBPClipIndex()
	{
		if (BPClipIndex == 23)
		{
			BPClipIndex = 23;
		}
		else
		{
			BPClipIndex++;
		}
		return BPClipIndex / 4;
	}

	@Override
	public void run()
	{
		while (threadFlag)
		{
			isInAttackRange();

			isPaintBP = true;

			try
			{
				Thread.sleep(attackTime / accelerate);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
