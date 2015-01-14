package com.hpu.commun.domain;

import cn.bmob.v3.BmobObject;

public class Feedback extends BmobObject {

	private String content;
	private String truename;
	private String username;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
