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
import javax.persistence.SequenceGenerator;
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
	private Integer addOne; 	//新增财务章
	private Integer addTwo;	//新增合同章
	private Integer addThree;	//新增个人章
	private Integer addFour;	//新增个人章
	private Integer addFive;	//新增公章
	
	private Integer renewOne; 	//更新财务章
	private Integer renewTwo;	//更新合同章
	private Integer renewThree;	//更新个人章
	private Integer renewFour;	//更新公章
	private Integer renewFive;	//更新公章
	private Integer changeOne; 	//变更财务章
	private Integer changeTwo;	//变更合同章
	private Integer changeThree;	//变更个人章
	private Integer changeFour;	//变更公章
	private Integer changeFive;	//变更公章

	public StatisticSealAppData() {
		super();
	}
	
	public StatisticSealAppData(Long id, Office office, ConfigApp app,
			Date statisticDate, Integer sealTotal, Double receiptTotal,
			Double sealMoney, Integer addOne, Integer addTwo, Integer addThree,
			Integer addFour, Integer addFive, Integer renewOne,
			Integer renewTwo, Integer renewThree, Integer renewFour,
			Integer renewFive, Integer changeOne, Integer changeTwo,
			Integer changeThree, Integer changeFour, Integer changeFive) {
		super();
		this.id = id;
		this.office = office;
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



	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATISTIC_SEAL_APP_D_SEQUENCE")
	//@SequenceGenerator(name = "seq_statistic_statisticSealAppData", sequenceName = "seq_statistic_statisticSealAppData")
	@SequenceGenerator(name="STATISTIC_SEAL_APP_D_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="STATISTIC_SEAL_APP_D_SEQUENCE")
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
	@Column(name="receipt_total",columnDefinition = "NUMBER")
	public Double getReceiptTotal() {
		return receiptTotal;
	}

	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}
	@Column(name="seal_money", precision = 20, scale = 3,columnDefinition = "FLOAT(126)")
	public Double getSealMoney() {
		return sealMoney;
	}

	public void setSealMoney(Double sealMoney) {
		this.sealMoney = sealMoney;
	}
	@Column(name="add_one")
	public Integer getAddOne() {
		return addOne;
	}

	public void setAddOne(Integer addOne) {
		this.addOne = addOne;
	}
	@Column(name="add_two")
	public Integer getAddTwo() {
		return addTwo;
	}

	public void setAddTwo(Integer addTwo) {
		this.addTwo = addTwo;
	}
	@Column(name="add_three")
	public Integer getAddThree() {
		return addThree;
	}

	public void setAddThree(Integer addThree) {
		this.addThree = addThree;
	}

	@Column(name="add_four")
	public Integer getAddFour() {
		return addFour;
	}

	public void setAddFour(Integer addFour) {
		this.addFour = addFour;
	}

	@Column(name="add_five")
	public Integer getAddFive() {
		return addFive;
	}

	public void setAddFive(Integer addFive) {
		this.addFive = addFive;
	}
	@Column(name="renew_one")
	public Integer getRenewOne() {
		return renewOne;
	}

	public void setRenewOne(Integer renewOne) {
		this.renewOne = renewOne;
	}
	@Column(name="renew_two")
	public Integer getRenewTwo() {
		return renewTwo;
	}

	public void setRenewTwo(Integer renewTwo) {
		this.renewTwo = renewTwo;
	}
	@Column(name="renew_three")
	public Integer getRenewThree() {
		return renewThree;
	}

	public void setRenewThree(Integer renewThree) {
		this.renewThree = renewThree;
	}
	@Column(name="renew_four")
	public Integer getRenewFour() {
		return renewFour;
	}

	public void setRenewFour(Integer renewFour) {
		this.renewFour = renewFour;
	}
	@Column(name="renew_five")
	public Integer getRenewFive() {
		return renewFive;
	}

	public void setRenewFive(Integer renewFive) {
		this.renewFive = renewFive;
	}
	@Column(name="change_one")
	public Integer getChangeOne() {
		return changeOne;
	}

	public void setChangeOne(Integer changeOne) {
		this.changeOne = changeOne;
	}
	@Column(name="change_two")
	public Integer getChangeTwo() {
		return changeTwo;
	}

	public void setChangeTwo(Integer changeTwo) {
		this.changeTwo = changeTwo;
	}
	@Column(name="change_three")
	public Integer getChangeThree() {
		return changeThree;
	}

	public void setChangeThree(Integer changeThree) {
		this.changeThree = changeThree;
	}
	@Column(name="change_four")
	public Integer getChangeFour() {
		return changeFour;
	}

	public void setChangeFour(Integer changeFour) {
		this.changeFour = changeFour;
	}
	@Column(name="change_five")
	public Integer getChangeFive() {
		return changeFive;
	}

	public void setChangeFive(Integer changeFive) {
		this.changeFive = changeFive;
	}
	

	
}


