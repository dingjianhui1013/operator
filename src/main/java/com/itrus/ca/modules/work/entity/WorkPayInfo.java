package com.itrus.ca.modules.work.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
/**
 * WorkPayInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_pay_info")
public class WorkPayInfo extends DataEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4818115172517371915L;
	private Long id;
	private String sn;
	private Double openAccountMoney;
	private Double addCert;
	private Double updateCert;
	private Double errorReplaceCert;
	private Double lostReplaceCert;
	private Double infoChange;
	private Double electron;
	private Boolean methodPos;
	private Boolean methodMoney;
	private Boolean methodBank;
	private Boolean methodAlipay;
	private Boolean methodGov;
	private Boolean methodContract;
	private Double posMoney;//pos付款
	private Double money;//现金付款
	private Double bankMoney;//银行付款
	private Double alipayMoney;//支付宝付款
	private Double workTotalMoney;
	private Double workPayedMoney;
	private Double workReceivaMoney;
	private Boolean userReceipt;
	private Double receiptAmount;
	//增加当前计费策略应缴纳金额与实际形成对应
	private Double oldOpenAccountMoney;
	private Double oldAddCert;
	private Double oldUpdateCert;
	private Double oldErrorReplaceCert;
	private Double oldLostReplaceCert;
	private Double oldInfoChange;
	private Double oldElectron;
	
	/**
	 * 1 现金 2pos 3银行 4支付宝
	 */
	private Integer relationMethod;//绑定支付信息的付款方式，以第一笔为准，可以为空
	private Set<WorkFinancePayInfoRelation> workFinancePayInfoRelations = new HashSet<WorkFinancePayInfoRelation>(
			0);
	private Set<WorkDealInfo> workDealInfos = new HashSet<WorkDealInfo>(0);
	private Set<ReceiptInvoice> receiptInvoices = new HashSet<ReceiptInvoice>(0);

	// Constructors

	/** default constructor */
	public WorkPayInfo() {
		this.money = 0d;
		this.bankMoney = 0d;
		this.posMoney = 0d;
		this.alipayMoney = 0d;
	}

	/** full constructor */
	public WorkPayInfo(String sn, Double openAccountMoney, Double addCert,
			Double updateCert, Double errorReplaceCert, Double lostReplaceCert,
			Double infoChange, Double electron, Boolean methodPos,
			Boolean methodMoney, Boolean methodBank, Boolean methodAlipay,
			Boolean methodGov, Boolean methodContract, Double posMoney,
			Double money, Double bankMoney, Double alipayMoney,
			Double workTotalMoney, Double workPayedMoney,
			Double workReceivaMoney, Boolean userReceipt, Double receiptAmount,
			Set<WorkFinancePayInfoRelation> workFinancePayInfoRelations,
			Set<WorkDealInfo> workDealInfos, Set<ReceiptInvoice> receiptInvoices) {
		this.sn = sn;
		this.openAccountMoney = openAccountMoney;
		this.addCert = addCert;
		this.updateCert = updateCert;
		this.errorReplaceCert = errorReplaceCert;
		this.lostReplaceCert = lostReplaceCert;
		this.infoChange = infoChange;
		this.electron = electron;
		this.methodPos = methodPos;
		this.methodMoney = methodMoney;
		this.methodBank = methodBank;
		this.methodAlipay = methodAlipay;
		this.methodGov = methodGov;
		this.methodContract = methodContract;
		this.posMoney = posMoney;
		this.money = money;
		this.bankMoney = bankMoney;
		this.alipayMoney = alipayMoney;
		this.workTotalMoney = workTotalMoney;
		this.workPayedMoney = workPayedMoney;
		this.workReceivaMoney = workReceivaMoney;
		this.userReceipt = userReceipt;
		this.receiptAmount = receiptAmount;
		this.workFinancePayInfoRelations = workFinancePayInfoRelations;
		this.workDealInfos = workDealInfos;
		this.receiptInvoices = receiptInvoices;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORK_PAY_INFO_SEQUENCE")
//	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@SequenceGenerator(name="WORK_PAY_INFO_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="WORK_PAY_INFO_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "sn",columnDefinition = "NVARCHAR2(255)")
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "open_account_money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getOpenAccountMoney() {
		return this.openAccountMoney;
	}

	public void setOpenAccountMoney(Double openAccountMoney) {
		this.openAccountMoney = openAccountMoney;
	}

	@Column(name = "add_cert", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getAddCert() {
		return this.addCert;
	}

	public void setAddCert(Double addCert) {
		this.addCert = addCert;
	}

	@Column(name = "update_cert", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getUpdateCert() {
		return this.updateCert;
	}

	public void setUpdateCert(Double updateCert) {
		this.updateCert = updateCert;
	}

	@Column(name = "error_replace_cert", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getErrorReplaceCert() {
		return this.errorReplaceCert;
	}

	public void setErrorReplaceCert(Double errorReplaceCert) {
		this.errorReplaceCert = errorReplaceCert;
	}

	@Column(name = "lost_replace_cert", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getLostReplaceCert() {
		return this.lostReplaceCert;
	}

	public void setLostReplaceCert(Double lostReplaceCert) {
		this.lostReplaceCert = lostReplaceCert;
	}

	@Column(name = "info_change", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getInfoChange() {
		return this.infoChange;
	}

	public void setInfoChange(Double infoChange) {
		this.infoChange = infoChange;
	}

	@Column(name = "electron", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getElectron() {
		return this.electron;
	}

	public void setElectron(Double electron) {
		this.electron = electron;
	}

	@Column(name = "method_pos")
	public Boolean getMethodPos() {
		return this.methodPos==null? false:this.methodPos;
	}

	public void setMethodPos(Boolean methodPos) {
		this.methodPos = methodPos;
	}

	@Column(name = "method_money")
	public Boolean getMethodMoney() {
		return this.methodMoney==null? false:this.methodMoney;
	}

	public void setMethodMoney(Boolean methodMoney) {
		this.methodMoney = methodMoney;
	}

	@Column(name = "method_bank")
	public Boolean getMethodBank() {
		return this.methodBank==null? false:this.methodBank;
	}

	public void setMethodBank(Boolean methodBank) {
		this.methodBank = methodBank;
	}

	@Column(name = "method_alipay")
	public Boolean getMethodAlipay() {
		return this.methodAlipay==null? false:this.methodAlipay;
	}

	public void setMethodAlipay(Boolean methodAlipay) {
		this.methodAlipay = methodAlipay;
	}

	@Column(name = "method_gov")
	public Boolean getMethodGov() {
		return this.methodGov==null? false:this.methodGov;
	}

	public void setMethodGov(Boolean methodGov) {
		this.methodGov = methodGov;
	}

	@Column(name = "method_contract")
	public Boolean getMethodContract() {
		return this.methodContract==null? false:this.methodContract;
	}

	public void setMethodContract(Boolean methodContract) {
		this.methodContract = methodContract;
	}

	@Column(name = "pos_money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getPosMoney() {
		return this.posMoney;
	}

	public void setPosMoney(Double posMoney) {
		this.posMoney = posMoney;
	}

	@Column(name = "money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "bank_money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getBankMoney() {
		return this.bankMoney;
	}

	public void setBankMoney(Double bankMoney) {
		this.bankMoney = bankMoney;
	}

	@Column(name = "alipay_money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getAlipayMoney() {
		return this.alipayMoney;
	}

	public void setAlipayMoney(Double alipayMoney) {
		this.alipayMoney = alipayMoney;
	}

	@Column(name = "work_total_money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getWorkTotalMoney() {
		return this.workTotalMoney;
	}

	public void setWorkTotalMoney(Double workTotalMoney) {
		this.workTotalMoney = workTotalMoney;
	}

	@Column(name = "work_payed_money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getWorkPayedMoney() {
		return this.workPayedMoney;
	}

	public void setWorkPayedMoney(Double workPayedMoney) {
		this.workPayedMoney = workPayedMoney;
	}

	

	@Column(name = "user_receipt")
	public Boolean getUserReceipt() {
		return this.userReceipt;
	}

	public void setUserReceipt(Boolean userReceipt) {
		this.userReceipt = userReceipt;
	}

	@Column(name = "receipt_amount", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getReceiptAmount() {
		return this.receiptAmount;
	}

	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workPayInfo")
	public Set<WorkFinancePayInfoRelation> getWorkFinancePayInfoRelations() {
		return this.workFinancePayInfoRelations;
	}

	public void setWorkFinancePayInfoRelations(
			Set<WorkFinancePayInfoRelation> workFinancePayInfoRelations) {
		this.workFinancePayInfoRelations = workFinancePayInfoRelations;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workPayInfo")
	public Set<WorkDealInfo> getWorkDealInfos() {
		return this.workDealInfos;
	}

	public void setWorkDealInfos(Set<WorkDealInfo> workDealInfos) {
		this.workDealInfos = workDealInfos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workPayInfo")
	public Set<ReceiptInvoice> getReceiptInvoices() {
		return this.receiptInvoices;
	}

	public void setReceiptInvoices(Set<ReceiptInvoice> receiptInvoices) {
		this.receiptInvoices = receiptInvoices;
	}

	@Column(name = "work_receiva_money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getWorkReceivaMoney() {
		return workReceivaMoney;
	}

	public void setWorkReceivaMoney(Double workReceivaMoney) {
		this.workReceivaMoney = workReceivaMoney;
	}

	@Column(name = "relation_method")
	public Integer getRelationMethod() {
		return relationMethod;
	}

	public void setRelationMethod(Integer relationMethod) {
		this.relationMethod = relationMethod;
	}

	/**
	 * @return the oldOpenAccountMoney
	 */
	@Column(name = "old_open_account_money", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getOldOpenAccountMoney() {
		return oldOpenAccountMoney;
	}

	/**
	 * @param oldOpenAccountMoney the oldOpenAccountMoney to set
	 */
	public void setOldOpenAccountMoney(Double oldOpenAccountMoney) {
		this.oldOpenAccountMoney = oldOpenAccountMoney;
	}

	/**
	 * @return the oldAddCert
	 */
	@Column(name = "old_add_cert", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getOldAddCert() {
		return oldAddCert;
	}

	/**
	 * @param oldAddCert the oldAddCert to set
	 */
	public void setOldAddCert(Double oldAddCert) {
		this.oldAddCert = oldAddCert;
	}

	/**
	 * @return the oldUpdateCert
	 */
	@Column(name = "old_update_cert", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getOldUpdateCert() {
		return oldUpdateCert;
	}

	/**
	 * @param oldUpdateCert the oldUpdateCert to set
	 */
	public void setOldUpdateCert(Double oldUpdateCert) {
		this.oldUpdateCert = oldUpdateCert;
	}

	/**
	 * @return the oldErrorReplaceCert
	 */
	@Column(name = "old_error_replace_cert", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getOldErrorReplaceCert() {
		return oldErrorReplaceCert;
	}

	/**
	 * @param oldErrorReplaceCert the oldErrorReplaceCert to set
	 */
	public void setOldErrorReplaceCert(Double oldErrorReplaceCert) {
		this.oldErrorReplaceCert = oldErrorReplaceCert;
	}

	/**
	 * @return the oldLostReplaceCert
	 */
	@Column(name = "old_lost_replace_cert", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getOldLostReplaceCert() {
		return oldLostReplaceCert;
	}

	/**
	 * @param oldLostReplaceCert the oldLostReplaceCert to set
	 */
	public void setOldLostReplaceCert(Double oldLostReplaceCert) {
		this.oldLostReplaceCert = oldLostReplaceCert;
	}

	/**
	 * @return the oldInfoChange
	 */
	@Column(name = "old_info_change", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getOldInfoChange() {
		return oldInfoChange;
	}

	/**
	 * @param oldInfoChange the oldInfoChange to set
	 */
	public void setOldInfoChange(Double oldInfoChange) {
		this.oldInfoChange = oldInfoChange;
	}

	/**
	 * @return the oldElectron
	 */
	@Column(name = "old_electron", precision = 20, scale = 3,columnDefinition = "NUMBER")
	public Double getOldElectron() {
		return oldElectron;
	}

	/**
	 * @param oldElectron the oldElectron to set
	 */
	public void setOldElectron(Double oldElectron) {
		this.oldElectron = oldElectron;
	}

}