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
	private static HashMap<Integer, KeCheng[]> map;// ���ڴ�ſα��map����
	private static ArrayList<KeCheng> list2;// ���ڴ��û�н��Ұ��ŵ�map����
	private GridView gv;
	private static LinearLayout ll; // titelBar�Ĳ���
	private MyAdapter myAdapter;
	private static int selectWeekNum;// �û�ѡ����ܴ�
	private static int curWeekNum;// ���ܵ��ܴ�
	private static int curDate; // ��ǰ���ܼ�
	private PopupWindow popWin;
	private ListView listView, lv_otherkc;
	private OtherAdapter otherAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_kctable);
		context = this;
		superSetTitleText("�γ̱�");
		initView();
		initData();
	}

	private void initView() {
		// ��ȡ��ǰ�ܴ�
		curWeekNum = HPUtil.getWeekNum();
		// ��ȡ��ǰ���ܼ�
		curDate = HPUtil.getWeekOfDate();
		// ˢ�±����м���ı�
		tv_selectzc.setText(Constant.weekNumStrings[curWeekNum - 1] + "*");
		ll_spinner.setVisibility(View.VISIBLE);

		map = new HashMap<Integer, KeCheng[]>();
		list2 = new ArrayList<KeCheng>();
		ll = (LinearLayout) findViewById(R.id.ll);
		gv = (GridView) findViewById(R.id.gv);
		// ��ʼ��û�н��Ұ��ŵĿα�
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
				intent.putExtra("weekly", "��");
				intent.putExtra("classroom", "��");
				intent.putExtra("credit", kc.getCredit());
				context.startActivity(intent);
				((Activity) context).overridePendingTransition(
						R.anim.base_slide_right_in, R.anim.base_slide_left_out);

			}
		});
		listView = new ListView(this);
		listView.setDivider(null); // ������Ŀ֮��ķָ���Ϊnull
		listView.setPadding(2, 14, 2, 10);
		listView.setVerticalScrollBarEnabled(false); // �ر�
		listView.setAdapter(new MyListAdapter());
		listView.setSelection(curWeekNum - 1);

		// ��ʼ��gridView��������
		myAdapter = new MyAdapter();
		gv.setAdapter(myAdapter);
		ll_spinner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popWin != null && popWin.isShowing()) {
					L.i("�Ѿ���ʾ������ʧ");
					popWin.dismiss();
				} else {
					L.i("Ӧ����ʧ������ʾ");
					// ��ʼ��popupWindow
					popWin = new PopupWindow(context);
					popWin.setWidth(v.getWidth()); // ���ÿ��
					int height = getWindowManager().getDefaultDisplay()
							.getHeight();
					int width = getWindowManager().getDefaultDisplay()
							.getWidth();
					// popWin.setHeight(height / 3); // ����popWin �߶�Ϊ��Ļ1/3
					popWin.setHeight(height / 3);
					popWin.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.ic_dropdown_week_bg));
					popWin.setContentView(listView); // ΪpopWindow�������
					popWin.setFocusable(true);
					popWin.setOutsideTouchable(true); // ���popWin �Դ��������Զ��ر�
					popWin.showAsDropDown(v, 0, 0);// ����
													// �������ڣ���ʾ��λ��
				}
			}
		});
	}

	public void initData() {

		// ˢ�±�ͷ
		refreshWeek(curWeekNum);
		// ��ʼ��ѡ���ܴ�Ϊ����
		selectWeekNum = HPUtil.getWeekNum();// ��ʼ���û�ѡ���ܴΣ�Ĭ��Ϊ����
		// ��ȡ�α��첽����GridView
		getKeBiao();

	}

	/**
	 * �Զ���������
	 * 
	 * @author LeeLay 2014-9-25
	 */
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 35;// �涨����35��Item����������
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
			// ��ʼ����ʾ����
			tv.setText("");
			// ��ʼ����ɫ
			tv.setBackgroundColor(Color.parseColor("#4De9f1f6"));
			zx_flag.setVisibility(View.INVISIBLE);

			// �������α��map��Ϊ���Ҽ����ϰ�����ǰposition
			if (!map.isEmpty() && map.keySet().contains(position)) {
				// ѭ����������
				for (KeCheng kc : map.get(position)) {
					// ���ѡ����ܴε��ڱ��ܣ���ʾ���ܵĿγ�
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
					// ���ѡ����ܴ�С�ڱ��ܣ���ʾ���еĿγ̺���ϰ����ɫ����Ϊ��ɫ
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
					// ����пΣ������ʾ�γ����ݵ�Dialog
					if (tv.getTag() instanceof KeCheng) {
						//
						KeCheng kc = (KeCheng) tv.getTag();
						Intent intent = new Intent(context,
								CourseInfoActivity.class);
						intent.putExtra("name", kc.getName());
						intent.putExtra("teacher", kc.getTeacher());
						intent.putExtra("kind", kc.getKind());
						intent.putExtra("weekly", kc.getWeekly() + "��");
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
	 * ��ȡ�α�
	 */
	private void getKeBiao() {
		ProgressUtil.showWaitting(this, "�α���ؽ���,��������Ŭ���Ľ�");

		// ���жϻ����Ƿ���
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
		// ����û�У���ȡ����
		getNetKeBiao();
	}

	private void getNetKeBiao() {
		if (!NetUtils.isNetworkAvailable(this)) {
			ToastUtil.show(this, "���粻����");
			ProgressUtil.dismiss();
			return;
		}
		// ��ַ���� �û�������Ϊ��ʱ��ת����¼ҳ�� ��Ϊ��ʱ��̨��¼
		if (username.equals("") || password.equals("")) {
			startActivity(new Intent(this, LoginActivity.class));
			ProgressUtil.dismiss();
			finish();
		} else {
			ProgressUtil.showWaitting(this, "�α���ؽ���,��������Ŭ���Ľ�");
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
	 * ˢ���ܴε�����
	 * 
	 * @param arg2�ܴ�
	 */
	public void refreshWeek(int arg2) {
		List<String> list = HPUtil.getAllWeekDate((arg2) - curWeekNum);
		// ��������䵽��ͷ
		for (int i = 1; i < ll.getChildCount(); i++) {
			TextView childAt = (TextView) ll.getChildAt(i);
			childAt.setText(list.get(i - 1) + "\n"
					+ Constant.weekStrings[i - 1]);
			// �仯�������ɫ
			if ((i == curDate) && (arg2 == curWeekNum)) {
				childAt.setBackgroundColor(Color.argb(0x55, 0xff, 0x00, 0x00));
			} else {
				childAt.setBackgroundColor(Color.alpha(1));
			}
		}
		// ����selectWeekNum��֪ͨ����������
		selectWeekNum = arg2;
		myAdapter.notifyDataSetChanged();
	}

	/**
	 * PopupWindow����listView������
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
					// ���������
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
