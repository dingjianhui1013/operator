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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.dao.ConfigChargeAgentBoundConfigProductDao;

/**
 * 计费策略模版详情历史Service
 * @author HuHao
 * @version 2015-07-17
 */
@Component
@Transactional(readOnly = true)
public class ConfigChargeAgentBoundConfigProductService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigChargeAgentBoundConfigProductService.class);
	
	@Autowired
	private ConfigChargeAgentBoundConfigProductDao configChargeAgentBoundConfigProductDao;
	
	public ConfigChargeAgentBoundConfigProduct get(Long id) {
		return configChargeAgentBoundConfigProductDao.findOne(id);
	}
	
	public Page<ConfigChargeAgentBoundConfigProduct> find(Page<ConfigChargeAgentBoundConfigProduct> page, ConfigChargeAgentBoundConfigProduct configChargeAgentBoundConfigProduct) {
		DetachedCriteria dc = configChargeAgentBoundConfigProductDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(configChargeAgentBoundConfigProduct.getName())){
//			dc.add(Restrictions.like("name", "%"+configChargeAgentBoundConfigProduct.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(ConfigChargeAgentBoundConfigProduct.DEL_FLAG, ConfigChargeAgentBoundConfigProduct.DEL_FLAG_NORMAL));
//		dc.addOrder(Order.desc("id"));
		return configChargeAgentBoundConfigProductDao.find(page, dc);
	}
	
	
	@Transactional(readOnly = false)
	public List<ConfigChargeAgentBoundConfigProduct> findByProId(Long proId){
		DetachedCriteria dc = configChargeAgentBoundConfigProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("product.id", proId));
		dc.createAlias("agent", "agent");
		dc.add(Restrictions.eq("agent.tempStyle", "1"));
		return configChargeAgentBoundConfigProductDao.find(dc);
	}
	
	
	
	@Transactional(readOnly = false)
	public List<ConfigChargeAgentBoundConfigProduct> findByProductId(Long proId){
		DetachedCriteria dc = configChargeAgentBoundConfigProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("product.id", proId));
		return configChargeAgentBoundConfigProductDao.find(dc);
	}
	
	
	
	
	
	@Transactional(readOnly = false)
	public List<ConfigChargeAgentBoundConfigProduct> findByProIdAll(Long proId){
		DetachedCriteria dc = configChargeAgentBoundConfigProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("product.id", proId));
		dc.createAlias("agent", "agent");
		dc.addOrder(Order.desc("agent.tempStyle"));
		return configChargeAgentBoundConfigProductDao.find(dc);
	}
	
	
	@Transactional(readOnly = false)
	public ConfigChargeAgentBoundConfigProduct findByProIdAgentName(Long proId,String agentName){
		DetachedCriteria dc = configChargeAgentBoundConfigProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("product.id", proId));
		dc.createAlias("agent", "agent");
		dc.add(Restrictions.eq("agent.tempName", agentName));
		if (configChargeAgentBoundConfigProductDao.find(dc).size()>0) {
			return configChargeAgentBoundConfigProductDao.find(dc).get(0);
		}else{
			return null;
		}
	}
	
	
	
	
	@Transactional(readOnly = false)
	public List<ConfigChargeAgentBoundConfigProduct> findByProIdAllByStyle(Long proId ,String style ){
		DetachedCriteria dc = configChargeAgentBoundConfigProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("product.id", proId));
		dc.createAlias("agent", "agent");
		dc.add(Restrictions.eq("agent.tempStyle", style));
		dc.addOrder(Order.desc("id"));
		return configChargeAgentBoundConfigProductDao.find(dc);
	}
	
	
	
	
	@Transactional(readOnly = false)
	public ConfigChargeAgentBoundConfigProduct findByAgentIdProductId(Long agentId , Long productId) {
		DetachedCriteria dc = configChargeAgentBoundConfigProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("agent.id", agentId));
		dc.add(Restrictions.eq("product.id", productId));
		if (configChargeAgentBoundConfigProductDao.find(dc).size()>0) {
			return configChargeAgentBoundConfigProductDao.find(dc).get(0);
		}else{
			
			return null;
		}
	}
	
	
	@Transactional(readOnly = false)
	public List<ConfigChargeAgentBoundConfigProduct> findByAgentId(Long agentId) {
		DetachedCriteria dc = configChargeAgentBoundConfigProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("agent.id", agentId));
		return configChargeAgentBoundConfigProductDao.find(dc);
	}
	
	
	
	
	@Transactional(readOnly = false)
	public void save(ConfigChargeAgentBoundConfigProduct configChargeAgentBoundConfigProduct) {
		configChargeAgentBoundConfigProductDao.save(configChargeAgentBoundConfigProduct);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configChargeAgentBoundConfigProductDao.deleteById(id);
	}
	
	
	@Transactional(readOnly = false)
	public void deleteByAgentId(Long agentId) {
		configChargeAgentBoundConfigProductDao.deleteByAgentId(agentId);
	}
	
	
}
