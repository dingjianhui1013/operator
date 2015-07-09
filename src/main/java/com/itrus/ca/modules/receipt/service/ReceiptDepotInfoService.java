/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javassist.expr.NewArray;

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
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.entity.ReceiptLog;
import com.itrus.ca.modules.receipt.dao.ReceiptDepotInfoDao;
import com.itrus.ca.modules.receipt.dao.ReceiptEnterInfoDao;
import com.itrus.ca.modules.receipt.dao.ReceiptInvoiceDao;
import com.itrus.ca.modules.receipt.dao.ReceiptLogDao;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 发票信息Service
 * 
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class ReceiptDepotInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(ReceiptDepotInfoService.class);

	@Autowired
	private ReceiptDepotInfoDao receiptDepotInfoDao;

	@Autowired
	private ReceiptLogDao receiptLogDao;
	
	@Autowired
	private ReceiptEnterInfoDao receiptEnterInfoDao;
	
	@Autowired
	private ReceiptInvoiceDao receiptInvoiceDao;

	public ReceiptDepotInfo get(Long id) {
		return receiptDepotInfoDao.findOne(id);
	}

	public Page<ReceiptDepotInfo> find(Page<ReceiptDepotInfo> page,
			ReceiptDepotInfo receiptDepotInfo,List<Office> office) {
		DetachedCriteria dc = receiptDepotInfoDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		
		dc.add(Restrictions.in("office", office));
		if (StringUtils.isNotEmpty(receiptDepotInfo.getReceiptName())) {
			dc.add(Restrictions.like("receiptName",
					"%" + EscapeUtil.escapeLike(receiptDepotInfo.getReceiptName()) + "%"));
		}
		dc.add(Restrictions.eq(ReceiptDepotInfo.DEL_FLAG,
				ReceiptDepotInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptDepotInfoDao.find(page, dc);
	}

	/**
	 * 统计列表显示页面
	 * @param page
	 * @param receiptDepotInfo
	 * @param area
	 * @param office
	 * @return
	 */
	public Page<ReceiptDepotInfo> findByAreaOfficeName(
			Page<ReceiptDepotInfo> page, ReceiptDepotInfo receiptDepotInfo,
			Long area,Long office,Date startTime,Date endTime,List<Office> offices) {
		DetachedCriteria dc = receiptDepotInfoDao.createDetachedCriteria();
//		dc.createAlias("createBy.office", "office1");
//		dc.createAlias("createBy", "createBy");
//		dc.add(dataScopeFilter(UserUtils.getUser(), "office1", "createBy"));
		
		if (StringUtils.isNotEmpty(receiptDepotInfo.getReceiptName())) {
			dc.add(Restrictions.like("receiptName",
					"%" + EscapeUtil.escapeLike(receiptDepotInfo.getReceiptName()) + "%"));
		}
		if (area != null) {
			dc.add(Restrictions.eq("area.id", area));
		}
		if (office != null) {
			dc.add(Restrictions.eq("office.id", office));
		}
		dc.add(Restrictions.in("office", offices));
		dc.add(Restrictions.eq(ReceiptDepotInfo.DEL_FLAG,
				ReceiptDepotInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		Page<ReceiptDepotInfo> pagee =receiptDepotInfoDao.find(page, dc);
		List<ReceiptDepotInfo> receiptDepotInfos = new ArrayList<ReceiptDepotInfo>();
		List<ReceiptDepotInfo> infos = pagee.getList();
		for(ReceiptDepotInfo depotInfo : infos){
			//入库金额
			depotInfo.setReceiptTotal(getNumMoney(depotInfo,startTime,endTime));
			//出库金额
			depotInfo.setReceiptOut(getchuku(depotInfo, startTime, endTime));
			//余额
//			if(endTime!=null){
//				depotInfo.setReceiptResidue(getYuE(depotInfo,endTime));
//			}
			
			//原来的剩余金额
			depotInfo.setPrewarning(getqs(depotInfo, startTime));
			
			depotInfo.setReceiptResidue(depotInfo.getPrewarning()+depotInfo.getReceiptTotal()-depotInfo.getReceiptOut());
			
			receiptDepotInfos.add(depotInfo);
		}
		pagee.setList(receiptDepotInfos);
		return pagee;
	}

	@Transactional(readOnly = false)
	public void save(ReceiptDepotInfo receiptDepotInfo) {
		receiptDepotInfoDao.save(receiptDepotInfo);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		receiptDepotInfoDao.deleteById(id);
	}

	public List<ReceiptDepotInfo> findDepotByOffice(Office office) {
		DetachedCriteria dc = receiptDepotInfoDao.createDetachedCriteria();
		if (office != null) {
			dc.createAlias("office", "office");
			dc.add(Restrictions.eq("office.id", office.getId()));
		}
		dc.add(Restrictions.eq(ReceiptDepotInfo.DEL_FLAG,
				ReceiptDepotInfo.DEL_FLAG_NORMAL));
		return receiptDepotInfoDao.find(dc);
	}

	
	/**
	 * 获取入库总金额
	 */
	private double getNumMoney(ReceiptDepotInfo receiptDepotInfo, Date staDate,
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
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(endDate);
				calendar.add(Calendar.DATE, 1);
				dc.add(Restrictions.le("createDate", calendar.getTime()));
			}
		}
		List<ReceiptEnterInfo> enterInfos = receiptEnterInfoDao.find(dc);
		for(ReceiptEnterInfo receiptEnterInfo :enterInfos){
			money+=receiptEnterInfo.getNow_Money();
		}
		return money;
	}
	
	/**
	 * 获取出库总金额
	 */
	private double getchuku(ReceiptDepotInfo receiptDepotInfo, Date staDate,
			Date endDate) {
		double money = 0;
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq(ReceiptInvoice.DEL_FLAG, ReceiptInvoice.DEL_FLAG_NORMAL));
		if (staDate!=null) {
			dc.add(Restrictions.ge("createDate", staDate));
		}
		if (endDate!=null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.DATE, 1);
			dc.add(Restrictions.le("createDate", calendar.getTime()));
		}
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		List<ReceiptInvoice> invoices = receiptInvoiceDao.find(dc);
		for(ReceiptInvoice receiptInvoice:invoices){
			if(receiptInvoice.getReceiptMoney()!=null){
				money+=receiptInvoice.getReceiptMoney();
			}
		}
		return money;
	}
	
	private Double getqs(ReceiptDepotInfo receiptDepotInfo, Date staDate){
		double money = 0;
		DetachedCriteria dc = receiptLogDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		if(staDate!=null){
			dc.add(Restrictions.eq("createDate",new Timestamp(staDate.getTime())));
		}
		List<ReceiptLog> receiptLogs =receiptLogDao.find(dc);
		if(receiptLogs.size()>0){
			money = receiptLogs.get(0).getReceiptResidue();
		}
		return money;
	}

	private Double getYuE(ReceiptDepotInfo receiptDepotInfo, Date endTime){
		double money = 0;
		DetachedCriteria dc = receiptLogDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		if(endTime!=null){
			dc.add(Restrictions.eq("createDate",new Timestamp(endTime.getTime())));
		}
		List<ReceiptLog> receiptLogs =receiptLogDao.find(dc);
		if(receiptLogs.size()>0){
			money = receiptLogs.get(0).getReceiptResidue();
		}
		return money;
	}
	
	public List<ReceiptDepotInfo> findAllDepot() {
		DetachedCriteria dc =receiptDepotInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ReceiptDepotInfo.DEL_FLAG, ReceiptDepotInfo.DEL_FLAG_NORMAL));
		return receiptDepotInfoDao.find(dc);
	}
	
	public Double getCurLastReceipt(Long officeId){
		DetachedCriteria dc = receiptDepotInfoDao.createDetachedCriteria();
		dc.createAlias("office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(ReceiptDepotInfo.DEL_FLAG,
				ReceiptDepotInfo.DEL_FLAG_NORMAL));
		List<ReceiptDepotInfo> depotInfos = receiptDepotInfoDao.find(dc);
		if (depotInfos!=null&&depotInfos.size()!=0) {
			return depotInfos.get(0).getReceiptResidue();
		}else {
			return 0d;
		}
	}
	
	public List<ReceiptDepotInfo> findByOfficeIds(List<Office> officeIds) {
		DetachedCriteria dc = receiptDepotInfoDao.createDetachedCriteria();
		dc.add(Restrictions.in("office", officeIds));
		dc.add(Restrictions.eq(KeyUsbKeyDepot.DEL_FLAG, KeyUsbKeyDepot.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptDepotInfoDao.find(dc);
	}
}
