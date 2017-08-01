package com.hbut_cc.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.hbut_cc.maptools.GameMap;
import com.hbut_cc.maptools.Monster;
import com.hbut_cc.maptools.SkillsValue;
import com.hbut_cc.maptools.Tower;
import com.hbut_cc.towerdefense.R;
import com.hbut_cc.utils.BitmapUtils;
import com.hbut_cc.utils.DBManager;
import com.hbut_cc.utils.DBMap;
import com.hbut_cc.utils.DBTower;
import com.hbut_cc.utils.MySharedPreferences;
import com.hbut_cc.utils.PaintBlood;
import com.hbut_cc.utils.ToastUtils;

public class GameMapView extends SurfaceView implements Callback, Runnable
{

	public static final String TAG = "TowerDefense";

	public static final int RefreshTime = 30;

	/**
	 * ÿ������ĸ���
	 */
	public static final int eachLevelMonster = 20;

	/**
	 * ����������
	 */
	private static final int maxTowerNumbers = 15;

	/**
	 * ����ģʽ�����
	 */
	private int NormalMaxWave = 14;

	private Context context;

	private SurfaceHolder surfaceHolder;

	private Canvas canvas;

	private Paint paint, paintGrid, paintCircle, paintRect;

	private PaintBlood paintBlood;

	/**
	 * ��Ϸ�Ѷȣ�0,1,2
	 */
	private int gameDifficulty;

	/**
	 * ��Ϸ��ͼ����0��ʼ
	 */
	private int selectedMap;

	/**
	 * ��Ϸģʽ,0��������ģʽ,1��������ģʽ
	 */
	private int gameMode;

	/**
	 * ���ݿ���DBMap�Ĵ�����
	 */
	private int dbMapNumber;

	/**
	 * ��һ��DBMap��mapid
	 */
	private String firstDBMapId;

	/**
	 * ��Ϸ�Ƿ����,falseΪ������
	 */
	private boolean isAccelerate;

	/**
	 * dollar���ŵ�ͼƬ��������ʾ��ǰ�ж���Ǯ;carrotͼƬ��������ʾ��ǰ������ֵ
	 */
	private Bitmap dollar, carrot;

	/**
	 * ���н�Ǯ��ʣ��������ֵ
	 */
	private int dollarValue, carrotValue;

	/**
	 * ��Ϸ�õķ���
	 */
	private int score;

	/**
	 * ��¼�ڼ�������,0�����һ��
	 */
	private int level;

	/**
	 * ��Ϸ��ͼ��R�ļ��е�id
	 */
	private int mapId;

	/**
	 * ��Ļ�Ŀ�͸�
	 */
	private int screenW, screenH;

	/**
	 * �������Ĵ�С
	 */
	private float frameW, frameH;

	private GameMap gameMap;

	/**
	 * ��¼�ܷ������ĵط�,0�����ܷ�,1�����ܷ�; �����MapData.java�е�getTowerPosition()����
	 */
	private int[][] towerPosition;

	private Bitmap grid;

	/**
	 * �߳��Ƿ�����־
	 */
	private boolean threadFlag = true;

	/** ��Ϸ�Ƿ����,true������� */
	private boolean isGameOver = false;

	/** �Ƿ�ֹͣ��ѯFrameW�Ƿ�ֵ,��Ϊtrueʱ�����ǵ� */
	private boolean isStopQuery = false;

	/**
	 * �Ƿ�grid
	 */
	private boolean isPaintGrid;

	/**
	 * �Ƿ�ʼ��һ��
	 */
	private boolean isBeginNextWave;

	/** �Ƿ���ͣ��Ϸ,trueʱ������ͣ */
	private boolean isStopGame = false;

	/**
	 * ���еĹ��Ƿ����
	 */
	private boolean isFullMoster;

	private ArrayList<Monster> monsters;

	/**
	 * �������������Ϣ
	 */
	private ArrayList<Tower> towers;

	public GameMapView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		init(context);
	}

	public GameMapView(Context context)
	{
		super(context);

		init(context);
	}

	private void init(Context context)
	{
		this.context = context;

		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);

		/* �ӵ�һ����ʼ */
		level = 0;
		/* ��ʼʱ����ֵΪ50 */
		carrotValue = 50;
		/* ��ʼ����Ϊ0 */
		score = 0;
		/* ����ʼ��һ�� */
		isBeginNextWave = false;

		initData();
	}

	/**
	 * �ô����ݿ��л�õ�������������Ϸ
	 */
	public void initIntentData(DBMap dbMap, ArrayList<DBTower> dbTowers,
			int dbMapNumber, String firstDBMapId)
	{

		this.dbMapNumber = dbMapNumber;
		this.firstDBMapId = firstDBMapId;

		int selectedMap = dbMap.getMapname().charAt(3) - '0';
		selectedMap = selectedMap - 1;

		level = dbMap.getMonsterWave() - 1;

		initIntentData(selectedMap, dbMap.getGamedifficulty(),
				dbMap.getGamemode());

		carrotValue = dbMap.getResiduallife();
		level = dbMap.getMonsterWave() - 1;
		score = dbMap.getScore();
		dollarValue = dbMap.getMoney();

		for (int i = 0; i < dbTowers.size(); i++)
		{
			addGameTower(dbTowers.get(i));
		}

	}

	/**
	 * 
	 * @param selectedMap
	 * @param gameDifficulty
	 * @param gameMode
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void initIntentData(int selectedMap, int gameDifficulty, int gameMode)
	{
		this.selectedMap = selectedMap;
		this.gameDifficulty = gameDifficulty;
		this.gameMode = gameMode;

		if (selectedMap == 0)
		{
			mapId = R.drawable.map1;
		}
		else if (selectedMap == 1)
		{
			mapId = R.drawable.map2;
		}
		else if (selectedMap == 2)
		{
			mapId = R.drawable.map3;
		}
		else if (selectedMap == 3)
		{
			mapId = R.drawable.map4;
		}
		else if (selectedMap == 4)
		{
			mapId = R.drawable.map5;
		}
		else
		{
			mapId = R.drawable.map6;
		}

		if (gameDifficulty == 0)
		{
			dollarValue = 50;
		}
		else if (gameDifficulty == 1)
		{
			dollarValue = 100;
		}
		else
		{
			dollarValue = 200;
		}

		if (gameMode == 0)
		{
			NormalMaxWave = 14;
		}
		else
		{
			NormalMaxWave = Integer.MAX_VALUE;
		}

		gameMap = new GameMap(getContext(), mapId);

		towerPosition = gameMap.getTowerPosition(mapId);

		gameMap.setScreenWHAndFramenWH(screenW, screenH, frameW, frameH);

		grid = BitmapUtils.changeBitmapSize(grid, screenW, screenH);

		/* �ѻ�ͼ�߳��Ƿ����ı�־����Ϊtrue */
		threadFlag = true;

		monsters = new ArrayList<Monster>();
		Monster.setGameMap(gameMap);

		/* ��ArrayList<Monster>����ӵ�һ��monster,��������monster���߳� */
		Monster monster = new Monster(context, gameDifficulty, level);
		monsters.add(monster);
		monster.startThread(threadFlag);

		/* ��������������ӹ��Ｏ�� */
		Tower.setMonsters(monsters);

		Thread thread = new Thread(this);
		thread.start();

	}

	private void initData()
	{
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);

		paintGrid = new Paint();
		paintGrid.setStyle(Paint.Style.STROKE);
		paintGrid.setAlpha(95); // ���ð�͸��
		paintGrid.setAntiAlias(true);

		paintCircle = new Paint();
		paintCircle.setColor(Color.MAGENTA);
		paintCircle.setStyle(Paint.Style.STROKE);
		paintCircle.setStrokeWidth(2);
		paintGrid.setAlpha(50);
		paintCircle.setAntiAlias(true);

		paintRect = new Paint();
		paintRect.setColor(Color.GRAY);
		paintRect.setStyle(Paint.Style.STROKE);
		paintRect.setStrokeWidth(3);
		paintRect.setAntiAlias(true);

		/* �Զ����Paint */
		paintBlood = new PaintBlood();
		paintBlood.setAntiAlias(true);

		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/hanyixue.ttf");
		paint.setTypeface(face);
		paint.setTextSize(30);

		grid = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.grid);

		dollar = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.dollar);
		dollar = BitmapUtils.changeBitmapSize(dollar, 30, 40);
		carrot = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.carrot);
		carrot = BitmapUtils.changeBitmapSize(carrot, 30, 40);

		/* ����һ��tower��ArrayList */
		towers = new ArrayList<Tower>();
	}

	public boolean isGameOver()
	{
		return isGameOver;
	}

	public boolean isStopGame()
	{
		return isStopGame;
	}

	public void setStopGame(boolean isStopGame)
	{
		this.isStopGame = isStopGame;
	}

	public boolean isThreadFlag()
	{
		return threadFlag;
	}

	public int getLevel()
	{
		return level;
	}

	public int getCarrotValue()
	{
		return carrotValue;
	}

	public float getFrameW()
	{
		return frameW;
	}

	public float getFrameH()
	{
		return frameH;
	}

	public int[][] getTowerPosition()
	{
		return towerPosition;
	}

	public boolean isStopQuery()
	{
		return isStopQuery;
	}

	public void setPaintGrid(boolean isPaintGrid)
	{
		this.isPaintGrid = isPaintGrid;
	}

	/**
	 * ��Ϸ�Ƿ����
	 * 
	 * @param a
	 *            true�������,false��������
	 */
	public void isAccelerate(boolean a)
	{
		isAccelerate = a;
	}

	/**
	 * ��ӷ�����
	 * 
	 * @param dbTower
	 */
	private void addGameTower(DBTower dbTower)
	{
		/* ע����DBHelper�ж����towernameΪ:tower1,tower2 */
		int left = dbTower.getCoordinateX();
		int top = dbTower.getCoordinateY();
		int towerType = (int) dbTower.getTowername().charAt(5) - (int) '0';
		int towerLevel = dbTower.getGrade();
		int skill = dbTower.getSkill();
		int skill1 = skill / 10;
		int skill0 = skill % 10;

		if (skill1 == 4)
		{
			towerPosition[top][left] = towerType * 10 + towerLevel + 5;
		}
		else
		{
			towerPosition[top][left] = towerType * 10 + towerLevel;
		}

		int pictureId = 0;

		if (towerType == 1)
		{
			pictureId = Tower.TowerId1[towerLevel - 1];
		}
		else if (towerType == 2)
		{
			pictureId = Tower.TowerId2[towerLevel - 1];
		}
		else if (towerType == 3)
		{
			pictureId = Tower.TowerId3[towerLevel - 1];
		}
		else
		{
			pictureId = Tower.TowerId4[towerLevel - 1];
		}

		Tower tower = new Tower(context, pictureId, towerType);
		tower.setTowerX(left * frameW);
		tower.setTowerY(top * frameH);
		tower.setFrameWAndFrameH(frameW, frameH);

		for (int i = 0; i < towerLevel - 1; i++)
		{
			tower.upgradeAttackLevel();
		}

		SkillsValue skillsValue = tower.getSkillsValue();

		/* �� */
		if (skill1 == 1)
		{
			for (int i = 0; i < skill0; i++)
			{
				skillsValue.increaseToxinLevel();
			}
		}
		else if (skill1 == 2)
		{/* �� */
			for (int i = 0; i < skill0; i++)
			{
				skillsValue.increaseFireLevel();
			}
		}
		else if (skill1 == 3)
		{/* �� */
			for (int i = 0; i < skill0; i++)
			{
				skillsValue.increaseIceLevel();
			}
		}
		else if (skill1 == 4)
		{/* �� */
			for (int i = 0; i < skill0; i++)
			{
				skillsValue.increaseSpecialLevel();
			}
		}

		tower.startThread(threadFlag);
		towers.add(tower);
	}

	/**
	 * 
	 * @param multipleLeft
	 *            ����ĺ��������frameW���õ�ֵ
	 * @param multipleTop
	 *            ��������������frameH���õ�ֵ
	 * @param towerType
	 *            tower�����ͣ���ȡֵΪ1~4,������TowerData���пɼ�
	 * @param pictureId
	 *            ������ͼƬ��R�ļ���Idֵ
	 * @param positionValue
	 *            λ��ֵ�������MapData.java�е�getTowerPosition()����
	 */
	public void addNewTower(int multipleLeft, int multipleTop, int towerType,
			int pictureId, int positionValue)
	{
		if (dollarValue - SkillsValue.InitialCost[towerType - 1] < 0)
		{
			ToastUtils.showToast(context, "�Բ�����Ľ�Ҳ���~");
		}
		else
		{
			dollarValue = dollarValue - SkillsValue.InitialCost[towerType - 1];
			if (towers.size() < maxTowerNumbers)
			{
				towerPosition[multipleTop][multipleLeft] = positionValue;

				Tower tower = new Tower(context, pictureId, towerType);
				tower.setTowerX(multipleLeft * frameW);
				tower.setTowerY(multipleTop * frameH);
				tower.setFrameWAndFrameH(frameW, frameH);

				tower.startThread(threadFlag);

				towers.add(tower);

				/* ������ʾ������Χ */
				paintOneAttackRange(tower);

			}
			else
			{
				ToastUtils.showToast(context, "���ֻ�ܷ���" + maxTowerNumbers
						+ "��������!");
			}
		}

	}

	/**
	 * ���ݵ����λ��ɾ��������
	 * 
	 * @param multipleLeft
	 *            ����ĺ��������frameW���õ�ֵ
	 * @param multipleTop
	 *            ��������������frameH���õ�ֵ
	 */
	public void deleteTower(int multipleLeft, int multipleTop)
	{
		for (int i = 0; i < towers.size(); i++)
		{
			if (towers.get(i).getTowerX() == multipleLeft * frameW
					&& towers.get(i).getTowerY() == multipleTop * frameH)
			{
				dollarValue = dollarValue
						+ towers.get(i).getSkillsValue().getSellMoney();

				towers.remove(i);

				towerPosition[multipleTop][multipleLeft] = 0;
				break;
			}
		}
	}

	/**
	 * ���ݵ������ı�������ȡ��Ӧ�ķ���������
	 * 
	 * @param multipleLeft
	 *            ����ĺ��������frameW���õ�ֵ
	 * @param multipleTop
	 *            ��������������frameH���õ�ֵ
	 * @return ��Ӧ�ķ���������
	 */
	public Tower getClickTower(int multipleLeft, int multipleTop)
	{
		for (int i = 0; i < towers.size(); i++)
		{
			if (towers.get(i).getTowerX() == multipleLeft * frameW
					&& towers.get(i).getTowerY() == multipleTop * frameH)
			{
				return towers.get(i);
			}
		}

		return null;
	}

	/**
	 * �ڽ���㹻�������,�����������Ĺ����ȼ�,������ʾ��Ҳ���������
	 * 
	 * @param tower
	 *            Ҫ�����ķ�����
	 */
	public void upgradeAttackLevel(Tower tower)
	{
		int uAttackM = tower.getSkillsValue().upgradeAttackMoney();
		if (dollarValue < uAttackM)
		{
			ToastUtils.showToast(context, "��Ҳ�����������~");
		}
		else
		{
			dollarValue = dollarValue - uAttackM;
			tower.upgradeAttackLevel();
		}
	}

	/**
	 * �ڽ���㹻�������,������������ ������ �ȼ�,������ʾ��Ҳ���������
	 * 
	 * @param tower
	 *            Ҫ�����ķ�����
	 */
	public void upgradeFireLevel(Tower tower)
	{
		int uFireM = tower.getSkillsValue().upgradeFireMoney();
		if (dollarValue < uFireM)
		{
			ToastUtils.showToast(context, "��Ҳ�����������~");
		}
		else
		{
			dollarValue = dollarValue - uFireM;
			tower.getSkillsValue().increaseFireLevel();
		}
	}

	/**
	 * �ڽ���㹻�������,������������ ������ �ȼ�,������ʾ��Ҳ���������
	 * 
	 * @param tower
	 *            Ҫ�����ķ�����
	 */
	public void upgradeIceLevel(Tower tower)
	{
		int uIceM = tower.getSkillsValue().upgradeIceMoney();
		if (dollarValue < uIceM)
		{
			ToastUtils.showToast(context, "��Ҳ�����������~");
		}
		else
		{
			dollarValue = dollarValue - uIceM;
			tower.getSkillsValue().increaseIceLevel();
		}
	}

	/**
	 * �ڽ���㹻�������,������������ ������ �ȼ�,������ʾ��Ҳ���������
	 * 
	 * @param tower
	 *            Ҫ�����ķ�����
	 */
	public void upgradeToxinLevel(Tower tower)
	{
		int uToxinM = tower.getSkillsValue().upgradeToxinMoney();
		if (dollarValue < uToxinM)
		{
			ToastUtils.showToast(context, "��Ҳ�����������~");
		}
		else
		{
			dollarValue = dollarValue - uToxinM;
			tower.getSkillsValue().increaseToxinLevel();
		}
	}

	/**
	 * �ڽ���㹻�������,������������ �������� ,������ʾ��Ҳ���������
	 * 
	 * @param tower
	 *            Ҫ�����ķ�����
	 */
	public void upgradeSepecialLevel(Tower tower)
	{
		int uSpecialM = tower.getSkillsValue().upgradeSpecialMoney();
		if (dollarValue < uSpecialM)
		{
			ToastUtils.showToast(context, "��Ҳ�����������~");
		}
		else
		{
			dollarValue = dollarValue - uSpecialM;
			tower.upgradeSpecialLevel();
		}
	}

	/**
	 * ���˲���tower���������� �Ƿ񻭹�����Χ ������Ϊ��
	 * 
	 * @param tower
	 */
	public void paintOneAttackRange(Tower tower)
	{
		doNotPaintAttackRange();
		tower.setPaintAttackR(true);
	}

	/**
	 * ���еķ����������� ������Χ
	 */
	public void doNotPaintAttackRange()
	{
		for (int i = 0; i < towers.size(); i++)
		{
			towers.get(i).setPaintAttackR(false);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		screenW = this.getWidth();
		screenH = this.getHeight();

		this.frameW = (float) (screenW / 8.0);
		this.frameH = (float) (screenH * 0.0758);

		isStopQuery = true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		threadFlag = false;
		for (int i = 0; i < monsters.size(); i++)
		{
			monsters.get(i).setThreadFlag(threadFlag);
		}

		for (int i = 0; i < towers.size(); i++)
		{
			towers.get(i).setThreadFlag(threadFlag);
		}

		if (carrotValue > 0)
		{
			DBManager dbManager = new DBManager(context);

			DBMap dbMap = new DBMap();
			ArrayList<DBTower> dbTowers = new ArrayList<DBTower>();

			if (dbMapNumber < DBManager.MAX_SAVE)
			{
			}
			else
			{
				dbManager.deleteTuple(firstDBMapId);
			}

			SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmm");
			Date curDate = new Date(System.currentTimeMillis());

			String mapid = format.format(curDate);

			dbMap.setMapid(mapid);
			dbMap.setMapname("map" + (selectedMap + 1));
			dbMap.setGamemode(gameMode);
			dbMap.setGamedifficulty(gameDifficulty);
			dbMap.setMonsterWave(level + 1);
			dbMap.setScore(score);
			dbMap.setResiduallife(carrotValue);
			dbMap.setMoney(dollarValue);

			int skill = 0;

			for (int i = 0; i < towers.size(); i++)
			{
				DBTower dbTower = new DBTower();
				dbTower.setCoordinateX((int) (towers.get(i).getTowerX() / frameW));
				dbTower.setCoordinateY((int) (towers.get(i).getTowerY() / frameH));
				dbTower.setMapid(mapid);
				dbTower.setTowername("tower"
						+ towers.get(i).getSkillsValue().getTowerType());
				dbTower.setGrade(towers.get(i).getSkillsValue()
						.getAttackLevel());

				int toxinLevel = towers.get(i).getSkillsValue().getToxinLevel();
				int fireLevel = towers.get(i).getSkillsValue().getFireLevel();
				int iceLevel = towers.get(i).getSkillsValue().getIceLevel();
				int specialLevel = towers.get(i).getSkillsValue()
						.getSpecialLevel();

				if (toxinLevel > 0)
				{
					skill = 10 + toxinLevel;
				}
				else if (fireLevel > 0)
				{
					skill = 20 + fireLevel;
				}
				else if (iceLevel > 0)
				{
					skill = 30 + iceLevel;
				}
				else if (specialLevel > 0)
				{
					skill = 40 + specialLevel;
				}

				dbTower.setSkill(skill);
				dbTower.setRecoveryprice(towers.get(i).getSkillsValue()
						.getSellMoney());

				dbTowers.add(dbTower);
			}

			dbManager.addTuple(dbMap, dbTowers);

			new MySharedPreferences(context)
					.saveGameState(MySharedPreferences.RESUME);
		}

	}

	@Override
	public void run()
	{
		while (threadFlag)
		{
			/* ����Ϸ��ͣʱ */
			if (isStopGame)
			{
			}
			else
			{
				if (isBeginNextWave)
				{
					/* ÿ����ʼ֮ǰ��20%����Ϣ */
					dollarValue = (int) (dollarValue + dollarValue * 0.2);

					/* ��ArrayList<Monster>����ӵ�һ��monster,��������monster���߳� */
					Monster monster = new Monster(context, gameDifficulty,
							level);
					monsters.add(monster);
					monster.startThread(threadFlag);

					/* ��������������ӹ��Ｏ�� */
					Tower.setMonsters(monsters);

					isFullMoster = false;

					isBeginNextWave = false;
				}
				
				
				long start = System.currentTimeMillis();
				myDraw();
				logic();
				long end = System.currentTimeMillis();

				try
				{
					if (end - start < RefreshTime)
					{
						Thread.sleep(RefreshTime - (end - start));
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

			}

		}
	}

	public void myDraw()
	{
		canvas = surfaceHolder.lockCanvas();

		try
		{
			if (canvas != null)
			{
				drawMap();

				if (isPaintGrid)
				{
					drawGrid();
				}

				for (int i = 0; i < towers.size(); i++)
				{
					drawTower(i);
				}

				for (int i = 0; i < monsters.size(); i++)
				{
					drawMonster(i);
				}

				for (int i = 0; i < towers.size(); i++)
				{
					drawTowerBuffPlacer(i);
				}

				for (int i = 0; i < towers.size(); i++)
				{
					drawAttackRange(i);
				}

			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			if (canvas != null)
				surfaceHolder.unlockCanvasAndPost(canvas);
		}

	}

	private void drawAttackRange(int index)
	{
		float x = towers.get(index).getTowerX();
		float y = towers.get(index).getTowerY();

		/* �Ƿ񻭹�����Χ */
		if (towers.get(index).isPaintAttackR())
		{
			canvas.drawCircle(x + frameW / 2, y + frameH / 2, towers.get(index)
					.getAttackRange(), paintCircle);
			canvas.drawCircle(x + frameW / 2, y + frameH / 2, towers.get(index)
					.getAttackRange() - 5, paintCircle);
		}
	}

	private void drawTower(int index)
	{
		canvas.drawBitmap(towers.get(index).getTowerBitmap(), towers.get(index)
				.getTowerX(), towers.get(index).getTowerY(), paint);

	}

	private void drawTowerBuffPlacer(int index)
	{
		/* ������ڱ������Ĺ���,��ʾ������Ч,�����ٹ����Ѫ�� */
		if (!towers.get(index).getCanAMonsters().isEmpty())
		{
			if (towers.get(index).isPaintBP())
			{
				drawBuffPlacer(towers.get(index).getCanAMonsters(), index);
			}

		}
	}

	/**
	 * ���������Ĺ�����Ч
	 * 
	 * @param canAMonsters
	 *            ���Թ������Ĺ��Ｏ��
	 * @param index
	 */
	private void drawBuffPlacer(ArrayList<Monster> canAMonsters, int index)
	{
		canvas.save();

		int towerType = towers.get(index).getSkillsValue().getTowerType();
		/* ������ͼƬ���Ͻǵ����� */
		float x = towers.get(index).getTowerX();
		float y = towers.get(index).getTowerY();

		if (towerType == Tower.TOWER3)
		{
			float aR = towers.get(index).getAttackRange();
			/* ������ΧͼƬ���Ͻǵ����� */
			float xR = x + frameW / 2 - aR;
			float yR = y + frameH / 2 - aR;

			canvas.clipRect(xR, yR, xR + aR * 2, yR + aR * 2);
			canvas.drawBitmap(towers.get(index).getAttackBitmap(), xR
					+ towers.get(index).getBuffPlacerIndex() * aR * 2, yR,
					paint);
		}
		else
		{
			/* ����ͼƬ�����Ͻ�����,���ڷ�����ֻ�ṥ����һ��,��ô�÷�����������һ������ */
			float xM = canAMonsters.get(0).getMonsterX();
			float yM = canAMonsters.get(0).getMonsterY();

			/* ��Ч�����ƶ��ľ��� */
			float xD = towers.get(index).getBPMoveInXAxis(x, xM);
			float yD = towers.get(index).getBPMoveInYAxis(y, yM);

			canvas.clipRect(x + frameW / 4 + xD, y + frameH / 4 + yD, x
					+ frameW / 4 + xD + frameW / 2, y + frameH / 4 + yD
					+ frameH / 2);
			canvas.drawBitmap(towers.get(index).getAttackBitmap(), x + frameW
					/ 4 + xD + towers.get(index).getBuffPlacerIndex() * frameW
					/ 2, y + frameH / 4 + yD, paint);
		}

		canvas.restore();
	}

	private void drawGrid()
	{
		canvas.drawBitmap(grid, 0, 0, paintGrid);
	}

	private void drawMap()
	{
		canvas.drawBitmap(gameMap.getGameMap(), 0, 0, paint);
		canvas.drawText("�÷֣�" + score, 30, 40, paint);
		canvas.drawBitmap(dollar, (float) ((screenW / 8) * 2.7), 10, paint);
		canvas.drawText("" + dollarValue, (float) ((screenW / 8) * 2.7) + 40,
				40, paint);
		canvas.drawBitmap(carrot, (float) ((screenW / 8) * 4), 10, paint);
		canvas.drawText("" + carrotValue, (float) ((screenW / 8) * 4) + 40, 40,
				paint);
		canvas.drawText("��" + (level + 1) + "��", frameW * 5, 40, paint);
	}

	/* ������ */
	private void drawMonster(int number)
	{
		canvas.save();

		float x = monsters.get(number).getMonsterX();
		float y = monsters.get(number).getMonsterY();

		if (monsters.get(number).isAlive())
		{
			canvas.clipRect(x, y, x + frameW, y + frameH);
			canvas.drawBitmap(monsters.get(number).getMonsterMove(), x
					+ monsters.get(number).getMonsterMoveIndex() * frameW, y,
					paint);

			/* ������Ѫ�� */
			float sHpRatio = monsters.get(number).getSurplusHpRatio();
			if (sHpRatio < 1)
			{
				paintBlood.setSurplusHpRatio(sHpRatio);
				canvas.drawRect(x, y, x + frameW, y + 10, paintRect);
				canvas.drawRect(x + 2, y + 2, x + sHpRatio * frameW - 2, y + 8,
						paintBlood.getPaint());
			}
		}
		else
		{
			/* ��������,��������ͼƬ,��λ�ò��ٸı� */
			monsters.get(number).setThreadFlag(false);

			canvas.drawBitmap(monsters.get(number).getMonsterDead(), monsters
					.get(number).getDeathX(), monsters.get(number).getDeathY(),
					paint);
		}

		canvas.restore();
	}

	public void logic()
	{

		/*�����ﻹδȫ������ʱ*/
		if (!isFullMoster && monsters.size() < eachLevelMonster)
		{
			/* ���һ�������Ĺ����ߵ������� */
			float x = monsters.get(monsters.size() - 1).getMonsterX();
			float y = monsters.get(monsters.size() - 1).getMonsterY();

			if (Math.abs(x - gameMap.getBeginningPointX()) >= 1.5 * frameW
					|| Math.abs(y - gameMap.getBeginningPointY()) >= 1.5 * frameH)
			{
				Monster monster = new Monster(context, gameDifficulty, level);
				monsters.add(monster);
				monster.startThread(threadFlag);

				/* ��������������ӹ��Ｏ�� */
				Tower.setMonsters(monsters);
			}
		}
		else
		{
			isFullMoster = true;

			/* �������û�й�����,��ô�Ϳ�ʼ��һ�� */
			int k = 0;
			for (k = 0; k < monsters.size(); k++)
			{
				if (monsters.get(k).isAlive())
				{
					break;
				}
			}

			/* ����ȫ����,�����ܹ��� */
			if (k == monsters.size())
			{
				monsters.clear();

				if (carrotValue > 0)
				{

					if (level < NormalMaxWave - 1)
					{
						level++;
						isBeginNextWave = true;

						isStopGame = true;
					}
					else
					{
						/* ʤ�� */
						threadFlag = false;
						isGameOver = true;
					}

				}
				else
				{
					/* ʧ�� */
					threadFlag = false;
					isGameOver = true;
				}

			}

		}

		/* y�᷽���ϵ�ƫ���� */
		float deviation = (float) (0.23 * frameH);

		for (int i = 0; i < monsters.size(); i++)
		{
			int escapeX = gameMap.getMonsterEscapeX(mapId);
			int escapeY = gameMap.getMonsterEscapeY(mapId);

			if (escapeX < 0)
			{
				/* �������ߵ�����ʱ,��ô�����������,����ֵ��һ */
				if ((monsters.get(i).getMonsterY() + deviation) >= escapeY
						* frameH
						&& monsters.get(i).getMonsterX() <= escapeX * frameW)
				{
					monsters.get(i).setThreadFlag(false);
					monsters.remove(i);
					carrotValue--;
				}
			}
			else
			{
				/* �������ߵ�����ʱ,��ô�����������,����ֵ��һ */
				if ((monsters.get(i).getMonsterY() + deviation) >= escapeY
						* frameH
						&& monsters.get(i).getMonsterX() >= escapeX * frameW)
				{
					monsters.get(i).setThreadFlag(false);
					monsters.remove(i);
					carrotValue--;
				}
			}
		}

		for (int i = 0; i < monsters.size(); i++)
		{
			/* ��ȡɱ���������õĽ�Һͷ��� */
			if (!monsters.get(i).isAlive())
			{
				dollarValue += monsters.get(i).getKillMoney();
				score += monsters.get(i).getKillScore();
			}

			monsters.get(i).isAccelerate(isAccelerate);
		}

		for (int i = 0; i < towers.size(); i++)
		{
			towers.get(i).isAccelerate(isAccelerate);
		}

	}

}
