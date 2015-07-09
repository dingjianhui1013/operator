package com.itrus.ca.modules.receipt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.itrus.ca.common.persistence.DataEntity;
/**
 * ReceiptDepotInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "receipt_depot_type_statistics")
public class ReceiptDepotTypeStatistics extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	
	private ReceiptDepotInfo receiptDepotInfo;
	private ReceiptType receiptType;
	private Integer inCount;
	private Integer outCount;
	private Integer alarmCount;
	private Integer totalEndCount;
	
	
	
	
	// Constructors

	/** default constructor */
	public ReceiptDepotTypeStatistics() {
	}

	/** full constructor */
	public ReceiptDepotTypeStatistics(
			ReceiptDepotInfo receiptDepotInfo,
			ReceiptType receiptType,
			Integer inCount,
			Integer outCount,
			Integer alarmCount,
			Integer totalEndCount) {
		this.receiptDepotInfo = receiptDepotInfo;
		this.receiptType = receiptType;
		this.inCount = inCount;
		this.outCount = outCount;
		this.alarmCount = alarmCount;
		this.totalEndCount = totalEndCount;
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "depot_id")
	public ReceiptDepotInfo getReceiptDepotInfo() {
		return receiptDepotInfo;
	}

	public void setReceiptDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		this.receiptDepotInfo = receiptDepotInfo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "receipt_type")
	public ReceiptType getReceiptType() {
		return receiptType;
	}
	
	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}
	
	
	@Column(name="in_count")
	public Integer getInCount() {
		return inCount;
	}

	public void setInCount(Integer inCount) {
		this.inCount = inCount;
	}

	@Column(name="out_count")
	public Integer getOutCount() {
		return outCount;
	}

	public void setOutCount(Integer outCount) {
		this.outCount = outCount;
	}

	@Column(name="alarm_count")
	public Integer getAlarmCount() {
		return alarmCount;
	}

	public void setAlarmCount(Integer alarmCount) {
		this.alarmCount = alarmCount;
	}

	@Column(name="total_end_count")
	public Integer getTotalEndCount() {
		return totalEndCount;
	}

	public void setTotalEndCount(Integer totalEndCount) {
		this.totalEndCount = totalEndCount;
	}

	

	
}