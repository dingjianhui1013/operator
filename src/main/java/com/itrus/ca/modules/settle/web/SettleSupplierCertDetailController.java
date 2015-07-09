/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

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
import com.itrus.ca.modules.settle.entity.SettleSupplierCertDetail;
import com.itrus.ca.modules.settle.service.SettleSupplierCertDetailService;

/**
 * 供应商证书结算Controller
 * @author WHW
 * @version 2014-07-17
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/settleSupplierCertDetail")
public class SettleSupplierCertDetailController extends BaseController {

	@Autowired
	private SettleSupplierCertDetailService settleSupplierCertDetailService;
	
	@ModelAttribute
	public SettleSupplierCertDetail get(@RequestParam(required=false) Long id) {
		if (id != null){
			return settleSupplierCertDetailService.get(id);
		}else{
			return new SettleSupplierCertDetail();
		}
	}
	
	@RequiresPermissions("settle:settleSupplierCertDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(SettleSupplierCertDetail settleSupplierCertDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
        Page<SettleSupplierCertDetail> page = settleSupplierCertDetailService.find(new Page<SettleSupplierCertDetail>(request, response), settleSupplierCertDetail); 
        model.addAttribute("page", page);
		return "modules/settle/settleSupplierCertDetailList";
	}

	@RequiresPermissions("settle:settleSupplierCertDetail:view")
	@RequestMapping(value = "form")
	public String form(SettleSupplierCertDetail settleSupplierCertDetail, Model model) {
		model.addAttribute("settleSupplierCertDetail", settleSupplierCertDetail);
		return "modules/settle/settleSupplierCertDetailForm";
	}

	@RequiresPermissions("settle:settleSupplierCertDetail:edit")
	@RequestMapping(value = "save")
	public String save(SettleSupplierCertDetail settleSupplierCertDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, settleSupplierCertDetail)){
			return form(settleSupplierCertDetail, model);
		}
		settleSupplierCertDetailService.save(settleSupplierCertDetail);
		return "redirect:"+Global.getAdminPath()+"/modules/settle/settleSupplierCertDetail/?repage";
	}
	
	@RequiresPermissions("settle:settleSupplierCertDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		settleSupplierCertDetailService.delete(id);
		addMessage(redirectAttributes, "删除供应商证书结算成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/settleSupplierCertDetail/?repage";
	}

}
