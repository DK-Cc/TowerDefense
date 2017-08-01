package com.hbut_cc.towerdefense;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbut_cc.maptools.MonsterData;
import com.hbut_cc.maptools.SkillsValue;
import com.hbut_cc.maptools.Tower;
import com.hbut_cc.utils.DBMap;
import com.hbut_cc.utils.DBTower;
import com.hbut_cc.utils.MySharedPreferences;
import com.hbut_cc.utils.ToastUtils;
import com.hbut_cc.view.GameMapView;

public class PlayGame extends Activity
{
	public static final String TAG = "TowerDefense";

	private Dialog dialog;

	private Dialog dialogInfo;

	/**
	 * ������ʾ��������Ϣ
	 */
	private WebView wv_towerInfo;

	/** webview������ */
	private String URL = "file:///android_asset/t1.html";

	/**
	 * ��Ϸ�Ƿ����,falseΪ������
	 */
	private boolean isAccelerate;

	/** �ж��Ƿ���в�ѯframeW�Ƿ�ֵ */
	private boolean isStopQueryFrameW = false;

	/** �ж���Ϸ�Ƿ����,trueΪ���� */
	private boolean isGameOver = false;

	/** �Ƿ���ͣ��Ϸ,true������ */
	private boolean isStopGame = false;
	
	/**�Ƿ�����չʾ������Ϣ,true������*/
	private boolean isShowMonsterIfo = false;

	/** �Ƿ���ʾ��ʾ */
	private boolean isShowTip;

	private GameMapView gamemapview;

	/** ѡ��ĵ�ͼ,��0��ʼ */
	private int selectedMap;
	/** ѡ����Ѷȣ�0,1,2,3 */
	private int gameDifficulty;
	/** ѡ���ģʽ��0,1 */
	private int gameMode;

	/** ���ݿ��е����ݣ���¼��ͼ����Ϣ */
	private DBMap dbMap;
	/** ���ݿ��е����ݣ���¼����������Ϣ */
	private ArrayList<DBTower> dbTowers;
	/** ���ݿ���һ���ж���DBMap,�����ж��Ƿ�ﵽ�涨����������� */
	private int dbMapNumber;
	/** ��һ��DBMap��mapid,�����ݿ��е����ݴﵽ���涨ֵʱ,ɾ����һ������,������������� */
	private String firstDBMapId;

	/*
	 * ��¼���������õ�λ�ã����Ƿ��ܷ��÷����������õ���ʲô������; �����MapData.java�е�getTowerPosition()����
	 */
	private int[][] towerPosition;

	/* ��ҳ�����ڼ���tower�Ȱ�ť */
	private LinearLayout ll_gamebut;

	/* ��Ϸ��ťxml�е�����linearlayout */
	private LinearLayout ll_towers, ll_towerinfo;

	/* ��Ϸ��ťxml�е�RelativeLayout */
	private RelativeLayout rl_upgrade_skills;

	/* tower_upgrade.xml�е�linearLayout */
	private LinearLayout ll_upgrade, ll_skillLevelShow;

	/* gamebut_towers.xml�еİ�ť */
	private ImageView iv_accelerate1, iv_tower1, iv_tower2, iv_tower3,
			iv_tower4;

	/* gamebut_towers.xml�еİ�ť */
	private ImageView iv_accelerate2, iv_towerInfo, iv_back;

	/* tower_upgrade.xml�еĿؼ� */
	private TextView tv_delete, tv_levelValue, tv_attackValue;

	/* �Լ�������view,Ҫ��ӵ�tower_upgrade.xml�е�ll_skillLevelShowȥ */
	private TextView tv_fireLevelValue, tv_specialLevelValue,
			tv_toxinLevelValue, tv_iceLevelValue;

	/* tower_upgrade.xml�еĿؼ� */
	private ImageView iv_upgrade_back, iv_showAttackIcon;

	/* �Լ�������view,Ҫ��ӵ�tower_upgrade.xml�е�ll_skillLevelShowȥ */
	private ImageView iv_showFireIcon, iv_showSpecialIcon, iv_showToxinIcon,
			iv_showIceIcon;

	/* �Լ�������view,Ҫ��ӵ�tower_upgrade.xml�е�ll_upgradeȥ */
	private TextView tv_attackLevel, tv_specialLevel, tv_toxinLevel,
			tv_fireLevel, tv_iceLevel;

	/* �Ƿ�paintGrid */
	private boolean isPaintGrid;

	/* ��������ͼƬ��R�ļ��е�id */
	private int towerPictureId;

	/* ��¼������λ�õ���Ϣ������ֵ�����MapData.java�е�getTowerPosition�������� */
	private int towerPositionValue;

	/* ��¼�����Ļ��ʱ�� */
	private long clickTime;

	/* ����assets�ļ����µ��Զ������� */
	private Typeface tface;

	/* �����������,��ȡ��ǰ����ķ������Ķ��� */
	private Tower clickTower;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.playgame_activity);

		Intent intent0 = this.getIntent();
		selectedMap = intent0.getIntExtra("selectedMap", -1);
		gameDifficulty = intent0.getIntExtra("gameDifficulty", -1);
		gameMode = intent0.getIntExtra("gameMode", -1);

		dbMap = intent0.getParcelableExtra("DBMap");
		dbTowers = intent0.getParcelableArrayListExtra("DBTowers");
		dbMapNumber = intent0.getIntExtra("DBMapNumber", -1);
		firstDBMapId = intent0.getStringExtra("FirstDBMapId");

		isShowTip = new MySharedPreferences(this).getOption();

		initView();

		creatViews();

		setView();

		setClickListener();

		linearLayoutAddView(ll_towers);

		thread.start();
		thread2.start();
		thread3.start();

	}

	@SuppressLint("ClickableViewAccessibility")
	private void setClickListener()
	{
		iv_accelerate1.setOnClickListener(clickListener);
		iv_tower1.setOnClickListener(clickListener);
		iv_tower2.setOnClickListener(clickListener);
		iv_tower3.setOnClickListener(clickListener);
		iv_tower4.setOnClickListener(clickListener);
		iv_accelerate2.setOnClickListener(clickListener);
		iv_towerInfo.setOnClickListener(clickListener);
		iv_back.setOnClickListener(clickListener);
		iv_upgrade_back.setOnClickListener(clickListener);

		gamemapview.setOnTouchListener(touchListener);

		tv_delete.setOnClickListener(clickListener);
		tv_attackLevel.setOnClickListener(clickListener);
		tv_fireLevel.setOnClickListener(clickListener);
		tv_specialLevel.setOnClickListener(clickListener);
		tv_toxinLevel.setOnClickListener(clickListener);
		tv_iceLevel.setOnClickListener(clickListener);

	}

	private OnClickListener clickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			int id = v.getId();

			switch (id)
			{
			case R.id.iv_accelerate1:
				buttonAccelerate();
				break;
			case R.id.iv_tower1:

				URL = "file:///android_asset/t1.html";
				iv_towerInfo.setBackgroundResource(R.drawable.t1_info);
				linearLayoutAddView(ll_towerinfo);
				isPaintGrid = true;
				gamemapview.setPaintGrid(isPaintGrid);
				towerPictureId = Tower.TowerId1[0];
				towerPositionValue = 11;

				break;
			case R.id.iv_tower2:

				URL = "file:///android_asset/t2.html";
				iv_towerInfo.setBackgroundResource(R.drawable.t2_info);
				linearLayoutAddView(ll_towerinfo);
				isPaintGrid = true;
				gamemapview.setPaintGrid(isPaintGrid);
				towerPictureId = Tower.TowerId2[0];
				towerPositionValue = 21;

				break;
			case R.id.iv_tower3:

				URL = "file:///android_asset/t3.html";
				iv_towerInfo.setBackgroundResource(R.drawable.t3_info);
				linearLayoutAddView(ll_towerinfo);
				isPaintGrid = true;
				gamemapview.setPaintGrid(isPaintGrid);
				towerPictureId = Tower.TowerId3[0];
				towerPositionValue = 31;

				break;
			case R.id.iv_tower4:

				URL = "file:///android_asset/t4.html";
				iv_towerInfo.setBackgroundResource(R.drawable.t4_info);
				linearLayoutAddView(ll_towerinfo);
				isPaintGrid = true;
				gamemapview.setPaintGrid(isPaintGrid);
				towerPictureId = Tower.TowerId4[0];
				towerPositionValue = 41;

				break;
			case R.id.iv_accelerate2:
				buttonAccelerate();
				break;
			case R.id.iv_towerInfo:

				setDialogInfo();
				setWebView();

				break;
			case R.id.but_webClose:
				dialogInfo.dismiss();
				break;
			case R.id.iv_back:

				linearLayoutAddView(ll_towers);
				isPaintGrid = false;
				gamemapview.setPaintGrid(isPaintGrid);

				gamemapview.doNotPaintAttackRange();

				break;
			case R.id.iv_upgrade_back:

				linearLayoutAddView(ll_towers);
				isPaintGrid = false;
				gamemapview.setPaintGrid(isPaintGrid);

				gamemapview.doNotPaintAttackRange();

				break;
			case R.id.tv_delete:

				if (gamemapview.getFrameW() != 0)
				{
					int multipleLeft = 0, multipleTop = 0;

					multipleLeft = (int) (clickTower.getTowerX() / gamemapview
							.getFrameW());
					multipleTop = (int) (clickTower.getTowerY() / gamemapview
							.getFrameH());

					gamemapview.deleteTower(multipleLeft, multipleTop);

					linearLayoutAddView(ll_towers);
					isPaintGrid = false;
					gamemapview.setPaintGrid(isPaintGrid);
				}
				else
				{
					/* ��ʱ��Ϸ������Bug,��frameWû��ʵ���� */
				}

				break;
			case R.id.tv_attackLevel:

				gamemapview.upgradeAttackLevel(clickTower);

				addUpgradeView(clickTower.getSkillsValue());
				break;

			case R.id.tv_fireLevel:
				gamemapview.upgradeFireLevel(clickTower);

				addUpgradeView(clickTower.getSkillsValue());
				break;
			case R.id.tv_iceLevel:
				gamemapview.upgradeIceLevel(clickTower);

				addUpgradeView(clickTower.getSkillsValue());
				break;
			case R.id.tv_toxinLevel:
				gamemapview.upgradeToxinLevel(clickTower);

				addUpgradeView(clickTower.getSkillsValue());
				break;
			case R.id.tv_specialLevel:
				gamemapview.upgradeSepecialLevel(clickTower);

				addUpgradeView(clickTower.getSkillsValue());
				break;

			case R.id.tv_yesquit:
				dialog.cancel();
				finishActivity();
				break;
			case R.id.tv_noquit:
				dialog.cancel();
				break;
			case R.id.tv_ok:
				if (dialog != null && dialog.isShowing())
				{
					dialog.cancel();
				}
				finish();
				break;
			case R.id.tv_monster_confirm:
				if (dialog != null && dialog.isShowing())
				{
					dialog.cancel();
				}
				
				isShowMonsterIfo = false;
				
				gamemapview.setStopGame(false);
				break;

			default:
				break;
			}
		}
	};

	private void linearLayoutAddView(LinearLayout linearLayout)
	{
		ll_gamebut.removeAllViews();
		ll_gamebut.addView(linearLayout);
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		isStopQueryFrameW = true;
		isGameOver = true;
		isStopGame = true;

		/* ������onStop()��ʱ��,������activity */
		this.finish();
	}

	@SuppressLint("InflateParams")
	private void initView()
	{
		gamemapview = (GameMapView) findViewById(R.id.gamemapview);
		ll_gamebut = (LinearLayout) findViewById(R.id.ll_gamebut);

		LayoutInflater lInflater = LayoutInflater.from(this);

		View view1 = lInflater.inflate(R.layout.gamebut_towers, null);
		ll_towers = (LinearLayout) view1.findViewById(R.id.ll_towers);
		iv_accelerate1 = (ImageView) ll_towers.getChildAt(0);
		iv_tower1 = (ImageView) ll_towers.getChildAt(1);
		iv_tower2 = (ImageView) ll_towers.getChildAt(2);
		iv_tower3 = (ImageView) ll_towers.getChildAt(3);
		iv_tower4 = (ImageView) ll_towers.getChildAt(4);

		View view2 = lInflater.inflate(R.layout.gamebut_towersinfo, null);
		ll_towerinfo = (LinearLayout) view2.findViewById(R.id.ll_towerinfo);
		iv_accelerate2 = (ImageView) ll_towerinfo.getChildAt(0);
		iv_towerInfo = (ImageView) ll_towerinfo.getChildAt(1);
		iv_back = (ImageView) ll_towerinfo.getChildAt(2);

		View view3 = lInflater.inflate(R.layout.tower_upgrade, null);
		rl_upgrade_skills = (RelativeLayout) view3
				.findViewById(R.id.rl_upgrade_skills);
		LinearLayout ll_upgrade_all = (LinearLayout) view3
				.findViewById(R.id.ll_upgrade_all);
		tv_delete = (TextView) ll_upgrade_all.getChildAt(0);
		ll_upgrade = (LinearLayout) ll_upgrade_all.getChildAt(2);
		tv_attackValue = (TextView) view3.findViewById(R.id.tv_attackValue);
		ll_skillLevelShow = (LinearLayout) view3
				.findViewById(R.id.ll_skillLevelShow);
		iv_showAttackIcon = (ImageView) ll_skillLevelShow.getChildAt(0);
		tv_levelValue = (TextView) ll_skillLevelShow.getChildAt(1);
		iv_upgrade_back = (ImageView) view3.findViewById(R.id.iv_upgrade_back);
	}

	private void creatViews()
	{
		LayoutParams params = (LayoutParams) tv_delete.getLayoutParams();

		tv_fireLevel = creatNewTextView(params, R.id.tv_fireLevel);
		tv_attackLevel = creatNewTextView(params, R.id.tv_attackLevel);
		tv_specialLevel = creatNewTextView(params, R.id.tv_specialLevel);
		tv_toxinLevel = creatNewTextView(params, R.id.tv_toxinLevel);
		tv_iceLevel = creatNewTextView(params, R.id.tv_iceLevel);

		LayoutParams params2 = (LayoutParams) iv_showAttackIcon
				.getLayoutParams();

		iv_showFireIcon = creatNewImageView(params2, R.id.iv_showFireIcon,
				R.drawable.icon_fire);
		iv_showSpecialIcon = creatNewImageView(params2,
				R.id.iv_showSpecialIcon, R.drawable.icon_special);
		iv_showToxinIcon = creatNewImageView(params2, R.id.iv_showToxinIcon,
				R.drawable.icon_poison);
		iv_showIceIcon = creatNewImageView(params2, R.id.iv_showIceIcon,
				R.drawable.icon_frost);

		LayoutParams params3 = (LayoutParams) tv_levelValue.getLayoutParams();

		tv_fireLevelValue = creatNewTextView2(params3, R.id.tv_fireLevelValue);
		tv_specialLevelValue = creatNewTextView2(params3,
				R.id.tv_specialLevelValue);
		tv_toxinLevelValue = creatNewTextView2(params3, R.id.tv_toxinLevelValue);
		tv_iceLevelValue = creatNewTextView2(params3, R.id.tv_iceLevelValue);

	}

	private TextView creatNewTextView2(LayoutParams params, int id)
	{

		TextView textview = new TextView(this);
		textview.setLayoutParams(params);
		textview.setId(id);
		textview.setTextAppearance(this, R.style.TextViewFormat_small2);

		return textview;
	}

	private TextView creatNewTextView(LayoutParams params, int id)
	{

		TextView textview = new TextView(this);
		textview.setLayoutParams(params);
		textview.setId(id);
		textview.setTextAppearance(this, R.style.TextViewFormat_small);
		textview.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

		return textview;
	}

	private ImageView creatNewImageView(LayoutParams params, int id, int resid)
	{
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setId(id);
		imageView.setBackgroundResource(resid);

		return imageView;
	}

	private void setDialogInfo()
	{
		dialogInfo = new Dialog(PlayGame.this, R.style.DialogStyle);
		dialogInfo.setCancelable(true);
		dialogInfo.setContentView(R.layout.help_dialog_web);

		Button but_webClose = (Button) dialogInfo
				.findViewById(R.id.but_webClose);

		but_webClose.setOnClickListener(clickListener);

		dialogInfo.setOnDismissListener(new OnDismissListener()
		{

			@SuppressWarnings("deprecation")
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				wv_towerInfo.clearView();
			}
		});

	}

	@SuppressWarnings("deprecation")
	private void setWebView()
	{
		wv_towerInfo = (WebView) dialogInfo.findViewById(R.id.wv_helps);

		wv_towerInfo.setBackgroundColor(0);

		WebSettings webSettings = wv_towerInfo.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(false);
		webSettings.setSupportZoom(false);

		wv_towerInfo.clearHistory();
		wv_towerInfo.loadUrl(URL);
		wv_towerInfo.setWebViewClient(new WebViewClient()
		{
			public void onPageFinished(WebView view, String url)
			{

				if (wv_towerInfo.getUrl().equals(URL))
				{

					wv_towerInfo.clearHistory();
				}
			};
		});

		dialogInfo.show();
	}

	/**
	 * ������Ϸ���٣��ı�iv_accelerate1��iv_accelerate2��ͼ�꣬�����б���
	 */
	private void buttonAccelerate()
	{
		if (!isAccelerate)
		{
			isAccelerate = true;
			iv_accelerate1.setBackgroundResource(R.drawable.xml_button_play);
			iv_accelerate2.setBackgroundResource(R.drawable.xml_button_play);
		}
		else
		{
			isAccelerate = false;
			iv_accelerate1.setBackgroundResource(R.drawable.xml_button_forward);
			iv_accelerate2.setBackgroundResource(R.drawable.xml_button_forward);
		}
		gamemapview.isAccelerate(isAccelerate);
	}

	private void setView()
	{
		tface = Typeface.createFromAsset(getAssets(), "fonts/hanyixue.ttf");
	}

	private void addTowerPosition(float left, float top)
	{

		towerPosition = gamemapview.getTowerPosition();

		int multipleLeft = 0, multipleTop = 0;
		if (gamemapview.getFrameW() != 0)
		{
			multipleLeft = (int) (left / gamemapview.getFrameW());
			multipleTop = (int) (top / gamemapview.getFrameH());
		}

		/* ����λ���ܷŷ�����ʱ */
		if (multipleTop < 1 || multipleTop > 11)
		{
			if (isPaintGrid)
			{
				ToastUtils.showToast(this, "�˴����ܷ��÷�����!");
			}

		}
		else
		{
			int position = towerPosition[multipleTop][multipleLeft];

			if (position == 1 && isPaintGrid)
			{
				ToastUtils.showToast(this, "�˴����ܷ��÷�����!");
			}
			else if (position == 0 && isPaintGrid)
			{
				gamemapview.addNewTower(multipleLeft, multipleTop,
						towerPositionValue / 10, towerPictureId,
						towerPositionValue);
			}
			else if (position > 1)
			{
				clickTower = gamemapview.getClickTower(multipleLeft,
						multipleTop);

				SkillsValue skillsValue = clickTower.getSkillsValue();

				isPaintGrid = false;
				gamemapview.setPaintGrid(isPaintGrid);

				/* ����ֻ����һ��tower�Ĺ�����Χ */
				gamemapview.paintOneAttackRange(clickTower);

				addUpgradeView(skillsValue);
			}
		}

	}

	private void addUpgradeView(SkillsValue skillsValue)
	{
		int uAttackLevel = skillsValue.getUpgradeAttackBitmapId();
		int uSpecialLevel = skillsValue.getUpgradeSpecialBitmapId();
		int uToxinLevel = skillsValue.getUpgradeToxinBitmapId();
		int uFireLevel = skillsValue.getUpgradeFireBitmapId();
		int uIceLevel = skillsValue.getUpgradeIceBitmapId();

		tv_delete.setText("+" + skillsValue.getSellMoney());
		tv_delete.setTypeface(tface);
		tv_attackValue.setText("" + skillsValue.getTowerAttackValue());

		ll_upgrade.removeAllViews();
		ll_skillLevelShow.removeAllViews();

		tv_levelValue.setText("" + skillsValue.getAttackLevel());

		ll_skillLevelShow.addView(iv_showAttackIcon);
		ll_skillLevelShow.addView(tv_levelValue);

		if (uAttackLevel != -1)
		{
			tv_attackLevel.setBackgroundResource(uAttackLevel);
			tv_attackLevel.setTypeface(tface);
			tv_attackLevel.setText("-" + skillsValue.upgradeAttackMoney());

			ll_upgrade.addView(tv_attackLevel);
		}
		if (uSpecialLevel != -1)
		{
			tv_specialLevel.setBackgroundResource(uSpecialLevel);
			tv_specialLevel.setTypeface(tface);
			tv_specialLevel.setText("-" + skillsValue.upgradeSpecialMoney());

			ll_upgrade.addView(tv_specialLevel);

		}
		if (uToxinLevel != -1)
		{
			tv_toxinLevel.setBackgroundResource(uToxinLevel);
			tv_toxinLevel.setTypeface(tface);
			tv_toxinLevel.setText("-" + skillsValue.upgradeToxinMoney());

			ll_upgrade.addView(tv_toxinLevel);

		}
		if (uFireLevel != -1)
		{
			tv_fireLevel.setBackgroundResource(uFireLevel);
			tv_fireLevel.setTypeface(tface);
			tv_fireLevel.setText("-" + skillsValue.upgradeFireMoney());

			ll_upgrade.addView(tv_fireLevel);
		}
		if (uIceLevel != -1)
		{
			tv_iceLevel.setBackgroundResource(uIceLevel);
			tv_iceLevel.setTypeface(tface);
			tv_iceLevel.setText("-" + skillsValue.upgradeIceMoney());

			ll_upgrade.addView(tv_iceLevel);

		}

		if (skillsValue.getSpecialLevel() > 0)
		{
			tv_specialLevelValue.setText("" + skillsValue.getSpecialLevel());

			ll_skillLevelShow.addView(iv_showSpecialIcon);
			ll_skillLevelShow.addView(tv_specialLevelValue);
		}

		if (skillsValue.getToxinLevel() > 0)
		{
			tv_toxinLevelValue.setText("" + skillsValue.getToxinLevel());

			ll_skillLevelShow.addView(iv_showToxinIcon);
			ll_skillLevelShow.addView(tv_toxinLevelValue);
		}

		if (skillsValue.getFireLevel() > 0)
		{
			tv_fireLevelValue.setText("" + skillsValue.getFireLevel());

			ll_skillLevelShow.addView(iv_showFireIcon);
			ll_skillLevelShow.addView(tv_fireLevelValue);
		}

		if (skillsValue.getIceLevel() > 0)
		{
			tv_iceLevelValue.setText("" + skillsValue.getIceLevel());

			ll_skillLevelShow.addView(iv_showIceIcon);
			ll_skillLevelShow.addView(tv_iceLevelValue);
		}

		ll_gamebut.removeAllViews();
		ll_gamebut.addView(rl_upgrade_skills);
	}

	private void showGameOverMessage(int background)
	{
		dialog = new Dialog(PlayGame.this, R.style.WidthDialogStyle);
		dialog.setContentView(R.layout.winorlose);
		dialog.setCancelable(true);

		ImageView iv_winrolose = (ImageView) dialog
				.findViewById(R.id.iv_winrolose);
		TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);

		iv_winrolose.setBackgroundResource(background);

		tv_ok.setOnClickListener(clickListener);

		dialog.show();
	}

	private void showBeginNextWave(int level)
	{
		dialog = new Dialog(PlayGame.this, R.style.WidthDialogStyle);
		dialog.setContentView(R.layout.dialog_monsterinfo);
		dialog.setCancelable(false);

		ImageView iv_monsterPic = (ImageView) dialog
				.findViewById(R.id.iv_monsterPic);
		TextView tv_boold = (TextView) dialog.findViewById(R.id.tv_boold);
		TextView tv_special_skill = (TextView) dialog
				.findViewById(R.id.tv_special_skill);
		TextView tv_phisic_ant = (TextView) dialog
				.findViewById(R.id.tv_phisic_ant);
		TextView tv_kill_getmoney = (TextView) dialog
				.findViewById(R.id.tv_kill_getmoney);
		TextView tv_kill_getscore = (TextView) dialog
				.findViewById(R.id.tv_kill_getscore);
		TextView tv_move_speed = (TextView) dialog
				.findViewById(R.id.tv_move_speed);
		TextView tv_monster_confirm = (TextView) dialog
				.findViewById(R.id.tv_monster_confirm);

		MonsterData monsterData = new MonsterData();
		int[] monsterMapValue = monsterData.getMonsterMapValue();
		iv_monsterPic.setBackgroundResource(monsterMapValue[level
				% monsterMapValue.length]);

		if (null != dbMap)
		{
			tv_boold.setText(""
					+ monsterData.getHPValue(dbMap.getGamedifficulty(), level));
		}
		else
		{
			tv_boold.setText("" + monsterData.getHPValue(gameDifficulty, level));
		}

		StringBuffer specialSkill = new StringBuffer();

		if (monsterData.getIceResistance(level))
		{
			specialSkill.append(" ����");
		}
		if (monsterData.getFireResistance(level))
		{
			specialSkill.append(" ��");
		}
		if (monsterData.getToxinResistance(level))
		{
			specialSkill.append(" ����");
		}
		if(specialSkill.length() == 0)
		{
			specialSkill.append("��");
		}

		tv_special_skill.setText(specialSkill);
		tv_phisic_ant.setText("" + monsterData.getPhysicsResistance(level));
		tv_kill_getmoney.setText("" + monsterData.getEachWaveMoney(level));
		tv_kill_getscore.setText("" + monsterData.getEachWaveScore(level));

		String speedStr = null;

		if (monsterData.getMoveSpeed(level) == 0.5)
		{
			speedStr = "��";
		}
		else if (monsterData.getMoveSpeed(level) == 1)
		{
			speedStr = "һ��";
		}
		else if (monsterData.getMoveSpeed(level) == 2)
		{
			speedStr = "��";
		}
		else
		{
			speedStr = "����";
		}

		tv_move_speed.setText(speedStr);

		tv_monster_confirm.setOnClickListener(clickListener);

		dialog.show();
	}

	private OnTouchListener touchListener = new OnTouchListener()
	{

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if ((System.currentTimeMillis() - clickTime) > 100)
			{

				addTowerPosition(event.getX(), event.getY());
				clickTime = System.currentTimeMillis();
			}
			return true;
		}
	};

	/**
	 * ��GameMapView�е�frameW��ֵ��֮��,����ʵ��������
	 */
	private Thread thread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (!isStopQueryFrameW)
			{
				if (gamemapview.isStopQuery())
				{
					if (null != dbMap)
					{
						gamemapview.initIntentData(dbMap, dbTowers,
								dbMapNumber, firstDBMapId);
					}
					else
					{
						gamemapview.initIntentData(selectedMap, gameDifficulty,
								gameMode);
					}

					isStopQueryFrameW = true;
				}

			}
		}
	});

	private Thread thread2 = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			/* ÿ��500ms�ͼ��һ��,��Ϸ�߳��Ƿ���� */
			while (!isGameOver)
			{
				/* GameMapView����Ϸ����ʱ,���ж���Ϸ�Ƿ�ʤ�����߽��� */
				if (gamemapview.isGameOver())
				{
					if (gamemapview.getCarrotValue() > 0)
					{
						/* ��Ϸʤ�� */
						Message msg = new Message();
						msg.what = 0;

						handler.sendMessage(msg);
					}
					else
					{
						/* ��Ϸʧ�� */
						Message msg = new Message();
						msg.what = 1;

						handler.sendMessage(msg);
					}

					isGameOver = true;
				}

				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

			}
		}
	});

	private Thread thread3 = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			/* ÿ��50ms�ͼ��һ��,��Ϸ�߳��Ƿ���ͣ */
			while (!isStopGame)
			{
				/* GameMapView����Ϸ��ͣʱ */
				if (gamemapview.isStopGame() && !isShowMonsterIfo)
				{
					if (isShowTip)
					{
						Message msg = new Message();
						msg.what = 2;

						handler.sendMessage(msg);
						
						isShowMonsterIfo = true;
					}
					else
					{
						/* ���û��Ҫ��ʾÿ��������Ϣ����ʾ,��ô�͹ر�stopGame,��ʼ�²� */
						gamemapview.setStopGame(false);
					}
				}

				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

			}
		}
	});

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			dialog = new Dialog(PlayGame.this, R.style.DialogStyle);
			dialog.setContentView(R.layout.dialog_quit);
			dialog.setCancelable(true);

			TextView tv_yesquit = (TextView) dialog
					.findViewById(R.id.tv_yesquit);
			TextView tv_noquit = (TextView) dialog.findViewById(R.id.tv_noquit);

			tv_yesquit.setOnClickListener(clickListener);
			tv_noquit.setOnClickListener(clickListener);

			dialog.show();

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void finishActivity()
	{
		finish();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:
				showGameOverMessage(R.drawable.victory);
				break;
			case 1:
				showGameOverMessage(R.drawable.defeat);
				break;
			case 2:
				showBeginNextWave(gamemapview.getLevel());
				break;
			}

		};
	};

}
