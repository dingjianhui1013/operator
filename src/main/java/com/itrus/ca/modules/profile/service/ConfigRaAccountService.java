/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

import java.util.List;

import com.itrus.ca.modules.profile.dao.ConfigProductDao;

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
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.dao.ConfigRaAccountDao;

import com.itrus.ca.modules.work.dao.WorkDealInfoDao;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * RA配置Service
 * @author ZhangJingtao
 * @version 2014-06-09
 */
@Component
@Transactional(readOnly = true)
public class ConfigRaAccountService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigRaAccountService.class);
	
	@Autowired
	private ConfigRaAccountDao configRaAccountDao;
	@Autowired
	private ConfigProductDao configProductDao;
	
	@Autowired
	private WorkDealInfoDao WorkDealInfoDao;
	
	
	
	public ConfigRaAccount get(Long id) {
		return configRaAccountDao.findOne(id);
	}
	
	public Page<ConfigRaAccount> find(Page<ConfigRaAccount> page, ConfigRaAccount configRaAccount) {
		DetachedCriteria dc = configRaAccountDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configRaAccount.getRaName())){
			dc.add(Restrictions.like("raName", "%"+EscapeUtil.escapeLike(configRaAccount.getRaName())+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return configRaAccountDao.find(page, dc);
	}
	
	/**
	 * 查看全部
	 * @return
	 */
	public List<ConfigRaAccount> findAll(){
		DetachedCriteria dc = configRaAccountDao.createDetachedCriteria();
		dc.addOrder(Order.asc("id"));
		return configRaAccountDao.find(dc);
	}
	@Transactional(readOnly = true)
	public List findByRaName(String raName) {
		DetachedCriteria dc = configRaAccountDao.createDetachedCriteria();
		dc.add(Restrictions.eq("raName", raName));
		List<ConfigRaAccount> list = configRaAccountDao.find(dc);
		return list;
	}
	
	
	@Transactional(readOnly = false)
	public void save(ConfigRaAccount configRaAccount) {
		configRaAccountDao.save(configRaAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configRaAccountDao.deleteById(id);
	}
	
	//判断product是否有RA配置信息
	public List<ConfigRaAccount> findByProductId(Long productId) {
		DetachedCriteria dc = configRaAccountDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(productId.toString())) {
			dc.add(Restrictions.like("configProduct.id",productId) );
		}
		return configRaAccountDao.find(dc);
	}
	//根据Id得到ra信息
		public List<ConfigRaAccount> findById(Long Id) {
			DetachedCriteria dc = configRaAccountDao.createDetachedCriteria();
			if (Id!=null) {
				dc.add(Restrictions.eq("id",Id) );
				return configRaAccountDao.find(dc);
			}
			
			return null;
			
		}
	
	//判断ra是否关联证书
		public ConfigRaAccount findByExtendId(Long extendId) {
			DetachedCriteria dc = configRaAccountDao.createDetachedCriteria();
			dc.add(Restrictions.eq("configRaAccountExtendInfo.id", extendId));
			if (configRaAccountDao.find(dc).size()>0) {
				return configRaAccountDao.find(dc).get(0);
			}else {
				return null;
			}
		}
	

	/**
	 * 检测模板是否使用过
	 * @param raId
	 * @return
	 */
	@Transactional(readOnly = true)
		public boolean checkUsed(Long raId){
			DetachedCriteria detachedCriteria = configProductDao.createDetachedCriteria();
			detachedCriteria.add(Restrictions.eq("raAccountId",raId));
			List list = configProductDao.find(detachedCriteria);
			if(list.size() > 0 ){
				return true;
			}else{
				return false;
			}
		}
	
	
	
	/**
	 * 检测模板是否制过证书
	 * @param raId
	 * @return
	 */
	@Transactional(readOnly = true)
		public boolean checkUsedByDeal(Long raId){
			DetachedCriteria dc = WorkDealInfoDao.createDetachedCriteria();
			dc.createAlias("configProduct", "configProduct");
			
			dc.add(Restrictions.eq("configProduct.raAccountId", raId));
			List<WorkDealInfo> list = WorkDealInfoDao.find(dc);
			if(list.size() > 0 ){
				return true;
			}else{
				return false;
			}
		}
	
	
	
	
}
