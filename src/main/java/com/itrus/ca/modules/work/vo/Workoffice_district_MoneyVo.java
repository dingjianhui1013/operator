package com.itrus.ca.modules.work.vo;

import java.util.List;
import java.util.Map;

public class Workoffice_district_MoneyVo {
	private String officeName;
	private String districtName;
	private double countMoney;
	private double countPostMoney;
	private double countBankMoney;
	private double countXjMoney;
	private double countAlipayMoney;
	private Boolean postMoney;
	private Boolean bankMoney;
	private Boolean xjMoney;
	private Boolean alipayMoney;
	private String date;
	
	
	
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public double getCountMoney() {
		return countMoney;
	}
	public void setCountMoney(double countMoney) {
		this.countMoney = countMoney;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getCountPostMoney() {
		return countPostMoney;
	}
	public void setCountPostMoney(double countPostMoney) {
		this.countPostMoney = countPostMoney;
	}
	public double getCountBankMoney() {
		return countBankMoney;
	}
	public void setCountBankMoney(double countBankMoney) {
		this.countBankMoney = countBankMoney;
	}
	public double getCountXjMoney() {
		return countXjMoney;
	}
	public void setCountXjMoney(double countXjMoney) {
		this.countXjMoney = countXjMoney;
	}
	public double getCountAlipayMoney() {
		return countAlipayMoney;
	}
	public void setCountAlipayMoney(double countAlipayMoney) {
		this.countAlipayMoney = countAlipayMoney;
	}
	public Boolean getPostMoney() {
		return this.postMoney==null? false:this.postMoney;
	}
	public void setPostMoney(Boolean postMoney) {
		this.postMoney = postMoney;
	}
	public Boolean getBankMoney() {
		return this.bankMoney==null? false:this.bankMoney;
	}
	public void setBankMoney(Boolean bankMoney) {
		this.bankMoney = bankMoney;
	}
	public Boolean getXjMoney() {
		return this.xjMoney==null? false:this.xjMoney;
	}
	public void setXjMoney(Boolean xjMoney) {
		this.xjMoney = xjMoney;
	}
	public Boolean getAlipayMoney() {
		return this.alipayMoney==null? false:this.alipayMoney;
	}
	public void setAlipayMoney(Boolean alipayMoney) {
		this.alipayMoney = alipayMoney;
	}
	
	
}
