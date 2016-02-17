package com.itrus.ca.modules.work.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.itrus.ca.common.persistence.DataEntity;

/**
 * WorkCertInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_cert_info")
public class WorkCertInfo  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5952758462854452344L;
	private Long id;
	private WorkCertApplyInfo workCertApplyInfo;
	private Boolean isMaster;
	private String issuerDn;
	private String issuerHashMd5;
	private Date notafter;
	private Date notbefore;
	private Integer productStatus;
	private Date renewalDate;
	private Long renewalNextId;
	private Long renewalPrevId;
	private String reqBuf;
	private String reqBufType;
	private String reqChallenge;
	private String reqComment;
	private String reqNonce;
	private Integer reqOverrideValidity;
	private Integer reqOverrideValidityExtend;
	private String reqTransid;
	private String serialnumber;
	private String signBuf;
	private String signBufP7;
	private Date signDate;
	private String source;
	private Integer status;
	private String subjectDn;
	private String subjectHashMd5;
	private Date suspendDate;
	private Integer trustDeviceCount;
	private Integer lastTrustDeviceCount;
	private Date lastTrustDeviceDate;
	private Date trustDeviceDate;
	private Set<WorkDealInfo> workDealInfos = new HashSet<WorkDealInfo>(0);
	private String provider;
	private Boolean obtained;
	private String type;
	private Date revokeDate;
	private Long workUserId;//为23号上线特殊处理的字段，有该字段的certInfo将生成一个新增业务的workDealInfo
	private String keySn;//为23号上线特殊处理的字段,对应dealInfo表的keySn
	// Constructors
	//双证
	private String certKmcRep1;
	private String certKmcRep2;
	private String certKmcRep3;
	private String certSignBufKmc;
	private String certTypeKmc;
	private String certReqBufKmc;
	private String certReqBufTypeKmc;
	private String certSerialnumberKmc;
	private String installMode;
	
	private Date createDate;
	
	
	/** default constructor */
	public WorkCertInfo() {
	}

	/** full constructor */
	public WorkCertInfo(WorkCertApplyInfo workCertApplyInfo, Boolean isMaster,
			String issuerDn, String issuerHashMd5, Date notafter,
			Date notbefore, Integer productStatus, Date renewalDate,
			Long renewalNextId, Long renewalPrevId, String reqBuf,
			String reqBufType, String reqChallenge, String reqComment,
			String reqNonce, Integer reqOverrideValidity,
			Integer reqOverrideValidityExtend, String reqTransid,
			String serialnumber, String signBuf, String signBufP7,
			Date signDate, String source, Integer status,
			String subjectDn, String subjectHashMd5, Date suspendDate,
			Integer trustDeviceCount, Integer lastTrustDeviceCount,
			Date lastTrustDeviceDate, Date trustDeviceDate,
			Set<WorkDealInfo> workDealInfos) {
		this.workCertApplyInfo = workCertApplyInfo;
		this.isMaster = isMaster;
		this.issuerDn = issuerDn;
		this.issuerHashMd5 = issuerHashMd5;
		this.notafter = notafter;
		this.notbefore = notbefore;
		this.productStatus = productStatus;
		this.renewalDate = renewalDate;
		this.renewalNextId = renewalNextId;
		this.renewalPrevId = renewalPrevId;
		this.reqBuf = reqBuf;
		this.reqBufType = reqBufType;
		this.reqChallenge = reqChallenge;
		this.reqComment = reqComment;
		this.reqNonce = reqNonce;
		this.reqOverrideValidity = reqOverrideValidity;
		this.reqOverrideValidityExtend = reqOverrideValidityExtend;
		this.reqTransid = reqTransid;
		this.serialnumber = serialnumber;
		this.signBuf = signBuf;
		this.signBufP7 = signBufP7;
		this.signDate = signDate;
		this.source = source;
		this.status = status;
		this.subjectDn = subjectDn;
		this.subjectHashMd5 = subjectHashMd5;
		this.suspendDate = suspendDate;
		this.trustDeviceCount = trustDeviceCount;
		this.lastTrustDeviceCount = lastTrustDeviceCount;
		this.lastTrustDeviceDate = lastTrustDeviceDate;
		this.trustDeviceDate = trustDeviceDate;
		this.workDealInfos = workDealInfos;
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
	@JoinColumn(name = "apply_info")
	public WorkCertApplyInfo getWorkCertApplyInfo() {
		return this.workCertApplyInfo;
	}

	public void setWorkCertApplyInfo(WorkCertApplyInfo workCertApplyInfo) {
		this.workCertApplyInfo = workCertApplyInfo;
	}

	@Column(name = "is_master")
	public Boolean getIsMaster() {
		return this.isMaster;
	}

	public void setIsMaster(Boolean isMaster) {
		this.isMaster = isMaster;
	}

	@Column(name = "issuer_dn")
	public String getIssuerDn() {
		return this.issuerDn;
	}

	public void setIssuerDn(String issuerDn) {
		this.issuerDn = issuerDn;
	}

	@Column(name = "issuer_hash_md5")
	public String getIssuerHashMd5() {
		return this.issuerHashMd5;
	}

	public void setIssuerHashMd5(String issuerHashMd5) {
		this.issuerHashMd5 = issuerHashMd5;
	}

	@Column(name = "notafter", length = 19)
	public Date getNotafter() {
		return this.notafter;
	}

	public void setNotafter(Date notafter) {
		this.notafter = notafter;
	}

	@Column(name = "notbefore", length = 19)
	public Date getNotbefore() {
		return this.notbefore;
	}

	public void setNotbefore(Date notbefore) {
		this.notbefore = notbefore;
	}

	@Column(name = "product_status")
	public Integer getProductStatus() {
		return this.productStatus;
	}

	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	@Column(name = "renewal_date", length = 19)
	public Date getRenewalDate() {
		return this.renewalDate;
	}

	public void setRenewalDate(Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	@Column(name = "renewal_next_id")
	public Long getRenewalNextId() {
		return this.renewalNextId;
	}

	public void setRenewalNextId(Long renewalNextId) {
		this.renewalNextId = renewalNextId;
	}

	@Column(name = "renewal_prev_id")
	public Long getRenewalPrevId() {
		return this.renewalPrevId;
	}

	public void setRenewalPrevId(Long renewalPrevId) {
		this.renewalPrevId = renewalPrevId;
	}

	@Column(name = "req_buf")
	public String getReqBuf() {
		return this.reqBuf;
	}

	public void setReqBuf(String reqBuf) {
		this.reqBuf = reqBuf;
	}

	@Column(name = "req_buf_type")
	public String getReqBufType() {
		return this.reqBufType;
	}

	public void setReqBufType(String reqBufType) {
		this.reqBufType = reqBufType;
	}

	@Column(name = "req_challenge")
	public String getReqChallenge() {
		return this.reqChallenge;
	}

	public void setReqChallenge(String reqChallenge) {
		this.reqChallenge = reqChallenge;
	}

	@Column(name = "req_comment")
	public String getReqComment() {
		return this.reqComment;
	}

	public void setReqComment(String reqComment) {
		this.reqComment = reqComment;
	}

	@Column(name = "req_nonce")
	public String getReqNonce() {
		return this.reqNonce;
	}

	public void setReqNonce(String reqNonce) {
		this.reqNonce = reqNonce;
	}

	@Column(name = "req_override_validity")
	public Integer getReqOverrideValidity() {
		return this.reqOverrideValidity;
	}

	public void setReqOverrideValidity(Integer reqOverrideValidity) {
		this.reqOverrideValidity = reqOverrideValidity;
	}

	@Column(name = "req_override_validity_extend")
	public Integer getReqOverrideValidityExtend() {
		return this.reqOverrideValidityExtend;
	}

	public void setReqOverrideValidityExtend(Integer reqOverrideValidityExtend) {
		this.reqOverrideValidityExtend = reqOverrideValidityExtend;
	}

	@Column(name = "req_transid")
	public String getReqTransid() {
		return this.reqTransid;
	}

	public void setReqTransid(String reqTransid) {
		this.reqTransid = reqTransid;
	}

	@Column(name = "serialnumber")
	public String getSerialnumber() {
		return this.serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	@Column(name = "sign_buf")
	public String getSignBuf() {
		return this.signBuf;
	}

	public void setSignBuf(String signBuf) {
		this.signBuf = signBuf;
	}
	@Column(name = "sign_buf_p7")
	public String getSignBufP7() {
		return this.signBufP7;
	}

	public void setSignBufP7(String signBufP7) {
		this.signBufP7 = signBufP7;
	}

	@Column(name = "sign_date", length = 19)
	public Date getSignDate() {
		return this.signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	@Column(name = "source")
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "subject_dn")
	public String getSubjectDn() {
		return this.subjectDn;
	}

	public void setSubjectDn(String subjectDn) {
		this.subjectDn = subjectDn;
	}

	@Column(name = "subject_hash_md5")
	public String getSubjectHashMd5() {
		return this.subjectHashMd5;
	}

	public void setSubjectHashMd5(String subjectHashMd5) {
		this.subjectHashMd5 = subjectHashMd5;
	}

	@Column(name = "suspend_date", length = 19)
	public Date getSuspendDate() {
		return this.suspendDate;
	}

	public void setSuspendDate(Date suspendDate) {
		this.suspendDate = suspendDate;
	}

	@Column(name = "trust_device_count")
	public Integer getTrustDeviceCount() {
		return this.trustDeviceCount==null? 0:this.trustDeviceCount;
	}

	public void setTrustDeviceCount(Integer trustDeviceCount) {
		this.trustDeviceCount = trustDeviceCount;
	}

	@Column(name = "last_trust_device_count")
	public Integer getLastTrustDeviceCount() {
		return this.lastTrustDeviceCount;
	}

	public void setLastTrustDeviceCount(Integer lastTrustDeviceCount) {
		this.lastTrustDeviceCount = lastTrustDeviceCount;
	}

	@Column(name = "last_trust_device_date", length = 19)
	public Date getLastTrustDeviceDate() {
		return this.lastTrustDeviceDate;
	}

	public void setLastTrustDeviceDate(Date lastTrustDeviceDate) {
		this.lastTrustDeviceDate = lastTrustDeviceDate;
	}

	@Column(name = "trust_device_date", length = 19)
	public Date getTrustDeviceDate() {
		return this.trustDeviceDate;
	}

	public void setTrustDeviceDate(Date trustDeviceDate) {
		this.trustDeviceDate = trustDeviceDate;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workCertInfo")
	public Set<WorkDealInfo> getWorkDealInfos() {
		return this.workDealInfos;
	}

	public void setWorkDealInfos(Set<WorkDealInfo> workDealInfos) {
		this.workDealInfos = workDealInfos;
	}

	@Column(name = "provider")
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Column(name = "obtained")
	public Boolean getObtained() {
		return obtained;
	}

	public void setObtained(Boolean obtained) {
		this.obtained = obtained;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "revoke_date")
	public Date getRevokeDate() {
		return revokeDate;
	}

	public void setRevokeDate(Date revokeDate) {
		this.revokeDate = revokeDate;
	}

	@Column(name = "work_user_id")
	public Long getWorkUserId() {
		return workUserId;
	}

	public void setWorkUserId(Long workUserId) {
		this.workUserId = workUserId;
	}

	@Column(name = "key_sn")
	public String getKeySn() {
		return keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}
	/**
	 *双证支持 
	 */

	@Column(name = "cert_kmc_rep1")
	public String getCertKmcRep1() {
		return certKmcRep1;
	}

	public void setCertKmcRep1(String certKmcRep1) {
		this.certKmcRep1 = certKmcRep1;
	}

	@Column(name = "cert_kmc_rep2")
	public String getCertKmcRep2() {
		return certKmcRep2;
	}

	public void setCertKmcRep2(String certKmcRep2) {
		this.certKmcRep2 = certKmcRep2;
	}

	@Column(name = "cert_kmc_rep3")
	public String getCertKmcRep3() {
		return certKmcRep3;
	}

	public void setCertKmcRep3(String certKmcRep3) {
		this.certKmcRep3 = certKmcRep3;
	}

	@Column(name = "cert_sign_buf_kmc")
	public String getCertSignBufKmc() {
		return certSignBufKmc;
	}

	public void setCertSignBufKmc(String certSignBufKmc) {
		this.certSignBufKmc = certSignBufKmc;
	}

	@Column(name = "cert_type_kmc")
	public String getCertTypeKmc() {
		return certTypeKmc;
	}

	public void setCertTypeKmc(String certTypeKmc) {
		this.certTypeKmc = certTypeKmc;
	}

	@Column(name = "cert_req_buf_kmc")
	public String getCertReqBufKmc() {
		return certReqBufKmc;
	}

	public void setCertReqBufKmc(String certReqBufKmc) {
		this.certReqBufKmc = certReqBufKmc;
	}

	@Column(name = "cert_req_buf_type_kmc")
	public String getCertReqBufTypeKmc() {
		return certReqBufTypeKmc;
	}

	public void setCertReqBufTypeKmc(String certReqBufTypeKmc) {
		this.certReqBufTypeKmc = certReqBufTypeKmc;
	}

	@Column(name = "cert_serialnumber_kmc")
	public String getCertSerialnumberKmc() {
		return certSerialnumberKmc;
	}

	public void setCertSerialnumberKmc(String certSerialnumberKmc) {
		this.certSerialnumberKmc = certSerialnumberKmc;
	}

	@Column(name = "install_mode")
	public String getInstallMode() {
		return installMode;
	}

	public void setInstallMode(String installMode) {
		this.installMode = installMode;
	}
	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}