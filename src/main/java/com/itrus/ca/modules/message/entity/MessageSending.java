/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkUser;

/**
 * 消息发送Entity
 * @author qt
 * @version 2015-11-30
 */
@Entity
@Table(name = "message_messageSending")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageSending extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private String name; 	// 名称
	private WorkDealInfo workDealInfo;
	private WorkCompany workCompany;
	private WorkUser workUser;
	private SmsConfiguration smsConfiguration;
	private Date smsSendDate;
	private String  messId;
	private int pId;
	private String phone;
	private String messageContext;
	private String returnStatus;//1 ,成功 ；0，失败
	public MessageSending() {
		
	}

	public MessageSending(Long id){
		this();
		this.id = id;
	}
	
	

	

	public MessageSending(String name, WorkDealInfo workDealInfo, WorkCompany workCompany, WorkUser workUser,
			SmsConfiguration smsConfiguration, Date smsSendDate, String messId, String phone, String messageContext,String returnStatus) {
		
		this.name = name;
		this.workDealInfo = workDealInfo;
		this.workCompany = workCompany;
		this.workUser = workUser;
		this.smsConfiguration = smsConfiguration;
		this.smsSendDate = smsSendDate;
		this.messId = messId;
		this.phone = phone;
		this.messageContext = messageContext;
		this.returnStatus=returnStatus;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_message_messageSending")
	//@SequenceGenerator(name = "seq_message_messageSending", sequenceName = "seq_message_messageSending")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Length(min=1, max=200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public WorkUser getWorkUser() {
		return workUser;
	}

	public void setWorkUser(WorkUser workUser) {
		this.workUser = workUser;
	}

	public SmsConfiguration getSmsConfiguration() {
		return smsConfiguration;
	}

	public void setSmsConfiguration(SmsConfiguration smsConfiguration) {
		this.smsConfiguration = smsConfiguration;
	}

	public Date getSmsSendDate() {
		return smsSendDate;
	}

	public void setSmsSendDate(Date smsSendDate) {
		this.smsSendDate = smsSendDate;
	}

	

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getMessId() {
		return messId;
	}

	public void setMessId(String messId) {
		this.messId = messId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessageContext() {
		return messageContext;
	}

	public void setMessageContext(String messageContext) {
		this.messageContext = messageContext;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}
	
}


