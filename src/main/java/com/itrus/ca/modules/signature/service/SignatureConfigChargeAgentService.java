/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.service;



import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
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
	
	
	public Page<SignatureConfigChargeAgent> find(Page<SignatureConfigChargeAgent> page, SignatureConfigChargeAgent signatureconfigChargeAgent) {
		DetachedCriteria dc = signatureConfigChargeAgentDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(signatureconfigChargeAgent.getTempName())) {
			dc.add(Restrictions.like("tempName", "%"+signatureconfigChargeAgent.getTempName()+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return signatureConfigChargeAgentDao.find(page, dc);
	}
	

	


	
	





	
	

	
	

	
}
