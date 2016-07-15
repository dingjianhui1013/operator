package com.itrus.ca.modules.signature.entity;

import java.util.Date;


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
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * SignatureInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "signature_info")
public class SignatureInfo extends DataEntity  implements java.io.Serializable {

	
	private static final long serialVersionUID = 3998517165221533641L;
	// Fields
	private Long id;
	private WorkDealInfo workDealInfo;//关联业务
	private SignatureConfigChargeAgent signatureAgent;//计费策略模版
	private String signatureInfoType;//印章业务类型
	private String signatureType;//印章类型
	private String signatureAppName;//印章产品名称
	private String year;//印章年限
	private String svn; //印章业务编号
	private WorkCertInfo workCertInfo;//证书信息
	private WorkCertInfo oldWorkCertInfo;//老的证书信息
	private String signatureInfoStatus;      //业务状态
	private String status;     				 //印章状态
	
	private Date startDate;                  //开始时间
	private Date endDate;                    //结束时间
	private User enterUser;//录入人
	private Date enterDate;//录入时间
	private User manageUser;//办理人
	private Date manageDate;//办理时间
	

	private ConfigApp configApp;     //应用
	private Long prevId;             //上笔业务的id
	private Long firstId;            //业务链首条id
	
	private Long officeId;//网点
	private Long areaId;//区域
	
	

	
	public SignatureInfo(){}
	
	public SignatureInfo(WorkDealInfo workDealInfo, SignatureConfigChargeAgent signatureAgent,
			String signatureInfoType, String signatureType, String signatureAppName, String year, String svn,
			WorkCertInfo workCertInfo, WorkCertInfo oldWorkCertInfo, String signatureInfoStatus, String status,
			Date startDate, Date endDate, User enterUser, Date enterDate, User manageUser, Date manageDate,
			ConfigApp configApp, Long prevId, Long firstId, Long officeId, Long areaId) {
		
		this.workDealInfo = workDealInfo;
		this.signatureAgent = signatureAgent;
		this.signatureInfoType = signatureInfoType;
		this.signatureType = signatureType;
		this.signatureAppName = signatureAppName;
		this.year = year;
		this.svn = svn;
		this.workCertInfo = workCertInfo;
		this.oldWorkCertInfo = oldWorkCertInfo;
		this.signatureInfoStatus = signatureInfoStatus;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.enterUser = enterUser;
		this.enterDate = enterDate;
		this.manageUser = manageUser;
		this.manageDate = manageDate;
		this.configApp = configApp;
		this.prevId = prevId;
		this.firstId = firstId;
		this.officeId = officeId;
		this.areaId = areaId;
		
	}


//	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@SequenceGenerator(name="SIGNATURE_INFO_SEQUENCE",allocationSize=1,initialValue=1,sequenceName="SIGNATURE_INFO_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "SIGNATURE_INFO_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deal_info_id")
	public WorkDealInfo getWorkDealInfo() {
		return workDealInfo;
	}
	public void setWorkDealInfo(WorkDealInfo workDealInfo) {
		this.workDealInfo = workDealInfo;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "signature_agent_id")
	public SignatureConfigChargeAgent getSignatureAgent() {
		return signatureAgent;
	}
	public void setSignatureAgent(SignatureConfigChargeAgent signatureAgent) {
		this.signatureAgent = signatureAgent;
	}
	
	@Column(name = "signature_info_type")
	public String getSignatureInfoType() {
		return signatureInfoType;
	}
	public void setSignatureInfoType(String signatureInfoType) {
		this.signatureInfoType = signatureInfoType;
	}
	
	@Column(name = "signature_type")
	public String getSignatureType() {
		return signatureType;
	}
	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}
	
	@Column(name = "signature_app_name")
	public String getSignatureAppName() {
		return signatureAppName;
	}
	public void setSignatureAppName(String signatureAppName) {
		this.signatureAppName = signatureAppName;
	}
	
	@Column(name = "year")
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	@Column(name = "svn")
	public String getSvn() {
		return svn;
	}
	public void setSvn(String svn) {
		this.svn = svn;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cert_id")
	public WorkCertInfo getWorkCertInfo() {
		return workCertInfo;
	}
	public void setWorkCertInfo(WorkCertInfo workCertInfo) {
		this.workCertInfo = workCertInfo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cert_old_id")
	public WorkCertInfo getOldWorkCertInfo() {
		return oldWorkCertInfo;
	}
	public void setOldWorkCertInfo(WorkCertInfo oldWorkCertInfo) {
		this.oldWorkCertInfo = oldWorkCertInfo;
	}
	
	@Column(name = "signature_info_status")
	public String getSignatureInfoStatus() {
		return signatureInfoStatus;
	}
	public void setSignatureInfoStatus(String signatureInfoStatus) {
		this.signatureInfoStatus = signatureInfoStatus;
	}
	
	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enter_user")
	public User getEnterUser() {
		return enterUser;
	}
	public void setEnterUser(User enterUser) {
		this.enterUser = enterUser;
	}
	
	@Column(name = "enter_date")
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manage_user")
	public User getManageUser() {
		return manageUser;
	}
	public void setManageUser(User manageUser) {
		this.manageUser = manageUser;
	}
	@Column(name = "manage_date")
	public Date getManageDate() {
		return manageDate;
	}
	public void setManageDate(Date manageDate) {
		this.manageDate = manageDate;
	}
	
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_id")
	public ConfigApp getConfigApp() {
		return configApp;
	}
	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}

	@Column(name = "prev_id")
	public Long getPrevId() {
		return prevId;
	}

	public void setPrevId(Long prevId) {
		this.prevId = prevId;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
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

	@Column(name = "first_id")
	public Long getFirstId() {
		return firstId;
	}


	public void setFirstId(Long firstId) {
		this.firstId = firstId;
	}



    
	
	
	
}