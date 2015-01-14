package com.hpu.commun;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.Bmob;

import com.hpu.commun.adapter.MainFragementAdapter;
import com.hpu.commun.fragement.ECardFragment;
import com.hpu.commun.fragement.HomeFragment;
import com.hpu.commun.fragement.MoreFragment;
import com.hpu.commun.view.PageIndicator;
import com.hpu.commun.view.PageIndicator.Callback;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends FragmentActivity {

	private ViewPager vp_main;
	private LinearLayout ll_title_group;
	private ArrayList<Fragment> list;
	private MainFragementAdapter adapter;
	private PageIndicator indicator;
	private int childCount;
	private int lastPosition;
	private int color;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bmob.initialize(this, "e3cd70cf711d04c7bc4b2a9f2b268673");
		color = Color.parseColor("#66000000");
		initView();
		initData();
		initImageLoader();
	}
	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)// Remove
				// app
				.build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// String truename = (String) SP.get(this, "truename", "xxx");
		// tv_truename.setText(truename);
		// tv_truename.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		// tv_truename.getPaint().setAntiAlias(true);
	}

	// 初始化布局
	private void initView() {
		vp_main = (ViewPager) findViewById(R.id.vp_main);
		ll_title_group = (LinearLayout) findViewById(R.id.ll_title_group);
		indicator = (PageIndicator) findViewById(R.id.indicator_main);
	}

	// 填充布局内容
	private void initData() {
		// 初始化Adapter
		list = new ArrayList<Fragment>();
		list.add(new HomeFragment());
		list.add(new ECardFragment());
		list.add(new MoreFragment());
		// list.add(new HomeFragment());
		adapter = new MainFragementAdapter(getSupportFragmentManager(), list);
		vp_main.setAdapter(adapter);
		// vp_main.setPersistentDrawingCache(2);
		vp_main.setOffscreenPageLimit(2);
		// 初始化指示器
		indicator.setColor(Color.parseColor("#33000000"));
		indicator.isCircle(false);
		indicator.setViewPager(vp_main);
		// 回调指示器位置的改变，原型是viewpage的onPageSelected(int position);
		indicator.getPosition(new Callback() {
			@Override
			public void onPageChanged(int position) {
				// 这里不用再调用vp_main.setCurrentItem(pos)了,切记
				// ll_title_group的第position孩子设为选中状态，第lastPosition孩子设为不选中状态
				((TextView) ll_title_group.getChildAt(lastPosition))
						.setTextColor(Color.WHITE);
				((TextView) ll_title_group.getChildAt(position))
						.setTextColor(color);
				lastPosition = position;// 记住ViewPage的位置
			}

		});
		// 处理四个选项的点击事件
		childCount = ll_title_group.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final TextView view = (TextView) ll_title_group.getChildAt(i);
			final int pos = i;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					vp_main.setCurrentItem(pos);// 必须调用，否则不切换
					((TextView) ll_title_group.getChildAt(lastPosition))
							.setTextColor(Color.WHITE);
					view.setTextColor(color);
					lastPosition = pos;// 记住ViewPage的位置
				}
			});
		}
	}
	long[] mHits = new long[2]; // 五次点击改成5
	/**
	 * 返回退出
	 */
	@Override
	public void onBackPressed() {
		System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
		mHits[mHits.length - 1] = SystemClock.uptimeMillis();
		if (mHits[0] >= SystemClock.uptimeMillis() - 1500) {
			this.finish();
			return;
		}
		// if (sp.getBoolean("exitHint", true)) {
		com.hpu.commun.utils.ToastUtil.show(this, "再按一次退出");
		// }
	}

}
