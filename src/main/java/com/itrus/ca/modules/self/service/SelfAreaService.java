package com.itrus.ca.modules.self.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.self.dao.SelfAreaDao;
import com.itrus.ca.modules.self.entity.SelfArea;

/**
 * 地区选择服务
 * 
 * @author Jack
 * @date 2015-2-9
 * @packageName cn.com.scca.service
 *
 */
@Service
public class SelfAreaService extends BaseService {

    private static final Log logger = LogFactory.getLog(SelfAreaService.class);

    @Autowired
    private SelfAreaDao selfAreaDao;

    public SelfArea get(Long id) {
        return selfAreaDao.findOne(id);
    }

    /**
     * 得到省
     */
    public List<SelfArea> getProvinceJsonData() {
        DetachedCriteria dc = selfAreaDao.createDetachedCriteria();
        dc.add(Restrictions.eq("parentId", BaseEntity.HIDE));
        dc.add(Restrictions.eq("status", "启用"));
        dc.add(Restrictions.eq("areaLevel", "省"));
       // dc.add(Restrictions.like("datastandard", "%四川省地方税务局%"));
        dc.addOrder(Order.asc("lastmodifydate"));
        return selfAreaDao.find(dc);
    }
    
    /**
     * 根据省市得到下面的市区
     * @param provinceCode
     * @return
     */
    public  List<SelfArea> getCityJsonData(String provinceCode) {
        DetachedCriteria dc = selfAreaDao.createDetachedCriteria();
        dc.add(Restrictions.eq("parentId", provinceCode));
        dc.add(Restrictions.eq("status", "启用"));
        //dc.add(Restrictions.like("datastandard", "%四川省地方税务局%"));
        dc.addOrder(Order.asc("parentId"));
        return selfAreaDao.find(dc);
    }
    
    /**
     * 根据市区得到下面的城镇
     * @param provinceCode
     * @return
     */
    public  List<SelfArea> getTownJsonData(String cityCode) {
        DetachedCriteria dc = selfAreaDao.createDetachedCriteria();
        dc.add(Restrictions.eq("parentId", cityCode));
        dc.add(Restrictions.eq("status", "启用"));
        dc.add(Restrictions.like("datastandard", "%四川省地方税务局%"));
        dc.addOrder(Order.asc("parentId"));
        return selfAreaDao.find(dc);
    }
    
    /**
     * 得到参保区县
     * @param provinceCode
     * @return
     */
    public  List<SelfArea> getSecurityAddress() {
        DetachedCriteria dc = selfAreaDao.createDetachedCriteria();
        dc.add(Restrictions.eq("status", "启用"));
        dc.add(Restrictions.like("datastandard", "%参保区县%"));
        dc.addOrder(Order.asc("areaId"));
        return selfAreaDao.find(dc);
    }
    
    
    public SelfArea findByAreaName(String areaName){
    	List<SelfArea> list = selfAreaDao.findByAreaName(areaName);
    	if(list.size()==1){
    		return list.get(0);
        }else{
        	return null;
        }
    }
    
    
    
    
 
    public SelfArea findByProvinceName(String areaName,String provincedId){
    	List<SelfArea> list = selfAreaDao.findByProvinceName(areaName,provincedId);
    	if(list.size()==1){
    		return list.get(0);
        }else{
        	return null;
        }
    }
}
