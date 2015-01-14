package com.hpu.commun.ui.ecard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.domain.ETotal;
import com.hpu.commun.server.ECardServer;
import com.hpu.commun.server.ECardServer.OnEcardResultListener;
import com.hpu.commun.ui.BaseActivity;
import com.hpu.commun.ui.LoginActivity;
import com.hpu.commun.utils.CommonUtil;
import com.hpu.commun.utils.HPUtil;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.ToastUtil;
import com.hpu.commun.view.MyListView;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class XFActivity extends BaseActivity {
	private com.hpu.commun.view.PieChart pieChart;
	private static final String[] DEFAULT_ITEMS_COLORS = { "#ff80FF",
			"#ffFF00", "#6A5ACD", "#20B2AA", "#00BFFF", "#CD5C5C", "#8B658B",
			"#CD853F", "#006400", "#FF4500", "#D8BFD8", "#4876FF", "#FF00FF",
			"#FF83FA", "#0000FF", "#363636", "#FFDAB9", "#90EE90", "#8B008B",
			"#00BFFF", "#00FF00", "#006400", "#00FFFF", "#668B8B", "#000080",
			"#008B8B" };
	private String[] select_colors;
	private float[] items;
	private int radius = 200;
	private ArrayList<ETotal> list;
	private MyListView listview;
	private MyAdapter myAdapter;
	private String curDate = HPUtil.getCurDate();
	private String firstDay = HPUtil.getFirstDay();
	private float total = 0;
	private CalendarPickerView dialogView;
	private LinearLayout ll_search;
	private AlertDialog theDialog;
	private Calendar nextYear;
	private Calendar lastYear;
	private TextView tv_enddate;
	private TextView tv_startdate;
	private DecimalFormat fnum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_xiaofei);
		radius = CommonUtil.screenWidth(this) / 12 * 4;
		superSetTitleText("消费记录");
		fnum = new DecimalFormat("##0.0");
		initView();
		initData();
	}

	private void getXF(String startdate, String enddate) {
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
			ECardServer.TTRecord(this, startdate, enddate,
					new OnEcardResultListener<List<ETotal>>() {

						@Override
						public void onSuccessed(List<ETotal> result) {
							if (result.size() > 0 && result != null) {
								list.clear();
								list.addAll(result);
								myAdapter.notifyDataSetChanged();
								total = 0;
								items = null;
								items = new float[result.size()];
								select_colors = null;
								select_colors = new String[result.size()];
								for (int i = 0; i < result.size(); i++) {
									String money = result.get(i).getMoney();
									String type = result.get(i).getType();
									float f = Float.parseFloat(money);
									items[i] = f;
									select_colors[i] = DEFAULT_ITEMS_COLORS[i
											% DEFAULT_ITEMS_COLORS.length];
									total += f;
								}
								pieChart.setItem(items);
								pieChart.setColors(select_colors);
								pieChart.notifyDraw();
							} else {
								list.clear();
								myAdapter.notifyDataSetChanged();
								// 显示出为空的错误页面
								pieChart.setItem(null);
								pieChart.notifyDraw();
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

	private void initView() {

		initChart();
		listview = (MyListView) findViewById(R.id.list);
		tv_startdate = (TextView) findViewById(R.id.tv_startdate);
		tv_enddate = (TextView) findViewById(R.id.tv_enddate);
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
		list = new ArrayList<ETotal>();
		myAdapter = new MyAdapter();
		listview.setAdapter(myAdapter);
		getXF(firstDay, curDate);

		nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		lastYear = Calendar.getInstance();
		lastYear.add(Calendar.YEAR, -1);

		tv_startdate.setText(firstDay);
		tv_enddate.setText(curDate);
		initDialog();
	}

	void initDialog() {
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
		theDialog = new AlertDialog.Builder(XFActivity.this)
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
						getXF(startdate, enddate);
					}
				}).create();
	}

	private void initChart() {
		pieChart = (com.hpu.commun.view.PieChart) findViewById(R.id.parbar_view);
		pieChart.setRadius(radius);

	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = View.inflate(XFActivity.this, R.layout.item_xiaofei,
					null);
			View view_rectangle = view.findViewById(R.id.view_rectangle);
			TextView tv_ratio = (TextView) view.findViewById(R.id.tv_ratio);
			TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
			TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
			ETotal eTotal = list.get(arg0);
			String money = eTotal.getMoney();
			tv_ratio.setText(fnum.format(Float.parseFloat(money) / total * 100)
					+ "%");
			String type = eTotal.getType();
			tv_type.setText(type);
			tv_money.setText(money);
			view_rectangle.setBackgroundColor(Color
					.parseColor(DEFAULT_ITEMS_COLORS[arg0
							% DEFAULT_ITEMS_COLORS.length]));
			return view;
		}
	}
}
