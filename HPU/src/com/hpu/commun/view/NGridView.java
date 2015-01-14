package com.hpu.commun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * ���ɻ�����GridView����ֹͬһ������������ɻ����ؼ���ͻ
 * 
 * @author LeeLay
 * 
 *         2014-9-25
 */
public class NGridView extends GridView {

	public NGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public NGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	// ͨ������dispatchTouchEvent��������ֹ����
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;// ��ֹGridview���л���
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
