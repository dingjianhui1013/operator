/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.FileDownloadUtil;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyManufacturerService;
import com.itrus.ca.modules.profile.entity.ConfigChargeSupplierDetail;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.settle.entity.SettleKey;
import com.itrus.ca.modules.settle.service.SettleKeyService;

/**
 * 供应商返修Key记录Controller
 * @author whw
 * @version 2014-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/settleKey")
public class SettleKeyController extends BaseController {
	
	@Autowired
	private KeyManufacturerService keyManufacturerService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	@Autowired
	private SettleKeyService settleKeyService;
	
	@Autowired
	private ConfigSupplierService configSupplierService;
	
	@Value(value = "${deploy.path}")
	public String deployPath;
	
	@ModelAttribute
	public SettleKey get(@RequestParam(required=false) Long id) {
		if (id != null){
			return settleKeyService.get(id);
		}else{
			return new SettleKey();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(SettleKey settleKey, 
			@RequestParam(value = "supplierId", required = false) Long supplierId,
			@RequestParam(value = "keyId", required = false) Long keyId,
			@RequestParam(value = "keySn", required = false) String keySn,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "startBackTime", required = false) Date startBackTime,
			@RequestParam(value = "endBackTime", required = false) Date endBackTime,
			HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<SettleKey> page = settleKeyService.find(new Page<SettleKey>(request, response), settleKey,supplierId,keyId,keySn,startTime,endTime,startBackTime,endBackTime); 
        model.addAttribute("page", page);
    	List<ConfigSupplier> suppliers = configSupplierService.findByKey();
    	if (checkIds!=null) {
    		String[] ids = checkIds.split(",");
    		model.addAttribute("ids", ids);
		}
    	
    	model.addAttribute("keySn", keySn);
    	model.addAttribute("configSupplierId", supplierId);
    	model.addAttribute("keyId", keyId);
//    	model.addAttribute("startTime", startTime==null? new Date(System.currentTimeMillis()- 30L*24*60*60*1000): startTime);   //开始时间为空，则为当前时间前30天
//		model.addAttribute("endTime", endTime ==null? new Date(): endTime); //结束时间，默认当前时间
    	model.addAttribute("checkIds", checkIds);
    	
		model.addAttribute("suppliers",suppliers);
		if (supplierId!=null) {
			 List<KeyGeneralInfo>  keys = keyGeneralInfoService.findBySupplierId(supplierId);
		        model.addAttribute("keys", keys);
		}
		return "modules/settle/settleKeyList";
	}

	@RequestMapping(value = "form")
	public String form(SettleKey settleKey, Model model) {
		List<ConfigSupplier> suppliers = configSupplierService.findByKey();
		model.addAttribute("suppliers",suppliers);
		
		List<KeyGeneralInfo> keyGeneralInfos = keyGeneralInfoService.findAll();
		model.addAttribute("keyGeneralInfo", keyGeneralInfos);
		model.addAttribute("settleKey", settleKey);
		model.addAttribute("coDate",  new Date(System.currentTimeMillis()- 30L*24*60*60*1000));   //到货时间默认当前时间前30天
		//model.addAttribute("baDate",  new Date()); //结束时间，默认当前时间
		return "modules/settle/settleKeyForm";
	}

	@RequestMapping(value = "save")
	public String save(SettleKey settleKey,
			@RequestParam(value = "coDate", required = false) Date coDate,
			@RequestParam(value = "baDate", required = false) Date baDate,
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, settleKey)){
			return form(settleKey, model);
		}
		
		if(baDate!=null){
			
			settleKey.setBackDate(new Timestamp(baDate.getTime()));
			
		}
		
		
		settleKey.setComeDate(new Timestamp(coDate.getTime()));
		settleKey.setCreateDate(new Timestamp(new Date().getTime()));
		settleKey.setSysUser(UserUtils.getUser());
		settleKeyService.save(settleKey);
		addMessage(redirectAttributes, "保存供应商返修Key记录成功");
		return "redirect:"+Global.getAdminPath()+"/settle/settleKey/?repage";
	}
	
	
	
	@RequestMapping(value = "updateSome")
	public String updateSome(Date updateDate,Date startTime,Date endTime ,	Model model, RedirectAttributes redirectAttributes){
		List<SettleKey> settleKeys = settleKeyService.findBySE(startTime, endTime);
		if(settleKeys.size()>0){
			for (int i = 0; i < settleKeys.size(); i++) {
				settleKeys.get(i).setComeDate(new Timestamp(updateDate.getTime()));
				settleKeyService.save(settleKeys.get(i));
			}
			addMessage(redirectAttributes, "批量修改到货时间成功");
		}else{
			addMessage(redirectAttributes, "所选时间段没有要修改的记录");

		}

		return "redirect:"+Global.getAdminPath()+"/settle/settleKey/?repage";
	}
	@RequestMapping(value = "updateBackSome")
	public String updateBackSome(Date updateDate,Date startTime,Date endTime ,	Model model, RedirectAttributes redirectAttributes){
		List<SettleKey> settleKeys = settleKeyService.findBySE(startTime, endTime);
		if(settleKeys.size()>0){
			for (int i = 0; i < settleKeys.size(); i++) {
				settleKeys.get(i).setBackDate(new Timestamp(updateDate.getTime()));
				settleKeyService.save(settleKeys.get(i));
			}
			addMessage(redirectAttributes, "批量修改返修时间成功");
		}else{
			addMessage(redirectAttributes, "所选时间段没有要修改的记录");

		}

		return "redirect:"+Global.getAdminPath()+"/settle/settleKey/?repage";
	}
	
	@RequestMapping(value = "updateCheckSome")
	public String updateCheckSome(Date changeTime,String checkIds ,	Model model, RedirectAttributes redirectAttributes){
		
		String[] ids = checkIds.split(",");
		List<Long> idsList = new ArrayList<Long>();
		for (int i = 0; i < ids.length; i++) {
			if (!ids[i].equals("")) {
				idsList.add(Long.parseLong(ids[i]));
			}
		}
		List<SettleKey> settleKeys = settleKeyService.findByIds(idsList);
		for (int i = 0; i < settleKeys.size(); i++) {
				settleKeys.get(i).setComeDate(new Timestamp(changeTime.getTime()));
				settleKeyService.save(settleKeys.get(i));
			}
		addMessage(redirectAttributes, "批量修改到货时间成功");
		return "redirect:"+Global.getAdminPath()+"/settle/settleKey/?repage";
	}
	@RequestMapping(value = "updateCheckBackSome")
	public String updateCheckBackSome(Date changeTime,String checkIds ,	Model model, RedirectAttributes redirectAttributes){
		
		String[] ids = checkIds.split(",");
		List<Long> idsList = new ArrayList<Long>();
		for (int i = 0; i < ids.length; i++) {
			if (!ids[i].equals("")) {
				idsList.add(Long.parseLong(ids[i]));
			}
		}
		List<SettleKey> settleKeys = settleKeyService.findByIds(idsList);
		for (int i = 0; i < settleKeys.size(); i++) {
				settleKeys.get(i).setBackDate(new Timestamp(changeTime.getTime()));
				settleKeyService.save(settleKeys.get(i));
			}
		addMessage(redirectAttributes, "批量修改返修时间成功");
		return "redirect:"+Global.getAdminPath()+"/settle/settleKey/?repage";
	}
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		addMessage(redirectAttributes, "删除供应商返修Key记录成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/settleKey/?repage";
	}
	
	@RequestMapping("exportSettle")
	public void exportSettle(
			@RequestParam(value = "supplierId", required = false) Long supplierId,
			@RequestParam(value = "keyId", required = false) Long keyId,
			@RequestParam(value = "keySn", required = false) String keySn,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			HttpServletRequest request,
			HttpServletResponse response) {
		try {

			List<SettleKey> settles = settleKeyService.exportSettle(supplierId,keyId,keySn,startTime,endTime); 
		       
//			System.out.println(settles.size());
			Workbook wb = new HSSFWorkbook();// 定义工作簿
			CellStyle style = wb.createCellStyle(); //样式对象
			Cell cell =null;
			Sheet sheet = wb.createSheet("供应商返修KEY记录");
			sheet.setDefaultColumnWidth(25);
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));//合并单元格
			Font  font  = wb.createFont(); 
			//第一行数据
			Row row0 = sheet.createRow(0);
			cell = row0.createCell(0);
			Font  font0  = wb.createFont(); 
			CellStyle style0 = wb.createCellStyle();
			font0.setFontHeightInPoints((short)14);
			style0.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平   
			style0.setFont(font0);
			
			cell.setCellStyle(style0);
			cell.setCellValue("供应商返修KEY记录");
			Row row2 = sheet.createRow(1);
			cell = row2.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("序号");
			cell = row2.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("到货时间");
			cell = row2.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue("返修时间");
			cell = row2.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue("厂商名称");
			cell = row2.createCell(4);
			cell.setCellStyle(style);
			cell.setCellValue("KEY类型名称");
			cell = row2.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue("KEY编码");
			cell = row2.createCell(6);
			cell.setCellStyle(style);
			cell.setCellValue("操作人员");
			
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
			int last = 0;
			for (int i = 0; i < settles.size(); i++) {
				
				Row row = sheet.createRow(i+2);
				cell = row.createCell(0);
				cell.setCellStyle(style);
				cell.setCellValue(i+1);
				cell = row.createCell(1);
				cell.setCellStyle(style);
				String comeString =dfs.format(settles.get(i).getComeDate());
				cell.setCellValue(comeString);
				cell = row.createCell(2);
				cell.setCellStyle(style);
				String backString =dfs.format(settles.get(i).getBackDate());
				cell.setCellValue(backString);
				cell = row.createCell(3);
				cell.setCellStyle(style);
				cell.setCellValue(settles.get(i).getConfigSupplier().getSupplierName());
				cell = row.createCell(4);
				cell.setCellStyle(style);
				cell.setCellValue(settles.get(i).getKeyGeneralInfo().getName());
				cell = row.createCell(5);
				cell.setCellStyle(style);
				cell.setCellValue(settles.get(i).getKeySn());
				cell = row.createCell(6);
				cell.setCellStyle(style);
				cell.setCellValue(settles.get(i).getSysUser()==null?"":settles.get(i).getSysUser().getName());
				last = i+2;
			}
			Row rowl = sheet.createRow(last+1);
			cell = rowl.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("总计："+settles.size()+"个");
			
			
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=fundExport.xls");
			wb.write(baos);
			byte[] bytes = baos.toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(bytes);
			bos.close();
			baos.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="downloadTemplate")
	public ModelAndView  templateDownLoad(String fileName,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		fileName = URLDecoder.decode(fileName, "utf-8");
		String filePath = this.deployPath + "/template/xls/" +fileName;
        String contentType = "application/octet-stream";  //二级制流,不知道文件类型可用，.*
//        String contentType = "application/x-xls";//.xls格式文件
        try {
			FileDownloadUtil.download(request, response, contentType,filePath,fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return null;
	}
	
	
	// 批量导入
		@RequestMapping("addAttach")
		@ResponseBody
		public String importFile(
				@RequestParam(value = "fileName", required = true) MultipartFile file)
				throws IllegalStateException, IOException, JSONException {
			JSONObject json = new JSONObject();
			String ifExcel = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));//截取.xlsx或.xls
			/*
			 * 判断@Param file文件是否为excel文件
			 */
			if(!(ifExcel.equals(".xls") || ifExcel.equals(".xlsx"))){
				json.put("status", -1);
				json.put("msg", "模板必须为Excel文件");
				return json.toString();
			}
			
			/*
			 * 解析存储excel文件
			 */
			try{
				json = settleKeyService.saveExcelDate(file, ifExcel);
			}catch(Exception ex){
				json.put("status", -1);
				json.put("msg", json.toString());
				return json.toString();
			}
			return json.toString();
		}
	
	
	
	

}
