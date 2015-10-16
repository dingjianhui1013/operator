/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.service;

import java.util.Date;
import java.util.List;

import com.itrus.ca.modules.key.dao.KeyUsbKeyDao;
import com.itrus.ca.modules.key.dao.KeyUsbKeyInvoiceDao;
import com.itrus.ca.modules.key.entity.*;
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
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.key.entity.KeyCheckLog;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.dao.KeyDepotGeneralStatisticsDao;
import com.itrus.ca.modules.key.dao.KeyUsbKeyDepotDao;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * key入库信息Service
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class KeyUsbKeyDepotService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyUsbKeyDepotService.class);
	
	@Autowired
	private KeyUsbKeyDepotDao keyUsbKeyDepotDao;
	
	@Autowired
	private KeyDepotGeneralStatisticsDao keyDepotGeneralStatisticsDao;
	
	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;
	@Autowired
	private KeyUsbKeyDao keyUsbKeyDao;
	@Autowired
	private KeyUsbKeyInvoiceDao keyUsbKeyInvoiceDao;
	
	public KeyUsbKeyDepot get(Long id) {
		return keyUsbKeyDepotDao.findOne(id);
	}
	
	public Page<KeyUsbKeyDepot> find(Page<KeyUsbKeyDepot> page, KeyUsbKeyDepot keyUsbKeyDepot) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
//		dc.createAlias("createBy.office", "office");
//		dc.createAlias("createBy", "createBy");
//		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		if (StringUtils.isNotEmpty(keyUsbKeyDepot.getDepotName())){
			dc.add(Restrictions.like("depotName", "%"+EscapeUtil.escapeLike(keyUsbKeyDepot.getDepotName())+"%"));
		}
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(page, dc);
	}
	
	
	
	public Page<KeyUsbKeyDepot> findByOffices(Page<KeyUsbKeyDepot> page, KeyUsbKeyDepot keyUsbKeyDepot,List<Office> offices  ) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
//		dc.createAlias("createBy.office", "office");
//		dc.createAlias("createBy", "createBy");
//		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.add(Restrictions.in("office", offices));
		if (StringUtils.isNotEmpty(keyUsbKeyDepot.getDepotName())){
			dc.add(Restrictions.like("depotName", "%"+EscapeUtil.escapeLike(keyUsbKeyDepot.getDepotName())+"%"));
		}
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(page, dc);
	}
	
	

	public Page<KeyUsbKeyDepot> find(Page<KeyUsbKeyDepot> page, KeyUsbKeyDepot keyUsbKeyDepot,Date startTime,
			Date endTime,Long office,Long area,String depotName,Long keyId,Long manufacturerId) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		dc.createAlias("createBy.office", "office");
		dc.createAlias("createBy", "createBy");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		if (depotName!=null&&!depotName.equals("")){
			dc.add(Restrictions.like("depotName", "%"+EscapeUtil.escapeLike(depotName)+"%"));
		}
		if (office!=null) {
			dc.add(Restrictions.eq("office.id", office));
		}else if(area!=null){
			dc.createAlias("office", "office");
			dc.add(Restrictions.eq("office.parent.id", area));
		}
		
		if(keyId!=null){
			 List<KeyDepotGeneralStatistics> depotGeneStatistics =  keyDepotGeneralStatisticsService.findByKeyId(keyId);
			 //DetachedCriteria dc1 = keyDepotGeneralStatisticsDao.createDetachedCriteria();
			 for(int i=0;i<depotGeneStatistics.size();i++){
				 dc.add(Restrictions.eq("id", depotGeneStatistics.get(i).getKeyUsbKeyDepot().getId()));
			 }
			
		}
		
		
		if(manufacturerId!=null){
			dc.add(Restrictions.eq("manufacturerId", manufacturerId));
		}
		
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(page, dc);
	}
	
	public Page<KeyUsbKeyDepot> findAllDepot(Page<KeyUsbKeyDepot> page,String depotName,List<Office> offices) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
//		dc.createAlias("createBy.office", "office");
//		dc.createAlias("createBy", "createBy");
//		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		
		dc.add(Restrictions.in("office", offices));
		if (StringUtils.isNotEmpty(depotName)) {
			dc.add(Restrictions.like("depotName","%"+EscapeUtil.escapeLike(depotName)+"%"));
		}
		
		
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(page, dc);
	}

	
	
	public Page<KeyUsbKeyDepot> findAll(Page<KeyUsbKeyDepot> page) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(page, dc);
	}

	
	//库存统计的条件查询方法
	public Page<KeyUsbKeyDepot> findAll(Page<KeyUsbKeyDepot> page,Long area,Long office,String depotName) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		if (area!=null) {
			dc.add(Restrictions.eq("office.parent.id",area));
		}
		if (office!=null) {
			dc.add(Restrictions.eq("office.id",office));
		}
		if (StringUtils.isNotEmpty(depotName)){
			dc.add(Restrictions.like("depotName", "%"+EscapeUtil.escapeLike(depotName)+"%"));
		}
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(page, dc);
	}
	public List<KeyUsbKeyDepot> findAll() {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(dc);
	}
	public List<KeyUsbKeyDepot> findByName(String name) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		if (name!=null){
			dc.add(Restrictions.eq("depotName", name));
		}
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(dc);
	}
	
	public List<KeyUsbKeyDepot> findByNameId(String name,Long id) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		if (name!=null){
			dc.add(Restrictions.eq("depotName", name));
		}
		
		dc.add(Restrictions.ne("id", id));
		
		
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(dc);
	}
	
	public List<KeyUsbKeyDepot> findByOfficeId(Long officeId) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(dc);
	}
	
	public List<KeyUsbKeyDepot> findByOfficeIds(List<Office> officeIds) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		dc.add(Restrictions.in("office", officeIds));
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(dc);
	}
	
	
	public List<KeyUsbKeyDepot> findByParent(Long parentId) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		
		dc.createAlias("office", "office");
		dc.add(Restrictions.eq("office.parent.id", parentId));
		dc.addOrder(Order.desc("id"));
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		return keyUsbKeyDepotDao.find(dc);
	}
	
	
	public List<KeyUsbKeyDepot> findByDepotIds(List<Long> depotIds) {
		DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
		dc.setResultTransformer(dc.DISTINCT_ROOT_ENTITY);
		dc.add(Restrictions.in("id", depotIds));
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyDepotDao.find(dc);
	}
	
	
	public int findDepotTotalNumByOffice(Long officeId){
		try {
			DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
			dc.add(Restrictions.eq("office.id", officeId));
			dc.add(Restrictions.ne("id", KeyDepotId.BAD_DEPOT_ID));//不等于坏key库存
			dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
			dc.addOrder(Order.desc("id"));
			List<KeyUsbKeyDepot> depots = keyUsbKeyDepotDao.find(dc);
			if (depots!=null&&depots.size()!=0) {
				Long depotId = depots.get(0).getId();
				DetachedCriteria staticCriteria = keyDepotGeneralStatisticsDao.createDetachedCriteria();
				staticCriteria.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
				staticCriteria.addOrder(Order.desc("id"));
				List<KeyDepotGeneralStatistics> statistics = keyDepotGeneralStatisticsDao.find(staticCriteria);
				Integer total = 0;
				for (KeyDepotGeneralStatistics s : statistics) {
					total += s.getInCount();
				}
				return total;
			}else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	
	
	
	
	
	@Transactional(readOnly = false)
	public void save(KeyUsbKeyDepot keyUsbKeyDepot) {
		keyUsbKeyDepotDao.save(keyUsbKeyDepot);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyUsbKeyDepotDao.deleteById(id);
	}

	/**
	 *  计算统计日期之前的key余量
	 * @param officeId
	 * @param countDate
	 * @return
	 */
	public int findDepotTotalNumByOffice(Long officeId,Date countDate){
		try {
			DetachedCriteria dc = keyUsbKeyDepotDao.createDetachedCriteria();
			dc.add(Restrictions.eq("office.id", officeId));
			dc.add(Restrictions.ne("id", KeyDepotId.BAD_DEPOT_ID));//不等于坏key库存
			dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
			dc.addOrder(Order.desc("id"));
			List<KeyUsbKeyDepot> depots = keyUsbKeyDepotDao.find(dc);
			if (depots!=null&&depots.size()!=0) {
				Long depotId = depots.get(0).getId();
//				DetachedCriteria staticCriteria = keyDepotGeneralStatisticsDao.createDetachedCriteria();
//				staticCriteria.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
//				staticCriteria.addOrder(Order.desc("id"));
//				List<KeyDepotGeneralStatistics> statistics = keyDepotGeneralStatisticsDao.find(staticCriteria);
//				Integer total = 0;
//				for (KeyDepotGeneralStatistics s : statistics) {
//					total += s.getInCount();
//				}
//				return total;
				DetachedCriteria keyUsbKey = keyUsbKeyDao.createDetachedCriteria();
				keyUsbKey.add(Restrictions.le("startDate",countDate));
				keyUsbKey.add(Restrictions.eq("keyUsbKeyDepot",depots.get(0)));
				List<KeyUsbKey> keyForIn = keyUsbKeyDao.find(keyUsbKey);

				DetachedCriteria keyUsbKeyInvoice = keyUsbKeyInvoiceDao.createDetachedCriteria();
				keyUsbKeyInvoice.add(Restrictions.le("startDate",countDate));
				keyUsbKeyInvoice.add(Restrictions.eq("keyUsbKeyDepot",depots.get(0)));
				List<KeyUsbKeyInvoice> keyForOut = keyUsbKeyInvoiceDao.find(keyUsbKeyInvoice);

				Integer total = 0 ;
				Integer inTotal = 0;
				Integer outTotal = 0;
				for(KeyUsbKey in : keyForIn){
					inTotal+=in.getCount();
				}

				for(KeyUsbKeyInvoice out : keyForOut){
					outTotal+=out.getDeliveryNum();
				}

				total = inTotal-outTotal;
				return total;
			}else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}


}
