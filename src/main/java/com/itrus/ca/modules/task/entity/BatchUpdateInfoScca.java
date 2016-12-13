package com.itrus.ca.modules.task.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 批量更新临时表
 * 
 * */

@Entity
@Table(name = "BATCH_UPDATE_INFO_SCCA")
public class BatchUpdateInfoScca  implements  Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	private Long id;
	
	private String certSn;            //证书序列号
	
	private String firstCertSn;       //首张证书序列号
	
	private String keySn;             //key编码
	
	private Date certStartTime;       //证书起始时间
	
	private Date certEndTime;         //证书结束时间
	
	private String companyName;       //单位名称
	
	private String remark;            //备注:向work_deal_info表中插入的数据
	
	private Integer statusCode;       //返回的错误状态码
	
	private Long workDealInfoId;      //返回的dealInfo表Id,

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BATCH_UPDATE_SEQUENCE")
	@SequenceGenerator(name = "BATCH_UPDATE_SEQUENCE", sequenceName = "BATCH_UPDATE_SEQUENCE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="CERT_SN")
	public String getCertSn() {
		return certSn;
	}

	public void setCertSn(String certSn) {
		this.certSn = certSn;
	}

	@Column(name="FIRST_CERT_SN")
	public String getFirstCertSn() {
		return firstCertSn;
	}

	public void setFirstCertSn(String firstCertSn) {
		this.firstCertSn = firstCertSn;
	}

	@Column(name="KEY_SN")
	public String getKeySn() {
		return keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	@Column(name="CERT_START_TIME")
	public Date getCertStartTime() {
		return certStartTime;
	}

	
	public void setCertStartTime(Date certStartTime) {
		this.certStartTime = certStartTime;
	}

	@Column(name="CERT_END_TIME")
	public Date getCertEndTime() {
		return certEndTime;
	}

	
	public void setCertEndTime(Date certEndTime) {
		this.certEndTime = certEndTime;
	}

	@Column(name="COMPANT_NAME")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="STATUSCODE")
	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	@Column(name="DEALINFOID")
	public Long getWorkDealInfoId() {
		return workDealInfoId;
	}

	public void setWorkDealInfoId(Long workDealInfoId) {
		this.workDealInfoId = workDealInfoId;
	}
	
	
	
	
	
	
}
