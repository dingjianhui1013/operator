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
import com.itrus.ca.modules.profile.entity.ConfigProjectType;
import com.itrus.ca.modules.profile.dao.ConfigProjectTypeDao;

/**
 * 项目类型管理Service
 * @author HuHao
 * @version 2015-10-31
 */
@Component
@Transactional(readOnly = true)
public class ConfigProjectTypeService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigProjectTypeService.class);
	
	@Autowired
	private ConfigProjectTypeDao configProjectTypeDao;
	
	public ConfigProjectType get(Long id) {
		return configProjectTypeDao.findOne(id);
	}
	
	public Page<ConfigProjectType> find(Page<ConfigProjectType> page, ConfigProjectType configProjectType) {
		DetachedCriteria dc = configProjectTypeDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configProjectType.getProjectName())){
			dc.add(Restrictions.like("projectName", "%"+configProjectType.getProjectName()+"%"));
		}
		dc.add(Restrictions.eq(ConfigProjectType.DEL_FLAG, ConfigProjectType.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configProjectTypeDao.find(page, dc);
	}
	
	
	public List<ConfigProjectType> findProjectTypeList() {
		DetachedCriteria dc = configProjectTypeDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ConfigProjectType.DEL_FLAG, ConfigProjectType.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("projectName"));
		return configProjectTypeDao.find(dc);
	}
	
	
	
	
	
	public List<ConfigProjectType> checkName(String proName , Long id) {
		DetachedCriteria dc = configProjectTypeDao.createDetachedCriteria();
		dc.add(Restrictions.eq("projectName", proName));
		if (id!=null) {
			dc.add(Restrictions.ne("id", id));
		}
		dc.add(Restrictions.eq(ConfigProjectType.DEL_FLAG, ConfigProjectType.DEL_FLAG_NORMAL));
		return configProjectTypeDao.find(dc);
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(ConfigProjectType configProjectType) {
		configProjectTypeDao.save(configProjectType);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configProjectTypeDao.deleteById(id);
	}
	
}
