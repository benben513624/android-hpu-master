package com.hpu.commun.fragement;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hpu.commun.MyApplication;
import com.hpu.commun.R;
import com.hpu.commun.domain.ECardUserInfo;
import com.hpu.commun.server.ECardServer;
import com.hpu.commun.server.ECardServer.OnEcardResultListener;
import com.hpu.commun.ui.LoginActivity;
import com.hpu.commun.ui.ecard.CZActivity;
import com.hpu.commun.ui.ecard.ENoticeActivity;
import com.hpu.commun.ui.ecard.EUserInfoActivity;
import com.hpu.commun.ui.ecard.XFActivity;
import com.hpu.commun.ui.ecard.ZCActivity;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.SDUtil;
import com.hpu.commun.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ECardFragment extends BaseFragment implements OnClickListener {
	private TextView tv_name, tv_emoney, tv_num;
	private ImageView iv_photo;
	private ImageLoader loader;
	private ECardUserInfo user;
	private ViewGroup rl_userinfo;
	private ViewGroup rl_chongzhi;
	private ViewGroup rl_xiaofei;
	private ViewGroup rl_zaocao;
	private ViewGroup rl_notice;

	@Override
	protected void setListener() {
	}

	@Override
	protected void initData() {
		if (user == null) {
			user = new ECardUserInfo();
		}
		if (!NetUtils.isNetworkAvailable(context)) {
			ToastUtil.show(context, "网络不可用");
			ProgressUtil.dismiss();
			return;
		}
		// 网址操作 用户名密码为空时跳转到登录页面 不为空时后台登录
		if (username.equals("") || password.equals("")) {
			// startActivity(new Intent(context, LoginActivity.class));
			// ProgressUtil.dismiss();
			return;
		} else {
			ProgressUtil.showWaitting(context);
			ECardServer.HomePage(context, username, password,
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

	@Override
	protected View initView(LayoutInflater inflater) {
		View v = inflater.inflate(R.layout.fragment_ecard, null, false);
		tv_name = (TextView) v.findViewById(R.id.tv_name);
		tv_num = (TextView) v.findViewById(R.id.tv_num);
		tv_emoney = (TextView) v.findViewById(R.id.tv_emoney);
		iv_photo = (ImageView) v.findViewById(R.id.iv_photo);
		rl_userinfo = (ViewGroup) v.findViewById(R.id.rl_userinfo);
		rl_chongzhi = (ViewGroup) v.findViewById(R.id.rl_chongzhi);
		rl_xiaofei = (ViewGroup) v.findViewById(R.id.rl_xiaofei);
		rl_zaocao = (ViewGroup) v.findViewById(R.id.rl_zaocao);
		rl_notice = (ViewGroup) v.findViewById(R.id.rl_notice);
		rl_userinfo.setOnClickListener(this);
		rl_chongzhi.setOnClickListener(this);
		rl_xiaofei.setOnClickListener(this);
		rl_zaocao.setOnClickListener(this);
		rl_notice.setOnClickListener(this);
		loader = ImageLoader.getInstance();
		return v;
	}

	private void userinfo() {
		Intent intent = new Intent(context, EUserInfoActivity.class);
		intent.putExtra("user", user);
		startActivity(intent);
		context.overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);

	}

	private void chongzhi() {
		startActivity(new Intent(context, CZActivity.class));
		context.overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);

	}

	private void xiaofei() {
		startActivity(new Intent(context, XFActivity.class));
		context.overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);
	}

	private void zaocao() {
		startActivity(new Intent(context, ZCActivity.class));
		context.overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);

	}

	private void notice() {
		startActivity(new Intent(context, ENoticeActivity.class));
		context.overridePendingTransition(R.anim.base_slide_right_in,
				R.anim.base_slide_left_out);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.rl_userinfo:
			userinfo();
			break;
		case R.id.rl_chongzhi:
			chongzhi();

			break;
		case R.id.rl_xiaofei:
			xiaofei();

			break;
		case R.id.rl_zaocao:
			zaocao();
			break;
		case R.id.rl_notice:
			notice();
			break;

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (username.equals("") || password.equals("")) {
			return;
		} else if (user == null || TextUtils.isEmpty(user.getStatus())) {
			if(((MyApplication)getActivity().getApplication()).Logined==true){
			initData();
			((MyApplication)getActivity().getApplication()).Logined=false;
			}
		}
	}

}
