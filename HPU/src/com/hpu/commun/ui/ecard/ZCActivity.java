package com.hpu.commun.ui.ecard;

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
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.domain.EZcRecord;
import com.hpu.commun.server.ECardServer;
import com.hpu.commun.server.ECardServer.OnEcardResultListener;
import com.hpu.commun.ui.BaseActivity;
import com.hpu.commun.ui.LoginActivity;
import com.hpu.commun.utils.HPUtil;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.ToastUtil;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class ZCActivity extends BaseActivity {

	private LinearLayout ll_one, ll_two, ll_search;
	private TextView tv_text, tv_time, tv_place;
	private TextView tv_startdate, tv_enddate;
	private RadioGroup rg_selecter;
	private ListView lv_two;
	private List<EZcRecord> list;
	private MyAdapter myAdapter;
	private String curDate = HPUtil.getCurDate();
	private String firstDay = HPUtil.getFirstDay();
	private CalendarPickerView dialogView;
	private AlertDialog theDialog;
	private Calendar nextYear;
	private Calendar lastYear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_zaocao);
		superSetTitleText("早操考勤");
		initView();
		initData();
	}

	private void initData() {
		list = new ArrayList<EZcRecord>();
		myAdapter = new MyAdapter();
		lv_two.setAdapter(myAdapter);
		getDateOfDay();

		nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		lastYear = Calendar.getInstance();
		lastYear.add(Calendar.YEAR, -1);

		tv_startdate.setText(firstDay);
		tv_enddate.setText(curDate);
		getDate(firstDay, curDate);
		initDialog();
	}

	private void initView() {
		ll_one = (LinearLayout) findViewById(R.id.ll_one);
		ll_two = (LinearLayout) findViewById(R.id.ll_two);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
		tv_text = (TextView) findViewById(R.id.tv_text);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_place = (TextView) findViewById(R.id.tv_place);
		tv_startdate = (TextView) findViewById(R.id.tv_startdate);
		tv_enddate = (TextView) findViewById(R.id.tv_enddate);
		rg_selecter = (RadioGroup) findViewById(R.id.rg_selecter);
		lv_two = (ListView) findViewById(R.id.lv_two);
		rg_selecter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = 0;
				switch (checkedId) {
				case R.id.rb_first:
					id = 0;
					break;
				case R.id.rb_second:
					id = 1;
					break;
				case R.id.rb_third:
					id = 2;
					break;
				default:
					break;
				}
				changeView(id);
			}
		});
		ll_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (theDialog != null && theDialog.isShowing() == false)
					theDialog.show();
			}
		});
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

		theDialog = new AlertDialog.Builder(ZCActivity.this)
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
						getDate(startdate, enddate);
					}
				}).create();

	}

	void changeView(int index) {
		ll_one.setVisibility(View.INVISIBLE);
		ll_two.setVisibility(View.INVISIBLE);
		switch (index) {
		case 0:
			ll_one.setVisibility(View.VISIBLE);
			break;
		case 1:
			ll_two.setVisibility(View.VISIBLE);

			break;
		case 2:
			break;
		default:
			break;
		}
	}

	private void getDateOfDay() {

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
			ECardServer.ZCRecord(this, curDate, curDate,
					new OnEcardResultListener<List<EZcRecord>>() {

						@Override
						public void onSuccessed(List<EZcRecord> result) {
							if (result.size() > 0 && result != null) {
								EZcRecord record = result.get(0);
								tv_time.setText("时间:" + record.getTime());
								tv_place.setText("卡机:" + record.getNum());
							} else {
								tv_time.setVisibility(View.GONE);
								tv_place.setVisibility(View.GONE);
								tv_text.setText("你今天没打卡\n你家人知道吗?");
							}
							ProgressUtil.dismiss();
						}

						@Override
						public void onError(int errorCode, String error) {
							tv_time.setVisibility(View.GONE);
							tv_place.setVisibility(View.GONE);
							tv_text.setText("未知错误");
							ProgressUtil.dismiss();
						}
					});
		}
	}

	private void getDate(String startdate, String enddate) {
		if (!NetUtils.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络不可用");
			ProgressUtil.dismiss();
			return;
		}
		// 网址操作 用户名密码为空时跳转到登录页面 不为空时后台登录
		if (username.equals("") || password.equals("")) {
//			startActivity(new Intent(this, LoginActivity.class));
//			ProgressUtil.dismiss();
//			finish();
			return;
		} else {
			ProgressUtil.showWaitting(this);
			ECardServer.ZCRecord(this, startdate, enddate,
					new OnEcardResultListener<List<EZcRecord>>() {

						@Override
						public void onSuccessed(List<EZcRecord> result) {
							if (result.size() > 0 && result != null) {
								list.clear();
								list.addAll(result);
								myAdapter.notifyDataSetChanged();
							} else {
								list.clear();
								myAdapter.notifyDataSetChanged();
								// 显示出为空的错误页面
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
				convertView = View.inflate(ZCActivity.this,
						R.layout.item_zaocao, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(viewHolder);
				viewHolder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				viewHolder.tv_place = (TextView) convertView
						.findViewById(R.id.tv_place);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			EZcRecord eZcRecord = list.get(arg0);
			// String[] split = eZcRecord.getDate().split(" ");
			viewHolder.tv_time.setText(eZcRecord.getTime());
			viewHolder.tv_date.setText(eZcRecord.getDate());
			viewHolder.tv_place.setText(eZcRecord.getNum());
			return convertView;
		}
	}

	class ViewHolder {
		TextView tv_date, tv_time, tv_place;
	}
}
