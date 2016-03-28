/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;
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
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.settle.entity.PaymethodCertificateSettle;
import com.itrus.ca.modules.settle.service.PaymethodCertificateSettleService;
import com.itrus.ca.modules.settle.vo.CertificatePayMethod;
import com.itrus.ca.modules.settle.vo.CertificatePayMethodDetails;
import com.itrus.ca.modules.settle.vo.PaymethodCertificateSettleVo;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 支付方式证书结算Controller
 * 
 * @author qt
 * @version 2016-01-31
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/paymethodCertificateSettle")
public class PaymethodCertificateSettleController extends BaseController {

	@Autowired
	private PaymethodCertificateSettleService paymethodCertificateSettleService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@ModelAttribute
	public PaymethodCertificateSettle get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return paymethodCertificateSettleService.get(id);
		} else {
			return new PaymethodCertificateSettle();
		}
	}

	@RequiresPermissions("settle:paymethodCertificateSettle:view")

	@RequestMapping(value = { "list", "" })
	public String list(Long areaId, Long officeId, Date startDate, Date endDate, String tempStyle, String agentId,
			boolean multiType, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "applyId", required = false) Long applyId,
			@RequestParam(value = "proList", required = false) List<String> productType,
			@RequestParam(value = "workTypes", required = false) List<String> workType, Model model) {

		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<String> agentIds = Lists.newArrayList();
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
		if (tempStyle != null) {
			List<ConfigChargeAgent> agentList = configChargeAgentService.findByStyle(tempStyle.toString());
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
		model.addAttribute("multiType", multiType);
		model.addAttribute("workType", workType);
		model.addAttribute("offsList", offsList);
		model.addAttribute("areaId", areaId);

		model.addAttribute("officeId", officeId);
		// 开始日期，结束日期，应用必须选择
		if (startDate == null || endDate == null || applyId == null) {
			model.addAttribute("startDate", DateUtils.firstDayOfMonth(new Date()));
			model.addAttribute("endDate", new Date());
			return "modules/settle/paymethodCertificateSettleList";
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

		if (tempStyle != null && StringUtils.isBlank(agentId)) {
			List<ConfigChargeAgent> agentList = configChargeAgentService.findByStyle(tempStyle.toString());
			for (ConfigChargeAgent configChargeAgent : agentList) {
				agentIds.add(configChargeAgent.getId().toString());
			}
		}
		if (StringUtils.isNotBlank(agentId)) {
			agentIds.add(agentId);
		}
		List<PaymethodCertificateSettleVo> findWorkList1 = paymethodCertificateSettleService.findMulitWorkList1(applyId,
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(productType),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(workType),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(officeIdList),
				org.springframework.util.StringUtils.collectionToCommaDelimitedString(agentIds), startDate, endDate);

		HashMap<String, CertificatePayMethod> monthMap = paymethodCertificateSettleService.getStaticMap(findWorkList1);

		model.addAttribute("list", findWorkList1);
		model.addAttribute("monthList", new ArrayList<String>(monthMap.keySet()));
		model.addAttribute("sumList", monthMap);

		return "modules/settle/paymethodCertificateSettleList";
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

	@RequiresPermissions("settle:paymethodCertificateSettle:view")
	@RequestMapping(value = "form")
	public String form(PaymethodCertificateSettle paymethodCertificateSettle, Model model) {
		model.addAttribute("paymethodCertificateSettle", paymethodCertificateSettle);
		return "modules/settle/paymethodCertificateSettleForm";
	}

	@RequiresPermissions("settle:paymethodCertificateSettle:edit")
	@RequestMapping(value = "save")
	public String save(PaymethodCertificateSettle paymethodCertificateSettle, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, paymethodCertificateSettle)) {
			return form(paymethodCertificateSettle, model);
		}
		paymethodCertificateSettleService.save(paymethodCertificateSettle);
		addMessage(redirectAttributes, "保存支付方式证书结算'" + paymethodCertificateSettle.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/modules/settle/paymethodCertificateSettle/?repage";
	}

	@RequiresPermissions("settle:paymethodCertificateSettle:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		paymethodCertificateSettleService.delete(id);
		addMessage(redirectAttributes, "删除支付方式证书结算成功");
		return "redirect:" + Global.getAdminPath() + "/modules/settle/paymethodCertificateSettle/?repage";
	}

	@RequiresPermissions("settle:paymethodCertificateSettle:edit")
	@RequestMapping(value = "export")
	public void export(Long areaId, Long officeId, Date startDate, Date endDate, String tempStyle, String agentId,
			boolean multiType, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "applyId", required = false) Long applyId,
			@RequestParam(value = "proList", required = false) List<String> productType,
			@RequestParam(value = "workTypes", required = false) List<String> workType, Model model) {
		try {
			String product = "";
			if (productType != null && productType.size() > 0) {
				for (int i = 0; i < productType.size(); i++) {
					product += ProductType.getProductTypeName(Integer.parseInt(productType.get(i))) + ",";
				}
				StringUtils.removeEnd(product, ",");
			}
			String yingyong = "";
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

			List<Long> officeIdList = new ArrayList<Long>();

			if (officeId != null) {
				officeIdList.add(officeId);
			} else if (areaId != null) {
				officeIdList = officeService.findOfficeIdsByParentId(areaId);
			}

			List<Long> agentIds = new ArrayList<Long>();
			if (tempStyle != null && StringUtils.isBlank(agentId)) {
				List<ConfigChargeAgent> agentList = configChargeAgentService.findByStyle(tempStyle.toString());
				for (ConfigChargeAgent configChargeAgent : agentList) {
					agentIds.add(configChargeAgent.getId());
				}
			}
			if (StringUtils.isNotBlank(agentId)) {
				agentIds.add(Long.parseLong(agentId));
			}

			List<PaymethodCertificateSettleVo> findWorkList = paymethodCertificateSettleService.findMulitWorkList1(
					applyId, org.springframework.util.StringUtils.collectionToCommaDelimitedString(productType),
					org.springframework.util.StringUtils.collectionToCommaDelimitedString(workType),
					org.springframework.util.StringUtils.collectionToCommaDelimitedString(officeIdList),
					org.springframework.util.StringUtils.collectionToCommaDelimitedString(agentIds), startDate,
					endDate);

			HashMap<String, CertificatePayMethod> monthMap = paymethodCertificateSettleService
					.getStaticMap(findWorkList);

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("证书结算统计表");
			HSSFCellStyle style = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 14);
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			HSSFRow row0 = sheet.createRow(0);
			HSSFCell cell0 = row0.createCell(0);
			cell0.setCellValue("项目：" + yingyong);

			cell0.setCellStyle(style);
			HSSFRow row1 = sheet.createRow(1);
			HSSFRow row2 = sheet.createRow(2);
			HSSFRow row3 = sheet.createRow(3);
			int totalColums = 1;
			int curCol = 1;
			row1.createCell(0).setCellValue("月份");
			if (monthMap.get("total").getXzqyadd1().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzqyadd1(), "新增", "企业证书", "一年");
			}
			if (monthMap.get("total").getXzqyadd2().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzqyadd2(), "新增", "企业证书", "二年");
			}
			if (monthMap.get("total").getXzqyadd3().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzqyadd3(), "新增", "企业证书", "三年");
			}
			if (monthMap.get("total").getXzqyadd4().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzqyadd4(), "新增", "企业证书", "四年");
			}
			if (monthMap.get("total").getXzqyadd5().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzqyadd5(), "新增", "企业证书", "五年");
			}
			if (monthMap.get("total").getXzgrQadd1().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrQadd1(), "新增", "个人证书（企业）", "一年");
			}
			if (monthMap.get("total").getXzgrQadd2().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrQadd2(), "新增", "个人证书（企业）", "二年");
			}
			if (monthMap.get("total").getXzgrQadd3().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrQadd3(), "新增", "个人证书（企业）", "三年");
			}
			if (monthMap.get("total").getXzgrQadd4().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrQadd4(), "新增", "个人证书（企业）", "四年");
			}
			if (monthMap.get("total").getXzgrQadd5().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrQadd5(), "新增", "个人证书（企业）", "五年");
			}

			if (monthMap.get("total").getXzgrGadd1().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrGadd1(), "新增", "个人证书（机构）", "一年");
			}
			if (monthMap.get("total").getXzgrGadd2().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrGadd2(), "新增", "个人证书（机构）", "二年");
			}
			if (monthMap.get("total").getXzgrGadd3().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrGadd3(), "新增", "个人证书（机构）", "三年");
			}
			if (monthMap.get("total").getXzgrGadd4().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrGadd4(), "新增", "个人证书（机构）", "四年");
			}
			if (monthMap.get("total").getXzgrGadd5().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzgrGadd5(), "新增", "个人证书（机构）", "五年");
			}

			if (monthMap.get("total").getXzjgadd1().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzjgadd1(), "新增", "机构证书", "一年");
			}
			if (monthMap.get("total").getXzjgadd2().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzjgadd2(), "新增", "机构证书", "二年");
			}
			if (monthMap.get("total").getXzjgadd3().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzjgadd3(), "新增", "机构证书", "三年");
			}
			if (monthMap.get("total").getXzjgadd4().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzjgadd4(), "新增", "机构证书", "四年");
			}
			if (monthMap.get("total").getXzjgadd5().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getXzjgadd5(), "新增", "机构证书", "五年");
			}

			if (monthMap.get("total").getGxqyadd1().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxqyadd1(), "更新", "企业证书", "一年");
			}
			if (monthMap.get("total").getGxqyadd2().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxqyadd2(), "更新", "企业证书", "二年");
			}
			if (monthMap.get("total").getGxqyadd3().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxqyadd3(), "更新", "企业证书", "三年");
			}
			if (monthMap.get("total").getGxqyadd4().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxqyadd4(), "更新", "企业证书", "四年");
			}
			if (monthMap.get("total").getGxqyadd5().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxqyadd5(), "更新", "企业证书", "五年");
			}
			if (monthMap.get("total").getGxgrQadd1().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrQadd1(), "更新", "个人证书（企业）", "一年");
			}
			if (monthMap.get("total").getGxgrQadd2().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrQadd2(), "更新", "个人证书（企业）", "二年");
			}
			if (monthMap.get("total").getGxgrQadd3().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrQadd3(), "更新", "个人证书（企业）", "三年");
			}
			if (monthMap.get("total").getGxgrQadd4().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrQadd4(), "更新", "个人证书（企业）", "四年");
			}
			if (monthMap.get("total").getGxgrQadd5().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrQadd5(), "更新", "个人证书（企业）", "五年");
			}

			if (monthMap.get("total").getGxgrGadd1().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrGadd1(), "更新", "个人证书（机构）", "一年");
			}
			if (monthMap.get("total").getGxgrGadd2().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrGadd2(), "更新", "个人证书（机构）", "二年");
			}
			if (monthMap.get("total").getGxgrGadd3().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrGadd3(), "更新", "个人证书（机构）", "三年");
			}
			if (monthMap.get("total").getGxgrGadd4().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrGadd4(), "新增", "个人证书（机构）", "四年");
			}
			if (monthMap.get("total").getGxgrGadd5().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxgrGadd5(), "更新", "个人证书（机构）", "五年");
			}

			if (monthMap.get("total").getGxjgadd1().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxjgadd1(), "更新", "机构证书", "一年");
			}
			if (monthMap.get("total").getGxjgadd2().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxjgadd2(), "更新", "机构证书", "二年");
			}
			if (monthMap.get("total").getGxjgadd3().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxjgadd3(), "更新", "机构证书", "三年");
			}
			if (monthMap.get("total").getGxjgadd4().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxjgadd4(), "更新", "机构证书", "四年");
			}
			if (monthMap.get("total").getGxjgadd5().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getGxjgadd5(), "更新", "机构证书", "五年");
			}

			if (monthMap.get("total").getLostCerateqy().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getLostCerateqy(), "遗失补办", "企业证书", "全部");
			}
			if (monthMap.get("total").getLostCerategrQ().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getLostCerategrQ(), "遗失补办", "个人证书（企业）",
						"全部");
			}
			if (monthMap.get("total").getLostCerategrG().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getLostCerategrG(), "遗失补办", "个人证书（机构）",
						"全部");
			}
			if (monthMap.get("total").getLostCeratejg().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getLostCeratejg(), "遗失补办", "机构证书", "全部");
			}

			if (monthMap.get("total").getDamageCertificateqy().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getDamageCertificateqy(), "损坏更换", "企业证书",
						"全部");
			}
			if (monthMap.get("total").getDamageCertificategrQ().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getDamageCertificategrQ(), "损坏更换",
						"个人证书（企业）", "全部");
			}
			if (monthMap.get("total").getDamageCertificategrG().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getDamageCertificategrG(), "损坏更换",
						"个人证书（机构）", "全部");
			}
			if (monthMap.get("total").getDamageCertificatejg().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getDamageCertificatejg(), "损坏更换", "机构证书",
						"全部");
			}

			if (monthMap.get("total").getModifyNumqy().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getModifyNumqy(), "变更", "企业证书", "全部");
			}
			if (monthMap.get("total").getModifyNumgrQ().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getModifyNumgrQ(), "变更", "个人证书（企业）",
						"全部");
			}
			if (monthMap.get("total").getModifyNumgrG().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getModifyNumgrG(), "变更", "个人证书（机构）",
						"全部");
			}
			if (monthMap.get("total").getModifyNumjg().getTotalCount() >= 0) {
				curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getModifyNumjg(), "变更", "机构证书", "全部");
			}
			if (multiType) {
				if (monthMap.get("total").getChangeLostqyNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeLostqyNum(), "信息变更+遗失补办",
							"企业证书", "全部");
				}
				if (monthMap.get("total").getChangeLostgrQNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeLostgrQNum(), "信息变更+遗失补办",
							"个人证书（企业）", "全部");
				}
				if (monthMap.get("total").getChangeLostgrGNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeLostgrGNum(), "信息变更+遗失补办",
							"个人证书（机构）", "全部");
				}
				if (monthMap.get("total").getChangeLostjgNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeLostjgNum(), "信息变更+遗失补办",
							"机构证书", "全部");
				}

				if (monthMap.get("total").getChangeReplaceqyNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeReplaceqyNum(), "变更+损坏更换",
							"企业证书", "全部");
				}
				if (monthMap.get("total").getChangeReplacegrQNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeReplacegrQNum(), "变更+损坏更换",
							"个人证书（企业）", "全部");
				}
				if (monthMap.get("total").getChangeReplacegrGNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeReplacegrGNum(), "变更+损坏更换",
							"个人证书（机构）", "全部");
				}
				if (monthMap.get("total").getChangeReplacejgNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeReplacejgNum(), "变更+损坏更换",
							"机构证书", "全部");
				}

				if (monthMap.get("total").getUpdateLostqyNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostqyNum(), "更新+遗失补办",
							"企业证书", "一年");
				}
				if (monthMap.get("total").getUpdateLostqyNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostqyNum2(), "更新+遗失补办",
							"企业证书", "二年");
				}
				if (monthMap.get("total").getUpdateLostqyNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostqyNum3(), "更新+遗失补办",
							"企业证书", "三年");
				}
				if (monthMap.get("total").getUpdateLostqyNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostqyNum4(), "更新+遗失补办",
							"企业证书", "四年");
				}
				if (monthMap.get("total").getUpdateLostqyNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostqyNum5(), "更新+遗失补办",
							"企业证书", "五年");
				}
				if (monthMap.get("total").getUpdateLostgrQNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrQNum(), "更新+遗失补办",
							"个人证书（企业）", "一年");
				}
				if (monthMap.get("total").getUpdateLostgrQNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrQNum2(), "更新+遗失补办",
							"个人证书（企业）", "二年");
				}
				if (monthMap.get("total").getUpdateLostgrQNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrQNum3(), "更新+遗失补办",
							"个人证书（企业）", "三年");
				}
				if (monthMap.get("total").getUpdateLostgrQNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrQNum4(), "更新+遗失补办",
							"个人证书（企业）", "四年");
				}
				if (monthMap.get("total").getUpdateLostgrQNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrQNum5(), "更新+遗失补办",
							"个人证书（企业）", "五年");
				}

				if (monthMap.get("total").getUpdateLostgrGNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrGNum(), "更新+遗失补办",
							"个人证书（机构）", "一年");
				}
				if (monthMap.get("total").getUpdateLostgrGNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrGNum2(), "更新+遗失补办",
							"个人证书（机构）", "二年");
				}
				if (monthMap.get("total").getUpdateLostgrGNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrGNum3(), "更新+遗失补办",
							"个人证书（机构）", "三年");
				}
				if (monthMap.get("total").getUpdateLostgrGNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrGNum4(), "更新+遗失补办",
							"个人证书（机构）", "四年");
				}
				if (monthMap.get("total").getUpdateLostgrGNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostgrGNum5(), "更新+遗失补办",
							"个人证书（机构）", "五年");
				}

				if (monthMap.get("total").getUpdateLostjgNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostjgNum(), "更新+遗失补办",
							"机构证书", "一年");
				}
				if (monthMap.get("total").getUpdateLostjgNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostjgNum2(), "更新+遗失补办",
							"机构证书", "二年");
				}
				if (monthMap.get("total").getUpdateLostjgNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostjgNum3(), "更新+遗失补办",
							"机构证书", "三年");
				}
				if (monthMap.get("total").getUpdateLostjgNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostjgNum4(), "更新+遗失补办",
							"机构证书", "四年");
				}
				if (monthMap.get("total").getUpdateLostjgNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateLostjgNum5(), "更新+遗失补办",
							"机构证书", "五年");
				}

				if (monthMap.get("total").getUpdateReplaceqyNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplaceqyNum(), "更新+损坏更换",
							"企业证书", "一年");
				}
				if (monthMap.get("total").getUpdateReplaceqyNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplaceqyNum2(), "更新+损坏更换",
							"企业证书", "二年");
				}
				if (monthMap.get("total").getUpdateReplaceqyNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplaceqyNum3(), "更新+损坏更换",
							"企业证书", "三年");
				}
				if (monthMap.get("total").getUpdateReplaceqyNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplaceqyNum4(), "更新+损坏更换",
							"企业证书", "四年");
				}
				if (monthMap.get("total").getUpdateReplaceqyNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplaceqyNum5(), "更新+损坏更换",
							"企业证书", "五年");
				}
				if (monthMap.get("total").getUpdateReplacegrQNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrQNum(), "更新+损坏更换",
							"个人证书（企业）", "一年");
				}
				if (monthMap.get("total").getUpdateReplacegrQNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrQNum2(), "更新+损坏更换",
							"个人证书（企业）", "二年");
				}
				if (monthMap.get("total").getUpdateReplacegrQNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrQNum3(), "更新+损坏更换",
							"个人证书（企业）", "三年");
				}
				if (monthMap.get("total").getUpdateReplacegrQNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrQNum4(), "更新+损坏更换",
							"个人证书（企业）", "四年");
				}
				if (monthMap.get("total").getUpdateReplacegrQNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrQNum5(), "更新+损坏更换",
							"个人证书（企业）", "五年");
				}

				if (monthMap.get("total").getUpdateReplacegrGNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrGNum(), "更新+损坏更换",
							"个人证书（机构）", "一年");
				}
				if (monthMap.get("total").getUpdateReplacegrGNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrGNum2(), "更新+损坏更换",
							"个人证书（机构）", "二年");
				}
				if (monthMap.get("total").getUpdateReplacegrGNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrGNum3(), "更新+损坏更换",
							"个人证书（机构）", "三年");
				}
				if (monthMap.get("total").getUpdateReplacegrGNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrGNum4(), "更新+损坏更换",
							"个人证书（机构）", "四年");
				}
				if (monthMap.get("total").getUpdateReplacegrGNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacegrGNum5(), "更新+损坏更换",
							"个人证书（机构）", "五年");
				}

				if (monthMap.get("total").getUpdateReplacejgNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacejgNum(), "更新+损坏更换",
							"机构证书", "一年");
				}
				if (monthMap.get("total").getUpdateReplacejgNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacejgNum2(), "更新+损坏更换",
							"机构证书", "二年");
				}
				if (monthMap.get("total").getUpdateReplacejgNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacejgNum3(), "更新+损坏更换",
							"机构证书", "三年");
				}
				if (monthMap.get("total").getUpdateReplacejgNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacejgNum4(), "更新+损坏更换",
							"机构证书", "四年");
				}
				if (monthMap.get("total").getUpdateReplacejgNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateReplacejgNum5(), "更新+损坏更换",
							"机构证书", "五年");
				}

				if (monthMap.get("total").getUpdateChangeqyNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangeqyNum(), "更新 +变更",
							"企业证书", "一年");
				}
				if (monthMap.get("total").getUpdateChangeqyNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangeqyNum2(), "更新 +变更",
							"企业证书", "二年");
				}
				if (monthMap.get("total").getUpdateChangeqyNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangeqyNum3(), "更新 +变更",
							"企业证书", "三年");
				}
				if (monthMap.get("total").getUpdateChangeqyNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangeqyNum4(), "更新 +变更",
							"企业证书", "四年");
				}
				if (monthMap.get("total").getUpdateChangeqyNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangeqyNum5(), "更新 +变更",
							"企业证书", "五年");
				}
				if (monthMap.get("total").getUpdateChangegrQNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrQNum(), "更新 +变更",
							"个人证书（企业）", "一年");
				}
				if (monthMap.get("total").getUpdateChangegrQNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrQNum2(), "更新 +变更",
							"个人证书（企业）", "二年");
				}
				if (monthMap.get("total").getUpdateChangegrQNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrQNum3(), "更新 +变更",
							"个人证书（企业）", "三年");
				}
				if (monthMap.get("total").getUpdateChangegrQNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrQNum4(), "更新 +变更",
							"个人证书（企业）", "四年");
				}
				if (monthMap.get("total").getUpdateChangegrQNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrQNum5(), "更新 +变更",
							"个人证书（企业）", "五年");
				}

				if (monthMap.get("total").getUpdateChangegrGNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrGNum(), "更新 +变更",
							"个人证书（机构）", "一年");
				}
				if (monthMap.get("total").getUpdateChangegrGNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrGNum2(), "更新 +变更",
							"个人证书（机构）", "二年");
				}
				if (monthMap.get("total").getUpdateChangegrGNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrGNum3(), "更新 +变更",
							"个人证书（机构）", "三年");
				}
				if (monthMap.get("total").getUpdateChangegrGNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrGNum4(), "更新 +变更",
							"个人证书（机构）", "四年");
				}
				if (monthMap.get("total").getUpdateChangegrGNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangegrGNum5(), "更新 +变更",
							"个人证书（机构）", "五年");
				}

				if (monthMap.get("total").getUpdateChangejgNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangejgNum(), "更新 +变更",
							"机构证书", "一年");
				}
				if (monthMap.get("total").getUpdateChangejgNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangejgNum2(), "更新 +变更",
							"机构证书", "二年");
				}
				if (monthMap.get("total").getUpdateChangejgNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangejgNum3(), "更新 +变更",
							"机构证书", "三年");
				}
				if (monthMap.get("total").getUpdateChangejgNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangejgNum4(), "更新 +变更",
							"机构证书", "四年");
				}
				if (monthMap.get("total").getUpdateChangejgNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getUpdateChangejgNum5(), "更新 +变更",
							"机构证书", "五年");
				}

				if (monthMap.get("total").getChangeUpdateLostqyNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostqyNum(),
							" 更新 +变更+遗失补办", "企业证书", "一年");
				}
				if (monthMap.get("total").getChangeUpdateLostqyNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostqyNum2(),
							" 更新 +变更+遗失补办", "企业证书", "二年");
				}
				if (monthMap.get("total").getChangeUpdateLostqyNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostqyNum3(),
							" 更新 +变更+遗失补办", "企业证书", "三年");
				}
				if (monthMap.get("total").getChangeUpdateLostqyNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostqyNum4(),
							"更新 +变更+遗失补办", "企业证书", "四年");
				}
				if (monthMap.get("total").getChangeUpdateLostqyNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostqyNum5(),
							"更新 +变更+遗失补办", "企业证书", "五年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrQNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrQNum(),
							"更新 +变更+遗失补办", "个人证书（企业）", "一年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrQNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrQNum2(),
							"更新 +变更+遗失补办", "个人证书（企业）", "二年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrQNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrQNum3(),
							"更新 +变更+遗失补办", "个人证书（企业）", "三年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrQNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrQNum4(),
							"更新 +变更+遗失补办", "个人证书（企业）", "四年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrQNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrQNum5(),
							"更新 +变更+遗失补办", "个人证书（企业）", "五年");
				}

				if (monthMap.get("total").getChangeUpdateLostgrGNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrGNum(),
							"更新 +变更+遗失补办", "个人证书（机构）", "一年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrGNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrGNum2(),
							"更新 +变更+遗失补办", "个人证书（机构）", "二年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrGNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrGNum3(),
							"更新 +变更+遗失补办", "个人证书（机构）", "三年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrGNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrGNum4(),
							"更新 +变更+遗失补办", "个人证书（机构）", "四年");
				}
				if (monthMap.get("total").getChangeUpdateLostgrGNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostgrGNum5(),
							"更新 +变更+遗失补办", "个人证书（机构）", "五年");
				}

				if (monthMap.get("total").getChangeUpdateLostjgNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostjgNum(),
							"更新 +变更+遗失补办", "机构证书", "一年");
				}
				if (monthMap.get("total").getChangeUpdateLostjgNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostjgNum2(),
							"更新 +变更+遗失补办", "机构证书", "二年");
				}
				if (monthMap.get("total").getChangeUpdateLostjgNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostjgNum3(),
							"更新 +变更+遗失补办", "机构证书", "三年");
				}
				if (monthMap.get("total").getChangeUpdateLostjgNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostjgNum4(),
							"更新 +变更+遗失补办", "机构证书", "四年");
				}
				if (monthMap.get("total").getChangeUpdateLostjgNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateLostjgNum5(),
							"更新 +变更+遗失补办", "机构证书", "五年");
				}

				if (monthMap.get("total").getChangeUpdateReplaceqyNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum(),
							"更新 +变更+损坏更换", "企业证书", "一年");
				}
				if (monthMap.get("total").getChangeUpdateReplaceqyNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum2(),
							"更新 +变更+损坏更换", "企业证书", "二年");
				}
				if (monthMap.get("total").getChangeUpdateReplaceqyNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum3(),
							"更新 +变更+损坏更换", "企业证书", "三年");
				}
				if (monthMap.get("total").getChangeUpdateReplaceqyNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum4(),
							"更新 +变更+损坏更换", "企业证书", "四年");
				}
				if (monthMap.get("total").getChangeUpdateReplaceqyNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum5(),
							"更新 +变更+损坏更换", "企业证书", "五年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrQNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum(),
							"更新 +变更+损坏更换", "个人证书（企业）", "一年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrQNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum2(),
							"更新 +变更+损坏更换", "个人证书（企业）", "二年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrQNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum3(),
							"更新 +变更+损坏更换", "个人证书（企业）", "三年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrQNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum4(),
							"更新 +变更+损坏更换", "个人证书（企业）", "四年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrQNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum5(),
							"更新 +变更+损坏更换", "个人证书（企业）", "五年");
				}

				if (monthMap.get("total").getChangeUpdateReplacegrGNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum(),
							"更新 +变更+损坏更换", "个人证书（机构）", "一年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrGNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum2(),
							"更新 +变更+损坏更换", "个人证书（机构）", "二年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrGNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum3(),
							"更新 +变更+损坏更换", "个人证书（机构）", "三年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrGNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum4(),
							"更新 +变更+损坏更换", "个人证书（机构）", "四年");
				}
				if (monthMap.get("total").getChangeUpdateReplacegrGNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum5(),
							"更新 +变更+损坏更换", "个人证书（机构）", "五年");
				}

				if (monthMap.get("total").getChangeUpdateReplacejgNum().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacejgNum(),
							"更新 +变更+损坏更换", "机构证书", "一年");
				}
				if (monthMap.get("total").getChangeUpdateReplacejgNum2().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacejgNum2(),
							"更新 +变更+损坏更换", "机构证书", "二年");
				}
				if (monthMap.get("total").getChangeUpdateReplacejgNum3().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacejgNum3(),
							"更新 +变更+损坏更换", "机构证书", "三年");
				}
				if (monthMap.get("total").getChangeUpdateReplacejgNum4().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacejgNum4(),
							"更新 +变更+损坏更换", "机构证书", "四年");
				}
				if (monthMap.get("total").getChangeUpdateReplacejgNum5().getTotalCount() >= 0) {
					curCol = addHeader2Sheet(sheet, curCol, monthMap.get("total").getChangeUpdateReplacejgNum5(),
							"更新 +变更+损坏更换", "机构证书", "五年");
				}

			}
			// totalColums = curCol; // 全部列数
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, curCol - 1));

			int k = 5;
			//
			for (String key : monthMap.keySet()) {
				curCol = 1;

				if (!"total".equals(key) && !"totalColumn".equals(key)) {
					HSSFRow rown = sheet.createRow(k++);
					rown.createCell(0).setCellValue(key);
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzqyadd1(),
							monthMap.get("total").getXzqyadd1());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzqyadd2(),
							monthMap.get("total").getXzqyadd2());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzqyadd3(),
							monthMap.get("total").getXzqyadd3());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzqyadd4(),
							monthMap.get("total").getXzqyadd4());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzqyadd5(),
							monthMap.get("total").getXzqyadd5());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrQadd1(),
							monthMap.get("total").getXzgrQadd1());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrQadd2(),
							monthMap.get("total").getXzgrQadd2());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrQadd3(),
							monthMap.get("total").getXzgrQadd3());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrQadd4(),
							monthMap.get("total").getXzgrQadd4());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrQadd5(),
							monthMap.get("total").getXzgrQadd5());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrGadd1(),
							monthMap.get("total").getXzgrGadd1());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrGadd2(),
							monthMap.get("total").getXzgrGadd2());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrGadd3(),
							monthMap.get("total").getXzgrGadd3());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrGadd4(),
							monthMap.get("total").getXzgrGadd4());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzgrGadd5(),
							monthMap.get("total").getXzgrGadd5());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzjgadd1(),
							monthMap.get("total").getXzjgadd1());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzjgadd2(),
							monthMap.get("total").getXzjgadd2());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzjgadd3(),
							monthMap.get("total").getXzjgadd3());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzjgadd4(),
							monthMap.get("total").getXzjgadd4());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getXzjgadd5(),
							monthMap.get("total").getXzjgadd5());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxqyadd1(),
							monthMap.get("total").getGxqyadd1());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxqyadd2(),
							monthMap.get("total").getGxqyadd2());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxqyadd3(),
							monthMap.get("total").getGxqyadd3());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxqyadd4(),
							monthMap.get("total").getGxqyadd4());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxqyadd5(),
							monthMap.get("total").getGxqyadd5());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrQadd1(),
							monthMap.get("total").getGxgrQadd1());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrQadd2(),
							monthMap.get("total").getGxgrQadd2());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrQadd3(),
							monthMap.get("total").getGxgrQadd3());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrQadd4(),
							monthMap.get("total").getGxgrQadd4());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrQadd5(),
							monthMap.get("total").getGxgrQadd5());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrGadd1(),
							monthMap.get("total").getGxgrGadd1());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrGadd2(),
							monthMap.get("total").getGxgrGadd2());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrGadd3(),
							monthMap.get("total").getGxgrGadd3());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrGadd4(),
							monthMap.get("total").getGxgrGadd4());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxgrGadd5(),
							monthMap.get("total").getGxgrGadd5());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxjgadd1(),
							monthMap.get("total").getGxjgadd1());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxjgadd2(),
							monthMap.get("total").getGxjgadd2());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxjgadd3(),
							monthMap.get("total").getGxjgadd3());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxjgadd4(),
							monthMap.get("total").getGxjgadd4());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getGxjgadd5(),
							monthMap.get("total").getGxjgadd5());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getLostCerateqy(),
							monthMap.get("total").getLostCerateqy());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getLostCerategrQ(),
							monthMap.get("total").getLostCerategrQ());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getLostCerategrG(),
							monthMap.get("total").getLostCerategrG());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getLostCeratejg(),
							monthMap.get("total").getLostCeratejg());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getDamageCertificateqy(),
							monthMap.get("total").getDamageCertificateqy());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getDamageCertificategrQ(),
							monthMap.get("total").getDamageCertificategrQ());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getDamageCertificategrG(),
							monthMap.get("total").getDamageCertificategrG());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getDamageCertificatejg(),
							monthMap.get("total").getDamageCertificatejg());

					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getModifyNumqy(),
							monthMap.get("total").getModifyNumqy());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getModifyNumgrQ(),
							monthMap.get("total").getModifyNumgrQ());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getModifyNumgrG(),
							monthMap.get("total").getModifyNumgrG());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getModifyNumjg(),
							monthMap.get("total").getModifyNumjg());
					if (multiType) {
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeLostqyNum(),
								monthMap.get("total").getChangeLostqyNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeLostgrQNum(),
								monthMap.get("total").getChangeLostgrQNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeLostgrGNum(),
								monthMap.get("total").getChangeLostgrGNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeLostjgNum(),
								monthMap.get("total").getChangeLostjgNum());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeReplaceqyNum(),
								monthMap.get("total").getChangeReplaceqyNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeReplacegrQNum(),
								monthMap.get("total").getChangeReplacegrQNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeReplacegrGNum(),
								monthMap.get("total").getChangeReplacegrGNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeReplacejgNum(),
								monthMap.get("total").getChangeReplacejgNum());

						// 更新+遗失补办

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostqyNum(),
								monthMap.get("total").getUpdateLostqyNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostqyNum2(),
								monthMap.get("total").getUpdateLostqyNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostqyNum3(),
								monthMap.get("total").getUpdateLostqyNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostqyNum4(),
								monthMap.get("total").getUpdateLostqyNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostqyNum5(),
								monthMap.get("total").getUpdateLostqyNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrQNum(),
								monthMap.get("total").getUpdateLostgrQNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrQNum2(),
								monthMap.get("total").getUpdateLostgrQNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrQNum3(),
								monthMap.get("total").getUpdateLostgrQNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrQNum4(),
								monthMap.get("total").getUpdateLostgrQNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrQNum5(),
								monthMap.get("total").getUpdateLostgrQNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrGNum(),
								monthMap.get("total").getUpdateLostgrGNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrGNum2(),
								monthMap.get("total").getUpdateLostgrGNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrGNum3(),
								monthMap.get("total").getUpdateLostgrGNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrGNum4(),
								monthMap.get("total").getUpdateLostgrGNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostgrGNum5(),
								monthMap.get("total").getUpdateLostgrGNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostjgNum(),
								monthMap.get("total").getUpdateLostjgNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostjgNum2(),
								monthMap.get("total").getUpdateLostjgNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostjgNum3(),
								monthMap.get("total").getUpdateLostjgNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostjgNum4(),
								monthMap.get("total").getUpdateLostjgNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateLostjgNum5(),
								monthMap.get("total").getUpdateLostjgNum5());

						// 更新+损坏更换

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplaceqyNum(),
								monthMap.get("total").getUpdateReplaceqyNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplaceqyNum2(),
								monthMap.get("total").getUpdateReplaceqyNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplaceqyNum3(),
								monthMap.get("total").getUpdateReplaceqyNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplaceqyNum4(),
								monthMap.get("total").getUpdateReplaceqyNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplaceqyNum5(),
								monthMap.get("total").getUpdateReplaceqyNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrQNum(),
								monthMap.get("total").getUpdateReplacegrQNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrQNum2(),
								monthMap.get("total").getUpdateReplacegrQNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrQNum3(),
								monthMap.get("total").getUpdateReplacegrQNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrQNum4(),
								monthMap.get("total").getUpdateReplacegrQNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrQNum5(),
								monthMap.get("total").getUpdateReplacegrQNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrGNum(),
								monthMap.get("total").getUpdateReplacegrGNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrGNum2(),
								monthMap.get("total").getUpdateReplacegrGNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrGNum3(),
								monthMap.get("total").getUpdateReplacegrGNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrGNum4(),
								monthMap.get("total").getUpdateReplacegrGNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacegrGNum5(),
								monthMap.get("total").getUpdateReplacegrGNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacejgNum(),
								monthMap.get("total").getUpdateReplacejgNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacejgNum2(),
								monthMap.get("total").getUpdateReplacejgNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacejgNum3(),
								monthMap.get("total").getUpdateReplacejgNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacejgNum4(),
								monthMap.get("total").getUpdateReplacejgNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateReplacejgNum5(),
								monthMap.get("total").getUpdateReplacejgNum5());

						// 更新+变更

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangeqyNum(),
								monthMap.get("total").getUpdateChangeqyNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangeqyNum2(),
								monthMap.get("total").getUpdateChangeqyNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangeqyNum3(),
								monthMap.get("total").getUpdateChangeqyNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangeqyNum4(),
								monthMap.get("total").getUpdateChangeqyNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangeqyNum5(),
								monthMap.get("total").getUpdateChangeqyNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrQNum(),
								monthMap.get("total").getUpdateChangegrQNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrQNum2(),
								monthMap.get("total").getUpdateChangegrQNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrQNum3(),
								monthMap.get("total").getUpdateChangegrQNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrQNum4(),
								monthMap.get("total").getUpdateChangegrQNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrQNum5(),
								monthMap.get("total").getUpdateChangegrQNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrGNum(),
								monthMap.get("total").getUpdateChangegrGNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrGNum2(),
								monthMap.get("total").getUpdateChangegrGNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrGNum3(),
								monthMap.get("total").getUpdateChangegrGNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrGNum4(),
								monthMap.get("total").getUpdateChangegrGNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangegrGNum5(),
								monthMap.get("total").getUpdateChangegrGNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangejgNum(),
								monthMap.get("total").getUpdateChangejgNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangejgNum2(),
								monthMap.get("total").getUpdateChangejgNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangejgNum3(),
								monthMap.get("total").getUpdateChangejgNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangejgNum4(),
								monthMap.get("total").getUpdateChangejgNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getUpdateChangejgNum5(),
								monthMap.get("total").getUpdateChangejgNum5());

						// 更新+变更+遗失补办

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostqyNum(),
								monthMap.get("total").getChangeUpdateLostqyNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostqyNum2(),
								monthMap.get("total").getChangeUpdateLostqyNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostqyNum3(),
								monthMap.get("total").getChangeUpdateLostqyNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostqyNum4(),
								monthMap.get("total").getChangeUpdateLostqyNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostqyNum5(),
								monthMap.get("total").getChangeUpdateLostqyNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrQNum(),
								monthMap.get("total").getChangeUpdateLostgrQNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrQNum2(),
								monthMap.get("total").getChangeUpdateLostgrQNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrQNum3(),
								monthMap.get("total").getChangeUpdateLostgrQNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrQNum4(),
								monthMap.get("total").getChangeUpdateLostgrQNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrQNum5(),
								monthMap.get("total").getChangeUpdateLostgrQNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrGNum(),
								monthMap.get("total").getChangeUpdateLostgrGNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrGNum2(),
								monthMap.get("total").getChangeUpdateLostgrGNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrGNum3(),
								monthMap.get("total").getChangeUpdateLostgrGNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrGNum4(),
								monthMap.get("total").getChangeUpdateLostgrGNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostgrGNum5(),
								monthMap.get("total").getChangeUpdateLostgrGNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostjgNum(),
								monthMap.get("total").getChangeUpdateLostjgNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostjgNum2(),
								monthMap.get("total").getChangeUpdateLostjgNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostjgNum3(),
								monthMap.get("total").getChangeUpdateLostjgNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostjgNum4(),
								monthMap.get("total").getChangeUpdateLostjgNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateLostjgNum5(),
								monthMap.get("total").getChangeUpdateLostjgNum5());

						// 更新+变更+损坏更换

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplaceqyNum(),
								monthMap.get("total").getChangeUpdateReplaceqyNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplaceqyNum2(),
								monthMap.get("total").getChangeUpdateReplaceqyNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplaceqyNum3(),
								monthMap.get("total").getChangeUpdateReplaceqyNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplaceqyNum4(),
								monthMap.get("total").getChangeUpdateReplaceqyNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplaceqyNum5(),
								monthMap.get("total").getChangeUpdateReplaceqyNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrQNum(),
								monthMap.get("total").getChangeUpdateReplacegrQNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrQNum2(),
								monthMap.get("total").getChangeUpdateReplacegrQNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrQNum3(),
								monthMap.get("total").getChangeUpdateReplacegrQNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrQNum4(),
								monthMap.get("total").getChangeUpdateReplacegrQNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrQNum5(),
								monthMap.get("total").getChangeUpdateReplacegrQNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrGNum(),
								monthMap.get("total").getChangeUpdateReplacegrGNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrGNum2(),
								monthMap.get("total").getChangeUpdateReplacegrGNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrGNum3(),
								monthMap.get("total").getChangeUpdateReplacegrGNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrGNum4(),
								monthMap.get("total").getChangeUpdateReplacegrGNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacegrGNum5(),
								monthMap.get("total").getChangeUpdateReplacegrGNum5());

						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacejgNum(),
								monthMap.get("total").getChangeUpdateReplacejgNum());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacejgNum2(),
								monthMap.get("total").getChangeUpdateReplacejgNum2());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacejgNum3(),
								monthMap.get("total").getChangeUpdateReplacejgNum3());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacejgNum4(),
								monthMap.get("total").getChangeUpdateReplacejgNum4());
						curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeUpdateReplacejgNum5(),
								monthMap.get("total").getChangeUpdateReplacejgNum5());

					}

				}
			}
			HSSFRow rown = sheet.createRow(4 + monthMap.size());
			rown.createCell(0).setCellValue("总计");
			curCol = 1;
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzqyadd1());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzqyadd2());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzqyadd3());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzqyadd4());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzqyadd5());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrQadd1());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrQadd2());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrQadd3());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrQadd4());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrQadd5());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrGadd1());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrGadd2());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrGadd3());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrGadd4());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzgrGadd5());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzjgadd1());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzjgadd2());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzjgadd3());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzjgadd4());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getXzjgadd5());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxqyadd1());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxqyadd2());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxqyadd3());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxqyadd4());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxqyadd5());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrQadd1());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrQadd2());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrQadd3());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrQadd4());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrQadd5());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrGadd1());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrGadd2());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrGadd3());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrGadd4());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxgrGadd5());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxjgadd1());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxjgadd2());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxjgadd3());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxjgadd4());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getGxjgadd5());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getLostCerateqy());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getLostCerategrQ());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getLostCerategrG());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getLostCeratejg());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getDamageCertificateqy());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getDamageCertificategrQ());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getDamageCertificategrG());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getDamageCertificatejg());

			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getModifyNumqy());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getModifyNumgrQ());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getModifyNumgrG());
			curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getModifyNumjg());
			if (multiType) {
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeLostqyNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeLostgrQNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeLostgrGNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeLostjgNum());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeReplaceqyNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeReplacegrQNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeReplacegrGNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeReplacejgNum());

				// 更新+遗失补办

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostqyNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostqyNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostqyNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostqyNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostqyNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrQNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrQNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrQNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrQNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrQNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrGNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrGNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrGNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrGNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostgrGNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostjgNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostjgNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostjgNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostjgNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateLostjgNum5());

				// 更新+损坏更换

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplaceqyNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplaceqyNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplaceqyNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplaceqyNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplaceqyNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrQNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrQNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrQNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrQNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrQNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrGNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrGNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrGNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrGNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacegrGNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacejgNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacejgNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacejgNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacejgNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateReplacejgNum5());

				// 更新+变更

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangeqyNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangeqyNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangeqyNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangeqyNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangeqyNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrQNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrQNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrQNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrQNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrQNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrGNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrGNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrGNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrGNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangegrGNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangejgNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangejgNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangejgNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangejgNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getUpdateChangejgNum5());

				// 更新+变更+遗失补办

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostqyNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostqyNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostqyNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostqyNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostqyNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrQNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrQNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrQNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrQNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrQNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrGNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrGNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrGNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrGNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostgrGNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostjgNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostjgNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostjgNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostjgNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateLostjgNum5());

				// 更新+变更+损坏更换

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplaceqyNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrQNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacegrGNum5());

				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacejgNum());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacejgNum2());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacejgNum3());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacejgNum4());
				curCol = addTotal2Sheet(sheet, rown, curCol, monthMap.get("total").getChangeUpdateReplacejgNum5());
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition", "attachment; filename=certificateSettlementStatistics.xls");
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

	public int addData2Sheet(HSSFSheet sheet, HSSFRow curRow, int curCol, CertificatePayMethodDetails data,
			CertificatePayMethodDetails total) {
		if (data == null || total.getTotalCount() == 0) {
			return curCol;
		}
		Map<String, String> payMethods = total.getMethods();
		// HSSFRow row4 = sheet.getRow(4);
		for (String method : payMethods.keySet()) {
			// HSSFRow row4 = sheet.createRow(4);

			// row4.createCell(curCol).setCellValue(payMethods.get(method));
			// HSSFRow curRow = sheet.createRow(row);
			switch (method) {
			case "1":
				curRow.createCell(curCol).setCellValue(data.getMethodPosCount());
				break;
			case "10":
				curRow.createCell(curCol).setCellValue(data.getMethodMoneyCount());
				break;
			case "11":
				curRow.createCell(curCol).setCellValue(data.getMethodMoneyAndPosCount());
				break;
			case "100":
				curRow.createCell(curCol).setCellValue(data.getMethodBankCount());
				break;
			case "101":
				curRow.createCell(curCol).setCellValue(data.getMethodBankAndPosCount());
				break;
			case "110":
				curRow.createCell(curCol).setCellValue(data.getMethodBankAndMoneyCount());
				break;
			case "1000":
				curRow.createCell(curCol).setCellValue(data.getMethodAlipayCount());
				break;
			case "1001":
				curRow.createCell(curCol).setCellValue(data.getMethodAlipayAndPosCount());
				break;
			case "1010":
				curRow.createCell(curCol).setCellValue(data.getMethodAlipayAndMoneyCount());
				break;
			case "1100":
				curRow.createCell(curCol).setCellValue(data.getMethodAlipayAndBankCount());
				break;
			default:
				break;
			}
			curCol++;
		}
		return curCol;
	}

	public int addHeader2Sheet(HSSFSheet sheet, int curCol, CertificatePayMethodDetails total, String row1Title,
			String row2Title, String row3Title) {
		if (total.getTotalCount() == 0) {
			return curCol;
		}
		Map<String, String> payMethods = total.getMethods();
		HSSFRow row1 = sheet.getRow(1);
		HSSFRow row2 = sheet.getRow(2);
		HSSFRow row3 = sheet.getRow(3);
		HSSFRow row4 = sheet.getRow(4);
		if (row4 == null) {
			row4 = sheet.createRow(4);
		}
		row1.createCell(curCol).setCellValue(row1Title);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, curCol, curCol + payMethods.size() - 1));
		row2.createCell(curCol).setCellValue(row2Title);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, curCol, curCol + payMethods.size() - 1));
		row3.createCell(curCol).setCellValue(row3Title);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, curCol, curCol + payMethods.size() - 1));
		for (String method : payMethods.keySet()) {
			row4.createCell(curCol).setCellValue(payMethods.get(method));
			curCol++;
		}
		return curCol;
	}

	public int addTotal2Sheet(HSSFSheet sheet, HSSFRow curRow, int curCol, CertificatePayMethodDetails total) {
		if (total.getTotalCount() == 0) {
			return curCol;
		}
		Map<String, String> payMethods = total.getMethods();
		curRow.createCell(curCol).setCellValue(total.getTotalCount());
		sheet.addMergedRegion(
				new CellRangeAddress(curRow.getRowNum(), curRow.getRowNum(), curCol, curCol + payMethods.size() - 1));
		curCol += payMethods.size();
		return curCol;
	}

}
