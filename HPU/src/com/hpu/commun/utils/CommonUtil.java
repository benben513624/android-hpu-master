package com.hpu.commun.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.util.DisplayMetrics;

public class CommonUtil {
	public static DisplayMetrics dm = new DisplayMetrics();

	public static int screenWidth(Activity context) {
		// 获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;

	}

	public static int screenHeigh(Activity context) {
		// 获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public static int restday(String data) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date enddate = formatter.parse(data);
			if (enddate == null) {
				return 404;
			} else {
				long endTime = enddate.getTime(); // date类型转成long类型
				long currentTime = System.currentTimeMillis();

				return (int) ((endTime - currentTime) / 1000 / 60 / 60 / 24);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
}
