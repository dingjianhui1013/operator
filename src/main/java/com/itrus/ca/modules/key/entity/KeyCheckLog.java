package com.itrus.ca.modules.key.entity;

import java.sql.Timestamp;
import java.util.Date;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itrus.ca.common.persistence.DataEntity;

/**
 * 盘点记录
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "key_check_log")
public class KeyCheckLog extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private KeyGeneralInfo keyGeneralInfo;
	private Timestamp checkTime;
	private Integer beforeTotal;
	private Integer beforeOut;
	private Integer beforeResidue;
	private Integer afterResidue;
	private String fixRemark;
	private Integer fixType;
	private Integer afterTotal;
	private Integer afterOut;
	private Date startDate;
	private Date endDate;
	private KeyUsbKeyDepot keyUsbKeyDepot;
	private Boolean isReset;
	private Integer checkNumber;
	// Constructors

	/** default constructor */
	public KeyCheckLog() {
	}

	/** full constructor */
	public KeyCheckLog(KeyGeneralInfo keyGeneralInfo, Timestamp checkTime,
			Integer beforeTotal, Integer beforeOut, Integer beforeResidue,
			Integer afterResidue, String fixRemark, Integer fixType,
			Integer afterTotal, Integer afterOut, Date startDate, Date endDate,
			KeyUsbKeyDepot keyUsbKeyDepot,
			Boolean isReset,Integer checkNumber) {
		this.keyGeneralInfo = keyGeneralInfo;
		this.checkTime = checkTime;
		this.beforeTotal = beforeTotal;
		this.beforeOut = beforeOut;
		this.beforeResidue = beforeResidue;
		this.afterResidue = afterResidue;
		this.fixRemark = fixRemark;
		this.fixType = fixType;
		this.afterTotal = afterTotal;
		this.afterOut = afterOut;
		this.startDate = startDate;
		this.endDate = endDate;
		this.keyUsbKeyDepot=keyUsbKeyDepot;
		this.isReset = isReset;
		this.checkNumber = checkNumber;
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
	@JoinColumn(name = "key_general_info_id")
	public KeyGeneralInfo getKeyGeneralInfo() {
		return this.keyGeneralInfo;
	}

	public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
		this.keyGeneralInfo = keyGeneralInfo;
	}

	@Column(name = "check_time", length = 19)
	public Timestamp getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "before_total")
	public Integer getBeforeTotal() {
		return this.beforeTotal;
	}

	public void setBeforeTotal(Integer beforeTotal) {
		this.beforeTotal = beforeTotal;
	}

	@Column(name = "before_out")
	public Integer getBeforeOut() {
		return this.beforeOut;
	}

	public void setBeforeOut(Integer beforeOut) {
		this.beforeOut = beforeOut;
	}

	@Column(name = "before_residue")
	public Integer getBeforeResidue() {
		return this.beforeResidue;
	}

	public void setBeforeResidue(Integer beforeResidue) {
		this.beforeResidue = beforeResidue;
	}

	@Column(name = "after_residue")
	public Integer getAfterResidue() {
		return this.afterResidue;
	}

	public void setAfterResidue(Integer afterResidue) {
		this.afterResidue = afterResidue;
	}

	@Column(name = "fix_remark")
	public String getFixRemark() {
		return this.fixRemark;
	}

	public void setFixRemark(String fixRemark) {
		this.fixRemark = fixRemark;
	}

	@Column(name = "fix_type")
	public Integer getFixType() {
		return this.fixType;
	}

	public void setFixType(Integer fixType) {
		this.fixType = fixType;
	}

	@Column(name = "after_total")
	public Integer getAfterTotal() {
		return this.afterTotal;
	}

	public void setAfterTotal(Integer afterTotal) {
		this.afterTotal = afterTotal;
	}

	@Column(name = "after_out")
	public Integer getAfterOut() {
		return this.afterOut;
	}

	public void setAfterOut(Integer afterOut) {
		this.afterOut = afterOut;
	}

	@Column(name = "start_date")
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date")
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_usb_key_depot_id")
	public KeyUsbKeyDepot getKeyUsbKeyDepot() {
		return keyUsbKeyDepot;
	}

	public void setKeyUsbKeyDepot(KeyUsbKeyDepot keyUsbKeyDepot) {
		this.keyUsbKeyDepot = keyUsbKeyDepot;
	}

	@Column(name = "is_reset")
	public Boolean getIsReset() {
		return isReset;
	}

	public void setIsReset(Boolean isReset) {
		this.isReset = isReset;
	}
	
	@Column(name = "check_number")
	public Integer getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(Integer checkNumber) {
		this.checkNumber = checkNumber;
	}

}