package com.itrus.ca.modules.receipt.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;
/**
 * ReceiptLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "receipt_type")
public class ReceiptType extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private Integer typeName;
	private String remark;

	// Constructors

	/** default constructor */
	public ReceiptType() {
	}

	/** minimal constructor */
	public ReceiptType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public ReceiptType(Long id, Integer typeName,String remark) {
		this.id = id;
		this.typeName = typeName;
		this.remark = remark;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="RECEIPT_TYPE_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="RECEIPT_TYPE_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "RECEIPT_TYPE_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "type_name")
	public Integer getTypeName() {
		return typeName;
	}

	public void setTypeName(Integer typeName) {
		this.typeName = typeName;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}