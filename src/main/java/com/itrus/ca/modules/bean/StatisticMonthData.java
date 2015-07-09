package com.itrus.ca.modules.bean;

import java.util.Date;

import com.itrus.ca.modules.sys.entity.Office;

public class StatisticMonthData {

	private Long id;
	private Office sysOffice;
	private Integer keyIn;
	private Integer keyOver;
	private Integer keyStoreTotal;
	private Double receiptIn;
	private Double receiptOver;
	private Double receiptStoreTotal;
	private Date statisticDate;
    private Integer certTotal;
	private Integer keyTotal;
	private Double receiptTotal;
	private Double certMoneyTotal;
	private Date createDate;
	
	
	public StatisticMonthData(){
		
	}
	
	
	public StatisticMonthData(Long id, Office sysOffice, Integer keyIn,
			Integer keyOver, Integer keyStoreTotal, Double receiptIn,
			Double receiptOver, Double receiptStoreTotal, Date statisticDate,
			Integer certTotal, Integer keyTotal, Double receiptTotal,
			Double certMoneyTotal, Date createDate) {
		super();
		this.id = id;
		this.sysOffice = sysOffice;
		this.keyIn = keyIn;
		this.keyOver = keyOver;
		this.keyStoreTotal = keyStoreTotal;
		this.receiptIn = receiptIn;
		this.receiptOver = receiptOver;
		this.receiptStoreTotal = receiptStoreTotal;
		this.statisticDate = statisticDate;
		this.certTotal = certTotal;
		this.keyTotal = keyTotal;
		this.receiptTotal = receiptTotal;
		this.certMoneyTotal = certMoneyTotal;
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


	public Integer getKeyIn() {
		return keyIn;
	}


	public void setKeyIn(Integer keyIn) {
		this.keyIn = keyIn;
	}


	public Integer getKeyOver() {
		return keyOver;
	}


	public void setKeyOver(Integer keyOver) {
		this.keyOver = keyOver;
	}


	public Integer getKeyStoreTotal() {
		return keyStoreTotal;
	}


	public void setKeyStoreTotal(Integer keyStoreTotal) {
		this.keyStoreTotal = keyStoreTotal;
	}


	public Double getReceiptIn() {
		return receiptIn;
	}


	public void setReceiptIn(Double receiptIn) {
		this.receiptIn = receiptIn;
	}


	public Double getReceiptOver() {
		return receiptOver;
	}


	public void setReceiptOver(Double receiptOver) {
		this.receiptOver = receiptOver;
	}


	public Double getReceiptStoreTotal() {
		return receiptStoreTotal;
	}


	public void setReceiptStoreTotal(Double receiptStoreTotal) {
		this.receiptStoreTotal = receiptStoreTotal;
	}


	public Date getStatisticDate() {
		return statisticDate;
	}


	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}


	public Integer getCertTotal() {
		return certTotal;
	}


	public void setCertTotal(Integer certTotal) {
		this.certTotal = certTotal;
	}


	public Integer getKeyTotal() {
		return keyTotal;
	}


	public void setKeyTotal(Integer keyTotal) {
		this.keyTotal = keyTotal;
	}


	public Double getReceiptTotal() {
		return receiptTotal;
	}


	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}


	public Double getCertMoneyTotal() {
		return certMoneyTotal;
	}


	public void setCertMoneyTotal(Double certMoneyTotal) {
		this.certMoneyTotal = certMoneyTotal;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
	
}
