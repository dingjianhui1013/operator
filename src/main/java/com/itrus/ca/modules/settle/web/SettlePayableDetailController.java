package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigCommercialAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.settle.vo.PayableDetailVo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;

/**
 * 年限结算表
 * 
 * @author huhao
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/settlePayableDetail")
public class SettlePayableDetailController extends BaseController {
	@Autowired
	private ConfigCommercialAgentService configCommercialAgentService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private ConfigProductService configProductService;
	@Autowired
	private WorkFinancePayInfoRelationService workFinancePayInfoRelationService;

	@RequiresPermissions("work:settlePayableDetail:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String settlePayableDetailList(Model model) {
		List<ConfigCommercialAgent> comAgents = configCommercialAgentService.findAllNameByType(1);
		model.addAttribute("comAgents", comAgents);
		model.addAttribute("startTime", DateUtils.firstDayOfMonth(new Date()));
		model.addAttribute("endTime", new Date());
		return "modules/settle/settlePayableDetail";
	}

	@RequiresPermissions("work:settlePayableDetail:view")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public String settlePayableDetailList(HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime

	) {
		List<ConfigCommercialAgent> comAgents = configCommercialAgentService.findAllNameByType(1);
		model.addAttribute("comAgents", comAgents);
		if (comAgentId == null) {
			return "modules/settle/settlePayableDetail";
		}
		model.addAttribute("appId", appId);
		model.addAttribute("productIds", productIds);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("comAgentId", comAgentId);
		model.addAttribute("proType", ProductType.productTypeStrMap);

		List<ConfigAgentAppRelation> relationByComAgentId = configAgentAppRelationService.findByComAgentId(comAgentId);
		model.addAttribute("relationByComAgentId", relationByComAgentId);

		if (appId != null) {
			List<ConfigProduct> products = configProductService.findByAppId(appId);
			model.addAttribute("products", products);
		}

		ConfigCommercialAgent comAgent = configCommercialAgentService.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId == null) {
			List<ConfigAgentAppRelation> relation = configAgentAppRelationService.findByComAgentId(comAgentId);
			for (int i = 0; i < relation.size(); i++) {
				appIds.add(relation.get(i).getConfigApp().getId());
			}
		}
		List<Long> productIdList = new ArrayList<Long>();
		if (productIds != null && !productIds.equals("")) {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				productIdList.add(Long.parseLong(products[i].toString()));
			}
		}
		model.addAttribute("productIdList", productIdList);
		Date start = new Date();
		Date end = new Date();
		if (startTime != null && !startTime.equals("")) {
			if (comAgent.getAgentContractStart().getTime() > startTime.getTime()) {
				start = new Date(comAgent.getAgentContractStart().getTime());
			} else {
				start = startTime;
			}
		} else {
			start = new Date(comAgent.getAgentContractStart().getTime());
		}
		if (endTime != null && !endTime.equals("")) {
			if (comAgent.getAgentContractEnd().getTime() < startTime.getTime()) {
				start = new Date(comAgent.getAgentContractStart().getTime());
			} else {
				start = startTime;
			}
		} else {

			end = new Date(comAgent.getAgentContractEnd().getTime());
		}
		List<WorkDealInfo> dealInfos = workDealInfoService.findDealInfo(appId, appIds, productIdList, start, end);

		Integer lenth = 0;
		for (int i = 0; i < dealInfos.size(); i++) {
			WorkDealInfo dealInfo = dealInfos.get(i);
			int totalAgentYear = comAgent.getSettlementPeriod();
			List<WorkDealInfo> infos = new ArrayList<WorkDealInfo>();
			infos.add(dealInfo);
			WorkDealInfo info = workDealInfoService.findDealInfo(dealInfo.getId());
			if (info != null) {
				infos.add(info);
			}
			while (info != null) {
				info = workDealInfoService.findDealInfo(info.getId());
				if (info != null) {
					infos.add(info);
				}
			}
			List<PayableDetailVo> detailList = new ArrayList<PayableDetailVo>();

			Date endLastDate = new Date();
			Calendar calendar = Calendar.getInstance();

			calendar.setTime(dealInfo.getBusinessCardUserDate());
			calendar.add(Calendar.YEAR, totalAgentYear);
			endLastDate = calendar.getTime();

			int yjNum = 0;
			int lastNum = 0;

			for (int j = 0; j < infos.size(); j++) {
				WorkDealInfo prvedDealInfo = infos.get(j);
				if (infos.get(j).getNotafter()==null) {
					continue;
				}
				
					PayableDetailVo detailVo = new PayableDetailVo();
					if (prvedDealInfo.getPayType()==null) {
						detailVo.setMethod(1);
					}else{
						detailVo.setMethod(prvedDealInfo.getPayType());
					}
					String dealInfoType = "";
					if (infos.get(j).getDealInfoType() != null) {
						dealInfoType = WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType())
								+ " ";
					}
					if (infos.get(j).getDealInfoType1() != null) {
						dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType1())
								+ " ";
					}
					if (infos.get(j).getDealInfoType2() != null) {
						dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType2())
								+ " ";
					}
					if (infos.get(j).getDealInfoType3() != null) {
						dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType3());
					}

					if (prvedDealInfo.getPayType() == null) {
						detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
						detailVo.setEndDate(infos.get(j).getNotafter());
						detailVo.setDealInfoType(dealInfoType);
						detailVo.setSettleYear("0");
						detailList.add(detailVo);
						continue;
					}
					if (!prvedDealInfo.getPayType().equals(1)) {
						detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
						detailVo.setEndDate(infos.get(j).getNotafter());
						detailVo.setDealInfoType(dealInfoType);
						detailVo.setSettleYear("0");
						detailList.add(detailVo);
						continue;
					}

					if (infos.get(j).getDealInfoType() != null) {
						if (infos.get(j).getDealInfoType().equals(1) || infos.get(j).getDealInfoType().equals(0)) {
							if (infos.get(j).getBusinessCardUserDate() != null) {
								if (infos.get(j).getBusinessCardUserDate().getTime() > endLastDate.getTime()) {
									detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
									detailVo.setEndDate(infos.get(j).getNotafter());
									detailVo.setDealInfoType(dealInfoType);
									detailVo.setSettleYear("0");
									detailList.add(detailVo);
								} else if (infos.get(j).getNotafter().getTime() < endLastDate.getTime()) {
									yjNum += infos.get(j).getYear();
									lastNum = infos.get(j).getYear();
									detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
									detailVo.setEndDate(infos.get(j).getNotafter());
									detailVo.setDealInfoType(dealInfoType);
									detailVo.setSettleYear(infos.get(j).getYear().toString());
									detailList.add(detailVo);
								} else if (infos.get(j).getBusinessCardUserDate().getTime() < endLastDate.getTime()
										&& infos.get(j).getNotafter().getTime() > endLastDate.getTime()) {
									long between = endLastDate.getTime()
											- infos.get(j).getBusinessCardUserDate().getTime();
									long a = between / 31536000000L;
									int yy = (int) Math.ceil(a);
									yjNum += yy;
									lastNum = yy;
									detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
									detailVo.setEndDate(infos.get(j).getNotafter());
									detailVo.setDealInfoType(dealInfoType);
									detailVo.setSettleYear(yy + "");
									detailList.add(detailVo);
								}
							}
						}
				}
			}

			dealInfos.get(i).setTotalNum(totalAgentYear);

			if (totalAgentYear == 0) {
				for (int k = 0; k < detailList.size(); k++) {
					detailList.get(k).setSettleYear("0");
				}
				dealInfos.get(i).setYyNum(0);
				dealInfos.get(i).setLastNum(0);

			} else {
				Integer ava = totalAgentYear;
				if (detailList.size() > 0) {
					for (int k = 0; k < detailList.size(); k++) {
						if (ava > 0) {
							int settle = Integer.parseInt(detailList.get(k).getSettleYear());

							if (ava > settle) {
								ava -= settle;
							} else {
								detailList.get(k).setSettleYear(ava.toString());
								ava = 0;
							}
						} else {
							detailList.get(k).setSettleYear("0");
						}
					}

					dealInfos.get(i)
							.setLastNum(Integer.parseInt(detailList.get(detailList.size() - 1).getSettleYear()));
					int yNum=0;
					for (int k = 0;  k < detailList.size()-1; k++) {
						yNum += Integer.parseInt(detailList.get(k).getSettleYear());
					}
					
					dealInfos.get(i).setYyNum(yNum);
				}
			}

			dealInfos.get(i).setDetailList(detailList);
			if (detailList.size() > lenth) {
				lenth = detailList.size()-1;
			}
		}

		for (int k = dealInfos.size() - 1; k >= 0; k--) {
			WorkDealInfo deal = dealInfos.get(k);
			List<PayableDetailVo> detailVos = deal.getDetailList();
			int payType=0;
			if (detailVos.size() < 1) {
				dealInfos.remove(k);
			}else{
				for (int j = detailVos.size()-1; j >=0 ; j--) {
					if (detailVos.get(j).getMethod().equals(1)) {
						payType=1;
						break;
					}
				}
			}
			if (payType == 0) {
				dealInfos.remove(k);
			}
		}
		model.addAttribute("dealInfos", dealInfos);
		model.addAttribute("lenth", lenth);
		return "modules/settle/settlePayableDetail";
	}

	@RequestMapping(value = "setApps")
	@ResponseBody
	public String setApps(Long comAgentId) throws JSONException {
		List<ConfigAgentAppRelation> relation = configAgentAppRelationService.findByComAgentId(comAgentId);

		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < relation.size(); i++) {
			json = new JSONObject();
			json.put("id", relation.get(i).getConfigApp().getId());
			json.put("name", relation.get(i).getConfigApp().getAppName());
			array.put(json);
		}
		return array.toString();
	}

	@RequestMapping(value = "setProducts")
	@ResponseBody
	public String setProducts(Long appId) throws JSONException {
		List<ConfigProduct> products = configProductService.findByAppId(appId);
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < products.size(); i++) {
			json = new JSONObject();
			json.put("id", products.get(i).getId());
			System.out.println(products.get(i).getId() + "===="
					+ ProductType.productTypeStrMap.get(products.get(i).getProductName()));
			json.put("name", ProductType.productTypeStrMap.get(products.get(i).getProductName()));
			array.put(json);
		}
		return array.toString();
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(System.currentTimeMillis());
		calendar.setTime(date);
		// calendar.add(Calendar.WEEK_OF_YEAR, -1);
		calendar.add(Calendar.YEAR, 5);
		date = calendar.getTime();
		System.out.println(date);
	}

	@RequestMapping(value = "dcPayableDetail")
	public void dcPayableDetail(HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime) {

		ConfigCommercialAgent comAgent = configCommercialAgentService.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId == null) {
			List<ConfigAgentAppRelation> relation = configAgentAppRelationService.findByComAgentId(comAgentId);
			for (int i = 0; i < relation.size(); i++) {
				appIds.add(relation.get(i).getConfigApp().getId());
			}
		}
		List<Long> productIdList = new ArrayList<Long>();
		if (productIds != null && !productIds.equals("")) {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				productIdList.add(Long.parseLong(products[i].toString()));
			}
		}
		model.addAttribute("productIdList", productIdList);
		Date start = new Date();
		Date end = new Date();
		if (startTime != null && !startTime.equals("")) {
			if (comAgent.getAgentContractStart().getTime() > startTime.getTime()) {
				start = new Date(comAgent.getAgentContractStart().getTime());
			} else {
				start = startTime;
			}
		} else {
			start = new Date(comAgent.getAgentContractStart().getTime());
		}
		if (endTime != null && !endTime.equals("")) {
			if (comAgent.getAgentContractEnd().getTime() < startTime.getTime()) {
				start = new Date(comAgent.getAgentContractStart().getTime());
			} else {
				start = startTime;
			}
		} else {

			end = new Date(comAgent.getAgentContractEnd().getTime());
		}
		List<WorkDealInfo> dealInfos = workDealInfoService.findDealInfo(appId, appIds, productIdList, start, end);

		Integer lenth = 0;

		for (int i = 0; i < dealInfos.size(); i++) {
			WorkDealInfo dealInfo = dealInfos.get(i);
			int totalAgentYear = comAgent.getSettlementPeriod();
			List<WorkDealInfo> infos = new ArrayList<WorkDealInfo>();
			infos.add(dealInfo);
			WorkDealInfo info = workDealInfoService.findDealInfo(dealInfo.getId());
			if (info != null) {
				infos.add(info);
			}
			while (info != null) {
				info = workDealInfoService.findDealInfo(info.getId());
				if (info != null) {
					infos.add(info);
				}
			}
			List<PayableDetailVo> detailList = new ArrayList<PayableDetailVo>();

			Date endLastDate = new Date();
			Calendar calendar = Calendar.getInstance();

			calendar.setTime(dealInfo.getBusinessCardUserDate());
			calendar.add(Calendar.YEAR, totalAgentYear);
			endLastDate = calendar.getTime();

			int yjNum = 0;
			int lastNum = 0;

			for (int j = 0; j < infos.size(); j++) {
				WorkDealInfo prvedDealInfo = infos.get(j);
				if (infos.get(j).getNotafter()==null) {
					continue;
				}
				
				PayableDetailVo detailVo = new PayableDetailVo();
				detailVo.setMethod(prvedDealInfo.getPayType());
				String dealInfoType = "";
				if (infos.get(j).getDealInfoType() != null) {
					dealInfoType = WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType()) + " ";
				}
				if (infos.get(j).getDealInfoType1() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType1()) + " ";
				}
				if (infos.get(j).getDealInfoType2() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType2()) + " ";
				}
				if (infos.get(j).getDealInfoType3() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew.get(infos.get(j).getDealInfoType3());
				}

				if (prvedDealInfo.getPayType() == null) {
					detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
					detailVo.setEndDate(infos.get(j).getNotafter());
					detailVo.setDealInfoType(dealInfoType);
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}
				if (!prvedDealInfo.getPayType().equals(1)) {

					detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
					detailVo.setEndDate(infos.get(j).getNotafter());
					detailVo.setDealInfoType(dealInfoType);
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}

				if (infos.get(j).getDealInfoType() != null) {
					if (infos.get(j).getDealInfoType().equals(1) || infos.get(j).getDealInfoType().equals(0)) {
						if (infos.get(j).getBusinessCardUserDate() != null) {
							if (infos.get(j).getBusinessCardUserDate().getTime() > endLastDate.getTime()) {
								detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
								detailVo.setEndDate(infos.get(j).getNotafter());
								detailVo.setDealInfoType(dealInfoType);
								detailVo.setSettleYear("0");
								detailList.add(detailVo);
							} else if (infos.get(j).getNotafter().getTime() < endLastDate.getTime()) {
								yjNum += infos.get(j).getYear();
								lastNum = infos.get(j).getYear();
								detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
								detailVo.setEndDate(infos.get(j).getNotafter());
								detailVo.setDealInfoType(dealInfoType);
								detailVo.setSettleYear(infos.get(j).getYear().toString());
								detailList.add(detailVo);
							} else if (infos.get(j).getBusinessCardUserDate().getTime() < endLastDate.getTime()
									&& infos.get(j).getNotafter().getTime() > endLastDate.getTime()) {
								long between = endLastDate.getTime() - infos.get(j).getBusinessCardUserDate().getTime();
								long a = between / 31536000000L;
								int yy = (int) Math.ceil(a);
								yjNum += yy;
								lastNum = yy;
								detailVo.setStartDate(infos.get(j).getBusinessCardUserDate());
								detailVo.setEndDate(infos.get(j).getNotafter());
								detailVo.setDealInfoType(dealInfoType);
								detailVo.setSettleYear(yy + "");
								detailList.add(detailVo);
							}
						}
					}
				}
			}
			dealInfos.get(i).setTotalNum(totalAgentYear);

			if (totalAgentYear == 0) {
				for (int k = 0; k < detailList.size(); k++) {
					detailList.get(k).setSettleYear("0");
				}
				dealInfos.get(i).setYyNum(0);
				dealInfos.get(i).setLastNum(0);

			} else {
				Integer ava = totalAgentYear;
				if (detailList.size() > 0) {
					for (int k = 0; k < detailList.size(); k++) {
						if (ava > 0) {
							int settle = Integer.parseInt(detailList.get(k).getSettleYear());

							if (ava > settle) {
								ava -= settle;
							} else {
								detailList.get(k).setSettleYear(ava.toString());
								ava = 0;
							}
						} else {
							detailList.get(k).setSettleYear("0");
						}
					}

					dealInfos.get(i)
							.setLastNum(Integer.parseInt(detailList.get(detailList.size() - 1).getSettleYear()));
					int yNum=0;
					for (int k = 0;  k < detailList.size()-1; k++) {
						yNum += Integer.parseInt(detailList.get(k).getSettleYear());
					}
					
					dealInfos.get(i).setYyNum(yNum);
				}
			}

			dealInfos.get(i).setDetailList(detailList);
			if (detailList.size() > lenth) {
				lenth = detailList.size()-1;
			}
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("年限结算表");
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 20);

		style.setFont(font);
		// sheet.addMergedRegion(new Region(0,(short)0, 0,(short)
		// (2*monthlist1.size()+3)));
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue("统计周期:" + format.format(startTime) + "-" + format.format(endTime));
		// row0.setHeightInPoints(40);
		cell0.setCellStyle(style);

		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("本期结算证书年限(本次结算年数总数)");

		HSSFRow row2 = sheet.createRow(2);
		row2.createCell(0).setCellValue("序号");
		row2.createCell(1).setCellValue("单位名称");
		row2.createCell(2).setCellValue("经办人姓名");
		row2.createCell(3).setCellValue("产品名称");

		for (int i = 0; i < lenth; i++) {
			row2.createCell(4 + 5 * i).setCellValue("第" + (i + 1) + "次结算");
		}

		row2.createCell(5 * lenth + 4).setCellValue("结算年限统计");

		HSSFRow row3 = sheet.createRow(3);

		for (int i = 0; i < lenth; i++) {
			row3.createCell(i * 5 + 4).setCellValue("缴费类型");
			row3.createCell(i * 5 + 4 + 1).setCellValue("起始时间");
			row3.createCell(i * 5 + 4 + 2).setCellValue("结束时间");
			row3.createCell(i * 5 + 4 + 3).setCellValue("业务类型");
			row3.createCell(i * 5 + 4 + 4).setCellValue("结算(年)");
		}

		row3.createCell(5 * lenth + 4).setCellValue("已结算(年)");
		row3.createCell(5 * lenth + 4 + 1).setCellValue("本期结算(年)");
		row3.createCell(5 * lenth + 4 + 2).setCellValue("剩余结算(年)");

		for (int i = 4; i < dealInfos.size() + 4; i++) {
			HSSFRow rowi = sheet.createRow(i);

			rowi.createCell(0).setCellValue(i - 4 + 1);
			rowi.createCell(1).setCellValue(dealInfos.get(i - 4).getWorkCompany().getCompanyName());
			rowi.createCell(2).setCellValue(dealInfos.get(i - 4).getWorkCertInfo().getWorkCertApplyInfo().getName());
			rowi.createCell(3).setCellValue(
					ProductType.productTypeStrMap.get(dealInfos.get(i - 4).getConfigProduct().getProductName()));

			for (int j = 0; j < dealInfos.get(i - 4).getDetailList().size(); j++) {
				rowi.createCell(j * 5 + 4).setCellValue(dealInfos.get(i - 4).getDetailList().get(j).getMethod() == null
						? "" : dealInfos.get(i - 4).getDetailList().get(j).getMethod().toString());
				rowi.createCell(j * 5 + 4 + 1)
						.setCellValue(dealInfos.get(i - 4).getDetailList().get(j).getStartDate() == null ? ""
								: dealInfos.get(i - 4).getDetailList().get(j).getStartDate().toString());
				rowi.createCell(j * 5 + 4 + 2)
						.setCellValue(dealInfos.get(i - 4).getDetailList().get(j).getEndDate() == null ? ""
								: dealInfos.get(i - 4).getDetailList().get(j).getEndDate().toString());
				rowi.createCell(j * 5 + 4 + 3)
						.setCellValue(dealInfos.get(i - 4).getDetailList().get(j).getDealInfoType() == null
								|| dealInfos.get(i - 4).getDetailList().get(j).getDealInfoType().equals("") ? ""
										: dealInfos.get(i - 4).getDetailList().get(j).getDealInfoType());
				rowi.createCell(j * 5 + 4 + 4)
						.setCellValue(dealInfos.get(i - 4).getDetailList().get(j).getSettleYear() == null ? ""
								: dealInfos.get(i - 4).getDetailList().get(j).getSettleYear());
			}

			rowi.createCell(5 * lenth + 4)
					.setCellValue(dealInfos.get(i - 4).getYyNum() - dealInfos.get(i - 4).getLastNum());
			rowi.createCell(5 * lenth + 4 + 1).setCellValue(dealInfos.get(i - 4).getLastNum());
			rowi.createCell(5 * lenth + 4 + 2)
					.setCellValue(dealInfos.get(i - 4).getTotalNum() - dealInfos.get(i - 4).getYyNum());
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition", "attachment; filename=dcPayableDetail.xls");
			wb.write(baos);
			byte[] bytes = baos.toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(bytes);
			bos.close();
			baos.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
