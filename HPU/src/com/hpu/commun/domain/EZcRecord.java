package com.hpu.commun.domain;

/**
 * ��ټ�¼
 * 
 * @author xst
 * 
 */
public class EZcRecord {

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * ˢ������ ( ��ʽ�� 12-18(����һ) )
	 */
	private String date;

	/**
	 * ˢ��ʱ��
	 */
	private String time;
	/**
	 * �������
	 */
	private String num;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public EZcRecord(String date, String time, String num) {
		super();
		this.date = date;
		this.time = time;
		this.num = num;
	}

}
