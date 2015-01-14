package com.hpu.commun.ui.ecard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.domain.EDeposit;
import com.hpu.commun.server.ECardServer;
import com.hpu.commun.server.ECardServer.OnEcardResultListener;
import com.hpu.commun.ui.BaseActivity;
import com.hpu.commun.ui.LoginActivity;
import com.hpu.commun.utils.HPUtil;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.ToastUtil;
import com.hpu.commun.view.MyListView;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class CZActivity extends BaseActivity {

	private String curDate = HPUtil.getCurDate();
	private String firstDay = HPUtil.getFirstDay();
	private TextView tv_startdate, tv_enddate, tv_total;
	private List<EDeposit> list;
	private MyListView listview;
	private MyAdapter myAdapter;
	private CalendarPickerView dialogView;;
	private AlertDialog theDialog;
	private Calendar nextYear;
	private Calendar lastYear;
	private LinearLayout ll_search;
	private float total = 0;
	private DecimalFormat fnum = new DecimalFormat("##0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_chongzhi);
		superSetTitleText("充值记录");
		initView();
		initData();
	}

	private void initView() {
		listview = (MyListView) findViewById(R.id.list);
		tv_startdate = (TextView) findViewById(R.id.tv_startdate);
		tv_enddate = (TextView) findViewById(R.id.tv_enddate);
		tv_total = (TextView) findViewById(R.id.tv_total);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
		ll_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (theDialog != null && theDialog.isShowing() == false)
					theDialog.show();
			}
		});
	}

	private void initData() {
		list = new ArrayList<EDeposit>();
		myAdapter = new MyAdapter();
		listview.setAdapter(myAdapter);
		getData(firstDay, curDate);
		nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		lastYear = Calendar.getInstance();
		lastYear.add(Calendar.YEAR, -1);

		tv_startdate.setText(firstDay);
		tv_enddate.setText(curDate);
		initDialog();
	}

	private void initDialog() {
		dialogView = (CalendarPickerView) getLayoutInflater() //
				.inflate(R.layout.dialog_customized, null, false);
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, 1);
		Date nextDate = today.getTime();
		ArrayList<Date> dates = new ArrayList<Date>();
		today.add(Calendar.DATE, -6);
		dates.add(today.getTime());
		today.add(Calendar.DATE, 4);
		dates.add(today.getTime());
		dialogView.init(lastYear.getTime(), nextDate) //
				.inMode(SelectionMode.RANGE) //
				.withSelectedDates(dates);
		theDialog = new AlertDialog.Builder(CZActivity.this)
				.setTitle("请选择一段日期").setView(dialogView)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						List<Date> selectedDates = dialogView
								.getSelectedDates();
						String startdate = HPUtil.formatDate(selectedDates
								.get(0));
						String enddate = HPUtil.formatDate(selectedDates
								.get(selectedDates.size() - 1));
						tv_enddate.setText(enddate);
						tv_startdate.setText(startdate);
						getData(startdate, enddate);
					}
				}).create();

	}

	private void getData(String startdate, String enddate) {
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
			ECardServer.DepositRecord(this, startdate, enddate,
					new OnEcardResultListener<List<EDeposit>>() {

						@Override
						public void onSuccessed(List<EDeposit> result) {
							if (result.size() > 0 && result != null) {
								list.clear();
								list.addAll(result);
								myAdapter.notifyDataSetChanged();

								total = 0;
								for (int i = 0; i < result.size(); i++) {
									String money = result.get(i).getMoney();
									float f = Float.parseFloat(money);
									total += f;
								}
								tv_total.setText("总计:" + fnum.format(total));
							} else {
								list.clear();
								myAdapter.notifyDataSetChanged();
							}
							ProgressUtil.dismiss();
						}

						@Override
						public void onError(int errorCode, String error) {
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
				convertView = View.inflate(CZActivity.this,
						R.layout.item_chongzhi, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(viewHolder);
				viewHolder.tv_money = (TextView) convertView
						.findViewById(R.id.tv_money);
				viewHolder.tv_remoney = (TextView) convertView
						.findViewById(R.id.tv_remoney);
				viewHolder.tv_terminal = (TextView) convertView
						.findViewById(R.id.tv_terminal);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			EDeposit eDeposit = list.get(arg0);
			// String[] split = eZcRecord.getDate().split(" ");
			viewHolder.tv_date.setText(eDeposit.getDate());
			viewHolder.tv_money.setText(eDeposit.getMoney());
			viewHolder.tv_remoney.setText(eDeposit.getRe_money());
			viewHolder.tv_terminal.setText(eDeposit.getTerminal());
			return convertView;
		}
	}

	class ViewHolder {
		TextView tv_date, tv_money, tv_remoney, tv_terminal;
	}
}
