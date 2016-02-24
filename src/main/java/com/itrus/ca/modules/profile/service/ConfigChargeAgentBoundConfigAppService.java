/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;


import org.hibernate.criterion.DetachedCriteria;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigApp;

import com.itrus.ca.modules.profile.dao.ConfigChargeAgentBoundConfigAppDao;


/**
 * 计费策略模版详情历史Service
 * @author CYC
 * @version 2016-02-13
 */
@Component
@Transactional(readOnly = true)
public class ConfigChargeAgentBoundConfigAppService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigChargeAgentBoundConfigProductService.class);
	
	@Autowired
	private ConfigChargeAgentBoundConfigAppDao configChargeAgentBoundConfigAppDao;
	
	
	
	
	
	
	@Transactional(readOnly = false)
	public ConfigChargeAgentBoundConfigApp findByAgentIdAppId(Long agentId , Long appId) {
		DetachedCriteria dc = configChargeAgentBoundConfigAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq("agent.id", agentId));
		dc.add(Restrictions.eq("app.id", appId));
		if (configChargeAgentBoundConfigAppDao.find(dc).size()>0) {
			return configChargeAgentBoundConfigAppDao.find(dc).get(0);
		}else{
			
			return null;
		}
	}
	
	
	
	
	
}

