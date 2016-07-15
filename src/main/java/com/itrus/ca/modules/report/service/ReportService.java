package com.itrus.ca.modules.report.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.modules.constant.ReportQueryType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.report.vo.WorkDealInfoVO;
import com.itrus.ca.modules.work.dao.WorkDealInfoDao;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.vo.WorkDealInfoListVo;

/**
 * 业务办理Service
 * 
 * @author ZhangShaoBo
 * @version 2014-06-13
 */
/**
 * @author CYC
 * 
 */
@Component

@Transactional(readOnly = true)
public class ReportService extends BaseService {

	@Autowired
	private WorkDealInfoDao workDealInfoDao;

	static Log log = LogFactory.getLog(ReportService.class);

	private static final int CELL_NUM = 9;

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReportService.class);

	
	
	/**
	 *   由于是只查数量，为了提高效率，我们选择用sql语句来查
	 */
	public Integer findCountByDate(Long appId, Date startTime, Date endTime, Integer method) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select count(*) as ct from work_deal_info wdi");
		sql.append(",config_app ca");
		
		if(method==ReportQueryType.TYPE_VALID_DEAL){
			sql.append(",work_cert_info wci");
		}
		
		sql.append(" where wdi.app_id = ca.id");
		sql.append(" and ca.id = ").append(appId);
		
		if(method==ReportQueryType.TYPE_VALID_DEAL){
			
			sql.append(" and wdi.cert_id = wci.id");
			sql.append(" and wci.notbefore <= TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			sql.append(" and wdi.notafter >= TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			
			sql.append(" and wdi.del_flag = ").append(DataEntity.DEL_FLAG_NORMAL);
		}
		
		
		if(method==ReportQueryType.TYPE_NEW_DEAL){
			sql.append(" and wdi.business_card_user_date >=TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.business_card_user_date <=TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			
			sql.append(" and wdi.deal_info_type = ").append(WorkDealInfoType.TYPE_ADD_CERT);
			
		}
		
		
		if(method == ReportQueryType.TYPE_UPDATE_DEAL){
			sql.append(" and wdi.business_card_user_date >=TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.business_card_user_date <=TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			
			sql.append(" and wdi.deal_info_type = ").append(WorkDealInfoType.TYPE_UPDATE_CERT);
		}
		
		
		if(method == ReportQueryType.TYPE_UNUPDATE_DEAL){
			sql.append(" and wdi.pay_user_date >=TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.pay_user_date <=TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.business_card_user_date is null");
			
			sql.append(" and wdi.deal_info_type = ").append(WorkDealInfoType.TYPE_UPDATE_CERT);
			
			sql.append(" and wdi.deal_info_status != ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			sql.append(" and wdi.deal_info_status != ").append(WorkDealInfoStatus.STATUS_CERT_REVOKE);
			
		}
		
		
		if(method == ReportQueryType.TYPE_MAINTENANCE_DEAL){
			sql.append(" and wdi.business_card_user_date >=TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.business_card_user_date <=TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			
			sql.append(" and wdi.deal_info_type is null");
		}
		
		
		System.out.println(sql);
		
		List<Map> ct = null;
		
		try {
			ct = workDealInfoDao.findBySQLListMap(sql.toString(), 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		return new Integer(ct.get(0).get("CT").toString());
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
			dc.createAlias("workCertInfo", "workCertInfo");
			
			endTime.setHours(0);
			endTime.setMinutes(0);
			endTime.setSeconds(00);
			dc.add(Restrictions.lt("workCertInfo.notbefore", endTime));
			
			dc.add(Restrictions.gt("notafter", endTime));
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
			dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));
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

		return workDealInfoDao.find(page,dc);
	}
	
	
	
	
	
	public List<WorkDealInfoVO> findListByDate(Long appId, Date startTime, Date endTime, Integer method) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select wdi.deal_info_type as type ");
		sql.append(",wdi.deal_info_type1 as type1");
		sql.append(",wdi.deal_info_type2 as type2");
		sql.append(",wdi.deal_info_type3 as type3");
		sql.append(",wdi.business_card_user_date as busidate");
		sql.append(",wc.company_name as cyname");
		sql.append(",wuh.contact_name as ctname");
		sql.append(" from work_deal_info wdi");
		sql.append(",config_app ca");
		sql.append(",work_company wc");
		sql.append(",work_user_his wuh");
		
		if(method==ReportQueryType.TYPE_VALID_DEAL){
			sql.append(",work_cert_info wci");
		}
		
		sql.append(" where wdi.app_id = ca.id");
		sql.append(" and wdi.work_company_id = wc.id");
		sql.append(" and wdi.work_user_his_id = wuh.id");
		sql.append(" and ca.id = ").append(appId);
		
		
		if(method==ReportQueryType.TYPE_VALID_DEAL){
			
			sql.append(" and wdi.cert_id = wci.id");
			sql.append(" and wci.notbefore <= TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			sql.append(" and wdi.notafter >= TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			
			sql.append(" and wdi.del_flag = ").append(DataEntity.DEL_FLAG_NORMAL);
		}
		
		
		if(method==ReportQueryType.TYPE_NEW_DEAL){
			sql.append(" and wdi.business_card_user_date >=TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.business_card_user_date <=TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			
			sql.append(" and wdi.deal_info_type = ").append(WorkDealInfoType.TYPE_ADD_CERT);
			
		}
		
		
		if(method == ReportQueryType.TYPE_UPDATE_DEAL){
			sql.append(" and wdi.business_card_user_date >=TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.business_card_user_date <=TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			
			sql.append(" and wdi.deal_info_type = ").append(WorkDealInfoType.TYPE_UPDATE_CERT);
		}
		
		
		if(method == ReportQueryType.TYPE_UNUPDATE_DEAL){
			sql.append(" and wdi.pay_user_date >=TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.pay_user_date <=TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.business_card_user_date is null");
			
			sql.append(" and wdi.deal_info_type = ").append(WorkDealInfoType.TYPE_UPDATE_CERT);
			
			sql.append(" and wdi.deal_info_status != ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			sql.append(" and wdi.deal_info_status != ").append(WorkDealInfoStatus.STATUS_CERT_REVOKE);
			
		}
		
		
		if(method == ReportQueryType.TYPE_MAINTENANCE_DEAL){
			sql.append(" and wdi.business_card_user_date >=TO_DATE('"
					+ DateUtils.formatDate(startTime, "yyyy-MM-dd 00:00:01")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.business_card_user_date <=TO_DATE('"
					+ DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
					+ "', 'yyyy-MM-dd hh24:mi:ss')");
			
			sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
			
			sql.append(" and wdi.deal_info_type is null");
		}
		
		
		List<Map> lst = null;
		
		try {
			lst = workDealInfoDao.findBySQLListMap(sql.toString(),0,0);
		
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<WorkDealInfoVO> resLst = new ArrayList<WorkDealInfoVO>();
		for (Map e : lst) {
			WorkDealInfoVO vo = new WorkDealInfoVO();
			Iterator<String> it = e.keySet().iterator();
			while (it.hasNext()) {
				String k = it.next();
				if (k.equals("CYNAME")) {
					if (e.get("CYNAME") != null)
						vo.setCompanyName(e.get("CYNAME").toString());
				}
				
				
				if (k.equals("CTNAME")) {
					if (e.get("CTNAME") != null)
						vo.setContactName(e.get("CTNAME").toString());

				}
				
				if (k.equals("TYPE")) {
					if (e.get("TYPE") != null)
						vo.setDealInfoType(new Integer(e.get("TYPE").toString()));
				}
				
				if (k.equals("TYPE1")) {
					if (e.get("TYPE1") != null)
						vo.setDealInfoType1(new Integer(e.get("TYPE1").toString()));
				}
				
				if (k.equals("TYPE2")) {
					if (e.get("TYPE2") != null)
						vo.setDealInfoType2(new Integer(e.get("TYPE2").toString()));
				}
				
				if (k.equals("TYPE3")) {
					if (e.get("TYPE3") != null)
						vo.setDealInfoType3(new Integer(e.get("TYPE3").toString()));
				}
				
				if (k.equals("BUSIDATE")) {
					if (e.get("BUSIDATE") != null)
						vo.setBusinessCardDate(new SimpleDateFormat("yyyy-MM-dd").parse(e.get("BUSIDATE").toString()));
				}
				
				
				
			}
			resLst.add(vo);
		}
		return resLst;
		
	}
	
	
	
}
