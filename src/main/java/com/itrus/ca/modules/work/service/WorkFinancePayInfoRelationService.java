/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.sys.utils.CreateExcelUtils;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.dao.WorkFinancePayInfoRelationDao;
import com.sun.org.apache.bcel.internal.generic.SIPUSH;

/**
 * 支付信息统计报表Service
 * @author HUHAO
 * @version 2014-06-16
 */
@Component
@Transactional(readOnly = true)
public class WorkFinancePayInfoRelationService extends BaseService {

	private static final int WB_TITLE_LENGTH = 8;
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkFinancePayInfoRelationService.class);
	
	@Autowired
	private WorkFinancePayInfoRelationDao workFinancePayInfoRelationDao;
	
	public WorkFinancePayInfoRelation get(Long id) {
		return workFinancePayInfoRelationDao.findOne(id);
	}
	
	public Page<WorkFinancePayInfoRelation> find(Page<WorkFinancePayInfoRelation> page, WorkFinancePayInfoRelation workFinancePayInfoRelation) {
		DetachedCriteria dc = workFinancePayInfoRelationDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(workFinancePayInfoRelation.getName())){
//			dc.add(Restrictions.like("name", "%"+workFinancePayInfoRelation.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(WorkFinancePayInfoRelation.DEL_FLAG, WorkFinancePayInfoRelation.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workFinancePayInfoRelationDao.find(page, dc);
	}
	
	public Page<WorkFinancePayInfoRelation> findByFinance(
			Page<WorkFinancePayInfoRelation> page, Long id,String appName) {
		
		DetachedCriteria dc = workFinancePayInfoRelationDao.createDetachedCriteria();
		dc.createAlias("financePaymentInfo", "financePaymentInfo");
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("financePaymentInfo.configApp", "configApp");
		dc.add(Restrictions.eq("financePaymentInfo.id", id));
		if(appName!=null)
		{
			dc.add(Restrictions.eq("configApp.appName",appName));
		}
//		if(startTime!=null)
//		{
//			dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
//		}
//		if(endTime!=null)
//		{
//			dc.add(Restrictions.le("workPayInfo.createDate", endTime));
//		}
		dc.addOrder(Order.desc("id"));
		return workFinancePayInfoRelationDao.find(page, dc);
	}
	
	public List<WorkFinancePayInfoRelation> findByFinancePay(
			 Long financeInfo,Long payInfo) {
		
		DetachedCriteria dc = workFinancePayInfoRelationDao.createDetachedCriteria();
		dc.createAlias("financePaymentInfo", "financePaymentInfo");
		dc.add(Restrictions.eq("financePaymentInfo.id", financeInfo));
		dc.add(Restrictions.eq("workPayInfo.id", payInfo));
		dc.addOrder(Order.desc("id"));
		return workFinancePayInfoRelationDao.find(dc);
	}
	
	
	
	
	
	public Page<WorkFinancePayInfoRelation> findFinancePay(
			Page<WorkFinancePayInfoRelation> page,
			WorkFinancePayInfoRelation workFinancePayInfoRelation,
			Date startTime,
			Date endTime,
			 List<Long> idList ,
			 List<Long> financeids ,
			 List<Long> financeByAreaIds ,
			 Integer payMethod
			) {
		DetachedCriteria dc = workFinancePayInfoRelationDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.createAlias("financePaymentInfo", "financePaymentInfo");
		if (startTime!=null) {
			dc.add(Restrictions.ge("workPayInfo.createDate", startTime));
		}
		if (endTime!=null) {
			dc.add(Restrictions.le("workPayInfo.createDate", endTime));
		}
//		if (workFinancePayInfoRelation.getWorkPayInfo()!=null) {
//			if (workFinancePayInfoRelation.getWorkPayInfo().getMethodGov()!=null&&workFinancePayInfoRelation.getWorkPayInfo().getMethodGov()) {
//				dc.add(Restrictions.eq("workPayInfo.methodGov", true));
//			}
//			if (workFinancePayInfoRelation.getWorkPayInfo().getMethodContract()!=null&&workFinancePayInfoRelation.getWorkPayInfo().getMethodContract()) {
//				dc.add(Restrictions.eq("workPayInfo.methodContract", true));
//			}
//		}
		
		if (idList!=null&& idList.size()>0) {
			dc.add(Restrictions.in("financePaymentInfo.id", idList));
		}
		if (financeids!=null&& financeids.size()>0) {
			dc.add(Restrictions.in("financePaymentInfo.id", financeids));
		}
		if (financeByAreaIds!=null&& financeByAreaIds.size()>0) {
			dc.add(Restrictions.in("financePaymentInfo.id", financeByAreaIds));
		}
		
		if (payMethod!=null) {
			dc.add(Restrictions.eq("financePaymentInfo.paymentMethod", payMethod));
		}
		
//		if (StringUtils.isNotEmpty(workFinancePayInfoRelation.getName())){
//			dc.add(Restrictions.like("name", "%"+workFinancePayInfoRelation.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(WorkFinancePayInfoRelation.DEL_FLAG, WorkFinancePayInfoRelation.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return workFinancePayInfoRelationDao.find(page, dc);
	}
	
	public WorkFinancePayInfoRelation findShow(WorkFinancePayInfoRelation workFinancePayInfoRelation) {
		DetachedCriteria dc = workFinancePayInfoRelationDao.createDetachedCriteria();
		dc.addOrder(Order.desc("id"));
		return workFinancePayInfoRelationDao.find(dc).get(0);
	}
	
	public List<WorkFinancePayInfoRelation> findByPayInfo(WorkPayInfo payInfo){
		DetachedCriteria dc = workFinancePayInfoRelationDao.createDetachedCriteria();
		dc.createAlias("workPayInfo", "workPayInfo");
		dc.add(Restrictions.eq("workPayInfo", payInfo));
		dc.addOrder(Order.desc("id"));
		return workFinancePayInfoRelationDao.find(dc);
	}
	
	
	
	
	
	@Transactional(readOnly = false)
	public void save(WorkFinancePayInfoRelation workFinancePayInfoRelation) {
		workFinancePayInfoRelationDao.save(workFinancePayInfoRelation);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		workFinancePayInfoRelationDao.deleteById(id);
	}
	
	/*
	 * 导出excel数据
	 */
	public void exportExcelData(HttpServletRequest request, HttpServletResponse response, Collection<FinancePaymentInfo> coll) throws Exception{
		String[] title = {"付款单位名称","付款金额\u0028元\u0029","未确认款项\u0028元\u0029","联系人","联系方式","付款时间","付款方式","记录时间"};
		String sheetName = "支付款项统计.xlsx";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Type","application/force-download");   
	    response.setHeader("Content-Type","application/vnd.ms-excel");   
	    response.setHeader("Content-disposition", "attachment; filename=" + new String(sheetName.getBytes("utf-8"),"ISO8859-1"));
		try {
			XSSFWorkbook wb = createDS(CreateExcelUtils.createExcel(title), coll);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.close();
		} catch (Exception e) {
			throw new Exception();
		}
	}
	
	/*
	 * 封装数据集
	 */
	private XSSFWorkbook createDS(XSSFWorkbook wb, Collection<FinancePaymentInfo> coll) throws Exception{
		if(wb == null){
			return null;
		}
		Sheet sheet = wb.getSheetAt(0);
		if(sheet == null){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Row row = null;
		Cell cell = null;
		FinancePaymentInfo financePaymentInfo = null;
		double balance = 0;
		int dsSize = 0;
		try{
			ArrayList<FinancePaymentInfo> arrList = (ArrayList<FinancePaymentInfo>)coll;
			dsSize = arrList.size() + 1;
			for(int i = 0; i < dsSize; i++){
				if(i == 0){
					continue;
				}
				financePaymentInfo = arrList.get(0);
				balance = financePaymentInfo.getPaymentMoney() - financePaymentInfo.getResidueMoney();
				row = sheet.createRow(i);
				for(int j = 0; j < WB_TITLE_LENGTH; j++){
					cell = row.createCell(j);
					switch(j){
					case 0: cell.setCellValue(financePaymentInfo.getCompany());
						break;
					case 1: cell.setCellValue(balance);
						break;
					case 2: cell.setCellValue(financePaymentInfo.getResidueMoney());
						break;
					case 3: cell.setCellValue(financePaymentInfo.getCommUserName());
						break;
					case 4: cell.setCellValue(financePaymentInfo.getCommMobile());
						break;
					case 5: cell.setCellValue(format.format(financePaymentInfo.getCreateDate()));
						break;
					case 6: cell.setCellValue(paymentMethod(financePaymentInfo.getPaymentMethod()));
						break;
					case 7: cell.setCellValue(format.format(financePaymentInfo.getCreateDate()));
						break;
					}
				}
				arrList.remove(0);
			}
		}catch(Exception ex){
			throw new Exception();
		}
		return wb;
	}
	
	private String paymentMethod(int m){
		switch(m){
			case 1: return "现金";
			case 2: return "POS收款";
			case 3: return "银行转账";
			case 4: return "支付宝转账";
		}
		return "";
	}
}
