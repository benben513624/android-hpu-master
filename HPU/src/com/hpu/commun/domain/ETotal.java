package com.hpu.commun.domain;

/**
 * �����ܽ��
 * 
 * @author xst
 * 
 */
public class ETotal {

	/**
	 * ����
	 */
	private String type;
	/**
	 * ���
	 */
	private String money;

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public ETotal(String type, String money) {
		super();
		this.type = type;
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ETotal [type=" + type + ", money=" + money + "]";
	}

}
