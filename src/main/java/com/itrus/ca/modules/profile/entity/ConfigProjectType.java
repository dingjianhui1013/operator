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
	
	// Constructors

	/** default constructor */
	public ConfigProjectType() {
	}

	/** full constructor */
	public ConfigProjectType(String projectName) {
		this.projectName = projectName;
		
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
	
	
	

}