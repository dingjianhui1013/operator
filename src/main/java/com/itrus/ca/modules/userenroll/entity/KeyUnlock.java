package com.itrus.ca.modules.userenroll.entity;

import java.sql.Timestamp;
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

import javax.persistence.Transient;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * KeyUnlock entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "key_unlock")
public class KeyUnlock extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice;
	private Timestamp approveTime;
	private String certCn;
	private Timestamp createTime;
	private Timestamp downloadTime;
	private String keySn;
	private Timestamp rejectTime;
	private String repCode;
	private String reqCode;
	private String status;
	private Timestamp unlockTime;
	private String approveSuggest;
	private Long approveAdminId;
	private Date validDate;//解锁有效期
	private Integer state; //是否已查看（0为未查看，1为已查看）
	
	private String companyName;//单位名称
	private String attnName;//经办人
	

	// Constructors

	/** default constructor */
	public KeyUnlock() {
	}

	/** minimal constructor */
	public KeyUnlock(Timestamp createTime, String keySn, String reqCode,
			String status) {
		this.createTime = createTime;
		this.keySn = keySn;
		this.reqCode = reqCode;
		this.status = status;
	}

	/** full constructor */
	public KeyUnlock(Office sysOffice, Timestamp approveTime, String certCn,
			Timestamp createTime, Timestamp downloadTime, String keySn,
			Timestamp rejectTime, String repCode, String reqCode,
			String status, Timestamp unlockTime, String approveSuggest,
			Long approveAdminId) {
		this.sysOffice = sysOffice;
		this.approveTime = approveTime;
		this.certCn = certCn;
		this.createTime = createTime;
		this.downloadTime = downloadTime;
		this.keySn = keySn;
		this.rejectTime = rejectTime;
		this.repCode = repCode;
		this.reqCode = reqCode;
		this.status = status;
		this.unlockTime = unlockTime;
		this.approveSuggest = approveSuggest;
		this.approveAdminId = approveAdminId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMON_SEQUENCE")
	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
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

	@Column(name = "approve_time", length = 19)
	public Timestamp getApproveTime() {
		return this.approveTime;
	}

	public void setApproveTime(Timestamp approveTime) {
		this.approveTime = approveTime;
	}

	@Column(name = "cert_cn")
	public String getCertCn() {
		return this.certCn;
	}

	public void setCertCn(String certCn) {
		this.certCn = certCn;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "download_time", length = 19)
	public Timestamp getDownloadTime() {
		return this.downloadTime;
	}

	public void setDownloadTime(Timestamp downloadTime) {
		this.downloadTime = downloadTime;
	}

	@Column(name = "key_sn", nullable = false)
	public String getKeySn() {
		return this.keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	@Column(name = "reject_time", length = 19)
	public Timestamp getRejectTime() {
		return this.rejectTime;
	}

	public void setRejectTime(Timestamp rejectTime) {
		this.rejectTime = rejectTime;
	}

	@Column(name = "rep_code")
	public String getRepCode() {
		return this.repCode;
	}

	public void setRepCode(String repCode) {
		this.repCode = repCode;
	}

	@Column(name = "req_code", nullable = false)
	public String getReqCode() {
		return this.reqCode;
	}

	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}

	@Column(name = "status", nullable = false)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "unlock_time", length = 19)
	public Timestamp getUnlockTime() {
		return this.unlockTime;
	}

	public void setUnlockTime(Timestamp unlockTime) {
		this.unlockTime = unlockTime;
	}

	@Column(name = "approve_suggest")
	public String getApproveSuggest() {
		return this.approveSuggest;
	}

	public void setApproveSuggest(String approveSuggest) {
		this.approveSuggest = approveSuggest;
	}

	@Column(name = "approve_admin_id")
	public Long getApproveAdminId() {
		return this.approveAdminId;
	}

	public void setApproveAdminId(Long approveAdminId) {
		this.approveAdminId = approveAdminId;
	}

	@Column(name = "valid_date")
	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	@Column(name = "state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Transient
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Transient
	public String getAttnName() {
		return attnName;
	}

	public void setAttnName(String attnName) {
		this.attnName = attnName;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}