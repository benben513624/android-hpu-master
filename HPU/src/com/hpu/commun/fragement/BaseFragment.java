package com.hpu.commun.fragement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hpu.commun.utils.SP;

public abstract class BaseFragment extends Fragment {
	protected String username;
	protected String password;
	protected Activity context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		username = (String) SP.get(context, "username", "");
		password = (String) SP.get(context, "password", "");
		View view = initView(inflater);
		initData();
		setListener();
		return view;
	}

	protected abstract void setListener();

	protected abstract void initData();

	protected abstract View initView(LayoutInflater inflater);

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (username.equals("") || password.equals("")) {
			username = (String) SP.get(context, "username", "");
			password = (String) SP.get(context, "password", "");
			return;
		}
	}

}
