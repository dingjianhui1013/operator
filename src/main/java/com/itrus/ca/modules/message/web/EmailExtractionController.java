/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.message.entity.EmailExtraction;
import com.itrus.ca.modules.message.service.EmailExtractionService;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.settle.entity.KeyPurchase;

/**
 * 邮箱提取Controller
 * 
 * @author qt
 * @version 2015-11-30
 */
@Controller
@RequestMapping(value = "${adminPath}/message/emailExtraction")
public class EmailExtractionController extends BaseController {

	@Autowired
	private EmailExtractionService emailExtractionService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@ModelAttribute
	public EmailExtraction get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return emailExtractionService.get(id);
		} else {
			return new EmailExtraction();
		}
	}

	@RequiresPermissions("message:emailExtraction:view")
	@RequestMapping(value = { "list", "" })
	public String list(EmailExtraction emailExtraction, WorkDealInfo workDealInfo, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "makeCertStart", required = false) Date makeCertStart,
			@RequestParam(value = "makeCertEnd", required = false) Date makeCertEnd,
			@RequestParam(value = "expiredStart", required = false) Date expiredStart,
			@RequestParam(value = "expiredEnd", required = false) Date expiredEnd, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			emailExtraction.setCreateBy(user);
		}
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("applyId", apply);
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();

		Page<WorkDealInfo> page = workDealInfoService.find11(new Page<WorkDealInfo>(request, response), workDealInfo,
				apply, workType, makeCertStart, makeCertEnd, expiredStart, expiredEnd);

		model.addAttribute("proList", ProductType.getProductTypeAutoTask());

		model.addAttribute("workTypes", workDealInfoType.getProductTypeListNew());
		model.addAttribute("workType", workType);
		model.addAttribute("wdiStatu", workDealInfo.getDealInfoStatus());
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		
		model.addAttribute("makeCertStart", makeCertStart);
		model.addAttribute("makeCertEnd", makeCertEnd);
		model.addAttribute("expiredStart", expiredStart);
		model.addAttribute("expiredEnd", expiredEnd);

		if (apply == null && workType == null) {

			return "modules/message/emailExtractionList";
		}
		
		
		
		model.addAttribute("page", page);

		return "modules/message/emailExtractionList";
	}

	@RequiresPermissions("message:emailExtraction:view")
	@RequestMapping(value = "form")
	public String form(EmailExtraction emailExtraction, Model model) {
		model.addAttribute("emailExtraction", emailExtraction);
		return "modules/message/emailExtractionForm";
	}

	@RequiresPermissions("message:emailExtraction:edit")
	@RequestMapping(value = "save")
	public String save(EmailExtraction emailExtraction, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, emailExtraction)) {
			return form(emailExtraction, model);
		}
		emailExtractionService.save(emailExtraction);
		// addMessage(redirectAttributes, "保存邮箱提取'" + emailExtraction.getName()
		// + "'成功");
		return "redirect:" + Global.getAdminPath() + "/modules/message/emailExtraction/?repage";
	}

	@RequiresPermissions("message:emailExtraction:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		emailExtractionService.delete(id);
		addMessage(redirectAttributes, "删除邮箱提取成功");
		return "redirect:" + Global.getAdminPath() + "/modules/message/emailExtraction/?repage";
	}

	@RequestMapping(value = "export")
	public void export(HttpServletRequest request, HttpServletResponse response, WorkDealInfo workDealInfo,
			@RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "makeCertStart", required = false) Date makeCertStart,
			@RequestParam(value = "makeCertEnd", required = false) Date makeCertEnd,
			@RequestParam(value = "expiredStart", required = false) Date expiredStart,
			@RequestParam(value = "expiredEnd", required = false) Date expiredEnd)

	{
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("邮箱信息提取");
			HSSFCellStyle style = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 18);
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
			HSSFRow row = sheet.createRow(0);
			row.setHeightInPoints((short) 20);
			HSSFCell cell = row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("邮箱信息提取");
			HSSFRow row1 = sheet.createRow(1);
			row1.createCell(0).setCellValue("产品名称");
			;
			row1.createCell(1).setCellValue("应用名称");
			row1.createCell(2).setCellValue("经办人姓名");
			row1.createCell(3).setCellValue("经办人邮箱");
			row1.createCell(4).setCellValue("证书持有人名称");
			row1.createCell(5).setCellValue("持有人邮箱");
			WorkCompany workCompany = new WorkCompany();
			workCompany.setCompanyName(companyName);
			workDealInfo.setWorkCompany(workCompany);
			List<WorkDealInfo> list = workDealInfoService.find12(workDealInfo, apply, workType,makeCertStart,makeCertEnd,expiredStart,expiredEnd);
			for (int i = 0; i < list.size(); i++) {
				HSSFRow rown = sheet.createRow(i + 2);
				rown.createCell(0).setCellValue(list.get(i).getWorkCompany().getCompanyName());
				rown.createCell(1).setCellValue(list.get(i).getConfigApp().getAppName());
				if (list.get(i).getWorkCertInfo().getWorkCertApplyInfo().getName() != null) {
					rown.createCell(2).setCellValue(list.get(i).getWorkCertInfo().getWorkCertApplyInfo().getName());
				} else {
					rown.createCell(2).setCellValue("");
				}
				if (list.get(i).getWorkCertInfo().getWorkCertApplyInfo().getEmail() != null) {
					rown.createCell(3)
							.setCellValue(list.get(i).getWorkCertInfo().getWorkCertApplyInfo().getEmail() + ";");
				} else {
					rown.createCell(3).setCellValue("");
				}
				if (list.get(i).getWorkUser().getContactName() != null) {
					rown.createCell(4).setCellValue(list.get(i).getWorkUser().getContactName());
				} else {
					rown.createCell(4).setCellValue("");
				}
				if (list.get(i).getWorkUser().getContactEmail() != null) {
					rown.createCell(5).setCellValue(list.get(i).getWorkUser().getContactEmail() + ";");
				} else {
					rown.createCell(5).setCellValue("");
				}

			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition", "attachment; filename=emailExtraction.xls");
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
