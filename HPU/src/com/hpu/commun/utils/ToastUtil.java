package com.hpu.commun.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast������
 * @author LeeLay 2014-9-24
 */
public class ToastUtil {

	/**
	 * Ĭ��ע��
	 * @param context
	 * @param content
	 */
	public static void show(Context context, String content) {
		Toast.makeText(context, content, 0).show();
	}

	/**
	 * ����ʾ
	 * @param context
	 * @param content
	 */
	public static void showL(Context context, String content) {
		Toast.makeText(context, content, 1).show();
	}

	/**
	 * �������Toast
	 * @param context
	 */
	public static void showNetErr(Context context) {
		Toast.makeText(context, "�������������", 0).show();
	}

}
