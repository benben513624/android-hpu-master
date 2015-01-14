package com.hpu.commun.domain;

/**
 * ������ˮ
 * 
 * @author xst
 * 
 */
public class EConsume {
	/**
	 * ˢ��ʱ��
	 */
	private String time;
	/**
	 * ��������
	 */
	private String type;
	/**
	 * ���ѽ��
	 */
	private String money;
	/**
	 * ���
	 */
	private String restmoney;
	/**
	 * ˢ������վ
	 */
	private String place;
	/**
	 * �����ն�
	 */
	private String terminal;

	public EConsume(String time, String type, String money, String restmoney,
			String place, String terminal) {
		super();
		this.time = time;
		this.type = type;
		this.money = money;
		this.restmoney = restmoney;
		this.place = place;
		this.terminal = terminal;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getRestmoney() {
		return restmoney;
	}

	public void setRestmoney(String restmoney) {
		this.restmoney = restmoney;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

}
