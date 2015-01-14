package com.hpu.commun.fragement;

import com.hpu.commun.R;
import com.hpu.commun.ui.LoginActivity;
import com.hpu.commun.utils.DataCleanManager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class MoreFragment extends BaseFragment {

	private View ll_clean;

	@Override
	protected void setListener() {
		ll_clean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logout();
				
			}
		});

	}

	protected void logout() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				DataCleanManager.cleanAll(context);	
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Intent intent = new Intent(context, LoginActivity.class);
						intent.putExtra("logout", true);
						context.startActivity(intent);
						context.finish();
						context.overridePendingTransition(
								R.anim.base_slide_right_in,
								R.anim.base_slide_left_out);
					}
				});
			}
		}).start();
	
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected View initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_other, null);
		ll_clean = view.findViewById(R.id.ll_clean);
		return view;
	}

}
