package com.zhijianlife.core.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OrderBean implements Serializable{
	private int accept;		//0:未接受； 1：已接受； -1：已拒绝
	private String address;// "杭州-西湖区世纪新城来吖来太极图来吖噢图噢田来你啊来图来来腾"
	private int bizId;//157
	private String bizTime;// "2014-08-12 10:31:16"
	private String phone;// "18758205975"
	private Double totalMoney;// 9016
	private String userName;// "vi
	public int getAccept() {
		return accept;
	}
	public void setAccept(int accept) {
		this.accept = accept;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getBizId() {
		return bizId;
	}
	public void setBizId(int bizId) {
		this.bizId = bizId;
	}
	public String getBizTime() {
		return bizTime;
	}
	public void setBizTime(String bizTime) {
		this.bizTime = bizTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
