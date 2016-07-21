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

import com.google.common.collect.Lists;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.dao.ConfigAppOfficeRelationDao;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.sys.entity.Area;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 供应商产品配置Service
 * @author HUHAO
 * @version 2014-06-11
 */
@Component
@Transactional(readOnly = true)
public class ConfigAppOfficeRelationService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigAppOfficeRelationService.class);
	
	@Autowired
	private ConfigAppOfficeRelationDao configAppOfficeRelationDao;
	
	public ConfigAppOfficeRelation get(Long id) {
		return configAppOfficeRelationDao.findOne(id);
	}
	/*
	public Page<ConfigAppOfficeRelation> find(Page<ConfigAppOfficeRelation> page, ConfigAppOfficeRelation configAppOfficeRelation) {
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configAppOfficeRelation.getName())){
			dc.add(Restrictions.like("name", "%"+configAppOfficeRelation.getName()+"%"));
		}
		dc.add(Restrictions.eq(ConfigAppOfficeRelation.DEL_FLAG, ConfigAppOfficeRelation.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configAppOfficeRelationDao.find(page, dc);
	}
	*/
	
	
	public List<ConfigAppOfficeRelation> findByArea(Long areaId,Long officeId) {
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.createAlias("office.parent", "parent");
		if (areaId!=null){
			dc.add(Restrictions.eq("parent.id", areaId));
		}
		if (officeId!=null) {
			dc.add(Restrictions.eq("office.id", officeId));
		}
		dc.addOrder(Order.desc("id"));
		return configAppOfficeRelationDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ConfigAppOfficeRelation configAppOfficeRelation) {
		configAppOfficeRelationDao.save(configAppOfficeRelation);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configAppOfficeRelationDao.deleteById(id);
	}
	
	public List<ConfigAppOfficeRelation> findAllByAppId(Long appId) {
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(appId.toString())){
			dc.add(Restrictions.eq("configApp.id", appId));
		}
		dc.addOrder(Order.desc("id"));
		return configAppOfficeRelationDao.find(dc);
	}
	
	public List<ConfigAppOfficeRelation> findAllByOfficeId(Long officeId) {
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(officeId.toString())){
			dc.add(Restrictions.eq("office.id", officeId));
		}
		dc.addOrder(Order.desc("id"));
		return configAppOfficeRelationDao.find(dc);
	}
	
	/**
	 * 根据网点集合查询是否关联app
	* @Title: findAllByOffices
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param offices
	* @param @return    设定文件
	* @return List<ConfigAppOfficeRelation>    返回类型
	* @throws
	 */
	public boolean findAllByOffices(List<Office> offices) {
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.in("office", offices));
		dc.addOrder(Order.desc("id"));
		if(configAppOfficeRelationDao.find(dc).size()>0){
			return true;
		}
		return false;
	}
	
	
	
	public List<Long> findAllAppIdsByOffices(List<Office> offices){
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.in("office", offices));
		
		List<ConfigAppOfficeRelation> caors = configAppOfficeRelationDao.find(dc);
		
		List<Long> appIds = Lists.newArrayList();
		
		
		for(ConfigAppOfficeRelation caor:caors){
			appIds.add(caor.getConfigApp().getId());
		}
		
		return appIds;
	}
	
	
	public List<ConfigAppOfficeRelation> findAllByOfficeId(List<Long> officeids) {
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		if (officeids.size()>0){
			dc.add(Restrictions.in("office.id", officeids));
		}
		dc.addOrder(Order.desc("id"));
		return configAppOfficeRelationDao.find(dc);
	}
	
	public List<ConfigAppOfficeRelation> findAllByAppId(List<Long> apps) {
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		if (apps.size()>0){
			dc.add(Restrictions.in("configApp.id", apps));
		}
		dc.addOrder(Order.desc("id"));
		return configAppOfficeRelationDao.find(dc);
	}
	
	public List<Office> findAreaByAppId(Long appId) {
		return configAppOfficeRelationDao.findAreaByAppId(appId);
	}
	
	/**
	 * 根据office集合删除关联
	* @Title: deleByOffices
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param offices    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void deleByOffices(List<Office> offices){
		configAppOfficeRelationDao.deleteByoffices(offices);
	}
	
	
	public void deleteByofficeId(Long id) {
		configAppOfficeRelationDao.deleteByofficeId(id);
	}
	
}
