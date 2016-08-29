package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.web.KeyUsbKeyController;
import com.itrus.ca.modules.profile.entity.ConfigChargeSupplierDetail;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.service.ConfigChargeSupplierDetailService;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.settle.service.SettleSupplierCertDetailService;
import com.itrus.ca.modules.settle.service.SettleVTNService;


@Controller
@RequestMapping(value = "${adminPath}/settle/supplierSettle")
public class SupplierSettleController extends BaseController{
	static Log log = LogFactory.getLog(SupplierSettleController.class);
	@Autowired
	private ConfigSupplierService configSupplierService;
	
	@Autowired
	private SettleSupplierCertDetailService settleSupplierCertDetailService;
	
	@Autowired
	private ConfigChargeSupplierDetailService configChargeSupplierDetailService;
	
	
	@Autowired
	private SettleVTNService settleVTNService;
	
	
	
	@ModelAttribute
	public ConfigSupplier get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configSupplierService.get(id);
		}else{
			return new ConfigSupplier();
		}
	}
	/**
	 * 四川CA对账统计表
	 * @return
	 */
	@RequestMapping("showTableF")
	public String showTableF(Long id,  Model model,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			ConfigSupplier configSupplier, HttpServletRequest request,
			HttpServletResponse response){
		configSupplier.setId(id);
		List<ConfigSupplier> suppliers = configSupplierService.findByCert();
		
		
		
		List<Map<String, Object>> list = settleSupplierCertDetailService.findSupplierJS(configSupplier, startTime, endTime);
		model.addAttribute("WorkDealInfoType", ProductType.productTypeMap);
		model.addAttribute("suppliers", list);
		model.addAttribute("liers",suppliers);
		model.addAttribute("startTime", startTime==null? DateUtils.formatDate(new Date(System.currentTimeMillis()- 30L*24*60*60*1000),"yyyy-MM-dd"): startTime);   //开始时间为空，则为当前时间前30天
		model.addAttribute("endTime", endTime ==null? DateUtils.formatDate(new Date()): endTime); //结束时间，默认当前时间model.addAttribute("WorkDealInfoType", WorkDealInfoType.WorkDealInfoTypeMap);
		return "modules/settle/supplierSettleShowTableF";
	}
	
	/**
	 * 四川CA对账统计表导出
	 */
	@RequestMapping("exportDZ")
	public void exportDZ(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "supplierId", required = false) Long supplierId,
			ConfigSupplier configSupplier, HttpServletRequest request,
			HttpServletResponse response){
		configSupplier.setId(supplierId);
		List<Map<String, Object>> list = settleSupplierCertDetailService.findSupplierJS(configSupplier, startTime, endTime);
		
		Workbook wb = new HSSFWorkbook();// 定义工作簿
		CellStyle style = wb.createCellStyle(); //样式对象
		Cell cell =null;
		Sheet sheet = wb.createSheet("四川CA对账统计表");
		Row row = sheet.createRow(0);
//		sheet.setDefaultColumnWidth(15);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,14));//合并单元格
		Font  font  = wb.createFont(); 
		//第一行数据
		Row row0 = sheet.createRow(0);
		cell = row0.createCell(0);
		Font  font0  = wb.createFont(); 
		CellStyle style0 = wb.createCellStyle();
		font0.setFontHeightInPoints((short)18);
		style0.setFont(font0);
		cell.setCellStyle(style0);
		cell.setCellValue("四川CA对账统计表");
		
		//第二行数据
		sheet.addMergedRegion(new CellRangeAddress(1,1,1,15));
		Row row1 = sheet.createRow(1);
		cell = row1.createCell(1);
		font.setFontHeightInPoints((short)14);
		style.setFont(font);
		cell.setCellStyle(style);
		cell.setCellValue("时间："+startTime+"—"+endTime);
		//第三行数据
		Row row2 = sheet.createRow(2);
        row2.createCell((short) 0).setCellValue("类别");
        row2.createCell((short) 1).setCellValue("证书类型");
        row2.createCell((short) 2).setCellValue("总签发证书数量");
        row2.createCell((short) 3).setCellValue("1替换证书数量");
        row2.createCell((short) 4).setCellValue("2吊销证书数量");
        row2.createCell((short) 5).setCellValue("3测试证书数量");
        row2.createCell((short) 6).setCellValue("4因KEY原因(非人为)补签证书数量");
        row2.createCell((short) 7).setCellValue("5因KEY原因(人为)补签证书数量");
        row2.createCell((short) 8).setCellValue("6因变更用户信息补签证书数量");
        row2.createCell((short) 9).setCellValue("一年期有效证书数量");
        row2.createCell((short) 10).setCellValue("两年期有效证书数量");
        row2.createCell((short) 11).setCellValue("三年期有效证书数量");
        row2.createCell((short) 12).setCellValue("四年期有效证书数量");
        row2.createCell((short) 13).setCellValue("五年期有效证书数量");
        row2.createCell((short) 14).setCellValue("备注");
        cell = row2.createCell((short) 15);
        //第四行遍历的数据
        int i=3;
        for (Map<String, Object> m : list) {
        	row = sheet.createRow(i);
        	row.createCell((short) 0).setCellValue(String.valueOf(m.get("OU")));
            row.createCell((short) 1).setCellValue(ProductType.getProductTypeName(Integer.parseInt(String.valueOf(m.get("REPLACE_AMOUNT")))));
            row.createCell((short) 2).setCellValue(Integer.parseInt(String.valueOf(m.get("TOTAL_AMOUNT"))));
            row.createCell((short) 3).setCellValue(Integer.parseInt(String.valueOf(m.get("REPLACE_AMOUNT"))));
            row.createCell((short) 4).setCellValue(Integer.parseInt(String.valueOf(m.get("REVOKE_AMOUNT"))));
            row.createCell((short) 5).setCellValue(Integer.parseInt(String.valueOf(m.get("TEST_AMOUNT"))));
            row.createCell((short) 6).setCellValue(Integer.parseInt(String.valueOf(m.get("FRW_BB_AMOUNT"))));
            row.createCell((short) 7).setCellValue(Integer.parseInt(String.valueOf(m.get("RW_BB_AMOUNT"))));
            row.createCell((short) 8).setCellValue(Integer.parseInt(String.valueOf(m.get("CHANGE_AMOUNT"))));
            row.createCell((short) 9).setCellValue(Integer.parseInt(String.valueOf(m.get("YEAR1"))));
            row.createCell((short) 10).setCellValue(Integer.parseInt(String.valueOf(m.get("YEAR2"))));
            if(m.get("YEAR3")!=null)
            {
            	row.createCell((short) 11).setCellValue(Integer.parseInt(String.valueOf(m.get("YEAR3"))));
            }else
            {
            	row.createCell((short) 11).setCellValue(0);
            }
            row.createCell((short) 12).setCellValue(Integer.parseInt(String.valueOf(m.get("YEAR4"))));
            row.createCell((short) 13).setCellValue(Integer.parseInt(String.valueOf(m.get("YEAR5"))));
            row.createCell((short) 14).setCellValue("");
            cell = row.createCell((short) 15);
            i++;
		}
        //第N行 小计
		//小计
		Integer total = 0;
		Integer replace = 0;
		Integer revoke = 0;
		Integer test = 0;
		Integer frw = 0;
		Integer rw = 0;
		Integer change = 0;
		Integer year1 = 0;
		Integer year2 = 0;
		Integer year3 = 0;
		Integer year4 = 0;
		Integer year5 = 0;
		for (Map<String, Object> m : list) {
			log.debug(m.get("OU"));
		   total += Integer.parseInt(String.valueOf(m.get("TOTAL_AMOUNT")));
           replace += Integer.parseInt(String.valueOf(m.get("REPLACE_AMOUNT")));
           revoke += Integer.parseInt(String.valueOf(m.get("REVOKE_AMOUNT")));
           test += Integer.parseInt(String.valueOf(m.get("TEST_AMOUNT")));
           frw += Integer.parseInt(String.valueOf(m.get("FRW_BB_AMOUNT")));
           rw += Integer.parseInt(String.valueOf(m.get("RW_BB_AMOUNT")));
           change += Integer.parseInt(String.valueOf(m.get("CHANGE_AMOUNT")));
           year1 += Integer.parseInt(String.valueOf(m.get("YEAR1")));
           year2 += Integer.parseInt(String.valueOf(m.get("YEAR2")));
           if(m.get("YEAR3")!=null)
           {
	         year3 += Integer.parseInt(String.valueOf(m.get("YEAR3")));
           }else
           {
        	   year3 += 0;
           }
           year4 += Integer.parseInt(String.valueOf(m.get("YEAR4")));
           year5 += Integer.parseInt(String.valueOf(m.get("YEAR5")));
		}
        Row row3 = sheet.createRow(i);
		row3.createCell((short) 0).setCellValue("小计");
        row3.createCell((short) 1).setCellValue("");
        row3.createCell((short) 2).setCellValue(total);
        row3.createCell((short) 3).setCellValue(replace);
        row3.createCell((short) 4).setCellValue(revoke);
        row3.createCell((short) 5).setCellValue(test);
        row3.createCell((short) 6).setCellValue(frw);
        row3.createCell((short) 7).setCellValue(rw);
        row3.createCell((short) 8).setCellValue(change);
        row3.createCell((short) 9).setCellValue(year1);
        row3.createCell((short) 10).setCellValue(year2);
        row3.createCell((short) 11).setCellValue(year3);
        row3.createCell((short) 12).setCellValue(year4);
        row3.createCell((short) 13).setCellValue(year5);
        row3.createCell((short) 14).setCellValue("");
        cell = row3.createCell((short) 15);
		
        try{
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
        }catch(Exception e){
        	e.printStackTrace();
        }
	 log.debug("创建成功！");
	}
	
	
	
	/**
	 * 对账统计表
	 * @param id
	 * @param startTime
	 * @param endDate
	 * @param officeId
	 * @param model
	 * @param configSupplier
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showTableT")
	public String showTableT(Model model,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "vtnUpplierId", required = false) Long vtnUpplierId,
			@RequestParam(value = "vtnProduct", required = false) String vtnProduct,
			@RequestParam(value = "vtnapp", required = false) String vtnapp,
			ConfigSupplier configSupplier, HttpServletRequest request,
			HttpServletResponse response){
		List<ConfigSupplier> suppliers = configSupplierService.findAllSupplier();
		model.addAttribute("suppliers", suppliers);
		
		List<Map<String,Object>> list =settleSupplierCertDetailService.findSupplierFKJS(configSupplier, endTime, startTime);
		model.addAttribute("supplierlists",list);
		model.addAttribute("size", list.size());
		model.addAttribute("WorkDealInfoType", ProductType.productTypeMap);
		List<ConfigChargeSupplierDetail> configChargeSupplierDetails = configChargeSupplierDetailService.findAll();
		Map<String, Object> map = new HashMap<String, Object>();
		int amount = 0;
		double money = 0d;
		for(ConfigChargeSupplierDetail supplierDetail : configChargeSupplierDetails){
			map.put("A"+supplierDetail.getYear()+"_"+supplierDetail.getMoneyType(), supplierDetail.getMoney());
			money += supplierDetail.getMoney();
		}
		List<String> sList = settleSupplierCertDetailService.findOuByType(configSupplier, startTime, endTime);
		Map<String,Object> oumap = new HashMap<String, Object>();
		if(sList!=null){
			for(int i = 0;i<sList.size();i++){
				oumap.put("ou"+i, sList.get(i));
			}
		}
		model.addAttribute("startTime", startTime==null? DateUtils.formatDate(new Date(System.currentTimeMillis()- 30L*24*60*60*1000),"yyyy-MM-dd"): startTime);   //开始时间为空，则为当前时间前30天
		model.addAttribute("endTime", endTime ==null? DateUtils.formatDate(new Date()): endTime); //结束时间，默认当前时间
		model.addAttribute("oumap", oumap);
		model.addAttribute("money", map);
		model.addAttribute("amountTotal", amount);
		model.addAttribute("moneyTotal", money);
		if (startTime!=null&&endTime!=null) {
			model.addAttribute("dateT", startTime+"-"+endTime);
			model.addAttribute("supplier", configSupplier);
		}
		
		
		
		
		List<Map<String, Object>> vtnProducts =  settleVTNService.findProducts();
		model.addAttribute("vtnProducts",vtnProducts);
		List<Map<String, Object>> vtnApps =  settleVTNService.findApps();
		model.addAttribute("vtnApps",vtnApps);
		List<ConfigSupplier> configSuppliers = configSupplierService.findByCert();
		model.addAttribute("configSuppliers",configSuppliers);
		List<Map<String, Object>> settleVTNs = settleVTNService.findVTNStatistics(vtnUpplierId, startTime, endTime, vtnProduct, vtnapp);
		Double vtnTotalPrice = 0d;
		for (int i = 0; i < settleVTNs.size(); i++) {
			Long  suppliId = Long.parseLong(settleVTNs.get(i).get("CONFIG_SUPPLIER_ID").toString());
			settleVTNs.get(i).put("supplier", configSupplierService.get(suppliId).getSupplierName());
	       Double totalPrice = Double.parseDouble(settleVTNs.get(i).get("PRICETOTAL").toString());
	       Double totalCount = Double.parseDouble(settleVTNs.get(i).get("COUNTTOTAL").toString());
	       Double onePrice = totalPrice/totalCount;
	       settleVTNs.get(i).put("onePrice",onePrice);
	       vtnTotalPrice+=totalPrice;
		}
		model.addAttribute("vtnTotalPrice", vtnTotalPrice);
		model.addAttribute("settleVTNs",settleVTNs);
		model.addAttribute("vtnUpplierId", vtnUpplierId);
		model.addAttribute("vtnProduct", vtnProduct);
		model.addAttribute("vtnapp", vtnapp);
		
		return "modules/settle/supplierSettleShowTableT";
	}
	
	
	
	
	@RequestMapping("exportFKJS")
	public void exportFKJS(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "vtnUpplierId", required = false) Long vtnUpplierId,
			@RequestParam(value = "vtnProduct", required = false) String vtnProduct,
			@RequestParam(value = "vtnapp", required = false) String vtnapp,
			@RequestParam(value = "configSupplierId", required = false) Long configSupplierId,
			HttpServletRequest request,
			HttpServletResponse response) {
		try {

			ConfigSupplier supplier = configSupplierService.get(configSupplierId);
			List<Map<String,Object>> list =settleSupplierCertDetailService.findSupplierFKJS(supplier, endTime, startTime);
			List<ConfigChargeSupplierDetail> configChargeSupplierDetails = configChargeSupplierDetailService.findAll();
			Map<String, Object> map = new HashMap<String, Object>();
			int amount = 0;
			double money = 0d;
			for(ConfigChargeSupplierDetail supplierDetail : configChargeSupplierDetails){
				map.put("A"+supplierDetail.getYear()+"_"+supplierDetail.getMoneyType(), supplierDetail.getMoney());
				money += supplierDetail.getMoney();
			}
			List<String> sList = settleSupplierCertDetailService.findOuByType(supplier, startTime, endTime);
			Map<String,Object> oumap = new HashMap<String, Object>();
			if(sList!=null){
				for(int i = 0;i<sList.size();i++){
					oumap.put("ou"+i, sList.get(i));
				}
			}
			
			Workbook wb = new HSSFWorkbook();// 定义工作簿
			CellStyle style = wb.createCellStyle(); //样式对象
			Cell cell =null;
			Sheet sheet = wb.createSheet("付款结算清单");
			sheet.setDefaultColumnWidth(35);
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));//合并单元格
			Font  font  = wb.createFont(); 
			//第一行数据
			Row row0 = sheet.createRow(0);
			cell = row0.createCell(0);
			Font  font0  = wb.createFont(); 
			CellStyle style0 = wb.createCellStyle();
			font0.setFontHeightInPoints((short)18);
			style0.setFont(font0);
			cell.setCellStyle(style0);
			cell.setCellValue("付款结算清单");
			//第二行数据
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,5));
			Row row1 = sheet.createRow(1);
			cell = row1.createCell(0);
			font.setFontHeightInPoints((short)14);
			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("制表时间："+startTime+"—"+endTime);
			//第三行数据
			sheet.addMergedRegion(new CellRangeAddress(2,2,0,5));
			Row row2 = sheet.createRow(2);
			cell = row2.createCell(0);
//			font.setFontHeightInPoints((short)14);
//			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("请款单位："+123);
			//第四行数据
			Row row3 = sheet.createRow(3);
			cell = row3.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("采购日期");
			cell = row3.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("产品名称");
			cell = row3.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue("项目名称");
			cell = row3.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue("数量/张");
			cell = row3.createCell(4);
			cell.setCellStyle(style);
			cell.setCellValue("单价/元");
			cell = row3.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue("小计/元");
			
			//5-10行第一列合并
			sheet.addMergedRegion(new CellRangeAddress(4,12,0,0));
			Double totolXJ = 0d;
			Double totalNumXJ = 0d;
			
			//第五行数据
			Row row4 = sheet.createRow(4);
			cell = row4.createCell(0);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);//水平   
			cell.setCellStyle(style);
			cell.setCellValue(startTime+"-"+endTime);
			cell = row4.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("一年期机构/个人证书");
			
			cell = row4.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			cell = row4.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			
			cell = row4.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou0").toString());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("PRODUCT_TYPE").toString().equals("2")) {
					cell = row4.createCell(3);
					cell.setCellStyle(style);
					double price = 0d;
					if (list.get(i).get("YEAR1").toString()!=null&&!list.get(i).get("YEAR1").toString().equals("")) {
						price = Double.parseDouble(list.get(i).get("YEAR1").toString());
					}
					totalNumXJ+=price;
					cell.setCellValue(price);
					cell = row4.createCell(5);
					cell.setCellStyle(style);
					Double d = price*Double.parseDouble(map.get("A1_2").toString());
					totolXJ+=d;
					cell.setCellValue(d);
				}
			}
			cell = row4.createCell(4);
			cell.setCellStyle(style);
			if (map.size()>0) {
				cell.setCellValue(map.get("A1_2").toString()!=null?map.get("A1_2").toString():0+"");
			}else {
				cell.setCellValue(0);
			}
			
			
			//第六行数据
			Row row5 = sheet.createRow(5);
			cell = row5.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("一年期企业证书");
			cell = row5.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			cell = row5.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			
			cell = row5.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou1").toString());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("PRODUCT_TYPE").toString().equals("1")) {
					cell = row5.createCell(3);
					cell.setCellStyle(style);
					double price = 0d;
					if (list.get(i).get("YEAR1").toString()!=null&&!list.get(i).get("YEAR1").toString().equals("")) {
						price = Double.parseDouble(list.get(i).get("YEAR1").toString());
					}
					totalNumXJ+=price;
					cell.setCellValue(price);
					cell = row5.createCell(5);
					cell.setCellStyle(style);
					Double d = price*Double.parseDouble(map.get("A1_1").toString());
					totolXJ+=d;
					cell.setCellValue(d);
				}
			}
			cell = row5.createCell(4);
			cell.setCellStyle(style);
			if (map.size()>0) {
				cell.setCellValue(map.get("A1_1").toString()!=null?map.get("A1_1").toString():0+"");
			}else {
				cell.setCellValue(0);
			}
			
			
			//第七行数据
			Row row6 = sheet.createRow(6);
			cell = row6.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("二年期机构/个人证书");
			cell = row6.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			cell = row6.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			
			cell = row6.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou2").toString());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("PRODUCT_TYPE").toString().equals("2")) {
					cell = row6.createCell(3);
					cell.setCellStyle(style);
					double price = 0d;
					if (list.get(i).get("YEAR2").toString()!=null&&!list.get(i).get("YEAR2").toString().equals("")) {
						price = Double.parseDouble(list.get(i).get("YEAR2").toString());
					}
					totalNumXJ+=price;
					cell.setCellValue(price);
					cell = row6.createCell(5);
					cell.setCellStyle(style);
					Double d = price*Double.parseDouble(map.get("A2_2").toString());
					totolXJ+=d;
					cell.setCellValue(d);
				}
			}
			cell = row6.createCell(4);
			cell.setCellStyle(style);
			if (map.size()>0) {
				cell.setCellValue(map.get("A2_2").toString()!=null?map.get("A2_2").toString():0+"");
			}else {
				cell.setCellValue(0);
			}
			
			
			
			//第八行数据
			Row row7 = sheet.createRow(7);
			cell = row7.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("二年期企业证书");
			cell = row7.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			cell = row7.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			
			cell = row7.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou3").toString());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("PRODUCT_TYPE").toString().equals("1")) {
					cell = row7.createCell(3);
					cell.setCellStyle(style);
					double price = 0d;
					if (list.get(i).get("YEAR2").toString()!=null&&!list.get(i).get("YEAR2").toString().equals("")) {
						price = Double.parseDouble(list.get(i).get("YEAR2").toString());
					}
					totalNumXJ+=price;
					cell.setCellValue(price);
					cell = row7.createCell(5);
					cell.setCellStyle(style);
					Double d = price*Double.parseDouble(map.get("A2_1").toString());
					totolXJ+=d;
					cell.setCellValue(d);
				}
			}
			cell = row7.createCell(4);
			cell.setCellStyle(style);
			if (map.size()>0) {
				cell.setCellValue(map.get("A2_1").toString()!=null?map.get("A2_1").toString():0+"");
			}else {
				cell.setCellValue(0);
			}
			
			
			//第九行数据
			Row row8 = sheet.createRow(8);
			cell = row8.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("四年期机构/个人证书");
			cell = row8.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			cell = row8.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			
			
			cell = row8.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou4").toString());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("PRODUCT_TYPE").toString().equals("2")) {
					cell = row8.createCell(3);
					cell.setCellStyle(style);
					double price = 0d;
					if (list.get(i).get("YEAR4").toString()!=null&&!list.get(i).get("YEAR4").toString().equals("")) {
						price = Double.parseDouble(list.get(i).get("YEAR4").toString());
					}
					totalNumXJ+=price;
					cell.setCellValue(price);
					cell = row8.createCell(5);
					cell.setCellStyle(style);
					Double d = price*Double.parseDouble(map.get("A4_2").toString());
					totolXJ+=d;
					cell.setCellValue(d);
				}
			}
			cell = row8.createCell(4);
			cell.setCellStyle(style);
			if (map.size()>0) {
				cell.setCellValue(map.get("A4_2").toString()!=null?map.get("A4_2").toString():0+"");
			}else {
				cell.setCellValue(0);
			}
			
			
			
			
			//第十行数据
			Row row9 = sheet.createRow(9);
			cell = row9.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("四年期企业证书");
			cell = row9.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			cell = row9.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			
			cell = row9.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou5").toString());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("PRODUCT_TYPE").toString().equals("1")) {
					cell = row9.createCell(3);
					cell.setCellStyle(style);
					double price = 0d;
					if (list.get(i).get("YEAR4").toString()!=null&&!list.get(i).get("YEAR4").toString().equals("")) {
						price = Double.parseDouble(list.get(i).get("YEAR4").toString());
					}
					totalNumXJ+=price;
					cell.setCellValue(price);
					cell = row9.createCell(5);
					cell.setCellStyle(style);
					Double d = price*Double.parseDouble(map.get("A4_1").toString());
					totolXJ+=d;
					cell.setCellValue(d);
				}
			}
			cell = row9.createCell(4);
			cell.setCellStyle(style);
			if (map.size()>0) {
				cell.setCellValue(map.get("A4_1").toString()!=null?map.get("A4_1").toString():0+"");
			}else {
				cell.setCellValue(0);
			}
			
			
			//第十一行数据
			Row row10 = sheet.createRow(10);
			cell = row10.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("五年期机构/个人证书");
			cell = row10.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			cell = row10.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			
			cell = row10.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou6").toString());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("PRODUCT_TYPE").toString().equals("2")) {
					cell = row10.createCell(3);
					cell.setCellStyle(style);
					double price = 0d;
					if (list.get(i).get("YEAR5").toString()!=null&&!list.get(i).get("YEAR5").toString().equals("")) {
						price = Double.parseDouble(list.get(i).get("YEAR5").toString());
					}
					totalNumXJ+=price;
					cell.setCellValue(price);
					cell = row10.createCell(5);
					cell.setCellStyle(style);
					Double d = price*Double.parseDouble(map.get("A5_2").toString());
					totolXJ+=d;
					cell.setCellValue(d);
				}
			}
			cell = row10.createCell(4);
			cell.setCellStyle(style);
			if (map.size()>0) {
				cell.setCellValue(map.get("A5_2").toString()!=null?map.get("A5_2").toString():0+"");
			}else {
				cell.setCellValue(0);
			}
			
			
			
			//第十二行数据
			Row row11 = sheet.createRow(11);
			cell = row11.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("五年期企业证书");
			cell = row11.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			cell = row11.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(0);
			
			cell = row11.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou7").toString());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).get("PRODUCT_TYPE").toString().equals("1")) {
					cell = row11.createCell(3);
					cell.setCellStyle(style);
					double price = 0d;
					if (list.get(i).get("YEAR5").toString()!=null&&!list.get(i).get("YEAR5").toString().equals("")) {
						price = Double.parseDouble(list.get(i).get("YEAR5").toString());
					}
					totalNumXJ+=price;
					cell.setCellValue(price);
					cell = row11.createCell(5);
					cell.setCellStyle(style);
					Double d = price*Double.parseDouble(map.get("A5_1").toString());
					totolXJ+=d;
					cell.setCellValue(d);
				}
			}
			cell = row11.createCell(4);
			cell.setCellStyle(style);
			if (map.size()>0) {
				cell.setCellValue(map.get("A5_1").toString()!=null?map.get("A5_1").toString():0+"");
			}else {
				cell.setCellValue(0);
			}
			
			
			
			//第十三行数据
			Row row12 = sheet.createRow(12);
			cell = row12.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("因KEY非人为原因补签证书");
			cell = row12.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(oumap.get("ou8").toString());
			double num = 0d;
			for (int i = 0; i < list.size(); i++) {
				num += Double.parseDouble(list.get(i).get("FRW_BB_AMOUNT").toString());
			}
			totalNumXJ+=num;
			cell = row12.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(num);
			
			
			//第十四行数据
			Row row13 = sheet.createRow(13);
			cell = row13.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("小计");
			cell = row13.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(totalNumXJ);
			
			cell = row13.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(totolXJ);
			
			
			
			
			
			//第十六行数据
			sheet.addMergedRegion(new CellRangeAddress(15,15,0,4));
			sheet.addMergedRegion(new CellRangeAddress(16,16,0,4));
			sheet.addMergedRegion(new CellRangeAddress(17,17,0,4));
			Row row15 = sheet.createRow(15);
			Font  font1  = wb.createFont(); 
			CellStyle style1 = wb.createCellStyle();
			font1.setFontHeightInPoints((short)18);
			style1.setFont(font1);
			cell = row15.createCell(0);
			cell.setCellStyle(style1);
			cell.setCellValue("VTN结算清单");
			//第十七行数据
			Row row16 = sheet.createRow(16);
			cell = row16.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("制表时间："+startTime+"—"+endTime);
			//第十八行数据
			Row row17 = sheet.createRow(17);
			cell = row17.createCell(0);
			cell.setCellStyle(style);
			if (vtnUpplierId!=null) {
				ConfigSupplier supplierVTN = configSupplierService.get(vtnUpplierId);
				cell.setCellValue("VTN供应商： "+supplierVTN.getSupplierName());
			}
			//第十九行数据
			Row row18 = sheet.createRow(18);
			cell = row18.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("产品名称");
			cell = row18.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("应用名称");
			cell = row18.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue("数量/张");
			cell = row18.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue("单价/元");
			cell = row18.createCell(4);
			cell.setCellStyle(style);
			cell.setCellValue("小计/元");
			

			List<Map<String, Object>> settleVTNs = settleVTNService.findVTNStatistics(vtnUpplierId, startTime, endTime, vtnProduct, vtnapp);
			Double vtnTotalPrice = 0d;
			for (int i = 0; i < settleVTNs.size(); i++) {
				Long  suppliId = Long.parseLong(settleVTNs.get(i).get("CONFIG_SUPPLIER_ID").toString());
				settleVTNs.get(i).put("supplier", configSupplierService.get(suppliId).getSupplierName());
		       Double totalPrice = Double.parseDouble(settleVTNs.get(i).get("PRICETOTAL").toString());
		       Double totalCount = Double.parseDouble(settleVTNs.get(i).get("COUNTTOTAL").toString());
		       Double onePrice = totalPrice/totalCount;
		       settleVTNs.get(i).put("onePrice",onePrice);
		       vtnTotalPrice+=totalPrice;
			}
			
			int vtnLength = 19;
			for (int i = 0; i < settleVTNs.size(); i++) {
				//第VTNN行数据
				Row rowN = sheet.createRow(vtnLength);
				cell = rowN.createCell(0);
				cell.setCellStyle(style);
				cell.setCellValue(settleVTNs.get(i).get("PRODUCT_NAME").toString());
				cell = rowN.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue(settleVTNs.get(i).get("APP_NAME").toString());
				cell = rowN.createCell(2);
				cell.setCellStyle(style);
				cell.setCellValue(settleVTNs.get(i).get("COUNTTOTAL").toString());
				cell = rowN.createCell(3);
				cell.setCellStyle(style);
				cell.setCellValue(settleVTNs.get(i).get("onePrice").toString());
				cell = rowN.createCell(4);
				cell.setCellStyle(style);
				cell.setCellValue(settleVTNs.get(i).get("PRICETOTAL").toString());
				vtnLength+=1;
			}
		
			//第十七行数据
			Row rowLast = sheet.createRow(vtnLength);
			cell = rowLast.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("合计");
			cell = rowLast.createCell(4);
			cell.setCellStyle(style);
			cell.setCellValue(vtnTotalPrice);
			
			
			
			
			
			
			

			
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
	
	
	
	
	
	
	
	
}
