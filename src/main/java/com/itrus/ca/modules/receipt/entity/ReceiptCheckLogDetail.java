package com.itrus.ca.modules.receipt.entity;

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

import net.sf.ehcache.search.aggregator.Count;

import com.itrus.ca.modules.sys.entity.Office;

/**
 * 发票盘点记录
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "receipt_check_log_detail")
public class ReceiptCheckLogDetail implements java.io.Serializable {

	// Fields

	private Long id;
	private ReceiptCheckLog receiptCheckLog;
	private ReceiptType receiptType;
	private Integer count;
	private Integer countPrice;
	private Integer type;//1.之前的数量2.入库数量3.出库数量4.库存余量5.盘点余量

	// Constructors

	/** default constructor */
	public ReceiptCheckLogDetail() {
	}

	/** full constructor */
	public ReceiptCheckLogDetail(
			ReceiptCheckLog receiptCheckLog,
			ReceiptType receiptType,
			Integer count,
			Integer countPrice,
			Integer type
			) {
		this.receiptCheckLog =receiptCheckLog;
		this.receiptType = receiptType;
		this.count = count;
		this.countPrice = countPrice;
		this.type = type;
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="check_id")
	public ReceiptCheckLog getReceiptCheckLog() {
		return receiptCheckLog;
	}

	public void setReceiptCheckLog(ReceiptCheckLog receiptCheckLog) {
		this.receiptCheckLog = receiptCheckLog;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id")
	public ReceiptType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}

	@Column(name="count")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name="count_price")
	public Integer getCountPrice() {
		return countPrice;
	}

	public void setCountPrice(Integer countPrice) {
		this.countPrice = countPrice;
	}

	@Column(name="type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


	
	
	
}