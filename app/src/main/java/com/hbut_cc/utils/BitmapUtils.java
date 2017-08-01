package com.hbut_cc.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils
{
	/**
	 * �ı�ͼƬ�Ĵ�С
	 * 
	 * @param bitmap
	 *            ԭbitmap
	 * @param width
	 *            ��bitmap�Ŀ�
	 * @param height
	 *            ��bitmap�ĸ�
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
