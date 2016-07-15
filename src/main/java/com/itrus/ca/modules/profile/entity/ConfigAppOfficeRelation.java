package com.itrus.ca.modules.profile.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.itrus.ca.modules.sys.entity.Office;

/**
 * ConfigAppOfficeRelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "config_app_office_relation")
public class ConfigAppOfficeRelation implements java.io.Serializable {

	// Fields

	public static final String DEL_FLAG_DELETE = "1";
	private Long id;
	private Office sysOffice;
	private ConfigApp configApp;
	private Date creatDate;

	// Constructors

	/** default constructor */
	public ConfigAppOfficeRelation() {
	}

	/** full constructor */
	public ConfigAppOfficeRelation(Office sysOffice, ConfigApp configApp,
			Date creatDate) {
		this.sysOffice = sysOffice;
		this.configApp = configApp;
		this.creatDate = creatDate;
	}

	// Property accessors
//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="CONFIG_APP_OFFICE_RE_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="CONFIG_APP_OFFICE_RE_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "CONFIG_APP_OFFICE_RE_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.sysOffice;
	}

	public void setOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_id")
	public ConfigApp getConfigApp() {
		return this.configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}

	@Column(name = "creat_date", length = 19)
	public Date getCreatDate() {
		return this.creatDate;
	}
	
	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}


}