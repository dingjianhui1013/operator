package com.itrus.ca.modules.statistic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * StatisticCertData entity. @author MyEclipse Persistence Tools
 * @see{ProductType} 只统计product.productName 1,2的证书
 * 企业证书/个人证书统计
 */
@Entity
@Table(name = "statistic_cert_data")
public class StatisticCertData implements java.io.Serializable {

	// Fields
	private Long id;
	private Integer add1;
	private Integer add2;
	
	private Integer add3;                 //新增三年
	
	private Integer add4;
	private Integer add5;
	private Integer renew1;
	private Integer renew2;
	
	private Integer renew3;              //新增三年更新
	
	private Integer renew4;
	private Integer renew5;
	private Date countDate;
	private Integer payType;//0自费1合同2政府统一采购
	private ConfigApp app;
	private Office office;
	private ConfigProduct product;
	private Integer productType;
	

	// Constructors


	@Column(name = "product_type")
	public Integer getProductType() {
		return productType;
	}


	public void setProductType(Integer productType) {
		this.productType = productType;
	}


	/** default constructor */
	public StatisticCertData() {
	}


	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="STATISTIC_CERT_DATA_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="STATISTIC_CERT_DATA_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "STATISTIC_CERT_DATA_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "add1")
	public Integer getAdd1() {
		return add1;
	}


	public void setAdd1(Integer add1) {
		this.add1 = add1;
	}

	@Column(name = "add2")
	public Integer getAdd2() {
		return add2;
	}


	public void setAdd2(Integer add2) {
		this.add2 = add2;
	}
	
	
	@Column(name = "add3")
	public Integer getAdd3() {
		return add3;
	}


	public void setAdd3(Integer add3) {
		this.add3 = add3;
	}
	
	

	@Column(name = "add4")
	public Integer getAdd4() {
		return add4;
	}


	public void setAdd4(Integer add4) {
		this.add4 = add4;
	}


	@Column(name = "renew1")
	public Integer getRenew1() {
		return renew1;
	}


	public void setRenew1(Integer renew1) {
		this.renew1 = renew1;
	}

	@Column(name = "renew2")
	public Integer getRenew2() {
		return renew2;
	}


	public void setRenew2(Integer renew2) {
		this.renew2 = renew2;
	}
	
	
	@Column(name = "renew3")
	public Integer getRenew3() {
		return renew3;
	}


	public void setRenew3(Integer renew3) {
		this.renew3 = renew3;
	}
	
	

	@Column(name = "renew4")
	public Integer getRenew4() {
		return renew4;
	}


	public void setRenew4(Integer renew4) {
		this.renew4 = renew4;
	}

	public Integer getAdd5() {
		return add5;
	}


	public void setAdd5(Integer add5) {
		this.add5 = add5;
	}

	@Column(name = "renew5")
	public Integer getRenew5() {
		return renew5;
	}


	public void setRenew5(Integer renew5) {
		this.renew5 = renew5;
	}

	
	
	
	@Column(name = "count_date")
	public Date getCountDate() {
		return countDate;
	}


	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}


	@Column(name = "pay_type")
	public Integer getPayType() {
		return payType;
	}


	public void setPayType(Integer payType) {
		this.payType = payType;
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="app_id")
	public ConfigApp getApp() {
		return app;
	}


	public void setApp(ConfigApp app) {
		this.app = app;
	}


	@ManyToOne
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return office;
	}


	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	public ConfigProduct getProduct() {
		return product;
	}


	public void setProduct(ConfigProduct product) {
		this.product = product;
	}


}