package com.hpu.commun.ui.ecard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.domain.ECardUserInfo;
import com.hpu.commun.server.ECardServer;
import com.hpu.commun.server.ECardServer.OnEcardResultListener;
import com.hpu.commun.ui.BaseActivity;
import com.hpu.commun.ui.LoginActivity;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.SDUtil;
import com.hpu.commun.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ECardActivity extends BaseActivity {
	private TextView tv_name, tv_emoney, tv_num;
	private TextView tv_record;
	private ImageView iv_photo;
	private ImageLoader loader;
	private ECardUserInfo user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.fragment_ecard);
		superSetTitleText("一卡通");
		initView();
		initData();

	}

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_emoney = (TextView) findViewById(R.id.tv_emoney);
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		loader = ImageLoader.getInstance();
	}

	private void initData() {
		user = new ECardUserInfo();
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
		} else {
			ProgressUtil.showWaitting(this);
			ECardServer.HomePage(this, username, password,
					new OnEcardResultListener<ECardUserInfo>() {

						@Override
						public void onSuccessed(ECardUserInfo result) {
							tv_name.setText(result.getName());
							tv_num.setText(result.getNum());
							tv_emoney.setText(result.getEmoney());
							loader.displayImage(
									"file://" + SDUtil.getProjectDir()
											+ "/photo.jpg", iv_photo);
							user = result;
							ProgressUtil.dismiss();
						}

						@Override
						public void onError(int errorCode, String error) {
							ProgressUtil.dismiss();

						}
					});
		}

	}

	public void zaocao(View v) {
		startActivity(new Intent(this, ZCActivity.class));
		overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);

	}

	public void chongzhi(View v) {
		startActivity(new Intent(this, CZActivity.class));
		overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);

	}

	public void xiaofei(View v) {
		startActivity(new Intent(this, XFActivity.class));
		overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);
	}

	public void userinfo(View v) {
		Intent intent = new Intent(this, EUserInfoActivity.class);
		intent.putExtra("user", user);
		startActivity(intent);
		overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);

	}

	public void notice(View v) {
		startActivity(new Intent(this, ENoticeActivity.class));
		overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);
	}

}
