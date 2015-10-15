package com.itrus.ca.modules.profile.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * 供应商
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "config_supplier")
public class ConfigSupplier extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String supplierName;
	private Integer supplierType; //0：证书 1：KEY 2：签章
	private Long keyType;
	private String supplierCommUsername;
	private String supplierCommMobile;
	private String supplierTechnicalName;
	private String supplierTechnicalMobile;
	private String supplierAddress;
	private String supplierRemarks;
	
	

	private Set<ConfigSupplierProductRelation> configSupplierProductRelations = new HashSet<ConfigSupplierProductRelation>(
			0);

	// Constructors

	/** default constructor */
	public ConfigSupplier() {
	}

	/** full constructor */
	public ConfigSupplier(String supplierName, Integer supplierType,
			Long keyType, String supplierCommUsername,
			String supplierCommMobile, String supplierTechnicalName,
			String supplierTechnicalMobile,
			Set<ConfigSupplierProductRelation> configSupplierProductRelations,
			String supplierAddress,
			String supplierRemarks
			
			) {
		this.supplierAddress = supplierAddress;
		this.supplierRemarks = supplierRemarks;
		this.supplierName = supplierName;
		this.supplierType = supplierType;
		this.keyType = keyType;
		this.supplierCommUsername = supplierCommUsername;
		this.supplierCommMobile = supplierCommMobile;
		this.supplierTechnicalName = supplierTechnicalName;
		this.supplierTechnicalMobile = supplierTechnicalMobile;
		this.configSupplierProductRelations = configSupplierProductRelations;
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

	@Column(name = "supplier_name")
	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Column(name = "supplier_type")
	public Integer getSupplierType() {
		return this.supplierType;
	}

	public void setSupplierType(Integer supplierType) {
		this.supplierType = supplierType;
	}

	@Column(name = "key_type")
	public Long getKeyType() {
		return this.keyType;
	}

	public void setKeyType(Long keyType) {
		this.keyType = keyType;
	}

	@Column(name = "supplier_comm_username")
	public String getSupplierCommUsername() {
		return this.supplierCommUsername;
	}

	public void setSupplierCommUsername(String supplierCommUsername) {
		this.supplierCommUsername = supplierCommUsername;
	}

	@Column(name = "supplier_comm_mobile")
	public String getSupplierCommMobile() {
		return this.supplierCommMobile;
	}

	public void setSupplierCommMobile(String supplierCommMobile) {
		this.supplierCommMobile = supplierCommMobile;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configSupplier")
	public Set<ConfigSupplierProductRelation> getConfigSupplierProductRelations() {
		return this.configSupplierProductRelations;
	}

	public void setConfigSupplierProductRelations(
			Set<ConfigSupplierProductRelation> configSupplierProductRelations) {
		this.configSupplierProductRelations = configSupplierProductRelations;
	}
	
	
	@Column(name = "supplier_technical_name")
	public String getSupplierTechnicalName() {
		return supplierTechnicalName;
	}

	public void setSupplierTechnicalName(String supplierTechnicalName) {
		this.supplierTechnicalName = supplierTechnicalName;
	}

	@Column(name = "supplier_technical_mobile")
	public String getSupplierTechnicalMobile() {
		return supplierTechnicalMobile;
	}

	public void setSupplierTechnicalMobile(String supplierTechnicalMobile) {
		this.supplierTechnicalMobile = supplierTechnicalMobile;
	}

	@Column(name = "supplier_address")
	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}
	
	@Column(name = "supplier_remarks")
	public String getSupplierRemarks() {
		return supplierRemarks;
	}

	public void setSupplierRemarks(String supplierRemarks) {
		this.supplierRemarks = supplierRemarks;
	}

	
	
	
	
	
	
	
	
}