package com.itrus.ca.modules.self.entity;



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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SelfImage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SELF_IMAGE", schema = "OPERATOR_SC")
public class SelfImage implements java.io.Serializable {

    // Fields

    private Long id;
    private SelfApplication selfApplication;
    private String companyImage;
    private String transactorImage;
    private Date createdate;// 创建时间
    private String no;
    private String status;

    // Constructors

    /** default constructor */
    public SelfImage() {
    }

    /** full constructor */
    public SelfImage(SelfApplication selfApplication, String companyImage,
            String transactorImage, String no, Date createdate, String status) {
        this.selfApplication = selfApplication;
        this.companyImage = companyImage;
        this.transactorImage = transactorImage;
        this.createdate = createdate;
        this.no = no;
        this.status = status;
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
    @JoinColumn(name = "APPLICATION_ID")
    public SelfApplication getSelfApplication() {
        return this.selfApplication;
    }

    public void setSelfApplication(SelfApplication selfApplication) {
        this.selfApplication = selfApplication;
    }

    @Column(name = "COMPANY_IMAGE", length = 510)
    public String getCompanyImage() {
        return this.companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    @Column(name = "TRANSACTOR_IMAGE", length = 510)
    public String getTransactorImage() {
        return this.transactorImage;
    }

    public void setTransactorImage(String transactorImage) {
        this.transactorImage = transactorImage;
    }

    @Column(name = "NO", length = 510)
    public String getNo() {
        return this.no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDATE", length = 7)
    public Date getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}