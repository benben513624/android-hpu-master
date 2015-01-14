package com.hpu.commun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hpu.commun.R;
import com.hpu.commun.domain.Report;
import com.hpu.commun.server.CommonServer;
import com.hpu.commun.server.CommonServer.OnResultListener;
import com.hpu.commun.utils.ListCache;
import com.hpu.commun.utils.ListCache.ClassEnum;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.SDUtil;
import com.hpu.commun.utils.ToastUtil;

/**
 * 学术报告界面
 * 
 * @author xst
 * 
 */
public class ReportActivity extends BaseActivity {
	private ListView lv_report;
	private ArrayList<Report> list;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_report);
		superSetTitleText("学术报告");
		list = new ArrayList<Report>();

		initView();
		initData();
	}

	private void initData() {
		getRePort();

	}

	private void getRePort() {
		ProgressUtil.showWaitting(this);
		ArrayList<Report> list2 = ListCache.getList(SDUtil.getProjectDir()
				+ "/report", ClassEnum.Report);
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
		getNetNote();

	}

	private void initView() {
		lv_report = (ListView) findViewById(R.id.lv_report);
		adapter = new MyAdapter();
		lv_report.setAdapter(adapter);
		tv_refresh.setVisibility(View.VISIBLE);
		tv_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ProgressUtil.showWaitting(ReportActivity.this);
				getNetNote();

			}
		});

		lv_report.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String href = list.get(arg2).getHref();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(href));
				startActivity(intent);
			}
		});
	}

	protected void getNetNote() {
		if (!NetUtils.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络不可用");
			ProgressUtil.dismiss();
			return;
		}
		// 获取网络数据
		CommonServer.getReport(this, new OnResultListener<List<Report>>() {
			@Override
			public void onSuccessed(List<Report> result) {
				if (result.size() > 0) {
					list.clear();
					list.addAll(result);
					adapter.notifyDataSetChanged();
					ListCache.save(SDUtil.getProjectDir() + "/report", list);

				}
				ProgressUtil.dismiss();
			}

			@Override
			public void onError(int errorCode, String error) {
				ProgressUtil.dismiss();
			}
		});

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
				convertView = View.inflate(ReportActivity.this,
						R.layout.item_report, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewHolder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(viewHolder);
				viewHolder.tv_address = (TextView) convertView
						.findViewById(R.id.tv_address);
				viewHolder.tv_author = (TextView) convertView
						.findViewById(R.id.tv_author);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Report repost = list.get(arg0);
			viewHolder.tv_title.setText(repost.getTitle());
			viewHolder.tv_date.setText(repost.getDate());
			viewHolder.tv_address.setText(repost.getAddress());
			viewHolder.tv_author.setText(repost.getAuthro());

			return convertView;
		}
	}

	class ViewHolder {
		TextView tv_author, tv_address, tv_title, tv_date;
	}

}
