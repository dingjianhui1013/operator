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
 * ConfigChargeSupplierDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_charge_supplier_detail")
public class ConfigChargeSupplierDetail implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigSupplierProductRelation configSupplierProductRelation;
	private Integer moneyType;
	private Integer year;
	private Double money;

	// Constructors

	/** default constructor */
	public ConfigChargeSupplierDetail() {
	}

	/** full constructor */
	public ConfigChargeSupplierDetail(
			ConfigSupplierProductRelation configSupplierProductRelation,
			Integer moneyType, Integer year, Double money) {
		this.configSupplierProductRelation = configSupplierProductRelation;
		this.moneyType = moneyType;
		this.year = year;
		this.money = money;
	}

	// Property accessors
	// @SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name = "CONFIG_CHARGE_SUPPLI_SEQUENCE", allocationSize = 1, initialValue = 1, sequenceName = "CONFIG_CHARGE_SUPPLI_SEQUENCE")
	@Id
	@GeneratedValue(generator = "CONFIG_CHARGE_SUPPLI_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "charge_supplier_id")
	public ConfigSupplierProductRelation getConfigSupplierProductRelation() {
		return this.configSupplierProductRelation;
	}

	public void setConfigSupplierProductRelation(
			ConfigSupplierProductRelation configSupplierProductRelation) {
		this.configSupplierProductRelation = configSupplierProductRelation;
	}

	@Column(name = "money_type")
	public Integer getMoneyType() {
		return this.moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	@Column(name = "year")
	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Column(name = "money", precision = 20, scale = 3, columnDefinition = "NUMBER")
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

}