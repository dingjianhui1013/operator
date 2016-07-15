package com.itrus.ca.modules.profile.entity;

import java.sql.Timestamp;
import java.util.HashSet;
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

import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * ConfigProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_product")
public class ConfigProduct extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigApp configApp;
	private String productName;
	private Integer productLabel;
	private Long raAccountId;
	private Set<ConfigRaAccount> configRaAccounts = new HashSet<ConfigRaAccount>(
			0);
	private Set<ConfigChargeAgent> configChargeAgents = new HashSet<ConfigChargeAgent>(
			0);
	private Long raAccountExtedId;
	private String productNameType;
	private Boolean deviceApplyAutoPass;
	private Long chargeAgentId;//绑定的计费策略模板
	// Constructors

	/** default constructor */
	public ConfigProduct() {
	}

	/** full constructor */
	public ConfigProduct(ConfigApp configApp, String productName,
			Timestamp createDate, String delFlag, String remarks,
			Timestamp updateDate, Integer productLabel,
			Set<ConfigSupplierProductRelation> configSupplierProductRelations,
			/*Set<ConfigRaAccount> configRaAccounts,*/
			Set<ConfigChargeAgent> configChargeAgents) {
		this.configApp = configApp;
		this.productName = productName;
		this.createDate = createDate;
		this.delFlag = delFlag;
		this.remarks = remarks;
		this.updateDate = updateDate;
		this.productLabel = productLabel;
		/*this.configRaAccounts = configRaAccounts;*/
		this.configChargeAgents = configChargeAgents;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="CONFIG_PRODUCT_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="CONFIG_PRODUCT_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "CONFIG_PRODUCT_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@ManyToOne
	@JoinColumn(name = "app_id")
	public ConfigApp getConfigApp() {
		return this.configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}

	@Column(name = "product_name")
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "product_label")
	public Integer getProductLabel() {
		return this.productLabel;
	}

	public void setProductLabel(Integer productLabel) {
		this.productLabel = productLabel;
	}

	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configProduct")
	@Transient
	public Set<ConfigRaAccount> getConfigRaAccounts() {
		return this.configRaAccounts;
	}

	public void setConfigRaAccounts(Set<ConfigRaAccount> configRaAccounts) {
		this.configRaAccounts = configRaAccounts;
	}

	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configProduct")
	@Transient
	public Set<ConfigChargeAgent> getConfigChargeAgents() {
		return this.configChargeAgents;
	}

	public void setConfigChargeAgents(Set<ConfigChargeAgent> configChargeAgents) {
		this.configChargeAgents = configChargeAgents;
	}

	@Column(name = "raaccount_id")
	public Long getRaAccountId() {
		return raAccountId;
	}

	public void setRaAccountId(Long raAccountId) {
		this.raAccountId = raAccountId;
	}
	
	@Column(name = "raaccount_ext_id")
	public Long getRaAccountExtedId() {
		return raAccountExtedId;
	}

	public void setRaAccountExtedId(Long raAccountExtedId) {
		this.raAccountExtedId = raAccountExtedId;
	}
	@Transient
	public String getProductNameType() {
		return productNameType;
	}

	public void setProductNameType(String productNameType) {
		this.productNameType = productNameType;
	}

	@Column(name = "device_apply_auto_pass")
	public Boolean getDeviceApplyAutoPass() {
		return deviceApplyAutoPass==null? false:deviceApplyAutoPass;
	}

	public void setDeviceApplyAutoPass(Boolean deviceApplyAutoPass) {
		this.deviceApplyAutoPass = deviceApplyAutoPass;
	}

	@Column(name="chargeAgent_id")
	public Long getChargeAgentId() {
		return chargeAgentId;
	}

	public void setChargeAgentId(Long chargeAgentId) {
		this.chargeAgentId = chargeAgentId;
	}
}