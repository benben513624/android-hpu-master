package com.hpu.commun.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.text.format.Time;

public class HPUtil {

	public static final String[] BUILDING = new String[] { "1号教学楼", "2号教学楼",
			"3号教学楼", "材料综合楼", "测绘综合楼", "电气综合楼", "机械综合楼", "计算机综合楼", "经管综合楼",
			"能源综合楼", "理化综合楼", "体育系", "土木综合楼", "文科综合楼", "音乐系", "资环综合楼" };

	public static final int[] BUILDING_NO = new int[] { 1, 2, 3, 15, 8, 4, 5,
			18, 13, 9, 6, 16, 12, 17, 14, 7 };

	public static final String[] ZHOUCI = new String[] { "第1周", "第2周", "第3周",
			"第4周", "第5周", "第6周", "第7周", "第8周", "第9周", "第10周", "第11周", "第12周",
			"第13周", "第14周", "第15周", "第16周", "第17周", "第18周", "第19周", "第20周" };

	public static final String[] WEEK = new String[] { "周一", "周二", "周三", "周四",
			"周五", "周六", "周日" };

	/**
	 * 获取今天是周几
	 */
	public static int getWeekOfDate() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		int k = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (k < 1)
			k = 7;
		return k;
	}

	/**
	 * 当前第几周
	 * 
	 * @return
	 */
	public static int getWeekNum() {
		int num = 1;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.CHINA);
			long to = new Date().getTime();
			long from = df.parse("2014-9-8 00:00:00").getTime();
			num = (int) Math.ceil((to - from) * 1.0 / (1000 * 60 * 60 * 24)
					/ 7.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * 获取本周周一到周日的日期
	 * 
	 * @return
	 */
	public static List<String> getWeekDate() {
		List<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		list.add(calendar.get(Calendar.MONTH) + 1 + "."
				+ calendar.get(Calendar.DATE));
		for (int i = 0; i < 6; i++) {
			calendar.add(Calendar.DATE, 1);
			String str = calendar.get(Calendar.MONTH) + 1 + "."
					+ calendar.get(Calendar.DATE);
			list.add(str);
		}
		return list;
	}

	/**
	 * 获取本周，上周，上上周，下周，下下周，周一到周日的日期,
	 * 
	 * @param index
	 *            0代表本周，+1代表下周，+2代表下下周，-1代表下周，以此类推
	 * @return
	 */
	public static List<String> getAllWeekDate(int index) {
		List<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		int weekly = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// 如果是星期日特殊处理
		if (weekly == 1) {
			calendar.add(Calendar.DATE, (index - 1) * 7);
		} else {
			calendar.add(Calendar.DATE, index * 7);
		}
		list.add(calendar.get(Calendar.MONTH) + 1 + "."
				+ calendar.get(Calendar.DATE));
		for (int i = 0; i < 6; i++) {
			calendar.add(Calendar.DATE, 1);
			String str = calendar.get(Calendar.MONTH) + 1 + "."
					+ calendar.get(Calendar.DATE);
			list.add(str);
		}
		return list;
	}

	/**
	 * 获取本周，上周，上上周，下周，下下周，周一到周日的日期,(一卡通用)
	 * 
	 * @param index
	 *            0代表本周，+1代表下周，+2代表下下周，-1代表下周，以此类推
	 * @return
	 */
	public static List<String> getAllWeekSimpleDate(int index) {
		List<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		int weekly = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		DecimalFormat df = new java.text.DecimalFormat("00");
		// 如果是星期日特殊处理
		if (weekly == 1) {
			calendar.add(Calendar.DATE, (index - 1) * 7);
		} else {
			calendar.add(Calendar.DATE, index * 7);
		}
		list.add(calendar.get(Calendar.YEAR) + "-"
				+ df.format(calendar.get(Calendar.MONTH) + 1) + "-"
				+ df.format(calendar.get(Calendar.DATE)));
		for (int i = 0; i < 6; i++) {
			calendar.add(Calendar.DATE, 1);
			String str = calendar.get(Calendar.YEAR) + "-"
					+ df.format(calendar.get(Calendar.MONTH) + 1) + "-"
					+ df.format(calendar.get(Calendar.DATE));
			list.add(str);
		}
		return list;
	}

	/**
	 * 获取当前节次
	 * 
	 * @return
	 */
	public static String getCurrentJc() {
		String s = "";
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int hour = t.hour; // 0-23
		// int minute = t.minute;

		if (hour >= 0 && hour <= 9) {
			s = "1,2";
		} else if (hour <= 11) {
			s = "3,4";
		} else if (hour <= 15) {
			s = "5,6";
		} else if (hour <= 18) {
			s = "7,8";
		} else if (hour <= 23) {
			s = "9,10";
		}
		return s;
	}

	/**
	 * 获取当前节次
	 * 
	 * @return
	 */
	public static int getCurrentJc2() {
		int s = 1;
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int hour = t.hour; // 0-23
		// int minute = t.minute;

		if (hour >= 0 && hour <= 9) {
			s = 1;
		} else if (hour <= 11) {
			s = 2;
		} else if (hour <= 15) {
			s = 3;
		} else if (hour <= 18) {
			s = 4;
		} else if (hour <= 23) {
			s = 5;
		}
		return s;
	}

	// /**
	// * 获取发表时间
	// *
	// * @param date
	// * @return
	// */
	// public static String converTime(String date) {
	// long timestamp = getStandardTime(date);
	//
	// Calendar calendar = Calendar.getInstance(Locale.CHINA);
	// calendar.setTimeInMillis(timestamp);
	// long currentSeconds = System.currentTimeMillis() / 1000;
	// long timeGap = currentSeconds - timestamp / 1000;// 与现在时间相差秒数
	// String timeStr = null;
	// if (timeGap > 24 * 60 * 60) {// 1天以上
	// timeStr = timeGap / (24 * 60 * 60) + "天前";
	// } else if (timeGap > 60 * 60) {// 1小时-24小时
	// timeStr = timeGap / (60 * 60) + "小时前";
	// } else if (timeGap > 60) {// 1分钟-59分钟
	// timeStr = timeGap / 60 + "分钟前";
	// } else {// 1秒钟-59秒钟
	// timeStr = "刚刚";
	// }
	// return timeStr;
	// }

	public static String converTime(String date) {
		// 传人的时间
		long timestamp = getStandardTime(date);
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(timestamp);
		int y = calendar.get(Calendar.YEAR);
		int m = calendar.get(Calendar.MONTH) + 1;
		int d = calendar.get(Calendar.DATE);
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		// 当前时间
		long curtime = System.currentTimeMillis();
		Calendar curcalendar = Calendar.getInstance(Locale.CHINA);
		curcalendar.setTimeInMillis(curtime);
		int cury = curcalendar.get(Calendar.YEAR);
		int curm = curcalendar.get(Calendar.MONTH) + 1;
		int curd = curcalendar.get(Calendar.DATE);
		int curh = curcalendar.get(Calendar.HOUR_OF_DAY);
		int curminute = calendar.get(Calendar.MINUTE);
		DecimalFormat df = new java.text.DecimalFormat("00");

		String s = date;
		if (cury == y) {// 本年
			if (curm == m && Math.abs(curd - d) < 3) {// 三天内
				switch (Math.abs(curd - d)) {
				case 0:// 当天
					if (curh > 12 && h < 12 && curh - h > 3)
						s = "上午" + h + ":" + df.format(minute);
					else
						s = "今天" + h + ":" + df.format(minute);
					break;
				case 1:// 昨天
					s = "昨天" + h + ":" + df.format(minute);
					break;
				case 2:// 前天
					s = "前天" + h + ":" + df.format(minute);
					break;
				default:
					break;
				}
			} else {
				s = m + "-" + d + " " + h + ":" + df.format(minute);
			}
		} else {
			// 其他年份
			s = y + "-" + m + "-" + d + " " + h + ":" + df.format(minute);
		}

		return s;
	}

	public static String getStandardTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		Date date = new Date(timestamp * 1000);
		sdf.format(date);
		return sdf.format(date);
	}

	public static long getStandardTime(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date2 = null;
		try {
			date2 = sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date2.getTime();
	}

	/**
	 * 获取今日日期(一卡通用)
	 * 
	 * @return
	 */
	public static String getCurDate() {

		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		DecimalFormat df = new java.text.DecimalFormat("00");
		String str = calendar.get(Calendar.YEAR) + "-"
				+ df.format(calendar.get(Calendar.MONTH) + 1) + "-"
				+ df.format(calendar.get(Calendar.DATE));
		return str;
	}

	/**
	 * 获取本月第一天(一卡通用)
	 * 
	 * @return
	 */
	public static String getFirstDay() {

		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		DecimalFormat df = new java.text.DecimalFormat("00");
		String str = calendar.get(Calendar.YEAR) + "-"
				+ df.format(calendar.get(Calendar.MONTH) + 1) + "-"
				+ df.format(01);
		return str;
	}

	public static String formatDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);

	}
}
