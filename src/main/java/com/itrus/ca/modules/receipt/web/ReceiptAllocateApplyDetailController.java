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
import com.itrus.ca.modules.receipt.entity.ReceiptAllocateApplyDetail;
import com.itrus.ca.modules.receipt.service.ReceiptAllocateApplyDetailService;

/**
 * 申请发票类型详情管理Controller
 * @author HuHao
 * @version 2015-03-17
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptAllocateApplyDetail")
public class ReceiptAllocateApplyDetailController extends BaseController {

	@Autowired
	private ReceiptAllocateApplyDetailService receiptAllocateApplyDetailService;
	
	@ModelAttribute
	public ReceiptAllocateApplyDetail get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptAllocateApplyDetailService.get(id);
		}else{
			return new ReceiptAllocateApplyDetail();
		}
	}
	
	@RequiresPermissions("receipt:receiptAllocateApplyDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptAllocateApplyDetail receiptAllocateApplyDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<ReceiptAllocateApplyDetail> page = receiptAllocateApplyDetailService.find(new Page<ReceiptAllocateApplyDetail>(request, response), receiptAllocateApplyDetail); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptAllocateApplyDetailList";
	}

	@RequiresPermissions("receipt:receiptAllocateApplyDetail:view")
	@RequestMapping(value = "form")
	public String form(ReceiptAllocateApplyDetail receiptAllocateApplyDetail, Model model) {
		model.addAttribute("receiptAllocateApplyDetail", receiptAllocateApplyDetail);
		return "modules/receipt/receiptAllocateApplyDetailForm";
	}

	@RequiresPermissions("receipt:receiptAllocateApplyDetail:edit")
	@RequestMapping(value = "save")
	public String save(ReceiptAllocateApplyDetail receiptAllocateApplyDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptAllocateApplyDetail)){
			return form(receiptAllocateApplyDetail, model);
		}
		receiptAllocateApplyDetailService.save(receiptAllocateApplyDetail);
		addMessage(redirectAttributes, "保存申请发票类型详情管理成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptAllocateApplyDetail/?repage";
	}
	
	@RequiresPermissions("receipt:receiptAllocateApplyDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		receiptAllocateApplyDetailService.delete(id);
		addMessage(redirectAttributes, "删除申请发票类型详情管理成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptAllocateApplyDetail/?repage";
	}

}
