/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.Region;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.bean.StaticMonth;
import com.itrus.ca.modules.bean.StaticProductMonth;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.statistic.entity.StatisticCertDataProduct;
import com.itrus.ca.modules.statistic.service.StatisticCertDataProductService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 证书发放统计Controller
 * @author ZhangJingtao
 * @version 2014-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/statistic/statisticCertDataProduct")
public class StatisticCertDataProductController extends BaseController {

	@Autowired
	private StatisticCertDataProductService statisticCertDataProductService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public StatisticCertDataProduct get(@RequestParam(required=false) Long id) {
		if (id != null){
			return statisticCertDataProductService.get(id);
		}else{
			return new StatisticCertDataProduct();
		}
	}
	
	@RequiresPermissions("statistic:statisticCertDataProduct:view")
	@RequestMapping(value = {"list", ""})
	public String list(StatisticCertDataProduct statisticCertDataProduct,Long areaId,
			Long officeId, String startDate, String endDate,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Office> offsList =  officeService.getOfficeByType(UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			}else {
				i++;
			}
		}
		if (areaId != null) {
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
		}
		model.addAttribute("offsList", offsList);
		model.addAttribute("areaId", areaId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("officeId", officeId);
		
		if (startDate == null&& endDate == null) {
			return "modules/statistic/statisticCertDataProductListF";
		}
		Office office = null;
		if (officeId!=null) {
			office = officeService.get(officeId);
		}
		
		if (startDate==null||startDate.equals("")) {
			startDate = "2013-01";
		}
		if (endDate==null||endDate.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			endDate = df.format(new Date());
		}
		List<String> monthList = getMonthList(startDate+"-01", endDate+"-01");
		List<StaticProductMonth> sumList = new ArrayList<StaticProductMonth>();
		//for (int i = 1; i < ProductType.productTypeMap.size()+1; i++) {
		for (Entry<Integer, String> entry : ProductType.productTypeMap.entrySet()) {     	
			StaticProductMonth month = new StaticProductMonth();
			month.setProductType(entry.getKey());
			List<StaticMonth> smList = new ArrayList<StaticMonth>();
			for (String s : monthList) {
				StaticMonth sm = new StaticMonth();
				try {
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					Date start=sdf.parse(s);
					Calendar rightNow = Calendar.getInstance();
					rightNow.setTime(start);
					rightNow.add(Calendar.MONTH, 1);
					Date end = rightNow.getTime();
					List<StatisticCertDataProduct> list = statisticCertDataProductService.getSum(entry.getKey(), office, start, end);
					sm.setMonth(start);
					Integer oneSum = 0;
					Integer twoSum = 0;
					
				Integer threeSum = 0;
					
					Integer fourSum = 0;
					Integer fiveSum = 0;
					
					for (StatisticCertDataProduct statisticCertDataProduct2 : list) {
						oneSum += statisticCertDataProduct2.getYear1()==null?0:statisticCertDataProduct2.getYear1();
						twoSum += statisticCertDataProduct2.getYear2()==null?0:statisticCertDataProduct2.getYear2();
						
						threeSum += statisticCertDataProduct2.getYear3()==null?0:statisticCertDataProduct2.getYear3();
						
						fourSum += statisticCertDataProduct2.getYear4()==null?0:statisticCertDataProduct2.getYear4();
						fiveSum += statisticCertDataProduct2.getYear5()==null?0:statisticCertDataProduct2.getYear5();
					}
					sm.setCount1(oneSum);
					sm.setCount2(twoSum);
					
					sm.setCount3(threeSum);
					
					sm.setCount4(fourSum);
					sm.setCount5(fiveSum);
					if (entry.getKey()==1) {
						sm.setPro1Sum(oneSum+twoSum+ threeSum +fourSum+fiveSum);
					}
					if (entry.getKey()==2) {
						sm.setPro2Sum(oneSum+twoSum+ threeSum + fourSum+fiveSum);
					}
					if (entry.getKey()==3) {
						sm.setPro3Sum(oneSum+twoSum+ threeSum + fourSum+fiveSum);
					}
					if (entry.getKey()==4) {
						sm.setPro4Sum(oneSum+twoSum+ threeSum + fourSum+fiveSum);
					}
//					if (i==5) {
//						sm.setPro5Sum(oneSum+twoSum+fourSum+fiveSum);
//					}
					if (entry.getKey()==6) {
						sm.setPro6Sum(oneSum+twoSum+ threeSum+ fourSum+fiveSum);
					}
					smList.add(sm);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			month.setMonths(smList);
			sumList.add(month);
		}
		
		model.addAttribute("sumList", sumList);
		model.addAttribute("pro", ProductType.productTypeMap);
		model.addAttribute("monthList", getMoList(startDate+"-01", endDate+"-01"));
		
		return "modules/statistic/statisticCertDataProductListF";
	}

	@RequiresPermissions("statistic:statisticCertDataProduct:view")
	@RequestMapping(value = "form")
	public String form(StatisticCertDataProduct statisticCertDataProduct, Model model) {
		model.addAttribute("statisticCertDataProduct", statisticCertDataProduct);
		return "modules/statistic/statisticCertDataProductForm";
	}

	@RequiresPermissions("statistic:statisticCertDataProduct:edit")
	@RequestMapping(value = "save")
	public String save(StatisticCertDataProduct statisticCertDataProduct, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, statisticCertDataProduct)){
			return form(statisticCertDataProduct, model);
		}
		statisticCertDataProductService.save(statisticCertDataProduct);
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticCertDataProduct/?repage";
	}
	
	@RequiresPermissions("statistic:statisticCertDataProduct:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		statisticCertDataProductService.delete(id);
		addMessage(redirectAttributes, "删除证书发放统计成功");
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticCertDataProduct/?repage";
	}
	
	@RequestMapping(value = "exportZS")
	public void exportZS(Long officeId, String startDate, String endDate,
			HttpServletRequest request, HttpServletResponse response)
	{

		Office office = null;
		if (officeId!=null) {
			office = officeService.get(officeId);
		}
		
		if (startDate==null||startDate.equals("")) {
			startDate = "2013-01";
		}
		if (endDate==null||endDate.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			endDate = df.format(new Date());
		}
		List<String> monthList = getMonthList(startDate+"-01", endDate+"-01");
		List<String> monthList1 = getMoList(startDate, endDate);
		List<StaticProductMonth> sumList = new ArrayList<StaticProductMonth>();
		//for (int i = 1; i < ProductType.productTypeMap.size()+1; i++) {
		for (Entry<Integer, String> entry : ProductType.productTypeMap.entrySet()) {     	
			StaticProductMonth month = new StaticProductMonth();
			month.setProductType(entry.getKey());
			List<StaticMonth> smList = new ArrayList<StaticMonth>();
			for (String s : monthList) {
				StaticMonth sm = new StaticMonth();
				try {
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					Date start=sdf.parse(s);
					Calendar rightNow = Calendar.getInstance();
					rightNow.setTime(start);
					rightNow.add(Calendar.MONTH, 1);
					Date end = rightNow.getTime();
					List<StatisticCertDataProduct> list = statisticCertDataProductService.getSum(entry.getKey(), office, start, end);
					sm.setMonth(start);
					Integer oneSum = 0;
					Integer twoSum = 0;
					
					Integer threeSum = 0;
					
					Integer fourSum = 0;
					Integer fiveSum = 0;
					
					for (StatisticCertDataProduct statisticCertDataProduct2 : list) {
						oneSum += statisticCertDataProduct2.getYear1();
						twoSum += statisticCertDataProduct2.getYear2();
						threeSum += statisticCertDataProduct2.getYear3();
						fourSum += statisticCertDataProduct2.getYear4();
						fiveSum += statisticCertDataProduct2.getYear5();
					}
					sm.setCount1(oneSum);
					sm.setCount2(twoSum);
					sm.setCount3(threeSum);
					sm.setCount4(fourSum);
					sm.setCount5(fiveSum);
					if (entry.getKey()==1) {
						sm.setPro1Sum(oneSum+twoSum+threeSum+fourSum+fiveSum);
					}
					if (entry.getKey()==2) {
						sm.setPro2Sum(oneSum+twoSum+threeSum+fourSum+fiveSum);
					}
					if (entry.getKey()==3) {
						sm.setPro3Sum(oneSum+twoSum+threeSum+fourSum+fiveSum);
					}
					if (entry.getKey()==4) {
						sm.setPro4Sum(oneSum+twoSum+threeSum+fourSum+fiveSum);
					}
//					if (i==5) {
//						sm.setPro5Sum(oneSum+twoSum+fourSum+fiveSum);
//					}
					if (entry.getKey()==6) {
						sm.setPro6Sum(oneSum+twoSum+threeSum+fourSum+fiveSum);
					}
					smList.add(sm);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			month.setMonths(smList);
			sumList.add(month);
		}
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("证书发放统计");
		//首行样式
		HSSFCellStyle style=wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Font font=wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short)20);
		font.setFontName("宋体");
		style.setFont(font);
		
		
		HSSFRow  row0=sheet.createRow(0);
		row0.setHeight((short)(5*100));
		
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)(monthList1.size()+2)));
		
		HSSFCell cell0=row0.createCell(0);
		cell0.setCellValue("证书发放统计");
		cell0.setCellStyle(style);
		//第2行
		HSSFRow row=sheet.createRow(1);
		HSSFCell cell=row.createCell(0);
		cell.setCellValue("产品名称");
		row.createCell(1).setCellValue("年限");
		HSSFRow rownz=sheet.createRow(5*sumList.size()+2);
		rownz.createCell(0).setCellValue("总计");
		for(int i=0;i<monthList1.size();i++)
		{
			row.createCell(i+2).setCellValue(monthList1.get(i));
			sheet.setColumnWidth(i, 20 * 200); 
		}
		row.createCell(monthList1.size()+2).setCellValue("小计");
		HSSFRow rown=null;
		HSSFRow rown1=null;
		HSSFRow rown2=null;
		HSSFRow rown3=null;
		HSSFRow rown4=null;
		HSSFRow rown5=null;
		int sum=0;
		for(int i=0;i<sumList.size();i++)
		{
			int sum1=0;
			int sum2=0;
			int sum3=0;
			int sum4=0;
			int sum5=0;
					sheet.addMergedRegion(new Region(i*5+2, (short)0, i*5+2+4, (short)0));
					rown=sheet.createRow(i*5+2);
					rown1= sheet.createRow(i*5+2);
					rown1.createCell(1).setCellValue("一年期限");
					rown2=sheet.createRow(i*5+2+1);
					rown2.createCell(1).setCellValue("二年期限");
					rown3=sheet.createRow(i*5+2+2);
					rown3.createCell(1).setCellValue("三年期限");
					rown4=sheet.createRow(i*5+2+3);
					rown4.createCell(1).setCellValue("四年期限");
					rown5=sheet.createRow(i*5+2+4);
					rown5.createCell(1).setCellValue("五年期限");
			for(int j=0;j<monthList1.size();j++)
			{
				sum1+=sumList.get(i).getMonths().get(j).getCount1();
				sum2+=sumList.get(i).getMonths().get(j).getCount2();
				sum3+=sumList.get(i).getMonths().get(j).getCount3();
				sum4+=sumList.get(i).getMonths().get(j).getCount4();
				sum5+=sumList.get(i).getMonths().get(j).getCount5();
				
				
			}
			sum+=(sum1+sum2+sum3+sum4+sum5);
			rown.createCell(0).setCellValue(ProductType.getProductTypeName(sumList.get(i).getProductType()));
			
			for(int j=0;j<monthList1.size();j++)
			{
				rown1.createCell(j+2).setCellValue(sumList.get(i).getMonths().get(j).getCount1());
				rown2.createCell(j+2).setCellValue(sumList.get(i).getMonths().get(j).getCount2());
				rown3.createCell(j+2).setCellValue(sumList.get(i).getMonths().get(j).getCount3());
				rown4.createCell(j+2).setCellValue(sumList.get(i).getMonths().get(j).getCount4());
				rown5.createCell(j+2).setCellValue(sumList.get(i).getMonths().get(j).getCount5());
				rown1.createCell(monthList1.size()+2).setCellValue(sum1);
				rown2.createCell(monthList1.size()+2).setCellValue(sum2);
				rown3.createCell(monthList1.size()+2).setCellValue(sum3);
				rown4.createCell(monthList1.size()+2).setCellValue(sum4);
				rown5.createCell(monthList1.size()+2).setCellValue(sum5);
				rownz.createCell(monthList1.size()+2).setCellValue(sum);
			}
			
		}
		for(int j=0;j<monthList1.size();j++)
		{
			int z=0;
			for(int i=0;i<sumList.size();i++)
			{
				z+=sumList.get(i).getMonths().get(j).getCount1()+sumList.get(i).getMonths().get(j).getCount2()+sumList.get(i).getMonths().get(j).getCount3()+sumList.get(i).getMonths().get(j).getCount4()+sumList.get(i).getMonths().get(j).getCount5();
			}
				rownz.createCell(j+2).setCellValue(z);
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=credentialGrantCount.xls");
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

	
	
	public List<String> getMonthList(String beginTime, String endTime) {  
        List<String> monthList = new ArrayList<String>();  
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd");  
        try {  
        	Date begin = monthFormat.parse(beginTime);
        	Date end = monthFormat.parse(endTime);
            int months = (end.getYear() - begin.getYear()) * 12  
                    + (end.getMonth() - begin.getMonth());  
              
            for (int i = 0; i <= months; i++) {  
                Calendar calendar = Calendar.getInstance();    
                calendar.setTime(begin);    
                calendar.add(Calendar.MONTH, i);  
                monthList.add(monthFormat.format(calendar.getTime()));  
            }  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return monthList;  
    }
	
	public List<String> getMoList(String beginTime, String endTime) {  
        List<String> monthList = new ArrayList<String>();  
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");  
        try {  
        	Date begin = monthFormat.parse(beginTime);
        	Date end = monthFormat.parse(endTime);
            int months = (end.getYear() - begin.getYear()) * 12  
                    + (end.getMonth() - begin.getMonth());  
              
            for (int i = 0; i <= months; i++) {  
                Calendar calendar = Calendar.getInstance();    
                calendar.setTime(begin);    
                calendar.add(Calendar.MONTH, i);  
                monthList.add(monthFormat.format(calendar.getTime()));  
            }  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return monthList;  
    }
	
}
