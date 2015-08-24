/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

import java.util.Date;

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
	
	
	
	public Page<ConfigAgentBoundDealInfo> findByAgentId(Page<ConfigAgentBoundDealInfo> page, Long agentId , String handle,
			Date startTime,
			Date endTime,
			Long areaId,
			Long officeId,
			 Long congifApplyId,
			 Long productId
			) {
		DetachedCriteria dc = configAgentBoundDealInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.createAlias("dealInfo", "dealInfo");
		if (StringUtils.isNotEmpty(handle)) {
			dc.add(Restrictions.like("createBy.name", "%"+handle+"%"));
		}
		if(startTime!=null){
			dc.add(Restrictions.ge("createDate", startTime));
		}
		if(endTime!=null){
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
			dc.add(Restrictions.le("createDate", endTime));
		}
		if(areaId!=null){
			dc.add(Restrictions.eq("office.parent.id", areaId));
		}
		if(officeId!=null){
			dc.add(Restrictions.eq("office.id", officeId));
		}
		if(congifApplyId!=null){
			dc.add(Restrictions.eq("dealInfo.configApp.id", congifApplyId));
			
		}
		if(productId!=null){
			dc.add(Restrictions.eq("dealInfo.configProduct.id", productId));
		}
		
		
		
		
		dc.add(Restrictions.eq("agent.id", agentId));
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
	
	
	@Transactional(readOnly = false)
	public void deleteById(Long boundId) {
		configAgentBoundDealInfoDao.delete(boundId);
	}
	
	
	
	@Transactional(readOnly = false)
	public ConfigAgentBoundDealInfo findByAgentIdDealId(Long agentId , Long dealId) {
		DetachedCriteria dc = configAgentBoundDealInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("agent.id", agentId));
		dc.add(Restrictions.eq("dealInfo.id", dealId));
		if (configAgentBoundDealInfoDao.find(dc).size()>0) {
			return configAgentBoundDealInfoDao.find(dc).get(0);
		}else{
			return null;
			
		}
	}
	
	
	
	
	
	
}
