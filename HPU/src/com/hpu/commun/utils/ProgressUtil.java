package com.hpu.commun.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 进度条工具类
 * 
 * @author LeeLay 2014-9-24
 */
public class ProgressUtil {

	private static ProgressDialog progressDialog;

	public static void showWaitting(Context context) {
		dismiss();
		progressDialog = new ProgressDialog(context);// 创建自定义样式dialog
		progressDialog.setMessage("请稍后…");
		progressDialog.setCancelable(true);// 不可以用“返回键”取消
		progressDialog.show();
	}

	public static void showWaitting(Context context, String message) {
		dismiss();
		progressDialog = new ProgressDialog(context);// 创建自定义样式dialog
		progressDialog.setMessage(message);
		progressDialog.setCancelable(true);// 不可以用“返回键”取消
		progressDialog.show();
	}

	public static void dismiss() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
