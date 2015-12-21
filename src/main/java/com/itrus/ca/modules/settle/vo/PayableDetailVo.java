package com.itrus.ca.modules.settle.vo;

import java.util.Date;

public class PayableDetailVo {
	
	private Date startDate;
	private Date endDate;
	private String dealInfoType;
	private String settleYear;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getDealInfoType() {
		return dealInfoType;
	}
	public void setDealInfoType(String dealInfoType) {
		this.dealInfoType = dealInfoType;
	}
	public String getSettleYear() {
		return settleYear;
	}
	public void setSettleYear(String settleYear) {
		this.settleYear = settleYear;
	}
	
	
	
}
