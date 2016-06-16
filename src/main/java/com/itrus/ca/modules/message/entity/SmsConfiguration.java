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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkUser;

/**
 * 短信配置Entity
 * 
 * @author qt
 * @version 2015-11-27
 */
@Entity
@Table(name = "message_sms_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SmsConfiguration extends DataEntity {

	private static final long serialVersionUID = 1L;
	private Long id; // 编号
	private String messageName; // 名称
	private String messageContent;
	private String messageAddress;

	public SmsConfiguration() {

	}

	public SmsConfiguration(Long id) {
		this.id = id;
	}

	

	public SmsConfiguration(String messageName, String messageContent,String messageAddress) {
		
		this.messageName = messageName;
		this.messageContent = messageContent;
		this.messageAddress =messageAddress;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "seq_message_smsConfiguration")
	// @SequenceGenerator(name = "seq_message_smsConfiguration", sequenceName =
	// "seq_message_smsConfiguration")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Length(min = 1, max = 200)
	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String name) {
		this.messageName = name;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageAddress() {
		return messageAddress;
	}

	public void setMessageAddress(String messageAddress) {
		this.messageAddress = messageAddress;
	}
	
	
}
