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
import javax.persistence.criteria.CriteriaBuilder.In;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * 发票入库
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "receipt_enter_info")
public class ReceiptEnterInfo extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private ReceiptDepotInfo receiptDepotInfo;
	private Double receiptMoney;
	private Integer receiptType;
	private Double beforMoney;
	private Double now_Money;
	private ReceiptType type;//例如：2元、10元、100元
	private Integer count;
	
	// Constructors

	/** default constructor */
	public ReceiptEnterInfo() {
	}

	/** full constructor */
	public ReceiptEnterInfo(ReceiptDepotInfo receiptDepotInfo,
			Double receiptMoney, Integer receiptType,ReceiptType type,Integer count) {
		this.receiptDepotInfo = receiptDepotInfo;
		this.receiptMoney = receiptMoney;
		this.receiptType = receiptType;
		this.type = type;
		this.count = count;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="RECEIPT_ENTER_INFO_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="RECEIPT_ENTER_INFO_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "RECEIPT_ENTER_INFO_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depot_id")
	public ReceiptDepotInfo getReceiptDepotInfo() {
		return receiptDepotInfo;
	}

	public void setReceiptDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		this.receiptDepotInfo = receiptDepotInfo;
	}

	@Column(name = "receipt_money", precision = 20, scale = 3)
	public Double getReceiptMoney() {
		return this.receiptMoney;
	}

	public void setReceiptMoney(Double receiptMoney) {
		this.receiptMoney = receiptMoney;
	}

	@Column(name = "receipt_type")
	public Integer getReceiptType() {
		return this.receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	@Column(name = "befor_money")
	public Double getBeforMoney() {
		return beforMoney;
	}

	public void setBeforMoney(Double beforMoney) {
		this.beforMoney = beforMoney;
	}

	@Column(name = "now_money")
	public Double getNow_Money() {
		return now_Money;
	}

	public void setNow_Money(Double now_Money) {
		this.now_Money = now_Money;
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
	
	
	
	
}