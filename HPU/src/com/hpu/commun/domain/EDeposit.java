package com.hpu.commun.domain;

/**
 * 充值实体类
 * 
 * @author xst
 * 
 */
public class EDeposit {
	private String date;// 日期 格式: 2014/12/2 12:24:03
	private String money;// 充值金额 100.00
	private String re_money;// 余额 177.50
	private String terminal;// 卡机终端 例如 ：学子出纳机

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getRe_money() {
		return re_money;
	}

	public void setRe_money(String re_money) {
		this.re_money = re_money;
	}

	public EDeposit(String date, String money, String re_money, String terminal) {
		super();
		this.date = date;
		this.money = money;
		this.re_money = re_money;
		this.terminal = terminal;
	}

	@Override
	public String toString() {
		return "EDeposit [date=" + date + ", money=" + money + ", re_money="
				+ re_money + ", terminal=" + terminal + "]";
	}

}
