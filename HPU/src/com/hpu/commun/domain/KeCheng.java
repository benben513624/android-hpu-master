package com.hpu.commun.domain;

/**
 * 课程实体类
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
	 * 学号
	 */
	private String stunum;

	public String getStunum() {
		return stunum;
	}

	public void setStunum(String stunum) {
		this.stunum = stunum;
	}

	/**
	 * 课程名
	 */
	private String name;
	/**
	 * 任课老师
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
	 * *学分
	 */
	private String credit;
	/**
	 * 类型
	 */
	private String kind;
	/**
	 * 周次
	 */
	private String weekly;
	/**
	 * 周几
	 */
	private String week;
	/**
	 * 节次
	 */
	private int section;
	/**
	 * 教学楼号
	 */
	private String building;
	/**
	 * 教室号
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
