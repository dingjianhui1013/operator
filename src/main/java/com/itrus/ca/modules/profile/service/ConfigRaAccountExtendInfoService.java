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

import antlr.Utils;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.dao.ConfigRaAccountExtendInfoDao;

/**
 * RA配置详情Service
 * @author ZhangJingtao
 * @version 2014-06-09
 */
@Component
@Transactional(readOnly = true)
public class ConfigRaAccountExtendInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigRaAccountExtendInfoService.class);
	
	@Autowired
	private ConfigRaAccountExtendInfoDao configRaAccountExtendInfoDao;
	
	public ConfigRaAccountExtendInfo get(Long id) {
		return configRaAccountExtendInfoDao.findOne(id);
	}
	
	public Page<ConfigRaAccountExtendInfo> find(Page<ConfigRaAccountExtendInfo> page, ConfigRaAccountExtendInfo configRaAccountExtendInfo) {
		DetachedCriteria dc = configRaAccountExtendInfoDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configRaAccountExtendInfo.getCertName())) {
			dc.add(Restrictions.like("certName", "%"+configRaAccountExtendInfo.getCertName()+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return configRaAccountExtendInfoDao.find(page, dc);
	}
	
	public ConfigRaAccountExtendInfo findByCertName(String certName,Long extendId){
		DetachedCriteria dc = configRaAccountExtendInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("certName", certName));
		if (extendId!=null) {
			dc.add(Restrictions.ne("id", extendId));
		}
		if (configRaAccountExtendInfoDao.find(dc).size()>0) {
			return configRaAccountExtendInfoDao.find(dc).get(0);
		}else {
			return null;
		}
	}
	public List<ConfigRaAccountExtendInfo> findAll()
	{
		DetachedCriteria dc =configRaAccountExtendInfoDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return configRaAccountExtendInfoDao.find(dc);
		
		
	}
	
	
	@Transactional(readOnly = false)
	public void save(ConfigRaAccountExtendInfo configRaAccountExtendInfo) {
		configRaAccountExtendInfoDao.save(configRaAccountExtendInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configRaAccountExtendInfoDao.deleteById(id);
	}
	
}
