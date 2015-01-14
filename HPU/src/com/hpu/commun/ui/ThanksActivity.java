package com.hpu.commun.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.listener.SaveListener;

import com.hpu.commun.R;
import com.hpu.commun.domain.Feedback;
import com.hpu.commun.utils.NetUtils;
import com.hpu.commun.utils.ProgressUtil;
import com.hpu.commun.utils.SP;
import com.hpu.commun.utils.ToastUtil;

public class ThanksActivity extends BaseActivity {
	private EditText et_content;
	private Button btn_send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.activity_thanks);
		superSetTitleText("��л����");
		et_content = (EditText) findViewById(R.id.et_content);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendFeedBack();

			}
		});
	}

	protected void sendFeedBack() {
		btn_send.setEnabled(false);
		if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
			ToastUtil.show(this, "���ݲ���Ϊ��");
			return;
		}
		if (!NetUtils.isNetworkAvailable(this)) {
			ToastUtil.show(this, "����δ����");
			return;
		}
		ProgressUtil.showWaitting(this);
		Feedback fb = new Feedback();
		fb.setContent(et_content.getText().toString().trim());
		fb.setUsername(username);
		fb.setTruename((String) SP.get(this, "truename", "δ֪"));
		fb.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				btn_send.setEnabled(true);
				ToastUtil.show(ThanksActivity.this, "�����ɹ�");
				ProgressUtil.dismiss();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				btn_send.setEnabled(true);
				ToastUtil.show(ThanksActivity.this, "����ʧ��,���������翪С����");
				ProgressUtil.dismiss();

			}
		});

	}
}
