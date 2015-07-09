package com.itrus.ca.modules.bean;

import java.util.Date;

import com.itrus.ca.modules.profile.entity.ConfigApp;


public class StatisticAppMonthData {

	private Long id;
	
	private ConfigApp app;
	private Date statisticDate;
	private Integer add1;
	private Integer add2;
	private Integer add4;
	private Integer add5;
	private Integer renew1;
	private Integer renew2;
	private Integer renew4;
	private Integer renew5;
	private Integer modifyNum;//变更数量
	private Integer reissueNum;//补办数量
	private Integer certTotal;
	private Integer keyTotal;
	private Double receiptTotal;
	private Double certMoneyTotal;
	
	
	public StatisticAppMonthData(){
		
	}
	
	
	public StatisticAppMonthData(Long id, Date statisticDate, Integer add1,
			Integer add2, Integer add4, Integer renew1, Integer renew2,
			Integer renew4, Integer modifyNum, Integer reissueNum,
			Integer certTotal, Integer keyTotal, Double receiptTotal,
			Double certMoneyTotal) {
		super();
		this.id = id;
		this.statisticDate = statisticDate;
		this.add1 = add1;
		this.add2 = add2;
		this.add4 = add4;
		this.renew1 = renew1;
		this.renew2 = renew2;
		this.renew4 = renew4;
		this.modifyNum = modifyNum;
		this.reissueNum = reissueNum;
		this.certTotal = certTotal;
		this.keyTotal = keyTotal;
		this.receiptTotal = receiptTotal;
		this.certMoneyTotal = certMoneyTotal;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getStatisticDate() {
		return statisticDate;
	}


	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}


	public Integer getAdd1() {
		return add1;
	}


	public void setAdd1(Integer add1) {
		this.add1 = add1;
	}


	public Integer getAdd2() {
		return add2;
	}


	public void setAdd2(Integer add2) {
		this.add2 = add2;
	}


	public Integer getAdd4() {
		return add4;
	}


	public void setAdd4(Integer add4) {
		this.add4 = add4;
	}


	public Integer getRenew1() {
		return renew1;
	}


	public void setRenew1(Integer renew1) {
		this.renew1 = renew1;
	}


	public Integer getRenew2() {
		return renew2;
	}


	public void setRenew2(Integer renew2) {
		this.renew2 = renew2;
	}


	public Integer getRenew4() {
		return renew4;
	}


	public void setRenew4(Integer renew4) {
		this.renew4 = renew4;
	}


	public Integer getModifyNum() {
		return modifyNum;
	}


	public void setModifyNum(Integer modifyNum) {
		this.modifyNum = modifyNum;
	}


	public Integer getReissueNum() {
		return reissueNum;
	}


	public void setReissueNum(Integer reissueNum) {
		this.reissueNum = reissueNum;
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


	public ConfigApp getApp() {
		return app;
	}


	public void setApp(ConfigApp app) {
		this.app = app;
	}


	public Integer getAdd5() {
		return add5;
	}

	public void setAdd5(Integer add5) {
		this.add5 = add5;
	}

	public Integer getRenew5() {
		return renew5;
	}

	public void setRenew5(Integer renew5) {
		this.renew5 = renew5;
	}
}
