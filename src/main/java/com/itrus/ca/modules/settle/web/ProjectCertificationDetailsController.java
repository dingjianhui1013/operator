/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.excel.ExportExcel;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.vo.WorkDealInfoVo;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.settle.entity.KeyPurchase;
import com.itrus.ca.modules.settle.entity.ProjectCertificationDetails;
import com.itrus.ca.modules.settle.service.ProjectCertificationDetailsService;
import com.itrus.ca.modules.settle.vo.ProjectCertificationDetailsVo;

/**
 * 项目发证明细Controller
 * @author qt
 * @version 2015-11-18
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/projectCertificationDetails")
public class ProjectCertificationDetailsController extends BaseController {

	@Autowired
	private ProjectCertificationDetailsService projectCertificationDetailsService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@Autowired
	private ConfigAppService configAppService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public ProjectCertificationDetails get(@RequestParam(required=false) Long id) {
		if (id != null){
			return projectCertificationDetailsService.get(id);
		}else{
			return new ProjectCertificationDetails();
		}
	}
	
	@RequiresPermissions("settle:projectCertificationDetails:view")
	@RequestMapping(value = {"list", ""})
	public String list(
			ProjectCertificationDetails projectCertificationDetails,WorkDealInfo workDealInfo,Date startTime, Date endTime,
			HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(value = "alias", required = false) Long alias) {
		
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("alias",alias);
		
		Page<WorkDealInfo> page = workDealInfoService.find4Apply(
				new Page<WorkDealInfo>(request, response), workDealInfo,
				startTime, endTime , alias);
		
		List<WorkDealInfo> noIxinInfos = page.getList();
		List<WorkDealInfo> isIxinInfos = workDealInfoService.find4ApplyIsIxin(
				workDealInfo, startTime, endTime , alias);
		noIxinInfos.addAll(isIxinInfos);
		
		page.setList(noIxinInfos);
		
		model.addAttribute("workType", workDealInfo.getDealInfoStatus());
		
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("page", page);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "modules/settle/projectCertificationDetailsList";
	}
	@RequestMapping(value = "export")
	public void export(
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "alias", required = false) Long alias,
			
			HttpServletRequest request,
			HttpServletResponse response)
	{	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WorkDealInfo workDealInfo=new WorkDealInfo();
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<Office> officeList = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
		try {
		List<WorkDealInfo> list = workDealInfoService.find4ApplyIsIxin(
				workDealInfo, startTime, endTime , alias);
		final String fileName = "WorkDealInfos.csv";
		final List<ProjectCertificationDetailsVo> ProjectCertificationDetailsVos = new ArrayList<ProjectCertificationDetailsVo>();
		String dealInfoType=null;
		String dealInfoType1=null;
		String dealInfoType2=null;
		String dealInfoType3=null;
		
		//新增个人一年证书
		int addPersonalYearCertificate=0;
		//新增个人两年证书
		int addPersonalTwoYearCertificate=0;
		//新增个人四年证书
		int addPersonalFourYearCertificate=0;
		//新增企业一年证书
		int addcompanyYearCertificate=0;
		//新增企业两年证书
		int addcompanyTwoYearCertificate=0;
		//新增企业四年证书
		int addcompanyFourYearCertificate=0;
		//更新个人一年证书
		int updatePersonalYearCertificate=0;
		//更新个人两年证书
		int updatePersonalTwoYearCertificate=0;
		//更新个人四年证书
		int updatePersonalFourYearCertificate=0;
		//更新企业一年证书
		int updatecompanyYearCertificate=0;
		//更新企业两年证书
		int updatecompanyTwoYearCertificate=0;
		//更新企业四年证书
		int updateFourYearCertificate=0;
		//遗失补办证书
		int lostCertificate=0;
		//损坏更换证书
		int damageCertificate=0;
			//System.out.println("lailemei");
		workDealInfo.setId(alias);
		ConfigApp configApp =configAppService.get(alias);
		//System.out.println(configApp.getAppName());
		
			
			/*Workbook wb = new HSSFWorkbook();// 定义工作簿
			CellStyle style = wb.createCellStyle(); //样式对象
			Cell cell =null;
			Sheet sheet = wb.createSheet(configApp.getAppName()+"项目发证明细");
			Row row = sheet.createRow(0);
			sheet.setDefaultColumnWidth(15);
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
			cell.setCellValue(configApp.getAppName()+"项目发证明细");
			//第二行数据
			sheet.addMergedRegion(new CellRangeAddress(1,1,1,14));
			Row row1 = sheet.createRow(1);
			cell = row1.createCell(1);
			font.setFontHeightInPoints((short)14);
			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("统计时间："+DateUtils.formatDate(startTime,"yyyy-MM-dd")+"—"+DateUtils.formatDate(endTime,"yyyy-MM-dd"));
			//第三行数据
			sheet.addMergedRegion(new CellRangeAddress(1,1,1,14));
			Row row2 = sheet.createRow(2);
			cell = row2.createCell(1);
			font.setFontHeightInPoints((short)14);
			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("新增：       个人证书 一年期证书"+5+"张； 两年期证书"+6+"张；  四年期证书"+6+"张；");				       
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,1,14));
			Row row3 = sheet.createRow(3);
			cell = row3.createCell(1);
			font.setFontHeightInPoints((short)14);
			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("        企业证书 一年期证书"+5+"张； 两年期证书"+6+"张；  四年期证书"+6+"张；");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,1,14));
			Row row4 = sheet.createRow(4);
			cell = row4.createCell(1);
			font.setFontHeightInPoints((short)14);
			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("更新：    个人证书 一年期证书"+5+"张； 两年期证书"+6+"张；  四年期证书"+6+"张；");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,1,14));
			Row row5 = sheet.createRow(5);
			cell = row5.createCell(1);
			font.setFontHeightInPoints((short)14);
			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("       企业证书 一年期证书"+5+"张； 两年期证书"+6+"张；  四年期证书"+6+"张；");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,1,14));
			Row row6 = sheet.createRow(6);
			cell = row6.createCell(1);
			font.setFontHeightInPoints((short)14);
			style.setFont(font);
			cell.setCellStyle(style);
			cell.setCellValue("遗失补办： 证书"+5+"张                         损坏更换：   证书"+5+"张");
			
			Row row7 = sheet.createRow(7);
	        row7.createCell((short) 0).setCellValue("ID");
	        row7.createCell((short) 1).setCellValue("单位名称");
	        row7.createCell((short) 2).setCellValue("业务类型");
	        row7.createCell((short) 3).setCellValue("证书类型");
	        row7.createCell((short) 4).setCellValue("证书有效期");
	        row7.createCell((short) 5).setCellValue("制证时间");
	        */
			/* int i=8;
			  for(int i=0;i<list.size();i++)
			{
			HSSFRow rown=sheet.createRow(i+8);
			
			rown.createCell(0).setCellValue(list.get(i).getKeySn());
			rown.createCell(0).setCellValue(list.get(i).getWorkCompany().getCompanyName());
			rown.createCell(1).setCellValue(productType.getProductTypeName(Integer.parseInt(list.get(i).getConfigProduct().getProductName()) ));
			rown.createCell(2).setCellValue(list.get(i).getDealInfoType());
			//证书有效期
			//rown.createCell(3).setCellValue(list.get(i).getEndCode()+"");
			rown.createCell(4).setCellValue(list.get(i).getWorkCertInfo().getSignDate());
			
			}
	        */
			 for (final WorkDealInfo dealInfo : list){
				 //row = sheet.createRow(i);
				 final ProjectCertificationDetailsVo dealInfoVo=new ProjectCertificationDetailsVo();
				 	dealInfoVo.setSvn(dealInfo.getSvn());
		        	dealInfoVo.setCompanyName(dealInfo.getWorkCompany().getCompanyName());;
		        	dealInfoVo.setCompanyName(productType.getProductTypeName(Integer.parseInt(dealInfo.getConfigProduct().getProductName())));
				// row.createCell((short) 0).setCellValue(dealInfo.getSvn());
				 System.out.println(dealInfo.getWorkCompany().getCompanyName());
				// row.createCell((short) 1).setCellValue(dealInfo.getWorkCompany().getCompanyName());
				 //row.createCell((short) 2).setCellValue(productType.getProductTypeName(Integer.parseInt(dealInfo.getConfigProduct().getProductName())));
				 if(dealInfo.getDealInfoType()==null)
					{
						 dealInfoType="";
					}else{
						dealInfoType=workDealInfoType.getDealInfoTypeName(dealInfo.getDealInfoType());
					}
					if(dealInfo.getDealInfoType1()==null)
					{
						 dealInfoType1="";
					}else{
						dealInfoType1=workDealInfoType.getDealInfoTypeName(dealInfo.getDealInfoType1());
					}
					if(dealInfo.getDealInfoType2()==null)
					{
						 dealInfoType2="";
					}else{
						dealInfoType2=workDealInfoType.getDealInfoTypeName(dealInfo.getDealInfoType2());
					}
					if(dealInfo.getDealInfoType3()==null)
					{
						 dealInfoType3="";
					}else{
						dealInfoType3=workDealInfoType.getDealInfoTypeName(dealInfo.getDealInfoType3());
					}
					dealInfoVo.setDealInfoType(dealInfoType+""+dealInfoType1+""+dealInfoType2+""+dealInfoType3);
					//row.createCell((short) 3).setCellValue(dealInfoType+""+dealInfoType1+""+dealInfoType2+""+dealInfoType3);
					if(dealInfo.getAddCertDays()==null){
						dealInfoVo.setCertDays(dealInfo.getYear()*365+dealInfo.getLastDays()+"（天）");
					}else{
						dealInfoVo.setCertDays(dealInfo.getYear()*365+dealInfo.getLastDays()+dealInfo.getAddCertDays()+"（天）");
					}
					String notafterString = df.format(dealInfo.getNotafter());
					dealInfoVo.setCertDays(notafterString);
					//row.createCell((short) 4).setCellValue(notafterString);
					if (dealInfo.getWorkCertInfo().getSignDate()!=null) {
						String signDateString = dfm.format(dealInfo.getWorkCertInfo().getSignDate());
						dealInfoVo.setSignDateString(signDateString);
						//row.createCell((short) 5).setCellValue(signDateString);
					}else{
						dealInfoVo.setSignDateString("");
						//row.createCell((short) 5).setCellValue("");
						
					}
			 }
			
			 new ExportExcel(configApp.getAppName()+"项目明细", ProjectCertificationDetailsVo.class).setDataList(ProjectCertificationDetailsVos).write(response, fileName).dispose();
				

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	        	
	       
		/*	 try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=keyPurchaseRecord.xls");
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
			System.out.println("+++++++++++++");
		}*/
	}
	

	@RequiresPermissions("settle:projectCertificationDetails:view")
	@RequestMapping(value = "form")
	public String form(ProjectCertificationDetails projectCertificationDetails, Model model) {
		model.addAttribute("projectCertificationDetails", projectCertificationDetails);
		return "modules/settle/projectCertificationDetailsForm";
	}

	@RequiresPermissions("settle:projectCertificationDetails:edit")
	@RequestMapping(value = "save")
	public String save(ProjectCertificationDetails projectCertificationDetails, Model model, RedirectAttributes redirectAttributes) {
		
		return "redirect:"+Global.getAdminPath()+"/modules/settle/projectCertificationDetails/?repage";
	}
	
	@RequiresPermissions("settle:projectCertificationDetails:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		projectCertificationDetailsService.delete(id);
		addMessage(redirectAttributes, "删除项目发证明细成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/projectCertificationDetails/?repage";
	}

}
