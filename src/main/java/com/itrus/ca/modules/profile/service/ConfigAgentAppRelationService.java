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
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.dao.ConfigAgentAppRelationDao;

/**
 * 代理商业务关联Service
 * @author WangHongwei
 * @version 2014-06-10
 */
@Component
@Transactional(readOnly = true)
public class ConfigAgentAppRelationService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigAgentAppRelationService.class);
	
	@Autowired
	private ConfigAgentAppRelationDao configAgentAppRelationDao;
	
	public ConfigAgentAppRelation get(Long id) {
		return configAgentAppRelationDao.findOne(id);
	}
	
	public ConfigCommercialAgent findAgentByApp(ConfigApp configApp){
		DetachedCriteria dc = configAgentAppRelationDao.createDetachedCriteria();
		dc.add(Restrictions.like("configApp",configApp));
		List<ConfigAgentAppRelation> list = configAgentAppRelationDao.find(dc);
		if (list.size()>0) {
			ConfigAgentAppRelation configAgentAppRelation = list.get(0);
			return configAgentAppRelation.getConfigCommercialAgent();
		}
		return null;
	}
	
	public List<ConfigAgentAppRelation> findByAgent(Long[] ids){
		DetachedCriteria dc = configAgentAppRelationDao.createDetachedCriteria();
		dc.createAlias("configCommercialAgent", "configCommercialAgent");
		dc.add(Restrictions.in("configCommercialAgent.id",ids));
		return configAgentAppRelationDao.find(dc);
	}
	public List<ConfigAgentAppRelation> findByAppName(Long appId){
		DetachedCriteria dc = configAgentAppRelationDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configApp.id",appId));
		return configAgentAppRelationDao.find(dc);
	}
	public List<ConfigAgentAppRelation> findByComAgentId(Long id){
		DetachedCriteria dc = configAgentAppRelationDao.createDetachedCriteria();
		dc.createAlias("configCommercialAgent", "configCommercialAgent");
		dc.add(Restrictions.eq("configCommercialAgent.id",id));
		return configAgentAppRelationDao.find(dc);
	}
	
	
	
	
	public List<ConfigAgentAppRelation> findByAgentAndApp(Long[] ids,ConfigApp configApp ){
		DetachedCriteria dc = configAgentAppRelationDao.createDetachedCriteria();
		dc.createAlias("configCommercialAgent", "configCommercialAgent");
		dc.add(Restrictions.in("configCommercialAgent.id",ids));
		if(configApp!=null)
		{
			dc.add(Restrictions.eq("configApp",configApp));
		}
		return configAgentAppRelationDao.find(dc);
	}
	@Transactional(readOnly = false)
	public void save(ConfigAgentAppRelation configAgentAppRelation) {
		configAgentAppRelationDao.save(configAgentAppRelation);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configAgentAppRelationDao.deleteByRaletionId(id);
	}
}
