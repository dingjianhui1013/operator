package com.itrus.ca.modules.log.entity;

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
 * SysOperateLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_operate_log")
public class SysOperateLog extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String type;
	private String remoteAddr;
	private String userAgent;
	private String requestUri;
	private String method;
	private String params;
	private String exception;

	// Constructors

	/** default constructor */
	public SysOperateLog() {
	}

	/** full constructor */
	public SysOperateLog(String type, String remoteAddr, String userAgent,
			String requestUri, String method, String params, String exception) {
		this.type = type;
		this.remoteAddr = remoteAddr;
		this.userAgent = userAgent;
		this.requestUri = requestUri;
		this.method = method;
		this.params = params;
		this.exception = exception;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="SYS_OPERATE_LOG_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="SYS_OPERATE_LOG_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "SYS_OPERATE_LOG_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "type",columnDefinition = "NVARCHAR2(255)")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "remote_addr",columnDefinition = "NVARCHAR2(255)")
	public String getRemoteAddr() {
		return this.remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	@Column(name = "user_agent",columnDefinition = "NVARCHAR2(255)")
	public String getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Column(name = "request_uri",columnDefinition = "NVARCHAR2(255)")
	public String getRequestUri() {
		return this.requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	@Column(name = "method", length = 5,columnDefinition = "NVARCHAR2(5)")
	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "params",columnDefinition = "NCLOB")
	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "exception",columnDefinition = "NCLOB")
	public String getException() {
		return this.exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}