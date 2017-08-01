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
	 * 每波怪物的个数
	 */
	public static final int eachLevelMonster = 20;

	/**
	 * 最大防御塔数
	 */
	private static final int maxTowerNumbers = 15;

	/**
	 * 正常模式最大波数
	 */
	private int NormalMaxWave = 14;

	private Context context;

	private SurfaceHolder surfaceHolder;

	private Canvas canvas;

	private Paint paint, paintGrid, paintCircle, paintRect;

	private PaintBlood paintBlood;

	/**
	 * 游戏难度，0,1,2
	 */
	private int gameDifficulty;

	/**
	 * 游戏地图，从0开始
	 */
	private int selectedMap;

	/**
	 * 游戏模式,0代表正常模式,1代表生存模式
	 */
	private int gameMode;

	/**
	 * 数据库中DBMap的储存量
	 */
	private int dbMapNumber;

	/**
	 * 第一个DBMap的mapid
	 */
	private String firstDBMapId;

	/**
	 * 游戏是否加速,false为不加速
	 */
	private boolean isAccelerate;

	/**
	 * dollar符号的图片，用于显示当前有多少钱;carrot图片，用于显示当前的生命值
	 */
	private Bitmap dollar, carrot;

	/**
	 * 现有金钱和剩余生命的值
	 */
	private int dollarValue, carrotValue;

	/**
	 * 游戏得的分数
	 */
	private int score;

	/**
	 * 记录第几波怪物,0代表第一波
	 */
	private int level;

	/**
	 * 游戏地图在R文件中的id
	 */
	private int mapId;

	/**
	 * 屏幕的宽和高
	 */
	private int screenW, screenH;

	/**
	 * 防御塔的大小
	 */
	private float frameW, frameH;

	private GameMap gameMap;

	/**
	 * 记录能放置塔的地方,0代表能放,1代表不能放; 详情见MapData.java中的getTowerPosition()方法
	 */
	private int[][] towerPosition;

	private Bitmap grid;

	/**
	 * 线程是否开启标志
	 */
	private boolean threadFlag = true;

	/** 游戏是否结束,true代表结束 */
	private boolean isGameOver = false;

	/** 是否停止查询FrameW是否赋值,当为true时代表是的 */
	private boolean isStopQuery = false;

	/**
	 * 是否画grid
	 */
	private boolean isPaintGrid;

	/**
	 * 是否开始下一波
	 */
	private boolean isBeginNextWave;

	/** 是否暂停游戏,true时代表暂停 */
	private boolean isStopGame = false;

	/**
	 * 所有的怪是否出完
	 */
	private boolean isFullMoster;

	private ArrayList<Monster> monsters;

	/**
	 * 保存防御塔的信息
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

		/* 从第一波开始 */
		level = 0;
		/* 开始时生命值为50 */
		carrotValue = 50;
		/* 开始分数为0 */
		score = 0;
		/* 不开始下一波 */
		isBeginNextWave = false;

		initData();
	}

	/**
	 * 用从数据库中获得的数据来完善游戏
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

		/* 把绘图线程是否开启的标志设置为true */
		threadFlag = true;

		monsters = new ArrayList<Monster>();
		Monster.setGameMap(gameMap);

		/* 往ArrayList<Monster>中添加第一个monster,并启动该monster的线程 */
		Monster monster = new Monster(context, gameDifficulty, level);
		monsters.add(monster);
		monster.startThread(threadFlag);

		/* 往防御塔类中添加怪物集合 */
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
		paintGrid.setAlpha(95); // 设置半透明
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

		/* 自定义的Paint */
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

		/* 创建一个tower的ArrayList */
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
	 * 游戏是否加速
	 * 
	 * @param a
	 *            true代表加速,false代表不加速
	 */
	public void isAccelerate(boolean a)
	{
		isAccelerate = a;
	}

	/**
	 * 添加防御塔
	 * 
	 * @param dbTower
	 */
	private void addGameTower(DBTower dbTower)
	{
		/* 注意在DBHelper中定义的towername为:tower1,tower2 */
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

		/* 毒 */
		if (skill1 == 1)
		{
			for (int i = 0; i < skill0; i++)
			{
				skillsValue.increaseToxinLevel();
			}
		}
		else if (skill1 == 2)
		{/* 火 */
			for (int i = 0; i < skill0; i++)
			{
				skillsValue.increaseFireLevel();
			}
		}
		else if (skill1 == 3)
		{/* 冰 */
			for (int i = 0; i < skill0; i++)
			{
				skillsValue.increaseIceLevel();
			}
		}
		else if (skill1 == 4)
		{/* 特 */
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
	 *            点击的横坐标除以frameW所得的值
	 * @param multipleTop
	 *            点击的纵坐标除以frameH所得的值
	 * @param towerType
	 *            tower的类型，其取值为1~4,具体在TowerData类中可见
	 * @param pictureId
	 *            防御塔图片在R文件中Id值
	 * @param positionValue
	 *            位置值，详情见MapData.java中的getTowerPosition()方法
	 */
	public void addNewTower(int multipleLeft, int multipleTop, int towerType,
			int pictureId, int positionValue)
	{
		if (dollarValue - SkillsValue.InitialCost[towerType - 1] < 0)
		{
			ToastUtils.showToast(context, "对不起你的金币不够~");
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

				/* 设置显示攻击范围 */
				paintOneAttackRange(tower);

			}
			else
			{
				ToastUtils.showToast(context, "最多只能放置" + maxTowerNumbers
						+ "个防御塔!");
			}
		}

	}

	/**
	 * 根据点击的位置删除防御塔
	 * 
	 * @param multipleLeft
	 *            点击的横坐标除以frameW所得的值
	 * @param multipleTop
	 *            点击的纵坐标除以frameH所得的值
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
	 * 根据点击坐标的倍数，获取相应的防御塔对象
	 * 
	 * @param multipleLeft
	 *            点击的横坐标除以frameW所得的值
	 * @param multipleTop
	 *            点击的纵坐标除以frameH所得的值
	 * @return 相应的防御塔对象
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
	 * 在金币足够的情况下,升级防御塔的攻击等级,否则显示金币不够的提醒
	 * 
	 * @param tower
	 *            要升级的防御塔
	 */
	public void upgradeAttackLevel(Tower tower)
	{
		int uAttackM = tower.getSkillsValue().upgradeAttackMoney();
		if (dollarValue < uAttackM)
		{
			ToastUtils.showToast(context, "金币不够不能升级~");
		}
		else
		{
			dollarValue = dollarValue - uAttackM;
			tower.upgradeAttackLevel();
		}
	}

	/**
	 * 在金币足够的情况下,升级防御塔的 火能力 等级,否则显示金币不够的提醒
	 * 
	 * @param tower
	 *            要升级的防御塔
	 */
	public void upgradeFireLevel(Tower tower)
	{
		int uFireM = tower.getSkillsValue().upgradeFireMoney();
		if (dollarValue < uFireM)
		{
			ToastUtils.showToast(context, "金币不够不能升级~");
		}
		else
		{
			dollarValue = dollarValue - uFireM;
			tower.getSkillsValue().increaseFireLevel();
		}
	}

	/**
	 * 在金币足够的情况下,升级防御塔的 冰能力 等级,否则显示金币不够的提醒
	 * 
	 * @param tower
	 *            要升级的防御塔
	 */
	public void upgradeIceLevel(Tower tower)
	{
		int uIceM = tower.getSkillsValue().upgradeIceMoney();
		if (dollarValue < uIceM)
		{
			ToastUtils.showToast(context, "金币不够不能升级~");
		}
		else
		{
			dollarValue = dollarValue - uIceM;
			tower.getSkillsValue().increaseIceLevel();
		}
	}

	/**
	 * 在金币足够的情况下,升级防御塔的 毒能力 等级,否则显示金币不够的提醒
	 * 
	 * @param tower
	 *            要升级的防御塔
	 */
	public void upgradeToxinLevel(Tower tower)
	{
		int uToxinM = tower.getSkillsValue().upgradeToxinMoney();
		if (dollarValue < uToxinM)
		{
			ToastUtils.showToast(context, "金币不够不能升级~");
		}
		else
		{
			dollarValue = dollarValue - uToxinM;
			tower.getSkillsValue().increaseToxinLevel();
		}
	}

	/**
	 * 在金币足够的情况下,升级防御塔的 特殊能力 ,否则显示金币不够的提醒
	 * 
	 * @param tower
	 *            要升级的防御塔
	 */
	public void upgradeSepecialLevel(Tower tower)
	{
		int uSpecialM = tower.getSkillsValue().upgradeSpecialMoney();
		if (dollarValue < uSpecialM)
		{
			ToastUtils.showToast(context, "金币不够不能升级~");
		}
		else
		{
			dollarValue = dollarValue - uSpecialM;
			tower.upgradeSpecialLevel();
		}
	}

	/**
	 * 除了参数tower以外其他的 是否画攻击范围 都设置为否
	 * 
	 * @param tower
	 */
	public void paintOneAttackRange(Tower tower)
	{
		doNotPaintAttackRange();
		tower.setPaintAttackR(true);
	}

	/**
	 * 所有的防御塔都不画 攻击范围
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
			/* 当游戏暂停时 */
			if (isStopGame)
			{
			}
			else
			{
				if (isBeginNextWave)
				{
					/* 每波开始之前加20%的利息 */
					dollarValue = (int) (dollarValue + dollarValue * 0.2);

					/* 往ArrayList<Monster>中添加第一个monster,并启动该monster的线程 */
					Monster monster = new Monster(context, gameDifficulty,
							level);
					monsters.add(monster);
					monster.startThread(threadFlag);

					/* 往防御塔类中添加怪物集合 */
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

		/* 是否画攻击范围 */
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
		/* 如果存在被攻击的怪物,显示攻击特效,并减少怪物的血量 */
		if (!towers.get(index).getCanAMonsters().isEmpty())
		{
			if (towers.get(index).isPaintBP())
			{
				drawBuffPlacer(towers.get(index).getCanAMonsters(), index);
			}

		}
	}

	/**
	 * 画防御塔的攻击特效
	 * 
	 * @param canAMonsters
	 *            可以攻击到的怪物集合
	 * @param index
	 */
	private void drawBuffPlacer(ArrayList<Monster> canAMonsters, int index)
	{
		canvas.save();

		int towerType = towers.get(index).getSkillsValue().getTowerType();
		/* 防御塔图片左上角的坐标 */
		float x = towers.get(index).getTowerX();
		float y = towers.get(index).getTowerY();

		if (towerType == Tower.TOWER3)
		{
			float aR = towers.get(index).getAttackRange();
			/* 攻击范围图片左上角的坐标 */
			float xR = x + frameW / 2 - aR;
			float yR = y + frameH / 2 - aR;

			canvas.clipRect(xR, yR, xR + aR * 2, yR + aR * 2);
			canvas.drawBitmap(towers.get(index).getAttackBitmap(), xR
					+ towers.get(index).getBuffPlacerIndex() * aR * 2, yR,
					paint);
		}
		else
		{
			/* 怪物图片的左上角坐标,由于防御塔只会攻击力一个,那么让防御塔攻击第一个怪物 */
			float xM = canAMonsters.get(0).getMonsterX();
			float yM = canAMonsters.get(0).getMonsterY();

			/* 特效区域移动的距离 */
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
		canvas.drawText("得分：" + score, 30, 40, paint);
		canvas.drawBitmap(dollar, (float) ((screenW / 8) * 2.7), 10, paint);
		canvas.drawText("" + dollarValue, (float) ((screenW / 8) * 2.7) + 40,
				40, paint);
		canvas.drawBitmap(carrot, (float) ((screenW / 8) * 4), 10, paint);
		canvas.drawText("" + carrotValue, (float) ((screenW / 8) * 4) + 40, 40,
				paint);
		canvas.drawText("第" + (level + 1) + "波", frameW * 5, 40, paint);
	}

	/* 画怪物 */
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

			/* 画怪物血条 */
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
			/* 怪物死了,换成死亡图片,且位置不再改变 */
			monsters.get(number).setThreadFlag(false);

			canvas.drawBitmap(monsters.get(number).getMonsterDead(), monsters
					.get(number).getDeathX(), monsters.get(number).getDeathY(),
					paint);
		}

		canvas.restore();
	}

	public void logic()
	{

		/*当怪物还未全部出现时*/
		if (!isFullMoster && monsters.size() < eachLevelMonster)
		{
			/* 最后一个出来的怪物走到哪里了 */
			float x = monsters.get(monsters.size() - 1).getMonsterX();
			float y = monsters.get(monsters.size() - 1).getMonsterY();

			if (Math.abs(x - gameMap.getBeginningPointX()) >= 1.5 * frameW
					|| Math.abs(y - gameMap.getBeginningPointY()) >= 1.5 * frameH)
			{
				Monster monster = new Monster(context, gameDifficulty, level);
				monsters.add(monster);
				monster.startThread(threadFlag);

				/* 往防御塔类中添加怪物集合 */
				Tower.setMonsters(monsters);
			}
		}
		else
		{
			isFullMoster = true;

			/* 如果场上没有怪物了,那么就开始下一波 */
			int k = 0;
			for (k = 0; k < monsters.size(); k++)
			{
				if (monsters.get(k).isAlive())
				{
					break;
				}
			}

			/* 怪物全死了,或者跑光了 */
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
						/* 胜利 */
						threadFlag = false;
						isGameOver = true;
					}

				}
				else
				{
					/* 失败 */
					threadFlag = false;
					isGameOver = true;
				}

			}

		}

		/* y轴方向上的偏移量 */
		float deviation = (float) (0.23 * frameH);

		for (int i = 0; i < monsters.size(); i++)
		{
			int escapeX = gameMap.getMonsterEscapeX(mapId);
			int escapeY = gameMap.getMonsterEscapeY(mapId);

			if (escapeX < 0)
			{
				/* 当怪物走到出口时,那么代表怪物跑了,生命值减一 */
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
				/* 当怪物走到出口时,那么代表怪物跑了,生命值减一 */
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
			/* 获取杀死怪物所得的金币和分数 */
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
