package com.itrus.ca.modules.profile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * 消息配置
 * 
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "config_msg")
public class ConfigMsg extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private String msgName;
	private String msgTitle;
	private Integer msgType;
	private Integer msgSendMethod;
	private Integer msgSendTimeType;
	private String msgContent;

	// Constructors

	/** default constructor */
	public ConfigMsg() {
	}

	/** full constructor */
	public ConfigMsg(String msgName, String msgTitle, Integer msgType,
			Integer msgSendMethod, Integer msgSendTimeType, String msgContent) {
		this.msgName = msgName;
		this.msgTitle = msgTitle;
		this.msgType = msgType;
		this.msgSendMethod = msgSendMethod;
		this.msgSendTimeType = msgSendTimeType;
		this.msgContent = msgContent;
	}

	// Property accessors
	// @SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name = "CONFIG_MSG_SEQUENCE", allocationSize = 1, initialValue = 1, sequenceName = "CONFIG_MSG_SEQUENCE")
	@Id
	@GeneratedValue(generator = "CONFIG_MSG_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "msg_name", columnDefinition = "NVARCHAR2(255)")
	public String getMsgName() {
		return this.msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	@Column(name = "msg_title", columnDefinition = "NVARCHAR2(255)")
	public String getMsgTitle() {
		return this.msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	@Column(name = "msg_type")
	public Integer getMsgType() {
		return this.msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	@Column(name = "msg_send_method")
	public Integer getMsgSendMethod() {
		return this.msgSendMethod;
	}

	public void setMsgSendMethod(Integer msgSendMethod) {
		this.msgSendMethod = msgSendMethod;
	}

	@Column(name = "msg_send_time_type")
	public Integer getMsgSendTimeType() {
		return this.msgSendTimeType;
	}

	public void setMsgSendTimeType(Integer msgSendTimeType) {
		this.msgSendTimeType = msgSendTimeType;
	}

	@Column(name = "msg_content", columnDefinition = "NVARCHAR2(255)")
	public String getMsgContent() {
		return this.msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

}