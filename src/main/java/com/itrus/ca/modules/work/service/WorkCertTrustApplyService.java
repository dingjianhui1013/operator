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
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertTrustApply;
import com.itrus.ca.modules.work.dao.WorkCertTrustApplyDao;

/**
 * 申请移动设备数量Service
 * @author ZhangJingtao
 * @version 2014-07-05
 */
@Component
@Transactional(readOnly = true)
public class WorkCertTrustApplyService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkCertTrustApplyService.class);
	
	@Autowired
	private WorkCertTrustApplyDao workCertTrustApplyDao;
	
	public WorkCertTrustApply get(Long id) {
		return workCertTrustApplyDao.findOne(id);
	}
	
	public Page<WorkCertTrustApply> find(Page<WorkCertTrustApply> page, WorkCertTrustApply workCertTrustApply) {
		DetachedCriteria dc = workCertTrustApplyDao.createDetachedCriteria();
		
		if (!StringHelper.isNull(workCertTrustApply.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%"+EscapeUtil.escapeLike(workCertTrustApply.getKeySn())+"%"));
		}
		if (!StringHelper.isNull(workCertTrustApply.getCertSn())) {
			dc.add(Restrictions.like("certSn", "%"+EscapeUtil.escapeLike(workCertTrustApply.getCertSn())+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return workCertTrustApplyDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkCertTrustApply workCertTrustApply) {
		workCertTrustApplyDao.save(workCertTrustApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
	}
	/**
	 * 查询在申请的移动设备证书
	 * @param certSn
	 * @return
	 */
	public WorkCertTrustApply getTrustByStatus(String certSn){
		DetachedCriteria dc = workCertTrustApplyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("certSn", certSn));
		dc.add(Restrictions.eq("state", 0));
		dc.addOrder(Order.desc("id"));
		List<WorkCertTrustApply> all = workCertTrustApplyDao.find(dc);
		if (all!=null&&all.size()!=0) {
			return all.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 查询已存在的业务编号的数量
	 * @param snStr
	 * @return
	 */
	public int countClientInfoAmount(String snStr){
		DetachedCriteria dc = workCertTrustApplyDao.createDetachedCriteria();
		dc.add(Restrictions.like("sn", EscapeUtil.escapeLike(snStr)+"%"));
		return (int) workCertTrustApplyDao.count(dc);
	}

	public WorkCertTrustApply findByKeySn(String keySn) {
		DetachedCriteria dc = workCertTrustApplyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keySn", keySn));
		dc.addOrder(Order.desc("id"));
		List<WorkCertTrustApply> w= workCertTrustApplyDao.find(dc);
		if(w.size()>0){
			return w.get(0);
		}
		return null;
	}
}
