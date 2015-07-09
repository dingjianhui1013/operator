package com.itrus.ca.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * SysConfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_config")
public class SysConfig extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String config;
	private String type;
	private Integer version;

	// Constructors

	/** default constructor */
	public SysConfig() {
	}

	/** minimal constructor */
	public SysConfig(String config, String type) {
		this.config = config;
		this.type = type;
	}

	/** full constructor */
	public SysConfig(String config, String type, Integer version) {
		this.config = config;
		this.type = type;
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

	@Column(name = "config", nullable = false)
	public String getConfig() {
		return this.config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	@Column(name = "type", unique = true, nullable = false)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "version")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}