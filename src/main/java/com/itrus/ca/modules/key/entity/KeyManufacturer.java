package com.itrus.ca.modules.key.entity;

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

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * key厂家
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "key_manufacturer")
public class KeyManufacturer extends DataEntity implements java.io.Serializable {

	// Fields

	private Long id;
	private Office sysOffice;
	private String name;
	private String linkman;
	private String linkmanPhone;
	private String linkmanEmail;
	private String description;
	private String code;
	private String address;

	// Constructors

	/** default constructor */
	public KeyManufacturer() {
	}

	/** minimal constructor */
	public KeyManufacturer(String name, String linkman, String linkmanPhone,
			String linkmanEmail, String code) {
		this.name = name;
		this.linkman = linkman;
		this.linkmanPhone = linkmanPhone;
		this.linkmanEmail = linkmanEmail;
		this.code = code;
	}

	/** full constructor */
	public KeyManufacturer(Office sysOffice, String name, String linkman,
			String linkmanPhone, String linkmanEmail, String description,
			String code, String address) {
		this.sysOffice = sysOffice;
		this.name = name;
		this.linkman = linkman;
		this.linkmanPhone = linkmanPhone;
		this.linkmanEmail = linkmanEmail;
		this.description = description;
		this.code = code;
		this.address = address;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return this.sysOffice;
	}

	public void setOffice(Office sysOffice) {
		this.sysOffice = sysOffice;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "linkman", nullable = false)
	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	@Column(name = "linkman_phone", nullable = false)
	public String getLinkmanPhone() {
		return this.linkmanPhone;
	}

	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}

	@Column(name = "linkman_email", nullable = false)
	public String getLinkmanEmail() {
		return this.linkmanEmail;
	}

	public void setLinkmanEmail(String linkmanEmail) {
		this.linkmanEmail = linkmanEmail;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "code")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}