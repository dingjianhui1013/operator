/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.service;

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

import com.alibaba.fastjson.JSON;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.dao.WorkUserDao;
import com.sun.mail.imap.protocol.Status;

/**
 * 联系人Service
 * @author WangHongwei
 * @version 2014-06-13
 */
@Component
@Transactional(readOnly = true)
public class WorkUserService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkUserService.class);
	
	@Autowired
	private WorkUserDao workUserDao;
	
	@Autowired
	private WorkCompanyService workCompanyService;
	
	@Autowired
	private WorkCompanyHisService workCompanyHisService;
	
	public WorkUser get(Long id) {
		return workUserDao.findOne(id);
	}
	
	public Page<WorkUser> find(Page<WorkUser> page, WorkUser workUser) {
		DetachedCriteria dc = workUserDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		if(workUser.getWorkCompany()!=null){
//			if(workUser.getWorkCompany().getId()!=null){
//				dc.add(Restrictions.eq("workCompany", workUser.getWorkCompany()));
//			}
			if(workUser.getWorkCompany().getCompanyName()!=null && !workUser.getWorkCompany().getCompanyName().equals("")){
				dc.add(Restrictions.like("workCompany.companyName","%"+EscapeUtil.escapeLike(workUser.getWorkCompany().getCompanyName())+"%"));
			}
		}
		if(workUser.getStatus()!=null&&workUser.getStatus()==2){
			dc.add(Restrictions.eq("status", workUser.getStatus()));
		}
		if(workUser.getContactName()!=null&&!workUser.getContactName().equals("")){
			dc.add(Restrictions.like("contactName","%"+EscapeUtil.escapeLike(workUser.getContactName())+"%"));
		}
		if(workUser.getContactPhone()!=null&&!workUser.getContactPhone().equals("")){
			dc.add(Restrictions.like("contactPhone","%"+EscapeUtil.escapeLike(workUser.getContactPhone())+"%"));
		}
		if(workUser.getContactTel()!=null&&!workUser.getContactTel().equals("")){
			dc.add(Restrictions.like("contactTel","%"+EscapeUtil.escapeLike(workUser.getContactTel())+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return workUserDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkUser workUser) {
		workUserDao.save(workUser);
	}
	
	@Transactional(readOnly = false)
	public void save(List<WorkUser> workUser) {
		workUserDao.save(workUser);
	}

	public List<WorkUser> findUserList(WorkCompany workCompany) {
		DetachedCriteria dc = workUserDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		dc.add(Restrictions.eq("workCompany.id", workCompany.getId()));
		dc.addOrder(Order.desc("id"));
		return workUserDao.find(dc);
		
	}
	public WorkUserHis change(WorkUser workUser, WorkCompanyHis workCompanyHis){
		WorkUserHis workUserHis = new WorkUserHis();
		workUserHis.setAddress(workUser.getAddress());
		workUserHis.setConCertNumber(workUser.getConCertNumber());
		workUserHis.setConCertType(workUser.getConCertType());
		workUserHis.setContactEmail(workUser.getContactEmail());
		workUserHis.setContactName(workUser.getContactName());
		workUserHis.setContactPhone(workUser.getContactPhone());
		workUserHis.setContactSex(workUser.getContactSex());
		workUserHis.setContactTel(workUser.getContactTel());
		workUserHis.setDepartment(workUser.getDepartment());
		workUserHis.setSource(workUser.getSource());
		workUserHis.setStatus(workUser.getStatus());
		workUserHis.setUserSn(workUser.getUserSn());
		workUserHis.setWorkCompanyHis(workCompanyHis);
		workUserHis.setWorkType(workUser.getWorkType());
		return workUserHis;
	}
}
