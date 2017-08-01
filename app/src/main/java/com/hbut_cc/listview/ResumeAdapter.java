package com.hbut_cc.listview;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hbut_cc.towerdefense.R;
import com.hbut_cc.utils.DBMap;

public class ResumeAdapter extends BaseAdapter
{
	private LayoutInflater lInflater;

	private ArrayList<DBMap> dbMaps;

	private int clickPosition;

	public ResumeAdapter(Context context, ArrayList<DBMap> dbMaps)
	{
		lInflater = LayoutInflater.from(context);
		this.dbMaps = dbMaps;
	}

	/**
	 * 设置点击位置背景为绿色的，其他的为透明的
	 * 
	 * @param position
	 *            点击的位置
	 */
	public void setGreenBackground(int position)
	{
		if (clickPosition != position)
			clickPosition = position;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return dbMaps.size();
	}

	@Override
	public Object getItem(int position)
	{
		return dbMaps.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;

		if (convertView == null)
		{
			viewHolder = new ViewHolder();

			convertView = lInflater.inflate(R.layout.item_showresume, null);

			viewHolder.iv_map = (ImageView) convertView
					.findViewById(R.id.iv_map);
			viewHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tv_mode = (TextView) convertView
					.findViewById(R.id.tv_mode);
			viewHolder.tv_difficulty = (TextView) convertView
					.findViewById(R.id.tv_difficulty);
			viewHolder.tv_wave = (TextView) convertView
					.findViewById(R.id.tv_wave);
			viewHolder.tv_rLife = (TextView) convertView
					.findViewById(R.id.tv_rLife);
			viewHolder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			viewHolder.tv_score = (TextView) convertView
					.findViewById(R.id.tv_score);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.iv_map.setBackgroundResource(getMapRid(dbMaps.get(position)
				.getMapname()));
		viewHolder.tv_time
				.setText(getGameTime(dbMaps.get(position).getMapid()));

		if (dbMaps.get(position).getGamemode() == 0)
		{
			viewHolder.tv_mode.setText("正常模式");
		}
		else
		{
			viewHolder.tv_mode.setText("生存模式");
		}

		if (dbMaps.get(position).getGamedifficulty() == 0)
		{
			viewHolder.tv_difficulty.setText("简单");
		}
		else if (dbMaps.get(position).getGamedifficulty() == 1)
		{
			viewHolder.tv_difficulty.setText("一般");
		}
		else
		{
			viewHolder.tv_difficulty.setText("困难");
		}

		viewHolder.tv_wave.setText("" + dbMaps.get(position).getMonsterWave());
		viewHolder.tv_rLife
				.setText("" + dbMaps.get(position).getResiduallife());
		viewHolder.tv_money.setText("" + dbMaps.get(position).getMoney());
		viewHolder.tv_score.setText("" + dbMaps.get(position).getScore());

		if (position == clickPosition)
		{
			convertView.setBackgroundColor(Color.GREEN);
		}
		else
		{
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}

		return convertView;
	}

	private String getGameTime(String mapid)
	{
		return "20" + mapid.substring(0, 2) + "年" + mapid.substring(2, 4) + "月"
				+ mapid.substring(4, 6) + "日" + mapid.substring(6, 8) + "时"
				+ mapid.substring(8, 10) + "分";
	}

	/**
	 * 根据地图的名字获得地图在R文件中的id
	 * 
	 * @param name
	 *            地图的名字
	 * @return 地图在R文件中的id
	 */
	private int getMapRid(String name)
	{
		if ("map1".equals(name))
		{
			return R.drawable.map1;
		}
		else if ("map2".equals(name))
		{
			return R.drawable.map2;
		}
		else if ("map3".equals(name))
		{
			return R.drawable.map3;
		}
		else if ("map4".equals(name))
		{
			return R.drawable.map4;
		}
		else if ("map5".equals(name))
		{
			return R.drawable.map5;
		}
		else
		{
			return R.drawable.map6;
		}
	}

	private class ViewHolder
	{
		ImageView iv_map;
		TextView tv_time;
		TextView tv_mode;
		TextView tv_difficulty;
		TextView tv_wave;
		TextView tv_rLife;
		TextView tv_money;
		TextView tv_score;
	}

}
