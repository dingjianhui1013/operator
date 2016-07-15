package com.itrus.ca.modules.key.entity;

import static javax.persistence.GenerationType.IDENTITY;

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
import javax.persistence.Transient;

/**
 * KeyDepotGeneralStatistics entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "key_depot_general_statistics")
public class KeyDepotGeneralStatistics implements java.io.Serializable {

	// Fields

	private Long id;
	private KeyGeneralInfo keyGeneralInfo;
	private KeyUsbKeyDepot keyUsbKeyDepot;
	private Integer totalCount;//当前结余 
	private Integer inCount;
	private Integer outCount;
    private Integer alarmValue;
    private Integer totalEndCount;
	// Constructors

	/** default constructor */
	public KeyDepotGeneralStatistics() {
	}

	/** minimal constructor */
	public KeyDepotGeneralStatistics(Long id) {
		this.id = id;
	}

	/** full constructor */
	public KeyDepotGeneralStatistics(Long id, KeyGeneralInfo keyGeneralInfo,
			KeyUsbKeyDepot keyUsbKeyDepot, Integer totalCount, Integer inCount,
			Integer outCount) {
		this.id = id;
		this.keyGeneralInfo = keyGeneralInfo;
		this.keyUsbKeyDepot = keyUsbKeyDepot;
		this.totalCount = totalCount;
		this.inCount = inCount;
		this.outCount = outCount;
	}


	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="KEY_DEPOT_GENERAL_ST_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="KEY_DEPOT_GENERAL_ST_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "KEY_DEPOT_GENERAL_ST_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "key_general_info_id")
	public KeyGeneralInfo getKeyGeneralInfo() {
		return this.keyGeneralInfo;
	}

	public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
		this.keyGeneralInfo = keyGeneralInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_usb_key_depot_id")
	public KeyUsbKeyDepot getKeyUsbKeyDepot() {
		return this.keyUsbKeyDepot;
	}

	public void setKeyUsbKeyDepot(KeyUsbKeyDepot keyUsbKeyDepot) {
		this.keyUsbKeyDepot = keyUsbKeyDepot;
	}

	@Column(name = "total_count")
	public Integer getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	@Column(name = "in_count")
	public Integer getInCount() {
		return this.inCount;
	}

	public void setInCount(Integer inCount) {
		this.inCount = inCount;
	}

	@Column(name = "out_count")
	public Integer getOutCount() {
		return this.outCount;
	}

	public void setOutCount(Integer outCount) {
		this.outCount = outCount;
	}

	@Column(name = "alarm_value")
	public Integer getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(Integer alarmValue) {
		this.alarmValue = alarmValue;
	}

	
	@Transient
	public Integer getTotalEndCount() {
		return totalEndCount;
	}

	public void setTotalEndCount(Integer totalEndCount) {
		this.totalEndCount = totalEndCount;
	}
	
}