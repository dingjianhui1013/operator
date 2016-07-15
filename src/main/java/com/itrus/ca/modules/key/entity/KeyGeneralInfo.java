package com.itrus.ca.modules.key.entity;

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
import com.itrus.ca.modules.profile.entity.ConfigSupplier;

/**
 * KeyGeneralInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "key_general_info")
public class KeyGeneralInfo extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigSupplier configSupplier;
	//private KeyManufacturer keyManufacturer;
	private String defaultSoPin;
	private String defaultUserPin;
	private String description;
	private String model;
	private String name;
	private String defaultSoPinType;
	private String linkman;
	private String linkmanPhone;
	private String manuKeyName;
	private Integer residue;
	
	// Constructors

	/** default constructor */
	public KeyGeneralInfo() {
	}

	/** minimal constructor */
	public KeyGeneralInfo(ConfigSupplier configSupplier) {
		this.configSupplier = configSupplier;
	}

	/** full constructor */
	public KeyGeneralInfo(String defaultSoPin,
			String defaultUserPin, String description, String model,
			String name, String defaultSoPinType,ConfigSupplier configSupplier) {
		this.configSupplier = configSupplier;
		
		this.defaultSoPin = defaultSoPin;
		this.defaultUserPin = defaultUserPin;
		this.description = description;
		this.model = model;
		this.name = name;
		this.defaultSoPinType = defaultSoPinType;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="KEY_GENERAL_INFO_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="KEY_GENERAL_INFO_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "KEY_GENERAL_INFO_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", nullable = false)
	public ConfigSupplier getConfigSupplier() {
		return configSupplier;
	}

	public void setConfigSupplier(ConfigSupplier configSupplier) {
		this.configSupplier = configSupplier;
	}

	@Column(name = "default_so_pin")
	public String getDefaultSoPin() {
		return this.defaultSoPin;
	}

	public void setDefaultSoPin(String defaultSoPin) {
		this.defaultSoPin = defaultSoPin;
	}

	@Column(name = "default_user_pin")
	public String getDefaultUserPin() {
		return this.defaultUserPin;
	}

	public void setDefaultUserPin(String defaultUserPin) {
		this.defaultUserPin = defaultUserPin;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "model")
	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "default_so_pin_type")
	public String getDefaultSoPinType() {
		return this.defaultSoPinType;
	}

	public void setDefaultSoPinType(String defaultSoPinType) {
		this.defaultSoPinType = defaultSoPinType;
	}
	@Column(name = "linkman")
	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	@Column(name = "linkman_phone")
	public String getLinkmanPhone() {
		return linkmanPhone;
	}

	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}

	@Transient
	public String getManuKeyName() {
		return manuKeyName;
	}

	public void setManuKeyName(String manuKeyName) {
		this.manuKeyName = manuKeyName;
	}

	@Transient
	public Integer getResidue() {
		return residue;
	}

	public void setResidue(Integer residue) {
		this.residue = residue;
	}
	

}