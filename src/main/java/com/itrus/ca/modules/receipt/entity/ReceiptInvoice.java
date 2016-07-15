package com.itrus.ca.modules.receipt.entity;

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

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.work.entity.WorkPayInfo;


/**
 * 发票出库单
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "receipt_invoice")
public class ReceiptInvoice extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private WorkPayInfo workPayInfo;
	private Double receiptMoney;
	private ReceiptDepotInfo receiptDepotInfo;
	private String companyName;
	private Integer receiptType;
	private ReceiptType type;//例如：2元、10元、100元
	private Integer count;
	private Long dealInfoId;
	
	

	// Constructors

	/** default constructor */
	public ReceiptInvoice() {
	}

	/** full constructor */
	public ReceiptInvoice(WorkPayInfo workPayInfo, Double receiptMoney,Long dealInfoId) {
		this.workPayInfo = workPayInfo;
		this.receiptMoney = receiptMoney;
		this.dealInfoId = dealInfoId ;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="RECEIPT_INVOICE_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="RECEIPT_INVOICE_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "RECEIPT_INVOICE_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pay_info_id")
	public WorkPayInfo getWorkPayInfo() {
		return this.workPayInfo;
	}

	public void setWorkPayInfo(WorkPayInfo workPayInfo) {
		this.workPayInfo = workPayInfo;
	}

	@Column(name = "receipt_money", precision = 20, scale = 3)
	public Double getReceiptMoney() {
		return this.receiptMoney;
	}

	public void setReceiptMoney(Double receiptMoney) {
		this.receiptMoney = receiptMoney;
	}

	@Column(name = "companyName")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depot_id")
	public ReceiptDepotInfo getReceiptDepotInfo() {
		return receiptDepotInfo;
	}

	public void setReceiptDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		this.receiptDepotInfo = receiptDepotInfo;
	}
	
	@Column(name = "receipt_type")
	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}
	

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="type")
	public ReceiptType getType() {
		return type;
	}

	public void setType(ReceiptType type) {
		this.type = type;
	}

	@Column(name="count")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Column(name="deal_info_Id")
	public Long getDealInfoId() {
		return dealInfoId;
	}

	public void setDealInfoId(Long dealInfoId) {
		this.dealInfoId = dealInfoId;
	}
	
	
	
	
}