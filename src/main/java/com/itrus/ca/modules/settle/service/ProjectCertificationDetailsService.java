/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

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
import com.itrus.ca.modules.settle.entity.ProjectCertificationDetails;
import com.itrus.ca.modules.settle.dao.ProjectCertificationDetailsDao;

/**
 * 项目发证明细Service
 * @author qt
 * @version 2015-11-18
 */
@Component
@Transactional(readOnly = true)
public class ProjectCertificationDetailsService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ProjectCertificationDetailsService.class);
	
	@Autowired
	private ProjectCertificationDetailsDao projectCertificationDetailsDao;
	
	public ProjectCertificationDetails get(Long id) {
		return projectCertificationDetailsDao.findOne(id);
	}
	
	public Page<ProjectCertificationDetails> find(Page<ProjectCertificationDetails> page, ProjectCertificationDetails projectCertificationDetails,Long workDealInfoId) {
		DetachedCriteria dc = projectCertificationDetailsDao.createDetachedCriteria();
		if (workDealInfoId!=null) {
			dc.add(Restrictions.eq("workDealInfoVo.id", workDealInfoId));
		}
		dc.add(Restrictions.eq(ProjectCertificationDetails.DEL_FLAG, ProjectCertificationDetails.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return projectCertificationDetailsDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectCertificationDetails projectCertificationDetails) {
		projectCertificationDetailsDao.save(projectCertificationDetails);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		projectCertificationDetailsDao.deleteById(id);
	}
	
}
