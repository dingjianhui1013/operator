/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.service;

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
import com.itrus.ca.modules.signature.entity.SignatureInfo;
import com.itrus.ca.modules.signature.entity.SignaturePayInfo;
import com.itrus.ca.modules.signature.dao.SignaturePayInfoDao;

/**
 * 计费策略模版详情历史Service
 * @author HuHao
 * @version 2016-03-23
 */
@Component
@Transactional(readOnly = true)
public class SignaturePayInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SignaturePayInfoService.class);
	
	@Autowired
	private SignaturePayInfoDao signaturePayInfoDao;
	
	public SignaturePayInfo get(Long id) {
		return signaturePayInfoDao.findOne(id);
	}
	
	public Page<SignaturePayInfo> find(Page<SignaturePayInfo> page, SignaturePayInfo signaturePayInfo) {
		DetachedCriteria dc = signaturePayInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(SignaturePayInfo.DEL_FLAG, SignaturePayInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return signaturePayInfoDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SignaturePayInfo signaturePayInfo) {
		signaturePayInfoDao.save(signaturePayInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		signaturePayInfoDao.deleteById(id);
	}
	public SignaturePayInfo findBySignatureInfo(SignatureInfo signatureInfo)
	{
		DetachedCriteria dc = signaturePayInfoDao.createDetachedCriteria();
		dc.createAlias("signatureInfo", "signatureInfo");
		dc.add(Restrictions.eq("signatureInfo", signatureInfo));
		dc.add(Restrictions.eq(SignaturePayInfo.DEL_FLAG, SignaturePayInfo.DEL_FLAG_NORMAL));
		return signaturePayInfoDao.find(dc).get(0);
	}
	
}
