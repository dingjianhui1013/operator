package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.cookie.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigCommercialAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.settle.entity.AgentSettle;
import com.itrus.ca.modules.settle.entity.SettleKey;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;

/**
 * 代理商结算
 * 
 * @author WHW
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/agentSettle")
public class AgentSettleController extends BaseController {

	@Autowired
	private ConfigCommercialAgentService configCommercialAgentService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigCommercialAgentService commercialAgentService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private ConfigProductService configProductService;

	@Autowired
	private ConfigChargeAgentDetailService chargeAgentDetailService;

	@Autowired
	private ConfigChargeAgentService chargeAgentService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private WorkPayInfoService workPayInfoService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@RequestMapping(value = "showTable")
	public String showTable(Long id, Date startTime, Date endDate,
			Long officeId, Model model) {
		List<Object> listSum = new ArrayList<Object>();
		ConfigCommercialAgent commercialAgent = commercialAgentService.get(id);
		model.addAttribute("id", id);
		Office office = null;
		if (officeId != null && officeId > 0) {
			office = officeService.get(officeId);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("title", commercialAgent.getAgentName()
				+ "代理商"
				+ (office != null ? office.getName() + "网点" : "")
				+ (startTime != null && endDate != null ? sdf.format(startTime)
						: "")
				+ (startTime != null && endDate != null ? "至" : "")
				+ (startTime != null && endDate != null ? sdf.format(endDate)
						: "") + "的结算统计");
		model.addAttribute("id", id);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endDate", endDate);
		model.addAttribute("officeId", officeId);
		model.addAttribute("listSum", listSum);
		return "modules/settle/agentSettleShowTableO";
	}

	@RequestMapping(value = "showT")
	public String showT(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agentId", required = false) Long agentId,
			@RequestParam(value = "configApp", required = false) String configApp) {
		// 获取当前登录用户可以管理的区域列表
		List<Office> offsList = officeService.getOfficeByType(
				UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			} else {
				i++;
			}
		}
		// 如果区域ID不为空，则获取当前区域下的网点列表
		if (areaId != null) {
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
		}
		// 2 是劳务代理
		List<ConfigCommercialAgent> ConfigCommercialAgents = configCommercialAgentService
				.findAllNameByType(2);
		Map<Long, String> cAgentList = new HashMap<Long, String>();

		for (ConfigCommercialAgent configCommercialAgent : ConfigCommercialAgents) {
			cAgentList.put(configCommercialAgent.getId(),
					configCommercialAgent.getAgentName());
		}

		model.addAttribute("ConfigCommercialAgents", cAgentList);
		model.addAttribute("agentId", agentId);
		model.addAttribute("offsList", offsList);
		model.addAttribute("officeId", officeId);
		model.addAttribute("areaId", areaId);
		model.addAttribute("startTime",
				startTime == null ? new Date(System.currentTimeMillis() - 30L
						* 24 * 60 * 60 * 1000) : startTime); // 开始时间为空，则为当前时间前30天
		model.addAttribute("endTime", endTime == null ? new Date() : endTime); // 结束时间，默认当前时间
		if (agentId == null) {
			return "modules/settle/agentSettleShowTableT";
		}
		Office office = null;
		if (officeId != null && officeId > 0) {
			office = officeService.get(officeId);
		}

		model.addAttribute(
				"title",
				"【"
						+ cAgentList.get(agentId)
						+ "代理商】"
						+ "【"
						+ (office != null ? office.getName() : "")
						+ "区域】【"
						+ (startTime != null && endTime != null ? DateUtils
								.formatDate(startTime, "yyyy-MM-dd")
								+ "】至"
								+ DateUtils.formatDate(endTime, "yyyy-MM-dd")
								: "") + "】的结算统计");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, 1);
		endTime = calendar.getTime();
		List<Object[]> sum4Agent = workDealInfoService.getSum4Agent(officeId,
				agentId, startTime, endTime);
		Map<Long, AgentSettle> agentList = new HashMap<Long, AgentSettle>();
		// List<WorkDealInfo> list = workDealInfoService.getDealInfo(office,
		// agentId, startTime, endTime);
		// List<AgentSettle> agentList = new ArrayList<AgentSettle>();
		for (Object[] sumInfo : sum4Agent) {
			if (sumInfo[1] != null) {
				AgentSettle agentSettle = agentList.get(Long
						.parseLong(sumInfo[1].toString()));
				if (agentSettle == null) {
					agentSettle = new AgentSettle();
				}
				agentSettle.setConfigApp(configAppService.get(Long
						.parseLong(sumInfo[0].toString())));
				agentSettle.setConfigProduct(configProductService.get(Long
						.parseLong(sumInfo[1].toString())));
				if (WorkDealInfoType.TYPE_ADD_CERT.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setAddSum(agentSettle.getAddSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_UPDATE_CERT.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setUpdateSum(agentSettle.getUpdateSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_LOST_CHILD.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setLostSum(agentSettle.getLostSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_DAMAGED_REPLACED
						.equals(Integer.parseInt(sumInfo[2].toString()))) {
					agentSettle.setReissueSum(agentSettle.getReissueSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_INFORMATION_REROUTE
						.equals(Integer.parseInt(sumInfo[2].toString()))) {
					agentSettle.setChangeSum(agentSettle.getChangeSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_REVOKE_CERT.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setRevokeSum(agentSettle.getRevokeSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_ELECTRONIC_SEAL.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setSealSum(agentSettle.getSealSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				}
				agentList.put(Long.parseLong(sumInfo[1].toString()),
						agentSettle);
			}
		}

		List<Object[]> sum4Agent1 = workDealInfoService.getSumType14Agent(
				officeId, agentId, startTime, endTime);

		// List<WorkDealInfo> list = workDealInfoService.getDealInfo(office,
		// agentId, startTime, endTime);
		// List<AgentSettle> agentList = new ArrayList<AgentSettle>();
		for (Object[] sumInfo : sum4Agent1) {
			if (sumInfo[1] != null) {
				AgentSettle agentSettle = agentList.get(Long
						.parseLong(sumInfo[1].toString()));
				if (agentSettle == null) {
					agentSettle = new AgentSettle();
				}
				agentSettle.setConfigApp(configAppService.get(Long
						.parseLong(sumInfo[0].toString())));
				agentSettle.setConfigProduct(configProductService.get(Long
						.parseLong(sumInfo[1].toString())));
				if (WorkDealInfoType.TYPE_ADD_CERT.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setAddSum(agentSettle.getAddSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_UPDATE_CERT.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setUpdateSum(agentSettle.getUpdateSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_LOST_CHILD.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setLostSum(agentSettle.getLostSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_DAMAGED_REPLACED
						.equals(Integer.parseInt(sumInfo[2].toString()))) {
					agentSettle.setReissueSum(agentSettle.getReissueSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_INFORMATION_REROUTE
						.equals(Integer.parseInt(sumInfo[2].toString()))) {
					agentSettle.setChangeSum(agentSettle.getChangeSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_REVOKE_CERT.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setRevokeSum(agentSettle.getRevokeSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				} else if (WorkDealInfoType.TYPE_ELECTRONIC_SEAL.equals(Integer
						.parseInt(sumInfo[2].toString()))) {
					agentSettle.setSealSum(agentSettle.getSealSum()
							+ Integer.parseInt(sumInfo[3].toString()));
				}
				agentList.put(Long.parseLong(sumInfo[1].toString()),
						agentSettle);
			}
		}
		model.addAttribute("listSum", agentList);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		/*
		 * List<ConfigAgentAppRelation> list =
		 * configAgentAppRelationService.findByAgent(commercialAgent); for
		 * (ConfigAgentAppRelation configAgentAppRelation : list) {
		 * List<ConfigProduct> lis =
		 * configProductService.findByApp(configAgentAppRelation
		 * .getConfigApp().getId()); for (ConfigProduct configProduct : lis) {
		 * Map<String,Object> map = new HashMap<String,Object>();
		 * map.put("appName",
		 * configAgentAppRelation.getConfigApp().getAppName());
		 * map.put("productName",
		 * ProductType.getProductTypeName(Integer.parseInt
		 * (configProduct.getProductName()))); List<WorkDealInfo> addList =
		 * workDealInfoService.getSumInfo(id, WorkDealInfoType.TYPE_ADD_CERT,
		 * configProduct.getProductName(), null, startTime, endDate, officeId);
		 * map.put("addSum", addList.size()); List<WorkDealInfo> updateList =
		 * workDealInfoService.getSumInfo(id, WorkDealInfoType.TYPE_UPDATE_CERT,
		 * configProduct.getProductName(), null, startTime, endDate, officeId);
		 * map.put("updateSum", updateList.size()); List<WorkDealInfo> lostList
		 * = workDealInfoService.getSumInfo(id,
		 * WorkDealInfoType.TYPE_LOST_CHILD, configProduct.getProductName(),
		 * null, startTime, endDate, officeId); map.put("lostSum",
		 * lostList.size()); List<WorkDealInfo> replacedList =
		 * workDealInfoService.getSumInfo(id,
		 * WorkDealInfoType.TYPE_DAMAGED_REPLACED,
		 * configProduct.getProductName(), null, startTime, endDate, officeId);
		 * map.put("reSum", replacedList.size()); List<WorkDealInfo> changeList
		 * = workDealInfoService.getSumInfo(id,
		 * WorkDealInfoType.TYPE_INFORMATION_REROUTE,
		 * configProduct.getProductName(), null, startTime, endDate, officeId);
		 * map.put("changeSum", changeList.size()); List<WorkDealInfo>
		 * revokeList = workDealInfoService.getSumInfo(id,
		 * WorkDealInfoType.TYPE_REVOKE_CERT, configProduct.getProductName(),
		 * null, startTime, endDate, officeId); map.put("revokeSum",
		 * revokeList.size()); List<WorkDealInfo> sealList =
		 * workDealInfoService.getSumInfo(id,
		 * WorkDealInfoType.TYPE_ELECTRONIC_SEAL,
		 * configProduct.getProductName(), null, startTime, endDate, officeId);
		 * map.put("sealSum", sealList.size()); listSum.add(map); } }
		 */

		return "modules/settle/agentSettleShowTableT";
	}

	private AgentSettle getSettle(List<AgentSettle> agentList,
			ConfigProduct product) {
		for (AgentSettle agentSettle : agentList) {
			if (agentSettle.getConfigProduct().getProductName()
					.equals(product.getProductName())) {
				return agentSettle;
			}
		}
		return new AgentSettle();
	}

	/*
	 * 列表显示
	 */
	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping(value = { "list", "" })
	public String list(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agentName", required = false) String agentName,
			@RequestParam(value = "congifApplyId", required = false) Long congifApplyId,
			@RequestParam(value = "productId", required = false) Long productId,
			/* @RequestParam(value="year",required =false) Integer year, */
			@RequestParam(value = "oneYear", required = false) boolean oneYear,
			@RequestParam(value = "twoYear", required = false) boolean twoYear,
			@RequestParam(value = "threeYear", required = false) boolean threeYear,
			@RequestParam(value = "fourYear", required = false) boolean fourYear,
			@RequestParam(value = "fiveYear", required = false) boolean fiveYear,
			@RequestParam(value = "chargeMethodPos", required = false) boolean chargeMethodPos,
			@RequestParam(value = "chargeMethodMoney", required = false) boolean chargeMethodMoney,
			@RequestParam(value = "chargeMethodBank", required = false) boolean chargeMethodBank,
			@RequestParam(value = "chargeMethodGov", required = false) boolean chargeMethodGov,
			@RequestParam(value = "chargeMethodContract", required = false) boolean chargeMethodContract) {
		// 获取当前登录用户被授权的区域。（1：公司；2：部门；3：小组） 此处1 代表地区
		List<Office> offsList = officeService.getOfficeByType(
				UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			} else {
				i++;
			}
		}
		// 如果选中区域，则此处获取区域下的网点
		if (areaId != null) {
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
		}
		// 获取代理商类型 ，1 为代理商 2 为劳务，此处与网点，区域无关
		List<ConfigCommercialAgent> ConfigCommercialAgents = configCommercialAgentService
				.findAllNameByType(1);
		List<String> nameList = new ArrayList<String>();
		for (ConfigCommercialAgent configCommercialAgent : ConfigCommercialAgents) {
			nameList.add(configCommercialAgent.getAgentName());
		}

		ConfigApp configApp = null;
		ConfigProduct configPro = null;
		List<ConfigAgentAppRelation> configAgentAppRelationlist = new ArrayList<ConfigAgentAppRelation>();
		List<ConfigProduct> configProducts = new ArrayList<ConfigProduct>();
		if (congifApplyId != null) {
			configApp = configAppService.get(congifApplyId);
			configProducts = configProductService.findByApp(congifApplyId);
			model.addAttribute("configProducts", configProducts);
		}
		List<ConfigApp> configApps = configAppService.selectAll();
		System.out.println(nameList.size());
		model.addAttribute("ConfigCommercialAgents", nameList);
		
		model.addAttribute("agentName", agentName);
		model.addAttribute("offsList", offsList);
		model.addAttribute("officeId", officeId);
		model.addAttribute("areaId", areaId);
		model.addAttribute("startTime",
				startTime == null ? new Date(System.currentTimeMillis() - 30L
						* 24 * 60 * 60 * 1000) : startTime); // 开始时间为空，则为当前时间前30天
		model.addAttribute("endTime", endTime == null ? new Date() : endTime); // 结束时间，默认当前时间
		model.addAttribute("configApps", configApps);
		model.addAttribute("congifApplyId", congifApplyId);
		model.addAttribute("productId", productId);
		model.addAttribute("productType", ProductType.productTypeStrMap);

		if (agentName == null) {
			return "modules/settle/agentSettleList";
		}
		List<Object> listSum = new ArrayList<Object>();
		Office office = null;
		if (officeId != null && officeId > 0) {
			office = officeService.get(officeId);
		}

		List<ConfigCommercialAgent> configCommercialAgents = commercialAgentService
				.findByName(agentName);
		Long[] agentIds = new Long[configCommercialAgents.size()];
		int i = 0;
		for (ConfigCommercialAgent agent : configCommercialAgents) {
			agentIds[i] = agent.getId();
			i++;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("title", agentName
				+ "代理商"
				+ (office != null ? "-" + office.getName() : "")
				+ "："
				+ (startTime != null && endTime != null ? sdf.format(startTime)
						+ "至" + sdf.format(endTime) : "") + "的结算统计");
		/*
		 * List<ConfigAgentAppRelation> list =
		 * configAgentAppRelationService.findByAgent(agentIds);
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, 1);
		endTime = calendar.getTime();

		configAgentAppRelationlist = configAgentAppRelationService
				.findByAgentAndApp(agentIds, configApp);
		if (productId != null) {
			configProducts = new ArrayList<ConfigProduct>();
			configPro = configProductService.get(productId);
			configProducts.add(configPro);
		}
		int colspan = 0;
		if (!(oneYear || twoYear || threeYear || fourYear || fiveYear)) {// 年限都不选的话，默认四个都选
			oneYear = true;
			twoYear = true;
			threeYear = true;
			fourYear = true;
			fiveYear = true;
		}
		if (!(chargeMethodPos || chargeMethodMoney || chargeMethodBank
				|| chargeMethodGov || chargeMethodContract)) {// 支付方式都不选的话，默认五个都选
			chargeMethodPos = true;
			chargeMethodMoney = true;
			chargeMethodBank = true;
			chargeMethodGov = true;
			chargeMethodContract = true;
		}
		if (oneYear) {
			colspan = colspan + 2;
		}
		if (twoYear) {
			colspan = colspan + 2;
		}
		if(threeYear)
		{
			colspan = colspan + 2;
		}
		if (fourYear) {
			colspan = colspan + 2;
		}

		if (fiveYear) {
			colspan = colspan + 2;
		}

		for (ConfigAgentAppRelation configAgentAppRelation : configAgentAppRelationlist) {
			if (congifApplyId == null) {
				configProducts = new ArrayList<ConfigProduct>();
				configProducts = configProductService
						.findByApp(configAgentAppRelation.getConfigApp()
								.getId());
			}
			for (ConfigProduct configProduct : configProducts) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (configApp == null) {
					// configProducts = null;
					configApp = configProduct.getConfigApp();// configAgentAppRelation.getConfigApp();
				}

				map.put("appName", configApp.getAppName());
				map.put("appId", configApp.getId());
				map.put("productName", ProductType.getProductTypeName(Integer
						.parseInt(configProduct.getProductName())));
				map.put("productLabel",
						configProduct.getProductLabel() == 0 ? "通用" : "专用");
				map.put("productId", configProduct.getId());
				// 初始化数据
				map.put("oneSum", null);
				map.put("oneMoney", null);
				map.put("twoSum", null);
				map.put("twoMoney", null);
				map.put("threeSum", null);
				map.put("threeMoney", null);
				map.put("fourSum", null);
				map.put("fourMoney", null);
				map.put("fiveSum", null);
				map.put("fiveMoney", null);

				map.put("oneSum1", null);
				map.put("oneMoney1", null);
				map.put("threeSum1", null);
				map.put("threeMoney1", null);
				map.put("twoSum1", null);
				map.put("twoMoney1", null);
				map.put("fourSum1", null);
				map.put("fourMoney1", null);
				map.put("fiveSum1", null);
				map.put("fiveMoney1", null);

				// 新增一年的业务集合
				Double oneSubTotal = 0D;
				if (oneYear) {//
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 1, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("oneSum",
							Long.parseLong(((Object[]) resList.get(0))[0]
									.toString()));
					Double oneMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 1);
					map.put("oneMoney", oneMoney == null ? 0 : oneMoney);
					oneSubTotal = Double
							.parseDouble(((Object[]) resList.get(0))[1]
									.toString());
				}
				Double twoSubTotal = 0D;
				// 新增两年的业务集合
				if (twoYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 2, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("twoSum",
							Long.parseLong(((Object[]) resList.get(0))[0]
									.toString()));
					Double twoMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 2);
					map.put("twoMoney", twoMoney == null ? 0 : twoMoney);
					twoSubTotal = Double
							.parseDouble(((Object[]) resList.get(0))[1]
									.toString());

				}
				Double threeSubTotal = 0D;
				if (threeYear) {
					// 新增三年的业务集合
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 3, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("threeSum",
							Long.parseLong(((Object[]) resList.get(0))[0]
									.toString()));
					Double threeMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 3);
					map.put("threeMoney", threeMoney == null ? 0 : threeMoney);
					threeSubTotal = Double
							.parseDouble(((Object[]) resList.get(0))[1]
									.toString());

				}
				Double fourSubTotal = 0D;
				if (fourYear) {
					// 新增四年的业务集合
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 4, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("fourSum", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double fourMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 4);
					map.put("fourMoney", fourMoney == null ? 0 : fourMoney);
					fourSubTotal = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());
				}
				Double fiveSubTotal = 0D;
				if (fiveYear) {
					// 新增五年的业务集合
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 5, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("fiveSum", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double fiveMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 5);
					map.put("fiveMoney", fiveMoney == null ? 0 : fiveMoney);
					fiveSubTotal = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());
				}
				map.put("subTotal", oneSubTotal + twoSubTotal +threeSubTotal+ fourSubTotal
						+ fiveSubTotal);

				Double oneSubTotal1 = 0D;
				if (oneYear) {
					// 更新一年的业务集合
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 1, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("oneSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double oneMoney1 = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, 1);
					map.put("oneMoney1", oneMoney1 == null ? 0 : oneMoney1);
					oneSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());

				}

				// 更新两年的业务集合
				Double twoSubTotal1 = 0D;
				if (twoYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 2, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("twoSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double twoMoney1 = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, 2);
					map.put("twoMoney1", twoMoney1 == null ? 0 : twoMoney1);
					twoSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());

				}
				// 更新三年的业务集合
				Double threeSubTotal1 = 0D;
				if (threeYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 3, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("threeSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double threeMoney1 = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, 3);
					map.put("threeMoney1", threeMoney1 == null ? 0 : threeMoney1);
					threeSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());

				}
				// 更新四年的业务集合
				Double fourSubTotal1 = 0D;
				if (fourYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 4, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("fourSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double fourMoney1 = chargeAgentDetailService
							.getChargeMoney(configProduct.getChargeAgentId(),
									WorkDealInfoType.TYPE_UPDATE_CERT, 4);
					map.put("fourMoney1", fourMoney1 == null ? 0 : fourMoney1);
					fourSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());
				}
				// 更新五年的业务集合
				Double fiveSubTotal1 = 0D;
				if (fiveYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 5, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("fiveSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double fiveMoney1 = chargeAgentDetailService
							.getChargeMoney(configProduct.getChargeAgentId(),
									WorkDealInfoType.TYPE_UPDATE_CERT, 5);
					map.put("fiveMoney1", fiveMoney1 == null ? 0 : fiveMoney1);
					fiveSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());
				}
				map.put("subTotal1", oneSubTotal1 + twoSubTotal1 +threeSubTotal1
						+ fourSubTotal1 + fiveSubTotal1);
				listSum.add(map);
				configApp = null;
			}
		}
		model.addAttribute("listSum", listSum); 
		model.addAttribute("colspan", colspan);
		model.addAttribute("oneYear", oneYear);
		model.addAttribute("twoYear", twoYear);
		model.addAttribute("threeYear", threeYear);
		model.addAttribute("fourYear", fourYear);
		model.addAttribute("fiveYear", fiveYear);
		model.addAttribute("chargeMethodPos", chargeMethodPos);
		model.addAttribute("chargeMethodMoney", chargeMethodMoney);
		model.addAttribute("chargeMethodBank", chargeMethodBank);
		model.addAttribute("chargeMethodGov", chargeMethodGov);
		model.addAttribute("chargeMethodContract", chargeMethodContract);
		return "modules/settle/agentSettleList";
	}

	/*
	 * 导出excel ，数据处理部分 下个版本考虑与list 方法合并
	 */
	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping(value = { "exportData", "" })
	public void exportData(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agentName", required = false) String agentName,
			@RequestParam(value = "congifApplyId", required = false) Long congifApplyId,
			@RequestParam(value = "productId", required = false) Long productId,
			/* @RequestParam(value="year",required =false) Integer year, */
			@RequestParam(value = "oneYear", required = false) boolean oneYear,
			@RequestParam(value = "twoYear", required = false) boolean twoYear,
			@RequestParam(value = "threeYear", required = false) boolean threeYear,
			@RequestParam(value = "fourYear", required = false) boolean fourYear,
			@RequestParam(value = "fiveYear", required = false) boolean fiveYear,
			@RequestParam(value = "chargeMethodPos", required = false) boolean chargeMethodPos,
			@RequestParam(value = "chargeMethodMoney", required = false) boolean chargeMethodMoney,
			@RequestParam(value = "chargeMethodBank", required = false) boolean chargeMethodBank,
			@RequestParam(value = "chargeMethodGov", required = false) boolean chargeMethodGov,
			@RequestParam(value = "chargeMethodContract", required = false) boolean chargeMethodContract) {

		ConfigApp configApp = null;
		ConfigProduct configPro = null;
		List<ConfigAgentAppRelation> configAgentAppRelationlist = new ArrayList<ConfigAgentAppRelation>();
		List<ConfigProduct> configProducts = new ArrayList<ConfigProduct>();
		if (congifApplyId != null) {
			configApp = configAppService.get(congifApplyId);
			configProducts = configProductService.findByApp(congifApplyId);
		}

		List<Object> listSum = new ArrayList<Object>();
		Office office = null;
		if (officeId != null && officeId > 0) {
			office = officeService.get(officeId);
		}

		List<ConfigCommercialAgent> configCommercialAgents = commercialAgentService
				.findByName(agentName);
		Long[] agentIds = new Long[configCommercialAgents.size()];
		int i = 0;
		for (ConfigCommercialAgent agent : configCommercialAgents) {
			agentIds[i] = agent.getId();
			i++;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String title = agentName
				+ "代理商"
				+ (office != null ? "-" + office.getName() : "")
				+ "："
				+ (startTime != null && endTime != null ? sdf.format(startTime)
						+ "至" + sdf.format(endTime) : "") + "的结算统计";

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, 1);
		endTime = calendar.getTime();

		configAgentAppRelationlist = configAgentAppRelationService
				.findByAgentAndApp(agentIds, configApp);
		if (productId != null) {
			configProducts = new ArrayList<ConfigProduct>();
			configPro = configProductService.get(productId);
			configProducts.add(configPro);
		}

		int colspan = 0;
		if (!(oneYear || twoYear || threeYear || fourYear || fiveYear)) {// 年限都不选的话，默认四个都选
			oneYear = true;
			twoYear = true;
			threeYear = true;
			fourYear = true;
			fiveYear = true;
			
		}
		if (!(chargeMethodPos || chargeMethodMoney || chargeMethodBank
				|| chargeMethodGov || chargeMethodContract)) {// 支付方式都不选的话，默认五个都选
			chargeMethodPos = true;
			chargeMethodMoney = true;
			chargeMethodBank = true;
			chargeMethodGov = true;
			chargeMethodContract = true;
		}
		if (oneYear) {
			colspan = colspan + 2;
		}
		if (twoYear) {
			colspan = colspan + 2;
		}
		if (threeYear) {
			colspan = colspan + 2;
		}
		if (fourYear) {
			colspan = colspan + 2;
		}

		if (fiveYear) {
			colspan = colspan + 2;
		}

		for (ConfigAgentAppRelation configAgentAppRelation : configAgentAppRelationlist) {
			if (congifApplyId == null) {
				configProducts = new ArrayList<ConfigProduct>();
				configProducts = configProductService
						.findByApp(configAgentAppRelation.getConfigApp()
								.getId());
			}
			for (ConfigProduct configProduct : configProducts) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (configApp == null) {
					configApp = configProduct.getConfigApp();// configAgentAppRelation.getConfigApp();
				}

				map.put("appName", configApp.getAppName());
				map.put("appId", configApp.getId());
				map.put("productName", ProductType.getProductTypeName(Integer
						.parseInt(configProduct.getProductName())));
				map.put("productLabel",
						configProduct.getProductLabel() == 0 ? "通用" : "专用");
				map.put("productId", configProduct.getId());
				// 初始化数据
				map.put("oneSum", null);
				map.put("oneMoney", null);
				map.put("twoSum", null);
				map.put("twoMoney", null);
				map.put("threeSum", null);
				map.put("threeMoney", null);
				map.put("fourSum", null);
				map.put("fourMoney", null);
				map.put("fiveSum", null);
				map.put("fiveMoney", null);

				map.put("oneSum1", null);
				map.put("oneMoney1", null);
				map.put("threeSum1", null);
				map.put("threeMoney1", null);
				map.put("twoSum1", null);
				map.put("twoMoney1", null);
				map.put("fourSum1", null);
				map.put("fourMoney1", null);
				map.put("fiveSum1", null);
				map.put("fiveMoney1", null);
				
				// 新增一年的业务集合
				Double oneSubTotal = 0D;
				if (oneYear) {//
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 1, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("oneSum",
							Long.parseLong(((Object[]) resList.get(0))[0]
									.toString()));
					Double oneMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 1);
					map.put("oneMoney", oneMoney == null ? 0 : oneMoney);
					oneSubTotal = Double
							.parseDouble(((Object[]) resList.get(0))[1]
									.toString());
				}
				Double twoSubTotal = 0D;
				// 新增两年的业务集合
				if (twoYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 2, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("twoSum",
							Long.parseLong(((Object[]) resList.get(0))[0]
									.toString()));
					Double twoMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 2);
					map.put("twoMoney", twoMoney == null ? 0 : twoMoney);
					twoSubTotal = Double
							.parseDouble(((Object[]) resList.get(0))[1]
									.toString());

				}
				Double threeSubTotal = 0D;
				if (threeYear) {
					// 新增三年的业务集合
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 3, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("threeSum",
							Long.parseLong(((Object[]) resList.get(0))[0]
									.toString()));
					Double threeMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 3);
					map.put("threeMoney", threeMoney == null ? 0 : threeMoney);
					threeSubTotal = Double
							.parseDouble(((Object[]) resList.get(0))[1]
									.toString());

				}
				Double fourSubTotal = 0D;
				if (fourYear) {
					// 新增四年的业务集合
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 4, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("fourSum", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double fourMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 4);
					map.put("fourMoney", fourMoney == null ? 0 : fourMoney);
					fourSubTotal = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());
				}
				Double fiveSubTotal = 0D;
				if (fiveYear) {
					// 新增五年的业务集合
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(), WorkDealInfoType.TYPE_ADD_CERT,
							configProduct.getId(), 5, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("fiveSum", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double fiveMoney = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_ADD_CERT, 5);
					map.put("fiveMoney", fiveMoney == null ? 0 : fiveMoney);
					fiveSubTotal = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());
				}
				map.put("subTotal", oneSubTotal + twoSubTotal +threeSubTotal+ fourSubTotal
						+ fiveSubTotal);

				Double oneSubTotal1 = 0D;
				if (oneYear) {
					// 更新一年的业务集合
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 1, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("oneSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double oneMoney1 = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, 1);
					map.put("oneMoney1", oneMoney1 == null ? 0 : oneMoney1);
					oneSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());

				}

				// 更新两年的业务集合
				Double twoSubTotal1 = 0D;
				if (twoYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 2, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("twoSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double twoMoney1 = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, 2);
					map.put("twoMoney1", twoMoney1 == null ? 0 : twoMoney1);
					twoSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());

				}
				// 更新三年的业务集合
				Double threeSubTotal1 = 0D;
				if (threeYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 3, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("threeSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double threeMoney1 = chargeAgentDetailService.getChargeMoney(
							configProduct.getChargeAgentId(),
							WorkDealInfoType.TYPE_UPDATE_CERT, 3);
					map.put("threeMoney1", threeMoney1 == null ? 0 : threeMoney1);
					threeSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());

				}
				// 更新四年的业务集合
				Double fourSubTotal1 = 0D;
				if (fourYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 4, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("fourSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double fourMoney1 = chargeAgentDetailService
							.getChargeMoney(configProduct.getChargeAgentId(),
									WorkDealInfoType.TYPE_UPDATE_CERT, 4);
					map.put("fourMoney1", fourMoney1 == null ? 0 : fourMoney1);
					fourSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());
				}
				// 更新五年的业务集合
				Double fiveSubTotal1 = 0D;
				if (fiveYear) {
					List<Object[]> resList = workDealInfoService.getSumCount(
							configApp.getId(),
							WorkDealInfoType.TYPE_UPDATE_CERT,
							configProduct.getId(), 5, startTime, endTime,
							officeId, agentIds[0], chargeMethodPos,
							chargeMethodMoney, chargeMethodBank,
							chargeMethodGov, chargeMethodContract);
					map.put("fiveSum1", Long.parseLong(((Object[]) resList
							.get(0))[0].toString()));
					Double fiveMoney1 = chargeAgentDetailService
							.getChargeMoney(configProduct.getChargeAgentId(),
									WorkDealInfoType.TYPE_UPDATE_CERT, 5);
					map.put("fiveMoney1", fiveMoney1 == null ? 0 : fiveMoney1);
					fiveSubTotal1 = Double.parseDouble(((Object[]) resList
							.get(0))[1].toString());
				}
				map.put("subTotal1", oneSubTotal1 + twoSubTotal1 +threeSubTotal1
						+ fourSubTotal1 + fiveSubTotal1);
				listSum.add(map);
				configApp = null;
			}
		}
		try {

			Workbook wb = new HSSFWorkbook();// 定义工作簿
			CellStyle style = wb.createCellStyle(); // 样式对象
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			Cell cell = null;
			Sheet sheet = wb.createSheet("供应商结算统计");
			sheet.setDefaultColumnWidth(10);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
					(colspan - 1) * 2 + 5));// 合并单元格
			Font font = wb.createFont();
			// 第一行数据
			Row row0 = sheet.createRow(0);
			cell = row0.createCell(0);
			Font font0 = wb.createFont();
			CellStyle style0 = wb.createCellStyle();
			font0.setFontHeightInPoints((short) 14);
			style0.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			style0.setFont(font0);

			cell.setCellStyle(style0);
			cell.setCellValue(title); // 标题

			sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));// 合并单元格
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));// 合并单元格
			Row row2 = sheet.createRow(1);
			cell = row2.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("应用名称");
			cell = row2.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue("产品名称");
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 1 + colspan));// 合并单元格
			cell = row2.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue("新增数量及价格");
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 2 + colspan,
					2 + colspan));// 合并单元格
			cell = row2.createCell(2 + colspan);
			cell.setCellStyle(style);
			cell.setCellValue("实收(元)");
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 3 + colspan,
					2 + colspan * 2));// 合并单元格
			cell = row2.createCell(3 + colspan);
			cell.setCellStyle(style);
			cell.setCellValue("更新数量及价格");
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 3 + colspan * 2,
					3 + colspan * 2));// 合并单元格
			cell = row2.createCell(3 + colspan * 2);
			cell.setCellStyle(style);
			cell.setCellValue("实收(元)");

			int curCol = 2; // 第三列，从0开始数
			row2 = sheet.createRow(2);
			if (oneYear) {
				cell = row2.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue("一年数量");
				cell = row2.createCell(curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				cell = row2.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue("一年数量");
				cell = row2.createCell(1 + colspan + curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				curCol = curCol + 2;
			}
			if (twoYear) {
				cell = row2.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue("两年数量");
				cell = row2.createCell(curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				cell = row2.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue("两年数量");
				cell = row2.createCell(1 + colspan + curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				curCol = curCol + 2;
			}
			if (threeYear) {
				cell = row2.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue("三年数量");
				cell = row2.createCell(curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				cell = row2.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue("三年数量");
				cell = row2.createCell(1 + colspan + curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				curCol = curCol + 2;
			}
			if (fourYear) {
				cell = row2.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue("四年数量");
				cell = row2.createCell(curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				cell = row2.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue("四年数量");
				cell = row2.createCell(1 + colspan + curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				curCol = curCol + 2;
			}
			if (fiveYear) {
				cell = row2.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue("五年数量");
				cell = row2.createCell(curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				cell = row2.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue("五年数量");
				cell = row2.createCell(1 + colspan + curCol + 1);
				cell.setCellStyle(style);
				cell.setCellValue("单价(元)");
				curCol = curCol + 2;
			}

			int last = 0;
			long addOneSum =0;
			long addTwoSum =0;
			long addThreeSum =0;
			long addFourSum =0;
			long addFiveSum =0;
			double addTotal =0;
			long updateOneSum=0;
			long updateTwoSum =0;
			long updateThreeSum =0;
			long updateFourSum=0;
			long updateFiveSum=0;
			double updateTotal=0;
			
			for (i = 0; i < listSum.size(); i++) {
				Map m = (Map) listSum.get(i);
				Row row = sheet.createRow(i + 3);
				cell = row.createCell(0);
				cell.setCellStyle(style);
				cell.setCellValue((String) m.get("appName"));
				cell = row.createCell(1);
				cell.setCellStyle(style);
				String sName = (String) m.get("productName")
						+ "(" +(String) m.get("productLabel") +")";
				cell.setCellValue(sName);
				curCol = 2; // 第三列，从0开始数

				if (oneYear) {
					cell = row.createCell(curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("oneSum"));
					cell = row.createCell(curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("oneMoney"));
					cell = row.createCell(1 + colspan + curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("oneSum1"));
					cell = row.createCell(1 + colspan + curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("oneMoney1"));
					curCol = curCol + 2;
					addOneSum = addOneSum+ (Long)m.get("oneSum");
					updateOneSum= updateOneSum+(Long)m.get("oneSum1");
				}
				if (twoYear) {
					cell = row.createCell(curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("twoSum"));
					cell = row.createCell(curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("twoMoney"));
					cell = row.createCell(1 + colspan + curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("twoSum1"));
					cell = row.createCell(1 + colspan + curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("twoMoney1"));
					curCol = curCol + 2;
					addTwoSum = addTwoSum + (Long)m.get("twoSum");
					updateTwoSum= updateTwoSum + (Long)m.get("twoSum1");
				}
				if (threeYear) {
					cell = row.createCell(curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("threeSum"));
					cell = row.createCell(curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("threeMoney"));
					cell = row.createCell(1 + colspan + curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("threeSum1"));
					cell = row.createCell(1 + colspan + curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("threeMoney1"));
					curCol = curCol + 2;
					addThreeSum = addThreeSum + (Long)m.get("threeSum");
					updateThreeSum= updateThreeSum + (Long)m.get("threeSum1");
				}
				if (fourYear) {
					cell = row.createCell(curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("fourSum"));
					cell = row.createCell(curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("fourMoney"));
					cell = row.createCell(1 + colspan + curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("fourSum1"));
					cell = row.createCell(1 + colspan + curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("fourMoney1"));
					curCol = curCol + 2;
					addFourSum = addFourSum + (Long)m.get("fourSum");
					updateFourSum= updateFourSum + (Long)m.get("fourSum1");
				}
				if (fiveYear) {
					cell = row.createCell(curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("fiveSum"));
					cell = row.createCell(curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("fiveMoney"));
					cell = row.createCell(1 + colspan + curCol);
					cell.setCellStyle(style);
					cell.setCellValue((Long)m.get("fiveSum1"));
					cell = row.createCell(1 + colspan + curCol + 1);
					cell.setCellStyle(style);
					cell.setCellValue((Double)m.get("fiveMoney1"));
					curCol = curCol + 2;
					addFiveSum = addFiveSum + (Long)m.get("fiveSum");
					updateFiveSum= updateFiveSum + (Long)m.get("fiveSum1");
				}
				cell = row.createCell(2 + colspan); // 新办实收
				cell.setCellStyle(style);
				cell.setCellValue((Double)m.get("subTotal"));
				addTotal = addTotal + (Double)m.get("subTotal");
				cell = row.createCell(3 + colspan * 2); //更新实收
				cell.setCellStyle(style);
				cell.setCellValue((Double)m.get("subTotal1"));
				updateTotal = updateTotal + (Double)m.get("subTotal1");
				last = i + 3;
			}
			Row rowl = sheet.createRow(last + 1);
			cell = rowl.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("合计：");
			
			curCol = 2; // 第三列，从0开始数

			if (oneYear) {
				cell = rowl.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue(addOneSum);				
				cell = rowl.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue(updateOneSum);
				curCol = curCol + 2;
			}
			if (twoYear) {
				cell = rowl.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue(addTwoSum);				
				cell = rowl.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue(updateTwoSum);	
				curCol = curCol + 2;
			}
			if (threeYear) {
				cell = rowl.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue(addThreeSum);				
				cell = rowl.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue(updateThreeSum);	
				curCol = curCol + 2;
			}
			if (fourYear) {
				cell = rowl.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue(addFourSum);				
				cell = rowl.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue(updateFourSum);	
				curCol = curCol + 2;
			}
			if (fiveYear) {
				cell = rowl.createCell(curCol);
				cell.setCellStyle(style);
				cell.setCellValue(addFiveSum);
				cell = rowl.createCell(1 + colspan + curCol);
				cell.setCellStyle(style);
				cell.setCellValue(updateFiveSum);	
				curCol = curCol + 2;
			}
			cell = rowl.createCell(2 + colspan); // 新办实收
			cell.setCellStyle(style);
			cell.setCellValue(addTotal);			
			cell = rowl.createCell(3 + colspan * 2); //更新实收
			cell.setCellStyle(style);
			cell.setCellValue(updateTotal);

			
			rowl = sheet.createRow(last + 2);
			cell = rowl.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("总计：");
			sheet.addMergedRegion(new CellRangeAddress(last + 2, last + 2, 1,
					(colspan - 1) * 2 + 5));// 合并单元格
			cell = rowl.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue(addTotal+updateTotal + "元");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=AgentSettleExport.xls");
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

	@RequiresPermissions("profile:configCommercialAgent:view")
	@RequestMapping(value = "showDealInfo")
	public String showDealInfo(Long appId, Long productId,
			Integer dealInfoType, Integer year, String startTime,
			String endTime, String agentName, String type,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			agentName = URLDecoder.decode(agentName, "UTF-8");
			startTime = URLDecoder.decode(startTime, "UTF-8");
			endTime = URLDecoder.decode(endTime, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Date startDate = new Date(startTime);
		startDate.setHours(00);
		startDate.setMinutes(00);
		startDate.setSeconds(00);
		Date endDate = new Date(endTime);
		endDate.setHours(00);
		endDate.setMinutes(00);
		endDate.setSeconds(00);

		if (type.equals("TG")) {
			Page<WorkDealInfo> page = workDealInfoService.findDealInfos(
					new Page<WorkDealInfo>(request, response), appId,
					productId, dealInfoType, year, agentName, startDate,
					endDate, true);
			model.addAttribute("page", page);
		}
		if (type.equals("LW")) {
			Page<WorkDealInfo> page = workDealInfoService.findDealInfos(
					new Page<WorkDealInfo>(request, response), appId,
					productId, dealInfoType, null, agentName, startDate,
					endDate, false);
			model.addAttribute("page", page);
		}
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		return "modules/settle/agentSettleShowDealInfoList";
	}

	// 通过AJAX查询应用下的所有产品
	@RequestMapping(value = "getProductName")
	@ResponseBody
	public String listProducts(Long applyId) throws JSONException {
		JSONObject json = new org.json.JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<ConfigProduct> configProducts = configProductService
					.findByApp(applyId);
			for (ConfigProduct configProduct : configProducts) {
				json = new JSONObject();
				json.put("id", configProduct.getId());
				ProductType productType = new ProductType();
				String product = productType.getProductTypeName(Integer
						.parseInt(configProduct.getProductName()));
				String ProdLable = configProduct.getProductLabel() == 0 ? "通用"
						: "专用";
				json.put("name", product + "(" + ProdLable + ")");
				array.put(json);
			}
			// 检查是否有权限操作
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return array.toString();
	}

}
