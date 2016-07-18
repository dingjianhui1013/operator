package com.itrus.ca.modules.key.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder.In;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 申请调拨
 * 
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "key_allocate_apply")
public class KeyAllocateApply extends DataEntity implements
		java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice;
	private KeyUsbKeyDepot keyUsbKeyDepot;
	
	private Long auditKeyDepotId;//新增字段      审核库房的id
	
	
	private Integer applyType;

	private Date warehouseDate;
	private Integer leavel;
	private Integer isBadKeyIn;
	private Integer approvalOpinion;
	private String refusalReason;

	private Integer state;
	private String stateName;
	private String leavelName;

	private Long officeId;
	private Long areaId;

	private List<KeyAllocateApplyDetailed> KeyApplyDetails;

	// private Set<KeyDisplaceDetail> keyDisplaceDetails = new
	// HashSet<KeyDisplaceDetail>(
	// 0);

	// Constructors

	/** default constructor */
	public KeyAllocateApply() {
	}

	/** full constructor */
	public KeyAllocateApply(Office sysOffice, KeyUsbKeyDepot keyUsbKeyDepot,
			Integer applyType, Integer applyNewNum,
			KeyGeneralInfo keyGeneralInfo, Timestamp warehouseDate,
			Integer leavel, Long officeId, Long areaId) {
		this.sysOffice = sysOffice;
		this.keyUsbKeyDepot = keyUsbKeyDepot;
		this.applyType = applyType;

		this.warehouseDate = warehouseDate;
		this.leavel = leavel;
		this.officeId = officeId;
		this.areaId = areaId;



		// this.keyDisplaceDetails = keyDisplaceDetails;

	}

	// Property accessors
	// @SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name = "KEY_ALLOCATE_APPLY_SEQUENCE", allocationSize = 1, initialValue = 1, sequenceName = "KEY_ALLOCATE_APPLY_SEQUENCE")
	@Id
	@GeneratedValue(generator = "KEY_ALLOCATE_APPLY_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "apply_key_type") public KeyGeneralInfo
	 * getKeyGeneralInfo() { return this.keyGeneralInfo; }
	 * 
	 * public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
	 * this.keyGeneralInfo = keyGeneralInfo; }
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depot_id")
	public KeyUsbKeyDepot getKeyUsbKeyDepot() {
		return this.keyUsbKeyDepot;
	}

	public void setKeyUsbKeyDepot(KeyUsbKeyDepot keyUsbKeyDepot) {
		this.keyUsbKeyDepot = keyUsbKeyDepot;
	}

	@Column(name = "apply_type")
	public Integer getApplyType() {
		return this.applyType;
	}

	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}

	/*
	 * @Column(name = "apply_new_num") public Integer getApplyNewNum() { return
	 * this.applyNewNum; }
	 * 
	 * public void setApplyNewNum(Integer applyNewNum) { this.applyNewNum =
	 * applyNewNum; }
	 */

	@Column(name = "leavel")
	public Integer getLeavel() {
		return this.leavel;
	}

	public void setLeavel(Integer leavel) {
		this.leavel = leavel;
	}

	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "keyAllocateApply")
	// public Set<KeyDisplaceDetail> getKeyDisplaceDetails() {
	// return this.keyDisplaceDetails;
	// }
	//
	// public void setKeyDisplaceDetails(Set<KeyDisplaceDetail>
	// keyDisplaceDetails) {
	// this.keyDisplaceDetails = keyDisplaceDetails;
	// }
	@Column(name = "warehouse_date")
	public Date getWarehouseDate() {
		return warehouseDate;
	}

	public void setWarehouseDate(Date warehouseDate) {
		this.warehouseDate = warehouseDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getSysOffice() {
		return sysOffice;
	}

	public void setSysOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}

	@Transient
	public String getLeavelName() {
		return leavelName;
	}

	public void setLeavelName(String leavelName) {
		this.leavelName = leavelName;
	}

	@Column(name = "is_bad_key_in")
	public Integer getIsBadKeyIn() {
		return isBadKeyIn;
	}

	public void setIsBadKeyIn(Integer isBadKeyIn) {
		this.isBadKeyIn = isBadKeyIn;
	}

	@Column(name = "approval_opinion")
	public Integer getApprovalOpinion() {
		return approvalOpinion;
	}

	public void setApprovalOpinion(Integer approvalOpinion) {
		this.approvalOpinion = approvalOpinion;
	}

	@Column(name = "refusal_reason", columnDefinition = "NVARCHAR2(255)")
	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	@Transient
	public List<KeyAllocateApplyDetailed> getKeyApplyDetails() {
		return KeyApplyDetails;
	}

	public void setKeyApplyDetails(
			List<KeyAllocateApplyDetailed> keyApplyDetails) {
		KeyApplyDetails = keyApplyDetails;
	}

	@Column(name = "state")
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Transient
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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
	public Long getAuditKeyDepotId() {
		return auditKeyDepotId;
	}

	public void setAuditKeyDepotId(Long auditKeyDepotId) {
		this.auditKeyDepotId = auditKeyDepotId;
	}
	
}