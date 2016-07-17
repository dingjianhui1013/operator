/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.ixin.service;

import java.util.Date;
import java.util.List;

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
import com.itrus.ca.modules.ixin.dao.IxinDataDao;
import com.itrus.ca.modules.ixin.entity.IxinData;
import com.itrus.ca.modules.profile.entity.ConfigApp;

/**
 * IXIN数据采集Service
 * @author HuHao
 * @version 2016-07-14
 */
@Component
@Transactional(readOnly = true)
public class IxinDataService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(IxinDataService.class);
	
	@Autowired
	private IxinDataDao ixinDataDao;
	
	public IxinData get(Long id) {
		return ixinDataDao.findOne(id);
	}
	
	public Page<IxinData> find(Page<IxinData> page,Date startTime,Date endTime,ConfigApp configApp,IxinData ixinData) {
		DetachedCriteria dc = ixinDataDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
        dc.add(Restrictions.eq("configApp.id", configApp.getId()));
        dc.add(Restrictions.gt("accessTime", startTime));
        dc.add(Restrictions.lt("accessTime", endTime));
		dc.addOrder(Order.desc("id"));
		return ixinDataDao.find(page, dc);
	}
	
   public List<IxinData> findByApp(ConfigApp configApp,Date startTime, Date endTime) {
        DetachedCriteria dc = ixinDataDao.createDetachedCriteria();
        dc.createAlias("configApp", "configApp");
        dc.add(Restrictions.eq("configApp.id",configApp.getId()));
        if(startTime!=null){
            startTime.setHours(0);
            startTime.setMinutes(0);
            startTime.setSeconds(00);
            dc.add(Restrictions.gt("accessTime", startTime));
        }
        if(endTime!=null){
            endTime.setHours(23);
            endTime.setMinutes(59);
            endTime.setSeconds(59);
            dc.add(Restrictions.lt("accessTime", endTime));
        }
        dc.addOrder(Order.desc("id"));
        return ixinDataDao.find(dc);
    }
	
	@Transactional(readOnly = false)
	public void save(IxinData ixinData) {
		ixinDataDao.save(ixinData);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		ixinDataDao.deleteById(id);
	}
	@Transactional(readOnly = false)
	public void saveList(List<IxinData> list) {
	   ixinDataDao.save(list);
    }
	
}
