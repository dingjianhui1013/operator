package com.itrus.ca.modules.work.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;


/**
 * WorkFinancePayInfoRelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_finance_pay_info_relation")
public class WorkFinancePayInfoRelation implements java.io.Serializable {

	// Fields

	private Long id;
	private WorkPayInfo workPayInfo;
	private FinancePaymentInfo financePaymentInfo;
	private Double money;
	private BigDecimal balance;
	private String sn;

	// Constructors

	/** default constructor */
	public WorkFinancePayInfoRelation() {
	}

	/** full constructor */
	public WorkFinancePayInfoRelation(WorkPayInfo workPayInfo,
			FinancePaymentInfo financePaymentInfo, Double money) {
		this.workPayInfo = workPayInfo;
		this.financePaymentInfo = financePaymentInfo;
		this.money = money;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORK_FINANCE_PAY_INF_SEQUENCE")
//	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@SequenceGenerator(name="WORK_FINANCE_PAY_INF_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="WORK_FINANCE_PAY_INF_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "work_pay_info")
	public WorkPayInfo getWorkPayInfo() {
		return this.workPayInfo;
	}

	public void setWorkPayInfo(WorkPayInfo workPayInfo) {
		this.workPayInfo = workPayInfo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "finance_pay_info")
	public FinancePaymentInfo getFinancePaymentInfo() {
		return this.financePaymentInfo;
	}

	public void setFinancePaymentInfo(FinancePaymentInfo financePaymentInfo) {
		this.financePaymentInfo = financePaymentInfo;
	}

	@Column(name = "money", precision = 10, scale = 3,columnDefinition = "NUMBER")
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
	@Transient
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

}