package com.itrus.ca.modules.settle.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;

/**
 * SettleKey entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "settle_vtn"/*, catalog = "caoperator"*/)
public class SettleVTN implements java.io.Serializable {

	// Fields

	private Long id;
	private String productName;
	private String appName;
	private Integer count;
	private Double price;
	private Double countPrice;
	private Date createTime;
	private ConfigSupplier configSupplier;
	
	
	// Constructors
	/** default constructor */
	public SettleVTN() {
	}

	/** minimal constructor */
	public SettleVTN(Long id) {
		this.id = id;
	}

	/** full constructor */
	public SettleVTN(
			String productName,
			String appName,
			Integer count,
			Double price,
			Double countPrice,
			Date createTime,
			ConfigSupplier configSupplier
			) {
		this.productName = productName;
		this.appName = appName;
		this.count = count;
		this.price = price;
		this.countPrice = countPrice;
		this.createTime = createTime;
		this.configSupplier = configSupplier;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="SETTLE_VTN_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="SETTLE_VTN_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "SETTLE_VTN_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "product_name")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "app_name")
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Column(name = "count")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "price")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "count_price")
	public Double getCountPrice() {
		return countPrice;
	}

	public void setCountPrice(Double countPrice) {
		this.countPrice = countPrice;
	}

	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "config_supplier_id")
	public ConfigSupplier getConfigSupplier() {
		return configSupplier;
	}

	public void setConfigSupplier(ConfigSupplier configSupplier) {
		this.configSupplier = configSupplier;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}