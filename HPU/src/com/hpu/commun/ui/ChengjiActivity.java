package com.hpu.commun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.domain.Chengji;
import com.hpu.commun.server.XXMHServer;
import com.hpu.commun.server.XXMHServer.OnResultListener;
import com.hpu.commun.utils.ListCache;
import com.hpu.commun.utils.ListCache.ClassEnum;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.SP;
import com.hpu.commun.utils.ToastUtil;

public class ChengjiActivity extends BaseActivity {
	private ListView lv_cj;
	private ArrayList<Chengji> list;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_chengji);
		superSetTitleText("成绩查询");
		initView();
		initData();
	}

	private void initData() {
		getChengji();

	}

	/**
	 * 初始化控件
	 * 
	 * @param v
	 */
	private void initView() {
		lv_cj = (ListView) findViewById(R.id.lv_cj);
		tv_refresh.setVisibility(View.VISIBLE);
		tv_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProgressUtil.showWaitting(ChengjiActivity.this);
				getNetData();

			}
		});
		list = new ArrayList<Chengji>();
		adapter = new MyAdapter();
		lv_cj.setAdapter(adapter);

	}

	/**
	 * 获取成绩列表并显示
	 */
	private void getChengji() {
		ProgressUtil.showWaitting(this);
		// 先判断缓存是否有
		ArrayList<Chengji> list2 = ListCache.getList(getFilesDir()
				.getAbsolutePath() + "/chengji", ClassEnum.ChengJi);
		try {
			if (list2 != null && list2.size() > 0) {
				list.clear();
				list.addAll(list2);
				adapter.notifyDataSetChanged();
				ProgressUtil.dismiss();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ProgressUtil.dismiss();
		}
		// 没有缓存,获取网络
		getNetData();

	}

	private void getNetData() {
		if (!NetUtils.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络不可用");
			ProgressUtil.dismiss();
			return;
		}
		// 网址操作 用户名密码为空时跳转到登录页面 不为空时后台登录
		String username = (String) SP.get(this, "username", "");
		String password = (String) SP.get(this, "password", "");
		if (username.equals("") || password.equals("")) {
			startActivity(new Intent(this, LoginActivity.class));
			ProgressUtil.dismiss();
			finish();
		} else {
			ProgressUtil.showWaitting(this);
			XXMHServer.getChengji(this, username, password,
					new OnResultListener<List<Chengji>>() {
						@Override
						public void onSuccessed(List<Chengji> result) {
							if (result.size() > 0) {
								list.clear();
								list.addAll(result);
								adapter.notifyDataSetChanged();
								ProgressUtil.dismiss();
							}
						}

						@Override
						public void onError(int errorCode, String error) {
							ToastUtil.show(ChengjiActivity.this, error);
							ProgressUtil.dismiss();
						}
					});
		}

	}

	private class MyAdapter extends BaseAdapter {
		public int getCount() {
			return list.size();
		}

		public View getView(int position, View view, ViewGroup parent) {
			Chengji chengji = list.get(position);
			if (view == null)
				view = View.inflate(ChengjiActivity.this,
						R.layout.item_chengji, null);
			TextView cjname = (TextView) view.findViewById(R.id.tv_cjname);
			TextView cjxf = (TextView) view.findViewById(R.id.tv_cjxf);
			TextView cjkcsx = (TextView) view.findViewById(R.id.tv_cjkcsx);
			TextView cjfs = (TextView) view.findViewById(R.id.tv_cjfs);
			if (chengji.getXf().equals("")) {
				cjname.setTextSize(16);
				cjname.setTextColor(Color.BLUE);
			} else {
				cjname.setTextSize(14);
				cjname.setTextColor(Color.BLACK);
			}
			cjname.setText(chengji.getCname());
			cjxf.setText(chengji.getXf());
			cjkcsx.setText(chengji.getKcsx());
			cjfs.setText(chengji.getChengji());
			return view;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
	}

}
