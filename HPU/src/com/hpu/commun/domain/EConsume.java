package com.hpu.commun.domain;

/**
 * 消费流水
 * 
 * @author xst
 * 
 */
public class EConsume {
	/**
	 * 刷卡时间
	 */
	private String time;
	/**
	 * 消费类型
	 */
	private String type;
	/**
	 * 消费金额
	 */
	private String money;
	/**
	 * 余额
	 */
	private String restmoney;
	/**
	 * 刷卡工作站
	 */
	private String place;
	/**
	 * 卡机终端
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
