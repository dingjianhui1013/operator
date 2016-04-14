/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.service;



import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigProjectType;
import com.itrus.ca.modules.signature.dao.SignatureConfigChargeAgentDao;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgent;


/**
 * 签章计费策略Service
 * @author CYC
 * @version 2016-02-23
 */
@Component
@Transactional(readOnly = true)
public class SignatureConfigChargeAgentService extends BaseService {

	
	
	@Autowired
	private SignatureConfigChargeAgentDao signatureConfigChargeAgentDao;
	
	public SignatureConfigChargeAgent get(Long id) {
		return signatureConfigChargeAgentDao.findOne(id);
	}
	
	
	
	public Page<SignatureConfigChargeAgent> find(Page<SignatureConfigChargeAgent> page, SignatureConfigChargeAgent signatureconfigChargeAgent) {
		DetachedCriteria dc = signatureConfigChargeAgentDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(signatureconfigChargeAgent.getTempName())) {
			dc.add(Restrictions.like("tempName", "%"+signatureconfigChargeAgent.getTempName()+"%"));
		}
		dc.add(Restrictions.eq("delFlag", SignatureConfigChargeAgent.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return signatureConfigChargeAgentDao.find(page, dc);
	}
	
 
	public List<SignatureConfigChargeAgent> findById(Long id){
		DetachedCriteria dc = signatureConfigChargeAgentDao.createDetachedCriteria();
		dc.add(Restrictions.eq("id", id));
		return signatureConfigChargeAgentDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SignatureConfigChargeAgent signatureConfigChargeAgent) {
		signatureConfigChargeAgentDao.save(signatureConfigChargeAgent);
	}
	
	@Transactional(readOnly = false)
	public void unDeleteById(Long id) {
		signatureConfigChargeAgentDao.unDeleteById(id);
	}

	public List<SignatureConfigChargeAgent> checkName(String name,Long id)
	{
		DetachedCriteria dc = signatureConfigChargeAgentDao.createDetachedCriteria();
		if(name!=null)
		{
			dc.add(Restrictions.eq("tempName", name));
		}
		if(id!=null)
		{
			dc.add(Restrictions.ne("id", id));
		}
		dc.add(Restrictions.eq("delFlag", "0"));
		return signatureConfigChargeAgentDao.find(dc);
	}
	





	
	

	
	

	
}
