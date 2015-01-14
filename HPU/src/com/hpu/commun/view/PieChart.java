package com.hpu.commun.view;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChart extends View {
	private float[] item;// ÿһ���ֵ
	private float total; // �ܹ���ֵ
	private String[] colors; // ����������ɫ
	private float[] itemsAngle;// ÿһ����ռ�ĽǶ�
	private float[] itemsBeginAngle;// ÿһ�����ʼ�Ƕ�
	private float[] itemsRate;// ÿһ��ռ�ı���
	private float radius = 200;// �뾶
	private DecimalFormat fnum;// ��ʽ��float

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		fnum = new DecimalFormat("##0.00");
	}

	public float[] getItem() {
		return item;
	}

	public void setItem(float[] item) {
		this.item = item;
		if (item != null && item.length > 0) {
			jsTotal();
			refreshItemsAngs();
		}
	}

	public String[] getColors() {
		return colors;
	}

	public void setColors(String[] colors) {
		this.colors = colors;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		float letftop = 0;
		float rightbottom = 2 * radius;
		float centerXY = radius;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		if (item == null || item.length == 0) {// ���ʲô���ݶ�û��
			paint.setTextSize(radius / 4);
			paint.setStyle(Paint.Style.FILL);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setColor(Color.parseColor("#000000"));
			FontMetrics fontMetrics = paint.getFontMetrics();
			float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
			float offY = fontTotalHeight / 2 - fontMetrics.bottom;
			float newY = centerXY + offY;
			canvas.drawText("����", centerXY, newY, paint);

		} else {
			// ������
			RectF oval = new RectF(letftop, letftop, rightbottom, rightbottom);
			for (int i = 0; i < item.length; i++) {
				paint.setColor(Color.parseColor(colors[i]));
				canvas.drawArc(oval, itemsBeginAngle[i], itemsAngle[i], true,
						paint);
			}
			// ����Բ
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.parseColor("#ffffff"));
			canvas.drawCircle(centerXY, centerXY, radius / 2, paint);
			// ����
			paint.setTextSize(radius / 4);
			paint.setStyle(Paint.Style.FILL);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setColor(Color.parseColor("#000000"));
			FontMetrics fontMetrics = paint.getFontMetrics();
			float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
			float offY = fontTotalHeight / 2 - fontMetrics.bottom;
			float newY = centerXY + offY;
			canvas.drawText(fnum.format(total) + "", centerXY, newY, paint);
		}
	}

	private void jsTotal() {
		total = 0;
		for (float i : item) {
			total += i;
		}
	}

	/**
	 * ����ÿ��item�Ĵ�С�����item��ռ�ĽǶȺ���ʼ�Ƕ�
	 */
	private void refreshItemsAngs() {
		if (item != null && item.length > 0) {
			itemsRate = new float[item.length];// ÿһ����ռ�ı���
			itemsBeginAngle = new float[item.length];// ÿһ���Ƕ��ٽ��
			itemsAngle = new float[item.length];// ÿһ���Ƕ��ٽ��
			float beginAngle = 0;
			for (int i = 0; i < item.length; i++) {
				itemsRate[i] = (float) (item[i] * 1.0 / total * 1.0);
			}
			for (int i = 0; i < itemsRate.length; i++) {
				if (i == 1) {
					beginAngle = 360 * itemsRate[i - 1];
				} else if (i > 1) {
					beginAngle = 360 * itemsRate[i - 1] + beginAngle;
				}
				itemsBeginAngle[i] = beginAngle;
				itemsAngle[i] = 360 * itemsRate[i];
			}

		}

	}

	public void notifyDraw() {
		invalidate();

	}

	/**
	 * �ؼ��ɻ�õĿռ�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		float widthHeight = 2 * (radius);
		setMeasuredDimension((int) widthHeight, (int) widthHeight);
	}
}
