/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.entity;

import java.util.Date;

import javax.persistence.Entity;
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
import com.itrus.ca.modules.sys.entity.User;

/**
 * 支付方式证书结算Entity
 * @author qt
 * @version 2016-01-31
 */
@Entity
@Table(name = "settle_paymethodCertificateSettle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymethodCertificateSettle extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private String name; 	// 名称

	public PaymethodCertificateSettle() {
		super();
	}

	public PaymethodCertificateSettle(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_settle_paymethodCertificateSettle")
	//@SequenceGenerator(name = "seq_settle_paymethodCertificateSettle", sequenceName = "seq_settle_paymethodCertificateSettle")
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
	
}


