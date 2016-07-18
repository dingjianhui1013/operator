package com.itrus.ca.modules.work.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * WorkUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_user")
public class WorkUser implements java.io.Serializable {

	// Fields

	private Long id;
	private String userSn;
	private WorkCompany workCompany;
	private String contactName; 		// 证书持有人姓名
	private String contactSex;			// 证书持有人性别
	private String conCertType;			// 证书持有人证件
	private String conCertNumber;		// 证件号码
	private String contactEmail;		// 证书持有人电子邮件
	private String contactPhone;		// 证书持有人手机号
	private String contactTel;			// 业务系统UID
	private String department;
	private String address;
	private Integer status;
	private Integer source;
	private Integer workType;//0:联系人 1:经办人
	private Set<WorkDealInfo> workDealInfos = new HashSet<WorkDealInfo>(0);

	// Constructors

	/** default constructor */
	public WorkUser() {
	}

	/** full constructor */
	public WorkUser(WorkCompany workCompany, String contactName,
			String contactSex, String conCertType, String conCertNumber,
			String contactEmail, String contactPhone, String contactTel,
			String department, String address, Integer status, Integer source,
			Set<WorkDealInfo> workDealInfos) {
		this.workCompany = workCompany;
		this.contactName = contactName;
		this.contactSex = contactSex;
		this.conCertType = conCertType;
		this.conCertNumber = conCertNumber;
		this.contactEmail = contactEmail;
		this.contactPhone = contactPhone;
		this.contactTel = contactTel;
		this.department = department;
		this.address = address;
		this.status = status;
		this.source = source;
		this.workDealInfos = workDealInfos;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORK_USER_SEQUENCE")
//	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@SequenceGenerator(name="WORK_USER_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="WORK_USER_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public WorkCompany getWorkCompany() {
		return this.workCompany;
	}

	public void setWorkCompany(WorkCompany workCompany) {
		this.workCompany = workCompany;
	}

	@Column(name = "contact_name", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name = "contact_sex", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactSex() {
		return this.contactSex;
	}

	public void setContactSex(String contactSex) {
		this.contactSex = contactSex;
	}

	@Column(name = "con_cert_type", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getConCertType() {
		return this.conCertType;
	}

	public void setConCertType(String conCertType) {
		this.conCertType = conCertType;
	}

	@Column(name = "con_cert_number", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getConCertNumber() {
		return this.conCertNumber;
	}

	public void setConCertNumber(String conCertNumber) {
		this.conCertNumber = conCertNumber;
	}

	@Column(name = "contact_email", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Column(name = "contact_phone", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@Column(name = "contact_tel", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getContactTel() {
		return this.contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	@Column(name = "department", length = 100,columnDefinition = "NVARCHAR2(100)")
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "address", length = 200,columnDefinition = "NVARCHAR2(200)")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "source")
	public Integer getSource() {
		return this.source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workUser")
	public Set<WorkDealInfo> getWorkDealInfos() {
		return this.workDealInfos;
	}

	public void setWorkDealInfos(Set<WorkDealInfo> workDealInfos) {
		this.workDealInfos = workDealInfos;
	}
	
	
	@Column(name = "user_sn",columnDefinition = "NVARCHAR2(255)")
	public String getUserSn() {
		return userSn;
	}

	@Column(name = "work_type")
	public Integer getWorkType() {
		return workType;
	}

	public void setWorkType(Integer workType) {
		this.workType = workType;
	}

	public void setUserSn(String userSn) {
		this.userSn = userSn;
	}

}