package com.itrus.ca.modules.finance.web;

import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.entity.FinanceQuitMoney;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.finance.service.FinanceQuitMoneyService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

@Controller
@RequestMapping(value = "${adminPath}/finance/financeQuitMoney")
public class FinanceQuitMoneyController {
	@Autowired
	private OfficeService officeService; 
	
	@Autowired
	FinanceQuitMoneyService financeQuitMoneyService;
	
	@Autowired
	FinancePaymentInfoService financePaymentInfoService;
	
	@ModelAttribute
	public FinanceQuitMoney get(@RequestParam(required = false)Long id){
		if(id != null){
			return financeQuitMoneyService.get(id);
		}else{
			return new FinanceQuitMoney();
		}
	}
	
	@RequiresPermissions("finance:financeQuitMoney:view")
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam(value = "commUserName", required = false) String commUserName
						,@RequestParam(value = "companyName", required = false) String companyName
						,@RequestParam(value = "payStartTime", required = false) String payStartTime
						,@RequestParam(value = "payEndTime", required = false) String payEndTime
						,@RequestParam(value = "quitStartTime", required = false) String quitStartTime
						,@RequestParam(value = "quitEndTime", required = false) String quitEndTime
						, HttpServletRequest request
						, HttpServletResponse response
						, Model model){
		try {
			
	
//		Page<FinanceQuitMoney> page = financeQuitMoneyService.findAllFinance(new Page<FinanceQuitMoney>(request, response), commUserName, payStartTime, payEndTime, quitStartTime, quitEndTime);
		//增加付款单位条件
		Page<FinanceQuitMoney> page = financeQuitMoneyService.findAllFinance(new Page<FinanceQuitMoney>(request, response), commUserName,companyName, payStartTime, payEndTime, quitStartTime, quitEndTime);
		
		
	
		
		if (page.getList().size() > 0 ) {
			for (int i = 0; i < page.getList().size(); i++) {
				if (page.getList().get(i).getWorkDealInfo()==null) {
					int methode = page.getList().get(i).getFinancePaymentInfo().getPaymentMethod();
					switch (methode) {
					case 1:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("现金");
						break;
					case 2:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("POS收款");
						break;
					case 3:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("银行转账");
						break;
					case 4:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("支付宝转账");
						break;
					default:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("您没有选择付款方式");
						break;
					}
				}else{
					
					
					
				}
			}
		}
//		List<FinanceQuitMoney> financeQuitMoney= financeQuitMoneyService.findAllFinance(commUserName, payStartTime, payEndTime, quitStartTime, quitEndTime);
		//增加付款单位条件
		List<FinanceQuitMoney> financeQuitMoney= financeQuitMoneyService.findAllFinance(commUserName,companyName, payStartTime, payEndTime, quitStartTime, quitEndTime);
		model.addAttribute("page", page);
		model.addAttribute("count",financeQuitMoney.size());
		
		model.addAttribute("commUserName", commUserName);
		model.addAttribute("companyName", companyName);
		model.addAttribute("payStartTime", payStartTime);
		model.addAttribute("payEndTime", payEndTime);
		model.addAttribute("quitStartTime", quitStartTime);
		model.addAttribute("quitEndTime", quitEndTime);
		
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "modules/finance/financeQuitMoneyList";
	}
	
	/*
	 * 退费
	 */
	@RequestMapping(value = "quitMoney")
	@ResponseBody
	public String quitMoney(Long quitMoneyId, String quitReason, Model model) throws JSONException{
		JSONObject json = new JSONObject();
		try{
			FinancePaymentInfo financePaymentInfo = financePaymentInfoService.get(quitMoneyId);
			financeQuitMoneyService.save(financePaymentInfo, quitReason);
			financePaymentInfo.setResidueMoney(0d);
			financePaymentInfo.setQuitMoneyStatus(1);
			financePaymentInfoService.save(financePaymentInfo);
		}catch(Exception ex){
			ex.printStackTrace();
			json.put("status", 0);
			return json.toString();
		}
		json.put("status", 1);
		return json.toString();
	}
	
	
	/*
	 * 退费
	 */
	@RequestMapping(value = "quitMoneyWorkDealInfo")
	@ResponseBody
	public String quitMoneyWorkDealInfo(Long quitMoneyId, String quitReason, Model model) throws JSONException{
		JSONObject json = new JSONObject();
		try{
			FinanceQuitMoney financeQuitMoney = financeQuitMoneyService.get(quitMoneyId);
			financeQuitMoney.setStatus("2");
			//financeQuitMoney.setRession(quitReason);
			financeQuitMoneyService.save(financeQuitMoney);
		}catch(Exception ex){
			ex.printStackTrace();
			json.put("status", 0);
			return json.toString();
		}
		json.put("status", 1);
		return json.toString();
	}
	
	
	
	
	@RequiresPermissions("finance:financeQuitMoney:view")
	@RequestMapping(value = { "dealQuitList"})
	public String dealQuitList(@RequestParam(value = "companyName", required = false) String companyName
						,@RequestParam(value = "contactName", required = false) String contactName
						,@RequestParam(value = "quitStartTime", required = false) String quitStartTime
						,@RequestParam(value = "quitEndTime", required = false) String quitEndTime
						, HttpServletRequest request
						, HttpServletResponse response
						, Model model){

		try {
			
		//权限控制
		List<Office> offices =  officeService.getOfficeByType(UserUtils.getUser(), 2);
		List<Long> officeIds = new  ArrayList();
		for(Office o:offices){
			officeIds.add(o.getId());
		}
			
//		Page<FinanceQuitMoney> page = financeQuitMoneyService.findAllDealInfo(new Page<FinanceQuitMoney>(request, response), companyName,contactName,quitStartTime, quitEndTime);
		Page<FinanceQuitMoney> page = financeQuitMoneyService.findAllDealInfo(new Page<FinanceQuitMoney>(request, response), companyName,contactName,quitStartTime, quitEndTime,officeIds);
		
		
	
		
		if (page.getList().size() > 0 ) {
			for (int i = 0; i < page.getList().size(); i++) {
				if (page.getList().get(i).getWorkDealInfo()==null) {
					int methode = page.getList().get(i).getFinancePaymentInfo().getPaymentMethod();
					switch (methode) {
					case 1:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("现金");
						break;
					case 2:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("POS收款");
						break;
					case 3:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("银行转账");
						break;
					case 4:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("支付宝转账");
						break;
					default:
						page.getList().get(i).getFinancePaymentInfo().setPaymentMethodName("您没有选择付款方式");
						break;
					}
				}else{
					
					
					
				}
			}
		}
//		List<FinanceQuitMoney> financeQuitMoney= financeQuitMoneyService.findAllDealInfo(companyName,contactName, quitStartTime, quitEndTime);
		List<FinanceQuitMoney> financeQuitMoney= financeQuitMoneyService.findAllDealInfo(companyName,contactName, quitStartTime, quitEndTime,officeIds);
		//汇总
		Map<String,Double> huizong = new HashMap();
		Double total = 0d;
		for(FinanceQuitMoney a:financeQuitMoney){
			total += a.getQuitMoney();
			String appName = a.getWorkDealInfo().getConfigApp().getAppName();
			if(huizong.containsKey(appName)){
				Double money = huizong.get(appName);
				money += a.getQuitMoney();
				huizong.put(appName, money);
			}else{
				huizong.put(appName, a.getQuitMoney());
			}
		}
		//汇总
		model.addAttribute("huizong", huizong);
		model.addAttribute("total", total);
		
		model.addAttribute("page", page);
		model.addAttribute("companyName", companyName);
		model.addAttribute("contactName", contactName);
		model.addAttribute("quitStartTime", quitStartTime);
		model.addAttribute("quitStartTime", quitEndTime);
		
		
		model.addAttribute("count",financeQuitMoney.size());
		
		
	
		model.addAttribute("quitStartTime", quitStartTime);
		model.addAttribute("quitEndTime", quitEndTime);
		
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return "modules/finance/financeQuitMoneyListByDealInfo";
	}
	
	
	
}
