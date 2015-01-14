package com.hpu.commun.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.utils.SP;

public abstract class BaseActivity extends Activity {
	protected TextView tv_refresh;
	protected String username;
	protected String password;
	protected LinearLayout ll_spinner;
	protected TextView tv_selectzc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		tv_refresh = (TextView) findViewById(R.id.tv_refresh);
		ll_spinner = (LinearLayout) findViewById(R.id.ll_spinner);
		tv_selectzc = (TextView) findViewById(R.id.tv_selectzc);
		username = (String) SP.get(this, "username", "");
		password = (String) SP.get(this, "password", "");
	}

	/**
	 * 设置内容填充
	 * 
	 * @param layoutResId
	 */
	public void superSetContentView(int layoutResId) {
		LinearLayout llContent = (LinearLayout) findViewById(R.id.ll_content);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		llContent.addView(v);
	}

	/**
	 * 设置titleBar标题
	 * 
	 * @param text
	 */
	public void superSetTitleText(String text) {
		((TextView) findViewById(R.id.tv_title)).setText(text);
	}

	public void back(View v) {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.base_slide_left_in,
				R.anim.base_slide_right_out);
	}
}
