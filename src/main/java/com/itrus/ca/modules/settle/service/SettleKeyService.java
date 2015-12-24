/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.ast.Var;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.settle.entity.SettleKey;
import com.itrus.ca.modules.settle.dao.SettleKeyDao;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 供应商返修Key记录Service
 * @author whw
 * @version 2014-07-16
 */
@Component
@Transactional(readOnly = true)
public class SettleKeyService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SettleKeyService.class);
	
	@Autowired
	private SettleKeyDao settleKeyDao;
	
	@Autowired
	private ConfigSupplierService configSupplierService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	public SettleKey get(Long id) {
		return settleKeyDao.findOne(id);
	}
	
	public Page<SettleKey> find(Page<SettleKey> page, SettleKey settleKey, Long configSupplierId, Long generalId, String keySn,Date startTime,Date endTime,Date startBackTime,Date endBackTime) {
		DetachedCriteria dc = settleKeyDao.createDetachedCriteria();
		
		dc.createAlias("sysUser", "sysUser");
		dc.createAlias("sysUser.office", "office");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "sysUser"));
		
		if (configSupplierId!=null) {
			dc.add(Restrictions.eq("configSupplier.id", configSupplierId));
		}
		if (generalId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", generalId));
		}
		if (StringUtils.isNotEmpty(keySn)) {
			dc.add(Restrictions.like("keySn", "%"+keySn+"%"));
		}
		if (startTime!=null) {
			dc.add(Restrictions.ge("comeDate", startTime));
		}
		if (endTime!=null) {
			dc.add(Restrictions.le("comeDate", endTime));
		}
		if (startBackTime!=null) {
			dc.add(Restrictions.ge("backDate", startBackTime));
		}
		if (endBackTime!=null) {
			dc.add(Restrictions.le("backDate", endBackTime));
		}
		dc.addOrder(Order.desc("id"));
		return settleKeyDao.find(page, dc);
	}
	
	public List<SettleKey> findBySE(Date startTime,Date endTime) {
		DetachedCriteria dc = settleKeyDao.createDetachedCriteria();
		if (startTime!=null) {
			dc.add(Restrictions.ge("comeDate", startTime));
		}
		if (endTime!=null) {
			dc.add(Restrictions.le("comeDate", endTime));
		}
		dc.addOrder(Order.desc("id"));
		return settleKeyDao.find(dc);
	}
	
	public List<SettleKey> findByIds(List<Long> ids) {
		DetachedCriteria dc = settleKeyDao.createDetachedCriteria();
		if (ids.size()>0) {
			dc.add(Restrictions.in("id", ids));
		}
		dc.addOrder(Order.desc("id"));
		return settleKeyDao.find(dc);
	}
	 
	
	
	public List<SettleKey> exportSettle(Long configSupplierId, Long generalId, String keySn,Date startTime,Date endTime) {
		DetachedCriteria dc = settleKeyDao.createDetachedCriteria();
		dc.createAlias("sysUser", "sysUser");
		dc.createAlias("sysUser.office", "office");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "sysUser"));
		if (configSupplierId!=null) {
			dc.add(Restrictions.eq("configSupplier.id", configSupplierId));
		}
		if (generalId!=null) {
			dc.add(Restrictions.eq("keyGeneralInfo.id", generalId));
		}
		if (StringUtils.isNotEmpty(keySn)) {
			dc.add(Restrictions.eq("keySn", keySn));
		}
		if (startTime!=null) {
			dc.add(Restrictions.ge("comeDate", startTime));
		}
		if (endTime!=null) {
			dc.add(Restrictions.le("comeDate", endTime));
		}
		dc.addOrder(Order.desc("id"));
		return settleKeyDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SettleKey settleKey) {
		settleKeyDao.save(settleKey);
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
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		List<SettleKey> listDate = new ArrayList<SettleKey>();
		int rows = 0;								//总行数
		int cells = 0;								//总列数
		Sheet sheetAt0 = wb.getSheetAt(0);			//获取第一个工作表
		rows = sheetAt0.getPhysicalNumberOfRows();
		if(rows <= 1){
			ifErr.append("模板为空！");
			return ifErr(-1, ifErr.toString());
		}
//		cells = sheetAt0.getRow(0).getPhysicalNumberOfCells();
		for(int i = 0; i < rows; i++){
			if(i == 0|| i==1){
				continue;
			}
			Row row = sheetAt0.getRow(i);
			if(row == null){
				ifErr.append("第"+(i+1)+"行为空，导入失败，请重新导入数据！");
				return ifErr(-1, ifErr.toString());
			}
			SettleKey settleKey = new SettleKey();
			for(int j = 0; j < 5; j++){
				Cell cell = row.getCell(j);
				
				if (j<=4) {
					if(cell == null){
						ifErr.append("第"+(i+1)+"行第"+(j+1)+"列数据数据为空，请重新录入！");
						return ifErr(-1, ifErr.toString());
					}
				}
				try{
					switch(j){
							case 0: 
								String comeDateStringa = cell.toString();
								Date comeDate =   dfs.parse(comeDateStringa);
								settleKey.setComeDate(new Timestamp(comeDate.getTime()));
								break;
							case 1: 
								String backDateStringa = cell.toString();
								if (backDateStringa==null||backDateStringa.equals("")) {
									settleKey.setBackDate(new Timestamp(new Date().getTime()));
								}else{
									Date backDate =   dfs.parse(backDateStringa);
									settleKey.setBackDate(new Timestamp(backDate.getTime()));
								}
								break;
							case 2: 
								ConfigSupplier supplier = configSupplierService.findByName(cell.toString());
								if (supplier==null) {
									ifErr.append("第"+(i+1)+"行第"+(j+1)+"列厂商名称不存在，请重新录入！");
									return ifErr(-1, ifErr.toString());
								}else {
									settleKey.setConfigSupplier(supplier);
								}
								break;
							case 3: 
								KeyGeneralInfo keyGeneralInfo = keyGeneralInfoService.findInfoByName(cell.toString());
								if (keyGeneralInfo==null) {
									ifErr.append("第"+(i+1)+"行第"+(j+1)+"列KEY类型名称不存在，请重新录入！");
									return ifErr(-1, ifErr.toString());
								}else {
									settleKey.setKeyGeneralInfo(keyGeneralInfo);
								}
								break;
							case 4: 
								
								settleKey.setKeySn(cell.toString());
								break;
					}
				}catch(Exception ex){
					ex.printStackTrace();
					ifErr.append("第"+i+"条数据出现问题，导入失败！");
					return ifErr(-1, ifErr.toString());
				}
			}
			
//			boolean repeat =  findByCompanyBank(row.getCell(0).toString(), row.getCell(5).toString(), row.getCell(6).toString());
//			if (repeat) {
//				ifErr.append("第"+i+"条数据已存在，导入失败！");
//				return ifErr(-1, ifErr.toString());
//			}
		
			
			settleKey.setSysUser(UserUtils.getUser());
			listDate.add(settleKey);
		}
		saveList(listDate);
		return ifErr(1, ifErr.toString());
	}
	
	
	
	@Transactional(readOnly = false)
	public void saveList(List<SettleKey> settleKey) {
		settleKeyDao.save(settleKey);
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
	
	
	
}
