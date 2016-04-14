/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.signature.dao.SignatureConfigChargeAgentDetailDao;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgent;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgentDetail;
import com.itrus.ca.modules.profile.dao.ConfigChargeAgentDetailDao;

/**
 * 签章配置Service
 * @author CYC
 * @version 2016-03-14
 */
@Component
@Transactional(readOnly = true)
public class SignatureConfigChargeAgentDetailService extends BaseService {

	
	
	
	@Autowired
	private SignatureConfigChargeAgentDetailDao signatureChargeAgentDetailDao;
	
	public SignatureConfigChargeAgentDetail get(Long id) {
		return signatureChargeAgentDetailDao.findOne(id);
	}
	
	public Page<SignatureConfigChargeAgentDetail> find(Page<SignatureConfigChargeAgentDetail> page, SignatureConfigChargeAgentDetail configChargeAgentDetail) {
		DetachedCriteria dc = signatureChargeAgentDetailDao.createDetachedCriteria();

		dc.addOrder(Order.desc("id"));
		return signatureChargeAgentDetailDao.find(page, dc);
	}
	
	public List<SignatureConfigChargeAgentDetail> findBySConfigChargeAgent(SignatureConfigChargeAgent signatureConfigChargeAgent){
		DetachedCriteria dc = signatureChargeAgentDetailDao.createDetachedCriteria();
		dc.add(Restrictions.like("signatureConfigChargeAgent", signatureConfigChargeAgent));
		return signatureChargeAgentDetailDao.find(dc);
	}
	
	public List<SignatureConfigChargeAgentDetail> findByAgentId(Long agentId ,Integer workType){
		DetachedCriteria dc = signatureChargeAgentDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("signatureConfigChargeAgent.id", agentId));
		dc.add(Restrictions.eq("workType", workType));
		Order.asc("chargeYear");
		return signatureChargeAgentDetailDao.find(dc);
	}
	
	
	public SignatureConfigChargeAgentDetail findByAgentId(Long agentId,Integer workType,Integer year){
		DetachedCriteria dc = signatureChargeAgentDetailDao.createDetachedCriteria();
		dc.add(Restrictions.eq("signatureConfigChargeAgent.id", agentId));
		dc.add(Restrictions.eq("workType", workType));
		dc.add(Restrictions.eq("chargeYear", year));
		
		List<SignatureConfigChargeAgentDetail> details = signatureChargeAgentDetailDao.find(dc);
		
		if(details.size()==1){
			return details.get(0);
		}else{
			return null;
		}
	}
	

	
	@Transactional(readOnly = false)
	public void save(SignatureConfigChargeAgentDetail configChargeAgentDetail) {
		signatureChargeAgentDetailDao.clear();
		signatureChargeAgentDetailDao.save(configChargeAgentDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
	}
	
	@Transactional(readOnly = false)
	public void deleteByAgentId(Long agentId){
		if (agentId==null) {
			return;
		}
		signatureChargeAgentDetailDao.deleteByAgentId(agentId);
	}

	
	/*
	 * 根据应用和业务类型找到
	 * */
	
	@Transactional(readOnly = true)
	public Map<Integer,Double> getChargeAgentYears(Long signatureChargeAgentId , Integer workType){
		DetachedCriteria detachedCriteria = signatureChargeAgentDetailDao.createDetachedCriteria();
		detachedCriteria.add(Restrictions.eq("signatureConfigChargeAgent.id",signatureChargeAgentId));
		detachedCriteria.add(Restrictions.eq("workType",workType));
		List<SignatureConfigChargeAgentDetail> list = signatureChargeAgentDetailDao.find(detachedCriteria);
		
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		
		for(int i = 0 ; i < list.size(); i++){
			SignatureConfigChargeAgentDetail signatureConfigChargeAgentDetail = list.get(i);
			map.put(signatureConfigChargeAgentDetail.getChargeYear(), signatureConfigChargeAgentDetail.getMoney());
		}
		return map;
	}
	
	
	
	@Transactional(readOnly = true)
	public Double getMoney(Long signatureChargeAgentId,Integer workType){
		DetachedCriteria detachedCriteria = signatureChargeAgentDetailDao.createDetachedCriteria();
		detachedCriteria.add(Restrictions.eq("signatureConfigChargeAgent.id",signatureChargeAgentId));
		detachedCriteria.add(Restrictions.eq("workType",workType));
		List<SignatureConfigChargeAgentDetail> list = signatureChargeAgentDetailDao.find(detachedCriteria);
		
		return list.get(0).getMoney();
	}
	
	
}
