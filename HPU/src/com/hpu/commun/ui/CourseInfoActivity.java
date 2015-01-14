package com.hpu.commun.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hpu.commun.R;

public class CourseInfoActivity extends BaseActivity {
	private TextView name;
	private TextView teacher;
	private TextView kslx;
	private TextView xf;
	private TextView time;
	private TextView posetion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		superSetContentView(R.layout.course_info_item);
		superSetTitleText("¿Î±íÏêÇé");
		initView();
		initDate();
	}

	public void initView() {
		/*
		 * intent.putExtra("name", kc.getName()); intent.putExtra("teacher",
		 * kc.getTeacher()); intent.putExtra("kind", kc.getKind());
		 * intent.putExtra("weekly", kc.getWeekly());
		 * intent.putExtra("classroom", kc.getBuilding() + kc.getClassroom());
		 * intent.putExtra("credit", kc.getCredit());
		 */

		name = (TextView) findViewById(R.id.tv_name);

		teacher = (TextView) findViewById(R.id.tv_teacher);

		kslx = (TextView) findViewById(R.id.tv_kslx);

		xf = (TextView) findViewById(R.id.tv_xf);

		time = (TextView) findViewById(R.id.tv_time);

		posetion = (TextView) findViewById(R.id.tv_posetion);
		initDate();

	}

	private void initDate() {

		name.setText(getIntent().getStringExtra("name"));
		teacher.setText(getIntent().getStringExtra("teacher"));
		kslx.setText(getIntent().getStringExtra("kind"));
		xf.setText(getIntent().getStringExtra("credit"));
		time.setText(getIntent().getStringExtra("weekly"));
		posetion.setText(getIntent().getStringExtra("classroom"));
	}

	public class ViewHolder {
		public ImageView userLogo;
		public TextView tv_username;
	}
}
