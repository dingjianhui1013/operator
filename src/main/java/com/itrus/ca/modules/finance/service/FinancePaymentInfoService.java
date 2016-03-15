/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.finance.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.TrueFalseType;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.dao.FinancePaymentInfoDao;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 支付信息Service
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class FinancePaymentInfoService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(FinancePaymentInfoService.class);
	
	@Autowired
	private FinancePaymentInfoDao financePaymentInfoDao;
	
	public FinancePaymentInfo get(Long id) {
		return financePaymentInfoDao.findOne(id);
	}
	
	public Page<FinancePaymentInfo> find(Page<FinancePaymentInfo> page, FinancePaymentInfo financePaymentInfo, Date startTime, Date endTime) {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
//		dc.createAlias("createBy", "createBy");
//		dc.createAlias("createBy.office", "office");
		//dc.add(dataScopeFilter(UserUtils.getUser(),"office", "createBy"));
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId", "officeId"));
		
		
		if (financePaymentInfo.getCompany()!=null) {
			if (StringUtils.isNotEmpty(financePaymentInfo.getCompany())){
				dc.add(Restrictions.like("company", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCompany())+"%"));
			}
		}
		if (StringUtils.isNotEmpty(financePaymentInfo.getCommUserName())) {
			dc.add(Restrictions.like("commUserName", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCommUserName())+"%"));
		}
		if (startTime!=null&&endTime!=null) {
			dc.add(Restrictions.ge("payDate", startTime));
			dc.add(Restrictions.le("payDate", endTime));
		}
		dc.add(Restrictions.not(Restrictions.eq("quitMoneyStatus", 1)));
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(page, dc);
	}
	public Page<FinancePaymentInfo> findAdjustment(Page<FinancePaymentInfo> page, FinancePaymentInfo financePaymentInfo, Date startTime, Date endTime) {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(dataScopeFilter(UserUtils.getUser(),"office", "createBy"));
		if (financePaymentInfo.getCompany()!=null) {
			if (StringUtils.isNotEmpty(financePaymentInfo.getCompany())){
				dc.add(Restrictions.like("company", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCompany())+"%"));
			}
		}
		if (StringUtils.isNotEmpty(financePaymentInfo.getCommUserName())) {
			dc.add(Restrictions.like("commUserName", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCommUserName())+"%"));
		}
		if (startTime!=null&&endTime!=null) {
			dc.add(Restrictions.ge("payDate", startTime));
			dc.add(Restrictions.le("payDate", endTime));
		}
		dc.add(Restrictions.not(Restrictions.eq("quitMoneyStatus", 1)));
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(page, dc);
	}
	public List<FinancePaymentInfo> findAdjustment(FinancePaymentInfo financePaymentInfo) {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
//		dc.createAlias("createBy", "createBy");
//		dc.createAlias("createBy.office", "office");
//		dc.add(dataScopeFilter(UserUtils.getUser(),"office", "createBy")); 
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId", "officeId"));
		if (financePaymentInfo.getCompany()!=null) {
			if (StringUtils.isNotEmpty(financePaymentInfo.getCompany())){
				dc.add(Restrictions.like("company", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCompany())+"%"));
			}
		}
		if (StringUtils.isNotEmpty(financePaymentInfo.getCommUserName())) {
			dc.add(Restrictions.like("commUserName", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCommUserName())+"%"));
		}
		dc.add(Restrictions.not(Restrictions.eq("quitMoneyStatus", 1)));
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(dc);
	}
	public Page<FinancePaymentInfo> find1(Page<FinancePaymentInfo> page, FinancePaymentInfo financePaymentInfo, Date startTime, Date endTime, List<Long> ids) {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId", "officeId"));
//		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId", "officeId"));
		if (financePaymentInfo.getCompany()!=null) {
			if (StringUtils.isNotEmpty(financePaymentInfo.getCompany())){
				dc.add(Restrictions.like("company", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCompany())+"%"));
			}
		}
		if (financePaymentInfo.getPaymentMethod()!=null) {
			dc.add(Restrictions.eq("paymentMethod", financePaymentInfo.getPaymentMethod()));
		}
		if (startTime!=null&&endTime!=null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE,1);
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", calendar.getTime()));
		}
		if (ids.size()>0) {
			dc.add(Restrictions.in("officeId", ids));
		}
		
		dc.add(Restrictions.gt("bingdingTimes", 0));
		
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(page, dc);
	}
	
	
	public List<FinancePaymentInfo> findList(FinancePaymentInfo financePaymentInfo, Date startTime, Date endTime, List<Long> ids) {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.add(dataScopeFilterByWorkDealInfo(UserUtils.getUser(), "areaId", "officeId"));
		if (financePaymentInfo.getCompany()!=null) {
			if (StringUtils.isNotEmpty(financePaymentInfo.getCompany())){
				dc.add(Restrictions.like("company", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCompany())+"%"));
			}
		}
		if (financePaymentInfo.getPaymentMethod()!=null) {
			dc.add(Restrictions.eq("paymentMethod", financePaymentInfo.getPaymentMethod()));
		}
		if (startTime!=null&&endTime!=null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE,1);
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", calendar.getTime()));
		}
		if (ids.size()>0) {
			dc.add(Restrictions.in("officeId", ids));
		}
		dc.add(Restrictions.gt("bingdingTimes", 0));
		
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(dc);
	}
	
	
	
	
	public List<FinancePaymentInfo> find1list(FinancePaymentInfo financePaymentInfo, Date startTime, Date endTime, List<Long> ids) {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(dataScopeFilter(UserUtils.getUser(),"office", "createBy"));
		if (financePaymentInfo.getCompany()!=null) {
			if (StringUtils.isNotEmpty(financePaymentInfo.getCompany())){
				dc.add(Restrictions.like("company", "%"+EscapeUtil.escapeLike(financePaymentInfo.getCompany())+"%"));
			}
		}
		if (financePaymentInfo.getPaymentMethod()!=null) {
			dc.add(Restrictions.eq("paymentMethod", financePaymentInfo.getPaymentMethod()));
		}
		if (startTime!=null&&endTime!=null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE,1);
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", calendar.getTime()));
		}
		if (ids.size()>0) {
			dc.add(Restrictions.in("office.id", ids));
		}
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(dc);
	}

	//根据应用名称查询出支付信息
	public List<FinancePaymentInfo> findByAppName(String appName) {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		if (appName!=null) {
				dc.add(Restrictions.like("configApp.appName", "%"+EscapeUtil.escapeLike(appName)+"%"));
		}
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(dc);
	}
	
	

	//根据单位名称或者备注查询
	public List<FinancePaymentInfo> findByCompany(String load, Date startTime, Date endTime, Long appId, Office office){
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		dc.add(Restrictions.or(Restrictions.like("company", "%"+EscapeUtil.escapeLike(load)+"%"),Restrictions.like("remarks", "%"+EscapeUtil.escapeLike(load)+"%")));
		if (startTime!=null&&endTime!=null) {
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
//		dc.add(Restrictions.eq("createBy.office", office));
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
//		dc.add(Restrictions.eq("configApp.id",appId));
		dc.add(Restrictions.gt("residueMoney",0D));
		dc.addOrder(Order.desc("payDate"));
		List<FinancePaymentInfo> all =  financePaymentInfoDao.find(dc);
		for (int i = 0; i < all.size();) {
			if (all.get(i).getConfigApp()==null||all.get(i).getConfigApp().getId().equals(appId)) {
				i++;
				continue;
			}else {
				all.remove(i);
			}
		}
		return all;
	}
	
	//根据应用ID查询出支付信息
		public List<FinancePaymentInfo> findByAppId(List<Long> appids ) {
			DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
			dc.createAlias("configApp", "configApp");
			if (appids!=null&& appids.size()>0) {
				dc.add(Restrictions.in("configApp.id", appids));
			}
			dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
			dc.addOrder(Order.desc("id"));
			return financePaymentInfoDao.find(dc);
		}
	
		
		public List<FinancePaymentInfo> findByIds(Long[] ids ) {
			DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
			dc.add(Restrictions.in("id", ids));
			dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("residueMoney"));
			return financePaymentInfoDao.find(dc);
		}
		
		public List<FinancePaymentInfo> findByIds(List<Long> ids ) {
			DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
			dc.add(Restrictions.in("id", ids));
			dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
			dc.addOrder(Order.asc("residueMoney"));
			return financePaymentInfoDao.find(dc);
		}
		
		
	public List<String> findCompanyId(){
		return financePaymentInfoDao.findCompanyId();
	}
	
	
	public List<FinancePaymentInfo> findByCompanyName(String companyName){
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq("company", companyName));
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("residueMoney"));
		return financePaymentInfoDao.find(dc);
	}
	
	public List<FinancePaymentInfo> findAll() {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(dc);
	}
	public List<FinancePaymentInfo> findAll(FinancePaymentInfo financePaymentInfo) {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		if(financePaymentInfo.getCreateBy()!=null)
		{
			dc.add(Restrictions.eq("createBy", financePaymentInfo.getCreateBy()));
		}
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return financePaymentInfoDao.find(dc);
	}
	//根据收款编号进行排序
	public List<FinancePaymentInfo> findRemark() {
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		dc.add(Restrictions.eq(FinancePaymentInfo.DEL_FLAG, FinancePaymentInfo.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("remark"));
		return financePaymentInfoDao.find(dc);
	}
	
	//根据流水号、单位、银行判断是否有支付信息录入信息
	public boolean findByCompanyBank(String serialNum,String paymentBank,String id){
		DetachedCriteria dc = financePaymentInfoDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(serialNum)&&StringUtils.isNotEmpty(paymentBank)) {
			dc.add(Restrictions.eq("serialNum", serialNum));
			dc.add(Restrictions.eq("paymentBank", paymentBank));
			
			if (id != null && !"".equals(id)) {
				dc.add(Restrictions.ne("id", Long.parseLong(id)));
			}
		}else{
			return false;
		}
		if (financePaymentInfoDao.find(dc).size()>0) {
			return true;
		}else {
			return false;
		}
	}
	
	@Transactional(readOnly = false)
	public void save(FinancePaymentInfo financePaymentInfo) {
		financePaymentInfoDao.save(financePaymentInfo);
	}
	
	@Transactional(readOnly = false)
	public void saveList(List<FinancePaymentInfo> financePaymentInfo) {
		financePaymentInfoDao.save(financePaymentInfo);
	}

	/*
	 * 更新状态，不删除数据
	 */
	@Transactional(readOnly = false)
	public void delete(Long id) {
		financePaymentInfoDao.deleteById(id);
	}
	
	/*
	 * 删除数据
	 */
	@Transactional(readOnly = false)
	public void deleteById(Long id){
		financePaymentInfoDao.delete(id);
	}
	
	@Transactional(readOnly = false)
	public JSONObject saveExcelDate(MultipartFile file, String ifExcel) {
		StringBuffer ifErr = new StringBuffer();
		/*
		 * 创建临时路径
		 */
		StringBuffer tempPath = new StringBuffer("E:/temp/");
		tempPath.append(System.currentTimeMillis());	//获取系统以毫秒为单位的当前时间
		tempPath.append(ifExcel);//获取参数@Param file的路径
		
		/*
		 * 创建临时excel文件存储@Param file数据
		 */
		File tempFile = new File(tempPath.toString());
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();			//获取父目录，创建
		}
		try {
			tempFile.createNewFile();					//创建文件
			file.transferTo(tempFile);					//收到文件传输到目标文件
		} catch (IOException e) {
			ifErr.append("本地目录  E:/temp/ 不存在");
			return ifErr(-1, ifErr.toString());
		}
		
		/*
		 * 解析@Param file文件
		 */
		Workbook wb = null;
		try {
			wb = createWB(tempFile.getAbsolutePath());
		} catch (Exception e) {
			ifErr.append("模板创建失败");
			return ifErr(-1, ifErr.toString());
		}
		List<FinancePaymentInfo> listDate = new ArrayList<FinancePaymentInfo>();
		int rows = 0;								//总行数
		int cells = 0;								//总列数
		Sheet sheetAt0 = wb.getSheetAt(0);			//获取第一个工作表
		Row row = null;
		row = sheetAt0.getRow(0);
		if(!validTitle(row)){
			ifErr.append("导入模板格式有误，请确认");
			return ifErr(-1, ifErr.toString());
		}
		rows = sheetAt0.getPhysicalNumberOfRows();
		if(rows <= 1){
			ifErr.append("模板为空！");
			return ifErr(-1, ifErr.toString());
		}
//		cells = sheetAt0.getRow(0).getPhysicalNumberOfCells();
		Long remark = queryCount();
		for(int i = 0; i < rows; i++){
			if(i == 0){
				continue;
			}
			row = sheetAt0.getRow(i);
			if(row == null){
				ifErr.append("第"+i+"行为空，导入失败，请重新导入数据！");
				return ifErr(-1, ifErr.toString());
			}
			FinancePaymentInfo financePaymentInfo = new FinancePaymentInfo();
			for(int j = 0; j < 10; j++){
				Cell cell = row.getCell(j);
				if (j==1||j==2||j==3||j==6) {
					if(cell== null || "".equals(cell.getStringCellValue())){
						switch (j){
							case 1:
								ifErr.append("第"+i+"行交易时间为空，请重新录入！");
								break;
							case 2:
								ifErr.append("第"+i+"行付款金额为空，请重新录入！");
								break;
							case 3:
								ifErr.append("第"+i+"行付款方式为空，请重新录入！");
								break;
							case 6:
								ifErr.append("第"+i+"行付款单位名称为空，请重新录入！");
								break;

						}
						return ifErr(-1, ifErr.toString());
					}
				}
				try{
					switch(j){
						case 0: 
							//financePaymentInfo.setSerialNum(disDataType(cell));
							
							if (cell!=null) {
								financePaymentInfo.setSerialNum(cell.toString());
							}
							break;
						case 1: financePaymentInfo.setPayDate(formatDate(disDataType(cell)));
							break;
						case 2:
								financePaymentInfo.setPaymentMoney(Double.parseDouble(disDataType(cell)));
								financePaymentInfo.setResidueMoney(Double.parseDouble(disDataType(cell)));
							break;
						case 3: 
							financePaymentInfo.setPaymentMethod((int)Double.parseDouble(disDataType(cell)));
							break;
						case 4: 
							if (cell!=null) {
							financePaymentInfo.setPaymentAccount(cell.toString());
							}
							break;
						case 5: 
							if (cell!=null) {
							financePaymentInfo.setPaymentBank(cell.toString());
							}
							break;
						case 6: 
							if (cell!=null) {
							financePaymentInfo.setCompany(cell.toString());
							}
							break;
						case 7: 
							if (cell!=null) {
							financePaymentInfo.setCommUserName(cell.toString());
							}
							break;
						case 8: 
							if (cell!=null) {
							financePaymentInfo.setCommMobile(cell.toString());
							}
							break;
						case 9: 
							if (cell!=null) {
								financePaymentInfo.setRemark(remark.toString());
								financePaymentInfo.setRemarks(cell.toString());
							}
								financePaymentInfo.setCreateDate(new Date());
								financePaymentInfo.setQuitMoneyStatus(0);
								
							break;
					}
				}catch(Exception ex){
					ex.printStackTrace();
					ifErr.append("第"+i+"条数据出现问题，导入失败！");
					return ifErr(-1, ifErr.toString());
				}
			}
			
			boolean repeat =  findByCompanyBank(financePaymentInfo.getSerialNum(), financePaymentInfo.getPaymentBank(),null);
			if (repeat) {
				ifErr.append("第"+i+"条数据已存在，导入失败！");
				return ifErr(-1, ifErr.toString());
			}

			if(financePaymentInfo.getPaymentMethod()==3 && (financePaymentInfo.getPaymentBank()==null ||
					"".equals(financePaymentInfo.getPaymentBank())|| financePaymentInfo.getPaymentAccount()==null
					|| "".equals(financePaymentInfo.getPaymentAccount())) ){//付款方式为 银行转账
				ifErr.append("第"+i+"条数据付款方式为银行转账，付款银行、付款账号不能为空，导入失败！");
				return ifErr(-1, ifErr.toString());
			}
			if(financePaymentInfo.getPaymentMethod()==4 &&
					(financePaymentInfo.getPaymentAccount()==null ||"".equals(financePaymentInfo.getPaymentAccount()))){//付款方式为 支付宝转账
				ifErr.append("第"+i+"条数据付款方式为支付宝转账，付款账号不能为空，导入失败！");
				return ifErr(-1, ifErr.toString());
			}
				for (int j = 0; j < listDate.size(); j++) {
				if (financePaymentInfo.getPaymentBank().equals(listDate.get(j).getPaymentBank())
						&&financePaymentInfo.getSerialNum().equals(listDate.get(j).getSerialNum())) {
					ifErr.append("第"+i+"条数据流水号重复，导入失败！");
					return ifErr(-1, ifErr.toString());
				}
			}
			financePaymentInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
			financePaymentInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
			listDate.add(financePaymentInfo);
		}
		saveList(listDate);
		return ifErr(1, ifErr.toString());
	}
	
	/**
	 * 创建workbook
	 */
	private Workbook createWB(String filePath) throws Exception{
		/*
		 * 判断路径为空或者不是xls或xlsx文件
		 */
		if(filePath.equals(null) || filePath.equals("") || !filePath.matches("^.+\\.(?i)((xls)|(xlsx))$")){
			return null;
		}
		
		/*
		 * 判断文件是xls还是xlsx
		 */
		boolean ifExcel2003 = true;
		if(filePath.matches("^.+\\.(?i)(xlsx)$")){
			ifExcel2003 = false;
		}
		
		/*
		 * 
		 */
		InputStream is = null;
		try {
			 is = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		}
		
		/*
		 * 根据版本选择创建Workbook的方式
		 */
		Workbook wb = null;
		try {
			wb = ifExcel2003 ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
		} catch (IOException e) {
			throw new IOException();
		}
		
		/*
		 * 解析excel数据
		 */
		is.close();
		return wb;
	}
	
	/*
	 * 处理数据类型
	 */
	private String disDataType(Cell cell) throws Exception{
		if(cell == null){
			throw new Exception();
		}
		if(cell.getCellType() == Cell.CELL_TYPE_BLANK){
			throw new Exception();
		}
		/** 判断cell是否为数字类型 0*/
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			/** 判断cell是否为日期类型 */
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				return new Long(cell.getDateCellValue().getTime()).toString();
			}
			return cell.getNumericCellValue() + "";
		}
		
		/**判断是否为boolean类型 4 */
		else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
			return cell.getBooleanCellValue() + "";
		}
		
		/** 判断是否为字符串类型 1 */
		else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
		}else{
			return cell.toString();
		}
	}
	
	private JSONObject ifErr(int status, String msg) {
		JSONObject json = new JSONObject();
		try {
			json.put("status", status);
			json.put("msg", msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public Long queryCount(){
		return financePaymentInfoDao.count();
	}
	
	/*
	 * 格式化日期字符串
	 */
	private Date formatDate(String date) throws ParseException{
		DateFormat sdf = null;
		if(date.indexOf("-") != -1){
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}else if(date.indexOf("/") != -1){
			sdf = new SimpleDateFormat("yyyy/MM/dd");
		}else{
			sdf = new SimpleDateFormat("yyyyMMdd");
		}
		return sdf.parse(date);
	}
	
	/*
	 * 处理数值类型手机号
	 */
	private String formatPhoneNum(Double phoneNum){
		if(phoneNum == null){
			return null;
		}
		DecimalFormat df = new DecimalFormat("#");
		return df.format(phoneNum);
	}
	
	/*
	 * 校验excle表头
	 */
	private boolean validTitle(Row row){
		if(!row.getCell(0).toString().replace(" ", "").equals("交易流水号"))return false;
		if(!row.getCell(1).toString().replace(" ", "").equals("交易时间"))return false;
		if(!row.getCell(2).toString().replace(" ", "").equals("付款金额"))return false;
		if(!row.getCell(3).toString().replace(" ", "").equals("付款方式"))return false;
		if(!row.getCell(4).toString().replace(" ", "").equals("付款账号"))return false;
		if(!row.getCell(5).toString().replace(" ", "").equals("付款银行"))return false;
		if(!row.getCell(6).toString().replace(" ", "").equals("付款单位名称"))return false;
		if(!row.getCell(7).toString().replace(" ", "").equals("联系人"))return false;
		if(!row.getCell(8).toString().replace(" ", "").equals("联系方式"))return false;
		if(!row.getCell(9).toString().replace(" ", "").equals("备注"))return false;
		return true;
	}
}
