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
import com.hpu.commun.domain.BorrowBook;
import com.hpu.commun.server.XXMHServer;
import com.hpu.commun.server.XXMHServer.OnResultListener;
import com.hpu.commun.utils.CommonUtil;
import com.hpu.commun.utils.ListCache;
import com.hpu.commun.utils.ListCache.ClassEnum;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.ToastUtil;

/**
 * 图书借阅展示界面
 * 
 * @author xst
 * 
 */
public class BorrowActivity extends BaseActivity {
	private ListView lv_borrow;
	private List<BorrowBook> list;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_borrow);
		superSetTitleText("图书借阅");
		initView();
		initData();
	}

	private void initView() {
		list = new ArrayList<BorrowBook>();
		lv_borrow = (ListView) findViewById(R.id.lv_borrow);
		adapter = new MyAdapter();
		lv_borrow.setAdapter(adapter);
		tv_refresh.setVisibility(View.VISIBLE);
		tv_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ProgressUtil.showWaitting(BorrowActivity.this);
				getNetBorrow();
			}
		});
	}

	private void initData() {
		getBorrow();
	}

	private void getBorrow() {
		// 先判断缓存是否有
		ProgressUtil.showWaitting(this);
		ArrayList<BorrowBook> list2 = ListCache.getList(getFilesDir()
				.getAbsolutePath() + "/borrow", ClassEnum.BorrowBook);
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
		}
		// 缓存没有，再取网络
		getNetBorrow();

	}

	// 取网络数据
	protected void getNetBorrow() {
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
			XXMHServer.borrowBook(this, username, password,
					new OnResultListener<List<BorrowBook>>() {
						@Override
						public void onSuccessed(List<BorrowBook> result) {
							if (result.size() > 0) {
								list.clear();
								list.addAll(result);
								adapter.notifyDataSetChanged();
								ProgressUtil.dismiss();
								ListCache.save(getFilesDir().getAbsolutePath()
										+ "/borrow", list);
							}
						}

						@Override
						public void onError(int errorCode, String error) {
							ToastUtil.show(BorrowActivity.this, error);
							ProgressUtil.dismiss();
						}
					});
		}

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(BorrowActivity.this,
						R.layout.item_borrow, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_barcode = (TextView) convertView
						.findViewById(R.id.tv_barcode);
				viewHolder.tv_remainder = (TextView) convertView
						.findViewById(R.id.tv_remainder);
				viewHolder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				viewHolder.tv_startdate = (TextView) convertView
						.findViewById(R.id.tv_startdate);
				viewHolder.tv_enddate = (TextView) convertView
						.findViewById(R.id.tv_enddate);
				viewHolder.tv_other = (TextView) convertView
						.findViewById(R.id.tv_other);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			BorrowBook borrowBook = list.get(arg0);
			viewHolder.tv_barcode.setText("条码" + borrowBook.getBarcode());
			int restDay = CommonUtil.restday(borrowBook.getEnddate());
			if (restDay >= 0 && restDay <= 30) {
				viewHolder.tv_remainder.setTextColor(Color.GREEN);
				viewHolder.tv_remainder.setText("剩余" + restDay + "天");
			} else if (restDay < 0) {
				viewHolder.tv_remainder.setTextColor(Color.RED);
				viewHolder.tv_remainder.setText("超期" + restDay + "天");

			}

			viewHolder.tv_name.setText(borrowBook.getName());
			viewHolder.tv_startdate
					.setText("借阅日期：" + borrowBook.getStartdate());

			viewHolder.tv_enddate.setText("到期日期：" + borrowBook.getEnddate());

			viewHolder.tv_other
					.setText(borrowBook.getRenewday().equals("") ? "初次借"
							: "已续借");
			return convertView;
		}
	}

	class ViewHolder {
		TextView tv_barcode, tv_remainder, tv_name, tv_startdate, tv_enddate,
				tv_other;
	}

}
