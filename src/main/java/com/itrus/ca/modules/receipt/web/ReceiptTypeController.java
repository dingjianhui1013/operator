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
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.service.ReceiptTypeService;

/**
 * 发票类型管理Controller
 * @author HuHao
 * @version 2015-03-17
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptType")
public class ReceiptTypeController extends BaseController {

	@Autowired
	private ReceiptTypeService receiptTypeService;
	
	@ModelAttribute
	public ReceiptType get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptTypeService.get(id);
		}else{
			return new ReceiptType();
		}
	}
	
	@RequiresPermissions("receipt:receiptType:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptType receiptType, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			receiptType.setCreateBy(user);
		}
        Page<ReceiptType> page = receiptTypeService.find(new Page<ReceiptType>(request, response), receiptType); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptTypeList";
	}

	@RequiresPermissions("receipt:receiptType:view")
	@RequestMapping(value = "form")
	public String form(ReceiptType receiptType, Model model) {
		model.addAttribute("receiptType", receiptType);
		return "modules/receipt/receiptTypeForm";
	}

	@RequiresPermissions("receipt:receiptType:edit")
	@RequestMapping(value = "save")
	public String save(ReceiptType receiptType, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptType)){
			return form(receiptType, model);
		}
		receiptTypeService.save(receiptType);
		addMessage(redirectAttributes, "保存发票类型管理'" + receiptType.getTypeName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptType/?repage";
	}
	
	@RequiresPermissions("receipt:receiptType:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		receiptTypeService.delete(id);
		addMessage(redirectAttributes, "删除发票类型管理成功");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptType/?repage";
	}

}
