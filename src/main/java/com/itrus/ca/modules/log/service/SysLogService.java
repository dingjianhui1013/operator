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
import com.itrus.ca.modules.log.entity.SysOperateLog;
import com.itrus.ca.modules.log.dao.SysOperateLogDao;

/**
 * 后台日志Service
 * @author ZhangJingtao
 * @version 2014-07-07
 */
@Component
@Transactional(readOnly = true)
public class SysLogService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SysLogService.class);
	
	@Autowired
	private SysOperateLogDao sysOperateLogDao;
	
	public SysOperateLog get(Long id) {
		return sysOperateLogDao.findOne(id);
	}
	
	public Page<SysOperateLog> find(Page<SysOperateLog> page, SysOperateLog sysOperateLog) {
		DetachedCriteria dc = sysOperateLogDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(sysOperateLog.getType())){
			dc.add(Restrictions.like("type", "%"+EscapeUtil.escapeLike(sysOperateLog.getType())+"%"));
		}
		if (sysOperateLog.getCreateBy()!=null&&StringUtils.isNotEmpty(sysOperateLog.getCreateBy().getName())){
			dc.createAlias("createBy", "createBy");
			dc.add(Restrictions.like("createBy.name", "%"+EscapeUtil.escapeLike(sysOperateLog.getCreateBy().getName())+"%"));
		}
		dc.add(Restrictions.eq(SysOperateLog.DEL_FLAG, SysOperateLog.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return sysOperateLogDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SysOperateLog sysOperateLog) {
		sysOperateLogDao.save(sysOperateLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		sysOperateLogDao.deleteById(id);
	}
	
}
