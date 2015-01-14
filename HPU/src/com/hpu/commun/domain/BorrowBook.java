package com.hpu.commun.domain;

import java.io.Serializable;

/**
 * ͼ�����
 * 
 * @author xst
 * 
 */
public class BorrowBook implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ���
	 */
	private String num;
	/**
	 * ͼ��������
	 */
	private String barcode;
	/**
	 * ͼ������
	 */
	private String name;
	/**
	 * ��������
	 */
	private String startdate;
	/**
	 * ����(��)
	 */
	private String borrowday;
	/**
	 * ����(��)
	 */
	private String renewday;
	/**
	 * ��������
	 */
	private String enddate;
	/**
	 * �Ƿ���
	 */
	private String isextended;

	public BorrowBook(String num, String barcode, String name,
			String startdate, String borrowday, String renewday,
			String enddate, String isextended) {
		super();
		this.num = num;
		this.barcode = barcode;
		this.name = name;
		this.startdate = startdate;
		this.borrowday = borrowday;
		this.renewday = renewday;
		this.enddate = enddate;
		this.isextended = isextended;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getBorrowday() {
		return borrowday;
	}

	public void setBorrowday(String borrowday) {
		this.borrowday = borrowday;
	}

	public String getRenewday() {
		return renewday;
	}

	public void setRenewday(String renewday) {
		this.renewday = renewday;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getIsextended() {
		return isextended;
	}

	public void setIsextended(String isextended) {
		this.isextended = isextended;
	}

	@Override
	public String toString() {
		return "BorrowBook [num=" + num + ", barcode=" + barcode + ", name="
				+ name + ", startdate=" + startdate + ", borrowday="
				+ borrowday + ", renewday=" + renewday + ", enddate=" + enddate
				+ ", isextended=" + isextended + "]";
	}

}
