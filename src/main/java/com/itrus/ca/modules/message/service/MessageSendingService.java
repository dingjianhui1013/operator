/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.service;

import java.util.Date;
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
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.message.entity.MessageSending;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.message.dao.MessageSendingDao;

/**
 * 消息发送Service
 * @author qt
 * @version 2015-11-30
 */
@Component
@Transactional(readOnly = true)
public class MessageSendingService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(MessageSendingService.class);
	
	@Autowired
	private MessageSendingDao messageSendingDao;
	
	public MessageSending get(Long id) {
		return messageSendingDao.findOne(id);
	}
	public List<MessageSending> findByDepotId(Long checkMessageId) {
		DetachedCriteria dc = messageSendingDao.createDetachedCriteria();
		dc.add(Restrictions.eq("messageSending.id", checkMessageId));
	//	dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return messageSendingDao.find(dc);
	}
	public Page<MessageSending> find(Page<MessageSending> page, MessageSending messageSending) {
		DetachedCriteria dc = messageSendingDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(messageSending.getName())){
			dc.add(Restrictions.like("name", "%"+messageSending.getName()+"%"));
		}
		dc.add(Restrictions.eq(MessageSending.DEL_FLAG, MessageSending.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return messageSendingDao.find(page, dc);
	}
	public Page<MessageSending> find(Page<MessageSending> page, MessageSending messageSending,Long apply,Date startTime,Date endTime) {
		DetachedCriteria dc = messageSendingDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.createAlias("workUser", "workUser");
		dc.createAlias("configApp", "configApp");
		if (messageSending.getWorkCompany() != null
				&& StringUtils.isNotEmpty(messageSending.getWorkCompany().getCompanyName())) {
			dc.add(Restrictions.like("workCompany.companyName",
					"%" + messageSending.getWorkCompany().getCompanyName() + "%"));
		}
		if (messageSending.getWorkUser() != null && StringUtils.isNotEmpty(messageSending.getWorkUser().getContactName())) {
			dc.add(Restrictions.like("workUser.contactName", "%" + messageSending.getWorkUser().getContactName() + "%"));
		}
		if (startTime != null && endTime != null) {
			dc.add(Restrictions.ge("smsSendDate", startTime));
			dc.add(Restrictions.le("smsSendDate", endTime));
		}
		if (apply != null) {
			dc.add(Restrictions.eq("configApp.id", apply));
		}
		if(messageSending.getReturnStatus()!=null&& !messageSending.getReturnStatus().equals("")){
			dc.add(Restrictions.eq("returnStatus", messageSending.getReturnStatus()));
		}
		//dc.add(Restrictions.eq(MessageSending.DEL_FLAG, MessageSending.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return messageSendingDao.find(page, dc);
	}
//	public Page<MessageSending> find(Page<MessageSending> page, MessageSending messageSending,Long apply,Date StartDate,Date endDate){
//		DetachedCriteria dc = messageSendingDao.createDetachedCriteria();
//		//dc.createAlias("workDealInfo", "workDealInfo");
//		//dc.createAlias("workCompany", "workCompany");
//		//dc.createAlias("workUser", "workUser");
//		dc.createAlias("configApp", "configApp");
////		if(messageSending.getWorkDealInfo()!=null&&StringUtils.isNotEmpty(messageSending.getWorkDealInfo())){
////			
////		}
////		if(messageSending.getWorkDealInfo().getWorkCompany()!=null&&StringUtils.isNotEmpty(messageSending.getWorkDealInfo().getWorkCompany().getCompanyName())){
////			dc.add(Restrictions.like("workDealInfo.workCompany.companyName",
////					"%" + messageSending.getWorkDealInfo().getWorkCompany().getCompanyName() + "%"));
////		}
////		if(messageSending.getWorkCompany()!=null&& StringUtils.isNotEmpty(messageSending.getWorkCompany().getCompanyName())){
////			dc.add(Restrictions.like("workCompany.companyName",
////					"%" + messageSending.getWorkCompany().getCompanyName() + "%"));
////		}
////		if (messageSending.getWorkDealInfo().getWorkUser() != null && StringUtils.isNotEmpty(messageSending.getWorkDealInfo().getWorkUser().getContactName())) {
////			dc.add(Restrictions.like("workDealInfo.workUser.contactName", "%" + messageSending.getWorkDealInfo().getWorkUser().getContactName() + "%"));
////		}
////		if (messageSending.getWorkUser() != null && StringUtils.isNotEmpty(messageSending.getWorkUser().getContactName())) {
////			dc.add(Restrictions.like("workUser.contactName", "%" + messageSending.getWorkUser().getContactName() + "%"));
////		}
//		if (apply != null) {
//			dc.add(Restrictions.eq("configApp.id", apply));
//		}
//		if (messageSending.getReturnStatus()!= null && !messageSending.getReturnStatus().equals("")) {
//			dc.add(Restrictions.eq("dealInfoStatus", messageSending.getReturnStatus()));
//		}
//		if (StartDate != null && endDate != null) {
//			dc.add(Restrictions.ge("notafter", StartDate));
//			dc.add(Restrictions.le("notafter", endDate));
//		}
//		return messageSendingDao.find(page, dc);
//		
//	}
	@Transactional(readOnly = false)
	public void save(MessageSending messageSending) {
		messageSendingDao.save(messageSending);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		messageSendingDao.deleteById(id);
	}
	
}
