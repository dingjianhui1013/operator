package com.itrus.ca.modules.report.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.utils.excel.ExportExcel;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ReportQueryType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.web.KeyUsbKeyController;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.report.service.ReportService;
import com.itrus.ca.modules.report.vo.WorkDealInfoVO;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 基于workdealinfo表的报表查询
 */
@Controller
@RequestMapping(value = "${adminPath}/report/businessReport")
public class reportController extends BaseController {
	static Log log = LogFactory.getLog(reportController.class);
	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private ConfigAppService appService;

	@ModelAttribute
	public WorkDealInfo get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return workDealInfoService.get(id);
		} else {
			return new WorkDealInfo();
		}
	}

	@RequiresPermissions("report:businessReport:view")
	@RequestMapping(value = "listByDate")
	public String deleteList(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		 /*Configuration cfg = new Configuration().configure();  
		SessionFactory factory = cfg.get
		*/
				
				WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();   
				String[] names = wac.getBeanDefinitionNames();  
				for(int i=0; i<names.length; i++){
				    log.debug("---"+names[i]);  
				      
				}  
				EntityManagerFactory factory = (EntityManagerFactory)wac.getBean("entityManagerFactory");
				
				log.debug(factory);
				
				EntityManager em = factory.createEntityManager();
				
				Session session = em.unwrap(org.hibernate.Session.class);
				
				log.debug(session);
		
		if (startTime == null && endTime == null) {
			startTime = StringHelper.getFirstDayOfLastMonth();
			endTime = StringHelper.getLastDayOfLastMonth();
		}

		// user对应的所有网点
		List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);

		// 取出网点对应的所有应用
		List<ConfigApp> configAppList = Lists.newArrayList();

		for (int i = 0; i < officeList.size(); i++) {

			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
					.findAllByOfficeId(officeList.get(i).getId());
			for (ConfigAppOfficeRelation appOffice : appOffices) {
				if (!configAppList.contains(appOffice.getConfigApp())) {
					configAppList.add(appOffice.getConfigApp());
				}
			}
		}

		List<WorkDealInfoVO> vos = Lists.newArrayList();

		for (int i = 0; i < configAppList.size(); i++) {
			WorkDealInfoVO vo = new WorkDealInfoVO();
			vo.setAppId(configAppList.get(i).getId());
			vo.setAppName(configAppList.get(i).getAppName());
			vo.setValidCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
					ReportQueryType.TYPE_VALID_DEAL));
			vo.setNewCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
					ReportQueryType.TYPE_NEW_DEAL));
			vo.setUpdateCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
					ReportQueryType.TYPE_UPDATE_DEAL));
			vo.setUnUpdateCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
					ReportQueryType.TYPE_UNUPDATE_DEAL));
			vo.setMaintenanceCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
					ReportQueryType.TYPE_MAINTENANCE_DEAL));

			vos.add(vo);
		}

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);

		model.addAttribute("start", formatter.format(startTime));
		model.addAttribute("end", formatter.format(endTime));

		model.addAttribute("list", vos);

		return "modules/report/businessListByDate";
	}

	@RequiresPermissions("report:businessReport:view")
	@RequestMapping(value = "listDetailDealInfo")
	public String listDetailDealInfo(HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes, @RequestParam(value = "appId", required = true) Long appId,
			@RequestParam(value = "startTime", required = true) String startTime,
			@RequestParam(value = "endTime", required = true) String endTime,
			@RequestParam(value = "method", required = true) Integer method) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Page<WorkDealInfo> page = reportService.findPageByDate(new Page<WorkDealInfo>(request, response), appId,
				formatter.parse(startTime), formatter.parse(endTime), method);
		model.addAttribute("page", page);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("appName", appService.findByAppId(appId).getAppName());
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("appId", appId);
		model.addAttribute("method", method);
		return "modules/report/listDetailDealInfo";
	}

	@RequestMapping(value = "exportCollect")
	public void exportCollect(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime) throws UnsupportedEncodingException {
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		 
		
		try {

			// user对应的所有网点
			List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);

			// 取出网点对应的所有应用
			List<ConfigApp> configAppList = Lists.newArrayList();

			for (int i = 0; i < officeList.size(); i++) {

				List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
						.findAllByOfficeId(officeList.get(i).getId());
				for (ConfigAppOfficeRelation appOffice : appOffices) {
					if (!configAppList.contains(appOffice.getConfigApp())) {
						configAppList.add(appOffice.getConfigApp());
					}
				}
			}

			List<WorkDealInfoVO> workDealInfoVos = Lists.newArrayList();

			for (int i = 0; i < configAppList.size(); i++) {
				WorkDealInfoVO vo = new WorkDealInfoVO();

				vo.setValidCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
						ReportQueryType.TYPE_VALID_DEAL));
				vo.setNewCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
						ReportQueryType.TYPE_NEW_DEAL));
				vo.setUpdateCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
						ReportQueryType.TYPE_UPDATE_DEAL));
				vo.setUnUpdateCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
						ReportQueryType.TYPE_UNUPDATE_DEAL));
				vo.setMaintenanceCount(reportService.findCountByDate(configAppList.get(i).getId(), startTime, endTime,
						ReportQueryType.TYPE_MAINTENANCE_DEAL));
				vo.setAppName(configAppList.get(i).getAppName());
				workDealInfoVos.add(vo);
			}

			final String fileName = "WorkDealInfo.csv";

			new ExportExcel( sdf.format(startTime)+"日-"+ sdf.format(endTime)+"日 业务办理数据汇总", WorkDealInfoVO.class).setDataList(workDealInfoVos).write(response, fileName)
					.dispose();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "exportDetail")
	public void exportDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "appId", required = true) Long appId,
			@RequestParam(value = "startTime", required = true) String startTime,
			@RequestParam(value = "endTime", required = true) String endTime,
			@RequestParam(value = "method", required = true) Integer method) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		ConfigApp configApp = appService.findByAppId(appId);

		HSSFWorkbook wb = new HSSFWorkbook();// 定义工作簿
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		Cell cell = null;
		HSSFSheet sheet = wb.createSheet(configApp.getAppName() + "项目");
		HSSFRow row = sheet.createRow(0);
		sheet.setDefaultColumnWidth(5);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));// 合并单元格
		HSSFFont font = wb.createFont();

		// 第一行数据
		HSSFRow row0 = sheet.createRow(0);
		cell = row0.createCell(0);
		HSSFFont font0 = wb.createFont();
		HSSFCellStyle style0 = wb.createCellStyle();
		font0.setFontHeightInPoints((short) 18);
		style0.setFont(font0);
		style0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellStyle(style0);
		cell.setCellValue(configApp.getAppName() + "项目业务明细");
		// 第二行数据
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));
		HSSFRow row1 = sheet.createRow(1);
		cell = row1.createCell(1);
		font.setFontHeightInPoints((short) 10);
		style.setFont(font);
		cell.setCellStyle(style);
		cell.setCellValue("统计时间：" + DateUtils.formatDate(formatter.parse(startTime), "yyyy-MM-dd") + "—"
				+ DateUtils.formatDate(formatter.parse(endTime), "yyyy-MM-dd"));

		List<WorkDealInfoVO> list = Lists.newArrayList();
		
	
			list = reportService.findListByDate(appId, formatter.parse(startTime),
					formatter.parse(endTime), method);	
		
		
		

		HSSFRow row2 = sheet.createRow(2);
		row2.createCell(0).setCellValue("ID");
		row2.createCell(1).setCellValue("单位名称");
		row2.createCell(2).setCellValue("经办人");
		row2.createCell(3).setCellValue("制证日期");
		row2.createCell(4).setCellValue("业务类型");

		for (int i = 0; i < list.size(); i++) {
			HSSFRow rown = sheet.createRow(i + 3);
			rown.createCell(0).setCellValue(i + 1);
			if (list.get(i).getCompanyName() == null) {

				rown.createCell(1).setCellValue("");
			} else {
				rown.createCell(1).setCellValue(list.get(i).getCompanyName());
			}

			if (list.get(i).getContactName() == null) {
				rown.createCell(2).setCellValue("");
			} else {
				rown.createCell(2).setCellValue(list.get(i).getContactName());
			}

			if (list.get(i).getBusinessCardDate() == null) {
				rown.createCell(3).setCellValue("");
			} else {
				rown.createCell(3).setCellValue(DateUtils.formatDate(list.get(i).getBusinessCardDate(), "yyyy-MM-dd"));
			}

			StringBuffer dealType = new StringBuffer();

			if (list.get(i).getDealInfoType() != null) {
				dealType.append(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).append(" ");
			}
			if (list.get(i).getDealInfoType1() != null) {
				dealType.append(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType1())).append(" ");
			}
			if (list.get(i).getDealInfoType2() != null) {
				dealType.append(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType2())).append(" ");
			}
			if (list.get(i).getDealInfoType3() != null) {
				dealType.append(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType3()));
			}

			rown.createCell(4).setCellValue(dealType.toString());

		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition", "attachment; filename=projectCertificationDetails.xls");
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
	}

}
