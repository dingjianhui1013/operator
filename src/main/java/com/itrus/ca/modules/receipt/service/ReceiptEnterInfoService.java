/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.service;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.dao.ReceiptEnterInfoDao;

/**
 * 入库信息Service
 * @author WHW
 * @version 2014-07-02
 */
@Component
@Transactional(readOnly = true)
public class ReceiptEnterInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptEnterInfoService.class);
	
	@Autowired
	private ReceiptEnterInfoDao receiptEnterInfoDao;
	
	public ReceiptEnterInfo get(Long id) {
		return receiptEnterInfoDao.findOne(id);
	}
	
	public Page<ReceiptEnterInfo> find(Page<ReceiptEnterInfo> page, ReceiptEnterInfo receiptEnterInfo) {
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(receiptEnterInfo.getName())){
//			dc.add(Restrictions.like("name", "%"+receiptEnterInfo.getName()+"%"));
//		}
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		if(receiptEnterInfo.getReceiptDepotInfo()!=null){
			dc.add(Restrictions.eq("receiptDepotInfo", receiptEnterInfo.getReceiptDepotInfo()));
		}
		dc.add(Restrictions.eq(ReceiptEnterInfo.DEL_FLAG, ReceiptEnterInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptEnterInfoDao.find(page, dc);
	}
	
	
	
	public List<ReceiptEnterInfo> findByCreateDate(Date date,Long officeId) {
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ge("createDate", date));
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.createAlias("receiptDepotInfo.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(ReceiptEnterInfo.DEL_FLAG, ReceiptEnterInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptEnterInfoDao.find(dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(ReceiptEnterInfo receiptEnterInfo) {
		receiptEnterInfoDao.save(receiptEnterInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		receiptEnterInfoDao.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public List<ReceiptEnterInfo> findEnterInfoList(
			ReceiptDepotInfo receiptDepotInfo, Date endDate,Date lastDate) {
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
		if(receiptDepotInfo!=null){
			dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
			if (endDate!=null) {
				dc.add(Restrictions.ge("createDate", endDate));
			}
			if (lastDate!=null) {
				dc.add(Restrictions.le("createDate", lastDate));
			}
		}
		dc.add(Restrictions.ne("receiptType", 2));
		return receiptEnterInfoDao.find(dc);
	}

	@Transactional(readOnly = false)
	public double getNumMoney(ReceiptDepotInfo receiptDepotInfo, Date staDate,
			Date endDate) {
		double money = 0;
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		if(receiptDepotInfo!=null){
			dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
			if (staDate!=null) {
				dc.add(Restrictions.ge("createDate", staDate));
			}
			if (endDate!=null) {
				dc.add(Restrictions.le("createDate", endDate));
			}
		}
		List<ReceiptEnterInfo> enterInfos = receiptEnterInfoDao.find(dc);
		for(ReceiptEnterInfo receiptEnterInfo :enterInfos){
			money+=receiptEnterInfo.getNow_Money();
		}
		return money;
	}

	@Transactional(readOnly = false)
	public List<ReceiptEnterInfo> selectByDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		return receiptEnterInfoDao.find(dc);
	}
	
	public List<ReceiptEnterInfo> selectByDepotType(ReceiptDepotInfo receiptDepotInfo,Long typeId) {
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		dc.add(Restrictions.eq("type.id", typeId));
		return receiptEnterInfoDao.find(dc);
	}
	
	
	
	
	public List<ReceiptEnterInfo> findByCreateDate(Date date,Long officeId,Date endDate) {
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ge("createDate", date));
		dc.add(Restrictions.le("createDate", endDate));
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.createAlias("receiptDepotInfo.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(ReceiptEnterInfo.DEL_FLAG, ReceiptEnterInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptEnterInfoDao.find(dc);
	}

	/**
	 *  获得统计日前一天到统计日当天的发票信息
	 * @param yesterDay
	 * @param countDate
	 * @param officeId
	 * @return
	 */
	public List<ReceiptEnterInfo> findByDate(Date yesterDay,Date countDate, Long officeId) {
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ge("createDate", yesterDay));
		dc.add(Restrictions.lt("createDate", countDate));
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.createAlias("receiptDepotInfo.office", "office");
		List<Integer> type = new ArrayList<Integer>();
		type.add(0);
		type.add(1);
		type.add(2);
		type.add(3);
		dc.add(Restrictions.in("receiptType",type ));
		
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(ReceiptEnterInfo.DEL_FLAG, ReceiptEnterInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptEnterInfoDao.find(dc);
	}

	/**
	 *  获得统计日（包括统计日）之前所有的发票入库数据
	 * @param countDate
	 * @param officeId
	 * @return
	 */
	public List<ReceiptEnterInfo> findByDate(Date countDate, Long officeId) {
		DetachedCriteria dc = receiptEnterInfoDao.createDetachedCriteria();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE,+1);
		dc.add(Restrictions.le("createDate", calendar.getTime()));
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.createAlias("receiptDepotInfo.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(ReceiptEnterInfo.DEL_FLAG, ReceiptEnterInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptEnterInfoDao.find(dc);
	}
}
