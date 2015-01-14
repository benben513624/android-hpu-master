package com.hpu.commun.domain;

import java.io.Serializable;

/**
 * һ��ͨ������Ϣ
 * 
 * @author xst
 * 
 */
public class ECardUserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ѧ��
	 */
	private String num;
	/**
	 * ����
	 */
	private String name;
	/**
	 * �Ա�
	 */
	private String sex;
	/**
	 * ���
	 */
	private String status;
	/**
	 * һ��ͨ���
	 */
	private String emoney;
	/**
	 * һ��ͨ״̬
	 */
	private String estate;
	/**
	 * �������
	 */
	private String eassistance;

	public ECardUserInfo() {
		super();
	}

	public ECardUserInfo(String num, String name, String sex, String status,
			String emoney, String estate, String eassistance) {
		super();
		this.num = num;
		this.name = name;
		this.sex = sex;
		this.status = status;
		this.emoney = emoney;
		this.estate = estate;
		this.eassistance = eassistance;
	}

	public String getNum() {
		return num;
	}

	public String getEassistance() {
		return eassistance;
	}

	public void setEassistance(String eassistance) {
		this.eassistance = eassistance;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmoney() {
		return emoney;
	}

	public void setEmoney(String emoney) {
		this.emoney = emoney;
	}

	public String getEstate() {
		return estate;
	}

	public void setEstate(String estate) {
		this.estate = estate;
	}

	@Override
	public String toString() {
		return "ECardUserInfo [num=" + num + ", name=" + name + ", sex=" + sex
				+ ", status=" + status + ", emoney=" + emoney + ", estate="
				+ estate + ", eassistance=" + eassistance + "]";
	}

}
