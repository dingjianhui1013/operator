package com.itrus.ca.modules.finance.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * 
 * @author shen
 * 退费实体
 */
@Entity
@Table(name = "finance_quit_money")
public class FinanceQuitMoney extends DataEntity implements Serializable{

	private Long id;
	private Double quitMoney;				//退费金额
	private Date quitDate;					//退费时间
	private String quitWindow;				//受理窗口
	private String quitReason;				//退费原因
	private FinancePaymentInfo financePaymentInfo;
	
	public FinanceQuitMoney() {}
	
	public FinanceQuitMoney(Long id, Double quitMoney, Date quitDate, String quitWindow, String quitReason, FinancePaymentInfo financePaymentInfo){
		this.id = id;
		this.quitMoney = quitMoney;
		this.quitDate = quitDate;
		this.quitWindow = quitWindow;
		this.quitReason = quitReason;
		this.financePaymentInfo = financePaymentInfo;
	}

	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@Id
	@GeneratedValue(generator = "", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "quit_money")
	public Double getQuitMoney() {
		return quitMoney;
	}

	public void setQuitMoney(Double quitMoney) {
		this.quitMoney = quitMoney;
	}

	@Column(name = "quit_date")
	public Date getQuitDate() {
		return quitDate;
	}

	public void setQuitDate(Date quitDate) {
		this.quitDate = quitDate;
	}

	@Column(name = "quit_window")
	public String getQuitWindow() {
		return quitWindow;
	}

	public void setQuitWindow(String quitWindow) {
		this.quitWindow = quitWindow;
	}

	@Column(name = "quit_reason")
	public String getQuitReason() {
		return quitReason;
	}

	public void setQuitReason(String quitReason) {
		this.quitReason = quitReason;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "payment_id")
	public FinancePaymentInfo getFinancePaymentInfo() {
		return financePaymentInfo;
	}

	public void setFinancePaymentInfo(FinancePaymentInfo financePaymentInfo) {
		this.financePaymentInfo = financePaymentInfo;
	}
	
}
