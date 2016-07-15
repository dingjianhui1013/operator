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
import com.itrus.ca.modules.work.entity.WorkDealInfo;

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
	private WorkDealInfo workDealInfo;
	private String status;  //1：未处理  2：已处理
	private String ression;  //原因
	
	
	
	
	
	
	public FinanceQuitMoney() {}
	
	public FinanceQuitMoney(Long id, 
			Double quitMoney, 
			Date quitDate, 
			String quitWindow, 
			String quitReason, 
			FinancePaymentInfo financePaymentInfo,
			WorkDealInfo workDealInfo,
			String status,
			String ression
			){
		this.id = id;
		this.quitMoney = quitMoney;
		this.quitDate = quitDate;
		this.quitWindow = quitWindow;
		this.quitReason = quitReason;
		this.financePaymentInfo = financePaymentInfo;
		this.workDealInfo = workDealInfo;
		this.status = status;
		this.ression = ression;
	}

//	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@SequenceGenerator(name="FINANCE_QUIT_MONEY_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="FINANCE_QUIT_MONEY_SEQUENCE")
	@Id
	@GeneratedValue(generator = "FINANCE_QUIT_MONEY_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "quit_money",columnDefinition = "FLOAT(126)")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	public FinancePaymentInfo getFinancePaymentInfo() {
		return financePaymentInfo;
	}

	public void setFinancePaymentInfo(FinancePaymentInfo financePaymentInfo) {
		this.financePaymentInfo = financePaymentInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_deal_info")
	public WorkDealInfo getWorkDealInfo() {
		return workDealInfo;
	}

	public void setWorkDealInfo(WorkDealInfo workDealInfo) {
		this.workDealInfo = workDealInfo;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ression")
	public String getRession() {
		return ression;
	}

	public void setRession(String ression) {
		this.ression = ression;
	}
	
	
	
	
	
	
	
	
}
