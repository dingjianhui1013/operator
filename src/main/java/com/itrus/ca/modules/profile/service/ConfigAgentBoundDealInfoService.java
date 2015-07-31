/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

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
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.dao.ConfigAgentBoundDealInfoDao;

/**
 * 计费策略模版关联业务表Service
 * @author HuHao
 * @version 2015-07-27
 */
@Component
@Transactional(readOnly = true)
public class ConfigAgentBoundDealInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigAgentBoundDealInfoService.class);
	
	@Autowired
	private ConfigAgentBoundDealInfoDao configAgentBoundDealInfoDao;
	
	public ConfigAgentBoundDealInfo get(Long id) {
		return configAgentBoundDealInfoDao.findOne(id);
	}
	
	public Page<ConfigAgentBoundDealInfo> find(Page<ConfigAgentBoundDealInfo> page, ConfigAgentBoundDealInfo configAgentBoundDealInfo) {
		DetachedCriteria dc = configAgentBoundDealInfoDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(configAgentBoundDealInfo.getName())){
//			dc.add(Restrictions.like("name", "%"+configAgentBoundDealInfo.getName()+"%"));
//		}
		dc.add(Restrictions.eq(ConfigAgentBoundDealInfo.DEL_FLAG, ConfigAgentBoundDealInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configAgentBoundDealInfoDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ConfigAgentBoundDealInfo configAgentBoundDealInfo) {
		configAgentBoundDealInfoDao.save(configAgentBoundDealInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configAgentBoundDealInfoDao.deleteById(id);
	}
	
}
