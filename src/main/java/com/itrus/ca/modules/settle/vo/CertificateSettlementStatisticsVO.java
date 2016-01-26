/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.vo;

import java.math.BigDecimal;

/**
 * 证书结算统计表Entity
 * 
 * @author qt
 * @version 2015-11-22
 */

public class CertificateSettlementStatisticsVO implements java.io.Serializable {

	private String month;
	private String productName;
	private Integer year;
	private Long workCount;
	private Integer dealInfoType;

	public CertificateSettlementStatisticsVO() {

	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getWorkCount() {
		return workCount;
	}

	public void setWorkCount(Long workCount) {
		this.workCount = workCount;
	}
	
	
	public CertificateSettlementStatisticsVO(String month, String productName, Integer year, Long workCount,
			Integer productType) {
		super();
		this.month = month;
		this.productName = productName;
		this.year = year;
		this.workCount = workCount;
		this.dealInfoType = productType;
	}

	public Integer getDealInfoType() {
		return dealInfoType;
	}

	public void setDealInfoType(Integer dealInfoType) {
		this.dealInfoType = dealInfoType;
	}

	// 大写为了oracle
	public String getMONTH() {
		return this.month;
	}

	public void setMONTH(String mONTH) {
		this.month = mONTH;
	}
	
	public Integer getDEALINFOTYPE() {
		return dealInfoType;
	}

	public void setDEALINFOTYPE(BigDecimal dealInfoType) {
		this.dealInfoType = dealInfoType.intValue();
	}
	
	public String getPRODUCTNAME() {
		return productName;
	}

	public void setPRODUCTNAME(String productName) {
		this.productName = productName;
	}

	public Integer getYEAR() {
		return year;
	}

	public void setYEAR(BigDecimal year) {
		this.year = year.intValue();
	}

	public Long getWORKCOUNT() {
		return workCount;
	}

	public void setWORKCOUNT(BigDecimal workCount) {
		this.workCount = workCount.longValue();
	}
	

}
