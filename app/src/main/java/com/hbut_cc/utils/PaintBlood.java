package com.hbut_cc.utils;

import android.graphics.Color;
import android.graphics.Paint;

public class PaintBlood
{
	private Paint paint;

	public PaintBlood()
	{
		paint = new Paint();
	}

	public void setAntiAlias(boolean aa)
	{
		paint.setAntiAlias(aa);
	}

	/**
	 * 设置剩余血量和总血量的比值,并根据sHpRatio的值,给paint设置相应的颜色
	 * @param sHpRatio 取值0~1
	 */
	public void setSurplusHpRatio(float sHpRatio)
	{
		if (sHpRatio > 1)
		{
			/* 输入的数值有错 */
		}
		else if (sHpRatio > 0.75f)
		{
			paint.setColor(Color.BLUE);
		}
		else if (sHpRatio > 0.5f)
		{
			paint.setColor(Color.GREEN);
		}
		else if (sHpRatio > 0.25f)
		{
			paint.setColor(Color.YELLOW);
		}
		else if (sHpRatio >= 0)
		{
			paint.setColor(Color.RED);
		}
		else
		{
			/* 输入的数值有错 */
		}
	}

	public Paint getPaint()
	{
		return paint;
	}

}
