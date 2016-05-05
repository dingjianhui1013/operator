package com.itrus.ca.modules.self.entity;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.work.entity.WorkCertInfo;

/**
 * SelfApplication entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SELF_APPLICATION")
public class SelfApplication implements java.io.Serializable {

    // Fields

    private Long id;
    private SelfApplication parent;
    private String appName;// 应用名称
    private String certificateType;//应用标识
    private String productName;// 产品名称
    private String applicationPeriod;// 申请年限
    private String companyName;// 单位名称
    private String companyType;// 单位类型
    private String companyTypeNumber;// 单位类型对应的号码
    private String companyNumber;// 机构代码
    private String securityNumber;// 社保编码
    private String securityAddress;// 参保区县
    private String managementProvince;// 地税管理部门所属省市
    private String managementDistrict;// 地税管理部门所属区县
    private String managementPlace;// 地税管理部门所属区域
    private String transactorName;// 代办人姓名
    private String transactorPhone;// 代办人电话
    private String transactorEmail;// 代办人邮箱
    private String transactorDocumentType;// 代办人证件类型
    private String transactorDocumentNumber;// 代办人证件号码
    private String receiverName;// 收件人姓名
    private String receiverPhone;// 收件人电话
    private String receiverAddress;// 收件人地址
    private String receiverNumber;// 收件人邮编
    private String receiverType;// 收件方式
    private String businessType;// 业务类型 新增 好时修改
    private String status;// 申请状态
    private String remarks;// 其他
    private String userPhone;
    private Date createdate;// 创建时间
    private Date updaredate;// 修改时间
    private String no;// 业务编号
    private String keySn;// key序列号
    private WorkCertInfo workCertInfo;
    private Set<SelfImage> selfImages = new HashSet<SelfImage>(0);
    private String denyText;// 拒绝原因
    private Double money;// 缴费金额
    private String isMaintain;
    private Long dealId;


    // Constructors

    /** default constructor */
    public SelfApplication() {
    }

    /** full constructor */
    public SelfApplication(SelfApplication parent, String appName,
    		 String certificateType, String productName,
            String applicationPeriod, String companyName, String companyType,
            String companyTypeNumber, String companyNumber,
            String securityNumber, String securityAddress,
            String managementProvince, String managementDistrict,
            String managementPlace, String transactorName,
            String transactorPhone, String transactorEmail,
            String transactorDocumentType, String transactorDocumentNumber,
            String receiverName, String receiverPhone, String receiverAddress,
            String receiverNumber, String receiverType, String businessType,
            String status, String remarks, String userPhone, Date createdate,
            Date updaredate, String no, String keySn,
            WorkCertInfo workCertInfo, Set<SelfImage> selfImages,
            String denyText, Double money, String isMaintain,Long dealId) {
        super();
        this.parent = parent;
        this.appName = appName;
        this.productName = productName;
        this.certificateType = certificateType;
        this.applicationPeriod = applicationPeriod;
        this.companyName = companyName;
        this.companyType = companyType;
        this.companyTypeNumber = companyTypeNumber;
        this.companyNumber = companyNumber;
        this.securityNumber = securityNumber;
        this.securityAddress = securityAddress;
        this.managementProvince = managementProvince;
        this.managementDistrict = managementDistrict;
        this.managementPlace = managementPlace;
        this.transactorName = transactorName;
        this.transactorPhone = transactorPhone;
        this.transactorEmail = transactorEmail;
        this.transactorDocumentType = transactorDocumentType;
        this.transactorDocumentNumber = transactorDocumentNumber;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.receiverNumber = receiverNumber;
        this.receiverType = receiverType;
        this.businessType = businessType;
        this.status = status;
        this.remarks = remarks;
        this.userPhone = userPhone;
        this.createdate = createdate;
        this.updaredate = updaredate;
        this.no = no;
        this.keySn = keySn;
        this.workCertInfo = workCertInfo;
        this.selfImages = selfImages;
        this.denyText = denyText;
        this.money = money;
        this.isMaintain = isMaintain;
        this.dealId = dealId;
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
    @JoinColumn(name = "parent_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public SelfApplication getParent() {
        return parent;
    }

    public void setParent(SelfApplication parent) {
        this.parent = parent;
    }

    @Column(name = "APP_NAME", length = 510)
    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Column(name = "CERTIFICATE_TYPE", length = 4)
    public String getCertificateType() {
        return this.certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    @Column(name = "PRODUCT_NAME", length = 4)
    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "APPLICATION_PERIOD", length = 510)
    public String getApplicationPeriod() {
        return this.applicationPeriod;
    }

    public void setApplicationPeriod(String applicationPeriod) {
        this.applicationPeriod = applicationPeriod;
    }

    @Column(name = "COMPANY_NAME", length = 510)
    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Column(name = "COMPANY_TYPE", length = 4)
    public String getCompanyType() {
        return this.companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    @Column(name = "COMPANY_NUMBER", length = 510)
    public String getCompanyNumber() {
        return this.companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    @Column(name = "SECURITY_NUMBER", length = 510)
    public String getSecurityNumber() {
        return this.securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    @Column(name = "SECURITY_ADDRESS", length = 510)
    public String getSecurityAddress() {
        return this.securityAddress;
    }

    public void setSecurityAddress(String securityAddress) {
        this.securityAddress = securityAddress;
    }

    @Column(name = "MANAGEMENT_PROVINCE", length = 510)
    public String getManagementProvince() {
        return this.managementProvince;
    }

    public void setManagementProvince(String managementProvince) {
        this.managementProvince = managementProvince;
    }

    @Column(name = "TRANSACTOR_NAME", length = 510)
    public String getTransactorName() {
        return this.transactorName;
    }

    public void setTransactorName(String transactorName) {
        this.transactorName = transactorName;
    }

    @Column(name = "TRANSACTOR_PHONE", length = 60)
    public String getTransactorPhone() {
        return this.transactorPhone;
    }

    public void setTransactorPhone(String transactorPhone) {
        this.transactorPhone = transactorPhone;
    }

    @Column(name = "TRANSACTOR_EMAIL", length = 510)
    public String getTransactorEmail() {
        return this.transactorEmail;
    }

    public void setTransactorEmail(String transactorEmail) {
        this.transactorEmail = transactorEmail;
    }

    @Column(name = "TRANSACTOR_DOCUMENT_TYPE", length = 510)
    public String getTransactorDocumentType() {
        return this.transactorDocumentType;
    }

    public void setTransactorDocumentType(String transactorDocumentType) {
        this.transactorDocumentType = transactorDocumentType;
    }

    @Column(name = "TRANSACTOR_DOCUMENT_NUMBER", length = 510)
    public String getTransactorDocumentNumber() {
        return this.transactorDocumentNumber;
    }

    public void setTransactorDocumentNumber(String transactorDocumentNumber) {
        this.transactorDocumentNumber = transactorDocumentNumber;
    }

    @Column(name = "STATUS", length = 510)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "REMARKS", length = 510)
    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(name = "USER_PHONE", length = 40)
    public String getUserPhone() {
        return this.userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDATE", length = 7)
    public Date getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "UPDAREDATE", length = 7)
    public Date getUpdaredate() {
        return this.updaredate;
    }

    public void setUpdaredate(Date updaredate) {
        this.updaredate = updaredate;
    }

    @Column(name = "COMPANY_TYPE_NUMBER", length = 40)
    public String getCompanyTypeNumber() {
        return this.companyTypeNumber;
    }

    public void setCompanyTypeNumber(String companyTypeNumber) {
        this.companyTypeNumber = companyTypeNumber;
    }

    @Column(name = "RECEIVER_NAME", length = 510)
    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @Column(name = "RECEIVER_PHONE", length = 40)
    public String getReceiverPhone() {
        return this.receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    @Column(name = "RECEIVER_ADDRESS", length = 510)
    public String getReceiverAddress() {
        return this.receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    @Column(name = "RECEIVER_NUMBER", length = 40)
    public String getReceiverNumber() {
        return this.receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    @Column(name = "BUSINESS_TYPE", length = 4)
    public String getBusinessType() {
        return this.businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Column(name = "MANAGEMENT_DISTRICT", length = 510)
    public String getManagementDistrict() {
        return this.managementDistrict;
    }

    public void setManagementDistrict(String managementDistrict) {
        this.managementDistrict = managementDistrict;
    }

    @Column(name = "MANAGEMENT_PLACE", length = 510)
    public String getManagementPlace() {
        return this.managementPlace;
    }

    public void setManagementPlace(String managementPlace) {
        this.managementPlace = managementPlace;
    }

    @Column(name = "NO", length = 510)
    public String getNo() {
        return this.no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "selfApplication")
    public Set<SelfImage> getSelfImages() {
        return this.selfImages;
    }

    public void setSelfImages(Set<SelfImage> selfImages) {
        this.selfImages = selfImages;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getKeySn() {
        return keySn;
    }

    public void setKeySn(String keySn) {
        this.keySn = keySn;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cert_id")
    public WorkCertInfo getWorkCertInfo() {
        return workCertInfo;
    }

    public void setWorkCertInfo(WorkCertInfo workCertInfo) {
        this.workCertInfo = workCertInfo;
    }

    public String getDenyText() {
        return denyText;
    }

    public void setDenyText(String denyText) {
        this.denyText = denyText;
    }

    @Column(name = "money", precision = 20, scale = 3)
    public Double getMoney() {
        return this.money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getIsMaintain() {
        return isMaintain;
    }

    public void setIsMaintain(String isMaintain) {
        this.isMaintain = isMaintain;
    }

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }
    
}