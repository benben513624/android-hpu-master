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
import com.hpu.commun.domain.Notice;
import com.hpu.commun.server.CommonServer;
import com.hpu.commun.server.CommonServer.OnResultListener;
import com.hpu.commun.utils.ListCache;
import com.hpu.commun.utils.ListCache.ClassEnum;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.SDUtil;
import com.hpu.commun.utils.ToastUtil;

/**
 * 最新公告页面
 * 
 * @author xst
 * 
 */
public class NoticeActivity extends BaseActivity {
	private ListView lv_notice;
	private ArrayList<Notice> list;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_notice);
		superSetTitleText("最新公告");
		list = new ArrayList<Notice>();

		initView();
		initData();
	}

	private void initView() {
		lv_notice = (ListView) findViewById(R.id.lv_notice);
		adapter = new MyAdapter();
		lv_notice.setAdapter(adapter);
		tv_refresh.setVisibility(View.VISIBLE);
		tv_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ProgressUtil.showWaitting(NoticeActivity.this);
				getNetNote();

			}
		});

		lv_notice.setOnItemClickListener(new OnItemClickListener() {
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

	private void initData() {

		getNotice();

	}

	private void getNotice() {
		ProgressUtil.showWaitting(this);
		ArrayList<Notice> list2 = ListCache.getList(SDUtil.getProjectDir()
				+ "/notice", ClassEnum.Notice);
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

	void getNetNote() {
		if (!NetUtils.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络不可用");
			ProgressUtil.dismiss();
			return;
		}
		CommonServer.getNote(this, new OnResultListener<List<Notice>>() {
			@Override
			public void onSuccessed(List<Notice> result) {
				if (result.size() > 0) {
					list.clear();
					list.addAll(result);
					adapter.notifyDataSetChanged();
					ListCache.save(SDUtil.getProjectDir() + "/notice", list);
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
				convertView = View.inflate(NoticeActivity.this,
						R.layout.item_notice, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewHolder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Notice notice = list.get(arg0);
			viewHolder.tv_title.setText(notice.getTitle());
			viewHolder.tv_date.setText(notice.getDate());

			return convertView;
		}
	}

	class ViewHolder {
		TextView tv_title, tv_date;
	}

}
