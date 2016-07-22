package com.itrus.ca.modules.ixin.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.itrus.ca.common.utils.DateUtils;
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

        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) as ct from work_deal_info wdi");
        sql.append(",config_app ca ,work_cert_info wci");
        sql.append(" where wdi.app_id = ca.id");
        sql.append(" and ca.id = ").append(appId);
        sql.append(" and wdi.cert_id = wci.id");
        sql.append(" and wci.notbefore <= TO_DATE('" + DateUtils.formatDate(endTime, "yyyy-MM-dd 23:59:59")
                + "', 'yyyy-MM-dd hh24:mi:ss')");
        sql.append(" and wdi.notafter >= TO_DATE('" + DateUtils.formatDate(endTime, "yyyy-MM-dd 00:00:01")
                + "', 'yyyy-MM-dd hh24:mi:ss')");
        sql.append(" and wdi.deal_info_status = ").append(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
        sql.append(" and wdi.del_flag = ").append(DataEntity.DEL_FLAG_NORMAL);
        System.out.println(sql);
        List<Map> ct = null;
        try {
            ct = workDealInfoDao.findBySQLListMap(sql.toString(), 0, 0);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException
                | InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return new Integer(ct.get(0).get("CT").toString());
	}

	
	
}
