package com.itrus.ca.modules.work.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.settle.vo.PayableDetailVo;
import com.itrus.ca.modules.sys.entity.User;

/**
 * WorkDealInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "work_deal_info")
public class WorkDealInfo extends DataEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6803990965287582259L;
	private Long id;
	private ConfigApp configApp;
	private WorkCompany workCompany;
	private WorkCompanyHis workCompanyHis;
	private WorkUser workUser;
	private WorkUserHis workUserHis;
	private Integer dealInfoType;
	private Integer dealInfoType1;
	private Integer dealInfoType2;
	private Integer dealInfoType3;
	private String dealInfoStatus;
	private Integer year;

	// 2016.06.06 具体到期时间 经信委
	private Date expirationDate;

	private ConfigProduct configProduct;
	private String certSn;
	private String keySn;
	private Integer certSort;
	private Set<WorkLog> workLogs = new HashSet<WorkLog>(0);
	private WorkPayInfo workPayInfo;
	private WorkCertInfo workCertInfo;
	private String dealTypeName;
	private Integer status;
	private String archiveNo;
	private Integer classifying;
	private String dealInfoSn;
	private Timestamp archiveDate;
	private Timestamp creactDate;
	private Integer downLoad;
	private Integer trustDeviceCount;
	private ConfigCommercialAgent configCommercialAgent;// 市场推广代理商
	private String svn;
	private Boolean canSettle;
	private String refuse;
	private Boolean manMadeDamage;
	private Integer lastDays;// 信息变更时，记录原有证书剩余的时间
	private String userSn;// 归档编号
	private Long prevId;// 上笔业务的id
	private Integer physicalLife;// 实际年限
	private ConfigCommercialAgent commercialAgent;// 劳务关系代理商
	private Date obtainedDate;
	private Double moneyDouble;
	private Integer payType;// 支付方式暂存（只为代入）1.通用;2.政府;3.合同
	private Integer settledLife;// 已经结算年限
	private Integer residualLife;// 剩余年限
	// private Integer dealInfoOffice;//业务归属
	private Date notafter;// 证书到期时间（业务办理列表大数据流已被关闭的问题）
	private Integer addCertDays;// 赠送天数
	private Boolean isIxin;// i信端更新标识
	private Integer isSJQY;// 是否是数据迁移的数据 是为1 否为null or 0 2批量导入数据
	// Constructors
	private Long configChargeAgentId;
	private User inputUser;// 录入人
	private User payUser;// 缴费人
	private User attestationUser;// 鉴证人
	private User businessCardUser;// 制证人

	private Date inputUserDate;// 录入人时间
	private Date payUserDate;// 缴费人时间
	private Date attestationUserDate;// 鉴证人时间
	private Date businessCardUserDate;// 制证人时间

	private List<PayableDetailVo> detailList;

	private Integer yyNum;      //已经结算
	private Integer totalNum;   //总结算
	private Integer lastNum;    //本次结算
	private Integer waitNum;    //待结算

	private Long officeId;
	private Long areaId;

	private Long selfApplyId;
	private String IsMainTain;

	private Timestamp openTime; // 证书首次签发日期

	private String firstCertSN; // 首个证书序列号，用来串起需要明细的导入数据

	/** default constructor */
	public WorkDealInfo() {
	}

	/** minimal constructor */
	public WorkDealInfo(ConfigApp configApp, Integer dealInfoType,
			String dealInfoStatus) {
		this.configApp = configApp;
		this.dealInfoType = dealInfoType;
		this.dealInfoStatus = dealInfoStatus;
	}

	/** full constructor */
	public WorkDealInfo(ConfigApp configApp, WorkCompany workCompany,
			WorkUser workUser, Integer dealInfoType, String dealInfoStatus,
			ConfigProduct configProduct, WorkCertInfo workCertInfo,
			Integer year, String certSn, String keySn, Integer certSort,
			Integer trustDeviceCount, Set<WorkLog> workLogs,
			Long configChargeAgentId, Integer isSJQY, User inputUser,
			User payUser, User attestationUser, User businessCardUser,
			Date inputUserDate, Date payUserDate, Date attestationUserDate,
			Date businessCardUserDate, Long officeId, Long areaId,
			Long selfApplyId, String IsMainTain) {
		this.configApp = configApp;
		this.workCompany = workCompany;
		this.workUser = workUser;
		this.dealInfoType = dealInfoType;
		this.dealInfoStatus = dealInfoStatus;
		this.year = year;
		this.certSn = certSn;
		this.keySn = keySn;
		this.certSort = certSort;
		this.workLogs = workLogs;
		this.configProduct = configProduct;
		this.workCertInfo = workCertInfo;
		this.trustDeviceCount = trustDeviceCount;
		this.configChargeAgentId = configChargeAgentId;
		this.isSJQY = isSJQY;
		this.inputUser = inputUser;
		this.payUser = payUser;
		this.attestationUser = attestationUser;
		this.businessCardUser = businessCardUser;
		this.inputUserDate = inputUserDate;
		this.payUserDate = payUserDate;
		this.attestationUserDate = attestationUserDate;
		this.businessCardUserDate = businessCardUserDate;
		this.officeId = officeId;
		this.areaId = areaId;
		this.selfApplyId = selfApplyId;
		this.IsMainTain = IsMainTain;

	}

	// Property accessors
	@SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
	@Id
	@GeneratedValue(generator = "COMMON_SEQUENCE", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	// @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_id", nullable = false)
	public ConfigApp getConfigApp() {
		return this.configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_company_id")
	public WorkCompany getWorkCompany() {
		return this.workCompany;
	}

	public void setWorkCompany(WorkCompany workCompany) {
		this.workCompany = workCompany;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id")
	public ConfigCommercialAgent getConfigCommercialAgent() {
		return configCommercialAgent;
	}

	public void setConfigCommercialAgent(
			ConfigCommercialAgent configCommercialAgent) {
		this.configCommercialAgent = configCommercialAgent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	// @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "work_user_id")
	public WorkUser getWorkUser() {
		return this.workUser;
	}

	public void setWorkUser(WorkUser workUser) {
		this.workUser = workUser;
	}

	@Column(name = "deal_info_type")
	public Integer getDealInfoType() {
		return this.dealInfoType;
	}

	public void setDealInfoType(Integer dealInfoType) {
		this.dealInfoType = dealInfoType;
	}

	@Column(name = "deal_info_status", nullable = false, length = 100)
	public String getDealInfoStatus() {
		return this.dealInfoStatus;
	}

	public void setDealInfoStatus(String dealInfoStatus) {
		this.dealInfoStatus = dealInfoStatus;
	}

	@Column(name = "year")
	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Column(name = "cert_sn")
	public String getCertSn() {
		return this.certSn;
	}

	public void setCertSn(String certSn) {
		this.certSn = certSn;
	}

	@Column(name = "key_sn")
	public String getKeySn() {
		return this.keySn;
	}

	public void setKeySn(String keySn) {
		this.keySn = keySn;
	}

	@Column(name = "cert_sort")
	public Integer getCertSort() {
		return this.certSort;
	}

	public void setCertSort(Integer certSort) {
		this.certSort = certSort;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workDealInfo")
	public Set<WorkLog> getWorkLogs() {
		return this.workLogs;
	}

	public void setWorkLogs(Set<WorkLog> workLogs) {
		this.workLogs = workLogs;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	// @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pay_id")
	public WorkPayInfo getWorkPayInfo() {
		return workPayInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cert_id")
	public WorkCertInfo getWorkCertInfo() {
		return workCertInfo;
	}

	public void setWorkPayInfo(WorkPayInfo workPayInfo) {
		this.workPayInfo = workPayInfo;
	}

	public void setWorkCertInfo(WorkCertInfo workCertInfo) {
		this.workCertInfo = workCertInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	// @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	public ConfigProduct getConfigProduct() {
		return configProduct;
	}

	public void setConfigProduct(ConfigProduct configProduct) {
		this.configProduct = configProduct;
	}

	@Transient
	public String getDealTypeName() {
		return dealTypeName;
	}

	public void setDealTypeName(String dealTypeName) {
		this.dealTypeName = dealTypeName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "deal_info_type1")
	public Integer getDealInfoType1() {
		return dealInfoType1;
	}

	@Column(name = "deal_info_type2")
	public Integer getDealInfoType2() {
		return dealInfoType2;
	}

	public void setDealInfoType1(Integer dealInfoType1) {
		this.dealInfoType1 = dealInfoType1;
	}

	public void setDealInfoType2(Integer dealInfoType2) {
		this.dealInfoType2 = dealInfoType2;
	}

	@Column(name = "archive_no")
	public String getArchiveNo() {
		return archiveNo;
	}

	public void setArchiveNo(String archiveNo) {
		this.archiveNo = archiveNo;
	}

	@Column(name = "classifying")
	public Integer getClassifying() {
		return classifying;
	}

	public void setClassifying(Integer classifying) {
		this.classifying = classifying;
	}

	@Column(name = "dealInfoSn")
	public String getDealInfoSn() {
		return dealInfoSn;
	}

	public void setDealInfoSn(String dealInfoSn) {
		this.dealInfoSn = dealInfoSn;
	}

	@Column(name = "archive_date")
	public Timestamp getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(Timestamp archiveDate) {
		this.archiveDate = archiveDate;
	}

	@Column(name = "creact_date")
	public Timestamp getCreactDate() {
		return creactDate;
	}

	public void setCreactDate(Timestamp creactDate) {
		this.creactDate = creactDate;
	}

	@Column(name = "OPEN_TIME")
	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	@Column(name = "down_load")
	public Integer getDownLoad() {
		return downLoad;
	}

	public void setDownLoad(Integer downLoad) {
		this.downLoad = downLoad;
	}

	@Column(name = "trust_device_count")
	public Integer getTrustDeviceCount() {
		return trustDeviceCount;
	}

	public void setTrustDeviceCount(Integer trustDeviceCount) {
		this.trustDeviceCount = trustDeviceCount;
	}

	@Column(name = "deal_info_type3")
	public Integer getDealInfoType3() {
		return dealInfoType3;
	}

	public void setDealInfoType3(Integer dealInfoType3) {
		this.dealInfoType3 = dealInfoType3;
	}

	@Column(name = "svn")
	public String getSvn() {
		return svn;
	}

	public void setSvn(String svn) {
		this.svn = svn;
	}

	@Column(name = "can_settle")
	public Boolean getCanSettle() {
		return canSettle;
	}

	/**
	 * 能否结算，新建、更新业务
	 * 新增一定结算，更新是否结算由以下条件判断：①新增时在当前代理商合同范围内②新增时间到现在时间<代理商合同范围③付款方式不是政府采购和合同采购
	 * 
	 * @param canSettle
	 */
	public void setCanSettle(Boolean canSettle) {
		this.canSettle = canSettle;
	}

	@Column(name = "refuse")
	public String getRefuse() {
		return refuse;
	}

	public void setRefuse(String refuse) {
		this.refuse = refuse;
	}

	@Column(name = "man_made_damage")
	public Boolean getManMadeDamage() {
		return manMadeDamage == null ? false : manMadeDamage;
	}

	public void setManMadeDamage(Boolean manMadeDamage) {
		this.manMadeDamage = manMadeDamage;
	}

	@Column(name = "last_days")
	public Integer getLastDays() {
		return lastDays == null ? 0 : lastDays;
	}

	public void setLastDays(Integer lastDays) {
		this.lastDays = lastDays;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_company_his_id")
	public WorkCompanyHis getWorkCompanyHis() {
		return workCompanyHis;
	}

	public void setWorkCompanyHis(WorkCompanyHis workCompanyHis) {
		this.workCompanyHis = workCompanyHis;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_user_his_id")
	public WorkUserHis getWorkUserHis() {
		return workUserHis;
	}

	public void setWorkUserHis(WorkUserHis workUserHis) {
		this.workUserHis = workUserHis;
	}

	@Column(name = "user_sn")
	public String getUserSn() {
		return userSn;
	}

	public void setUserSn(String userSn) {
		this.userSn = userSn;
	}

	@Column(name = "prev_id")
	public Long getPrevId() {
		return prevId;
	}

	public void setPrevId(Long prevId) {
		this.prevId = prevId;
	}

	@Column(name = "physical_life")
	public Integer getPhysicalLife() {
		return physicalLife;
	}

	public void setPhysicalLife(Integer physicalLife) {
		this.physicalLife = physicalLife;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commercial_agent_id")
	public ConfigCommercialAgent getCommercialAgent() {
		return commercialAgent;
	}

	public void setCommercialAgent(ConfigCommercialAgent commercialAgent) {
		this.commercialAgent = commercialAgent;
	}

	@Column(name = "obtained_date")
	public Date getObtainedDate() {
		return obtainedDate;
	}

	public void setObtainedDate(Date obtainedDate) {
		this.obtainedDate = obtainedDate;
	}

	@Transient
	public Double getMoneyDouble() {
		return moneyDouble;
	}

	public void setMoneyDouble(Double moneyDouble) {
		this.moneyDouble = moneyDouble;
	}

	@Column(name = "pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@Column(name = "settled_life")
	public Integer getSettledLife() {
		return settledLife;
	}

	public void setSettledLife(Integer settledLife) {
		this.settledLife = settledLife;
	}

	@Column(name = "residual_life")
	public Integer getResidualLife() {
		return residualLife;
	}

	public void setResidualLife(Integer residualLife) {
		this.residualLife = residualLife;
	}

	/*
	 * @Column(name = "deal_info_office") public Integer getDealInfoOffice() {
	 * return dealInfoOffice; }
	 * 
	 * public void setDealInfoOffice(Integer dealInfoOffice) {
	 * this.dealInfoOffice = dealInfoOffice; }
	 */
	@Column(name = "notafter")
	public Date getNotafter() {
		return notafter;
	}

	public void setNotafter(Date notafter) {
		this.notafter = notafter;
	}

	@Column(name = "add_cert_days")
	public Integer getAddCertDays() {
		return addCertDays;
	}

	public void setAddCertDays(Integer addCertDays) {
		this.addCertDays = addCertDays;
	}

	@Column(name = "is_ixin")
	public Boolean getIsIxin() {
		return isIxin;
	}

	public void setIsIxin(Boolean isIxin) {
		this.isIxin = isIxin;
	}

	@Column(name = "config_agent_id")
	public Long getConfigChargeAgentId() {
		return configChargeAgentId;
	}

	public void setConfigChargeAgentId(Long configChargeAgentId) {
		this.configChargeAgentId = configChargeAgentId;
	}

	@Column(name = "is_sjqy")
	public Integer getIsSJQY() {
		return isSJQY;
	}

	public void setIsSJQY(Integer isSJQY) {
		this.isSJQY = isSJQY;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "input_user")
	public User getInputUser() {
		return inputUser;
	}

	public void setInputUser(User inputUser) {
		this.inputUser = inputUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pay_user")
	public User getPayUser() {
		return payUser;
	}

	public void setPayUser(User payUser) {
		this.payUser = payUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attestation_user")
	public User getAttestationUser() {
		return attestationUser;
	}

	public void setAttestationUser(User attestationUser) {
		this.attestationUser = attestationUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_card_user")
	public User getBusinessCardUser() {
		return businessCardUser;
	}

	public void setBusinessCardUser(User businessCardUser) {
		this.businessCardUser = businessCardUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "input_user_date")
	public Date getInputUserDate() {
		return inputUserDate;
	}

	public void setInputUserDate(Date inputUserDate) {
		this.inputUserDate = inputUserDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "pay_user_date")
	public Date getPayUserDate() {
		return payUserDate;
	}

	public void setPayUserDate(Date payUserDate) {
		this.payUserDate = payUserDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "attestation_user_date")
	public Date getAttestationUserDate() {
		return attestationUserDate;
	}

	public void setAttestationUserDate(Date attestationUserDate) {
		this.attestationUserDate = attestationUserDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "business_card_user_date")
	public Date getBusinessCardUserDate() {
		return businessCardUserDate;
	}

	public void setBusinessCardUserDate(Date businessCardUserDate) {
		this.businessCardUserDate = businessCardUserDate;
	}

	@Transient
	public List<PayableDetailVo> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PayableDetailVo> detailList) {
		this.detailList = detailList;
	}

	@Transient
	public Integer getYyNum() {
		return yyNum;
	}

	public void setYyNum(Integer yyNum) {
		this.yyNum = yyNum;
	}

	@Transient
	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	@Transient
	public Integer getLastNum() {
		return lastNum;
	}

	public void setLastNum(Integer lastNum) {
		this.lastNum = lastNum;
	}

	@Transient
	public Integer getWaitNum() {
		return waitNum;
	}

	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}

	@Column(name = "office_id")
	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	@Column(name = "area_id")
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "self_apply_id")
	public Long getSelfApplyId() {
		return selfApplyId;
	}

	public void setSelfApplyId(Long selfApplyId) {
		this.selfApplyId = selfApplyId;
	}

	@Column(name = "is_main_tain")
	public String getIsMainTain() {
		return IsMainTain;
	}

	public void setIsMainTain(String isMainTain) {
		IsMainTain = isMainTain;
	}

	private SelfImage selfImage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_Image")
	public SelfImage getSelfImage() {
		return selfImage;
	}

	public void setSelfImage(SelfImage selfImage) {
		this.selfImage = selfImage;
	}

	@Column(name = "FIRST_CERT_SN")
	public String getFirstCertSN() {
		return firstCertSN;
	}

	public void setFirstCertSN(String firstCertSN) {
		this.firstCertSN = firstCertSN;
	}

	@Column(name = "expiration_date")
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

}