package com.hbut_cc.towerdefense;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hbut_cc.listview.ResumeAdapter;
import com.hbut_cc.utils.DBManager;
import com.hbut_cc.utils.DBMap;
import com.hbut_cc.utils.DBThread;
import com.hbut_cc.utils.DBTower;
import com.hbut_cc.utils.MySharedPreferences;
import com.hbut_cc.utils.ToastUtils;

public class MenuActivity extends Activity
{
	public static final String TAG = "TowerDefense";

	private Context context;
	
	/* 取值为0或1，当为1是代表有可以继续的游戏 */
	private int state;

	private ImageButton ib_startGame;
	private ImageButton ib_options;
	private ImageButton ib_help;
	/*private ImageButton ib_multiplayer;*/

	private Dialog dialogInfo;
	
	private Dialog dialog;
	
	/**
	 * 数据库中的数据
	 */
	private ArrayList<DBMap> dbMaps;
	/**
	 * 数据库中的数据
	 */
	private ArrayList<ArrayList<DBTower>> dbTowerss;
	
	/**
	 * listview的适配器
	 */
	private ResumeAdapter adapter;
	
	/**
	 * 点击listview的位置
	 */
	private int clickPosition;

	/** webview的链接 */
	private String URL = "file:///android_asset/instructions.html";

	private WebView wv_helps;
	/** help中的返回按钮 */
	private Button but_webBack;

	/** 储存第一次按下返回键（退出时）的时间 */
	private long exitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);

		context = this;
		
		getApplicationParameters();

		initView();

		setOnClickListener();

	}

	private void getApplicationParameters()
	{
		state = new MySharedPreferences(this).getGameState();
	}

	private void setOnClickListener()
	{
		ib_startGame.setOnClickListener(clickListener);
		ib_options.setOnClickListener(clickListener);
		ib_help.setOnClickListener(clickListener);
		/*ib_multiplayer.setOnClickListener(clickListener);*/

	}

	private OnClickListener clickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			int id = v.getId();
			switch (id)
			{
			case R.id.ib_startGame:
				if (state == MySharedPreferences.START_GAME)
				{
					Intent intent = new Intent(MenuActivity.this,
							StartGame.class);
					startActivity(intent);
				}
				else
				{
					dialog = new Dialog(MenuActivity.this,
							R.style.DialogStyle);
					dialog.setContentView(R.layout.whether_resume);
					dialog.setCancelable(true);

					ImageButton ib_newGame = (ImageButton) dialog
							.findViewById(R.id.ib_newGame);

					ib_newGame.setOnClickListener(clickListener);

					ImageButton ib_resume = (ImageButton) dialog
							.findViewById(R.id.ib_resume);

					ib_resume.setOnClickListener(clickListener);

					dialog.show();

				}
				break;
			case R.id.ib_options:
				Intent intent = new Intent(MenuActivity.this, Options.class);
				startActivity(intent);
				break;
			case R.id.ib_help:

				setDialogInfo();
				setWebView();

				dialogInfo.show();

				break;
			case R.id.ib_newGame:
				Intent intent3 = new Intent(MenuActivity.this,
						StartGame.class);
				startActivity(intent3);
				if(dialog != null)
				{
					dialog.cancel();
				}
				
				break;
			case R.id.ib_resume:
				
				startNewDBThread();
				
				if(dialog.isShowing())
				{
					dialog.cancel();
				}
				
				if(dbMaps != null)
				{
					dialog = new Dialog(MenuActivity.this,
							R.style.WidthDialogStyle);
					dialog.setContentView(R.layout.resume_dialog);
					dialog.setCancelable(true);
					
					ListView lv_resumeInfo = (ListView) dialog.findViewById(R.id.lv_resumeInfo);
					
					TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
					TextView tv_delete = (TextView) dialog.findViewById(R.id.tv_delete);
					
					adapter = new ResumeAdapter(context, dbMaps);
					lv_resumeInfo.setAdapter(adapter);
					
					lv_resumeInfo.setOnItemClickListener(itemClickListener);
					
					tv_confirm.setOnClickListener(clickListener);
					tv_delete.setOnClickListener(clickListener);
					
					dialog.show();
				}
				
				break;
			case R.id.tv_confirm:
				if(dbMaps.size()>0)
				{
					Intent intent2 = new Intent(MenuActivity.this,PlayGame.class);
					intent2.putExtra("DBMap", dbMaps.get(clickPosition));
					intent2.putParcelableArrayListExtra("DBTowers", dbTowerss.get(clickPosition));
					intent2.putExtra("DBMapNumber", dbMaps.size());
					intent2.putExtra("FirstDBMapId", dbMaps.get(0).getMapid());
					startActivity(intent2);
					if(null != dialog)
					{
						dialog.cancel();
					}
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
			case R.id.but_webBack:
				wv_helps.goBack();
				break;
			case R.id.but_webClose:
				dialogInfo.dismiss();
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
	
	/**
	 * 开始DBThread线程,暂停主线程,进行数据库访问,并获得数据
	 */
	private void startNewDBThread()
	{
		DBThread dbTread = new DBThread(this);
		dbTread.start();
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		dbMaps = dbTread.getDbMaps();
		dbTowerss = dbTread.getDbTowerss();
		
	}

	private void setDialogInfo()
	{
		dialogInfo = new Dialog(MenuActivity.this, R.style.DialogStyle);
		dialogInfo.setCancelable(true);
		dialogInfo.setContentView(R.layout.help_dialog_web);

		but_webBack = (Button) dialogInfo.findViewById(R.id.but_webBack);
		Button but_webClose = (Button) dialogInfo
				.findViewById(R.id.but_webClose);

		but_webBack.setOnClickListener(clickListener);
		but_webClose.setOnClickListener(clickListener);

		dialogInfo.setOnDismissListener(new OnDismissListener()
		{

			@SuppressWarnings("deprecation")
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				wv_helps.clearView();
			}
		});

	}

	@SuppressWarnings("deprecation")
	private void setWebView()
	{
		wv_helps = (WebView) dialogInfo.findViewById(R.id.wv_helps);

		wv_helps.setBackgroundColor(0);

		WebSettings webSettings = wv_helps.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(false);
		webSettings.setSupportZoom(false);

		wv_helps.clearHistory();
		wv_helps.loadUrl(URL);
		wv_helps.setWebViewClient(new WebViewClient()
		{
			public void onPageFinished(WebView view, String url)
			{

				if (wv_helps.getUrl().equals(URL))
				{

					wv_helps.clearHistory();
				}
				if (wv_helps.canGoBack())
					but_webBack.setVisibility(View.VISIBLE);
				else
					but_webBack.setVisibility(View.INVISIBLE);
			};
		});
	}

	private void initView()
	{
		ib_startGame = (ImageButton) findViewById(R.id.ib_startGame);
		ib_options = (ImageButton) findViewById(R.id.ib_options);
		ib_help = (ImageButton) findViewById(R.id.ib_help);
		/*ib_multiplayer = (ImageButton) findViewById(R.id.ib_multiplayer);*/
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if ((System.currentTimeMillis() - exitTime) > 800)
			{
				ToastUtils.showToast(this, "再按一次退出游戏");
				exitTime = System.currentTimeMillis();
			}
			else
				finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
