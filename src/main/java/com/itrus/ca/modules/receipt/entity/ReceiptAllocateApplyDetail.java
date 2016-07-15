package com.itrus.ca.modules.receipt.entity;

import java.sql.Timestamp;
import java.util.List;

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
import com.itrus.ca.modules.key.entity.KeyAllocateApply;
import com.itrus.ca.modules.key.entity.KeyAllocateApplyDetailed;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 发票调拨详细信息
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "receipt_allocate_apply_detail")
public class ReceiptAllocateApplyDetail  implements java.io.Serializable {

	// Fields

	private Long id;
	private Integer applyNum;
	private ReceiptType receiptType;
	private ReceiptAllocateApply receiptAllocateApply;
	// Constructors

	/** default constructor */
	public ReceiptAllocateApplyDetail() {
	}

	/** full constructor */
	public ReceiptAllocateApplyDetail(Integer applyNum,
	ReceiptType receiptType,
	ReceiptAllocateApply receiptAllocateApply
) {
		this.applyNum = applyNum;
		this.receiptType = receiptType;
		this.receiptAllocateApply = receiptAllocateApply;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="RECEIPT_ALLOCATE_APP_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="RECEIPT_ALLOCATE_APP_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "RECEIPT_ALLOCATE_APP_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="apply_num")
	public Integer getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "receipt_allocate_apply")
	public ReceiptAllocateApply getReceiptAllocateApply() {
		return receiptAllocateApply;
	}

	public void setReceiptAllocateApply(ReceiptAllocateApply receiptAllocateApply) {
		this.receiptAllocateApply = receiptAllocateApply;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "receipt_type")
	public ReceiptType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}

	
}