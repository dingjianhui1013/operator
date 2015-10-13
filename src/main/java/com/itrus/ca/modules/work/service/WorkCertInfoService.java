/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.service;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.dao.WorkCertInfoDao;

/**
 * 证书信息Service
 * 
 * @author ZhangJingtao
 * @version 2014-06-16
 */
@Component
@Transactional(readOnly = true)
public class WorkCertInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(WorkCertInfoService.class);

	@Autowired
	private WorkCertInfoDao workCertInfoDao;

	public WorkCertInfo get(Long id) {
		return workCertInfoDao.findOne(id);
	}

	/*
	 * 
	 * 此方法是为了兼容之前的对create Date 数据处理不够来临时增加的update方法，正常后，直接可以通过 workCertInfo.getCreateDate获取。
	 * 
	 */
	public Date getCreateDate(Long id) {
		WorkCertInfo wci = workCertInfoDao.findOne(id);
		if (wci == null) { // 证书不存在
			return null;
		} else if (wci.getCreateDate() != null) { // 有create Date 直接同步
			return wci.getCreateDate();
		} else if (wci.getRenewalPrevId() != null) { // create Date 为空且
														// 证书存在之前的证书id，递归查找
			return getCreateDate(wci.getRenewalPrevId());
		} else if (wci.getSignDate() != null) { // 已经不存在之前的id了，最早的创建记录
			// create date 未记录，同步signDate
			wci.setCreateDate(wci.getSignDate());
			workCertInfoDao.save(wci);
			return wci.getCreateDate();
		} else { // 如果证书还未发放返回空值
			return null;
		}

	}

	public Page<WorkCertInfo> find(Page<WorkCertInfo> page,
			WorkCertInfo workCertInfo) {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(workCertInfo.getSerialnumber())) {
			dc.add(Restrictions.like("serialnumber",
					"%" + EscapeUtil.escapeLike(workCertInfo.getSerialnumber())
							+ "%"));
		}
		dc.addOrder(Order.desc("id"));
		return workCertInfoDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(WorkCertInfo workCertInfo) {
		workCertInfoDao.save(workCertInfo);
	}

	@Transactional(readOnly = false)
	public void save(List<WorkCertInfo> workCertInfo) {
		workCertInfoDao.save(workCertInfo);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {

	}

	@Transactional(readOnly = false)
	public WorkCertInfo getByCertSn(String certSn) {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("serialnumber", certSn));
		List<WorkCertInfo> certInfos = workCertInfoDao.find(dc);
		if (certInfos.size()>0) {
			return certInfos.get(0);
		} else {
			return new WorkCertInfo();
		}
	}

	/**
	 * 获得当前需要生成新增业务的certInfo
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<WorkCertInfo> getCertInfoNeedAdd() {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		dc.add(Restrictions.isNotNull("workUserId"));
		List<WorkCertInfo> certInfos = workCertInfoDao.find(dc);
		return certInfos;
	}

	@Transactional(readOnly = true)
	public List<WorkCertInfo> getWorkInfos() {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		dc.add(Restrictions.isNotNull("workUserId"));
		List<WorkCertInfo> certInfos = workCertInfoDao.find(dc);
		return certInfos;
	}
	
	
	public List<WorkCertInfo> findZhiZhengTime(Date zhizhengStartTime,Date zhizhengEndTime) {
		DetachedCriteria dc = workCertInfoDao.createDetachedCriteria();
		if (zhizhengStartTime!=null) {
			dc.add(Restrictions.ge("signDate", zhizhengStartTime));
		}
		if (zhizhengEndTime!=null) {
			zhizhengEndTime.setHours(23);
			zhizhengEndTime.setMinutes(59);
			zhizhengEndTime.setSeconds(59);
			dc.add(Restrictions.le("signDate", zhizhengEndTime));
		}
		dc.addOrder(Order.desc("id"));
		return workCertInfoDao.find(dc);
	}
	
	
	
	

}
