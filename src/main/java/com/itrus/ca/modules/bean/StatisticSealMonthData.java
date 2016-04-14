package com.itrus.ca.modules.bean;

import java.util.Date;

import com.itrus.ca.modules.sys.entity.Office;

public class StatisticSealMonthData {

	private Long id;
	private Office sysOffice;
	private Double receiptIn; //发票入库
	private Double receiptTotal;//发票总量
	private Integer sealDay;   //印章
	private Double sealMoney;  //费用
	private Double receiptDay; //今日发票
	private Double receiptSurplus;//发票剩余
	private Date statisticDate;    //统计日期
	private Date createDate;       //创建日期
	
	public StatisticSealMonthData() {
	}
	public StatisticSealMonthData(Long id, Office sysOffice, Double receiptIn,
			Double receiptTotal, Integer sealDay, Double sealMoney,
			Double receiptDay, Double receiptSurplus, Date statisticDate,
			Date createDate) {
		super();
		this.id = id;
		this.sysOffice = sysOffice;
		this.receiptIn = receiptIn;
		this.receiptTotal = receiptTotal;
		this.sealDay = sealDay;
		this.sealMoney = sealMoney;
		this.receiptDay = receiptDay;
		this.receiptSurplus = receiptSurplus;
		this.statisticDate = statisticDate;
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Office getSysOffice() {
		return sysOffice;
	}
	public void setSysOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}
	public Double getReceiptIn() {
		return receiptIn;
	}
	public void setReceiptIn(Double receiptIn) {
		this.receiptIn = receiptIn;
	}
	public Double getReceiptTotal() {
		return receiptTotal;
	}
	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}
	public Integer getSealDay() {
		return sealDay;
	}
	public void setSealDay(Integer sealDay) {
		this.sealDay = sealDay;
	}
	public Double getSealMoney() {
		return sealMoney;
	}
	public void setSealMoney(Double sealMoney) {
		this.sealMoney = sealMoney;
	}
	public Double getReceiptDay() {
		return receiptDay;
	}
	public void setReceiptDay(Double receiptDay) {
		this.receiptDay = receiptDay;
	}
	public Double getReceiptSurplus() {
		return receiptSurplus;
	}
	public void setReceiptSurplus(Double receiptSurplus) {
		this.receiptSurplus = receiptSurplus;
	}
	public Date getStatisticDate() {
		return statisticDate;
	}
	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
