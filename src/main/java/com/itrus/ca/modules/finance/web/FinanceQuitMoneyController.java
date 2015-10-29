package com.itrus.ca.modules.finance.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.entity.FinanceQuitMoney;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.finance.service.FinanceQuitMoneyService;

@Controller
@RequestMapping(value = "${adminPath}/finance/financeQuitMoney")
public class FinanceQuitMoneyController {

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
						,@RequestParam(value = "payStartTime", required = false) String payStartTime
						,@RequestParam(value = "payEndTime", required = false) String payEndTime
						,@RequestParam(value = "quitStartTime", required = false) String quitStartTime
						,@RequestParam(value = "quitEndTime", required = false) String quitEndTime
						, HttpServletRequest request
						, HttpServletResponse response
						, Model model){
//		System.out.println(result.getErrorCount());
		Page<FinanceQuitMoney> page = financeQuitMoneyService.findAll(new Page<FinanceQuitMoney>(request, response), commUserName, payStartTime, payEndTime, quitStartTime, quitEndTime);
		if (page.getList().size() > 0) {
			for (int i = 0; i < page.getList().size(); i++) {
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
			}
		}
		List<FinanceQuitMoney> financeQuitMoney= financeQuitMoneyService.findAll(commUserName, payStartTime, payEndTime, quitStartTime, quitEndTime);
		model.addAttribute("page", page);
		model.addAttribute("count",financeQuitMoney.size());
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
			json.put("status", 0);
			return json.toString();
		}
		json.put("status", 1);
		return json.toString();
	}
}
