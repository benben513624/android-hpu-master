package com.hpu.commun.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * ������������
 * 
 * @author LeeLay 2014-9-24
 */
public class ProgressUtil {

	private static ProgressDialog progressDialog;

	public static void showWaitting(Context context) {
		dismiss();
		progressDialog = new ProgressDialog(context);// �����Զ�����ʽdialog
		progressDialog.setMessage("���Ժ�");
		progressDialog.setCancelable(true);// �������á����ؼ���ȡ��
		progressDialog.show();
	}

	public static void showWaitting(Context context, String message) {
		dismiss();
		progressDialog = new ProgressDialog(context);// �����Զ�����ʽdialog
		progressDialog.setMessage(message);
		progressDialog.setCancelable(true);// �������á����ؼ���ȡ��
		progressDialog.show();
	}

	public static void dismiss() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
