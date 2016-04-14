/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;

/**
 * 印章日经营详细表Entity
 * @author DingJianHui
 * @version 2016-03-23
 */
@Entity
@Table(name = "statistic_seal_app_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StatisticSealAppData  implements java.io.Serializable   {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private Office office;  //网点
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

	public StatisticSealAppData() {
		super();
	}
	
	public StatisticSealAppData(Long id, Office office, ConfigApp app,
			Date statisticDate, Integer sealTotal, Double receiptTotal,
			Double sealMoney, Integer addFinance, Integer addContract,
			Integer addPersonal, Integer addOfficial, Integer renewFinance,
			Integer renewContract, Integer renewPersonal,
			Integer renewOfficial, Integer changeFinance,
			Integer changeContract, Integer changePersonal,
			Integer changeOfficial) {
		super();
		this.id = id;
		this.office = office;
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_statistic_statisticSealAppData")
	//@SequenceGenerator(name = "seq_statistic_statisticSealAppData", sequenceName = "seq_statistic_statisticSealAppData")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="office_id")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_id")
	public ConfigApp getApp() {
		return app;
	}

	public void setApp(ConfigApp app) {
		this.app = app;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="statistic_date", length = 10)
	public Date getStatisticDate() {
		return statisticDate;
	}

	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}
	@Column(name="seal_total")
	public Integer getSealTotal() {
		return sealTotal;
	}

	public void setSealTotal(Integer sealTotal) {
		this.sealTotal = sealTotal;
	}
	@Column(name="receipt_total")
	public Double getReceiptTotal() {
		return receiptTotal;
	}

	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}
	@Column(name="seal_money", precision = 20, scale = 3)
	public Double getSealMoney() {
		return sealMoney;
	}

	public void setSealMoney(Double sealMoney) {
		this.sealMoney = sealMoney;
	}
	@Column(name="add_finance")
	public Integer getAddFinance() {
		return addFinance;
	}

	public void setAddFinance(Integer addFinance) {
		this.addFinance = addFinance;
	}
	@Column(name="add_contract")
	public Integer getAddContract() {
		return addContract;
	}

	public void setAddContract(Integer addContract) {
		this.addContract = addContract;
	}
	@Column(name="add_personal")
	public Integer getAddPersonal() {
		return addPersonal;
	}

	public void setAddPersonal(Integer addPersonal) {
		this.addPersonal = addPersonal;
	}
	@Column(name="add_official")
	public Integer getAddOfficial() {
		return addOfficial;
	}

	public void setAddOfficial(Integer addOfficial) {
		this.addOfficial = addOfficial;
	}
	@Column(name="renew_finance")
	public Integer getRenewFinance() {
		return renewFinance;
	}

	public void setRenewFinance(Integer renewFinance) {
		this.renewFinance = renewFinance;
	}
	@Column(name="renew_contract")
	public Integer getRenewContract() {
		return renewContract;
	}

	public void setRenewContract(Integer renewContract) {
		this.renewContract = renewContract;
	}
	@Column(name="renew_personal")
	public Integer getRenewPersonal() {
		return renewPersonal;
	}

	public void setRenewPersonal(Integer renewPersonal) {
		this.renewPersonal = renewPersonal;
	}
	@Column(name="renew_official")
	public Integer getRenewOfficial() {
		return renewOfficial;
	}

	public void setRenewOfficial(Integer renewOfficial) {
		this.renewOfficial = renewOfficial;
	}
	@Column(name="change_finance")
	public Integer getChangeFinance() {
		return changeFinance;
	}

	public void setChangeFinance(Integer changeFinance) {
		this.changeFinance = changeFinance;
	}
	@Column(name="change_contract")
	public Integer getChangeContract() {
		return changeContract;
	}

	public void setChangeContract(Integer changeContract) {
		this.changeContract = changeContract;
	}
	@Column(name="change_personal")
	public Integer getChangePersonal() {
		return changePersonal;
	}

	public void setChangePersonal(Integer changePersonal) {
		this.changePersonal = changePersonal;
	}
	@Column(name="change_official")
	public Integer getChangeOfficial() {
		return changeOfficial;
	}

	public void setChangeOfficial(Integer changeOfficial) {
		this.changeOfficial = changeOfficial;
	}

	
}


