package com.itrus.ca.modules.statistic.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 日经营统计的列表
 */
@Entity
@Table(name = "statistic_app_data")
public class StatisticAppData implements java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice;
	private ConfigApp app;
	private Double keyOver;
	private Double receiptOver;
	private Date statisticDate;
	private Integer add1;
	private Integer add2;
	private Integer add4;
	private Integer add5;
	private Integer renew1;
	private Integer renew2;
	private Integer renew4;
	private Integer renew5;
	private Integer modifyNum;//变更数量
	private Integer reissueNum;//补办数量
	private Integer certTotal;
	private Integer keyTotal;
	private Double receiptTotal;
	private Double certMoneyTotal;

	// Constructors

	/** default constructor */
	public StatisticAppData() {
	}

	/** full constructor */
	public StatisticAppData(Office sysOffice, Integer keyIn, Double keyOver,
			Integer keyStoreTotal, Double receiptIn, Double receiptOver,
			Double receiptStoreTotal, Date statisticDate, Integer add1,
			Integer add2, Integer add4, Integer renew1, Integer renew2,
			Integer renew4, Integer modifyNum, Integer reissueNum,
			Integer certTotal, Integer keyTotal, Double receiptTotal,
			Double certMoneyTotal) {
		this.sysOffice = sysOffice;
		this.keyOver = keyOver;
		this.receiptOver = receiptOver;
		this.statisticDate = statisticDate;
		this.add1 = add1;
		this.add2 = add2;
		this.add4 = add4;
		this.renew1 = renew1;
		this.renew2 = renew2;
		this.renew4 = renew4;
		this.modifyNum = modifyNum;
		this.reissueNum = reissueNum;
		this.certTotal = certTotal;
		this.keyTotal = keyTotal;
		this.receiptTotal = receiptTotal;
		this.certMoneyTotal = certMoneyTotal;
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
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.sysOffice;
	}

	public void setOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}


	@Column(name = "key_over", precision = 20, scale = 3)
	public Double getKeyOver() {
		return this.keyOver;
	}

	public void setKeyOver(Double keyOver) {
		this.keyOver = keyOver;
	}


	@Column(name = "receipt_over", precision = 20, scale = 3)
	public Double getReceiptOver() {
		return this.receiptOver;
	}

	public void setReceiptOver(Double receiptOver) {
		this.receiptOver = receiptOver;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "statistic_date", length = 10)
	public Date getStatisticDate() {
		return this.statisticDate;
	}

	public void setStatisticDate(Date statisticDate) {
		this.statisticDate = statisticDate;
	}

	@Column(name = "add1")
	public Integer getAdd1() {
		return this.add1;
	}

	public void setAdd1(Integer add1) {
		this.add1 = add1;
	}

	@Column(name = "add2")
	public Integer getAdd2() {
		return this.add2;
	}

	public void setAdd2(Integer add2) {
		this.add2 = add2;
	}

	@Column(name = "add4")
	public Integer getAdd4() {
		return this.add4;
	}

	public void setAdd4(Integer add4) {
		this.add4 = add4;
	}

	@Column(name = "renew1")
	public Integer getRenew1() {
		return this.renew1;
	}

	public void setRenew1(Integer renew1) {
		this.renew1 = renew1;
	}

	@Column(name = "renew2")
	public Integer getRenew2() {
		return this.renew2;
	}

	public void setRenew2(Integer renew2) {
		this.renew2 = renew2;
	}

	@Column(name = "renew4")
	public Integer getRenew4() {
		return this.renew4;
	}

	public void setRenew4(Integer renew4) {
		this.renew4 = renew4;
	}

	@Column(name = "modify_num")
	public Integer getModifyNum() {
		return this.modifyNum;
	}

	public void setModifyNum(Integer modifyNum) {
		this.modifyNum = modifyNum;
	}

	@Column(name = "reissue_num")
	public Integer getReissueNum() {
		return this.reissueNum;
	}

	public void setReissueNum(Integer reissueNum) {
		this.reissueNum = reissueNum;
	}

	@Column(name = "cert_total")
	public Integer getCertTotal() {
		return this.certTotal;
	}

	public void setCertTotal(Integer certTotal) {
		this.certTotal = certTotal;
	}

	@Column(name = "key_total")
	public Integer getKeyTotal() {
		return this.keyTotal;
	}

	public void setKeyTotal(Integer keyTotal) {
		this.keyTotal = keyTotal;
	}

	@Column(name = "receipt_total", precision = 20, scale = 3)
	public Double getReceiptTotal() {
		return this.receiptTotal;
	}

	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}

	@Column(name = "cert_money_total", precision = 20, scale = 3)
	public Double getCertMoneyTotal() {
		return this.certMoneyTotal;
	}

	public void setCertMoneyTotal(Double certMoneyTotal) {
		this.certMoneyTotal = certMoneyTotal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_id")
	public ConfigApp getApp() {
		return app;
	}

	public void setApp(ConfigApp appId) {
		this.app = appId;
	}

	@Column(name = "add5")
	public Integer getAdd5() {
		return add5;
	}

	public void setAdd5(Integer add5) {
		this.add5 = add5;
	}
	@Column(name = "renew5")
	public Integer getRenew5() {
		return renew5;
	}

	public void setRenew5(Integer renew5) {
		this.renew5 = renew5;
	}
}