package com.hpu.commun.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharUtil {

	/**
	 * 把从教务系统抓到的课表里边的“节次”属性内容 替换成阿拉伯数字
	 * 
	 * @param str
	 * @return
	 */
	public static int switchChar(String str) {
		int num[] = { 1, 2, 3, 4, 5 };
		String hanzi[] = { "一", "三", "五", "七", "九" };
		for (int i = 0; i < hanzi.length; i++) {
			if (hanzi[i].equals(str.trim())) {
				return num[i];
			}
		}
		return 0;

	}

	/**
	 * 把从教务系统抓到的课表里边的“周次”属性内容 替换掉中文
	 */
	public static String replaceCN(String str) {
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	/**
	 * 将1-5,12-15这样的周次转成int[],效果{1,2,3,4,5,12,13,14,15}
	 */
	public static ArrayList<Integer> toIntArr(String str) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();
		for (String s : str.split(",")) {
			int start = Integer.parseInt(s.split("-")[0].trim());
			int end = Integer.parseInt(s.split("-")[1].trim());
			for (int i = start; i <= end; i++) {
				sb.append(i + ",");
			}
		}
		String temp = sb.substring(0, sb.length() - 1);
		for (String s : temp.split(",")) {
			list.add(Integer.parseInt(s));
		}
		return list;
	}
}
