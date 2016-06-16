/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 邮箱提取Entity
 * @author qt
 * @version 2015-11-30
 */
@Entity
@Table(name = "message_email_Extraction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmailExtraction extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private WorkDealInfo workDealInfo;
	private WorkCompany workCompany;
	public EmailExtraction() {
		
	}

	public EmailExtraction(Long id){
		
		this.id = id;
	}
	
	
	public EmailExtraction(WorkDealInfo workDealInfo) {
		
		this.workDealInfo = workDealInfo;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_message_emailExtraction")
	//@SequenceGenerator(name = "seq_message_emailExtraction", sequenceName = "seq_message_emailExtraction")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WorkDealInfo_id")
	public WorkDealInfo getWorkDealInfo() {
		return workDealInfo;
	}

	public void setWorkDealInfo(WorkDealInfo workDealInfo) {
		this.workDealInfo = workDealInfo;
	}
	
	public WorkCompany getWorkCompany() {
		return workCompany;
	}

	public void setWorkCompany(WorkCompany workCompany) {
		this.workCompany = workCompany;
	}
	
	
	
}


