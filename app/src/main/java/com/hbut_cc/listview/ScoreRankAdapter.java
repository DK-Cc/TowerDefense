package com.hbut_cc.listview;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hbut_cc.towerdefense.R;
import com.hbut_cc.utils.DBMap;

public class ScoreRankAdapter extends BaseAdapter
{
	private ArrayList<DBMap> dbMaps;
	private LayoutInflater lInflater;

	private int clickPosition;

	public ScoreRankAdapter(Context context, ArrayList<DBMap> dbMaps)
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

			convertView = lInflater.inflate(R.layout.item_scorerank, null);

			viewHolder.tv_rankTime = (TextView) convertView
					.findViewById(R.id.tv_rankTime);
			viewHolder.tv_rankScore = (TextView) convertView
					.findViewById(R.id.tv_rankScore);
			viewHolder.tv_rankWave = (TextView) convertView
					.findViewById(R.id.tv_rankWave);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tv_rankTime.setText(getGameTime(dbMaps.get(position)
				.getMapid()));

		viewHolder.tv_rankScore.setText("" + dbMaps.get(position).getScore());
		viewHolder.tv_rankWave.setText(""
				+ dbMaps.get(position).getMonsterWave());

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

	private class ViewHolder
	{
		TextView tv_rankTime;
		TextView tv_rankScore;
		TextView tv_rankWave;
	}

}
