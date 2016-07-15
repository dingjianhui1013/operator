package com.itrus.ca.modules.work.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * WorkCertTrustApply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_cert__trust_apply")
public class WorkCertTrustApply implements java.io.Serializable {

	// Fields

	private Long id;
	private WorkCertInfo workCertInfo;
	private String certSn;
	private String keySn;
	private Integer applyCount;
	private Double money;
	private Date applyDate;
	private Integer year;
	private Integer status;//状态0待审核 1审核通过 -1 审核拒绝
	private String sn;//业务编号
	private String suggest;
	private WorkPayInfo workPayInfo;
	private Integer state; //是否查看状态（0为未查看，1为已查看）

	// Constructors

	/** default constructor */
	public WorkCertTrustApply() {
	}

	/** full constructor */
	public WorkCertTrustApply(WorkCertInfo workCertInfo, String certSn,
			Integer applyCount, Double money, Date applyDate, Integer year) {
		this.workCertInfo = workCertInfo;
		this.certSn = certSn;
		this.applyCount = applyCount;
		this.money = money;
		this.applyDate = applyDate;
		this.year = year;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="WORK_CERT__TRUST_APP_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="WORK_CERT__TRUST_APP_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "WORK_CERT__TRUST_APP_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cert_id")
	public WorkCertInfo getWorkCertInfo() {
		return this.workCertInfo;
	}

	public void setWorkCertInfo(WorkCertInfo workCertInfo) {
		this.workCertInfo = workCertInfo;
	}

	@Column(name = "cert_sn")
	public String getCertSn() {
		return this.certSn;
	}

	public void setCertSn(String certSn) {
		this.certSn = certSn;
	}

	@Column(name = "apply_count")
	public Integer getApplyCount() {
		return this.applyCount;
	}

	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}

	@Column(name = "money", precision = 10)
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "apply_date")
	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	@Column(name = "year")
	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Column(name = "key_sn")
	public String getKeySn() {
		return keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "sn")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "suggest")
	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_pay_info")
	public WorkPayInfo getWorkPayInfo() {
		return workPayInfo;
	}

	public void setWorkPayInfo(WorkPayInfo workPayInfo) {
		this.workPayInfo = workPayInfo;
	}

	@Column(name = "state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}