package com.hpu.commun.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.server.CommonServer;
import com.hpu.commun.server.CommonServer.OnResultListener;
import com.hpu.commun.utils.HPUtil;
import com.hpu.commun.utils.ProgressUtil;

/**
 * 空闲教室查询界面
 * 
 * @author xst
 * 
 */
public class FreeClassRoomActivity extends BaseActivity implements
		OnClickListener {

	private Spinner sn_bid;
	private Spinner sn_zhouci;
	private Spinner sn_week;
	private int curzc = HPUtil.getWeekNum();
	private int curweek = HPUtil.getWeekOfDate();
	private TextView tv_one;
	private TextView tv_two;
	private TextView tv_three;
	private TextView tv_four;
	private TextView tv_five;
	private ScrollView sv_free;
	private float y;
	private TextView tv_alter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_freeroom);
		superSetTitleText("空闲教室");
		initView();
		initData();
		setListener();
	}

	@SuppressLint("NewApi")
	private void setListener() {
		tv_refresh.setOnClickListener(this);
		sv_free.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					y = arg1.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					if (arg1.getY() - y > 100) {// 下滑
						tv_alter.setVisibility(View.VISIBLE);
						y = arg1.getY();
					} else if (arg1.getY() - y < -100) {// 上拉

						tv_alter.setVisibility(View.GONE);
						y = arg1.getY();
					}
					break;
				case MotionEvent.ACTION_UP:

					break;

				default:
					break;
				}

				return false;
			}
		});

	}

	private void initData() {
		tv_refresh.setVisibility(View.VISIBLE);
		tv_refresh.setText("查询");
		// 填充三个Spinner容器内容
		setArrayAdapter(sn_bid, HPUtil.BUILDING);
		setArrayAdapter(sn_zhouci, HPUtil.ZHOUCI);
		setArrayAdapter(sn_week, HPUtil.WEEK);
		// 设置三个Spinner默认选中值
		sn_bid.setSelection(2);
		sn_zhouci.setSelection(curzc - 1);
		sn_week.setSelection(curweek - 1);
		getNetData(sn_week.getSelectedItemPosition() + 1 + "",
				sn_zhouci.getSelectedItemPosition() + 1 + "", "2014-2015-1-1",
				HPUtil.BUILDING_NO[sn_bid.getSelectedItemPosition()] + "");
		tv_alter.setText("本周是第" + curzc + "周");
	}

	private void initView() {
		sn_bid = (Spinner) findViewById(R.id.sn_bid);
		sn_zhouci = (Spinner) findViewById(R.id.sn_zhouci);
		sn_week = (Spinner) findViewById(R.id.sn_week);
		tv_one = (TextView) findViewById(R.id.tv_one);
		tv_two = (TextView) findViewById(R.id.tv_two);
		tv_three = (TextView) findViewById(R.id.tv_three);
		tv_four = (TextView) findViewById(R.id.tv_four);
		tv_five = (TextView) findViewById(R.id.tv_five);
		tv_alter = (TextView) findViewById(R.id.tv_alter);
		sv_free = (ScrollView) findViewById(R.id.sv_free);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_refresh:

			getNetData(sn_week.getSelectedItemPosition() + 1 + "",
					sn_zhouci.getSelectedItemPosition() + 1 + "",
					"2014-2015-1-1",
					HPUtil.BUILDING_NO[sn_bid.getSelectedItemPosition()] + "");
			break;
		default:
			break;
		}

	}

	private void setArrayAdapter(Spinner sn, String[] array) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, array);
		sn.setAdapter(adapter);
	}

	/**
	 * 
	 * @param dqweek
	 *            当前星期几(1-7)
	 * @param dqzhou
	 *            当前周次(1-20)
	 * @param jxzhxh
	 *            当前学期2014-2015-1-1
	 * @param lh
	 *            教学楼号
	 */
	private void getNetData(final String dqweek, final String dqzhou,
			final String jxzhxh, final String lh) {
		ProgressUtil.showWaitting(this);
		CommonServer.getClassRoom(this, dqweek, dqzhou, jxzhxh, lh,
				new OnResultListener<String>() {
					@Override
					public void onSuccessed(String result) {
						updateView(result);
						ProgressUtil.dismiss();
					}

					@Override
					public void onError(int errorCode, String error) {
						ProgressUtil.dismiss();
					}
				});

	}

	private void updateView(String result) {
		String[] results = result.split("#");
		tv_one.setText(results[0].replaceAll(",", " "));
		tv_two.setText(results[1].replaceAll(",", " "));
		tv_three.setText(results[2].replaceAll(",", " "));
		tv_four.setText(results[3].replaceAll(",", " "));
		tv_five.setText(results[4].replaceAll(",", " "));

	}

}
