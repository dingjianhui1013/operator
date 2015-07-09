package com.itrus.ca.modules.receipt.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;

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

/**
 * ReceiptLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "receipt_log")
public class ReceiptLog implements java.io.Serializable {

	// Fields

	private Long id;
	private ReceiptDepotInfo receiptDepotInfo;
	private Double receiptTotal;
	private Double receiptOut;
	private Double receiptResidue;
	private Timestamp createDate;

	// Constructors

	/** default constructor */
	public ReceiptLog() {
	}

	/** minimal constructor */
	public ReceiptLog(Long id) {
		this.id = id;
	}

	/** full constructor */
	public ReceiptLog(Long id, ReceiptDepotInfo receiptDepotInfo,
			Double receiptTotal, Double receiptOut, Double receiptResidue,
			Timestamp createDate) {
		this.id = id;
		this.receiptDepotInfo = receiptDepotInfo;
		this.receiptTotal = receiptTotal;
		this.receiptOut = receiptOut;
		this.receiptResidue = receiptResidue;
		this.createDate = createDate;
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
	@JoinColumn(name = "depot_id")
	public ReceiptDepotInfo getReceiptDepotInfo() {
		return this.receiptDepotInfo;
	}

	public void setReceiptDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		this.receiptDepotInfo = receiptDepotInfo;
	}

	@Column(name = "receipt_total", precision = 255, scale = 0)
	public Double getReceiptTotal() {
		return this.receiptTotal;
	}

	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}

	@Column(name = "receipt_out", precision = 255, scale = 0)
	public Double getReceiptOut() {
		return this.receiptOut;
	}

	public void setReceiptOut(Double receiptOut) {
		this.receiptOut = receiptOut;
	}

	@Column(name = "receipt_residue", precision = 255, scale = 0)
	public Double getReceiptResidue() {
		return this.receiptResidue;
	}

	public void setReceiptResidue(Double receiptResidue) {
		this.receiptResidue = receiptResidue;
	}

	@Column(name = "createDate", length = 19)
	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

}