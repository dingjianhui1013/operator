/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

import java.util.List;

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
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetailHistory;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentHistory;
import com.itrus.ca.modules.profile.dao.ConfigChargeAgentDetailHistoryDao;

/**
 * 计费策略模版详情历史Service
 * @author HuHao
 * @version 2015-07-16
 */
@Component
@Transactional(readOnly = true)
public class ConfigChargeAgentDetailHistoryService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigChargeAgentDetailHistoryService.class);
	
	@Autowired
	private ConfigChargeAgentDetailHistoryDao configChargeAgentDetailHistoryDao;
	
	public ConfigChargeAgentDetailHistory get(Long id) {
		return configChargeAgentDetailHistoryDao.findOne(id);
	}
	
	public Page<ConfigChargeAgentDetailHistory> find(Page<ConfigChargeAgentDetailHistory> page, ConfigChargeAgentDetailHistory configChargeAgentDetailHistory) {
		DetachedCriteria dc = configChargeAgentDetailHistoryDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(configChargeAgentDetailHistory.getName())){
//			dc.add(Restrictions.like("name", "%"+configChargeAgentDetailHistory.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(ConfigChargeAgentDetailHistory.DEL_FLAG, ConfigChargeAgentDetailHistory.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.desc("id"));
		return configChargeAgentDetailHistoryDao.find(page, dc);
	}
	
	
	public List<ConfigChargeAgentDetailHistory> findByAgentHistory(Long agentHistoryId) {
		DetachedCriteria dc = configChargeAgentDetailHistoryDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(configChargeAgentDetailHistory.getName())){
//			dc.add(Restrictions.like("name", "%"+configChargeAgentDetailHistory.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(ConfigChargeAgentDetailHistory.DEL_FLAG, ConfigChargeAgentDetailHistory.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq("agent.id", agentHistoryId));
		
		return configChargeAgentDetailHistoryDao.find(dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(ConfigChargeAgentDetailHistory configChargeAgentDetailHistory) {
		configChargeAgentDetailHistoryDao.save(configChargeAgentDetailHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
	//	configChargeAgentDetailHistoryDao.deleteById(id);
	}
	
}
