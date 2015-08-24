/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentHistory;
import com.itrus.ca.modules.profile.dao.ConfigChargeAgentHistoryDao;

/**
 * 计费策略模版详情历史Service
 * @author HuHao
 * @version 2015-07-16
 */
@Component
@Transactional(readOnly = true)
public class ConfigChargeAgentHistoryService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigChargeAgentHistoryService.class);
	
	@Autowired
	private ConfigChargeAgentHistoryDao configChargeAgentHistoryDao;
	
	public ConfigChargeAgentHistory get(Long id) {
		return configChargeAgentHistoryDao.findOne(id);
	}
	
	public Page<ConfigChargeAgentHistory> find(Page<ConfigChargeAgentHistory> page, ConfigChargeAgentHistory configChargeAgentHistory) {
		DetachedCriteria dc = configChargeAgentHistoryDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(configChargeAgentHistory.getName())){
//			dc.add(Restrictions.like("name", "%"+configChargeAgentHistory.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(ConfigChargeAgentHistory.DEL_FLAG, ConfigChargeAgentHistory.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.desc("id"));
		return configChargeAgentHistoryDao.find(page, dc);
	}
	
	
	public Page<ConfigChargeAgentHistory> findByAgentId(Page<ConfigChargeAgentHistory> page, Long agentId,String createName,Date startTime,Date endTime) {
		DetachedCriteria dc = configChargeAgentHistoryDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(createName)) {
			dc.createAlias("createBy", "createBy");
			dc.add(Restrictions.like("createBy.name", "%"+createName+"%"));
		}
		
		if(startTime!=null){
			dc.add(Restrictions.ge("createDate", startTime));
		}
		if(endTime!=null){
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			dc.add(Restrictions.le("createDate", endTime));
		}
//		if (StringUtils.isNotEmpty(configChargeAgentHistory.getName())){
//			dc.add(Restrictions.like("name", "%"+configChargeAgentHistory.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(ConfigChargeAgentHistory.DEL_FLAG, ConfigChargeAgentHistory.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("superiorId", agentId));
		dc.addOrder(Order.asc("id"));
		return configChargeAgentHistoryDao.find(page, dc);
	}
	
	
	
	
	
	
	
	
	
	@Transactional(readOnly = false)
	public void save(ConfigChargeAgentHistory configChargeAgentHistory) {
		configChargeAgentHistoryDao.save(configChargeAgentHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		//configChargeAgentHistoryDao.deleteById(id);
	}
	
}
