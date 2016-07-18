package com.itrus.ca.modules.key.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * key出库信息
 */
@Entity
@Table(name = "key_usb_key_invoice")
public class KeyUsbKeyInvoice extends DataEntity implements
		java.io.Serializable {

	// Fields

	private Long id;
	private KeyUsbKeyDepot keyUsbKeyDepot;
	private Integer deliveryNum;
	private String description;
	private int outReason;
	private KeyGeneralInfo keyGeneralInfo;
	private String outReasonName;
	private KeyUsbKeyDepot keyUsbKeyDepotReceive;
	private String keySn;
	private Date startDate;
	private String companyName;

	// Constructors

	@Override
	public void setCreateDate(Date createDate) {
		// TODO Auto-generated method stub
		super.setCreateDate(createDate);
	}

	/** default constructor */
	public KeyUsbKeyInvoice() {
	}

	/** full constructor */
	public KeyUsbKeyInvoice(Integer deliveryNum, KeyUsbKeyDepot keyUsbKeyDepot,
			int outReason, KeyGeneralInfo keyGeneralInfo, String description,
			String keySn, Date startDate) {
		this.deliveryNum = deliveryNum;
		this.description = description;
		this.keyUsbKeyDepot = keyUsbKeyDepot;
		this.keyGeneralInfo = keyGeneralInfo;
		this.outReason = outReason;
		this.keySn = keySn;
		this.startDate = startDate;
	}

	// Property accessors
	// @SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name = "KEY_USB_KEY_INVOICE_SEQUENCE", allocationSize = 1, initialValue = 1, sequenceName = "KEY_USB_KEY_INVOICE_SEQUENCE")
	@Id
	@GeneratedValue(generator = "KEY_USB_KEY_INVOICE_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "description", columnDefinition = "NVARCHAR2(255)")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usb_key_depot_id")
	public KeyUsbKeyDepot getKeyUsbKeyDepot() {
		return keyUsbKeyDepot;
	}

	public void setKeyUsbKeyDepot(KeyUsbKeyDepot keyUsbKeyDepot) {
		this.keyUsbKeyDepot = keyUsbKeyDepot;
	}

	@Column(name = "out_reason")
	public int getOutReason() {
		return outReason;
	}

	public void setOutReason(int outReason) {
		this.outReason = outReason;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_general_info_id")
	public KeyGeneralInfo getKeyGeneralInfo() {
		return keyGeneralInfo;
	}

	public void setKeyGeneralInfo(KeyGeneralInfo keyGeneralInfo) {
		this.keyGeneralInfo = keyGeneralInfo;
	}

	@Column(name = "delivery_num")
	public Integer getDeliveryNum() {
		return deliveryNum;
	}

	public void setDeliveryNum(Integer deliveryNum) {
		this.deliveryNum = deliveryNum;
	}

	@Transient
	public String getOutReasonName() {
		return outReasonName;
	}

	public void setOutReasonName(String outReasonName) {
		this.outReasonName = outReasonName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usb_key_depot_receive")
	public KeyUsbKeyDepot getKeyUsbKeyDepotReceive() {
		return keyUsbKeyDepotReceive;
	}

	public void setKeyUsbKeyDepotReceive(KeyUsbKeyDepot keyUsbKeyDepotReceive) {
		this.keyUsbKeyDepotReceive = keyUsbKeyDepotReceive;
	}

	@Column(name = "key_sn", columnDefinition = "NVARCHAR2(255)")
	public String getKeySn() {
		return keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "company_name", columnDefinition = "NVARCHAR2(255)")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}