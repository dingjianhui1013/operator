/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.message.entity.SmsConfiguration;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.message.dao.SmsConfigurationDao;

/**
 * 短信配置Service
 * @author qt
 * @version 2015-11-27
 */
@Component
@Transactional(readOnly = true)
public class SmsConfigurationService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SmsConfigurationService.class);
	
	@Autowired
	private SmsConfigurationDao smsConfigurationDao;
	
	public SmsConfiguration get(Long id) {
		return smsConfigurationDao.findOne(id);
	}
	
	public Page<SmsConfiguration> find(Page<SmsConfiguration> page, SmsConfiguration smsConfiguration) {
		DetachedCriteria dc = smsConfigurationDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(smsConfiguration.getMessageName())){
			dc.add(Restrictions.like("messageName", "%"+smsConfiguration.getMessageName()+"%"));
		}
		dc.add(Restrictions.eq(SmsConfiguration.DEL_FLAG, SmsConfiguration.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return smsConfigurationDao.find(page, dc);
	}
	public List<SmsConfiguration> findAll(){
		DetachedCriteria dc = smsConfigurationDao.createDetachedCriteria();
		dc.addOrder(Order.asc("id"));
		dc.add(Restrictions.eq(SmsConfiguration.DEL_FLAG, SmsConfiguration.DEL_FLAG_NORMAL));
		return smsConfigurationDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SmsConfiguration smsConfiguration) {
		smsConfigurationDao.save(smsConfiguration);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		smsConfigurationDao.deleteById(id);
	}
	@Transactional(readOnly = true)
	public List findByRaName(String messageName) {
		DetachedCriteria dc = smsConfigurationDao.createDetachedCriteria();
		dc.add(Restrictions.eq("messageName", messageName));
		List<SmsConfiguration> list = smsConfigurationDao.find(dc);
		return list;
	}
	public SmsConfiguration findByMessageName(String messageName){
		DetachedCriteria dc = smsConfigurationDao.createDetachedCriteria();
		dc.add(Restrictions.eq("messageName", messageName));
		
		if (smsConfigurationDao.find(dc).size()>0) {
			return smsConfigurationDao.find(dc).get(0);
		}else {
			return null;
		}
	}
}
