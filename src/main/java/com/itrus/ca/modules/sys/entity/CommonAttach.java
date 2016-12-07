package com.itrus.ca.modules.sys.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * CommonAttach entity. @author MyEclipse Persistence Tools
 */
/**
 * 附件表，可以存储所有业务表的附件，只要业务表中有groupId字段和本表中的groupid对应就行 groupId一定要用UUID生成
 * 
 * @author ZhangJingtao
 *
 */
@Entity
@Table(name = "common_attach")
public class CommonAttach implements java.io.Serializable {

	// Fields

	private Long id;
	private String attachName;         //名称
	private Long contentId;
	private String oldFileName;
	private String path;               //路径
	private String realPath;           //真实路径
	private String remark;
	private Integer status;            //-1 图片失效
	private String suffix;
	private String uploadCount;
	private Integer uploadModel;
	private String uploadUserName;
	private String uploadUserOrgName;
	private Integer uploadUserOrgid;
	private Integer uploadUserid;
	private Timestamp uploadTime;      //上传时间
	private String groupid;   
	
	private WorkDealInfo workDealInfo;     //对应的业务数据

	// Constructors

	/** default constructor */
	public CommonAttach() {
	}
	public CommonAttach(CommonAttach commonAttach) {
		this.attachName = commonAttach.getAttachName();
		this.realPath = commonAttach.getRealPath();
		this.uploadTime = commonAttach.getUploadTime();
		this.contentId = commonAttach.getContentId();
		this.oldFileName = commonAttach.getOldFileName();
		this.path = commonAttach.getPath();
		this.remark = commonAttach.getRemark();
		this.status = commonAttach.getStatus();
		this.suffix = commonAttach.getSuffix();
		this.uploadCount = commonAttach.getUploadCount();
		this.uploadModel = commonAttach.getUploadModel();
		this.uploadUserName = commonAttach.getUploadUserName();
		this.uploadUserOrgName = commonAttach.getUploadUserOrgName();
		this.uploadUserOrgid = commonAttach.getUploadUserOrgid();
		this.uploadUserid = commonAttach.getUploadUserid();
		this.groupid = commonAttach.getGroupid();
	}
	
	
	

	public CommonAttach(String attachName, String realPath, Timestamp uploadTime) {
		super();
		this.attachName = attachName;
		this.realPath = realPath;
		this.uploadTime = uploadTime;
	}





	/** full constructor */
	public CommonAttach(String attachName, Long contentId, String oldFileName,
			String path, String realPath, String remark, Integer status,
			String suffix, String uploadCount, Integer uploadModel,
			String uploadUserName, String uploadUserOrgName,
			Integer uploadUserOrgid, Integer uploadUserid,
			Timestamp uploadTime, String groupid) {
		this.attachName = attachName;
		this.contentId = contentId;
		this.oldFileName = oldFileName;
		this.path = path;
		this.realPath = realPath;
		this.remark = remark;
		this.status = status;
		this.suffix = suffix;
		this.uploadCount = uploadCount;
		this.uploadModel = uploadModel;
		this.uploadUserName = uploadUserName;
		this.uploadUserOrgName = uploadUserOrgName;
		this.uploadUserOrgid = uploadUserOrgid;
		this.uploadUserid = uploadUserid;
		this.uploadTime = uploadTime;
		this.groupid = groupid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMON_ATTACH_SEQUENCE")
	// @SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName =
	// "COMMON_SEQUENCE")
	@SequenceGenerator(name = "COMMON_ATTACH_SEQUENCE", allocationSize = 1, initialValue = 1, sequenceName = "COMMON_ATTACH_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "attach_name", columnDefinition = "NVARCHAR2(255)")
	public String getAttachName() {
		return this.attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	@Column(name = "content_id")
	public Long getContentId() {
		return this.contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Column(name = "oldFileName",columnDefinition = "NVARCHAR2(255)")
	public String getOldFileName() {
		return this.oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	@Column(name = "path",columnDefinition = "NVARCHAR2(255)")
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "realPath",columnDefinition = "NVARCHAR2(255)")
	public String getRealPath() {
		return this.realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	@Column(name = "remark",columnDefinition = "NVARCHAR2(255)")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "suffix",columnDefinition = "NVARCHAR2(255)")
	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "upload_count",columnDefinition = "NVARCHAR2(255)")
	public String getUploadCount() {
		return this.uploadCount;
	}

	public void setUploadCount(String uploadCount) {
		this.uploadCount = uploadCount;
	}

	@Column(name = "upload_model")
	public Integer getUploadModel() {
		return this.uploadModel;
	}

	public void setUploadModel(Integer uploadModel) {
		this.uploadModel = uploadModel;
	}

	@Column(name = "upload_user_name",columnDefinition = "NVARCHAR2(255)")
	public String getUploadUserName() {
		return this.uploadUserName;
	}

	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}

	@Column(name = "upload_user_org_name",columnDefinition = "NVARCHAR2(255)")
	public String getUploadUserOrgName() {
		return this.uploadUserOrgName;
	}

	public void setUploadUserOrgName(String uploadUserOrgName) {
		this.uploadUserOrgName = uploadUserOrgName;
	}

	@Column(name = "upload_user_orgid")
	public Integer getUploadUserOrgid() {
		return this.uploadUserOrgid;
	}

	public void setUploadUserOrgid(Integer uploadUserOrgid) {
		this.uploadUserOrgid = uploadUserOrgid;
	}

	@Column(name = "upload_userid")
	public Integer getUploadUserid() {
		return this.uploadUserid;
	}

	public void setUploadUserid(Integer uploadUserid) {
		this.uploadUserid = uploadUserid;
	}

	@Column(name = "upload_time", length = 19)
	public Timestamp getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

	@Column(name = "groupid",columnDefinition = "NVARCHAR2(255)")
	public String getGroupid() {
		return this.groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}




    @ManyToOne
	public WorkDealInfo getWorkDealInfo() {
		return workDealInfo;
	}





	public void setWorkDealInfo(WorkDealInfo workDealInfo) {
		this.workDealInfo = workDealInfo;
	}
	
	
	

}