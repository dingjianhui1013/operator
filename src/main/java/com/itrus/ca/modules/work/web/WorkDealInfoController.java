/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.config.ConfigError;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.Region;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedBy;
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
import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.AdminPinEncKey;
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
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigAgentOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
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
import com.itrus.ca.modules.profile.service.ConfigRaAccountExtendInfoService;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.service.ItrustProxyUtil;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.AreaService;
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
import com.itrus.ca.modules.work.vo.WorkDealInfoVo;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
	public String list(WorkDealInfo workDealInfo, Date startTime, Date endTime,
			HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes,
			@RequestParam(value = "checkIds", required = false) String checkIds,
			@RequestParam(value = "alias", required = false) Long alias
			) {
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());
		
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("alias",alias);
		
		Page<WorkDealInfo> page = workDealInfoService.find4Apply(
				new Page<WorkDealInfo>(request, response), workDealInfo,
				startTime, endTime , alias);

		if (checkIds!=null) {
    		String[] ids = checkIds.split(",");
    		model.addAttribute("ids", ids);
		}
		model.addAttribute("checkIds", checkIds);
		
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
		return "modules/work/workDealInfoList";
	}

	
	// 批量导入
		@RequestMapping("addAttach")
		@ResponseBody
		public String importFile(
				@RequestParam(value = "fileName", required = true) MultipartFile file)
				throws IllegalStateException, IOException, JSONException {
			JSONObject json = new JSONObject();
			String ifExcel = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));//截取.xlsx或.xls
			if(!(ifExcel.equals(".xls") || ifExcel.equals(".xlsx"))){
				json.put("status", -1);
				json.put("msg", "模板必须为Excel文件");
				return json.toString();
			}
			try{
				json = workDealInfoService.saveExcelDate(file, ifExcel);//解析存储excel文件
			}catch(Exception ex){
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
			HttpServletRequest request, HttpServletResponse response,
			Long financePaymentInfoId, Model model) {
		
		List<ConfigApp> appNames=configAppService.findall();
		Page<WorkFinancePayInfoRelation> financePay = workFinancePayInfoRelationService
				.findByFinance(new Page<WorkFinancePayInfoRelation>(request,
						response), financePaymentInfoId,appName);
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
			
			List<WorkDealInfo> list=workDealInfoService.findByFinanceId(idList);
			
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
				model.addAttribute("pro", ProductType.productTypeStrMap);
				model.addAttribute("infoType",
						WorkDealInfoType.WorkDealInfoTypeMap);

			}
			model.addAttribute("count", list.size());
			model.addAttribute("page", page);
		} else {
			model.addAttribute("count", 0);
			model.addAttribute("page", financePay);
		}
		model.addAttribute("appNames",appNames);
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
		if (officeId != null && officeId != 0) {
			List<Long> appids = Lists.newArrayList();
			List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
					.findAllByOfficeId(officeId);// 通过网店获取引用的id
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
				List<Long> officeids = Lists.newArrayList();
				List<Office> offices = officeService.findByParentId(area);// 根据区域id获取网店id
				if (offices.size() > 0) {
					for (int i = 0; i < offices.size(); i++) {
						officeids.add(offices.get(i).getId());
					}
				} else {
					officeids.add(-1l);
				}

				List<ConfigAppOfficeRelation> appOffices = configAppOfficeRelationService
						.findAllByOfficeId(officeids);// 根据网店id获取应用id
				if (appOffices.size() > 0) {
					for (int i = 0; i < appOffices.size(); i++) {
						appids.add(appOffices.get(i).getConfigApp().getId());
					}
				} else {
					appids.add(-1l);
				}

				List<WorkDealInfo> deals = workDealInfoService
						.findByAppId(appids);// 根据应用id获取dealInfo信息
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

		Page<WorkDealInfo> page = workDealInfoService.findByDealPay(
				new Page<WorkDealInfo>(request, response), workDealInfo,
				startTime, endTime, idList, dealInfoByAreaIds,
				dealInfoByOfficeAreaIds, appId);

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
					if (page.getList().get(i).getWorkPayInfo().getMethodPos() != null) {
						if (page.getList().get(i).getWorkPayInfo()
								.getMethodPos() == true) {
							posCount += 1;
							if (page.getList().get(i).getWorkPayInfo()
									.getPosMoney() != null) {
								posCountMoney += page.getList().get(i)
										.getWorkPayInfo().getPosMoney();
							}
						}
					}
					if (page.getList().get(i).getWorkPayInfo().getMethodMoney() != null) {
						if (page.getList().get(i).getWorkPayInfo()
								.getMethodMoney() == true) {
							moneyCount += 1;
							if (page.getList().get(i).getWorkPayInfo()
									.getMoney() != null) {
								moneyCountMoney += page.getList().get(i)
										.getWorkPayInfo().getMoney();
							}
						}
					}
					if (page.getList().get(i).getWorkPayInfo().getMethodBank() != null) {
						if (page.getList().get(i).getWorkPayInfo()
								.getMethodBank() == true) {
							bankCount += 1;
							if (page.getList().get(i).getWorkPayInfo()
									.getBankMoney() != null) {
								bankCountMoney += page.getList().get(i)
										.getWorkPayInfo().getBankMoney();
							}
						}
					}
					if (page.getList().get(i).getWorkPayInfo()
							.getMethodAlipay() != null) {
						if (page.getList().get(i).getWorkPayInfo()
								.getMethodAlipay() == true) {
							alipayCount += 1;
							if (page.getList().get(i).getWorkPayInfo()
									.getAlipayMoney() != null) {
								alipayCountMoney += page.getList().get(i)
										.getWorkPayInfo().getAlipayMoney();
							}
						}
					}
					if (page.getList().get(i).getWorkPayInfo().getMethodGov() != null) {
						if (page.getList().get(i).getWorkPayInfo()
								.getMethodGov() == true) {
							alipayCount += 1;
						}
					}
					if (page.getList().get(i).getWorkPayInfo()
							.getMethodContract() != null) {
						if (page.getList().get(i).getWorkPayInfo()
								.getMethodContract() == true) {
							contractCount += 1;
						}
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
					+ "网点"
					+ moneyCountMoney
					+ "元，POS收款"
					+ posCountMoney
					+ "元，银行转账"
					+ bankCountMoney + "元，支付宝转账" + alipayCountMoney + "元";
			model.addAttribute("dealMsg", dealMsg);

		}

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
		return "modules/work/statisticalDealPayList";
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
			List<ConfigProduct> products = configProductService
					.findByApp(workDealInfo.getConfigApp().getId());
			List<ProductTypeObj> listProductTypeObjs = new ArrayList<ProductTypeObj>();
			for (int i = 0; i < products.size(); i++) {
				// products.get(i).getProductName();

				String ssssi = ProductType.productTypeStrMap.get(products
						.get(i).getProductName());
				ProductTypeObj obj = new ProductTypeObj(
						Integer.parseInt(products.get(i).getProductName()),
						ssssi);
				listProductTypeObjs.add(obj);
			}
			model.addAttribute("proList", listProductTypeObjs);
		} else {

			model.addAttribute("proList", ProductType.getProductTypeList());
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
	public String save(Model model, RedirectAttributes redirectAttributes,
			Long appId, String product, Integer dealInfoType, Integer year,
			Long workDealInfoId, Integer yar, Long companyId,
			String companyName, String companyType, String organizationNumber,
			String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber,
			String comCertficateTime, String legalName, String s_province,
			String s_city, String s_county, String address,
			String companyMobile, String remarks, Long workuserId,
			Integer workType, String contactName, String conCertType,
			String contacEmail, String conCertNumber, String contactPhone,
			String contactTel, String deal_info_status, String recordContent,
			Integer agentId,Long agentDetailId,
			
			
			
			Integer classifying, String pName, String pEmail,
			String pIDCard, String contactSex, Integer lable, String areaRemark) {
		ConfigApp configApp = configAppService.get(appId);
		WorkCompany workCompany = workCompanyService.finByNameAndNumber(
				companyName, organizationNumber);
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);
//		ConfigProduct configProduct = configProductService.findByIdOrLable(
//				appId, product, lable);
		// 保存单位信息
		ConfigProduct configProduct = bound.getProduct();
		workCompany.setCompanyName(companyName);
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
		workUser.setId(workuserId);
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
		
		//新增时扣减计费策略数量 
		ConfigChargeAgent agent = bound.getAgent();
		if (workDealInfoId==null) {
			Integer reseNum = agent.getReserveNum();
			Integer surNum = agent.getSurplusNum();
			agent.setReserveNum(reseNum+1);
			agent.setSurplusNum(surNum-1);
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
		WorkUserHis workUserHis = workUserService.change(workUser,
				workCompanyHis);
		workUserHisService.save(workUserHis);
		workDealInfo.setWorkUserHis(workUserHis);
		// 保存申请人信息
		WorkCertApplyInfo workCertApplyInfo = null;
		WorkCertInfo workCertInfo = null;
		// if (workDealInfo.getConfigProduct().getProductName().equals("2")
		// || workDealInfo.getConfigProduct().getProductName().equals("3")) {
		//
		// if (workDealInfo.getWorkCertInfo() != null) {
		// workCertApplyInfo = workDealInfo.getWorkCertInfo()
		// .getWorkCertApplyInfo();
		// } else {
		// workCertApplyInfo = new WorkCertApplyInfo();
		// }
		//
		// workCertApplyInfo.setName(pName);
		// workCertApplyInfo.setEmail(pEmail);
		// workCertApplyInfo.setIdCard(pIDCard);
		// workCertApplyInfo.setProvince(workCompany.getProvince());
		// workCertApplyInfo.setCity(workCompany.getCity());
		//
		// workCertApplyInfoService.save(workCertApplyInfo);
		// // 保存work_cert_info
		// if (workDealInfo.getWorkCertInfo() != null) {
		// workCertInfo = workDealInfo.getWorkCertInfo();
		// } else {
		// workCertInfo = new WorkCertInfo();
		// }
		// workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		// workCertInfoService.save(workCertInfo);
		// workDealInfo.setWorkCertInfo(workCertInfo);
		// } else {
		if (workDealInfo.getWorkCertInfo() != null) {
			workCertApplyInfo = workDealInfo.getWorkCertInfo()
					.getWorkCertApplyInfo();
		} else {
			workCertApplyInfo = new WorkCertApplyInfo();
		}
		// workCertApplyInfo.setName(workCompany.getCompanyName());
		// workCertApplyInfo.setEmail(workUser.getContactEmail());
		// workCertApplyInfo.setMobilePhone(workCompany.getCompanyMobile());
		// workCertApplyInfo.setProvince(workCompany.getProvince());
		// workCertApplyInfo.setCity(workCompany.getCity());

		workCertApplyInfo.setName(pName);
		workCertApplyInfo.setEmail(pEmail);
		workCertApplyInfo.setIdCard(pIDCard);
		// workCertApplyInfo.setProvince(workCompany.getProvince());
		// workCertApplyInfo.setCity(workCompany.getCity());

		workCertApplyInfoService.save(workCertApplyInfo);

		if (workDealInfo.getWorkCertInfo() != null) {
			workCertInfo = workDealInfo.getWorkCertInfo();
		} else {
			workCertInfo = new WorkCertInfo();
		}
		workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
		workCertInfoService.save(workCertInfo);
		workDealInfo.setWorkCertInfo(workCertInfo);
		// }
		// List<ConfigAgentOfficeRelation> li =
		// configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		// if (li.size()>0) {
		// workDealInfo.setCommercialAgent(li.get(0).getConfigCommercialAgent());
		// }
		
		workDealInfo.setInputUser(UserUtils.getUser());
		workDealInfo.setInputUserDate(new Date());
		
		workDealInfoService.save(workDealInfo);
		
		if (workDealInfoId==null) {
			ConfigAgentBoundDealInfo dealInfoBound = new ConfigAgentBoundDealInfo();
			dealInfoBound.setDealInfo(workDealInfo);
			ConfigChargeAgent agentBound =  configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
			dealInfoBound.setAgent(agentBound);
			configAgentBoundDealInfoService.save(dealInfoBound);
			logUtil.saveSysLog("计费策略模版", "计费策略模版："+agent.getId()+"--业务编号："+workDealInfo.getId()+"--关联成功!", "");
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
					workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType
							.getWorkType(WorkDealInfoType.TYPE_OPEN_ACCOUNT),
					null);
			model.addAttribute("type0", money);
		}
		// 按照业务类型查询费用
		if (workDealInfo.getDealInfoType() != null) {
			Double money = configChargeAgentDetailService
					.getChargeMoney(workDealInfo.getConfigChargeAgentId(), 
							WorkDealInfoType.getWorkType(workDealInfo.getDealInfoType()),
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
					workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType.getWorkType(workDealInfo
							.getDealInfoType1()), workDealInfo.getYear());
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
					workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType.getWorkType(workDealInfo
							.getDealInfoType2()), workDealInfo.getYear());
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
					workDealInfo.getConfigChargeAgentId(),
					WorkDealInfoType.getWorkType(workDealInfo
							.getDealInfoType3()), workDealInfo.getYear());
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
		
		ArrayList<Integer> dealInfoTypes = new ArrayList<Integer>();
		if (workDealInfo.getDealInfoType()!=null) {
			dealInfoTypes.add(workDealInfo.getDealInfoType());
		}
		if(workDealInfo.getDealInfoType1()!=null){
			dealInfoTypes.add(workDealInfo.getDealInfoType1());
		}
		if(workDealInfo.getDealInfoType2()!=null){
			dealInfoTypes.add(workDealInfo.getDealInfoType2());
		}
		if(workDealInfo.getDealInfoType3()!=null){
			dealInfoTypes.add(workDealInfo.getDealInfoType3());
		}
		
		if (dealInfoTypes.size()==1) {
			if (dealInfoTypes.get(0).equals(1)) {
				model.addAttribute("isOneUpdate", "yes");
			}else{
				model.addAttribute("isOneUpdate", "no");
			}
		}else{
			model.addAttribute("isOneUpdate", "no");
		}
		
		return "modules/work/workDealInfoMaintainLoad";
	}

	
	/**
	 * 返回基本信息界面
	 */
	@RequestMapping(value = "typeFormReturnUpdate")
	public String typeFormReturnUpdate(String dealType, WorkDealInfo workDealInfo, Model model, String reissueType, RedirectAttributes redirectAttributes){
		boolean inOffice = false;
		
		 workDealInfo.setDealInfoStatus("15");
		
		workDealInfoService.save(workDealInfo);
		
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId().equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}
			if (dealType.equals("3")) {
				model.addAttribute("update", "3");
				ConfigProduct configProduct = workDealInfo.getConfigProduct();
				String[] years = configChargeAgentDetailService.getChargeAgentYears(configProduct.getChargeAgentId(), WorkDealInfoType.TYPE_UPDATE_CERT);
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
				
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID().toString());
					addMessage(redirectAttributes, "请到业务办理网点更新！");
					return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/?repage";
				}
			}
		if (workDealInfo.getWorkCertInfo() != null) {
			model.addAttribute("workCertApplyInfo", workDealInfo
					.getWorkCertInfo().getWorkCertApplyInfo());
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
		
		
		if (dealType.indexOf("3")>=0) {
			
			model.addAttribute("isOK","isYes");
		}else{
			
			model.addAttribute("isOK","isNo");
			
		}
		model.addAttribute("dealType", dealType);
		return "modules/work/maintain/workDealInfoMaintainReturnUpdate";
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
	public String temporarySave(Model model, Long workDealInfoId,
			RedirectAttributes redirectAttributes, Long appId, String product,
			Integer dealInfType1, Integer year, Integer yar, Long companyId,
			String companyName, String companyType, String organizationNumber,
			String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber,
			String comCertficateTime, String legalName, String s_province,
			String s_city, String s_county, String address,
			String companyMobile, String remarks, Long workuserId,
			Integer workType, String contactName, String conCertType,
			String contacEmail, String conCertNumber, String contactPhone,
			String contactTel, String deal_info_status, String recordContent,
			Integer lable, Integer classifying, String pName, String pEmail,
			String pIDCard, String contactSex, String areaRemark,Integer agentId, Long agentDetailId) {

		//Integer agentId,Long agentDetailId,
		
		
		WorkCompany workCompany = workCompanyService.finByNameAndNumber(
				companyName, organizationNumber);

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
		
		ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);

		ConfigProduct configProduct = bound.getProduct();
//		
//		ConfigProduct configProduct = configProductService.findByIdOrLable(
//				appId, product, lable);

		
		
		
		workCompanyService.save(workCompany);
		workUserService.save(workUser);

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
		WorkUserHis workUserHis = workUserService.change(workUser,
				workCompanyHis);
		workUserHisService.save(workUserHis);
		workDealInfo.setWorkUserHis(workUserHis);
		// 保存申请人信息
		WorkCertApplyInfo workCertApplyInfo = null;
		WorkCertInfo workCertInfo = null;
		if (workDealInfo.getConfigProduct().getProductName().equals("2")
				|| workDealInfo.getConfigProduct().getProductName().equals("3")) {

			if (workDealInfo.getWorkCertInfo() != null) {
				workCertApplyInfo = workDealInfo.getWorkCertInfo()
						.getWorkCertApplyInfo();
			} else {
				workCertApplyInfo = new WorkCertApplyInfo();
			}

			workCertApplyInfo.setName(pName);
			workCertApplyInfo.setEmail(pEmail);
			workCertApplyInfo.setIdCard(pIDCard);
			workCertApplyInfo.setProvince(workCompany.getProvince());
			workCertApplyInfo.setCity(workCompany.getCity());

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
		} else {
			if (workDealInfo.getWorkCertInfo() != null) {
				workCertApplyInfo = workDealInfo.getWorkCertInfo()
						.getWorkCertApplyInfo();
			} else {
				workCertApplyInfo = new WorkCertApplyInfo();
			}
			workCertApplyInfo.setName(workCompany.getCompanyName());
			workCertApplyInfo.setEmail(workUser.getContactEmail());
			workCertApplyInfo.setMobilePhone(workCompany.getCompanyMobile());
			workCertApplyInfo.setProvince(workCompany.getProvince());
			workCertApplyInfo.setCity(workCompany.getCity());
			workCertApplyInfoService.save(workCertApplyInfo);
			if (workDealInfo.getWorkCertInfo() != null) {
				workCertInfo = workDealInfo.getWorkCertInfo();
			} else {
				workCertInfo = new WorkCertInfo();
			}

			workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
			workCertInfoService.save(workCertInfo);
			workDealInfo.setWorkCertInfo(workCertInfo);
		}
		// List<ConfigAgentOfficeRelation> lis =
		// configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
		// if (lis.size()>0) {
		// workDealInfo.setCommercialAgent(lis.get(0).getConfigCommercialAgent());
		// }
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
		logUtil.saveSysLog("业务中心", "新增业务暂时保存：编号" + workDealInfo.getId()
				+ "单位名称：" + workDealInfo.getWorkCompany().getCompanyName(), "");

		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}

	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		workDealInfoService.delete(id);
		WorkDealInfo workDealInfo = workDealInfoService.get(id);
		if(workDealInfo.getDealInfoType()!=null){
			Long agentId = workDealInfo.getConfigChargeAgentId();
			ConfigChargeAgent agent =  configChargeAgentService.get(agentId);
			agent.setSurplusUpdateNum(agent.getSurplusUpdateNum()+1);
			agent.setReserveUpdateNum(agent.getReserveUpdateNum()-1);
			configChargeAgentService.save(agent);
			
			ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agent.getId(),id);
			if (bound!=null) {
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
		if(workDealInfo.getDealInfoType()!=null){
			Long agentId = workDealInfo.getConfigChargeAgentId();
			ConfigChargeAgent agent =  configChargeAgentService.get(agentId);
			agent.setSurplusNum(agent.getSurplusNum()+1);
			agent.setReserveNum(agent.getReserveNum()-1);
			configChargeAgentService.save(agent);
			
			ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agent.getId(),id);
			if (bound!=null) {
				configAgentBoundDealInfoService.deleteById(bound.getId());
			}
		}

	//	return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
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
				lis.add(con.getProductName());
			}
			set.addAll(lis);
			for (Iterator it = set.iterator(); it.hasNext();) {
				String productName = (String) it.next();
				if (productName.equals("1")) {
					json.put("product1", true);
				}
				if (productName.equals("2")) {
					json.put("product2", true);
				}
				if (productName.equals("3")) {
					json.put("product3", true);
				}
				if (productName.equals("4")) {
					json.put("product4", true);
				}
				if (productName.equals("5")) {
					json.put("product5", true);
				}
				if (productName.equals("6")) {
					json.put("product6", true);
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
	public String paymentList(String payInfoIds)
			throws Exception {
		String[] ids = payInfoIds.split(",");
		List<Long> idsL = new ArrayList<Long>();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i]!=null && !ids[i].equals("")) {
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

	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "businessQuery")
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
			@RequestParam(value = "jianzhengStartTime", required = false) Date jianzhengStartTime,
			@RequestParam(value = "jianzhengEndTime", required = false) Date jianzhengEndTime,
			@RequestParam(value = "zhizhengStartTime", required = false) Date zhizhengStartTime,
			@RequestParam(value = "zhizhengEndTime", required = false) Date zhizhengEndTime,
			Model model) {
		
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

		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<Office> officeList = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
		Calendar calendar = Calendar.getInstance();
	/*	if (endTime != null) {
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);
		}*/
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>() ;
		if (zhizhengStartTime!=null&&zhizhengEndTime!=null) {
			certInfoList =  workCertInfoService.findZhiZhengTime(zhizhengStartTime, zhizhengEndTime);
		}
		Page<WorkDealInfo> page = workDealInfoService.find(
				new Page<WorkDealInfo>(request, response), workDealInfo, area,
				officeId, apply, certType, workType, year, luruStartTime,
				luruEndTime, officeList, daoqiStartTime, daoqiEndTime, jianzhengStartTime, 
				jianzhengEndTime,certInfoList);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus",
				WorkDealInfoStatus.WorkDealInfoStatusMap);
		model.addAttribute("certTypes", productType.getProductTypeList());
		model.addAttribute("workTypes", workDealInfoType.getProductTypeListNew());

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
		return (int) ((notAfterLong - nowLong) / MILL);
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
			}// cMXu5u1wKtBO8j+UW881hA==
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
		boolean inOffice = false;
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
				.findAllByOfficeId(UserUtils.getUser().getOffice().getId());
		for (ConfigAppOfficeRelation appOffice : configAppOfficeRelations) {
			if (appOffice.getConfigApp().getId()
					.equals(workDealInfo.getConfigApp().getId())) {
				inOffice = true;
			}
		}

		String[] type = dealType.replace(",", " ").split(" ");
		for (int i = 0; i < type.length; i++) {
			if (type[i].equals("1")) {
				model.addAttribute("change", "1");
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "请到业务办理网点变更！");
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
					addMessage(redirectAttributes, "请到业务办理网点补办！");
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

				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "请到业务办理网点更新！");
					return "redirect:" + Global.getAdminPath()
							+ "/work/workDealInfo/?repage";
				}
			} else if (type[i].equals("4")) {
				model.addAttribute("revoke", "4");
				if (!inOffice) {
					redirectAttributes.addAttribute("fd", UUID.randomUUID()
							.toString());
					addMessage(redirectAttributes, "请到业务办理网点吊销！");
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
		model.addAttribute("workDealInfo", workDealInfo);
		ConfigChargeAgent chargeAgent = null;
		if (workDealInfo.getConfigChargeAgentId()!=null) {
			chargeAgent = chargeAgentService.get(workDealInfo
					.getConfigChargeAgentId());
			
		}
		//model.addAttribute("tempStyle", chargeAgent.getTempStyle());
		

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
			if (type[i]!=null&&!type[i].equals("")) {
				dealInfoTypes.add(type[i]);
			}
		}

		if (dealInfoTypes.size()==1) {
			if(dealInfoTypes.get(0).equals("1")){
				
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
				
				if (chargeAgent!=null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				
				
				
				
				return "modules/work/maintain/workDealInfoMaintainChange";
			}else if(dealInfoTypes.get(0).equals("2")){
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
				if (chargeAgent!=null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				
				//model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainLost";
			}else if(dealInfoTypes.get(0).equals("3")){
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
				if (chargeAgent!=null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				//model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdate";
			}else if(dealInfoTypes.get(0).equals("4")){
				ConfigChargeAgent agent =  configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
				model.addAttribute("jfMB", agent.getTempName());
				return "modules/work/maintain/workDealInfoMaintainRevoke";
			}
			
			
			
		}else if(dealInfoTypes.size()==2){
			if(dealInfoTypes.get(0).equals("1")&&dealInfoTypes.get(1).equals("2")){
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
				
				if (chargeAgent!=null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				
				//model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				
				return "modules/work/maintain/workDealInfoMaintainChange";
			}else if(dealInfoTypes.get(0).equals("2")&&dealInfoTypes.get(1).equals("3")){
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
				if (chargeAgent!=null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				//model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdate";
			}else if(dealInfoTypes.get(0).equals("1")&&dealInfoTypes.get(1).equals("3")){
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
				if (chargeAgent!=null) {
					model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				}
				//model.addAttribute("tempStyle", chargeAgent.getTempStyle());
				return "modules/work/maintain/workDealInfoMaintainUpdateChange";
			}
		}else if(dealInfoTypes.size()==3){
			
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
			
			if (chargeAgent!=null) {
				model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			}
			
			//model.addAttribute("tempStyle", chargeAgent.getTempStyle());
			return "modules/work/maintain/workDealInfoMaintainUpdateChange";
		}else{
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
	
	@RequestMapping(value = "exportZS")
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
			@RequestParam(value = "jianzhengStartTime", required = false) Date jianzhengStartTime,
			@RequestParam(value = "jianzhengEndTime", required = false) Date jianzhengEndTime,
			@RequestParam(value = "zhizhengStartTime", required = false) Date zhizhengStartTime,
			@RequestParam(value = "zhizhengEndTime", required = false) Date zhizhengEndTime
			) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WorkDealInfo workDealInfo=new WorkDealInfo();
		WorkCompany company = new WorkCompany();
		company.setOrganizationNumber(organizationNumber);
		company.setCompanyName(companyName);
		company.setCity(city);
		company.setDistrict(county);
		company.setProvince(province);
		WorkUser workUser=new WorkUser();
		workUser.setConCertNumber(conCertNumber);
		workUser.setContactName(contactName);
		User  user=new User();
		user.setName(createByname);
		User  user1=new User();
		user1.setName(updateByname);
		workDealInfo.setWorkCompany(company);
		workDealInfo.setWorkUser(workUser);
		workDealInfo.setPayType(payType);
		workDealInfo.setKeySn(keySn);
		workDealInfo.setCreateBy(user);
		workDealInfo.setUpdateBy(user1);
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
		
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		WorkDealInfoStatus workDealInfoStatus=new WorkDealInfoStatus(); 
		List<Office> officeList = officeService.getOfficeByType(
				UserUtils.getUser(), 2);
//		Calendar calendar = Calendar.getInstance();
		try {
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>() ;
		if (zhizhengStartTime!=null&&zhizhengEndTime!=null) {
			certInfoList =  workCertInfoService.findZhiZhengTime(zhizhengStartTime, zhizhengEndTime);
		}
		final List<WorkDealInfo> list = workDealInfoService.find( workDealInfo, area,
				officeId, apply, certType, workType, year, luruStartTime,
				luruEndTime, officeList, daoqiStartTime, daoqiEndTime, jianzhengStartTime, 
				jianzhengEndTime,certInfoList);
		final String fileName = "WorkDealInfos.csv";
        final List<WorkDealInfoVo> workDealInfoVos = new ArrayList<WorkDealInfoVo>();
        String dealInfoType=null;
		String dealInfoType1=null;
		String dealInfoType2=null;
		String dealInfoType3=null;
        for (final WorkDealInfo dealInfo : list) {
        	
        	final WorkDealInfoVo dealInfoVo = new WorkDealInfoVo();
        	dealInfoVo.setSvn(dealInfo.getSvn());
        	dealInfoVo.setAlias(dealInfo.getConfigApp().getAlias());
        	dealInfoVo.setCompanyName(dealInfo.getWorkCompany().getCompanyName());
        	dealInfoVo.setContactName(dealInfo.getWorkUser().getContactName());
        	dealInfoVo.setCertApplyInfoName(dealInfo.getWorkCertInfo().getWorkCertApplyInfo().getName());
        	dealInfoVo.setProductName(productType.getProductTypeName(Integer.parseInt(dealInfo.getConfigProduct().getProductName())));
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
			dealInfoVo.setKeySn(dealInfo.getKeySn());
			
			if (dealInfo.getWorkCertInfo().getSignDate()!=null) {
				String signDateString = dfm.format(dealInfo.getWorkCertInfo().getSignDate());
				dealInfoVo.setSignDateString(signDateString);
			}else{
				dealInfoVo.setSignDateString("");
				
			}
			
			if(dealInfo.getAddCertDays()==null){
				dealInfoVo.setCertDays(dealInfo.getYear()*365+dealInfo.getLastDays()+"（天）");
			}else{
				dealInfoVo.setCertDays(dealInfo.getYear()*365+dealInfo.getLastDays()+dealInfo.getAddCertDays()+"（天）");
			}
			String notafterString = df.format(dealInfo.getNotafter());
			dealInfoVo.setNotAfter(notafterString);
			dealInfoVo.setDealInfoStatus(workDealInfoStatus.WorkDealInfoStatusMap.get(dealInfo.getDealInfoStatus()));
            workDealInfoVos.add(dealInfoVo);
           //System.out.println(workDealInfoVos.size());
        }
		
        new ExportExcel("业务查询", WorkDealInfoVo.class).setDataList(workDealInfoVos).write(response, fileName).dispose();
		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
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
	 * @Title: showAgentProduct
	 * @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置)
	 * @param app
	 *            应用ID
	 * @param productName
	 *            产品名称
	 * @param infoType
	 *            业务类型
	 * @param lable
	 *            专用、通用
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
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
	 * @Title: showAgentProduct
	 * @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置)
	 * @param app
	 *            应用ID
	 * @param productName
	 *            产品名称
	 * @param infoType
	 *            业务类型
	 * @param lable
	 *            专用、通用
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
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
				iter2.put("id", boundStyleList.get(i).getId());//关联表ID
				iter2.put("name", boundStyleList.get(i).getAgent()
						.getTempName());
				iter2.put("agentId", boundStyleList.get(i).getAgent()
						.getId());
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
			ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(boundId);
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
	 * @Title: showYear
	 * @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置)
	 * @param @param productName
	 * @param @param lable
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
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
	 * @Title: showYearNew
	 * @Description: TODO(新增选中应用、产品后带回年限、缴费方式、证书模版项配置)
	 * @param @param productName
	 * @param @param lable
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "showYearNew")
	@ResponseBody
	public String showYearNew(Long boundId,Integer infoType) {
		JSONObject json = new JSONObject();
		try {

			ConfigChargeAgentBoundConfigProduct bound= configChargeAgentBoundConfigProductService.get(boundId);

//			ConfigProduct configProduct = configProductService.findByIdOrLable(
//					app, productName, lable);

			if (infoType != null) {
				String[] years = configChargeAgentDetailService
						.getChargeAgentYears(bound.getAgent().getId(),
								infoType);
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
	 * @Title: manySave
	 * @Description: TODO(走ajax后台保存，批量保存)
	 * @param @param model
	 * @param @param workDealInfoId
	 * @param @param redirectAttributes
	 * @param @param appId
	 * @param @param product
	 * @param @param dealInfType1
	 * @param @param year
	 * @param @param yar
	 * @param @param companyId
	 * @param @param companyName
	 * @param @param companyType
	 * @param @param organizationNumber
	 * @param @param orgExpirationTime
	 * @param @param selectLv
	 * @param @param comCertificateType
	 * @param @param comCertficateNumber
	 * @param @param comCertficateTime
	 * @param @param legalName
	 * @param @param s_province
	 * @param @param s_city
	 * @param @param s_county
	 * @param @param address
	 * @param @param companyMobile
	 * @param @param remarks
	 * @param @param workuserId
	 * @param @param workType
	 * @param @param contactName
	 * @param @param conCertType
	 * @param @param contacEmail
	 * @param @param conCertNumber
	 * @param @param contactPhone
	 * @param @param contactTel
	 * @param @param deal_info_status
	 * @param @param recordContent
	 * @param @param lable
	 * @param @param classifying
	 * @param @param pName
	 * @param @param pEmail
	 * @param @param pIDCard
	 * @param @param contactSex
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequiresPermissions("work:workDealInfo:edit")
	@RequestMapping(value = "manySave")
	@ResponseBody
	public String manySave(Model model, Long workDealInfoId,
			RedirectAttributes redirectAttributes, Long appId, String product,
			Integer dealInfType1, Integer year, Integer yar, Long companyId,
			String companyName, String companyType, String organizationNumber,
			String orgExpirationTime, String selectLv,
			String comCertificateType, String comCertficateNumber,
			String comCertficateTime, String legalName, String s_province,
			String s_city, String s_county, String address,
			String companyMobile, String remarks, Long workuserId,
			Integer workType, String contactName, String conCertType,
			String contacEmail, String conCertNumber, String contactPhone,
			String contactTel, String deal_info_status, String recordContent,
			Integer lable, Integer classifying, String pName, String pEmail,
			String pIDCard, String contactSex, String areaRemark,Integer agentId, Long agentDetailId) {

		JSONObject json = new JSONObject();
		try {
			WorkCompany workCompany = workCompanyService.finByNameAndNumber(
					companyName, organizationNumber);

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
//			ConfigProduct configProduct = configProductService.findByIdOrLable(
//					appId, product, lable);
			
			ConfigChargeAgentBoundConfigProduct bound =  configChargeAgentBoundConfigProductService.get(agentDetailId);

			ConfigProduct configProduct = bound.getProduct();
			
			

			workCompanyService.save(workCompany);
			workUserService.save(workUser);

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

			if (year == null) {
				workDealInfo.setYear(0);
			} else {
				workDealInfo.setYear(year);
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
			WorkCertApplyInfo workCertApplyInfo = null;
			WorkCertInfo workCertInfo = null;
			if (workDealInfo.getConfigProduct().getProductName().equals("2")
					|| workDealInfo.getConfigProduct().getProductName()
							.equals("3")) {

				if (workDealInfo.getWorkCertInfo() != null) {
					workCertApplyInfo = workDealInfo.getWorkCertInfo()
							.getWorkCertApplyInfo();
				} else {
					workCertApplyInfo = new WorkCertApplyInfo();
				}

				workCertApplyInfo.setName(pName);
				workCertApplyInfo.setEmail(pEmail);
				workCertApplyInfo.setIdCard(pIDCard);
				workCertApplyInfo.setProvince(workCompany.getProvince());
				workCertApplyInfo.setCity(workCompany.getCity());

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
			} else {
				if (workDealInfo.getWorkCertInfo() != null) {
					workCertApplyInfo = workDealInfo.getWorkCertInfo()
							.getWorkCertApplyInfo();
				} else {
					workCertApplyInfo = new WorkCertApplyInfo();
				}
				workCertApplyInfo.setName(workCompany.getCompanyName());
				workCertApplyInfo.setEmail(workUser.getContactEmail());
				workCertApplyInfo
						.setMobilePhone(workCompany.getCompanyMobile());
				workCertApplyInfo.setProvince(workCompany.getProvince());
				workCertApplyInfo.setCity(workCompany.getCity());
				workCertApplyInfoService.save(workCertApplyInfo);
				if (workDealInfo.getWorkCertInfo() != null) {
					workCertInfo = workDealInfo.getWorkCertInfo();
				} else {
					workCertInfo = new WorkCertInfo();
				}

				workCertInfo.setWorkCertApplyInfo(workCertApplyInfo);
				workCertInfoService.save(workCertInfo);
				workDealInfo.setWorkCertInfo(workCertInfo);
			}
			// List<ConfigAgentOfficeRelation> lis =
			// configAgentOfficeRelationService.findByOffice(UserUtils.getUser().getOffice());
			// if (lis.size()>0) {
			// workDealInfo.setCommercialAgent(lis.get(0).getConfigCommercialAgent());
			// }
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
		return json.toString();
	}

	@RequestMapping(value = "findByKeySn")
	@ResponseBody
	public String findByKeySn(String keySn, Long dealId) {
		JSONObject json = new JSONObject();
		try {

			Integer isUserInteger = workDealInfoService.findByKey(keySn);
			WorkDealInfo dealInfo = workDealInfoService.get(dealId);
			if (dealInfo.getWorkCertInfo().getIssuerDn() != null
					&& !dealInfo.getWorkCertInfo().getIssuerDn().equals("")) {

				if (dealInfo.getDealInfoStatus().equals("1")) {
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
		//	if ((nowLong - certAfterLong) > 0) {
		//		json.put("isUpdate", 0);
		//	} else {
				if ((certAfterLong - nowLong) <= (1000 * 60 * 60 * 24 * 60L)) {
					json.put("isUpdate", 1);
				} else {
					json.put("isUpdate", 0);
				}
			//}
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
	
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "cancelMaintenance")
	public String cancelMaintenance(Long id, Integer type , Model model, RedirectAttributes redirectAttributes){
		try {
			WorkDealInfo dealinfo = workDealInfoService.get(id);
			Long certId = dealinfo.getWorkCertInfo().getId();
			Long agnetId = dealinfo.getConfigChargeAgentId();
			Long dealPreId = dealinfo.getPrevId();
			
			ConfigChargeAgent agentOri =  configChargeAgentService.get(agnetId);
			agentOri.setReserveUpdateNum(agentOri.getReserveUpdateNum()-1);
			agentOri.setSurplusUpdateNum(agentOri.getSurplusUpdateNum()+1);
			configChargeAgentService.save(agentOri);
			
			ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agnetId,id);
			if (bound!=null) {
				configAgentBoundDealInfoService.deleteById(bound.getId());
			}
			
			workDealInfoService.deleteWork(id);
			workCertInfoService.delete(certId);
			workDealInfoService.deleteReturnById(dealPreId);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		logUtil.saveSysLog("业务中心", "删除业务：编号" + id + "成功", "");
		model.addAttribute("message", "取消业务编号为"+id+"业务成功！");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}
	
	
	
	
	@RequiresPermissions("work:workDealInfo:view")
	@RequestMapping(value = "cancelMaintenanceByReturn")
	public String cancelMaintenanceByReturn(Long id, Model model, RedirectAttributes redirectAttributes){
		try {
			
				WorkDealInfo dealInfo = workDealInfoService.get(id);
				WorkPayInfo payInfo = dealInfo.getWorkPayInfo();
				Set<WorkFinancePayInfoRelation> relations = payInfo.getWorkFinancePayInfoRelations();
				if (relations.size() != 0) {
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
			
			//this.fixOldPayInfo(dealInfo);
			
			Double money = dealInfo.getWorkPayInfo().getReceiptAmount();
			if (money>0d) {
				ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService.findDepotByOffice(dealInfo.getCreateBy().getOffice()).get(0);
				// 修改余额
				receiptDepotInfo.setReceiptResidue(receiptDepotInfo
						.getReceiptResidue() + money);
				receiptDepotInfo.setReceiptTotal(receiptDepotInfo.getReceiptTotal()+money);
				
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
						"库房" + receiptDepotInfo.getReceiptName() + "添加入库信息成功",
						"");
				receiptDepotInfoService.save(receiptDepotInfo);
			}
			
			workPayInfoService.delete(dealInfo.getWorkPayInfo().getId());
			dealInfo.setWorkPayInfo(null);
			dealInfo.setDealInfoStatus("5");
			workDealInfoService.save(dealInfo);
			
//			private Integer configureNum;//配置数量
//			private Integer surplusNum;//剩余数量
//			private Integer availableNum;//已用数量
//			private Integer reserveNum;//预留数量
			
			ConfigChargeAgent agentOri =  configChargeAgentService.get(dealInfo.getConfigChargeAgentId());
			agentOri.setAvailableUpdateNum(agentOri.getAvailableUpdateNum()-1);
			agentOri.setSurplusUpdateNum(agentOri.getSurplusUpdateNum()+1);
			configChargeAgentService.save(agentOri);
			
			ConfigAgentBoundDealInfo bound = configAgentBoundDealInfoService.findByAgentIdDealId(agentOri.getId(),id);
			if (bound!=null) {
				configAgentBoundDealInfoService.deleteById(bound.getId());
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
		model.addAttribute("message", "取消业务编号为"+id+"业务成功！");
		return "redirect:" + Global.getAdminPath() + "/work/workDealInfo/";
	}
	

}
