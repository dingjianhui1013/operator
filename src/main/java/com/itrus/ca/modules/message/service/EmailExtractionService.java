/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.service;

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
import com.itrus.ca.modules.message.entity.EmailExtraction;
import com.itrus.ca.modules.message.dao.EmailExtractionDao;

/**
 * 邮箱提取Service
 * @author qt
 * @version 2015-11-30
 */
@Component
@Transactional(readOnly = true)
public class EmailExtractionService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(EmailExtractionService.class);
	
	@Autowired
	private EmailExtractionDao emailExtractionDao;
	
	public EmailExtraction get(Long id) {
		return emailExtractionDao.findOne(id);
	}
	
	public Page<EmailExtraction> find(Page<EmailExtraction> page, EmailExtraction emailExtraction,Long workDealInfoId) {
		DetachedCriteria dc = emailExtractionDao.createDetachedCriteria();
		if (workDealInfoId!=null) {
			dc.add(Restrictions.eq("workDealInfoVo.id", workDealInfoId));
		}
		dc.add(Restrictions.eq(EmailExtraction.DEL_FLAG, EmailExtraction.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return emailExtractionDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(EmailExtraction emailExtraction) {
		emailExtractionDao.save(emailExtraction);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		emailExtractionDao.deleteById(id);
	}
	
}
