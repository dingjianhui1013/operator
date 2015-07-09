/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

import java.util.ArrayList;
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
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.dao.ConfigCommercialAgentDao;

/**
 * 代理商Service
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class ConfigCommercialAgentService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigCommercialAgentService.class);
	
	@Autowired
	private ConfigCommercialAgentDao configCommercialAgentDao;
	
	public ConfigCommercialAgent get(Long id) {
		return configCommercialAgentDao.findOne(id);
	}
	
	public Page<ConfigCommercialAgent> find(Page<ConfigCommercialAgent> page, ConfigCommercialAgent configCommercialAgent) {
		DetachedCriteria dc = configCommercialAgentDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configCommercialAgent.getAgentName())){
			dc.add(Restrictions.like("agentName", "%"+EscapeUtil.escapeLike(configCommercialAgent.getAgentName())+"%"));
		}
		dc.add(Restrictions.ne("del_flag", 1));
		dc.addOrder(Order.desc("id"));
		return configCommercialAgentDao.find(page, dc);
	}
	
	public List<ConfigCommercialAgent> findByName(String agentName) {
		DetachedCriteria dc = configCommercialAgentDao.createDetachedCriteria();
		dc.add(Restrictions.eq("agentName", agentName));
		dc.add(Restrictions.ne("del_flag", 1));
		dc.addOrder(Order.desc("id"));
		return configCommercialAgentDao.find(dc);
	}
	
	public List<ConfigCommercialAgent> findAllNameByType(Integer type) {
		DetachedCriteria dc = configCommercialAgentDao.createDetachedCriteria();
		//市场推广
		if (type==1) {
			dc.add(Restrictions.eq("agentType1", true));
		}
		//劳务关系
		if (type==2) {
			dc.add(Restrictions.eq("agentType2", true));
		}
		dc.add(Restrictions.ne("del_flag", 1));
		return configCommercialAgentDao.find(dc);
	}
	@Transactional(readOnly = false)
	public void save(ConfigCommercialAgent configCommercialAgent) {
		configCommercialAgentDao.save(configCommercialAgent);
	}
	
	
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configCommercialAgentDao.deleteById(id);
	}
	
}
