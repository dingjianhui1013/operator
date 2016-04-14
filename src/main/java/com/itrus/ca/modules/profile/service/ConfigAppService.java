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
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.profile.dao.ConfigAppDao;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 应用Service
 * @author HuHao
 * @version 2014-06-03
 */
@Component
@Transactional(readOnly = true)
public class ConfigAppService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigAppService.class);
	
	@Autowired
	private ConfigAppDao configAppDao;
	
	
	public ConfigApp get(Long id) {
		return configAppDao.findOne(id);
	}
	
	public Page<ConfigApp> find(Page<ConfigApp> page, ConfigApp configApp,List<Long> appIdList,Long areaId) {
		DetachedCriteria dc = configAppDao.createDetachedCriteria();
		dc.setResultTransformer(dc.DISTINCT_ROOT_ENTITY);
		if (StringUtils.isNotEmpty(configApp.getAppName())){
			dc.add(Restrictions.like("appName", "%"+EscapeUtil.escapeLike(configApp.getAppName())+"%"));
		}
		if (appIdList.size()>0) {
			dc.add(Restrictions.in("id", appIdList));
		}else if(areaId!=null&&appIdList.size()<1){
			
			dc.add(Restrictions.eq("id",-1L));
		}
		dc.add(Restrictions.eq(ConfigApp.DEL_FLAG, ConfigApp.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configAppDao.find(page, dc);
	}
	
	public List<ConfigApp> findAllConfigApp(){
		DetachedCriteria dc = configAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ConfigApp.DEL_FLAG, ConfigApp.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configAppDao.find(dc);
	} 
	public List<ConfigApp> findByconfigProjectType(
			Long configProjectTypeId) {
		DetachedCriteria dc = configAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ConfigApp.DEL_FLAG, ConfigApp.DEL_FLAG_NORMAL));
		if(configProjectTypeId!=null&&!"".equals(configProjectTypeId))
		{
			dc.createAlias("configProjectType", "configProjectType");
			dc.add(Restrictions.eq("configProjectType.id",configProjectTypeId));
		}
		return configAppDao.find(dc);
	}
	public List<ConfigApp> findByconfigProjectTypes(
			List<Long> configProjectTypeIds) {
		DetachedCriteria dc = configAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ConfigApp.DEL_FLAG, ConfigApp.DEL_FLAG_NORMAL));
		if(configProjectTypeIds!=null&&configProjectTypeIds.size()>0)
		{
			dc.add(Restrictions.in("configProjectType.id",configProjectTypeIds));
		}
		return configAppDao.find(dc);
	}
	public ConfigApp findByAppname(String appName) {
		DetachedCriteria dc = configAppDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(appName)){
			dc.add(Restrictions.eq("appName", appName));
		}
		dc.add(Restrictions.eq(ConfigApp.DEL_FLAG, ConfigApp.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		List<ConfigApp> apps = configAppDao.find(dc);
		return apps==null||apps.size()==0? null:apps.get(0);
	}
	
	public ConfigApp findByAppId(Long appId) {
		DetachedCriteria dc = configAppDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(appId.toString())){
			dc.add(Restrictions.eq("id", appId));
		}
		dc.add(Restrictions.eq(ConfigApp.DEL_FLAG, ConfigApp.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configAppDao.find(dc).get(0);
	}
	
	
	
	
	@Transactional(readOnly = false)
	public void save(ConfigApp configApp) {
		configAppDao.save(configApp);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configAppDao.deleteById(id);
	}
	
	/**
	 * 查询全部没有被代理商选择的业务
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<ConfigApp> findall() {
		if (UserUtils.getUser().getCompany().getId().equals(1L)) {
			return configAppDao.selectAll();
		}
		return configAppDao.findAllConfigApp();
	}
	
	@Transactional(readOnly = false)
	public List<ConfigApp> findallApps() {
		if (UserUtils.getUser().getCompany().getId().equals(1L)) {
			return configAppDao.selectAll();
		}
		return configAppDao.findAllConfigApp();
	}
	
	@Transactional(readOnly = false)
	public List<ConfigApp> findallAppsT() {
		return configAppDao.findAllConfigApp();
	}
	
	public List<ConfigApp> selectAll(){
		return configAppDao.selectAll();
	}
	
	@Transactional(readOnly = false)
	public List<ConfigApp> findAllByid(Long id) {
		return configAppDao.findAllById(id);
	}
	
	public List<ConfigApp> findByAppName(String name){
		return configAppDao.findByAppName(name);
	}
	
	@Transactional(readOnly = false)
	public boolean  findAppByName(String name){
		if(configAppDao.findAppByName(name).size()==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 查询与指定代理商关联的业务
	 * @param id
	 * @return
	 */
	public List<ConfigApp> findByAgentId(Long id){
		return configAppDao.findByAgentId(id);
	}

	public List<ConfigApp> findById(Long id) {
		return configAppDao.findById(id);
	}

	
   
	
	/**
	 * 查询属于当前登录网点的应用
	 * 
	 */
//	public List<ConfigApp> findByOffice(Office office){
//		DetachedCriteria dc = configAppDao.createDetachedCriteria();
//		dc.createAlias("createBy", "createBy");
//		if (office!=null){
//			dc.add(Restrictions.eq("createBy.office", office));
//		}
//		dc.add(Restrictions.eq(ConfigApp.DEL_FLAG, ConfigApp.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.desc("id"));
//		return configAppDao.find(dc);
//	}
}
