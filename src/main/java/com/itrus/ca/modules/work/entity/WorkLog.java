package com.itrus.ca.modules.work.entity;

import java.util.Date;

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
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * WorkLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_info_log")
public class WorkLog extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice;
	private WorkCompany workCompany;
	private WorkDealInfo workDealInfo;
	private String recordContent;
	private String serType;
	private String serTitle;
	private Integer completeType;
	private String leftoverProblem;
	private Date creatTime;
	private String probleType;//问题类型
	private String access;//客服接入
	private Integer status; //服务类型状态 1为日常客服 0为普通客服
	private Integer state; //状态 1为客服记录Null为工作记录
	private ConfigApp configApp;

	// Constructors

	/** default constructor */
	public WorkLog() {
	}

	/** full constructor */
	public WorkLog(Office sysOffice, WorkCompany workCompany,
			WorkDealInfo workDealInfo, String recordContent, String serType,
			String serTitle, Integer completeType, String leftoverProblem) {
		this.sysOffice = sysOffice;
		this.workCompany = workCompany;
		this.workDealInfo = workDealInfo;
		this.recordContent = recordContent;
		this.serType = serType;
		this.serTitle = serTitle;
		this.completeType = completeType;
		this.leftoverProblem = leftoverProblem;
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
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.sysOffice;
	}

	public void setOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "configApp")
	public ConfigApp getConfigApp() {
		return configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_company_id")
	public WorkCompany getWorkCompany() {
		return this.workCompany;
	}

	public void setWorkCompany(WorkCompany workCompany) {
		this.workCompany = workCompany;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deal_info_id")
	public WorkDealInfo getWorkDealInfo() {
		return this.workDealInfo;
	}

	public void setWorkDealInfo(WorkDealInfo workDealInfo) {
		this.workDealInfo = workDealInfo;
	}

	@Column(name = "record_content")
	public String getRecordContent() {
		return this.recordContent;
	}

	public void setRecordContent(String recordContent) {
		this.recordContent = recordContent;
	}

	@Column(name = "ser_type", length = 100)
	public String getSerType() {
		return this.serType;
	}

	public void setSerType(String serType) {
		this.serType = serType;
	}

	@Column(name = "ser_title", length = 100)
	public String getSerTitle() {
		return this.serTitle;
	}

	public void setSerTitle(String serTitle) {
		this.serTitle = serTitle;
	}

	@Column(name = "complete_type")
	public Integer getCompleteType() {
		return this.completeType;
	}

	public void setCompleteType(Integer completeType) {
		this.completeType = completeType;
	}

	@Column(name = "leftover_problem")
	public String getLeftoverProblem() {
		return this.leftoverProblem;
	}

	public void setLeftoverProblem(String leftoverProblem) {
		this.leftoverProblem = leftoverProblem;
	}

	@Column(name = "creat_time")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	@Column(name = "proble_type")
	public String getProbleType() {
		return probleType;
	}

	public void setProbleType(String probleType) {
		this.probleType = probleType;
	}

	@Column(name = "customer_service_access")
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
}