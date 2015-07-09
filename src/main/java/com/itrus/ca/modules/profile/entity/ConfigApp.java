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
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * ConfigApp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_app")
public class ConfigApp extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String appName;
	private String alias;
	
	private Integer appType;
	private String appDescription;
	private String appImg;
	private String productName;
	private List<WorkDealInfo> workDealInfos;
	private Long govDeviceAmount;
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
			String appDescription, String appImg) {
		this.appName = appName;
		this.appType = appType;
		this.appDescription = appDescription;
		this.appImg = appImg;
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

	@Column(name = "app_name")
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

	@Column(name = "app_description")
	public String getAppDescription() {
		return this.appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	@Column(name = "app_img")
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

	@Column(name="alias")
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
	
}