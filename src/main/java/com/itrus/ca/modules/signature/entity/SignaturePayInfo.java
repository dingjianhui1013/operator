package com.itrus.ca.modules.signature.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;

/**
 * SignatureInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "signature_pay_info")
public class SignaturePayInfo extends DataEntity  implements java.io.Serializable {

	
	private static final long serialVersionUID = 3998517165221533641L;
	// Fields
	private Long id;
	private SignatureInfo signatureInfo;//印章业务
	private Double addSignature;	//新增金额
	private Double changeSignature; //变更金额
	private Double updateSignature; //更新金额
	private Integer year;		//年限
	private Boolean methodPos;//pos付款方式
	private Boolean methodMoney;//现金付款方式
	private Double posMoney;//POS缴费
	private Double cashMoney;//现金缴费
	private Double totalMoney;//缴费总额
	private Boolean isReceipt;//是否开具发票
	private ReceiptInvoice receiptInvoice;//发票出库单
	
	public SignaturePayInfo(){
		
	}
	
	public SignaturePayInfo(SignatureInfo signatureInfo,
			Boolean methodPos,
			Boolean methodMoney,
			Double posMoney,
			Double cashMoney,
			Double totalMoney,
			Boolean isReceipt,
			ReceiptInvoice receiptInvoice,
			Double addSignature,
			Double changeSignature,
			Double updateSignature,
			Integer year
			) {
		this.signatureInfo = signatureInfo;
		this.methodPos = methodPos;
		this.methodMoney = methodMoney;
		this.posMoney = posMoney;
		this.cashMoney = cashMoney;
		this.totalMoney = totalMoney;
		this.isReceipt = isReceipt;
		this.receiptInvoice = receiptInvoice;
		this.addSignature = addSignature;
		this.changeSignature = changeSignature;
		this.updateSignature = updateSignature;
		this.year = year;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIGNATURE_PAY_INFO_SEQUENCE")
//	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@SequenceGenerator(name="SIGNATURE_PAY_INFO_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="SIGNATURE_PAY_INFO_SEQUENCE")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "signature_info_id")
	public SignatureInfo getSignatureInfo() {
		return signatureInfo;
	}
	public void setSignatureInfo(SignatureInfo signatureInfo) {
		this.signatureInfo = signatureInfo;
	}
	
	
	@Column(name = "method_pos")
	public Boolean getMethodPos() {
		return methodPos;
	}
	public void setMethodPos(Boolean methodPos) {
		this.methodPos = methodPos;
	}
	
	@Column(name = "method_money")
	public Boolean getMethodMoney() {
		return methodMoney;
	}
	public void setMethodMoney(Boolean methodMoney) {
		this.methodMoney = methodMoney;
	}
	
	@Column(name = "pos_money", precision = 20, scale = 3)
	public Double getPosMoney() {
		return posMoney;
	}
	public void setPosMoney(Double posMoney) {
		this.posMoney = posMoney;
	}
	
	@Column(name = "cash_money", precision = 20, scale = 3)
	public Double getCashMoney() {
		return cashMoney;
	}
	public void setCashMoney(Double cashMoney) {
		this.cashMoney = cashMoney;
	}
	
	@Column(name = "total_money", precision = 20, scale = 3)
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	@Column(name = "is_receipt")
	public Boolean getIsReceipt() {
		return isReceipt;
	}
	public void setIsReceipt(Boolean isReceipt) {
		this.isReceipt = isReceipt;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receipt_invoice_id")
	public ReceiptInvoice getReceiptInvoice() {
		return receiptInvoice;
	}
	public void setReceiptInvoice(ReceiptInvoice receiptInvoice) {
		this.receiptInvoice = receiptInvoice;
	}

	@Column(name = "add_signature", precision = 20, scale = 3)
	public Double getAddSignature() {
		return addSignature;
	}

	public void setAddSignature(Double addSignature) {
		this.addSignature = addSignature;
	}

	@Column(name = "change_signature", precision = 20, scale = 3)
	public Double getChangeSignature() {
		return changeSignature;
	}

	public void setChangeSignature(Double changeSignature) {
		this.changeSignature = changeSignature;
	}

	@Column(name = "update_signature", precision = 20, scale = 3)
	public Double getUpdateSignature() {
		return updateSignature;
	}

	public void setUpdateSignature(Double updateSignature) {
		this.updateSignature = updateSignature;
	}

	@Column(name = "year")
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	
}