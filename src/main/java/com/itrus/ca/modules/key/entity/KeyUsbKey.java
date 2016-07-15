package com.itrus.ca.modules.key.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * key入库基本信息
 */
@Entity
@Table(name = "key_usb_key")
public class KeyUsbKey extends DataEntity implements java.io.Serializable {

	// Fields
	private Long id;
	private String description;
	private KeyUsbKeyDepot keyUsbKeyDepot;
	private KeyUsbKeyDepot fromkeyUsbKeyDepot;
	private Integer count;
	private int  inReason;
	private KeyGeneralInfo keyGeneralInfo;
	private String inReasonName;
	private Date startDate;
	
	// Constructors

	/** default constructor */
	public KeyUsbKey() {
	}

	/** full constructor */
	public KeyUsbKey(String description, KeyUsbKeyDepot keyUsbKeyDepot,
			int  inReason, KeyGeneralInfo keyGeneralInfo, Integer count,Date startDate,KeyUsbKeyDepot fromkeyUsbKeyDepot) {
		this.description = description;
		this.keyUsbKeyDepot = keyUsbKeyDepot;
		this.inReason = inReason;
		this.keyGeneralInfo = keyGeneralInfo;
		this.count = count;
		this.startDate = startDate;
		this.fromkeyUsbKeyDepot = fromkeyUsbKeyDepot;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="KEY_USB_KEY_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="KEY_USB_KEY_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "KEY_USB_KEY_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "count")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usb_key_depot_id")
	public KeyUsbKeyDepot getKeyUsbKeyDepot() {
		return keyUsbKeyDepot;
	}

	public void setKeyUsbKeyDepot(KeyUsbKeyDepot keyUsbKeyDepot) {
		this.keyUsbKeyDepot = keyUsbKeyDepot;
	}

	@Column(name = "in_reason")
	public int getInReason() {
		return inReason;
	}

	public void setInReason(int inReason) {
		this.inReason = inReason;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_general_info_id")
	public KeyGeneralInfo getKeyGeneralInfo() {
		return keyGeneralInfo;
	}

	public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
		this.keyGeneralInfo = keyGeneralInfo;
	}

	@Transient
	public String getInReasonName() {
		return inReasonName;
	}

	public void setInReasonName(String inReasonName) {
		this.inReasonName = inReasonName;
	}

	
	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_usb_key_depot_id")
	public KeyUsbKeyDepot getFromkeyUsbKeyDepot() {
		return fromkeyUsbKeyDepot;
	}

	public void setFromkeyUsbKeyDepot(KeyUsbKeyDepot fromkeyUsbKeyDepot) {
		this.fromkeyUsbKeyDepot = fromkeyUsbKeyDepot;
	}

	
	
	
}