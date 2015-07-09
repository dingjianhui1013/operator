/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.service;

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
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.dao.WorkUserHisDao;

/**
 * 原始经办人信息Service
 * @author ZhangShaoBo
 * @version 2014-07-09
 */
@Component
@Transactional(readOnly = true)
public class WorkUserHisService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkUserHisService.class);
	
	@Autowired
	private WorkUserHisDao workUserHisDao;
	
	public WorkUserHis get(Long id) {
		return workUserHisDao.findOne(id);
	}
	
	public Page<WorkUserHis> find(Page<WorkUserHis> page, WorkUserHis workUserHis) {
		DetachedCriteria dc = workUserHisDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(workUserHis.getName())){
//			dc.add(Restrictions.like("name", "%"+workUserHis.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(WorkUserHis.DEL_FLAG, WorkUserHis.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workUserHisDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkUserHis workUserHis) {
		workUserHisDao.save(workUserHis);
	}
	
	@Transactional(readOnly = false)
	public void save(List<WorkUserHis> workUserHis) {
		workUserHisDao.save(workUserHis);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
//		workUserHisDao.deleteById(id);
	}
	
}
