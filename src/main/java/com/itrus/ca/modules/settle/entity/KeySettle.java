/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.entity;

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

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.sys.entity.User;

/**
 * key结算功能Entity
 * @author qt
 * @version 2015-11-12
 */
@Entity
@Table(name = "settle_keySettle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class KeySettle extends DataEntity {
	
	// Fields
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	private ConfigSupplier configSupplier;
	private KeyGeneralInfo keyGeneralInfo;
	private SettleKey settleKey;
	private KeyPurchase keyPurchase;
	private Double keySubtotal;//key小计
    private Long keyTotalQuantity;//key总量
    private Double keyTotalAmount;//key总金额
	
	public KeySettle(){}
	public KeySettle(Long id){
		this.id = id;
	}
	
	
	
	
	public KeySettle( ConfigSupplier configSupplier, KeyGeneralInfo keyGeneralInfo, SettleKey settleKey,
			KeyPurchase keyPurchase, Double keySubtotal, Long keyTotalQuantity, Double keyTotalAmount) {
		this.configSupplier = configSupplier;
		this.keyGeneralInfo = keyGeneralInfo;
		this.settleKey = settleKey;
		this.keyPurchase = keyPurchase;
		this.keySubtotal = keySubtotal;
		this.keyTotalQuantity = keyTotalQuantity;
		this.keyTotalAmount = keyTotalAmount;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_settle_keySettle")
	//@SequenceGenerator(name = "seq_settle_keySettle", sequenceName = "seq_settle_keySettle")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "config_supplier_id")
	public ConfigSupplier getConfigSupplier() {
		return configSupplier;
	}

	public void setConfigSupplier(ConfigSupplier configSupplier) {
		this.configSupplier = configSupplier;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "general_id")
	public KeyGeneralInfo getKeyGeneralInfo() {
		return this.keyGeneralInfo;
	}

	public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
		this.keyGeneralInfo = keyGeneralInfo;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "settleKey_id")
	public SettleKey getSettleKey() {
		return settleKey;
	}
	public void setSettleKey(SettleKey settleKey) {
		this.settleKey = settleKey;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_id")
	public KeyPurchase getKeyPurchase() {
		return keyPurchase;
	}
	public void setKeyPurchase(KeyPurchase keyPurchase) {
		this.keyPurchase = keyPurchase;
	}
	
	public Double getKeySubtotal() {
		return keySubtotal;
	}
	public void setKeySubtotal(Double keySubtotal) {
		keySubtotal = keySubtotal;
	}
	public Long getKeyTotalQuantity() {
		return keyTotalQuantity;
	}
	public void setKeyTotalQuantity(Long keyTotalQuantity) {
		keyTotalQuantity = keyTotalQuantity;
	}
	public Double getKeyTotalAmount() {
		return keyTotalAmount;
	}
	public void setKeyTotalAmount(Double keyTotalAmount) {
		keyTotalAmount = keyTotalAmount;
	}
	
}


