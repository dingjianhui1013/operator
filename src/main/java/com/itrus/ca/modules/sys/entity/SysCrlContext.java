package com.itrus.ca.modules.sys.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * SysCrlContext entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_crl_context")
public class SysCrlContext extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String crlName ;
	private byte[] caCertBuf;
	private Date certEndTime;
	private String certSn;
	private Date certStartTime;
	private String certSubject;
	private Boolean checkCrl;
	private String crlUrl;
	private String issuerdn;
	private String retryPolicy;
	private Integer version;

	// Constructors

	/** default constructor */
	public SysCrlContext() {
	}

	/** minimal constructor */
	public SysCrlContext(byte[] caCertBuf, Boolean checkCrl, String crlUrl,
			String issuerdn, String retryPolicy) {
		this.caCertBuf = caCertBuf;
		this.checkCrl = checkCrl;
		this.crlUrl = crlUrl;
		this.issuerdn = issuerdn;
		this.retryPolicy = retryPolicy;
	}

	/** full constructor */
	public SysCrlContext(String crlName,byte[] caCertBuf, Timestamp certEndTime,
			String certSn, Timestamp certStartTime, String certSubject,
			Boolean checkCrl, String crlUrl, String issuerdn,
			String retryPolicy, Integer version) {
		this.crlName = crlName;
		this.caCertBuf = caCertBuf;
		this.certEndTime = certEndTime;
		this.certSn = certSn;
		this.certStartTime = certStartTime;
		this.certSubject = certSubject;
		this.checkCrl = checkCrl;
		this.crlUrl = crlUrl;
		this.issuerdn = issuerdn;
		this.retryPolicy = retryPolicy;
		this.version = version;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMON_SEQUENCE")
	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ca_cert_buf", nullable = false)
	public byte[] getCaCertBuf() {
		return this.caCertBuf;
	}
	@Column(name = "crl_Name", nullable = false)
	public String getCrlName() {
		return crlName;
	}

	public void setCrlName(String crlName) {
		this.crlName = crlName;
	}

	public void setCaCertBuf(byte[] caCertBuf) {
		this.caCertBuf = caCertBuf;
	}

	@Column(name = "cert_end_time", length = 19)
	public Date getCertEndTime() {
		return this.certEndTime;
	}

	public void setCertEndTime(Date certEndTime) {
		this.certEndTime = certEndTime;
	}

	@Column(name = "cert_sn")
	public String getCertSn() {
		return this.certSn;
	}

	public void setCertSn(String certSn) {
		this.certSn = certSn;
	}

	@Column(name = "cert_start_time", length = 19)
	public Date getCertStartTime() {
		return this.certStartTime;
	}

	public void setCertStartTime(Date certStartTime) {
		this.certStartTime = certStartTime;
	}

	@Column(name = "cert_subject")
	public String getCertSubject() {
		return this.certSubject;
	}

	public void setCertSubject(String certSubject) {
		this.certSubject = certSubject;
	}

	@Column(name = "check_crl", nullable = false)
	public Boolean getCheckCrl() {
		return this.checkCrl;
	}

	public void setCheckCrl(Boolean checkCrl) {
		this.checkCrl = checkCrl;
	}

	@Column(name = "crl_url", nullable = false)
	public String getCrlUrl() {
		return this.crlUrl;
	}

	public void setCrlUrl(String crlUrl) {
		this.crlUrl = crlUrl;
	}

	@Column(name = "issuerdn", nullable = false)
	public String getIssuerdn() {
		return this.issuerdn;
	}

	public void setIssuerdn(String issuerdn) {
		this.issuerdn = issuerdn;
	}

	@Column(name = "retry_policy", nullable = false)
	public String getRetryPolicy() {
		return this.retryPolicy;
	}

	public void setRetryPolicy(String retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	@Column(name = "version")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}