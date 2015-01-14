package com.hpu.commun.ui.ecard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.domain.ECardUserInfo;
import com.hpu.commun.server.ECardServer;
import com.hpu.commun.server.ECardServer.OnEcardResultListener;
import com.hpu.commun.ui.BaseActivity;
import com.hpu.commun.ui.LoginActivity;
import com.hpu.commun.utils.L;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.SDUtil;
import com.hpu.commun.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EUserInfoActivity extends BaseActivity {
	private ECardUserInfo user;
	private ImageView iv_photo;
	private TextView tv_num, tv_name, tv_sex, tv_state, tv_estatus,
			tv_eassistance, tv_emoney;
	private ImageLoader loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_euserinfo);
		superSetTitleText("一卡通用户");
//		if (username.equals("") || password.equals("")) {
//			startActivity(new Intent(this, LoginActivity.class));
//			ProgressUtil.dismiss();
//			finish();
//		}
		initView();
		initData();
	}

	private void initData() {

		if (user == null || TextUtils.isEmpty(user.getStatus())) {
			L.i("满足条件了");
			if (!NetUtils.isNetworkAvailable(this)) {
				ToastUtil.show(this, "网络不可用");
				ProgressUtil.dismiss();
				return;
			}
			// 网址操作 用户名密码为空时跳转到登录页面 不为空时后台登录
			if (username.equals("") || password.equals("")) {
				startActivity(new Intent(this, LoginActivity.class));
				ProgressUtil.dismiss();
				finish();
				return;
			} else {
				L.i("执行了");
				user = new ECardUserInfo();
				ProgressUtil.showWaitting(this);
				ECardServer.HomePage(this, username, password,
						new OnEcardResultListener<ECardUserInfo>() {
							@Override
							public void onSuccessed(ECardUserInfo result) {
								L.i("成功了");
								user = result;
								loader.displayImage(
										"file://" + SDUtil.getProjectDir()
												+ "/photo.jpg", iv_photo);
								if (!TextUtils.isEmpty(username)) {
									tv_num.setText(username);
								}
								tv_name.setText(user.getName());
								tv_sex.setText(user.getSex());
								tv_state.setText(user.getStatus());
								tv_estatus.setText(user.getEstate());
								tv_eassistance.setText(user.getEassistance());
								tv_emoney.setText("余额：" + user.getEmoney());
								ProgressUtil.dismiss();
							}

							@Override
							public void onError(int errorCode, String error) {
								ProgressUtil.dismiss();
								L.i("失败了");
							}
						});
			}
		} else {
			loader.displayImage("file://" + SDUtil.getProjectDir()
					+ "/photo.jpg", iv_photo);
			if (!TextUtils.isEmpty(username)) {
				tv_num.setText(username);
			}
			tv_name.setText(user.getName());
			tv_sex.setText(user.getSex());
			tv_state.setText(user.getStatus());
			tv_estatus.setText(user.getEstate());
			tv_eassistance.setText(user.getEassistance());
			tv_emoney.setText("余额：" + user.getEmoney());
		}
	}

	private void initView() {
		user = (ECardUserInfo) getIntent().getSerializableExtra("user");
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_state = (TextView) findViewById(R.id.tv_state);
		tv_estatus = (TextView) findViewById(R.id.tv_estatus);
		tv_eassistance = (TextView) findViewById(R.id.tv_eassistance);
		tv_emoney = (TextView) findViewById(R.id.tv_emoney);
		loader = ImageLoader.getInstance();
	}
}
