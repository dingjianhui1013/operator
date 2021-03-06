package com.itrus.ca.modules.receipt.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.sys.entity.Area;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * ReceiptDepotInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "receipt_depot_info")
public class ReceiptDepotInfo extends DataEntity implements
		java.io.Serializable {

	// Fields

	private Long id;
	
	private ReceiptDepotInfo parent;
	
	private Office sysArea;
	private Office sysOffice;
	private String receiptName;
	private Double receiptTotal;
	private Double receiptOut;
	private Double receiptResidue;
	private Integer receiptType;
	private String receiptCommUser;
	private String receiptCommMobile;
	private String sex; // 联系人性别
	private String phone; // 联系人手机号
	private String address; // 地址
	private String email; // 邮箱
	private String codeZip; // 邮政编码
	private String department; // 部门名称
	private Double prewarning;//
	private String warningName;// 预警人姓名
	private String warningEmail;// 预警人邮箱

	private HashMap<Integer, Integer> beforeList;
	private HashMap<Integer, Integer> inList;
	private HashMap<Integer, Integer> outList;
	private HashMap<Integer, Integer> afterList;

	// Constructors

	/** default constructor */
	public ReceiptDepotInfo() {
	}

	/** full constructor */
	public ReceiptDepotInfo(Office sysArea, Office sysOffice,
			String receiptName, Double receiptTotal, Double receiptOut,
			Double receiptResidue, Integer receiptType, String receiptCommUser,
			String receiptCommMobile, String warningName) {
		this.sysArea = sysArea;
		this.sysOffice = sysOffice;
		this.receiptName = receiptName;
		this.receiptTotal = receiptTotal;
		this.receiptOut = receiptOut;
		this.receiptResidue = receiptResidue;
		this.receiptType = receiptType;
		this.receiptCommUser = receiptCommUser;
		this.receiptCommMobile = receiptCommMobile;
		this.warningName = warningName;
	}

	// Property accessors
	// @SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name = "RECEIPT_DEPOT_INFO_SEQUENCE", allocationSize = 1, initialValue = 1, sequenceName = "RECEIPT_DEPOT_INFO_SEQUENCE")
	@Id
	@GeneratedValue(generator = "RECEIPT_DEPOT_INFO_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	public ReceiptDepotInfo getParent() {
		return parent;
	}

	public void setParent(ReceiptDepotInfo parent) {
		this.parent = parent;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id")
	public Office getArea() {
		return this.sysArea;
	}

	public void setArea(Office sysArea) {
		this.sysArea = sysArea;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.sysOffice;
	}

	public void setOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}

	@Column(name = "receipt_name", length = 20, columnDefinition = "NVARCHAR2(127)")
	public String getReceiptName() {
		return this.receiptName;
	}

	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}

	@Column(name = "receipt_total", precision = 20, scale = 3, columnDefinition = "NUMBER")
	public Double getReceiptTotal() {
		return this.receiptTotal;
	}

	@Column(name = "warning_name", length = 20)
	public String getWarningName() {
		return warningName;
	}

	public void setWarningName(String warningName) {
		this.warningName = warningName;
	}

	@Column(name = "warning_email")
	public String getWarningEmail() {
		return warningEmail;
	}

	public void setWarningEmail(String warningEmail) {
		this.warningEmail = warningEmail;
	}

	public void setReceiptTotal(Double receiptTotal) {
		this.receiptTotal = receiptTotal;
	}

	@Column(name = "receipt_out", precision = 20, scale = 3, columnDefinition = "NUMBER")
	public Double getReceiptOut() {
		return this.receiptOut;
	}

	public void setReceiptOut(Double receiptOut) {
		this.receiptOut = receiptOut;
	}

	@Column(name = "receipt_residue", precision = 20, scale = 3, columnDefinition = "NUMBER")
	public Double getReceiptResidue() {
		return this.receiptResidue;
	}

	public void setReceiptResidue(Double receiptResidue) {
		this.receiptResidue = receiptResidue;
	}

	@Column(name = "receipt_type")
	public Integer getReceiptType() {
		return this.receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	@Column(name = "receipt_comm_user", columnDefinition = "NVARCHAR2(255)")
	public String getReceiptCommUser() {
		return this.receiptCommUser;
	}

	public void setReceiptCommUser(String receiptCommUser) {
		this.receiptCommUser = receiptCommUser;
	}

	@Column(name = "receipt_comm_mobile", columnDefinition = "NVARCHAR2(255)")
	public String getReceiptCommMobile() {
		return this.receiptCommMobile;
	}

	public void setReceiptCommMobile(String receiptCommMobile) {
		this.receiptCommMobile = receiptCommMobile;
	}

	@Column(name = "sex", columnDefinition = "NVARCHAR2(255)")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "phone", columnDefinition = "NVARCHAR2(255)")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "address", columnDefinition = "NVARCHAR2(255)")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "email", columnDefinition = "NVARCHAR2(255)")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "codeZip", columnDefinition = "NVARCHAR2(255)")
	public String getCodeZip() {
		return codeZip;
	}

	public void setCodeZip(String codeZip) {
		this.codeZip = codeZip;
	}

	@Column(name = "department", columnDefinition = "NVARCHAR2(255)")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "prewarning", columnDefinition = "NUMBER")
	public Double getPrewarning() {
		return prewarning;
	}

	public void setPrewarning(Double prewarning) {
		this.prewarning = prewarning;
	}

	@Transient
	public HashMap<Integer, Integer> getBeforeList() {
		return beforeList;
	}

	public void setBeforeList(HashMap<Integer, Integer> beforeList) {
		this.beforeList = beforeList;
	}

	@Transient
	public HashMap<Integer, Integer> getInList() {
		return inList;
	}

	public void setInList(HashMap<Integer, Integer> inList) {
		this.inList = inList;
	}

	@Transient
	public HashMap<Integer, Integer> getOutList() {
		return outList;
	}

	public void setOutList(HashMap<Integer, Integer> outList) {
		this.outList = outList;
	}

	@Transient
	public HashMap<Integer, Integer> getAfterList() {
		return afterList;
	}

	public void setAfterList(HashMap<Integer, Integer> afterList) {
		this.afterList = afterList;
	}

}