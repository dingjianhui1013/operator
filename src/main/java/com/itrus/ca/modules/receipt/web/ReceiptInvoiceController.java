/**
' * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;

/**
 * 出库信息Controller
 * @author WHW
 * @version 2014-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptInvoice")
public class ReceiptInvoiceController extends BaseController {

	private LogUtil logUtil = new LogUtil();
	
	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	
	@ModelAttribute
	public ReceiptInvoice get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptInvoiceService.get(id);
		}else{
			return new ReceiptInvoice();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptInvoice receiptInvoice, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			receiptInvoice.setCreateBy(user);
		}
        Page<ReceiptInvoice> page = receiptInvoiceService.find(new Page<ReceiptInvoice>(request, response), receiptInvoice); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptInvoiceList";
	}

	@RequiresPermissions("receipt:receiptInvoice:view")
	@RequestMapping(value = "form")
	public String form(ReceiptInvoice receiptInvoice, Model model) {
		model.addAttribute("receiptInvoice", receiptInvoice);
		return "modules/receipt/receiptInvoiceForm";
	}

	@RequiresPermissions("receipt:receiptInvoice:edit")
	@RequestMapping(value = "save")
	public String save(ReceiptInvoice receiptInvoice, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptInvoice)){
			return form(receiptInvoice, model);
		}
		receiptInvoiceService.save(receiptInvoice);
//		addMessage(redirectAttributes, "保存出库信息'" + receiptInvoice.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptInvoice/?repage";
	}
	
	@RequiresPermissions("receipt:receiptInvoice:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		receiptInvoiceService.delete(id);
		addMessage(redirectAttributes, "删除出库信息成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptInvoice/?repage";
	}
	
	
	
	@RequestMapping(value = "verifiInvoice")
	@ResponseBody
	public String verifiInvoice(){
		JSONObject json = new JSONObject();
		try {
			json.put("status", false);
			User user = UserUtils.getUser();
			boolean check =  receiptInvoiceService.checkOffice(user.getOffice());
			json.put("status", check);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	
	
}
