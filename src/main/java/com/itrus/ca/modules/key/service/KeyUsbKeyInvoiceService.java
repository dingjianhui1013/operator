/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
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
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.dao.KeyUsbKeyDao;
import com.itrus.ca.modules.key.dao.KeyUsbKeyInvoiceDao;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 出库管理Service
 * 
 * @author HUHAO
 * @version 2014-06-27
 */
@Component
@Transactional(readOnly = true)
public class KeyUsbKeyInvoiceService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(KeyUsbKeyInvoiceService.class);

	@Autowired
	private KeyUsbKeyInvoiceDao keyUsbKeyInvoiceDao;

	@Autowired
	private KeyUsbKeyService keyUsbKeyService;

	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;

	public KeyUsbKeyInvoice get(Long id) {
		return keyUsbKeyInvoiceDao.findOne(id);
	}

	public Page<KeyUsbKeyInvoice> find(Page<KeyUsbKeyInvoice> page,
			KeyUsbKeyInvoice keyUsbKeyInvoice) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		// if (StringUtils.isNotEmpty(keyUsbKeyInvoice.getName())){
		// dc.add(Restrictions.like("name",
		// "%"+keyUsbKeyInvoice.getName()+"%"));
		// }
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", keyUsbKeyInvoice
				.getKeyUsbKeyDepot().getId()));
//		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG,
//				KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(page, dc);
	}
	
	
	public List<KeyUsbKeyInvoice> find(KeyUsbKeyInvoice keyUsbKeyInvoice){
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", keyUsbKeyInvoice
				.getKeyUsbKeyDepot().getId()));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}
	

	public Page<KeyUsbKeyInvoice> findStartEnd(Page<KeyUsbKeyInvoice> page,
			KeyUsbKeyInvoice keyUsbKeyInvoice,Date start,Date end,Long supplierId ,Long keyId) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", keyUsbKeyInvoice
				.getKeyUsbKeyDepot().getId()));
		

		dc.add(Restrictions.le("startDate", end));
		dc.add(Restrictions.gt("startDate", start));
		
		if (supplierId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.configSupplier.id", supplierId));
		}
		if (keyId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", keyId));
		}
	//	dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG,	KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(page, dc);
	}
	
	
	public List<KeyUsbKeyInvoice> findByDepotId(Long depotId) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}
	
	
	public List<KeyUsbKeyInvoice> findByDepotId(Long depotId,Date startTime) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.le("startDate", startTime));
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}

	
	
	
	

	public List<KeyUsbKeyInvoice> findByDepotIdAndTime(Long depotId,Date startTime,Date endTime) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		if(startTime!=null){
			dc.add(Restrictions.ge("createDate",startTime));
		}
		if(endTime!=null){
			dc.add(Restrictions.le("createDate",endTime));
		}
		
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}
	
	/**
	 * 根据库房查出库单
	 * 如果根据操作人来查是错的%>_<%
	 * @param date
	 * @param officeId
	 * @return
	 */
	public List<KeyUsbKeyInvoice> findByCreateDate(Date date,Long officeId) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.ge("createDate", date));
		dc.createAlias("createBy", "createBy");
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.createAlias("keyUsbKeyDepot.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}

	
	public List<KeyUsbKeyInvoice> findByTime(Long id,Date date) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", id));
		if(date!=null){
			dc.add(Restrictions.le("createDate", date));
		}
		
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}
	
	public List<KeyUsbKeyInvoice> findByStartAndEnd(Date startTime,Date endTime,String depotName,Long keyId) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		
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
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}
	
	
	
	
	public List<KeyUsbKeyInvoice> findByStartEnd(Date start) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("startDate", start));
		dc.add(Restrictions.eq("outReason", 5));
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}
	
	
	
	
	
	
	
	
	public List<KeyUsbKeyInvoice> findByKeysn(String keysn) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keySn", keysn));
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}

	public List<KeyUsbKeyInvoice> findByDepotId(Long depotId, Date start,
			Date end,Long supplierId,Long keyId) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.le("startDate", end));
		dc.add(Restrictions.gt("startDate", start));
		if (supplierId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.configSupplier.id", supplierId));
		}
		if (keyId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", keyId));
		}
	//	dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}

	public List<KeyUsbKeyInvoice> findByDepotId(Long depotId, Date start,
			Date end) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.le("startDate", end));
		dc.add(Restrictions.gt("startDate", start));
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}
	
	
	public List<KeyUsbKeyInvoice> findByDepotId(Long depotId,Date startTime,Long supplierId,Long keyId) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keyUsbKeyDepot.id", depotId));
		dc.add(Restrictions.le("startDate", startTime));
		if (supplierId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.configSupplier.id", supplierId));
		}
		if (keyId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", keyId));
		}
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(KeyUsbKeyInvoice keyUsbKeyInvoice) {
		keyUsbKeyInvoiceDao.save(keyUsbKeyInvoice);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyUsbKeyInvoiceDao.deleteById(id);
	}
	
	
	public boolean validateCSPvalid(String csp){
		List<KeyUsbKey> kuks = keyUsbKeyService.findByGeneName(csp);
		if (kuks==null||kuks.size()==0) {
			return false;
		}else {
			return true;
		}
	}

	// 销售出库接口
	@Transactional(readOnly = false)
	public void saveInvoice(String genName, String keySn,String companyName) {
		User user = UserUtils.getUser();
		List<KeyUsbKey> kuks = keyUsbKeyService.findByGeneName(genName);
		KeyUsbKey kuk = new KeyUsbKey();
		for (int i = 0; i < kuks.size(); i++) {
			if (kuks.get(i).getKeyUsbKeyDepot().getOffice().getId().equals(user
					.getOffice().getId())) {
				kuk = kuks.get(i);
				break;
			}
		}
		KeyUsbKeyInvoice invoice = new KeyUsbKeyInvoice();
		invoice.setKeyGeneralInfo(kuk.getKeyGeneralInfo());
		invoice.setKeyUsbKeyDepot(kuk.getKeyUsbKeyDepot());
		invoice.setDeliveryNum(1);
		invoice.setOutReason(4);
		invoice.setKeySn(keySn);
		invoice.setCompanyName(companyName);
		invoice.setStartDate(new Date());
		
		//判断是否已出库
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.createAlias("keyGeneralInfo", "keyGeneralInfo");
		dc.add(Restrictions.eq("keySn", keySn));
		dc.add(Restrictions.eq("delFlag", KeyUsbKeyInvoice.DEL_FLAG_NORMAL));//出库单有可能失效，坏key置换时使它失效
		long count = keyUsbKeyInvoiceDao.count(dc);
		
		if (count==0) {//在该仓库下从未出过库
			List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
					.findByDepotIdAndGeneId(kuk.getKeyUsbKeyDepot().getId(), kuk
							.getKeyGeneralInfo().getId());
			if (staList.size() > 0) {
				if(staList.get(0).getInCount()>0){
					keyUsbKeyInvoiceDao.save(invoice);
					KeyDepotGeneralStatistics sta = staList.get(0);
					int outtotal = sta.getOutCount();
					int intotal = sta.getInCount();
					sta.setOutCount(outtotal + invoice.getDeliveryNum());
					sta.setInCount(intotal - invoice.getDeliveryNum());
					keyDepotGeneralStatisticsService.save(sta);
				}
			}
		}
	}

	// 坏key入库接口
	@Transactional(readOnly = false)
	public void saveKeyUsbKey(String keySn) {
		try {
			KeyUsbKeyInvoice invoice = this.findByKeysn(keySn).get(0);
			invoice.setDelFlag(KeyUsbKeyInvoice.DEL_FLAG_DELETE);

			KeyUsbKey key = new KeyUsbKey();
			key.setKeyGeneralInfo(invoice.getKeyGeneralInfo());
			key.setKeyUsbKeyDepot(invoice.getKeyUsbKeyDepot());
			key.setCount(1);
			key.setInReason(4);
			key.setStartDate(new Date());
			keyUsbKeyService.save(key);
			List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
					.findByDepotIdAndGeneId(invoice.getKeyUsbKeyDepot().getId(),
							invoice.getKeyGeneralInfo().getId());
			keyUsbKeyInvoiceDao.save(invoice);//让出库单失效

			if (staList.size() > 0) {
				KeyDepotGeneralStatistics sta = staList.get(0);
				int intotal = sta.getInCount();
//				sta.setTotalCount(total + key.getCount());
				sta.setInCount(intotal + key.getCount());
				sta.setOutCount(sta.getOutCount()-key.getCount());
				keyDepotGeneralStatisticsService.save(sta);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 退费入库接口
	@Transactional(readOnly = false)
	public void saveKeyUsbKey1(String keySn) {
		try {
			KeyUsbKeyInvoice invoice = this.findByKeysn(keySn).get(0);
			invoice.setDelFlag(KeyUsbKeyInvoice.DEL_FLAG_DELETE);

			KeyUsbKey key = new KeyUsbKey();
			key.setKeyGeneralInfo(invoice.getKeyGeneralInfo());
			key.setKeyUsbKeyDepot(invoice.getKeyUsbKeyDepot());
			key.setCount(1);
			key.setInReason(6);
			key.setStartDate(new Date());
			keyUsbKeyService.save(key);
			List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
					.findByDepotIdAndGeneId(invoice.getKeyUsbKeyDepot().getId(),
							invoice.getKeyGeneralInfo().getId());
			keyUsbKeyInvoiceDao.save(invoice);//让出库单失效

			if (staList.size() > 0) {
				KeyDepotGeneralStatistics sta = staList.get(0);
				int intotal = sta.getInCount();
//				sta.setTotalCount(total + key.getCount());
				sta.setInCount(intotal + key.getCount());
				sta.setOutCount(sta.getOutCount()-key.getCount());
				keyDepotGeneralStatisticsService.save(sta);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 根据keySn查询出DefaultSoPinType
	@Transactional(readOnly = false)
	public KeyGeneralInfo findDefaultSoPinTypeByKeysn(String keySn) {
		List<KeyUsbKeyInvoice> invoices = this.findByKeysn(keySn);
		if (invoices.size() > 0) {
			KeyUsbKeyInvoice invoice = this.findByKeysn(keySn).get(0);
			return invoice.getKeyGeneralInfo();
		}
		return null;
	}
	
	/**
	 * 根据库房查出库单
	 * 如果根据操作人来查是错的%>_<%
	 * @param date
	 * @param officeId
	 * @return
	 */
	public List<KeyUsbKeyInvoice> findByCreateDate(Date date,Long officeId,Date endDate) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.ge("createDate", date));
		dc.add(Restrictions.lt("createDate", endDate));
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.createAlias("keyUsbKeyDepot.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		//dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}

	
	/**
	 *  获得统计日前一天到统计日当天的key出库信息
	 * @param countDate
	 * @param officeId
	 * @return
	 */
	public List<KeyUsbKeyInvoice> findByDate(Date countDate , Long officeId) {
		DetachedCriteria dc = keyUsbKeyInvoiceDao.createDetachedCriteria();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE,+1);
		dc.add(Restrictions.le("createDate", calendar.getTime()));
		dc.createAlias("createBy", "createBy");
		dc.createAlias("keyUsbKeyDepot", "keyUsbKeyDepot");
		dc.createAlias("keyUsbKeyDepot.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(KeyUsbKeyInvoice.DEL_FLAG, KeyUsbKeyInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUsbKeyInvoiceDao.find(dc);
	}

}
