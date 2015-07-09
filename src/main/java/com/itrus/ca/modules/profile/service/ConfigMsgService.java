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
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.profile.entity.ConfigMsg;
import com.itrus.ca.modules.profile.dao.ConfigMsgDao;

/**
 * 消息Service
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class ConfigMsgService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigMsgService.class);
	
	@Autowired
	private ConfigMsgDao configMsgDao;
	
	public ConfigMsg get(Long id) {
		return configMsgDao.findOne(id);
	}
	
	public Page<ConfigMsg> find(Page<ConfigMsg> page, ConfigMsg configMsg) {
		DetachedCriteria dc = configMsgDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configMsg.getMsgName())){
			dc.add(Restrictions.like("msgName", "%"+EscapeUtil.escapeLike(configMsg.getMsgName())+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return configMsgDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ConfigMsg configMsg) {
		configMsgDao.save(configMsg);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configMsgDao.deleteById(id);
	}
	
	/**
	 * 查询模板内容
	 * @param type
	 * @param msgType
	 * @return
	 */
	@Transactional(readOnly = false)
	public String gentContent(Integer type,Integer method){
		DetachedCriteria dc = configMsgDao.createDetachedCriteria();
		dc.add(Restrictions.eq("msgType", type));
		dc.add(Restrictions.eq("msgSendMethod", method));
		List<ConfigMsg> msg = configMsgDao.find(dc);
		if (msg!=null&&msg.size()!=0) {
			return msg.get(0).getMsgContent();
		}else {
			return "未配置邮件模板";
		}
	}
}
