package com.itrus.ca.modules.finance.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkPayInfo;

/**
 * FinancePaymentInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "finance_payment_info")
public class FinancePaymentInfo extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigApp configApp;
	private Double paymentMoney;
	private Double residueMoney;
	private Integer paymentMethod;
	private String paymentMethodName;
	private String commUserName;
	private String commMobile;
	private String remark;
	private Integer bingdingTimes;
	private String company;
	private Date payDate;
	private String serialNum;			//交易流水号
	private String paymentAccount;		//付款账号
	private String paymentBank;			//付款银行
	private Integer quitMoneyStatus;		//退款状态      0：未退款      1：已退款
	private String distinguish;			// 区别导入和手动添加  0为添加 null为导入
	
	private Long officeId;
	private Long areaId;
	
	
	// Constructors

	/** default constructor */
	public FinancePaymentInfo() {
	}

	/** full constructor */
	public FinancePaymentInfo(ConfigApp configApp,
			Double paymentMoney,
			Integer paymentMethod, String commUserName, String commMobile,
			String remark, Timestamp createDate, String delFlag,
			String remarks, Timestamp updateDate, Integer bingdingTimes,String company,Date payDate,String serialNum,
			String paymentAccount,String paymentBank,String distinguish,Long officeId,Long areaId) {
		this.configApp = configApp;
		this.paymentMoney = paymentMoney;
		this.paymentMethod = paymentMethod;
		this.commUserName = commUserName;
		this.commMobile = commMobile;
		this.remark = remark;
		this.createDate = createDate;
		this.delFlag = delFlag;
		this.remarks = remarks;
		this.updateDate = updateDate;
		this.bingdingTimes = bingdingTimes;
		this.company = company;
		this.payDate = payDate;
		this.serialNum = serialNum;
		this.paymentAccount = paymentAccount;
		this.paymentBank = paymentBank;
		this.distinguish = distinguish;
		this.officeId = officeId;
		this.areaId = areaId;
	}

	// Property accessors
	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "COMMON_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_id")
	public ConfigApp getConfigApp() {
		return this.configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}

	@Column(name = "payment_money", precision = 12, scale = 3)
	public Double getPaymentMoney() {
		return this.paymentMoney;
	}

	public void setPaymentMoney(Double paymentMoney) {
		this.paymentMoney = paymentMoney;
	}

	@Column(name = "payment_method")
	public Integer getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Column(name = "comm_user_name")
	public String getCommUserName() {
		return this.commUserName;
	}

	public void setCommUserName(String commUserName) {
		this.commUserName = commUserName;
	}

	@Column(name = "comm_mobile")
	public String getCommMobile() {
		return this.commMobile;
	}

	public void setCommMobile(String commMobile) {
		this.commMobile = commMobile;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Column(name = "bingding_times")
	public Integer getBingdingTimes() {
		return this.bingdingTimes==null? 0:this.bingdingTimes;
	}

	public void setBingdingTimes(Integer bingdingTimes) {
		this.bingdingTimes = bingdingTimes;
	}

	@Transient
	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}


	@Column(name = "residue_money", precision = 10, scale = 2)
	public Double getResidueMoney() {
		return residueMoney;
	}

	public void setResidueMoney(Double residueMoney) {
		this.residueMoney = residueMoney;
	}
	
	@Column(name = "company")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}


	@Column(name = "pay_date")
	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	@Column(name = "serial_num")
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	@Column(name = "payment_account")
	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	@Column(name = "payment_bank")
	public String getPaymentBank() {
		return paymentBank;
	}

	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}

	@Column(name = "quit_money_status")
	public Integer getQuitMoneyStatus() {
		return quitMoneyStatus;
	}

	public void setQuitMoneyStatus(Integer quitMoneyStatus) {
		this.quitMoneyStatus = quitMoneyStatus;
	}
	
	@Column(name = "distinguish")
	public String getDistinguish() {
		return distinguish;
	}

	public void setDistinguish(String distinguish) {
		this.distinguish = distinguish;
	}
	
	@Column(name = "office_id")
	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	@Column(name = "area_id")
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	
	
	
}