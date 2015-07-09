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
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.dao.WorkCompanyHisDao;

/**
 * 原始单位信息Service
 * @author ZhangShaoBo
 * @version 2014-07-09
 */
@Component
@Transactional(readOnly = true)
public class WorkCompanyHisService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkCompanyHisService.class);
	
	@Autowired
	private WorkCompanyHisDao workCompanyHisDao;
	
	public WorkCompanyHis get(Long id) {
		return workCompanyHisDao.findOne(id);
	}
	
	public Page<WorkCompanyHis> find(Page<WorkCompanyHis> page, WorkCompanyHis workCompanyHis) {
		DetachedCriteria dc = workCompanyHisDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(workCompanyHis.getName())){
//			dc.add(Restrictions.like("name", "%"+workCompanyHis.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(WorkCompanyHis.DEL_FLAG, WorkCompanyHis.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workCompanyHisDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkCompanyHis workCompanyHis) {
		workCompanyHisDao.save(workCompanyHis);
	}
	
	@Transactional(readOnly = false)
	public void save(List<WorkCompanyHis> workCompanyHis) {
		workCompanyHisDao.save(workCompanyHis);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
//		workCompanyHisDao.deleteById(id);
	}
	
}
