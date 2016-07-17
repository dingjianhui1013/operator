/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.ixin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkUser;

/**
 * 短信配置Entity
 * 
 * @author qt
 * @version 2015-11-27
 */
@Entity
@Table(name = "IXIN_DATA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IxinData implements java.io.Serializable{

    private static final long serialVersionUID = 6803990965287582259L;
    
	private Long id; // 编号
	private String keySn;//key序列号
	private String certSn;//证书 序列号
	private String ip;//客户端ip
	private String cpuId;//机器码
	private String iXinVersion;//I信版本
	private String osVersion;//操作系统版本
	private String iEVersion;//IE版本
	private Date accessTime;//时间
	private String accessType;//类型  0-插入  1-签名
	private ConfigApp configApp;
	protected Date createDate;// 创建日期
    protected String remarks;    // 备注
    protected Date updateDate;// 更新日期
    protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	
    public IxinData() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public IxinData(String keySn, String certSn, String ip, String cpuId, String iXinVersion, String osVersion, String iEVersion, Date accessTime, ConfigApp configApp,String accessType) {
        super();
        this.keySn = keySn;
        this.certSn = certSn;
        this.ip = ip;
        this.cpuId = cpuId;
        this.iXinVersion = iXinVersion;
        this.osVersion = osVersion;
        this.iEVersion = iEVersion;
        this.accessTime = accessTime;
        this.configApp = configApp;
        this.accessType = accessType;
    }


    @SequenceGenerator(name = "COMMON_SEQUENCE", sequenceName = "COMMON_SEQUENCE")
    @Id
    @GeneratedValue(generator = "COMMON_SEQUENCE", strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name = "key_sn")
    public String getKeySn() {
        return keySn;
    }
    public void setKeySn(String keySn) {
        this.keySn = keySn;
    }
    
    @Column(name = "cert_sn")
    public String getCertSn() {
        return certSn;
    }
    public void setCertSn(String certSn) {
        this.certSn = certSn;
    }
    
    @Column(name = "ip")
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    @Column(name = "cpu_id")
    public String getCpuId() {
        return cpuId;
    }
    public void setCpuId(String cpuId) {
        this.cpuId = cpuId;
    }
    
    @Column(name = "IXIN_VERSION")
    public String getiXinVersion() {
        return iXinVersion;
    }
    public void setiXinVersion(String iXinVersion) {
        this.iXinVersion = iXinVersion;
    }
    
    @Column(name = "OS_VERSION")
    public String getOsVersion() {
        return osVersion;
    }
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
    
    @Column(name = "IE_VERSION")
    public String getIEVersion() {
        return iEVersion;
    }
    public void setIEVersion(String iEVersion) {
        this.iEVersion = iEVersion;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    public ConfigApp getConfigApp() {
        return configApp;
    }
    
    public void setConfigApp(ConfigApp configApp) {
        this.configApp = configApp;
    }
    
    @Column(name = "ACCESS_TIME")
    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    @Column(name = "ACCESS_TYPE")
    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

   
    
    @Length(min=0, max=255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Length(min=1, max=1)
    @Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

	
}
