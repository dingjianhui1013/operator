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
	private Integer addFinance; 	//新增财务章
	private Integer addContract;	//新增合同章
	private Integer addPersonal;	//新增个人章
	private Integer addOfficial;	//新增公章
	private Integer renewFinance; 	//更新财务章
	private Integer renewContract;	//更新合同章
	private Integer renewPersonal;	//更新个人章
	private Integer renewOfficial;	//更新公章
	private Integer changeFinance; 	//变更财务章
	private Integer changeContract;	//变更合同章
	private Integer changePersonal;	//变更个人章
	private Integer changeOfficial;	//变更公章
	
	public StatisticSealAppMonth() {
		
	}
	
	public StatisticSealAppMonth(Long id, ConfigApp app, Date statisticDate,
			Integer sealTotal, Double receiptTotal, Double sealMoney,
			Integer addFinance, Integer addContract, Integer addPersonal,
			Integer addOfficial, Integer renewFinance, Integer renewContract,
			Integer renewPersonal, Integer renewOfficial,
			Integer changeFinance, Integer changeContract,
			Integer changePersonal, Integer changeOfficial) {
		super();
		this.id = id;
		this.app = app;
		this.statisticDate = statisticDate;
		this.sealTotal = sealTotal;
		this.receiptTotal = receiptTotal;
		this.sealMoney = sealMoney;
		this.addFinance = addFinance;
		this.addContract = addContract;
		this.addPersonal = addPersonal;
		this.addOfficial = addOfficial;
		this.renewFinance = renewFinance;
		this.renewContract = renewContract;
		this.renewPersonal = renewPersonal;
		this.renewOfficial = renewOfficial;
		this.changeFinance = changeFinance;
		this.changeContract = changeContract;
		this.changePersonal = changePersonal;
		this.changeOfficial = changeOfficial;
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
	public Integer getAddFinance() {
		return addFinance;
	}
	public void setAddFinance(Integer addFinance) {
		this.addFinance = addFinance;
	}
	public Integer getAddContract() {
		return addContract;
	}
	public void setAddContract(Integer addContract) {
		this.addContract = addContract;
	}
	public Integer getAddPersonal() {
		return addPersonal;
	}
	public void setAddPersonal(Integer addPersonal) {
		this.addPersonal = addPersonal;
	}
	public Integer getAddOfficial() {
		return addOfficial;
	}
	public void setAddOfficial(Integer addOfficial) {
		this.addOfficial = addOfficial;
	}
	public Integer getRenewFinance() {
		return renewFinance;
	}
	public void setRenewFinance(Integer renewFinance) {
		this.renewFinance = renewFinance;
	}
	public Integer getRenewContract() {
		return renewContract;
	}
	public void setRenewContract(Integer renewContract) {
		this.renewContract = renewContract;
	}
	public Integer getRenewPersonal() {
		return renewPersonal;
	}
	public void setRenewPersonal(Integer renewPersonal) {
		this.renewPersonal = renewPersonal;
	}
	public Integer getRenewOfficial() {
		return renewOfficial;
	}
	public void setRenewOfficial(Integer renewOfficial) {
		this.renewOfficial = renewOfficial;
	}
	public Integer getChangeFinance() {
		return changeFinance;
	}
	public void setChangeFinance(Integer changeFinance) {
		this.changeFinance = changeFinance;
	}
	public Integer getChangeContract() {
		return changeContract;
	}
	public void setChangeContract(Integer changeContract) {
		this.changeContract = changeContract;
	}
	public Integer getChangePersonal() {
		return changePersonal;
	}
	public void setChangePersonal(Integer changePersonal) {
		this.changePersonal = changePersonal;
	}
	public Integer getChangeOfficial() {
		return changeOfficial;
	}
	public void setChangeOfficial(Integer changeOfficial) {
		this.changeOfficial = changeOfficial;
	}
	
}
