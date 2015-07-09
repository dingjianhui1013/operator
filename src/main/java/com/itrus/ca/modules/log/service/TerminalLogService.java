/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.log.service;

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
import com.itrus.ca.modules.log.entity.TerminalLog;
import com.itrus.ca.modules.log.dao.TerminalLogDao;

/**
 * 前台日志Service
 * @author ZhangJingtao
 * @version 2014-07-07
 */
@Component
@Transactional(readOnly = true)
public class TerminalLogService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(TerminalLogService.class);
	
	@Autowired
	private TerminalLogDao terminalLogDao;
	
	public TerminalLog get(Long id) {
		return terminalLogDao.findOne(id);
	}
	
	public Page<TerminalLog> find(Page<TerminalLog> page, TerminalLog terminalLog) {
		DetachedCriteria dc = terminalLogDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(terminalLog.getType())){
			dc.add(Restrictions.like("type", "%"+EscapeUtil.escapeLike(terminalLog.getType())+"%"));
		}
		//dc.add(Restrictions.eq(TerminalLog.DEL_FLAG, TerminalLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return terminalLogDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(TerminalLog terminalLog) {
		terminalLogDao.save(terminalLog);
	}
	
//	@Transactional(readOnly = false)
//	public void delete(Long id) {
//		terminalLogDao.deleteById(id);
//	}
	
}
