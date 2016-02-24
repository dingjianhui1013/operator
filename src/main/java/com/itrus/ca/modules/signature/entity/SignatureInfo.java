package com.itrus.ca.modules.signature.entity;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;

/**
 * SignatureInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "signature_info")
public class SignatureInfo  implements java.io.Serializable {

	
	private static final long serialVersionUID = 3998517165221533641L;
	// Fields

	
	private Long id;
	private WorkCertInfo workCertInfo;
    private ConfigApp configApp;             //应用
    
    private ConfigProduct configProduct;     //产品
    
	private Integer year;                    //印章年限
	
	private String svn;                      //业务编号
	
	private User applyUser;                  //申请人
	
	private WorkCompany workCompany;         //单位
	
	private Date startDate;                  //开始时间
	
	private Date endDate;                    //结束时间
	
	private String signatureInfoStatus;      //业务状态
	
	private Date createDate;                 //创建日期
	
	
	/** default constructor */
	public SignatureInfo() {
	}

	

	
	@SequenceGenerator(name="COMMON_SEQUENCE",sequenceName="COMMON_SEQUENCE")
	@Id 
	@GeneratedValue(generator = "COMMON_SEQUENCE",strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@JoinColumn(name="cert_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="证书不能为空")
	public WorkCertInfo getWorkCertInfo() {
		return workCertInfo;
	}

	public void setWorkCertInfo(WorkCertInfo workCertInfo) {
		this.workCertInfo = workCertInfo;
	}


	

	@JoinColumn(name="app_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	@NotNull(message="应用不能为空")
	public ConfigApp getConfigApp() {
		return configApp;
	}

	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}



	@JoinColumn(name="product_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore	
	public ConfigProduct getConfigProduct() {
		return configProduct;
	}

	public void setConfigProduct(ConfigProduct configProduct) {
		this.configProduct = configProduct;
	}



	@Column(name = "year")
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}



	@Column(name = "svn")
	public String getSvn() {
		return svn;
	}

	public void setSvn(String svn) {
		this.svn = svn;
	}



	@JoinColumn(name = "user_id")
	public User getApplyUser() {
		return applyUser;
	}




	public void setApplyUser(User applyUser) {
		this.applyUser = applyUser;
	}



	@JoinColumn(name = "company_id")
	public WorkCompany getWorkCompany() {
		return workCompany;
	}




	public void setWorkCompany(WorkCompany workCompany) {
		this.workCompany = workCompany;
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



	@Column(name = "signature_info_status")
	public String getSignatureInfoStatus() {
		return signatureInfoStatus;
	}

	public void setSignatureInfoStatus(String signatureInfoStatus) {
		this.signatureInfoStatus = signatureInfoStatus;
	}




	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}