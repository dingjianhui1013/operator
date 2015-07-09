/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.receipt.entity.ReceiptLog;
import com.itrus.ca.modules.receipt.service.ReceiptLogService;

/**
 * 发票定时统计Controller
 * @author WangHongwei
 * @version 2014-07-15
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptLog")
public class ReceiptLogController extends BaseController {

	@Autowired
	private ReceiptLogService receiptLogService;
	
	@ModelAttribute
	public ReceiptLog get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptLogService.get(id);
		}else{
			return new ReceiptLog();
		}
	}
	
	@RequiresPermissions("receipt:receiptLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptLog receiptLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<ReceiptLog> page = receiptLogService.find(new Page<ReceiptLog>(request, response), receiptLog); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptLogList";
	}

	@RequiresPermissions("receipt:receiptLog:view")
	@RequestMapping(value = "form")
	public String form(ReceiptLog receiptLog, Model model) {
		model.addAttribute("receiptLog", receiptLog);
		return "modules/receipt/receiptLogForm";
	}

	@RequiresPermissions("receipt:receiptLog:edit")
	@RequestMapping(value = "save")
	public String save(ReceiptLog receiptLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptLog)){
			return form(receiptLog, model);
		}
		receiptLogService.save(receiptLog);
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptLog/?repage";
	}
	
	@RequiresPermissions("receipt:receiptLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		addMessage(redirectAttributes, "删除发票定时统计成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptLog/?repage";
	}

}
