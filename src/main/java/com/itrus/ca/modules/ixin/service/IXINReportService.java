package com.itrus.ca.modules.ixin.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.constant.ReportQueryType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.work.dao.WorkDealInfoDao;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 业务办理Service
 * 
 * @author ZhangShaoBo
 * @version 2014-06-13
 */
/**
 * @author liumeng
 * 
 */
@Component

@Transactional(readOnly = true)
public class IXINReportService extends BaseService {

	@Autowired
	private WorkDealInfoDao workDealInfoDao;

	static Log log = LogFactory.getLog(IXINReportService.class);


	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(IXINReportService.class);

	public Integer findCountByDate(Long appId, Date startTime, Date endTime) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if(appId!=null){
		    dc.add(Restrictions.eq("configApp.id", appId));
		}
		if(startTime!=null){
		    startTime.setHours(0);
	        startTime.setMinutes(0);
	        startTime.setSeconds(00);
		    dc.add(Restrictions.gt("notafter", startTime));
		}else if(endTime!=null){
		    endTime.setHours(23);
	        endTime.setMinutes(59);
	        endTime.setSeconds(59);
	        dc.add(Restrictions.lt("businessCardUserDate", endTime));
		    dc.add(Restrictions.gt("notafter", endTime));
        }
		dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));
		
		List<WorkDealInfo> list = workDealInfoDao.find(dc);
		for (WorkDealInfo workDealInfo : list) {
            System.out.println("应用名称"+workDealInfo.getConfigApp().getAppName()+"***证书序列号："+workDealInfo.getCertSn());
        }
		
		
		return workDealInfoDao.find(dc).size();
	}

	
	public Page<WorkDealInfo> findPageByDate(Page<WorkDealInfo> page,Long appId, Date startTime, Date endTime, Integer method) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");

		dc.add(Restrictions.eq("configApp.id", appId));

		startTime.setHours(0);
		startTime.setMinutes(0);
		startTime.setSeconds(00);

		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);

		if (method == ReportQueryType.TYPE_VALID_DEAL) {
			dc.add(Restrictions.gt("notafter", startTime));
			dc.add(Restrictions.lt("notafter", endTime));
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		} else if (method == ReportQueryType.TYPE_NEW_DEAL) {
			dc.add(Restrictions.gt("businessCardUserDate", startTime));
			dc.add(Restrictions.lt("businessCardUserDate", endTime));
			dc.add(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_ADD_CERT));
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		} else if (method == ReportQueryType.TYPE_UPDATE_DEAL) { // 时间段内已更新数量
			dc.add(Restrictions.gt("businessCardUserDate", startTime));
			dc.add(Restrictions.lt("businessCardUserDate", endTime));
			dc.add(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_UPDATE_CERT));
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		} else if (method == ReportQueryType.TYPE_UNUPDATE_DEAL) { // 时间段内已缴费但未更新数量
			dc.add(Restrictions.gt("payUserDate", startTime));
			dc.add(Restrictions.lt("payUserDate", endTime));
			dc.add(Restrictions.isNull("businessCardUserDate"));
			dc.add(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_UPDATE_CERT));
			dc.add(Restrictions.and(Restrictions.ne("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED),
					Restrictions.ne("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));
		} else if (method == ReportQueryType.TYPE_MAINTENANCE_DEAL) {
			dc.add(Restrictions.gt("businessCardUserDate", startTime));
			dc.add(Restrictions.lt("businessCardUserDate", endTime));
			dc.add(Restrictions.isNull("dealInfoType")); // 不是新增也不是更新
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		}

		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));

		return workDealInfoDao.find(page,dc);
	}
	
	
	
	
	
	public List<WorkDealInfo> findListByDate(Long appId, Date startTime, Date endTime, Integer method) {
		DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUserHis", "workUserHis");
		dc.add(Restrictions.eq("configApp.id", appId));

		startTime.setHours(0);
		startTime.setMinutes(0);
		startTime.setSeconds(00);

		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);

		if (method == ReportQueryType.TYPE_VALID_DEAL) {
			dc.add(Restrictions.gt("notafter", startTime));
			dc.add(Restrictions.lt("notafter", endTime));
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		} else if (method == ReportQueryType.TYPE_NEW_DEAL) {
			dc.add(Restrictions.gt("businessCardUserDate", startTime));
			dc.add(Restrictions.lt("businessCardUserDate", endTime));
			dc.add(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_ADD_CERT));
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		} else if (method == ReportQueryType.TYPE_UPDATE_DEAL) { // 时间段内已更新数量
			dc.add(Restrictions.gt("businessCardUserDate", startTime));
			dc.add(Restrictions.lt("businessCardUserDate", endTime));
			dc.add(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_UPDATE_CERT));
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		} else if (method == ReportQueryType.TYPE_UNUPDATE_DEAL) { // 时间段内已缴费但未更新数量
			dc.add(Restrictions.gt("payUserDate", startTime));
			dc.add(Restrictions.lt("payUserDate", endTime));
			dc.add(Restrictions.isNull("businessCardUserDate"));
			dc.add(Restrictions.eq("dealInfoType", WorkDealInfoType.TYPE_UPDATE_CERT));
			dc.add(Restrictions.and(Restrictions.ne("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED),
					Restrictions.ne("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_REVOKE)));
		} else if (method == ReportQueryType.TYPE_MAINTENANCE_DEAL) {
			dc.add(Restrictions.gt("businessCardUserDate", startTime));
			dc.add(Restrictions.lt("businessCardUserDate", endTime));
			dc.add(Restrictions.isNull("dealInfoType")); // 不是新增也不是更新
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
		}

		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));

		return workDealInfoDao.find(dc);
	}
	
	
	
}
