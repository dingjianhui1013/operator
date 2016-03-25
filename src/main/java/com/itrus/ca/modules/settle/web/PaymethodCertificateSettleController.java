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

			// if(monthMap.get("total").getXzqyadd1().getTotalCount() ==0){
			// sheet.setColumnHidden(1, true);
			// }else{
			//
			// }
			// if(monthMap.get("total").getXzqyadd2()==0){
			// sheet.setColumnHidden(2, true);
			// }
			// if(monthMap.get("total").getXzqyadd3()==0){
			// sheet.setColumnHidden(3, true);
			// }
			// if(monthMap.get("total").getXzqyadd4()==0){
			// sheet.setColumnHidden(4, true);
			// }
			// if(monthMap.get("total").getXzqyadd5()==0){
			// sheet.setColumnHidden(5, true);
			// }
			// if(monthMap.get("total").getXzgrQadd1()==0){
			// sheet.setColumnHidden(6, true);
			// }
			// if(monthMap.get("total").getXzgrQadd2()==0){
			// sheet.setColumnHidden(7, true);
			// }
			// if(monthMap.get("total").getXzgrQadd3()==0){
			// sheet.setColumnHidden(8, true);
			// }
			// if(monthMap.get("total").getXzgrQadd4()==0){
			// sheet.setColumnHidden(9, true);
			// }
			// if(monthMap.get("total").getXzgrQadd5()==0){
			// sheet.setColumnHidden(10, true);
			// }
			// if(monthMap.get("total").getXzgrGadd1()==0){
			// sheet.setColumnHidden(11, true);
			// }
			// if(monthMap.get("total").getXzgrGadd2()==0){
			// sheet.setColumnHidden(12, true);
			// }
			// if(monthMap.get("total").getXzgrGadd3()==0){
			// sheet.setColumnHidden(13, true);
			// }
			// if(monthMap.get("total").getXzgrGadd4()==0){
			// sheet.setColumnHidden(14, true);
			// }
			// if(monthMap.get("total").getXzgrGadd5()==0){
			// sheet.setColumnHidden(15, true);
			// }
			// if(monthMap.get("total").getXzjgadd1()==0){
			// sheet.setColumnHidden(16, true);
			// }
			// if(monthMap.get("total").getXzjgadd2()==0){
			// sheet.setColumnHidden(17, true);
			// }
			// if(monthMap.get("total").getXzjgadd3()==0){
			// sheet.setColumnHidden(18, true);
			// }
			// if(monthMap.get("total").getXzjgadd4()==0){
			// sheet.setColumnHidden(19, true);
			// }
			// if(monthMap.get("total").getXzjgadd5()==0){
			// sheet.setColumnHidden(20, true);
			// }
			//
			// if(monthMap.get("total").getGxqyadd1()==0){
			// sheet.setColumnHidden(21, true);
			// }
			// if(monthMap.get("total").getGxqyadd2()==0){
			// sheet.setColumnHidden(22, true);
			// }
			// if(monthMap.get("total").getGxqyadd3()==0){
			// sheet.setColumnHidden(23, true);
			// }
			// if(monthMap.get("total").getGxqyadd4()==0){
			// sheet.setColumnHidden(24, true);
			// }
			// if(monthMap.get("total").getGxqyadd5()==0){
			// sheet.setColumnHidden(25, true);
			// }
			// if(monthMap.get("total").getGxgrQadd1()==0){
			// sheet.setColumnHidden(26, true);
			// }
			// if(monthMap.get("total").getGxgrQadd2()==0){
			// sheet.setColumnHidden(27, true);
			// }
			// if(monthMap.get("total").getGxgrQadd3()==0){
			// sheet.setColumnHidden(28, true);
			// }
			// if(monthMap.get("total").getGxgrQadd4()==0){
			// sheet.setColumnHidden(29, true);
			// }
			// if(monthMap.get("total").getGxgrQadd5()==0){
			// sheet.setColumnHidden(30, true);
			// }
			// if(monthMap.get("total").getGxgrGadd1()==0){
			// sheet.setColumnHidden(31, true);
			// }
			// if(monthMap.get("total").getGxgrGadd2()==0){
			// sheet.setColumnHidden(32, true);
			// }
			// if(monthMap.get("total").getGxgrGadd3()==0){
			// sheet.setColumnHidden(33, true);
			// }
			// if(monthMap.get("total").getGxgrGadd4()==0){
			// sheet.setColumnHidden(34, true);
			// }
			// if(monthMap.get("total").getGxgrGadd5()==0){
			// sheet.setColumnHidden(35, true);
			// }
			// if(monthMap.get("total").getGxjgadd1()==0){
			// sheet.setColumnHidden(36, true);
			// }
			// if(monthMap.get("total").getGxjgadd2()==0){
			// sheet.setColumnHidden(37, true);
			// }
			// if(monthMap.get("total").getGxjgadd3()==0){
			// sheet.setColumnHidden(38, true);
			// }
			// if(monthMap.get("total").getGxjgadd4()==0){
			// sheet.setColumnHidden(39, true);
			// }
			// if(monthMap.get("total").getGxjgadd5()==0){
			// sheet.setColumnHidden(40, true);
			// }
			// //遗失补办
			//
			// if(monthMap.get("total").getLostCerateqy()==0){
			// sheet.setColumnHidden(41, true);
			// }
			// if(monthMap.get("total").getLostCerategrQ()==0){
			// sheet.setColumnHidden(42, true);
			// }
			// if(monthMap.get("total").getLostCerategrG()==0){
			// sheet.setColumnHidden(43, true);
			// }
			// if(monthMap.get("total").getLostCeratejg()==0){
			// sheet.setColumnHidden(44, true);
			// }
			//
			// //损坏更换 -->
			//
			// if(monthMap.get("total").getDamageCertificateqy()==0){
			// sheet.setColumnHidden(45, true);
			// }
			// if(monthMap.get("total").getDamageCertificategrQ()==0){
			// sheet.setColumnHidden(46, true);
			// }
			// if(monthMap.get("total").getDamageCertificategrG()==0){
			// sheet.setColumnHidden(47, true);
			// }
			// if(monthMap.get("total").getDamageCertificatejg()==0){
			// sheet.setColumnHidden(48, true);
			// }
			// //<=--变更 -->
			//
			// if(monthMap.get("total").getModifyNumqy()==0){
			// sheet.setColumnHidden(49, true);
			// }
			// if(monthMap.get("total").getModifyNumqy()==0){
			// sheet.setColumnHidden(50, true);
			// }
			// if(monthMap.get("total").getModifyNumqy()==0){
			// sheet.setColumnHidden(51, true);
			// }
			// if(monthMap.get("total").getModifyNumqy()==0){
			// sheet.setColumnHidden(52, true);
			// }
			// //<=--变更+遗失补办 -->
			//
			// if(monthMap.get("total").getChangeLostqyNum()==0){
			// sheet.setColumnHidden(53, true);
			// }
			// if(monthMap.get("total").getChangeLostgrQNum()==0){
			// sheet.setColumnHidden(54, true);
			// }
			// if(monthMap.get("total").getChangeLostgrGNum()==0){
			// sheet.setColumnHidden(55, true);
			// }
			// if(monthMap.get("total").getChangeLostjgNum()==0){
			// sheet.setColumnHidden(56, true);
			// }
			// //<=--变更+损坏更换 -->
			//
			// if(monthMap.get("total").getChangeReplaceqyNum()==0){
			// sheet.setColumnHidden(57, true);
			// }
			// if(monthMap.get("total").getChangeReplacegrQNum()==0){
			// sheet.setColumnHidden(58, true);
			// }
			// if(monthMap.get("total").getChangeReplacegrGNum()==0){
			// sheet.setColumnHidden(59, true);
			// }
			// if(monthMap.get("total").getChangeReplacejgNum()==0){
			// sheet.setColumnHidden(60, true);
			// }
			// //<=-- 更新+遗失补办 -->
			//
			// if(monthMap.get("total").getUpdateLostqyNum()==0){
			// sheet.setColumnHidden(61, true);
			// }
			// if(monthMap.get("total").getUpdateLostqyNum2()==0){
			// sheet.setColumnHidden(62, true);
			// }
			// if(monthMap.get("total").getUpdateLostqyNum3()==0){
			// sheet.setColumnHidden(63, true);
			// }
			// if(monthMap.get("total").getUpdateLostqyNum4()==0){
			// sheet.setColumnHidden(64, true);
			// }
			// if(monthMap.get("total").getUpdateLostqyNum5()==0){
			// sheet.setColumnHidden(65, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrQNum()==0){
			// sheet.setColumnHidden(66, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrQNum2()==0){
			// sheet.setColumnHidden(67, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrQNum3()==0){
			// sheet.setColumnHidden(68, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrQNum4()==0){
			// sheet.setColumnHidden(69, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrQNum5()==0){
			// sheet.setColumnHidden(70, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrGNum()==0){
			// sheet.setColumnHidden(71, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrGNum2()==0){
			// sheet.setColumnHidden(72, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrGNum3()==0){
			// sheet.setColumnHidden(73, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrGNum4()==0){
			// sheet.setColumnHidden(74, true);
			// }
			// if(monthMap.get("total").getUpdateLostgrGNum5()==0){
			// sheet.setColumnHidden(75, true);
			// }
			// if(monthMap.get("total").getUpdateLostjgNum()==0){
			// sheet.setColumnHidden(76, true);
			// }
			// if(monthMap.get("total").getUpdateLostjgNum2()==0){
			// sheet.setColumnHidden(77, true);
			// }
			// if(monthMap.get("total").getUpdateLostjgNum3()==0){
			// sheet.setColumnHidden(78, true);
			// }
			// if(monthMap.get("total").getUpdateLostjgNum4()==0){
			// sheet.setColumnHidden(79, true);
			// }
			// if(monthMap.get("total").getUpdateLostjgNum5()==0){
			// sheet.setColumnHidden(80, true);
			// }
			// //<=-- 更新+损坏更换 -->
			//
			// if(monthMap.get("total").getUpdateReplaceqyNum()==0){
			// sheet.setColumnHidden(81, true);
			// }
			// if(monthMap.get("total").getUpdateReplaceqyNum2()==0){
			// sheet.setColumnHidden(82, true);
			// }
			// if(monthMap.get("total").getUpdateReplaceqyNum3()==0){
			// sheet.setColumnHidden(83, true);
			// }
			// if(monthMap.get("total").getUpdateReplaceqyNum4()==0){
			// sheet.setColumnHidden(84, true);
			// }
			// if(monthMap.get("total").getUpdateReplaceqyNum5()==0){
			// sheet.setColumnHidden(85, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrQNum()==0){
			// sheet.setColumnHidden(86, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrQNum2()==0){
			// sheet.setColumnHidden(87, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrQNum3()==0){
			// sheet.setColumnHidden(88, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrQNum4()==0){
			// sheet.setColumnHidden(89, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrQNum5()==0){
			// sheet.setColumnHidden(90, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrGNum()==0){
			// sheet.setColumnHidden(91, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrGNum2()==0){
			// sheet.setColumnHidden(92, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrGNum3()==0){
			// sheet.setColumnHidden(93, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrGNum4()==0){
			// sheet.setColumnHidden(94, true);
			// }
			// if(monthMap.get("total").getUpdateReplacegrGNum5()==0){
			// sheet.setColumnHidden(95, true);
			// }
			// if(monthMap.get("total").getUpdateReplacejgNum()==0){
			// sheet.setColumnHidden(96, true);
			// }
			// if(monthMap.get("total").getUpdateReplacejgNum2()==0){
			// sheet.setColumnHidden(97, true);
			// }
			// if(monthMap.get("total").getUpdateReplacejgNum3()==0){
			// sheet.setColumnHidden(98, true);
			// }
			// if(monthMap.get("total").getUpdateReplacejgNum4()==0){
			// sheet.setColumnHidden(99, true);
			// }
			// if(monthMap.get("total").getUpdateReplacejgNum5()==0){
			// sheet.setColumnHidden(100, true);
			// }
			// //<!-- 更新 +变更-->
			// if(monthMap.get("total").getUpdateChangeqyNum()==0){
			// sheet.setColumnHidden(101, true);
			// }
			// if(monthMap.get("total").getUpdateChangeqyNum2()==0){
			// sheet.setColumnHidden(102, true);
			// }
			// if(monthMap.get("total").getUpdateChangeqyNum3()==0){
			// sheet.setColumnHidden(103, true);
			// }
			// if(monthMap.get("total").getUpdateChangeqyNum4()==0){
			// sheet.setColumnHidden(104, true);
			// }
			// if(monthMap.get("total").getUpdateChangeqyNum5()==0){
			// sheet.setColumnHidden(105, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrQNum()==0){
			// sheet.setColumnHidden(106, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrQNum2()==0){
			// sheet.setColumnHidden(107, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrQNum3()==0){
			// sheet.setColumnHidden(108, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrQNum4()==0){
			// sheet.setColumnHidden(109, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrQNum5()==0){
			// sheet.setColumnHidden(110, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrGNum()==0){
			// sheet.setColumnHidden(111, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrGNum2()==0){
			// sheet.setColumnHidden(112, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrGNum3()==0){
			// sheet.setColumnHidden(113, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrGNum4()==0){
			// sheet.setColumnHidden(114, true);
			// }
			// if(monthMap.get("total").getUpdateChangegrGNum5()==0){
			// sheet.setColumnHidden(115, true);
			// }
			// if(monthMap.get("total").getUpdateChangejgNum()==0){
			// sheet.setColumnHidden(116, true);
			// }
			// if(monthMap.get("total").getUpdateChangejgNum2()==0){
			// sheet.setColumnHidden(117, true);
			// }
			// if(monthMap.get("total").getUpdateChangejgNum3()==0){
			// sheet.setColumnHidden(118, true);
			// }
			// if(monthMap.get("total").getUpdateChangejgNum4()==0){
			// sheet.setColumnHidden(119, true);
			// }
			// if(monthMap.get("total").getUpdateChangejgNum5()==0){
			// sheet.setColumnHidden(120, true);
			// }
			//
			// //<!-- 更新 +变更+遗失补办-->
			// if(monthMap.get("total").getChangeUpdateLostqyNum()==0){
			// sheet.setColumnHidden(121, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostqyNum2()==0){
			// sheet.setColumnHidden(122, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostqyNum3()==0){
			// sheet.setColumnHidden(123, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostqyNum4()==0){
			// sheet.setColumnHidden(124, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostqyNum5()==0){
			// sheet.setColumnHidden(125, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrQNum()==0){
			// sheet.setColumnHidden(126, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrQNum2()==0){
			// sheet.setColumnHidden(127, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrQNum3()==0){
			// sheet.setColumnHidden(128, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrQNum4()==0){
			// sheet.setColumnHidden(129, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrQNum5()==0){
			// sheet.setColumnHidden(130, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrGNum()==0){
			// sheet.setColumnHidden(131, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrGNum2()==0){
			// sheet.setColumnHidden(132, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrGNum3()==0){
			// sheet.setColumnHidden(133, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrGNum4()==0){
			// sheet.setColumnHidden(134, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostgrGNum5()==0){
			// sheet.setColumnHidden(135, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostjgNum()==0){
			// sheet.setColumnHidden(136, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostjgNum2()==0){
			// sheet.setColumnHidden(137, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostjgNum3()==0){
			// sheet.setColumnHidden(138, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostjgNum4()==0){
			// sheet.setColumnHidden(139, true);
			// }
			// if(monthMap.get("total").getChangeUpdateLostjgNum5()==0){
			// sheet.setColumnHidden(140, true);
			// }
			//
			// //<!-- 更新 +变更+损坏更换-->
			// if(monthMap.get("total").getChangeUpdateReplaceqyNum()==0){
			// sheet.setColumnHidden(141, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplaceqyNum2()==0){
			// sheet.setColumnHidden(142, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplaceqyNum3()==0){
			// sheet.setColumnHidden(143, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplaceqyNum4()==0){
			// sheet.setColumnHidden(144, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplaceqyNum5()==0){
			// sheet.setColumnHidden(145, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrQNum()==0){
			// sheet.setColumnHidden(146, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrQNum2()==0){
			// sheet.setColumnHidden(147, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrQNum3()==0){
			// sheet.setColumnHidden(148, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrQNum4()==0){
			// sheet.setColumnHidden(149, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrQNum5()==0){
			// sheet.setColumnHidden(150, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrGNum()==0){
			// sheet.setColumnHidden(151, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrGNum2()==0){
			// sheet.setColumnHidden(152, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrGNum3()==0){
			// sheet.setColumnHidden(153, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrGNum4()==0){
			// sheet.setColumnHidden(154, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacegrGNum5()==0){
			// sheet.setColumnHidden(155, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacejgNum()==0){
			// sheet.setColumnHidden(156, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacejgNum2()==0){
			// sheet.setColumnHidden(157, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacejgNum3()==0){
			// sheet.setColumnHidden(158, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacejgNum4()==0){
			// sheet.setColumnHidden(159, true);
			// }
			// if(monthMap.get("total").getChangeUpdateReplacejgNum5()==0){
			// sheet.setColumnHidden(160, true);
			// }
			// if(multiType==false){
			// sheet.setColumnHidden(53, true);
			// sheet.setColumnHidden(54, true);
			// sheet.setColumnHidden(55, true);
			// sheet.setColumnHidden(56, true);
			// sheet.setColumnHidden(57, true);
			// sheet.setColumnHidden(58, true);
			// sheet.setColumnHidden(59, true);
			// sheet.setColumnHidden(60, true);
			// sheet.setColumnHidden(61, true);
			// sheet.setColumnHidden(62, true);
			// sheet.setColumnHidden(63, true);
			// sheet.setColumnHidden(64, true);
			// sheet.setColumnHidden(65, true);
			// sheet.setColumnHidden(66, true);
			// sheet.setColumnHidden(67, true);
			// sheet.setColumnHidden(68, true);
			// sheet.setColumnHidden(69, true);
			// sheet.setColumnHidden(70, true);
			// sheet.setColumnHidden(71, true);
			// sheet.setColumnHidden(72, true);
			// sheet.setColumnHidden(73, true);
			// sheet.setColumnHidden(74, true);
			// sheet.setColumnHidden(75, true);
			// sheet.setColumnHidden(76, true);
			// sheet.setColumnHidden(77, true);
			// sheet.setColumnHidden(78, true);
			// sheet.setColumnHidden(79, true);
			// sheet.setColumnHidden(80, true);
			// sheet.setColumnHidden(81, true);
			// sheet.setColumnHidden(82, true);
			// sheet.setColumnHidden(83, true);
			// sheet.setColumnHidden(84, true);
			// sheet.setColumnHidden(85, true);
			// sheet.setColumnHidden(86, true);
			// sheet.setColumnHidden(87, true);
			// sheet.setColumnHidden(88, true);
			// sheet.setColumnHidden(89, true);
			// sheet.setColumnHidden(90, true);
			// sheet.setColumnHidden(91, true);
			// sheet.setColumnHidden(92, true);
			// sheet.setColumnHidden(93, true);
			// sheet.setColumnHidden(94, true);
			// sheet.setColumnHidden(95, true);
			// sheet.setColumnHidden(96, true);
			// sheet.setColumnHidden(97, true);
			// sheet.setColumnHidden(98, true);
			// sheet.setColumnHidden(99, true);
			// sheet.setColumnHidden(100, true);
			// sheet.setColumnHidden(101, true);
			// sheet.setColumnHidden(102, true);
			// sheet.setColumnHidden(103, true);
			// sheet.setColumnHidden(104, true);
			// sheet.setColumnHidden(105, true);
			// sheet.setColumnHidden(106,true);
			// sheet.setColumnHidden(107, true);
			// sheet.setColumnHidden(108, true);
			// sheet.setColumnHidden(109, true);
			// sheet.setColumnHidden(110, true);
			// sheet.setColumnHidden(111, true);
			// sheet.setColumnHidden(112, true);
			// sheet.setColumnHidden(113, true);
			// sheet.setColumnHidden(114, true);
			// sheet.setColumnHidden(115, true);
			// sheet.setColumnHidden(116, true);
			// sheet.setColumnHidden(117, true);
			// sheet.setColumnHidden(118, true);
			// sheet.setColumnHidden(119, true);
			// sheet.setColumnHidden(120, true);
			// sheet.setColumnHidden(121, true);
			// sheet.setColumnHidden(122, true);
			// sheet.setColumnHidden(123, true);
			// sheet.setColumnHidden(124, true);
			// sheet.setColumnHidden(125, true);
			// sheet.setColumnHidden(126, true);
			// sheet.setColumnHidden(127, true);
			// sheet.setColumnHidden(128, true);
			// sheet.setColumnHidden(129, true);
			// sheet.setColumnHidden(130, true);
			// sheet.setColumnHidden(131, true);
			// sheet.setColumnHidden(132, true);
			// sheet.setColumnHidden(133, true);
			// sheet.setColumnHidden(134, true);
			// sheet.setColumnHidden(135, true);
			// sheet.setColumnHidden(136, true);
			// sheet.setColumnHidden(137, true);
			// sheet.setColumnHidden(138, true);
			// sheet.setColumnHidden(139, true);
			// sheet.setColumnHidden(140, true);
			// sheet.setColumnHidden(141, true);
			// sheet.setColumnHidden(142, true);
			// sheet.setColumnHidden(143, true);
			// sheet.setColumnHidden(144, true);
			// sheet.setColumnHidden(145, true);
			// sheet.setColumnHidden(146, true);
			// sheet.setColumnHidden(147, true);
			// sheet.setColumnHidden(148, true);
			// sheet.setColumnHidden(149, true);
			// sheet.setColumnHidden(150, true);
			// sheet.setColumnHidden(151, true);
			// sheet.setColumnHidden(152, true);
			// sheet.setColumnHidden(153, true);
			// sheet.setColumnHidden(154, true);
			// sheet.setColumnHidden(155, true);
			// sheet.setColumnHidden(156, true);
			// sheet.setColumnHidden(157, true);
			// sheet.setColumnHidden(158, true);
			// sheet.setColumnHidden(159, true);
			// sheet.setColumnHidden(160, true);
			//
			// }
			row1.createCell(0).setCellValue("月份");

			row1.createCell(1).setCellValue("新增");
			sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 1, (short) 20));
			row1.createCell(21).setCellValue("更新");
			sheet.addMergedRegion(new Region(1, (short) 21, 1, (short) 40));
			row1.createCell(41).setCellValue("遗失补办");
			sheet.addMergedRegion(new Region(1, (short) 41, 1, (short) 44));
			row1.createCell(45).setCellValue("损坏更换");
			sheet.addMergedRegion(new Region(1, (short) 45, 1, (short) 48));
			row1.createCell(49).setCellValue("信息变更");
			sheet.addMergedRegion(new Region(1, (short) 49, 1, (short) 52));
			row1.createCell(53).setCellValue("信息变更+遗失补办");
			sheet.addMergedRegion(new Region(1, (short) 53, 1, (short) 56));
			row1.createCell(57).setCellValue("信息变更+损坏更换");
			sheet.addMergedRegion(new Region(1, (short) 57, 1, (short) 60));
			row1.createCell(61).setCellValue("信息变更+更新");
			sheet.addMergedRegion(new Region(1, (short) 61, 1, (short) 80));
			row1.createCell(81).setCellValue("更新+遗失补办");
			sheet.addMergedRegion(new Region(1, (short) 81, 1, (short) 100));
			row1.createCell(101).setCellValue("更新+损坏更换");
			sheet.addMergedRegion(new Region(1, (short) 101, 1, (short) 120));
			row1.createCell(121).setCellValue("更新+变更+遗失补办");
			sheet.addMergedRegion(new Region(1, (short) 121, 1, (short) 140));
			row1.createCell(141).setCellValue("更新+变更+损坏更换");
			sheet.addMergedRegion(new Region(1, (short) 141, 1, (short) 160));

			row2.createCell(1).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 1, 2, (short) 5));
			row2.createCell(6).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 6, 2, (short) 10));
			row2.createCell(11).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 11, 2, (short) 15));
			row2.createCell(16).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 16, 2, (short) 20));

			row2.createCell(21).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 21, 2, (short) 25));
			row2.createCell(26).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 26, 2, (short) 30));
			row2.createCell(31).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 31, 2, (short) 35));
			row2.createCell(36).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 36, 2, (short) 40));

			row2.createCell(41).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 41, 3, (short) 41));
			row2.createCell(42).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 42, 3, (short) 42));
			row2.createCell(43).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 43, 3, (short) 43));
			row2.createCell(44).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 44, 3, (short) 44));

			row2.createCell(45).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 45, 3, (short) 45));
			row2.createCell(46).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 46, 3, (short) 46));
			row2.createCell(47).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 47, 3, (short) 47));
			row2.createCell(48).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 48, 3, (short) 48));

			row2.createCell(49).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 49, 3, (short) 49));
			row2.createCell(50).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 50, 3, (short) 50));
			row2.createCell(51).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 51, 3, (short) 51));
			row2.createCell(52).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 52, 3, (short) 52));

			row2.createCell(53).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 53, 3, (short) 53));
			row2.createCell(54).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 54, 3, (short) 54));
			row2.createCell(55).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 55, 3, (short) 55));
			row2.createCell(56).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 56, 3, (short) 56));

			row2.createCell(57).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 57, 3, (short) 57));
			row2.createCell(58).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 58, 3, (short) 58));
			row2.createCell(59).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 59, 3, (short) 59));
			row2.createCell(60).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 60, 3, (short) 60));

			row2.createCell(61).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 61, 2, (short) 65));
			row2.createCell(66).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 66, 2, (short) 70));
			row2.createCell(71).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 71, 2, (short) 75));
			row2.createCell(76).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 76, 2, (short) 80));

			row2.createCell(81).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 81, 2, (short) 85));
			row2.createCell(86).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 86, 2, (short) 90));
			row2.createCell(91).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 91, 2, (short) 95));
			row2.createCell(96).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 96, 2, (short) 100));

			row2.createCell(101).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 101, 2, (short) 105));
			row2.createCell(106).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 106, 2, (short) 110));
			row2.createCell(111).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 111, 2, (short) 115));
			row2.createCell(116).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 116, 2, (short) 120));

			row2.createCell(121).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 121, 2, (short) 125));
			row2.createCell(126).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 126, 2, (short) 130));
			row2.createCell(131).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 131, 2, (short) 135));
			row2.createCell(136).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 136, 2, (short) 140));

			row2.createCell(141).setCellValue("企业证书");
			sheet.addMergedRegion(new Region(2, (short) 141, 2, (short) 145));
			row2.createCell(146).setCellValue("个人证书（企业）");
			sheet.addMergedRegion(new Region(2, (short) 146, 2, (short) 150));
			row2.createCell(151).setCellValue("个人证书（机构）");
			sheet.addMergedRegion(new Region(2, (short) 151, 2, (short) 155));
			row2.createCell(156).setCellValue("机构证书");
			sheet.addMergedRegion(new Region(2, (short) 156, 2, (short) 160));

			row3.createCell(1).setCellValue("一年");
			row3.createCell(2).setCellValue("二年");
			row3.createCell(3).setCellValue("三年");
			row3.createCell(4).setCellValue("四年");
			row3.createCell(5).setCellValue("五年");
			row3.createCell(6).setCellValue("一年");
			row3.createCell(7).setCellValue("二年");
			row3.createCell(8).setCellValue("三年");
			row3.createCell(9).setCellValue("四年");
			row3.createCell(10).setCellValue("五年");
			row3.createCell(11).setCellValue("一年");
			row3.createCell(12).setCellValue("二年");
			row3.createCell(13).setCellValue("三年");
			row3.createCell(14).setCellValue("四年");
			row3.createCell(15).setCellValue("五年");
			row3.createCell(16).setCellValue("一年");
			row3.createCell(17).setCellValue("二年");
			row3.createCell(18).setCellValue("三年");
			row3.createCell(19).setCellValue("四年");
			row3.createCell(20).setCellValue("五年");

			row3.createCell(21).setCellValue("一年");
			row3.createCell(22).setCellValue("二年");
			row3.createCell(23).setCellValue("三年");
			row3.createCell(24).setCellValue("四年");
			row3.createCell(25).setCellValue("五年");
			row3.createCell(26).setCellValue("一年");
			row3.createCell(27).setCellValue("二年");
			row3.createCell(28).setCellValue("三年");
			row3.createCell(29).setCellValue("四年");
			row3.createCell(30).setCellValue("五年");
			row3.createCell(31).setCellValue("一年");
			row3.createCell(32).setCellValue("二年");
			row3.createCell(33).setCellValue("三年");
			row3.createCell(34).setCellValue("四年");
			row3.createCell(35).setCellValue("五年");
			row3.createCell(36).setCellValue("一年");
			row3.createCell(37).setCellValue("二年");
			row3.createCell(38).setCellValue("三年");
			row3.createCell(39).setCellValue("四年");
			row3.createCell(40).setCellValue("五年");

			row3.createCell(61).setCellValue("一年");
			row3.createCell(62).setCellValue("二年");
			row3.createCell(63).setCellValue("三年");
			row3.createCell(64).setCellValue("四年");
			row3.createCell(65).setCellValue("五年");
			row3.createCell(66).setCellValue("一年");
			row3.createCell(67).setCellValue("二年");
			row3.createCell(68).setCellValue("三年");
			row3.createCell(69).setCellValue("四年");
			row3.createCell(70).setCellValue("五年");
			row3.createCell(71).setCellValue("一年");
			row3.createCell(72).setCellValue("二年");
			row3.createCell(73).setCellValue("三年");
			row3.createCell(74).setCellValue("四年");
			row3.createCell(75).setCellValue("五年");
			row3.createCell(76).setCellValue("一年");
			row3.createCell(77).setCellValue("二年");
			row3.createCell(78).setCellValue("三年");
			row3.createCell(79).setCellValue("四年");
			row3.createCell(80).setCellValue("五年");

			row3.createCell(81).setCellValue("一年");
			row3.createCell(82).setCellValue("二年");
			row3.createCell(83).setCellValue("三年");
			row3.createCell(84).setCellValue("四年");
			row3.createCell(85).setCellValue("五年");
			row3.createCell(86).setCellValue("一年");
			row3.createCell(87).setCellValue("二年");
			row3.createCell(88).setCellValue("三年");
			row3.createCell(89).setCellValue("四年");
			row3.createCell(90).setCellValue("五年");
			row3.createCell(91).setCellValue("一年");
			row3.createCell(92).setCellValue("二年");
			row3.createCell(93).setCellValue("三年");
			row3.createCell(94).setCellValue("四年");
			row3.createCell(95).setCellValue("五年");
			row3.createCell(96).setCellValue("一年");
			row3.createCell(97).setCellValue("二年");
			row3.createCell(98).setCellValue("三年");
			row3.createCell(99).setCellValue("四年");
			row3.createCell(100).setCellValue("五年");

			row3.createCell(101).setCellValue("一年");
			row3.createCell(102).setCellValue("二年");
			row3.createCell(103).setCellValue("三年");
			row3.createCell(104).setCellValue("四年");
			row3.createCell(105).setCellValue("五年");
			row3.createCell(106).setCellValue("一年");
			row3.createCell(107).setCellValue("二年");
			row3.createCell(108).setCellValue("三年");
			row3.createCell(109).setCellValue("四年");
			row3.createCell(110).setCellValue("五年");
			row3.createCell(111).setCellValue("一年");
			row3.createCell(112).setCellValue("二年");
			row3.createCell(113).setCellValue("三年");
			row3.createCell(114).setCellValue("四年");
			row3.createCell(115).setCellValue("五年");
			row3.createCell(116).setCellValue("一年");
			row3.createCell(117).setCellValue("二年");
			row3.createCell(118).setCellValue("三年");
			row3.createCell(119).setCellValue("四年");
			row3.createCell(120).setCellValue("五年");

			row3.createCell(121).setCellValue("一年");
			row3.createCell(122).setCellValue("二年");
			row3.createCell(123).setCellValue("三年");
			row3.createCell(124).setCellValue("四年");
			row3.createCell(125).setCellValue("五年");
			row3.createCell(126).setCellValue("一年");
			row3.createCell(127).setCellValue("二年");
			row3.createCell(128).setCellValue("三年");
			row3.createCell(129).setCellValue("四年");
			row3.createCell(130).setCellValue("五年");
			row3.createCell(131).setCellValue("一年");
			row3.createCell(132).setCellValue("二年");
			row3.createCell(133).setCellValue("三年");
			row3.createCell(134).setCellValue("四年");
			row3.createCell(135).setCellValue("五年");
			row3.createCell(136).setCellValue("一年");
			row3.createCell(137).setCellValue("二年");
			row3.createCell(138).setCellValue("三年");
			row3.createCell(139).setCellValue("四年");
			row3.createCell(140).setCellValue("五年");

			row3.createCell(141).setCellValue("一年");
			row3.createCell(142).setCellValue("二年");
			row3.createCell(143).setCellValue("三年");
			row3.createCell(144).setCellValue("四年");
			row3.createCell(145).setCellValue("五年");
			row3.createCell(146).setCellValue("一年");
			row3.createCell(147).setCellValue("二年");
			row3.createCell(148).setCellValue("三年");
			row3.createCell(149).setCellValue("四年");
			row3.createCell(150).setCellValue("五年");
			row3.createCell(151).setCellValue("一年");
			row3.createCell(152).setCellValue("二年");
			row3.createCell(153).setCellValue("三年");
			row3.createCell(154).setCellValue("四年");
			row3.createCell(155).setCellValue("五年");
			row3.createCell(156).setCellValue("一年");
			row3.createCell(157).setCellValue("二年");
			row3.createCell(158).setCellValue("三年");
			row3.createCell(159).setCellValue("四年");
			row3.createCell(160).setCellValue("五年");
			//
			int k = 5;

			//
			for (String key : monthMap.keySet()) {
				curCol = 1;
				HSSFRow rown = sheet.createRow(k++);
				if (!"total".equals(key) && !"totalColumn".equals(key)) {
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
					
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeLostqyNum(),
							monthMap.get("total").getChangeLostqyNum());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeLostgrQNum(),
							monthMap.get("total").getChangeLostgrQNum());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeLostgrGNum(),
							monthMap.get("total").getChangeLostgrGNum());
					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeLostjgNum(),
							monthMap.get("total").getChangeLostjgNum());
					
//					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeReplaceqyNum(),
//							monthMap.get("total").getChangeReplaceqyNum());
//					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeReplacegrQNum(),
//							monthMap.get("total").getChangeReplacegrQNum());
//					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeReplacegrGNum(),
//							monthMap.get("total").getChangeReplacegrGNum());
//					curCol = addData2Sheet(sheet, rown, curCol, monthMap.get(key).getChangeReplacejgNum(),
//							monthMap.get("total").getChangeReplacejgNum());
					// rown.createCell(11).setCellValue(monthMap.get(key).getXzgrGadd1()==null?0:monthMap.get(key).getXzgrGadd1());
					// rown.createCell(12).setCellValue(monthMap.get(key).getXzgrGadd2()==null?0:monthMap.get(key).getXzgrGadd2());
					// rown.createCell(13).setCellValue(monthMap.get(key).getXzgrGadd3()==null?0:monthMap.get(key).getXzgrGadd3());
					// rown.createCell(14).setCellValue(monthMap.get(key).getXzgrGadd4()==null?0:monthMap.get(key).getXzgrGadd4());
					// rown.createCell(15).setCellValue(monthMap.get(key).getXzgrGadd5()==null?0:monthMap.get(key).getXzgrGadd5());
					// rown.createCell(16).setCellValue(monthMap.get(key).getXzjgadd1()==null?0:monthMap.get(key).getXzjgadd1());
					// rown.createCell(17).setCellValue(monthMap.get(key).getXzjgadd2()==null?0:monthMap.get(key).getXzjgadd2());
					// rown.createCell(18).setCellValue(monthMap.get(key).getXzjgadd3()==null?0:monthMap.get(key).getXzjgadd3());
					// rown.createCell(19).setCellValue(monthMap.get(key).getXzjgadd4()==null?0:monthMap.get(key).getXzjgadd4());
					// rown.createCell(20).setCellValue(monthMap.get(key).getXzjgadd5()==null?0:monthMap.get(key).getXzjgadd5());
					//**
					// rown.createCell(21).setCellValue(monthMap.get(key).getGxqyadd1()==null?0:monthMap.get(key).getGxqyadd1());
					// rown.createCell(22).setCellValue(monthMap.get(key).getGxqyadd2()==null?0:monthMap.get(key).getGxqyadd2());
					// rown.createCell(23).setCellValue(monthMap.get(key).getGxqyadd3()==null?0:monthMap.get(key).getGxqyadd3());
					// rown.createCell(24).setCellValue(monthMap.get(key).getGxqyadd4()==null?0:monthMap.get(key).getGxqyadd4());
					// rown.createCell(25).setCellValue(monthMap.get(key).getGxqyadd5()==null?0:monthMap.get(key).getGxqyadd5());
					
					// rown.createCell(26).setCellValue(monthMap.get(key).getGxgrQadd1()==null?0:monthMap.get(key).getGxgrQadd1());
					// rown.createCell(27).setCellValue(monthMap.get(key).getGxgrQadd2()==null?0:monthMap.get(key).getGxgrQadd2());
					// rown.createCell(28).setCellValue(monthMap.get(key).getGxgrQadd3()==null?0:monthMap.get(key).getGxgrQadd3());
					// rown.createCell(29).setCellValue(monthMap.get(key).getGxgrQadd4()==null?0:monthMap.get(key).getGxgrQadd4());
					// rown.createCell(30).setCellValue(monthMap.get(key).getGxgrQadd5()==null?0:monthMap.get(key).getGxgrQadd5());
					
					// rown.createCell(31).setCellValue(monthMap.get(key).getGxgrGadd1()==null?0:monthMap.get(key).getGxgrGadd1());
					// rown.createCell(32).setCellValue(monthMap.get(key).getGxgrGadd2()==null?0:monthMap.get(key).getGxgrGadd2());
					// rown.createCell(33).setCellValue(monthMap.get(key).getGxgrGadd3()==null?0:monthMap.get(key).getGxgrGadd3());
					// rown.createCell(34).setCellValue(monthMap.get(key).getGxgrGadd4()==null?0:monthMap.get(key).getGxgrGadd4());
					// rown.createCell(35).setCellValue(monthMap.get(key).getGxgrGadd5()==null?0:monthMap.get(key).getGxgrGadd5());
					//**
					// rown.createCell(36).setCellValue(monthMap.get(key).getGxjgadd1()==null?0:monthMap.get(key).getGxjgadd1());
					// rown.createCell(37).setCellValue(monthMap.get(key).getGxjgadd2()==null?0:monthMap.get(key).getGxjgadd2());
					// rown.createCell(38).setCellValue(monthMap.get(key).getGxjgadd3()==null?0:monthMap.get(key).getGxjgadd3());
					// rown.createCell(39).setCellValue(monthMap.get(key).getGxjgadd4()==null?0:monthMap.get(key).getGxjgadd4());
					// rown.createCell(40).setCellValue(monthMap.get(key).getGxjgadd5()==null?0:monthMap.get(key).getGxjgadd5());
					//**
					// rown.createCell(41).setCellValue(monthMap.get(key).getLostCerateqy()==null?0:monthMap.get(key).getLostCerateqy());
					// rown.createCell(42).setCellValue(monthMap.get(key).getLostCerategrQ()==null?0:monthMap.get(key).getLostCerategrQ());
					// rown.createCell(43).setCellValue(monthMap.get(key).getLostCerategrG()==null?0:monthMap.get(key).getLostCerategrG());
					// rown.createCell(44).setCellValue(monthMap.get(key).getLostCeratejg()==null?0:monthMap.get(key).getLostCeratejg());
					//**
					// rown.createCell(45).setCellValue(monthMap.get(key).getDamageCertificateqy()==null?0:monthMap.get(key).getDamageCertificateqy());
					// rown.createCell(46).setCellValue(monthMap.get(key).getDamageCertificategrQ()==null?0:monthMap.get(key).getDamageCertificategrQ());
					// rown.createCell(47).setCellValue(monthMap.get(key).getDamageCertificategrG()==null?0:monthMap.get(key).getDamageCertificategrG());
					// rown.createCell(48).setCellValue(monthMap.get(key).getDamageCertificatejg()==null?0:monthMap.get(key).getDamageCertificatejg());
					//**
					// rown.createCell(49).setCellValue(monthMap.get(key).getModifyNumqy()==null?0:monthMap.get(key).getModifyNumqy());
					// rown.createCell(50).setCellValue(monthMap.get(key).getModifyNumgrQ()==null?0:monthMap.get(key).getModifyNumgrQ());
					// rown.createCell(51).setCellValue(monthMap.get(key).getModifyNumgrG()==null?0:monthMap.get(key).getModifyNumgrG());
					// rown.createCell(52).setCellValue(monthMap.get(key).getModifyNumjg()==null?0:monthMap.get(key).getModifyNumjg());
					//**
					// rown.createCell(53).setCellValue(monthMap.get(key).getChangeLostqyNum()==null?0:monthMap.get(key).getChangeLostqyNum());
					// rown.createCell(54).setCellValue(monthMap.get(key).getChangeLostgrQNum()==null?0:monthMap.get(key).getChangeLostgrQNum());
					// rown.createCell(55).setCellValue(monthMap.get(key).getChangeLostgrGNum()==null?0:monthMap.get(key).getChangeLostgrGNum());
					// rown.createCell(56).setCellValue(monthMap.get(key).getChangeLostjgNum()==null?0:monthMap.get(key).getChangeLostjgNum());
					//**
					// rown.createCell(57).setCellValue(monthMap.get(key).getChangeReplaceqyNum()==null?0:monthMap.get(key).getChangeReplaceqyNum());
					// rown.createCell(58).setCellValue(monthMap.get(key).getChangeReplacegrQNum()==null?0:monthMap.get(key).getChangeReplacegrQNum());
					// rown.createCell(59).setCellValue(monthMap.get(key).getChangeReplacegrGNum()==null?0:monthMap.get(key).getChangeReplacegrGNum());
					// rown.createCell(60).setCellValue(monthMap.get(key).getChangeReplacejgNum()==null?0:monthMap.get(key).getChangeReplacejgNum());
					//**
					// rown.createCell(61).setCellValue(monthMap.get(key).getUpdateLostqyNum()==null?0:monthMap.get(key).getUpdateLostqyNum());
					// rown.createCell(62).setCellValue(monthMap.get(key).getUpdateLostqyNum2()==null?0:monthMap.get(key).getUpdateLostqyNum2());
					// rown.createCell(63).setCellValue(monthMap.get(key).getUpdateLostqyNum3()==null?0:monthMap.get(key).getUpdateLostqyNum3());
					// rown.createCell(64).setCellValue(monthMap.get(key).getUpdateLostqyNum4()==null?0:monthMap.get(key).getUpdateLostqyNum4());
					// rown.createCell(65).setCellValue(monthMap.get(key).getUpdateLostqyNum5()==null?0:monthMap.get(key).getUpdateLostqyNum5());
					// rown.createCell(66).setCellValue(monthMap.get(key).getUpdateLostgrQNum()==null?0:monthMap.get(key).getUpdateLostgrQNum());
					// rown.createCell(67).setCellValue(monthMap.get(key).getUpdateLostgrQNum2()==null?0:monthMap.get(key).getUpdateLostgrQNum2());
					// rown.createCell(68).setCellValue(monthMap.get(key).getUpdateLostgrQNum3()==null?0:monthMap.get(key).getUpdateLostgrQNum3());
					// rown.createCell(69).setCellValue(monthMap.get(key).getUpdateLostgrQNum4()==null?0:monthMap.get(key).getUpdateLostgrQNum4());
					// rown.createCell(70).setCellValue(monthMap.get(key).getUpdateLostgrQNum5()==null?0:monthMap.get(key).getUpdateLostgrQNum5());
					// rown.createCell(71).setCellValue(monthMap.get(key).getUpdateLostgrGNum()==null?0:monthMap.get(key).getUpdateLostgrGNum());
					// rown.createCell(72).setCellValue(monthMap.get(key).getUpdateLostgrGNum2()==null?0:monthMap.get(key).getUpdateLostgrGNum2());
					// rown.createCell(73).setCellValue(monthMap.get(key).getUpdateLostgrGNum3()==null?0:monthMap.get(key).getUpdateLostgrGNum3());
					// rown.createCell(74).setCellValue(monthMap.get(key).getUpdateLostgrGNum4()==null?0:monthMap.get(key).getUpdateLostgrGNum4());
					// rown.createCell(75).setCellValue(monthMap.get(key).getUpdateLostgrGNum5()==null?0:monthMap.get(key).getUpdateLostgrGNum5());
					// rown.createCell(76).setCellValue(monthMap.get(key).getUpdateLostjgNum()==null?0:monthMap.get(key).getUpdateLostjgNum());
					// rown.createCell(77).setCellValue(monthMap.get(key).getUpdateLostjgNum2()==null?0:monthMap.get(key).getUpdateLostjgNum2());
					// rown.createCell(78).setCellValue(monthMap.get(key).getUpdateLostjgNum3()==null?0:monthMap.get(key).getUpdateLostjgNum3());
					// rown.createCell(79).setCellValue(monthMap.get(key).getUpdateLostjgNum4()==null?0:monthMap.get(key).getUpdateLostjgNum4());
					// rown.createCell(80).setCellValue(monthMap.get(key).getUpdateLostjgNum5()==null?0:monthMap.get(key).getUpdateLostjgNum5());
					//
					//
					//
					// rown.createCell(81).setCellValue(monthMap.get(key).getUpdateReplaceqyNum()==null?0:monthMap.get(key).getUpdateReplaceqyNum());
					// rown.createCell(82).setCellValue(monthMap.get(key).getUpdateReplaceqyNum2()==null?0:monthMap.get(key).getUpdateReplaceqyNum2());
					// rown.createCell(83).setCellValue(monthMap.get(key).getUpdateReplaceqyNum3()==null?0:monthMap.get(key).getUpdateReplaceqyNum3());
					// rown.createCell(84).setCellValue(monthMap.get(key).getUpdateReplaceqyNum4()==null?0:monthMap.get(key).getUpdateReplaceqyNum4());
					// rown.createCell(85).setCellValue(monthMap.get(key).getUpdateReplaceqyNum5()==null?0:monthMap.get(key).getUpdateReplaceqyNum5());
					// rown.createCell(86).setCellValue(monthMap.get(key).getUpdateReplacegrQNum()==null?0:monthMap.get(key).getUpdateReplacegrQNum());
					// rown.createCell(87).setCellValue(monthMap.get(key).getUpdateReplacegrQNum2()==null?0:monthMap.get(key).getUpdateReplacegrQNum2());
					// rown.createCell(88).setCellValue(monthMap.get(key).getUpdateReplacegrQNum3()==null?0:monthMap.get(key).getUpdateReplacegrQNum3());
					// rown.createCell(89).setCellValue(monthMap.get(key).getUpdateReplacegrQNum4()==null?0:monthMap.get(key).getUpdateReplacegrQNum4());
					// rown.createCell(90).setCellValue(monthMap.get(key).getUpdateReplacegrQNum5()==null?0:monthMap.get(key).getUpdateReplacegrQNum5());
					// rown.createCell(91).setCellValue(monthMap.get(key).getUpdateReplacegrGNum()==null?0:monthMap.get(key).getUpdateReplacegrGNum());
					// rown.createCell(92).setCellValue(monthMap.get(key).getUpdateReplacegrGNum2()==null?0:monthMap.get(key).getUpdateReplacegrGNum2());
					// rown.createCell(93).setCellValue(monthMap.get(key).getUpdateReplacegrGNum3()==null?0:monthMap.get(key).getUpdateReplacegrGNum3());
					// rown.createCell(94).setCellValue(monthMap.get(key).getUpdateReplacegrGNum4()==null?0:monthMap.get(key).getUpdateReplacegrGNum4());
					// rown.createCell(95).setCellValue(monthMap.get(key).getUpdateReplacegrGNum5()==null?0:monthMap.get(key).getUpdateReplacegrGNum5());
					// rown.createCell(96).setCellValue(monthMap.get(key).getUpdateReplacejgNum()==null?0:monthMap.get(key).getUpdateReplacejgNum());
					// rown.createCell(97).setCellValue(monthMap.get(key).getUpdateReplacejgNum2()==null?0:monthMap.get(key).getUpdateReplacejgNum2());
					// rown.createCell(98).setCellValue(monthMap.get(key).getUpdateReplacejgNum3()==null?0:monthMap.get(key).getUpdateReplacejgNum3());
					// rown.createCell(99).setCellValue(monthMap.get(key).getUpdateReplacejgNum4()==null?0:monthMap.get(key).getUpdateReplacejgNum4());
					// rown.createCell(100).setCellValue(monthMap.get(key).getUpdateReplacejgNum5()==null?0:monthMap.get(key).getUpdateReplacejgNum5());
					//
					// rown.createCell(101).setCellValue(monthMap.get(key).getUpdateChangeqyNum()==null?0:monthMap.get(key).getUpdateChangeqyNum());
					// rown.createCell(102).setCellValue(monthMap.get(key).getUpdateChangeqyNum2()==null?0:monthMap.get(key).getUpdateChangeqyNum2());
					// rown.createCell(103).setCellValue(monthMap.get(key).getUpdateChangeqyNum3()==null?0:monthMap.get(key).getUpdateChangeqyNum3());
					// rown.createCell(104).setCellValue(monthMap.get(key).getUpdateChangeqyNum4()==null?0:monthMap.get(key).getUpdateChangeqyNum4());
					// rown.createCell(105).setCellValue(monthMap.get(key).getUpdateChangeqyNum5()==null?0:monthMap.get(key).getUpdateChangeqyNum5());
					// rown.createCell(106).setCellValue(monthMap.get(key).getUpdateChangegrQNum()==null?0:monthMap.get(key).getUpdateChangegrQNum());
					// rown.createCell(107).setCellValue(monthMap.get(key).getUpdateChangegrQNum2()==null?0:monthMap.get(key).getUpdateChangegrQNum2());
					// rown.createCell(108).setCellValue(monthMap.get(key).getUpdateChangegrQNum3()==null?0:monthMap.get(key).getUpdateChangegrQNum3());
					// rown.createCell(109).setCellValue(monthMap.get(key).getUpdateChangegrQNum4()==null?0:monthMap.get(key).getUpdateChangegrQNum4());
					// rown.createCell(110).setCellValue(monthMap.get(key).getUpdateChangegrQNum5()==null?0:monthMap.get(key).getUpdateChangegrQNum5());
					// rown.createCell(111).setCellValue(monthMap.get(key).getUpdateChangegrGNum()==null?0:monthMap.get(key).getUpdateChangegrGNum());
					// rown.createCell(112).setCellValue(monthMap.get(key).getUpdateChangegrGNum2()==null?0:monthMap.get(key).getUpdateChangegrGNum2());
					// rown.createCell(113).setCellValue(monthMap.get(key).getUpdateChangegrGNum3()==null?0:monthMap.get(key).getUpdateChangegrGNum3());
					// rown.createCell(114).setCellValue(monthMap.get(key).getUpdateChangegrGNum4()==null?0:monthMap.get(key).getUpdateChangegrGNum4());
					// rown.createCell(115).setCellValue(monthMap.get(key).getUpdateChangegrGNum5()==null?0:monthMap.get(key).getUpdateChangegrGNum5());
					// rown.createCell(116).setCellValue(monthMap.get(key).getUpdateChangejgNum()==null?0:monthMap.get(key).getUpdateChangejgNum());
					// rown.createCell(117).setCellValue(monthMap.get(key).getUpdateChangejgNum2()==null?0:monthMap.get(key).getUpdateChangejgNum2());
					// rown.createCell(118).setCellValue(monthMap.get(key).getUpdateChangejgNum3()==null?0:monthMap.get(key).getUpdateChangejgNum3());
					// rown.createCell(119).setCellValue(monthMap.get(key).getUpdateChangejgNum4()==null?0:monthMap.get(key).getUpdateChangejgNum4());
					// rown.createCell(120).setCellValue(monthMap.get(key).getUpdateChangejgNum5()==null?0:monthMap.get(key).getUpdateChangejgNum5());
					//
					// rown.createCell(121).setCellValue(monthMap.get(key).getChangeUpdateLostqyNum()==null?0:monthMap.get(key).getChangeUpdateLostqyNum());
					// rown.createCell(122).setCellValue(monthMap.get(key).getChangeUpdateLostqyNum2()==null?0:monthMap.get(key).getChangeUpdateLostqyNum2());
					// rown.createCell(123).setCellValue(monthMap.get(key).getChangeUpdateLostqyNum3()==null?0:monthMap.get(key).getChangeUpdateLostqyNum3());
					// rown.createCell(124).setCellValue(monthMap.get(key).getChangeUpdateLostqyNum4()==null?0:monthMap.get(key).getChangeUpdateLostqyNum4());
					// rown.createCell(125).setCellValue(monthMap.get(key).getChangeUpdateLostqyNum5()==null?0:monthMap.get(key).getChangeUpdateLostqyNum5());
					// rown.createCell(126).setCellValue(monthMap.get(key).getChangeUpdateLostgrQNum()==null?0:monthMap.get(key).getChangeUpdateLostgrQNum());
					// rown.createCell(127).setCellValue(monthMap.get(key).getChangeUpdateLostgrQNum2()==null?0:monthMap.get(key).getChangeUpdateLostgrQNum2());
					// rown.createCell(128).setCellValue(monthMap.get(key).getChangeUpdateLostgrQNum3()==null?0:monthMap.get(key).getChangeUpdateLostgrQNum3());
					// rown.createCell(129).setCellValue(monthMap.get(key).getChangeUpdateLostgrQNum4()==null?0:monthMap.get(key).getChangeUpdateLostgrQNum4());
					// rown.createCell(130).setCellValue(monthMap.get(key).getChangeUpdateLostgrQNum5()==null?0:monthMap.get(key).getChangeUpdateLostgrQNum5());
					// rown.createCell(131).setCellValue(monthMap.get(key).getChangeUpdateLostgrGNum()==null?0:monthMap.get(key).getChangeUpdateLostgrGNum());
					// rown.createCell(132).setCellValue(monthMap.get(key).getChangeUpdateLostgrGNum2()==null?0:monthMap.get(key).getChangeUpdateLostgrGNum2());
					// rown.createCell(133).setCellValue(monthMap.get(key).getChangeUpdateLostgrGNum3()==null?0:monthMap.get(key).getChangeUpdateLostgrGNum3());
					// rown.createCell(134).setCellValue(monthMap.get(key).getChangeUpdateLostgrGNum4()==null?0:monthMap.get(key).getChangeUpdateLostgrGNum4());
					// rown.createCell(135).setCellValue(monthMap.get(key).getChangeUpdateLostgrGNum5()==null?0:monthMap.get(key).getChangeUpdateLostgrGNum5());
					// rown.createCell(136).setCellValue(monthMap.get(key).getChangeUpdateLostjgNum()==null?0:monthMap.get(key).getChangeUpdateLostjgNum());
					// rown.createCell(137).setCellValue(monthMap.get(key).getChangeUpdateLostjgNum2()==null?0:monthMap.get(key).getChangeUpdateLostjgNum2());
					// rown.createCell(138).setCellValue(monthMap.get(key).getChangeUpdateLostjgNum3()==null?0:monthMap.get(key).getChangeUpdateLostjgNum3());
					// rown.createCell(139).setCellValue(monthMap.get(key).getChangeUpdateLostjgNum4()==null?0:monthMap.get(key).getChangeUpdateLostjgNum4());
					// rown.createCell(140).setCellValue(monthMap.get(key).getChangeUpdateLostjgNum5()==null?0:monthMap.get(key).getChangeUpdateLostjgNum5());
					//
					// rown.createCell(141).setCellValue(monthMap.get(key).getChangeUpdateReplaceqyNum()==null?0:monthMap.get(key).getChangeUpdateReplaceqyNum());
					// rown.createCell(142).setCellValue(monthMap.get(key).getChangeUpdateReplaceqyNum2()==null?0:monthMap.get(key).getChangeUpdateReplaceqyNum2());
					// rown.createCell(143).setCellValue(monthMap.get(key).getChangeUpdateReplaceqyNum3()==null?0:monthMap.get(key).getChangeUpdateReplaceqyNum3());
					// rown.createCell(144).setCellValue(monthMap.get(key).getChangeUpdateReplaceqyNum4()==null?0:monthMap.get(key).getChangeUpdateReplaceqyNum4());
					// rown.createCell(145).setCellValue(monthMap.get(key).getChangeUpdateReplaceqyNum5()==null?0:monthMap.get(key).getChangeUpdateReplaceqyNum5());
					// rown.createCell(146).setCellValue(monthMap.get(key).getChangeUpdateReplacegrQNum()==null?0:monthMap.get(key).getChangeUpdateReplacegrQNum());
					// rown.createCell(147).setCellValue(monthMap.get(key).getChangeUpdateReplacegrQNum2()==null?0:monthMap.get(key).getChangeUpdateReplacegrQNum2());
					// rown.createCell(148).setCellValue(monthMap.get(key).getChangeUpdateReplacegrQNum3()==null?0:monthMap.get(key).getChangeUpdateReplacegrQNum3());
					// rown.createCell(149).setCellValue(monthMap.get(key).getChangeUpdateReplacegrQNum4()==null?0:monthMap.get(key).getChangeUpdateReplacegrQNum4());
					// rown.createCell(150).setCellValue(monthMap.get(key).getChangeUpdateReplacegrQNum5()==null?0:monthMap.get(key).getChangeUpdateReplacegrQNum5());
					// rown.createCell(151).setCellValue(monthMap.get(key).getChangeUpdateReplacegrGNum()==null?0:monthMap.get(key).getChangeUpdateReplacegrGNum());
					// rown.createCell(152).setCellValue(monthMap.get(key).getChangeUpdateReplacegrGNum2()==null?0:monthMap.get(key).getChangeUpdateReplacegrGNum2());
					// rown.createCell(153).setCellValue(monthMap.get(key).getChangeUpdateReplacegrGNum3()==null?0:monthMap.get(key).getChangeUpdateReplacegrGNum3());
					// rown.createCell(154).setCellValue(monthMap.get(key).getChangeUpdateReplacegrGNum4()==null?0:monthMap.get(key).getChangeUpdateReplacegrGNum4());
					// rown.createCell(155).setCellValue(monthMap.get(key).getChangeUpdateReplacegrGNum5()==null?0:monthMap.get(key).getChangeUpdateReplacegrGNum5());
					// rown.createCell(156).setCellValue(monthMap.get(key).getChangeUpdateReplacejgNum()==null?0:monthMap.get(key).getChangeUpdateReplacejgNum());
					// rown.createCell(157).setCellValue(monthMap.get(key).getChangeUpdateReplacejgNum2()==null?0:monthMap.get(key).getChangeUpdateReplacejgNum2());
					// rown.createCell(158).setCellValue(monthMap.get(key).getChangeUpdateReplacejgNum3()==null?0:monthMap.get(key).getChangeUpdateReplacejgNum3());
					// rown.createCell(159).setCellValue(monthMap.get(key).getChangeUpdateReplacejgNum4()==null?0:monthMap.get(key).getChangeUpdateReplacejgNum4());
					// rown.createCell(160).setCellValue(monthMap.get(key).getChangeUpdateReplacejgNum5()==null?0:monthMap.get(key).getChangeUpdateReplacejgNum5());
				}
			}
			// HSSFRow rowi = sheet.createRow(4+monthMap.size());
			// rowi.createCell(0).setCellValue("总计");
			// rowi.createCell(1).setCellValue(monthMap.get("total").getXzqyadd1());
			// rowi.createCell(2).setCellValue(monthMap.get("total").getXzqyadd2());
			// rowi.createCell(3).setCellValue(monthMap.get("total").getXzqyadd3());
			// rowi.createCell(4).setCellValue(monthMap.get("total").getXzqyadd4());
			// rowi.createCell(5).setCellValue(monthMap.get("total").getXzqyadd5());
			// rowi.createCell(6).setCellValue(monthMap.get("total").getXzgrQadd1());
			// rowi.createCell(7).setCellValue(monthMap.get("total").getXzgrQadd2());
			// rowi.createCell(8).setCellValue(monthMap.get("total").getXzgrQadd3());
			// rowi.createCell(9).setCellValue(monthMap.get("total").getXzgrQadd4());
			// rowi.createCell(10).setCellValue(monthMap.get("total").getXzgrQadd5());
			// rowi.createCell(11).setCellValue(monthMap.get("total").getXzgrGadd1());
			// rowi.createCell(12).setCellValue(monthMap.get("total").getXzgrGadd2());
			// rowi.createCell(13).setCellValue(monthMap.get("total").getXzgrGadd3());
			// rowi.createCell(14).setCellValue(monthMap.get("total").getXzgrGadd4());
			// rowi.createCell(15).setCellValue(monthMap.get("total").getXzgrGadd5());
			// rowi.createCell(16).setCellValue(monthMap.get("total").getXzjgadd1());
			// rowi.createCell(17).setCellValue(monthMap.get("total").getXzjgadd2());
			// rowi.createCell(18).setCellValue(monthMap.get("total").getXzjgadd3());
			// rowi.createCell(19).setCellValue(monthMap.get("total").getXzjgadd4());
			// rowi.createCell(20).setCellValue(monthMap.get("total").getXzjgadd5());
			//
			// rowi.createCell(21).setCellValue(monthMap.get("total").getGxqyadd1());
			// rowi.createCell(22).setCellValue(monthMap.get("total").getGxqyadd2());
			// rowi.createCell(23).setCellValue(monthMap.get("total").getGxqyadd3());
			// rowi.createCell(24).setCellValue(monthMap.get("total").getGxqyadd4());
			// rowi.createCell(25).setCellValue(monthMap.get("total").getGxqyadd5());
			// rowi.createCell(26).setCellValue(monthMap.get("total").getGxgrQadd1());
			// rowi.createCell(27).setCellValue(monthMap.get("total").getGxgrQadd2());
			// rowi.createCell(28).setCellValue(monthMap.get("total").getGxgrQadd3());
			// rowi.createCell(29).setCellValue(monthMap.get("total").getGxgrQadd4());
			// rowi.createCell(30).setCellValue(monthMap.get("total").getGxgrQadd5());
			// rowi.createCell(31).setCellValue(monthMap.get("total").getGxgrGadd1());
			// rowi.createCell(32).setCellValue(monthMap.get("total").getGxgrGadd2());
			// rowi.createCell(33).setCellValue(monthMap.get("total").getGxgrGadd3());
			// rowi.createCell(34).setCellValue(monthMap.get("total").getGxgrGadd4());
			// rowi.createCell(35).setCellValue(monthMap.get("total").getGxgrGadd5());
			// rowi.createCell(36).setCellValue(monthMap.get("total").getGxjgadd1());
			// rowi.createCell(37).setCellValue(monthMap.get("total").getGxjgadd2());
			// rowi.createCell(38).setCellValue(monthMap.get("total").getGxjgadd3());
			// rowi.createCell(39).setCellValue(monthMap.get("total").getGxjgadd4());
			// rowi.createCell(40).setCellValue(monthMap.get("total").getGxjgadd5());
			//
			// rowi.createCell(41).setCellValue(monthMap.get("total").getLostCerateqy());
			// rowi.createCell(42).setCellValue(monthMap.get("total").getLostCerategrQ());
			// rowi.createCell(43).setCellValue(monthMap.get("total").getLostCerategrG());
			// rowi.createCell(44).setCellValue(monthMap.get("total").getLostCeratejg());
			//
			// rowi.createCell(45).setCellValue(monthMap.get("total").getDamageCertificateqy());
			// rowi.createCell(46).setCellValue(monthMap.get("total").getDamageCertificategrQ());
			// rowi.createCell(47).setCellValue(monthMap.get("total").getDamageCertificategrG());
			// rowi.createCell(48).setCellValue(monthMap.get("total").getDamageCertificatejg());
			//
			// rowi.createCell(49).setCellValue(monthMap.get("total").getModifyNumqy());
			// rowi.createCell(50).setCellValue(monthMap.get("total").getModifyNumgrQ());
			// rowi.createCell(51).setCellValue(monthMap.get("total").getModifyNumgrG());
			// rowi.createCell(52).setCellValue(monthMap.get("total").getModifyNumjg());
			//
			// rowi.createCell(53).setCellValue(monthMap.get("total").getChangeLostqyNum());
			// rowi.createCell(54).setCellValue(monthMap.get("total").getChangeLostgrQNum());
			// rowi.createCell(55).setCellValue(monthMap.get("total").getChangeLostgrGNum());
			// rowi.createCell(56).setCellValue(monthMap.get("total").getChangeLostjgNum());
			//
			// rowi.createCell(57).setCellValue(monthMap.get("total").getChangeReplaceqyNum());
			// rowi.createCell(58).setCellValue(monthMap.get("total").getChangeReplacegrQNum());
			// rowi.createCell(59).setCellValue(monthMap.get("total").getChangeReplacegrGNum());
			// rowi.createCell(60).setCellValue(monthMap.get("total").getChangeReplacejgNum());
			//
			// rowi.createCell(61).setCellValue(monthMap.get("total").getUpdateLostqyNum());
			// rowi.createCell(62).setCellValue(monthMap.get("total").getUpdateLostqyNum2());
			// rowi.createCell(63).setCellValue(monthMap.get("total").getUpdateLostqyNum3());
			// rowi.createCell(64).setCellValue(monthMap.get("total").getUpdateLostqyNum4());
			// rowi.createCell(65).setCellValue(monthMap.get("total").getUpdateLostqyNum5());
			// rowi.createCell(66).setCellValue(monthMap.get("total").getUpdateLostgrQNum());
			// rowi.createCell(67).setCellValue(monthMap.get("total").getUpdateLostgrQNum2());
			// rowi.createCell(68).setCellValue(monthMap.get("total").getUpdateLostgrQNum3());
			// rowi.createCell(69).setCellValue(monthMap.get("total").getUpdateLostgrQNum4());
			// rowi.createCell(70).setCellValue(monthMap.get("total").getUpdateLostgrQNum5());
			// rowi.createCell(71).setCellValue(monthMap.get("total").getUpdateLostgrGNum());
			// rowi.createCell(72).setCellValue(monthMap.get("total").getUpdateLostgrGNum2());
			// rowi.createCell(73).setCellValue(monthMap.get("total").getUpdateLostgrGNum3());
			// rowi.createCell(74).setCellValue(monthMap.get("total").getUpdateLostgrGNum4());
			// rowi.createCell(75).setCellValue(monthMap.get("total").getUpdateLostgrGNum5());
			// rowi.createCell(76).setCellValue(monthMap.get("total").getUpdateLostjgNum());
			// rowi.createCell(77).setCellValue(monthMap.get("total").getUpdateLostjgNum2());
			// rowi.createCell(78).setCellValue(monthMap.get("total").getUpdateLostjgNum3());
			// rowi.createCell(79).setCellValue(monthMap.get("total").getUpdateLostjgNum4());
			// rowi.createCell(80).setCellValue(monthMap.get("total").getUpdateLostjgNum5());
			//
			//
			//
			// rowi.createCell(81).setCellValue(monthMap.get("total").getUpdateReplaceqyNum());
			// rowi.createCell(82).setCellValue(monthMap.get("total").getUpdateReplaceqyNum2());
			// rowi.createCell(83).setCellValue(monthMap.get("total").getUpdateReplaceqyNum3());
			// rowi.createCell(84).setCellValue(monthMap.get("total").getUpdateReplaceqyNum4());
			// rowi.createCell(85).setCellValue(monthMap.get("total").getUpdateReplaceqyNum5());
			// rowi.createCell(86).setCellValue(monthMap.get("total").getUpdateReplacegrQNum());
			// rowi.createCell(87).setCellValue(monthMap.get("total").getUpdateReplacegrQNum2());
			// rowi.createCell(88).setCellValue(monthMap.get("total").getUpdateReplacegrQNum3());
			// rowi.createCell(89).setCellValue(monthMap.get("total").getUpdateReplacegrQNum4());
			// rowi.createCell(90).setCellValue(monthMap.get("total").getUpdateReplacegrQNum5());
			// rowi.createCell(91).setCellValue(monthMap.get("total").getUpdateReplacegrGNum());
			// rowi.createCell(92).setCellValue(monthMap.get("total").getUpdateReplacegrGNum2());
			// rowi.createCell(93).setCellValue(monthMap.get("total").getUpdateReplacegrGNum3());
			// rowi.createCell(94).setCellValue(monthMap.get("total").getUpdateReplacegrGNum4());
			// rowi.createCell(95).setCellValue(monthMap.get("total").getUpdateReplacegrGNum5());
			// rowi.createCell(96).setCellValue(monthMap.get("total").getUpdateReplacejgNum());
			// rowi.createCell(97).setCellValue(monthMap.get("total").getUpdateReplacejgNum2());
			// rowi.createCell(98).setCellValue(monthMap.get("total").getUpdateReplacejgNum3());
			// rowi.createCell(99).setCellValue(monthMap.get("total").getUpdateReplacejgNum4());
			// rowi.createCell(100).setCellValue(monthMap.get("total").getUpdateReplacejgNum5());
			//
			// rowi.createCell(101).setCellValue(monthMap.get("total").getUpdateChangeqyNum());
			// rowi.createCell(102).setCellValue(monthMap.get("total").getUpdateChangeqyNum2());
			// rowi.createCell(103).setCellValue(monthMap.get("total").getUpdateChangeqyNum3());
			// rowi.createCell(104).setCellValue(monthMap.get("total").getUpdateChangeqyNum4());
			// rowi.createCell(105).setCellValue(monthMap.get("total").getUpdateChangeqyNum5());
			// rowi.createCell(106).setCellValue(monthMap.get("total").getUpdateChangegrQNum());
			// rowi.createCell(107).setCellValue(monthMap.get("total").getUpdateChangegrQNum2());
			// rowi.createCell(108).setCellValue(monthMap.get("total").getUpdateChangegrQNum3());
			// rowi.createCell(109).setCellValue(monthMap.get("total").getUpdateChangegrQNum4());
			// rowi.createCell(110).setCellValue(monthMap.get("total").getUpdateChangegrQNum5());
			// rowi.createCell(111).setCellValue(monthMap.get("total").getUpdateChangegrGNum());
			// rowi.createCell(112).setCellValue(monthMap.get("total").getUpdateChangegrGNum2());
			// rowi.createCell(113).setCellValue(monthMap.get("total").getUpdateChangegrGNum3());
			// rowi.createCell(114).setCellValue(monthMap.get("total").getUpdateChangegrGNum4());
			// rowi.createCell(115).setCellValue(monthMap.get("total").getUpdateChangegrGNum5());
			// rowi.createCell(116).setCellValue(monthMap.get("total").getUpdateChangejgNum());
			// rowi.createCell(117).setCellValue(monthMap.get("total").getUpdateChangejgNum2());
			// rowi.createCell(118).setCellValue(monthMap.get("total").getUpdateChangejgNum3());
			// rowi.createCell(119).setCellValue(monthMap.get("total").getUpdateChangejgNum4());
			// rowi.createCell(120).setCellValue(monthMap.get("total").getUpdateChangejgNum5());
			//
			// rowi.createCell(121).setCellValue(monthMap.get("total").getChangeUpdateLostqyNum());
			// rowi.createCell(122).setCellValue(monthMap.get("total").getChangeUpdateLostqyNum2());
			// rowi.createCell(123).setCellValue(monthMap.get("total").getChangeUpdateLostqyNum3());
			// rowi.createCell(124).setCellValue(monthMap.get("total").getChangeUpdateLostqyNum4());
			// rowi.createCell(125).setCellValue(monthMap.get("total").getChangeUpdateLostqyNum5());
			// rowi.createCell(126).setCellValue(monthMap.get("total").getChangeUpdateLostgrQNum());
			// rowi.createCell(127).setCellValue(monthMap.get("total").getChangeUpdateLostgrQNum2());
			// rowi.createCell(128).setCellValue(monthMap.get("total").getChangeUpdateLostgrQNum3());
			// rowi.createCell(129).setCellValue(monthMap.get("total").getChangeUpdateLostgrQNum4());
			// rowi.createCell(130).setCellValue(monthMap.get("total").getChangeUpdateLostgrQNum5());
			// rowi.createCell(131).setCellValue(monthMap.get("total").getChangeUpdateLostgrGNum());
			// rowi.createCell(132).setCellValue(monthMap.get("total").getChangeUpdateLostgrGNum2());
			// rowi.createCell(133).setCellValue(monthMap.get("total").getChangeUpdateLostgrGNum3());
			// rowi.createCell(134).setCellValue(monthMap.get("total").getChangeUpdateLostgrGNum4());
			// rowi.createCell(135).setCellValue(monthMap.get("total").getChangeUpdateLostgrGNum5());
			// rowi.createCell(136).setCellValue(monthMap.get("total").getChangeUpdateLostjgNum());
			// rowi.createCell(137).setCellValue(monthMap.get("total").getChangeUpdateLostjgNum2());
			// rowi.createCell(138).setCellValue(monthMap.get("total").getChangeUpdateLostjgNum3());
			// rowi.createCell(139).setCellValue(monthMap.get("total").getChangeUpdateLostjgNum4());
			// rowi.createCell(140).setCellValue(monthMap.get("total").getChangeUpdateLostjgNum5());
			//
			// rowi.createCell(141).setCellValue(monthMap.get("total").getChangeUpdateReplaceqyNum());
			// rowi.createCell(142).setCellValue(monthMap.get("total").getChangeUpdateReplaceqyNum2());
			// rowi.createCell(143).setCellValue(monthMap.get("total").getChangeUpdateReplaceqyNum3());
			// rowi.createCell(144).setCellValue(monthMap.get("total").getChangeUpdateReplaceqyNum4());
			// rowi.createCell(145).setCellValue(monthMap.get("total").getChangeUpdateReplaceqyNum5());
			// rowi.createCell(146).setCellValue(monthMap.get("total").getChangeUpdateReplacegrQNum());
			// rowi.createCell(147).setCellValue(monthMap.get("total").getChangeUpdateReplacegrQNum2());
			// rowi.createCell(148).setCellValue(monthMap.get("total").getChangeUpdateReplacegrQNum3());
			// rowi.createCell(149).setCellValue(monthMap.get("total").getChangeUpdateReplacegrQNum4());
			// rowi.createCell(150).setCellValue(monthMap.get("total").getChangeUpdateReplacegrQNum5());
			// rowi.createCell(151).setCellValue(monthMap.get("total").getChangeUpdateReplacegrGNum());
			// rowi.createCell(152).setCellValue(monthMap.get("total").getChangeUpdateReplacegrGNum2());
			// rowi.createCell(153).setCellValue(monthMap.get("total").getChangeUpdateReplacegrGNum3());
			// rowi.createCell(154).setCellValue(monthMap.get("total").getChangeUpdateReplacegrGNum4());
			// rowi.createCell(155).setCellValue(monthMap.get("total").getChangeUpdateReplacegrGNum5());
			// rowi.createCell(156).setCellValue(monthMap.get("total").getChangeUpdateReplacejgNum());
			// rowi.createCell(157).setCellValue(monthMap.get("total").getChangeUpdateReplacejgNum2());
			// rowi.createCell(158).setCellValue(monthMap.get("total").getChangeUpdateReplacejgNum3());
			// rowi.createCell(159).setCellValue(monthMap.get("total").getChangeUpdateReplacejgNum4());
			// rowi.createCell(160).setCellValue(monthMap.get("total").getChangeUpdateReplacejgNum5());
			//
			//

			/*
			 * if (multiType == false) {
			 * rowi.createCell(1).setCellValue(monthMap.get("total").getXzqyadd1
			 * ()); } else { row1.createCell(17).setCellValue("更新+遗失补办");
			 * row1.createCell(18).setCellValue("更新+损坏更换");
			 * row1.createCell(19).setCellValue("更新+更换/补办+信息变更"); }
			 * 
			 * int k = 3; int oneA1 = 0; int oneA2 = 0; int oneA4 = 0; int oneA5
			 * = 0; int twoA1 = 0; int twoA2 = 0; int twoA4 = 0; int twoA5 = 0;
			 * int oneR1 = 0; int oneR2 = 0; int oneR4 = 0; int oneR5 = 0; int
			 * twoR1 = 0; int twoR2 = 0; int twoR4 = 0; int twoR5 = 0; int
			 * replacementLosted = 0; int replacementDamaged = 0; int
			 * alterInfomation = 0;
			 */
			/*
			 * for (String key : monthMap.keySet()){ HSSFRow rown =
			 * sheet.createRow(k++); rown.createCell(0).setCellValue(key);
			 * rown.createCell(1).setCellValue(monthMap.get(key).getOneAdd1());
			 * oneA1+=(monthMap.get(key).getOneAdd1());
			 * rown.createCell(2).setCellValue(monthMap.get(key).getOneAdd2());
			 * oneA2+=(monthMap.get(key).getOneAdd2());
			 * rown.createCell(3).setCellValue(monthMap.get(key).getOneAdd4());
			 * oneA4+=(monthMap.get(key).getOneAdd4());
			 * rown.createCell(4).setCellValue(monthMap.get(key).getOneAdd5());
			 * oneA5+=(monthMap.get(key).getOneAdd5());
			 * rown.createCell(5).setCellValue(monthMap.get(key).getTwoAdd1()+
			 * monthMap.get(key).getFourAdd1());
			 * twoA1+=(monthMap.get(key).getTwoAdd1()+monthMap.get(key).
			 * getFourAdd1());
			 * rown.createCell(6).setCellValue(monthMap.get(key).getTwoAdd2()+
			 * monthMap.get(key).getFourAdd2());
			 * twoA2+=(monthMap.get(key).getTwoAdd2()+monthMap.get(key).
			 * getFourAdd2());
			 * rown.createCell(7).setCellValue(monthMap.get(key).getTwoAdd4()+
			 * monthMap.get(key).getFourAdd4());
			 * twoA4+=(monthMap.get(key).getTwoAdd4()+monthMap.get(key).
			 * getFourAdd4());
			 * rown.createCell(8).setCellValue(monthMap.get(key).getTwoAdd5()+
			 * monthMap.get(key).getFourAdd5());
			 * twoA5+=(monthMap.get(key).getTwoAdd5()+monthMap.get(key).
			 * getFourAdd5());
			 * rown.createCell(9).setCellValue(monthMap.get(key).getOneRenew1())
			 * ; oneR1+=(monthMap.get(key).getOneRenew1());
			 * rown.createCell(10).setCellValue(monthMap.get(key).getOneRenew2()
			 * ); oneR2+=(monthMap.get(key).getOneRenew2());
			 * rown.createCell(11).setCellValue(monthMap.get(key).getOneRenew4()
			 * ); oneR4+=(monthMap.get(key).getOneRenew4());
			 * rown.createCell(12).setCellValue(monthMap.get(key).getOneRenew5()
			 * ); oneR5+=(monthMap.get(key).getOneRenew5());
			 * rown.createCell(13).setCellValue(monthMap.get(key).getTwoRenew1()
			 * +monthMap.get(key).getFourRenew1());
			 * twoR1+=(monthMap.get(key).getTwoRenew1()+monthMap.get(key).
			 * getFourRenew1());
			 * rown.createCell(14).setCellValue(monthMap.get(key).getTwoRenew2()
			 * +monthMap.get(key).getFourRenew2());
			 * twoR2+=(monthMap.get(key).getTwoRenew2()+monthMap.get(key).
			 * getFourRenew2());
			 * rown.createCell(15).setCellValue(monthMap.get(key).getTwoRenew4()
			 * +monthMap.get(key).getFourRenew4());
			 * twoR4+=(monthMap.get(key).getTwoRenew4()+monthMap.get(key).
			 * getFourRenew4());
			 * rown.createCell(16).setCellValue(monthMap.get(key).getTwoRenew5()
			 * +monthMap.get(key).getFourRenew5());
			 * twoR5+=(monthMap.get(key).getTwoRenew5()+monthMap.get(key).
			 * getFourRenew5());
			 * rown.createCell(17).setCellValue(monthMap.get(key).
			 * getReplacementLosted());
			 * replacementLosted+=(monthMap.get(key).getReplacementLosted());
			 * rown.createCell(18).setCellValue(monthMap.get(key).
			 * getReplacementDamaged());
			 * replacementDamaged+=(monthMap.get(key).getReplacementDamaged());
			 * rown.createCell(19).setCellValue(monthMap.get(key).
			 * getAlterInfomation());
			 * alterInfomation+=(monthMap.get(key).getAlterInfomation());
			 * 
			 * }
			 * 
			 * HSSFRow rowi = sheet.createRow(k++);
			 * rowi.createCell(0).setCellValue("总计");
			 * rowi.createCell(1).setCellValue(oneA1);
			 * rowi.createCell(2).setCellValue(oneA2);
			 * rowi.createCell(3).setCellValue(oneA4);
			 * rowi.createCell(4).setCellValue(oneA5);
			 * rowi.createCell(5).setCellValue(twoA1);
			 * rowi.createCell(6).setCellValue(twoA2);
			 * rowi.createCell(7).setCellValue(twoA4);
			 * rowi.createCell(8).setCellValue(twoA5);
			 * rowi.createCell(9).setCellValue(oneR1);
			 * rowi.createCell(10).setCellValue(oneR2);
			 * rowi.createCell(11).setCellValue(oneR4);
			 * rowi.createCell(12).setCellValue(oneR5);
			 * rowi.createCell(13).setCellValue(twoR1);
			 * rowi.createCell(14).setCellValue(twoR2);
			 * rowi.createCell(15).setCellValue(twoR4);
			 * rowi.createCell(16).setCellValue(twoR5);
			 * rowi.createCell(17).setCellValue(replacementLosted);
			 * rowi.createCell(18).setCellValue(replacementDamaged);
			 * rowi.createCell(19).setCellValue(alterInfomation);
			 */

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
		HSSFRow row5 = sheet.getRow(5);
		for (String method : payMethods.keySet()) {
			// HSSFRow row4 = sheet.createRow(4);
			
			row5.createCell(curCol).setCellValue(payMethods.get(method));
			// HSSFRow curRow = sheet.createRow(row);
			switch (method) {
			case "1":
				curRow.createCell(curCol).setCellValue(data.getMethodPosCount());
				break;
			case "10":
				curRow.createCell(curCol).setCellValue(data.getMethodMoneyCount());
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

}
