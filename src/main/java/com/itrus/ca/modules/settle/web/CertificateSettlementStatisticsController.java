/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.settle.entity.CertificateSettlementStatistics;
import com.itrus.ca.modules.settle.service.CertificateSettlementStatisticsService;
import com.itrus.ca.modules.settle.vo.CertificateF;
import com.itrus.ca.modules.settle.vo.CertificateSettlementStatisticsVO;
import com.itrus.ca.modules.settle.vo.DealInfoType_Year;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 证书结算统计表Controller
 * 
 * @author qt
 * @version 2015-11-22
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/certificateSettlementStatistics")
public class CertificateSettlementStatisticsController extends BaseController {

	@Autowired
	private CertificateSettlementStatisticsService certificateSettlementStatisticsService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private ConfigProductService configProductService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@ModelAttribute
	public CertificateSettlementStatistics get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return certificateSettlementStatisticsService.get(id);
		} else {
			return new CertificateSettlementStatistics();
		}
	}
	
	@RequiresPermissions("settle:certificateSettlementStatistics:view")
	@RequestMapping(value = { "list", "" })
	public String list(Long areaId, Long officeId, Date startDate, Date endDate, String tempStyle,String agentId,
			boolean multiType,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "applyId", required = false) Long applyId,
			@RequestParam(value = "proList", required = false) List<String> productType,
			@RequestParam(value = "workTypes", required = false) List<String> workType, Model model) {

		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		//List<Long> officeids = Lists.newArrayList();
		List<String> agentids = Lists.newArrayList();
		/*User user = UserUtils.getUser();
		Map<String, Set<String>> dealInfoType_productType = new HashMap<>();
		Map<String, List<String>> productType_year = new HashMap<>();
		Map<String, List<Double>> productType_number = new HashMap<>();*/
		List<Office> offsList = officeService.getOfficeByType(UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			} else {
				i++;
			}
		}
		
		if (areaId != null) {
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
		}
		if(tempStyle !=null){
			List<ConfigChargeAgent> agentList=configChargeAgentService.findByStyle(tempStyle.toString());
			model.addAttribute("agentList", agentList);
			model.addAttribute("tempStyle", tempStyle);
			model.addAttribute("agentId", agentId);
		}
		/* 应用查询列表显示 */
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("proList", ProductType.getProductTypeAutoTask());
		
		
		String product = "";
		if (productType != null && productType.size() > 0) {
			for (int i = 0; i < productType.size(); i++) {
				product += ProductType.getProductTypeName(Integer.parseInt(productType.get(i))) + ",";
			}
			StringUtils.removeEnd(product, ",");
		}

		// 如果应用不为空，产品也不为空，则显示XX应用XX产品
		if (applyId != null && (productType != null && productType.size() > 0)) {
			ConfigApp appName = configAppService.get(applyId);// 获取应用名称
			model.addAttribute("applyId", appName.getId());
			String yingyong = appName.getAppName() + "应用[" + product + "]产品";
			model.addAttribute("yingyong", yingyong);
		}
		// 如果应用不为空，产品为空 则显示 XX应用全部产品
		if (applyId != null && (productType == null || productType.size() == 0)) {
			ConfigApp appName = configAppService.get(applyId);// 获取应用名称
			model.addAttribute("applyId", appName.getId());
			String yingyong = appName.getAppName() + "全部产品";
			model.addAttribute("yingyong", yingyong);
		}
		// // 如果应用为空，产品不为空，则显示全部应用XX产品
		// if (applyId == null && (productType != null && productType.length >
		// 0)) {
		// String yingyong = "全部应用" + product + "产品";
		// model.addAttribute("yingyong", yingyong);
		// }
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("workTypes", workDealInfoType.getProductTypeListLess());
		// model.addAttribute("workType", workDealInfo.getDealInfoStatus());
		model.addAttribute("productId", productType);
		model.addAttribute("multiType",multiType);
		model.addAttribute("workType", workType);
		model.addAttribute("offsList", offsList);
		model.addAttribute("areaId", areaId);
		

		model.addAttribute("officeId", officeId);
		// 开始日期，结束日期，应用必须选择
		if (startDate == null || endDate == null || applyId == null) {
			model.addAttribute("startDate", DateUtils.firstDayOfMonth(new Date()));
			model.addAttribute("endDate", new Date());
 			return "modules/settle/certificateSettlementStatisticsList";
		} else {
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
		}
		List<Long> officeIdList = new ArrayList<Long>();

		if (officeId != null) {
			officeIdList.add(officeId);
		} else if (areaId != null) {
			officeIdList = officeService.findOfficeIdsByParentId(areaId);
		}
		List<CertificateSettlementStatisticsVO> findWorkList1 = certificateSettlementStatisticsService.findMulitWorkList1(
				applyId, org.springframework.util.StringUtils.collectionToCommaDelimitedString(productType),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(workType),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(officeIdList), agentId, startDate,
				endDate);

		HashMap<String, CertificateF> monthMap1 = certificateSettlementStatisticsService.getStaticMap1(findWorkList1);
		
		Map<Integer,Set<String>> deal_pro =new HashMap<Integer,Set<String>>();
		Map<String, Set<Integer>> productType_year=new HashMap<String,Set<Integer>>();
		List<DealInfoType_Year> dealInfoType_Year=Lists.newArrayList();
		if(findWorkList1!=null){
			Set<Integer> dealInfoTypes = new LinkedHashSet<Integer>();
			for(int i=0;i<findWorkList1.size();i++){
				dealInfoTypes.add(findWorkList1.get(i).getDealInfoType());
			}
			Object deals[] = dealInfoTypes.toArray();
			
			for(int j=0;j<deals.length;j++){
				Set<String> productTypes =new LinkedHashSet<String>();
				for(int CSVO=0;CSVO<findWorkList1.size();CSVO++)
				{
					if(deals[j]==findWorkList1.get(CSVO).getDealInfoType())
					{
						productTypes.add(findWorkList1.get(CSVO).getProductName());
					}
					
				}
				
				
				deal_pro.put(Integer.parseInt(String.valueOf(deals[j])), productTypes);
				Iterator<Map.Entry<Integer, Set<String>>> it=deal_pro.entrySet().iterator();
				while (it.hasNext()) {
					DealInfoType_Year d_y=new DealInfoType_Year();
					Entry<Integer, Set<String>> productT = it.next();
					Object productTypeas[]=productT.getValue().toArray();
					for(int i=0;i<productTypeas.length;i++)
					{
						Set<Integer> years=new LinkedHashSet<Integer>();
						for(int CSVO=0;CSVO<findWorkList1.size();CSVO++)
						{
							if(productTypeas[i]==findWorkList1.get(CSVO).getProductName())
							{
								years.add(findWorkList1.get(CSVO).getYear());
							}
							
						}
						d_y.setDeal(productT.getKey());
						d_y.setProducType((String)productTypeas[i]);
						d_y.setYear(years);
						dealInfoType_Year.add(d_y);
					}
					System.out.println(productT.getKey());
				}
			}
			
		
		}
		
	/*	Set entries = monthMap1.entrySet( );

		if(entries != null) {

		Iterator iterator = entries.iterator( );

		while(iterator.hasNext( )) {

		Map.Entry entry =(Entry) iterator.next( );

		Object key = entry.getKey( );

		Object value = entry.getValue();
		System.out.println(key);
		System.out.println(value);
			
		}
		}*/
		model.addAttribute("deal_pro", deal_pro);
		model.addAttribute("dealInfoType_Year", dealInfoType_Year);
		model.addAttribute("monthList", new ArrayList<String>(monthMap1.keySet()));
		model.addAttribute("sumList", monthMap1);	
		//model.addAttribute("certificate", certificate);
		return "modules/settle/certificateSettlementStatisticsList";
	}
	
	public List<String> getMonthList(Date begin, Date end) {
		List<String> monthList = new ArrayList<String>();
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// Date begin = monthFormat.parse(beginTime);
			// Date end = monthFormat.parse(endTime);
			int months = (end.getYear() - begin.getYear()) * 12 + (end.getMonth() - begin.getMonth());
			Calendar calen = Calendar.getInstance();
			calen.setTime(end);
			calen.add(Calendar.DATE, 1);
			for (int i = 0; i <= months; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(begin);
				calendar.add(Calendar.MONTH, i);
				if (i == 0) {
					monthList.add(monthFormat.format(begin));
				} else {
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					monthList.add(monthFormat.format(calendar.getTime()));
				}

			}
			monthList.add(monthFormat.format(calen.getTime()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return monthList;
	}

	public List<String> getMoList(Date begin, Date end) {
		List<String> monthList = new ArrayList<String>();
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
		try {
			// Date begin = monthFormat.parse(beginTime);
			// Date end = monthFormat.parse(endTime);
			int months = (end.getYear() - begin.getYear()) * 12 + (end.getMonth() - begin.getMonth());
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
	

	@RequiresPermissions("settle:certificateSettlementStatistics:edit")
	@RequestMapping(value = "save")
	public String save(CertificateSettlementStatistics certificateSettlementStatistics, Model model,
			RedirectAttributes redirectAttributes) {
		/*
		 * if (!beanValidator(model, certificateSettlementStatistics)){ return
		 * form(certificateSettlementStatistics, model); }
		 */
		certificateSettlementStatisticsService.save(certificateSettlementStatistics);
		// addMessage(redirectAttributes, "保存证书结算统计表'" +
		// certificateSettlementStatistics.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/modules/settle/certificateSettlementStatistics/?repage";
	}

	/*@RequiresPermissions("settle:certificateSettlementStatistics:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		certificateSettlementStatisticsService.delete(id);
		addMessage(redirectAttributes, "删除证书结算统计表成功");
		return "redirect:" + Global.getAdminPath() + "/modules/settle/certificateSettlementStatistics/?repage";
	}

	@RequestMapping(value = "export")
	public void export(Long areaId, Long officeId, Date startDate, Date endDate, String agentId,
			boolean multiType,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "applyId", required = false) Long applyId,
			@RequestParam(value = "proList", required = false) List<String> productType,
			@RequestParam(value = "workTypes", required = false) List<String> workType,Model model)
	{
		try {
			String product = "";
			if (productType != null && productType.size() > 0) {
				for (int i = 0; i < productType.size(); i++) {
					product += ProductType.getProductTypeName(Integer.parseInt(productType.get(i))) + ",";
				}
				StringUtils.removeEnd(product, ",");
			}
			String yingyong="";
			// 如果应用不为空，产品也不为空，则显示XX应用XX产品
			if (applyId != null && (productType != null && productType.size() > 0)) {
				ConfigApp appName = configAppService.get(applyId);// 获取应用名称
				model.addAttribute("applyId", appName.getId());
				 yingyong = appName.getAppName() + "应用[" + product + "]产品";
				
			}
			// 如果应用不为空，产品为空 则显示 XX应用全部产品
			if (applyId != null && (productType == null || productType.size() == 0)) {
				ConfigApp appName = configAppService.get(applyId);// 获取应用名称
				model.addAttribute("applyId", appName.getId());
				 yingyong = appName.getAppName() + "全部产品";
				
			}
			
			HSSFWorkbook wb=new HSSFWorkbook();
			HSSFSheet sheet=wb.createSheet("证书结算统计表");
			HSSFCellStyle style=wb.createCellStyle();
			HSSFFont font=wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short)14);
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			sheet.addMergedRegion(new Region(0, (short)0, 0, (short)19));
			sheet.addMergedRegion(new Region(1, (short) 0, 2, (short) 0));
			
			
			HSSFRow row0 = sheet.createRow(0);
			HSSFCell cell0 = row0.createCell(0);
			cell0.setCellValue("项目："+yingyong);
			cell0.setCellStyle(style);
			HSSFRow row1 = sheet.createRow(1);
			HSSFRow row2 = sheet.createRow(2);
			row1.createCell(0).setCellValue("月份");
			row1.createCell(1).setCellValue("新增（企业专用）");
			row1.createCell(5).setCellValue("新增（个人专用）");
			row1.createCell(9).setCellValue("更新（企业专用）");
			row1.createCell(13).setCellValue("更新（个人专用）");
			row2.createCell(1).setCellValue("一年");
			row2.createCell(2).setCellValue("二年");
			row2.createCell(3).setCellValue("四年");
			row2.createCell(4).setCellValue("五年");
			row2.createCell(5).setCellValue("一年");
			row2.createCell(6).setCellValue("二年");
			row2.createCell(7).setCellValue("四年");
			row2.createCell(8).setCellValue("五年");
			row2.createCell(9).setCellValue("一年");
			row2.createCell(10).setCellValue("二年");
			row2.createCell(11).setCellValue("四年");
			row2.createCell(12).setCellValue("五年");
			row2.createCell(13).setCellValue("一年");
			row2.createCell(14).setCellValue("二年");
			row2.createCell(15).setCellValue("四年");
			row2.createCell(16).setCellValue("五年");
			if(multiType==false){
				row1.createCell(17).setCellValue("遗失补办");
				row1.createCell(18).setCellValue("损坏更换");
				row1.createCell(19).setCellValue("信息变更");
			}else{
				row1.createCell(17).setCellValue("更新+遗失补办");
				row1.createCell(18).setCellValue("更新+损坏更换");
				row1.createCell(19).setCellValue("更新+更换/补办+信息变更");
			}
			
			List<Long> officeIdList = new ArrayList<Long>();
			
			if (officeId != null) {
				officeIdList.add(officeId);
			} else if (areaId != null) {
				officeIdList = officeService.findOfficeIdsByParentId(areaId);
			}
			List<CertificateSettlementStatisticsVO> findWorkList = certificateSettlementStatisticsService.findWorkList(
					applyId, org.springframework.util.StringUtils.collectionToCommaDelimitedString(productType),
					org.springframework.util.StringUtils.collectionToCommaDelimitedString(workType),
					org.springframework.util.StringUtils.collectionToCommaDelimitedString(officeIdList), agentId, startDate,
					endDate, multiType);

			HashMap<String, StaticCertMonth> monthMap = certificateSettlementStatisticsService.getStaticMap(findWorkList);
			int k=3;
			int oneA1=0;
			int oneA2=0;
			int oneA4=0;
			int oneA5=0;
			int twoA1=0;
			int twoA2=0;
			int twoA4=0;
			int twoA5=0;
			int oneR1=0;
			int oneR2=0;
			int oneR4=0;
			int oneR5=0;
			int twoR1=0;
			int twoR2=0;
			int twoR4=0;
			int twoR5=0;
			int replacementLosted=0;
			int replacementDamaged=0;
			int alterInfomation=0;
			for (String key : monthMap.keySet()){
				HSSFRow rown = sheet.createRow(k++);
				rown.createCell(0).setCellValue(key);
				rown.createCell(1).setCellValue(monthMap.get(key).getOneAdd1());
				oneA1+=(monthMap.get(key).getOneAdd1());
				rown.createCell(2).setCellValue(monthMap.get(key).getOneAdd2());
				oneA2+=(monthMap.get(key).getOneAdd2());
				rown.createCell(3).setCellValue(monthMap.get(key).getOneAdd4());
				oneA4+=(monthMap.get(key).getOneAdd4());
				rown.createCell(4).setCellValue(monthMap.get(key).getOneAdd5());
				oneA5+=(monthMap.get(key).getOneAdd5());
				rown.createCell(5).setCellValue(monthMap.get(key).getTwoAdd1()+monthMap.get(key).getFourAdd1());
				twoA1+=(monthMap.get(key).getTwoAdd1()+monthMap.get(key).getFourAdd1());
				rown.createCell(6).setCellValue(monthMap.get(key).getTwoAdd2()+monthMap.get(key).getFourAdd2());
				twoA2+=(monthMap.get(key).getTwoAdd2()+monthMap.get(key).getFourAdd2());
				rown.createCell(7).setCellValue(monthMap.get(key).getTwoAdd4()+monthMap.get(key).getFourAdd4());
				twoA4+=(monthMap.get(key).getTwoAdd4()+monthMap.get(key).getFourAdd4());
				rown.createCell(8).setCellValue(monthMap.get(key).getTwoAdd5()+monthMap.get(key).getFourAdd5());
				twoA5+=(monthMap.get(key).getTwoAdd5()+monthMap.get(key).getFourAdd5());
				rown.createCell(9).setCellValue(monthMap.get(key).getOneRenew1());
				oneR1+=(monthMap.get(key).getOneRenew1());
				rown.createCell(10).setCellValue(monthMap.get(key).getOneRenew2());
				oneR2+=(monthMap.get(key).getOneRenew2());
				rown.createCell(11).setCellValue(monthMap.get(key).getOneRenew4());
				oneR4+=(monthMap.get(key).getOneRenew4());
				rown.createCell(12).setCellValue(monthMap.get(key).getOneRenew5());
				oneR5+=(monthMap.get(key).getOneRenew5());
				rown.createCell(13).setCellValue(monthMap.get(key).getTwoRenew1()+monthMap.get(key).getFourRenew1());
				twoR1+=(monthMap.get(key).getTwoRenew1()+monthMap.get(key).getFourRenew1());
				rown.createCell(14).setCellValue(monthMap.get(key).getTwoRenew2()+monthMap.get(key).getFourRenew2());
				twoR2+=(monthMap.get(key).getTwoRenew2()+monthMap.get(key).getFourRenew2());
				rown.createCell(15).setCellValue(monthMap.get(key).getTwoRenew4()+monthMap.get(key).getFourRenew4());
				twoR4+=(monthMap.get(key).getTwoRenew4()+monthMap.get(key).getFourRenew4());
				rown.createCell(16).setCellValue(monthMap.get(key).getTwoRenew5()+monthMap.get(key).getFourRenew5());
				twoR5+=(monthMap.get(key).getTwoRenew5()+monthMap.get(key).getFourRenew5());
				rown.createCell(17).setCellValue(monthMap.get(key).getReplacementLosted());
				replacementLosted+=(monthMap.get(key).getReplacementLosted());
				rown.createCell(18).setCellValue(monthMap.get(key).getReplacementDamaged());
				replacementDamaged+=(monthMap.get(key).getReplacementDamaged());
				rown.createCell(19).setCellValue(monthMap.get(key).getAlterInfomation());
				alterInfomation+=(monthMap.get(key).getAlterInfomation());
			}
			HSSFRow rowi = sheet.createRow(k++);
			rowi.createCell(0).setCellValue("总计");
			rowi.createCell(1).setCellValue(oneA1);
			rowi.createCell(2).setCellValue(oneA2);
			rowi.createCell(3).setCellValue(oneA4);
			rowi.createCell(4).setCellValue(oneA5);
			rowi.createCell(5).setCellValue(twoA1);
			rowi.createCell(6).setCellValue(twoA2);
			rowi.createCell(7).setCellValue(twoA4);
			rowi.createCell(8).setCellValue(twoA5);
			rowi.createCell(9).setCellValue(oneR1);
			rowi.createCell(10).setCellValue(oneR2);
			rowi.createCell(11).setCellValue(oneR4);
			rowi.createCell(12).setCellValue(oneR5);
			rowi.createCell(13).setCellValue(twoR1);
			rowi.createCell(14).setCellValue(twoR2);
			rowi.createCell(15).setCellValue(twoR4);
			rowi.createCell(16).setCellValue(twoR5);
			rowi.createCell(17).setCellValue(replacementLosted);
			rowi.createCell(18).setCellValue(replacementDamaged);
			rowi.createCell(19).setCellValue(alterInfomation);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=certificateSettlementStatistics.xls");
			wb.write(baos);
			byte[] bytes = baos.toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(bytes);
			bos.close();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
