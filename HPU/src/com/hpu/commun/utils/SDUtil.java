package com.hpu.commun.utils;

import java.io.File;

import android.os.Environment;

public class SDUtil {

	public static boolean sdIsAvail() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 返回自定义工程路径
	 * 
	 * @return
	 */
	public static String getProjectDir() {
		String dir = null;
		if (sdIsAvail()) {
			dir = Environment.getExternalStorageDirectory().getPath()
					+ File.separatorChar + "HPU";
			File f = new File(dir);
			if (!f.exists()) {
				f.mkdirs();
			}
			return dir;
		} else {
			return null;
		}
	}

}
