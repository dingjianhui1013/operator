/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.service;

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
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.dao.ReceiptInvoiceDao;
import com.itrus.ca.modules.sys.dao.UserDao;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;

/**
 * 出库信息Service
 * @author WHW
 * @version 2014-07-02
 */
@Component
@Transactional(readOnly = true)
public class ReceiptInvoiceService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ReceiptInvoiceService.class);
	
	@Autowired
	private ReceiptInvoiceDao receiptInvoiceDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	
	public ReceiptInvoice get(Long id) {
		return receiptInvoiceDao.findOne(id);
	}
	
	public Page<ReceiptInvoice> find(Page<ReceiptInvoice> page, ReceiptInvoice receiptInvoice) {
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(receiptInvoice.getName())){
//			dc.add(Restrictions.like("name", "%"+receiptInvoice.getName()+"%"));
//		}
		
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq(ReceiptInvoice.DEL_FLAG, ReceiptInvoice.DEL_FLAG_NORMAL));
		if(receiptInvoice.getReceiptDepotInfo()!=null){
			dc.add(Restrictions.eq("receiptDepotInfo", receiptInvoice.getReceiptDepotInfo()));
		}
		dc.addOrder(Order.desc("id"));
		return receiptInvoiceDao.find(page, dc);
	}
	
	public List<ReceiptInvoice> findByCreateDate(Date date) {
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();

		dc.add(Restrictions.ge("createDate", date));
		dc.add(Restrictions.eq(ReceiptInvoice.DEL_FLAG, ReceiptInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptInvoiceDao.find(dc);
	}
	
	
	public Page<ReceiptInvoice> findByDepotInfo(Page<ReceiptInvoice> page, ReceiptInvoice receiptInvoice,Date staDate,Date endDate) {
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq(ReceiptInvoice.DEL_FLAG, ReceiptInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		if (staDate!=null) {
			dc.add(Restrictions.ge("createDate", staDate));
		}
		if (endDate!=null) {
			dc.add(Restrictions.le("createDate", endDate));
		}
		if(receiptInvoice.getReceiptDepotInfo()!=null){
			dc.add(Restrictions.eq("receiptDepotInfo", receiptInvoice.getReceiptDepotInfo()));
		}
		if(receiptInvoice.getCompanyName()!=null&&!receiptInvoice.getCompanyName().equals("")){
			dc.add(Restrictions.like("companyName", "%"+receiptInvoice.getCompanyName()+"%"));
		}
		return receiptInvoiceDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ReceiptInvoice receiptInvoice) {
		receiptInvoiceDao.save(receiptInvoice);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		receiptInvoiceDao.deleteById(id);
	}

	
	@Transactional(readOnly = false)
	public double findInvoiceByDepotInfo(Date staDate, Date endDate,
			ReceiptDepotInfo receiptDepotInfo) {
		double money = 0;
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq(ReceiptInvoice.DEL_FLAG, ReceiptInvoice.DEL_FLAG_NORMAL));
		if (staDate!=null) {
			dc.add(Restrictions.ge("createDate", staDate));
		}
		if (endDate!=null) {
			dc.add(Restrictions.le("createDate", endDate));
		}
		dc.add(Restrictions.ne("receiptType", 2));
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		List<ReceiptInvoice> invoices = receiptInvoiceDao.find(dc);
		for(ReceiptInvoice receiptInvoice:invoices){
			if(receiptInvoice.getReceiptMoney()!=null){
				money+=receiptInvoice.getReceiptMoney();
			}
		}
		return money;
	}
	
	//获取已开发票集合
	public List<ReceiptInvoice> findByInvoice(Date staDate, Date endDate,
			ReceiptDepotInfo receiptDepotInfo) {
		double money = 0;
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq(ReceiptInvoice.DEL_FLAG, ReceiptInvoice.DEL_FLAG_NORMAL));
		if (staDate!=null) {
			dc.add(Restrictions.ge("createDate", staDate));
		}
		if (endDate!=null) {
			dc.add(Restrictions.le("createDate", endDate));
		}
		dc.add(Restrictions.ne("receiptType", 2));
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		return receiptInvoiceDao.find(dc);
	}
	
	
	
	
	/**
	 * 
	 * @param money 发票金额
	 * @param office 办理网点
	 * @param companyName 公司名称
	 * @return  返回 true 则出库成功  返回false 则出库失败（余额不足 或者是 未发现此网点）
	 */
	@Transactional(readOnly = false)
	public boolean receiptIncoiceI(double money,Office office,String companyName,Long dealInfoId){
		ReceiptInvoice receiptInvoice = new ReceiptInvoice();
		System.out.println("==========发票出库查询=========");
		List<ReceiptDepotInfo> depotInfos =receiptDepotInfoService.findDepotByOffice(office);
		System.out.println("==========发票出库查询结束=========");
		if(depotInfos.size()>0){
			ReceiptInvoice invoice = this.findByCompanyNameDealInfoId(companyName, dealInfoId);
			if (invoice==null) {
			ReceiptDepotInfo receiptDepotInfo = depotInfos.get(0);
			
			receiptInvoice.setReceiptDepotInfo(receiptDepotInfo);
			//if(receiptDepotInfo.getReceiptResidue()!=null&&receiptDepotInfo.getReceiptResidue()>money){
				receiptInvoice.setCompanyName(companyName);
				receiptInvoice.setReceiptMoney(money);
				receiptInvoice.setReceiptType(0);//销售出库
				receiptInvoice.setDealInfoId(dealInfoId);
				this.save(receiptInvoice);
				receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue()-money);
				receiptDepotInfo.setReceiptOut(receiptDepotInfo.getReceiptOut()+money);
				receiptDepotInfoService.save(receiptDepotInfo);
				}
				return true;
//			}else {
//				return false;//余额不足
//			}
		}else{
			return false;  //未发现此网点
		}
	}
	@Transactional(readOnly = false)
	public ReceiptInvoice findByCompanyNameDealInfoId(String companyName,Long dealInfoId){
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.eq("companyName", companyName));
		dc.add(Restrictions.eq("dealInfoId", dealInfoId));
		if (receiptInvoiceDao.find(dc).size()>0) {
			return receiptInvoiceDao.find(dc).get(0);
		}else{
			return null;
		}
		
	}
	
	/*
	 	*判断网点是否有库存 
	*/
	@Transactional(readOnly = false)
	public boolean checkOffice(Office office){
		List<ReceiptDepotInfo> depotInfos =receiptDepotInfoService.findDepotByOffice(office);
		if (depotInfos.size()>0) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
	
	
	/**
	 * 
	 * @param money 发票金额
	 * @param office 办理网点
	 * @param companyName 公司名称
	 * @return  返回 true 则出库成功  返回false 则出库失败（余额不足 或者是 未发现此网点）
	 */
	@Transactional(readOnly = false)
	public boolean receiptIncoiceT(double money,Office office,String companyName){
		ReceiptInvoice receiptInvoice = new ReceiptInvoice();
		System.out.println("==========发票出库查询=========");
		ReceiptDepotInfo depotInfos =receiptDepotInfoService.get(KeyDepotId.RECEIPT_DEPOT_ID);
		System.out.println("==========发票出库查询结束=========");
		if(depotInfos!=null){
			ReceiptDepotInfo receiptDepotInfo = depotInfos;
			receiptInvoice.setReceiptDepotInfo(receiptDepotInfo);
			if(receiptDepotInfo.getReceiptResidue()!=null&&receiptDepotInfo.getReceiptResidue()>money){
				receiptInvoice.setCompanyName(companyName);
				receiptInvoice.setReceiptMoney(money);
				receiptInvoice.setReceiptType(0);//销售出库
				User user = userDao.findOne(1L);
				receiptInvoice.setCreateBy(user);//销售出库
				this.save(receiptInvoice);
				receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue()-money);
				receiptDepotInfoService.save(receiptDepotInfo);
				return true;
			}else {
				return false;//余额不足
			}
		}else{
			return false;  //未发现此网点
		}
	}
	
	
	@Transactional(readOnly = false)
	public List<ReceiptInvoice> selectByDepotInfo(ReceiptDepotInfo receiptDepotInfo) {
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		return receiptInvoiceDao.find(dc);
	}
	
	
	public List<ReceiptInvoice> selectByDepotType(ReceiptDepotInfo receiptDepotInfo,Long typeId) {
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.add(Restrictions.eq("receiptDepotInfo", receiptDepotInfo));
		dc.add(Restrictions.eq("type.id", typeId));
		return receiptInvoiceDao.find(dc);
	}
	
	
	
	

	/**
	 * 获得
	 * @param countDate
	 * @param officeId
	 * @return
	 */
	public List<ReceiptInvoice> findByDate(Date countDate, Long officeId) {
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(countDate);
		calendar.add(Calendar.DATE,+1);
		dc.add(Restrictions.le("createDate", calendar.getTime()));
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.createAlias("receiptDepotInfo.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(ReceiptInvoice.DEL_FLAG, ReceiptInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptInvoiceDao.find(dc);
	}
	
	/**
	 * 获得
	 * @param countDate
	 * @param officeId
	 * @return
	 */
	public List<ReceiptInvoice> findByDate(Date countDate,Date endDate, Long officeId) {
		DetachedCriteria dc = receiptInvoiceDao.createDetachedCriteria();
		dc.add(Restrictions.ge("createDate", countDate));
		dc.add(Restrictions.lt("createDate", endDate));
		dc.createAlias("receiptDepotInfo", "receiptDepotInfo");
		dc.createAlias("receiptDepotInfo.office", "office");
		dc.add(Restrictions.eq("office.id", officeId));
		dc.add(Restrictions.eq(ReceiptInvoice.DEL_FLAG, ReceiptInvoice.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return receiptInvoiceDao.find(dc);
	}
}
