package com.itrus.ca.modules.profile.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.signature.entity.SignatureAgentBoundConfigApp;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * ConfigApp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_app")
public class ConfigApp extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String appName;//应用名称
	private String alias;//别名
	
	private Integer appType;
	private String appDescription;//应用描述
	private String appImg;
	private String productName;
	private List<WorkDealInfo> workDealInfos;
	private Long govDeviceAmount;//政府统一采购设备证书最大数量  该字段已停用
	
	private ConfigProjectType configProjectType;//应用类型
	
	private List<SignatureAgentBoundConfigApp> bounds;
	
	private Boolean applyFlag1;//判断地税
	private Boolean applyFlag2;//判断社保
	
	/**
	 * 应用配是否支持通用，决定产品列表是否支持选通用or专用
	 */
	private Boolean supportCommon;//是否支持通用
	

	// Constructors

	/** default constructor */
	public ConfigApp() {
	}

	/** full constructor */
	public ConfigApp(String appName, Integer appType,
			String appDescription, String appImg , ConfigProjectType configProjectType,Boolean applyFlag1,Boolean applyFlag2) {
		this.appName = appName;
		this.appType = appType;
		this.appDescription = appDescription;
		this.appImg = appImg;
		this.configProjectType = configProjectType;
		this.applyFlag1=applyFlag1;
		this.applyFlag2=applyFlag2;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="CONFIG_APP_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="CONFIG_APP_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "CONFIG_APP_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "app_name",columnDefinition = "NVARCHAR2(255)")
	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Column(name = "app_type")
	public Integer getAppType() {
		return this.appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	@Column(name = "app_description",columnDefinition = "NVARCHAR2(255)")
	public String getAppDescription() {
		return this.appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	@Column(name = "app_img",columnDefinition = "NVARCHAR2(255)")
	public String getAppImg() {
		return this.appImg;
	}

	public void setAppImg(String appImg) {
		this.appImg = appImg;
	}

	@Transient
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "support_common")
	public Boolean getSupportCommon() {
		return supportCommon;
	}

	public void setSupportCommon(Boolean supportCommon) {
		this.supportCommon = supportCommon;
	}

	@Transient
	public List<WorkDealInfo> getWorkDealInfos() {
		return workDealInfos;
	}

	public void setWorkDealInfos(List<WorkDealInfo> workDealInfos) {
		this.workDealInfos = workDealInfos;
	}

	@Column(name = "gov_device_amount")
	public Long getGovDeviceAmount() {
		return govDeviceAmount;
	}

	public void setGovDeviceAmount(Long govDeviceAmount) {
		this.govDeviceAmount = govDeviceAmount;
	}

	@Column(name="alias",columnDefinition = "NVARCHAR2(255)")
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@ManyToOne
	@JoinColumn(name="config_product_type")
	public ConfigProjectType getConfigProjectType() {
		return configProjectType;
	}

	public void setConfigProjectType(ConfigProjectType configProjectType) {
		this.configProjectType = configProjectType;
	}

	@Transient
	public List<SignatureAgentBoundConfigApp> getBounds() {
		return bounds;
	}

	public void setBounds(List<SignatureAgentBoundConfigApp> bounds) {
		this.bounds = bounds;
	}
	
	@Column(name = "apply_flag1")
	public Boolean getApplyFlag1() {
		return applyFlag1;
	}

	public void setApplyFlag1(Boolean applyFlag1) {
		this.applyFlag1 = applyFlag1;
	}
	@Column(name = "apply_flag2")
	public Boolean getApplyFlag2() {
		return applyFlag2;
	}

	public void setApplyFlag2(Boolean applyFlag2) {
		this.applyFlag2 = applyFlag2;
	}
	
	
	
	
	
	
	
}