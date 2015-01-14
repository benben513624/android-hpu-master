package com.hpu.commun.domain;

/**
 * �γ�ʵ����
 * 
 * @author LeeLay
 * 
 *         2014-10-10
 */
public class KeCheng {

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * ѧ��
	 */
	private String stunum;

	public String getStunum() {
		return stunum;
	}

	public void setStunum(String stunum) {
		this.stunum = stunum;
	}

	/**
	 * �γ���
	 */
	private String name;
	/**
	 * �ο���ʦ
	 */
	private String teacher;

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	private int color;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	/*
	 * *ѧ��
	 */
	private String credit;
	/**
	 * ����
	 */
	private String kind;
	/**
	 * �ܴ�
	 */
	private String weekly;
	/**
	 * �ܼ�
	 */
	private String week;
	/**
	 * �ڴ�
	 */
	private int section;
	/**
	 * ��ѧ¥��
	 */
	private String building;
	/**
	 * ���Һ�
	 */
	private String classroom;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeekly() {
		return weekly;
	}

	public void setWeekly(String weekly) {
		this.weekly = weekly;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int i) {
		this.section = i;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	@Override
	public String toString() {
		return "KeCheng [id=" + id + ", position=" + position + ", stunum="
				+ stunum + ", name=" + name + ", teacher=" + teacher
				+ ", color=" + color + ", credit=" + credit + ", kind=" + kind
				+ ", weekly=" + weekly + ", week=" + week + ", section="
				+ section + ", building=" + building + ", classroom="
				+ classroom + "]";
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

}
