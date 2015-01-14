package com.hpu.commun.domain;

import java.io.Serializable;

/**
 * 学术报告实体类
 * 
 * @author xst
 * 
 */
public class Report implements Serializable {

	private static final long serialVersionUID = 1L;
	private String href;
	private String title;
	private String authro;
	private String date;
	private String address;

	public String getHref() {
		return href;
	}

	public void setHref(String herf) {
		this.href = herf;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthro() {
		return authro;
	}

	public void setAuthro(String authro) {
		this.authro = authro;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Report(String href, String title, String authro, String date,
			String address) {
		super();
		this.href = href;
		this.title = title;
		this.authro = authro;
		this.date = date;
		this.address = address;
	}

}
