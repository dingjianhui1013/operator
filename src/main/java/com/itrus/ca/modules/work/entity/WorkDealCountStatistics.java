package com.itrus.ca.modules.work.entity;

import javax.persistence.Transient;

public class WorkDealCountStatistics  {

	private String areaName;
	private String officeName;
	private String appName;
	private Integer posCount;// pos机收款数量
	private Integer moneyCount;// 现金收款数量
	private Integer bankCount;// 银行转账付款数量
	private Integer alipayCount;// 支付宝转账数量
	private Integer govCount;//政府统一采购数量
	private Integer contractCount;//合同采购数量


	public WorkDealCountStatistics() {
	}

	public WorkDealCountStatistics(String areaName, String officeName,
			Integer posCount, Integer moneyCount, Integer bankCount,
			Integer alipayCount, Integer govCount, Integer contractCount
	) {
		this.areaName = areaName;
		this.officeName = officeName;
		this.posCount = posCount;
		this.moneyCount = moneyCount;
		this.bankCount = bankCount;
		this.alipayCount = alipayCount;
		this.govCount = govCount;
		this.contractCount = contractCount;
	}

	@Transient
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@Transient
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	@Transient
	public Integer getPosCount() {
		return posCount;
	}

	public void setPosCount(Integer posCount) {
		this.posCount = posCount;
	}
	@Transient
	public Integer getMoneyCount() {
		return moneyCount;
	}
	
	public void setMoneyCount(Integer moneyCount) {
		this.moneyCount = moneyCount;
	}
	@Transient
	public Integer getBankCount() {
		return bankCount;
	}

	public void setBankCount(Integer bankCount) {
		this.bankCount = bankCount;
	}
	@Transient
	public Integer getAlipayCount() {
		return alipayCount;
	}

	public void setAlipayCount(Integer alipayCount) {
		this.alipayCount = alipayCount;
	}

	@Transient
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@Transient
	public Integer getGovCount() {
		return govCount;
	}

	public void setGovCount(Integer govCount) {
		this.govCount = govCount;
	}
	@Transient
	public Integer getContractCount() {
		return contractCount;
	}

	public void setContractCount(Integer contractCount) {
		this.contractCount = contractCount;
	}
	

}