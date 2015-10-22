package com.itrus.ca.modules.key.entity;

import java.util.ArrayList;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.mapping.Array;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkLog;

/**
 * key仓库信息
 */
@Entity
@Table(name = "key_usb_key_depot")
public class KeyUsbKeyDepot extends DataEntity implements java.io.Serializable {

	// Fields
	private Long id;
	private String depotName;
	private Long manufacturerId;
	private Long keyTypeId;
	private String linkmanName;
	private String linkmanCode;
	private Integer linkmanSex;
	private String linkmanOrg;
	private String linkmanMobilePhone;
	private String linkmanEmail;
	private String linkmanLocation;
	private String linkmanOffical;
	private String linkmanPost;
	private String linkmanTel;
	private Long amountTotal;
	private Long amountValid;
	private Long amountDelivery;
	private Office office;
	private List<KeyDepotGeneralStatistics> keyDepotGeneralStatisticsList;
	private Integer totolCount; //上期结余
	private List<Integer> inCounts; 
	private List<Integer> outCounts; 
	private List<Integer> startCounts; 
	private List<Integer> endCounts; 
	private Integer totalEndCount;//本期结余
	private Integer afterIn;
	private Date startDate;
	private Date endDate;
	private int  isReset;
	private int inCount;
	private int outCount;
	private String warningName;//预警人姓名
	private String warningEmail;//预警人邮箱
	
	
	private String totolName;
	
	// Constructors

	/** default constructor */
	public KeyUsbKeyDepot() {
	}

	/** full constructor */
	public KeyUsbKeyDepot(String depotName, Long manufacturerId,
			Long keyTypeId, String linkmanName, String linkmanCode,
			Integer linkmanSex, String linkmanOrg, String linkmanMobilePhone,
			String linkmanEmail, String linkmanLocation, String linkmanOffical,
			String linkmanPost, String linkmanTel, Long amountTotal,
			Long amountValid, Long amountDelivery,Office office,String company) {
		this.depotName = depotName;
		this.manufacturerId = manufacturerId;
		this.keyTypeId = keyTypeId;
		this.linkmanName = linkmanName;
		this.linkmanCode = linkmanCode;
		this.linkmanSex = linkmanSex;
		this.linkmanOrg = linkmanOrg;
		this.linkmanMobilePhone = linkmanMobilePhone;
		this.linkmanEmail = linkmanEmail;
		this.linkmanLocation = linkmanLocation;
		this.linkmanOffical = linkmanOffical;
		this.linkmanPost = linkmanPost;
		this.linkmanTel = linkmanTel;
		this.amountTotal = amountTotal;
		this.amountValid = amountValid;
		this.amountDelivery = amountDelivery;
		this.office = office;
		
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

	@Column(name = "depot_name")
	public String getDepotName() {
		return this.depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	@Column(name = "manufacturer_id")
	public Long getManufacturerId() {
		return this.manufacturerId;
	}

	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	@Column(name = "key_type_id")
	public Long getKeyTypeId() {
		return this.keyTypeId;
	}

	public void setKeyTypeId(Long keyTypeId) {
		this.keyTypeId = keyTypeId;
	}

	@Column(name = "linkman_name")
	public String getLinkmanName() {
		return this.linkmanName;
	}

	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}

	@Column(name = "linkman_code")
	public String getLinkmanCode() {
		return this.linkmanCode;
	}

	public void setLinkmanCode(String linkmanCode) {
		this.linkmanCode = linkmanCode;
	}
	@Column(name = "linkman_sex")
	public Integer getLinkmanSex() {
		return this.linkmanSex;
	}

	public void setLinkmanSex(Integer linkmanSex) {
		this.linkmanSex = linkmanSex;
	}

	@Column(name = "linkman_org")
	public String getLinkmanOrg() {
		return this.linkmanOrg;
	}

	public void setLinkmanOrg(String linkmanOrg) {
		this.linkmanOrg = linkmanOrg;
	}

	@Column(name = "linkman_mobile_phone")
	public String getLinkmanMobilePhone() {
		return this.linkmanMobilePhone;
	}

	public void setLinkmanMobilePhone(String linkmanMobilePhone) {
		this.linkmanMobilePhone = linkmanMobilePhone;
	}

	@Column(name = "linkman_email")
	public String getLinkmanEmail() {
		return this.linkmanEmail;
	}

	public void setLinkmanEmail(String linkmanEmail) {
		this.linkmanEmail = linkmanEmail;
	}

	@Column(name = "linkman_location")
	public String getLinkmanLocation() {
		return this.linkmanLocation;
	}

	public void setLinkmanLocation(String linkmanLocation) {
		this.linkmanLocation = linkmanLocation;
	}

	@Column(name = "linkman_offical")
	public String getLinkmanOffical() {
		return this.linkmanOffical;
	}

	public void setLinkmanOffical(String linkmanOffical) {
		this.linkmanOffical = linkmanOffical;
	}

	@Column(name = "linkman_post", length = 10)
	public String getLinkmanPost() {
		return this.linkmanPost;
	}

	public void setLinkmanPost(String linkmanPost) {
		this.linkmanPost = linkmanPost;
	}

	@Column(name = "linkman_tel")
	public String getLinkmanTel() {
		return this.linkmanTel;
	}

	public void setLinkmanTel(String linkmanTel) {
		this.linkmanTel = linkmanTel;
	}

	@Column(name = "warning_name", length= 20)
	public String getWarningName() {
		return warningName;
	}
	
	public void setWarningName(String warningName) {
		this.warningName = warningName;
	}
	@Column(name = "warning_email")
	public String getWarningEmail() {
		return warningEmail;
	}

	public void setWarningEmail(String warningEmail) {
		this.warningEmail = warningEmail;
	}

	@Column(name = "amount_total")
	public Long getAmountTotal() {
		return this.amountTotal;
	}

	public void setAmountTotal(Long amountTotal) {
		this.amountTotal = amountTotal;
	}

	@Column(name = "amount_valid")
	public Long getAmountValid() {
		return this.amountValid;
	}

	public void setAmountValid(Long amountValid) {
		this.amountValid = amountValid;
	}

	@Column(name = "amount_delivery")
	public Long getAmountDelivery() {
		return this.amountDelivery;
	}

	public void setAmountDelivery(Long amountDelivery) {
		this.amountDelivery = amountDelivery;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}


	
	@Transient
	public Integer getTotolCount() {
		return totolCount;
	}

	public void setTotolCount(Integer totolCount) {
		this.totolCount = totolCount;
	}
	
	@Transient
	public List<Integer> getInCounts() {
		return inCounts;
	}

	public void setInCounts(List<Integer> inCounts) {
		this.inCounts = inCounts;
	}
	
	@Transient
	public List<Integer> getOutCounts() {
		return outCounts;
	}

	public void setOutCounts(List<Integer> outCounts) {
		this.outCounts = outCounts;
	}
	
	@Transient
	public List<Integer> getStartCounts() {
		return startCounts;
	}

	public void setStartCounts(List<Integer> startCounts) {
		this.startCounts = startCounts;
	}
	
	@Transient
	public List<Integer> getEndCounts() {
		return endCounts;
	}

	public void setEndCounts(List<Integer> endCounts) {
		this.endCounts = endCounts;
	}

	@Transient
	public List<KeyDepotGeneralStatistics> getKeyDepotGeneralStatisticsList() {
		return keyDepotGeneralStatisticsList;
	}

	public void setKeyDepotGeneralStatisticsList(
			List<KeyDepotGeneralStatistics> keyDepotGeneralStatisticsList) {
		this.keyDepotGeneralStatisticsList = keyDepotGeneralStatisticsList;
	}

	@Transient
	public Integer getAfterIn() {
		return afterIn;
	}

	public void setAfterIn(Integer afterIn) {
		this.afterIn = afterIn;
	}
	
	@Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
	@Transient
	public Integer getTotalEndCount() {
		return totalEndCount;
	}

	public void setTotalEndCount(Integer totalEndCount) {
		this.totalEndCount = totalEndCount;
	}
	
	@Transient
	public int getIsReset() {
		return isReset;
	}

	public void setIsReset(int isReset) {
		this.isReset = isReset;
	}

	@Transient
	public int getInCount() {
		return inCount;
	}

	public void setInCount(int inCount) {
		this.inCount = inCount;
	}

	@Transient
	public int getOutCount() {
		return outCount;
	}

	public void setOutCount(int outCount) {
		this.outCount = outCount;
	}

	@Transient
	public String getTotolName() {
		return totolName;
	}

	public void setTotolName(String totolName) {
		this.totolName = totolName;
	}

	public boolean canDel(){
		List<Long> depotIds = new ArrayList<Long>();
		depotIds.add(KeyDepotId.MAIN_DEPOT_ID);
		depotIds.add(KeyDepotId.BAD_DEPOT_ID);
		return !depotIds.contains(this.id);
	}

	public boolean canDelTotal(){
		List<Long> depotIds = new ArrayList<Long>();
		depotIds.add(KeyDepotId.MAIN_DEPOT_ID);
		return depotIds.contains(this.id);
	}
	public boolean canDelTotalBad(){
		List<Long> depotIds = new ArrayList<Long>();
		depotIds.add(KeyDepotId.BAD_DEPOT_ID);
		return depotIds.contains(this.id);
	}
	
	
}