/**

 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
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
import java.util.TreeMap;
import java.util.TreeSet;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.AdminPinEncKey;
import com.itrus.ca.common.utils.Base64ImageTransform;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.utils.excel.ExportExcel;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.log.service.LogUtil;
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
import com.itrus.ca.modules.self.entity.SelfArea;
import com.itrus.ca.modules.self.entity.SelfImage;
import com.itrus.ca.modules.self.service.SelfAreaService;
import com.itrus.ca.modules.self.service.SelfImageService;
import com.itrus.ca.modules.service.ItrustProxyUtil;
import com.itrus.ca.modules.sys.entity.CommonAttach;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.Role;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.security.SystemAuthorizingRealm;
import com.itrus.ca.modules.sys.service.CommonAttachService;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.task.FixSVNThread;

import com.itrus.ca.modules.task.entity.BatchUpdateInfoScca;
import com.itrus.ca.modules.task.service.BatchUpdateInfoSccaService;
import com.itrus.ca.modules.work.entity.MonthPayment;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.entity.WorkDealCountStatistics;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfoExpView;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.entity.WorkUserHis;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoExpViewService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkFinancePayInfoRelationService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;
import com.itrus.ca.modules.work.vo.ReceivedPaymentsVo;
import com.itrus.ca.modules.work.vo.WorkDate_MoneVo;
import com.itrus.ca.modules.work.vo.WorkDealInfoExpViewVO;
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
	
	private static Logger log = LoggerFactory.getLogger(WorkDealInfoController.class);

/*	@Value(value = "${imgFileUpload.path}")
	private String imgFileUpload;*/
	
	@Autowired
	private CommonAttachService attachService;
	
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

	@Autowired
	private WorkDealInfoExpViewService workDealInfoExpViewService;
	
	@Autowired
	private SelfAreaService selfAreaService;
	
	@Autowired
	private BatchUpdateInfoSccaService batchUpdateInfoSccaService;

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
	public String list(
			WorkDealInfo workDealInfo,
			Date startTime,
			Date endTime,
			Date makeCertStartTime,
			Date makeCertEndTime,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "isSelectedAll", required = false) Integer isSelectedAll,
			@RequestParam(value = "isSelectButton", required = false) Integer isSelectButton,
			@RequestParam(value = "alias", required = false) Long alias,
			@RequestParam(value = "productName", required = false) String productName)
			throws ParseException {

		long start = System.currentTimeMillis();
		
		
		
		User user = UserUtils.getUser();
		System.out.print(user.getName());
		workDealInfo.setCreateBy(user.getCreateBy());
		
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("alias", alias);

		Page<WorkDealInfoListVo> page = workDealInfoService.find4Apply(
				new Page<WorkDealInfoListVo>(request, response), workDealInfo,
				startTime, endTime, alias, productName, makeCertStartTime,
				makeCertEndTime);

		List<WorkDealInfoListVo> vos = page.getList();

		for (WorkDealInfoListVo vo : vos) {
			if (vo.getNotafter() != null) {
				if (StringHelper.getDvalueDay(new Date(), vo.getNotafter()) <= 60) {
					vo.setCanUpdate(1);
				} else {
					vo.setCanUpdate(0);
				}
			} else {
				vo.setCanUpdate(0);
			}

		}

		page.setList(vos);

		if (checkIds != null) {
			String[] ids = checkIds.split(",");
			model.addAttribute("ids", ids);
		}
		model.addAttribute("checkIds", checkIds);

		
		if(isSelectButton!=null&&isSelectButton==1){
			model.addAttribute("isSelectedAll", 0);
		}else{
			model.addAttribute("isSelectedAll", isSelectedAll);	
		}
		
		

		model.addAttribute("workType", workDealInfo.getDealInfoStatus());

		model.addAttribute("proList", ProductType.getProductList());

		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("page", page);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("productName", productName);
		model.addAttribute("makeCertStartTime", makeCertStartTime);
		model.addAttribute("makeCertEndTime", makeCertEndTime);
		// 经信委
		model.addAttribute("expirationDate",
				StringHelper.getLastDateOfCurrentYear());
		
		long end = System.currentTimeMillis();
		
		log.debug("workDealInfoList用时:"+(end-start)+"ms");
		
		return "modules/work/workDealInfoList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "deleteList")
	public String deleteList(
			WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "isSelectedAll", required = false) Integer isSelectedAll,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agentId", required = false) Long agentId) {
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());

		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);

		Page<WorkDealInfo> page = workDealInfoService.findByBatchAdd(
				new Page<WorkDealInfo>(request, response), workDealInfo,
				startTime, endTime, agentId);

		if (checkIds != null) {
			String[] ids = checkIds.split(",");
			model.addAttribute("ids", ids);
		}

		List<ConfigChargeAgent> agents = configChargeAgentService.findAll();

		model.addAttribute("agents", agents);
		model.addAttribute("agentId", agentId);
		model.addAttribute("checkIds", checkIds);
		model.addAttribute("isSelectedAll", isSelectedAll);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("workType", workDealInfo.getDealInfoStatus());
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("page", page);
		return "modules/work/workDealInfoDeleteList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "deleteByDealInfoId")
	public String deleteList(Long dealInfoId, Model model,
			RedirectAttributes redirectAttributes) {

		WorkDealInfo workDealInfo = workDealInfoService.get(dealInfoId);
		ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo
				.getConfigChargeAgentId());
		agent.setSurplusNum(agent.getSurplusNum() + 1);
		agent.setReserveNum(agent.getReserveNum() - 1);
		configChargeAgentService.save(agent);
		ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService
				.findByAgentIdDealId(agent.getId(), dealInfoId);
		if (bound != null) {
			configAgentBoundDealInfoService.deleteById(bound.getId());
		}

		workDealInfoService.delete(dealInfoId);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/deleteList";
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
				WorkDealInfo workDealInfo = workDealInfoService.get(dealIdList
						.get(i));
				ConfigChargeAgent agent = configChargeAgentService
						.get(workDealInfo.getConfigChargeAgentId());
				agent.setSurplusNum(agent.getSurplusNum() + 1);
				agent.setReserveNum(agent.getReserveNum() - 1);
				configChargeAgentService.save(agent);
				ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService
						.findByAgentIdDealId(agent.getId(), dealIdList.get(i));
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
	public String importFile(
			@RequestParam(value = "appId", required = true) Long appId,
			@RequestParam(value = "dealInfoTypeT", required = true) String dealInfoType,
			@RequestParam(value = "productT", required = true) String productT,
			@RequestParam(value = "lableT", required = true) String lableT,
			@RequestParam(value = "agentIdT", required = true) Integer agentIdT,
			@RequestParam(value = "agentDetailIdT", required = true) Long agentDetailIdT,
			@RequestParam(value = "yearT", required = true) Integer yearT,
			@RequestParam(value = "payMethodT", required = true) Integer payMethodT,
			@RequestParam(value = "fileName", required = true) MultipartFile file)
			throws IllegalStateException, IOException, JSONException {
		JSONObject json = new JSONObject();
		String ifExcel = file.getOriginalFilename().substring(
				file.getOriginalFilename().indexOf("."));// 截取.xlsx或.xls
		if (!(ifExcel.equals(".xls") || ifExcel.equals(".xlsx"))) {
			json.put("status", -1);
			json.put("msg", "模板必须为Excel文件");
			return json.toString();
		}
		try {
			json = workDealInfoService.saveExcelDate(appId, dealInfoType,
					productT, lableT, agentIdT, agentDetailIdT, yearT,
					payMethodT, file, ifExcel);// 解析存储excel文件
		} catch (Exception ex) {
			json.put("status", -1);
			json.put("msg", json.toString());
			return json.toString();
		}
		return json.toString();
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "financeList")
	public String listFinancePaymentInfo(
			FinancePaymentInfo financePaymentInfo,
			String appName,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			HttpServletRequest request, HttpServletResponse response,
			Long financePaymentInfoId, Model model) {

		List<ConfigApp> appNames = configAppService.findall();
		Page<WorkFinancePayInfoRelation> financePay = workFinancePayInfoRelationService
				.findByFinance(new Page<WorkFinancePayInfoRelation>(request,
						response), financePaymentInfoId, appName, startTime,
						endTime);
		if (financePay.getCount() != 0) {
			List<Long> idList = Lists.newArrayList();
			if (financePay.getList().size() > 0) {
				for (int i = 0; i < financePay.getList().size(); i++) {
					idList.add(financePay.getList().get(i).getWorkPayInfo()
							.getId());
				}
			}

			Page<WorkDealInfo> page = workDealInfoService.findByFinanceId(
					new Page<WorkDealInfo>(request, response), idList);
			for (int i = 0; i < page.getList().size(); i++) {
				List<WorkFinancePayInfoRelation> financePayOne = workFinancePayInfoRelationService
						.findByFinancePay(financePaymentInfoId, page.getList()
								.get(i).getWorkPayInfo().getId());

				page.getList().get(i)
						.setMoneyDouble(financePayOne.get(0).getMoney());

				int proName = Integer.parseInt(page.getList().get(i)
						.getConfigProduct().getProductName());
				String proNames = ProductType.getProductTypeName(proName);
				int proLabel = page.getList().get(i).getConfigProduct()
						.getProductLabel();
				if (proLabel == 1) {
					proNames += "(专用)";
				} else {
					proNames += "(通用)";
				}

				page.getList().get(i).setCertSn(proNames);

				model.addAttribute("pro", ProductType.productTypeStrMap);
				model.addAttribute("infoType",
						WorkDealInfoType.WorkDealInfoTypeMap);

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
	public String StatisticalDealPayList(
			WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "payMethod", required = false) String payMethod,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "appId", required = false) Long appId,
			Model model) {
		User user = UserUtils.getUser();

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

			workDealInfo.setWorkPayInfo(workPayInfo);
		}

		List<Long> officeids = Lists.newArrayList();
		Map<Long, Set<String>> Id_paymethod = new HashMap<Long, Set<String>>();
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);
		} else {
			if (area != null) {

				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}

		Page<WorkDealInfo> page = workDealInfoService.findByDealPay(
				new Page<WorkDealInfo>(request, response), workDealInfo,
				startTime, endTime, appId, officeids);
		if (page.getList().size() > 0) {
			for (int i = 0; i < page.getList().size(); i++) {
				if (page.getList().get(i).getWorkPayInfo().getRelationMethod() != null) {
					List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
							.findByPayInfoId(page.getList().get(i)
									.getWorkPayInfo().getId());
					Set<String> payMethods = new LinkedHashSet<String>();
					for (int w = 0; w < workfinancePayinfos.size(); w++) {
						if (workfinancePayinfos.get(w).getFinancePaymentInfo()
								.getPaymentMethod() == 1) {
							payMethods.add("现金付款  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo()
								.getPaymentMethod() == 2) {
							payMethods.add("POS付款  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo()
								.getPaymentMethod() == 3) {
							payMethods.add("银行转账  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo()
								.getPaymentMethod() == 4) {
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
			
			List<WorkDealInfo> countList = workDealInfoService.findByDealPay(
					 workDealInfo,
					startTime, endTime, appId, officeids);

			if (countList.size() > 0) {
				for (int i = 0; i < countList.size(); i++) {
					if (countList.get(i).getWorkPayInfo()
							.getRelationMethod() == null) {
						if (countList.get(i).getWorkPayInfo()
								.getMethodPos() == true) {
							posCount += 1;
							if (countList.get(i).getWorkPayInfo()
									.getPosMoney() != null) {
								if (countList.get(i).getWorkPayInfo()
										.getPosMoney() > 0) {
									posCountMoney += countList.get(i)
											.getWorkPayInfo().getPosMoney();
								} else {
									posCountMoney += (-countList.get(i)
											.getWorkPayInfo().getPosMoney());
								}
							}
						}
						if (countList.get(i).getWorkPayInfo()
								.getMethodBank() == true) {
							bankCount += 1;
							if (countList.get(i).getWorkPayInfo()
									.getBankMoney() != null) {
								if (countList.get(i).getWorkPayInfo()
										.getBankMoney() > 0) {
									bankCountMoney += countList.get(i)
											.getWorkPayInfo().getBankMoney();
								} else {
									bankCountMoney += (-countList.get(i)
											.getWorkPayInfo().getBankMoney());
								}
							}
						}
						if (countList.get(i).getWorkPayInfo()
								.getMethodMoney() == true) {
							moneyCount += 1;
							if (countList.get(i).getWorkPayInfo()
									.getMoney() != null) {
								if (countList.get(i).getWorkPayInfo()
										.getMoney() > 0) {
									moneyCountMoney += countList.get(i)
											.getWorkPayInfo().getMoney();
								} else {
									moneyCountMoney += (-countList.get(i)
											.getWorkPayInfo().getMoney());
								}
							}
						}
						if (countList.get(i).getWorkPayInfo()
								.getMethodAlipay() == true) {
							alipayCount += 1;
							if (countList.get(i).getWorkPayInfo()
									.getAlipayMoney() != null) {
								if (countList.get(i).getWorkPayInfo()
										.getAlipayMoney() > 0) {
									alipayCountMoney += countList.get(i)
											.getWorkPayInfo().getAlipayMoney();
								} else {
									alipayCountMoney += (-countList.get(i)
											.getWorkPayInfo().getAlipayMoney());
								}
							}
						}
						if (countList.get(i).getWorkPayInfo()
								.getMethodGov() != null) {
							if (countList.get(i).getWorkPayInfo()
									.getMethodGov() == true) {
								govCount += 1;
							}
						}
						if (countList.get(i).getWorkPayInfo()
								.getMethodContract() != null) {
							if (countList.get(i).getWorkPayInfo()
									.getMethodContract() == true) {
								contractCount += 1;
							}
						}
					} else {
						List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
								.findByPayInfoId(countList.get(i)
										.getWorkPayInfo().getId());
						Set<String> payMethods = new LinkedHashSet<String>();
						for (int w = 0; w < workfinancePayinfos.size(); w++) {
							if (workfinancePayinfos.get(w)
									.getFinancePaymentInfo().getPaymentMethod() == 1) {
								payMethods.add("现金付款");
								// moneyCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									if (workfinancePayinfos.get(w).getMoney() > 0) {
										moneyCountMoney += workfinancePayinfos
												.get(w).getMoney();
									} else {
										moneyCountMoney += (-workfinancePayinfos
												.get(w).getMoney());
									}
								}
							}
							if (workfinancePayinfos.get(w)
									.getFinancePaymentInfo().getPaymentMethod() == 2) {
								payMethods.add("POS付款");
								// posCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									if (workfinancePayinfos.get(w).getMoney() > 0) {
										posCountMoney += workfinancePayinfos
												.get(w).getMoney();
									} else {
										posCountMoney += (-workfinancePayinfos
												.get(w).getMoney());
									}
								}
							}
							if (workfinancePayinfos.get(w)
									.getFinancePaymentInfo().getPaymentMethod() == 3) {
								payMethods.add("银行转账");
								// bankCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									if (workfinancePayinfos.get(w).getMoney() > 0) {
										bankCountMoney += workfinancePayinfos
												.get(w).getMoney();
									} else {
										bankCountMoney += (-workfinancePayinfos
												.get(w).getMoney());
									}
								}
							}
							if (workfinancePayinfos.get(w)
									.getFinancePaymentInfo().getPaymentMethod() == 4) {
								payMethods.add("支付宝付款");
								// alipayCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									if (workfinancePayinfos.get(w).getMoney() > 0) {
										alipayCountMoney += workfinancePayinfos
												.get(w).getMoney();
									} else {
										alipayCountMoney += (-workfinancePayinfos
												.get(w).getMoney());
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
						Id_paymethod.put(countList.get(i).getId(),
								payMethods);
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
			String dealMsg = (startTime != null && endTime != null ? sdf
					.format(startTime) + "至" + sdf.format(endTime) : "本次统计")
					+ areaOffice.getName()
					+ "区域"
					+ offOffice.getName()
					+ "现金"
					+ moneyCountMoney
					+ "元，POS收款"
					+ posCountMoney
					+ "元，银行转账"
					+ bankCountMoney + "元，支付宝转账" + alipayCountMoney + "元";
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
		} else if (officeId != null) {
			model.addAttribute("office", officeId);
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
	public String StatisticalDayList(
			WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office,
			Model model) throws ParseException {
		User user = UserUtils.getUser();
		
		
		//用于前台选择应用使用
		List<ConfigApp> configApps = UserUtils.getAppList();
		//用于后台循环展示使用
		List<ConfigApp> configAppList = Lists.newArrayList();
		
		if(appId == null){
			configAppList = UserUtils.getAppList();
		}else{
			configAppList.add(configAppService.findByAppId(appId));
		}
		//用于前台选择网点使用
		List<Office> officeList = Lists.newArrayList();
		//用于后台循环展示使用
		List<Long> officeids = UserUtils.getOfficeIdList(area, office, officeList);
		
		if(!(area==null&&office==null)){
			model.addAttribute("offices", officeList);
		}
		
		//前台展示的最外层循环体
		List<ReceivedPaymentsVo> paymentsVos = Lists.newArrayList();
		
		
		//如果起始时间和结束时间都为空,则默认查询当天数据
		if(startTime==null&&endTime==null){
			startTime = new Date();
			endTime = new Date();
		}
		
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
				
		
		//循环所有应用
		for(ConfigApp configapp: configAppList){
			List<MonthPayment> list = workDealInfoService.findByDayPay(startTime, endTime, officeids, configapp.getId());
			if (list != null&& list.size()>0) {
				
				ReceivedPaymentsVo paymentsVo = new ReceivedPaymentsVo();
				Map<String, List<String>> office_payMethod = new HashMap<>();
				
				List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
				List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
				Set<String> month = new LinkedHashSet<String>();
				for (int i = 0; i < list.size(); i++) {
					String a = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getCreateDate());
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
						
						for (int i = 0; i < list.size(); i++) {
							String mo = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getCreateDate());
							if (list.get(i).getOfficeId().equals(offs[o]) && ((String) months[m]).indexOf(mo) != -1) {
								double postMoney = 0L;
								double bankMoney = 0L;
								double xjMoney = 0L;
								double alipayMoney = 0L;
								if (list.get(i).getRealtionMethod() == null) {
									if (list.get(i).getMethodPos() == 1) {
										if (list.get(i).getPosMoney() >= 0) {
											postMoney = list.get(i).getPosMoney();
										}/* else {
											postMoney = (-list.get(i).getPosMoney());
										}*/
									}
									if (list.get(i).getMethodBank() == 1) {
										if (list.get(i).getBankMoney() >= 0) {
											bankMoney = list.get(i).getBankMoney();
										} /*else {
											bankMoney = (-list.get(i).getBankMoney());
										}*/
									}
									if (list.get(i).getMethodMoney() == 1) {
										if (list.get(i).getMoney() >= 0) {
											xjMoney = list.get(i).getMoney();
										}/* else {
											xjMoney = (-list.get(i).getMoney());
										}*/

									}
									if (list.get(i).getMethodAlipay() == 1) {
										if (list.get(i).getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getAlipayMoney();
										} /*else {
											alipayMoney = (-list.get(i).getAlipayMoney());
										}*/

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i).getWorkPayInfoId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												}/* else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												}/* else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

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
						payMethods.add("现金支付");
					}
					if (alipay > 0) {
						for (int of = 0; of < o_m.size(); of++) {
							if (o_m.get(of).getOfficeName().equals(officeService.findById((Long) offs[ofs]).getName())) {
								o_m.get(of).setAlipayMoney(true);
							}
						}
						payMethods.add("支付宝付款");
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
					if (entry.getValue().indexOf("现金支付") != -1) {
						payMethodMoneys.add(xj);
					}
					if (entry.getValue().indexOf("支付宝付款") != -1) {
						payMethodMoneys.add(alipay);
					}
				}
				if(moneys==0d){
					continue;
				}
				
				payMethodMoneys.add(moneys);				
				
				paymentsVo.setDates(months);
				paymentsVo.setAppName(configapp.getAppName());
				paymentsVo.setOfficePayMethod(office_payMethod);
				paymentsVo.setOfficeMoneyVo(o_m);
				paymentsVo.setDateMoneyVo(w_m);
				paymentsVo.setPayMethodMoneys(payMethodMoneys);
				
				paymentsVos.add(paymentsVo);
				
				
			}
		}
		
		if (paymentsVos.isEmpty()) {
			model.addAttribute("message", "此查询条件没有数据");
		}
		
		model.addAttribute("receivedPayments", paymentsVos);
		model.addAttribute("area", area);
		model.addAttribute("appId", appId);
		model.addAttribute("officeId", office);

		List<Office> offsList = officeService.getOfficeByType(user, 1);
		model.addAttribute("offsList", offsList);
		
		model.addAttribute("appList", configApps);
		return "modules/work/statisticalDayList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "statisticalMonthList")
	public String statisticalMonthList(
			WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office,
			Model model) throws ParseException {
		//当前用户
		User user = UserUtils.getUser();
		
		Date start = null;
		Date end = null;
		if (startTime != null) {
			StringBuffer startt = new StringBuffer(startTime);
			startt.append("-1");
			
			start = new SimpleDateFormat("yyyy-MM-dd").parse(startt
						.toString());
		}
		if(endTime !=null){
			StringBuffer endtt = new StringBuffer(endTime);
			endtt.append("-31");
			end = new SimpleDateFormat("yyyy-MM-dd")
					.parse(endtt.toString());
		}
		
		
		//用于前台选择应用使用
		List<ConfigApp> configApps = UserUtils.getAppList();
		//用于后台循环展示使用
		List<ConfigApp> configAppList = Lists.newArrayList();
		if(appId == null){
			configAppList = UserUtils.getAppList();
		}else{
			configAppList.add(configAppService.findByAppId(appId));
		}
		//用于前台选择网点使用
		List<Office> officeList = Lists.newArrayList();
		//用于后台循环展示使用	
		List<Long> officeids = UserUtils.getOfficeIdList(area, office, officeList);
		
		if(!(area==null&&office==null)){
			model.addAttribute("offices", officeList);
		}
		
		//前台展示的最外层循环体
		List<ReceivedPaymentsVo> paymentsVos = Lists.newArrayList();
		
				
		//如果起始时间和结束时间都为空,则默认查询当月数据
		if(startTime==null&&endTime==null){
			startTime = new SimpleDateFormat("yyyy-MM").format(new Date());
			endTime = new SimpleDateFormat("yyyy-MM").format(new Date());
			StringBuffer startt = new StringBuffer(startTime);
			startt.append("-1");
			StringBuffer endtt = new StringBuffer(endTime);
			endtt.append("-31");
			start = new SimpleDateFormat("yyyy-MM-dd").parse(startt
					.toString());
			end = new SimpleDateFormat("yyyy-MM-dd")
					.parse(endtt.toString());
			
		}
				
		model.addAttribute("startTime", new SimpleDateFormat("yyyy-MM").parse(startTime));
		model.addAttribute("endTime", new SimpleDateFormat("yyyy-MM").parse(endTime));	
		
		//循环所有应用
		for(ConfigApp configapp: configAppList){
			List<MonthPayment> list = workDealInfoService.findByDayPay(start, end,officeids, configapp.getId());
			if (list != null&&list.size()>0) {
				
				ReceivedPaymentsVo paymentsVo = new ReceivedPaymentsVo();
				Map<String, List<String>> office_payMethod = new HashMap<>();
				
				List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
				List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
				Set<String> month = new LinkedHashSet<String>();
				for (int i = 0; i < list.size(); i++) {
					String a = new SimpleDateFormat("yyyy-MM").format(list.get(i).getCreateDate());
					month.add(a);
					
				}
				Set<Long> offices = new LinkedHashSet<Long>();
				if (officeids != null) {
					for (int i = 0; i < list.size(); i++) {
						
						if (list.get(i).getOfficeId() != null) {
							offices.add(list.get(i).getOfficeId());
						}
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
						
						for (int i = 0; i < list.size(); i++) {
							String mo = new SimpleDateFormat("yyyy-MM").format(list.get(i).getCreateDate());
							if (list.get(i).getOfficeId() != null) {

								if (list.get(i).getOfficeId().equals(offs[o])&& ((String) months[m]).indexOf(mo) != -1) {
									double postMoney = 0L;
									double bankMoney = 0L;
									double xjMoney = 0L;
									double alipayMoney = 0L;
									if (list.get(i).getRealtionMethod() == null) {
										if (list.get(i).getMethodPos() == 1) {
											if (list.get(i).getPosMoney() >= 0) {
												postMoney = list.get(i).getPosMoney();
											}/* else {
												postMoney = (-list.get(i).getPosMoney());
											}*/
										}
										if (list.get(i).getMethodBank() == 1) {
											if (list.get(i).getBankMoney() >= 0) {
												bankMoney = list.get(i).getBankMoney();
											} /*else {
												bankMoney = (-list.get(i).getBankMoney());
											}*/
										}
										if (list.get(i).getMethodMoney() == 1) {
											if (list.get(i).getMoney() >= 0) {
												xjMoney = list.get(i).getMoney();
											}/* else {
												xjMoney = (-list.get(i).getMoney());
											}*/

										}
										if (list.get(i).getMethodAlipay() == 1) {
											if (list.get(i).getAlipayMoney() >= 0) {
												alipayMoney = list.get(i).getAlipayMoney();
											}/* else {
												alipayMoney = (-list.get(i).getAlipayMoney());
											}*/

										}
									} else {
										List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService.findByPayInfoId(list.get(i).getWorkPayInfoId());
										for (int w = 0; w < workfinancePayinfos.size(); w++) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														xjMoney = workfinancePayinfos.get(w).getMoney();
													} /*else {
														xjMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														postMoney = workfinancePayinfos.get(w).getMoney();
													} /*else {
														postMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														bankMoney = workfinancePayinfos.get(w).getMoney();
													}/* else {
														bankMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														alipayMoney = workfinancePayinfos.get(w).getMoney();
													}/* else {
														alipayMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

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
				
				if(moneys==0d){
					continue;
				}
				
				payMethodMoneys.add(moneys);
				
				paymentsVo.setDates(months);
				paymentsVo.setAppName(configapp.getAppName());
				paymentsVo.setOfficePayMethod(office_payMethod);
				paymentsVo.setOfficeMoneyVo(o_m);
				paymentsVo.setDateMoneyVo(w_m);
				paymentsVo.setPayMethodMoneys(payMethodMoneys);
				
				paymentsVos.add(paymentsVo);
				

			}
			
		}
		

		if (paymentsVos.isEmpty()) {
			model.addAttribute("message", "此查询条件没有数据");
		}
		
		model.addAttribute("receivedPayments", paymentsVos);
		model.addAttribute("area", area);
		model.addAttribute("appId", appId);
		model.addAttribute("officeId", office);

		List<Office> offsList = officeService.getOfficeByType(user, 1);
		model.addAttribute("offsList", offsList);
		
		model.addAttribute("appList", configApps);

		return "modules/work/statisticalMonthList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "StatisticalProjectList")
	public String StatisticalProjectList(
			WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office,
			Model model) throws ParseException {
		User user = UserUtils.getUser();
		
		//用于前台选择应用使用
		List<ConfigApp> configApps = UserUtils.getAppList();
		//用于后台循环展示使用
		List<ConfigApp> configAppList = Lists.newArrayList();
		if(appId == null){
			configAppList = UserUtils.getAppList();
		}else{
			configAppList.add(configAppService.findByAppId(appId));
		}
		//用于前台选择网点使用
		List<Office> officeList = Lists.newArrayList();
		//用于后台循环展示使用	
		List<Long> officeids = UserUtils.getOfficeIdList(area, office, officeList);
		
		if(!(area==null&&office==null)){
			model.addAttribute("offices", officeList);
		}
		
		//前台展示的最外层循环体
		List<ReceivedPaymentsVo> paymentsVos = Lists.newArrayList();
			
		
		//如果起始时间和结束时间都为空,则默认查询当天数据
		if(startTime==null&&endTime==null){
			startTime = new Date();
			endTime = new Date();
		}
				
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		
		
	//循环所有应用
	for(ConfigApp configapp: configAppList){
		List<WorkDealInfo> list = workDealInfoService.findByProjectPay(
				startTime, endTime, officeids, configapp.getId());
		if (list != null && list.size()>0) {
			
			ReceivedPaymentsVo paymentsVo = new ReceivedPaymentsVo();
			
			Map<String, Set<String>> office_District = new TreeMap<>();
			
			
			Map<String, Map<String,List<Double>>> officeDistrictMoney = new TreeMap<>(); 
			Map<String, Map<String,List<String>>> officeDistrictPayMethod = new TreeMap<>(); 
			
			List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();			
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
			Set<String> district = new TreeSet<>();//LinkedHashSet<String>();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getWorkCompany().getDistrict() != null) {
					district.add(list.get(i).getWorkCompany().getDistrict());
				} else {
					district.add("其他");
				}
			}

			Long offs[] = offices.toArray(new Long[offices.size()]);
			Object months[] = month.toArray();
			Object districts[] = district.toArray();
			for (int o = 0; o < offs.length; o++) {

				Set<String> dis = new LinkedHashSet<String>();
				
				for (int d = 0; d < districts.length; d++) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getWorkCompany().getDistrict() != null) {

							if (list.get(i).getOfficeId().equals(offs[o])&& list.get(i).getWorkCompany().getDistrict().equals(districts[d])) {
								dis.add((String) districts[d]);
							}
						} else {
							if (list.get(i).getOfficeId().equals(offs[o])&& districts[d].equals("其他")) {
								dis.add((String) districts[d]);
							}
						}

					}
				}
				office_District.put((officeService.findById((Long) offs[o])).getName(),dis);

			}
			for (int m = 0; m < months.length; m++) {
				
				for(Long officeId:offs){
					String officeName = officeService.get(officeId).getName();
					for (int d = 0; d < districts.length; d++) {
						
						double countPostMoney = 0L;
						double countBankMoney = 0L;
						double countXjMoney = 0L;
						double countAlipayMoney = 0L;
						double countMoney = 0L;
						
					
						for (int i = 0; i < list.size(); i++) {
							String mo = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getWorkPayInfo().getCreateDate());
							if (list.get(i).getWorkCompany().getDistrict() != null) {
								
								if (((String) months[m]).indexOf(mo) != -1&&officeId.longValue()==list.get(i).getOfficeId().longValue() &&list.get(i).getWorkCompany().getDistrict().equals((String) districts[d])) {
									double postMoney = 0L;
									double bankMoney = 0L;
									double xjMoney = 0L;
									double alipayMoney = 0L;
									if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
										if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
											if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
												postMoney = list.get(i).getWorkPayInfo().getPosMoney();
											} /*else {
												postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
											}*/
										}
										if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
											if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
												bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
											} /*else {
												bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
											}*/
										}
										if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
											if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
												xjMoney = list.get(i).getWorkPayInfo().getMoney();
											} /*else {
												xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
											}*/

										}
										if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
											if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
												alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
											} /*else {
												alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
											}*/

										}
									} else {
										List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
										for (int w = 0; w < workfinancePayinfos.size(); w++) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														xjMoney = workfinancePayinfos.get(w).getMoney();
													}/* else {
														xjMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														postMoney = workfinancePayinfos.get(w).getMoney();
													}/* else {
														postMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														bankMoney = workfinancePayinfos.get(w).getMoney();
													} /*else {
														bankMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														alipayMoney = workfinancePayinfos.get(w).getMoney();
													} /*else {
														alipayMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

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

							} else {

								if (((String) months[m]).indexOf(mo) != -1&&officeId.longValue()==list.get(i).getOfficeId().longValue()&&(districts[d].equals("其他"))) {
									double postMoney = 0L;
									double bankMoney = 0L;
									double xjMoney = 0L;
									double alipayMoney = 0L;
									if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
										if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
											if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
												postMoney = list.get(i).getWorkPayInfo().getPosMoney();
											} /*else {
												postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
											}*/
										}
										if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
											if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
												bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
											} /*else {
												bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
											}*/
										}
										if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
											if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
												xjMoney = list.get(i).getWorkPayInfo().getMoney();
											} /*else {
												xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
											}*/

										}
										if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
											if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
												alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
											} /*else {
												alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
											}*/

										}
									} else {
										List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
										for (int w = 0; w < workfinancePayinfos.size(); w++) {
											if (workfinancePayinfos.get(w).getMoney() >= 0) {
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														xjMoney = workfinancePayinfos.get(w).getMoney();
													}/* else {
														xjMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														postMoney = workfinancePayinfos.get(w).getMoney();
													} /*else {
														postMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														bankMoney = workfinancePayinfos.get(w).getMoney();
													} /*else {
														bankMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

												}
												if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
													if (workfinancePayinfos.get(w).getMoney() >= 0) {
														alipayMoney = workfinancePayinfos.get(w).getMoney();
													}/* else {
														alipayMoney = (-workfinancePayinfos.get(w).getMoney());
													}*/

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
						dm.setOfficeName(officeName);
						odms.add(dm);

					}
				}
				

			


			}

			double moneys = 0L;
			
			for(Long officeId:offs){
				Map<String, List<Double>> district_Moneys = new TreeMap<>();
				Map<String, List<String>> district_payMethod = new TreeMap<>();
				String officeName = officeService.get(officeId).getName();
				
				for (int d = 0; d < districts.length; d++) {
					int post = 0;
					int bank = 0;
					int xj = 0;
					int alipay = 0;
					for (int od = 0; od < odms.size(); od++) {
						if (odms.get(od).getDistrictName().equals(districts[d]) && officeName.equals(odms.get(od).getOfficeName()) ) {
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
							if (odms.get(od).getDistrictName().equals(districts[d])&& officeName.equals(odms.get(od).getOfficeName())) {
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
							if (odms.get(od).getDistrictName().equals(districts[d])&& officeName.equals(odms.get(od).getOfficeName())) {
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
							if (odms.get(od).getDistrictName().equals(districts[d])&& officeName.equals(odms.get(od).getOfficeName())) {
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
							if (odms.get(od).getDistrictName().equals(districts[d])&& officeName.equals(odms.get(od).getOfficeName())) {
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
				
				officeDistrictMoney.put(officeName, district_Moneys);
				officeDistrictPayMethod.put(officeName, district_payMethod);
				
			}
			

			
			if(moneys==0d){
				continue;
			}
			
			paymentsVo.setDates(months);
			paymentsVo.setAppName(configapp.getAppName());
		//	paymentsVo.setDistrictMoneys(district_Moneys);
			paymentsVo.setOfficeDistrictMoneyVo(odms);
			paymentsVo.setDateMoneyVo(w_m);
			paymentsVo.setOfficeDistrict(office_District);
		//	paymentsVo.setDistrictPayMethod(district_payMethod);
			paymentsVo.setMoneys(moneys);
			paymentsVo.setMonth(month);
			paymentsVo.setOfficeDistrictMoney(officeDistrictMoney);
			paymentsVo.setOfficeDistrictPayMethod(officeDistrictPayMethod);
			paymentsVos.add(paymentsVo);
		}				
	}
		

	if (paymentsVos.isEmpty()) {
		model.addAttribute("message", "此查询条件没有数据");
	}
	
	model.addAttribute("receivedPayments", paymentsVos);
	model.addAttribute("area", area);
	model.addAttribute("appId", appId);
	model.addAttribute("officeId", office);

	List<Office> offsList = officeService.getOfficeByType(user, 1);
	model.addAttribute("offsList", offsList);
	
	model.addAttribute("appList", configApps);
		return "modules/work/statisticalProjectList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "statisticalAdjustment")
	public String statisticalAdjustment(FinancePaymentInfo financePaymentInfo,
			Date dkstartTime, Date dkendTime, Date zzstartTime, Date zzendTime,
			HttpServletRequest request, String companyName,//应用名称
			HttpServletResponse response, Model model) throws Exception {
		User user = UserUtils.getUser();
		//用于前台选择应用使用
		List<ConfigApp> configApps = UserUtils.getAppList();
		//用于后台循环展示使用
		List<ConfigApp> configAppList = Lists.newArrayList();
		
		if(companyName == null||companyName.isEmpty()){
			configAppList = UserUtils.getAppList();
		}else{
			configAppList.add(configAppService.findByAppname(companyName));
		}
		//前台展示的最外层循环体
		List<ReceivedPaymentsVo> paymentsVos = Lists.newArrayList();

		
		List<Long> officeids = Lists.newArrayList();
		
		
	
		if (zzstartTime == null && zzendTime == null) {
			zzstartTime = new Date();
			zzendTime = new Date();	
		}
		
		//找到当前user的网点权限
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
		
		
		for(ConfigApp configapp: configAppList){
			List<WorkpaymentInfo_dealinfoVo> dealInfoVos = workDealInfoService.findPaymentDeal(financePaymentInfo,configapp.getId(),zzstartTime,zzendTime,dkstartTime,dkendTime,officeids);
			
			if(dealInfoVos.size()>0){
				ReceivedPaymentsVo paymentsVo = new ReceivedPaymentsVo();
				paymentsVo.setPaymentDealInfoVo(dealInfoVos);
				
				paymentsVos.add(paymentsVo);
			}
		}
		
		
		
				
		model.addAttribute("receivedPayments", paymentsVos);
		if (paymentsVos.size() == 0) {
			model.addAttribute("message", "此查询条件没有数据");
		}
		model.addAttribute("dkstartTime", dkstartTime);
		model.addAttribute("dkendTime", dkendTime);
		model.addAttribute("zzstartTime", zzstartTime);
		model.addAttribute("zzendTime", zzendTime);
		model.addAttribute("companyName", companyName);
		model.addAttribute("companys", configApps);
		return "modules/work/statisticalAdjustmentList";
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "statisticalYearProjectList")
	public String statisticalYearProjectList(String startTime, Model model,
			Long configProjectTypeId, Long appId) throws ParseException {
		User user = UserUtils.getUser();
		List<Long> dealInfoByAreaIds = Lists.newArrayList();

		if (configProjectTypeId == null && startTime == null && appId == null) {
			// List<ConfigApp> configApps =
			// configAppService.findByconfigProjectType(configProjectTypeId);
			// model.addAttribute("appList", configApps);
			List<ConfigProjectType> configProjectTypes = configProjectTypeService
					.findProjectTypeList();
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
		List<ConfigProjectType> configProjectTypes = configProjectTypeService
				.findProjectTypeList();
		model.addAttribute("configProjectTypes", configProjectTypes);

		List<Long> configappids = Lists.newArrayList();
		if (configProjectTypeId != null && appId == null) {
			List<Long> appids = Lists.newArrayList();
			List<ConfigApp> configApps = configAppService
					.findByconfigProjectType(configProjectTypeId);
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
			List<ConfigApp> configApps = configAppService
					.findByconfigProjectType(configProjectTypeId);
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
		List<WorkDealInfo> list = workDealInfoService.findByProjectYear(startT,
				endT, configappids);
		if (list != null) {
			for (int c = 0; c < configappids.size(); c++) {
				double zmoney = 0;
				WorkDealInfo wdi = new WorkDealInfo();
				List<Double> month_Moneys = Lists.newArrayList();
				for (int m = 0; m < months.size(); m++) {
					double moneys = 0;
					for (int i = 0; i < list.size(); i++) {
						if (m < months.size() - 1) {
							if (list.get(i).getCreateDate().getTime() >= months
									.get(m).getTime()
									&& list.get(i).getCreateDate().getTime() < months
											.get(m + 1).getTime()
									&& list.get(i).getConfigApp().getId()
											.equals(configappids.get(c))) {
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
								if (list.get(i).getWorkPayInfo()
										.getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo()
											.getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo()
												.getPosMoney() >= 0) {
											postMoney = list.get(i)
													.getWorkPayInfo()
													.getPosMoney();
										} else {
											postMoney = (-list.get(i)
													.getWorkPayInfo()
													.getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo()
											.getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo()
												.getBankMoney() >= 0) {
											bankMoney = list.get(i)
													.getWorkPayInfo()
													.getBankMoney();
										} else {
											bankMoney = (-list.get(i)
													.getWorkPayInfo()
													.getBankMoney());
										}

									}
									if (list.get(i).getWorkPayInfo()
											.getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo()
												.getMoney() >= 0) {
											xjMoney = list.get(i)
													.getWorkPayInfo()
													.getMoney();
										} else {
											xjMoney = (-list.get(i)
													.getWorkPayInfo()
													.getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo()
											.getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo()
												.getAlipayMoney() >= 0) {
											alipayMoney = list.get(i)
													.getWorkPayInfo()
													.getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i)
													.getWorkPayInfo()
													.getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i)
													.getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos
											.size(); w++) {
										if (workfinancePayinfos.get(w)
												.getMoney() >= 0) {
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													xjMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													postMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													bankMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													alipayMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}
											}
										}
									}
								}
								moneys += (postMoney + bankMoney + alipayMoney + xjMoney);
							}
						} else if (m == months.size() - 1) {
							if (list.get(i).getCreateDate().getTime() >= months
									.get(m).getTime()
									&& list.get(i).getConfigApp().getId()
											.equals(configappids.get(c))) {
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
								if (list.get(i).getWorkPayInfo()
										.getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo()
											.getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo()
												.getPosMoney() >= 0) {
											postMoney = list.get(i)
													.getWorkPayInfo()
													.getPosMoney();
										} else {
											postMoney = (-list.get(i)
													.getWorkPayInfo()
													.getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo()
											.getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo()
												.getBankMoney() >= 0) {
											bankMoney = list.get(i)
													.getWorkPayInfo()
													.getBankMoney();
										} else {
											bankMoney = (-list.get(i)
													.getWorkPayInfo()
													.getBankMoney());
										}

									}
									if (list.get(i).getWorkPayInfo()
											.getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo()
												.getMoney() >= 0) {
											xjMoney = list.get(i)
													.getWorkPayInfo()
													.getMoney();
										} else {
											xjMoney = (-list.get(i)
													.getWorkPayInfo()
													.getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo()
											.getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo()
												.getAlipayMoney() >= 0) {
											alipayMoney = list.get(i)
													.getWorkPayInfo()
													.getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i)
													.getWorkPayInfo()
													.getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i)
													.getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos
											.size(); w++) {
										if (workfinancePayinfos.get(w)
												.getMoney() >= 0) {
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													xjMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													postMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													bankMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													alipayMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos
															.get(w).getMoney());
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
				configProduct.setConfigApp(configAppService
						.findByAppId(configappids.get(c)));
				List<ConfigProduct> pros = configProductService
						.findByAppId(configProduct);// 获取应用下的产品信息
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
				wdi.setConfigApp(configAppService.findByAppId(configappids
						.get(c)));
				wdi.getConfigApp().setAppName(
						wdi.getConfigApp().getAppName() + proLabel);
				month_Moneys.add(zmoney);
				w_months.put(wdi, month_Moneys);
			}
		}

		List<Double> zj = Lists.newArrayList();
		int a = months.size() + 1;
		for (int l = 0; l < a; l++) {
			double aa = 0;
			Iterator<Map.Entry<WorkDealInfo, List<Double>>> itt = w_months
					.entrySet().iterator();
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
	public String addConfigProjectType(Long configProjectTypeId)
			throws JSONException {
		JSONObject json = new org.json.JSONObject();
		JSONArray array = new JSONArray();
		List<ConfigApp> configApps = configAppService
				.findByconfigProjectType(configProjectTypeId);
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

		WorkDealInfo workDealInfo = workDealInfoService
				.findDealPayShow(dealInfoId);
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
			model.addAttribute("workCertApplyInfo", workDealInfo
					.getWorkCertInfo().getWorkCertApplyInfo());
		}
		
		String url = Global.getConfig("images.path");
		if (workDealInfo.getId() != null) {
			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			
			if(attachs!=null&&attachs.size()>0){
				String imgNames = "";
				for(int i =0;i<attachs.size();i++){
					if(i==0){
						imgNames+=url+"/"+attachs.get(0).getAttachName()+"##"+attachs.get(0).getStatus();
					}else{
						imgNames+=","+url+"/"+attachs.get(i).getAttachName()+"##"+attachs.get(i).getStatus();	
					}
				}
				model.addAttribute("imgNames", imgNames);
			}
			

			ConfigProduct configProduct = workDealInfo.getConfigProduct();

			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());
			Set<Integer> nameSet = new HashSet<Integer>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
						.getTempStyle()));
			}

			model.addAttribute("boundLabelList", nameSet);

			List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
			model.addAttribute("workLog", list);
			ConfigChargeAgent chargeAgent = chargeAgentService.get(workDealInfo
					.getConfigChargeAgentId());

			// 经信委
			if (chargeAgent.getIsSupportExpirationDate() != null
					&& chargeAgent.getIsSupportExpirationDate() == 1) {
				model.addAttribute("isSupport", true);

				if (workDealInfo.getExpirationDate() != null) {
					model.addAttribute("expirationDate",
							workDealInfo.getExpirationDate());
				}
			}

			model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			List<ConfigProduct> products = configProductService
					.findByApp(workDealInfo.getConfigApp().getId());
			List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
			for (int i = 0; i < products.size(); i++) {
				// products.get(i).getProductName();

				String ssssi = ProductType.productTypeStrMap.get(products
						.get(i).getProductName())
						+ "["
						+ (products.get(i).getProductLabel() == 0 ? "通用" : "专用")
						+ "]";
				ProductTypeObj obj = new ProductTypeObj(products.get(i).getId()
						.intValue(), ssssi);
				listProductTypeObjs.add(obj);

			}
			model.addAttribute("proList", listProductTypeObjs);
		} else {

			// model.addAttribute("proList", ProductType.getProductTypeList());

			model.addAttribute("proList", ProductType.getProductList());

			/*
			 * //经信委 model.addAttribute("expirationDate",
			 * StringHelper.getLastDateOfCurrentYear());
			 */
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
	public String save(
			Model model,
			RedirectAttributes redirectAttributes,
			Long appId,
			String product,
			Integer dealInfoType,
			Integer year,
			
			Integer certSort,    //多证书编号
			
			String imgNames,     //上传的图片名称
			
			Date expirationDate, // 经信委到期时间 和年限二选一
			Long workDealInfoId, Integer yar, Long companyId,
			String companyName,String twoLevelCompanyName, String companyType, String organizationNumber,
			String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber,
			String comCertficateTime, String legalName, String s_province,
			String s_city, String s_county, String address,
			String companyMobile, String remarks, Long workuserId,
			Integer workType, String contactName, String conCertType,
			String contacEmail, String conCertNumber, String contactPhone,
			String contactTel, String deal_info_status, String recordContent,
			Integer agentId, Long agentDetailId,

			Integer classifying, String pName, String pEmail, String pIDCard,
			String contactSex, Integer lable, String areaRemark) {
		
		long start = System.currentTimeMillis();
		
		
		ConfigApp configApp = configAppService.get(appId);
		WorkCompany workCompany = workCompanyService.finByNameAndNumber(
				companyName, organizationNumber);
		ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService
				.get(agentDetailId);
		// ConfigProduct configProduct = configProductService.findByIdOrLable(
		// appId, product, lable);
		// 保存单位信息
		ConfigProduct configProduct = bound.getProduct();
		workCompany.setCompanyName(companyName);
		workCompany.setTwoLevelCompanyName(twoLevelCompanyName);
		workCompany.setCompanyType(companyType);
		workCompany.setComCertificateType(comCertificateType);

		SimpleDateFormat dnf = new SimpleDateFormat("yyyy-MM-dd");

		try {

			if (orgExpirationTime != null && !orgExpirationTime.equals("")) {
				Timestamp ts1 = new Timestamp(dnf.parse(orgExpirationTime)
						.getTime());
				workCompany.setOrgExpirationTime(ts1);
			}
			if (comCertficateTime != null && !comCertficateTime.equals("")) {

				Timestamp ts = new Timestamp(dnf.parse(comCertficateTime)
						.getTime());
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
		//保存多证书编号
		workDealInfo.setCertSort(certSort);
		
		long start1 = System.currentTimeMillis();
		workCompanyService.save(workCompany);
		long end1 = System.currentTimeMillis();
		log.debug("业务新增提交保存workCompany用时:"+(end1-start1)+"ms");
		long start2 = System.currentTimeMillis();
		workUserService.save(workUser);
		long end2 = System.currentTimeMillis();
		log.debug("业务新增提交保存workUser用时:"+(end2-start2)+"ms");
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(configApp);
		workDealInfo.setConfigCommercialAgent(commercialAgent);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
				.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size() > 0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0)
					.getConfigCommercialAgent());// 劳务关系外键
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

		// 经信委
		if (year != null && expirationDate == null) {
			workDealInfo.setYear(year);
			workDealInfo.setExpirationDate(null);
		} else {
			workDealInfo.setYear(StringHelper.getDvalueYear(expirationDate));
			workDealInfo.setExpirationDate(expirationDate);
		}

		workDealInfo.setClassifying(classifying);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date()); 
		workDealInfo.setPayType(agentId);

		WorkCompanyHis workCompanyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(workCompanyHis);
		workDealInfo.setWorkCompanyHis(workCompanyHis);
		WorkUserHis workUserHis = workUserService.change(workUser,
				workCompanyHis);
		workUserHisService.save(workUserHis);
		workDealInfo.setWorkUserHis(workUserHis);
		// 保存申请人信息
		WorkCertApplyInfo workCertApplyInfo = null;
		WorkCertInfo workCertInfo = null;

		if (workDealInfo.getWorkCertInfo() != null) {
			workCertApplyInfo = workDealInfo.getWorkCertInfo()
					.getWorkCertApplyInfo();
		} else {
			workCertApplyInfo = new WorkCertApplyInfo();
		}

		workCertApplyInfo.setName(pName);
		workCertApplyInfo.setEmail(pEmail);
		workCertApplyInfo.setIdCard(pIDCard);

		long start3 = System.currentTimeMillis();
		workCertApplyInfoService.save(workCertApplyInfo);
		long end3 = System.currentTimeMillis();
		log.debug("业务新增提交保存workCertApplyInfo用时:"+(end3-start3)+"ms");
		
		if (workDealInfo.getWorkCertInfo() != null) {
			workCertInfo = workDealInfo.getWorkCertInfo();
		} else {
			workCertInfo = new WorkCertInfo();
		}
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		
		long start4 = System.currentTimeMillis();
		workCertInfoService.save(workCertInfo);
		long end4 = System.currentTimeMillis();
		log.debug("业务新增提交保存workCertInfo用时:"+(end4-start4)+"ms");
		
		workDealInfo.setWorkCertInfo(workCertInfo);

		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());

		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent()
				.getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());

		long start5 = System.currentTimeMillis();
		workDealInfoService.save(workDealInfo);
		long end5 = System.currentTimeMillis();
		log.debug("业务新增提交保存workDealInfo用时:"+(end5-start5)+"ms");
		if (workDealInfoId == null) {
			ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
			dealInfoBound.setDealInfo(workDealInfo);
			ConfigChargeAgent agentBound = configChargeAgentService
					.get(workDealInfo.getConfigChargeAgentId());
			dealInfoBound.setAgent(agentBound);
			configAgentBoundDealInfoService.save(dealInfoBound);
			logUtil.saveSysLog("计费策略模版", "计费策略模版：" + agent.getId() + "--业务编号："
					+ workDealInfo.getId() + "--关联成功!", "");
		}
		//把以前查询出来
		List<CommonAttach> befor = attachService.findCommonAttachByWorkDealInfo(workDealInfoId);
		Map<String,CommonAttach> map = new HashMap<String,CommonAttach>();//键值对保存 便于查询
		for(CommonAttach c:befor){
			map.put(c.getAttachName(), c);
		}
		if(imgNames!=null&&imgNames.length()>0){
			String [] imgs= imgNames.split(",");
			
			CommonAttach attach = null;
			for(int i=0;i<imgs.length;i++){
				CommonAttach comm = map.get(imgs[i]);
				if(comm!=null){
					map.remove(imgs[i]);
					//以前的图片复制一份保存
					comm.setWorkDealInfo(workDealInfo);
					comm.setStatus(null);
					attachService.saveAttach(comm);
				}else{//新图片直接修改workDealInfo
					attach = attachService.findCommonAttachByattachName(imgs[i]);
					if(attach!=null){
						attach.setWorkDealInfo(workDealInfo);
						attach.setStatus(null);
						attachService.saveAttach(attach);						
					}
				}
			}
		}	
		for(String s:map.keySet()){
			CommonAttach comm = map.get(s);
			comm.setStatus(-1);
			attachService.saveAttach(comm);
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

		logUtil.saveSysLog("业务中心", "新增业务保存：编号" + workDealInfo.getId() + "单位名称："
				+ workDealInfo.getWorkCompany().getCompanyName(), "");
		logUtil.saveSysLog("业务办理", "新增业务保存：编号" + workDealInfo.getId() + "单位名称："
				+ workDealInfo.getWorkCompany().getCompanyName(), "");
		
		long end = System.currentTimeMillis();
		
		log.debug("业务新增提交总用时:"+(end-start)+"ms");
		
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}

	/**
	 * 收费信息页面
	 * 
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "pay")
	public String pay(Model model, RedirectAttributes redirectAttributes,
			Long id) {
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
		String product = ProductType.getProductTypeName(Integer
				.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", product);
		ConfigChargeAgent chargeAgent = configChargeAgentService
				.get(workDealInfo.getConfigChargeAgentId());
		model.addAttribute("chargeAgent", chargeAgent);
		// 增加开户费
		if (!account) {
			Double money = configChargeAgentDetailService.getChargeMoney(
					workDealInfo.getConfigChargeAgentId(), WorkDealInfoType
							.getWorkType(WorkDealInfoType.TYPE_OPEN_ACCOUNT),
					null);
			model.addAttribute("type0", money);
		}
		// 按照业务类型查询费用
		if (workDealInfo.getDealInfoType() != null) {
			Double money = configChargeAgentDetailService.getChargeMoney(
					workDealInfo.getConfigChargeAgentId(), WorkDealInfoType
							.getWorkType(workDealInfo.getDealInfoType()),
					workDealInfo.getYear());
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
			Double money = configChargeAgentDetailService.getChargeMoney(
					workDealInfo.getConfigChargeAgentId(), WorkDealInfoType
							.getWorkType(workDealInfo.getDealInfoType1()),
					workDealInfo.getYear());
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
			Double money = configChargeAgentDetailService.getChargeMoney(
					workDealInfo.getConfigChargeAgentId(), WorkDealInfoType
							.getWorkType(workDealInfo.getDealInfoType2()),
					workDealInfo.getYear());
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
			Double money = configChargeAgentDetailService.getChargeMoney(
					workDealInfo.getConfigChargeAgentId(), WorkDealInfoType
							.getWorkType(workDealInfo.getDealInfoType3()),
					workDealInfo.getYear());
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
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getMoney().toString(), officeName, userName,
						date, "现金", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodBank() && payInfo.getBankMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getBankMoney().toString(), officeName,
						userName, date, "银行转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodPos() && payInfo.getPosMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getPosMoney().toString(), officeName, userName,
						date, "POS收款", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodAlipay() && payInfo.getAlipayMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getAlipayMoney().toString(), officeName,
						userName, date, "支付宝转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodGov()) {
				String[] s = new String[] { payInfo.getSn(), officeName, "",
						officeName, userName, date, "政府统一采购", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodContract()) {
				String[] s = new String[] { payInfo.getSn(), officeName, "",
						officeName, userName, date, "合同采购", "" };
				payInfos.add(s);
			}
			List<WorkFinancePayInfoRelation> pay = workFinancePayInfoRelationService
					.findByPayInfo(workDealInfo.getWorkPayInfo());

			model.addAttribute("fpir", pay);
			model.addAttribute("infos", payInfos);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice()
				.getName());
		model.addAttribute("date", sdf.format(new Date()));
		model.addAttribute(
				"dealInfoType",
				WorkDealInfoType.getDealInfoTypeName(workDealInfo
						.getDealInfoType())
						+ " "
						+ WorkDealInfoType.getDealInfoTypeName(workDealInfo
								.getDealInfoType1())
						+ " "
						+ WorkDealInfoType.getDealInfoTypeName(workDealInfo
								.getDealInfoType2())
						+ " "
						+ WorkDealInfoType.getDealInfoTypeName(workDealInfo
								.getDealInfoType3()));

		if (workDealInfo.getDealInfoType() != null
				&& workDealInfo.getDealInfoType().equals(
						WorkDealInfoType.TYPE_ADD_CERT)) {
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
	public String typeFormReturnUpdate(WorkDealInfo workDealInfo, Model model,
			RedirectAttributes redirectAttributes) {

		boolean inOffice = false;
		workDealInfo.setDealInfoStatus("15");
		workDealInfoService.save(workDealInfo);
			
		String url = Global.getConfig("images.path");
		List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
		
		if(attachs!=null&&attachs.size()>0){
			String imgNames = "";
			for(int i =0;i<attachs.size();i++){
				if(i==0){
					imgNames+=url+"/"+attachs.get(0).getAttachName()+"##"+attachs.get(0).getStatus();
				}else{
					imgNames+=","+url+"/"+attachs.get(i).getAttachName()+"##"+attachs.get(i).getStatus();	
				}
			}
			model.addAttribute("imgNames", imgNames);
		}
		
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId()
					.equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		if (!inOffice) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "请到业务办理网点办理！");
			return "redirect:" + Global.getAdminPath()
					+ "/work/workDealInfo/?repage";
		}

		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo
					.getWorkCertInfo().getWorkCertApplyInfo());
		}

		ConfigChargeAgent chargeAgent = chargeAgentService.get(workDealInfo
				.getConfigChargeAgentId());
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
			nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
					.getTempStyle()));
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
			model.addAttribute("imgUrl", Global.getConfig("images.path"));
			SelfImage selfImage = selfImageService
					.findByApplicationId(workDealInfo.getSelfApplyId());
			workDealInfo.setSelfImage(selfImage);
		}
		if (workDealInfo.getSelfImage() != null) {
			model.addAttribute("imgUrl", Global.getConfig("images.path"));
		}

		// 获得应用下的产品
		List<ConfigProduct> products = configProductService
				.findByAppAndProName(workDealInfo.getConfigApp().getId(),
						workDealInfo.getConfigProduct().getProductName());
		List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
		for (int i = 0; i < products.size(); i++) {
			String ssssi = ProductType.productTypeStrMap.get(products.get(i)
					.getProductName())
					+ "["
					+ (products.get(i).getProductLabel() == 0 ? "通用" : "专用")
					+ "]";
			ProductTypeObj obj = new ProductTypeObj(products.get(i).getId()
					.intValue(), ssssi);
			listProductTypeObjs.add(obj);
		}
		
		model.addAttribute("proList", listProductTypeObjs);

		
		//获得省和市对应self_area表中的id
		if(workDealInfo.getWorkCompany().getProvince()!=null&&!workDealInfo.getWorkCompany().getProvince().isEmpty()){
			String provinceId = selfAreaService.findByAreaName(workDealInfo.getWorkCompany().getProvince()).getAreaId();
			model.addAttribute("provinceId", provinceId);
			if(workDealInfo.getWorkCompany().getCity()!=null&&!workDealInfo.getWorkCompany().getCity().isEmpty()){
						
				String cityId = selfAreaService.findByProvinceName(workDealInfo.getWorkCompany().getCity(),provinceId).getAreaId();
				model.addAttribute("cityId", cityId);
			}
				
		}
		
		if (dealInfoTypes.size() == 1) {
			if (dealInfoTypes.get(0).equals(4)) {// 变更
				return "modules/work/maintain/workDealInfoMaintainReturnChange";
			} else if (dealInfoTypes.get(0).equals(1)) {
				model.addAttribute("update", "3");
				ConfigProduct configProductOld = workDealInfo
						.getConfigProduct();
				String[] years = configChargeAgentDetailService
						.getChargeAgentYears(
								configProductOld.getChargeAgentId(),
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
			} else if (dealInfoTypes.get(0).equals(2)
					|| dealInfoTypes.get(0).equals(3)) {
				model.addAttribute("reissue", dealInfoTypes.get(0));
				return "modules/work/maintain/workDealInfoMaintainReturnLost";
			}

		} else if (dealInfoTypes.size() == 2) {
			if (dealInfoTypes.get(0).equals(2)
					|| dealInfoTypes.get(0).equals(3)) {
				if (dealInfoTypes.get(1).equals(4)) {
					model.addAttribute("reissue", dealInfoTypes.get(0));
					return "modules/work/maintain/workDealInfoMaintainReturnChange";
				}
			} else if (dealInfoTypes.get(0).equals(1)) {
				if (dealInfoTypes.get(1).equals(2)
						|| dealInfoTypes.get(1).equals(3)) {
					model.addAttribute("update", "3");
					model.addAttribute("reissue", dealInfoTypes.get(1));
					ConfigProduct configProductOld = workDealInfo
							.getConfigProduct();
					String[] years = configChargeAgentDetailService
							.getChargeAgentYears(
									configProductOld.getChargeAgentId(),
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
				} else if (dealInfoTypes.get(1).equals(4)) {
					model.addAttribute("update", "3");
					ConfigProduct configProductOld = workDealInfo
							.getConfigProduct();
					String[] years = configChargeAgentDetailService
							.getChargeAgentYears(
									configProductOld.getChargeAgentId(),
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
			}
		} else if (dealInfoTypes.size() == 3) {
			model.addAttribute("update", "3");
			model.addAttribute("reissue", dealInfoTypes.get(1));
			ConfigProduct configProductOld = workDealInfo.getConfigProduct();
			String[] years = configChargeAgentDetailService
					.getChargeAgentYears(configProductOld.getChargeAgentId(),
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
	public String updateForm(WorkDealInfo workDealInfo, Model model,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("workDealInfo", workDealInfo);
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo
					.getWorkCertInfo().getWorkCertApplyInfo());
		}
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId()
					.equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
		if (!inOffice) {
			redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
			addMessage(redirectAttributes, "请到业务办理网点更新！");
			return "redirect:" + Global.getAdminPath()
					+ "/work/workDealInfo/?repage";
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
	public String saveUpdate(Model model,
			RedirectAttributes redirectAttributes, String recordContent,
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
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0)
					.getConfigCommercialAgent());// 劳务关系外键
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
			int day = getLastCertDay(workDealInfo1.getWorkCertInfo()
					.getNotafter());
			workDealInfo.setLastDays(day);
		} else {
			workDealInfo.setLastDays(0);
		}

		WorkCertInfo oldCertInfo = workDealInfo1.getWorkCertInfo();
		WorkCertApplyInfo workCertApplyInfo = workDealInfo1.getWorkCertInfo()
				.getWorkCertApplyInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfo.setRenewalPrevId(oldCertInfo.getId());
		workCertInfo.setCreateDate(workCertInfoService
				.getCreateDate(oldCertInfo.getId()));
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

		Double money = configChargeAgentDetailService.selectMoney(chargeAgent,
				WorkDealInfoType.TYPE_UPDATE_CERT, year, workDealInfo
						.getConfigProduct().getProductLabel());
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
		model.addAttribute("dealInfoType", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		String product = ProductType.getProductTypeName(Integer
				.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", product);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice()
				.getName());
		model.addAttribute("date", sdf.format(new Date()));
		logUtil.saveSysLog("业务中心", "业务更新：编号" + workDealInfo.getId() + "单位名称："
				+ workDealInfo.getWorkCompany().getCompanyName(), "");
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
	public String temporarySave(
			Model model,
			Long workDealInfoId,
			RedirectAttributes redirectAttributes,
			Long appId,
			String product,
			Integer dealInfType1,
			Integer year,
			
			Integer certSort,
			
			Date expirationDate, // 到期时间 和年限二选一
			Integer yar, Long companyId, String companyName,String twoLevelCompanyName,
			String companyType, String organizationNumber,
			String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber,
			String comCertficateTime, String legalName, String s_province,
			String s_city, String s_county, String address,
			String companyMobile, String remarks, Long workuserId,
			Integer workType, String contactName, String conCertType,
			String contacEmail, String conCertNumber, String contactPhone,
			String contactTel, String deal_info_status, String recordContent,
			Integer lable, Integer classifying, String pName, String pEmail,
			String pIDCard, String contactSex, String areaRemark,
			Integer agentId, Long agentDetailId,HttpServletRequest request) {

		// Integer agentId,Long agentDetailId,
		long start = System.currentTimeMillis();
		WorkCompany workCompany = workCompanyService.finByNameAndNumber(
				companyName, organizationNumber);

		// 保存单位信息

		workCompany.setCompanyName(companyName);
		workCompany.setTwoLevelCompanyName(twoLevelCompanyName);
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
		
		//保存多证书编号
		workDealInfo.setCertSort(certSort);
		ConfigApp configApp = configAppService.get(appId);

		ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService
				.get(agentDetailId);

		ConfigProduct configProduct = bound.getProduct();
		//
		// ConfigProduct configProduct = configProductService.findByIdOrLable(
		// appId, product, lable);

		long start1 = System.currentTimeMillis();
		workCompanyService.save(workCompany);
		long end1 = System.currentTimeMillis();
		log.debug("业务新增保存workCompany用时:"+(end1-start1)+"ms");
		long start2 = System.currentTimeMillis();
		workUserService.save(workUser);
		long end2 = System.currentTimeMillis();
		log.debug("业务新增保存workUser用时:"+(end2-start2)+"ms");
		workDealInfo.setConfigApp(configApp);
		ConfigCommercialAgent commercialAgent = configAgentAppRelationService
				.findAgentByApp(configApp);
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
				.findByOffice(UserUtils.getUser().getOffice());
		if (configAgentOfficeRelations.size() > 0) {
			workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(0)
					.getConfigCommercialAgent());// 劳务关系外键
		}
		workDealInfo.setCommercialAgent(commercialAgent);
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setConfigProduct(configProduct);

		workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
		workDealInfo.setPayType(agentId);

		workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
		workDealInfo.setDealInfoType1(dealInfType1);

		// 经信委
		if (year != null && expirationDate == null) {
			workDealInfo.setYear(year);
			workDealInfo.setExpirationDate(null);
		} else {
			workDealInfo.setYear(StringHelper.getDvalueYear(expirationDate));
			workDealInfo.setExpirationDate(expirationDate);
		}

		workDealInfo.setClassifying(classifying);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_TEMPED);
		workDealInfo.setCreateBy(UserUtils.getUser());
		workDealInfo.setCreateDate(new Date());
		workDealInfo.setSvn(workDealInfoService.getSVN(0));
		WorkCompanyHis workCompanyHis = workCompanyService.change(workCompany);
		workCompanyHisService.save(workCompanyHis);
		workDealInfo.setWorkCompanyHis(workCompanyHis);
		WorkUserHis workUserHis = workUserService.change(workUser,
				workCompanyHis);
		workUserHisService.save(workUserHis);
		workDealInfo.setWorkUserHis(workUserHis);
		// 保存申请人信息
		
		WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
		WorkCertInfo workCertInfo = new WorkCertInfo();
		
		workCertApplyInfo.setName(pName);
		workCertApplyInfo.setEmail(pEmail);
		workCertApplyInfo.setIdCard(pIDCard);
		workCertApplyInfo.setProvince(workCompany.getProvince());
		workCertApplyInfo.setCity(workCompany.getCity());
		long start3 = System.currentTimeMillis();
		workCertApplyInfoService.save(workCertApplyInfo);
		long end3 = System.currentTimeMillis();
		log.debug("业务新增保存workCertApplyInfo用时:"+(end3-start3)+"ms");
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		long start4 = System.currentTimeMillis();
		workCertInfoService.save(workCertInfo);
		long end4 = System.currentTimeMillis();
		log.debug("业务新增保存workCertInfo用时:"+(end4-start4)+"ms");
		workDealInfo.setWorkCertInfo(workCertInfo);
		workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent()
				.getId());
		workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
		
		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());
		
		long start5 = System.currentTimeMillis();
		workDealInfoService.save(workDealInfo);
		long end5 = System.currentTimeMillis();
		log.debug("业务新增保存workDealInfo用时:"+(end5-start5)+"ms");
		
		//把以前查询出来
		String imgNames=request.getParameter("imgNames");
		List<CommonAttach> befor = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
		Map<String,CommonAttach> map = new HashMap<String,CommonAttach>();//键值对保存 便于查询
		for(CommonAttach c:befor){
			map.put(c.getAttachName(), c);
		}
		if(imgNames!=null&&imgNames.length()>0){
			String [] imgs= imgNames.split(",");
			
			CommonAttach attach = null;
			for(int i=0;i<imgs.length;i++){
				CommonAttach comm = map.get(imgs[i]);
				if(comm!=null){
					map.remove(imgs[i]);
					//以前的图片复制一份保存
					comm.setWorkDealInfo(workDealInfo);
					comm.setStatus(null);
					attachService.saveAttach(comm);
				}else{//新图片直接修改workDealInfo
					attach = attachService.findCommonAttachByattachName(imgs[i]);
					if(attach!=null){
						if(workDealInfoId==null){
							CommonAttach newattach = new CommonAttach(attach);
							newattach.setWorkDealInfo(workDealInfo);
							newattach.setStatus(null);
							attachService.saveAttach(newattach);
						}else{
							attach.setWorkDealInfo(workDealInfo);
							attach.setStatus(null);
							attachService.saveAttach(attach);						
						}
					}
				}
			}
		}	
		for(String s:map.keySet()){
			CommonAttach comm = map.get(s);
			comm.setStatus(-1);
			attachService.saveAttach(comm);
		}

		
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
		logUtil.saveSysLog("业务中心", "新增业务暂时保存：编号" + workDealInfo.getId()
				+ "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(), "");

		long end = System.currentTimeMillis();
		log.debug("业务新增保存总用时:"+(end-start)+"ms");
		
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

			ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService
					.findByAgentIdDealId(agent.getId(), id);
			if (bound != null) {
				configAgentBoundDealInfoService.deleteById(bound.getId());
			}
		}

		if (workDealInfo.getPrevId() != null) {
			WorkDealInfo workDealInfo1 = workDealInfoService.get(workDealInfo
					.getPrevId());
			workDealInfo1.setDelFlag(WorkDealInfo.DEL_FLAG_NORMAL);
			workDealInfoService.save(workDealInfo1);
		}
		logUtil.saveSysLog("业务中心", "删除业务：编号" + id + "单位名称："
				+ workDealInfo.getWorkCompany().getCompanyName(), "");
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

			ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService
					.findByAgentIdDealId(agent.getId(), id);
			if (bound != null) {
				configAgentBoundDealInfoService.deleteById(bound.getId());
			}
		}

		// return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
		return "redirect:" + Global.getAdminPath()
				+ "/work/workDealInfo/pay?id=" + workDealInfo.getId();
	}

	/**
	 * 收费信息页面
	 * 
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "load")
	public String load(Model model, RedirectAttributes redirectAttributes,
			Long id) {
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
		String product = ProductType.getProductTypeName(Integer
				.parseInt(workDealInfo.getConfigProduct().getProductName()));
		model.addAttribute("product", product);
		int type = workDealInfo.getDealInfoType();
		model.addAttribute("dealInfoType", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType()));
		model.addAttribute("dealInfoType1", WorkDealInfoType
				.getDealInfoTypeName(workDealInfo.getDealInfoType1()));
		ConfigChargeAgent chargeAgent = configChargeAgentService
				.get(workDealInfo.getConfigProduct().getChargeAgentId());

		if (!account) {
			Double money = configChargeAgentDetailService.selectMoney(
					chargeAgent, WorkDealInfoType.TYPE_OPEN_ACCOUNT, null,
					workDealInfo.getConfigProduct().getProductLabel());
			model.addAttribute("type0", money);
		}
		Double money = configChargeAgentDetailService.selectMoney(chargeAgent,
				type, workDealInfo.getYear(), workDealInfo.getConfigProduct()
						.getProductLabel());

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
			money1 = configChargeAgentDetailService.selectMoney(chargeAgent,
					type1, workDealInfo.getYear(), workDealInfo
							.getConfigProduct().getProductLabel());
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
		if (type == WorkDealInfoType.TYPE_INFORMATION_REROUTE
				&& workDealInfo.getDealInfoType1() != null) {
			Double addMoney = configChargeAgentDetailService.selectMoney(
					chargeAgent, WorkDealInfoType.TYPE_ADD_CERT, workDealInfo
							.getYear(), workDealInfo.getConfigProduct()
							.getProductLabel());
			if (money + money1 > addMoney) {
				model.addAttribute("addMoney", addMoney);
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("userName", UserUtils.getUser().getName());
		model.addAttribute("officeName", UserUtils.getUser().getOffice()
				.getName());
		model.addAttribute("date", sdf.format(new Date()));
		List<String[]> payInfos = new ArrayList<String[]>();
		WorkPayInfo payInfo = workDealInfo.getWorkPayInfo();
		if (payInfo != null) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String officeName = workDealInfo.getWorkCompany().getCompanyName();
			String userName = workDealInfo.getCreateBy().getName();
			String date = sdf1.format(workDealInfo.getCreateDate());
			if (payInfo.getMethodMoney() && payInfo.getMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getMoney().toString(), officeName, userName,
						date, "现金", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodBank() && payInfo.getBankMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getBankMoney().toString(), officeName,
						userName, date, "银行转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodPos() && payInfo.getPosMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getPosMoney().toString(), officeName, userName,
						date, "POS收款", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodAlipay() && payInfo.getAlipayMoney() > 0L) {
				String[] s = new String[] { payInfo.getSn(), officeName,
						payInfo.getAlipayMoney().toString(), officeName,
						userName, date, "支付宝转账", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodGov()) {
				String[] s = new String[] { payInfo.getSn(), officeName, "",
						officeName, userName, date, "政府统一采购", "" };
				payInfos.add(s);
			}
			if (payInfo.getMethodContract()) {
				String[] s = new String[] { payInfo.getSn(), officeName, "",
						officeName, userName, date, "合同采购", "" };
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
		if (workDealInfo.getDealInfoType().equals(
				WorkDealInfoType.TYPE_ADD_CERT)) {
			if (workDealInfo.getWorkPayInfo() != null) {
				url = "modules/work/workDealInfoErrorLoad";
			} else {
				url = "modules/work/workDealInfoLoad";
			}
		} else if (workDealInfo.getDealInfoType().equals(
				WorkDealInfoType.TYPE_UPDATE_CERT)) {
			url = "modules/work/workDealInfoUpdateLoad";
		} else if (workDealInfo.getDealInfoType().equals(
				WorkDealInfoType.TYPE_LOST_CHILD)) {
			url = "modules/work/workDealInfoReissueLoad";
		} else if (workDealInfo.getDealInfoType().equals(
				WorkDealInfoType.TYPE_DAMAGED_REPLACED)) {
			url = "modules/work/workDealInfoReissueLoad";
		} else if (workDealInfo.getDealInfoType().equals(
				WorkDealInfoType.TYPE_INFORMATION_REROUTE)) {
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
				map.put("title", configAppOfficeRelation.getConfigApp()
						.getAppName());
				map.put("result", configAppOfficeRelation.getConfigApp()
						.getId());
				lis.add(map);
			}
			json.put("lis", lis);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return json.toString();
	}
	
	
	
	/**
	 * 判断app是地税还是社保
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "appApplay")
	@ResponseBody
	public String appApplay(HttpServletRequest request, HttpServletResponse response,Long appId) {
		JSONObject json = new JSONObject();
		try {
			ConfigApp app = configAppService.findByAppId(appId);
			
			json.put("appName", app.getAppName());
			
			if(app.getApplyFlag1()!=null&&app.getApplyFlag1()==true){
				json.put("isLandTax", true);
			}else{
				json.put("isLandTax", false);
			}
			
			
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
	public String product(Long appId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			json.put("appId", appId);
			ConfigApp configApp = new ConfigApp();
			configApp.setId(appId);
			ConfigProduct configProduct = new ConfigProduct();
			configProduct.setConfigApp(configApp);
			List<ConfigProduct> list = configProductService
					.findByAppIdAndRa(configProduct);
			Set set = new HashSet();
			ArrayList lis = new ArrayList();
			for (ConfigProduct con : list) {
				lis.add(con.getProductName() + con.getProductLabel()
						+ con.getId());

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
	public String completeCompanyName(HttpServletRequest request,
			HttpServletResponse response, String companyname, Long productId) {
		JSONObject json = new JSONObject();
		try {
			List<WorkCompany> workCompany = workCompanyService
					.findByCompanyName(companyname);
			ArrayList<Long> lis = new ArrayList<Long>();
			for (int i = 0; i < workCompany.size(); i++) {
				lis.add(workCompany.get(i).getId());
			}
			json.put("Idlis", lis);
			if(productId!=null){
				ConfigProduct product = configProductService.get(productId);
				json.put("productId", product.getProductName());
			}

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
	public String cert(Long id, HttpServletRequest request,
			HttpServletResponse response) {
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
			json.put("comCertficateNumber",
					workCompany.getComCertficateNumber());
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
				json.put("orgExpirationTime",
						smd.format(workCompany.getOrgExpirationTime()));
			}
			if (workCompany.getComCertficateTime() != null) {
				json.put("comCertficateTime",
						smd.format(workCompany.getComCertficateTime()));
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
	public String findCompanyInformation(Long id, HttpServletRequest request,
			HttpServletResponse response) {
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
				json.put("companyName", workCompany.getCompanyName());
				json.put("organizationNumber",
						workCompany.getOrganizationNumber());
				json.put("companyType", workCompany.getCompanyType());
				json.put("selectLv", workCompany.getSelectLv());
				json.put("comCertficateNumber",
						workCompany.getComCertficateNumber());
				json.put("comCertificateType",
						workCompany.getComCertificateType());
				json.put("legalName", workCompany.getLegalName());
				json.put("address", workCompany.getAddress());
				json.put("remarks", workCompany.getRemarks());
				json.put("workCompany", workCompany);
				json.put("city", workCompany.getCity());
				json.put("province", workCompany.getProvince());
				json.put("district", workCompany.getDistrict());
				
				//获得省和市对应self_area表中的id
				if(workDealInfo.getWorkCompany().getProvince()!=null&&!workDealInfo.getWorkCompany().getProvince().isEmpty()){
					String provinceId = selfAreaService.findByAreaName(workDealInfo.getWorkCompany().getProvince()).getAreaId();
					json.put("provinceId", provinceId);
					if(workDealInfo.getWorkCompany().getCity()!=null&&!workDealInfo.getWorkCompany().getCity().isEmpty()){
						
						String cityId = selfAreaService.findByProvinceName(workDealInfo.getWorkCompany().getCity(),provinceId).getAreaId();
						json.put("cityId", cityId);
					}
				
				}
				
				
				json.put("companyMobile", workCompany.getCompanyMobile());

				if (workCompany.getOrgExpirationTime() != null) {
					json.put("orgExpirationTime",
							smd.format(workCompany.getOrgExpirationTime()));
				}
				if (workCompany.getComCertficateTime() != null) {
					json.put("comCertficateTime",
							smd.format(workCompany.getComCertficateTime()));
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
	public String type(String name, Long appId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			List<ConfigProduct> list = configProductService.findByName(name,
					appId);

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
	public String payment(String load, Date startTime, Date endTime, Long appId)
			throws Exception {
		load = URLDecoder.decode(load, "UTF-8");
		JSONArray jsonArray = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			List<FinancePaymentInfo> list = financePaymentInfoService
					.findByCompany(load, startTime, endTime, appId, UserUtils
							.getUser().getOffice());
			for (FinancePaymentInfo financePaymentInfo : list) {
				JSONObject json = new JSONObject();
				json.put("financePayInfoId", financePaymentInfo.getId());
				json.put("remark", financePaymentInfo.getRemark());
				json.put("companyName", financePaymentInfo.getCompany());
				json.put("paymentMoney", financePaymentInfo.getPaymentMoney());
				json.put("officeName", financePaymentInfo.getCreateBy()
						.getOffice().getName());
				json.put("ceartName", financePaymentInfo.getCreateBy()
						.getName());
				json.put("createDate",
						sdf.format(financePaymentInfo.getCreateDate()));
				if (financePaymentInfo.getPaymentMethod() == 1) {
					json.put("paymentMethod", "现金");
				} else if (financePaymentInfo.getPaymentMethod() == 2) {
					json.put("paymentMethod", "POS收款");
				} else if (financePaymentInfo.getPaymentMethod() == 3) {
					json.put("paymentMethod", "银行转帐");
				} else if (financePaymentInfo.getPaymentMethod() == 4) {
					json.put("paymentMethod", "支付宝转账");
				}
				json.put("remarks",
						financePaymentInfo.getRemarks() == null ? ""
								: financePaymentInfo.getRemarks());
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
			List<FinancePaymentInfo> list = financePaymentInfoService
					.findByIds(idsL);
			for (FinancePaymentInfo financePaymentInfo : list) {
				JSONObject json = new JSONObject();
				json.put("financePayInfoId", financePaymentInfo.getId());
				json.put("remark", financePaymentInfo.getRemark());
				json.put("companyName", financePaymentInfo.getCompany());
				json.put("paymentMoney", financePaymentInfo.getPaymentMoney());
				json.put("officeName", financePaymentInfo.getCreateBy()
						.getOffice().getName());
				json.put("ceartName", financePaymentInfo.getCreateBy()
						.getName());
				json.put("createDate",
						sdf.format(financePaymentInfo.getCreateDate()));
				if (financePaymentInfo.getPaymentMethod() == 1) {
					json.put("paymentMethod", "现金");
				} else if (financePaymentInfo.getPaymentMethod() == 2) {
					json.put("paymentMethod", "POS收款");
				} else if (financePaymentInfo.getPaymentMethod() == 3) {
					json.put("paymentMethod", "银行转帐");
				} else if (financePaymentInfo.getPaymentMethod() == 4) {
					json.put("paymentMethod", "支付宝转账");
				}
				json.put("remarks",
						financePaymentInfo.getRemarks() == null ? ""
								: financePaymentInfo.getRemarks());
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
	public String quickCert(String name, String email, String keySn,
			String reqBuf) {
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
	public String showCert(Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		WorkCompany workCompany = workCompanyService.get(id);
		WorkDealInfo workDealInfo = new WorkDealInfo();
		workDealInfo.setWorkCompany(workCompany);
		workDealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_CERT_OBTAINED);
		Page<WorkDealInfo> page = workDealInfoService.find1(
				new Page<WorkDealInfo>(request, response), workDealInfo);
		model.addAttribute("page", page);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		return "modules/work/workDealInfoShow";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "showCertEnterprise")
	public String showCertEnterprise(
			Model model,
			HttpServletRequest request,
			@RequestParam(value = "companyIds", required = false) List<Long> companyIds,
			HttpServletResponse response,
			@RequestParam(value = "workdealinfoIds", required = false) String workdealinfoIds,
			String productId) {

		List<Long> workdealinfos = new ArrayList<Long>();
		String[] workdealinfoss = workdealinfoIds.split(",");
		for (String string : workdealinfoss) {
			workdealinfos.add(Long.parseLong(string));
		}

		Page<WorkDealInfo> page = workDealInfoService.findEnterprise2(
				new Page<WorkDealInfo>(request, response), workdealinfos);
		model.addAttribute("page", page);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("companyId", companyIds);
		model.addAttribute("productId", productId);
		return "modules/work/workDealInfoShow";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "showCertPersonal")
	public String showCertPersonal(
			Model model,
			HttpServletRequest request,
			@RequestParam(value = "companyIds", required = false) List<Long> companyIds,
			HttpServletResponse response,
			@RequestParam(value = "workdealinfoIds", required = false) String workdealinfoIds,
			String productId) {
		List<Long> workdealinfos = new ArrayList<Long>();
		String[] workdealinfoss = workdealinfoIds.split(",");
		for (String string : workdealinfoss) {
			workdealinfos.add(Long.parseLong(string));
		}
		List<WorkDealInfo> list = workDealInfoService
				.findPersonal1(workdealinfos);
		model.addAttribute("list", list);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		model.addAttribute("companyId", companyIds);
		model.addAttribute("productId", productId);
		model.addAttribute("pro", ProductType.productTypeStrMap);
		return "modules/work/workDealInfoShowPersonal";
	}

	@RequestMapping(value = "ajaxEnterpriseCount")
	@ResponseBody
	public String ajaxEnterpriseCount(
			Model model,
			HttpServletRequest request,
			@RequestParam(value = "companyIds", required = false) List<Long> companyIds,
			HttpServletResponse response, String productId) {

		JSONObject json = new JSONObject();
		try {
			// Page<WorkDealInfo> page = workDealInfoService.findEnterprise(new
			// Page<WorkDealInfo>(request, response),
			// companyIds, productId);
			Page<WorkDealInfo> list = workDealInfoService.findEnterprise(
					new Page<WorkDealInfo>(request, response), companyIds,
					productId);

			List<Long> workdealinfoIds = new ArrayList<Long>();
			// List<Long> workdealinfos = new ArrayList<Long>();
			// List<Long> previds = new ArrayList<Long>();
			for (WorkDealInfo workDealInfo : list.getList()) {
				WorkDealInfo workDealInfo1 = workDealInfoService
						.findNextDealInfo(workDealInfo.getId());
				if (workDealInfo1 == null) {
					workdealinfoIds.add(workDealInfo.getId());
				}

				// if(workDealInfo.getPrevId()!=null)
				// {
				// previds.add(workDealInfo.getPrevId());
				// }
			}
			// int size=workdealinfoIds.size();
			// workdealinfos = workdealinfoIds;
			// for(int j=0;j<size;j++)
			// {
			// for(int i = 0 ;i<previds.size();i++)
			// {
			// if(!previds.isEmpty())
			// {
			// if(workdealinfos.get(j).equals(previds.get(i)))
			// {
			// workdealinfos.remove(j);
			// }
			// }
			// }
			// }
			//
			if (workdealinfoIds.size() > 0 && !workdealinfoIds.isEmpty()) {
				StringBuffer workdealinfoss = new StringBuffer();
				for (Long long1 : workdealinfoIds) {
					workdealinfoss.append(long1 + ",");
				}

				int end = workdealinfoss.length() - 1;
				String wi = new String();
				wi = workdealinfoss.substring(0, end);
				json.put("index", workdealinfoIds.size());
				json.put("workdealinfoss", wi);
			} else {
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
	public String ajaxPersonalCount(
			Model model,
			HttpServletRequest request,
			@RequestParam(value = "companyIds", required = false) List<Long> companyIds,
			HttpServletResponse response, String productId) {

		JSONObject json = new JSONObject();
		try {
			List<WorkDealInfo> list = workDealInfoService.findPersonal(
					companyIds, productId);
			List<Long> workdealinfoIds = new ArrayList<Long>();
			List<Long> previds = new ArrayList<Long>();

			System.out.println("workdealinfoIds长度为:" + workdealinfoIds.size());
			System.out.println("previds长度为:" + previds.size());

			for (WorkDealInfo workDealInfo : list) {

				workdealinfoIds.add(workDealInfo.getId());

				if (workDealInfo.getPrevId() != null) {
					previds.add(workDealInfo.getPrevId());
				}
			}
			for (int j = 0; j < workdealinfoIds.size(); j++) {
				for (int i = 0; i < previds.size(); i++) {
					if (!previds.isEmpty()) {
						System.out.println("j===" + j);
						System.out.println("i===" + i);
						if (workdealinfoIds.get(j).equals(previds.get(i))) {
							workdealinfoIds.remove(j);
						}
					}
				}
			}

			if (workdealinfoIds.size() > 0 && !workdealinfoIds.isEmpty()) {
				StringBuffer workdealinfoss = new StringBuffer();
				for (Long long1 : workdealinfoIds) {
					workdealinfoss.append(long1 + ",");
				}

				int end = workdealinfoss.length() - 1;
				String wi = new String();
				wi = workdealinfoss.substring(0, end);
				json.put("index", workdealinfoIds.size());
				json.put("workdealinfoss", wi);
			} else {
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
	public String businessQueryNew(
			WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response,
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
			@RequestParam(value = "paymentStartTime", required = false) Date paymentStartTime,
			@RequestParam(value = "paymentEndTime", required = false) Date paymentEndTime,
			@RequestParam(value = "zhizhengStartTime", required = false) Date zhizhengStartTime,
			@RequestParam(value = "zhizhengEndTime", required = false) Date zhizhengEndTime,
			Model model) {
		// 获取前台的付款方式
		if (payMethod != null) {
			WorkPayInfo workPayInfo = new WorkPayInfo();
			if (payMethod.equals("1")) {
				workPayInfo.setMethodMoney(true);
			}
			if (payMethod.equals("2")) {
				workPayInfo.setMethodPos(true);
			}
			if (payMethod.equals("3")) {
				workPayInfo.setMethodBank(true);
			}
			if (payMethod.equals("4")) {
				workPayInfo.setMethodAlipay(true);
			}
			if (payMethod.equals("5")) {
				workPayInfo.setMethodGov(true);
			}
			if (payMethod.equals("6")) {
				workPayInfo.setMethodContract(true);
			}
			workDealInfo.setWorkPayInfo(workPayInfo);
		}
		// 更改:之前前台展示所有的app 现改为user的数据权限下的所有应用
		List<Office> officeList = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
		List<ConfigApp> configAppList = Lists.newArrayList();
		List<Long> officeIds = new ArrayList<Long>();
		for (int i = 0; i < officeList.size(); i++) {
			officeIds.add(officeList.get(i).getId());
			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
					.findAllByOfficeId(officeList.get(i).getId());
			for (ConfigAppOfficeRelation appOffice : appOffices) {
				if (!configAppList.contains(appOffice.getConfigApp())) {
					configAppList.add(appOffice.getConfigApp());
				}
			}
		}
		List<Long> officeids = Lists.newArrayList();
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);
		} else {
			if (area != null) {
				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();

		WorkDealInfoExpViewVO query = new WorkDealInfoExpViewVO();
		query.setWorkDealInfo(workDealInfo);
		query.setAreaId(area);
		query.setOfficeId(officeId);
		query.setApply(apply);
		query.setCertType(certType);
		query.setWorkType(workType);
		query.setYear(year);
		query.setPayMethod(payMethod);
		query.setLuruStartTime(luruStartTime);
		query.setLuruEndTime(luruEndTime);
		query.setDaoqiStartTime(daoqiStartTime);
		query.setDaoqiEndTime(daoqiEndTime);
		query.setPaymentStartTime(paymentStartTime);
		query.setPaymentEndTime(paymentEndTime);
		query.setZhizhengStartTime(zhizhengStartTime);
		query.setZhizhengEndTime(zhizhengEndTime);
		query.setOfficeIds(officeIds);
		query.setOffices(officeids);

		// 单独处理吊销
		if (workType != null && workType == 5) {
			Page<WorkDealInfo> page = workDealInfoExpViewService.find(request,
					response, query, false, true);
			model.addAttribute("proType", ProductType.productTypeStrMap);
			model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
			model.addAttribute("wdiStatus",
					WorkDealInfoStatus.WorkDealInfoStatusMap);
			model.addAttribute("certTypes", productType.getProductTypeList());
			model.addAttribute("workTypes",
					workDealInfoType.getProductTypeList());

			model.addAttribute("page", page);

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

			if (area != null) {
				List<Office> offices = officeService.findByParentId(area);
				model.addAttribute("offices", offices);
			}

			model.addAttribute("offsList", offsList);

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

			model.addAttribute("paymentStartTime", paymentStartTime);
			model.addAttribute("paymentEndTime", paymentEndTime);

			model.addAttribute("zhizhengStartTime", zhizhengStartTime);
			model.addAttribute("zhizhengEndTime", zhizhengEndTime);

			model.addAttribute("officeId", officeId);
			model.addAttribute("area", area);
			return "modules/work/workDealInfoBusinessQueryList";
		}

		Page<WorkDealInfo> page = workDealInfoExpViewService.find(request,
				response, query, false, false);

		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("certTypes", productType.getProductTypeList());
		model.addAttribute("workTypes", workDealInfoType.getProductTypeList());

		model.addAttribute("page", page);

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
		if (area != null) {
			List<Office> offices = officeService.findByParentId(area);
			model.addAttribute("offices", offices);
		}

		model.addAttribute("offsList", offsList);

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

		model.addAttribute("paymentStartTime", paymentStartTime);
		model.addAttribute("paymentEndTime", paymentEndTime);

		model.addAttribute("zhizhengStartTime", zhizhengStartTime);
		model.addAttribute("zhizhengEndTime", zhizhengEndTime);

		model.addAttribute("officeId", officeId);
		model.addAttribute("area", area);
		return "modules/work/workDealInfoBusinessQueryList";
	}

	// @RequiresPermissions("work:workDealInfo:view")
	// @RequestMapping(value = "businessQuery2")
	public String businessQuery(
			WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response,
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
			@RequestParam(value = "paymentStartTime", required = false) Date paymentStartTime,
			@RequestParam(value = "paymentEndTime", required = false) Date paymentEndTime,
			@RequestParam(value = "zhizhengStartTime", required = false) Date zhizhengStartTime,
			@RequestParam(value = "zhizhengEndTime", required = false) Date zhizhengEndTime,
			Model model) {

		// 获取前台的付款方式

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
		/*
		 * List<Long> dealInfoByOfficeAreaIds = Lists.newArrayList(); List<Long>
		 * dealInfoByAreaIds = Lists.newArrayList();
		 */

		// 更改:之前前台展示所以的app 现改为user的数据权限下的所有应用
		List<Office> officeList = officeService.getOfficeByType(
				UserUtils.getUser(), 2);

		List<ConfigApp> configAppList = Lists.newArrayList();

		List<Long> officeIds = new ArrayList<Long>();
		for (int i = 0; i < officeList.size(); i++) {
			officeIds.add(officeList.get(i).getId());
			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
					.findAllByOfficeId(officeList.get(i).getId());

			for (ConfigAppOfficeRelation appOffice : appOffices) {
				if (!configAppList.contains(appOffice.getConfigApp())) {
					configAppList.add(appOffice.getConfigApp());
				}

			}
		}

		List<Long> officeids = Lists.newArrayList();
		if (officeId != null && officeId != 0) {

			officeids.add(officeId);

		} else {
			if (area != null) {

				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}
			}
		}
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();

		// 单独处理吊销
		if (workType != null && workType == 5) {
			Page<WorkDealInfo> page = workDealInfoService.findCX(
					new Page<WorkDealInfo>(request, response), workDealInfo,
					officeids, apply, certType, workType, year, luruStartTime,
					luruEndTime, officeIds, daoqiStartTime, daoqiEndTime,
					paymentStartTime, paymentEndTime, zhizhengStartTime,
					zhizhengEndTime);
			model.addAttribute("proType", ProductType.productTypeStrMap);
			model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
			model.addAttribute("wdiStatus",
					WorkDealInfoStatus.WorkDealInfoStatusMap);
			model.addAttribute("certTypes", productType.getProductTypeList());
			model.addAttribute("workTypes",
					workDealInfoType.getProductTypeList());

			model.addAttribute("page", page);

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
			/*
			 * Office scca = officeService.get(1L); offsList.add(scca);
			 */
			if (area != null) {
				List<Office> offices = officeService.findByParentId(area);
				model.addAttribute("offices", offices);
			}

			model.addAttribute("offsList", offsList);

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

			model.addAttribute("paymentStartTime", paymentStartTime);
			model.addAttribute("paymentEndTime", paymentEndTime);

			model.addAttribute("zhizhengStartTime", zhizhengStartTime);
			model.addAttribute("zhizhengEndTime", zhizhengEndTime);

			model.addAttribute("officeId", officeId);
			model.addAttribute("area", area);
			return "modules/work/workDealInfoBusinessQueryList";
		}
		Page<WorkDealInfo> page = workDealInfoService.find(
				new Page<WorkDealInfo>(request, response), workDealInfo,
				officeids, apply, certType, workType, year, luruStartTime,
				luruEndTime, officeIds, daoqiStartTime, daoqiEndTime,
				paymentStartTime, paymentEndTime, zhizhengStartTime,
				zhizhengEndTime);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("certTypes", productType.getProductTypeList());
		model.addAttribute("workTypes", workDealInfoType.getProductTypeList());

		model.addAttribute("page", page);

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
		/*
		 * Office scca = officeService.get(1L); offsList.add(scca);
		 */
		if (area != null) {
			List<Office> offices = officeService.findByParentId(area);
			model.addAttribute("offices", offices);
		}

		model.addAttribute("offsList", offsList);

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

		model.addAttribute("paymentStartTime", paymentStartTime);
		model.addAttribute("paymentEndTime", paymentEndTime);

		model.addAttribute("zhizhengStartTime", zhizhengStartTime);
		model.addAttribute("zhizhengEndTime", zhizhengEndTime);

		model.addAttribute("officeId", officeId);
		model.addAttribute("area", area);
		return "modules/work/workDealInfoBusinessQueryList";

	}

	@RequestMapping(value = "showWorkDeal")
	public String showWorkDeal(Long dealInfoId, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		List<WorkDealInfo> page = workDealInfoService
				.findByDealInfoId(dealInfoId);
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
	public String initKey(String csp, Model model, String keySn,
			HttpServletRequest request) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, ClientProtocolException, IOException {
		csp = URLDecoder.decode(csp, "UTF-8");
		KeyGeneralInfo info = generalInfoService.findInfoByName(csp);
		if (info != null) {
			model.addAttribute("userPin", info.getDefaultUserPin());
			String adminPin = info.getDefaultSoPin();
			if (adminPin == null || adminPin.equals("")) {
				// keyunlocks/key/{sn}
				String url = ixinUrl + "/rest/key/" + keySn + "/adminpin/"
						+ info.getDefaultSoPinType();
				HttpClient client = new DefaultHttpClient();
				String respCode = ItrustProxyUtil.sendPost(client, url, null,
						request, key);
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
				SecretKeySpec skeySpec = new SecretKeySpec(
						AdminPinEncKey.adminPinEncKey.substring(0, 16)
								.getBytes(), unlockCipher);
				IvParameterSpec ivSpec = new IvParameterSpec(
						AdminPinEncKey.adminPinEncKey.substring(16, 32)
								.getBytes());
				Cipher cipher = Cipher.getInstance(unlockCipher
						+ "/CBC/PKCS5Padding");

				cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec, null);
				byte[] decadminpin = cipher.doFinal(com.itrus.util.Base64
						.decode(adminPin.getBytes()));
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
	public String typeForm(String dealType, WorkDealInfo workDealInfo,
			Model model, String reissueType,
			RedirectAttributes redirectAttributes) {
		String url = Global.getConfig("images.path");
		//根据dealType 判断是不是信息追加
		if(dealType.contains("6")){
			return information(url, workDealInfo,model,redirectAttributes);
		}else{
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			
			if(attachs!=null&&attachs.size()>0){
				String imgNames = "";
				for(int i =0;i<attachs.size();i++){
					if(i==0){
						imgNames+=url+"/"+attachs.get(0).getAttachName()+"##"+attachs.get(0).getStatus();
					}else{
						imgNames+=","+url+"/"+attachs.get(i).getAttachName()+"##"+attachs.get(i).getStatus();	
					}
				}
				model.addAttribute("imgNames", imgNames);
			}
			
			
			if(UserUtils.getUser().getOffice().getParent().getId()==1){
				addMessage(redirectAttributes, "当前网点没有办理业务的权限！");
				return "redirect:" + Global.getAdminPath()
						+ "/work/workDealInfo/?repage";
			}
			
			
			boolean inOffice = false;
			List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
					.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
			for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
				if (appOffice.getConfigApp().getId()
						.equals(workDealInfo.getConfigApp().getId())) {
					inOffice = true;
				}
			}
			workDealInfo.setIsMainTain("mainTain");
			workDealInfoService.save(workDealInfo);
			//区别再次编辑的页面
			model.addAttribute("iseditor", "noeditor");
			String[] type = dealType.replace(",", " ").split(" ");
			for (int i = 0; i < type.length; i++) {
				if (type[i].equals("1")) {
					model.addAttribute("change", "1");
					if (!inOffice) {
						redirectAttributes.addAttribute("fd", UUID.randomUUID()
								.toString());
						addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点变更！");
						return "redirect:" + Global.getAdminPath()
								+ "/work/workDealInfo/?repage";
					}
				} else if (type[i].equals("2")) {
					if (reissueType.equals("1")) {
						model.addAttribute("reissue", "1");
					} else {
						model.addAttribute("reissue", "2");
					}
					if (!inOffice) {
						redirectAttributes.addAttribute("fd", UUID.randomUUID()
								.toString());
						addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点补办！");
						return "redirect:" + Global.getAdminPath()
								+ "/work/workDealInfo/?repage";
					}
				} else if (type[i].equals("3")) {
					model.addAttribute("update", "3");
					
					ConfigProduct configProduct = workDealInfo.getConfigProduct();
					String[] years = configChargeAgentDetailService
							.getChargeAgentYears(configProduct.getChargeAgentId(),
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
					
					
					if (!inOffice) { redirectAttributes.addAttribute("fd",
							UUID.randomUUID().toString()); addMessage(redirectAttributes,
									"请到业务办理网点更新！"); return "redirect:" + Global.getAdminPath() +
											"/work/workDealInfo/?repage"; }
					
				} else if (type[i].equals("4")) {
					model.addAttribute("revoke", "4");
					if (!inOffice) {
						redirectAttributes.addAttribute("fd", UUID.randomUUID()
								.toString());
						addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点吊销！");
						return "redirect:" + Global.getAdminPath()
								+ "/work/workDealInfo/?repage";
					}
				} else if (type[i].equals("5")) {
					
					model.addAttribute("reissue", "2");
					
					if (!inOffice) {
						redirectAttributes.addAttribute("fd", UUID.randomUUID()
								.toString());
						addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点补办！");
						return "redirect:" + Global.getAdminPath()
								+ "/work/workDealInfo/?repage";
					}
				}
			}
			if (workDealInfo.getWorkCertInfo() != null) {
				model.addAttribute("workCertApplyInfo", workDealInfo
						.getWorkCertInfo().getWorkCertApplyInfo());
			}
			model.addAttribute("pro", ProductType.productTypeStrMap);
			
			model.addAttribute("user", UserUtils.getUser());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			model.addAttribute("date", sdf.format(new Date()));
			
			ConfigChargeAgent chargeAgent = null;
			if (workDealInfo.getConfigChargeAgentId() != null) {
				chargeAgent = chargeAgentService.get(workDealInfo
						.getConfigChargeAgentId());
				
			}
			// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			
			if (workDealInfo.getPayType() == null) {
				workDealInfo
				.setPayType(Integer.parseInt(chargeAgent.getTempStyle()));
			}
			
			model.addAttribute("workDealInfo", workDealInfo);
			
			// 获得应用下的产品
			List<ConfigProduct> products = configProductService
					.findByAppAndProName(workDealInfo.getConfigApp().getId(),
							workDealInfo.getConfigProduct().getProductName());
			List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
			for (int i = 0; i < products.size(); i++) {
				String ssssi = ProductType.productTypeStrMap.get(products.get(i)
						.getProductName())
						+ "["
						+ (products.get(i).getProductLabel() == 0 ? "通用" : "专用")
						+ "]";
				ProductTypeObj obj = new ProductTypeObj(products.get(i).getId()
						.intValue(), ssssi);
				listProductTypeObjs.add(obj);
			}
			model.addAttribute("proList", listProductTypeObjs);
			
			model.addAttribute("expirationDate",StringHelper.getLastDateOfCurrentYear());
			
			//获得省和市对应self_area表中的id
			if(workDealInfo.getWorkCompany().getProvince()!=null&&!workDealInfo.getWorkCompany().getProvince().isEmpty()){
				String provinceId = selfAreaService.findByAreaName(workDealInfo.getWorkCompany().getProvince()).getAreaId();
				model.addAttribute("provinceId", provinceId);
				if(workDealInfo.getWorkCompany().getCity()!=null&&!workDealInfo.getWorkCompany().getCity().isEmpty()){
					
					String cityId = selfAreaService.findByProvinceName(workDealInfo.getWorkCompany().getCity(),provinceId).getAreaId();
					model.addAttribute("cityId", cityId);
				}
				
			}
			
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
				model.addAttribute("imgUrl", Global.getConfig("images.path"));
				SelfImage selfImage = selfImageService
						.findByApplicationId(workDealInfo.getSelfApplyId());
				workDealInfo.setSelfImage(selfImage);
			}
			if (workDealInfo.getSelfImage() != null) {
				model.addAttribute("imgUrl", Global.getConfig("images.path"));
			}
			
			if (dealInfoTypes.size() == 1) {
				if (dealInfoTypes.get(0).equals("1")) {
					
					ConfigProduct configProduct = workDealInfo.getConfigProduct();
					List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
							.findByProIdAll(configProduct.getId());
					Set<Integer> nameSet = new HashSet<Integer>();
					for (int i = 0; i < boundList.size(); i++) {
						nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
								.getTempStyle()));
					}
					
					model.addAttribute("boundLabelList", nameSet);
					
					List<WorkLog> list = workLogService
							.findByDealInfo(workDealInfo);
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
						nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
								.getTempStyle()));
					}
					
					model.addAttribute("boundLabelList", nameSet);
					
					List<WorkLog> list = workLogService
							.findByDealInfo(workDealInfo);
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
						nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
								.getTempStyle()));
					}
					
					model.addAttribute("boundLabelList", nameSet);
					
					List<WorkLog> list = workLogService
							.findByDealInfo(workDealInfo);
					model.addAttribute("workLog", list);
					if (chargeAgent != null) {
						model.addAttribute("tempStyle", chargeAgent.getTempStyle());
					}
					// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
					return "modules/work/maintain/workDealInfoMaintainUpdate";
				} else if (dealInfoTypes.get(0).equals("4")) {
					ConfigChargeAgent agent = configChargeAgentService
							.get(workDealInfo.getConfigChargeAgentId());
					model.addAttribute("jfMB", agent.getTempName());
					return "modules/work/maintain/workDealInfoMaintainRevoke";
				} /*else if (dealInfoTypes.get(0).equals("5")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// key升级
				model.addAttribute("isKeyUpgrade", 1);
				return "modules/work/maintain/workDealInfoMaintainLost";

			}
				 */
			} else if (dealInfoTypes.size() == 2) {
				if (dealInfoTypes.get(0).equals("1")
						&& dealInfoTypes.get(1).equals("2")) {
					ConfigProduct configProduct = workDealInfo.getConfigProduct();
					List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
							.findByProIdAll(configProduct.getId());
					Set<Integer> nameSet = new HashSet<Integer>();
					for (int i = 0; i < boundList.size(); i++) {
						nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
								.getTempStyle()));
					}
					
					model.addAttribute("boundLabelList", nameSet);
					
					List<WorkLog> list = workLogService
							.findByDealInfo(workDealInfo);
					model.addAttribute("workLog", list);
					
					if (chargeAgent != null) {
						model.addAttribute("tempStyle", chargeAgent.getTempStyle());
					}
					
					// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
					
					return "modules/work/maintain/workDealInfoMaintainChange";
				} else if (dealInfoTypes.get(0).equals("2")
						&& dealInfoTypes.get(1).equals("3")) {
					ConfigProduct configProduct = workDealInfo.getConfigProduct();
					List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
							.findByProIdAll(configProduct.getId());
					Set<Integer> nameSet = new HashSet<Integer>();
					for (int i = 0; i < boundList.size(); i++) {
						nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
								.getTempStyle()));
					}
					model.addAttribute("boundLabelList", nameSet);
					List<WorkLog> list = workLogService
							.findByDealInfo(workDealInfo);
					model.addAttribute("workLog", list);
					if (chargeAgent != null) {
						model.addAttribute("tempStyle", chargeAgent.getTempStyle());
					}
					// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
					return "modules/work/maintain/workDealInfoMaintainUpdate";
				} else if (dealInfoTypes.get(0).equals("1")
						&& dealInfoTypes.get(1).equals("3")) {
					ConfigProduct configProduct = workDealInfo.getConfigProduct();
					List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
							.findByProIdAll(configProduct.getId());
					Set<Integer> nameSet = new HashSet<Integer>();
					for (int i = 0; i < boundList.size(); i++) {
						nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
								.getTempStyle()));
					}
					
					model.addAttribute("boundLabelList", nameSet);
					
					List<WorkLog> list = workLogService
							.findByDealInfo(workDealInfo);
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
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
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
	}
	private String information(String url,WorkDealInfo workDealInfo,Model model ,RedirectAttributes redirectAttributes){
		//类型判断
		Integer dealInfoType = workDealInfo.getDealInfoType();
		Integer dealInfoType1 = workDealInfo.getDealInfoType1();
		Integer dealInfoType2 = workDealInfo.getDealInfoType2();
		
		List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
		
		if(attachs!=null&&attachs.size()>0){
			String imgNames = "";
			for(int i =0;i<attachs.size();i++){
				if(i==0){
					imgNames+=url+"/"+attachs.get(0).getAttachName()+"##"+attachs.get(0).getStatus();
				}else{
					imgNames+=","+url+"/"+attachs.get(i).getAttachName()+"##"+attachs.get(i).getStatus();	
				}
			}
			model.addAttribute("imgNames", imgNames);
		}
		List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
		//区别再次编辑的页面
		model.addAttribute("iseditor", "iseditor");
		model.addAttribute("workLog", list);
		
		if(dealInfoType!=null){
			if(dealInfoType==1){//更新证书
				if(dealInfoType1!=null){
					if(dealInfoType1==2){//遗失补办
						if(dealInfoType2!=null){
							if(dealInfoType2==4){//信息变更
								return typeFormCheck("1,2,3",workDealInfo,model,"1",redirectAttributes);
							}
						}
						//更新+遗失补办
						return typeFormCheck(",2,3",workDealInfo,model,"1",redirectAttributes);
					}else if(dealInfoType1==3){
						if(dealInfoType2!=null){
							if(dealInfoType2==4){//信息变更
								return typeFormCheck("1,2,3",workDealInfo,model,"2",redirectAttributes);
							}
						}
						//更新+损坏更换
						return typeFormCheck(",2,3",workDealInfo,model,"2",redirectAttributes);
					}
				}else{
					if(dealInfoType2!=null){
						if(dealInfoType2==4){//信息变更
							return typeFormCheck("1,3",workDealInfo,model,"1",redirectAttributes);
						}
					}
					return typeFormCheck(",3",workDealInfo,model,"1",redirectAttributes);					
				}
			}else{
				if(workDealInfo.getDealInfoType()==WorkDealInfoType.TYPE_ADD_CERT){
					model.addAttribute("proType", ProductType.productTypeStrMap);
					model.addAttribute("workDealInfo", workDealInfo);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					model.addAttribute("user", UserUtils.getUser());
					model.addAttribute("date", sdf.format(new Date()));
					if (workDealInfo.getWorkCertInfo() != null) {
						model.addAttribute("workCertApplyInfo", workDealInfo.getWorkCertInfo().getWorkCertApplyInfo());
					}
					if (workDealInfo.getDealInfoStatus().equals(WorkDealInfoStatus.STATUS_UPDATE_USER)) {
						model.addAttribute("canEdit", true);
					} else {
						model.addAttribute("canEdit", false);
					}
					
					if (workDealInfo.getIsIxin() != null && workDealInfo.getIsIxin()) {
						
						ConfigProduct configProduct = workDealInfo.getConfigProduct();
						List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
								.findByProIdAll(configProduct.getId());
						Set<Integer> nameSet = new HashSet<Integer>();
						for (int i = 0; i < boundList.size(); i++) {
							nameSet.add(Integer.parseInt(boundList.get(i).getAgent().getTempStyle()));
						}
						
						model.addAttribute("boundLabelList", nameSet);
						ConfigChargeAgent chargeAgent = chargeAgentService.get(workDealInfo.getConfigChargeAgentId());
						model.addAttribute("tempStyle", chargeAgent.getTempStyle());
						
					} else {
						ConfigChargeAgent agent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
						model.addAttribute("jfMB", agent.getTempName());
						
					}
					
					if(workDealInfo.getExpirationDate()!=null){
						model.addAttribute("expirationDate", workDealInfo.getExpirationDate());
					}
					
					return "modules/work/workDealInfoAuditACUForm";
				}
				return null;
			}
		}else{
			if(dealInfoType1!=null){
				if(dealInfoType1==2){//遗失补办
					if(dealInfoType2!=null){
						if(dealInfoType2==4){//+信息变更
							return typeFormCheck("1,2",workDealInfo,model,"1",redirectAttributes);	
						}
					}
					return typeFormCheck(",2",workDealInfo,model,"1",redirectAttributes);	
				}else if(dealInfoType1==3){//损坏更换
					if(dealInfoType2!=null){
						if(dealInfoType2==4){//+信息变更
							return typeFormCheck("1,2",workDealInfo,model,"2",redirectAttributes);	
						}
					}
					return typeFormCheck(",2",workDealInfo,model,"2",redirectAttributes);	
				}
			}else{
				if(dealInfoType2!=null){
					return typeFormCheck("1",workDealInfo,model,"1",redirectAttributes);	
				}
			}
		}
		return null;
	}
	
	
	public String typeFormCheck(String dealType, WorkDealInfo workDealInfo,
			Model model, String reissueType,
			RedirectAttributes redirectAttributes) {
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId()
					.equals(workDealInfo.getConfigApp().getId())) {
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
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点变更！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("2")) {
				if (reissueType.equals("1")) {
					model.addAttribute("reissue", "1");
				} else {
					model.addAttribute("reissue", "2");
				}
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点补办！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("3")) {
				model.addAttribute("update", "3");

				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				String[] years = configChargeAgentDetailService
						.getChargeAgentYears(configProduct.getChargeAgentId(),
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

					
				 if (!inOffice) { redirectAttributes.addAttribute("fd",
				UUID.randomUUID().toString()); addMessage(redirectAttributes,
				"请到业务办理网点更新！"); return "redirect:" + Global.getAdminPath() +
						"/work/workDealInfo/?repage"; }
					 
			} else if (type[i].equals("4")) {
				model.addAttribute("revoke", "4");
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点吊销！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("5")) {

				model.addAttribute("reissue", "2");

				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "此业务应用未授权给当前网点，请到业务办理网点补办！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			}
		}
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo
					.getWorkCertInfo().getWorkCertApplyInfo());
		}
		model.addAttribute("pro", ProductType.productTypeStrMap);

		model.addAttribute("user", UserUtils.getUser());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("date", sdf.format(new Date()));

		ConfigChargeAgent chargeAgent = null;
		if (workDealInfo.getConfigChargeAgentId() != null) {
			chargeAgent = chargeAgentService.get(workDealInfo
					.getConfigChargeAgentId());

		}
		// model.addAttribute("tempStyle", chargeAgent.getTempStyle());

		if (workDealInfo.getPayType() == null) {
			workDealInfo
					.setPayType(Integer.parseInt(chargeAgent.getTempStyle()));
		}

		model.addAttribute("workDealInfo", workDealInfo);

		// 获得应用下的产品
		List<ConfigProduct> products = configProductService
				.findByAppAndProName(workDealInfo.getConfigApp().getId(),
						workDealInfo.getConfigProduct().getProductName());
		List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
		for (int i = 0; i < products.size(); i++) {
			String ssssi = ProductType.productTypeStrMap.get(products.get(i)
					.getProductName())
					+ "["
					+ (products.get(i).getProductLabel() == 0 ? "通用" : "专用")
					+ "]";
			ProductTypeObj obj = new ProductTypeObj(products.get(i).getId()
					.intValue(), ssssi);
			listProductTypeObjs.add(obj);
		}
		model.addAttribute("proList", listProductTypeObjs);

		model.addAttribute("expirationDate",StringHelper.getLastDateOfCurrentYear());
		
		//获得省和市对应self_area表中的id
		if(workDealInfo.getWorkCompany().getProvince()!=null&&!workDealInfo.getWorkCompany().getProvince().isEmpty()){
			String provinceId = selfAreaService.findByAreaName(workDealInfo.getWorkCompany().getProvince()).getAreaId();
			model.addAttribute("provinceId", provinceId);
			if(workDealInfo.getWorkCompany().getCity()!=null&&!workDealInfo.getWorkCompany().getCity().isEmpty()){
				
				String cityId = selfAreaService.findByProvinceName(workDealInfo.getWorkCompany().getCity(),provinceId).getAreaId();
				model.addAttribute("cityId", cityId);
			}
		
		}
		
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
			model.addAttribute("imgUrl", Global.getConfig("images.path"));
			SelfImage selfImage = selfImageService
					.findByApplicationId(workDealInfo.getSelfApplyId());
			workDealInfo.setSelfImage(selfImage);
		}
		if (workDealInfo.getSelfImage() != null) {
			model.addAttribute("imgUrl", Global.getConfig("images.path"));
		}

		if (dealInfoTypes.size() == 1) {
			if (dealInfoTypes.get(0).equals("1")) {

				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				return "modules/work/maintain/workDealInfoMaintainChangeCheck";
			} else if (dealInfoTypes.get(0).equals("2")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainLostCheck";
			} else if (dealInfoTypes.get(0).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdateCheck";
			} else if (dealInfoTypes.get(0).equals("4")) {
				ConfigChargeAgent agent = configChargeAgentService
						.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("jfMB", agent.getTempName());
				return "modules/work/maintain/workDealInfoMaintainRevoke";
			} /*else if (dealInfoTypes.get(0).equals("5")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// key升级
				model.addAttribute("isKeyUpgrade", 1);
				return "modules/work/maintain/workDealInfoMaintainLost";

			}
*/
		} else if (dealInfoTypes.size() == 2) {
			if (dealInfoTypes.get(0).equals("1")
					&& dealInfoTypes.get(1).equals("2")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);

				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}

				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());

				return "modules/work/maintain/workDealInfoMaintainChangeCheck";
			} else if (dealInfoTypes.get(0).equals("2")
					&& dealInfoTypes.get(1).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}
				model.addAttribute("boundLabelList", nameSet);
				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdateCheck";
			} else if (dealInfoTypes.get(0).equals("1")
					&& dealInfoTypes.get(1).equals("3")) {
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
						.findByProIdAll(configProduct.getId());
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < boundList.size(); i++) {
					nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
							.getTempStyle()));
				}

				model.addAttribute("boundLabelList", nameSet);

				List<WorkLog> list = workLogService
						.findByDealInfo(workDealInfo);
				model.addAttribute("workLog", list);
				if (chargeAgent != null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdateChangeCheck";
			}
		} else if (dealInfoTypes.size() == 3) {

			ConfigProduct configProduct = workDealInfo.getConfigProduct();
			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());
			Set<Integer> nameSet = new HashSet<Integer>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(Integer.parseInt(boundList.get(i).getAgent()
						.getTempStyle()));
			}

			model.addAttribute("boundLabelList", nameSet);

			List<WorkLog> list = workLogService.findByDealInfo(workDealInfo);
			model.addAttribute("workLog", list);

			if (chargeAgent != null) {
				model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			}

			// model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			return "modules/work/maintain/workDealInfoMaintainUpdateChangeCheck";
		} else {
			return "modules/work/workDealInfoMaintain";
		}
		
		return "modules/work/workDealInfoMaintain";
	}
	
	
	@RequestMapping(value = "exportExcel")
	public void exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			workDealInfoService.exportExcelData(request, response);
		} catch (Exception ex) {
		}
	}

	@RequestMapping(value = "exportDealPayList")
	public void exportDealPayList(
			WorkDealInfo workDealInfo,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "payMethod", required = false) String payMethod,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "appId", required = false) Long appId)
			throws IOException {
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

			workDealInfo.setWorkPayInfo(workPayInfo);
		}

		List<Long> officeids = Lists.newArrayList();
		Map<Long, Set<String>> Id_paymethod = new HashMap<Long, Set<String>>();
		int index = 0;
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);
		} else {
			if (area != null) {
				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}

			}
		}

		List<WorkDealInfo> list = workDealInfoService.findByDealPay(
				workDealInfo, startTime, endTime, appId, officeids);

		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getWorkPayInfo().getRelationMethod() != null) {
					List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
							.findByPayInfoId(list.get(i).getWorkPayInfo()
									.getId());
					Set<String> payMethods = new LinkedHashSet<String>();
					for (int w = 0; w < workfinancePayinfos.size(); w++) {
						if (workfinancePayinfos.get(w).getFinancePaymentInfo()
								.getPaymentMethod() == 1) {
							payMethods.add("现金付款  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo()
								.getPaymentMethod() == 2) {
							payMethods.add("POS付款  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo()
								.getPaymentMethod() == 3) {
							payMethods.add("银行转账  ");
						}
						if (workfinancePayinfos.get(w).getFinancePaymentInfo()
								.getPaymentMethod() == 4) {
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
								posCountMoney += list.get(i).getWorkPayInfo()
										.getPosMoney();
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
							bankCount += 1;
							if (list.get(i).getWorkPayInfo().getBankMoney() != null) {
								bankCountMoney += list.get(i).getWorkPayInfo()
										.getBankMoney();
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
							moneyCount += 1;
							if (list.get(i).getWorkPayInfo().getMoney() != null) {
								moneyCountMoney += list.get(i).getWorkPayInfo()
										.getMoney();
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
							alipayCount += 1;
							if (list.get(i).getWorkPayInfo().getAlipayMoney() != null) {
								alipayCountMoney += list.get(i)
										.getWorkPayInfo().getAlipayMoney();
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodGov() != null) {
							if (list.get(i).getWorkPayInfo().getMethodGov() == true) {
								govCount += 1;
							}
						}
						if (list.get(i).getWorkPayInfo().getMethodContract() != null) {
							if (list.get(i).getWorkPayInfo()
									.getMethodContract() == true) {
								contractCount += 1;
							}
						}
					} else {
						List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
								.findByPayInfoId(list.get(i).getWorkPayInfo()
										.getId());
						Set<String> payMethods = new LinkedHashSet<String>();
						for (int w = 0; w < workfinancePayinfos.size(); w++) {
							if (workfinancePayinfos.get(w)
									.getFinancePaymentInfo().getPaymentMethod() == 1) {
								payMethods.add("现金付款  ");
								// moneyCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									moneyCountMoney += workfinancePayinfos.get(
											w).getMoney();
								}
							}
							if (workfinancePayinfos.get(w)
									.getFinancePaymentInfo().getPaymentMethod() == 2) {
								payMethods.add("POS付款  ");
								// posCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									posCountMoney += workfinancePayinfos.get(w)
											.getMoney();
								}
							}
							if (workfinancePayinfos.get(w)
									.getFinancePaymentInfo().getPaymentMethod() == 3) {
								payMethods.add("银行转账  ");
								// bankCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									bankCountMoney += workfinancePayinfos
											.get(w).getMoney();
								}
							}
							if (workfinancePayinfos.get(w)
									.getFinancePaymentInfo().getPaymentMethod() == 4) {
								payMethods.add("支付宝付款  ");
								// alipayCount += 1;
								if (workfinancePayinfos.get(w).getMoney() != null) {
									alipayCountMoney += workfinancePayinfos
											.get(w).getMoney();
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
			String dealMsg = (startTime != null && endTime != null ? sdf
					.format(startTime) + "至" + sdf.format(endTime) : "本次统计")
					+ areaOffice.getName()
					+ "区域"
					+ offOffice.getName()
					+ "网点"
					+ moneyCountMoney
					+ "元，POS收款"
					+ posCountMoney
					+ "元，银行转账"
					+ bankCountMoney + "元，支付宝转账" + alipayCountMoney + "元";
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
			rown.createCell(0).setCellValue(
					list.get(i).getWorkCompany().getCompanyName());
			rown.createCell(1).setCellValue(
					list.get(i).getWorkPayInfo().getWorkPayedMoney());
			rown.createCell(2).setCellValue(
					list.get(i).getConfigApp().getAppName());
			rown.createCell(3).setCellValue(
					list.get(i).getWorkUser().getContactName());
			rown.createCell(4).setCellValue(
					list.get(i).getWorkUser().getContactPhone());
			rown.createCell(5).setCellValue(
					list.get(i).getWorkPayInfo().getCreateDate() + "");
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
					Iterator<Map.Entry<Long, Set<String>>> it = Id_paymethod
							.entrySet().iterator();
					while (it.hasNext()) {
						Entry<Long, Set<String>> entry = it.next();
						if (entry.getKey().equals(list.get(i).getId())) {
							rown.createCell(6).setCellValue(
									entry.getValue().toString());
						}
					}
				}

			}
			// else
			// {
			// rown.createCell(6).setCellValue("其他付款方式");
			// }
			rown.createCell(7).setCellValue(
					list.get(i).getCreateBy().getOffice().getAreaName());
			rown.createCell(8).setCellValue(
					list.get(i).getCreateBy().getOffice().getName());
			rown.createCell(9).setCellValue(
					list.get(i).getWorkPayInfo().getCreateBy().getName());
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition",
				"attachment; filename=dealPaylist.xls");
		wb.write(baos);
		byte[] bytes = baos.toByteArray();
		response.setHeader("Content-Length", String.valueOf(bytes.length));
		BufferedOutputStream bos = null;
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(bytes);
		bos.close();
		baos.close();

	}

	@RequestMapping(value = "exportZS")
	public void exportZSNew(
			HttpServletRequest request,
			HttpServletResponse response,
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
			@RequestParam(value = "paymentStartTime", required = false) Date paymentStartTime,
			@RequestParam(value = "paymentEndTime", required = false) Date paymentEndTime,
			@RequestParam(value = "zhizhengStartTime", required = false) Date zhizhengStartTime,
			@RequestParam(value = "zhizhengEndTime", required = false) Date zhizhengEndTime,
			@RequestParam(value = "information", required = false) String information)
			throws UnsupportedEncodingException {
		Map<String, String> queryStr = new HashMap<String, String>();
		queryStr.put("area", area == null ? "" : area.toString());
		queryStr.put("officeId", officeId == null ? "" : officeId.toString());
		queryStr.put("apply", apply == null ? "" : apply.toString());

		queryStr.put("certType", certType == null ? "" : certType.toString());
		queryStr.put("workType", workType == null ? "" : workType.toString());
		queryStr.put("companyName",
				companyName == null ? "" : companyName.toString());
		queryStr.put("organizationNumber", organizationNumber == null ? ""
				: organizationNumber.toString());
		queryStr.put("contactName",
				contactName == null ? "" : contactName.toString());
		queryStr.put("conCertNumber", conCertNumber == null ? ""
				: conCertNumber.toString());
		queryStr.put("keySn", keySn == null ? "" : keySn.toString());
		queryStr.put("createByname",
				createByname == null ? "" : createByname.toString());
		queryStr.put("zhizhengname",
				zhizhengname == null ? "" : zhizhengname.toString());

		queryStr.put("updateByname",
				updateByname == null ? "" : updateByname.toString());
		queryStr.put("payType", payType == null ? "" : payType.toString());
		queryStr.put("s_province", province == null ? "" : province.toString());
		queryStr.put("s_city", city == null ? "" : city.toString());
		queryStr.put("s_county", county == null ? "" : county.toString());
		queryStr.put("year", year == null ? "" : year.toString());
		queryStr.put("payMethod", payMethod == null ? "" : payMethod.toString());
		queryStr.put("luruStartTime", luruStartTime == null ? ""
				: luruStartTime.toString());
		queryStr.put("luruEndTime",
				luruEndTime == null ? "" : luruEndTime.toString());
		queryStr.put("daoqiStartTime", daoqiStartTime == null ? ""
				: daoqiStartTime.toString());
		queryStr.put("daoqiEndTime",
				daoqiEndTime == null ? "" : daoqiEndTime.toString());
		queryStr.put("paymentStartTime", paymentStartTime == null ? ""
				: paymentStartTime.toString());
		queryStr.put("paymentEndTime", paymentEndTime == null ? ""
				: paymentEndTime.toString());
		queryStr.put("zhizhengStartTime", zhizhengStartTime == null ? ""
				: zhizhengStartTime.toString());
		queryStr.put("zhizhengEndTime", zhizhengEndTime == null ? ""
				: zhizhengEndTime.toString());

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
		List<Long> officeids = Lists.newArrayList();
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);
		} else {
			if (area != null) {
				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}

			}
		}

		List<Office> officeList = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
		List<Long> officeIds = new ArrayList<Long>();
		for (int i = 0; i < officeList.size(); i++) {
			officeIds.add(officeList.get(i).getId());
		}
		WorkDealInfoExpViewVO query = new WorkDealInfoExpViewVO();
		query.setWorkDealInfo(workDealInfo);
		query.setAreaId(area);
		query.setOfficeId(officeId);
		query.setApply(apply);
		query.setCertType(certType);
		query.setWorkType(workType);
		query.setYear(year);
		query.setPayMethod(payMethod);
		query.setLuruStartTime(luruStartTime);
		query.setLuruEndTime(luruEndTime);
		query.setDaoqiStartTime(daoqiStartTime);
		query.setDaoqiEndTime(daoqiEndTime);
		query.setPaymentStartTime(paymentStartTime);
		query.setPaymentEndTime(paymentEndTime);
		query.setZhizhengStartTime(zhizhengStartTime);
		query.setZhizhengEndTime(zhizhengEndTime);
		query.setOfficeIds(officeIds);
		query.setOffices(officeids);
		try {
			List<WorkDealInfoExpView> list = new ArrayList<WorkDealInfoExpView>();
			if (workType != null && workType == 5) {
				Page<WorkDealInfoExpView> res = workDealInfoExpViewService
						.findByView(request, response, query, true, true);
				if (res != null && res.getList() != null
						&& res.getList().size() > 0)
					list = res.getList();
			} else {
				Page<WorkDealInfoExpView> res = workDealInfoExpViewService
						.findByView(request, response, query, true, false);
				if (res != null && res.getList() != null
						&& res.getList().size() > 0)
					list = res.getList();
			}

			final String fileName = "WorkDealInfos.csv";

			workDealInfoExpViewService.exportExcel(queryStr, list, fileName,
					information, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void exportZS(
			HttpServletRequest request,
			HttpServletResponse response,
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
			@RequestParam(value = "paymentStartTime", required = false) Date paymentStartTime,
			@RequestParam(value = "paymentEndTime", required = false) Date paymentEndTime,
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
		List<Long> officeids = Lists.newArrayList();
		if (officeId != null && officeId != 0) {
			officeids.add(officeId);

		} else {
			if (area != null) {

				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}

			}
		}
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		WorkDealInfoStatus workDealInfoStatus = new WorkDealInfoStatus();
		List<Office> officeList = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
		List<Long> officeIds = new ArrayList<Long>();
		for (int i = 0; i < officeList.size(); i++) {
			officeIds.add(officeList.get(i).getId());
		}
		try {

			List<WorkDealInfo> list = new ArrayList<WorkDealInfo>();
			if (workType != null && workType == 5) {
				list = workDealInfoService.findCX(workDealInfo, officeids,
						apply, certType, workType, year, luruStartTime,
						luruEndTime, officeIds, daoqiStartTime, daoqiEndTime,
						paymentStartTime, paymentEndTime, zhizhengStartTime,
						zhizhengEndTime);
			}
			list = workDealInfoService.find(workDealInfo, officeids, apply,
					certType, workType, year, luruStartTime, luruEndTime,
					officeIds, daoqiStartTime, daoqiEndTime, paymentStartTime,
					paymentEndTime, zhizhengStartTime, zhizhengEndTime);
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
				dealInfoVo.setCompanyName(dealInfo.getWorkCompany()
						.getCompanyName());
				dealInfoVo.setContactName(dealInfo.getWorkUser()
						.getContactName());
				dealInfoVo.setCertApplyInfoName(dealInfo.getWorkCertInfo()
						.getWorkCertApplyInfo().getName());
				dealInfoVo.setProductName(productType
						.getProductTypeName(Integer.parseInt(dealInfo
								.getConfigProduct().getProductName())));
				if (dealInfo.getDealInfoType() == null) {
					dealInfoType = "";
				} else {
					dealInfoType = workDealInfoType
							.getDealInfoTypeName(dealInfo.getDealInfoType());
				}
				if (dealInfo.getDealInfoType1() == null) {
					dealInfoType1 = "";
				} else {
					dealInfoType1 = workDealInfoType
							.getDealInfoTypeName(dealInfo.getDealInfoType1());
				}
				if (dealInfo.getDealInfoType2() == null) {
					dealInfoType2 = "";
				} else {
					dealInfoType2 = workDealInfoType
							.getDealInfoTypeName(dealInfo.getDealInfoType2());
				}
				if (dealInfo.getDealInfoType3() == null) {
					dealInfoType3 = "";
				} else {
					dealInfoType3 = workDealInfoType
							.getDealInfoTypeName(dealInfo.getDealInfoType3());
				}
				dealInfoVo.setDealInfoType(dealInfoType + "" + dealInfoType1
						+ "" + dealInfoType2 + "" + dealInfoType3);
				dealInfoVo.setKeySn(dealInfo.getKeySn());

				if (dealInfo.getWorkCertInfo().getSignDate() != null) {
					String signDateString = dfm.format(dealInfo
							.getWorkCertInfo().getSignDate());
					dealInfoVo.setSignDateString(signDateString);
				} else {
					dealInfoVo.setSignDateString("");

				}

				if (dealInfo.getAddCertDays() == null) {
					dealInfoVo.setCertDays((dealInfo.getYear() == null ? 0
							: dealInfo.getYear())
							* 365
							+ (dealInfo.getLastDays() == null ? 0 : dealInfo
									.getLastDays()) + "（天）");
				} else {
					dealInfoVo.setCertDays(dealInfo.getYear() * 365
							+ dealInfo.getLastDays()
							+ dealInfo.getAddCertDays() + "（天）");
				}
				String notafterString = dealInfo.getNotafter() == null ? ""
						: df.format(dealInfo.getNotafter());
				dealInfoVo.setNotAfter(notafterString);
				dealInfoVo
						.setDealInfoStatus(workDealInfoStatus.WorkDealInfoStatusMap
								.get(dealInfo.getDealInfoStatus()));
				workDealInfoVos.add(dealInfoVo);
				// System.out.println(workDealInfoVos.size());
			}

			new ExportExcel("业务查询", WorkDealInfoVo.class)
					.setDataList(workDealInfoVos).write(response, fileName)
					.dispose();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "exportMonthPayment")
	public void exportMonthPayment(
			WorkDealInfo workDealInfo,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws IOException, ParseException {
		Date start = null;
		Date end = null;
		if (startTime != null) {
			startTime += "-1";
			start = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
		}
		if (endTime != null){
			endTime += "-31";
			end = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
		}
		
		//前台展示的最外层循环体
		List<ReceivedPaymentsVo> paymentsVos = Lists.newArrayList();
				
		//用于后台循环展示使用
		List<ConfigApp> configAppList = Lists.newArrayList();		
		if(appId == null){
			configAppList = UserUtils.getAppList();
		}else{
			configAppList.add(configAppService.findByAppId(appId));
		}
		//user所能看到的所有officeId
		List<Long> officeids = UserUtils.getOfficeIdList(area, office, null);
		
		for(ConfigApp configApp:configAppList){

			List<MonthPayment> list = workDealInfoService.findByDayPay(start, end,officeids, configApp.getId());
			
			if (list != null && list.size()>0){
				
				ReceivedPaymentsVo paymentsVo = new ReceivedPaymentsVo();
				
				Set<String> month = new LinkedHashSet<String>();
				List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
				List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
				List<Double> payMethodMoneys = Lists.newArrayList();
				Map<String, List<String>> office_payMethod = new HashMap<>();
				
				
				for (int i = 0; i < list.size(); i++) {
					String a = new SimpleDateFormat("yyyy-MM").format(list.get(i).getCreateDate());
					month.add(a);
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
						for (int i = 0; i < list.size(); i++) {
							String mo = new SimpleDateFormat("yyyy-MM").format(list
									.get(i).getCreateDate());
							if (list.get(i).getOfficeId().equals(offs[o])
									&& ((String) months[m]).indexOf(mo) != -1) {
								double postMoney = 0L;
								double bankMoney = 0L;
								double xjMoney = 0L;
								double alipayMoney = 0L;
								if (list.get(i).getRealtionMethod() == null) {
									if (list.get(i).getMethodPos() == 1) {
										if (list.get(i).getPosMoney() >= 0) {
											postMoney = list.get(i).getPosMoney();
										} /*else {
											postMoney = (-list.get(i).getPosMoney());
										}*/
									}
									if (list.get(i).getMethodBank() == 1) {
										if (list.get(i).getBankMoney() >= 0) {
											bankMoney = list.get(i).getBankMoney();
										} /*else {
											bankMoney = (-list.get(i)
													.getBankMoney());
										}*/
									}
									if (list.get(i).getMethodMoney() == 1) {
										if (list.get(i).getMoney() >= 0) {
											xjMoney = list.get(i).getMoney();
										}/* else {
											xjMoney = (-list.get(i).getMoney());
										}*/

									}
									if (list.get(i).getMethodAlipay() == 1) {
										if (list.get(i).getAlipayMoney() >= 0) {
											alipayMoney = list.get(i)
													.getAlipayMoney();
										} /*else {
											alipayMoney = (-list.get(i)
													.getAlipayMoney());
										}*/

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService.findByPayInfoId(list.get(i).getWorkPayInfoId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

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
				if(moneys==0d){
					continue;
				}
				payMethodMoneys.add(moneys);
				
				//应用名称
				paymentsVo.setAppName(configApp.getAppName());
				paymentsVo.setDateMoneyVo(w_m);
				paymentsVo.setOfficePayMethod(office_payMethod);
				paymentsVo.setPayMethodMoneys(payMethodMoneys);
				paymentsVo.setOfficeMoneyVo(o_m);
				paymentsVo.setMonth(month);
				paymentsVos.add(paymentsVo);
			}
	}
			
		int sheetcount = 0;
		for(ReceivedPaymentsVo paymentsVo:paymentsVos){
			Iterator<Map.Entry<String, List<String>>> it =paymentsVo.getOfficePayMethod().entrySet().iterator();
			int count = 0;
			while (it.hasNext()) {
				Entry<String, List<String>> entry = it.next();
				count += entry.getValue().size();
			}
			sheetcount = (sheetcount>=count)?sheetcount:count;	
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
		sheet.addMergedRegion(new Region(0, (short) 0, 0,(short) (2 + sheetcount)));
		
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue(startTime + "至" + endTime + "月回款统计表");
		cell0.setCellStyle(style);
		
		int previous = 0;
		
		for(int i=0;i<paymentsVos.size();i++){
			
			sheet.addMergedRegion(new Region(1+previous, (short) 0, 2+previous, (short) 0));
			sheet.addMergedRegion(new Region(1+previous, (short) 1, 2+previous, (short) 1));
			
			ReceivedPaymentsVo paymentsVo = paymentsVos.get(i);
			HSSFRow row1 = sheet.createRow(1+previous);
			HSSFRow row2 = sheet.createRow(2+previous);
			row1.createCell(0).setCellValue("日期");
			row1.createCell(1).setCellValue("项目名称");
			int index = 0;
			int count = 0;
			int s = 0;
			Iterator<Map.Entry<String, List<String>>> itt = paymentsVo.getOfficePayMethod().entrySet().iterator();
			while (itt.hasNext()) {
				Entry<String, List<String>> entry = itt.next();
			/*	if (entry.getValue().size() == 1) {
					index += (entry.getValue().size());
					sheet.addMergedRegion(new Region(1+previous, (short) (1 + count + 1), 2+previous,(short) (1 + count + 1)));
					row1.createCell(1 + count + 1).setCellValue(entry.getKey());
					count++;
				}*/
				if (entry.getValue().size() >= 1) {
					count += entry.getValue().size();
					sheet.addMergedRegion(new Region(1+previous,(short) (1 + index + 1 + s), 1+previous, (short) (1 + index+ entry.getValue().size() + s)));
					row1.createCell(1 + 1 + index + s).setCellValue(entry.getKey());
					for (int v = 0; v < entry.getValue().size(); v++) {
						row2.createCell(1 + 1 + index + s).setCellValue(
								entry.getValue().get(v));
						s++;
					}
				}
			}
			sheet.addMergedRegion(new Region(1+previous, (short) (2 + count), 2+previous,(short) (2 + count)));
			row1.createCell(2 + count).setCellValue("合计");
			
			Set<String> month = paymentsVo.getMonth();
			Object ms[] = month.toArray();
			for (int m = 0; m < ms.length; m++) {
				int a = 0;
				HSSFRow rown = sheet.createRow(2 + m + 1 + previous);
				rown.createCell(0).setCellValue(ms[m] + "");
				rown.createCell(1).setCellValue(paymentsVo.getAppName());
				Iterator<Map.Entry<String, List<String>>> its = paymentsVo.getOfficePayMethod().entrySet().iterator();
				while (its.hasNext()) {
					Entry<String, List<String>> entry = its.next();
					List<Workoffice_MoneyVo> o_m = paymentsVo.getOfficeMoneyVo();
					for (int om = 0; om < o_m.size(); om++) {
						if (o_m.get(om).getDate().equals(ms[m])
								&& o_m.get(om).getOfficeName().equals(entry.getKey())) {
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
				
				List<WorkDate_MoneVo> w_m = paymentsVo.getDateMoneyVo();
				for (int wm = 0; wm < w_m.size(); wm++) {
					if (w_m.get(wm).getDate() == ms[m]) {
						rown.createCell(2 + a).setCellValue(
								w_m.get(wm).getCountMoney());
					}
				}

			}
			HSSFRow rowz = sheet.createRow(2 + ms.length + 1 + previous);
			rowz.createCell(0).setCellValue("合计");
			rowz.createCell(1).setCellValue(" ");
			
			List<Double> payMethodMoneys = paymentsVo.getPayMethodMoneys();
			
			for (int pm = 0; pm < payMethodMoneys.size(); pm++) {
				rowz.createCell(2 + pm).setCellValue(payMethodMoneys.get(pm));
			}
			
			//previous:之前所占行
			//2:表头所占两行
			//ms.length body所占行数
			//1:合计所占一行
			//1:中间空一行
			previous = previous+2+ms.length+1+1; 
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition",
				"attachment; filename=monthPayment.xls");
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
	public void exportDayPayment(
			WorkDealInfo workDealInfo,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws IOException, ParseException {
		
		//前台展示的最外层循环体
		List<ReceivedPaymentsVo> paymentsVos = Lists.newArrayList();
		
		//用于后台循环展示使用
		List<ConfigApp> configAppList = Lists.newArrayList();		
		if(appId == null){
			configAppList = UserUtils.getAppList();
		}else{
			configAppList.add(configAppService.findByAppId(appId));
		}
		//user所能看到的所有officeId
		List<Long> officeids = UserUtils.getOfficeIdList(area, office, null);

		
		for(ConfigApp configApp:configAppList){
			
			
					
			List<MonthPayment> list = workDealInfoService.findByDayPay(startTime,endTime, officeids, configApp.getId());
			if (list != null&& list.size()>0) {
				
				ReceivedPaymentsVo paymentsVo = new ReceivedPaymentsVo();
				
				Set<String> month = new LinkedHashSet<String>();
				List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();
				List<Workoffice_MoneyVo> o_m = new ArrayList<Workoffice_MoneyVo>();
				List<Double> payMethodMoneys = Lists.newArrayList();
				Map<String, List<String>> office_payMethod = new HashMap<>();
				
				
				for (int i = 0; i < list.size(); i++) {
					String a = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getCreateDate());
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
						
						for (int i = 0; i < list.size(); i++) {
							String mo = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getCreateDate());
							if (list.get(i).getOfficeId().equals(offs[o])&& ((String) months[m]).indexOf(mo) != -1) {
								double postMoney = 0L;
								double bankMoney = 0L;
								double xjMoney = 0L;
								double alipayMoney = 0L;
								if (list.get(i).getRealtionMethod() == null) {
									if (list.get(i).getMethodPos() == 1) {
										if (list.get(i).getPosMoney() >= 0) {
											postMoney = list.get(i).getPosMoney();
										}/* else {
											postMoney = (-list.get(i).getPosMoney());
										}*/
									}
									if (list.get(i).getMethodBank() == 1) {
										if (list.get(i).getBankMoney() >= 0) {
											bankMoney = list.get(i).getBankMoney();
										} /*else {
											bankMoney = (-list.get(i).getBankMoney());
										}*/
									}
									if (list.get(i).getMethodMoney() == 1) {
										if (list.get(i).getMoney() >= 0) {
											xjMoney = list.get(i).getMoney();
										} /*else {
											xjMoney = (-list.get(i).getMoney());
										}*/

									}
									if (list.get(i).getMethodAlipay() == 1) {
										if (list.get(i).getAlipayMoney() >= 0) {
											alipayMoney = list.get(i).getAlipayMoney();
										}/* else {
											alipayMoney = (-list.get(i).getAlipayMoney());
										}*/

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService.findByPayInfoId(list.get(i).getWorkPayInfoId());
									for (int w = 0; w < workfinancePayinfos.size(); w++) {
										if (workfinancePayinfos.get(w).getMoney() >= 0) {
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													xjMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													xjMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													postMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													postMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													bankMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													bankMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

											}
											if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													alipayMoney = workfinancePayinfos.get(w).getMoney();
												} /*else {
													alipayMoney = (-workfinancePayinfos.get(w).getMoney());
												}*/

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
				
				if(moneys==0d){
					continue;
				}
				
				payMethodMoneys.add(moneys);
				
				
				//应用名称
				paymentsVo.setAppName(configApp.getAppName());
				paymentsVo.setDateMoneyVo(w_m);
				paymentsVo.setOfficePayMethod(office_payMethod);
				paymentsVo.setPayMethodMoneys(payMethodMoneys);
				paymentsVo.setOfficeMoneyVo(o_m);
				paymentsVo.setMonth(month);
				paymentsVos.add(paymentsVo);
		
			}	
		}
		
		
		
		int sheetcount = 0;
		
		for(ReceivedPaymentsVo paymentsVo:paymentsVos){
			Iterator<Map.Entry<String, List<String>>> it =paymentsVo.getOfficePayMethod().entrySet().iterator();
			int count = 0;
			while (it.hasNext()) {
				Entry<String, List<String>> entry = it.next();
				count += entry.getValue().size();
			}
			sheetcount = (sheetcount>=count)?sheetcount:count;	
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
		
		
		//第一行 
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		
		cell0.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(startTime)+ "至" + new SimpleDateFormat("yyyy-MM-dd").format(endTime) + "日回款统计表");
		cell0.setCellStyle(style);
		
		
		int previous = 0;
		
		for(int i=0;i<paymentsVos.size();i++){
			
			sheet.addMergedRegion(new Region(1+previous, (short) 0, 2+previous, (short) 0));
			sheet.addMergedRegion(new Region(1+previous, (short) 1, 2+previous, (short) 1));
			
			ReceivedPaymentsVo paymentsVo = paymentsVos.get(i);
			
			HSSFRow row1 = sheet.createRow(1+previous);
			HSSFRow row2 = sheet.createRow(2+previous);
			row1.createCell(0).setCellValue("日期");
			row1.createCell(1).setCellValue("项目名称");
			int index = 0;
			int count = 0;
			int s = 0;
			Iterator<Map.Entry<String, List<String>>> itt = paymentsVo.getOfficePayMethod().entrySet().iterator();
			while (itt.hasNext()) {
				Entry<String, List<String>> entry = itt.next();
				/*if (entry.getValue().size() == 1) {
					index++;
					sheet.addMergedRegion(new Region(1+previous, (short) (1 + count + 1), 2+previous, (short) (1 + count + 1)));
					row1.createCell(1 + count + 1).setCellValue(entry.getKey());
					count++;
				}*/
				if (entry.getValue().size() >= 1) {
					count += entry.getValue().size();
					sheet.addMergedRegion(new Region(1+previous, (short) (1 + index + 1 + s), 1+previous, (short) (1 + index + entry.getValue().size() + s)));
					row1.createCell(1 + 1 + index + s).setCellValue(entry.getKey());
					for (int v = 0; v < entry.getValue().size(); v++) {
						row2.createCell(1 + 1 + index + s).setCellValue(
								entry.getValue().get(v));
						s++;
					}
				}
			}
			sheet.addMergedRegion(new Region(1+previous, (short) (2 + count), 2+previous,(short) (2 + count)));
			row1.createCell(2 + count).setCellValue("合计");
			Object ms[] = paymentsVo.getMonth().toArray();
			for (int m = 0; m < ms.length; m++) {
				int a = 0;
				HSSFRow rown = sheet.createRow(2 + m + 1 +previous);
				rown.createCell(0).setCellValue(ms[m] + "");
				rown.createCell(1).setCellValue(paymentsVo.getAppName());
				Iterator<Map.Entry<String, List<String>>> its = paymentsVo.getOfficePayMethod().entrySet().iterator();
				while (its.hasNext()) {
					Entry<String, List<String>> entry = its.next();
					
					List<Workoffice_MoneyVo> o_m = paymentsVo.getOfficeMoneyVo();
					
					for (int om = 0; om < o_m.size(); om++) {
						if (o_m.get(om).getDate().equals(ms[m])&& o_m.get(om).getOfficeName().equals(entry.getKey())) {
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
				
				List<WorkDate_MoneVo> w_m = paymentsVo.getDateMoneyVo();
				
				for (int wm = 0; wm < w_m.size(); wm++) {
					if (w_m.get(wm).getDate() == ms[m]) {
						rown.createCell(2 + a).setCellValue(w_m.get(wm).getCountMoney());
					}
				}

			}
			HSSFRow rowz = sheet.createRow(2 + ms.length + 1 + previous);
			rowz.createCell(0).setCellValue("合计");
			rowz.createCell(1).setCellValue(" ");
			int j = 0;
			
			List<Double> payMethodMoneys = paymentsVo.getPayMethodMoneys();
			
			for (int pm = 0; pm < payMethodMoneys.size(); pm++) {
				rowz.createCell(2 + j).setCellValue(payMethodMoneys.get(pm));
				j++;
			}
			
			//previous:之前所占行
			//2:表头所占两行
			//ms.length body所占行数
			//1:合计所占一行
			//1:中间空一行
			previous = previous+2+ms.length+1+1; 
			
		}


		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition",
				"attachment; filename=dayPayment.xls");
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
	public void exportProjectPayment(
			WorkDealInfo workDealInfo,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "office", required = false) Long office,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws IOException {
		
		//前台展示的最外层循环体
		List<ReceivedPaymentsVo> paymentsVos = Lists.newArrayList();
				
		//用于后台循环展示使用
		List<ConfigApp> configAppList = Lists.newArrayList();		
		if(appId == null){
			configAppList = UserUtils.getAppList();
		}else{
			configAppList.add(configAppService.findByAppId(appId));
		}
		//user所能看到的所有officeId
		List<Long> officeids = UserUtils.getOfficeIdList(area, office, null);

		

		//循环所有应用
		for(ConfigApp configapp: configAppList){
			List<WorkDealInfo> list = workDealInfoService.findByProjectPay(
					startTime, endTime, officeids, configapp.getId());
			if (list != null && list.size()>0) {
				
				ReceivedPaymentsVo paymentsVo = new ReceivedPaymentsVo();
				
				Map<String, Set<String>> office_District = new TreeMap<>();
				
				
				Map<String, Map<String,List<Double>>> officeDistrictMoney = new TreeMap<>(); 
				Map<String, Map<String,List<String>>> officeDistrictPayMethod = new TreeMap<>(); 
				
				List<WorkDate_MoneVo> w_m = new ArrayList<WorkDate_MoneVo>();			
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
				Set<String> district = new TreeSet<>();//LinkedHashSet<String>();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getWorkCompany().getDistrict() != null) {
						district.add(list.get(i).getWorkCompany().getDistrict());
					} else {
						district.add("其他");
					}
				}

				Long offs[] = offices.toArray(new Long[offices.size()]);
				Object months[] = month.toArray();
				Object districts[] = district.toArray();
				for (int o = 0; o < offs.length; o++) {

					Set<String> dis = new LinkedHashSet<String>();
					
					for (int d = 0; d < districts.length; d++) {
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getWorkCompany().getDistrict() != null) {

								if (list.get(i).getOfficeId().equals(offs[o])&& list.get(i).getWorkCompany().getDistrict().equals(districts[d])) {
									dis.add((String) districts[d]);
								}
							} else {
								if (list.get(i).getOfficeId().equals(offs[o])&& districts[d].equals("其他")) {
									dis.add((String) districts[d]);
								}
							}

						}
					}
					office_District.put((officeService.findById((Long) offs[o])).getName(),dis);

				}
				for (int m = 0; m < months.length; m++) {
					
					for(Long officeId:offs){
						String officeName = officeService.get(officeId).getName();
						for (int d = 0; d < districts.length; d++) {
							
							double countPostMoney = 0L;
							double countBankMoney = 0L;
							double countXjMoney = 0L;
							double countAlipayMoney = 0L;
							double countMoney = 0L;
							
						
							for (int i = 0; i < list.size(); i++) {
								String mo = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getWorkPayInfo().getCreateDate());
								if (list.get(i).getWorkCompany().getDistrict() != null) {
									
									if (((String) months[m]).indexOf(mo) != -1&&officeId.longValue()==list.get(i).getOfficeId().longValue() &&list.get(i).getWorkCompany().getDistrict().equals((String) districts[d])) {
										double postMoney = 0L;
										double bankMoney = 0L;
										double xjMoney = 0L;
										double alipayMoney = 0L;
										if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
											if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
												if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
													postMoney = list.get(i).getWorkPayInfo().getPosMoney();
												} /*else {
													postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
												}*/
											}
											if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
												if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
													bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
												} /*else {
													bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
												}*/
											}
											if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
												if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
													xjMoney = list.get(i).getWorkPayInfo().getMoney();
												} /*else {
													xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
												}*/

											}
											if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
												if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
													alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
												} /*else {
													alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
												}*/

											}
										} else {
											List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
											for (int w = 0; w < workfinancePayinfos.size(); w++) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
														if (workfinancePayinfos.get(w).getMoney() >= 0) {
															xjMoney = workfinancePayinfos.get(w).getMoney();
														} /*else {
															xjMoney = (-workfinancePayinfos.get(w).getMoney());
														}*/

													}
													if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
														if (workfinancePayinfos.get(w).getMoney() >= 0) {
															postMoney = workfinancePayinfos.get(w).getMoney();
														} /*else {
															postMoney = (-workfinancePayinfos.get(w).getMoney());
														}*/

													}
													if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
														if (workfinancePayinfos.get(w).getMoney() >= 0) {
															bankMoney = workfinancePayinfos.get(w).getMoney();
														} /*else {
															bankMoney = (-workfinancePayinfos.get(w).getMoney());
														}*/

													}
													if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
														if (workfinancePayinfos.get(w).getMoney() >= 0) {
															alipayMoney = workfinancePayinfos.get(w).getMoney();
														} /*else {
															alipayMoney = (-workfinancePayinfos.get(w).getMoney());
														}*/

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

								} else {

									if (((String) months[m]).indexOf(mo) != -1&&officeId.longValue()==list.get(i).getOfficeId().longValue()&&(districts[d].equals("其他"))) {
										double postMoney = 0L;
										double bankMoney = 0L;
										double xjMoney = 0L;
										double alipayMoney = 0L;
										if (list.get(i).getWorkPayInfo().getRelationMethod() == null) {
											if (list.get(i).getWorkPayInfo().getMethodPos() == true) {
												if (list.get(i).getWorkPayInfo().getPosMoney() >= 0) {
													postMoney = list.get(i).getWorkPayInfo().getPosMoney();
												} /*else {
													postMoney = (-list.get(i).getWorkPayInfo().getPosMoney());
												}*/
											}
											if (list.get(i).getWorkPayInfo().getMethodBank() == true) {
												if (list.get(i).getWorkPayInfo().getBankMoney() >= 0) {
													bankMoney = list.get(i).getWorkPayInfo().getBankMoney();
												} /*else {
													bankMoney = (-list.get(i).getWorkPayInfo().getBankMoney());
												}*/
											}
											if (list.get(i).getWorkPayInfo().getMethodMoney() == true) {
												if (list.get(i).getWorkPayInfo().getMoney() >= 0) {
													xjMoney = list.get(i).getWorkPayInfo().getMoney();
												} /*else {
													xjMoney = (-list.get(i).getWorkPayInfo().getMoney());
												}*/

											}
											if (list.get(i).getWorkPayInfo().getMethodAlipay() == true) {
												if (list.get(i).getWorkPayInfo().getAlipayMoney() >= 0) {
													alipayMoney = list.get(i).getWorkPayInfo().getAlipayMoney();
												} /*else {
													alipayMoney = (-list.get(i).getWorkPayInfo().getAlipayMoney());
												}*/

											}
										} else {
											List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService.findByPayInfoId(list.get(i).getWorkPayInfo().getId());
											for (int w = 0; w < workfinancePayinfos.size(); w++) {
												if (workfinancePayinfos.get(w).getMoney() >= 0) {
													if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 1) {
														if (workfinancePayinfos.get(w).getMoney() >= 0) {
															xjMoney = workfinancePayinfos.get(w).getMoney();
														} /*else {
															xjMoney = (-workfinancePayinfos.get(w).getMoney());
														}*/

													}
													if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 2) {
														if (workfinancePayinfos.get(w).getMoney() >= 0) {
															postMoney = workfinancePayinfos.get(w).getMoney();
														} /*else {
															postMoney = (-workfinancePayinfos.get(w).getMoney());
														}*/

													}
													if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 3) {
														if (workfinancePayinfos.get(w).getMoney() >= 0) {
															bankMoney = workfinancePayinfos.get(w).getMoney();
														} /*else {
															bankMoney = (-workfinancePayinfos.get(w).getMoney());
														}*/

													}
													if (workfinancePayinfos.get(w).getFinancePaymentInfo().getPaymentMethod() == 4) {
														if (workfinancePayinfos.get(w).getMoney() >= 0) {
															alipayMoney = workfinancePayinfos.get(w).getMoney();
														} /*else {
															alipayMoney = (-workfinancePayinfos.get(w).getMoney());
														}*/

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
							dm.setOfficeName(officeName);
							odms.add(dm);

						}
					}
					

				


				}

				double moneys = 0L;
				
				for(Long officeId:offs){
					Map<String, List<Double>> district_Moneys = new TreeMap<>();
					Map<String, List<String>> district_payMethod = new TreeMap<>();
					String officeName = officeService.get(officeId).getName();
					
					for (int d = 0; d < districts.length; d++) {
						int post = 0;
						int bank = 0;
						int xj = 0;
						int alipay = 0;
						for (int od = 0; od < odms.size(); od++) {
							if (odms.get(od).getDistrictName().equals(districts[d]) && officeName.equals(odms.get(od).getOfficeName()) ) {
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
								if (odms.get(od).getDistrictName().equals(districts[d])&& officeName.equals(odms.get(od).getOfficeName())) {
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
								if (odms.get(od).getDistrictName().equals(districts[d])&& officeName.equals(odms.get(od).getOfficeName())) {
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
								if (odms.get(od).getDistrictName().equals(districts[d])&& officeName.equals(odms.get(od).getOfficeName())) {
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
								if (odms.get(od).getDistrictName().equals(districts[d])&& officeName.equals(odms.get(od).getOfficeName())) {
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
					
					officeDistrictMoney.put(officeName, district_Moneys);
					officeDistrictPayMethod.put(officeName, district_payMethod);
					
				}
				

				
				if(moneys==0d){
					continue;
				}
				
				paymentsVo.setDates(months);
				paymentsVo.setAppName(configapp.getAppName());
			//	paymentsVo.setDistrictMoneys(district_Moneys);
				paymentsVo.setOfficeDistrictMoneyVo(odms);
				paymentsVo.setDateMoneyVo(w_m);
				paymentsVo.setOfficeDistrict(office_District);
			//	paymentsVo.setDistrictPayMethod(district_payMethod);
				paymentsVo.setMoneys(moneys);
				paymentsVo.setMonth(month);
				paymentsVo.setOfficeDistrictMoney(officeDistrictMoney);
				paymentsVo.setOfficeDistrictPayMethod(officeDistrictPayMethod);
				paymentsVos.add(paymentsVo);
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
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(startTime)+ "至" + new SimpleDateFormat("yyyy-MM-dd").format(endTime)+ "项目回款统计表");
		cell0.setCellStyle(style);
		
		int previous = 0;
		
		for(int j=0;j<paymentsVos.size();j++){
			
			

			sheet.addMergedRegion(new Region(1+previous, (short) 0, 3+previous, (short) 0));
			sheet.addMergedRegion(new Region(1+previous, (short) 1, 3+previous, (short) 1));
			
			ReceivedPaymentsVo paymentsVo = paymentsVos.get(j);
			
			HSSFRow row1 = sheet.createRow(1+previous);
			row1.createCell(0).setCellValue("统计时间");
			row1.createCell(1).setCellValue("项目名称");
			HSSFRow row2 = sheet.createRow(2+previous);
			HSSFRow row3 = sheet.createRow(3+previous);
			int ii = 2;
			int aa = 0;
			int i1 = 2;

			
			
			Iterator<Map.Entry<String, Map<String, List<String>>>> off_dis_paymethods = paymentsVo.getOfficeDistrictPayMethod().entrySet().iterator();
			
			int index = 0;
			
			while (off_dis_paymethods.hasNext()) {
				
				int qu = 0;
				Entry<String, Map<String, List<String>>> off_dis_paymethod = off_dis_paymethods.next();
				
				Iterator<Map.Entry<String, List<String>>> dis_paymethods = off_dis_paymethod.getValue().entrySet().iterator();
				
				while(dis_paymethods.hasNext()){
					Entry<String, List<String>> dis_paymethod =dis_paymethods.next();
					
					List<String> paymethods = dis_paymethod.getValue();
					
					for(String paymethod:paymethods){
					
						qu++;
						
					}
				}	
				sheet.addMergedRegion(new Region(1+previous, (short) (2+index), 1+previous, (short) (2+index+qu-1)));
				row1.createCell(2+index).setCellValue(off_dis_paymethod.getKey());
				index = index+qu;	
			}
			
			off_dis_paymethods = paymentsVo.getOfficeDistrictPayMethod().entrySet().iterator();
			index=0;
			while (off_dis_paymethods.hasNext()) {
				
			
				Entry<String, Map<String, List<String>>> off_dis_paymethod = off_dis_paymethods.next();
				
				Iterator<Map.Entry<String, List<String>>> dis_paymethods = off_dis_paymethod.getValue().entrySet().iterator();
				
				while(dis_paymethods.hasNext()){
					
					int qu = 0;
					
					Entry<String, List<String>> dis_paymethod =dis_paymethods.next();
					
					List<String> paymethods = dis_paymethod.getValue();
					
					for(String paymethod:paymethods){
					
						qu++;
						
					}
					
					sheet.addMergedRegion(new Region(2+previous, (short) (2+index), 2+previous, (short) (2+index+qu-1)));
					row2.createCell(2+index).setCellValue(dis_paymethod.getKey());
					index = index+qu;
				}
				
			
				
			}
			
			
		off_dis_paymethods = paymentsVo.getOfficeDistrictPayMethod().entrySet().iterator();
		int qu = 0;
			while (off_dis_paymethods.hasNext()) {
				
				
				Entry<String, Map<String, List<String>>> off_dis_paymethod = off_dis_paymethods.next();
				
				Iterator<Map.Entry<String, List<String>>> dis_paymethods = off_dis_paymethod.getValue().entrySet().iterator();
				
				while(dis_paymethods.hasNext()){
					Entry<String, List<String>> dis_paymethod =dis_paymethods.next();
					
					List<String> paymethods = dis_paymethod.getValue();
					
					for(String paymethod:paymethods){
						sheet.addMergedRegion(new Region(2+previous, (short) (2+qu), 2+previous, (short) (2+qu)));
						row3.createCell(2+qu).setCellValue(paymethod);
						
						qu++;
						
					}
					
					
				}
				
			
				
			}
			
			sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (qu+2)));
			row1.createCell(qu+2).setCellValue("合计");
			sheet.addMergedRegion(new Region(1+previous, (short) (qu+2), 3+previous, (short) (qu+2)));
			Object months[] = paymentsVo.getMonth().toArray();
			for (int m = 0; m < months.length; m++) {
				double countMoneys = 0L;
				int a = 0;
				HSSFRow rown = sheet.createRow(m + 4 +previous);
				rown.createCell(0).setCellValue((String) months[m]);
				rown.createCell(1).setCellValue(paymentsVo.getAppName());
				Iterator<Map.Entry<String, Set<String>>> f_ds = paymentsVo.getOfficeDistrict().entrySet().iterator();
				
				List<Workoffice_district_MoneyVo> odms = paymentsVo.getOfficeDistrictMoneyVo();
				while (f_ds.hasNext()) {
					Entry<String, Set<String>> entry = f_ds.next();
					Object districts[] = entry.getValue().toArray();
					for (int d = 0; d < districts.length; d++) {
						for (int od = 0; od < odms.size(); od++) {
							if (odms.get(od).getDate().equals(months[m])&& entry.getKey().equals(odms.get(od).getOfficeName()) &&odms.get(od).getDistrictName().equals(districts[d])) {
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
				
				rown.createCell(qu+2).setCellValue(countMoneys);
			}
			HSSFRow rowz = sheet.createRow(3 + 1 +  paymentsVo.getMonth().size() + previous);
			rowz.createCell(0).setCellValue("合计");
			Iterator<Map.Entry<String,Map<String,List<Double>>>> office_district_moneys = paymentsVo.getOfficeDistrictMoney().entrySet().iterator();
			int a = 0;
			double money = 0L;
			while (office_district_moneys.hasNext()) {
				Entry<String,Map<String,List<Double>>> office_district_money = office_district_moneys.next();
				Iterator<Map.Entry<String,List<Double>>> district_moneys = office_district_money.getValue().entrySet().iterator();
				
				while(district_moneys.hasNext()){
					
					Entry<String,List<Double>> district_money = district_moneys.next();
					
					List<Double> moneys = district_money.getValue();
					
					for(Double rowmoney:moneys){
						rowz.createCell(2+a).setCellValue(rowmoney);
						money += rowmoney;
						a++;
					}
				}
				
			}
			
			rowz.createCell(2+a).setCellValue(money);
			
			//rowz.createCell(ii).setCellValue(money);
			//previous:之前所占行
			//3:表头所占两行
			//ms.length body所占行数
			//1:合计所占一行
			//1:中间空一行
			previous = previous+3+months.length+1+1; 		
		}
	
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition",
				"attachment; filename=projectPayment.xls");
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
	public void exportAdjustment(FinancePaymentInfo financePaymentInfo,
			Date dkstartTime, Date dkendTime, Date zzstartTime, Date zzendTime,
			HttpServletRequest request, String companyName,
			HttpServletResponse response) throws Exception {
		
		
		User user = UserUtils.getUser();
		//用于前台选择应用使用
		List<ConfigApp> configApps = UserUtils.getAppList();
		//用于后台循环展示使用
		List<ConfigApp> configAppList = Lists.newArrayList();
		
		if(companyName == null){
			configAppList = UserUtils.getAppList();
		}else{
			configAppList.add(configAppService.findByAppname(companyName));
		}
		//前台展示的最外层循环体
		List<ReceivedPaymentsVo> paymentsVos = Lists.newArrayList();

		
		List<Long> officeids = Lists.newArrayList();
		
		
	
		if (zzstartTime == null && zzendTime == null) {
			zzstartTime = new Date();
			zzendTime = new Date();	
		}
		
		//找到当前user的网点权限
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
		
		
		for(ConfigApp configapp: configAppList){
			List<WorkpaymentInfo_dealinfoVo> dealInfoVos = workDealInfoService.findPaymentDeal(financePaymentInfo,configapp.getId(),zzstartTime,zzendTime,dkstartTime,dkendTime,officeids);
			
			if(dealInfoVos.size()>0){
				ReceivedPaymentsVo paymentsVo = new ReceivedPaymentsVo();
				paymentsVo.setPaymentDealInfoVo(dealInfoVos);
				
				paymentsVos.add(paymentsVo);
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
			cell0.setCellValue("到款日期"
					+ new SimpleDateFormat("yyyy-Mm-dd").format(dkstartTime)
					+ "至"
					+ new SimpleDateFormat("yyyy-MM-dd").format(dkendTime)
					+ "日项目调整统计表");
		} else if (dkstartTime != null && zzstartTime != null) {
			cell0.setCellValue("到款日期"
					+ new SimpleDateFormat("yyyy-Mm-dd").format(dkstartTime)
					+ "至"
					+ new SimpleDateFormat("yyyy-MM-dd").format(dkendTime)
					+ " 制证日期"
					+ new SimpleDateFormat("yyyy-Mm-dd").format(zzstartTime)
					+ "至"
					+ new SimpleDateFormat("yyyy-MM-dd").format(zzendTime)
					+ "日项目调整统计表");
		} else if (dkstartTime == null && zzstartTime != null) {
			cell0.setCellValue("制证日期"
					+ new SimpleDateFormat("yyyy-Mm-dd").format(zzstartTime)
					+ "至"
					+ new SimpleDateFormat("yyyy-MM-dd").format(zzendTime)
					+ "日项目调整统计表");
		} else {
			cell0.setCellValue("本月项目调整统计表");
		}
		cell0.setCellStyle(style);
		
		int previous = 0;
		
		for(ReceivedPaymentsVo paymentsVo:paymentsVos){
			
			List<WorkpaymentInfo_dealinfoVo> dealInfoVos = paymentsVo.getPaymentDealInfoVo();
			
			HSSFRow row1 = sheet.createRow(1+previous);
			row1.createCell(0).setCellValue("支付款项录入时间");
			row1.createCell(1).setCellValue("业务使用款项");
			row1.createCell(2).setCellValue("单位名称");
			row1.createCell(3).setCellValue("应用名称");
			row1.createCell(4).setCellValue("业务制证时间");
			row1.createCell(5).setCellValue("备注");
			for (int i = 0; i < dealInfoVos.size(); i++) {
				WorkpaymentInfo_dealinfoVo dealInfoVo = dealInfoVos.get(i);
				HSSFRow rown = sheet.createRow(i + 2 + previous);
				if (dealInfoVo.getDealPayDate() == null) {
					rown.createCell(0).setCellValue("");
				} else {
					rown.createCell(0).setCellValue(dealInfoVo.getDealPayDate() + "");
				}
				if (dealInfoVo.getPayMoney() == null) {
					rown.createCell(1).setCellValue("");
				} else {
					rown.createCell(1).setCellValue(dealInfoVo.getPayMoney() + "");
				}
				if (dealInfoVo.getCompanyName() == null) {
					rown.createCell(2).setCellValue("");
				} else {
					rown.createCell(2).setCellValue(dealInfoVo.getCompanyName());
				}
				if (dealInfoVo.getSignDate() == null) {
					rown.createCell(3).setCellValue("");
				} else {
					rown.createCell(3).setCellValue(dealInfoVo.getAliasName());
				}
				if (dealInfoVo.getSignDate() == null) {
					rown.createCell(4).setCellValue("");

				} else {
					rown.createCell(4).setCellValue(dealInfoVo.getSignDate() + "");
				}
				if (dealInfoVo.getRemarks() == null) {
					rown.createCell(5).setCellValue("");
				} else {
					rown.createCell(5).setCellValue(dealInfoVo.getRemarks());
				}

			}
			
			//previous:之前所占行
			//1:表头所占两行
			//ms.length body所占行数
			//1:中间空一行
			previous = previous+1+dealInfoVos.size()+1; 	
			
		}
		

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition",
				"attachment; filename=projectSchedule.xls");
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
	public void exportYearProjectList(String startTime, String endTime,
			Model model, Long configProjectTypeId, Long appId,
			HttpServletRequest request, HttpServletResponse response)
			throws ParseException, IOException {
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
			List<ConfigApp> configApps = configAppService
					.findByconfigProjectType(configProjectTypeId);
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
		List<WorkDealInfo> list = workDealInfoService.findByProjectYear(startT,
				endT, configappids);
		if (list != null) {
			for (int c = 0; c < configappids.size(); c++) {
				double zmoney = 0;
				WorkDealInfo wdi = new WorkDealInfo();
				List<Double> month_Moneys = Lists.newArrayList();
				for (int m = 0; m < months.size(); m++) {
					double moneys = 0;
					for (int i = 0; i < list.size(); i++) {
						if (m < months.size() - 1) {
							if (list.get(i).getCreateDate().getTime() >= months
									.get(m).getTime()
									&& list.get(i).getCreateDate().getTime() < months
											.get(m + 1).getTime()
									&& list.get(i).getConfigApp().getId()
											.equals(configappids.get(c))) {
								double postMoney = 0.0;
								double bankMoney = 0.0;
								double alipayMoney = 0.0;
								double xjMoney = 0.0;
								
								if (list.get(i).getWorkPayInfo()
										.getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo()
											.getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo()
												.getPosMoney() >= 0) {
											postMoney = list.get(i)
													.getWorkPayInfo()
													.getPosMoney();
										} else {
											postMoney = (-list.get(i)
													.getWorkPayInfo()
													.getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo()
											.getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo()
												.getBankMoney() >= 0) {
											bankMoney = list.get(i)
													.getWorkPayInfo()
													.getBankMoney();
										} else {
											bankMoney = (-list.get(i)
													.getWorkPayInfo()
													.getBankMoney());
										}
									}
									if (list.get(i).getWorkPayInfo()
											.getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo()
												.getMoney() >= 0) {
											xjMoney = list.get(i)
													.getWorkPayInfo()
													.getMoney();
										} else {
											xjMoney = (-list.get(i)
													.getWorkPayInfo()
													.getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo()
											.getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo()
												.getAlipayMoney() >= 0) {
											alipayMoney = list.get(i)
													.getWorkPayInfo()
													.getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i)
													.getWorkPayInfo()
													.getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i)
													.getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos
											.size(); w++) {
										if (workfinancePayinfos.get(w)
												.getMoney() >= 0) {
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													xjMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													postMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													bankMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													alipayMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
										}
									}
								}
								moneys += (postMoney + bankMoney + alipayMoney + xjMoney);
							}
						} else if (m == months.size() - 1) {
							if (list.get(i).getCreateDate().getTime() >= months
									.get(m).getTime()
									&& list.get(i).getConfigApp().getId()
											.equals(configappids.get(c))) {
								double postMoney = 0.0;
								double bankMoney = 0.0;
								double alipayMoney = 0.0;
								double xjMoney = 0.0;
							
								if (list.get(i).getWorkPayInfo()
										.getRelationMethod() == null) {
									if (list.get(i).getWorkPayInfo()
											.getMethodPos() == true) {
										if (list.get(i).getWorkPayInfo()
												.getPosMoney() >= 0) {
											postMoney = list.get(i)
													.getWorkPayInfo()
													.getPosMoney();
										} else {
											postMoney = (-list.get(i)
													.getWorkPayInfo()
													.getPosMoney());
										}
									}
									if (list.get(i).getWorkPayInfo()
											.getMethodBank() == true) {
										if (list.get(i).getWorkPayInfo()
												.getBankMoney() >= 0) {
											bankMoney = list.get(i)
													.getWorkPayInfo()
													.getBankMoney();
										} else {
											bankMoney = (-list.get(i)
													.getWorkPayInfo()
													.getBankMoney());
										}
									}
									if (list.get(i).getWorkPayInfo()
											.getMethodMoney() == true) {
										if (list.get(i).getWorkPayInfo()
												.getMoney() >= 0) {
											xjMoney = list.get(i)
													.getWorkPayInfo()
													.getMoney();
										} else {
											xjMoney = (-list.get(i)
													.getWorkPayInfo()
													.getMoney());
										}

									}
									if (list.get(i).getWorkPayInfo()
											.getMethodAlipay() == true) {
										if (list.get(i).getWorkPayInfo()
												.getAlipayMoney() >= 0) {
											alipayMoney = list.get(i)
													.getWorkPayInfo()
													.getAlipayMoney();
										} else {
											alipayMoney = (-list.get(i)
													.getWorkPayInfo()
													.getAlipayMoney());
										}

									}
								} else {
									List<WorkFinancePayInfoRelation> workfinancePayinfos = workFinancePayInfoRelationService
											.findByPayInfoId(list.get(i)
													.getWorkPayInfo().getId());
									for (int w = 0; w < workfinancePayinfos
											.size(); w++) {
										if (workfinancePayinfos.get(w)
												.getMoney() >= 0) {
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 1) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													xjMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													xjMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 2) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													postMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													postMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 3) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													bankMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													bankMoney = (-workfinancePayinfos
															.get(w).getMoney());
												}

											}
											if (workfinancePayinfos.get(w)
													.getFinancePaymentInfo()
													.getPaymentMethod() == 4) {
												if (workfinancePayinfos.get(w)
														.getMoney() >= 0) {
													alipayMoney = workfinancePayinfos
															.get(w).getMoney();
												} else {
													alipayMoney = (-workfinancePayinfos
															.get(w).getMoney());
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
				configProduct.setConfigApp(configAppService
						.findByAppId(configappids.get(c)));
				List<ConfigProduct> pros = configProductService
						.findByAppId(configProduct);// 获取应用下的产品信息
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
				ConfigApp configapp = configAppService.findByAppId(configappids
						.get(c));
				configapp.setAppName(configapp.getAppName() + proLabel);
				wdi.setConfigApp(configAppService.findByAppId(configappids
						.get(c)));
				month_Moneys.add(zmoney);
				w_months.put(wdi, month_Moneys);
			}
		}
		List<Double> zj = Lists.newArrayList();
		int a = months.size() + 1;
		for (int l = 0; l < a; l++) {
			double aa = 0;
			Iterator<Map.Entry<WorkDealInfo, List<Double>>> itt = w_months
					.entrySet().iterator();
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
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (months
				.size() + 3)));
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
		Iterator<Map.Entry<WorkDealInfo, List<Double>>> it = w_months
				.entrySet().iterator();
		int index = 0;
		int count = 0;
		while (it.hasNext()) {
			Entry<WorkDealInfo, List<Double>> entry = it.next();
			HSSFRow rown = sheet.createRow(2 + index);
			rown.createCell(0).setCellValue(index + 1);
			rown.createCell(1).setCellValue(
					entry.getKey().getConfigApp().getConfigProjectType()
							.getProjectName());
			rown.createCell(2).setCellValue(
					entry.getKey().getConfigApp().getAppName());
			for (int l = 0; l < entry.getValue().size(); l++) {
				rown.createCell(3 + l).setCellValue(entry.getValue().get(l));
			}
			index++;
		}
		sheet.addMergedRegion(new Region(2 + index, (short) 1, 2 + index,
				(short) 2));
		HSSFRow rowz = sheet.createRow(2 + index);
		rowz.createCell(1).setCellValue("项目收入合计");
		for (int i = 0; i < zj.size(); i++) {
			rowz.createCell(3 + i).setCellValue(zj.get(i));
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType(response.getContentType());
		response.setHeader("Content-disposition",
				"attachment; filename=yearProjectSchedule.xls");
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
	public String certUpdateCount(Date date, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		List<ConfigApp> configApps = configAppService.selectAll();
		List<WorkDealInfo> workDealInfos = null;
		List<Object> listSum = new ArrayList<Object>();
		for (ConfigApp configApp : configApps) {
			Map map = new HashMap<>();
			map.put("configAppName", configApp.getAppName());
			// 后3月
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "A3M");
			map.put("aThreeMoth", workDealInfos.size());
			// 后2月
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "A1M");
			map.put("aOneMoth", workDealInfos.size());
			// 前1月
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "B1M");
			map.put("bOneMoth", workDealInfos.size());
			// 前3月
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "B3M");
			map.put("bThreeMoth", workDealInfos.size());
			// 前1年更新率
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "B1Y");
			int bOneYear = workDealInfos.size();
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "MB1Y");
			int mBOneYear = workDealInfos.size();
			if (bOneYear == 0) {
				map.put("probabilityOne", "0%");
			} else {
				map.put("probabilityOne",
						mBOneYear == 0 ? "本年度需更新的证书0张,本年度已更新证书:" + bOneYear
								+ "张" : bOneYear / mBOneYear * 100 + "%");
			}

			// 前2年更新率
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "B2Y");
			int bTwoYear = workDealInfos.size();
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "MB2Y");
			int MbTwoYear = workDealInfos.size();
			if (bTwoYear == 0) {
				map.put("probabilityTwo", "0%");
			} else {
				map.put("probabilityTwo",
						MbTwoYear == 0 ? "本年度需更新的证书0张,本年度已更新证书:" + bTwoYear
								+ "张" : bTwoYear / MbTwoYear * 100 + "%");
			}
			// 前3年更新率
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "B3Y");
			int bThreeYear = workDealInfos.size();
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "MB3Y");
			int mBThreeYear = workDealInfos.size();
			if (bThreeYear == 0) {
				map.put("probabilityThree", "0%");
			} else {
				map.put("probabilityThree",
						mBThreeYear == 0 ? "本年度需更新的证书0张,本年度已更新证书:" + bThreeYear
								+ "张" : bOneYear / mBThreeYear * 100 + "%");
			}

			// 当前时间更新率
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "NEWY");
			int reNew = workDealInfos.size();
			workDealInfos = workDealInfoService.getWorkInfoByApp(date,
					configApp, "MNEWY");
			int mustReNew = workDealInfos.size();
			if (reNew == 0) {
				map.put("probability", "0%");
			} else {
				map.put("probability", mustReNew == 0 ? "本年度需更新的证书0张,本年度已更新证书:"
						+ reNew + "张" : reNew / mustReNew * 100 + "%");
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
	 * @Title: showAgentProduct @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param
	 *         app 应用ID @param productName 产品名称 @param infoType 业务类型 @param
	 *         lable 专用、通用 @return 设定文件 @return String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "showAgentProduct")
	@ResponseBody
	public String showAgentProduct(Long app, String productName, Integer lable,
			Integer infoType) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.findByIdOrLable(
					app, productName, lable);

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
						.findByProIdAllByStyle(configProduct.getId(), jsonMap
								.get("id").toString());

				JSONArray iterList2 = new JSONArray();

				for (int i = 0; i < boundStyleList.size(); i++) {
					JSONObject iter2 = new JSONObject();
					iter2.put("id", boundStyleList.get(i).getId());
					iter2.put("name", boundStyleList.get(i).getAgent()
							.getTempName());
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
	 * @Title: showAgentProduct @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param
	 *         app 应用ID @param productName 产品名称 @param infoType 业务类型 @param
	 *         lable 专用、通用 @return 设定文件 @return String 返回类型 @throws
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
						.findByProIdAllByStyle(configProduct.getId(), jsonMap
								.get("id").toString());

				JSONArray iterList2 = new JSONArray();

				for (int i = 0; i < boundStyleList.size(); i++) {
					JSONObject iter2 = new JSONObject();
					iter2.put("id", boundStyleList.get(i).getId());
					iter2.put("name", boundStyleList.get(i).getAgent()
							.getTempName());
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
	 * @Title: showAgentProduct @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param
	 *         app 应用ID @param productName 产品名称 @param infoType 业务类型 @param
	 *         lable 专用、通用 @return 设定文件 @return String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "setStyleList")
	@ResponseBody
	public String setStyleList(Long app, String productName, Integer lable,
			Integer infoType, String style) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.findByIdOrLable(
					app, productName, lable);
			JSONArray array = new JSONArray();
			List<ConfigChargeAgentBoundConfigProduct> boundStyleList = configChargeAgentBoundConfigProductService
					.findByProIdAllByStyle(configProduct.getId(), style);
			for (int i = 0; i < boundStyleList.size(); i++) {
				JSONObject iter2 = new JSONObject();
				iter2.put("id", boundStyleList.get(i).getId());// 关联表ID
				iter2.put("name", boundStyleList.get(i).getAgent()
						.getTempName());
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
		JSONArray array = new JSONArray();
		try {

			ConfigProduct configProduct = configProductService.get(productId);
			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());

			for (int i = 0; i < boundList.size(); i++) {

				JSONObject obj = new JSONObject();

				obj.put("styleId", boundList.get(i).getAgent().getTempStyle());

				if (i == 0) {
					obj.put("agentId", boundList.get(i).getId());
					obj.put("agentName", boundList.get(i).getAgent()
							.getTempName());
				}

				array.put(obj);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return array.toString();
	}

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "setTemplateList")
	@ResponseBody
	public String setTemplateList(Long productId, Integer infoType, String style) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.get(productId);
			JSONArray array = new JSONArray();
			List<ConfigChargeAgentBoundConfigProduct> boundStyleList = configChargeAgentBoundConfigProductService
					.findByProIdAllByStyle(configProduct.getId(), style);
			for (int i = 0; i < boundStyleList.size(); i++) {
				JSONObject iter2 = new JSONObject();
				iter2.put("id", boundStyleList.get(i).getId());// 关联表ID
				iter2.put("name", boundStyleList.get(i).getAgent()
						.getTempName());
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
			ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService
					.get(boundId);
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
	 * @Title: showYear @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param @param
	 *         productName @param @param lable @param @return 设定文件 @return
	 *         String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "showYear")
	@ResponseBody
	public String showYear(Long app, String productName, Integer lable,
			Integer infoType) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.findByIdOrLable(
					app, productName, lable);

			if (configProduct.getChargeAgentId() != null) {
				if (infoType != null) {
					String[] years = configChargeAgentDetailService
							.getChargeAgentYears(
									configProduct.getChargeAgentId(), infoType);
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
				ConfigChargeAgent chargeAgent = chargeAgentService
						.get(configProduct.getChargeAgentId());
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
	 * @Title: showYearNew @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置) @param @param
	 *         productName @param @param lable @param @return 设定文件 @return
	 *         String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "showYearNew")
	@ResponseBody
	public String showYearNew(Long boundId, Integer infoType) {
		JSONObject json = new JSONObject();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService
					.get(boundId);
			json.put("pos", bound.getAgent().getChargeMethodPos());
			json.put("bank", bound.getAgent().getChargeMethodBank());
			json.put("money", bound.getAgent().getChargeMethodMoney());

			if (bound.getAgent().getIsSupportExpirationDate() == null) {
				json.put("support", false);
			} else {
				if (bound.getAgent().getIsSupportExpirationDate() == 1) {
					json.put("support", true);
					json.put("expirationDate",
							df.format(StringHelper.getLastDateOfCurrentYear()));
				} else {
					json.put("support", false);

				}

			}

			if (bound.getAgent().getSurplusUpdateNum() == null) {
				json.put("avaNum", 0);
			} else {
				json.put("avaNum", bound.getAgent().getSurplusUpdateNum());
			}
			if (infoType != null) {
				String[] years = configChargeAgentDetailService
						.getChargeAgentYears(bound.getAgent().getId(), infoType);
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
	 * @Title: manySave @Description: TODO(走ajax后台保存，批量保存) @param @param model @param @param
	 *         workDealInfoId @param @param redirectAttributes @param @param
	 *         appId @param @param product @param @param dealInfType1 @param @param
	 *         year @param @param yar @param @param companyId @param @param
	 *         companyName @param @param companyType @param @param
	 *         organizationNumber @param @param orgExpirationTime @param @param
	 *         selectLv @param @param comCertificateType @param @param
	 *         comCertficateNumber @param @param comCertficateTime @param @param
	 *         legalName @param @param s_province @param @param s_city @param @param
	 *         s_county @param @param address @param @param companyMobile @param @param
	 *         remarks @param @param workuserId @param @param workType @param @param
	 *         contactName @param @param conCertType @param @param contacEmail @param @param
	 *         conCertNumber @param @param contactPhone @param @param contactTel @param @param
	 *         deal_info_status @param @param recordContent @param @param lable @param @param
	 *         classifying @param @param pName @param @param pEmail @param @param
	 *         pIDCard @param @param contactSex @param @return 设定文件 @return
	 *         String 返回类型 @throws
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "manySave")
	@ResponseBody
	public String manySave(
			Model model,
			Long workDealInfoId,
			RedirectAttributes redirectAttributes,
			Long appId,
			String product,
			Integer dealInfType1,
			Integer year,
			
			Integer certSort,
			
			Date expirationDate, // 到期时间 和年限二选一
			Integer yar, Long companyId, String companyName,String twoLevelCompanyName,
			String companyType, String organizationNumber,
			String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber,
			String comCertficateTime, String legalName, String s_province,
			String s_city, String s_county, String address,
			String companyMobile, String remarks, Long workuserId,
			Integer workType, String contactName, String conCertType,
			String contacEmail, String conCertNumber, String contactPhone,
			String contactTel, String deal_info_status, String recordContent,
			Integer lable, Integer classifying, String pName, String pEmail,
			String pIDCard, String contactSex, String areaRemark,
			Integer agentId, Long agentDetailId,HttpServletRequest request) {
		
		long start = System.currentTimeMillis();
		JSONObject json = new JSONObject();
		try {
			WorkCompany workCompany = workCompanyService.finByNameAndNumber(
					companyName, organizationNumber);

			// 保存单位信息

			workCompany.setCompanyName(companyName);
			workCompany.setTwoLevelCompanyName(twoLevelCompanyName);
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
			 //workUser.setId(workuserId);
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
			
			//保存多证书编号
			workDealInfo.setCertSort(certSort);
			
			ConfigApp configApp = configAppService.get(appId);
		

			ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService
					.get(agentDetailId);

			ConfigProduct configProduct = bound.getProduct();

			
			long start1 = System.currentTimeMillis();
			workCompanyService.save(workCompany);
			long end1 = System.currentTimeMillis();
			log.debug("新增业务保存并再次添加workCompany用时:"+(start1-end1)+"ms");
			
			
			long start2 = System.currentTimeMillis();
			workUserService.save(workUser);
			long end2 = System.currentTimeMillis();
			log.debug("新增业务保存并再次添加workUser用时:"+(start2-end2)+"ms");
			
			workDealInfo.setConfigApp(configApp);
			ConfigCommercialAgent commercialAgent = configAgentAppRelationService
					.findAgentByApp(configApp);
			List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService
					.findByOffice(UserUtils.getUser().getOffice());
			if (configAgentOfficeRelations.size() > 0) {
				workDealInfo.setCommercialAgent(configAgentOfficeRelations.get(
						0).getConfigCommercialAgent());// 劳务关系外键
			}
			workDealInfo.setCommercialAgent(commercialAgent);
			workDealInfo.setWorkUser(workUser);
			workDealInfo.setWorkCompany(workCompany);

			workDealInfo.setConfigProduct(configProduct);
			workDealInfo.setConfigChargeAgentId(bound.getAgent().getId());
			workDealInfo.setPayType(agentId);

			workDealInfo.setDealInfoType(WorkDealInfoType.TYPE_ADD_CERT);
			workDealInfo.setDealInfoType1(dealInfType1);

			// 经信委
			if (year != null && expirationDate == null) {
				workDealInfo.setYear(year);
				workDealInfo.setExpirationDate(null);
			} else {
				workDealInfo
						.setYear(StringHelper.getDvalueYear(expirationDate));
				workDealInfo.setExpirationDate(expirationDate);
			}

			workDealInfo.setClassifying(classifying);
			workDealInfo
					.setDealInfoStatus(WorkDealInfoStatus.STATUS_ENTRY_SUCCESS);
			workDealInfo.setCreateBy(UserUtils.getUser());
			workDealInfo.setCreateDate(new Date());
			workDealInfo.setSvn(workDealInfoService.getSVN(0));
			WorkCompanyHis workCompanyHis = workCompanyService
					.change(workCompany);
			workCompanyHisService.save(workCompanyHis);
			workDealInfo.setWorkCompanyHis(workCompanyHis);
			WorkUserHis workUserHis = workUserService.change(workUser,
					workCompanyHis);
			workUserHisService.save(workUserHis);
			workDealInfo.setWorkUserHis(workUserHis);
			// 保存申请人信息
			WorkCertApplyInfo workCertApplyInfo = new WorkCertApplyInfo();
			WorkCertInfo workCertInfo = new WorkCertInfo();

			if (workDealInfo.getWorkCertInfo() != null) {
				workCertApplyInfo = workDealInfo.getWorkCertInfo()
						.getWorkCertApplyInfo();
			} else {
				workCertApplyInfo = new WorkCertApplyInfo();
			}

			workCertApplyInfo.setName(pName);
			workCertApplyInfo.setEmail(pEmail);
			workCertApplyInfo.setIdCard(pIDCard);

			long start3 = System.currentTimeMillis();
			workCertApplyInfoService.save(workCertApplyInfo);
			long end3 = System.currentTimeMillis();
			log.debug("新增业务保存并再次添加workCertApplyInfo用时:"+(start3-end3)+"ms");
			
			
			// 保存work_cert_info
			if (workDealInfo.getWorkCertInfo() != null) {
				workCertInfo = workDealInfo.getWorkCertInfo();
			} else {
				workCertInfo = new WorkCertInfo();
			}
			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			
			long start4 = System.currentTimeMillis();
			workCertInfoService.save(workCertInfo);
			long end4 = System.currentTimeMillis();
			log.debug("新增业务保存并再次添加workCertInfo用时:"+(start4-end4)+"ms");
			
			workDealInfo.setWorkCertInfo(workCertInfo);
			workDealInfo.setAreaId(UserUtils.getUser().getOffice().getParent()
					.getId());
			workDealInfo.setOfficeId(UserUtils.getUser().getOffice().getId());
			
			workDealInfo.setInputUser(UserUtils.getUser());
			workDealInfo.setInputUserDate(new Date());
			long start5 = System.currentTimeMillis();
			workDealInfoService.save(workDealInfo);
			long end5 = System.currentTimeMillis();
			log.debug("新增业务保存并再次添加workDealInfo用时:"+(start5-end5)+"ms");
			
			//把以前查询出来
			String imgNames=request.getParameter("imgNames");
			List<CommonAttach> befor = attachService.findCommonAttachByWorkDealInfo(workDealInfo.getId());
			Map<String,CommonAttach> map = new HashMap<String,CommonAttach>();//键值对保存 便于查询
			for(CommonAttach c:befor){
				map.put(c.getAttachName(), c);
			}
			if(imgNames!=null&&imgNames.length()>0){
				String [] imgs= imgNames.split(",");
				
				CommonAttach attach = null;
				for(int i=0;i<imgs.length;i++){
					CommonAttach comm = map.get(imgs[i]);
					if(comm!=null){
						map.remove(imgs[i]);
						//以前的图片复制一份保存
						if(workDealInfoId==null){
							CommonAttach newcomm = comm;
							newcomm.setWorkDealInfo(workDealInfo);
							newcomm.setStatus(null);
							attachService.saveAttach(newcomm);
						}else{
							comm.setWorkDealInfo(workDealInfo);
							comm.setStatus(null);
							attachService.saveAttach(comm);
						}
						
					}else{//新图片直接修改workDealInfo
						attach = attachService.findCommonAttachByattachName(imgs[i]);
						if(attach!=null){
							if(workDealInfoId==null){
								CommonAttach newattach = new CommonAttach(attach);
								newattach.setWorkDealInfo(workDealInfo);
								newattach.setStatus(null);
								attachService.saveAttach(newattach);
							}else{
								attach.setWorkDealInfo(workDealInfo);
								attach.setStatus(null);
								attachService.saveAttach(attach);						
							}
						}
					}
				}
			}	
			for(String s:map.keySet()){
				CommonAttach comm = map.get(s);
				comm.setStatus(-1);
				attachService.saveAttach(comm);
			}

			
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
			logUtil.saveSysLog("业务中心", "新增业务批量保存：编号" + workDealInfo.getId()
					+ "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(),
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
		
		long end = System.currentTimeMillis();
		log.debug("新增业务保存并再次添加总用时:"+(end-start)+"ms");
		return json.toString();
	}

	@RequestMapping(value = "findByKeySn")
	@ResponseBody
	public String findByKeySn(String keySn, Long dealId) {
		JSONObject json = new JSONObject();
		try {
			Integer isUserInteger = workDealInfoService.findByKey(keySn);

			if (isUserInteger > 0) {
				// 和吊销的证书做比对
				WorkDealInfo dealInfo = workDealInfoService
						.findByRevokeStatus(keySn);
				if (dealInfo != null) {
					int count = 1;
					while (dealInfo.getPrevId() != null) {
						dealInfo = workDealInfoService.findPreDealInfo(dealInfo
								.getPrevId());
						if (dealInfo.getKeySn().equals(keySn)) {
							count += 1;
						}
					}
					isUserInteger = isUserInteger - count;
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
		log.debug("方法findByKeySnFailInstall:start");
		JSONObject json = new JSONObject();
		
		try {
			Integer isUserInteger = workDealInfoService
					.findByKey(keySn, dealId);
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
		log.debug("方法findByKeySnFailInstall:end");
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
			if (dealInfo.getIsMainTain() != null) {
				if (dealInfo.getIsMainTain().equalsIgnoreCase("selfMainTain")) {
					json.put("isMainTain", 1);
				}
			} else {
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
	public String checkUpdateByIds(String dealInfoIds,String remarkInfo) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			
			
			
			String[] dealInfos = dealInfoIds.split(",");
			
			
			
			List<Long> dealIdList = new ArrayList<Long>();
			for (int i = 0; i < dealInfos.length; i++) {
				if (!dealInfos[i].equals("")) {
					dealIdList.add(Long.parseLong(dealInfos[i]));
				}

			}
			
			
			List<ConfigProduct> products;
			
			if(!StringHelper.isNull(remarkInfo)){
			    products = workDealInfoService.findByDistinkIds(remarkInfo);
			}else{
				
				if(dealInfos.length>=1000){
					json.put("status", 1);
					json.put("isUpdate", 0);

					String html = "数据量过大,请缩小数据范围!";
					json.put("html", html);
					return json.toString();
				}
				
				products = workDealInfoService.findByDistinkIds(dealIdList);
			}
			
			
		
			if (products.size() > 1) {
				json.put("status", 1);
				json.put("isUpdate", 0);

				String html = "选中的业务不为同一个产品，请检查！";
				json.put("html", html);
				return json.toString();
			}
			
			
			

			String html = "";
/*			List<String> companyNames = new ArrayList<>();
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
					tip = "'" + dealInfo.getWorkCompany().getCompanyName()
							+ "'单位的证书未在更新范围内！";
					companyNames.add(tip);
				}

			}
			for (int i = 0; i < companyNames.size(); i++) {
				html += companyNames.get(i);
				if (i < (companyNames.size() - 1)) {
					html += "<br>&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}*/
			if (html.equals("")) {
				json.put("isUpdate", 1);

				Long proIdd = products.get(0).getId();
				List<ConfigChargeAgentBoundConfigProduct> bounds = configChargeAgentBoundConfigProductService
						.findByProductId(proIdd);
				Set<Integer> nameSet = new HashSet<Integer>();
				for (int i = 0; i < bounds.size(); i++) {
					nameSet.add(Integer.parseInt(bounds.get(i).getAgent()
							.getTempStyle()));
				}
				json.put("boundLabelList", nameSet);

				json.put("appIdd", products.get(0).getConfigApp().getId());
				json.put("productNamee", products.get(0).getProductName());
				json.put("updateSize", dealIdList.size());
				json.put("labell", products.get(0).getProductLabel());
				
				json.put("count", dealInfos.length);

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
	public String checkYears(String dealInfoIds, String year)
			throws JSONException {
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
						.getAgentDetail(dealInfo.getConfigChargeAgentId(), 1,
								Integer.parseInt(year));
				String tip = "";
				if (detail == null) {
					ConfigChargeAgent configChargeAgent = configChargeAgentService
							.get(dealInfo.getConfigChargeAgentId());
					tip = "'" + dealInfo.getWorkCompany().getCompanyName()
							+ "'单位绑定'" + configChargeAgent.getTempName()
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
	public String cancelMaintenance(Long id, Integer type, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			WorkDealInfo dealinfo = workDealInfoService.get(id);
			Long certId = dealinfo.getWorkCertInfo().getId();
			Long agnetId = dealinfo.getConfigChargeAgentId();
			Long dealPreId = dealinfo.getPrevId();
			if (dealinfo.getDealInfoType() != null
					&& dealinfo.getDealInfoType().equals(1)) {
				ConfigChargeAgent agentOri = configChargeAgentService
						.get(agnetId);
				agentOri.setReserveUpdateNum(agentOri.getReserveUpdateNum() - 1);
				agentOri.setSurplusUpdateNum(agentOri.getSurplusUpdateNum() + 1);
				configChargeAgentService.save(agentOri);
				ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService
						.findByAgentIdDealId(agnetId, id);
				if (bound != null) {
					configAgentBoundDealInfoService.deleteById(bound.getId());
				}
			}
			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfoAll(id);
			if(attachs!=null&&attachs.size()>0){
				for(CommonAttach c:attachs){					
					attachService.delAttach(c.getId());
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
	public String cancelMaintenanceByReturn(Long id, Model model,
			RedirectAttributes redirectAttributes) {
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
				if (dealInfoList.get(0).equals(1)
						&& dealInfoList.get(1).equals(4)) {
					isOut = true;
				}
			}

			if (isOut) {
				WorkPayInfo payInfo = dealInfo.getWorkPayInfo();
				Set<WorkFinancePayInfoRelation> relations = null;
				if(payInfo!=null){
					relations = payInfo.getWorkFinancePayInfoRelations();
					if (relations!=null&&relations.size() != 0) {
						for (WorkFinancePayInfoRelation relation : relations) {// 退费
							FinancePaymentInfo financePaymentInfo = relation
									.getFinancePaymentInfo();
							financePaymentInfo.setBingdingTimes(financePaymentInfo
									.getBingdingTimes() - 1);
							financePaymentInfo.setResidueMoney(financePaymentInfo
									.getResidueMoney() + relation.getMoney());// 返还金额
							financePaymentInfoService.save(financePaymentInfo);
							workFinancePayInfoRelationService.delete(relation
									.getId());
						}
					}
					Double money = dealInfo.getWorkPayInfo().getReceiptAmount();
					if (money > 0d) {
						ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService
								.findDepotByOffice(
										dealInfo.getCreateBy().getOffice()).get(0);
						// 修改余额
						receiptDepotInfo.setReceiptResidue(receiptDepotInfo
								.getReceiptResidue() + money);
						receiptDepotInfo.setReceiptTotal(receiptDepotInfo
								.getReceiptTotal() + money);
						
						// 创建入库信息
						ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
						receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
						receiptEnterInfo.setNow_Money(Double.valueOf(money));
						receiptEnterInfo.setBeforMoney(receiptEnterInfo
								.getReceiptDepotInfo().getReceiptResidue()
								- Double.valueOf(money));
						receiptEnterInfo.setReceiptMoney(receiptEnterInfo
								.getReceiptDepotInfo().getReceiptResidue());
						receiptEnterInfo.setReceiptType(4);// 退费入库
						receiptEnterInfoService.save(receiptEnterInfo);
						
						logUtil.saveSysLog("更新业务办理重新缴费",
								"库房" + receiptDepotInfo.getReceiptName()
								+ "添加入库信息成功", "");
						receiptDepotInfoService.save(receiptDepotInfo);
					}
					workPayInfoService.delete(dealInfo.getWorkPayInfo().getId());
					dealInfo.setWorkPayInfo(null);
				}


			}

			dealInfo.setDealInfoStatus("5");
			workDealInfoService.save(dealInfo);

			if (dealInfo.getDealInfoType() != null
					&& dealInfo.getDealInfoType().equals(1)) {
				ConfigChargeAgent agentOri = configChargeAgentService
						.get(dealInfo.getConfigChargeAgentId());
				agentOri.setAvailableUpdateNum(agentOri.getAvailableUpdateNum() - 1);
				agentOri.setSurplusUpdateNum(agentOri.getSurplusUpdateNum() + 1);
				configChargeAgentService.save(agentOri);
				ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService
						.findByAgentIdDealId(agentOri.getId(), id);
				if (bound != null) {
					configAgentBoundDealInfoService.deleteById(bound.getId());
				}
			}

			WorkDealInfo dealinfo = workDealInfoService.get(id);
			Long certId = dealinfo.getWorkCertInfo().getId();
			Long dealPreId = dealinfo.getPrevId();
			
			List<CommonAttach> attachs = attachService.findCommonAttachByWorkDealInfoAll(id);
			if(attachs!=null&&attachs.size()>0){
				for(CommonAttach c:attachs){					
					attachService.delAttach(c.getId());
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

	@RequestMapping(value = "updateDealInfos")
	public String updateDealInfos(String dealInfoIds, Integer yearU,
			Date expirationDate,// 经信委 年限和日期二选一
			Long bounddId, String methodPay) {

		
		long start = System.currentTimeMillis();
		
		boolean success = true;
		
		//找到计费策略agent
		ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(bounddId);
		ConfigChargeAgent agent = bound.getAgent();
		
		String[] infoIds = dealInfoIds.split(",");

		String timeSvn = workDealInfoService.getTimeSvn();
		int num = workDealInfoService.getNumSvn(timeSvn);
		
		User user = UserUtils.getUser();
		Office office = user.getOffice();
		
		
		
		Double money = configChargeAgentDetailService.getChargeMoney(agent.getId(), 1, yearU!=null?yearU:StringHelper.getDvalueYear(expirationDate));
		
		List<ConfigAgentOfficeRelation> configAgentOfficeRelations = configAgentOfficeRelationService.findByOffice(office);
		ConfigAgentOfficeRelation configAgentOfficeRelation = null;
		if(configAgentOfficeRelations.size()>0){
			configAgentOfficeRelation = configAgentOfficeRelations.get(0);
		}
		
		List<ReceiptDepotInfo> depotInfos = receiptDepotInfoService.findDepotByOffice(office);
		ReceiptDepotInfo depotInfo = null;
		if(depotInfos.size()>0){
			depotInfo = depotInfos.get(0);
		}
		
		WorkDealInfo workDealInfo1 = null;
		
		log.debug("开始执行:");
		
		
		
		for (int i = 0; i < infoIds.length; i++) {
			
			Long infoId = Long.parseLong(infoIds[i]);
			
			try {
	
				workDealInfo1 = get(infoId);
				if (workDealInfo1.getDelFlag().equals("1")) {
					log.debug("该业务已被删除,不符合更新条件,业务Id为"+infoId);
					continue;
				}
				
				workDealInfoService.executeSingleUpdate(infoId,agent,yearU,expirationDate,methodPay,workDealInfo1,money,user,office,configAgentOfficeRelation,depotInfo,timeSvn,num,i);
				List<BatchUpdateInfoScca> sccas = batchUpdateInfoSccaService.findByDealInfoId(workDealInfo1.getId());
				batchUpdateInfoSccaService.deleteList(sccas);
				
				log.debug("更新成功!业务Id为"+infoId);
				log.debug("已执行"+(i+1)+"条数据");
			} catch (Exception e) {
				
				success = false;
				
                log.debug("在执行第"+(i+1)+"条业务时出现异常,更新失败!业务Id为"+infoId);	
				e.printStackTrace();
			
				
				try {
					Integer availableNum= agent.getAvailableUpdateNum();
					Integer surUpdateNum = agent.getSurplusUpdateNum();
					agent.setAvailableUpdateNum(availableNum+i);
					agent.setSurplusUpdateNum(surUpdateNum - i);
					configChargeAgentService.save(agent);
					
					logUtil.saveSysLog("计费策略模版", "更改剩余数量和使用数量成功!", "");
					
					log.debug("计费策略模板(id:"+agent.getId()+")的剩余更新数量和使用更新数量更新成功! 使用更新数量+"+i+",剩余更新数量-"+i);
					
					
					
					if(depotInfo!=null){
						depotInfo.setReceiptResidue(depotInfo.getReceiptResidue() - money*i);
						depotInfo.setReceiptOut(depotInfo.getReceiptOut() + money*i);
						receiptDepotInfoService.save(depotInfo);
						
						log.debug("发票库存(id:"+depotInfo.getId()+")更新成功! 剩余量-"+(money*i)+",出库量+"+(money*i));
					}
					
					
				} catch (Exception e2) {
					log.debug("异常:计费策略模板的Id为"+agent.getId()+",成功的业务数量为:"+i);
					
					if(depotInfo!=null){
						log.debug("异常:发票库存的Id为"+depotInfo.getId()+",成功的业务数量为:"+i+";每笔业务缴费:"+money);
					}
				}
				
				long end = System.currentTimeMillis();
				log.debug("执行强制结束,用时:"+(end-start));
				break;	
			}			
		}
		
		if(success){
			try {
				Integer availableNum= agent.getAvailableUpdateNum();
				Integer surUpdateNum = agent.getSurplusUpdateNum();
				agent.setAvailableUpdateNum(availableNum+infoIds.length);
				agent.setSurplusUpdateNum(surUpdateNum - infoIds.length);
				configChargeAgentService.save(agent);
				
				logUtil.saveSysLog("计费策略模版", "更改剩余数量和使用数量成功!", "");
				
				log.debug("计费策略模板(id:"+agent.getId()+")的剩余更新数量和使用更新数量更新成功! 使用更新数量+"+infoIds.length+",剩余更新数量-"+infoIds.length);
				
				
				
				if(depotInfo!=null){
					depotInfo.setReceiptResidue(depotInfo.getReceiptResidue() - money*infoIds.length);
					depotInfo.setReceiptOut(depotInfo.getReceiptOut() + money*infoIds.length);
					receiptDepotInfoService.save(depotInfo);
					
					log.debug("发票库存(id:"+depotInfo.getId()+")更新成功! 剩余量-"+(money*infoIds.length)+",出库量+"+(money*infoIds.length));
				}
				
				
			} catch (Exception e2) {
				log.debug("异常:计费策略模板的Id为"+agent.getId()+",成功的业务数量为:"+infoIds.length);
				
				if(depotInfo!=null){
					log.debug("异常:发票库存的Id为"+depotInfo.getId()+",成功的业务数量为:"+infoIds.length+";每笔业务缴费:"+money);
				}
			}
			
			
			long end = System.currentTimeMillis();
			
			log.debug("执行结束,用时:"+(end-start));
			
			return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";	
		}
		
		
		return null;
		
	}

	@RequestMapping(value = "returnDZZ")
	public String returnDZZ(Long dealId) {
		WorkDealInfo dealInfo = workDealInfoService.get(dealId);
		dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_UPDATE_USER);
		workDealInfoService.save(dealInfo);

		return "redirect:" + Global.getAdminPath() + "/work/workDealInfoAudit/";
	}

	/*
	 * 数据全选 批量更新用
	 */
	@RequestMapping(value = "selectAllDataToUpdate")
	@ResponseBody
	public String selectAllDataToUpdate(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "alias", required = false) Long alias,
			@RequestParam(value = "productName", required = false) String productName,
			@RequestParam(value = "dealInfoStatus", required = false) String dealInfoStatus,
			@RequestParam(value = "organizationNumber", required = false) String organizationNumber,
			@RequestParam(value = "keySn", required = false) String keySn,
			@RequestParam(value = "remarkInfo", required = false) String remarkInfo,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "makeCertStartTime", required = false) Date makeCertStartTime,
			@RequestParam(value = "makeCertEndTime", required = false) Date makeCertEndTime,
			Model model) throws Exception {

		JSONObject json = new JSONObject();
		
		WorkDealInfo workDealInfo = new WorkDealInfo();

		workDealInfo.setDealInfoStatus(dealInfoStatus);
		workDealInfo.setKeySn(keySn);
		workDealInfo.setRemarkInfo(remarkInfo);
		WorkCompany workCompany = new WorkCompany();
		workCompany.setCompanyName(companyName);
		workCompany.setOrganizationNumber(organizationNumber);
		workDealInfo.setWorkCompany(workCompany);

		Page<WorkDealInfoListVo> page = workDealInfoService
				.findAllDataToUpdate(new Page<WorkDealInfoListVo>(request,
						response), workDealInfo, startTime, endTime, alias,
						productName, makeCertStartTime, makeCertEndTime);

		List<WorkDealInfoListVo> list = page.getList();

		StringBuffer sb = new StringBuffer();
		for (WorkDealInfoListVo vo : list) {
			if (vo.getDealInfoStatus().equals("7")
					&& StringHelper.getDvalueDay(new Date(), vo.getNotafter()) <= 60) {
				sb.append(vo.getId() + ",");
			}
		}
		sb = sb.replace(sb.length() - 1, sb.length(), "");
		
		json.put("checkIds", sb);
		
		json.put("count", list.size());
		
		return json.toString();

	}

	/*
	 * 数据全选 批量删除用
	 */
	@RequestMapping(value = "deleteAllData")
	@ResponseBody
	public String deleteAllData(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agentId", required = false) Long agentId,
			Model model) throws Exception {
		WorkDealInfo workDealInfo = new WorkDealInfo();
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());
		WorkCompany workCompany = new WorkCompany();
		workCompany.setCompanyName(companyName);
		workDealInfo.setWorkCompany(workCompany);

		List<WorkDealInfo> list = workDealInfoService.findAllDeleteData(
				workDealInfo, startTime, endTime, agentId);

		StringBuffer sb = new StringBuffer();
		for (WorkDealInfo info : list) {
			if (!info.getDealInfoStatus().equals("6")) {
				sb.append(info.getId() + ",");
			}
		}
		sb = sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();

	}
	
	
	
	
	
	/**
	 * 保存证书页面上传来的base64的图片
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "saveUploadImg")
	@ResponseBody
	public String saveUploadImg(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model) throws Exception {
		//参数 文件名 和文件base64字符串
		String file = request.getParameter("_FileName");
		String szBase64 = request.getParameter("szBase64");
		
		//String ParentDirectory = new File(request.getSession().getServletContext().getRealPath("")).getParent();
		//String saveto = ParentDirectory+File.separator+imgFileUpload;

		String saveto = Global.getConfig("images.url");
		
		log.debug("图片保存路径："+saveto);
		if (file != null&&!"".equals(file)&&szBase64!=null&&!"".equals(szBase64)){
			//file = new String(file.getBytes("ISO8859-1"), "UTF-8");
			String serverPath = saveto+File.separator+file;
			File fileout = 	new File(saveto);
			if(!fileout.exists()){
				fileout.mkdir();
			}
			if(Base64ImageTransform.GenerateImage(szBase64, serverPath)){
				CommonAttach attach = new CommonAttach(file, saveto, new Timestamp(System.currentTimeMillis()));
				attachService.saveAttach(attach);
				return "true";
			}
		}
		return "false";
	}
	
	

	
	/**
	 * 生成多证书编号
	 */
	@RequestMapping(value = "generateCertSort")
	@ResponseBody
	public String generateCertSort(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "productName", required = false) Integer productName,
			@RequestParam(value = "orgNum", required = false) String orgNum,
			@RequestParam(value = "conCertNum", required = false) String conCertNum,
			@RequestParam(value = "contactName", required = false) String contactName,
			@RequestParam(value = "companyName", required = false) String companyName,
			Model model) throws Exception {

		Integer certSortInteger = null;
		
		JSONObject json = new JSONObject();
		
		if(productName==1){
			certSortInteger = workDealInfoService.getCertSortByOrganizationNumber(orgNum, productName);	
		}else if(productName==2){
			if(conCertNum!=null&&contactName==null){
				certSortInteger = workDealInfoService.getCertSortByConCertNumber(conCertNum);	
			}else{
				certSortInteger = workDealInfoService.getCertSortByContactName(contactName);
			}
				
		}else if(productName==3){
			if(conCertNum!=null&&contactName==null){
				certSortInteger = workDealInfoService.getCertSortByOrganizationNumber(conCertNum,productName);
			}else{
				certSortInteger = workDealInfoService.getCertSortByCompanyName(contactName,productName);
			}
		}
		
		json.put("certSort", certSortInteger);
		
		return json.toString();

	}	
	
	
}
