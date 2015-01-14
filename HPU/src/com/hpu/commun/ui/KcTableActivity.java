package com.hpu.commun.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hpu.commun.Constant;
import com.hpu.commun.R;
import com.hpu.commun.domain.KeCheng;
import com.hpu.commun.server.XXMHServer;
import com.hpu.commun.server.XXMHServer.OnResultListener;
import com.hpu.commun.utils.CharUtil;
import com.hpu.commun.utils.HPUtil;
import com.hpu.commun.utils.L;
import com.hpu.commun.utils.ListCache;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.ToastUtil;

public class KcTableActivity extends BaseActivity {
	private Context context;
	private static HashMap<Integer, KeCheng[]> map;// 用于存放课表的map集合
	private static ArrayList<KeCheng> list2;// 用于存放没有教室安排的map集合
	private GridView gv;
	private static LinearLayout ll; // titelBar的布局
	private MyAdapter myAdapter;
	private static int selectWeekNum;// 用户选择的周次
	private static int curWeekNum;// 本周的周次
	private static int curDate; // 当前是周几
	private PopupWindow popWin;
	private ListView listView, lv_otherkc;
	private OtherAdapter otherAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_kctable);
		context = this;
		superSetTitleText("课程表");
		initView();
		initData();
	}

	private void initView() {
		// 获取当前周次
		curWeekNum = HPUtil.getWeekNum();
		// 获取当前是周几
		curDate = HPUtil.getWeekOfDate();
		// 刷新标题中间的文本
		tv_selectzc.setText(Constant.weekNumStrings[curWeekNum - 1] + "*");
		ll_spinner.setVisibility(View.VISIBLE);

		map = new HashMap<Integer, KeCheng[]>();
		list2 = new ArrayList<KeCheng>();
		ll = (LinearLayout) findViewById(R.id.ll);
		gv = (GridView) findViewById(R.id.gv);
		// 初始化没有教室安排的课表
		lv_otherkc = (ListView) findViewById(R.id.lv_otherkc);
		otherAdapter = new OtherAdapter();
		lv_otherkc.setAdapter(otherAdapter);
		lv_otherkc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				KeCheng kc = (KeCheng) list2.get(arg2);
				Intent intent = new Intent(context, CourseInfoActivity.class);
				intent.putExtra("name", kc.getName());
				intent.putExtra("teacher", kc.getTeacher());
				intent.putExtra("kind", kc.getKind());
				intent.putExtra("weekly", "无");
				intent.putExtra("classroom", "无");
				intent.putExtra("credit", kc.getCredit());
				context.startActivity(intent);
				((Activity) context).overridePendingTransition(
						R.anim.base_slide_right_in, R.anim.base_slide_left_out);

			}
		});
		listView = new ListView(this);
		listView.setDivider(null); // 设置条目之间的分隔线为null
		listView.setPadding(2, 14, 2, 10);
		listView.setVerticalScrollBarEnabled(false); // 关闭
		listView.setAdapter(new MyListAdapter());
		listView.setSelection(curWeekNum - 1);

		// 初始化gridView的适配器
		myAdapter = new MyAdapter();
		gv.setAdapter(myAdapter);
		ll_spinner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popWin != null && popWin.isShowing()) {
					L.i("已经显示，请消失");
					popWin.dismiss();
				} else {
					L.i("应经消失，请显示");
					// 初始化popupWindow
					popWin = new PopupWindow(context);
					popWin.setWidth(v.getWidth()); // 设置宽度
					int height = getWindowManager().getDefaultDisplay()
							.getHeight();
					int width = getWindowManager().getDefaultDisplay()
							.getWidth();
					// popWin.setHeight(height / 3); // 设置popWin 高度为屏幕1/3
					popWin.setHeight(height / 3);
					popWin.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.ic_dropdown_week_bg));
					popWin.setContentView(listView); // 为popWindow填充内容
					popWin.setFocusable(true);
					popWin.setOutsideTouchable(true); // 点击popWin 以处的区域，自动关闭
					popWin.showAsDropDown(v, 0, 0);// 设置
													// 弹出窗口，显示的位置
				}
			}
		});
	}

	public void initData() {

		// 刷新表头
		refreshWeek(curWeekNum);
		// 初始化选择周次为本周
		selectWeekNum = HPUtil.getWeekNum();// 初始化用户选择周次，默认为本周
		// 获取课表，异步更新GridView
		getKeBiao();

	}

	/**
	 * 自定义适配器
	 * 
	 * @author LeeLay 2014-9-25
	 */
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 35;// 规定好了35个Item，五行七列
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_table, null);
			final TextView tv = (TextView) view.findViewById(R.id.item_tv);
			final View zx_flag = view.findViewById(R.id.zx_flag);
			// 初始化显示内荣
			tv.setText("");
			// 初始化颜色
			tv.setBackgroundColor(Color.parseColor("#4De9f1f6"));
			zx_flag.setVisibility(View.INVISIBLE);

			// 如果代表课表的map不为空且键集合包含当前position
			if (!map.isEmpty() && map.keySet().contains(position)) {
				// 循环遍历数组
				for (KeCheng kc : map.get(position)) {
					// 如果选择的周次等于本周，显示本周的课程
					if (CharUtil.toIntArr(kc.getWeekly()).contains(
							selectWeekNum)) {
						RelativeLayout parent2 = (RelativeLayout) tv
								.getParent();
						parent2.setBackgroundColor(kc.getColor());
						// tv.setBackgroundColor();
						tv.setTag(kc);
						tv.setText(kc.getName() + kc.getBuilding()
								+ kc.getClassroom());
					}
					// 如果选择的周次小于本周，显示所有的课程和自习，颜色都设为灰色
					// else if (selectWeekNum < curWeekNum) {
					// RelativeLayout parent2 = (RelativeLayout) tv
					// .getParent();
					// parent2.setBackgroundColor(Color
					// .parseColor("#aa7faac5"));
					// // tv.setBackgroundColor();
					// tv.setTag(kc);
					// tv.setText(kc.getName() + kc.getBuilding()
					// + kc.getClassroom());
					// }
				}
			}

			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 如果有课，点击显示课程内容的Dialog
					if (tv.getTag() instanceof KeCheng) {
						//
						KeCheng kc = (KeCheng) tv.getTag();
						Intent intent = new Intent(context,
								CourseInfoActivity.class);
						intent.putExtra("name", kc.getName());
						intent.putExtra("teacher", kc.getTeacher());
						intent.putExtra("kind", kc.getKind());
						intent.putExtra("weekly", kc.getWeekly() + "周");
						intent.putExtra("classroom",
								kc.getBuilding() + kc.getClassroom());
						intent.putExtra("credit", kc.getCredit());
						context.startActivity(intent);
						((Activity) context).overridePendingTransition(
								R.anim.base_slide_right_in,
								R.anim.base_slide_left_out);
					}

				}
			});
			return view;
		}
	}

	/**
	 * 获取课表
	 */
	private void getKeBiao() {
		ProgressUtil.showWaitting(this, "课表加载较慢,我们正在努力改进");

		// 先判断缓存是否有
		Map<Integer, KeCheng[]> result = ListCache.getTable(username,
				getFilesDir().getAbsolutePath() + "/table");
		if (result != null && result.size() > 0) {
			// System.out.println(result.size());
			if (result.containsKey(-1)) {
				list2.clear();
				for (KeCheng kc : result.get(-1)) {
					list2.add(kc);
				}
				otherAdapter.notifyDataSetChanged();
			}
			if (result.size() > 0) {
				map.clear();
				map.putAll(result);
				if (map.containsKey(-1))
					map.remove(-1);
				myAdapter.notifyDataSetChanged();
			}
			ProgressUtil.dismiss();
			return;
		}
		// 缓存没有，再取网络
		getNetKeBiao();
	}

	private void getNetKeBiao() {
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
			ProgressUtil.showWaitting(this, "课表加载较慢,我们正在努力改进");
			XXMHServer.getTable(this, username, password,
					new OnResultListener<Map<Integer, KeCheng[]>>() {
						@Override
						public void onSuccessed(Map<Integer, KeCheng[]> result) {
							if (result.containsKey(-1)) {
								list2.clear();
								for (KeCheng kc : result.get(-1)) {
									list2.add(kc);
								}
								otherAdapter.notifyDataSetChanged();
							}
							if (result.size() > 0) {
								map.clear();
								map.putAll(result);
								if (map.containsKey(-1))
									map.remove(-1);
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

	/**
	 * 刷新周次的日期
	 * 
	 * @param arg2周次
	 */
	public void refreshWeek(int arg2) {
		List<String> list = HPUtil.getAllWeekDate((arg2) - curWeekNum);
		// 将日期填充到表头
		for (int i = 1; i < ll.getChildCount(); i++) {
			TextView childAt = (TextView) ll.getChildAt(i);
			childAt.setText(list.get(i - 1) + "\n"
					+ Constant.weekStrings[i - 1]);
			// 变化当天的颜色
			if ((i == curDate) && (arg2 == curWeekNum)) {
				childAt.setBackgroundColor(Color.argb(0x55, 0xff, 0x00, 0x00));
			} else {
				childAt.setBackgroundColor(Color.alpha(1));
			}
		}
		// 更改selectWeekNum，通知适配器更新
		selectWeekNum = arg2;
		myAdapter.notifyDataSetChanged();
	}

	/**
	 * PopupWindow下拉listView适配器
	 * 
	 * @author LeeLay 2014-10-5
	 */
	private class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return Constant.weekNumStrings.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_popuplist, null);
				holder = new ViewHolder();
				holder.tv_msg = (TextView) convertView
						.findViewById(R.id.tv_popuitem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_msg.setText(Constant.weekNumStrings[position]
					+ (position != curWeekNum - 1 ? "" : "*"));

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 设置输入框
					if (position != curWeekNum - 1) {
						tv_selectzc.setText(""
								+ Constant.weekNumStrings[position] + "");
					} else {
						tv_selectzc.setText(Constant.weekNumStrings[position]
								+ "*");
					}
					refreshWeek(position + 1);
					popWin.dismiss();
				}
			});

			return convertView;
		}
	}

	private class ViewHolder {
		TextView tv_msg;
	}

	class OtherAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list2.size();
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
		public View getView(int arg0, View view, ViewGroup arg2) {
			if (view == null)
				view = View.inflate(context, R.layout.item_other_kc, null);

			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_other = (TextView) view.findViewById(R.id.tv_other);
			KeCheng keCheng = list2.get(arg0);
			tv_name.setText(keCheng.getName());
			return view;
		}
	}

}
