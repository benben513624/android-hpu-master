package com.hpu.commun.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharUtil {

	/**
	 * �Ѵӽ���ϵͳץ���Ŀα���ߵġ��ڴΡ��������� �滻�ɰ���������
	 * 
	 * @param str
	 * @return
	 */
	public static int switchChar(String str) {
		int num[] = { 1, 2, 3, 4, 5 };
		String hanzi[] = { "һ", "��", "��", "��", "��" };
		for (int i = 0; i < hanzi.length; i++) {
			if (hanzi[i].equals(str.trim())) {
				return num[i];
			}
		}
		return 0;

	}

	/**
	 * �Ѵӽ���ϵͳץ���Ŀα���ߵġ��ܴΡ��������� �滻������
	 */
	public static String replaceCN(String str) {
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	/**
	 * ��1-5,12-15�������ܴ�ת��int[],Ч��{1,2,3,4,5,12,13,14,15}
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
