/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 项目发证明细Entity
 * @author qt
 * @version 2015-11-18
 */
@Entity
@Table(name = "settle_projectCertificationDetails")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectCertificationDetails extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private WorkDealInfo workDealInfo; 	

	public ProjectCertificationDetails() {
		
	}

	public ProjectCertificationDetails(Long id){
		
		this.id = id;
	}
	
	
	public ProjectCertificationDetails(WorkDealInfo workDealInfo) {
		
		this.workDealInfo = workDealInfo;
	}

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_settle_projectCertificationDetails")
	@SequenceGenerator(name = "seq_settle_projectCertificationDetails", sequenceName = "seq_settle_projectCertificationDetails")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WorkDealInfo_id")
	public WorkDealInfo getWorkDealInfoVo() {
		return workDealInfo;
	}

	public void setWorkDealInfoVo(WorkDealInfo workDealInfoVo) {
		this.workDealInfo = workDealInfoVo;
	}

	
	
}


