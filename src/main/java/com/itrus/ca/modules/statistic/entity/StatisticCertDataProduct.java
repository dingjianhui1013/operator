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
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 区分产品和网点的数量汇总
 * 证书发放统计汇总表
 */
@Entity
@Table(name = "statistic_cert_data_product")
public class StatisticCertDataProduct implements java.io.Serializable {

	// Fields

	private Long id;
	private Office office;
	private Integer year1;
	private Integer year2;
	
	private Integer year3;             //新增三年
	
	private Integer year4;
	private Integer year5;
	private Date countDate;
	private Integer productType;

	// Constructors

	/** default constructor */
	public StatisticCertDataProduct() {
	}

	/** full constructor */
	public StatisticCertDataProduct(Long officeId, Long appId, Integer year,
			Integer add, Integer renew, Integer total, Date deadLineDate,
			Integer productType, Integer payType) {
		this.productType = productType;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="STATISTIC_CERT_DATA__SEQUENCE",allocationSize=1,initialValue=1,sequenceName="STATISTIC_CERT_DATA__SEQUENCE")
	@Id 
	@GeneratedValue(generator = "STATISTIC_CERT_DATA__SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "product_type")
	public Integer getProductType() {
		return this.productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Column(name = "year1")
	public Integer getYear1() {
		return year1;
	}

	public void setYear1(Integer year1) {
		this.year1 = year1;
	}
	
	@Column(name = "year2")
	public Integer getYear2() {
		return year2;
	}

	public void setYear2(Integer year2) {
		this.year2 = year2;
	}
	
	
	@Column(name = "year3")
	public Integer getYear3() {
		return year3;
	}

	public void setYear3(Integer year3) {
		this.year3 = year3;
	}
	

	@Column(name = "year4")
	public Integer getYear4() {
		return year4;
	}

	public void setYear4(Integer year4) {
		this.year4 = year4;
	}
	
	@Column(name = "year5")
	public Integer getYear5() {
		return year5;
	}

	public void setYear5(Integer year5) {
		this.year5 = year5;
	}
	
	@Column(name = "count_date")
	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

}