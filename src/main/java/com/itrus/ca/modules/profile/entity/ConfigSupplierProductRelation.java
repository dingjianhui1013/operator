package com.itrus.ca.modules.profile.entity;

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

/**
 * ConfigSupplierProductRelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_sup_pro_relation")
public class ConfigSupplierProductRelation implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigSupplier configSupplier;
	private Integer productType;

	// Constructors

	/** default constructor */
	public ConfigSupplierProductRelation() {
	}

	/** full constructor */
	public ConfigSupplierProductRelation(ConfigSupplier configSupplier,
			ConfigProduct configProduct, Integer productType) {
		this.configSupplier = configSupplier;
		this.productType = productType;
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
	@JoinColumn(name = "supplier_id")
	public ConfigSupplier getConfigSupplier() {
		return this.configSupplier;
	}

	public void setConfigSupplier(ConfigSupplier configSupplier) {
		this.configSupplier = configSupplier;
	}

	@Column(name = "product_type")
	public Integer getProductType() {
		return this.productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

}