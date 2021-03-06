/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * 字典Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "sys_dict")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dict extends DataEntity {

	private static final long serialVersionUID = 1L;
	private Long id;		// 编号
	private String label;	// 标签名
	private String value;	// 数据值
	private String type;	// 类型
	private String description;// 描述
	private Integer sort;	// 排序

	public Dict() {
		super();
	}
	
	public Dict(Long id) {
		this();
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYS_DICT_SEQUENCE")
//	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@SequenceGenerator(name="SYS_DICT_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="SYS_DICT_SEQUENCE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Length(min=1, max=100)
	@Column(name = "LABEL",columnDefinition = "NVARCHAR2(127)")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Length(min=1, max=100)
	@Column(name = "VALUE",columnDefinition = "NVARCHAR2(127)")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Length(min=1, max=100)
	@Column(name = "TYPE",columnDefinition = "NVARCHAR2(127)")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	@Column(name = "DESCRIPTION",columnDefinition = "NVARCHAR2(127)")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}