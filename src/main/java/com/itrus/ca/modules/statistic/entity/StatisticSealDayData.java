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
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;

/**
 * 印章日经营Entity
 * @author DingJianHui
 * @version 2016-03-23
 */
@Entity
@Table(name = "statistic_seal_day_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StatisticSealDayData implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private Office sysOffice; //网点
	private Double receiptIn; //发票入库
	private Double receiptTotal;//发票总量
	private Integer sealDay;   //印章
	private Double sealMoney;  //费用
	private Double receiptDay; //今日发票
	private Double receiptSurplus;//发票剩余
	private Date statisticDate;    //统计日期
	private Date createDate;       //创建日期
//	private Date receiptStoreTota;//
	public StatisticSealDayData() {
		super();
	}

	public StatisticSealDayData(Long id, Office sysOffice, Double receiptIn,
			Double receiptTotal, Integer sealDay, Double sealMoney,
			Double receiptDay, Double receiptSurplus, Date statisticDate,
			Date createDate) {
		super();
		this.id = id;
		this.sysOffice = sysOffice;
		this.receiptIn = receiptIn;
		this.receiptTotal = receiptTotal;
		this.sealDay = sealDay;
		this.sealMoney = sealMoney;
		this.receiptDay = receiptDay;
		this.receiptSurplus = receiptSurplus;
		this.statisticDate = statisticDate;
		this.createDate = createDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_statistic_statisticSealDayData")
	//@SequenceGenerator(name = "seq_statistic_statisticSealDayData", sequenceName = "seq_statistic_statisticSealDayData")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getSysOffice() {
		return sysOffice;
	}

	public void setSysOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}

	@Column(name="receipt_ind")
	public Double getReceiptIn() {
		return receiptIn;
	}

	public void setReceiptIn(Double receiptIn) {
		this.receiptIn = receiptIn;
	}
	@Column(name="receipt_total")
	public Double getReceiptTotal() {
		return receiptTotal;
	}

	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}
	@Column(name="seal_day")
	public Integer getSealDay() {
		return sealDay;
	}

	public void setSealDay(Integer sealDay) {
		this.sealDay = sealDay;
	}
	@Column(name="seal_money")
	public Double getSealMoney() {
		return sealMoney;
	}

	public void setSealMoney(Double sealMoney) {
		this.sealMoney = sealMoney;
	}
	@Column(name="receipt_day")
	public Double getReceiptDay() {
		return receiptDay;
	}

	public void setReceiptDay(Double receiptDay) {
		this.receiptDay = receiptDay;
	}
	@Column(name="receipt_surplus")
	public Double getReceiptSurplus() {
		return receiptSurplus;
	}

	public void setReceiptSurplus(Double receiptSurplus) {
		this.receiptSurplus = receiptSurplus;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "statistic_date", length = 10)
	public Date getStatisticDate() {
		return statisticDate;
	}

	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}


