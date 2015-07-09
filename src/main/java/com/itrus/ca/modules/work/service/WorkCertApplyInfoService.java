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
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.dao.WorkCertApplyInfoDao;

/**
 * 申请人信息Service
 * @author ZhangShaoBo
 * @version 2014-07-07
 */
@Component
@Transactional(readOnly = true)
public class WorkCertApplyInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkCertApplyInfoService.class);
	
	@Autowired
	private WorkCertApplyInfoDao workCertApplyInfoDao;
	
	public WorkCertApplyInfo get(Long id) {
		return workCertApplyInfoDao.findOne(id);
	}
	
	public Page<WorkCertApplyInfo> find(Page<WorkCertApplyInfo> page, WorkCertApplyInfo workCertApplyInfo) {
		DetachedCriteria dc = workCertApplyInfoDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(workCertApplyInfo.getName())){
			dc.add(Restrictions.like("name", "%"+EscapeUtil.escapeLike(workCertApplyInfo.getName())+"%"));
		}
		//dc.add(Restrictions.eq(WorkCertApplyInfo.DEL_FLAG, WorkCertApplyInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workCertApplyInfoDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkCertApplyInfo workCertApplyInfo) {
		workCertApplyInfoDao.save(workCertApplyInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(List<WorkCertApplyInfo> workCertApplyInfo) {
		workCertApplyInfoDao.save(workCertApplyInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
	}
	
}
