package com.itrus.ca.modules.bean;

import java.util.Date;

import com.itrus.ca.modules.profile.entity.ConfigApp;

public class StatisticSealAppMonth {

	private Long id; 		// 编号
	private ConfigApp app;  //应用
	private Date statisticDate; //日期
	private Integer sealTotal;
	private Double receiptTotal;
	private Double sealMoney;
	private Integer addOne; 	//新增财务章
	private Integer addTwo;	//新增合同章
	private Integer addThree;	//新增个人章
	private Integer addFour;	//新增公章
	private Integer addFive;	//新增公章
	private Integer renewOne; 	//更新财务章
	private Integer renewTwo;	//更新合同章
	private Integer renewThree;	//更新个人章
	private Integer renewFour;	//更新公章
	private Integer renewFive;	//新增公章
	private Integer changeOne; 	//变更财务章
	private Integer changeTwo;	//变更合同章
	private Integer changeThree;	//变更个人章
	private Integer changeFour;	//变更公章
	private Integer changeFive;	//变更公章
	
	public StatisticSealAppMonth() {
		
	}
	
	

	public StatisticSealAppMonth(Long id, ConfigApp app, Date statisticDate,
			Integer sealTotal, Double receiptTotal, Double sealMoney,
			Integer addOne, Integer addTwo, Integer addThree, Integer addFour,
			Integer addFive, Integer renewOne, Integer renewTwo,
			Integer renewThree, Integer renewFour, Integer renewFive,
			Integer changeOne, Integer changeTwo, Integer changeThree,
			Integer changeFour, Integer changeFive) {
		super();
		this.id = id;
		this.app = app;
		this.statisticDate = statisticDate;
		this.sealTotal = sealTotal;
		this.receiptTotal = receiptTotal;
		this.sealMoney = sealMoney;
		this.addOne = addOne;
		this.addTwo = addTwo;
		this.addThree = addThree;
		this.addFour = addFour;
		this.addFive = addFive;
		this.renewOne = renewOne;
		this.renewTwo = renewTwo;
		this.renewThree = renewThree;
		this.renewFour = renewFour;
		this.renewFive = renewFive;
		this.changeOne = changeOne;
		this.changeTwo = changeTwo;
		this.changeThree = changeThree;
		this.changeFour = changeFour;
		this.changeFive = changeFive;
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ConfigApp getApp() {
		return app;
	}
	public void setApp(ConfigApp app) {
		this.app = app;
	}
	public Date getStatisticDate() {
		return statisticDate;
	}
	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}
	public Integer getSealTotal() {
		return sealTotal;
	}
	public void setSealTotal(Integer sealTotal) {
		this.sealTotal = sealTotal;
	}
	public Double getReceiptTotal() {
		return receiptTotal;
	}
	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}
	public Double getSealMoney() {
		return sealMoney;
	}
	public void setSealMoney(Double sealMoney) {
		this.sealMoney = sealMoney;
	}
	public Integer getAddOne() {
		return addOne;
	}
	public void setAddOne(Integer addOne) {
		this.addOne = addOne;
	}
	public Integer getAddTwo() {
		return addTwo;
	}
	public void setAddTwo(Integer addTwo) {
		this.addTwo = addTwo;
	}
	public Integer getAddThree() {
		return addThree;
	}
	public void setAddThree(Integer addThree) {
		this.addThree = addThree;
	}
	public Integer getAddFour() {
		return addFour;
	}
	public void setAddFour(Integer addFour) {
		this.addFour = addFour;
	}
	public Integer getRenewOne() {
		return renewOne;
	}
	public void setRenewOne(Integer renewOne) {
		this.renewOne = renewOne;
	}
	public Integer getRenewTwo() {
		return renewTwo;
	}
	public void setRenewTwo(Integer renewTwo) {
		this.renewTwo = renewTwo;
	}
	public Integer getRenewThree() {
		return renewThree;
	}
	public void setRenewThree(Integer renewThree) {
		this.renewThree = renewThree;
	}
	public Integer getRenewFour() {
		return renewFour;
	}
	public void setRenewFour(Integer renewFour) {
		this.renewFour = renewFour;
	}
	public Integer getChangeOne() {
		return changeOne;
	}
	public void setChangeOne(Integer changeOne) {
		this.changeOne = changeOne;
	}
	public Integer getChangeTwo() {
		return changeTwo;
	}
	public void setChangeTwo(Integer changeTwo) {
		this.changeTwo = changeTwo;
	}
	public Integer getChangeThree() {
		return changeThree;
	}
	public void setChangeThree(Integer changeThree) {
		this.changeThree = changeThree;
	}
	public Integer getChangeFour() {
		return changeFour;
	}
	public void setChangeFour(Integer changeFour) {
		this.changeFour = changeFour;
	}



	public Integer getAddFive() {
		return addFive;
	}



	public void setAddFive(Integer addFive) {
		this.addFive = addFive;
	}



	public Integer getRenewFive() {
		return renewFive;
	}



	public void setRenewFive(Integer renewFive) {
		this.renewFive = renewFive;
	}



	public Integer getChangeFive() {
		return changeFive;
	}



	public void setChangeFive(Integer changeFive) {
		this.changeFive = changeFive;
	}
	
	
}
