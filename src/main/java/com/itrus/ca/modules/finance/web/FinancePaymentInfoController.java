/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.finance.web;

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

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
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

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;

/**
 * 支付信息Controller
 * 
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/finance/financePaymentInfo")
public class FinancePaymentInfoController extends BaseController {

	@Autowired
	private FinancePaymentInfoService financePaymentInfoService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private WorkCompanyService workCompanyService;

	private LogUtil logUtil = new LogUtil();

	private String realPath;

	@ModelAttribute
	public FinancePaymentInfo get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return financePaymentInfoService.get(id);
		} else {
			return new FinancePaymentInfo();
		}
	}

	@RequiresPermissions("finance:financePaymentInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(FinancePaymentInfo financePaymentInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		// Page<FinancePaymentInfo> page = financePaymentInfoService.find(new
		// Page<FinancePaymentInfo>(request, response), financePaymentInfo);
		// model.addAttribute("page", page);
		return "modules/finance/financePaymentInfoList";
	}

	@RequiresPermissions("finance:financePaymentInfo:view")
	@RequestMapping(value = "payList")
	public String listPay(FinancePaymentInfo financePaymentInfo,
			Date startTime, Date endTime, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// if (!user.isAdmin()){
		financePaymentInfo.setCreateBy(user);
		if (startTime != null && endTime != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			model.addAttribute("startTime", sdf.format(startTime));
			model.addAttribute("endTime", sdf.format(endTime));
		}

		// }
		Page<FinancePaymentInfo> page = financePaymentInfoService.find(
				new Page<FinancePaymentInfo>(request, response),
				financePaymentInfo, startTime, endTime);
		if (page.getList().size() > 0) {
			for (int i = 0; i < page.getList().size(); i++) {
				int methode = page.getList().get(i).getPaymentMethod();
				switch (methode) {
				case 1:
					page.getList().get(i).setPaymentMethodName("现金");
					break;
				case 2:
					page.getList().get(i).setPaymentMethodName("POS收款");
					break;
				case 3:
					page.getList().get(i).setPaymentMethodName("银行转账");
					break;
				case 4:
					page.getList().get(i).setPaymentMethodName("支付宝转账");
					break;
				default:
					page.getList().get(i).setPaymentMethodName("您没有选择付款方式");
					break;
				}
			}
		}

		model.addAttribute("page", page);
		return "modules/finance/financePaymentInfoList";
	}

	@RequiresPermissions("finance:financePaymentInfo:view")
	@RequestMapping(value = "show")
	public String show(FinancePaymentInfo financePaymentInfo, Model model) {

		int method = financePaymentInfo.getPaymentMethod();
		switch (method) {
		case 1:
			financePaymentInfo.setPaymentMethodName("现金");
			break;
		case 2:
			financePaymentInfo.setPaymentMethodName("POS收款");
			break;
		case 3:
			financePaymentInfo.setPaymentMethodName("银行转账");
			break;
		case 4:
			financePaymentInfo.setPaymentMethodName("支付宝转账");
			break;
		default:
			financePaymentInfo.setPaymentMethodName("您没有选择付款方式");
			break;
		}

		model.addAttribute("financePaymentInfo", financePaymentInfo);

//		financePaymentInfo.setCreateDate(new Date());
		List<ConfigApp> apps = configAppService.findAllConfigApp();
		model.addAttribute("apps", apps);
		return "modules/finance/financePaymentInfoShow";
	}

	@RequiresPermissions("finance:financePaymentInfo:view")
	@RequestMapping(value = "form")
	public String form(FinancePaymentInfo financePaymentInfo, Model model) {
		model.addAttribute("financePaymentInfo", financePaymentInfo);
		List<ConfigApp> apps = configAppService.findAllConfigApp();
		// List<FinancePaymentInfo> paymentInfos =
		// financePaymentInfoService.findRemark();
		// int remark = 0;
		// if (financePaymentInfo.getId()==null) {
		// List<FinancePaymentInfo> financePaymentInfos =
		// financePaymentInfoService.findAll();
		//
		// if (financePaymentInfos.size()>0) {
		// remark+=Integer.parseInt(financePaymentInfos.get(0).getRemark());
		// remark+=1;
		// }else {
		// remark =1;
		// }
		// }
		//
		// model.addAttribute("remark",remark);
		model.addAttribute("apps", apps);
		return "modules/finance/financePaymentInfoForm";
	}

	@RequiresPermissions("finance:financePaymentInfo:edit")
	@RequestMapping(value = "save")
	public String save(FinancePaymentInfo financePaymentInfo,
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, financePaymentInfo)) {
			return form(financePaymentInfo, model);
		}
		if (financePaymentInfo.getId() == null) {
			Long remark = financePaymentInfoService.queryCount();
			if (remark > 0) {
				remark = remark + 1;
				financePaymentInfo.setRemark(remark + "");
			} else {
				financePaymentInfo.setRemark("1");
			}
			financePaymentInfo
					.setResidueMoney(financePaymentInfo.getPaymentMoney());//新增 把录入的支付金额作为余额
		}else{
			financePaymentInfo
					.setResidueMoney(financePaymentInfo.getResidueMoney());//更新 设置为当前记录的余额
		}
		// WorkCompany workCompany = new WorkCompany();
		// workCompany.setId(companyId);
//		if (configAppId!=null) {
//			ConfigApp configApp = new ConfigApp();
//			configApp.setId(configAppId);
//			financePaymentInfo.setConfigApp(configApp);
//		}
		// financePaymentInfo.setWorkCompany(workCompany);


		financePaymentInfo.setCreateDate(new Date());//设置创建时间
		String detail = "";
		if (financePaymentInfo.getId() == null) {
			detail = "支付信息添加：" + financePaymentInfo.getCompany() + "公司支付"
					+ financePaymentInfo.getPaymentMoney() + "元";
		} else {
			detail = "支付信息修改：" + financePaymentInfo.getId() + ","
					+ financePaymentInfo.getCompany() + "公司";
		}
		financePaymentInfo.setQuitMoneyStatus(0);
		financePaymentInfoService.save(financePaymentInfo);
		logUtil.saveSysLog("财务管理", detail, "");
		addMessage(redirectAttributes, "保存支付信息成功");
		return "redirect:" + Global.getAdminPath()
				+ "/finance/financePaymentInfo/payList";
	}

	@RequiresPermissions("finance:financePaymentInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
			financePaymentInfoService.deleteById(id);
			addMessage(redirectAttributes, "删除支付信息成功");
		return "redirect:" + Global.getAdminPath()
				+ "/finance/financePaymentInfo/payList";
	}

	// 批量导入
	@RequestMapping("addAttach")
	@ResponseBody
	public String importFile(
			@RequestParam(value = "fileName", required = true) MultipartFile file)
			throws IllegalStateException, IOException, JSONException {
		JSONObject json = new JSONObject();
		String ifExcel = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));//截取.xlsx或.xls
		/*
		 * 判断@Param file文件是否为excel文件
		 */
		if(!(ifExcel.equals(".xls") || ifExcel.equals(".xlsx"))){
			json.put("status", -1);
			json.put("msg", "模板必须为Excel文件");
			return json.toString();
		}
		
		/*
		 * 解析存储excel文件
		 */
		try{
			json = financePaymentInfoService.saveExcelDate(file, ifExcel);
		}catch(Exception ex){
			json.put("status", -1);
			json.put("msg", json.toString());
			return json.toString();
		}
		return json.toString();
	}

	public String getRealPath() {
		return realPath;
	}

	@Value(value = "${uploadFile.path}")
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	@RequestMapping("companyName")
	@ResponseBody
	public String companyName() throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			List<String> list = financePaymentInfoService.findCompanyId();
			ArrayList<Object> lis = new ArrayList<Object>();
			for (String workCompany : list) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", workCompany);
				lis.add(map);
			}
			jsonObject.put("list", lis);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	@RequiresPermissions("finance:financePaymentInfo:editForm")
	@RequestMapping(value = "editForm")
	public String editForm(FinancePaymentInfo financePaymentInfo, Model model){
			model.addAttribute("financePaymentInfo", financePaymentInfo);
			List<ConfigApp> apps = configAppService.findAllConfigApp();
			model.addAttribute("apps", apps);
			return "modules/finance/financePaymentInfoEditForm";
	}

	@RequestMapping(value = "editSave")
	public String editSave(FinancePaymentInfo financePaymentInfo,
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, financePaymentInfo)) {
			return form(financePaymentInfo, model);
		}
		financePaymentInfoService.save(financePaymentInfo);
		logUtil.saveSysLog("财务管理", "鉴证/客服对编号"+financePaymentInfo.getId()+"支付信息进行了修改", "");
		addMessage(redirectAttributes, "保存支付信息成功");
		return "redirect:" + Global.getAdminPath()
				+ "/finance/financePaymentInfo/payList";
	}

	/**
	 * 检测 流水号重复
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="checkLr")
	@ResponseBody
	public String checkLr(HttpServletRequest request,HttpServletResponse response){
		String serialNum = request.getParameter("serialNum");
		String company= request.getParameter("company");
		String paymentBank  = request.getParameter("paymentBank");
		String id = request.getParameter("id");
		boolean flag = financePaymentInfoService.findByCompanyBank(serialNum,paymentBank,id);
		JSONObject jsonObject = new JSONObject();
		try{
			if (flag){
				jsonObject.put("status","1");
				jsonObject.put("msg",paymentBank+"下的交易流水号"+serialNum+"已被录入，请确认");
				return jsonObject.toString();
			}else{
				jsonObject.put("status","0");
				return jsonObject.toString();
			}
		}catch (Exception e){
			e.printStackTrace();

		}
		return jsonObject.toString();
	}
	@RequiresPermissions("finance:financePaymentInfo:view")
	@RequestMapping(value ="summary")
	public String Summary() {
		System.out.println("成功进入");
		return "modules/finance/yearSummary";
	}
}
