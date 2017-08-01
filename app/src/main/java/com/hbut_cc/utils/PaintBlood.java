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
	 * ����ʣ��Ѫ������Ѫ���ı�ֵ,������sHpRatio��ֵ,��paint������Ӧ����ɫ
	 * @param sHpRatio ȡֵ0~1
	 */
	public void setSurplusHpRatio(float sHpRatio)
	{
		if (sHpRatio > 1)
		{
			/* �������ֵ�д� */
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
			/* �������ֵ�д� */
		}
	}

	public Paint getPaint()
	{
		return paint;
	}

}
