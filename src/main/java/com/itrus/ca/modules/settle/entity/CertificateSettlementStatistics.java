/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 证书结算统计表Entity
 * @author qt
 * @version 2015-11-22
 */
@Entity
@Table(name = "settle_certificateSettlementStatistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CertificateSettlementStatistics implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private Integer add1;
	private Integer add2;
	private Integer add4;
	private Integer add5;
	private Integer renew1;
	private Integer renew2;
	private Integer renew4;
	private Integer renew5;
	private Date countDate;
	private Integer payType;//0自费1合同2政府统一采购
	private ConfigApp app;
	private Office office;
	private ConfigProduct product;
	private Integer productType;
	private Long pconfigChargeAgentId;
	private Integer dealInfoType;
	private Integer dealInfoType1;
	private Integer dealInfoType2;
	private Integer dealInfoType3;
	private Integer year1;
	private Integer year2;
	private Integer year4;
	private Integer year5;
	private ConfigChargeAgent configChargeAgent;
	
	public CertificateSettlementStatistics() {
		
	}
	

	public CertificateSettlementStatistics(Long id) {
		
		this.id = id;
	}


	

	public CertificateSettlementStatistics(Integer add1, Integer add2, Integer add4, Integer add5, Integer renew1,
			Integer renew2, Integer renew4, Integer renew5, Date countDate, Integer payType, ConfigApp app,
			Office office,ConfigChargeAgent configChargeAgent,
			ConfigProduct product, Integer productType, Long pconfigChargeAgentId, Integer dealInfoType,
			Integer dealInfoType1, Integer dealInfoType2, Integer dealInfoType3, Integer year1, Integer year2,
			Integer year4, Integer year5) {
		this.office=office;
		this.add1 = add1;
		this.add2 = add2;
		this.add4 = add4;
		this.add5 = add5;
		this.renew1 = renew1;
		this.renew2 = renew2;
		this.renew4 = renew4;
		this.renew5 = renew5;
		this.countDate = countDate;
		this.payType = payType;
		this.app = app;
		this.product = product;
		this.productType = productType;
		this.pconfigChargeAgentId = pconfigChargeAgentId;
		this.dealInfoType = dealInfoType;
		this.dealInfoType1 = dealInfoType1;
		this.dealInfoType2 = dealInfoType2;
		this.dealInfoType3 = dealInfoType3;
		this.year1 = year1;
		this.year2 = year2;
		this.year4 = year4;
		this.year5 = year5;
	}


	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_settle_certificateSettlementStatistics")
	@SequenceGenerator(name = "seq_settle_certificateSettlementStatistics", sequenceName = "seq_settle_certificateSettlementStatistics")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Integer getAdd1() {
		return add1;
	}


	public void setAdd1(Integer add1) {
		this.add1 = add1;
	}


	public Integer getAdd2() {
		return add2;
	}


	public void setAdd2(Integer add2) {
		this.add2 = add2;
	}


	public Integer getAdd4() {
		return add4;
	}


	public void setAdd4(Integer add4) {
		this.add4 = add4;
	}


	public Integer getAdd5() {
		return add5;
	}


	public void setAdd5(Integer add5) {
		this.add5 = add5;
	}


	public Integer getRenew1() {
		return renew1;
	}


	public void setRenew1(Integer renew1) {
		this.renew1 = renew1;
	}


	public Integer getRenew2() {
		return renew2;
	}


	public void setRenew2(Integer renew2) {
		this.renew2 = renew2;
	}


	public Integer getRenew4() {
		return renew4;
	}


	public void setRenew4(Integer renew4) {
		this.renew4 = renew4;
	}


	public Integer getRenew5() {
		return renew5;
	}


	public void setRenew5(Integer renew5) {
		this.renew5 = renew5;
	}


	public Date getCountDate() {
		return countDate;
	}


	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}


	public Integer getPayType() {
		return payType;
	}


	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_id", nullable = false)
	public ConfigApp getApp() {
		return app;
	}


	public void setApp(ConfigApp app) {
		this.app = app;
	}


	/*public Office getOffice() {
		return office;
	}


	public void setOffice(Office office) {
		this.office = office;
	}*/

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	public ConfigProduct getProduct() {
		return product;
	}


	public void setProduct(ConfigProduct product) {
		this.product = product;
	}


	public Integer getProductType() {
		return productType;
	}


	public void setProductType(Integer productType) {
		this.productType = productType;
	}


	public Long getPconfigChargeAgentId() {
		return pconfigChargeAgentId;
	}


	public void setPconfigChargeAgentId(Long pconfigChargeAgentId) {
		this.pconfigChargeAgentId = pconfigChargeAgentId;
	}


	public Integer getDealInfoType() {
		return dealInfoType;
	}


	public void setDealInfoType(Integer dealInfoType) {
		this.dealInfoType = dealInfoType;
	}


	public Integer getDealInfoType1() {
		return dealInfoType1;
	}


	public void setDealInfoType1(Integer dealInfoType1) {
		this.dealInfoType1 = dealInfoType1;
	}


	public Integer getDealInfoType2() {
		return dealInfoType2;
	}


	public void setDealInfoType2(Integer dealInfoType2) {
		this.dealInfoType2 = dealInfoType2;
	}


	public Integer getDealInfoType3() {
		return dealInfoType3;
	}


	public void setDealInfoType3(Integer dealInfoType3) {
		this.dealInfoType3 = dealInfoType3;
	}


	public Integer getYear1() {
		return year1;
	}


	public void setYear1(Integer year1) {
		this.year1 = year1;
	}


	public Integer getYear2() {
		return year2;
	}


	public void setYear2(Integer year2) {
		this.year2 = year2;
	}


	public Integer getYear4() {
		return year4;
	}


	public void setYear4(Integer year4) {
		this.year4 = year4;
	}


	public Integer getYear5() {
		return year5;
	}


	public void setYear5(Integer year5) {
		this.year5 = year5;
	}

	@ManyToOne
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return office;
	}


	public void setOffice(Office office) {
		this.office = office;
	}

	@ManyToOne
	@JoinColumn(name = "configChargeAgent_id")
	public ConfigChargeAgent getConfigChargeAgent() {
		return configChargeAgent;
	}


	public void setConfigChargeAgent(ConfigChargeAgent configChargeAgent) {
		this.configChargeAgent = configChargeAgent;
	}
	
	
}


