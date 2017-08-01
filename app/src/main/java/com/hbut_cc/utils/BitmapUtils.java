package com.hbut_cc.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils
{
	/**
	 * 改变图片的大小
	 * 
	 * @param bitmap
	 *            原bitmap
	 * @param width
	 *            新bitmap的宽
	 * @param height
	 *            新bitmap的高
	 */
	public static Bitmap changeBitmapSize(Bitmap bitmap, float width,
			float height)
	{
		int bitMapWidth = bitmap.getWidth();
		int bitMapHeight = bitmap.getHeight();

		float scaleWidth = width / bitMapWidth;
		float scaleHeight = height / bitMapHeight;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		return Bitmap.createBitmap(bitmap, 0, 0, bitMapWidth, bitMapHeight,
				matrix, true);

	}
}
