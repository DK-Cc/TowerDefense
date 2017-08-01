package com.hbut_cc.maptools;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hbut_cc.utils.BitmapUtils;

/**
 * ��������
 * 
 * @author c.c
 *
 */
public class Tower extends TowerData implements Runnable
{
	/**
	 * Ŀǰ���еĹ��Ｏ��
	 */
	private static ArrayList<Monster> monsters;

	/**
	 * �����ű����������Թ������Ĺ���
	 * <p>
	 */
	private ArrayList<Monster> canAMonsters;

	/**
	 * �Ƿ�Ҫ��������Ч��<br>
	 * ÿ�λ�ȡ�ڹ�����Χ�ڵĹ���ʱ,������Ϊtrue;<br>
	 * ÿ�λ��˹�����Ч�������Ϊfalse
	 */
	private boolean isPaintBP;

	/* ��ŷ�����ͼƬ */
	private Bitmap towerBitmap;

	private Context context;

	/* ���������Ͻǵ����� */
	private float towerX, towerY;

	private float frameW, frameH;

	/* ������ */
	private int attackValue;

	/**
	 * ����ʱ����
	 */
	private int attackTime;

	/**
	 * ��Ϸ���٣�ȡֵΪ1,2����Ϊ2��ʱ��������.��ʼֵΪ1
	 */
	private int accelerate;

	/* ����������Χ */
	private float attackRange;

	/* run()�����е�while�������� */
	private boolean threadFlag;

	/**
	 * ����ͼƬ
	 */
	private Bitmap attackBitmap;

	/**
	 * �Ƿ񻭹�����Χ,��GameMapView�е���
	 */
	private boolean isPaintAttackR;

	/**
	 * ������Чindex,�����ֻ�һ��ͼƬ�ϵ�λ��
	 */
	private int buffPlacerIndex;

	/**
	 * ������ЧClip�����index,��4��ʼ
	 */
	private int BPClipIndex = 4;

	/* ���漼�ܵȼ����� */
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
	 * ����frameW��frameH�����ı������ͼƬ�Ĵ�СΪframeW��frameH<br>
	 * ������attackRange�Ĵ�С,�͸ı�attackRBitmap�Ĵ�С<br>
	 * ���ı�attackBitmap�Ĵ�С
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
	 * ��ø÷������ܹ������Ĺ��� ��List
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
	 * ��Ϸ�Ƿ���٣����ټ�����ʱ��������
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

	/**
	 * ��SkillsValue��ø÷������Ĺ�����,��������10%��randomֵ
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
	 * ���÷�������ͼƬ,���޸�ͼƬ�Ĵ�С
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
	 * ����attack�ĵȼ�,����tower��bitmap������Ӧ��ͼƬ<br>
	 * ������������Χ,����������ΧͼƬ�Ĵ�С<br>
	 * ������ ������3 ������Ч ��Χ<br>
	 * �����������Ĺ�����
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
	 * ����SpecialLevel,����tower��bitmap������Ӧ��ͼƬ
	 */
	public void upgradeSpecialLevel()
	{
		skillsValue.increaseSpecialLevel();
		setTowerBitmap(skillsValue.getTowerBitmapId());

	}

	/**
	 * ��ȡ�������Ĺ�����Ч��ʾָʾ<br>
	 * �����ܵ����������Ѫ��
	 * 
	 * @return ������Ч��ʾָʾ(0~7)
	 */
	public int getBuffPlacerIndex()
	{
		if (buffPlacerIndex == 15)
		{
			/* ��һ�ι�����ɺ�,��ֵ�ָ�����ʼֵ */
			buffPlacerIndex = 0;
			BPClipIndex = 4;

			/* һ�ι�����Ч��ɺ��ټ��������ȴ���һ���ж��Ƿ��й����ڹ�����Χ�� */
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
					/* �����ܵ��˺��Ĺ����Ѫ�� */
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
	 * �ж��Ƿ��й�������˸÷������Ĺ�����Χ,������canAMonsters����Ӹù���������
	 */
	private void isInAttackRange()
	{
		canAMonsters.clear();

		for (int i = 0; i < monsters.size(); i++)
		{
			/* ����ͼƬ���Ͻǵ����� */
			float mX = monsters.get(i).getMonsterX();
			float mY = monsters.get(i).getMonsterY();

			/* ������û����ʱ�� */
			if (monsters.get(i).getHp() > 0)
			{
				/* �������ͼƬ���Ͻ�����������Ͻǵľ��� С�� ��������������ײ�뾶�ĺ� */
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
	 * ��ù�����Ч��x�᷽����ÿ���ƶ��ľ���
	 * <p>
	 * ��õ�ֵΪxM-xT����(������)
	 * 
	 * @param xT
	 *            ������ͼƬ���Ͻǵ�x����
	 * @param xM
	 *            ����ͼƬ���Ͻǵ�x����
	 * @return
	 */
	public float getBPMoveInXAxis(float xT, float xM)
	{
		return ((xM - xT) / 5) * getBPClipIndex();
	}

	/**
	 * ��ù�����Ч��y�᷽����ÿ���ƶ��ľ���
	 * <p>
	 * ��õ�ֵΪyM-yT����(������)
	 * 
	 * @param yT
	 *            ������ͼƬ���Ͻǵ�x����
	 * @param yM
	 *            ����ͼƬ���Ͻǵ�x����
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
