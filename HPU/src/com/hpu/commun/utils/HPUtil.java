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

	public static final String[] BUILDING = new String[] { "1�Ž�ѧ¥", "2�Ž�ѧ¥",
			"3�Ž�ѧ¥", "�����ۺ�¥", "����ۺ�¥", "�����ۺ�¥", "��е�ۺ�¥", "������ۺ�¥", "�����ۺ�¥",
			"��Դ�ۺ�¥", "���ۺ�¥", "����ϵ", "��ľ�ۺ�¥", "�Ŀ��ۺ�¥", "����ϵ", "�ʻ��ۺ�¥" };

	public static final int[] BUILDING_NO = new int[] { 1, 2, 3, 15, 8, 4, 5,
			18, 13, 9, 6, 16, 12, 17, 14, 7 };

	public static final String[] ZHOUCI = new String[] { "��1��", "��2��", "��3��",
			"��4��", "��5��", "��6��", "��7��", "��8��", "��9��", "��10��", "��11��", "��12��",
			"��13��", "��14��", "��15��", "��16��", "��17��", "��18��", "��19��", "��20��" };

	public static final String[] WEEK = new String[] { "��һ", "�ܶ�", "����", "����",
			"����", "����", "����" };

	/**
	 * ��ȡ�������ܼ�
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
	 * ��ǰ�ڼ���
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
	 * ��ȡ������һ�����յ�����
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
	 * ��ȡ���ܣ����ܣ������ܣ����ܣ������ܣ���һ�����յ�����,
	 * 
	 * @param index
	 *            0�����ܣ�+1�������ܣ�+2���������ܣ�-1�������ܣ��Դ�����
	 * @return
	 */
	public static List<String> getAllWeekDate(int index) {
		List<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		int weekly = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// ��������������⴦��
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
	 * ��ȡ���ܣ����ܣ������ܣ����ܣ������ܣ���һ�����յ�����,(һ��ͨ��)
	 * 
	 * @param index
	 *            0�����ܣ�+1�������ܣ�+2���������ܣ�-1�������ܣ��Դ�����
	 * @return
	 */
	public static List<String> getAllWeekSimpleDate(int index) {
		List<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		int weekly = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		DecimalFormat df = new java.text.DecimalFormat("00");
		// ��������������⴦��
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
	 * ��ȡ��ǰ�ڴ�
	 * 
	 * @return
	 */
	public static String getCurrentJc() {
		String s = "";
		Time t = new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�
		t.setToNow(); // ȡ��ϵͳʱ�䡣
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
	 * ��ȡ��ǰ�ڴ�
	 * 
	 * @return
	 */
	public static int getCurrentJc2() {
		int s = 1;
		Time t = new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�
		t.setToNow(); // ȡ��ϵͳʱ�䡣
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
	// * ��ȡ����ʱ��
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
	// long timeGap = currentSeconds - timestamp / 1000;// ������ʱ���������
	// String timeStr = null;
	// if (timeGap > 24 * 60 * 60) {// 1������
	// timeStr = timeGap / (24 * 60 * 60) + "��ǰ";
	// } else if (timeGap > 60 * 60) {// 1Сʱ-24Сʱ
	// timeStr = timeGap / (60 * 60) + "Сʱǰ";
	// } else if (timeGap > 60) {// 1����-59����
	// timeStr = timeGap / 60 + "����ǰ";
	// } else {// 1����-59����
	// timeStr = "�ո�";
	// }
	// return timeStr;
	// }

	public static String converTime(String date) {
		// ���˵�ʱ��
		long timestamp = getStandardTime(date);
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(timestamp);
		int y = calendar.get(Calendar.YEAR);
		int m = calendar.get(Calendar.MONTH) + 1;
		int d = calendar.get(Calendar.DATE);
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		// ��ǰʱ��
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
		if (cury == y) {// ����
			if (curm == m && Math.abs(curd - d) < 3) {// ������
				switch (Math.abs(curd - d)) {
				case 0:// ����
					if (curh > 12 && h < 12 && curh - h > 3)
						s = "����" + h + ":" + df.format(minute);
					else
						s = "����" + h + ":" + df.format(minute);
					break;
				case 1:// ����
					s = "����" + h + ":" + df.format(minute);
					break;
				case 2:// ǰ��
					s = "ǰ��" + h + ":" + df.format(minute);
					break;
				default:
					break;
				}
			} else {
				s = m + "-" + d + " " + h + ":" + df.format(minute);
			}
		} else {
			// �������
			s = y + "-" + m + "-" + d + " " + h + ":" + df.format(minute);
		}

		return s;
	}

	public static String getStandardTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM��dd�� HH:mm");
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
	 * ��ȡ��������(һ��ͨ��)
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
	 * ��ȡ���µ�һ��(һ��ͨ��)
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
