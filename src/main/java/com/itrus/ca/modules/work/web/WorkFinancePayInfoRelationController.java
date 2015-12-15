/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jboss.logging.Param;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.sys.entity.Area;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.AreaService;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;

/**
 * 支付信息统计报表Controller
 * 
 * @author HUHAO
 * @version 2014-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workFinancePayInfoRelation")
public class WorkFinancePayInfoRelationController extends BaseController {

	@Autowired
	private WorkFinancePayInfoRelationService workFinancePayInfoRelationService;

	@Autowired
	private FinancePaymentInfoService financePaymentInfoService;

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private AreaService areaService;

	@ModelAttribute
	public WorkFinancePayInfoRelation get(
			@RequestParam(required = false) Long id) {
		if (id != null) {
			return workFinancePayInfoRelationService.get(id);
		} else {
			return new WorkFinancePayInfoRelation();
		}
	}

	@RequiresPermissions("work:workFinancePayInfoRelation:view")
	@RequestMapping(value = { "list", "" })
	public String list(WorkFinancePayInfoRelation workFinancePayInfoRelation,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			// workFinancePayInfoRelation.setCreateBy(user);
		}
		Page<WorkFinancePayInfoRelation> page = workFinancePayInfoRelationService
				.find(new Page<WorkFinancePayInfoRelation>(request, response),
						workFinancePayInfoRelation);
		model.addAttribute("page", page);
		return "modules/work/workFinancePayInfoRelationList";
	}

	@RequiresPermissions("work:workFinancePayInfoRelation:view")
	@RequestMapping(value = "statisticalList")
	public String statisticalReportlist(
			WorkFinancePayInfoRelation workFinancePayInfoRelation,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "payMethod", required = false) Integer payMethod,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "office", required = false) Long office,
			Model model) {
		
		User user = UserUtils.getUser();
//		List<Long> financeids = Lists.newArrayList();
//		List<Long> financeByAreaIds = Lists.newArrayList();
//		if (office != null && office != 0) {
//			List<Long> appids = Lists.newArrayList();
//			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
//					.findAllByOfficeId(office);// 通过网店获取引用的id
//
//			if (appOffices.size() > 0) {
//				for (int i = 0; i < appOffices.size(); i++) {
//					appids.add(appOffices.get(i).getConfigApp().getId());
//				}
//			} else {
//				appids.add(-1l);
//			}
//			List<FinancePaymentInfo> financeByAppid = financePaymentInfoService
//					.findByAppId(appids);// 根据应用id获取支付信息id
//			if (financeByAppid.size() < 1) {
//
//				financeids.add(-1l);
//			} else {
//				for (int i = 0; i < financeByAppid.size(); i++) {
//					financeids.add(financeByAppid.get(i).getId());
//				}
//			}
//		} else {
//			if (area != null) {
//				List<Long> appids = Lists.newArrayList();
//				List<Long> officeids = Lists.newArrayList();
//				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
//				if (offices.size() > 0) {
//					for (int i = 0; i < offices.size(); i++) {
//						officeids.add(offices.get(i).getId());
//					}
//				} else {
//					officeids.add(-1l);
//				}
//
//				List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
//						.findAllByOfficeId(officeids);// 根据网店id获取应用id
//				if (appOffices.size() > 0) {
//					for (int i = 0; i < appOffices.size(); i++) {
//						appids.add(appOffices.get(i).getConfigApp().getId());
//					}
//				} else {
//					appids.add(-1l);
//				}
//
//				List<FinancePaymentInfo> financeByAppid = financePaymentInfoService
//						.findByAppId(appids);// 根据应用id获取支付信息id
//				if (financeByAppid.size() < 1) {
//					financeByAreaIds.add(-1l);
//				} else {
//					for (int i = 0; i < financeByAppid.size(); i++) {
//						financeByAreaIds.add(financeByAppid.get(i).getId());
//					}
//				}
//			}
//
//		}
//		List<Long> idList = Lists.newArrayList();// 根据产品名称查询出支付信息
//		if (aplName != null) {
//			List<FinancePaymentInfo> finances = financePaymentInfoService
//					.findByAppName(aplName);
//
//			if (finances != null && finances.size() > 0) {
//				for (int i = 0; i < finances.size(); i++) {
//					idList.add(finances.get(i).getId());
//				}
//			}
//			if (finances == null || finances.size() < 1) {
//				idList.add(-1l);
//			}
//		}
//
//		Page<WorkFinancePayInfoRelation> page = workFinancePayInfoRelationService
//				.findFinancePay(new Page<WorkFinancePayInfoRelation>(request,
//						response), workFinancePayInfoRelation, startTime,
//						endTime, idList, financeids, financeByAreaIds,payMethod);
//		for (int i = 0; i < page.getList().size(); i++) {
//			double total = page.getList().get(i).getFinancePaymentInfo()
//					.getPaymentMoney();
//			double money = page.getList().get(i).getMoney();
//			BigDecimal result = new BigDecimal(Double.toString(total - money));
//			BigDecimal balance = result.setScale(2, RoundingMode.DOWN);
//			page.getList().get(i).setBalance(balance);
//		}
//
//		List<Area> areas = areaService.findAll();
//		List<Office> offsList = officeService.selectAreaList();
//
//		if (area != null) {
//			model.addAttribute("areaId", area);
//			List<Office> offices = officeService.findByParentId(area);
//			model.addAttribute("offices", offices);
//			if (office != null) {
//				model.addAttribute("office", office);
//			}
//		}
//		
		
		List<Office> offsList =  officeService.getOfficeByType(user, 1);
//    	
//		
//		List<Office> offices =  officeService.getOfficeByType(user, 2);
//		if (offices.size()==1) {
//			model.addAttribute("offsListSize",1);
//			model.addAttribute("offices",offices);
//		}
    	
    	if (area!=null) {
    		model.addAttribute("areaId",area);
    		
    		List<Office> offs = officeService.findByParentId(area);
    		model.addAttribute("offices",offs);
    		if (office!=null) {
    			model.addAttribute("office",office);
    		}
    	}
    	List<Long> ids = new ArrayList<Long>();
    	if (office!=null) {
			ids.add(office);
		}
    	if (office==null&&area!=null) {
    		List<Office> offs = officeService.findByParentId(area);
    		for (Office office2 : offs) {
    			ids.add(office2.getId());
			}
		}
		model.addAttribute("offsList", offsList);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("payMethod", payMethod);
		FinancePaymentInfo financePaymentInfo = new FinancePaymentInfo();
		financePaymentInfo.setCreateBy(user);
		financePaymentInfo.setPaymentMethod(payMethod);
	    Page<FinancePaymentInfo> page = financePaymentInfoService.find1(new Page<FinancePaymentInfo>(request, response), financePaymentInfo, startTime, endTime, ids); 
	    if (page.getList().size()>0) {
			for (int i = 0; i < page.getList().size(); i++) {
				int methode = page.getList().get(i).getPaymentMethod();
				switch (methode) {
				case 1: page.getList().get(i).setPaymentMethodName("现金"); break;
				case 2: page.getList().get(i).setPaymentMethodName("POS收款"); break;
				case 3: page.getList().get(i).setPaymentMethodName("银行转账"); break;
				case 4: page.getList().get(i).setPaymentMethodName("支付宝转账"); break;
				default:page.getList().get(i).setPaymentMethodName("您没有选择付款方式"); break;
				}
			}
		}        
	  List<FinancePaymentInfo>  list=financePaymentInfoService.findAll(financePaymentInfo);
	  int count=0;
	  int money=0;
	  int moneyIsNull=0;
	  for(int i=0;i<list.size();i++)
	  {
		 if(list.get(i).getDistinguish()==null)
		 {
			 count+=1;
		 }
		 if(list.get(i).getResidueMoney()>0)
		 {
			 money+=1;
		 }
		 if(list.get(i).getResidueMoney()==0)
		 {
			 moneyIsNull+=1;
		 }
	  }
	    model.addAttribute("page", page);
	    model.addAttribute("count", count);
	    model.addAttribute("money", money);
	    model.addAttribute("moneyIsNull", moneyIsNull);
		return "modules/finance/statisticalReportList";
	}

	@RequestMapping(value = "statisticalShow")
	public String statisticalReportShow(
			WorkFinancePayInfoRelation workFinancePayInfoRelation, Model model) {

		workFinancePayInfoRelation = workFinancePayInfoRelationService
				.findShow(workFinancePayInfoRelation);

		model.addAttribute("workFinancePayInfoRelation",
				workFinancePayInfoRelation);

		return "modules/finance/statisticalReportForm";
	}

	@RequiresPermissions("work:workFinancePayInfoRelation:view")
	@RequestMapping(value = "form")
	public String form(WorkFinancePayInfoRelation workFinancePayInfoRelation,
			Model model) {
		model.addAttribute("workFinancePayInfoRelation",
				workFinancePayInfoRelation);
		return "modules/work/workFinancePayInfoRelationForm";
	}

	@RequiresPermissions("work:workFinancePayInfoRelation:edit")
	@RequestMapping(value = "save")
	public String save(WorkFinancePayInfoRelation workFinancePayInfoRelation,
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, workFinancePayInfoRelation)) {
			return form(workFinancePayInfoRelation, model);
		}
		workFinancePayInfoRelationService.save(workFinancePayInfoRelation);
		// addMessage(redirectAttributes, "保存支付信息统计报表'" +
		// workFinancePayInfoRelation.getName() + "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/work/workFinancePayInfoRelation/?repage";
	}

	@RequiresPermissions("work:workFinancePayInfoRelation:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workFinancePayInfoRelationService.delete(id);
		addMessage(redirectAttributes, "删除支付信息统计报表成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/work/workFinancePayInfoRelation/?repage";
	}
	
	
	
	@RequestMapping(value = "exportDatalist")
	public void exportDatalist(WorkFinancePayInfoRelation workFinancePayInfoRelation,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "payMethod", required = false) Integer payMethod,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "office", required = false) Long office) throws IOException
	{
		
		User user = UserUtils.getUser();
		List<Office> offsList =  officeService.getOfficeByType(user, 1);
    	List<Long> ids = new ArrayList<Long>();
    	if (office!=null) {
			ids.add(office);
		}
    	if (office==null&&area!=null) {
    		List<Office> offs = officeService.findByParentId(area);
    		for (Office office2 : offs) {
    			ids.add(office2.getId());
			}
		}
		FinancePaymentInfo financePaymentInfo = new FinancePaymentInfo();
		financePaymentInfo.setCreateBy(user);
		financePaymentInfo.setPaymentMethod(payMethod);
	    List<FinancePaymentInfo> listpay = financePaymentInfoService.find1list(financePaymentInfo, startTime, endTime, ids); 
	    if (listpay.size()>0) {
			for (int i = 0; i < listpay.size(); i++) {
				int methode = listpay.get(i).getPaymentMethod();
				switch (methode) {
				case 1: listpay.get(i).setPaymentMethodName("现金"); break;
				case 2: listpay.get(i).setPaymentMethodName("POS收款"); break;
				case 3: listpay.get(i).setPaymentMethodName("银行转账"); break;
				case 4: listpay.get(i).setPaymentMethodName("支付宝转账"); break;
				default:listpay.get(i).setPaymentMethodName("您没有选择付款方式"); break;
				}
			}
		}        
	  List<FinancePaymentInfo>  list=financePaymentInfoService.findAll(financePaymentInfo);
	  int count=0;
	  int money=0;
	  int moneyIsNull=0;
	  for(int i=0;i<list.size();i++)
	  {
		 if(list.get(i).getDistinguish()==null)
		 {
			 count+=1;
		 }
		 if(list.get(i).getResidueMoney()>0)
		 {
			 money+=1;
		 }
		 if(list.get(i).getResidueMoney()==0)
		 {
			 moneyIsNull+=1;
		 }
	  }
	  HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("支付款项统计");
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0,(short)0, 0, (short)9));
		HSSFRow row0=sheet.createRow(0);
		HSSFCell cell0=row0.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("支付款项统计表");
		HSSFRow row1=sheet.createRow(1);
		row1.createCell(0).setCellValue("付款单位名称");
		row1.createCell(1).setCellValue("付款金额/元");
		row1.createCell(2).setCellValue("未确认付款项/元");
		row1.createCell(3).setCellValue("联系人");
		row1.createCell(4).setCellValue("联系方式");
		row1.createCell(5).setCellValue("付款时间");
		row1.createCell(6).setCellValue("付款方式");
		row1.createCell(7).setCellValue("记录方式");
		row1.createCell(8).setCellValue("记录人员");
		row1.createCell(9).setCellValue("记录是时间");
		for(int i=0;i<listpay.size();i++)
		{
			HSSFRow rown=sheet.createRow(i+2);
			rown.createCell(0).setCellValue(listpay.get(i).getCompany());
			rown.createCell(1).setCellValue(listpay.get(i).getPaymentMoney()-listpay.get(i).getResidueMoney());
			rown.createCell(2).setCellValue(listpay.get(i).getResidueMoney());
			rown.createCell(3).setCellValue(listpay.get(i).getCommUserName());
			rown.createCell(4).setCellValue(listpay.get(i).getCommMobile());
			rown.createCell(5).setCellValue(listpay.get(i).getCreateDate()+"");
			if(listpay.get(i).getPaymentMethod()==1)
			{
				rown.createCell(6).setCellValue("现金");
			}
			if(listpay.get(i).getPaymentMethod()==2)
			{
				rown.createCell(6).setCellValue("POS付款");
			}
			if(listpay.get(i).getPaymentMethod()==3)
			{
				rown.createCell(6).setCellValue("银行转账");
			}
			if(listpay.get(i).getPaymentMethod()==4)
			{
				rown.createCell(6).setCellValue("支付宝转账");
			}
			if(listpay.get(i).getDistinguish()!=null)
			{
				rown.createCell(7).setCellValue("手动添加");
			}else
			{
				rown.createCell(7).setCellValue("批量导入");
			}
			rown.createCell(8).setCellValue(listpay.get(i).getCreateBy().getName());
			rown.createCell(9).setCellValue(listpay.get(i).getCreateBy().getCreateDate()+"");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition", "attachment; filename=paymentMoney.xls");
		wb.write(baos);
		byte[] bytes = baos.toByteArray();
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(bytes);
		bos.close();
		baos.close();
	}
	
	/*
	 * 导出数据
	 */
	@RequestMapping(value = "exportData")
	@ResponseBody
	public String exportExcelData(HttpServletRequest request, HttpServletResponse response, Model model){
		JSONObject json = new JSONObject();
		try{
			workFinancePayInfoRelationService.exportExcelData(request, response, financePaymentInfoService.findAll());
			json.put("status", 1);
			json.put("msg", "导出成功");
		}catch(Exception ex){
			try {
				json.put("msg", "导出失败");
				json.put("status", -1);
				return json.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return json.toString();
	}

}
