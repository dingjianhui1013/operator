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
	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "COMMON_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "remote_addr")
	public String getRemoteAddr() {
		return this.remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	@Column(name = "user_agent")
	public String getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Column(name = "request_uri")
	public String getRequestUri() {
		return this.requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	@Column(name = "method", length = 5)
	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "params")
	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "exception")
	public String getException() {
		return this.exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}