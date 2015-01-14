package com.hpu.commun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hpu.commun.MainActivity;
import com.hpu.commun.MyApplication;
import com.hpu.commun.R;
import com.hpu.commun.server.XXMHServer;
import com.hpu.commun.server.XXMHServer.OnResultListener;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.SP;
import com.hpu.commun.utils.ToastUtil;

public class LoginActivity extends BaseActivity {

	private EditText et_username;// 用户名
	private EditText et_password;// 密码
	private Button btn_help;// 帮助
	private TextView tv_yk;// 游客登录
	private Button btn_login;
	private boolean b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_login);
		b = getIntent().getBooleanExtra("logout", false);
		superSetTitleText(b==true?"退出":"登录");
		initView();
	}

	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_help = (Button) findViewById(R.id.btn_help);
		tv_yk = (TextView) findViewById(R.id.tv_yk);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_yk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LoginActivity.this.startActivity(new Intent(LoginActivity.this,
						MainActivity.class));

				LoginActivity.this.finish();
			}
		});
	}

	public void loginxx(View v) {
		btn_login.setEnabled(false);
		tv_yk.setEnabled(false);
		// 判断用户名密码是否为空
		final String username = et_username.getText().toString();
		final String password = et_password.getText().toString();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			ToastUtil.show(this, "用户名密码不能为空");
			btn_login.setEnabled(true);
			tv_yk.setEnabled(true);
			return;
		}
		// 判断是否联网
		if (!NetUtils.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络未开启");
			btn_login.setEnabled(true);
			tv_yk.setEnabled(true);
			return;
		}
		// 执行登录逻辑
		ProgressUtil.showWaitting(this, "正在登录…");
		XXMHServer.index(this, username, password,
				new OnResultListener<String>() {
					@Override
					public void onSuccessed(String result) {
						ToastUtil.show(LoginActivity.this, "欢迎归队:" + result);
						SP.put(LoginActivity.this, "username", username);
						SP.put(LoginActivity.this, "password", password);
						SP.put(LoginActivity.this, "truename", result);
						btn_login.setEnabled(true);
						tv_yk.setEnabled(true);
						ProgressUtil.dismiss();
						LoginActivity.this.startActivity(new Intent(
								LoginActivity.this, MainActivity.class));
((MyApplication)LoginActivity.this.getApplication()).Logined=true;
						LoginActivity.this.finish();
					}

					@Override
					public void onError(int errorCode, String error) {
						ToastUtil.show(LoginActivity.this, error);
						btn_login.setEnabled(true);
						tv_yk.setEnabled(true);
						ProgressUtil.dismiss();
					}
				});

	}
	
	long[] mHits = new long[2]; // 五次点击改成5
	/**
	 * 返回退出
	 */
	@Override
	public void onBackPressed() {
		if(b){
		System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
		mHits[mHits.length - 1] = SystemClock.uptimeMillis();
		if (mHits[0] >= SystemClock.uptimeMillis() - 1500) {
			this.finish();
			return;
		}
		// if (sp.getBoolean("exitHint", true)) {
		com.hpu.commun.utils.ToastUtil.show(this, "再按一次退出");
		// }
	}else {
		super.onBackPressed();
	}
	}
}
