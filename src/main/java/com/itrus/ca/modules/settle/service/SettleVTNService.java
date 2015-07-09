/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.settle.dao.SettleVTNDao;
import com.itrus.ca.modules.settle.entity.SettleVTN;

/**
 * VTN管理Service
 * @author HuHao
 * @version 2015-03-12
 */
@Component
@Transactional(readOnly = true)
public class SettleVTNService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SettleVTNService.class);
	
	@Autowired
	private SettleVTNDao settleVTNDao;
	
	public SettleVTN get(Long id) {
		return settleVTNDao.findOne(id);
	}
	
	public Page<SettleVTN> find(Page<SettleVTN> page, SettleVTN settleVTN,Date startTime,Date endTime) {
		DetachedCriteria dc = settleVTNDao.createDetachedCriteria();
		
		if (startTime!=null) {
			dc.add(Restrictions.ge("createTime", startTime));
		}
		if (endTime!=null) {
			dc.add(Restrictions.le("createTime", endTime));
		}
		if (StringUtils.isNotEmpty(settleVTN.getProductName())){
			dc.add(Restrictions.like("productName", "%"+settleVTN.getProductName()+"%"));
		}
		if (StringUtils.isNotEmpty(settleVTN.getAppName())){
			dc.add(Restrictions.like("appName", "%"+settleVTN.getAppName()+"%"));
		}
		//dc.add(Restrictions.eq(SettleVTN.DEL_FLAG, SettleVTN.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return settleVTNDao.find(page, dc);
	}
	
	public List<Map<String, Object>> findProducts(){
		Query query = settleVTNDao.createSqlQuery("SELECT  PRODUCT_NAME FROM SETTLE_VTN GROUP BY PRODUCT_NAME");
		List list = query.list();
		List result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(int i = 0;i<result.size();i++){
			Map<String, Object> map = (Map<String, Object>) result.get(i);
			mapList.add(map);
		}
		return mapList;
	}
	
	public List<Map<String, Object>> findApps(){
		Query query = settleVTNDao.createSqlQuery("SELECT  APP_NAME FROM SETTLE_VTN GROUP BY APP_NAME");
		List list = query.list();
		List result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(int i = 0;i<result.size();i++){
			Map<String, Object> map = (Map<String, Object>) result.get(i);
			mapList.add(map);
		}
		return mapList;
	}
	
	public List<Map<String, Object>> findVTNStatistics(Long vtnUpplierId,String staDate,String endDate, String vtnProduct,
			 String vtnapp){
		String timeSQL = "where  1=1";
		if (vtnUpplierId!=null) {
			timeSQL+=" and a.CONFIG_SUPPLIER_ID = "+vtnUpplierId;
		}
		if(staDate!=null&&!staDate.equals("")){
			timeSQL+=" and a.CREATE_TIME >= to_date('"+staDate+"','yyyy-mm-dd hh24:mi:ss')";
		}
		if(endDate!=null&&!endDate.equals("")){
			timeSQL+=" and a.CREATE_TIME <= to_date('"+endDate+"','yyyy-mm-dd hh24:mi:ss')";
		}
		if (!StringUtils.isEmpty(vtnProduct)) {
			timeSQL += "and a.product_name='"+vtnProduct+"'";
		}
		if (!StringUtils.isEmpty(vtnapp)) {
			timeSQL += "and a.APP_NAME = '"+vtnapp+"'";
		}
		Query query = settleVTNDao.createSqlQuery("select a.CONFIG_SUPPLIER_ID  ,a.app_name , a.product_name,sum(a.COUNT) as countTotal ,sum(a.COUNT_PRICE) as priceTotal from settle_vtn a "+timeSQL+" group by a.app_name ,a.PRODUCT_NAME , a.CONFIG_SUPPLIER_ID");
		List list = query.list();
		List result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(int i = 0;i<result.size();i++){
			Map<String, Object> map = (Map<String, Object>) result.get(i);
			mapList.add(map);
		}
		return mapList;
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(SettleVTN settleVTN) {
		settleVTNDao.save(settleVTN);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		settleVTNDao.deleteById(id);
	}
	
}
