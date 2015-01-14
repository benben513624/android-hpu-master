package com.hpu.commun.domain;

import java.io.Serializable;

/**
 * 最新公告实体类
 * 
 * @author xst
 * 
 */
public class Notice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String href;
	private String title;
	private String date;

	public Notice() {
		super();

	}

	public Notice(String href, String title, String date) {
		super();
		this.href = href;
		this.title = title;
		this.date = date;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
