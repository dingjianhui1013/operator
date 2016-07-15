/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.dao.KeyUsbKeyDao;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 入库管理Service
 * @author HUHAO
 * @version 2014-06-27
 */
@Component
@Transactional(readOnly = true)
public class KeyUsbKeyService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyUsbKeyService.class);
	
	@Autowired
	private KeyUsbKeyDao keyUsbKeyDao;
	
	public KeyUsbKey get(Long id) {
		return keyUsbKeyDao.findOne(id);
	}
	
	public Page<KeyUsbKey> find(Page<KeyUsbKey> page, KeyUsbKey keyUsbKey) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(keyUsbKey.getName())){
//			dc.add(Restrictions.like("name", "%"+keyUsbKey.getName()+"%"));
//		}
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", keyUsbKey.getKeyUsbKeyDepot().getId()));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(page, dc);
	}
	
	public Page<KeyUsbKey> findByStartEnd(Page<KeyUsbKey> page, KeyUsbKey keyUsbKey,Date start,Date end,Long supplierId ,Long keyId ) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", keyUsbKey.getKeyUsbKeyDepot().getId()));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		if (supplierId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.configSupplier.id", supplierId));
		}
		if (keyId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", keyId));
		}
		if(end!=null){
			dc.add(Restrictions.le("startDate", end));
		}
		if(start!=null){
			dc.add(Restrictions.gt("startDate", start));
		}
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(page, dc);
	}
	
	
	
	public List<KeyUsbKey> findByGeneralInfoId(Long geneId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		
		dc.add(Restrictions.eq("keyGeneralInfo.id",geneId));
		
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	public List<KeyUsbKey> findByCreateDate(Date date,Long officeId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.ge("createDate",date));
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.createAlias("keyUsbKeyDepot.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	public List<KeyUsbKey> findTime(Long id,Date date) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", id));
		if(date!=null){
			dc.add(Restrictions.le("createDate",date));
		}
		
		
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	public List<KeyUsbKey> findByStartAndEnd(Date startTime,Date endTime,String depotName,Long keyId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		
		dc.add(Restrictions.ge("createDate",startTime));
		dc.add(Restrictions.le("createDate",endTime));
		if(depotName!=null&&!depotName.equals("")){
			dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
			dc.add(Restrictions.eq("keyUsbKeyDepot.depotName",depotName));
			
		}
		if(keyId!=null){
			dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
			dc.add(Restrictions.eq("keyGeneralInfo.id",keyId));
			
		}
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	public List<KeyUsbKey> findByGeneralInfoIds(List<Long> geneIds) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.in("keyGeneralInfo.id", geneIds));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	public List<KeyUsbKey> findByDepotIds(List<Long> depotIds) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.in("keyUsbKeyDepot.id", depotIds));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	public List<KeyUsbKey> findByDepotId(Long depotId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	public List<KeyUsbKey> findByDepotIdAndTime(Long depotId,Date startTime,Date endTime) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		if(startTime!=null){
			dc.add(Restrictions.ge("createDate",startTime));
		}
		if(endTime!=null){
			dc.add(Restrictions.le("createDate",endTime));
		}
		
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	public List<KeyUsbKey> findByGeneId(Long geneId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("KeyGeneralInfo.id", geneId));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	public List<KeyUsbKey> findByGeneName(String genName) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.createAlias("createBy.office", "office").createAlias("createBy", "createBy");
		dc.add(Restrictions.eq("keyGeneralInfo.name", genName));
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.add(Restrictions.eq("keyUsbKeyDepot.office.id",UserUtils.getUser().getOffice().getId()));//所属网点
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		
		return keyUsbKeyDao.find(dc);
	}
	
	
	
	
	public List<KeyUsbKey> findByDepotIdDate(Long depotId,Date start,Date end) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.le("startDate", end));
		dc.add(Restrictions.gt("startDate", start));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	public List<KeyUsbKey> findByDepotIdNotBadKeyin(Long depotId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.ne("inReason", 4));
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	
	public List<KeyUsbKey> findByStartEnd(Date start) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("startDate", start));
		dc.add(Restrictions.eq("inReason", 5));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	
	
	public List<KeyUsbKey> findByDepotIdEndTime(Long depotId,Date endTime) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.le("startDate", endTime));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	public List<KeyUsbKey> findByDepotIdEndTime(Long depotId,Date endTime,Long supplierId,Long keyId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		if(endTime!=null){
			dc.add(Restrictions.le("startDate", endTime));
		}
		if (supplierId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.configSupplier.id", supplierId));
		}
		if (keyId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", keyId));
		}
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	public List<KeyUsbKey> findByDepotIdStartTimeEndTime(Long depotId,Date startTime,Date endTime) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.le("startDate", endTime));
		dc.add(Restrictions.gt("startDate", startTime));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	
	public List<KeyUsbKey> findByDepotIdStartTimeEndTime(Long depotId,Date startTime,Date endTime,Long supplierId,Long keyId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		if(startTime!=null){
			dc.add(Restrictions.gt("startDate", startTime));
		}
		if(endTime!=null){
			dc.add(Restrictions.le("startDate", endTime));
		}
		if (supplierId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.configSupplier.id", supplierId));
		}
		if (keyId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", keyId));
		}
	//	dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	/**
	 * 所有仓库入库总量汇总
	 * @return
	 */
	public int ruKuZongliang(Date startTime,Date endTime,List<KeyUsbKeyDepot> cangku,Long supplierId,Long keyId){
		int total = 0;
		StringBuffer sqlString = new StringBuffer();
		sqlString.append("select SUM(count) from KEY_USB_KEY a where 1=1 ");
		if(cangku.size()>=0){
			sqlString.append("and usb_key_depot_id in (");
			for(int i=0;i<cangku.size();i++){
				sqlString.append(cangku.get(i).getId());
				if(i!=(cangku.size()-1)){
					sqlString.append(",");
				}
			}
			sqlString.append(")");
		}
		sqlString.append(")");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
		
		if(startTime!=null){
			String start = simpleDateFormat.format(startTime);
			sqlString.append(" and a.START_DATE>TO_DATE('");
			sqlString.append(start);
			sqlString.append("', 'yy-MM-dd')");
		}
		if(endTime!=null){
			//endTime 日期+1
			Calendar   calendar   =   new   GregorianCalendar(); 
			calendar.setTime(endTime); 
			calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
			String end = simpleDateFormat.format(calendar.getTime());
			
			sqlString.append(" and a.START_DATE<TO_DATE('");
			sqlString.append(end);
			sqlString.append("', 'yy-MM-dd')");
		}
//		if(supplierId!=null){
//			sqlString.append(" and ");
//		}
		
		List<Object> list = keyUsbKeyDao.findBySql(sqlString.toString());
		if(list.get(0)!=null){
			BigDecimal b = (BigDecimal)list.get(0);
			total = b.intValue();
		}
		return total;
	}
	
	
	
	
	//库存统计，获取开始时间之前的入库信息
	public List<KeyUsbKey> findByDepotIdStartTime(Long depotId,Date startTime,Long supplierId,Long keyId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.le("startDate", startTime));
		if (supplierId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.configSupplier.id", supplierId));
		}
		if (keyId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", keyId));
		}
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(KeyUsbKey keyUsbKey) {
		keyUsbKeyDao.save(keyUsbKey);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyUsbKeyDao.deleteById(id);
	}
	/**********************
	 * test
	 * 
	 *********************/
	public List<KeyUsbKey> findByCreateDate(Date date,Long officeId,Date endDate) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		dc.add(Restrictions.ge("createDate",date));
		dc.add(Restrictions.lt("createDate",endDate));
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.createAlias("keyUsbKeyDepot.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}

	/**
	 * 	获得统计日期之前的key入库信息
	 * @param countDate
	 * @param officeId
	 * @return
	 */
	public List<KeyUsbKey> findByDate(Date countDate ,Long officeId) {
		DetachedCriteria dc = keyUsbKeyDao.createDetachedCriteria();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE,+1);
		dc.add(Restrictions.le("createDate",calendar.getTime()));//统计当天
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.createAlias("keyUsbKeyDepot.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(KeyUsbKey.DEL_FLAG, KeyUsbKey.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDao.find(dc);
	}
	
}
