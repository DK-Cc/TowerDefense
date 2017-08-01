package com.hbut_cc.towerdefense;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hbut_cc.listview.ScoreRankAdapter;
import com.hbut_cc.utils.DBManager;
import com.hbut_cc.utils.DBMap;
import com.hbut_cc.utils.DBThread;
import com.hbut_cc.utils.MySharedPreferences;

public class Options extends Activity
{

	private Context context;
	
	private Dialog dialog;
	
	private int clickPosition;
	
	private ScoreRankAdapter adapter;
	
	private ArrayList<DBMap> dbMaps;
	
	private TextView tv_nextLevel;
	private TextView tv_showHighScores;
	private TextView tv_back;

	private CheckBox rb_nextLevel;

	/** 下一波提示按钮的状态 */
	private boolean nextLevel;

	public static final String TAG = "TowerDefense";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu_options);

		context = this;
		
		getApplicationParameters();

		initView();

		changeView();

		setOnClickListener();

	}

	private void changeView()
	{
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/hanyixue.ttf");
		tv_nextLevel.setTypeface(face);
		tv_showHighScores.setTypeface(face);
		tv_back.setTypeface(face);

		rb_nextLevel.setChecked(nextLevel);

	}

	private void setOnClickListener()
	{
		tv_showHighScores.setOnClickListener(clickListener);
		tv_back.setOnClickListener(clickListener);

		rb_nextLevel.setOnCheckedChangeListener(changeListener);
	}

	private OnCheckedChangeListener changeListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked)
		{
			int id = buttonView.getId();

			switch (id)
			{
			case R.id.rb_nextLevel:
				if (isChecked)
				{
					nextLevel = true;
					buttonView.setChecked(true);
				}
				else
				{
					nextLevel = false;
					buttonView.setChecked(false);
				}
				break;
			default:
				break;
			}
		}
	};

	private OnClickListener clickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			int id = v.getId();

			switch (id)
			{
			case R.id.tv_showHighScores:
				
				getRankData();
				
				dialog = new Dialog(Options.this,
						R.style.WidthDialogStyle);
				dialog.setContentView(R.layout.resume_dialog);
				dialog.setCancelable(true);
				
				ListView lv_resumeInfo = (ListView) dialog.findViewById(R.id.lv_resumeInfo);
				
				TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
				TextView tv_delete = (TextView) dialog.findViewById(R.id.tv_delete);
				
				adapter = new ScoreRankAdapter(context, dbMaps);
				lv_resumeInfo.setAdapter(adapter);
				
				lv_resumeInfo.setOnItemClickListener(itemClickListener);
				
				tv_confirm.setOnClickListener(clickListener);
				tv_delete.setOnClickListener(clickListener);
				
				dialog.show();

				
				break;
			case R.id.tv_back:
				Options.this.finish();
				break;
			case R.id.tv_confirm:
				if(dialog != null && dialog.isShowing())
				{
					dialog.cancel();
				}
				break;
			case R.id.tv_delete:
				if(dbMaps.size()>0)
				{
					DBManager dbManager = new DBManager(context);
					dbManager.deleteTuple(dbMaps.get(clickPosition).getMapid());
					dbMaps.remove(clickPosition);
					clickPosition = 0;
					adapter.setGreenBackground(clickPosition);
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		}
	};
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			adapter.setGreenBackground(position);
			
			clickPosition = position;
		}
	};

	private void initView()
	{
		tv_nextLevel = (TextView) findViewById(R.id.tv_nextLevel);
		tv_showHighScores = (TextView) findViewById(R.id.tv_showHighScores);
		tv_back = (TextView) findViewById(R.id.tv_back);

		rb_nextLevel = (CheckBox) findViewById(R.id.rb_nextLevel);
	}

	private void getApplicationParameters()
	{
		MySharedPreferences sharedPreferences = new MySharedPreferences(this);
		nextLevel = sharedPreferences.getOption();
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		new MySharedPreferences(this).saveOption(nextLevel);
	}
	
	/**
	 * 获得排名的数据
	 */
	private void getRankData()
	{
		DBThread thread = new DBThread(this);
		thread.start();
		
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		dbMaps = thread.getDbMaps();
		
		/*把数据从大到校进行排序*/
		rankByScore(dbMaps);
		
		/*删除十条以外的数据*/
		if(dbMaps.size() > 10)
		{
			for (int i = 0; i < dbMaps.size()-10; i++)
			{
				dbMaps.remove(dbMaps.size()-1-i);
			}
		}
		
	}
	
	private void rankByScore(ArrayList<DBMap> dbMapTemp)
	{
		DBMap temp1 = null;
		DBMap temp2 = null;
		
		for (int i = 0; i < dbMapTemp.size()-1; i++)
		{
			for (int j = i+1; j < dbMapTemp.size(); j++)
			{
				if(dbMapTemp.get(i).getScore()<dbMapTemp.get(j).getScore())
				{
					temp1 = dbMapTemp.get(i);
					temp2 = dbMapTemp.get(j);
					dbMapTemp.remove(i);
					dbMapTemp.add(i, temp2);
					dbMapTemp.remove(j);
					dbMapTemp.add(j, temp1);
					
				}
			}
		}
	}

}
