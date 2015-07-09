/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.sys.service;

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
import com.itrus.ca.modules.sys.entity.SysCrlContext;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.dao.SysCrlContextDao;

/**
 * 信任源管理Service
 * @author WangHongwei
 * @version 2014-06-03
 */
@Component
@Transactional(readOnly = true)
public class SysCrlContextService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SysCrlContextService.class);
	
	@Autowired
	private SysCrlContextDao sysCrlContextDao;
	
	public SysCrlContext get(Long id) {
		return sysCrlContextDao.findOne(id);
	}
	
	public Page<SysCrlContext> find(Page<SysCrlContext> page) {
		DetachedCriteria dc = sysCrlContextDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(SysCrlContext.DEL_FLAG, SysCrlContext.DEL_FLAG_NORMAL));
		return sysCrlContextDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SysCrlContext sysCrlContext) {
		sysCrlContextDao.save(sysCrlContext);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		sysCrlContextDao.deleteById(id);
	}
	
}
