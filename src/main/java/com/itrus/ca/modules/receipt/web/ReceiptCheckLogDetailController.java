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
import com.itrus.ca.modules.receipt.entity.ReceiptCheckLogDetail;
import com.itrus.ca.modules.receipt.service.ReceiptCheckLogDetailService;

/**
 * 发票盘点详情Controller
 * @author HuHao
 * @version 2015-03-20
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptCheckLogDetail")
public class ReceiptCheckLogDetailController extends BaseController {

	@Autowired
	private ReceiptCheckLogDetailService receiptCheckLogDetailService;
	
	@ModelAttribute
	public ReceiptCheckLogDetail get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptCheckLogDetailService.get(id);
		}else{
			return new ReceiptCheckLogDetail();
		}
	}
	
	@RequiresPermissions("receipt:receiptCheckLogDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptCheckLogDetail receiptCheckLogDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
//		if (!user.isAdmin()){
//			receiptCheckLogDetail.setCreateBy(user);
//		}
        Page<ReceiptCheckLogDetail> page = receiptCheckLogDetailService.find(new Page<ReceiptCheckLogDetail>(request, response), receiptCheckLogDetail); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptCheckLogDetailList";
	}

	@RequiresPermissions("receipt:receiptCheckLogDetail:view")
	@RequestMapping(value = "form")
	public String form(ReceiptCheckLogDetail receiptCheckLogDetail, Model model) {
		model.addAttribute("receiptCheckLogDetail", receiptCheckLogDetail);
		return "modules/receipt/receiptCheckLogDetailForm";
	}

	@RequiresPermissions("receipt:receiptCheckLogDetail:edit")
	@RequestMapping(value = "save")
	public String save(ReceiptCheckLogDetail receiptCheckLogDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptCheckLogDetail)){
			return form(receiptCheckLogDetail, model);
		}
		receiptCheckLogDetailService.save(receiptCheckLogDetail);
		//addMessage(redirectAttributes, "保存发票盘点详情'" + receiptCheckLogDetail.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptCheckLogDetail/?repage";
	}
	
	@RequiresPermissions("receipt:receiptCheckLogDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		receiptCheckLogDetailService.delete(id);
		addMessage(redirectAttributes, "删除发票盘点详情成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptCheckLogDetail/?repage";
	}

}
