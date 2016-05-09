/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.AdminPinEncKey;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.utils.excel.ExportExcel;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.log.entity.SysOperateLog;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.dao.ConfigAppDao;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigProjectType;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.entity.ProductTypeObj;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigProjectTypeService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountExtendInfoService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountService;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.self.service.SelfImageService;
import com.itrus.ca.modules.service.ItrustProxyUtil;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealCountStatistics;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;
import com.itrus.ca.modules.work.vo.WorkDate_MoneVo;
import com.itrus.ca.modules.work.vo.WorkDealInfoListVo;
import com.itrus.ca.modules.work.vo.WorkDealInfoVo;
import com.itrus.ca.modules.work.vo.Workoffice_MoneyVo;
import com.itrus.ca.modules.work.vo.Workoffice_district_MoneyVo;
import com.itrus.ca.modules.work.vo.WorkpaymentInfo_dealinfoVo;

/**
 ************************************************************** 
 ** 
 ** @author ZhangJingtao
 ** 
 **         date:2014年8月12日 上午11:31:54
 ** 
 ** @describe:业务办理controller
 ** 
 ************************************************************** 
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workDealInfo")
public class WorkDealInfoController extends BaseController {

	@Autowired
	private WorkUserService workUserService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private WorkCompanyService workCompanyService;

	@Autowired
	private WorkLogService workLogService;

	private LogUtil logUtil = new LogUtil();

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private WorkPayInfoService workPayInfoService;

	@Autowired
	private WorkFinancePayInfoRelationService workFinancePayInfoRelationService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private ConfigProductService configProductService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;

	@Autowired
	private FinancePaymentInfoService financePaymentInfoService;

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private WorkCertApplyInfoService workCertApplyInfoService;

	@Autowired
	private WorkCertInfoService workCertInfoService;

	@Autowired
	private WorkCompanyHisService workCompanyHisService;

	@Autowired
	private WorkUserHisService workUserHisService;

	@Autowired
	private ConfigAgentOfficeRelationService configAgentOfficeRelationService;

	@Autowired
	private KeyGeneralInfoService generalInfoService;

	@Autowired
	private ConfigChargeAgentService chargeAgentService;

	@Autowired
	private ConfigRaAccountExtendInfoService configRaAccountExtendInfoService;

	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;

	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;

	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;

	@Autowired
	private ConfigRaAccountService raAccountService;

	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;

	@Autowired
	private ConfigProjectTypeService configProjectTypeService;

	@Autowired
	private SelfImageService selfImageService;
	
	
	@Value(value = "${ixin.url}")
	private String ixinUrl;

	@Value(value = "${ixin.key}")
	String key;

	@ModelAttribute
	public WorkDealInfo get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return workDealInfoService.get(id);
		} else {
			return new WorkDealInfo();
		}
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(WorkDealInfo workDealInfo, Date startTime, Date endTime, Date makeCertStartTime,
			Date makeCertEndTime, HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes, @RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "alias", required = false) Long alias,
			@RequestParam(value = "productName", required = false) String productName) throws ParseException {
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());

		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("alias", alias);

		Page<WorkDealInfoListVo> page = workDealInfoService.find4Apply(new Page<WorkDealInfoListVo>(request, response),
				workDealInfo, startTime, endTime, alias, productName, makeCertStartTime, makeCertEndTime);

		if (checkIds != null) {
			String[] ids = checkIds.split(",");
			model.addAttribute("ids", ids);
		}
		model.addAttribute("checkIds", checkIds);

		// List<WorkDealInfo> noIxinInfos = page.getList();
		// List<WorkDealInfo> isIxinInfos =
		// workDealInfoService.find4ApplyIsIxin(workDealInfo, startTime,
		// endTime, alias);
		// noIxinInfos.addAll(isIxinInfos);
		// page.setList(noIxinInfos);

		model.addAttribute("workType", workDealInfo.getDealInfoStatus());

		//model.addAttribute("proList", ProductType.getProductTypeList());
		model.addAttribute("proList", ProductType.getProductList());
		
		
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("page", page);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("productName", productName);
		model.addAttribute("makeCertStartTime", makeCertStartTime);
		model.addAttribute("makeCertEndTime", makeCertEndTime);
		return "modules/work/workDealInfoList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "deleteList")
	public String deleteList(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agentId", required = false) Long agentId) {
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());

		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);

		Page<WorkDealInfo> page = workDealInfoService.findByBatchAdd(new Page<WorkDealInfo>(request, response),
				workDealInfo, startTime, endTime, agentId);

		if (checkIds != null) {
			String[] ids = checkIds.split(",");
			model.addAttribute("ids", ids);
		}

		List<ConfigChargeAgent> agents = configChargeAgentService.findAll();

		model.addAttribute("agents", agents);
		model.addAttribute("agentId", agentId);
		model.addAttribute("checkIds", checkIds);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("workType", workDealInfo.getDealInfoStatus());
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("page", page);
		return "modules/work/workDealInfoDeleteList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "deleteByDealInfoId")
	public String deleteList(Long dealInfoId, Model model, RedirectAttributes redirectAttributes) {

		WorkDealInfo workDealInfo = workDealInfoService.get(dealInfoId);
		ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
		agent.setSurplusNum(agent.getSurplusNum() + 1);
		agent.setReserveNum(agent.getReserveNum() - 1);
		configChargeAgentService.save(agent);
		ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agent.getId(), dealInfoId);
		if (bound != null) {
			configAgentBoundDealInfoService.deleteById(bound.getId());
		}

		workDealInfoService.delete(dealInfoId);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/deleteList";
	}

	@RequestMapping(value = "deleteDealInfoIds")
	@ResponseBody
	public String deleteDealInfoIds(String dealInfoIds) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			String[] dealInfos = dealInfoIds.split(",");
			List<Long> dealIdList = new ArrayList<Long>();
			for (int i = 0; i < dealInfos.length; i++) {
				if (!dealInfos[i].equals("")) {
					dealIdList.add(Long.parseLong(dealInfos[i]));
				}
			}
			for (int i = 0; i < dealIdList.size(); i++) {
				WorkDealInfo workDealInfo = workDealInfoService.get(dealIdList.get(i));
				ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
				agent.setSurplusNum(agent.getSurplusNum() + 1);
				agent.setReserveNum(agent.getReserveNum() - 1);
				configChargeAgentService.save(agent);
				ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agent.getId(),
						dealIdList.get(i));
				if (bound != null) {
					configAgentBoundDealInfoService.deleteById(bound.getId());
				}
				workDealInfoService.delete(dealIdList.get(i));
			}
			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 0);
		}
		return json.toString();
	}

	// 批量导入
	@RequestMapping("addAttach")
	@ResponseBody
	public String importFile(@RequestParam(value = "appId", required = true) Long appId,
			@RequestParam(value = "dealInfoTypeT", required = true) String dealInfoType,
			@RequestParam(value = "productT", required = true) String productT,
			@RequestParam(value = "lableT", required = true) String lableT,
			@RequestParam(value = "agentIdT", required = true) Integer agentIdT,
			@RequestParam(value = "agentDetailIdT", required = true) Long agentDetailIdT,
			@RequestParam(value = "yearT", required = true) Integer yearT,
			@RequestParam(value = "fileName", required = true) MultipartFile file)
					throws IllegalStateException, IOException, JSONException {
		JSONObject json = new JSONObject();
		String ifExcel = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));// 截取.xlsx或.xls
		if (!(ifExcel.equals(".xls") || ifExcel.equals(".xlsx"))) {
			json.put("status", -1);
			json.put("msg", "模板必须为Excel文件");
			return json.toString();
		}
		try {
			json = workDealInfoService.saveExcelDate(appId, dealInfoType, productT, lableT, agentIdT, agentDetailIdT,
					yearT, file, ifExcel);// 解析存储excel文件
		} catch (Exception ex) {
			json.put("status", -1);
			json.put("msg", json.toString());
			return json.toString();
		}
		return json.toString();
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "financeList")
	public String listFinancePaymentInfo(FinancePaymentInfo financePaymentInfo, 
			String appName,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			HttpServletRequest request, HttpServletResponse response, Long financePaymentInfoId, Model model) {

		List<ConfigApp> appNames = configAppService.findall();
		Page<WorkFinancePayInfoRelation> financePay = workFinancePayInfoRelationService
				.findByFinance(new Page<WorkFinancePayInfoRelation>(request, response), financePaymentInfoId, appName,startTime,endTime);
		if (financePay.getCount() != 0) {
			List<Long> idList = Lists.newArrayList();
			if (financePay.getList().size() > 0) {
				for (int i = 0; i < financePay.getList().size(); i++) {
					idList.add(financePay.getList().get(i).getWorkPayInfo().getId());
				}
			}

			Page<WorkDealInfo> page = workDealInfoService.findByFinanceId(new Page<WorkDealInfo>(request, response),
					idList);
			for (int i = 0; i < page.getList().size(); i++) {
				List<WorkFinancePayInfoRelation> financePayOne = workFinancePayInfoRelationService
						.findByFinancePay(financePaymentInfoId, page.getList().get(i).getWorkPayInfo().getId());

				page.getList().get(i).setMoneyDouble(financePayOne.get(0).getMoney());

				int proName = Integer.parseInt(page.getList().get(i).getConfigProduct().getProductName());
				String proNames = ProductType.getProductTypeName(proName);
				int proLabel = page.getList().get(i).getConfigProduct().getProductLabel();
				if (proLabel == 1) {
					proNames += "(专用)";
				} else {
					proNames += "(通用)";
				}
				
				page.getList().get(i).setCertSn(proNames);
				
				model.addAttribute("pro", ProductType.productTypeStrMap);
				model.addAttribute("infoType", WorkDealInfoType.WorkDealInfoTypeMap);

			}
			model.addAttribute("count", page.getCount());
			model.addAttribute("page", page);
		} else {
			model.addAttribute("count", 0);
			model.addAttribute("page", financePay);
		}
		
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		
		model.addAttribute("appNames", appNames);
		model.addAttribute("financePaymentInfoId", financePaymentInfoId);
		return "modules/work/workDealInfoListForFinancePaymnetInfo";
	}

	// 获取统计信息列表
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "StatisticalDealPayList")
	public String StatisticalDealPayList(WorkDealInfo workDealInfo, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "payMethod", required = false) String payMethod,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "appId", required = false) Long appId, Model model) {
		User user = UserUtils.getUser();
		// 获取前台的付款方式
		// List<Long> method = Lists.newArrayList();
		if (payMethod != null) {
			// String[] methods = payMethod.split(",");
			// method = Lists.newArrayList();
			// for (int i = 0; i < methods.length; i++) {
			// method.add(Long.parseLong(methods[i]));
			// }

			WorkPayInfo workPayInfo = new WorkPayInfo();
			// for (int i = 0; i < methods.length; i++) {
			if (payMethod.equals("1")) {
				workPayInfo.setMethodMoney(true);
				// continue;
			}
			if (payMethod.equals("2")) {
				workPayInfo.setMethodPos(true);
				// continue;
			}
			if (payMethod.equals("3")) {
				workPayInfo.setMethodBank(true);
				// continue;
			}
			if (payMethod.equals("4")) {
				workPayInfo.setMethodAlipay(true);
				// continue;
			}
			if (payMethod.equals("5")) {
				workPayInfo.setMethodGov(true);
				// continue;
			}
			if (payMethod.equals("6")) {
				workPayInfo.setMethodContract(true);
				// continue;
			}
			// }
			workDealInfo.setWorkPayInfo(workPayInfo);
		}

		List<Long> dealInfoByOfficeAreaIds = Lists.newArrayList();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		Map<Long, Set<String>> Id_paymethod = new HashMap<Long, Set<String>>();
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);
			List<Long> appids = Lists.newArrayList();
			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService.findAllByOfficeId(officeId);// 通过网店获取引用的id
			if (appOffices.size() > 0) {
				for (int i = 0; i < appOffices.size(); i++) {
					appids.add(appOffices.get(i).getConfigApp().getId());
				}
			} else {
				appids.add(-1l);
			}
			List<WorkDealInfo> deals = workDealInfoService.findByAppId(appids);// 根据应用id获取dealInfo信息
			if (deals.size() < 1) {
				dealInfoByOfficeAreaIds.add(-1l);
			} else {
				for (int i = 0; i < deals.size(); i++) {
					dealInfoByOfficeAreaIds.add(deals.get(i).getId());
				}
			}
		} else {
			if (area != null) {
				List<Long> appids = Lists.newArrayList();
				// List<Long> officeids = Lists.newArrayList();
				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}

				List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService.findAllByOfficeId(officeids);// 根据网店id获取应用id
				if (appOffices.size() > 0) {
					for (int i = 0; i < appOffices.size(); i++) {
						appids.add(appOffices.get(i).getConfigApp().getId());
					}
				} else {
					appids.add(-1l);
				}

				List<WorkDealInfo> deals = workDealInfoService.findByAppId(appids);// 根据应用id获取dealInfo信息
				if (deals.size() < 1) {
					dealInfoByAreaIds.add(-1l);
				} else {
					for (int i = 0; i < deals.size(); i++) {
						dealInfoByAreaIds.add(deals.get(i).getId());
					}
				}
			}
		}

		List<Long> idList = Lists.newArrayList();// 根据产品名称查询出支付信息
		Page<WorkDealInfo> page = workDealInfoService.findByDealPay(new Page<WorkDealInfo>(request, response),
				workDealInfo, startTime, endTime, idList, dealInfoByAreaIds, dealInfoByOfficeAreaIds, appId, officeids);
		if (page.getList().size() > 0) {
			for (int i = 0; i < page.getList().size(); i++) {
				if (page.getList().get(i).getWorkPayInfo().getRelationMethod() != null) {
					List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
							.findByPayInfoId(page.getList().get(i).getWorkPayInfo().getId());
					Set<String> payMethods = new LinkedHashSet<String>();
					for (int w = 0; w < workfinancePayinfos.size(); w++) {
						if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
							payMethods.add("现金付款  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
							payMethods.add("POS付款  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
							payMethods.add("银行转账  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
							payMethods.add("支付宝付款  ");
						}
					}
					Id_paymethod.put(page.getList().get(i).getId(), payMethods);
				}
			}
		}
		if (area != null && officeId != null) {
			WorkDealCountStatistics dealCountSta = new WorkDealCountStatistics();

			Office areaOffice = officeService.get(area);
			dealCountSta.setAreaName(areaOffice.getName());
			Office offOffice = officeService.get(officeId);
			dealCountSta.setOfficeName(offOffice.getName());
			int posCount = 0;
			int moneyCount = 0;
			int bankCount = 0;
			int alipayCount = 0;
			int govCount = 0;
			int contractCount = 0;
			double posCountMoney = 0L;
			double moneyCountMoney = 0L;
			double bankCountMoney = 0L;
			double alipayCountMoney = 0L;

			if (page.getList().size() > 0) {
				for (int i = 0; i < page.getList().size(); i++) {
					if (page.getList().get(i).getWorkPayInfo().getRelationMethod() == null) {
						if (page.getList().get(i).getWorkPayInfo().getMethodPos() == true) {
							posCount += 1;
							if (page.getList().get(i).getWorkPayInfo().getPosMoney() != null) {
								if (page.getList().get(i).getWorkPayInfo().getPosMoney() > 0) {
									posCountMoney += page.getList().get(i).getWorkPayInfo().getPosMoney();
								} else {
									posCountMoney += (-page.getList().get(i).getWorkPayInfo().getPosMoney());
								}
							}
						}
						if (page.getList().get(i).getWorkPayInfo().getMethodBank() == true) {
							bankCount += 1;
							if (page.getList().get(i).getWorkPayInfo().getBankMoney() != null) {
								if (page.getList().get(i).getWorkPayInfo().getBankMoney() > 0) {
									bankCountMoney += page.getList().get(i).getWorkPayInfo().getBankMoney();
								} else {
									bankCountMoney += (-page.getList().get(i).getWorkPayInfo().getBankMoney());
								}
							}
						}
						if (page.getList().get(i).getWorkPayInfo().getMethodMoney() == true) {
							moneyCount += 1;
							if (page.getList().get(i).getWorkPayInfo().getMoney() != null) {
								if (page.getList().get(i).getWorkPayInfo().getMoney() > 0) {
									moneyCountMoney += page.getList().get(i).getWorkPayInfo().getMoney();
								} else {
									moneyCountMoney += (-page.getList().get(i).getWorkPayInfo().getMoney());
								}
							}
						}
						if (page.getList().get(i).getWorkPayInfo().getMethodAlipay() == true) {
							alipayCount += 1;
							if (page.getList().get(i).getWorkPayInfo().getAlipayMoney() != null) {
								if (page.getList().get(i).getWorkPayInfo().getAlipayMoney() > 0) {
									alipayCountMoney += page.getList().get(i).getWorkPayInfo().getAlipayMoney();
								} else {
									alipayCountMoney += (-page.getList().get(i).getWorkPayInfo().getAlipayMoney());
								}
							}
						}
						if (page.getList().get(i).getWorkPayInfo().getMethodGov() != null) {
							if (page.getList().get(i).getWorkPayInfo().getMethodGov() == true) {
								govCount += 1;
							}
						}
						if (page.getList().get(i).getWorkPayInfo().getMethodContract() != null) {
							if (page.getList().get(i).getWorkPayInfo().getMethodContract() == true) {
								contractCount += 1;
							}
						}
					} else {
						List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
								.findByPayInfoId(page.getList().get(i).getWorkPayInfo().getId());
						Set<String> payMethods = new LinkedHashSet<String>();
						for (int w = 0; w < workfinancePayinfos.size(); w++) {
							if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
								payMethods.add("现金付款");
								// moneyCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									if (workfinancePayinfos.get(w).getMoney() > 0) {
										moneyCountMoney += workfinancePayinfos.get(w).getMoney();
									} else {
										moneyCountMoney += (-workfinancePayinfos.get(w).getMoney());
									}
								}
							}
							if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
								payMethods.add("POS付款");
								// posCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									if (workfinancePayinfos.get(w).getMoney() > 0) {
										posCountMoney += workfinancePayinfos.get(w).getMoney();
									} else {
										posCountMoney += (-workfinancePayinfos.get(w).getMoney());
									}
								}
							}
							if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
								payMethods.add("银行转账");
								// bankCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									if (workfinancePayinfos.get(w).getMoney() > 0) {
										bankCountMoney += workfinancePayinfos.get(w).getMoney();
									} else {
										bankCountMoney += (-workfinancePayinfos.get(w).getMoney());
									}
								}
							}
							if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
								payMethods.add("支付宝付款");
								// alipayCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									if (workfinancePayinfos.get(w).getMoney() > 0) {
										alipayCountMoney += workfinancePayinfos.get(w).getMoney();
									} else {
										alipayCountMoney += (-workfinancePayinfos.get(w).getMoney());
									}
								}
							}
						}
						Object payMehtod[] = payMethods.toArray();
						for (Object object : payMehtod) {
							if (object.equals("现金付款")) {
								moneyCount += 1;
							}
							if (object.equals("POS付款")) {
								posCount += 1;
							}
							if (object.equals("银行转账")) {
								bankCount += 1;
							}
							if (object.equals("支付宝付款")) {
								alipayCount += 1;
							}
						}
						Id_paymethod.put(page.getList().get(i).getId(), payMethods);
					}
				}
			}
			dealCountSta.setAlipayCount(alipayCount);
			dealCountSta.setMoneyCount(moneyCount);
			dealCountSta.setPosCount(posCount);
			dealCountSta.setBankCount(bankCount);
			dealCountSta.setGovCount(govCount);
			dealCountSta.setContractCount(contractCount);

			model.addAttribute("dealCountSta", dealCountSta);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dealMsg = (startTime != null && endTime != null ? sdf.format(startTime) + "至" + sdf.format(endTime)
					: "本次统计") + areaOffice.getName() + "区域" + offOffice.getName() + "现金" + moneyCountMoney + "元，POS收款"
					+ posCountMoney + "元，银行转账" + bankCountMoney + "元，支付宝转账" + alipayCountMoney + "元";
			model.addAttribute("dealMsg", dealMsg);
			model.addAttribute("Id_paymethod", Id_paymethod);

		}

		List<Office> offsList = officeService.getOfficeByType(user, 1);

		List<Office> offices = officeService.getOfficeByType(user, 2);
		if (offices.size() == 1) {
			model.addAttribute("offsListSize", 1);
			model.addAttribute("offices", offices);
		}

		if (area != null) {
			model.addAttribute("areaId", area);

			List<Office> offs = officeService.findByParentId(area);
			model.addAttribute("offices", offs);
			if (officeId != null) {
				model.addAttribute("office", officeId);
			}
		}

		List<ConfigApp> appList = configAppService.findAllConfigApp();
		model.addAttribute("appId", appId);
		model.addAttribute("appList", appList);
		// model.addAttribute("method", method);
		model.addAttribute("offsList", offsList);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("payMethod", payMethod);
		model.addAttribute("page", page);
		model.addAttribute("Id_paymethod", Id_paymethod);
		List<Office> offs = officeService.findall();
		model.addAttribute("offs", offs);
		return "modules/work/statisticalDealPayList";
	}

	/**
	 * @param workDealInfo
	 * @param startTime
	 * @param endTime
	 * @param area
	 * @param appId
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "StatisticalDayList")
	public String StatisticalDayList(WorkDealInfo workDealInfo, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office, Model model) throws ParseException {
		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		// List<String> payMethod = Lists.newArrayList();
		// List<Double> money = Lists.newArrayList();
		Map<String, List<String>> office_payMethod = new HashMap<>();
		Map<String, List<Double>> office_money = new HashMap<>();
		if (appId == null || startTime == null || endTime == null) {// 首次进入，或者条件不全
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			model.addAttribute("offsList", offsList);
			List<ConfigApp> appList = configAppService.findAllConfigApp();
			model.addAttribute("appList", appList);
			model.addAttribute("startTime", startTime == null ? DateUtils.firstDayOfMonth(new Date()) : startTime);
			model.addAttribute("endTime", endTime == null ? new Date() : endTime);
			return "modules/work/statisticalDayList";
		}
		if (area != null && office != null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			officeids.add(office);
		} else if (area != null && office == null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			for (Office o : offices) {
				officeids.add(o.getId());
			}

		} else { //

			List<Office> offsList = officeService.getOfficeByType(user, 1);
			List<Long> areas = Lists.newArrayList();
			if (offsList.size() > 0) {
				for (int i = 0; i < offsList.size(); i++) {
					areas.add(offsList.get(i).getId());
				}
			} else {
				areas.add(-1l);
			}
			List<Office> offices = officeService.findByParentIds(areas);// 根据区域id获取网点id
			if (offices.size() > 0) {
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}
		List<WorkDealInfo> list = workDealInfoService.findByDayPay(startTime, endTime, officeids, appId);
		if (list != null) {
			List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
			List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
			Set<String> month = new LinkedHashSet<String>();
			for (int i = 0; i < list.size(); i++) {
				String a = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getWorkPayInfo().getCreateDate());
				month.add(a);
			}
			Object months[] = month.toArray();
			Set<Long> offices = new LinkedHashSet<Long>();
			if (officeids != null) {
				// for (int i = 0; i < list.size(); i++) {
				// offices.add(list.get(i).getUpdateBy().getOffice().getId());
				// }
				for (int i = 0; i < list.size(); i++) {
					offices.add(list.get(i).getOfficeId());
				}
			}
			Object offs[] = offices.toArray();
			for (int m = 0; m < months.length; m++) {
				WorkDate_MoneVo workDate_MoneVo = new WorkDate_MoneVo();
				double countMoneys = 0L;
				for (int o = 0; o < offs.length; o++) {
					double countPostMoney = 0L;
					double countBankMoney = 0L;
					double countXjMoney = 0L;
					double countAlipayMoney = 0L;
					double countMoney = 0L;
					// double postMoney = 0L;
					// double bankMoney = 0L;
					// double xjMoney = 0L;
					// double alipayMoney = 0L;
					for (int i = 0; i < list.size(); i++) {
						String mo = new SimpleDateFormat("yyyy-MM-dd")
								.format(list.get(i).getWorkPayInfo().getCreateDate());
						if (list.get(i).getOfficeId().equals(offs[o]) && ((String) months[m]).indexOf(mo) != -1) {
							double postMoney = 0L;
							double bankMoney = 0L;
							double xjMoney = 0L;
							double alipayMoney = 0L;
							if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
								if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
									if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
										postMoney = list.get(i).getWorkPayInfo().getPosMoney();
									} else {
										postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
									}
								}
								if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
									if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
										bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
									} else {
										bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
									}
								}
								if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
									if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
										xjMoney = list.get(i).getWorkPayInfo().getMoney();
									} else {
										xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
									}

								}
								if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
									if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
										alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
									} else {
										alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
									}

								}
							} else {
								List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
										.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
								for (int w = 0; w < workfinancePayinfos.size(); w++) {
									if (workfinancePayinfos.get(w).getMoney() >= 0) {
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 1) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												xjMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												xjMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 2) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												postMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												postMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 3) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												bankMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												bankMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 4) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												alipayMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												alipayMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
									}
								}
							}
							countPostMoney += postMoney;
							countBankMoney += bankMoney;
							countXjMoney += xjMoney;
							countAlipayMoney += alipayMoney;
							countMoney += (postMoney + bankMoney + xjMoney + alipayMoney);
						}
					}
					String officeName = (officeService.findById((Long) offs[o])).getName();
					Workoffice_MoneyVo workoffice_MoneyVo = new Workoffice_MoneyVo();
					workoffice_MoneyVo.setOfficeName(officeName);
					workoffice_MoneyVo.setDate((String) months[m]);
					workoffice_MoneyVo.setCountPostMoney(countPostMoney);
					workoffice_MoneyVo.setCountBankMoney(countBankMoney);
					workoffice_MoneyVo.setCountXjMoney(countXjMoney);
					workoffice_MoneyVo.setCountAlipayMoney(countAlipayMoney);
					workoffice_MoneyVo.setCountMoney(countMoney);
					o_m.add(workoffice_MoneyVo);
					countMoneys += countMoney;
				}
				workDate_MoneVo.setDate((String) months[m]);
				workDate_MoneVo.setCountMoney(countMoneys);
				w_m.add(workDate_MoneVo);
			}

			for (int ofs = 0; ofs < offs.length; ofs++) {
				int post = 0;
				int bank = 0;
				int xj = 0;
				int alipay = 0;
				for (int of = 0; of < o_m.size(); of++) {
					if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
						if (o_m.get(of).getCountPostMoney() > 0) {
							post++;
						}
						if (o_m.get(of).getCountBankMoney() > 0) {
							bank++;
						}
						if (o_m.get(of).getCountXjMoney() > 0) {
							xj++;
						}
						if (o_m.get(of).getCountAlipayMoney() > 0) {
							alipay++;
						}
					}
				}
				List<String> payMethods = Lists.newArrayList();
				if (post > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setPostMoney(true);
						}
					}
					payMethods.add("pos付款");
				}
				if (bank > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setBankMoney(true);
						}
					}
					payMethods.add("银行转账");
				}
				if (xj > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setXjMoney(true);
						}
					}
					payMethods.add("现金付款");
				}
				if (alipay > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setAlipayMoney(true);
						}
					}
					payMethods.add("支付宝");
				}
				office_payMethod.put(officeService.findById((Long) offs[ofs]).getName(), payMethods);
			}

			List<Double> payMethodMoneys = Lists.newArrayList();
			double moneys = 0L;
			Iterator<Map.Entry<String, List<String>>> it = office_payMethod.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, List<String>> entry = it.next();
				double post = 0.0;
				double xj = 0.0;
				double bank = 0.0;
				double alipay = 0.0;
				for (int o = 0; o < o_m.size(); o++) {
					if (entry.getKey().equals(o_m.get(o).getOfficeName())) {
						if (o_m.get(o).getPostMoney()) {
							post += o_m.get(o).getCountPostMoney();
						}
						if (o_m.get(o).getXjMoney()) {
							xj += o_m.get(o).getCountXjMoney();
						}
						if (o_m.get(o).getBankMoney()) {
							bank += o_m.get(o).getCountBankMoney();
						}
						if (o_m.get(o).getAlipayMoney()) {
							alipay += o_m.get(o).getCountAlipayMoney();
						}
					}
				}
				moneys += (post + xj + bank + alipay);
				if (entry.getValue().indexOf("pos付款") != -1) {
					payMethodMoneys.add(post);
				}
				if (entry.getValue().indexOf("银行转账") != -1) {

					payMethodMoneys.add(bank);
				}
				if (entry.getValue().indexOf("现金付款") != -1) {

					payMethodMoneys.add(xj);
				}
				if (entry.getValue().indexOf("支付宝") != -1) {
					payMethodMoneys.add(alipay);
				}
			}
			payMethodMoneys.add(moneys);
			model.addAttribute("moneys", payMethodMoneys);
			model.addAttribute("dates", months);
			if (months.length == 0) {
				model.addAttribute("message", "此查询条件没有数据");
			}
			model.addAttribute("office_payMethod", office_payMethod);
			model.addAttribute("workoffice_MoneyVo", o_m);
			model.addAttribute("workDate_Mone", w_m);
			model.addAttribute("area", area);
			model.addAttribute("endTime", endTime);
			model.addAttribute("appId", appId);
			ConfigApp configApp = configAppService.findByAppId(appId);
			model.addAttribute("appName", configApp.getAppName());
			model.addAttribute("startTime", startTime);
			model.addAttribute("officeId", office);
		}

		List<Office> offsList = officeService.getOfficeByType(user, 1);
		model.addAttribute("offsList", offsList);
		List<ConfigApp> appList = configAppService.findAllConfigApp();
		model.addAttribute("appList", appList);
		return "modules/work/statisticalDayList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "statisticalMonthList")
	public String statisticalMonthList(WorkDealInfo workDealInfo, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office, Model model) throws ParseException {
		Date start = null;
		Date end = null;
		if (startTime != null) {
			StringBuffer startt = new StringBuffer(startTime);
			startt.append("-1");
			StringBuffer endtt = new StringBuffer(endTime);
			endtt.append("-31");
			try {
				start = new SimpleDateFormat("yyyy-MM-dd").parse(startt.toString());
				end = new SimpleDateFormat("yyyy-MM-dd").parse(endtt.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		// List<String> payMethod = Lists.newArrayList();
		// List<Double> money = Lists.newArrayList();
		Map<String, Double> officeMoneys = new HashMap<>();
		Map<String, List<String>> office_payMethod = new HashMap<>();
		Map<String, List<Double>> office_money = new HashMap<>();
		if (appId == null || startTime == null || endTime == null) {// 首次进入，或者条件不全
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			model.addAttribute("offsList", offsList);
			List<ConfigApp> appList = configAppService.findAllConfigApp();
			model.addAttribute("appList", appList);
			model.addAttribute("startTime", startTime == null ? DateUtils.firstDayOfMonth(new Date()) : startTime);
			model.addAttribute("endTime", endTime == null ? new Date() : endTime);
			return "modules/work/statisticalMonthList";
		}
		if (area != null && office != null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			officeids.add(office);
		} else if (area != null && office == null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			for (Office o : offices) {
				officeids.add(o.getId());
			}

		} else { //
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			List<Long> areas = Lists.newArrayList();
			if (offsList.size() > 0) {
				for (int i = 0; i < offsList.size(); i++) {
					areas.add(offsList.get(i).getId());
				}
			} else {
				areas.add(-1l);
			}
			List<Office> offices = officeService.findByParentIds(areas);// 根据区域id获取网点id
			if (offices.size() > 0) {
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}
		List<WorkDealInfo> list = workDealInfoService.findByDayPay(start, end, officeids, appId);
		if (list != null) {
			List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
			List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
			Set<String> month = new LinkedHashSet<String>();
			for (int i = 0; i < list.size(); i++) {
				String a = new SimpleDateFormat("yyyy-MM").format(list.get(i).getWorkPayInfo().getCreateDate());
				month.add(a);
				// if(list.get(i).getCreateBy().getOffice().getId()==632001)
				// {
				// System.out.println(list.get(i).getWorkPayInfo().getCreateDate()+":"+list.get(i).getWorkPayInfo().getPosMoney()+"***********");
				// }
			}
			Set<Long> offices = new LinkedHashSet<Long>();
			if (officeids != null) {
				for (int i = 0; i < list.size(); i++) {
					// offices.add(list.get(i).getUpdateBy().getOffice().getId());
					offices.add(list.get(i).getOfficeId());
				}
			}
			Object offs[] = offices.toArray();
			Object months[] = month.toArray();
			for (int m = 0; m < months.length; m++) {
				WorkDate_MoneVo workDate_MoneVo = new WorkDate_MoneVo();
				double countMoneys = 0L;
				for (int o = 0; o < offs.length; o++) {
					double countPostMoney = 0L;
					double countBankMoney = 0L;
					double countXjMoney = 0L;
					double countAlipayMoney = 0L;
					double countMoney = 0L;
					// double postMoney = 0L;
					// double bankMoney = 0L;
					// double xjMoney = 0L;
					// double alipayMoney = 0L;
					for (int i = 0; i < list.size(); i++) {
						String mo = new SimpleDateFormat("yyyy-MM")
								.format(list.get(i).getWorkPayInfo().getCreateDate());
						if (list.get(i).getOfficeId().equals(offs[o]) && ((String) months[m]).indexOf(mo) != -1) {
							double postMoney = 0L;
							double bankMoney = 0L;
							double xjMoney = 0L;
							double alipayMoney = 0L;
							if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
								if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
									if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
										postMoney = list.get(i).getWorkPayInfo().getPosMoney();
									} else {
										postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
									}
								}
								if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
									if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
										bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
									} else {
										bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
									}
								}
								if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
									if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
										xjMoney = list.get(i).getWorkPayInfo().getMoney();
									} else {
										xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
									}

								}
								if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
									if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
										alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
									} else {
										alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
									}

								}
							} else {
								List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
										.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
								for (int w = 0; w < workfinancePayinfos.size(); w++) {
									if (workfinancePayinfos.get(w).getMoney() >= 0) {
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 1) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												xjMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												xjMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 2) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												postMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												postMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 3) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												bankMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												bankMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 4) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												alipayMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												alipayMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
									}
								}
							}
							countPostMoney += postMoney;
							countBankMoney += bankMoney;
							countXjMoney += xjMoney;
							countAlipayMoney += alipayMoney;
							countMoney += (postMoney + bankMoney + xjMoney + alipayMoney);
						}
					}
					String officeName = (officeService.findById((Long) offs[o])).getName();
					Workoffice_MoneyVo workoffice_MoneyVo = new Workoffice_MoneyVo();
					workoffice_MoneyVo.setOfficeName(officeName);
					workoffice_MoneyVo.setDate((String) months[m]);
					workoffice_MoneyVo.setCountPostMoney(countPostMoney);
					workoffice_MoneyVo.setCountBankMoney(countBankMoney);
					workoffice_MoneyVo.setCountXjMoney(countXjMoney);
					workoffice_MoneyVo.setCountAlipayMoney(countAlipayMoney);
					workoffice_MoneyVo.setCountMoney(countMoney);
					o_m.add(workoffice_MoneyVo);
					countMoneys += countMoney;
				}
				workDate_MoneVo.setDate((String) months[m]);
				workDate_MoneVo.setCountMoney(countMoneys);
				w_m.add(workDate_MoneVo);
			}
			for (int ofs = 0; ofs < offs.length; ofs++) {
				int post = 0;
				int bank = 0;
				int xj = 0;
				int alipay = 0;
				for (int of = 0; of < o_m.size(); of++) {
					if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
						if (o_m.get(of).getCountPostMoney() > 0) {
							post++;
						}
						if (o_m.get(of).getCountBankMoney() > 0) {
							bank++;
						}
						if (o_m.get(of).getCountXjMoney() > 0) {
							xj++;
						}
						if (o_m.get(of).getCountAlipayMoney() > 0) {
							alipay++;
						}
					}
				}
				List<String> payMethods = Lists.newArrayList();
				if (post > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setPostMoney(true);
						}
					}
					payMethods.add("pos付款");
				}
				if (bank > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setBankMoney(true);
						}
					}
					payMethods.add("银行转账");
				}
				if (xj > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setXjMoney(true);
						}
					}
					payMethods.add("现金付款");
				}
				if (alipay > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setAlipayMoney(true);
						}
					}
					payMethods.add("支付宝");
				}
				office_payMethod.put(officeService.findById((Long) offs[ofs]).getName(), payMethods);
			}
			List<Double> payMethodMoneys = Lists.newArrayList();
			double moneys = 0L;
			Iterator<Map.Entry<String, List<String>>> it = office_payMethod.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, List<String>> entry = it.next();
				double post = 0.0;
				double xj = 0.0;
				double bank = 0.0;
				double alipay = 0.0;
				for (int o = 0; o < o_m.size(); o++) {
					if (entry.getKey().equals(o_m.get(o).getOfficeName())) {
						if (o_m.get(o).getPostMoney()) {
							post += o_m.get(o).getCountPostMoney();
						}
						if (o_m.get(o).getXjMoney()) {
							xj += o_m.get(o).getCountXjMoney();
						}
						if (o_m.get(o).getBankMoney()) {
							bank += o_m.get(o).getCountBankMoney();
						}
						if (o_m.get(o).getAlipayMoney()) {
							alipay += o_m.get(o).getCountAlipayMoney();
						}
					}
				}
				moneys += (post + xj + bank + alipay);
				if (entry.getValue().indexOf("pos付款") != -1) {
					payMethodMoneys.add(post);
				}
				if (entry.getValue().indexOf("银行转账") != -1) {

					payMethodMoneys.add(bank);
				}
				if (entry.getValue().indexOf("现金付款") != -1) {

					payMethodMoneys.add(xj);
				}
				if (entry.getValue().indexOf("支付宝") != -1) {
					payMethodMoneys.add(alipay);
				}
			}
			payMethodMoneys.add(moneys);
			model.addAttribute("moneys", payMethodMoneys);
			model.addAttribute("dates", months);
			if (months.length == 0) {
				model.addAttribute("message", "此查询条件没有数据");
			}
			model.addAttribute("office_payMethod", office_payMethod);
			model.addAttribute("workoffice_MoneyVo", o_m);
			model.addAttribute("workDate_Mone", w_m);
			model.addAttribute("area", area);
			model.addAttribute("endTime", new SimpleDateFormat("yyyy-MM").parse(endTime));
			model.addAttribute("appId", appId);
			ConfigApp configApp = configAppService.findByAppId(appId);
			model.addAttribute("appName", configApp.getAppName());
			model.addAttribute("startTime", new SimpleDateFormat("yyyy-MM").parse(startTime));
			model.addAttribute("officeId", office);
		}
		List<Office> offsList = officeService.getOfficeByType(user, 1);
		model.addAttribute("offsList", offsList);
		List<ConfigApp> appList = configAppService.findAllConfigApp();
		model.addAttribute("appList", appList);

		return "modules/work/statisticalMonthList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "StatisticalProjectList")
	public String StatisticalProjectList(WorkDealInfo workDealInfo, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office, Model model) throws ParseException {
		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		Map<String, Set<String>> office_District = new HashMap<>();
		Map<String, List<String>> district_payMethod = new HashMap<>();
		Map<String, List<Double>> district_Moneys = new HashMap<>();
		List<Long> appids = Lists.newArrayList();
		if (appId == null || startTime == null || endTime == null) {// 首次进入，或者条件不全
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			model.addAttribute("offsList", offsList);
			List<ConfigApp> appList = configAppService.findAllConfigApp();
			model.addAttribute("appList", appList);
			model.addAttribute("startTime", startTime == null ? DateUtils.firstDayOfMonth(new Date()) : startTime);
			model.addAttribute("endTime", endTime == null ? new Date() : endTime);
			return "modules/work/statisticalProjectList";
		}
		if (area != null && office != null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			officeids.add(office);
		} else if (area != null && office == null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			for (Office o : offices) {
				officeids.add(o.getId());
			}

		} else { //
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			List<Long> areas = Lists.newArrayList();
			if (offsList.size() > 0) {
				for (int i = 0; i < offsList.size(); i++) {
					areas.add(offsList.get(i).getId());
				}
			} else {
				areas.add(-1l);
			}
			List<Office> offices = officeService.findByParentIds(areas);// 根据区域id获取网点id
			if (offices.size() > 0) {
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}

		List<WorkDealInfo> list = workDealInfoService.findByProjectPay(startTime, endTime, officeids, appId);
		if (list != null) {
			List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
			List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
			List<Workoffice_district_MoneyVo> odms = new ArrayList<Workoffice_district_MoneyVo>();
			Set<String> month = new LinkedHashSet<String>();
			for (int i = 0; i < list.size(); i++) {
				String a = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getWorkPayInfo().getCreateDate());
				month.add(a);
			}
			Set<Long> offices = new LinkedHashSet<Long>();
			if (officeids != null) {

				for (int i = 0; i < list.size(); i++) {
					offices.add(list.get(i).getOfficeId());
				}
			}
			Set<String> district = new LinkedHashSet<String>();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getWorkCompany().getDistrict() != null) {
					district.add(list.get(i).getWorkCompany().getDistrict());
				} else {
					district.add("其他");
				}
			}

			
			Object offs[] = offices.toArray();
			Object months[] = month.toArray();
			Object districts[] = district.toArray();
			for (int o = 0; o < offs.length; o++) {
				
		
					Set<String> dis = new LinkedHashSet<String>();
					for (int d = 0; d < districts.length; d++) {
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getWorkCompany().getDistrict() != null) {

								if (list.get(i).getOfficeId().equals(offs[o])
										&& list.get(i).getWorkCompany().getDistrict().equals(districts[d])) {
									dis.add((String) districts[d]);
								}
							}else{
								if (list.get(i).getOfficeId().equals(offs[o])
										&& districts[d].equals("其他")) {
									dis.add((String) districts[d]);
								}
							}

						}
					}
					office_District.put((officeService.findById((Long) offs[o])).getName(), dis);
				
				
				
			}
			for (int m = 0; m < months.length; m++) {

				for (int d = 0; d < districts.length; d++) {
					double countPostMoney = 0L;
					double countBankMoney = 0L;
					double countXjMoney = 0L;
					double countAlipayMoney = 0L;
					double countMoney = 0L;

					for (int i = 0; i < list.size(); i++) {
						String mo = new SimpleDateFormat("yyyy-MM-dd")
								.format(list.get(i).getWorkPayInfo().getCreateDate());
						if (list.get(i).getWorkCompany().getDistrict() != null) {
							if (((String) months[m]).indexOf(mo) != -1
									&& list.get(i).getWorkCompany().getDistrict().equals((String) districts[d])) {
								double postMoney = 0L;
								double bankMoney = 0L;
								double xjMoney = 0L;
								double alipayMoney = 0L;
								if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
											postMoney = list.get(i).getWorkPayInfo().getPosMoney();
										} else {
											postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
											bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
										} else {
											bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
											xjMoney = list.get(i).getWorkPayInfo().getMoney();
										} else {
											xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
										}
									}
								}
								countPostMoney += postMoney;
								countBankMoney += bankMoney;
								countXjMoney += xjMoney;
								countAlipayMoney += alipayMoney;
								countMoney += (postMoney + bankMoney + xjMoney + alipayMoney);

							}
						
						
						
						}else{
							
							
							if (((String) months[m]).indexOf(mo) != -1
									&& (districts[d].equals("其他"))) {
								double postMoney = 0L;
								double bankMoney = 0L;
								double xjMoney = 0L;
								double alipayMoney = 0L;
								if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
											postMoney = list.get(i).getWorkPayInfo().getPosMoney();
										} else {
											postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
											bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
										} else {
											bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
											xjMoney = list.get(i).getWorkPayInfo().getMoney();
										} else {
											xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
										}
									}
								}
								countPostMoney += postMoney;
								countBankMoney += bankMoney;
								countXjMoney += xjMoney;
								countAlipayMoney += alipayMoney;
								countMoney += (postMoney + bankMoney + xjMoney + alipayMoney);

							}
							
						}
						
						
						
						
						
						
					}
					Workoffice_district_MoneyVo dm = new Workoffice_district_MoneyVo();
					dm.setDate((String) months[m]);
					dm.setDistrictName((String) districts[d]);
					dm.setCountPostMoney(countPostMoney);
					dm.setCountBankMoney(countBankMoney);
					dm.setCountXjMoney(countXjMoney);
					dm.setCountAlipayMoney(countAlipayMoney);
					odms.add(dm);

				}

			}

			double moneys = 0L;
			for (int d = 0; d < districts.length; d++) {
				int post = 0;
				int bank = 0;
				int xj = 0;
				int alipay = 0;
				for (int od = 0; od < odms.size(); od++) {
					if (odms.get(od).getDistrictName().equals(districts[d])) {
						if (odms.get(od).getCountPostMoney() > 0) {
							post++;
						}
						if (odms.get(od).getCountBankMoney() > 0) {
							bank++;
						}
						if (odms.get(od).getCountXjMoney() > 0) {
							xj++;
						}
						if (odms.get(od).getCountAlipayMoney() > 0) {
							alipay++;
						}
					}
				}
				List<String> payMethods = Lists.newArrayList();
				List<Double> dis_Money = Lists.newArrayList();
				if (post > 0) {
					double postMoney = 0L;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d])) {
							odms.get(od).setPostMoney(true);
							postMoney += odms.get(od).getCountPostMoney();
						}
					}
					moneys += postMoney;
					dis_Money.add(postMoney);
					payMethods.add("pos付款");
				}
				if (bank > 0) {
					double bankMoney = 0.0;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d])) {
							odms.get(od).setBankMoney(true);
							bankMoney += odms.get(od).getCountBankMoney();
						}
					}
					moneys += bankMoney;
					dis_Money.add(bankMoney);
					payMethods.add("银行转账");
				}
				if (xj > 0) {
					double xjMoney = 0.0;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d])) {
							odms.get(od).setXjMoney(true);
							xjMoney += odms.get(od).getCountXjMoney();
						}
					}
					moneys += xjMoney;
					dis_Money.add(xjMoney);
					payMethods.add("现金付款");
				}
				if (alipay > 0) {
					double alipayMoney = 0.0;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d])) {
							odms.get(od).setAlipayMoney(true);
							alipayMoney += odms.get(od).getCountAlipayMoney();
						}
					}
					moneys += alipayMoney;
					dis_Money.add(alipayMoney);
					payMethods.add("支付宝");
				}
				if (payMethods != null && payMethods.size() > 0) {
					district_payMethod.put((String) districts[d], payMethods);
				}
				if (dis_Money != null && dis_Money.size() > 0) {
					district_Moneys.put((String) districts[d], dis_Money);
				}
			}
			model.addAttribute("district_Moneys", district_Moneys);
			model.addAttribute("moneys", moneys);
			model.addAttribute("dates", months);
			if (months.length == 0) {
				model.addAttribute("message", "此查询条件没有数据");
			}
			model.addAttribute("office_District", office_District);
			model.addAttribute("odms", odms);
			model.addAttribute("district_payMethod", district_payMethod);
			model.addAttribute("workDate_Mone", w_m);
			model.addAttribute("area", area);
			model.addAttribute("endTime", endTime);
			model.addAttribute("appId", appId);
			ConfigApp configApp = configAppService.findByAppId(appId);
			model.addAttribute("appName", configApp.getAppName());
			model.addAttribute("startTime", startTime);
			model.addAttribute("officeId", office);

		}
		List<Office> offsList = officeService.getOfficeByType(user, 1);
		model.addAttribute("offsList", offsList);
		List<ConfigApp> appList = configAppService.findAllConfigApp();
		model.addAttribute("appList", appList);
		return "modules/work/statisticalProjectList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "statisticalAdjustment")
	public String statisticalAdjustment(FinancePaymentInfo financePaymentInfo, Date dkstartTime, Date dkendTime,
			Date zzstartTime, Date zzendTime, HttpServletRequest request, String companyName,
			HttpServletResponse response, Model model) throws ParseException {
		if (dkstartTime == null && dkstartTime == null && companyName == null
				&& financePaymentInfo.getCompany() == null) {
			List<ConfigApp> configApps = configAppService.findAllConfigApp();
			model.addAttribute("companys", configApps);
			return "modules/work/statisticalAdjustmentList";
		}
		if (dkendTime != null) {
			dkendTime.setHours(23);
			dkendTime.setMinutes(59);
			dkendTime.setSeconds(59);
		}
		if (zzendTime != null) {
			zzendTime.setHours(23);
			zzendTime.setSeconds(59);
			zzendTime.setMinutes(59);
		}
		List<FinancePaymentInfo> list = financePaymentInfoService.findAdjustment(financePaymentInfo);
		List<WorkpaymentInfo_dealinfoVo> p_d = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			List<WorkFinancePayInfoRelation> financePay = workFinancePayInfoRelationService
					.findByFinance(list.get(i).getId());
			if (financePay.size() == 0) {
				WorkpaymentInfo_dealinfoVo workpaymentInfo_dealinfoVo = new WorkpaymentInfo_dealinfoVo();
				workpaymentInfo_dealinfoVo.setId(list.get(i).getId());
				workpaymentInfo_dealinfoVo.setDealPayDate(list.get(i).getPayDate());
				workpaymentInfo_dealinfoVo.setPayMoney(list.get(i).getPaymentMoney());
				workpaymentInfo_dealinfoVo.setCompanyName(list.get(i).getCompany());
				workpaymentInfo_dealinfoVo.setRemarks(list.get(i).getRemark());
				p_d.add(workpaymentInfo_dealinfoVo);
			} else {
				List<Long> idList = Lists.newArrayList();
				if (financePay.size() > 0) {
					for (int f = 0; f < financePay.size(); f++) {
						idList.add(financePay.get(f).getWorkPayInfo().getId());
					}
				}
				List<WorkDealInfo> workDealInfos = workDealInfoService.findByFinanceId(idList);
				for (int w = 0; w < workDealInfos.size(); w++) {
					WorkpaymentInfo_dealinfoVo workpaymentInfo_dealinfoVo = new WorkpaymentInfo_dealinfoVo();
					workpaymentInfo_dealinfoVo.setId(list.get(i).getId());
					workpaymentInfo_dealinfoVo.setDealPayDate(list.get(i).getPayDate());
					workpaymentInfo_dealinfoVo.setPayMoney(list.get(i).getPaymentMoney());
					workpaymentInfo_dealinfoVo.setCompanyName(list.get(i).getCompany());
					workpaymentInfo_dealinfoVo.setRemarks(list.get(i).getRemarks());
					workpaymentInfo_dealinfoVo.setAliasName(workDealInfos.get(w).getConfigApp().getAppName());
					workpaymentInfo_dealinfoVo.setSignDate(workDealInfos.get(w).getWorkCertInfo().getSignDate());
					p_d.add(workpaymentInfo_dealinfoVo);
				}
			}
		}
		List<WorkpaymentInfo_dealinfoVo> p_ds = Lists.newArrayList();

		for (int i = 0; i < p_d.size(); i++) {

			if (dkstartTime != null && dkendTime != null && zzstartTime == null && zzendTime == null
					&& "".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime.getTime()) {
					p_ds.add(p_d.get(i));
				}

			} else if (dkstartTime != null && dkendTime != null && zzstartTime != null && zzendTime != null
					&& "".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime.getTime()) {
					if (p_d.get(i).getSignDate() != null) {
						if (p_d.get(i).getSignDate().getTime() >= zzstartTime.getTime()
								&& p_d.get(i).getSignDate().getTime() <= zzendTime.getTime()) {
							p_ds.add(p_d.get(i));
						}
					}
				}
			} else if (dkstartTime != null && dkendTime != null && zzstartTime != null && zzendTime != null
					&& !"".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime.getTime()) {
					if (p_d.get(i).getSignDate() != null) {
						if (p_d.get(i).getSignDate().getTime() >= zzstartTime.getTime()
								&& p_d.get(i).getSignDate().getTime() <= zzendTime.getTime()) {
							if (p_d.get(i).getAliasName() != null) {
								if (p_d.get(i).getAliasName().equals(companyName)) {
									p_ds.add(p_d.get(i));
								}
							}
						}
					}
				}
			} else if (dkstartTime != null && dkendTime != null && zzstartTime != null && zzendTime != null
					&& "".equals(companyName) && !"".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime.getTime()) {
					if (p_d.get(i).getSignDate() != null) {
						if (p_d.get(i).getSignDate().getTime() >= zzstartTime.getTime()
								&& p_d.get(i).getSignDate().getTime() <= zzendTime.getTime()) {
							if (p_d.get(i).getCompanyName() != null) {
								if (p_d.get(i).getCompanyName().equals(financePaymentInfo.getCompany())) {
									p_ds.add(p_d.get(i));
								}
							}
						}
					}
				}
			} else if (dkstartTime == null && dkendTime == null && zzstartTime == null && zzendTime == null
					&& "".equals(companyName) && !"".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getCompanyName() != null) {
					if (p_d.get(i).getCompanyName().equals(financePaymentInfo.getCompany())) {
						p_ds.add(p_d.get(i));
					}
				}
			} else if (dkstartTime == null && dkendTime == null && zzstartTime == null && zzendTime == null
					&& !"".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getAliasName() != null) {
					if (p_d.get(i).getAliasName().equals(companyName)) {
						p_ds.add(p_d.get(i));
					}
				}
			} else if (dkstartTime == null && dkendTime == null && zzstartTime != null && zzendTime != null
					&& "".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getSignDate() != null) {
					if (p_d.get(i).getSignDate().getTime() >= zzstartTime.getTime()
							&& p_d.get(i).getSignDate().getTime() <= zzendTime.getTime()) {
						p_ds.add(p_d.get(i));
					}
				}
			} else {
				String start = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01";
				String end = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-31";
				Date dkstartTime_ = new SimpleDateFormat("yyyy-MM-dd").parse(start);
				Date dkendTime_ = new SimpleDateFormat("yyyy-MM-dd").parse(end);
				dkendTime_.setHours(23);
				dkendTime_.setMinutes(59);
				dkendTime_.setSeconds(59);
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime_.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime_.getTime()) {
					p_ds.add(p_d.get(i));
				}
			}
		}
		// Page<WorkpaymentInfo_dealinfoVo> page=new
		// Page<WorkpaymentInfo_dealinfoVo>(request, response);
		// page.setCount(p_ds.size());
		// page.setList(p_ds);
		// model.addAttribute("page", page);
		model.addAttribute("workpaymentInfo_dealinfoVo", p_ds);
		if (p_ds.size() == 0) {
			model.addAttribute("message", "此查询条件没有数据");
		}
		model.addAttribute("dkstartTime", dkstartTime);
		model.addAttribute("dkendTime", dkendTime);
		model.addAttribute("zzstartTime", zzstartTime);
		model.addAttribute("zzendTime", zzendTime);
		model.addAttribute("companyName", companyName);
		List<ConfigApp> configApps = configAppService.findAllConfigApp();
		model.addAttribute("companys", configApps);
		return "modules/work/statisticalAdjustmentList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "statisticalYearProjectList")
	public String statisticalYearProjectList(String startTime, Model model, Long configProjectTypeId, Long appId)
			throws ParseException {
		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();

		if (configProjectTypeId == null && startTime == null && appId == null) {
			// List<ConfigApp> configApps =
			// configAppService.findByconfigProjectType(configProjectTypeId);
			// model.addAttribute("appList", configApps);
			List<ConfigProjectType> configProjectTypes = configProjectTypeService.findProjectTypeList();
			model.addAttribute("configProjectTypes", configProjectTypes);
			model.addAttribute("startTime", new Date());
			return "modules/work/statisticalYearProjectList";
		}

		if (startTime == null || "".equals(startTime)) {
			startTime = new SimpleDateFormat("yyyy").format(new Date());
		}

		String start = startTime + "-01-01";
		String end = startTime + "-12-31";
		Date startT = new SimpleDateFormat("yyyy-MM-dd").parse(start);
		Date endT = new SimpleDateFormat("yyyy-MM-dd").parse(end);
		model.addAttribute("startTime", startT);
		int month = 0;
		List<Date> months = Lists.newArrayList();
		if (startT.getYear() == new Date().getYear()) {
			month = new Date().getMonth() + 1;
		} else {
			month = 12;
		}
		for (int i = 0; i < month; i++) {
			String m = startTime + "-" + (i + 1) + "-1";
			Date mo = new SimpleDateFormat("yyyy-MM-dd").parse(m);
			months.add(mo);
		}
		List<ConfigProjectType> configProjectTypes = configProjectTypeService.findProjectTypeList();
		model.addAttribute("configProjectTypes", configProjectTypes);

		List<Long> configappids = Lists.newArrayList();
		if (configProjectTypeId != null && appId == null) {
			List<Long> appids = Lists.newArrayList();
			List<ConfigApp> configApps = configAppService.findByconfigProjectType(configProjectTypeId);
			model.addAttribute("appList", configApps);
			if (configApps != null) {
				for (int a = 0; a < configApps.size(); a++) {
					appids.add(configApps.get(a).getId());
					configappids.add(configApps.get(a).getId());
				}
			} else {
				appids.add(-1L);
			}

		} else if (configProjectTypeId != null && appId != null) {
			List<ConfigApp> configApps = configAppService.findByconfigProjectType(configProjectTypeId);
			model.addAttribute("appList", configApps);
			model.addAttribute("appid", appId);
			configappids.add(appId);
		} else if (configProjectTypeId == null && appId == null) {
			List<Long> appids = Lists.newArrayList();
			List<ConfigApp> configApps = configAppService.findAllConfigApp();
			// model.addAttribute("appList", configApps);
			if (configApps != null) {
				for (int a = 0; a < configApps.size(); a++) {
					appids.add(configApps.get(a).getId());
					configappids.add(configApps.get(a).getId());
				}
			} else {
				appids.add(-1L);
			}
		} else {
			configappids.clear();
		}

		Map<WorkDealInfo, List<Double>> w_months = new HashMap<>();
		List<WorkDealInfo> list = workDealInfoService.findByProjectYear(startT, endT, configappids);
		if (list != null) {
			for (int c = 0; c < configappids.size(); c++) {
				double zmoney = 0;
				WorkDealInfo wdi = new WorkDealInfo();
				List<Double> month_Moneys = Lists.newArrayList();
				for (int m = 0; m < months.size(); m++) {
					double moneys = 0;
					for (int i = 0; i < list.size(); i++) {
						if (m < months.size() - 1) {
							if (list.get(i).getCreateDate().getTime() >= months.get(m).getTime()
									&& list.get(i).getCreateDate().getTime() < months.get(m + 1).getTime()
									&& list.get(i).getConfigApp().getId().equals(configappids.get(c))) {
								double postMoney = 0.0;
								double bankMoney = 0.0;
								double alipayMoney = 0.0;
								double xjMoney = 0.0;
								// double posMoney =
								// list.get(i).getWorkPayInfo().getPosMoney();
								// double bankMoney =
								// list.get(i).getWorkPayInfo().getBankMoney();
								// double alipayMoney =
								// list.get(i).getWorkPayInfo().getAlipayMoney();
								// double xjMoney =
								// list.get(i).getWorkPayInfo().getMoney();
								// moneys += (posMoney + bankMoney + alipayMoney
								// + xjMoney);
								if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
											postMoney = list.get(i).getWorkPayInfo().getPosMoney();
										} else {
											postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
											bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
										} else {
											bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
											xjMoney = list.get(i).getWorkPayInfo().getMoney();
										} else {
											xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}
											}
										}
									}
								}
								moneys += (postMoney + bankMoney + alipayMoney + xjMoney);
							}
						} else if (m == months.size() - 1) {
							if (list.get(i).getCreateDate().getTime() >= months.get(m).getTime()
									&& list.get(i).getConfigApp().getId().equals(configappids.get(c))) {
								double postMoney = 0.0;
								double bankMoney = 0.0;
								double alipayMoney = 0.0;
								double xjMoney = 0.0;
								// double posMoney =
								// list.get(i).getWorkPayInfo().getPosMoney();
								// double bankMoney =
								// list.get(i).getWorkPayInfo().getBankMoney();
								// double alipayMoney =
								// list.get(i).getWorkPayInfo().getAlipayMoney();
								// double xjMoney =
								// list.get(i).getWorkPayInfo().getMoney();
								// moneys += (posMoney + bankMoney + alipayMoney
								// + xjMoney);
								if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
											postMoney = list.get(i).getWorkPayInfo().getPosMoney();
										} else {
											postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
											bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
										} else {
											bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
											xjMoney = list.get(i).getWorkPayInfo().getMoney();
										} else {
											xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}
											}
										}
									}
								}
								moneys += (postMoney + bankMoney + alipayMoney + xjMoney);
							}
						}
					}
					month_Moneys.add(moneys);
					zmoney += moneys;
				}
				ConfigProduct configProduct = new ConfigProduct();
				configProduct.setConfigApp(configAppService.findByAppId(configappids.get(c)));
				List<ConfigProduct> pros = configProductService.findByAppId(configProduct);// 获取应用下的产品信息
				String proLabel = "";
				for (int j = 0; j < pros.size(); j++) {
					if (pros.get(j).getProductLabel() == 1) {
						proLabel = "(专用)";
					} else {
						proLabel = "(通用)";
					}
					if (j != pros.size() - 1) {
					}
				}
				// ConfigApp
				// configapp=configAppService.findByAppId(configappids.get(c));
				// configapp.setAppName(configapp.getAppName()+proLabel);
				wdi.setConfigApp(configAppService.findByAppId(configappids.get(c)));
				wdi.getConfigApp().setAppName(wdi.getConfigApp().getAppName() + proLabel);
				month_Moneys.add(zmoney);
				w_months.put(wdi, month_Moneys);
			}
		}

		List<Double> zj = Lists.newArrayList();
		int a = months.size() + 1;
		for (int l = 0; l < a; l++) {
			double aa = 0;
			Iterator<Map.Entry<WorkDealInfo, List<Double>>> itt = w_months.entrySet().iterator();
			while (itt.hasNext()) {
				Entry<WorkDealInfo, List<Double>> entry = itt.next();
				aa += entry.getValue().get(l);
			}
			zj.add(aa);
		}

		model.addAttribute("zj", zj);
		model.addAttribute("w_months", w_months);
		if (w_months.size() == 0) {
			model.addAttribute("message", "本次查询结果没有记录");
		}
		model.addAttribute("months", months);
		model.addAttribute("appid", appId);
		model.addAttribute("configProjectTypeId", configProjectTypeId);

		return "modules/work/statisticalYearProjectList";
	}

	@RequestMapping(value = "addConfigProjectType")
	@ResponseBody
	public String addConfigProjectType(Long configProjectTypeId) throws JSONException {
		JSONObject json = new org.json.JSONObject();
		JSONArray array = new JSONArray();
		List<ConfigApp> configApps = configAppService.findByconfigProjectType(configProjectTypeId);
		for (ConfigApp configApp : configApps) {
			try {
				json = new JSONObject();
				json.put("id", configApp.getId());
				json.put("name", configApp.getAppName());
				array.put(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				json.put("status", "0");
			}
		}
		return array.toString();
	}

	@RequestMapping(value = "statisticalDealPayListShow")
	public String statisticalReportShow(Long dealInfoId, Model model) {

		WorkDealInfo workDealInfo = workDealInfoService.findDealPayShow(dealInfoId);
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wType", WorkDealInfoType.WorkDealInfoTypeMap);
		return "modules/work/statisticalDealPayForm";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "form")
	public String form(WorkDealInfo workDealInfo, Model model) {
		model.addAttribute("workDealInfo", workDealInfo);
		WorkCompany workCompany = workDealInfo.getWorkCompany();
		WorkUser workUser = workDealInfo.getWorkUser();
		model.addAttribute("workCompany", workCompany);
		model.addAttribute("workUser", workUser);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("user", UserUtils.getUser());
		model.addAttribute("date", sdf.format(new Date()));
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo.getWorkCertInfo().getWorkCertApplyInfo());
		}
		if (workDealInfo.getId() != null) {

			ConfigProduct configProduct = workDealInfo.getConfigProduct();

			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());
			Set<Integer> nameSet = new HashSet<Integer>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
			}

			model.addAttribute("boundLabelList", nameSet);

			List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
			model.addAttribute("workLog", list);
			ConfigChargeAgent chargeAgent = chargeAgentService.get(workDealInfo.getConfigChargeAgentId());
			model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			List<ConfigProduct> products = configProductService.findByApp(workDealInfo.getConfigApp().getId());
			List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
			for (int i = 0; i < products.size(); i++) {
				// products.get(i).getProductName();

			
					String ssssi = ProductType.productTypeStrMap.get(products.get(i).getProductName())+"["+(products.get(i).getProductLabel()==0?"通用":"专用")+"]";
					ProductTypeObj obj = new ProductTypeObj(products.get(i).getId().intValue(), ssssi);
					listProductTypeObjs.add(obj);
				
				
			}
			model.addAttribute("proList", listProductTypeObjs);
		} else {

			//model.addAttribute("proList", ProductType.getProductTypeList());
			
			model.addAttribute("proList", ProductType.getProductList());

			
		}
		return "modules/work/workDealInfoForm";
	}

	/**
	 * 新增保存
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @param appId
	 * @param product
	 * @param dealInfType
	 * @param year
	 * @param yar
	 * @param companyId
	 * @param companyName
	 * @param organizationNumber
	 * @param orgExpirationTime
	 * @param selectLv
	 * @param comCertificateType
	 * @param comCertficateNumber
	 * @param comCertficateTime
	 * @param legalName
	 * @param s_province
	 * @param s_city
	 * @param s_county
	 * @param address
	 * @param companyMobile
	 * @param remarks
	 * @param workuserId
	 * @param workType
	 * @param contactName
	 * @param conCertType
	 * @param contacEmail
	 * @param conCertNumber
	 * @param contactPhone
	 * @param contactTel
	 * @param deal_info_status
	 * @param recordContent
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "save")
	public String save(Model model, RedirectAttributes redirectAttributes, Long appId, String product,
			Integer dealInfoType, Integer year, Long workDealInfoId, Integer yar, Long companyId, String companyName,
			String companyType, String organizationNumber, String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber, String comCertficateTime, String legalName,
			String s_province, String s_city, String s_county, String address, String companyMobile, String remarks,
			Long workuserId, Integer workType, String contactName, String conCertType, String contacEmail,
			String conCertNumber, String contactPhone, String contactTel, String deal_info_status, String recordContent,
			Integer agentId, Long agentDetailId,

	Integer classifying, String pName, String pEmail, String pIDCard, String contactSex, Integer lable,
			String areaRemark) {
		ConfigApp configApp = configAppService.get(appId);
		WorkCompany workCompany = workCompanyService.finByNameAndNumber(companyName, organizationNumber);
		ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(agentDetailId);
		// ConfigProduct configProduct = configProductService.findByIdOrLable(
		// appId, product, lable);
		// 保存单位信息
		ConfigProduct configProduct = bound.getProduct();
		workCompany.setCompanyName(companyName);
		workCompany.setCompanyType(companyType);
		workCompany.setComCertificateType(comCertificateType);

		SimpleDateFormat dnf = new SimpleDateFormat("yyyy-MM-dd");

		try {

			if (orgExpirationTime != null && !orgExpirationTime.equals("")) {
				Timestamp ts1 = new Timestamp(dnf.parse(orgExpirationTime).getTime());
				workCompany.setOrgExpirationTime(ts1);
			}
			if (comCertficateTime != null && !comCertficateTime.equals("")) {

				Timestamp ts = new Timestamp(dnf.parse(comCertficateTime).getTime());
				workCompany.setComCertficateTime(ts);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		// workCompany.setComCertficateTime(ts);
		workCompany.setComCertficateNumber(comCertficateNumber);
		workCompany.setSelectLv(selectLv);
		workCompany.setOrganizationNumber(organizationNumber);
		// workCompany.setOrgExpirationTime(ts1);
		workCompany.setProvince(s_province);
		workCompany.setLegalName(legalName);
		workCompany.setCity(s_city);
		workCompany.setDistrict(s_county);
		workCompany.setAddress(address);
		workCompany.setCompanyMobile(companyMobile);
		workCompany.setRemarks(remarks);
		workCompany.setAreaRemark(areaRemark);

		// 保存经办人信息
		WorkUser workUser = new WorkUser();
		workUser.setStatus(1);
		workUser.setContactName(contactName);
		workUser.setConCertType(conCertType);
		workUser.setConCertNumber(conCertNumber);
		workUser.setContactEmail(contacEmail);
		workUser.setContactPhone(contactPhone);
		workUser.setContactTel(contactTel);
		workUser.setWorkCompany(workCompany);
		workUser.setContactSex(contactSex);
		// 保存work_deal-info
		WorkDealInfo workDealInfo = null;
		if (workDealInfoId != null) {
			workDealInfo = workDealInfoService.get(workDealInfoId);
		} else {
			workDealInfo = new WorkDealInfo();
			workDealInfo.setSvn(workDealInfoService.getSVN(0));
		}
		if (configApp == null) {
			model.addAttribute("workUser", workUser);
			model.addAttribute("workCompany", workCompany);
			model.addAttribute("product", product);
			model.addAttribute("year", year);

			return "modules/work/workDealInfoForm";
		}
		workCompanyService.save(workCompany);
		workUserService.save(workUser);

		ConfigCommercialAgent commercialAgent = configAgentAppRelationService.findAgentByApp(configApp);
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
				.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size() > 0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());// 劳务关系外键
		}
		workDealInfo.setConfigApp(configApp);
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setConfigProduct(configProduct);

		// 新增时扣减计费策略数量
		ConfigChargeAgent agent = bound.getAgent();
		if (workDealInfoId == null) {
			Integer reseNum = agent.getReserveNum();
			Integer surNum = agent.getSurplusNum();

			if (reseNum != null) {
				agent.setReserveNum(reseNum + 1);
			}
			if (surNum != null && surNum >= 1) {
				agent.setSurplusNum(surNum - 1);
			}

			configChargeAgentService.save(agent);
		}
		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
		workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
		if (year == null) {
			workDealInfo.setYear(0);
		} else {
			workDealInfo.setYear(year);
		}
		workDealInfo.setClassifying(classifying);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setPayType(agentId);

		WorkCompanyHis workCompanyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(workCompanyHis);
		workDealInfo.setWorkCompanyHis(workCompanyHis);
		WorkUserHis workUserHis = workUserService.change(workUser, workCompanyHis);
		workUserHisService.save(workUserHis);
		workDealInfo.setWorkUserHis(workUserHis);
		// 保存申请人信息
		WorkCertApplyInfo workCertApplyInfo = null;
		WorkCertInfo workCertInfo = null;

		if (workDealInfo.getWorkCertInfo() != null) {
			workCertApplyInfo = workDealInfo.getWorkCertInfo().getWorkCertApplyInfo();
		} else {
			workCertApplyInfo = new WorkCertApplyInfo();
		}

		workCertApplyInfo.setName(pName);
		workCertApplyInfo.setEmail(pEmail);
		workCertApplyInfo.setIdCard(pIDCard);

		workCertApplyInfoService.save(workCertApplyInfo);

		if (workDealInfo.getWorkCertInfo() != null) {
			workCertInfo = workDealInfo.getWorkCertInfo();
		} else {
			workCertInfo = new WorkCertInfo();
		}
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfoService.save(workCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);

		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());

		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());

		workDealInfoService.save(workDealInfo);

		if (workDealInfoId == null) {
			ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
			dealInfoBound.setDealInfo(workDealInfo);
			ConfigChargeAgent agentBound = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
			dealInfoBound.setAgent(agentBound);
			configAgentBoundDealInfoService.save(dealInfoBound);
			logUtil.saveSysLog("计费策略模版", "计费策略模版：" + agent.getId() + "--业务编号：" + workDealInfo.getId() + "--关联成功!", "");
		}

		// 录入人日志保存
		WorkLog workLog1 = new WorkLog();
		workLog1.setRecordContent("录入完毕");
		workLog1.setWorkDealInfo(workDealInfo);
		workLog1.setCreateDate(new Date());
		workLog1.setCreateBy(UserUtils.getUser());
		workLog1.setConfigApp(configApp);
		workLog1.setWorkCompany(workCompany);
		workLog1.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog1);

		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(configApp);
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLog.setWorkCompany(workCompany);
		workLogService.save(workLog);

		logUtil.saveSysLog("业务中心",
				"新增业务保存：编号" + workDealInfo.getId() + "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(), "");
		logUtil.saveSysLog("业务办理",
				"新增业务保存：编号" + workDealInfo.getId() + "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(), "");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}

	/**
	 * 收费信息页面
	 * 
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "pay")
	public String pay(Model model, RedirectAttributes redirectAttributes, Long id) {
		model.addAttribute("id", id);
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		// 判断是否开户
		boolean account = false;
		if (workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_ADD_CERT) {
			account = false;
		} else {
			account = true;
		}
		Integer lable = workDealInfo.getConfigProduct().getProductLabel();
		if (lable == 0) {
			model.addAttribute("lable", "通用");
		}
		if (lable == 1) {
			model.addAttribute("lable", "专用");
		}
		model.addAttribute("workDealInfo", workDealInfo);
		String product = ProductType
				.getProductTypeName(Integer.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", product);
		ConfigChargeAgent chargeAgent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
		model.addAttribute("chargeAgent", chargeAgent);
		// 增加开户费
		if (!account) {
			Double money = configChargeAgentDetailService.getChargeMoney(workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType.getWorkType(WorkDealInfoType.TYPE_OPEN_ACCOUNT), null);
			model.addAttribute("type0", money);
		}
		// 按照业务类型查询费用
		if (workDealInfo.getDealInfoType() != null) {
			Double money = configChargeAgentDetailService.getChargeMoney(workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType.getWorkType(workDealInfo.getDealInfoType()), workDealInfo.getYear());
			if (workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_ADD_CERT) {
				model.addAttribute("type1", money);
			} else if (workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_UPDATE_CERT) {
				model.addAttribute("type2", money);
			} else if (workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_LOST_CHILD) {
				model.addAttribute("type3", money);
			} else if (workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_DAMAGED_REPLACED) {
				model.addAttribute("type4", money);
			} else if (workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_INFORMATION_REROUTE) {
				model.addAttribute("type5", money);
			}
		}
		if (workDealInfo.getDealInfoType1() != null) {
			Double money = configChargeAgentDetailService.getChargeMoney(workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType.getWorkType(workDealInfo.getDealInfoType1()), workDealInfo.getYear());
			if (workDealInfo.getDealInfoType1() == WorkDealInfoType.TYPE_ADD_CERT) {
				model.addAttribute("type1", money);
			} else if (workDealInfo.getDealInfoType1() == WorkDealInfoType.TYPE_UPDATE_CERT) {
				model.addAttribute("type2", money);
			} else if (workDealInfo.getDealInfoType1() == WorkDealInfoType.TYPE_LOST_CHILD) {
				model.addAttribute("type3", money);
			} else if (workDealInfo.getDealInfoType1() == WorkDealInfoType.TYPE_DAMAGED_REPLACED) {
				model.addAttribute("type4", money);
			} else if (workDealInfo.getDealInfoType1() == WorkDealInfoType.TYPE_INFORMATION_REROUTE) {
				model.addAttribute("type5", money);
			}
		}
		if (workDealInfo.getDealInfoType2() != null) {
			Double money = configChargeAgentDetailService.getChargeMoney(workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType.getWorkType(workDealInfo.getDealInfoType2()), workDealInfo.getYear());
			if (workDealInfo.getDealInfoType2() == WorkDealInfoType.TYPE_ADD_CERT) {
				model.addAttribute("type1", money);
			} else if (workDealInfo.getDealInfoType2() == WorkDealInfoType.TYPE_UPDATE_CERT) {
				model.addAttribute("type2", money);
			} else if (workDealInfo.getDealInfoType2() == WorkDealInfoType.TYPE_LOST_CHILD) {
				model.addAttribute("type3", money);
			} else if (workDealInfo.getDealInfoType2() == WorkDealInfoType.TYPE_DAMAGED_REPLACED) {
				model.addAttribute("type4", money);
			} else if (workDealInfo.getDealInfoType2() == WorkDealInfoType.TYPE_INFORMATION_REROUTE) {
				model.addAttribute("type5", money);
			}
		}
		if (workDealInfo.getDealInfoType3() != null) {
			Double money = configChargeAgentDetailService.getChargeMoney(workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType.getWorkType(workDealInfo.getDealInfoType3()), workDealInfo.getYear());
			if (workDealInfo.getDealInfoType3() == WorkDealInfoType.TYPE_ADD_CERT) {
				model.addAttribute("type1", money);
			} else if (workDealInfo.getDealInfoType3() == WorkDealInfoType.TYPE_UPDATE_CERT) {
				model.addAttribute("type2", money);
			} else if (workDealInfo.getDealInfoType3() == WorkDealInfoType.TYPE_LOST_CHILD) {
				model.addAttribute("type3", money);
			} else if (workDealInfo.getDealInfoType3() == WorkDealInfoType.TYPE_DAMAGED_REPLACED) {
				model.addAttribute("type4", money);
			} else if (workDealInfo.getDealInfoType3() == WorkDealInfoType.TYPE_INFORMATION_REROUTE) {
				model.addAttribute("type5", money);
			}
		}

		// 同时更新变更如果钱大于新增按新增算（本次修改不做控制，缴费页面灵活录入）
		// if (type == WorkDealInfoType.TYPE_INFORMATION_REROUTE &&
		// workDealInfo.getDealInfoType1() !=null) {
		// Double addMoney = configChargeAgentDetailService.selectMoney(
		// chargeAgent, WorkDealInfoType.TYPE_ADD_CERT, workDealInfo.getYear(),
		// workDealInfo
		// .getConfigProduct().getProductLabel());
		// if (money+money1 > addMoney) {
		// model.addAttribute("addMoney", addMoney);
		// }
		// }
		// 如果为修改则待会上次缴费历史
		List<String[]> payInfos = new ArrayList<String[]>();
		WorkPayInfo payInfo = workDealInfo.getWorkPayInfo();
		if (payInfo != null) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String officeName = workDealInfo.getWorkCompany().getCompanyName();
			String userName = workDealInfo.getCreateBy().getName();
			String date = sdf1.format(workDealInfo.getCreateDate());
			if (payInfo.getMethodMoney() && payInfo.getMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getMoney().toString(), officeName,
						userName, date, "现金", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodBank() && payInfo.getBankMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getBankMoney().toString(), officeName,
						userName, date, "银行转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodPos() && payInfo.getPosMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getPosMoney().toString(), officeName,
						userName, date, "POS收款", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodAlipay() && payInfo.getAlipayMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getAlipayMoney().toString(),
						officeName, userName, date, "支付宝转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodGov()) {
				String[] s = new String[] { payInfo.getSn(), officeName, "", officeName, userName, date, "政府统一采购", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodContract()) {
				String[] s = new String[] { payInfo.getSn(), officeName, "", officeName, userName, date, "合同采购", "" };
				payInfos.add(s);
			}
			List<WorkFinancePayInfoRelation> pay = workFinancePayInfoRelationService
					.findByPayInfo(workDealInfo.getWorkPayInfo());

			model.addAttribute("fpir", pay);
			model.addAttribute("infos", payInfos);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice().getName());
		model.addAttribute("date", sdf.format(new Date()));
		model.addAttribute("dealInfoType",
				WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType()) + " "
						+ WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType1()) + " "
						+ WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType2()) + " "
						+ WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType3()));

		if (workDealInfo.getDealInfoType() != null
				&& workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_ADD_CERT)) {
			if (workDealInfo.getWorkPayInfo() != null) {
				return "modules/work/workDealInfoErrorLoad";
			}
		}

		return "modules/work/workDealInfoMaintainLoad";
	}

	/**
	 * 返回基本信息界面
	 */
	@RequestMapping(value = "typeFormReturnUpdate")
	public String typeFormReturnUpdate(WorkDealInfo workDealInfo, Model model, RedirectAttributes redirectAttributes) {

		boolean inOffice = false;
		workDealInfo.setDealInfoStatus("15");
		workDealInfoService.save(workDealInfo);

		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		if (!inOffice) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "请到业务办理网点办理！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}

		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo.getWorkCertInfo().getWorkCertApplyInfo());
		}

		ConfigChargeAgent chargeAgent = chargeAgentService.get(workDealInfo.getConfigChargeAgentId());
		model.addAttribute("tempStyle", chargeAgent.getTempStyle());
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));
		model.addAttribute("workDealInfo", workDealInfo);

		ConfigProduct configProduct = workDealInfo.getConfigProduct();
		List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
				.findByProIdAll(configProduct.getId());
		Set<Integer> nameSet = new HashSet<Integer>();
		for (int i = 0; i < boundList.size(); i++) {
			nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
		}
		model.addAttribute("boundLabelList", nameSet);
		List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
		model.addAttribute("workLog", list);
		model.addAttribute("tempStyle", chargeAgent.getTempStyle());

		ArrayList<Integer> dealInfoTypes = new ArrayList<Integer>();
		if (workDealInfo.getDealInfoType() != null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType());
		}
		if (workDealInfo.getDealInfoType1() != null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType1());
		}
		if (workDealInfo.getDealInfoType2() != null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType2());
		}
		if (workDealInfo.getDealInfoType3() != null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType3());
		}

		if (workDealInfo.getSelfApplyId() != null) {
			model.addAttribute("imgUrl",Global.getConfig("images.path"));
			SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo.getSelfApplyId());
			workDealInfo.setSelfImage(selfImage);
		}
		if (workDealInfo.getSelfImage()!=null) {
			model.addAttribute("imgUrl",Global.getConfig("images.path"));
		}
		
		
		if (dealInfoTypes.size() == 1) {
			if (dealInfoTypes.get(0).equals(4)) {// 变更
				return "modules/work/maintain/workDealInfoMaintainReturnChange";
			} else if (dealInfoTypes.get(0).equals(1)) {
				model.addAttribute("update", "3");
				ConfigProduct configProductOld = workDealInfo.getConfigProduct();
				String[] years = configChargeAgentDetailService.getChargeAgentYears(configProductOld.getChargeAgentId(),
						WorkDealInfoType.TYPE_UPDATE_CERT);
				for (int j = 0; j < years.length; j++) {
					switch (years[j]) {
					case "1":
						model.addAttribute("year1", true);
						break;
					case "2":
						model.addAttribute("year2", true);
						break;
					case "3":
						model.addAttribute("year3", true);
						break;
					case "4":
						model.addAttribute("year4", true);
						break;
					case "5":
						model.addAttribute("year5", true);
						break;
					}
				}
				model.addAttribute("dealType", dealInfoTypes.toString());
				return "modules/work/maintain/workDealInfoMaintainReturnUpdate";
			} else if (dealInfoTypes.get(0).equals(2) || dealInfoTypes.get(0).equals(3)) {
				model.addAttribute("reissue", dealInfoTypes.get(0));
				return "modules/work/maintain/workDealInfoMaintainReturnLost";
			}

		} else if (dealInfoTypes.size() == 2) {
			if (dealInfoTypes.get(0).equals(2) || dealInfoTypes.get(0).equals(3)) {
				if (dealInfoTypes.get(1).equals(4)) {
					model.addAttribute("reissue", dealInfoTypes.get(0));
					return "modules/work/maintain/workDealInfoMaintainReturnChange";
				}
			} else if (dealInfoTypes.get(0).equals(1)) {
				if (dealInfoTypes.get(1).equals(2) || dealInfoTypes.get(1).equals(3)) {
					model.addAttribute("update", "3");
					model.addAttribute("reissue", dealInfoTypes.get(1));
					ConfigProduct configProductOld = workDealInfo.getConfigProduct();
					String[] years = configChargeAgentDetailService.getChargeAgentYears(
							configProductOld.getChargeAgentId(), WorkDealInfoType.TYPE_UPDATE_CERT);
					for (int j = 0; j < years.length; j++) {
						switch (years[j]) {
						case "1":
							model.addAttribute("year1", true);
							break;
						case "2":
							model.addAttribute("year2", true);
							break;
						case "3":
							model.addAttribute("year3", true);
							break;
						case "4":
							model.addAttribute("year4", true);
							break;
						case "5":
							model.addAttribute("year5", true);
							break;
						}
					}
					model.addAttribute("dealType", dealInfoTypes.toString());
					return "modules/work/maintain/workDealInfoMaintainReturnUpdate";
				} else if (dealInfoTypes.get(1).equals(4)) {
					model.addAttribute("update", "3");
					ConfigProduct configProductOld = workDealInfo.getConfigProduct();
					String[] years = configChargeAgentDetailService.getChargeAgentYears(
							configProductOld.getChargeAgentId(), WorkDealInfoType.TYPE_UPDATE_CERT);
					for (int j = 0; j < years.length; j++) {
						switch (years[j]) {
						case "1":
							model.addAttribute("year1", true);
							break;
						case "2":
							model.addAttribute("year2", true);
							break;
						case "3":
							model.addAttribute("year3", true);
							break;
						case "4":
							model.addAttribute("year4", true);
							break;
						case "5":
							model.addAttribute("year5", true);
							break;
						}
					}
					model.addAttribute("dealType", dealInfoTypes.toString());
					return "modules/work/maintain/workDealInfoMaintainReturnUpdateChange";
				}
			}
		} else if (dealInfoTypes.size() == 3) {
			model.addAttribute("update", "3");
			model.addAttribute("reissue", dealInfoTypes.get(1));
			ConfigProduct configProductOld = workDealInfo.getConfigProduct();
			String[] years = configChargeAgentDetailService.getChargeAgentYears(configProductOld.getChargeAgentId(),
					WorkDealInfoType.TYPE_UPDATE_CERT);
			for (int j = 0; j < years.length; j++) {
				switch (years[j]) {
				case "1":
					model.addAttribute("year1", true);
					break;
				case "2":
					model.addAttribute("year2", true);
					break;
				case "3":
					model.addAttribute("year3", true);
					break;
				case "4":
					model.addAttribute("year4", true);
					break;
				case "5":
					model.addAttribute("year5", true);
					break;
				}
			}
			model.addAttribute("dealType", dealInfoTypes.toString());
			return "modules/work/maintain/workDealInfoMaintainReturnUpdateChange";
		}
		return null;
	}

	/**
	 * 业务更新操作form界面
	 * 
	 * @param workDealInfo
	 * @param model
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "updateForm")
	public String updateForm(WorkDealInfo workDealInfo, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("workDealInfo", workDealInfo);
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo.getWorkCertInfo().getWorkCertApplyInfo());
		}
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		if (!inOffice) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "请到业务办理网点更新！");
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
		}

		// List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
		// model.addAttribute("workLog", list);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));
		return "modules/work/workDealInfoUpdate";
	}

	/**
	 * 更新操作计费界面
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @param recordContent
	 * @param workDealInfoId
	 * @param year
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "saveUpdate")
	public String saveUpdate(Model model, RedirectAttributes redirectAttributes, String recordContent,
			Long workDealInfoId, Integer year, Long newInfoId) {
		if (newInfoId != null) {
			workDealInfoService.deleteWork(newInfoId);
		}
		// 新建业务信息
		WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfoId);
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(workDealInfo.getConfigApp());
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
				.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size() > 0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());// 劳务关系外键
		}
		workDealInfo.setWorkUser(workDealInfo1.getWorkUser());
		workDealInfo.setWorkCompany(workDealInfo1.getWorkCompany());
		workDealInfo.setWorkCompanyHis(workDealInfo1.getWorkCompanyHis());
		workDealInfo.setWorkUserHis(workDealInfo1.getWorkUserHis());
		workDealInfo.setConfigProduct(workDealInfo1.getConfigProduct());
		if (year == null) {
			workDealInfo.setYear(0);
		} else {
			workDealInfo.setYear(year);
		}
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setClassifying(workDealInfo1.getClassifying());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		workDealInfo.setWorkCertInfo(workDealInfo1.getWorkCertInfo());
		workDealInfo.setPrevId(workDealInfo1.getId());
		if (workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo().getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}

		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		WorkCertApplyInfo workCertApplyInfo = workDealInfo1.getWorkCertInfo().getWorkCertApplyInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate(workCertInfoService.getCreateDate(oldCertInfo.getId()));
		workCertInfoService.save(workCertInfo);
		// 给上张证书存nextId
		oldCertInfo.setRenewalNextId(workCertInfo.getId());
		workCertInfoService.save(oldCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		// List<ConfigAgentOfficeRelation> list =
		// configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		// if (list.size()>0) {
		// workDealInfo.setCommercialAgent(list.get(0).getConfigCommercialAgent());
		// }
		workDealInfoService.delete(workDealInfo1.getId());
		workDealInfoService.save(workDealInfo);
		// 保存日志信息
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(workDealInfo.getConfigApp());
		workLog.setWorkCompany(workDealInfo.getWorkCompany());
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		// 计算更新金额
		ConfigChargeAgent chargeAgent = configChargeAgentService
				.get(workDealInfo.getConfigProduct().getChargeAgentId());

		Double money = configChargeAgentDetailService.selectMoney(chargeAgent, WorkDealInfoType.TYPE_UPDATE_CERT, year,
				workDealInfo.getConfigProduct().getProductLabel());
		model.addAttribute("type2", money);
		if (workDealInfo.getConfigProduct().getProductLabel() == 0) {
			model.addAttribute("lable", "通用");
		}
		if (workDealInfo.getConfigProduct().getProductLabel() == 1) {
			model.addAttribute("lable", "专用");
		}
		model.addAttribute("chargeAgent", chargeAgent);
		model.addAttribute("year", year);
		model.addAttribute("workDealInfo", workDealInfo);
		model.addAttribute("dealInfoType", WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		String product = ProductType
				.getProductTypeName(Integer.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", product);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice().getName());
		model.addAttribute("date", sdf.format(new Date()));
		logUtil.saveSysLog("业务中心",
				"业务更新：编号" + workDealInfo.getId() + "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(), "");
		return "modules/work/workDealInfoUpdateLoad";
	}

	/**
	 * 暂时保存
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @param appId
	 * @param product
	 * @param dealInfType
	 * @param year
	 * @param yar
	 * @param companyId
	 * @param companyName
	 * @param companyType
	 * @param organizationNumber
	 * @param orgExpirationTime
	 * @param selectLv
	 * @param comCertificateType
	 * @param comCertficateNumber
	 * @param comCertficateTime
	 * @param legalName
	 * @param s_province
	 * @param s_city
	 * @param s_county
	 * @param address
	 * @param companyMobile
	 * @param remarks
	 * @param workuserId
	 * @param workType
	 * @param contactName
	 * @param conCertType
	 * @param contacEmail
	 * @param conCertNumber
	 * @param contactPhone
	 * @param contactTel
	 * @param deal_info_status
	 * @param recordContent
	 * @param classifying
	 * @param submiType
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "temporarySave")
	public String temporarySave(Model model, Long workDealInfoId, RedirectAttributes redirectAttributes, Long appId,
			String product, Integer dealInfType1, Integer year, Integer yar, Long companyId, String companyName,
			String companyType, String organizationNumber, String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber, String comCertficateTime, String legalName,
			String s_province, String s_city, String s_county, String address, String companyMobile, String remarks,
			Long workuserId, Integer workType, String contactName, String conCertType, String contacEmail,
			String conCertNumber, String contactPhone, String contactTel, String deal_info_status, String recordContent,
			Integer lable, Integer classifying, String pName, String pEmail, String pIDCard, String contactSex,
			String areaRemark, Integer agentId, Long agentDetailId) {

		// Integer agentId,Long agentDetailId,

		WorkCompany workCompany = workCompanyService.finByNameAndNumber(companyName, organizationNumber);

		// 保存单位信息

		workCompany.setCompanyName(companyName);
		workCompany.setCompanyType(companyType);
		workCompany.setComCertificateType(comCertificateType);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		try {
			ts1 = Timestamp.valueOf(orgExpirationTime + " 00:00:00");
			ts = Timestamp.valueOf(comCertficateTime + " 00:00:00");
		} catch (Exception e) {
			// TODO: handle exception
		}
		workCompany.setComCertficateTime(ts);
		workCompany.setComCertficateNumber(comCertficateNumber);
		workCompany.setSelectLv(selectLv);
		workCompany.setOrganizationNumber(organizationNumber);
		workCompany.setOrgExpirationTime(ts1);
		workCompany.setProvince(s_province);
		workCompany.setLegalName(legalName);
		workCompany.setCity(s_city);
		workCompany.setDistrict(s_county);
		workCompany.setAddress(address);
		workCompany.setCompanyMobile(companyMobile);
		workCompany.setRemarks(remarks);
		workCompany.setAreaRemark(areaRemark);

		// 保存经办人信息
		WorkUser workUser = new WorkUser();
		workUser.setId(workuserId);
		workUser.setStatus(1);
		workUser.setContactName(contactName);
		workUser.setConCertType(conCertType);
		workUser.setConCertNumber(conCertNumber);
		workUser.setContactEmail(contacEmail);
		workUser.setContactPhone(contactPhone);
		workUser.setContactTel(contactTel);
		workUser.setContactSex(contactSex);
		// 保存work_deal-info
		WorkDealInfo workDealInfo = null;
		if (workDealInfoId != null) {
			workDealInfo = workDealInfoService.get(workDealInfoId);
		} else {
			workDealInfo = new WorkDealInfo();
		}

		if (appId == null) {
			model.addAttribute("workUser", workUser);
			model.addAttribute("workCompany", workCompany);
			model.addAttribute("product", product);
			model.addAttribute("year", year);

			return "modules/work/workDealInfoForm";
		}
		ConfigApp configApp = configAppService.get(appId);

		ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(agentDetailId);

		ConfigProduct configProduct = bound.getProduct();
		//
		// ConfigProduct configProduct = configProductService.findByIdOrLable(
		// appId, product, lable);

		workCompanyService.save(workCompany);
		workUserService.save(workUser);

		workDealInfo.setConfigApp(configApp);
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService.findAgentByApp(configApp);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
				.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size() > 0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());// 劳务关系外键
		}
		workDealInfo.setCommercialAgent(commercialAgent);
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setConfigProduct(configProduct);

		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
		workDealInfo.setPayType(agentId);

		workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
		workDealInfo.setDealInfoType1(dealInfType1);

		if (year == null) {
			workDealInfo.setYear(0);
		} else {
			workDealInfo.setYear(year);
		}
		workDealInfo.setClassifying(classifying);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_TEMPED);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		WorkCompanyHis workCompanyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(workCompanyHis);
		workDealInfo.setWorkCompanyHis(workCompanyHis);
		WorkUserHis workUserHis = workUserService.change(workUser, workCompanyHis);
		workUserHisService.save(workUserHis);
		workDealInfo.setWorkUserHis(workUserHis);
		// 保存申请人信息
		WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		;
		workCertApplyInfo.setName(pName);
		workCertApplyInfo.setEmail(pEmail);
		workCertApplyInfo.setIdCard(pIDCard);
		workCertApplyInfo.setProvince(workCompany.getProvince());
		workCertApplyInfo.setCity(workCompany.getCity());
		workCertApplyInfoService.save(workCertApplyInfo);
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfoService.save(workCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
		workDealInfoService.save(workDealInfo);
		// 录入人日志保存
		WorkLog workLog = new WorkLog();
		workLog.setRecordContent(recordContent);
		workLog.setWorkDealInfo(workDealInfo);
		workLog.setCreateDate(new Date());
		workLog.setCreateBy(UserUtils.getUser());
		workLog.setConfigApp(configApp);
		workLog.setWorkCompany(workCompany);
		workLog.setOffice(UserUtils.getUser().getOffice());
		workLogService.save(workLog);
		logUtil.saveSysLog("业务中心",
				"新增业务暂时保存：编号" + workDealInfo.getId() + "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(), "");

		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workDealInfoService.delete(id);
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		if (workDealInfo.getDealInfoType() != null) {
			Long agentId = workDealInfo.getConfigChargeAgentId();
			ConfigChargeAgent agent = configChargeAgentService.get(agentId);

			if (workDealInfo.getDealInfoType().equals(1)) {
				agent.setSurplusUpdateNum(agent.getSurplusUpdateNum() + 1);
				agent.setReserveUpdateNum(agent.getReserveUpdateNum() - 1);
			} else if (workDealInfo.getDealInfoType().equals(0)) {
				agent.setSurplusNum(agent.getSurplusNum() + 1);
				agent.setReserveNum(agent.getReserveNum() - 1);

			}
			configChargeAgentService.save(agent);

			ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agent.getId(), id);
			if (bound != null) {
				configAgentBoundDealInfoService.deleteById(bound.getId());
			}
		}

		if (workDealInfo.getPrevId() != null) {
			WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfo.getPrevId());
			workDealInfo1.setDelFlag(WorkDealInfo.DEL_FLAG_NORMAL);
			workDealInfoService.save(workDealInfo1);
		}
		logUtil.saveSysLog("业务中心", "删除业务：编号" + id + "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(), "");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "exceptionEdit")
	public String exceptionEdit(Long id, RedirectAttributes redirectAttributes) {
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		if (workDealInfo.getDealInfoType() != null) {
			Long agentId = workDealInfo.getConfigChargeAgentId();
			ConfigChargeAgent agent = configChargeAgentService.get(agentId);
			agent.setSurplusNum(agent.getSurplusNum() + 1);
			agent.setReserveNum(agent.getReserveNum() - 1);
			configChargeAgentService.save(agent);

			ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agent.getId(), id);
			if (bound != null) {
				configAgentBoundDealInfoService.deleteById(bound.getId());
			}
		}

		// return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}

	/**
	 * 收费信息页面
	 * 
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "load")
	public String load(Model model, RedirectAttributes redirectAttributes, Long id) {
		model.addAttribute("id", id);
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		// // 判断是否开户
		// List<WorkDealInfo> wdi = workDealInfoService.selectAccount(
		// workDealInfo.getConfigApp(), workDealInfo.getConfigProduct(),
		// workDealInfo.getWorkCompany(), id);
		boolean account = false;
		if (workDealInfo.getDealInfoType() == WorkDealInfoType.TYPE_ADD_CERT) {
			account = false;
		} else {
			account = true;
		}
		Integer lable = workDealInfo.getConfigProduct().getProductLabel();
		if (lable == 0) {
			model.addAttribute("lable", "通用");
		}
		if (lable == 1) {
			model.addAttribute("lable", "专用");
		}
		model.addAttribute("workDealInfo", workDealInfo);
		String product = ProductType
				.getProductTypeName(Integer.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", product);
		int type = workDealInfo.getDealInfoType();
		model.addAttribute("dealInfoType", WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		ConfigChargeAgent chargeAgent = configChargeAgentService
				.get(workDealInfo.getConfigProduct().getChargeAgentId());

		if (!account) {
			Double money = configChargeAgentDetailService.selectMoney(chargeAgent, WorkDealInfoType.TYPE_OPEN_ACCOUNT,
					null, workDealInfo.getConfigProduct().getProductLabel());
			model.addAttribute("type0", money);
		}
		Double money = configChargeAgentDetailService.selectMoney(chargeAgent, type, workDealInfo.getYear(),
				workDealInfo.getConfigProduct().getProductLabel());

		if (type == 0) {
			model.addAttribute("type1", money);
		}
		if (type == 1) {
			model.addAttribute("type2", money);
		}
		if (type == 2) {
			model.addAttribute("type3", money);
		}
		if (type == 3) {
			model.addAttribute("type4", money);
		}
		if (type == 4) {
			model.addAttribute("type5", money);
		}
		if (type == 6) {
			model.addAttribute("type6", money);
		}
		Double money1 = 0D;
		if (workDealInfo.getDealInfoType1() != null) {
			int type1 = workDealInfo.getDealInfoType1();
			money1 = configChargeAgentDetailService.selectMoney(chargeAgent, type1, workDealInfo.getYear(),
					workDealInfo.getConfigProduct().getProductLabel());
			if (type1 == 0) {
				model.addAttribute("type1", money1);
			}
			if (type1 == 1) {
				model.addAttribute("type2", money1);
			}
			if (type1 == 2) {
				model.addAttribute("type3", money1);
			}
			if (type1 == 3) {
				model.addAttribute("type4", money1);
			}
			if (type1 == 4) {
				model.addAttribute("type5", money1);
			}
			if (type1 == 6) {
				model.addAttribute("type6", money1);
			}
		}
		if (type == WorkDealInfoType.TYPE_INFORMATION_REROUTE && workDealInfo.getDealInfoType1() != null) {
			Double addMoney = configChargeAgentDetailService.selectMoney(chargeAgent, WorkDealInfoType.TYPE_ADD_CERT,
					workDealInfo.getYear(), workDealInfo.getConfigProduct().getProductLabel());
			if (money + money1 > addMoney) {
				model.addAttribute("addMoney", addMoney);
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice().getName());
		model.addAttribute("date", sdf.format(new Date()));
		List<String[]> payInfos = new ArrayList<String[]>();
		WorkPayInfo payInfo = workDealInfo.getWorkPayInfo();
		if (payInfo != null) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String officeName = workDealInfo.getWorkCompany().getCompanyName();
			String userName = workDealInfo.getCreateBy().getName();
			String date = sdf1.format(workDealInfo.getCreateDate());
			if (payInfo.getMethodMoney() && payInfo.getMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getMoney().toString(), officeName,
						userName, date, "现金", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodBank() && payInfo.getBankMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getBankMoney().toString(), officeName,
						userName, date, "银行转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodPos() && payInfo.getPosMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getPosMoney().toString(), officeName,
						userName, date, "POS收款", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodAlipay() && payInfo.getAlipayMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName, payInfo.getAlipayMoney().toString(),
						officeName, userName, date, "支付宝转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodGov()) {
				String[] s = new String[] { payInfo.getSn(), officeName, "", officeName, userName, date, "政府统一采购", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodContract()) {
				String[] s = new String[] { payInfo.getSn(), officeName, "", officeName, userName, date, "合同采购", "" };
				payInfos.add(s);
			}
		}
		List<WorkFinancePayInfoRelation> pay = workFinancePayInfoRelationService
				.findByPayInfo(workDealInfo.getWorkPayInfo());

		model.addAttribute("fpir", pay);
		model.addAttribute("infos", payInfos);
		// 缴费信息方式
		model.addAttribute("chargeAgent", chargeAgent);
		addMessage(redirectAttributes, "录入信息成功");
		String url = "";
		if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_ADD_CERT)) {
			if (workDealInfo.getWorkPayInfo() != null) {
				url = "modules/work/workDealInfoErrorLoad";
			} else {
				url = "modules/work/workDealInfoLoad";
			}
		} else if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_UPDATE_CERT)) {
			url = "modules/work/workDealInfoUpdateLoad";
		} else if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_LOST_CHILD)) {
			url = "modules/work/workDealInfoReissueLoad";
		} else if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
			url = "modules/work/workDealInfoReissueLoad";
		} else if (workDealInfo.getDealInfoType().equals(WorkDealInfoType.TYPE_INFORMATION_REROUTE)) {
			url = "modules/work/workDealInfoChangeLoad";
		}

		return url;
	}

	/**
	 * app姓名自动补全
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "app")
	@ResponseBody
	public String app(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			ArrayList<Object> lis = new ArrayList<Object>();
			User user = UserUtils.getUser();
			List<ConfigAppOfficeRelation> list = configAppOfficeRelationService
					.findAllByOfficeId(user.getOffice().getId());
			for (ConfigAppOfficeRelation configAppOfficeRelation : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", configAppOfficeRelation.getConfigApp().getAppName());
				map.put("result", configAppOfficeRelation.getConfigApp().getId());
				lis.add(map);
			}
			json.put("lis", lis);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return json.toString();
	}

	/**
	 * 根据带回的app查询应有的product
	 * 
	 * @param appId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "product")
	@ResponseBody
	public String product(Long appId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			json.put("appId", appId);
			ConfigApp configApp = new ConfigApp();
			configApp.setId(appId);
			ConfigProduct configProduct = new ConfigProduct();
			configProduct.setConfigApp(configApp);
			List<ConfigProduct> list = configProductService.findByAppIdAndRa(configProduct);
			Set set = new HashSet();
			ArrayList lis = new ArrayList();
			for (ConfigProduct con : list) {
				lis.add(con.getProductName()+con.getProductLabel()+con.getId());
				
			}
			set.addAll(lis);
			for (Iterator it = set.iterator(); it.hasNext();) {
				String product = (String) it.next();
				
				if (product.startsWith("10")) {
					json.put("product10", product.substring(2));
				}
				if (product.startsWith("11")) {
					json.put("product11", product.substring(2));
				}
				if (product.startsWith("20")) {
					json.put("product20", product.substring(2));
				}
				if (product.startsWith("21")) {
					json.put("product21", product.substring(2));
				}
				if (product.startsWith("30")) {
					json.put("product30", product.substring(2));
				}
				if (product.startsWith("31")) {
					json.put("product31", product.substring(2));
				}
				if (product.startsWith("40")) {
					json.put("product40", product.substring(2));
				}
				if (product.startsWith("41")) {
					json.put("product41", product.substring(2));
				}
				if (product.startsWith("60")) {
					json.put("product60", product.substring(2));
				}
				if (product.startsWith("61")) {
					json.put("product61", product.substring(2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 单位自动补全
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "completeCompanyName")
	@ResponseBody
	public String completeCompanyName(HttpServletRequest request, HttpServletResponse response, String companyname,Long productId) {
		JSONObject json = new JSONObject();
		try {
			List<WorkCompany> workCompany = workCompanyService.findByCompanyName(companyname);
			ArrayList<Long> lis = new ArrayList<Long>();
			for (int i = 0; i < workCompany.size(); i++) {
				lis.add(workCompany.get(i).getId());
			}
			json.put("Idlis", lis);
			
			ConfigProduct product = configProductService.get(productId);
			json.put("productId", product.getProductName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "tt")
	@ResponseBody
	public String tt(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			List<WorkCompany> list = workCompanyService.findByNotNull();
			ArrayList lis = new ArrayList();
			for (WorkCompany workCompany : list) {
				Map map = new HashMap();
				map.put("title", workCompany.getCompanyName());
				map.put("result", workCompany.getId());
				lis.add(map);
			}
			json.put("lis", lis);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return json.toString();
	}

	/**
	 * 根据单位名称带回
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "cert")
	@ResponseBody
	public String cert(Long id, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			WorkCompany workCompany = workCompanyService.get(id);
			List<WorkUser> list = workUserService.findUserList(workCompany);
			if (list.size() > 0) {
				WorkUser workuser = list.get(0);
				json.put("workuserId", workuser.getId());
				json.put("contactName", workuser.getContactName());
				json.put("contacEmail", workuser.getContactEmail());
				json.put("contactPhone", workuser.getContactPhone());
				json.put("contactTel", workuser.getContactTel());
				json.put("conCertType", workuser.getConCertType());
				json.put("conCertSex", workuser.getContactSex());
				json.put("conCertNumber", workuser.getConCertNumber());
			}

			// 前台带回单位数据
			SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");

			json.put("companyId", workCompany.getId());
			json.put("organizationNumber", workCompany.getOrganizationNumber());
			json.put("companyType", workCompany.getCompanyType());
			json.put("selectLv", workCompany.getSelectLv());
			json.put("comCertficateNumber", workCompany.getComCertficateNumber());
			json.put("comCertificateType", workCompany.getComCertificateType());
			json.put("legalName", workCompany.getLegalName());
			json.put("address", workCompany.getAddress());
			json.put("remarks", workCompany.getRemarks());
			json.put("workCompany", workCompany);
			json.put("city", workCompany.getCity());
			json.put("province", workCompany.getProvince());
			json.put("district", workCompany.getDistrict());
			json.put("companyMobile", workCompany.getCompanyMobile());

			if (workCompany.getOrgExpirationTime() != null) {
				json.put("orgExpirationTime", smd.format(workCompany.getOrgExpirationTime()));
			}
			if (workCompany.getComCertficateTime() != null) {
				json.put("comCertficateTime", smd.format(workCompany.getComCertficateTime()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	// @RequiresPermissions("work:workDealInfo:edit")
	// @RequestMapping(value = "cert1")
	// @ResponseBody
	// public String cert(List<Long> id, HttpServletRequest request,
	// HttpServletResponse response) {
	// JSONObject json = new JSONObject();
	// try {
	// List<WorkCompany> workCompany = workCompanyService.getIds(id);
	// if(workCompany.size()>0)
	// {
	// ArrayList<Long> arrayList=new ArrayList<Long>();
	// for(int i=0;i<workCompany.size();i++)
	// {
	// arrayList.add(workCompany.get(i).getId());
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return json.toString();
	// }

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "findCompanyInformation")
	@ResponseBody
	public String findCompanyInformation(Long id, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			WorkDealInfo workDealInfo = workDealInfoService.get(id);
			if (workDealInfo.getWorkUser() != null) {
				WorkUser workuser = workDealInfo.getWorkUser();
				json.put("workuserId", workuser.getId());
				json.put("contactName", workuser.getContactName());
				json.put("contacEmail", workuser.getContactEmail());
				json.put("contactPhone", workuser.getContactPhone());
				json.put("contactTel", workuser.getContactTel());
				json.put("conCertType", workuser.getConCertType());
				json.put("conCertSex", workuser.getContactSex());
				json.put("conCertNumber", workuser.getConCertNumber());
			}
			if (workDealInfo.getWorkCompany() != null) {
				WorkCompany workCompany = workDealInfo.getWorkCompany();

				// 前台带回单位数据
				SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd");

				json.put("companyId", workCompany.getId());
				json.put("organizationNumber", workCompany.getOrganizationNumber());
				json.put("companyType", workCompany.getCompanyType());
				json.put("selectLv", workCompany.getSelectLv());
				json.put("comCertficateNumber", workCompany.getComCertficateNumber());
				json.put("comCertificateType", workCompany.getComCertificateType());
				json.put("legalName", workCompany.getLegalName());
				json.put("address", workCompany.getAddress());
				json.put("remarks", workCompany.getRemarks());
				json.put("workCompany", workCompany);
				json.put("city", workCompany.getCity());
				json.put("province", workCompany.getProvince());
				json.put("district", workCompany.getDistrict());
				json.put("companyMobile", workCompany.getCompanyMobile());

				if (workCompany.getOrgExpirationTime() != null) {
					json.put("orgExpirationTime", smd.format(workCompany.getOrgExpirationTime()));
				}
				if (workCompany.getComCertficateTime() != null) {
					json.put("comCertficateTime", smd.format(workCompany.getComCertficateTime()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "type")
	@ResponseBody
	public String type(String name, Long appId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			List<ConfigProduct> list = configProductService.findByName(name, appId);

			for (ConfigProduct configProduct : list) {
				if (configProduct.getProductLabel() == 0) {
					json.put("type0", true);
				}
				if (configProduct.getProductLabel() == 1) {
					json.put("type1", true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 缴费查询快速搜索
	 * 
	 * @param load
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "payment")
	@ResponseBody
	public String payment(String load, Date startTime, Date endTime, Long appId) throws Exception {
		load = URLDecoder.decode(load, "UTF-8");
		JSONArray jsonArray = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			List<FinancePaymentInfo> list = financePaymentInfoService.findByCompany(load, startTime, endTime, appId,
					UserUtils.getUser().getOffice());
			for (FinancePaymentInfo financePaymentInfo : list) {
				JSONObject json = new JSONObject();
				json.put("financePayInfoId", financePaymentInfo.getId());
				json.put("remark", financePaymentInfo.getRemark());
				json.put("companyName", financePaymentInfo.getCompany());
				json.put("paymentMoney", financePaymentInfo.getPaymentMoney());
				json.put("officeName", financePaymentInfo.getCreateBy().getOffice().getName());
				json.put("ceartName", financePaymentInfo.getCreateBy().getName());
				json.put("createDate", sdf.format(financePaymentInfo.getCreateDate()));
				if (financePaymentInfo.getPaymentMethod() == 1) {
					json.put("paymentMethod", "现金");
				} else if (financePaymentInfo.getPaymentMethod() == 2) {
					json.put("paymentMethod", "POS收款");
				} else if (financePaymentInfo.getPaymentMethod() == 3) {
					json.put("paymentMethod", "银行转帐");
				} else if (financePaymentInfo.getPaymentMethod() == 4) {
					json.put("paymentMethod", "支付宝转账");
				}
				json.put("remarks", financePaymentInfo.getRemarks() == null ? "" : financePaymentInfo.getRemarks());
				json.put("residueMoney", financePaymentInfo.getResidueMoney());
				jsonArray.put(json);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return jsonArray.toString();
	}

	@RequestMapping(value = "paymentList")
	@ResponseBody
	public String paymentList(String payInfoIds) throws Exception {
		String[] ids = payInfoIds.split(",");
		List<Long> idsL = new ArrayList<Long>();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] != null && !ids[i].equals("")) {
				idsL.add(Long.parseLong(ids[i]));
			}
		}

		JSONArray jsonArray = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			List<FinancePaymentInfo> list = financePaymentInfoService.findByIds(idsL);
			for (FinancePaymentInfo financePaymentInfo : list) {
				JSONObject json = new JSONObject();
				json.put("financePayInfoId", financePaymentInfo.getId());
				json.put("remark", financePaymentInfo.getRemark());
				json.put("companyName", financePaymentInfo.getCompany());
				json.put("paymentMoney", financePaymentInfo.getPaymentMoney());
				json.put("officeName", financePaymentInfo.getCreateBy().getOffice().getName());
				json.put("ceartName", financePaymentInfo.getCreateBy().getName());
				json.put("createDate", sdf.format(financePaymentInfo.getCreateDate()));
				if (financePaymentInfo.getPaymentMethod() == 1) {
					json.put("paymentMethod", "现金");
				} else if (financePaymentInfo.getPaymentMethod() == 2) {
					json.put("paymentMethod", "POS收款");
				} else if (financePaymentInfo.getPaymentMethod() == 3) {
					json.put("paymentMethod", "银行转帐");
				} else if (financePaymentInfo.getPaymentMethod() == 4) {
					json.put("paymentMethod", "支付宝转账");
				}
				json.put("remarks", financePaymentInfo.getRemarks() == null ? "" : financePaymentInfo.getRemarks());
				json.put("residueMoney", financePaymentInfo.getResidueMoney());
				jsonArray.put(json);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return jsonArray.toString();
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "quickCert")
	@ResponseBody
	public String quickCert(String name, String email, String keySn, String reqBuf) {
		return null;
	}

	/**
	 * 已有证书明细
	 * 
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "showCert")
	public String showCert(Long id, Model model, HttpServletRequest request, HttpServletResponse response) {
		WorkCompany workCompany = workCompanyService.get(id);
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		Page<WorkDealInfo> page = workDealInfoService.find1(new Page<WorkDealInfo>(request, response), workDealInfo);
		model.addAttribute("page", page);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		return "modules/work/workDealInfoShow";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "showCertEnterprise")
	public String showCertEnterprise(Model model, HttpServletRequest request,
			@RequestParam(value = "companyIds", required = false) List<Long> companyIds, HttpServletResponse response,
			@RequestParam(value = "workdealinfoIds", required = false) String workdealinfoIds,
			String productId) {
		
		List<Long> workdealinfos = new ArrayList<Long>();
		String [] workdealinfoss = workdealinfoIds.split(",");
		for (String string : workdealinfoss) {
			workdealinfos.add(Long.parseLong(string));
		}

		Page<WorkDealInfo> page = workDealInfoService.findEnterprise2(new Page<WorkDealInfo>(request, response),workdealinfos);
		model.addAttribute("page", page);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("companyId", companyIds);
		model.addAttribute("productId", productId);
		return "modules/work/workDealInfoShow";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "showCertPersonal")
	public String showCertPersonal(Model model, HttpServletRequest request,
			@RequestParam(value = "companyIds", required = false) List<Long> companyIds, HttpServletResponse response,
			@RequestParam(value = "workdealinfoIds", required = false) String workdealinfoIds,
			String productId) {
		List<Long> workdealinfos = new ArrayList<Long>();
		String [] workdealinfoss = workdealinfoIds.split(",");
		for (String string : workdealinfoss) {
			workdealinfos.add(Long.parseLong(string));
		}
		List<WorkDealInfo> list = workDealInfoService.findPersonal1(workdealinfos);
		model.addAttribute("list", list);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("companyId", companyIds);
		model.addAttribute("productId", productId);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		return "modules/work/workDealInfoShowPersonal";
	}

	@RequestMapping(value = "ajaxEnterpriseCount")
	@ResponseBody
	public String ajaxEnterpriseCount(Model model, HttpServletRequest request,
			@RequestParam(value = "companyIds", required = false) List<Long> companyIds, HttpServletResponse response,
			String productId) {

		JSONObject json = new JSONObject();
		try {
//			Page<WorkDealInfo> page = workDealInfoService.findEnterprise(new Page<WorkDealInfo>(request, response),
//					companyIds, productId);
			Page<WorkDealInfo> list = workDealInfoService.findEnterprise(new Page<WorkDealInfo>(request, response),
					companyIds, productId);
			
			List<Long> workdealinfoIds = new ArrayList<Long>();
//			List<Long> workdealinfos = new ArrayList<Long>();
//			List<Long> previds = new ArrayList<Long>();
			for (WorkDealInfo workDealInfo : list.getList()) {
				WorkDealInfo workDealInfo1 = workDealInfoService.findNextDealInfo(workDealInfo.getId());
				if(workDealInfo1==null)
				{
					workdealinfoIds.add(workDealInfo.getId());
				}
				
//				if(workDealInfo.getPrevId()!=null)
//				{
//					previds.add(workDealInfo.getPrevId());
//				}
			}
//			int size=workdealinfoIds.size();
//			workdealinfos = workdealinfoIds;
//			for(int j=0;j<size;j++)
//			{
//				for(int i = 0 ;i<previds.size();i++)
//				{
//					if(!previds.isEmpty())
//					{
//						if(workdealinfos.get(j).equals(previds.get(i)))
//						{
//							workdealinfos.remove(j);
//						}
//					}
//				}
//			}
//			
			if(workdealinfoIds.size()>0&&!workdealinfoIds.isEmpty())
			{
				StringBuffer workdealinfoss = new StringBuffer();
				for (Long long1 : workdealinfoIds) {
					workdealinfoss.append(long1+",");
				}
				
				int end =  workdealinfoss.length()-1;
				String wi=new String();
				wi=workdealinfoss.substring(0, end);
				json.put("index", workdealinfoIds.size());
				json.put("workdealinfoss", wi);
			}else
			{
				json.put("index", 0);
				json.put("workdealinfoss", "");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	@RequestMapping(value = "ajaxPersonalCount")
	@ResponseBody
	public String ajaxPersonalCount(Model model, HttpServletRequest request,
			@RequestParam(value = "companyIds", required = false) List<Long> companyIds, HttpServletResponse response,
			String productId) {

		JSONObject json = new JSONObject();
		try {
			List<WorkDealInfo> list = workDealInfoService.findPersonal(companyIds, productId);
			List<Long> workdealinfoIds = new ArrayList<Long>();
			List<Long> previds = new ArrayList<Long>();
			for (WorkDealInfo workDealInfo : list) {
				
				workdealinfoIds.add(workDealInfo.getId());
				
				if(workDealInfo.getPrevId()!=null)
				{
					previds.add(workDealInfo.getPrevId());
				}
			}
			for(int j=0;j<workdealinfoIds.size();j++)
			{
				for(int i = 0 ;i<previds.size();i++)
				{
					if(!previds.isEmpty())
					{
						if(workdealinfoIds.get(j).equals(previds.get(i)))
						{
							workdealinfoIds.remove(j);
						}
					}
				}
			}
			
			if(workdealinfoIds.size()>0&&!workdealinfoIds.isEmpty())
			{
				StringBuffer workdealinfoss = new StringBuffer();
				for (Long long1 : workdealinfoIds) {
					workdealinfoss.append(long1+",");
				}
				
				int end =  workdealinfoss.length()-1;
				String wi=new String();
				wi=workdealinfoss.substring(0, end);
				json.put("index", workdealinfoIds.size());
				json.put("workdealinfoss", wi);
			}else
			{
				json.put("index", 0);
				json.put("workdealinfoss", "");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "businessQuery")
	public String businessQuery(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "certType", required = false) String certType,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "payMethod", required = false) String payMethod,
			@RequestParam(value = "luruStartTime", required = false) Date luruStartTime,
			@RequestParam(value = "luruEndTime", required = false) Date luruEndTime,
			@RequestParam(value = "daoqiStartTime", required = false) Date daoqiStartTime,
			@RequestParam(value = "daoqiEndTime", required = false) Date daoqiEndTime,
			@RequestParam(value = "jianzhengStartTime", required = false) Date jianzhengStartTime,
			@RequestParam(value = "jianzhengEndTime", required = false) Date jianzhengEndTime,
			@RequestParam(value = "zhizhengStartTime", required = false) Date zhizhengStartTime,
			@RequestParam(value = "zhizhengEndTime", required = false) Date zhizhengEndTime, Model model) {

		// 获取前台的付款方式
		List<Long> method = Lists.newArrayList();
		if (payMethod != null) {
			WorkPayInfo workPayInfo = new WorkPayInfo();
			if (payMethod.equals("1")) {
				workPayInfo.setMethodMoney(true);
				// continue;
			}
			if (payMethod.equals("2")) {
				workPayInfo.setMethodPos(true);
				// continue;
			}
			if (payMethod.equals("3")) {
				workPayInfo.setMethodBank(true);
				// continue;
			}
			if (payMethod.equals("4")) {
				workPayInfo.setMethodAlipay(true);
				// continue;
			}
			if (payMethod.equals("5")) {
				workPayInfo.setMethodGov(true);
				// continue;
			}
			if (payMethod.equals("6")) {
				workPayInfo.setMethodContract(true);
				// continue;
			}
			// }
			workDealInfo.setWorkPayInfo(workPayInfo);
		}
		List<Long> dealInfoByOfficeAreaIds = Lists.newArrayList();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);
			List<Long> appids = Lists.newArrayList();
			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService.findAllByOfficeId(officeId);// 通过网店获取引用的id
			if (appOffices.size() > 0) {
				for (int i = 0; i < appOffices.size(); i++) {
					appids.add(appOffices.get(i).getConfigApp().getId());
				}
			} else {
				appids.add(-1l);
			}
			List<WorkDealInfo> deals = workDealInfoService.findByAppId(appids);// 根据应用id获取dealInfo信息
			if (deals.size() < 1) {
				dealInfoByOfficeAreaIds.add(-1l);
			} else {
				for (int i = 0; i < deals.size(); i++) {
					dealInfoByOfficeAreaIds.add(deals.get(i).getId());
				}
			}
		} else {
			if (area != null) {
				List<Long> appids = Lists.newArrayList();
				// List<Long> officeids = Lists.newArrayList();
				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}

				List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService.findAllByOfficeId(officeids);// 根据网店id获取应用id
				if (appOffices.size() > 0) {
					for (int i = 0; i < appOffices.size(); i++) {
						appids.add(appOffices.get(i).getConfigApp().getId());
					}
				} else {
					appids.add(-1l);
				}

				List<WorkDealInfo> deals = workDealInfoService.findByAppId(appids);// 根据应用id获取dealInfo信息
				if (deals.size() < 1) {
					dealInfoByAreaIds.add(-1l);
				} else {
					for (int i = 0; i < deals.size(); i++) {
						dealInfoByAreaIds.add(deals.get(i).getId());
					}
				}
			}
		}
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);

		List<Long> officeIds = new ArrayList<Long>();
		for (int i = 0; i < officeList.size(); i++) {
			officeIds.add(officeList.get(i).getId());
		}

		Calendar calendar = Calendar.getInstance();
		/*
		 * if (endTime != null) { calendar.setTime(endTime);
		 * calendar.add(Calendar.DATE, 1); }
		 */
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
		if (zhizhengStartTime != null && zhizhengEndTime != null) {
			certInfoList = workCertInfoService.findZhiZhengTime(zhizhengStartTime, zhizhengEndTime);
		}
		//单独处理吊销
		if(workType!=null&&workType==5)
		{
			Page<WorkDealInfo> page = workDealInfoService.findCX(new Page<WorkDealInfo>(request, response), workDealInfo,
					dealInfoByOfficeAreaIds, dealInfoByAreaIds, officeids, apply, certType, workType, year, luruStartTime,
					luruEndTime, officeIds, daoqiStartTime, daoqiEndTime, jianzhengStartTime, jianzhengEndTime,
					certInfoList);
			model.addAttribute("proType", ProductType.productTypeStrMap);
			model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
			model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
			model.addAttribute("certTypes", productType.getProductTypeList());
			model.addAttribute("workTypes", workDealInfoType.getProductTypeList());
	
			model.addAttribute("page", page);
	
			List<Office> offsList = officeService.getOfficeByType(UserUtils.getUser(), 1);
			for (int i = 0; i < offsList.size();) {
				Office office = offsList.get(i);
				if (office.getType().equals("2")) {
					offsList.remove(i);
				} else {
					i++;
				}
			}
			/*
			 * Office scca = officeService.get(1L); offsList.add(scca);
			 */
			if (area != null) {
				List<Office> offices = officeService.findByParentId(area);
				model.addAttribute("offices", offices);
			}
	
			model.addAttribute("offsList", offsList);
	
			List<ConfigApp> configAppList = configAppService.selectAll();
			model.addAttribute("configAppList", configAppList);
			model.addAttribute("apply", apply);
			model.addAttribute("certType", certType);
			model.addAttribute("workType", workType);
			model.addAttribute("year", year);
			model.addAttribute("payMethod", payMethod);
			model.addAttribute("luruStartTime", luruStartTime);
			model.addAttribute("luruEndTime", luruEndTime);
	
			model.addAttribute("daoqiStartTime", daoqiStartTime);
			model.addAttribute("daoqiEndTime", daoqiEndTime);
	
			model.addAttribute("jianzhengStartTime", jianzhengStartTime);
			model.addAttribute("jianzhengEndTime", jianzhengEndTime);
	
			model.addAttribute("zhizhengStartTime", zhizhengStartTime);
			model.addAttribute("zhizhengEndTime", zhizhengEndTime);
	
			model.addAttribute("officeId", officeId);
			model.addAttribute("area", area);
			return "modules/work/workDealInfoBusinessQueryList";
		}
		Page<WorkDealInfo> page = workDealInfoService.find(new Page<WorkDealInfo>(request, response), workDealInfo,
				dealInfoByOfficeAreaIds, dealInfoByAreaIds, officeids, apply, certType, workType, year, luruStartTime,
				luruEndTime, officeIds, daoqiStartTime, daoqiEndTime, jianzhengStartTime, jianzhengEndTime,
				certInfoList);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("certTypes", productType.getProductTypeList());
		model.addAttribute("workTypes", workDealInfoType.getProductTypeList());

		model.addAttribute("page", page);

		List<Office> offsList = officeService.getOfficeByType(UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			} else {
				i++;
			}
		}
		/*
		 * Office scca = officeService.get(1L); offsList.add(scca);
		 */
		if (area != null) {
			List<Office> offices = officeService.findByParentId(area);
			model.addAttribute("offices", offices);
		}

		model.addAttribute("offsList", offsList);

		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("apply", apply);
		model.addAttribute("certType", certType);
		model.addAttribute("workType", workType);
		model.addAttribute("year", year);
		model.addAttribute("payMethod", payMethod);
		model.addAttribute("luruStartTime", luruStartTime);
		model.addAttribute("luruEndTime", luruEndTime);

		model.addAttribute("daoqiStartTime", daoqiStartTime);
		model.addAttribute("daoqiEndTime", daoqiEndTime);

		model.addAttribute("jianzhengStartTime", jianzhengStartTime);
		model.addAttribute("jianzhengEndTime", jianzhengEndTime);

		model.addAttribute("zhizhengStartTime", zhizhengStartTime);
		model.addAttribute("zhizhengEndTime", zhizhengEndTime);

		model.addAttribute("officeId", officeId);
		model.addAttribute("area", area);
		return "modules/work/workDealInfoBusinessQueryList";
		
	}

	@RequestMapping(value = "showWorkDeal")
	public String showWorkDeal(Long dealInfoId, Model model, HttpServletRequest request, HttpServletResponse response) {

		List<WorkDealInfo> page = workDealInfoService.findByDealInfoId(dealInfoId);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("businessType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("dealInfo", page.get(0));
		return "modules/statistic/workCertInfoShow";
	}

	static Long MILL = 86400000L;

	private int getLastCertDay(Date notAfter) {
		Long notAfterLong = notAfter.getTime();
		Long nowLong = System.currentTimeMillis();
		if (notAfterLong < nowLong) {
			return 0;
		}

		long d = (notAfterLong - nowLong) / MILL;

		long hour1 = (notAfterLong - nowLong) % MILL;
		if (hour1 > 0) {
			d += 1;
		}
		return (int) d;
	}

	@RequestMapping(value = "initKey")
	public String initKey(String csp, Model model, String keySn, HttpServletRequest request)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, ClientProtocolException,
			IOException {
		csp = URLDecoder.decode(csp, "UTF-8");
		KeyGeneralInfo info = generalInfoService.findInfoByName(csp);
		if (info != null) {
			model.addAttribute("userPin", info.getDefaultUserPin());
			String adminPin = info.getDefaultSoPin();
			if (adminPin == null || adminPin.equals("")) {
				// keyunlocks/key/{sn}
				String url = ixinUrl + "/rest/key/" + keySn + "/adminpin/" + info.getDefaultSoPinType();
				HttpClient client = new DefaultHttpClient();
				String respCode = ItrustProxyUtil.sendPost(client, url, null, request, key);
				try {
					JSONObject json = new JSONObject(respCode);
					if (json.getInt("status") == 0) {
						adminPin = json.getString("adminPin");
					} else {
						adminPin = "";
						System.out.println("msg:" + json.getString("message"));
					}
				} catch (JSONException e) {
					adminPin = "";
				}
			} // cMXu5u1wKtBO8j+UW881hA==
			if (adminPin != null && !adminPin.equals("")) {
				String unlockCipher = "AES";
				SecretKeySpec skeySpec = new SecretKeySpec(AdminPinEncKey.adminPinEncKey.substring(0, 16).getBytes(),
						unlockCipher);
				IvParameterSpec ivSpec = new IvParameterSpec(
						AdminPinEncKey.adminPinEncKey.substring(16, 32).getBytes());
				Cipher cipher = Cipher.getInstance(unlockCipher + "/CBC/PKCS5Padding");

				cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec, null);
				byte[] decadminpin = cipher.doFinal(com.itrus.util.Base64.decode(adminPin.getBytes()));
				adminPin = new String(decadminpin);
			} else {
				adminPin = "";
			}
			model.addAttribute("adminPin", adminPin);
			model.addAttribute("keySn", keySn);
		}
		return "modules/work/initKey";
	}

	/**
	 * 
	 * 
	 * date:2014年8月12日 user:ZhangJingtao void
	 */
	public void test() {

	}

	@RequestMapping(value = "typeShow")
	public String typeShow(String infoId, Model model) {
		model.addAttribute("id", infoId);
		return "modules/work/workDealInfoType";
	}

	@RequestMapping(value = "typeForm")
	public String typeForm(String dealType, WorkDealInfo workDealInfo, Model model, String reissueType,
			RedirectAttributes redirectAttributes) {
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		workDealInfo.setIsMainTain("mainTain");
		workDealInfoService.save(workDealInfo);
		String[] type = dealType.replace(",", " ").split(" ");
		for (int i = 0; i < type.length; i++) {
			if (type[i].equals("1")) {
				model.addAttribute("change", "1");
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
					addMessage(redirectAttributes, "请到业务办理网点变更！");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("2")) {
				if (reissueType.equals("1")) {
					model.addAttribute("reissue", "1");
				} else {
					model.addAttribute("reissue", "2");
				}
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
					addMessage(redirectAttributes, "请到业务办理网点补办！");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("3")) {
				model.addAttribute("update", "3");

				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				String[] years = configChargeAgentDetailService.getChargeAgentYears(configProduct.getChargeAgentId(),
						WorkDealInfoType.TYPE_UPDATE_CERT);
				for (int j = 0; j < years.length; j++) {
					switch (years[j]) {
					case "1":
						model.addAttribute("year1", true);
						break;
					case "2":
						model.addAttribute("year2", true);
						break;
					case "4":
						model.addAttribute("year4", true);
						break;
					case "5":
						model.addAttribute("year5", true);
						break;
					}
				}

				/*if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
					addMessage(redirectAttributes, "请到业务办理网点更新！");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
				}*/
			} else if (type[i].equals("4")) {
				model.addAttribute("revoke", "4");
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
					addMessage(redirectAttributes, "请到业务办理网点吊销！");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
				}
			}
		}
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo.getWorkCertInfo().getWorkCertApplyInfo());
		}
		model.addAttribute("pro", ProductType.productTypeStrMap);
		
		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));
		
		ConfigChargeAgent chargeAgent = null;
		if (workDealInfo.getConfigChargeAgentId() != null) {
			chargeAgent = chargeAgentService.get(workDealInfo.getConfigChargeAgentId());

		}
		// model.addAttribute("tempStyle", chargeAgent.getTempStyle());

		
		if(workDealInfo.getPayType()==null){
			workDealInfo.setPayType(Integer.parseInt(chargeAgent.getTempStyle()));
		}
		
		model.addAttribute("workDealInfo", workDealInfo);
		
		
		//获得应用下的产品
		List<ConfigProduct> products = configProductService.findByAppAndProName(workDealInfo.getConfigApp().getId(), workDealInfo.getConfigProduct().getProductName());
		List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
		for (int i = 0; i < products.size(); i++) {
			String ssssi = ProductType.productTypeStrMap.get(products.get(i).getProductName())+"["+(products.get(i).getProductLabel()==0?"通用":"专用")+"]";
			ProductTypeObj obj = new ProductTypeObj(products.get(i).getId().intValue(), ssssi);
			listProductTypeObjs.add(obj);
		}
		model.addAttribute("proList", listProductTypeObjs);
		
		if (dealType.indexOf("1") >= 0) {

			model.addAttribute("isOK", "isNo");

		} else {

			if (dealType.indexOf("3") >= 0) {

				model.addAttribute("isOK", "isYes");
			} else {

				model.addAttribute("isOK", "isNo");

			}
		}

		model.addAttribute("dealType", dealType);

		ArrayList<String> dealInfoTypes = new ArrayList<String>();
		for (int i = 0; i < type.length; i++) {
			if (type[i] != null && !type[i].equals("")) {
				dealInfoTypes.add(type[i]);
			}
		}

		if (workDealInfo.getSelfApplyId() != null) {
			model.addAttribute("imgUrl",Global.getConfig("images.path"));
			SelfImage selfImage =  selfImageService.findByApplicationId(workDealInfo.getSelfApplyId());
			workDealInfo.setSelfImage(selfImage);
		}
		if (workDealInfo.getSelfImage()!=null) {
			model.addAttribute("imgUrl",Global.getConfig("images.path"));
		}
		
		
		if (dealInfoTypes.size() == 1) {
			if (dealInfoTypes.get(0).equals("1")) {

				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
				}

				
				model.addAttribute("boundLabelList", nameSet);

				
				
				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				return "modules/work/maintain/workDealInfoMaintainChange";
			} else if (dealInfoTypes.get(0).equals("2")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainLost";
			} else if (dealInfoTypes.get(0).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdate";
			} else if (dealInfoTypes.get(0).equals("4")) {
				ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("jfMB", agent.getTempName());
				return "modules/work/maintain/workDealInfoMaintainRevoke";
			}

		} else if (dealInfoTypes.size() == 2) {
			if (dealInfoTypes.get(0).equals("1") && dealInfoTypes.get(1).equals("2")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());

				return "modules/work/maintain/workDealInfoMaintainChange";
			} else if (dealInfoTypes.get(0).equals("2") && dealInfoTypes.get(1).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
				}
				model.addAttribute("boundLabelList", nameSet);
				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdate";
			} else if (dealInfoTypes.get(0).equals("1") && dealInfoTypes.get(1).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdateChange";
			}
		} else if (dealInfoTypes.size() == 3) {

			ConfigProduct configProduct = workDealInfo.getConfigProduct();
			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());
			Set<Integer> nameSet = new HashSet<Integer>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
			}

			model.addAttribute("boundLabelList", nameSet);

			List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
			model.addAttribute("workLog", list);

			if (chargeAgent != null) {
				model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			}

			// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			return "modules/work/maintain/workDealInfoMaintainUpdateChange";
		} else {
			return "modules/work/workDealInfoMaintain";
		}

		return "modules/work/workDealInfoMaintain";
	}

	@RequestMapping(value = "exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
		try {
			workDealInfoService.exportExcelData(request, response);
		} catch (Exception ex) {
		}
	}

	@RequestMapping(value = "exportDealPayList")
	public void exportDealPayList(WorkDealInfo workDealInfo, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "payMethod", required = false) String payMethod,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "appId", required = false) Long appId) throws IOException {
		User user = UserUtils.getUser();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("业务款项统计");
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		// 获取前台的付款方式
		// List<Long> method = Lists.newArrayList();
		if (payMethod != null) {
			// String[] methods = payMethod.split(",");
			// method = Lists.newArrayList();
			// for (int i = 0; i < methods.length; i++) {
			// method.add(Long.parseLong(methods[i]));
			// }

			WorkPayInfo workPayInfo = new WorkPayInfo();
			// for (int i = 0; i < methods.length; i++) {
			if (payMethod.equals("1")) {
				workPayInfo.setMethodMoney(true);
				// continue;
			}
			if (payMethod.equals("2")) {
				workPayInfo.setMethodPos(true);
				// continue;
			}
			if (payMethod.equals("3")) {
				workPayInfo.setMethodBank(true);
				// continue;
			}
			if (payMethod.equals("4")) {
				workPayInfo.setMethodAlipay(true);
				// continue;
			}
			if (payMethod.equals("5")) {
				workPayInfo.setMethodGov(true);
				// continue;
			}
			if (payMethod.equals("6")) {
				workPayInfo.setMethodContract(true);
				// continue;
			}
			// }
			workDealInfo.setWorkPayInfo(workPayInfo);
		}

		List<Long> dealInfoByOfficeAreaIds = Lists.newArrayList();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		Map<Long, Set<String>> Id_paymethod = new HashMap<Long, Set<String>>();
		int index = 0;
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);
			List<Long> appids = Lists.newArrayList();
			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService.findAllByOfficeId(officeId);// 通过网店获取引用的id
			if (appOffices.size() > 0) {
				for (int i = 0; i < appOffices.size(); i++) {
					appids.add(appOffices.get(i).getConfigApp().getId());
				}
			} else {
				appids.add(-1l);
			}
			List<WorkDealInfo> deals = workDealInfoService.findByAppId(appids);// 根据应用id获取dealInfo信息
			if (deals.size() < 1) {
				dealInfoByOfficeAreaIds.add(-1l);
			} else {
				for (int i = 0; i < deals.size(); i++) {
					dealInfoByOfficeAreaIds.add(deals.get(i).getId());
				}
			}
		} else {
			if (area != null) {
				List<Long> appids = Lists.newArrayList();
				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}

				List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService.findAllByOfficeId(officeids);// 根据网店id获取应用id
				if (appOffices.size() > 0) {
					for (int i = 0; i < appOffices.size(); i++) {
						appids.add(appOffices.get(i).getConfigApp().getId());
					}
				} else {
					appids.add(-1l);
				}

				List<WorkDealInfo> deals = workDealInfoService.findByAppId(appids);// 根据应用id获取dealInfo信息
				if (deals.size() < 1) {
					dealInfoByAreaIds.add(-1l);
				} else {
					for (int i = 0; i < deals.size(); i++) {
						dealInfoByAreaIds.add(deals.get(i).getId());
					}
				}
			}
		}
		List<Long> idList = Lists.newArrayList();// 根据产品名称查询出支付信息
		// if (appId != null) {
		// List<WorkDealInfo> dealInfos = workDealInfoService
		// .findByAppId(appId);
		//
		// if (dealInfos != null && dealInfos.size() > 0) {
		// for (int i = 0; i < dealInfos.size(); i++) {
		// idList.add(dealInfos.get(i).getId());
		// }
		// }
		// if (dealInfos == null || dealInfos.size() < 1) {
		// idList.add(-1l);
		// }
		// }
		List<WorkDealInfo> list = workDealInfoService.findByDealPay(workDealInfo, startTime, endTime, idList,
				dealInfoByAreaIds, dealInfoByOfficeAreaIds, appId, officeids);
		// 除去合同采购和统一采购的信息
		// for (int i = 0; i < page.getList().size(); i++) {
		// if (page.getList().get(i).getWorkPayInfo().getMethodGov() != null) {
		// if (page.getList().get(i).getWorkPayInfo().getMethodGov() == true) {
		// page.getList().remove(i);
		// continue;
		// }
		// }
		// if (page.getList().get(i).getWorkPayInfo().getMethodContract() !=
		// null) {
		// if (page.getList().get(i).getWorkPayInfo().getMethodContract() ==
		// true) {
		// page.getList().remove(i);
		// continue;
		// }
		// }
		//
		// }

		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getWorkPayInfo().getRelationMethod() != null) {
					List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
							.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
					Set<String> payMethods = new LinkedHashSet<String>();
					for (int w = 0; w < workfinancePayinfos.size(); w++) {
						if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
							payMethods.add("现金付款  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
							payMethods.add("POS付款  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
							payMethods.add("银行转账  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
							payMethods.add("支付宝付款  ");
						}
					}
					Id_paymethod.put(list.get(i).getId(), payMethods);
				}
			}
		}

		if (area != null && officeId != null) {
			WorkDealCountStatistics dealCountSta = new WorkDealCountStatistics();
			Office areaOffice = officeService.get(area);
			dealCountSta.setAreaName(areaOffice.getName());
			Office offOffice = officeService.get(officeId);
			dealCountSta.setOfficeName(offOffice.getName());
			int posCount = 0;
			int moneyCount = 0;
			int bankCount = 0;
			int alipayCount = 0;
			int govCount = 0;
			int contractCount = 0;
			double posCountMoney = 0L;
			double moneyCountMoney = 0L;
			double bankCountMoney = 0L;
			double alipayCountMoney = 0L;
			index = 5;
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
						if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
							posCount += 1;
							if (list.get(i).getWorkPayInfo().getPosMoney() != null) {
								posCountMoney += list.get(i).getWorkPayInfo().getPosMoney();
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
							bankCount += 1;
							if (list.get(i).getWorkPayInfo().getBankMoney() != null) {
								bankCountMoney += list.get(i).getWorkPayInfo().getBankMoney();
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
							moneyCount += 1;
							if (list.get(i).getWorkPayInfo().getMoney() != null) {
								moneyCountMoney += list.get(i).getWorkPayInfo().getMoney();
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
							alipayCount += 1;
							if (list.get(i).getWorkPayInfo().getAlipayMoney() != null) {
								alipayCountMoney += list.get(i).getWorkPayInfo().getAlipayMoney();
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodGov() != null) {
							if (list.get(i).getWorkPayInfo().getMethodGov() == true) {
								govCount += 1;
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodContract() != null) {
							if (list.get(i).getWorkPayInfo().getMethodContract() == true) {
								contractCount += 1;
							}
						}
					} else {
						List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
								.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
						Set<String> payMethods = new LinkedHashSet<String>();
						for (int w = 0; w < workfinancePayinfos.size(); w++) {
							if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
								payMethods.add("现金付款  ");
								// moneyCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									moneyCountMoney += workfinancePayinfos.get(w).getMoney();
								}
							}
							if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
								payMethods.add("POS付款  ");
								// posCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									posCountMoney += workfinancePayinfos.get(w).getMoney();
								}
							}
							if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
								payMethods.add("银行转账  ");
								// bankCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									bankCountMoney += workfinancePayinfos.get(w).getMoney();
								}
							}
							if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
								payMethods.add("支付宝付款  ");
								// alipayCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									alipayCountMoney += workfinancePayinfos.get(w).getMoney();
								}
							}
						}
						Object payMehtod[] = payMethods.toArray();
						for (Object object : payMehtod) {
							if (object.equals("现金付款")) {
								moneyCount += 1;
							}
							if (object.equals("POS付款")) {
								posCount += 1;
							}
							if (object.equals("银行转账")) {
								bankCount += 1;
							}
							if (object.equals("支付宝付款")) {
								alipayCount += 1;
							}
						}
						Id_paymethod.put(list.get(i).getId(), payMethods);
					}
				}
			}
			dealCountSta.setAlipayCount(alipayCount);
			dealCountSta.setMoneyCount(moneyCount);
			dealCountSta.setPosCount(posCount);
			dealCountSta.setBankCount(bankCount);
			dealCountSta.setGovCount(govCount);
			dealCountSta.setContractCount(contractCount);

			// model.addAttribute("dealCountSta", dealCountSta);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dealMsg = (startTime != null && endTime != null ? sdf.format(startTime) + "至" + sdf.format(endTime)
					: "本次统计") + areaOffice.getName() + "区域" + offOffice.getName() + "网点" + moneyCountMoney + "元，POS收款"
					+ posCountMoney + "元，银行转账" + bankCountMoney + "元，支付宝转账" + alipayCountMoney + "元";
			// model.addAttribute("dealMsg", dealMsg);

			HSSFRow rowi1 = sheet.createRow(0);
			rowi1.createCell(0).setCellValue("区域名称");
			rowi1.createCell(1).setCellValue("网点名称");
			rowi1.createCell(2).setCellValue("现金支付数量");
			rowi1.createCell(3).setCellValue("pos付款数量");
			rowi1.createCell(4).setCellValue("银行付款数量");
			rowi1.createCell(5).setCellValue("支付宝付款数量");
			rowi1.createCell(6).setCellValue("政府统一采购数量");
			rowi1.createCell(7).setCellValue("合同采购付款数量");
			HSSFRow rowi2 = sheet.createRow(1);
			rowi2.createCell(0).setCellValue(dealCountSta.getAreaName());
			rowi2.createCell(1).setCellValue(dealCountSta.getOfficeName());
			rowi2.createCell(2).setCellValue(dealCountSta.getMoneyCount());
			rowi2.createCell(3).setCellValue(dealCountSta.getPosCount());
			rowi2.createCell(4).setCellValue(dealCountSta.getBankCount());
			rowi2.createCell(5).setCellValue(dealCountSta.getAlipayCount());
			rowi2.createCell(6).setCellValue(dealCountSta.getGovCount());
			rowi2.createCell(7).setCellValue(dealCountSta.getContractCount());
			sheet.addMergedRegion(new Region(2, (short) 0, 2, (short) 7));
			HSSFRow rowi3 = sheet.createRow(2);
			rowi3.createCell(0).setCellValue(dealMsg);
		}

		sheet.addMergedRegion(new Region(index, (short) 0, index, (short) 9));
		HSSFRow row0 = sheet.createRow(index);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue("业务款项统计");
		cell0.setCellStyle(style);
		HSSFRow row1 = sheet.createRow(1 + index);
		row1.createCell(0).setCellValue("付款单位名称");
		row1.createCell(1).setCellValue("付款金额/元");
		row1.createCell(2).setCellValue("应用名称");
		row1.createCell(3).setCellValue("联系人");
		row1.createCell(4).setCellValue("联系方式");
		row1.createCell(5).setCellValue("付款时间");
		row1.createCell(6).setCellValue("付款方式");
		row1.createCell(7).setCellValue("区域");
		row1.createCell(8).setCellValue("网点");
		row1.createCell(9).setCellValue("记录人员");
		for (int i = 0; i < list.size(); i++) {
			HSSFRow rown = sheet.createRow(2 + i + index);
			rown.createCell(0).setCellValue(list.get(i).getWorkCompany().getCompanyName());
			rown.createCell(1).setCellValue(list.get(i).getWorkPayInfo().getWorkPayedMoney());
			rown.createCell(2).setCellValue(list.get(i).getConfigApp().getAppName());
			rown.createCell(3).setCellValue(list.get(i).getWorkUser().getContactName());
			rown.createCell(4).setCellValue(list.get(i).getWorkUser().getContactPhone());
			rown.createCell(5).setCellValue(list.get(i).getWorkPayInfo().getCreateDate() + "");
			if (list.get(i).getWorkPayInfo().getMethodMoney()) {
				rown.createCell(6).setCellValue("现金");
			}
			if (list.get(i).getWorkPayInfo().getMethodPos()) {
				rown.createCell(6).setCellValue("POS付款");
			}
			if (list.get(i).getWorkPayInfo().getMethodBank()) {
				rown.createCell(6).setCellValue("银行转账");
			}
			if (list.get(i).getWorkPayInfo().getMethodAlipay()) {
				rown.createCell(6).setCellValue("支付宝");
			}
			if (list.get(i).getWorkPayInfo().getMethodGov()) {
				rown.createCell(6).setCellValue("政府统一采购");
			}
			if (list.get(i).getWorkPayInfo().getMethodContract()) {
				rown.createCell(6).setCellValue("合同统一采购");
			}
			if (list.get(i).getWorkPayInfo().getRelationMethod() != null) {
				if (list.get(i).getWorkPayInfo().getRelationMethod() > 0) {
					Iterator<Map.Entry<Long, Set<String>>> it = Id_paymethod.entrySet().iterator();
					while (it.hasNext()) {
						Entry<Long, Set<String>> entry = it.next();
						if (entry.getKey().equals(list.get(i).getId())) {
							rown.createCell(6).setCellValue(entry.getValue().toString());
						}
					}
				}

			}
			// else
			// {
			// rown.createCell(6).setCellValue("其他付款方式");
			// }
			rown.createCell(7).setCellValue(list.get(i).getCreateBy().getOffice().getAreaName());
			rown.createCell(8).setCellValue(list.get(i).getCreateBy().getOffice().getName());
			rown.createCell(9).setCellValue(list.get(i).getWorkPayInfo().getCreateBy().getName());
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition", "attachment; filename=dealPaylist.xls");
		wb.write(baos);
		byte[] bytes = baos.toByteArray();
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(bytes);
		bos.close();
		baos.close();
		// List<Office> offsList = officeService.selectAreaList();
		//
		// if (area != null) {
		// model.addAttribute("areaId", area);
		// List<Office> offices = officeService.findByParentId(area);
		// model.addAttribute("offices", offices);
		// if (officeId != null) {
		// model.addAttribute("officeId", officeId);
		// }
		// }

		// List<Office> offsList = officeService.getOfficeByType(user, 1);
		//
		// List<Office> offices = officeService.getOfficeByType(user, 2);
		// if (offices.size() == 1) {
		// // model.addAttribute("offsListSize", 1);
		// // model.addAttribute("offices", offices);
		// }
		//
		// if (area != null) {
		// // model.addAttribute("areaId", area);
		//
		// List<Office> offs = officeService.findByParentId(area);
		// // model.addAttribute("offices", offs);
		// if (officeId != null) {
		// // model.addAttribute("office", officeId);
		// }
		// }
	}

	@RequestMapping(value = "exportZS")
	public void exportZS(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "apply", required = false) Long apply,
			@RequestParam(value = "certType", required = false) String certType,
			@RequestParam(value = "workType", required = false) Integer workType,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "organizationNumber", required = false) String organizationNumber,
			@RequestParam(value = "contactName", required = false) String contactName,
			@RequestParam(value = "conCertNumber", required = false) String conCertNumber,
			@RequestParam(value = "keySn", required = false) String keySn,
			@RequestParam(value = "createByname", required = false) String createByname,
			@RequestParam(value = "zhizhengname", required = false) String zhizhengname,
			@RequestParam(value = "updateByname", required = false) String updateByname,
			@RequestParam(value = "payType", required = false) Integer payType,
			@RequestParam(value = "s_province", required = false) String province,
			@RequestParam(value = "s_city", required = false) String city,
			@RequestParam(value = "s_county", required = false) String county,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "payMethod", required = false) String payMethod,
			@RequestParam(value = "luruStartTime", required = false) Date luruStartTime,
			@RequestParam(value = "luruEndTime", required = false) Date luruEndTime,
			@RequestParam(value = "daoqiStartTime", required = false) Date daoqiStartTime,
			@RequestParam(value = "daoqiEndTime", required = false) Date daoqiEndTime,
			@RequestParam(value = "jianzhengStartTime", required = false) Date jianzhengStartTime,
			@RequestParam(value = "jianzhengEndTime", required = false) Date jianzhengEndTime,
			@RequestParam(value = "zhizhengStartTime", required = false) Date zhizhengStartTime,
			@RequestParam(value = "zhizhengEndTime", required = false) Date zhizhengEndTime)
					throws UnsupportedEncodingException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WorkDealInfo workDealInfo = new WorkDealInfo();
		WorkCompany company = new WorkCompany();
		company.setOrganizationNumber(organizationNumber);
		company.setCompanyName(companyName);
		company.setCity(city);
		company.setDistrict(county);
		company.setProvince(province);
		WorkUser workUser = new WorkUser();
		workUser.setConCertNumber(conCertNumber);
		workUser.setContactName(contactName);
		User user = new User();
		user.setName(createByname);
		User user1 = new User();
		user1.setName(updateByname);
		User user2 = new User();
		user2.setName(zhizhengname);
		workDealInfo.setWorkCompany(company);
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setPayType(payType);
		workDealInfo.setKeySn(keySn);
		workDealInfo.setInputUser(user);
		workDealInfo.setAttestationUser(user1);
		workDealInfo.setBusinessCardUser(user2);
		if (payMethod != null) {
			WorkPayInfo workPayInfo = new WorkPayInfo();
			if (payMethod.equals("1")) {
				workPayInfo.setMethodMoney(true);
				// continue;
			}
			if (payMethod.equals("2")) {
				workPayInfo.setMethodPos(true);
				// continue;
			}
			if (payMethod.equals("3")) {
				workPayInfo.setMethodBank(true);
				// continue;
			}
			if (payMethod.equals("4")) {
				workPayInfo.setMethodAlipay(true);
				// continue;
			}
			if (payMethod.equals("5")) {
				workPayInfo.setMethodGov(true);
				// continue;
			}
			if (payMethod.equals("6")) {
				workPayInfo.setMethodContract(true);
				// continue;
			}
			// }
			workDealInfo.setWorkPayInfo(workPayInfo);
		}
		List<Long> dealInfoByOfficeAreaIds = Lists.newArrayList();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);
			List<Long> appids = Lists.newArrayList();
			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService.findAllByOfficeId(officeId);// 通过网店获取引用的id
			if (appOffices.size() > 0) {
				for (int i = 0; i < appOffices.size(); i++) {
					appids.add(appOffices.get(i).getConfigApp().getId());
				}
			} else {
				appids.add(-1l);
			}
			List<WorkDealInfo> deals = workDealInfoService.findByAppId(appids);// 根据应用id获取dealInfo信息
			if (deals.size() < 1) {
				dealInfoByOfficeAreaIds.add(-1l);
			} else {
				for (int i = 0; i < deals.size(); i++) {
					dealInfoByOfficeAreaIds.add(deals.get(i).getId());
				}
			}
		} else {
			if (area != null) {
				List<Long> appids = Lists.newArrayList();
				// List<Long> officeids = Lists.newArrayList();
				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}

				List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService.findAllByOfficeId(officeids);// 根据网店id获取应用id
				if (appOffices.size() > 0) {
					for (int i = 0; i < appOffices.size(); i++) {
						appids.add(appOffices.get(i).getConfigApp().getId());
					}
				} else {
					appids.add(-1l);
				}

				List<WorkDealInfo> deals = workDealInfoService.findByAppId(appids);// 根据应用id获取dealInfo信息
				if (deals.size() < 1) {
					dealInfoByAreaIds.add(-1l);
				} else {
					for (int i = 0; i < deals.size(); i++) {
						dealInfoByAreaIds.add(deals.get(i).getId());
					}
				}
			}
		}
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		WorkDealInfoStatus workDealInfoStatus = new WorkDealInfoStatus();
		List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);
		List<Long> officeIds = new ArrayList<Long>();
		for (int i = 0; i < officeList.size(); i++) {
			officeIds.add(officeList.get(i).getId());
		}
		// Calendar calendar = Calendar.getInstance();
		try {
			List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
			if (zhizhengStartTime != null && zhizhengEndTime != null) {
				certInfoList = workCertInfoService.findZhiZhengTime(zhizhengStartTime, zhizhengEndTime);
			}
			List<WorkDealInfo> list = new ArrayList<WorkDealInfo>();
			if(workType!=null&&workType==5)
			{
				list = workDealInfoService.findCX(workDealInfo,
						dealInfoByOfficeAreaIds, dealInfoByAreaIds, officeids, apply, certType, workType, year, luruStartTime,
						luruEndTime, officeIds, daoqiStartTime, daoqiEndTime, jianzhengStartTime, jianzhengEndTime,
						certInfoList);
			}
			list = workDealInfoService.find(workDealInfo, dealInfoByOfficeAreaIds,
					dealInfoByAreaIds, officeids, apply, certType, workType, year, luruStartTime, luruEndTime,
					officeIds, daoqiStartTime, daoqiEndTime, jianzhengStartTime, jianzhengEndTime, certInfoList);
			final String fileName = "WorkDealInfos.csv";
			final List<WorkDealInfoVo> workDealInfoVos = new ArrayList<WorkDealInfoVo>();
			String dealInfoType = null;
			String dealInfoType1 = null;
			String dealInfoType2 = null;
			String dealInfoType3 = null;
			for (final WorkDealInfo dealInfo : list) {

				final WorkDealInfoVo dealInfoVo = new WorkDealInfoVo();
				dealInfoVo.setSvn(dealInfo.getSvn());
				dealInfoVo.setAlias(dealInfo.getConfigApp().getAlias());
				dealInfoVo.setCompanyName(dealInfo.getWorkCompany().getCompanyName());
				dealInfoVo.setContactName(dealInfo.getWorkUser().getContactName());
				dealInfoVo.setCertApplyInfoName(dealInfo.getWorkCertInfo().getWorkCertApplyInfo().getName());
				dealInfoVo.setProductName(
						productType.getProductTypeName(Integer.parseInt(dealInfo.getConfigProduct().getProductName())));
				if (dealInfo.getDealInfoType() == null) {
					dealInfoType = "";
				} else {
					dealInfoType = workDealInfoType.getDealInfoTypeName(dealInfo.getDealInfoType());
				}
				if (dealInfo.getDealInfoType1() == null) {
					dealInfoType1 = "";
				} else {
					dealInfoType1 = workDealInfoType.getDealInfoTypeName(dealInfo.getDealInfoType1());
				}
				if (dealInfo.getDealInfoType2() == null) {
					dealInfoType2 = "";
				} else {
					dealInfoType2 = workDealInfoType.getDealInfoTypeName(dealInfo.getDealInfoType2());
				}
				if (dealInfo.getDealInfoType3() == null) {
					dealInfoType3 = "";
				} else {
					dealInfoType3 = workDealInfoType.getDealInfoTypeName(dealInfo.getDealInfoType3());
				}
				dealInfoVo.setDealInfoType(dealInfoType + "" + dealInfoType1 + "" + dealInfoType2 + "" + dealInfoType3);
				dealInfoVo.setKeySn(dealInfo.getKeySn());

				if (dealInfo.getWorkCertInfo().getSignDate() != null) {
					String signDateString = dfm.format(dealInfo.getWorkCertInfo().getSignDate());
					dealInfoVo.setSignDateString(signDateString);
				} else {
					dealInfoVo.setSignDateString("");

				}

				if (dealInfo.getAddCertDays() == null) {
					dealInfoVo.setCertDays((dealInfo.getYear() == null ? 0 : dealInfo.getYear()) * 365
							+ (dealInfo.getLastDays() == null ? 0 : dealInfo.getLastDays()) + "（天）");
				} else {
					dealInfoVo.setCertDays(
							dealInfo.getYear() * 365 + dealInfo.getLastDays() + dealInfo.getAddCertDays() + "（天）");
				}
				String notafterString = dealInfo.getNotafter() == null ? "" : df.format(dealInfo.getNotafter());
				dealInfoVo.setNotAfter(notafterString);
				dealInfoVo
						.setDealInfoStatus(workDealInfoStatus.WorkDealInfoStatusMap.get(dealInfo.getDealInfoStatus()));
				workDealInfoVos.add(dealInfoVo);
				// System.out.println(workDealInfoVos.size());
			}

			new ExportExcel("业务查询", WorkDealInfoVo.class).setDataList(workDealInfoVos).write(response, fileName)
					.dispose();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "exportMonthPayment")
	public void exportMonthPayment(WorkDealInfo workDealInfo,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office, HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		Date start = null;
		Date end = null;
		if (startTime != null) {
			StringBuffer startt = new StringBuffer(startTime);
			startt.append("-1");
			StringBuffer endtt = new StringBuffer(endTime);
			endtt.append("-31");
			try {
				start = new SimpleDateFormat("yyyy-MM-dd").parse(startt.toString());
				end = new SimpleDateFormat("yyyy-MM-dd").parse(endtt.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		// List<String> payMethod = Lists.newArrayList();
		// List<Double> money = Lists.newArrayList();
		Map<String, Double> officeMoneys = new HashMap<>();
		Map<String, List<String>> office_payMethod = new HashMap<>();
		Map<String, List<Double>> office_money = new HashMap<>();
		if (area != null && office != null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			officeids.add(office);
		} else if (area != null && office == null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			for (Office o : offices) {
				officeids.add(o.getId());
			}

		} else { //
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			List<Long> areas = Lists.newArrayList();
			if (offsList.size() > 0) {
				for (int i = 0; i < offsList.size(); i++) {
					areas.add(offsList.get(i).getId());
				}
			} else {
				areas.add(-1l);
			}
			List<Office> offices = officeService.findByParentIds(areas);// 根据区域id获取网点id
			if (offices.size() > 0) {
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}
		ConfigApp configApp = configAppService.findByAppId(appId);
		Set<String> month = new LinkedHashSet<String>();
		List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
		List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
		List<Double> payMethodMoneys = Lists.newArrayList();
		List<WorkDealInfo> list = workDealInfoService.findByDayPay(start, end, officeids, appId);
		if (list != null) {
			// List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
			// List<Workoffice_MoneyVo> o_m = new
			// ArrayList<Workoffice_MoneyVo>();
			// Set<String> month = new LinkedHashSet<String>();
			for (int i = 0; i < list.size(); i++) {
				String a = new SimpleDateFormat("yyyy-MM").format(list.get(i).getWorkPayInfo().getCreateDate());
				month.add(a);
				// if(list.get(i).getCreateBy().getOffice().getId()==632001)
				// {
				// System.out.println(list.get(i).getWorkPayInfo().getCreateDate()+":"+list.get(i).getWorkPayInfo().getPosMoney()+"***********");
				// }
			}
			Set<Long> offices = new LinkedHashSet<Long>();
			if (officeids != null) {
				for (int i = 0; i < list.size(); i++) {
					offices.add(list.get(i).getOfficeId());
				}
			}
			Object offs[] = offices.toArray();
			Object months[] = month.toArray();
			for (int m = 0; m < months.length; m++) {
				WorkDate_MoneVo workDate_MoneVo = new WorkDate_MoneVo();
				double countMoneys = 0L;
				for (int o = 0; o < offs.length; o++) {
					double countPostMoney = 0L;
					double countBankMoney = 0L;
					double countXjMoney = 0L;
					double countAlipayMoney = 0L;
					double countMoney = 0L;
					// double postMoney = 0L;
					// double bankMoney = 0L;
					// double xjMoney = 0L;
					// double alipayMoney = 0L;
					for (int i = 0; i < list.size(); i++) {
						String mo = new SimpleDateFormat("yyyy-MM")
								.format(list.get(i).getWorkPayInfo().getCreateDate());
						if (list.get(i).getOfficeId().equals(offs[o]) && ((String) months[m]).indexOf(mo) != -1) {
							double postMoney = 0L;
							double bankMoney = 0L;
							double xjMoney = 0L;
							double alipayMoney = 0L;
							if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
								if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
									if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
										postMoney = list.get(i).getWorkPayInfo().getPosMoney();
									} else {
										postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
									}
								}
								if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
									if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
										bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
									} else {
										bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
									}
								}
								if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
									if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
										xjMoney = list.get(i).getWorkPayInfo().getMoney();
									} else {
										xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
									}

								}
								if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
									if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
										alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
									} else {
										alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
									}

								}
							} else {
								List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
										.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
								for (int w = 0; w < workfinancePayinfos.size(); w++) {
									if (workfinancePayinfos.get(w).getMoney() >= 0) {
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 1) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												xjMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												xjMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 2) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												postMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												postMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 3) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												bankMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												bankMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 4) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												alipayMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												alipayMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
									}
								}
							}
							countPostMoney += postMoney;
							countBankMoney += bankMoney;
							countXjMoney += xjMoney;
							countAlipayMoney += alipayMoney;
							countMoney += (postMoney + bankMoney + xjMoney + alipayMoney);

						}
					}
					String officeName = (officeService.findById((Long) offs[o])).getName();
					Workoffice_MoneyVo workoffice_MoneyVo = new Workoffice_MoneyVo();
					workoffice_MoneyVo.setOfficeName(officeName);
					workoffice_MoneyVo.setDate((String) months[m]);
					workoffice_MoneyVo.setCountPostMoney(countPostMoney);
					workoffice_MoneyVo.setCountBankMoney(countBankMoney);
					workoffice_MoneyVo.setCountXjMoney(countXjMoney);
					workoffice_MoneyVo.setCountAlipayMoney(countAlipayMoney);
					workoffice_MoneyVo.setCountMoney(countMoney);
					o_m.add(workoffice_MoneyVo);
					countMoneys += countMoney;
				}
				workDate_MoneVo.setDate((String) months[m]);
				workDate_MoneVo.setCountMoney(countMoneys);
				w_m.add(workDate_MoneVo);
			}
			for (int ofs = 0; ofs < offs.length; ofs++) {
				int post = 0;
				int bank = 0;
				int xj = 0;
				int alipay = 0;
				for (int of = 0; of < o_m.size(); of++) {
					if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
						if (o_m.get(of).getCountPostMoney() > 0) {
							post++;
						}
						if (o_m.get(of).getCountBankMoney() > 0) {
							bank++;
						}
						if (o_m.get(of).getCountXjMoney() > 0) {
							xj++;
						}
						if (o_m.get(of).getCountAlipayMoney() > 0) {
							alipay++;
						}
					}
				}
				List<String> payMethods = Lists.newArrayList();
				if (post > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setPostMoney(true);
						}
					}
					payMethods.add("pos付款");
				}
				if (bank > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setBankMoney(true);
						}
					}
					payMethods.add("银行转账");
				}
				if (xj > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setXjMoney(true);
						}
					}
					payMethods.add("现金付款");
				}
				if (alipay > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setAlipayMoney(true);
						}
					}
					payMethods.add("支付宝");
				}
				office_payMethod.put(officeService.findById((Long) offs[ofs]).getName(), payMethods);
			}
		}
		// List<Double> payMethodMoneys = Lists.newArrayList();
		double moneys = 0L;
		Iterator<Map.Entry<String, List<String>>> office_m = office_payMethod.entrySet().iterator();
		while (office_m.hasNext()) {
			Entry<String, List<String>> entry = office_m.next();
			double post = 0.0;
			double xj = 0.0;
			double bank = 0.0;
			double alipay = 0.0;
			for (int o = 0; o < o_m.size(); o++) {
				if (entry.getKey().equals(o_m.get(o).getOfficeName())) {
					if (o_m.get(o).getPostMoney()) {
						post += o_m.get(o).getCountPostMoney();
					}
					if (o_m.get(o).getXjMoney()) {
						xj += o_m.get(o).getCountXjMoney();
					}
					if (o_m.get(o).getBankMoney()) {
						bank += o_m.get(o).getCountBankMoney();
					}
					if (o_m.get(o).getAlipayMoney()) {
						alipay += o_m.get(o).getCountAlipayMoney();
					}
				}
			}
			moneys += (post + xj + bank + alipay);
			if (entry.getValue().indexOf("pos付款") != -1) {
				payMethodMoneys.add(post);
			}
			if (entry.getValue().indexOf("银行转账") != -1) {

				payMethodMoneys.add(bank);
			}
			if (entry.getValue().indexOf("现金付款") != -1) {

				payMethodMoneys.add(xj);
			}
			if (entry.getValue().indexOf("支付宝") != -1) {
				payMethodMoneys.add(alipay);
			}
		}
		payMethodMoneys.add(moneys);
		int sheetcount = 0;
		Iterator<Map.Entry<String, List<String>>> it = office_payMethod.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<String>> entry = it.next();
			sheetcount += entry.getValue().size();
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("月回款统计表");
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (2 + sheetcount)));
		sheet.addMergedRegion(new Region(1, (short) 0, 2, (short) 0));
		sheet.addMergedRegion(new Region(1, (short) 1, 2, (short) 1));
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue(startTime + "至" + endTime + "月回款统计表");
		cell0.setCellStyle(style);
		HSSFRow row1 = sheet.createRow(1);
		HSSFRow row2 = sheet.createRow(2);
		row1.createCell(0).setCellValue("日期");
		row1.createCell(1).setCellValue("项目名称");
		int index = 0;
		int count = 0;
		int s = 0;
		Iterator<Map.Entry<String, List<String>>> itt = office_payMethod.entrySet().iterator();
		while (itt.hasNext()) {
			Entry<String, List<String>> entry = itt.next();
			if (entry.getValue().size() == 1) {
				index += (entry.getValue().size());
				sheet.addMergedRegion(new Region(1, (short) (1 + count + 1), 2, (short) (1 + count + 1)));
				row1.createCell(1 + count + 1).setCellValue(entry.getKey());
				count++;
			}
			if (entry.getValue().size() > 1) {
				count += entry.getValue().size();
				sheet.addMergedRegion(new Region(1, (short) (1 + index + 1 + s), 1,
						(short) (1 + index + entry.getValue().size() + s)));
				row1.createCell(1 + 1 + index + s).setCellValue(entry.getKey());// 1+index//
																				// +//
																				// 1//
																				// 加上本身的单元格
				for (int v = 0; v < entry.getValue().size(); v++) {
					row2.createCell(1 + 1 + index + s).setCellValue(entry.getValue().get(v));
					s++;
				}
			}
		}
		sheet.addMergedRegion(new Region(1, (short) (2 + count), 2, (short) (2 + count)));
		row1.createCell(2 + count).setCellValue("合计");
		Object ms[] = month.toArray();
		for (int m = 0; m < ms.length; m++) {
			int a = 0;
			HSSFRow rown = sheet.createRow(2 + m + 1);
			rown.createCell(0).setCellValue(ms[m] + "");
			rown.createCell(1).setCellValue(configApp.getAppName());
			Iterator<Map.Entry<String, List<String>>> its = office_payMethod.entrySet().iterator();
			while (its.hasNext()) {
				Entry<String, List<String>> entry = its.next();
				for (int om = 0; om < o_m.size(); om++) {
					if (o_m.get(om).getDate().equals(ms[m]) && o_m.get(om).getOfficeName().equals(entry.getKey())) {
						if (o_m.get(om).getPostMoney()) {
							rown.createCell(2 + a).setCellValue(o_m.get(om).getCountPostMoney());
							a++;
						}
						if (o_m.get(om).getBankMoney()) {
							rown.createCell(2 + a).setCellValue(o_m.get(om).getCountBankMoney());
							a++;
						}
						if (o_m.get(om).getXjMoney()) {
							rown.createCell(2 + a).setCellValue(o_m.get(om).getCountXjMoney());
							a++;
						}
						if (o_m.get(om).getAlipayMoney()) {
							rown.createCell(2 + a).setCellValue(o_m.get(om).getCountAlipayMoney());
							a++;
						}
					}
				}
			}
			for (int wm = 0; wm < w_m.size(); wm++) {
				if (w_m.get(wm).getDate() == ms[m]) {
					rown.createCell(2 + a).setCellValue(w_m.get(wm).getCountMoney());
				}
			}

		}
		HSSFRow rowz = sheet.createRow(2 + ms.length + 1);
		rowz.createCell(0).setCellValue("合计");
		rowz.createCell(1).setCellValue(" ");
		for (int pm = 0; pm < payMethodMoneys.size(); pm++) {
			rowz.createCell(2 + pm).setCellValue(payMethodMoneys.get(pm));
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition", "attachment; filename=monthPayment.xls");
		wb.write(baos);
		byte[] bytes = baos.toByteArray();
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(bytes);
		bos.close();
		baos.close();
	}

	@RequestMapping(value = "exportDayPayment")
	public void exportDayPayment(WorkDealInfo workDealInfo,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office, HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		// List<String> payMethod = Lists.newArrayList();
		// List<Double> money = Lists.newArrayList();
		Map<String, List<String>> office_payMethod = new HashMap<>();
		Map<String, List<Double>> office_money = new HashMap<>();
		if (area != null && office != null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			officeids.add(office);
		} else if (area != null && office == null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			for (Office o : offices) {
				officeids.add(o.getId());
			}

		} else { //
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			List<Long> areas = Lists.newArrayList();
			if (offsList.size() > 0) {
				for (int i = 0; i < offsList.size(); i++) {
					areas.add(offsList.get(i).getId());
				}
			} else {
				areas.add(-1l);
			}
			List<Office> offices = officeService.findByParentIds(areas);// 根据区域id获取网点id
			if (offices.size() > 0) {
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}
		ConfigApp configApp = configAppService.findByAppId(appId);
		Set<String> month = new LinkedHashSet<String>();
		List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
		List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
		List<Double> payMethodMoneys = Lists.newArrayList();
		List<WorkDealInfo> list = workDealInfoService.findByDayPay(startTime, endTime, officeids, appId);
		if (list != null) {
			// List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
			// List<Workoffice_MoneyVo> o_m = new
			// ArrayList<Workoffice_MoneyVo>();
			// Set<String> month = new LinkedHashSet<String>();
			for (int i = 0; i < list.size(); i++) {
				String a = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getWorkPayInfo().getCreateDate());
				month.add(a);
			}
			Object months[] = month.toArray();
			Set<Long> offices = new LinkedHashSet<Long>();
			if (officeids != null) {
				for (int i = 0; i < list.size(); i++) {
					offices.add(list.get(i).getOfficeId());
				}
			}
			Object offs[] = offices.toArray();
			for (int m = 0; m < months.length; m++) {
				WorkDate_MoneVo workDate_MoneVo = new WorkDate_MoneVo();
				double countMoneys = 0L;
				for (int o = 0; o < offs.length; o++) {
					double countPostMoney = 0L;
					double countBankMoney = 0L;
					double countXjMoney = 0L;
					double countAlipayMoney = 0L;
					double countMoney = 0L;
					// double postMoney = 0L;
					// double bankMoney = 0L;
					// double xjMoney = 0L;
					// double alipayMoney = 0L;
					for (int i = 0; i < list.size(); i++) {
						String mo = new SimpleDateFormat("yyyy-MM-dd")
								.format(list.get(i).getWorkPayInfo().getCreateDate());
						if (list.get(i).getOfficeId().equals(offs[o]) && ((String) months[m]).indexOf(mo) != -1) {
							double postMoney = 0L;
							double bankMoney = 0L;
							double xjMoney = 0L;
							double alipayMoney = 0L;
							if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
								if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
									if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
										postMoney = list.get(i).getWorkPayInfo().getPosMoney();
									} else {
										postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
									}
								}
								if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
									if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
										bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
									} else {
										bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
									}
								}
								if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
									if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
										xjMoney = list.get(i).getWorkPayInfo().getMoney();
									} else {
										xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
									}

								}
								if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
									if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
										alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
									} else {
										alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
									}

								}
							} else {
								List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
										.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
								for (int w = 0; w < workfinancePayinfos.size(); w++) {
									if (workfinancePayinfos.get(w).getMoney() >= 0) {
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 1) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												xjMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												xjMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 2) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												postMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												postMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 3) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												bankMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												bankMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
										if (workfinancePayinfos.get(w).getFinancePaymentInfo()
												.getPaymentMethod() == 4) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												alipayMoney = workfinancePayinfos.get(w).getMoney();
											} else {
												alipayMoney = (-workfinancePayinfos.get(w).getMoney());
											}

										}
									}
								}
							}
							countPostMoney += postMoney;
							countBankMoney += bankMoney;
							countXjMoney += xjMoney;
							countAlipayMoney += alipayMoney;
							countMoney += (postMoney + bankMoney + xjMoney + alipayMoney);
						}
					}
					String officeName = (officeService.findById((Long) offs[o])).getName();
					Workoffice_MoneyVo workoffice_MoneyVo = new Workoffice_MoneyVo();
					workoffice_MoneyVo.setOfficeName(officeName);
					workoffice_MoneyVo.setDate((String) months[m]);
					workoffice_MoneyVo.setCountPostMoney(countPostMoney);
					workoffice_MoneyVo.setCountBankMoney(countBankMoney);
					workoffice_MoneyVo.setCountXjMoney(countXjMoney);
					workoffice_MoneyVo.setCountAlipayMoney(countAlipayMoney);
					workoffice_MoneyVo.setCountMoney(countMoney);
					o_m.add(workoffice_MoneyVo);
					countMoneys += countMoney;
				}
				workDate_MoneVo.setDate((String) months[m]);
				workDate_MoneVo.setCountMoney(countMoneys);
				w_m.add(workDate_MoneVo);
			}
			for (int ofs = 0; ofs < offs.length; ofs++) {
				int post = 0;
				int bank = 0;
				int xj = 0;
				int alipay = 0;
				for (int of = 0; of < o_m.size(); of++) {
					if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
						if (o_m.get(of).getCountPostMoney() > 0) {
							post++;
						}
						if (o_m.get(of).getCountBankMoney() > 0) {
							bank++;
						}
						if (o_m.get(of).getCountXjMoney() > 0) {
							xj++;
						}
						if (o_m.get(of).getCountAlipayMoney() > 0) {
							alipay++;
						}
					}
				}
				List<String> payMethods = Lists.newArrayList();
				if (post > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setPostMoney(true);
						}
					}
					payMethods.add("pos付款");
				}
				if (bank > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setBankMoney(true);
						}
					}
					payMethods.add("银行转账");
				}
				if (xj > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setXjMoney(true);
						}
					}
					payMethods.add("现金付款");
				}
				if (alipay > 0) {
					for (int of = 0; of < o_m.size(); of++) {
						if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
							o_m.get(of).setAlipayMoney(true);
						}
					}
					payMethods.add("支付宝");
				}
				office_payMethod.put(officeService.findById((Long) offs[ofs]).getName(), payMethods);
			}
		}
		// List<Double> payMethodMoneys = Lists.newArrayList();
		double moneys = 0L;
		Iterator<Map.Entry<String, List<String>>> office_m = office_payMethod.entrySet().iterator();
		while (office_m.hasNext()) {
			Entry<String, List<String>> entry = office_m.next();
			double post = 0.0;
			double xj = 0.0;
			double bank = 0.0;
			double alipay = 0.0;
			for (int o = 0; o < o_m.size(); o++) {
				if (entry.getKey().equals(o_m.get(o).getOfficeName())) {
					if (o_m.get(o).getPostMoney()) {
						post += o_m.get(o).getCountPostMoney();
					}
					if (o_m.get(o).getXjMoney()) {
						xj += o_m.get(o).getCountXjMoney();
					}
					if (o_m.get(o).getBankMoney()) {
						bank += o_m.get(o).getCountBankMoney();
					}
					if (o_m.get(o).getAlipayMoney()) {
						alipay += o_m.get(o).getCountAlipayMoney();
					}
				}
			}
			moneys += (post + xj + bank + alipay);
			if (entry.getValue().indexOf("pos付款") != -1) {
				payMethodMoneys.add(post);
			}
			if (entry.getValue().indexOf("银行转账") != -1) {

				payMethodMoneys.add(bank);
			}
			if (entry.getValue().indexOf("现金付款") != -1) {

				payMethodMoneys.add(xj);
			}
			if (entry.getValue().indexOf("支付宝") != -1) {
				payMethodMoneys.add(alipay);
			}
		}
		payMethodMoneys.add(moneys);
		int sheetcount = 0;
		Iterator<Map.Entry<String, List<String>>> it = office_payMethod.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<String>> entry = it.next();
			sheetcount += entry.getValue().size();
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("日回款统计表");
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (2 + sheetcount)));
		sheet.addMergedRegion(new Region(1, (short) 0, 2, (short) 0));
		sheet.addMergedRegion(new Region(1, (short) 1, 2, (short) 1));
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(startTime) + "至"
				+ new SimpleDateFormat("yyyy-MM-dd").format(endTime) + "日回款统计表");
		cell0.setCellStyle(style);
		HSSFRow row1 = sheet.createRow(1);
		HSSFRow row2 = sheet.createRow(2);
		row1.createCell(0).setCellValue("日期");
		row1.createCell(1).setCellValue("项目名称");
		int index = 0;
		int count = 0;
		int s = 0;
		Iterator<Map.Entry<String, List<String>>> itt = office_payMethod.entrySet().iterator();
		while (itt.hasNext()) {
			Entry<String, List<String>> entry = itt.next();
			if (entry.getValue().size() == 1) {
				index++;
				sheet.addMergedRegion(new Region(1, (short) (1 + count + 1), 2, (short) (1 + count + 1)));
				row1.createCell(1 + count + 1).setCellValue(entry.getKey());
				count++;
			}
			if (entry.getValue().size() > 1) {
				count += entry.getValue().size();
				sheet.addMergedRegion(new Region(1, (short) (1 + index + 1 + s), 1,
						(short) (1 + index + entry.getValue().size() + s)));
				row1.createCell(1 + 1 + index + s).setCellValue(entry.getKey());
				for (int v = 0; v < entry.getValue().size(); v++) {
					row2.createCell(1 + 1 + index + s).setCellValue(entry.getValue().get(v));
					s++;
				}
			}
		}
		sheet.addMergedRegion(new Region(1, (short) (2 + count), 2, (short) (2 + count)));
		row1.createCell(2 + count).setCellValue("合计");
		Object ms[] = month.toArray();
		for (int m = 0; m < ms.length; m++) {
			int a = 0;
			HSSFRow rown = sheet.createRow(2 + m + 1);
			rown.createCell(0).setCellValue(ms[m] + "");
			rown.createCell(1).setCellValue(configApp.getAppName());
			Iterator<Map.Entry<String, List<String>>> its = office_payMethod.entrySet().iterator();
			while (its.hasNext()) {
				Entry<String, List<String>> entry = its.next();
				for (int om = 0; om < o_m.size(); om++) {
					if (o_m.get(om).getDate().equals(ms[m]) && o_m.get(om).getOfficeName().equals(entry.getKey())) {
						if (o_m.get(om).getPostMoney()) {
							rown.createCell(2 + a).setCellValue(o_m.get(om).getCountPostMoney());
							a++;
						}
						if (o_m.get(om).getBankMoney()) {
							rown.createCell(2 + a).setCellValue(o_m.get(om).getCountBankMoney());
							a++;
						}
						if (o_m.get(om).getXjMoney()) {
							rown.createCell(2 + a).setCellValue(o_m.get(om).getCountXjMoney());
							a++;
						}
						if (o_m.get(om).getAlipayMoney()) {
							rown.createCell(2 + a).setCellValue(o_m.get(om).getCountAlipayMoney());
							a++;
						}
					}
				}
			}
			for (int wm = 0; wm < w_m.size(); wm++) {
				if (w_m.get(wm).getDate() == ms[m]) {
					rown.createCell(2 + a).setCellValue(w_m.get(wm).getCountMoney());
				}
			}

		}
		HSSFRow rowz = sheet.createRow(2 + ms.length + 1);
		rowz.createCell(0).setCellValue("合计");
		rowz.createCell(1).setCellValue(" ");
		int i = 0;
		for (int pm = 0; pm < payMethodMoneys.size(); pm++) {
			rowz.createCell(2 + i).setCellValue(payMethodMoneys.get(pm));
			i++;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition", "attachment; filename=dayPayment.xls");
		wb.write(baos);
		byte[] bytes = baos.toByteArray();
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(bytes);
		bos.close();
		baos.close();
	}

	@RequestMapping(value = "exportProjectPayment")
	public void exportProjectPayment(WorkDealInfo workDealInfo,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office, HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		List<Long> officeids = Lists.newArrayList();
		List<String> officenames = Lists.newArrayList();
		Map<String, Double> officeMoneys = new HashMap<>();
		Map<String, Set<String>> office_District = new HashMap<>();
		Map<String, List<String>> district_payMethod = new HashMap<>();
		Map<String, List<Double>> district_Moneys = new HashMap<>();
		List<Long> appids = Lists.newArrayList();
		if (area != null && office != null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			officeids.add(office);
		} else if (area != null && office == null) {
			List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网点id
			model.addAttribute("offices", offices);
			for (Office o : offices) {
				officeids.add(o.getId());
			}

		} else { //
			List<Office> offsList = officeService.getOfficeByType(user, 1);
			List<Long> areas = Lists.newArrayList();
			if (offsList.size() > 0) {
				for (int i = 0; i < offsList.size(); i++) {
					areas.add(offsList.get(i).getId());
				}
			} else {
				areas.add(-1l);
			}
			List<Office> offices = officeService.findByParentIds(areas);// 根据区域id获取网点id
			if (offices.size() > 0) {
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}
		ConfigApp configApp = configAppService.findByAppId(appId);
		Set<String> month = new LinkedHashSet<String>();
		Set<String> district = new LinkedHashSet<String>();
		List<Workoffice_district_MoneyVo> odms = new ArrayList<Workoffice_district_MoneyVo>();
		List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
		double moneys = 0L;
		List<WorkDealInfo> list = workDealInfoService.findByProjectPay(startTime, endTime, officeids, appId);
		if (list != null) {

			List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
			for (int i = 0; i < list.size(); i++) {
				String a = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getWorkPayInfo().getCreateDate());
				month.add(a);
			}
			Set<Long> offices = new LinkedHashSet<Long>();
			if (officeids != null) {
				for (int i = 0; i < list.size(); i++) {
					offices.add(list.get(i).getOfficeId());
				}
			}

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getWorkCompany().getDistrict() != null) {
					district.add(list.get(i).getWorkCompany().getDistrict());
				}
			}
			Object offs[] = offices.toArray();
			Object months[] = month.toArray();
			Object districts[] = district.toArray();
			for (int o = 0; o < offs.length; o++) {
				Set<String> dis = new LinkedHashSet<String>();
				for (int d = 0; d < districts.length; d++) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getWorkCompany().getDistrict() != null) {
							if (list.get(i).getOfficeId().equals(offs[o])
									&& list.get(i).getWorkCompany().getDistrict().equals(districts[d])) {
								dis.add((String) districts[d]);
							}
						}
					}
				}
				office_District.put((officeService.findById((Long) offs[o])).getName(), dis);
			}
			for (int m = 0; m < months.length; m++) {
				double countMoneys = 0L;
				WorkDate_MoneVo workDate_MoneVo = new WorkDate_MoneVo();
				for (int d = 0; d < districts.length; d++) {
					double countPostMoney = 0L;
					double countBankMoney = 0L;
					double countXjMoney = 0L;
					double countAlipayMoney = 0L;
					double countMoney = 0L;
					// double postMoney = 0L;
					// double bankMoney = 0L;
					// double xjMoney = 0L;
					// double alipayMoney = 0L;
					for (int i = 0; i < list.size(); i++) {
						String mo = new SimpleDateFormat("yyyy-MM-dd")
								.format(list.get(i).getWorkPayInfo().getCreateDate());
						if (list.get(i).getWorkCompany().getDistrict() != null) {
							if (((String) months[m]).indexOf(mo) != -1
									&& list.get(i).getWorkCompany().getDistrict().equals((String) districts[d])) {
								double postMoney = 0L;
								double bankMoney = 0L;
								double xjMoney = 0L;
								double alipayMoney = 0L;
								if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
											postMoney = list.get(i).getWorkPayInfo().getPosMoney();
										} else {
											postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
											bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
										} else {
											bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
											xjMoney = list.get(i).getWorkPayInfo().getMoney();
										} else {
											xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
										}
									}
								}
								countPostMoney += postMoney;
								countBankMoney += bankMoney;
								countXjMoney += xjMoney;
								countAlipayMoney += alipayMoney;
								countMoney += (postMoney + bankMoney + xjMoney + alipayMoney);
							}
						}
					}
					Workoffice_district_MoneyVo dm = new Workoffice_district_MoneyVo();
					dm.setDate((String) months[m]);
					dm.setDistrictName((String) districts[d]);
					dm.setCountPostMoney(countPostMoney);
					dm.setCountBankMoney(countBankMoney);
					dm.setCountXjMoney(countXjMoney);
					dm.setCountAlipayMoney(countAlipayMoney);
					odms.add(dm);
					countMoneys += (countPostMoney + countBankMoney + countXjMoney + countAlipayMoney);
				}
				workDate_MoneVo.setDate((String) months[m]);
				workDate_MoneVo.setCountMoney(countMoneys);
				w_m.add(workDate_MoneVo);
			}
			List<Double> payMethodMoneys = Lists.newArrayList();

			for (int d = 0; d < districts.length; d++) {
				int post = 0;
				int bank = 0;
				int xj = 0;
				int alipay = 0;
				for (int od = 0; od < odms.size(); od++) {
					if (odms.get(od).getDistrictName().equals(districts[d])) {
						if (odms.get(od).getCountPostMoney() > 0) {
							post++;
						}
						if (odms.get(od).getCountBankMoney() > 0) {
							bank++;
						}
						if (odms.get(od).getCountXjMoney() > 0) {
							xj++;
						}
						if (odms.get(od).getCountAlipayMoney() > 0) {
							alipay++;
						}
					}
				}
				List<String> payMethods = Lists.newArrayList();
				List<Double> dis_Money = Lists.newArrayList();
				if (post > 0) {
					double postMoney = 0L;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d])) {
							odms.get(od).setPostMoney(true);
							postMoney += odms.get(od).getCountPostMoney();
						}
					}
					moneys += postMoney;
					dis_Money.add(postMoney);
					payMethods.add("pos付款");
				}
				if (bank > 0) {
					double bankMoney = 0L;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d])) {
							odms.get(od).setBankMoney(true);
							bankMoney += odms.get(od).getCountBankMoney();
						}
					}
					moneys += bankMoney;
					dis_Money.add(bankMoney);
					payMethods.add("银行转账");
				}
				if (xj > 0) {
					double xjMoney = 0L;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d])) {
							odms.get(od).setXjMoney(true);
							xjMoney += odms.get(od).getCountXjMoney();
						}
					}
					moneys += xjMoney;
					dis_Money.add(xjMoney);
					payMethods.add("现金付款");
				}
				if (alipay > 0) {
					double alipayMoney = 0L;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d])) {
							odms.get(od).setAlipayMoney(true);
							alipayMoney += odms.get(od).getCountAlipayMoney();
						}
					}
					moneys += alipayMoney;
					dis_Money.add(alipayMoney);
					payMethods.add("支付宝");
				}
				if (payMethods != null && payMethods.size() > 0) {
					district_payMethod.put((String) districts[d], payMethods);
				}
				if (dis_Money != null && dis_Money.size() > 0) {
					district_Moneys.put((String) districts[d], dis_Money);
				}
			}
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("项目回款统计表");
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);

		sheet.addMergedRegion(new Region(1, (short) 0, 3, (short) 0));
		sheet.addMergedRegion(new Region(1, (short) 1, 3, (short) 1));
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(startTime) + "至"
				+ new SimpleDateFormat("yyyy-MM-dd").format(endTime) + "项目回款统计表");
		cell0.setCellStyle(style);
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("统计时间");
		row1.createCell(1).setCellValue("项目名称");
		HSSFRow row2 = sheet.createRow(2);
		HSSFRow row3 = sheet.createRow(3);
		int i = 2;
		int aa = 0;
		int i1 = 2;

		Iterator<Map.Entry<String, Set<String>>> off_dis = office_District.entrySet().iterator();
		while (off_dis.hasNext()) {
			int index = 0;
			int qu = 0;
			int count = 0;
			Entry<String, Set<String>> off = off_dis.next();
			// qu=off.getValue().size();
			for (int o = 0; o < off.getValue().size(); o++) {
				int index1 = 0;
				Object districts[] = off.getValue().toArray();
				Iterator<Map.Entry<String, List<String>>> dis_payM = district_payMethod.entrySet().iterator();
				while (dis_payM.hasNext()) {
					Entry<String, List<String>> dp = dis_payM.next();
					if (dp.getKey().equals(districts[o])) {
						qu++;
						index += dp.getValue().size();
						index1 += dp.getValue().size();
						for (int d = 0; d < dp.getValue().size(); d++) {
							row3.createCell(aa + 2).setCellValue(dp.getValue().get(d));
							aa++;
						}
					}
				}
				if (index1 == 1) {
					row2.createCell(i1).setCellValue(districts[o] + "");
					i1++;
				}
				if (index1 > 1) {
					sheet.addMergedRegion(new Region(2, (short) i1, 2, (short) (i1 + index1 - 1)));
					row2.createCell(i1).setCellValue(districts[o] + "");
					i1 = i1 + index1;
				}
			}
			if (index == 1) {
				if (qu == 1) {
					row1.createCell(i).setCellValue(off.getKey());
					sheet.addMergedRegion(new Region(1, (short) i, 2, (short) i));
					i = i + qu;
				}
				if (qu > 1) {
					row1.createCell(i).setCellValue(off.getKey());
					sheet.addMergedRegion(new Region(1, (short) i, 1, (short) (i + qu - 1)));
					i = i + qu;
				}
			}
			if (index > 1) {
				for (int o1 = 0; o1 < off.getValue().size(); o1++) {
					Object districts[] = off.getValue().toArray();
					Iterator<Map.Entry<String, List<String>>> dis_payM1 = district_payMethod.entrySet().iterator();
					while (dis_payM1.hasNext()) {
						Entry<String, List<String>> dp = dis_payM1.next();
						if (dp.getKey().equals(districts[o1])) {
							count += dp.getValue().size();

						}
					}
				}
				sheet.addMergedRegion(new Region(1, (short) i, 1, (short) (i + count - 1)));
				row1.createCell(i).setCellValue(off.getKey());
				i = i + count;
			}
		}
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (i)));
		row1.createCell(i).setCellValue("合计");
		sheet.addMergedRegion(new Region(1, (short) i, 3, (short) i));
		Object months[] = month.toArray();
		for (int m = 0; m < months.length; m++) {
			double countMoneys = 0L;
			int a = 0;// 使单元格恢复为零
			HSSFRow rown = sheet.createRow(m + 4);
			rown.createCell(0).setCellValue((String) months[m]);
			rown.createCell(1).setCellValue(configApp.getAppName());
			Iterator<Map.Entry<String, Set<String>>> f_ds = office_District.entrySet().iterator();
			while (f_ds.hasNext()) {
				Entry<String, Set<String>> entry = f_ds.next();
				Object districts[] = entry.getValue().toArray();
				for (int d = 0; d < districts.length; d++) {
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDate().equals(months[m])
								&& odms.get(od).getDistrictName().equals(districts[d])) {
							if (odms.get(od).getPostMoney()) {
								rown.createCell(2 + a).setCellValue(odms.get(od).getCountPostMoney());
								countMoneys += odms.get(od).getCountPostMoney();
								a++;
							}
							if (odms.get(od).getBankMoney()) {
								rown.createCell(2 + a).setCellValue(odms.get(od).getCountBankMoney());
								countMoneys += odms.get(od).getCountBankMoney();
								a++;
							}
							if (odms.get(od).getXjMoney()) {
								rown.createCell(2 + a).setCellValue(odms.get(od).getCountXjMoney());
								countMoneys += odms.get(od).getCountXjMoney();
								a++;
							}
							if (odms.get(od).getAlipayMoney()) {
								rown.createCell(2 + a).setCellValue(odms.get(od).getCountAlipayMoney());
								countMoneys += odms.get(od).getCountAlipayMoney();
								a++;
							}
						}

					}
				}
			}
			// for (int w = 0; w < w_m.size(); w++) {
			// if (w_m.get(w).getDate().equals(months[m])) {
			// rown.createCell(i).setCellValue(w_m.get(w).getCountMoney());
			// }
			// }
			rown.createCell(i).setCellValue(countMoneys);
		}
		HSSFRow rowz = sheet.createRow(3 + 1 + month.size());
		rowz.createCell(0).setCellValue("合计");
		Iterator<Map.Entry<String, Set<String>>> f_ds = office_District.entrySet().iterator();
		int a = 0;
		double money = 0L;
		while (f_ds.hasNext()) {
			Entry<String, Set<String>> entry = f_ds.next();
			Object districts[] = entry.getValue().toArray();
			for (int d = 0; d < districts.length; d++) {
				Iterator<Entry<String, List<Double>>> d_m = district_Moneys.entrySet().iterator();
				while (d_m.hasNext()) {
					Entry<String, List<Double>> dm = d_m.next();
					if (dm.getKey().equals(districts[d])) {
						for (int m = 0; m < dm.getValue().size(); m++) {
							rowz.createCell(2 + a).setCellValue(dm.getValue().get(m));
							money += dm.getValue().get(m);
							a++;
						}
					}

				}
			}
		}
		rowz.createCell(i).setCellValue(money);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition", "attachment; filename=projectPayment.xls");
		wb.write(baos);
		byte[] bytes = baos.toByteArray();
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(bytes);
		bos.close();
		baos.close();

	}

	@RequestMapping(value = "exportAdjustment")
	public void exportAdjustment(FinancePaymentInfo financePaymentInfo, Date dkstartTime, Date dkendTime,
			Date zzstartTime, Date zzendTime, HttpServletRequest request, String companyName,
			HttpServletResponse response) throws ParseException, IOException {
		if (dkendTime != null) {
			dkendTime.setHours(23);
			dkendTime.setMinutes(59);
			dkendTime.setSeconds(59);
		}
		if (zzendTime != null) {
			zzendTime.setHours(23);
			zzendTime.setSeconds(59);
			zzendTime.setMinutes(59);
		}
		List<FinancePaymentInfo> list = financePaymentInfoService.findAdjustment(financePaymentInfo);
		List<WorkpaymentInfo_dealinfoVo> p_d = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			List<WorkFinancePayInfoRelation> financePay = workFinancePayInfoRelationService
					.findByFinance(list.get(i).getId());
			if (financePay.size() == 0) {
				WorkpaymentInfo_dealinfoVo workpaymentInfo_dealinfoVo = new WorkpaymentInfo_dealinfoVo();
				workpaymentInfo_dealinfoVo.setId(list.get(i).getId());
				workpaymentInfo_dealinfoVo.setDealPayDate(list.get(i).getPayDate());
				workpaymentInfo_dealinfoVo.setPayMoney(list.get(i).getPaymentMoney());
				workpaymentInfo_dealinfoVo.setCompanyName(list.get(i).getCompany());
				workpaymentInfo_dealinfoVo.setRemarks(list.get(i).getRemark());
				p_d.add(workpaymentInfo_dealinfoVo);
			} else {
				List<Long> idList = Lists.newArrayList();
				if (financePay.size() > 0) {
					for (int f = 0; f < financePay.size(); f++) {
						idList.add(financePay.get(f).getWorkPayInfo().getId());
					}
				}
				List<WorkDealInfo> workDealInfos = workDealInfoService.findByFinanceId(idList);
				for (int w = 0; w < workDealInfos.size(); w++) {
					WorkpaymentInfo_dealinfoVo workpaymentInfo_dealinfoVo = new WorkpaymentInfo_dealinfoVo();
					workpaymentInfo_dealinfoVo.setId(list.get(i).getId());
					workpaymentInfo_dealinfoVo.setDealPayDate(list.get(i).getPayDate());
					workpaymentInfo_dealinfoVo.setPayMoney(list.get(i).getPaymentMoney());
					workpaymentInfo_dealinfoVo.setCompanyName(list.get(i).getCompany());
					workpaymentInfo_dealinfoVo.setRemarks(list.get(i).getRemark());
					workpaymentInfo_dealinfoVo.setAliasName(workDealInfos.get(w).getWorkCompany().getCompanyName());
					workpaymentInfo_dealinfoVo.setSignDate(workDealInfos.get(w).getWorkCertInfo().getSignDate());
					p_d.add(workpaymentInfo_dealinfoVo);
				}
			}
		}
		List<WorkpaymentInfo_dealinfoVo> p_ds = Lists.newArrayList();
		for (int i = 0; i < p_d.size(); i++) {
			if (dkstartTime != null && dkendTime != null && zzstartTime == null && zzendTime == null
					&& "".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime.getTime()) {
					p_ds.add(p_d.get(i));
				}

			} else if (dkstartTime != null && dkendTime != null && zzstartTime != null && zzendTime != null
					&& "".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime.getTime()) {
					if (p_d.get(i).getSignDate() != null) {
						if (p_d.get(i).getSignDate().getTime() >= zzstartTime.getTime()
								&& p_d.get(i).getSignDate().getTime() <= zzendTime.getTime()) {
							p_ds.add(p_d.get(i));
						}
					}
				}
			} else if (dkstartTime != null && dkendTime != null && zzstartTime != null && zzendTime != null
					&& !"".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime.getTime()) {
					if (p_d.get(i).getSignDate() != null) {
						if (p_d.get(i).getSignDate().getTime() >= zzstartTime.getTime()
								&& p_d.get(i).getSignDate().getTime() <= zzendTime.getTime()) {
							if (p_d.get(i).getAliasName() != null) {
								if (p_d.get(i).getAliasName().equals(companyName)) {
									p_ds.add(p_d.get(i));
								}
							}
						}
					}
				}
			} else if (dkstartTime != null && dkendTime != null && zzstartTime != null && zzendTime != null
					&& "".equals(companyName) && !"".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime.getTime()) {
					if (p_d.get(i).getSignDate() != null) {
						if (p_d.get(i).getSignDate().getTime() >= zzstartTime.getTime()
								&& p_d.get(i).getSignDate().getTime() <= zzendTime.getTime()) {
							if (p_d.get(i).getCompanyName() != null) {
								if (p_d.get(i).getCompanyName().equals(financePaymentInfo.getCompany())) {
									p_ds.add(p_d.get(i));
								}
							}
						}
					}
				}
			} else if (dkstartTime == null && dkendTime == null && zzstartTime == null && zzendTime == null
					&& "".equals(companyName) && !"".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getCompanyName() != null) {
					if (p_d.get(i).getCompanyName().equals(financePaymentInfo.getCompany())) {
						p_ds.add(p_d.get(i));
					}
				}
			} else if (dkstartTime == null && dkendTime == null && zzstartTime == null && zzendTime == null
					&& !"".equals(companyName) && "".equals(financePaymentInfo.getCompany())) {
				if (p_d.get(i).getAliasName() != null) {
					if (p_d.get(i).getAliasName().equals(companyName)) {
						p_ds.add(p_d.get(i));
					}
				}
			} else {
				String start = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01";
				String end = new SimpleDateFormat("yyyy-MM").format(new Date()) + "-31";
				Date dkstartTime_ = new SimpleDateFormat("yyyy-MM-dd").parse(start);
				Date dkendTime_ = new SimpleDateFormat("yyyy-MM-dd").parse(end);
				dkendTime_.setHours(23);
				dkendTime_.setMinutes(59);
				dkendTime_.setSeconds(59);
				if (p_d.get(i).getDealPayDate().getTime() >= dkstartTime_.getTime()
						&& p_d.get(i).getDealPayDate().getTime() <= dkendTime_.getTime()) {
					p_ds.add(p_d.get(i));
				}
			}
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("项目调整统计表");
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 5));
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		row0.setHeightInPoints((short) 25);
		if (dkstartTime != null && zzstartTime == null) {
			cell0.setCellValue("到款日期" + new SimpleDateFormat("yyyy-Mm-dd").format(dkstartTime) + "至"
					+ new SimpleDateFormat("yyyy-MM-dd").format(dkendTime) + "日项目调整统计表");
		} else if (dkstartTime != null && zzstartTime != null) {
			cell0.setCellValue("到款日期" + new SimpleDateFormat("yyyy-Mm-dd").format(dkstartTime) + "至"
					+ new SimpleDateFormat("yyyy-MM-dd").format(dkendTime) + " 制证日期"
					+ new SimpleDateFormat("yyyy-Mm-dd").format(zzstartTime) + "至"
					+ new SimpleDateFormat("yyyy-MM-dd").format(zzendTime) + "日项目调整统计表");
		} else if (dkstartTime == null && zzstartTime != null) {
			cell0.setCellValue("制证日期" + new SimpleDateFormat("yyyy-Mm-dd").format(zzstartTime) + "至"
					+ new SimpleDateFormat("yyyy-MM-dd").format(zzendTime) + "日项目调整统计表");
		} else {
			cell0.setCellValue("本月项目调整统计表");
		}
		cell0.setCellStyle(style);
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("支付款项录入时间");
		row1.createCell(1).setCellValue("业务使用款项");
		row1.createCell(2).setCellValue("单位名称");
		row1.createCell(3).setCellValue("应用名称");
		row1.createCell(4).setCellValue("业务制证时间");
		row1.createCell(5).setCellValue("备注");
		for (int i = 0; i < p_ds.size(); i++) {
			HSSFRow rown = sheet.createRow(i + 2);
			if (p_ds.get(i).getDealPayDate() == null) {
				rown.createCell(0).setCellValue("");
			} else {
				rown.createCell(0).setCellValue(p_ds.get(i).getDealPayDate() + "");
			}
			if (p_ds.get(i).getPayMoney() == null) {
				rown.createCell(1).setCellValue("");
			} else {
				rown.createCell(1).setCellValue(p_ds.get(i).getPayMoney() + "");
			}
			if (p_ds.get(i).getCompanyName() == null) {
				rown.createCell(2).setCellValue("");
			} else {
				rown.createCell(2).setCellValue(p_ds.get(i).getCompanyName());
			}
			if (p_ds.get(i).getSignDate() == null) {
				rown.createCell(3).setCellValue("");
			} else {
				rown.createCell(3).setCellValue(p_ds.get(i).getAliasName());
			}
			if (p_ds.get(i).getSignDate() == null) {
				rown.createCell(4).setCellValue("");

			} else {

				rown.createCell(4).setCellValue(p_ds.get(i).getSignDate() + "");
			}
			if (p_ds.get(i).getRemarks() == null) {
				rown.createCell(5).setCellValue("");
			} else {

				rown.createCell(5).setCellValue(p_ds.get(i).getRemarks());
			}

		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition", "attachment; filename=projectSchedule.xls");
		wb.write(baos);
		byte[] bytes = baos.toByteArray();
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(bytes);
		bos.close();
		baos.close();
	}

	@RequestMapping(value = "exportYearProjectList")
	public void exportYearProjectList(String startTime, String endTime, Model model, Long configProjectTypeId,
			Long appId, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();
		if (startTime == null || "".equals(startTime)) {
			startTime = new SimpleDateFormat("yyyy").format(new Date());
			endTime = new SimpleDateFormat("yyyy").format(new Date());
		}
		String start = startTime + "-01-01";
		String end = endTime + "-12-30";
		Date startT = new SimpleDateFormat("yyyy-MM-dd").parse(start);
		Date endT = new SimpleDateFormat("yyyy-MM-dd").parse(end);
		int month = 0;
		List<Date> months = Lists.newArrayList();
		if (startT.getYear() == new Date().getYear()) {
			month = new Date().getMonth() + 1;
		} else {
			month = 12;
		}
		for (int i = 0; i < month; i++) {
			String m = startTime + "-" + (i + 1) + "-1";
			Date mo = new SimpleDateFormat("yyyy-MM-dd").parse(m);
			months.add(mo);
		}
		List<Long> configappids = Lists.newArrayList();
		if (configProjectTypeId != null && appId == null) {
			List<Long> appids = Lists.newArrayList();
			List<ConfigApp> configApps = configAppService.findByconfigProjectType(configProjectTypeId);
			if (configApps != null) {
				for (int a = 0; a < configApps.size(); a++) {
					appids.add(configApps.get(a).getId());
					configappids.add(configApps.get(a).getId());
				}
			} else {
				appids.add(-1L);
			}

		} else if (configProjectTypeId != null && appId != null) {
			configappids.add(appId);
		} else {
			configappids.clear();
		}
		Map<WorkDealInfo, List<Double>> w_months = new HashMap<>();
		List<WorkDealInfo> list = workDealInfoService.findByProjectYear(startT, endT, configappids);
		if (list != null) {
			for (int c = 0; c < configappids.size(); c++) {
				double zmoney = 0;
				WorkDealInfo wdi = new WorkDealInfo();
				List<Double> month_Moneys = Lists.newArrayList();
				for (int m = 0; m < months.size(); m++) {
					double moneys = 0;
					for (int i = 0; i < list.size(); i++) {
						if (m < months.size() - 1) {
							if (list.get(i).getCreateDate().getTime() >= months.get(m).getTime()
									&& list.get(i).getCreateDate().getTime() < months.get(m + 1).getTime()
									&& list.get(i).getConfigApp().getId().equals(configappids.get(c))) {
								double postMoney = 0.0;
								double bankMoney = 0.0;
								double alipayMoney = 0.0;
								double xjMoney = 0.0;
								// double posMoney =
								// list.get(i).getWorkPayInfo().getPosMoney();
								// double bankMoney =
								// list.get(i).getWorkPayInfo().getBankMoney();
								// double alipayMoney =
								// list.get(i).getWorkPayInfo().getAlipayMoney();
								// double xjMoney =
								// list.get(i).getWorkPayInfo().getMoney();
								// moneys += (posMoney + bankMoney + alipayMoney
								// + xjMoney);
								if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
											postMoney = list.get(i).getWorkPayInfo().getPosMoney();
										} else {
											postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
											bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
										} else {
											bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
											xjMoney = list.get(i).getWorkPayInfo().getMoney();
										} else {
											xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
										}
									}
								}
								moneys += (postMoney + bankMoney + alipayMoney + xjMoney);
							}
						} else if (m == months.size() - 1) {
							if (list.get(i).getCreateDate().getTime() >= months.get(m).getTime()
									&& list.get(i).getConfigApp().getId().equals(configappids.get(c))) {
								double postMoney = 0.0;
								double bankMoney = 0.0;
								double alipayMoney = 0.0;
								double xjMoney = 0.0;
								// double posMoney =
								// list.get(i).getWorkPayInfo().getPosMoney();
								// double bankMoney =
								// list.get(i).getWorkPayInfo().getBankMoney();
								// double alipayMoney =
								// list.get(i).getWorkPayInfo().getAlipayMoney();
								// double xjMoney =
								// list.get(i).getWorkPayInfo().getMoney();
								// moneys += (posMoney + bankMoney + alipayMoney
								// + xjMoney);
								if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
											postMoney = list.get(i).getWorkPayInfo().getPosMoney();
										} else {
											postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
											bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
										} else {
											bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
										}
									}
									if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
											xjMoney = list.get(i).getWorkPayInfo().getMoney();
										} else {
											xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}

											}
										}
									}
								}
								moneys += (postMoney + bankMoney + alipayMoney + xjMoney);
							}
						}
					}
					month_Moneys.add(moneys);
					zmoney += moneys;
				}

				ConfigProduct configProduct = new ConfigProduct();
				configProduct.setConfigApp(configAppService.findByAppId(configappids.get(c)));
				List<ConfigProduct> pros = configProductService.findByAppId(configProduct);// 获取应用下的产品信息
				String proLabel = "";
				for (int j = 0; j < pros.size(); j++) {
					if (pros.get(j).getProductLabel() == 1) {
						proLabel = "(专用)";
					} else {
						proLabel = "(通用)";
					}
					if (j != pros.size() - 1) {
					}
				}
				ConfigApp configapp = configAppService.findByAppId(configappids.get(c));
				configapp.setAppName(configapp.getAppName() + proLabel);
				wdi.setConfigApp(configAppService.findByAppId(configappids.get(c)));
				month_Moneys.add(zmoney);
				w_months.put(wdi, month_Moneys);
			}
		}
		List<Double> zj = Lists.newArrayList();
		int a = months.size() + 1;
		for (int l = 0; l < a; l++) {
			double aa = 0;
			Iterator<Map.Entry<WorkDealInfo, List<Double>>> itt = w_months.entrySet().iterator();
			while (itt.hasNext()) {
				Entry<WorkDealInfo, List<Double>> entry = itt.next();
				aa += entry.getValue().get(l);
			}
			zj.add(aa);
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("年项目汇总表");
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 18);
		style.setFont(font);
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (months.size() + 3)));
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		row0.setHeightInPoints((short) 25);
		cell0.setCellValue(startTime + "年项目汇总表");
		cell0.setCellStyle(style);
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("序号");
		row1.createCell(1).setCellValue("项目");
		sheet.addMergedRegion(new Region(1, (short) 1, 1, (short) 2));
		for (int m = 0; m < months.size(); m++) {
			row1.createCell(m + 3).setCellValue((m + 1) + "月");
		}
		row1.createCell(months.size() + 3).setCellValue(startTime + "年合计");
		Iterator<Map.Entry<WorkDealInfo, List<Double>>> it = w_months.entrySet().iterator();
		int index = 0;
		int count = 0;
		while (it.hasNext()) {
			Entry<WorkDealInfo, List<Double>> entry = it.next();
			HSSFRow rown = sheet.createRow(2 + index);
			rown.createCell(0).setCellValue(index + 1);
			rown.createCell(1).setCellValue(entry.getKey().getConfigApp().getConfigProjectType().getProjectName());
			rown.createCell(2).setCellValue(entry.getKey().getConfigApp().getAppName());
			for (int l = 0; l < entry.getValue().size(); l++) {
				rown.createCell(3 + l).setCellValue(entry.getValue().get(l));
			}
			index++;
		}
		sheet.addMergedRegion(new Region(2 + index, (short) 1, 2 + index, (short) 2));
		HSSFRow rowz = sheet.createRow(2 + index);
		rowz.createCell(1).setCellValue("项目收入合计");
		for (int i = 0; i < zj.size(); i++) {
			rowz.createCell(3 + i).setCellValue(zj.get(i));
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition", "attachment; filename=yearProjectSchedule.xls");
		wb.write(baos);
		byte[] bytes = baos.toByteArray();
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(bytes);
		bos.close();
		baos.close();
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "certCount")
	public String certCount(Model model) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int nowYear = c.get(Calendar.YEAR);
		model.addAttribute("certListSum", null);
		model.addAttribute("countDate", date);
		model.addAttribute("oneYear", nowYear - 1);
		model.addAttribute("twoYear", nowYear - 2);
		model.addAttribute("threeyear", nowYear - 3);
		return "modules/work/workDealInfoCertUpdateCount";
	}

	// 证书更新率统计
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "certUpdateCount")
	public String certUpdateCount(Date date, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ConfigApp> configApps = configAppService.selectAll();
		List<WorkDealInfo> workDealInfos = null;
		List<Object> listSum = new ArrayList<Object>();
		for (ConfigApp configApp : configApps) {
			Map map = new HashMap<>();
			map.put("configAppName", configApp.getAppName());
			// 后3月
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "A3M");
			map.put("aThreeMoth", workDealInfos.size());
			// 后2月
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "A1M");
			map.put("aOneMoth", workDealInfos.size());
			// 前1月
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "B1M");
			map.put("bOneMoth", workDealInfos.size());
			// 前3月
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "B3M");
			map.put("bThreeMoth", workDealInfos.size());
			// 前1年更新率
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "B1Y");
			int bOneYear = workDealInfos.size();
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "MB1Y");
			int mBOneYear = workDealInfos.size();
			if (bOneYear == 0) {
				map.put("probabilityOne", "0%");
			} else {
				map.put("probabilityOne",
						mBOneYear == 0 ? "本年度需更新的证书0张,本年度已更新证书:" + bOneYear + "张" : bOneYear / mBOneYear * 100 + "%");
			}

			// 前2年更新率
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "B2Y");
			int bTwoYear = workDealInfos.size();
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "MB2Y");
			int MbTwoYear = workDealInfos.size();
			if (bTwoYear == 0) {
				map.put("probabilityTwo", "0%");
			} else {
				map.put("probabilityTwo",
						MbTwoYear == 0 ? "本年度需更新的证书0张,本年度已更新证书:" + bTwoYear + "张" : bTwoYear / MbTwoYear * 100 + "%");
			}
			// 前3年更新率
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "B3Y");
			int bThreeYear = workDealInfos.size();
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "MB3Y");
			int mBThreeYear = workDealInfos.size();
			if (bThreeYear == 0) {
				map.put("probabilityThree", "0%");
			} else {
				map.put("probabilityThree", mBThreeYear == 0 ? "本年度需更新的证书0张,本年度已更新证书:" + bThreeYear + "张"
						: bOneYear / mBThreeYear * 100 + "%");
			}

			// 当前时间更新率
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "NEWY");
			int reNew = workDealInfos.size();
			workDealInfos = workDealInfoService.getWorkInfoByApp(date, configApp, "MNEWY");
			int mustReNew = workDealInfos.size();
			if (reNew == 0) {
				map.put("probability", "0%");
			} else {
				map.put("probability",
						mustReNew == 0 ? "本年度需更新的证书0张,本年度已更新证书:" + reNew + "张" : reNew / mustReNew * 100 + "%");
			}
			listSum.add(map);
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int nowYear = c.get(Calendar.YEAR);
		model.addAttribute("certListSum", listSum);
		model.addAttribute("countDate", date);
		model.addAttribute("oneYear", nowYear - 1);
		model.addAttribute("twoYear", nowYear - 2);
		model.addAttribute("threeyear", nowYear - 3);
		return "modules/work/workDealInfoCertUpdateCount";
	}

	/**
	 * 
	 * @Title: showAgentProduct @Description:
	 *         TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param app 应用ID @param
	 *         productName 产品名称 @param infoType 业务类型 @param lable 专用、通用 @return
	 *         设定文件 @return String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "showAgentProduct")
	@ResponseBody
	public String showAgentProduct(Long app, String productName, Integer lable, Integer infoType) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.findByIdOrLable(app, productName, lable);

			JSONArray array = new JSONArray();

			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());

			Set<String> nameSet = new HashSet<String>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(boundList.get(i).getAgent().getTempStyle());
			}
			if (nameSet.size() > 0) {
				json.put("tempStyle", 1);
				Iterator<String> itName = nameSet.iterator();
				while (itName.hasNext()) {
					JSONObject iter = new JSONObject();
					String str = itName.next();
					if (str.endsWith("1")) {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "标准");
						array.put(iter);
					} else if (str.endsWith("2")) {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "政府统一采购");
						array.put(iter);
					} else {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "合同采购");
						array.put(iter);
					}
				}

				JSONObject jsonMap = (JSONObject) array.get(0);
				List<ConfigChargeAgentBoundConfigProduct> boundStyleList = configChargeAgentBoundConfigProductService
						.findByProIdAllByStyle(configProduct.getId(), jsonMap.get("id").toString());

				JSONArray iterList2 = new JSONArray();

				for (int i = 0; i < boundStyleList.size(); i++) {
					JSONObject iter2 = new JSONObject();
					iter2.put("id", boundStyleList.get(i).getId());
					iter2.put("name", boundStyleList.get(i).getAgent().getTempName());
					iterList2.put(iter2);
				}

				json.put("boundStyleList", iterList2);

				json.put("typeMap", array);

			} else {

				json.put("tempStyle", -1);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}
	
	
	/**
	 * 
	 * @Title: showAgentProduct @Description:
	 *         TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param app 应用ID @param
	 *         productName 产品名称 @param infoType 业务类型 @param lable 专用、通用 @return
	 *         设定文件 @return String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "showAgentProductById")
	@ResponseBody
	public String showAgentProduct(Long productId, Integer infoType) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.get(productId);

			JSONArray array = new JSONArray();

			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());

			Set<String> nameSet = new HashSet<String>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(boundList.get(i).getAgent().getTempStyle());
			}
			if (nameSet.size() > 0) {
				json.put("tempStyle", 1);
				Iterator<String> itName = nameSet.iterator();
				while (itName.hasNext()) {
					JSONObject iter = new JSONObject();
					String str = itName.next();
					if (str.endsWith("1")) {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "标准");
						array.put(iter);
					} else if (str.endsWith("2")) {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "政府统一采购");
						array.put(iter);
					} else {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "合同采购");
						array.put(iter);
					}
				}

				JSONObject jsonMap = (JSONObject) array.get(0);
				List<ConfigChargeAgentBoundConfigProduct> boundStyleList = configChargeAgentBoundConfigProductService
						.findByProIdAllByStyle(configProduct.getId(), jsonMap.get("id").toString());

				JSONArray iterList2 = new JSONArray();

				for (int i = 0; i < boundStyleList.size(); i++) {
					JSONObject iter2 = new JSONObject();
					iter2.put("id", boundStyleList.get(i).getId());
					iter2.put("name", boundStyleList.get(i).getAgent().getTempName());
					iterList2.put(iter2);
				}

				json.put("boundStyleList", iterList2);

				json.put("typeMap", array);

			} else {

				json.put("tempStyle", -1);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}
	
	

	/**
	 * 
	 * @Title: showAgentProduct @Description:
	 *         TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param app 应用ID @param
	 *         productName 产品名称 @param infoType 业务类型 @param lable 专用、通用 @return
	 *         设定文件 @return String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "setStyleList")
	@ResponseBody
	public String setStyleList(Long app, String productName, Integer lable, Integer infoType, String style) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.findByIdOrLable(app, productName, lable);
			JSONArray array = new JSONArray();
			List<ConfigChargeAgentBoundConfigProduct> boundStyleList = configChargeAgentBoundConfigProductService
					.findByProIdAllByStyle(configProduct.getId(), style);
			for (int i = 0; i < boundStyleList.size(); i++) {
				JSONObject iter2 = new JSONObject();
				iter2.put("id", boundStyleList.get(i).getId());// 关联表ID
				iter2.put("name", boundStyleList.get(i).getAgent().getTempName());
				iter2.put("agentId", boundStyleList.get(i).getAgent().getId());
				array.put(iter2);
			}

			json.put("array", array);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}
	
	
	
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "setStyleList1")
	@ResponseBody
	public String setStyleList1(Long productId) {
		JSONObject json = new JSONObject();
		try {
			
			
			ConfigProduct configProduct = configProductService.get(productId);
			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());
			
			for (int i = 0; i < boundList.size(); i++) {
				json.put(boundList.get(i).getAgent().getTempStyle(),boundList.get(i).getAgent().getTempStyle());
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}
	
	
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "setTemplateList")
	@ResponseBody
	public String setTemplateList(Long productId,Integer infoType,String style) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.get(productId);
			JSONArray array = new JSONArray();
			List<ConfigChargeAgentBoundConfigProduct> boundStyleList = configChargeAgentBoundConfigProductService
					.findByProIdAllByStyle(configProduct.getId(), style);
			for (int i = 0; i < boundStyleList.size(); i++) {
				JSONObject iter2 = new JSONObject();
				iter2.put("id", boundStyleList.get(i).getId());// 关联表ID
				iter2.put("name", boundStyleList.get(i).getAgent().getTempName());
				iter2.put("agentId", boundStyleList.get(i).getAgent().getId());
				array.put(iter2);
			}

			json.put("array", array);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}
	
	
	

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "checkSurplusNum")
	@ResponseBody
	public String checkSurplusNum(Long boundId) {
		JSONObject json = new JSONObject();
		try {
			ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(boundId);
			json.put("surplusNum", bound.getAgent().getSurplusNum());
			json.put("agentId", bound.getAgent().getId());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 
	 * @Title: showYear @Description:
	 *         TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param @param
	 *         productName @param @param lable @param @return 设定文件 @return
	 *         String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "showYear")
	@ResponseBody
	public String showYear(Long app, String productName, Integer lable, Integer infoType) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.findByIdOrLable(app, productName, lable);

			if (configProduct.getChargeAgentId() != null) {
				if (infoType != null) {
					String[] years = configChargeAgentDetailService
							.getChargeAgentYears(configProduct.getChargeAgentId(), infoType);
					for (int i = 0; i < years.length; i++) {
						switch (years[i]) {
						case "1":
							json.put("year1", true);
							break;
						case "2":
							json.put("year2", true);
							break;
						case "4":
							json.put("year4", true);
							break;
						case "5":
							json.put("year5", true);
							break;
						}
					}
				}
				ConfigChargeAgent chargeAgent = chargeAgentService.get(configProduct.getChargeAgentId());
				json.put("tempStyle", chargeAgent.getTempStyle());
			} else {
				json.put("tempStyle", -1);
			}

			ConfigRaAccountExtendInfo configRaAccountExtendInfo = configRaAccountExtendInfoService
					.get(configProduct.getRaAccountExtedId());
			RaAccountUtil.getExtendInfo(json, configRaAccountExtendInfo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 
	 * @Title: showYearNew @Description:
	 *         TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param @param
	 *         productName @param @param lable @param @return 设定文件 @return
	 *         String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "showYearNew")
	@ResponseBody
	public String showYearNew(Long boundId, Integer infoType) {
		JSONObject json = new JSONObject();
		try {
			ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(boundId);
			json.put("pos", bound.getAgent().getChargeMethodPos());
			json.put("bank", bound.getAgent().getChargeMethodBank());
			json.put("money", bound.getAgent().getChargeMethodMoney());
			
			if (bound.getAgent().getSurplusUpdateNum()==null) {
				json.put("avaNum", 0);
			}else{
				json.put("avaNum", bound.getAgent().getSurplusUpdateNum());
			}
			if (infoType != null) {
				String[] years = configChargeAgentDetailService.getChargeAgentYears(bound.getAgent().getId(), infoType);
				for (int i = 0; i < years.length; i++) {
					switch (years[i]) {
					case "1":
						json.put("year1", true);
						break;
					case "2":
						json.put("year2", true);
						break;
					case "3":
						json.put("year3", true);
						break;
					case "4":
						json.put("year4", true);
						break;
					case "5":
						json.put("year5", true);
						break;
					}
				}
			}
			ConfigRaAccountExtendInfo configRaAccountExtendInfo = configRaAccountExtendInfoService
					.get(bound.getProduct().getRaAccountExtedId());
			RaAccountUtil.getExtendInfo(json, configRaAccountExtendInfo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 
	 * @Title: manySave @Description: TODO(走ajax后台保存，批量保存) @param @param
	 *         model @param @param workDealInfoId @param @param
	 *         redirectAttributes @param @param appId @param @param
	 *         product @param @param dealInfType1 @param @param
	 *         year @param @param yar @param @param companyId @param @param
	 *         companyName @param @param companyType @param @param
	 *         organizationNumber @param @param orgExpirationTime @param @param
	 *         selectLv @param @param comCertificateType @param @param
	 *         comCertficateNumber @param @param comCertficateTime @param @param
	 *         legalName @param @param s_province @param @param
	 *         s_city @param @param s_county @param @param address @param @param
	 *         companyMobile @param @param remarks @param @param
	 *         workuserId @param @param workType @param @param
	 *         contactName @param @param conCertType @param @param
	 *         contacEmail @param @param conCertNumber @param @param
	 *         contactPhone @param @param contactTel @param @param
	 *         deal_info_status @param @param recordContent @param @param
	 *         lable @param @param classifying @param @param pName @param @param
	 *         pEmail @param @param pIDCard @param @param
	 *         contactSex @param @return 设定文件 @return String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "manySave")
	@ResponseBody
	public String manySave(Model model, Long workDealInfoId, RedirectAttributes redirectAttributes, Long appId,
			String product, Integer dealInfType1, Integer year, Integer yar, Long companyId, String companyName,
			String companyType, String organizationNumber, String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber, String comCertficateTime, String legalName,
			String s_province, String s_city, String s_county, String address, String companyMobile, String remarks,
			Long workuserId, Integer workType, String contactName, String conCertType, String contacEmail,
			String conCertNumber, String contactPhone, String contactTel, String deal_info_status, String recordContent,
			Integer lable, Integer classifying, String pName, String pEmail, String pIDCard, String contactSex,
			String areaRemark, Integer agentId, Long agentDetailId) {

		JSONObject json = new JSONObject();
		try {
			WorkCompany workCompany = workCompanyService.finByNameAndNumber(companyName, organizationNumber);

			// 保存单位信息

			workCompany.setCompanyName(companyName);
			workCompany.setCompanyType(companyType);
			workCompany.setComCertificateType(comCertificateType);
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Timestamp ts1 = new Timestamp(System.currentTimeMillis());
			try {
				ts1 = Timestamp.valueOf(orgExpirationTime + " 00:00:00");
				ts = Timestamp.valueOf(comCertficateTime + " 00:00:00");
			} catch (Exception e) {
				// TODO: handle exception
			}

			workCompany.setComCertficateNumber(comCertficateNumber);
			workCompany.setSelectLv(selectLv);
			workCompany.setOrganizationNumber(organizationNumber);
			if (orgExpirationTime != null && !orgExpirationTime.equals("")) {
				workCompany.setOrgExpirationTime(ts1);
			}
			if (comCertficateTime != null && !comCertficateTime.equals("")) {
				workCompany.setComCertficateTime(ts);
			}

			workCompany.setProvince(s_province);
			workCompany.setLegalName(legalName);
			workCompany.setCity(s_city);
			workCompany.setDistrict(s_county);
			workCompany.setAddress(address);
			workCompany.setCompanyMobile(companyMobile);
			workCompany.setRemarks(remarks);
			workCompany.setAreaRemark(areaRemark);

			// 保存经办人信息
			WorkUser workUser = new WorkUser();
			workUser.setId(workuserId);
			workUser.setStatus(1);
			workUser.setContactName(contactName);
			workUser.setConCertType(conCertType);
			workUser.setConCertNumber(conCertNumber);
			workUser.setContactEmail(contacEmail);
			workUser.setContactPhone(contactPhone);
			workUser.setContactTel(contactTel);
			workUser.setContactSex(contactSex);
			// 保存work_deal-info
			WorkDealInfo workDealInfo = null;
			if (workDealInfoId != null) {
				workDealInfo = workDealInfoService.get(workDealInfoId);
			} else {
				workDealInfo = new WorkDealInfo();
			}

			if (appId == null) {
				model.addAttribute("workUser", workUser);
				model.addAttribute("workCompany", workCompany);
				model.addAttribute("product", product);
				model.addAttribute("year", year);

				json.put("status", -1);
				return json.toString();
			}
			ConfigApp configApp = configAppService.get(appId);
			// ConfigProduct configProduct =
			// configProductService.findByIdOrLable(
			// appId, product, lable);

			ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(agentDetailId);

			ConfigProduct configProduct = bound.getProduct();

			workCompanyService.save(workCompany);
			workUserService.save(workUser);

			workDealInfo.setConfigApp(configApp);
			ConfigCommercialAgent commercialAgent = configAgentAppRelationService.findAgentByApp(configApp);
			List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
					.findByOffice(UserUtils.getUser().getOffice());
			if (configAgentOfficeRelations.size() > 0) {
				workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());// 劳务关系外键
			}
			workDealInfo.setCommercialAgent(commercialAgent);
			workDealInfo.setWorkUser(workUser);
			workDealInfo.setWorkCompany(workCompany);

			workDealInfo.setConfigProduct(configProduct);
			workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
			workDealInfo.setPayType(agentId);

			workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
			workDealInfo.setDealInfoType1(dealInfType1);

			if (year == null) {
				workDealInfo.setYear(0);
			} else {
				workDealInfo.setYear(year);
			}
			workDealInfo.setClassifying(classifying);
			workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
			workDealInfo.setCreateBy(UserUtils.getUser());
			workDealInfo.setCreateDate(new Date());
			workDealInfo.setSvn(workDealInfoService.getSVN(0));
			WorkCompanyHis workCompanyHis = workCompanyService.change(workCompany);
			workCompanyHisService.save(workCompanyHis);
			workDealInfo.setWorkCompanyHis(workCompanyHis);
			WorkUserHis workUserHis = workUserService.change(workUser, workCompanyHis);
			workUserHisService.save(workUserHis);
			workDealInfo.setWorkUserHis(workUserHis);
			// 保存申请人信息
			WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
			WorkCertInfo workCertInfo = new WorkCertInfo();

			if (workDealInfo.getWorkCertInfo() != null) {
				workCertApplyInfo = workDealInfo.getWorkCertInfo().getWorkCertApplyInfo();
			} else {
				workCertApplyInfo = new WorkCertApplyInfo();
			}

			workCertApplyInfo.setName(pName);
			workCertApplyInfo.setEmail(pEmail);
			workCertApplyInfo.setIdCard(pIDCard);

			workCertApplyInfoService.save(workCertApplyInfo);
			// 保存work_cert_info
			if (workDealInfo.getWorkCertInfo() != null) {
				workCertInfo = workDealInfo.getWorkCertInfo();
			} else {
				workCertInfo = new WorkCertInfo();
			}
			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			workCertInfoService.save(workCertInfo);
			workDealInfo.setWorkCertInfo(workCertInfo);
			workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
			workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
			workDealInfoService.save(workDealInfo);
			// 录入人日志保存
			WorkLog workLog = new WorkLog();
			workLog.setRecordContent(recordContent);
			workLog.setWorkDealInfo(workDealInfo);
			workLog.setCreateDate(new Date());
			workLog.setCreateBy(UserUtils.getUser());
			workLog.setConfigApp(configApp);
			workLog.setWorkCompany(workCompany);
			workLog.setOffice(UserUtils.getUser().getOffice());
			workLogService.save(workLog);
			logUtil.saveSysLog("业务中心",
					"新增业务批量保存：编号" + workDealInfo.getId() + "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(),
					"");

			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				json.put("status", -1);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return json.toString();
	}

	@RequestMapping(value = "findByKeySn")
	@ResponseBody
	public String findByKeySn(String keySn, Long dealId) {
		JSONObject json = new JSONObject();
		try {
			Integer isUserInteger = workDealInfoService.findByKey(keySn);
			
			if(isUserInteger>0){	
				//和吊销的证书做比对
				WorkDealInfo dealInfo = workDealInfoService.findByRevokeStatus(keySn);
				if(dealInfo!=null){
					int count=1;
					while (dealInfo.getPrevId() != null) {
						dealInfo = workDealInfoService.findPreDealInfo(dealInfo.getPrevId());
						if(dealInfo.getKeySn().equals(keySn)){
							count +=1;
						}
					}
					isUserInteger = isUserInteger-count;
				}
			}
			
			WorkDealInfo dealInfo = workDealInfoService.get(dealId);
			if (dealInfo.getWorkCertInfo().getIssuerDn() != null
					&& !dealInfo.getWorkCertInfo().getIssuerDn().equals("")) {

				if (!dealInfo.getDealInfoStatus().equals("7")) {
					json.put("isOK", 0);
				} else {
					json.put("isOK", 1);
				}
			} else {
				json.put("isOK", 0);
			}

			if (isUserInteger > 0) {
				json.put("isUser", 1);
			} else {
				json.put("isUser", 0);
			}
			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				json.put("status", 0);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return json.toString();
	}
	
	
	@RequestMapping(value = "findByKeySnFailInstall")
	@ResponseBody
	public String findByKeySnFailInstall(String keySn, Long dealId) {
		JSONObject json = new JSONObject();
		try {
			Integer isUserInteger = workDealInfoService.findByKey(keySn,dealId);
			WorkDealInfo dealInfo = workDealInfoService.get(dealId);
			if (dealInfo.getWorkCertInfo().getIssuerDn() != null
					&& !dealInfo.getWorkCertInfo().getIssuerDn().equals("")) {

				if (!dealInfo.getDealInfoStatus().equals("7")) {
					json.put("isOK", 0);
				} else {
					json.put("isOK", 1);
				}
			} else {
				json.put("isOK", 0);
			}

			if (isUserInteger > 0) {
				json.put("isUser", 0);
			} else {
				json.put("isUser", 1);
				json.put("dealKeySn", dealInfo.getKeySn());
			}
			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				json.put("status", 0);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return json.toString();
	}

	
	

	@RequestMapping(value = "findById")
	@ResponseBody
	public String findById(Long dealInfoId) {
		JSONObject json = new JSONObject();
		try {

			WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
			Date now = new Date();
			Date certAfter = dealInfo.getNotafter();
			Long nowLong = now.getTime();
			Long certAfterLong = certAfter.getTime();
			if ((nowLong - certAfterLong) > 0) {
				json.put("isDeal", 1);
			} else {
				json.put("isDeal", 0);
			}
			if ((certAfterLong - nowLong) <= (1000 * 60 * 60 * 24 * 60L)) {
				json.put("isUpdate", 1);
			} else {
				json.put("isUpdate", 0);
			}
			if(dealInfo.getIsMainTain()!=null){
				if(dealInfo.getIsMainTain().equalsIgnoreCase("selfMainTain")){
					json.put("isMainTain", 1);
				}
			}else{
				json.put("isMainTain", 0);
			}
			
			json.put("status", 1);
		} catch (Exception e) {
			try {
				json.put("status", 0);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return json.toString();
	}

	@RequestMapping(value = "checkUpdateByIds")
	@ResponseBody
	public String checkUpdateByIds(String dealInfoIds) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			String[] dealInfos = dealInfoIds.split(",");
			List<Long> dealIdList = new ArrayList<Long>();
			for (int i = 0; i < dealInfos.length; i++) {
				if (!dealInfos[i].equals("")) {
					dealIdList.add(Long.parseLong(dealInfos[i]));
				}

			}
			
			
			
			List<ConfigProduct> productNames = workDealInfoService.findByDistinkIds(dealIdList);
			if (productNames.size()>1) {
				json.put("status", 1);
				json.put("isUpdate", 0);
				
				String html = "选中的业务不为同一个产品，请检查！";
				json.put("html", html);
				return json.toString();
			}
			
			
			
			String html = "";
			List<String> companyNames = new ArrayList<>();
			for (int i = 0; i < dealInfos.length; i++) {
				if (dealInfos[i].equals("1") || dealInfos[i].equals("")) {
					continue;
				}
				Long dealInfoId = Long.parseLong(dealInfos[i]);

				WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
				if (dealInfo == null) {
					continue;
				}
				Date now = new Date();
				Date certAfter = dealInfo.getNotafter();
				Long nowLong = now.getTime();
				Long certAfterLong = certAfter.getTime();
				String tip = "";
				if ((certAfterLong - nowLong) > (1000 * 60 * 60 * 24 * 60L)) {
					tip = "'" + dealInfo.getWorkCompany().getCompanyName() + "'单位的证书未在更新范围内！";
					companyNames.add(tip);
				} 

			}
			for (int i = 0; i < companyNames.size(); i++) {
				html += companyNames.get(i);
				if (i < (companyNames.size() - 1)) {
					html += "<br>&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}
			if (html.equals("")) {
				json.put("isUpdate", 1);
				
				Long proIdd = productNames.get(0).getId();
				List<ConfigChargeAgentBoundConfigProduct> bounds =  configChargeAgentBoundConfigProductService.findByProductId(proIdd);
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < bounds.size(); i++) {
					nameSet.add(Integer.parseInt(bounds.get(i).getAgent().getTempStyle()));
				}
				json.put("boundLabelList", nameSet);
				
				json.put("appIdd",  productNames.get(0).getConfigApp().getId());
				json.put("productNamee",  productNames.get(0).getProductName());
				json.put("updateSize", dealIdList.size());
				json.put("labell", productNames.get(0).getProductLabel());
				
			} else {
				json.put("isUpdate", 0);
				json.put("html", html);
			}
			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 0);
		}
		return json.toString();
	}

	@RequestMapping(value = "checkYears")
	@ResponseBody
	public String checkYears(String dealInfoIds, String year) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			String[] dealInfos = dealInfoIds.split(",");
			String html = "";
			List<String> companyNames = new ArrayList<>();
			for (int i = 0; i < dealInfos.length; i++) {
				if (dealInfos[i].equals("1") || dealInfos[i].equals("")) {
					continue;
				}
				Long dealInfoId = Long.parseLong(dealInfos[i]);
				WorkDealInfo dealInfo = workDealInfoService.get(dealInfoId);
				ConfigChargeAgentDetail detail = configChargeAgentDetailService
						.getAgentDetail(dealInfo.getConfigChargeAgentId(), 1, Integer.parseInt(year));
				String tip = "";
				if (detail == null) {
					ConfigChargeAgent configChargeAgent = configChargeAgentService
							.get(dealInfo.getConfigChargeAgentId());
					tip = "'" + dealInfo.getWorkCompany().getCompanyName() + "'单位绑定'" + configChargeAgent.getTempName()
							+ "'缴费模板没有" + year + "年更新配置！";
					companyNames.add(tip);
				}
			}
			for (int i = 0; i < companyNames.size(); i++) {

				html += companyNames.get(i);
				if (i < (companyNames.size() - 1)) {
					html += "<br>&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}
			if (html.equals("")) {
				json.put("isYes", 1);
			} else {
				json.put("isYes", 0);
				json.put("html", html);
			}
			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", 0);
		}
		return json.toString();
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "cancelMaintenance")
	public String cancelMaintenance(Long id, Integer type, Model model, RedirectAttributes redirectAttributes) {
		try {
			WorkDealInfo dealinfo = workDealInfoService.get(id);
			Long certId = dealinfo.getWorkCertInfo().getId();
			Long agnetId = dealinfo.getConfigChargeAgentId();
			Long dealPreId = dealinfo.getPrevId();
			if (dealinfo.getDealInfoType()!=null && dealinfo.getDealInfoType().equals(1)) {
				ConfigChargeAgent agentOri = configChargeAgentService.get(agnetId);
				agentOri.setReserveUpdateNum(agentOri.getReserveUpdateNum() - 1);
				agentOri.setSurplusUpdateNum(agentOri.getSurplusUpdateNum() + 1);
				configChargeAgentService.save(agentOri);
				ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agnetId, id);
				if (bound != null) {
					configAgentBoundDealInfoService.deleteById(bound.getId());
				}
			}



			workDealInfoService.deleteWork(id);
			workCertInfoService.delete(certId);
			workDealInfoService.deleteReturnById(dealPreId);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		logUtil.saveSysLog("业务中心", "删除业务：编号" + id + "成功", "");
		model.addAttribute("message", "取消业务编号为" + id + "业务成功！");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "cancelMaintenanceByReturn")
	public String cancelMaintenanceByReturn(Long id, Model model, RedirectAttributes redirectAttributes) {
		try {

			WorkDealInfo dealInfo = workDealInfoService.get(id);
			List<Integer> dealInfoList = new ArrayList<Integer>();
			if (dealInfo.getDealInfoType() != null) {
				dealInfoList.add(dealInfo.getDealInfoType());
			}
			if (dealInfo.getDealInfoType1() != null) {
				dealInfoList.add(dealInfo.getDealInfoType1());
			}
			if (dealInfo.getDealInfoType2() != null) {
				dealInfoList.add(dealInfo.getDealInfoType2());
			}
			boolean isOut = false;
			if (dealInfoList.size() == 1) {
				if (dealInfoList.get(0).equals(1)) {
					isOut = true;
				} else if (dealInfoList.get(0).equals(4)) {
					isOut = true;
				}
			} else if (dealInfoList.size() == 2) {
				if (dealInfoList.get(0).equals(1) && dealInfoList.get(1).equals(4)) {
					isOut = true;
				}
			}

			if (isOut) {
				WorkPayInfo payInfo = dealInfo.getWorkPayInfo();
				Set<WorkFinancePayInfoRelation> relations = payInfo.getWorkFinancePayInfoRelations();
				if (relations.size() != 0) {
					for (WorkFinancePayInfoRelation relation : relations) {// 退费
						FinancePaymentInfo financePaymentInfo = relation.getFinancePaymentInfo();
						financePaymentInfo.setBingdingTimes(financePaymentInfo.getBingdingTimes() - 1);
						financePaymentInfo.setResidueMoney(financePaymentInfo.getResidueMoney() + relation.getMoney());// 返还金额
						financePaymentInfoService.save(financePaymentInfo);
						workFinancePayInfoRelationService.delete(relation.getId());
					}
				}
				Double money = dealInfo.getWorkPayInfo().getReceiptAmount();

				if (money > 0d) {
					ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService
							.findDepotByOffice(dealInfo.getCreateBy().getOffice()).get(0);
					// 修改余额
					receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue() + money);
					receiptDepotInfo.setReceiptTotal(receiptDepotInfo.getReceiptTotal() + money);

					// 创建入库信息
					ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
					receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
					receiptEnterInfo.setNow_Money(Double.valueOf(money));
					receiptEnterInfo.setBeforMoney(
							receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue() - Double.valueOf(money));
					receiptEnterInfo.setReceiptMoney(receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue());
					receiptEnterInfo.setReceiptType(4);// 退费入库
					receiptEnterInfoService.save(receiptEnterInfo);

					logUtil.saveSysLog("更新业务办理重新缴费", "库房" + receiptDepotInfo.getReceiptName() + "添加入库信息成功", "");
					receiptDepotInfoService.save(receiptDepotInfo);
				}

			}

			workPayInfoService.delete(dealInfo.getWorkPayInfo().getId());
			dealInfo.setWorkPayInfo(null);
			dealInfo.setDealInfoStatus("5");
			workDealInfoService.save(dealInfo);

			// private Integer configureNum;//配置数量
			// private Integer surplusNum;//剩余数量
			// private Integer availableNum;//已用数量
			// private Integer reserveNum;//预留数量

			if (dealInfo.getDealInfoType() != null && dealInfo.getDealInfoType().equals(1)) {
				ConfigChargeAgent agentOri = configChargeAgentService.get(dealInfo.getConfigChargeAgentId());
				agentOri.setAvailableUpdateNum(agentOri.getAvailableUpdateNum() - 1);
				agentOri.setSurplusUpdateNum(agentOri.getSurplusUpdateNum() + 1);
				configChargeAgentService.save(agentOri);
				ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agentOri.getId(),
						id);
				if (bound != null) {
					configAgentBoundDealInfoService.deleteById(bound.getId());
				}
			}

			WorkDealInfo dealinfo = workDealInfoService.get(id);
			Long certId = dealinfo.getWorkCertInfo().getId();
			Long dealPreId = dealinfo.getPrevId();
			workDealInfoService.deleteWork(id);
			workCertInfoService.delete(certId);
			workDealInfoService.deleteReturnById(dealPreId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		logUtil.saveSysLog("业务中心", "删除业务：编号" + id + "成功", "");
		model.addAttribute("message", "取消业务编号为" + id + "业务成功！");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}

	@RequestMapping(value = "updateDealInfos")
	public String updateDealInfos(String dealInfoIds, Integer year,Long bounddId,String methodPay) {
		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(bounddId);
		ConfigChargeAgent agent = bound.getAgent();
		
		
		
		
		String[] infoIds = dealInfoIds.split(",");
		for (int i = 0; i < infoIds.length; i++) {
			try {
				Long infoId = Long.parseLong(infoIds[i]);
				WorkDealInfo workDealInfo1 = workDealInfoService.get(infoId);
				if (workDealInfo1.getDelFlag().equals("1")) {
					continue;
				}

				WorkCompanyHis companyHis = null;
				// 保存经办人信息
				WorkUser workUser = workDealInfo1.getWorkUser();
				WorkUserHis userHis = workUserService.change(workUser, companyHis);
				workUserHisService.save(userHis);

				// 变更业务保存单位信息
				WorkCompany workCompany = workDealInfo1.getWorkCompany();
				companyHis = workCompanyService.change(workCompany);
				workCompanyHisService.save(companyHis);

				// workDealInfo1
				WorkDealInfo workDealInfo = new WorkDealInfo();
				workDealInfo.setConfigApp(workDealInfo1.getConfigApp());
				ConfigCommercialAgent commercialAgent = configAgentAppRelationService
						.findAgentByApp(workDealInfo.getConfigApp());
				workDealInfo.setConfigCommercialAgent(commercialAgent);
				List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
						.findByOffice(UserUtils.getUser().getOffice());
				if (configAgentOfficeRelations.size() > 0) {
					workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0).getConfigCommercialAgent());// 劳务关系外键
				}

				workDealInfo.setPayType(Integer.parseInt(agent.getTempStyle()));
				
				Integer reseUpdateNum = agent.getReserveUpdateNum();
				Integer surUpdateNum = agent.getSurplusUpdateNum();
				agent.setReserveUpdateNum(reseUpdateNum + 1);
				agent.setSurplusUpdateNum(surUpdateNum - 1);
				configChargeAgentService.save(agent);
				workDealInfo.setConfigChargeAgentId(agent.getId());
				workDealInfo.setWorkUser(workUser);
				workDealInfo.setWorkCompany(workCompany);
				workDealInfo.setWorkUserHis(userHis);
				workDealInfo.setWorkCompanyHis(companyHis);
				workDealInfo.setConfigProduct(workDealInfo1.getConfigProduct());
				workDealInfo.setYear(year);
				workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
				workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
				workDealInfo.setCreateBy(UserUtils.getUser());
				workDealInfo.setCreateDate(new Date());
				workDealInfo.setClassifying(workDealInfo1.getClassifying());
				workDealInfo.setSvn(workDealInfoService.getSVN(0));
				workDealInfo.setPrevId(workDealInfo1.getId());
				if (workDealInfo1.getWorkCertInfo().getNotafter().after(new Date())) {
					int day = getLastCertDay(workDealInfo1.getWorkCertInfo().getNotafter());
					workDealInfo.setLastDays(day);
				} else {
					workDealInfo.setLastDays(0);
				}
				WorkCertApplyInfo workCertApplyInfo = workDealInfo1.getWorkCertInfo().getWorkCertApplyInfo();
				workCertApplyInfoService.save(workCertApplyInfo);
				WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
				WorkCertInfo workCertInfo = new WorkCertInfo();
				workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
				workCertInfo.setRenewalPrevId(oldCertInfo.getId());
				workCertInfo.setCreateDate(workCertInfoService.getCreateDate(oldCertInfo.getId()));
				workCertInfoService.save(workCertInfo);
				// 给上张证书存nextId
				oldCertInfo.setRenewalNextId(workCertInfo.getId());
				workCertInfoService.save(oldCertInfo);
				workDealInfo.setWorkCertInfo(workCertInfo);
				workDealInfoService.delete(workDealInfo1.getId());

				workDealInfo.setInputUser(UserUtils.getUser());
				workDealInfo.setInputUserDate(new Date());
				workDealInfoService.save(workDealInfo);

				ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
				dealInfoBound.setDealInfo(workDealInfo);
				dealInfoBound.setAgent(agent);
				configAgentBoundDealInfoService.save(dealInfoBound);
				logUtil.saveSysLog("计费策略模版", "计费策略模版：" + agent.getId() + "--业务编号：" + workDealInfo.getId() + "--关联成功!",
						"");

				// 保存日志信息
				WorkLog workLog = new WorkLog();
				workLog.setRecordContent("批量更新");
				workLog.setWorkDealInfo(workDealInfo);
				workLog.setCreateDate(new Date());
				workLog.setCreateBy(UserUtils.getUser());
				workLog.setConfigApp(workDealInfo.getConfigApp());
				workLog.setWorkCompany(workDealInfo.getWorkCompany());
				workLog.setOffice(UserUtils.getUser().getOffice());
				workLogService.save(workLog);

				WorkPayInfo workPayInfo = new WorkPayInfo();
				workPayInfo.setOpenAccountMoney(0d);
				workPayInfo.setAddCert(0d);

				Double money = configChargeAgentDetailService.getChargeMoney(agent.getId(), 1,
						year);
			
				if (methodPay.equals("0")) {
					workPayInfo.setMethodPos(true);
				}else if (methodPay.equals("1")) {
					workPayInfo.setMethodMoney(true);
				}else if (methodPay.equals("2")) {
					workPayInfo.setMethodBank(true);
				}
				
				if (agent.getTempStyle().equals("2")) {
					workPayInfo.setMethodGov(true);
				}else if(agent.getTempStyle().equals("3")){
					workPayInfo.setMethodContract(true);
				}else{
					workPayInfo.setMethodGov(false);
					workPayInfo.setMethodContract(false);
				}
				
				workPayInfo.setUpdateCert(money);
				workPayInfo.setErrorReplaceCert(0d);
				workPayInfo.setLostReplaceCert(0d);
				workPayInfo.setInfoChange(0d);
				workPayInfo.setElectron(0d);
				workPayInfo.setOldOpenAccountMoney(0d);
				workPayInfo.setOldAddCert(0d);
				workPayInfo.setOldUpdateCert(0d);
				workPayInfo.setOldErrorReplaceCert(0d);
				workPayInfo.setOldLostReplaceCert(0d);
				workPayInfo.setOldInfoChange(0d);
				workPayInfo.setOldElectron(0d);

				double bindMoney = money;
				workPayInfo.setMoney(bindMoney);

				workPayInfo.setWorkTotalMoney(money);
				workPayInfo.setWorkPayedMoney(money);
				workPayInfo.setWorkReceivaMoney(money);
				workPayInfo.setUserReceipt(true);
				workPayInfo.setReceiptAmount(money);
				workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
				workPayInfoService.save(workPayInfo);

				workDealInfo.setWorkPayInfo(workPayInfo);

				workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_WAIT);

				workDealInfoService.checkWorkDealInfoNeedSettle(workDealInfo);

				if (workDealInfo.getDealInfoType().equals(1)) {
					ConfigChargeAgent agentAfter = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
					agent.setAvailableUpdateNum(agentAfter.getAvailableUpdateNum() + 1);// 已用数量
					agent.setReserveUpdateNum(agentAfter.getReserveUpdateNum() - 1);// 预留数量
					configChargeAgentService.save(agent);
					logUtil.saveSysLog("计费策略模版", "更改剩余数量和使用数量成功!", "");
				}

				workDealInfo.setPayUser(UserUtils.getUser());
				workDealInfo.setPayUserDate(new Date());

				workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
				workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());

				workDealInfoService.save(workDealInfo);
				ConfigRaAccount raAccount = raAccountService.get(workDealInfo.getConfigProduct().getRaAccountId());
				List<String[]> list = RaAccountUtil.outPageLine(workDealInfo, raAccount.getConfigRaAccountExtendInfo());

				logUtil.saveSysLog("业务中心", "业务维护：编号" + workDealInfo.getId() + "缴费" + money + "元", "");
				ReceiptInvoice receiptInvoice = new ReceiptInvoice();
				Office office = workDealInfo.getCreateBy().getOffice();
				List<ReceiptDepotInfo> depotInfos = receiptDepotInfoService.findDepotByOffice(office);
				receiptInvoice.setReceiptDepotInfo(depotInfos.get(0));
				receiptInvoice.setCompanyName(workDealInfo.getWorkCompany().getCompanyName());
				receiptInvoice.setReceiptMoney(workDealInfo.getWorkPayInfo().getReceiptAmount());
				receiptInvoice.setReceiptType(0);// 销售出库
				receiptInvoice.setDealInfoId(workDealInfo.getId());
				receiptInvoiceService.save(receiptInvoice);
				ReceiptDepotInfo receiptDepotInfo = depotInfos.get(0);
				receiptDepotInfo.setReceiptResidue(
						receiptDepotInfo.getReceiptResidue() - workDealInfo.getWorkPayInfo().getReceiptAmount());
				receiptDepotInfo.setReceiptOut(
						receiptDepotInfo.getReceiptOut() + workDealInfo.getWorkPayInfo().getReceiptAmount());
				receiptDepotInfoService.save(receiptDepotInfo);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}

	@RequestMapping(value = "returnDZZ")
	public String returnDZZ(Long dealId) {
		WorkDealInfo dealInfo = workDealInfoService.get(dealId);
		dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_UPDATE_USER);
		workDealInfoService.save(dealInfo);

		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
	}

}
