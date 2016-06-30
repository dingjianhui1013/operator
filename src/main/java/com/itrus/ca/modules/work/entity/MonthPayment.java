package com.itrus.ca.modules.work.entity;

import java.util.Date;

public class MonthPayment {

	public MonthPayment() {
		// TODO Auto-generated constructor stub
	}
	private Long workPayInfoId;
	private Long officeId;
	private Date createDate;
	private String realtionMethod;
	private int methodPos;
	private int posMoney;
	private int methodBank;
	private int bankMoney;
	private int methodMoney;
	private int money;
	private int methodAlipay;
	private int alipayMoney;
	public Long getWorkPayInfoId() {
		return workPayInfoId;
	}
	public void setWorkPayInfoId(Long workPayInfoId) {
		this.workPayInfoId = workPayInfoId;
	}
	public Long getOfficeId() {
		return officeId;
	}
	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRealtionMethod() {
		return realtionMethod;
	}
	public void setRealtionMethod(String realtionMethod) {
		this.realtionMethod = realtionMethod;
	}
	
	public int getMethodPos() {
		return methodPos;
	}
	public void setMethodPos(int methodPos) {
		this.methodPos = methodPos;
	}
	public int getPosMoney() {
		return posMoney;
	}
	public void setPosMoney(int posMoney) {
		this.posMoney = posMoney;
	}
	public int getMethodBank() {
		return methodBank;
	}
	public void setMethodBank(int methodBank) {
		this.methodBank = methodBank;
	}
	public int getBankMoney() {
		return bankMoney;
	}
	public void setBankMoney(int bankMoney) {
		this.bankMoney = bankMoney;
	}
	public int getMethodMoney() {
		return methodMoney;
	}
	public void setMethodMoney(int methodMoney) {
		this.methodMoney = methodMoney;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getMethodAlipay() {
		return methodAlipay;
	}
	public void setMethodAlipay(int methodAlipay) {
		this.methodAlipay = methodAlipay;
	}
	public int getAlipayMoney() {
		return alipayMoney;
	}
	public void setAlipayMoney(int alipayMoney) {
		this.alipayMoney = alipayMoney;
	}
	
	

}
