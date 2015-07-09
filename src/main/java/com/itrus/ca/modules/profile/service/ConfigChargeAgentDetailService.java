/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

import java.util.ArrayList;
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
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.constant.WorkType;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.profile.dao.ConfigChargeAgentDetailDao;

/**
 * 供应商计费type配置Service
 * @author ZhangShaoBo
 * @version 2014-06-11
 */
@Component
@Transactional(readOnly = true)
public class ConfigChargeAgentDetailService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigChargeAgentDetailService.class);
	
	@Autowired
	private ConfigChargeAgentDetailDao configChargeAgentDetailDao;
	
	public ConfigChargeAgentDetail get(Long id) {
		return configChargeAgentDetailDao.findOne(id);
	}
	
	public Page<ConfigChargeAgentDetail> find(Page<ConfigChargeAgentDetail> page, ConfigChargeAgentDetail configChargeAgentDetail) {
		DetachedCriteria dc = configChargeAgentDetailDao.createDetachedCriteria();

		dc.addOrder(Order.desc("id"));
		return configChargeAgentDetailDao.find(page, dc);
	}
	
	public List<ConfigChargeAgentDetail> findByConfigChargeAgent(ConfigChargeAgent configChargeAgent){
		DetachedCriteria dc = configChargeAgentDetailDao.createDetachedCriteria();
		dc.add(Restrictions.like("configChargeAgent", configChargeAgent));
		return configChargeAgentDetailDao.find(dc);
	}
	/**
	 * 根据type得到钱
	 * @param configChargeAgent
	 * @param workType 
	 * @see com.itrus.ca.modules.constant.WorkType
	 * @param year
	 * @param productType
	 * @return
	 */
	public Double selectMoney(ConfigChargeAgent configChargeAgent, Integer dealInfoType, Integer year, Integer productType){
		Integer workType = WorkDealInfoType.getWorkType(dealInfoType);
		DetachedCriteria dc = configChargeAgentDetailDao.createDetachedCriteria();
		dc.add(Restrictions.like("configChargeAgent", configChargeAgent));
		dc.add(Restrictions.eq("workType", workType));
		List<Integer> yearScopeTypeList = new ArrayList<Integer>();
		yearScopeTypeList.add(WorkDealInfoType.TYPE_ADD_CERT);
		yearScopeTypeList.add(WorkDealInfoType.TYPE_TRUST_MOBILE);
		yearScopeTypeList.add(WorkDealInfoType.TYPE_UPDATE_CERT);
		
		if (year!=null&&yearScopeTypeList.contains(dealInfoType)) {
			if (year == 3) {
				year = 4;
			}
			dc.add(Restrictions.like("chargeYear", year));
		}
		dc.add(Restrictions.like("productType", productType));
		List<ConfigChargeAgentDetail> list =  configChargeAgentDetailDao.find(dc);
		if (list.size()>0) {
			return list.get(0).getMoney();
		}
		return null;
	}
	/**
	 * 查询可移动设备半年的钱
	 * @param configChargeAgent
	 * @param productType
	 * @return
	 */
	public Double selectTrust0Money(ConfigChargeAgent configChargeAgent,Integer productType){
		DetachedCriteria dc = configChargeAgentDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configChargeAgent", configChargeAgent));
		dc.add(Restrictions.eq("workType", WorkType.TYPE_TRUST));
		dc.add(Restrictions.eq("chargeYear", 0));
		dc.add(Restrictions.eq("productType", productType));
		List<ConfigChargeAgentDetail> list = configChargeAgentDetailDao.find(dc);
		if (list.size()>0) {
			return list.get(0).getMoney();
		}
		return null;
	}
	/**
	 * 查询可移动设备1年的钱
	 * @param configChargeAgent
	 * @param productType
	 * @return
	 */
	public Double selectTrust1Money(ConfigChargeAgent configChargeAgent,Integer productType){
		DetachedCriteria dc = configChargeAgentDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configChargeAgent", configChargeAgent));
		dc.add(Restrictions.eq("workType", WorkType.TYPE_TRUST));
		dc.add(Restrictions.eq("chargeYear", 1));
		dc.add(Restrictions.eq("productType", productType));
		List<ConfigChargeAgentDetail> list = configChargeAgentDetailDao.find(dc);
		if (list.size()>0) {
			return list.get(0).getMoney();
		}
		return null;
	}
	
	@Transactional(readOnly = false)
	public void save(ConfigChargeAgentDetail configChargeAgentDetail) {
		configChargeAgentDetailDao.clear();
		configChargeAgentDetailDao.save(configChargeAgentDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
	}
	
	@Transactional(readOnly = false)
	public void deleteByAgentId(Long agentId){
		if (agentId==null) {
			return;
		}
		configChargeAgentDetailDao.deleteByAgentId(agentId);
	}

	/**
	 *  根据产品和办理类型来获取对应的年限
	 * @param chargeAgentId
	 * @param workType
	 * @return
	 */
	@Transactional(readOnly = true)
	public String[] getChargeAgentYears(Long chargeAgentId , Integer workType){
		DetachedCriteria detachedCriteria = configChargeAgentDetailDao.createDetachedCriteria();
		detachedCriteria.add(Restrictions.eq("configChargeAgent.id",chargeAgentId));
		detachedCriteria.add(Restrictions.eq("workType",workType));
		List<ConfigChargeAgentDetail> list = configChargeAgentDetailDao.find(detachedCriteria);
		String[] years = new String[list.size()];
		for(int i = 0 ; i < list.size(); i++){
			ConfigChargeAgentDetail configChargeAgentDetail = list.get(i);
			years[i] = configChargeAgentDetail.getChargeYear().toString();
		}
		return years;
	}

	/**
	 *	获得产品绑定的计费策略 对应年限的金额
	 * @param chargeAgentId 模板id
	 * @param workType	业务类型
	 * @param chargeYear 缴费年限 可为空
	 * @return
	 */
	@Transactional(readOnly = true)
	public double getChargeMoney(Long chargeAgentId, Integer workType, Integer chargeYear){
		DetachedCriteria detachedCriteria = configChargeAgentDetailDao.createDetachedCriteria();
		detachedCriteria.add(Restrictions.eq("configChargeAgent.id",chargeAgentId));
		detachedCriteria.add(Restrictions.eq("workType",workType));
		if(chargeYear != null){
			//如果是更新和新增并且年限不为空根据年限查询
			if (WorkType.TYPE_ADD.equals( workType) || WorkType.TYPE_UPDATE.equals( workType)) {
				detachedCriteria.add(Restrictions.eq("chargeYear",chargeYear));
			}
		}
		List<ConfigChargeAgentDetail> list  = configChargeAgentDetailDao.find(detachedCriteria);
		double money =0;
		if(list.size()>0)
		{
			ConfigChargeAgentDetail configChargeAgentDetail = list.get(0);
			money = configChargeAgentDetail.getMoney();
		}
		return money;
	}
	
}
