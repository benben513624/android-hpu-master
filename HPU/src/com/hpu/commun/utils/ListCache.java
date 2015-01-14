package com.hpu.commun.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hpu.commun.domain.BorrowBook;
import com.hpu.commun.domain.Chengji;
import com.hpu.commun.domain.KeCheng;
import com.hpu.commun.domain.Notice;
import com.hpu.commun.domain.Report;

public class ListCache {

	/**
	 * 普通枚举
	 */
	public enum ClassEnum {
		Notice, Report, BorrowBook, ChengJi;
	}

	/**
	 * 保存object类型
	 * 
	 * @param path
	 * @param object
	 */
	public static void save(String path, Object object) {
		BufferedWriter writer = null;
		try {
			Gson gson = new Gson();
			String json = gson.toJson(object);
			writer = new BufferedWriter(new FileWriter(path), 1024);
			writer.write(json);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 泛型的方法不能用,先用枚举代替
	 * 
	 * @param path
	 * @param class1
	 * @return
	 */
	public static <T> ArrayList<T> getList(String path, ClassEnum num) {
		File file = new File(path);
		if (!file.exists())
			return null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String readString = "";
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				readString += currentLine;
			}
			ArrayList<T> list = new ArrayList<T>();
			switch (num) {
			case Notice:
				list = new Gson().fromJson(readString,
						new TypeToken<ArrayList<Notice>>() {
						}.getType());
				break;
			case BorrowBook:
				list = new Gson().fromJson(readString,
						new TypeToken<ArrayList<BorrowBook>>() {
						}.getType());
				break;
			case Report:
				list = new Gson().fromJson(readString,
						new TypeToken<ArrayList<Report>>() {
						}.getType());
				break;
			case ChengJi:
				list = new Gson().fromJson(readString,
						new TypeToken<ArrayList<Chengji>>() {
						}.getType());
				break;
			default:
				break;
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 保存String类型
	 * 
	 * @param path
	 * @param object
	 */
	public static void save(String path, String content) {
		BufferedWriter writer = null;
		try {
			String json = content;
			writer = new BufferedWriter(new FileWriter(path), 1024);
			writer.write(json);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Map<Integer, KeCheng[]> getTable(String username, String path) {
		File file = new File(path);
		if (!file.exists())
			return null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();

			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				sb.append(currentLine + "\n");
			}
			Map<Integer, KeCheng[]> table = TableUtil.getTable(username,
					sb.toString());
			return table;
		} catch (Exception e) {
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
