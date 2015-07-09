package com.itrus.ca.modules.sys.service;



import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.sys.dao.CommonAttachDao;
import com.itrus.ca.modules.sys.entity.CommonAttach;


/**
 * 附件Service
 * @author ZhangJingtao
 * @version 2013-12-16
 */
@Component
@Transactional(readOnly = true)
public class CommonAttachService extends BaseService {

	@Autowired
	private CommonAttachDao attachDao;
	
	
	@Transactional(readOnly = false)
	public void saveAttach(CommonAttach attach) {
		attachDao.save(attach);
	}
	
	public List<CommonAttach> findContentAttach(String attachGroupId){
		DetachedCriteria dc = attachDao.createDetachedCriteria();
		dc.add(Restrictions.eq("groupid", attachGroupId));
		return attachDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void delAttach(Long id){
		attachDao.delete(id);
	}
}
