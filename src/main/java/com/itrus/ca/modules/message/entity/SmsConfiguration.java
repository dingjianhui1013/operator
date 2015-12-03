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
@Table(name = "message_smsConfiguration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SmsConfiguration extends DataEntity {

	private static final long serialVersionUID = 1L;
	private Long id; // 编号
	private String messageName; // 名称
	private String jgmc;
	private String jgdm;
	private String frdb;
	private String zcdm;
	private String keySn;
	private String jgdz;
	private String jbrxm;
	private String zszt;
	private String xmmc;
	private Date zsdqr;

	public SmsConfiguration() {

	}

	public SmsConfiguration(Long id) {
		this.id = id;
	}

	public SmsConfiguration(String messageName, String jgmc, String jgdm, String frdb, String zcdm, String keySn,
			String jgdz, String jbrxm, String zszt, String xmmc, Date zsdqr) {
		
		this.messageName = messageName;
		this.jgmc = jgmc;
		this.jgdm = jgdm;
		this.frdb = frdb;
		this.zcdm = zcdm;
		this.keySn = keySn;
		this.jgdz = jgdz;
		this.jbrxm = jbrxm;
		this.zszt = zszt;
		this.xmmc = xmmc;
		this.zsdqr = zsdqr;
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

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getFrdb() {
		return frdb;
	}

	public void setFrdb(String frdb) {
		this.frdb = frdb;
	}

	public String getZcdm() {
		return zcdm;
	}

	public void setZcdm(String zcdm) {
		this.zcdm = zcdm;
	}

	public String getKeySn() {
		return keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	public String getJgdz() {
		return jgdz;
	}

	public void setJgdz(String jgdz) {
		this.jgdz = jgdz;
	}

	public String getJbrxm() {
		return jbrxm;
	}

	public void setJbrxm(String jbrxm) {
		this.jbrxm = jbrxm;
	}

	public String getZszt() {
		return zszt;
	}

	public void setZszt(String zszt) {
		this.zszt = zszt;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public Date getZsdqr() {
		return zsdqr;
	}

	public void setZsdqr(Date zsdqr) {
		this.zsdqr = zsdqr;
	}

}
