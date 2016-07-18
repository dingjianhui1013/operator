package com.itrus.ca.modules.receipt.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.key.entity.KeyAllocateApplyDetailed;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 发票调拨申请
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "receipt_allocate_apply")
public class ReceiptAllocateApply extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice;
	private ReceiptDepotInfo receiptDepotInfo;
	
	private Long auditDepotId;        //新增   审核库房id
	
	
	private Integer applyNewNum;
	private Timestamp warehouseDate;
	private Integer leavel;
	private Integer state;
	private Integer receiptType;
	private Double money;
	
	private Long officeId;
	private Long areaId;
	
	
	
	
	
	private List<ReceiptAllocateApplyDetail> receiptApplyDetails; 	
	// Constructors

	/** default constructor */
	public ReceiptAllocateApply() {
	}

	/** full constructor */
	public ReceiptAllocateApply(Office sysOffice,
			ReceiptDepotInfo receiptDepotInfo, Integer applyNewNum,
			Timestamp warehouseDate, Integer leavel, Integer state,
			Integer receiptType, Long officeId, Long areaId) {
		this.sysOffice = sysOffice;
		this.receiptDepotInfo = receiptDepotInfo;
		this.applyNewNum = applyNewNum;
		this.warehouseDate = warehouseDate;
		this.leavel = leavel;
		this.state = state;
		this.receiptType = receiptType;
		this.officeId = officeId;
		this.areaId = areaId;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.sysOffice;
	}

	public void setOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depot_id")
	public ReceiptDepotInfo getReceiptDepotInfo() {
		return receiptDepotInfo;
	}

	public void setReceiptDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		this.receiptDepotInfo = receiptDepotInfo;
	}

	@Column(name = "apply_new_num")
	public Integer getApplyNewNum() {
		return this.applyNewNum;
	}

	public void setApplyNewNum(Integer applyNewNum) {
		this.applyNewNum = applyNewNum;
	}

	@Column(name = "warehouse_date", length = 19)
	public Timestamp getWarehouseDate() {
		return this.warehouseDate;
	}

	public void setWarehouseDate(Timestamp warehouseDate) {
		this.warehouseDate = warehouseDate;
	}

	@Column(name = "leavel")
	public Integer getLeavel() {
		return this.leavel;
	}

	public void setLeavel(Integer leavel) {
		this.leavel = leavel;
	}

	@Column(name = "state")
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "receipt_type")
	public Integer getReceiptType() {
		return this.receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	@Column(name ="money",columnDefinition = "NUMBER")
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Transient
	public List<ReceiptAllocateApplyDetail> getReceiptApplyDetails() {
		return receiptApplyDetails;
	}

	public void setReceiptApplyDetails(
			List<ReceiptAllocateApplyDetail> receiptApplyDetails) {
		this.receiptApplyDetails = receiptApplyDetails;
	}
	
	
	@Column(name = "create_office_id")
	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	@Column(name = "create_area_id")
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "audit_depot_id")
	public Long getAuditDepotId() {
		return auditDepotId;
	}

	public void setAuditDepotId(Long auditDepotId) {
		this.auditDepotId = auditDepotId;
	}
	
	
	
}