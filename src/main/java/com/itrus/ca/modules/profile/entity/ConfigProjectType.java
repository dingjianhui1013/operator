package com.itrus.ca.modules.profile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

import javax.persistence.Id;

/**
 *	项目类型管理
 */
@Entity
@Table(name = "config_project_type")
public class ConfigProjectType extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String projectName;
	private Boolean applyFlag1;//判断地税
	private Boolean applyFlag2;//判断社保
	// Constructors

	/** default constructor */
	public ConfigProjectType() {
	}

	/** full constructor */
	public ConfigProjectType(String projectName,Boolean applyFlag1,Boolean applyFlag2) {
		this.projectName = projectName;
		this.applyFlag1=applyFlag1;
		this.applyFlag2=applyFlag2;
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

	@Column(name = "project_name")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Column(name = "apply_flag1")
	public Boolean getApplyFlag1() {
		return applyFlag1;
	}

	public void setApplyFlag1(Boolean applyFlag1) {
		this.applyFlag1 = applyFlag1;
	}
	@Column(name = "apply_flag2")
	public Boolean getApplyFlag2() {
		return applyFlag2;
	}

	public void setApplyFlag2(Boolean applyFlag2) {
		this.applyFlag2 = applyFlag2;
	}
	

}