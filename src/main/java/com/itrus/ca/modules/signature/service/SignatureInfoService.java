/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;







import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;







import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.constant.SignatureInfoStatus;
import com.itrus.ca.modules.constant.SignatureInfoType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.signature.dao.SignatureInfoDao;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgent;
import com.itrus.ca.modules.signature.entity.SignatureInfo;

import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.dao.WorkCertInfoDao;

/**
 * 证书信息Service
 * 
 * @author ZhangJingtao
 * @version 2014-06-16
 */
@Component
@Transactional(readOnly = true)
public class SignatureInfoService extends BaseService {

	

	@Autowired
	private WorkCertInfoDao workCertInfoDao;

	@Autowired
	private SignatureInfoDao signatureInfoDao;
	
	
	
	public SignatureInfo get(Long id) {
		return signatureInfoDao.findOne(id);
	}
	
	

	/*
	 * 
	 * 此方法是为了兼容之前的对create Date 数据处理不够来临时增加的update方法，正常后，直接可以通过 workCertInfo.getCreateDate获取。
	 * 
	 */
	public Date getCreateDate(Long id) {
		WorkCertInfo wci = workCertInfoDao.findOne(id);
		if (wci == null) { // 证书不存在
			return null;
		} else if (wci.getCreateDate() != null) { // 有create Date 直接同步
			return wci.getCreateDate();
		} else if (wci.getRenewalPrevId() != null) { // create Date 为空且
														// 证书存在之前的证书id，递归查找
			return getCreateDate(wci.getRenewalPrevId());
		} else if (wci.getSignDate() != null) { // 已经不存在之前的id了，最早的创建记录
			// create date 未记录，同步signDate
			wci.setCreateDate(wci.getSignDate());
			workCertInfoDao.save(wci);
			return wci.getCreateDate();
		} else { // 如果证书还未发放返回空值
			return null;
		}

	}

	public Page<WorkCertInfo> find(Page<WorkCertInfo> page,
			WorkCertInfo workCertInfo) {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(workCertInfo.getSerialnumber())) {
			dc.add(Restrictions.like("serialnumber",
					"%" + EscapeUtil.escapeLike(workCertInfo.getSerialnumber())
							+ "%"));
		}
		dc.addOrder(Order.desc("id"));
		return workCertInfoDao.find(page, dc);
	}


	@Transactional(readOnly = false)
	public void delete(Long id) {
		signatureInfoDao.deleteById(id);
	}

	@Transactional(readOnly = false)
	public WorkCertInfo getByCertSn(String certSn) {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("serialnumber", certSn));
		List<WorkCertInfo> certInfos = workCertInfoDao.find(dc);
		if (certInfos.size()>0) {
			return certInfos.get(0);
		} else {
			return new WorkCertInfo();
		}
	}

	/**
	 * 获得当前需要生成新增业务的certInfo
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<WorkCertInfo> getCertInfoNeedAdd() {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		dc.add(Restrictions.isNotNull("workUserId"));
		List<WorkCertInfo> certInfos = workCertInfoDao.find(dc);
		return certInfos;
	}

	@Transactional(readOnly = true)
	public List<WorkCertInfo> getWorkInfos() {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		dc.add(Restrictions.isNotNull("workUserId"));
		List<WorkCertInfo> certInfos = workCertInfoDao.find(dc);
		return certInfos;
	}
	
	
	public List<WorkCertInfo> findZhiZhengTime(Date zhizhengStartTime,Date zhizhengEndTime) {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		if (zhizhengStartTime!=null) {
			dc.add(Restrictions.ge("signDate", zhizhengStartTime));
		}
		if (zhizhengEndTime!=null) {
			zhizhengEndTime.setHours(23);
			zhizhengEndTime.setMinutes(59);
			zhizhengEndTime.setSeconds(59);
			dc.add(Restrictions.le("signDate", zhizhengEndTime));
		}
		dc.addOrder(Order.desc("id"));
		return workCertInfoDao.find(dc);
	}
	
	
	
	
	@Transactional(readOnly = false)
	public void save(SignatureInfo signatureInfo) {
		signatureInfoDao.save(signatureInfo);
	}
	
	public Page<SignatureInfo> findByCondition(Page<SignatureInfo> page,
			SignatureInfo signatureInfo,String signatureType,Date start,Date end , List<Long> offices) {
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.add(Restrictions.in("officeId", offices));
		if(signatureInfo.getWorkDealInfo()!=null){
			if(signatureInfo.getWorkDealInfo().getWorkCompany()!=null&& StringUtils.isNotEmpty(signatureInfo.getWorkDealInfo().getWorkCompany()
					.getCompanyName())){
				dc.createAlias("workDealInfo", "workDealInfo");
				dc.createAlias("workDealInfo.workCompany", "workCompany");
				dc.add(Restrictions.like("workCompany.companyName", "%"
						+ signatureInfo.getWorkDealInfo().getWorkCompany().getCompanyName() + "%"));
			}	
		}
		if(signatureType!=null&&!signatureType.equals("")){
			dc.add(Restrictions.eq("signatureType", signatureType));
		}
		if (start != null) {
			start.setHours(0);
			start.setMinutes(0);
			start.setSeconds(0);
			dc.add(Restrictions.ge("createDate", start));
		}
		if (end != null) {
			end.setHours(23);
			end.setMinutes(59);
			end.setSeconds(59);
			dc.add(Restrictions.le("createDate", end));
		}
		
		dc.add(Restrictions.eq(SignatureInfo.DEL_FLAG,
				SignatureInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		
	
		return signatureInfoDao.find(page, dc);
		
	}

	public int finDaySealCount(Date countDate, Long officeId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		if (countDate!=null) {
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(countDate);
			endDate.add(Calendar.DATE,1);
			dc.add(Restrictions.ge("manageDate", countDate));
			dc.add(Restrictions.le("manageDate", endDate.getTime()));
		}
		if(officeId!=null)
		{
			dc.add(Restrictions.eq("officeId", officeId));
		}
		List<String> list = new ArrayList<String>();
		list.add(SignatureInfoStatus.STATUS_START);
		list.add(SignatureInfoStatus.STATUS_UPDATE);
		list.add(SignatureInfoStatus.STATUS_CHANGE);
		dc.add(Restrictions.in("status", list));
		dc.add(Restrictions.eq("signatureInfoStatus",SignatureInfoStatus.STATUS_ABNORMAL_USER));
		dc.addOrder(Order.desc("updateDate"));
		return signatureInfoDao.find(dc).size();
	}

	public List<SignatureInfo> finDaySeal(Date countDate, Long officeId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		if (countDate!=null) {
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(countDate);
			endDate.add(Calendar.DATE,1);
			dc.add(Restrictions.ge("manageDate", countDate));
			dc.add(Restrictions.le("manageDate", endDate.getTime()));
		}
		if(officeId!=null)
		{
			dc.add(Restrictions.eq("officeId", officeId));
		}
		List<String> list = new ArrayList<String>();
		list.add(SignatureInfoStatus.STATUS_START);
		list.add(SignatureInfoStatus.STATUS_UPDATE);
		list.add(SignatureInfoStatus.STATUS_CHANGE);
		dc.add(Restrictions.in("status", list));
		dc.add(Restrictions.eq("signatureInfoStatus",SignatureInfoStatus.STATUS_ABNORMAL_USER));
		dc.addOrder(Order.desc("updateDate"));
		return signatureInfoDao.find(dc);
	}
	public List<SignatureInfo> finDaySeal(Date countDate, Long officeId,Long appId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		if (countDate!=null) {
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(countDate);
			endDate.add(Calendar.DATE,1);
			dc.add(Restrictions.ge("manageDate", countDate));
			dc.add(Restrictions.le("manageDate", endDate.getTime()));
		}
		if(appId!=null)
		{
			dc.add(Restrictions.eq("configApp.id",appId));
		}
		if(officeId!=null)
		{
			dc.add(Restrictions.eq("officeId", officeId));
		}
		List<String> list = new ArrayList<String>();
		list.add(SignatureInfoStatus.STATUS_START);
		list.add(SignatureInfoStatus.STATUS_UPDATE);
		list.add(SignatureInfoStatus.STATUS_CHANGE);
		dc.add(Restrictions.in("status", list));
		dc.add(Restrictions.eq("signatureInfoStatus",SignatureInfoStatus.STATUS_ABNORMAL_USER));
		dc.addOrder(Order.desc("updateDate"));
		return signatureInfoDao.find(dc);
	}
	public int finSealCount(Date countDate, Long officeId,Long appId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		Date endDate  = countDate;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.DATE, +1);
		dc.add(Restrictions.ge("manageDate", countDate));
		dc.add(Restrictions.lt("manageDate", calendar.getTime()));
		List<String> list = new ArrayList<String>();
		list.add(SignatureInfoStatus.STATUS_START);
		list.add(SignatureInfoStatus.STATUS_UPDATE);
		list.add(SignatureInfoStatus.STATUS_CHANGE);
		dc.add(Restrictions.in("status", list));
		dc.add(Restrictions.eq("signatureInfoStatus",SignatureInfoStatus.STATUS_ABNORMAL_USER));
		dc.addOrder(Order.desc("createDate"));
		return signatureInfoDao.find(dc).size();
	}
	public List<SignatureInfo> findSvn(String svn) {
		return signatureInfoDao.findSvn(svn);
	}

	
	/**
	 * 生成业务编号 serviceType是区分客户端办理和柜台办理 0是柜台，1是客户端
	 * 
	 * @param serviceType
	 * @return
	 */
	public String getSVN() {
		Date date = new Date();
		String svn = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM-");
			try {
				User user = UserUtils.getUser();
				String timeSvn = "Y-" + user.getOffice().getName() + "-"
						+ sdf.format(date).substring(2);
				List<SignatureInfo> list = findSvn("%" + timeSvn + "%");
				int num = 1;
				if (list.size() > 0) {
					String oldSvn = list.get(0).getSvn();
					num = Integer
							.parseInt(oldSvn.substring(oldSvn.length() - 3)) + 1;
				}
				String numStr = "";
				if (num > 0 && num < 10) {
					numStr = "000" + num;
				} else if (num > 9 && num < 100) {
					numStr = "00" + num;
				} else if (num > 99 && num < 1000) {
					numStr = "0" + num;
				} else {
					numStr = "" + num;
				}
				svn = timeSvn + numStr;
			} catch (Exception e) {
				// TODO: handle exception
			}
		return svn;
	}
	
	
	
	//制印章时检查是否可以新增的方法
	@Transactional(readOnly = true)
	public SignatureInfo getNewByCertSn(String certSn) {
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(Restrictions.eq("workCertInfo.serialnumber", certSn));
		//缴费完成或签章失败都可以办理
		dc.add(Restrictions.or(Restrictions.eq("signatureInfoStatus", SignatureInfoStatus.STATUS_ADD_Info), Restrictions.eq("signatureInfoStatus", SignatureInfoStatus.STATUS_REFUND_USER)));
		dc.add(Restrictions.eq("signatureInfoType", SignatureInfoType.TYPE_ADD_SIGNATURE));
		dc.add(Restrictions.eq("status", SignatureInfoStatus.STATUS_UNSTART));
		dc.add(Restrictions.eq("delFlag", SignatureInfo.DEL_FLAG_NORMAL));
		List<SignatureInfo> signInfos = signatureInfoDao.find(dc);
		if (signInfos.size()==1) {
			return signInfos.get(0);
		} else {
			return null;
		}
	}

	//检查同一时间只允许办理一个印章方法
	@Transactional(readOnly = true)
	public SignatureInfo getCanDoByCertSn(String certSn) {
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(Restrictions.eq("workCertInfo.serialnumber", certSn));
		//和getNewByCertSn方法的区别之处
		dc.add(Restrictions.eq("signatureInfoStatus", SignatureInfoStatus.STATUS_ADD_Info));
		dc.add(Restrictions.eq("signatureInfoType", SignatureInfoType.TYPE_ADD_SIGNATURE));
		dc.add(Restrictions.eq("status", SignatureInfoStatus.STATUS_UNSTART));
		dc.add(Restrictions.eq("delFlag", SignatureInfo.DEL_FLAG_NORMAL));
		List<SignatureInfo> signInfos = signatureInfoDao.find(dc);
		if (signInfos.size()==1) {
			return signInfos.get(0);
		} else {
			return null;
		}
	}
	
	
	
	
	public SignatureInfo getChangeBySignatureId(String old,String young,Long signId){
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		//dc.createAlias("workCertInfo", "workCertInfo");
		dc.createAlias("oldWorkCertInfo", "oldWorkCertInfo");
		//dc.add(Restrictions.eq("workCertInfo.serialnumber", young));
		dc.add(Restrictions.eq("oldWorkCertInfo.serialnumber", old));
		dc.add(Restrictions.eq("firstId",signId));
		//缴费完成或签章失败都可以办理
		dc.add(Restrictions.or(Restrictions.eq("signatureInfoStatus", SignatureInfoStatus.STATUS_ADD_Info), Restrictions.eq("signatureInfoStatus", SignatureInfoStatus.STATUS_REFUND_USER)));
		dc.add(Restrictions.eq("signatureInfoType",SignatureInfoType.TYPE_CHANGE_SIGNATURE ));
		dc.add(Restrictions.eq("status", SignatureInfoStatus.STATUS_UNSTART));
		dc.add(Restrictions.eq("delFlag", SignatureInfo.DEL_FLAG_NORMAL));
		List<SignatureInfo> signInfos = signatureInfoDao.find(dc);
		if (signInfos.size()==1) {
			return signInfos.get(0);
		} else {
			return null;
		}
		
	}
	
	
	public SignatureInfo getUpdateBySignatureId(Long signId){
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("firstId",signId));
		
		//缴费完成或签章失败都可以办理
		dc.add(Restrictions.or(Restrictions.eq("signatureInfoStatus", SignatureInfoStatus.STATUS_ADD_Info), Restrictions.eq("signatureInfoStatus", SignatureInfoStatus.STATUS_REFUND_USER)));
		dc.add(Restrictions.eq("signatureInfoType", SignatureInfoType.TYPE_UPDATE_SIGNATURE));
		dc.add(Restrictions.eq("status", SignatureInfoStatus.STATUS_UNSTART));
		dc.add(Restrictions.eq("delFlag", SignatureInfo.DEL_FLAG_NORMAL));
		List<SignatureInfo> signInfos = signatureInfoDao.find(dc);
		if (signInfos.size()==1) {
			return signInfos.get(0);
		} else {
			return null;
		}
	}
	public int getInfocount(Date date, Long officeId,String year,Long appId, String signatureInfoType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1); 
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("officeId", officeId));
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.ge("manageDate", date));
		dc.add(Restrictions.lt("manageDate", calendar.getTime()));
		dc.add(Restrictions.eq("signatureInfoType",signatureInfoType));
		dc.add(Restrictions.eq("year", year));
		List<String> list = new ArrayList<String>();
		list.add(SignatureInfoStatus.STATUS_START);
		list.add(SignatureInfoStatus.STATUS_UPDATE);
		list.add(SignatureInfoStatus.STATUS_CHANGE);
		dc.add(Restrictions.in("status", list));
		dc.add(Restrictions.eq("signatureInfoStatus",SignatureInfoStatus.STATUS_ABNORMAL_USER));
		return (int) signatureInfoDao.count(dc);
	}
	public Double getSealRecripetCount(Long officeId,Long appId,Date countDate)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE, 1);
		DetachedCriteria dc=signatureInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("officeId", officeId));
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		List<String> list = new ArrayList<String>();
		list.add(SignatureInfoStatus.STATUS_START);
		list.add(SignatureInfoStatus.STATUS_UPDATE);
		list.add(SignatureInfoStatus.STATUS_CHANGE);
		dc.add(Restrictions.in("status", list));
		dc.add(Restrictions.eq("signatureInfoStatus",SignatureInfoStatus.STATUS_ABNORMAL_USER));
		dc.createAlias("workDealInfo", "workDealInfo");
		dc.createAlias("workDealInfo.workPayInfo", "workPayInfo");
		dc.add(Restrictions.gt("workPayInfo.updateDate", countDate));
		dc.add(Restrictions.lt("workPayInfo.updateDate", calendar.getTime()));
		dc.add(Restrictions.eq("workPayInfo.userReceipt", true));
		List<SignatureInfo> dealInfos = signatureInfoDao.find(dc);
		Double totalReceipt = 0d;
		for (SignatureInfo signatureInfo : dealInfos) {
			try {
				totalReceipt += signatureInfo.getWorkDealInfo().getWorkPayInfo()
						.getReceiptAmount();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return totalReceipt;
	}

	public List<SignatureInfo> findBycertSn(String certSn){
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.createAlias("workCertInfo", "workCertInfo");
		dc.add(Restrictions.eq("workCertInfo.serialnumber", certSn));
		dc.add(Restrictions.eq("delFlag", SignatureInfo.DEL_FLAG_NORMAL));
		return signatureInfoDao.find(dc);
	}
	public Page<SignatureInfo> findAll(Page<SignatureInfo> page,SignatureInfo signatureInfo)
	{
		DetachedCriteria dc=signatureInfoDao.createDetachedCriteria();
		dc.createAlias("workDealInfo", "workDealInfo");
		dc.createAlias("workDealInfo.workCompany", "workCompany");
		dc.createAlias("enterUser", "enterUser");
		if(signatureInfo.getWorkDealInfo()!=null)
		{
			if(signatureInfo.getWorkDealInfo().getWorkCompany()!=null)
			{
				if(signatureInfo.getWorkDealInfo().getWorkCompany().getCompanyName()!=null&&!"".equals(signatureInfo.getWorkDealInfo().getWorkCompany().getCompanyName()))
				{
					
					dc.add(Restrictions.eq("workCompany.companyName", signatureInfo.getWorkDealInfo().getWorkCompany().getCompanyName()));
				}
			}
		}
		if(signatureInfo.getEnterUser()!=null)
		{
			if(signatureInfo.getEnterUser().getName()!=null&&!"".equals(signatureInfo.getEnterUser().getName()))
			{
				dc.add(Restrictions.eq("enterUser.name", signatureInfo.getEnterUser().getName()));
			}
		}
		if(signatureInfo.getSignatureInfoType()!=null&&!"".equals(signatureInfo.getSignatureInfoType()))
		{
			dc.add(Restrictions.eq("signatureInfoType", signatureInfo.getSignatureInfoType()));
		}
		dc.addOrder(Order.desc("updateDate"));
		return signatureInfoDao.find(page,dc);
	}
	
	
	public List<SignatureInfo> findByAgentIdProductId(ConfigApp app,SignatureConfigChargeAgent agent){
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.createAlias("configApp","configApp" );
		dc.createAlias("signatureAgent", "signatureAgent");
		dc.add(Restrictions.eq("configApp", app));
		dc.add(Restrictions.eq("signatureAgent", agent));
		return signatureInfoDao.find(dc);
	}
	
	
	public SignatureInfo getByPreId (Long preId){
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("prevId", preId));
		
	    List<SignatureInfo> list = signatureInfoDao.find(dc);
	    
	    if(list.isEmpty()){
	    	return null;
	    }else{
	    	return list.get(0);
	    }
	}
	
	
	public List<SignatureInfo> findByDealInfo(WorkDealInfo dealInfo){
		DetachedCriteria dc = signatureInfoDao.createDetachedCriteria();
		dc.createAlias("workDealInfo", "workDealInfo");
		dc.add(Restrictions.eq("workDealInfo", dealInfo));
		
		return signatureInfoDao.find(dc);
	}
}
