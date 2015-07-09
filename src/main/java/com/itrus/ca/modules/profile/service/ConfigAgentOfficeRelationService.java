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
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.dao.ConfigAgentOfficeRelationDao;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * RA配置详情Service
 * @author ZhangJingtao
 * @version 2014-06-10
 */
@Component
@Transactional(readOnly = true)
public class ConfigAgentOfficeRelationService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigAgentOfficeRelationService.class);
	
	@Autowired
	private ConfigAgentOfficeRelationDao configAgentOfficeRelationDao;
	
	public ConfigAgentOfficeRelation get(Long id) {
		return configAgentOfficeRelationDao.findOne(id);
	}
	
	public List<ConfigAgentOfficeRelation> findByOffice (Office office) {
		DetachedCriteria dc = configAgentOfficeRelationDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.eq("office", office));
		dc.addOrder(Order.desc("id"));
		return configAgentOfficeRelationDao.find(dc);
	}
	@Transactional(readOnly = false)
	public void save(ConfigAgentOfficeRelation configAgentOfficeRelation) {
		configAgentOfficeRelationDao.save(configAgentOfficeRelation);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configAgentOfficeRelationDao.deleteByRaletionId(id);
	}
	
}
