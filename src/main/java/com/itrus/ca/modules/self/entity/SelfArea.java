package com.itrus.ca.modules.self.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SelfArea entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SELF_AREA")
public class SelfArea implements java.io.Serializable {

	// Fields

	private Long id;
	private String areaId;
	private String areaName;
	private String parentId;
	private String areaLevel;
	private String status;
	private String lastmodifydate;
	private String lastmodifypers;
	private String isstandardcode;
	private String datastandard;
	private String deleteTag;

	// Constructors

	/** default constructor */
	public SelfArea() {
	}

	/** full constructor */
	public SelfArea(String areaId, String areaName, String parentId,
			String areaLevel, String status, String lastmodifydate,
			String lastmodifypers, String isstandardcode, String datastandard,
			String deleteTag) {
		this.areaId = areaId;
		this.areaName = areaName;
		this.parentId = parentId;
		this.areaLevel = areaLevel;
		this.status = status;
		this.lastmodifydate = lastmodifydate;
		this.lastmodifypers = lastmodifypers;
		this.isstandardcode = isstandardcode;
		this.datastandard = datastandard;
		this.deleteTag = deleteTag;
	}

	// Property accessors
	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@Id
	@GeneratedValue(generator = "COMMON_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "AREA_ID", columnDefinition = "NVARCHAR2(40)")
	public String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	@Column(name = "AREA_NAME", columnDefinition = "NVARCHAR2(100)")
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "PARENT_ID", columnDefinition = "NVARCHAR2(40)")
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "AREA_LEVEL", columnDefinition = "NVARCHAR2(100)")
	public String getAreaLevel() {
		return this.areaLevel;
	}

	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}

	@Column(name = "STATUS", columnDefinition = "NVARCHAR2(100)")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "LASTMODIFYDATE", columnDefinition = "NVARCHAR2(100)")
	public String getLastmodifydate() {
		return this.lastmodifydate;
	}

	public void setLastmodifydate(String lastmodifydate) {
		this.lastmodifydate = lastmodifydate;
	}

	@Column(name = "LASTMODIFYPERS", columnDefinition = "NVARCHAR2(60)")
	public String getLastmodifypers() {
		return this.lastmodifypers;
	}

	public void setLastmodifypers(String lastmodifypers) {
		this.lastmodifypers = lastmodifypers;
	}

	@Column(name = "ISSTANDARDCODE", columnDefinition = "NVARCHAR2(4)")
	public String getIsstandardcode() {
		return this.isstandardcode;
	}

	public void setIsstandardcode(String isstandardcode) {
		this.isstandardcode = isstandardcode;
	}

	@Column(name = "DATASTANDARD", columnDefinition = "NVARCHAR2(60)")
	public String getDatastandard() {
		return this.datastandard;
	}

	public void setDatastandard(String datastandard) {
		this.datastandard = datastandard;
	}

	@Column(name = "DELETE_TAG", columnDefinition = "NVARCHAR2(20)")
	public String getDeleteTag() {
		return this.deleteTag;
	}

	public void setDeleteTag(String deleteTag) {
		this.deleteTag = deleteTag;
	}

}
