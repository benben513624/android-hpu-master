package com.hpu.commun.domain;

import java.io.Serializable;

/**
 * �ɼ�ʵ����
 * 
 * @author xst
 * 
 */
public class Chengji implements Serializable {
	// �γ��� ѧ�� �γ����� �ɼ�
	private String cname;
	private String xf;
	private String kcsx;
	private String chengji;

	@Override
	public String toString() {
		return "Chengji [cname=" + cname + ", xf=" + xf + ", kcsx=" + kcsx
				+ ", chengji=" + chengji + "]";
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getXf() {
		return xf;
	}

	public void setXf(String xf) {
		this.xf = xf;
	}

	public String getKcsx() {
		return kcsx;
	}

	public void setKcsx(String kcsx) {
		this.kcsx = kcsx;
	}

	public String getChengji() {
		return chengji;
	}

	public void setChengji(String chengji) {
		this.chengji = chengji;
	}

}
