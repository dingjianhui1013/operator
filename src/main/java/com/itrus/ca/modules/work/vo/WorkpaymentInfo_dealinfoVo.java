package com.itrus.ca.modules.work.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class WorkpaymentInfo_dealinfoVo {
	private Long id;                               
	private Date dealPayDate;        //支付日期(到款日期)
	private Double payMoney;
	private String companyName;      //公司名称 
	private String remarks;          //备注
	private String aliasName;        //应用名称
	private Date signDate;           //制证日期
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDealPayDate() {
		return dealPayDate;
	}
	public void setDealPayDate(Date dealPayDate) {
		this.dealPayDate = dealPayDate;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

}
