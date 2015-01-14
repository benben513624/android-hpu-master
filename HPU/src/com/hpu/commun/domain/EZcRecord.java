package com.hpu.commun.domain;

/**
 * 早操记录
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
	 * 刷卡日期 ( 格式如 12-18(星期一) )
	 */
	private String date;

	/**
	 * 刷卡时间
	 */
	private String time;
	/**
	 * 卡机编号
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
