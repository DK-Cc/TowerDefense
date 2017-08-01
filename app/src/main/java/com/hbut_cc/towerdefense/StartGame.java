package com.hbut_cc.towerdefense;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TextView;

public class StartGame extends Activity
{

	private LinearLayout ll_selectMap;

	private ImageView[] iv_maps;

	private TextView tv_describeMap;
	
	private RadioButton rb_easy;
	private RadioButton rb_normal;
	private RadioButton rb_hard;
	private RadioButton rb_normalGame;
	private RadioButton rb_survivalGame;

	private Button but_startGame;

	/**
	 * 记录游戏难度，0代表简单，1代表一般，2代表困难
	 */
	private int gameDifficulty;
	/**
	 * 记录游戏模式，0代表正常模式，1代表生存模式
	 */
	private int gameMode;
	/**
	 * 记录选择的地图，0代表第一张图
	 */
	private int selectedMap;

	/**
	 * 储存游戏地图位置
	 */
	private int[] maps;
	/**
	 * 储存游戏地图各ImageView的id
	 */
	private int[] ids;
	
	/**
	 * 储存从arrays中获取的数据，来描述地图
	 */
	private String[] mapDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu_startgame);

		initView();

		initData();

		changeView();

		setClickListener();

	}

	/**
	 * 获取并实例化数据
	 */
	private void initData()
	{

		mapDescription = getResources().getStringArray(R.array.mapDescription);
		
		maps = new int[] { R.drawable.map1, R.drawable.map2, R.drawable.map3,
				R.drawable.map4, R.drawable.map5, R.drawable.map6 };

		ids = new int[] { R.id.iv_map1, R.id.iv_map2, R.id.iv_map3,
				R.id.iv_map4, R.id.iv_map5, R.id.iv_map6 };

		iv_maps = new ImageView[maps.length];

		for (int i = 0; i < maps.length; i++)
		{
			ll_selectMap.addView(insertMaps(maps[i], i));
		}
		
	}

	private View insertMaps(int resid, int position)
	{
		LinearLayout linearLayout = new LinearLayout(this);

		LayoutParams layoutParams = new LayoutParams(240,
				LayoutParams.MATCH_PARENT);
		layoutParams.setMargins(0, 0, 10, 0);
		linearLayout.setLayoutParams(layoutParams);

		linearLayout.setPadding(2, 2, 2, 2);
		linearLayout.setGravity(Gravity.CENTER);

		int color = getResources().getColor(R.color.black);
		linearLayout.setBackgroundColor(color);

		iv_maps[position] = new ImageView(this);
		iv_maps[position].setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv_maps[position].setBackgroundResource(resid);
		iv_maps[position].setId(ids[position]);
		iv_maps[position].setAlpha(0.5f);
		if(position == 0)
		{
			iv_maps[position].setAlpha(1f);
			
			tv_describeMap.setText(mapDescription[position]);
		}
		
		iv_maps[position].setOnClickListener(clickListener);

		linearLayout.addView(iv_maps[position]);

		return linearLayout;
	}
	/**
	 * 根据已经选择的地图，把它透明度设置为1，其他的设置为0.5f
	 */
	private void choiseMaps()
	{
		for (int i = 0; i < iv_maps.length; i++)
		{
			iv_maps[i].setAlpha(0.5f);
		}
		
		iv_maps[selectedMap].setAlpha(1f);
		
		tv_describeMap.setText(mapDescription[selectedMap]);
	}

	/**
	 * 改变控件的字体
	 */
	private void changeView()
	{
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/hanyixue.ttf");
		rb_easy.setTypeface(face);
		rb_normal.setTypeface(face);
		rb_hard.setTypeface(face);
		rb_normalGame.setTypeface(face);
		rb_survivalGame.setTypeface(face);
		
		tv_describeMap.setTypeface(face);
	}

	private void setClickListener()
	{
		rb_easy.setOnClickListener(clickListener);
		rb_normal.setOnClickListener(clickListener);
		rb_hard.setOnClickListener(clickListener);

		rb_normalGame.setOnClickListener(clickListener);
		rb_survivalGame.setOnClickListener(clickListener);

		but_startGame.setOnClickListener(clickListener);

	}

	private OnClickListener clickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			int id = v.getId();

			switch (id)
			{
			case R.id.rb_easy:
				rb_easy.setChecked(true);
				rb_normal.setChecked(false);
				rb_hard.setChecked(false);

				gameDifficulty = 0;

				break;
			case R.id.rb_normal:
				rb_easy.setChecked(false);
				rb_normal.setChecked(true);
				rb_hard.setChecked(false);

				gameDifficulty = 1;

				break;
			case R.id.rb_hard:
				rb_easy.setChecked(false);
				rb_normal.setChecked(false);
				rb_hard.setChecked(true);

				gameDifficulty = 2;

				break;

			case R.id.rb_normalGame:
				rb_normalGame.setChecked(true);
				rb_survivalGame.setChecked(false);

				gameMode = 0;

				break;
			case R.id.rb_survivalGame:
				rb_normalGame.setChecked(false);
				rb_survivalGame.setChecked(true);

				gameMode = 1;

				break;
			case R.id.but_startGame:
				Intent intent = new Intent(StartGame.this,PlayGame.class);
				intent.putExtra("gameDifficulty", gameDifficulty);
				intent.putExtra("gameMode", gameMode);
				intent.putExtra("selectedMap", selectedMap);
				startActivity(intent);
				finish();
				break;
			case R.id.iv_map1:
				selectedMap = 0;
				choiseMaps();
				break;
			case R.id.iv_map2:
				selectedMap = 1;
				choiseMaps();
				break;
			case R.id.iv_map3:
				selectedMap = 2;
				choiseMaps();
				break;
			case R.id.iv_map4:
				selectedMap = 3;
				choiseMaps();
				break;
			case R.id.iv_map5:
				selectedMap = 4;
				choiseMaps();
				break;
			case R.id.iv_map6:
				selectedMap = 5;
				choiseMaps();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 实例化地图的控件
	 */
	private void initView()
	{
		ll_selectMap = (LinearLayout) findViewById(R.id.ll_selectMap);

		tv_describeMap = (TextView) findViewById(R.id.tv_describeMap);
		
		rb_easy = (RadioButton) findViewById(R.id.rb_easy);
		rb_normal = (RadioButton) findViewById(R.id.rb_normal);
		rb_hard = (RadioButton) findViewById(R.id.rb_hard);

		rb_normalGame = (RadioButton) findViewById(R.id.rb_normalGame);
		rb_survivalGame = (RadioButton) findViewById(R.id.rb_survivalGame);

		but_startGame = (Button) findViewById(R.id.but_startGame);
	}

}
