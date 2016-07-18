package com.itrus.ca.modules.settle.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.itrus.ca.modules.profile.entity.ConfigSupplier;

/**
 * SettleSupplierCertDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "settle_supplier_cert_detail")
public class SettleSupplierCertDetail implements java.io.Serializable {

	// Fields

	private Long id;
	private ConfigSupplier configSupplier;
	private Date settleDate;
	private Integer productType;
	private Integer addAmount;
	private Integer totalAmount;
	private Integer replaceAmount;
	private Integer revokeAmount;
	private Integer testAmount;
	private Integer rwBbAmount;
	private Integer frwBbAmount;
	private Integer changeAmount;
	private Integer amountYear1;
	private Integer amountYear2;
	private Integer amountYear3;
	private Integer amountYear4;
	private Integer amountYear5;
	private String ou;

	// Constructors

	/** default constructor */
	public SettleSupplierCertDetail() {
	}

	/** full constructor */
	public SettleSupplierCertDetail(ConfigSupplier configSupplier,
			Date settleDate, Integer productType, Integer totalAmount,
			Integer replaceAmount, Integer revokeAmount, Integer testAmount,
			Integer rwBbAmount, Integer frwBbAmount, Integer changeAmount,
			Integer amountYear1, Integer amountYear2, Integer amountYear3,
			Integer amountYear4, Integer amountYear5, String ou) {
		this.configSupplier = configSupplier;
		this.settleDate = settleDate;
		this.productType = productType;
		this.totalAmount = totalAmount;
		this.replaceAmount = replaceAmount;
		this.revokeAmount = revokeAmount;
		this.testAmount = testAmount;
		this.rwBbAmount = rwBbAmount;
		this.frwBbAmount = frwBbAmount;
		this.changeAmount = changeAmount;
		this.amountYear1 = amountYear1;
		this.amountYear2 = amountYear2;
		this.amountYear3 = amountYear3;
		this.amountYear4 = amountYear4;
		this.amountYear5 = amountYear5;
		this.ou = ou;
	}

	// Property accessors
	// @SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name = "SETTLE_SUPPLIER_CERT_SEQUENCE", allocationSize = 1, initialValue = 1, sequenceName = "SETTLE_SUPPLIER_CERT_SEQUENCE")
	@Id
	@GeneratedValue(generator = "SETTLE_SUPPLIER_CERT_SEQUENCE", strategy = GenerationType.SEQUENCE)
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

	@Temporal(TemporalType.DATE)
	@Column(name = "settle_date", length = 10)
	public Date getSettleDate() {
		return this.settleDate;
	}

	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}

	@Column(name = "product_type")
	public Integer getProductType() {
		return this.productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	@Column(name = "total_amount")
	public Integer getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "replace_amount")
	public Integer getReplaceAmount() {
		return this.replaceAmount;
	}

	public void setReplaceAmount(Integer replaceAmount) {
		this.replaceAmount = replaceAmount;
	}

	@Column(name = "revoke_amount")
	public Integer getRevokeAmount() {
		return this.revokeAmount;
	}

	public void setRevokeAmount(Integer revokeAmount) {
		this.revokeAmount = revokeAmount;
	}

	@Column(name = "test_amount")
	public Integer getTestAmount() {
		return this.testAmount;
	}

	public void setTestAmount(Integer testAmount) {
		this.testAmount = testAmount;
	}

	@Column(name = "rw_bb_amount")
	public Integer getRwBbAmount() {
		return this.rwBbAmount;
	}

	public void setRwBbAmount(Integer rwBbAmount) {
		this.rwBbAmount = rwBbAmount;
	}

	@Column(name = "frw_bb_amount")
	public Integer getFrwBbAmount() {
		return this.frwBbAmount;
	}

	public void setFrwBbAmount(Integer frwBbAmount) {
		this.frwBbAmount = frwBbAmount;
	}

	@Column(name = "change_amount")
	public Integer getChangeAmount() {
		return this.changeAmount;
	}

	public void setChangeAmount(Integer changeAmount) {
		this.changeAmount = changeAmount;
	}

	@Column(name = "amount_year1")
	public Integer getAmountYear1() {
		return this.amountYear1;
	}

	public void setAmountYear1(Integer amountYear1) {
		this.amountYear1 = amountYear1;
	}

	@Column(name = "amount_year2")
	public Integer getAmountYear2() {
		return this.amountYear2;
	}

	public void setAmountYear2(Integer amountYear2) {
		this.amountYear2 = amountYear2;
	}

	@Column(name = "amount_year3")
	public Integer getAmountYear3() {
		return amountYear3;
	}

	public void setAmountYear3(Integer amountYear3) {
		this.amountYear3 = amountYear3;
	}

	@Column(name = "amount_year4")
	public Integer getAmountYear4() {
		return this.amountYear4;
	}

	public void setAmountYear4(Integer amountYear4) {
		this.amountYear4 = amountYear4;
	}

	@Column(name = "amount_year5")
	public Integer getAmountYear5() {
		return this.amountYear5;
	}

	public void setAmountYear5(Integer amountYear5) {
		this.amountYear5 = amountYear5;
	}

	@Column(name = "ou", columnDefinition = "NVARCHAR2(255)")
	public String getOu() {
		return this.ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	@Column(name = "add_amount")
	public Integer getAddAmount() {
		return addAmount;
	}

	public void setAddAmount(Integer addAmount) {
		this.addAmount = addAmount;
	}

}