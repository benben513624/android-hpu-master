package com.hpu.commun.fragement;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.ui.LoginActivity;
import com.hpu.commun.ui.ThanksActivity;
import com.hpu.commun.utils.SP;

public class HomeFragment extends BaseFragment {

	private String[] names = new String[8];
	private GridView gv;
	private ListView lv;
	private static String[] className = new String[] {
			"com.hpu.commun.ui.NoticeActivity",
			"com.hpu.commun.ui.ReportActivity",
			"com.hpu.commun.ui.FreeClassRoomActivity",
			"com.hpu.commun.ui.BorrowActivity",
			"com.hpu.commun.ui.KcTableActivity",
			"com.hpu.commun.ui.ChengjiActivity" };
	private static final int[] IMAGES = new int[] { R.drawable.ic_notice,
			R.drawable.ic_report, R.drawable.ic_free, R.drawable.ic_book,
			R.drawable.ic_table, R.drawable.ic_chengji };

	private static final String[] title = new String[]{
		"昨晚兰五发洪水了","圣诞节快乐","平安夜还在撸代码有木有","小伙伴们快来测试吧"
	};
	private static final String[] time = new String[]{
		"2014-12-28","2014-12-25","2014-12-24","2014-12-12"
	};
	@Override
	protected View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		gv = (GridView) view.findViewById(R.id.gv_home);
		lv = (ListView) view.findViewById(R.id.lv_home);
		gv.setAdapter(new GridAdapter());
		lv.setAdapter(new ListAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String username = (String) SP
						.get(getActivity(), "username", "");
				String password = (String) SP
						.get(getActivity(), "password", "");
				if ("".equals(username) || "".equals(password)) {
					context.startActivity(new Intent(context,
							LoginActivity.class));
					((Activity) context).overridePendingTransition(
							R.anim.base_slide_right_in,
							R.anim.base_slide_left_out);
				} else {
					context.startActivity(new Intent(context,
							ThanksActivity.class));
					((Activity) context).overridePendingTransition(
							R.anim.base_slide_right_in,
							R.anim.base_slide_left_out);
				}

			}
		});
		return view;
	}

	@Override
	protected void initData() {
		names = new String[] { "最新公告", "学术报告", "空闲教室", "借阅情况", "课程表", "成绩查询" };

	}

	@Override
	protected void setListener() {
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					context.startActivity(new Intent(context, Class
							.forName(className[arg2])));
					((Activity) context).overridePendingTransition(
							R.anim.base_slide_right_in,
							R.anim.base_slide_left_out);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	class GridAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
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
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_gvhome, null);
			TextView tv = (TextView) view.findViewById(R.id.item_tv);
			ImageView iv = (ImageView) view.findViewById(R.id.item_iv);
			tv.setText(names[arg0]);
			iv.setImageResource(IMAGES[arg0 % names.length]);
			return view;
		}
	}

	class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return title.length;
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
			View view = LayoutInflater.from(context).inflate(
					R.layout.item_lvhome, null);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_title.setText(title[arg0]);
			tv_time.setText(time[arg0]);
			return view;
		}

	}
}
