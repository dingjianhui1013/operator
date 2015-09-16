package com.itrus.ca.modules.userenroll.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.FileDownloadUtil;
import com.itrus.ca.common.utils.PayinfoUtil;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.constant.WorkType;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentBoundDealInfo;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentBoundDealInfoService;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.sys.entity.Log;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.userenroll.entity.KeyUnlock;
import com.itrus.ca.modules.userenroll.service.KeyUnlockService;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkCertTrustApply;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.entity.WorkUser;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCertTrustApplyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;

/**
 * 前台服务controller
 * 
 * @author ZhangJingtao
 * 
 */
@Controller
@RequestMapping(value = "/enroll")
public class UserEnrollController extends BaseController {
	
	@Autowired
	private ConfigAppService configAppService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private WorkDealInfoService dealInfoService;

	@Autowired
	private WorkCertInfoService workCertInfoService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;

	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigProductService configProductService;
	
	@Autowired
	private SystemService systemService;

	@Autowired
	private WorkCertTrustApplyService trustApplyService;
	
	@Autowired
	private KeyUnlockService keyUnlockService;
	
	@Autowired
	private WorkCertTrustApplyService workCertTrustApplyService;
	
	@Autowired
	private WorkPayInfoService workPayInfoService;
	
	@Autowired
	private ConfigAgentBoundDealInfoService configAgentBoundDealInfoService;
	
	
	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;
	
	
	@Value(value = "${deploy.path}")
	public String deployPath;
	
	private LogUtil logUtil = new LogUtil();

	// 通过AJAX判断
	@RequestMapping(value = "bbfw1Submit")
	@ResponseBody
	public String bbfw1Submit(String certSn, Integer dealInfoType, Integer year)
			throws JSONException {
		JSONObject json = new JSONObject();
		Double money = 0.00;
		try {
			String appName = "";
			List<WorkDealInfo> workDealInfos = dealInfoService
					.findByCertSn(certSn);
			if (workDealInfos.size() > 0) {
				ConfigChargeAgent configChargeAgent = configChargeAgentService
						.get(workDealInfos.get(0)
								.getConfigProduct().getChargeAgentId());
				int i = workDealInfos.get(0).getConfigProduct()
						.getProductLabel();
				money = configChargeAgentDetailService.selectMoney(
						configChargeAgent, dealInfoType, year, i);
				appName = workDealInfos.get(0).getConfigApp().getAppName();
				if (money == null) {
					money = 0.00;
				}
			}
			json.put("appName", appName);
			json.put("money", money);
			json.put("status", "1");
		} catch (Exception e) {
			json.put("status", "0");
		}
		return json.toString();
	}

	// 通过AJAX判断
	@RequestMapping(value = "appName")
	@ResponseBody
	public String appName(String certSn, Integer dealInfoType, Integer year)
			throws JSONException {
		JSONObject json = new JSONObject();
		try {
			String appName = "";
			List<WorkDealInfo> workDealInfos = dealInfoService
					.findByCertSn(certSn);
			if (workDealInfos.size() > 0) {
				appName = workDealInfos.get(0).getConfigApp().getAppName();
			}
			json.put("appName", appName);
			json.put("status", "1");
		} catch (Exception e) {
			json.put("status", "0");
		}
		return json.toString();
	}

	// 通过AJAX判断
	@RequestMapping(value = "status")
	@ResponseBody
	public String bbfw2Submit() throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", "1");
		} catch (Exception e) {
			json.put("status", "0");
		}
		return json.toString();
	}

	//
	// List<WorkDealInfo> dealInfos =
	// workDealInfoService.findgxfw1(keySn,certSn);
	// if (dealInfos.size()>0) {
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	// WorkCertInfo certInfo = dealInfos.get(0).getWorkCertInfo();
	// WorkCertApplyInfo applyInfo = certInfo.getWorkCertApplyInfo();
	// WorkUser user = dealInfos.get(0).getWorkUser();
	// json.put("email", user.getContactEmail());
	// json.put("startDate", sdf.format(certInfo.getNotbefore()));
	// json.put("endDate", sdf.format(certInfo.getNotafter()));
	// }
	//

	@RequestMapping("changeApp")
	@ResponseBody
	public String changeApp(Long configAppId){
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		List<ConfigProduct> configProducts = null;
		try {
			configProducts = configProductService.findByApp(configAppId);
			for(int i = 0;i<configProducts.size();i++){
				json=new JSONObject();
				ConfigProduct configProduct = configProducts.get(i);
				json.put("id", configProduct.getId());
				json.put("productName",ProductType.productTypeMap.get(new Integer(configProduct.getProductName()))+((configProduct.getProductLabel()==1)?"(专用)":"(通用)"));
				array.put(json);
			}
		} catch (Exception e) {
			return null;
		}
		return array.toString();
	}
	
	@RequestMapping("changeAgent")
	@ResponseBody
	public String changeAgent(Long products){
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		try {
		List<ConfigChargeAgentBoundConfigProduct> agents = configChargeAgentBoundConfigProductService.findByProductId(products);
		
			for(int i = 0;i<agents.size();i++){
				json=new JSONObject();
				ConfigChargeAgentBoundConfigProduct bound = agents.get(i);
				json.put("id", bound.getAgent().getId());
				json.put("productName",bound.getAgent().getTempName());
				array.put(json);
			}
		} catch (Exception e) {
			return null;
		}
		return array.toString();
	}
	
	
	
	
	// 通过AJAX判断
	@RequestMapping(value = "bbfw")
	@ResponseBody
	public String bbfw(String keySn, String certSn, int dealInfoType)
			throws JSONException {
		JSONObject json = new JSONObject();
		Double money = 0.00;
		String appName = "";
		try {
			List<WorkDealInfo> dealInfos = dealInfoService.findgxfw1(certSn);
			if (dealInfos.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				WorkCertInfo certInfo = dealInfos.get(0).getWorkCertInfo();
				WorkCertApplyInfo applyInfo = certInfo.getWorkCertApplyInfo();
				WorkUser user = dealInfos.get(0).getWorkUser();
				json.put("certSn", certInfo.getSerialnumber());
				json.put("email", user.getContactEmail());
				json.put("startDate", sdf.format(certInfo.getNotbefore()));
				json.put("endDate", sdf.format(certInfo.getNotafter()));

				ConfigChargeAgent configChargeAgent = configChargeAgentService
						.get(dealInfos.get(0).getConfigChargeAgentId());
				Long product = dealInfos.get(0).getConfigProduct().getId();
				int i = dealInfos.get(0).getConfigProduct().getProductLabel();
				int year = 0;
				if (dealInfoType == 2) {
					year = dealInfos.get(0).getYear();
				}
				if (dealInfoType != 5) {
					money = configChargeAgentDetailService.selectMoney(
							configChargeAgent, dealInfoType, year, i);
				}
				appName = dealInfos.get(0).getConfigApp().getAppName();
				if (money == null) {
					money = 0.00;
				}
			}else{
				json.put("status", 0);
				return json.toString();
			}
			json.put("appName", appName);
			json.put("money", money);
			json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequestMapping(value = "bbfw1form")
	public String form(Model model) {
		List<ConfigApp> configApps = configAppService.selectAll();
		model.addAttribute("configApps", configApps);
		return "iLetter/zhengshufuwu_bbfw1";
	}

	@RequestMapping(value = "bbfw2form")
	public String bbfw2form(Long configProductId,Long configAppId,Model model,Long configAgentId) {
		model.addAttribute("configProductId", configProductId);
		model.addAttribute("configAppId",configAppId);
		System.out.println(configAppId);
		model.addAttribute("configAgentId", configAgentId);
		//ConfigProduct product =  configProductService.findByAppIdProduct(configAppId , configProductId);
		
		//List<ConfigChargeAgentBoundConfigProduct> list = configChargeAgentBoundConfigProductService.findByProductId(configProductId);
		
		//ConfigChargeAgent agent  = configChargeAgentService.get(product.getChargeAgentId());
		ConfigChargeAgent agent = configChargeAgentService.get(configAgentId);
		List<ConfigChargeAgentDetail> list2 = configChargeAgentDetailService.findByConfigChargeAgent(agent);
		if(list2.size()==0){
			List<ConfigApp> configApps = configAppService.selectAll();
			model.addAttribute("configApps", configApps);
			model.addAttribute("msg","此产品没有录入补办费用，请选择其他产品！");
			return "iLetter/zhengshufuwu_bbfw1";
		}else{
			
			for (ConfigChargeAgentDetail d : list2) {
				if (d.getWorkType().equals(WorkType.TYPE_REISSUE)) {
					model.addAttribute("bb0", d.getMoney());
				}
				if (d.getWorkType().equals(WorkType.TYPE_DAMAGE)) {
					model.addAttribute("bb1", d.getMoney());
				}
			}
		}
		return "iLetter/zhengshufuwu_bbfw2";
	}

	@RequestMapping(value = "bbfw2Nextform")
	public String bbfw2Nextform(Long configAppid, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService.findAllByAppId(configAppid);
		if (configAppOfficeRelations.size() > 0) {
			List<Long> officeIds = Lists.newArrayList();
			for (int i = 0; i < configAppOfficeRelations.size(); i++) {
				officeIds.add(configAppOfficeRelations.get(i)
						.getOffice().getId());
			}
			List<Office> page = officeService.findByIds(
					new Page<Office>(request, response), officeIds);
			model.addAttribute("offices", page);
		}
		return "iLetter/zhengshufuwu_bbfw3";
	}

	// ____________________________________________
	@RequestMapping(value = "bzfw1form")
	public String bzfw1form(Model model, Double money, String appName) {
		model.addAttribute("money", money);
		model.addAttribute("appName", appName);
		return "iLetter/zhengshufuwu_bzfw1";
	}

	@RequestMapping(value = "bzfw2form")
	public String bzfw1Nextform(Model model,String appName) {
		try {
			appName = URLDecoder.decode(appName, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		model.addAttribute("appName", appName);
		return "iLetter/zhengshufuwu_bzfw2";
	}

	@RequestMapping(value = "bzfw2Nextform")
	public String bzfw2Nextform(String certSn, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (certSn != null) {
				List<WorkDealInfo> workDealInfos = workDealInfoService
						.findByCertSnIgnoreDel(certSn);
				if (workDealInfos.size() > 0) {
					List<Long> appIds = Lists.newArrayList();
					for (int i = 0; i < workDealInfos.size(); i++) {
						appIds.add(workDealInfos.get(i).getConfigApp().getId());
					}
					List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
							.findAllByAppId(appIds);
					if (configAppOfficeRelations.size() > 0) {
						List<Long> officeIds = Lists.newArrayList();
						for (int i = 0; i < configAppOfficeRelations.size(); i++) {
							officeIds.add(configAppOfficeRelations.get(i)
									.getOffice().getId());
						}
						List<Office> page = officeService.findByIds(
								new Page<Office>(request, response), officeIds);
						model.addAttribute("offices", page);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "iLetter/zhengshufuwu_bzfw3";
	}

	// ____________________________________________
	@RequestMapping(value = "dxfw1form")
	public String dxfw1form() {
		return "iLetter/zhengshufuwu_dxfw1";
	}

	@RequestMapping(value = "dxfw1Nextform")
	public String dxfw1Nextform(Model model,String appName) {
		try {
			appName = URLDecoder.decode(appName, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		model.addAttribute("appName", appName);
		return "iLetter/zhengshufuwu_dxfw2";
	}

	@RequestMapping(value = "dxfw2Nextform")
	public String dxfw2Nextform(String certSn, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (certSn != null) {
				List<WorkDealInfo> workDealInfos = workDealInfoService
						.findByCertSnIgnoreDel(certSn);
				if (workDealInfos.size() > 0) {
					List<Long> appIds = Lists.newArrayList();
					for (int i = 0; i < workDealInfos.size(); i++) {
						appIds.add(workDealInfos.get(i).getConfigApp().getId());
					}
					List<ConfigAppOfficeRelation> configAppOfficeRelations = configAppOfficeRelationService
							.findAllByAppId(appIds);
					if (configAppOfficeRelations.size() > 0) {
						List<Long> officeIds = Lists.newArrayList();
						for (int i = 0; i < configAppOfficeRelations.size(); i++) {
							officeIds.add(configAppOfficeRelations.get(i)
									.getOffice().getId());
						}
						List<Office> page = officeService.findByIds(
								new Page<Office>(request, response), officeIds);
						model.addAttribute("offices", page);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "iLetter/zhengshufuwu_dxfw3";
	}

	// ____________________________________________
	@RequestMapping(value = "ktydsb1form")
	public String ktydsb1form(Model model) {
		return "iLetter/zhengshufuwu_ktydsb1";
	}

//	@RequestMapping(value = "ktydsb1")
//	@ResponseBody
//	public String ktydsb1(String keySn,String certSn) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			List<WorkDealInfo> dealInfos = workDealInfoService
//					.findgxfw1(keySn,certSn);
//			if (dealInfos.size() != 0) {
//				WorkDealInfo workDealInfo = dealInfos.get(0);
//			}
//			WorkCertTrustApply workCertTrustApply = workCertTrustApplyService.findByKeySn(keySn);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return JSON.toJSONString("");
//	}

	// 通过AJAX判断
	@RequestMapping(value = "ktydsb1Submit")
	@ResponseBody
	public String ktydsb1Submit(String certSn)
			throws JSONException {
		JSONObject json = new JSONObject();
		try {
			boolean getInfoFlag = false;
			WorkCertTrustApply apply = trustApplyService
					.getTrustByStatus(certSn);
			if (apply != null) {
				json.put("year", apply.getYear());
				json.put("count", apply.getApplyCount());
				json.put("money", apply.getMoney());
				json.put("status",apply.getStatus());
				WorkCertInfo certInfo = workDealInfoService.findByCertSn(certSn).get(0).getWorkCertInfo();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				json.put("count1", certInfo.getTrustDeviceCount());
				json.put("date", sdf.format(certInfo.getTrustDeviceDate()));
				json.put("id",apply.getId());
			} else {
				getInfoFlag = true;
				json.put("status", 2);
			}
			if (getInfoFlag) {
				List<WorkDealInfo> dealInfos = workDealInfoService.findgxfw1(certSn);
				if (dealInfos.size() > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
					WorkCertInfo certInfo = dealInfos.get(0).getWorkCertInfo();
					WorkUser user = dealInfos.get(0).getWorkUser();
					json.put("email", user.getContactEmail());
					json.put("startDate", sdf.format(certInfo.getNotbefore()));
					json.put("endDate", sdf.format(certInfo.getNotafter()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", 0);
		}
		return json.toString();
	}

	@RequestMapping(value = "ktydsbShow")
	public String ktydsbShow(Model model, @RequestParam(required = false)String money,Long id,
			@RequestParam(required = false) String msg) {
		if (money!=null) {
			model.addAttribute("money", money+"元");
		}
		if (msg!=null) {
			try {
				msg = URLDecoder.decode(msg, "UTF-8");
			} catch (Exception e) {
				
			}
		}
		model.addAttribute("id", id);
		model.addAttribute("msg", msg == null ? "开通申请已提交成功，您需要支付费用" : msg);
		return "iLetter/zhengshufuwu_ktydsb2";
	}

	@RequestMapping(value = "ktydsb1Nextform")
	@ResponseBody
	public String ktydsb1Nextform(String count, String certsn,String keySn)
			throws JSONException {
		JSONObject json = new JSONObject();
		if (count.equals("")) {
			json.put("msg", "请填写申请数量！");
			json.put("status", 4);
			return json.toString();
			
		}
		
		
		
		
		List<WorkDealInfo> dealInfos = workDealInfoService.findgxfw1(certsn);
		try {
			if (dealInfos.size() > 0) {
				WorkDealInfo workDealInfo = dealInfos.get(0);
				WorkCertInfo certInfo = workDealInfo.getWorkCertInfo();
				WorkCertTrustApply applyInfo = new WorkCertTrustApply();
				if (certInfo.getNotafter()==null||certInfo.getNotafter().before(new Date())) {
					json.put("msg", "证书已过期");
					json.put("status", 0);
					return json.toString();
				}
				applyInfo.setApplyCount(Integer.valueOf(count));
				applyInfo.setApplyDate(new Date());
				applyInfo.setCertSn(certsn);
				if(keySn!=null&&!keySn.equals("")){
					applyInfo.setKeySn(keySn);
				}
				
//				int halfYearAmount = 0;
//				int oneYearAmount = 0;
//				double certYear = (certInfo.getNotafter().getTime()-System.currentTimeMillis())/(MILL*365);
//				oneYearAmount = (int)certYear;
//				boolean flag = false;//未整除
//				String 
//				if (.split("")) {
//					
//				}
//				
//				Integer millCertYear = Integer.valueOf((int) (certYear*10));
//				Integer m1 = millCertYear - oneYearAmount *10;
//				if (m1) {
//					
//				}
				
				
				applyInfo.setState(0);
				int lastDays = getLastCertDay(certInfo.getNotafter());
				int intYear = lastDays/365;   //取申请移动设备的整数部分
				double doubleYear = (lastDays%365)/365.0; //取申请移动设备的小数部分
				Double totalMoney = 0d; //结算过后总年数
				
				ConfigChargeAgent configChargeAgent = configChargeAgentService
                        .get(workDealInfo.getConfigChargeAgentId());
				int i = workDealInfo.getConfigProduct().getProductLabel();
				Double yearMoney = configChargeAgentDetailService.selectTrust1Money(configChargeAgent, i);//该应用申请移动设备1年的钱数
				Double halfYearMoney = configChargeAgentDetailService.selectTrust0Money(configChargeAgent, i);//该应用申请移动设备半年的钱数
				
				totalMoney = intYear * yearMoney;//算出整数部分的钱数
				if (doubleYear > 0.5) {
					totalMoney += yearMoney; 
				} else {
					totalMoney += halfYearMoney;
				}
//				Calendar certEnd = Calendar.getInstance();
//				certEnd.setTime(certInfo.getNotafter());// 证书截止日期
//				Calendar halfYear = Calendar.getInstance();
//				halfYear.add(Calendar.MONTH, 6);// 增加半年
//				int year = 1;// 1年有效期的可信数量
//				if (halfYear.getTime().after(certEnd.getTime())) {// 不足半年
//					year = 0;// 半年计费
//				}
				
//				Double money = configChargeAgentDetailService.selectMoney(
//						configChargeAgent, 7, year, i);
//				if (money == null) {
//					money = 0.00;
//				}
				boolean autoPass = false;
				ConfigProduct product = workDealInfo.getConfigProduct();
				if (workDealInfo.getWorkPayInfo().getMethodGov()) {//原有缴费方式是政府统一采购，限制数量
					ConfigApp app = workDealInfo.getConfigApp();
					if (app.getGovDeviceAmount()!=null) {//数量限制不是null
						Long appId = workDealInfo.getConfigApp().getId();
						Integer curValidDevice = workDealInfoService.getValidDeviceTotal(appId);
						if (curValidDevice+Integer.valueOf(count)>app.getGovDeviceAmount()) {//数量超过...
							json.put("msg", "当前应用可信移动设备数量超过最大限制");
							json.put("id", -1L);
							json.put("status", -1);
							return json.toString();
						}
					}
					if (product.getDeviceApplyAutoPass()) {//政府统一采购自动通过
						
						WorkPayInfo workPayInfo = new WorkPayInfo();

						workPayInfo.setWorkTotalMoney(totalMoney * Integer.valueOf(count));
						workPayInfo.setWorkPayedMoney(0d);
						workPayInfo.setWorkReceivaMoney(totalMoney * Integer.valueOf(count));
						workPayInfo.setUserReceipt(false);
						workPayInfo.setReceiptAmount(0d);
						workPayInfo.setSn(PayinfoUtil.getPayInfoNo());
						workPayInfo.setMethodGov(true);
						User user = systemService.getUser(1L);
						workPayInfo.setCreateBy(user);
						workPayInfo.setUpdateBy(user);
						workPayInfoService.save(workPayInfo);

						applyInfo.setWorkPayInfo(workPayInfo);
						int valid = 0;
						if (certInfo.getTrustDeviceCount()!=null) {
							valid = certInfo.getTrustDeviceCount();
						}
						certInfo.setTrustDeviceCount(valid + Integer.valueOf(count));
						certInfo.setTrustDeviceDate(certInfo.getNotafter());
						workCertInfoService.save(certInfo);
						autoPass = true;
					}
				}
				applyInfo.setMoney(totalMoney * Integer.valueOf(count));
				applyInfo.setStatus(0);
				if (autoPass) {
					applyInfo.setStatus(1);
				}
				applyInfo.setWorkCertInfo(certInfo);
//				applyInfo.setYear(year);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				String sn = dealInfoService.getSVN(1);
				applyInfo.setSn(sn);
				trustApplyService.save(applyInfo);
				if (!autoPass) {//原有流程
					json.put("id",applyInfo.getId());
//					json.put("year", year);
					json.put("money", totalMoney * Integer.valueOf(count));
					json.put("msg", "开通申请已提交成功，您需要支付费用");
					json.put("status", 1);
				}else {//新的流程
					SimpleDateFormat certSdf = new SimpleDateFormat("yyyy-MM-dd");
					json.put("count", count);
					json.put("id", applyInfo.getId());
					json.put("date", certSdf.format(applyInfo.getWorkCertInfo().getNotafter()));
					json.put("status", 2);
				}
			} else {
				json.put("msg", "证书不合法");
				json.put("status", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg", "系统异常");
			json.put("status", 0);
		}
		return json.toString();
	}

	// 通过AJAX判断
	@RequestMapping(value = "ktydsb2Submit")
	@ResponseBody
	public String ktydsb2Submit(String ukey,String certSn) throws JSONException {
		JSONObject json = new JSONObject();
		WorkCertTrustApply apply = trustApplyService
				.getTrustByStatus(certSn);
		try {
			json.put("status", apply.getStatus());
			json.put("id", apply.getId());
			json.put("count", apply.getApplyCount());
			WorkCertInfo certInfo = workDealInfoService.findByCertSn(certSn).get(0).getWorkCertInfo();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			json.put("date", sdf.format(certInfo.getTrustDeviceDate()));
			System.out.println(certInfo.getTrustDeviceCount());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json.toString();
	}

	@RequestMapping(value = "ktydsb2Nextform")
	public String ktydsb2Nextform(Model model,String count,String date,Integer status,Long id) {
		if(status!=null){
			model.addAttribute("status",1);
		}else{
			model.addAttribute("status",0);
		}
		if(id!=null){
			WorkCertTrustApply workCertTrustApply = workCertTrustApplyService.get(id);
			workCertTrustApply.setState(1);
			workCertTrustApplyService.save(workCertTrustApply);
			model.addAttribute("suggest","拒绝原因:"+workCertTrustApply.getSuggest());
		}
		model.addAttribute("count", count);
		model.addAttribute("date", date);
		return "iLetter/zhengshufuwu_ktydsb3";
	}

	// ____________________________________________
	@RequestMapping(value = "jsfw1form")
	public String jsfw1form(Model model) {
		return "iLetter/zhengshufuwu_jsfw1";
	}

	// 通过AJAX判断
	@RequestMapping(value = "jsfw1Submit")
	@ResponseBody
	public String jsfw1Submit() throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", "1");
		} catch (Exception e) {
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequestMapping(value = "jsfw1Nextform")
	public String jsfw1Nextform(Model model, String status,Long id) {
		try {
			if (status.equals("ENROLL")) {
				model.addAttribute("status", "解锁申请已提交成功，等待管理员审批！");
			}else {
				KeyUnlock keyUnlock = keyUnlockService.get(id);
				keyUnlockService.save(keyUnlock);
				model.addAttribute("status", "解锁申请被拒绝");
				model.addAttribute("msg","拒绝原因："+keyUnlock.getRemarks());
				keyUnlock.getReqCode();
				keyUnlock.getKeySn();
				keyUnlock.getCertCn();
				model.addAttribute("lastId",id);
				model.addAttribute("keySn",keyUnlock.getKeySn());
				model.addAttribute("reqCode",keyUnlock.getReqCode());
				model.addAttribute("certCn",keyUnlock.getCertCn());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "iLetter/zhengshufuwu_jsfw2";
	}

	// 通过AJAX判断
	@RequestMapping(value = "jsfw2Submit")
	@ResponseBody
	public String jsfw2Submit() throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", "1");
		} catch (Exception e) {
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequestMapping(value = "jsfw2Nextform")
	public String jsfw2Nextform(Model model, String respCode, Long id) {
		try {
			KeyUnlock unlock = keyUnlockService.get(id);
			model.addAttribute("respCode", unlock.getRepCode());
			model.addAttribute("id", id);
			return "iLetter/zhengshufuwu_jsfw3";
		} catch (Exception e) {
			return "iLetter/zhengshufuwu_jsfw2";
		}
	}

	// 通过AJAX判断
	@RequestMapping(value = "jsfw3Submit")
	@ResponseBody
	public String jsfw3Submit() throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", "1");
		} catch (Exception e) {
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequestMapping(value = "jsfw3Nextform")
	public String jsfw3Nextform(Model model, String msg,HttpServletRequest request,@RequestParam(required = false)String keySn,Long id) {
		model.addAttribute("status", "解锁失败，错误码" + msg);
		logUtil.saveTerminalLog(request.getRemoteHost(), keySn+"解锁失败:"+msg, request.getRemoteAddr(),keySn, "申请解锁");
		//解锁失败不允许再次申请，直接更新为解锁失败状态
		try {
			KeyUnlock unlock = keyUnlockService.get(id);
			unlock.setStatus("UNLOCK");
			keyUnlockService.save(unlock);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "iLetter/zhengshufuwu_jsfw4";
	}

	// ____________________________________________
	@RequestMapping(value = "gxfw1form")
	public String gxfw1form(Model model) {
		return "iLetter/zhengshufuwu_gxfw1";
	}

	@RequestMapping("gxfw1")
	@ResponseBody
	public String gxfw1(String certSn) throws JSONException {
		JSONObject json = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<WorkDealInfo> dealInfos = workDealInfoService.findgxfw1(certSn);
			System.out.println("更新业务证书序列号:"+certSn);
			System.out.println("查询到的业务数量:"+dealInfos.size());
			if (dealInfos.size() == 0) {
				// 未发现此Ukey绑定业务
				json.put("status", 103);
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				WorkDealInfo workDealInfo = dealInfos.get(0);
				json.put("dealInfoId", workDealInfo.getId());
				json.put("certSn", workDealInfo.getCertSn());
				json.put("userEmail", workDealInfo.getWorkUser().getContactEmail());
				json.put("certTime", sdf.format(workDealInfo.getWorkCertInfo().getNotbefore())
						+ " 到 " + sdf.format(workDealInfo.getWorkCertInfo().getNotafter()));
				
//				if (workDealInfo.getDelFlag().equals("1")) {
//					json.put("status", 110);
//					return json.toString();
//				}
//				
				
				
				if (workDealInfo.getWorkCertInfo() != null) {
					if (workDealInfo.getWorkCertInfo().getRenewalNextId() != null) {
						// 存在更新过的证书
						List<WorkDealInfo> infos = workDealInfoService.findgx(workDealInfo.getWorkCertInfo()
										.getRenewalNextId());
						// 传递前台审批状态 0为新建未审核 1为异常业务 2为退费用户 3为审核通过 4为 审核不通过11审核通过待获取
						if (infos.get(0).getDelFlag().equals("1")) {
							json.put("status","4");
						}else{
							
							json.put("status", infos.get(0).getDealInfoStatus());
						}
						
						
						
						// 传递前台审批状态，如果为业务为后台办理更新并未制证可在i信端做制证操作，即未持key更新业务。 106 后台更新审批通过待制证
						if ( WorkDealInfoType.TYPE_UPDATE_CERT.equals(infos.get(0).getDealInfoType())
								&& WorkDealInfoStatus.STATUS_CERT_WAIT.equals(infos.get(0).getDealInfoStatus())) {
							json.put("updateStatus", 106);
						}
						json.put("remarks", infos.get(0).getRemarks());
						json.put("archiveNo", (infos.get(0).getArchiveNo()!=null?1:0));
						json.put("id",infos.get(0).getId());
						json.put("remarks", infos.get(0).getRemarks());
						return json.toString();
					} else {
						// 没有更新过证书 可以继续使用 9为没有更新过的业务 可以更新操作
						json.put("status", 104);
					}
				}
				//判断是否可以更新证书（当前时间距离证书有效期小于30）
				if (workDealInfo.getWorkCertInfo().getNotafter()!=null) {
					Date now  = new Date();
					Date last = workDealInfo.getWorkCertInfo().getNotafter();
					long between=(last.getTime()-now.getTime())/1000;//除以1000是为了转换成秒
					Long iLong = 60*24*60*60*1l;
					if (between>iLong) {
						json.put("status", 105);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return json.toString();
	}

	// 通过AJAX判断 更新新业务
	@RequestMapping(value = "gxfw1Submit")
	@ResponseBody
	public String gxfw1Submit(Integer year,String certSn,String keySn,
			Integer dealInfoId,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			WorkDealInfo workDealInfo = workDealInfoService.get(new Long(
					dealInfoId));
			
			workDealInfo.setDelFlag("1");
			
			workDealInfoService.save(workDealInfo);
			
			//ConfigChargeAgent configChargeAgent = configChargeAgentService.get(workDealInfo.getConfigChargeAgentId());
					
			
			// 新建证书 并赋予值
			WorkCertInfo workCertInfo = workDealInfo.getWorkCertInfo();
			WorkCertInfo certInfo = new WorkCertInfo();
			// 获取更新前证书信息 并且将赋予下级证书ID
			certInfo.setRenewalPrevId(workCertInfo.getId());
			certInfo.setCreateDate( workCertInfoService.getCreateDate(workCertInfo.getId()));
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(workCertInfo.getNotafter());
			calendar.add(calendar.YEAR, year);
			Date afterDate = calendar.getTime();
			// 新证书的有效期设置
			certInfo.setNotbefore(new Timestamp(new Date().getTime()));
			certInfo.setNotafter(new Timestamp(afterDate.getTime()));
			certInfo.setWorkCertApplyInfo(workCertInfo.getWorkCertApplyInfo());
			workCertInfoService.save(certInfo);
			// 添加更新证书的 下级证书
			workCertInfo.setRenewalNextId(certInfo.getId());
			workCertInfoService.save(workCertInfo);
			// 新建一个业务 并且赋予值
			WorkDealInfo new_dealInfo = new WorkDealInfo();
			new_dealInfo.setIsIxin(true);
			if(keySn!=null&&!keySn.equals("")){
				new_dealInfo.setKeySn(keySn);
			}
			User user = systemService.getUser(workDealInfo.getCreateBy()
					.getId());
			new_dealInfo.setWorkUserHis(workDealInfo.getWorkUserHis());
			new_dealInfo.setWorkCompanyHis(workDealInfo.getWorkCompanyHis());
			new_dealInfo.setPrevId(workDealInfo.getId());
//			new_dealInfo.setCreateBy(new User(1L));
//			new_dealInfo.setUpdateBy(new User(1L));
			new_dealInfo.setCreateBy(user);//四川1期改造，将更新证书归属到证书新增申请网点
			new_dealInfo.setUpdateBy(user);//四川1期改造，将更新证书归属到证书新增申请网点
			new_dealInfo.setLastDays(getLastCertDay(workCertInfo.getNotafter()));
			new_dealInfo.setSvn(workDealInfoService.getSVN(1));
			new_dealInfo.setConfigApp(workDealInfo.getConfigApp());
			new_dealInfo.setConfigProduct(workDealInfo.getConfigProduct());
			new_dealInfo.setWorkUser(workDealInfo.getWorkUser());
			new_dealInfo.setWorkCompany(workDealInfo.getWorkCompany());
			new_dealInfo.setDealInfoType(WorkDealInfoType.TYPE_UPDATE_CERT);
			new_dealInfo.setYear(null);
			new_dealInfo.setCertSort(workDealInfo.getCertSort());
			new_dealInfo.setDealTypeName(workDealInfo.getDealTypeName());
			new_dealInfo.setCreateDate(new Timestamp(new Date().getTime()));
			new_dealInfo.setStatus(workDealInfo.getStatus());
			new_dealInfo.setDownLoad(0);
			new_dealInfo.setDealInfoStatus(WorkDealInfoStatus.STATUS_UPDATE_USER);
			new_dealInfo.setWorkCertInfo(certInfo);
			new_dealInfo.setNotafter(new Timestamp(afterDate.getTime()));
			new_dealInfo.setPayType(workDealInfo.getPayType());
			new_dealInfo.setConfigChargeAgentId(workDealInfo.getConfigChargeAgentId());
			
			dealInfoService.save(new_dealInfo);

			logUtil.saveTerminalLog(request.getRemoteHost(), "申请更新业务", request.getRemoteAddr(),certSn, "申请更新");
//			double money = configChargeAgentDetailService.selectMoney(
//					configChargeAgent,1, new_dealInfo.getYear(), new_dealInfo
//							.getConfigProduct().getProductLabel());
//			map.put("money", money);
			map.put("status", 1);
			map.put("id", new_dealInfo.getId());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 0);
		}
		return JSON.toJSONString(map);
	}
	static Long MILL = 86400000L;
	private int getLastCertDay(Date notAfter) {
		Long notAfterLong = notAfter.getTime();
		Long nowLong = System.currentTimeMillis();
		if (notAfterLong<nowLong) {
			return 0;
		}
		return (int) ((notAfterLong - nowLong) / MILL);
	}

	@RequestMapping(value = "gxfw1Nextform")
	public String gxfw1Nextform(Model model, Long dealInfoId,String mmssgg) {
		try {
			WorkDealInfo workDealInfo = workDealInfoService
					.get(new Long(dealInfoId));
//			double money = configChargeAgentDetailService.selectMoney(
//					configChargeAgent,1, workDealInfo.getYear(), workDealInfo
//							.getConfigProduct().getProductLabel());
//			model.addAttribute("money", money);
			if(mmssgg!=null&&!mmssgg.equals("")){
				if(mmssgg.equals("SQ4X85Q63F25G8R9E63R")){
					mmssgg="证书还未审核请耐心等待";
				}else{
					workDealInfo.setArchiveNo("1");
					workDealInfoService.save(workDealInfo);
				}
			}
			model.addAttribute("mmssgg",mmssgg);
			return "iLetter/zhengshufuwu_gxfw2";
		} catch (Exception e) {
			e.printStackTrace();
			return "iLetter/zhengshufuwu_gxfw2";
		}
	}

	// 通过AJAX判断
	@RequestMapping(value = "gxfw2Submit")
	@ResponseBody
	public String gxfw2Submit( String certSn) throws JSONException {
		certSn = getCertSn(certSn);
		JSONObject json = new JSONObject();
		try {
			List<WorkDealInfo> workDealInfos = workDealInfoService.findgxfw1(certSn);
			if (workDealInfos.size() == 0) {
				// 未发现存在更新的证书
				json.put("status", "101");
			} else {
				WorkDealInfo workDealInfo = workDealInfos.get(0);
				if (workDealInfo.getWorkCertInfo() != null) {
					List<WorkDealInfo> dealInfos = workDealInfoService.findgx(workDealInfo.getWorkCertInfo()
									.getRenewalNextId());
					if (dealInfos.size() == 0) {
						// 未查到下个业务信息
						json.put("status", "102");
					} else {
						WorkDealInfo dealInfo = dealInfos.get(0);
						json.put("status", dealInfo.getDealInfoStatus());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "-1");
		}
		return json.toString();
	}

	@RequestMapping(value = "gxfw2Nextform")
	public String gxfw2Nextform(Model model,Long id) {
		try {
			WorkDealInfo workDealInfo = workDealInfoService
					.get(new Long(id));
			model.addAttribute("workDealInfo", workDealInfo);
			model.addAttribute("status", WorkDealInfoStatus.WorkDealInfoStatusMap.get(workDealInfo.getDealInfoStatus()));
			if (workDealInfo.getPrevId()!=null) {
				//获取上一张证书的签名证书序列号
				WorkDealInfo oldDealInfo = workDealInfoService.get(workDealInfo.getPrevId());
					model.addAttribute("signSerialNumber", oldDealInfo.getWorkCertInfo().getSerialnumber().toLowerCase());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "iLetter/zhengshufuwu_gxfw3";
	}

	// 通过AJAX判断
	@RequestMapping(value = "gxfw3Submit")
	@ResponseBody
	public String gxfw3Submit() throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", "1");
		} catch (Exception e) {
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequestMapping(value = "gxfw3Nextform")
	public String gxfw3Nextform(Model model,Long id) {
		WorkDealInfo dealInfo = workDealInfoService.get(id);
		WorkCertInfo certInfo = dealInfo.getWorkCertInfo();
		if (certInfo!=null) {
			model.addAttribute("sn", certInfo.getSerialnumber());
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			model.addAttribute("notbefore", sdf1.format(certInfo.getNotbefore()));
			model.addAttribute("notafter", sdf1.format(certInfo.getNotafter()));//有效期
			model.addAttribute("email", dealInfo.getWorkUser().getContactEmail());//有效期
		}
		return "iLetter/zhengshufuwu_gxfw4";
	}

	@RequestMapping(value = "gxfw3falseNextform")
	public String gxfw3errorNextform(Model model) {
		return "iLetter/zhengshufuwu_gxfw4error";
	}

	// 通过序列号判断是否关联业务
//	@RequestMapping(value = "keySnBusiness")
//	@ResponseBody
//	public String keySnBusinessSubmit(String keySn, int dealInfoType)
//			throws JSONException {
//		JSONObject json = new JSONObject();
//		try {
//			List<WorkDealInfo> workDealInfos = workDealInfoService.findByKeySn(
//					keySn, dealInfoType);
//			if (workDealInfos.size() > 0) {
//				json.put("status", 1);
//			} else {
//				json.put("status", 0);
//				return json.toString();
//			}
//		} catch (Exception e) {
//			json.put("status", "0");
//		}
//		return json.toString();
//	}

	/**
	 * 
	 * @param certSn
	 * @return
	 */
	private String getCertSn(String certSn) {
		return certSn.substring(0, certSn.length() - 1);
	}
	
	
	@RequestMapping(value="downloadTemplate")
	public ModelAndView  templateDownLoad(String fileName,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		fileName = URLDecoder.decode(fileName, "utf-8");
		String filePath = deployPath+"/template/xls/"+fileName;
        String contentType = "application/octet-stream";  //二级制流,不知道文件类型可用，.*
//        String contentType = "application/x-xls";//.xls格式文件
        try {
			FileDownloadUtil.download(request, response, contentType,filePath,fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return null;
	}
	
	@RequestMapping(value="downloadDoc")
	public ModelAndView  templateDownLoadDoc(String fileName,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		fileName = URLDecoder.decode(fileName, "utf-8");
		String filePath = deployPath+"/template/doc/"+fileName;
        String contentType = "application/octet-stream";  
        try {
			FileDownloadUtil.download(request, response, contentType,filePath,fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return null;  
	}
}
