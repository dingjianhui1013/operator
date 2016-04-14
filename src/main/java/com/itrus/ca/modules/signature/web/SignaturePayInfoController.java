/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.web;

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
import com.itrus.ca.modules.signature.entity.SignaturePayInfo;
import com.itrus.ca.modules.signature.service.SignaturePayInfoService;

/**
 * 计费策略模版详情历史Controller
 * @author HuHao
 * @version 2016-03-23
 */
@Controller
@RequestMapping(value = "${adminPath}/signature/signaturePayInfo")
public class SignaturePayInfoController extends BaseController {

	@Autowired
	private SignaturePayInfoService signaturePayInfoService;
	
	@ModelAttribute
	public SignaturePayInfo get(@RequestParam(required=false) Long id) {
		if (id != null){
			return signaturePayInfoService.get(id);
		}else{
			return new SignaturePayInfo();
		}
	}
	
	@RequiresPermissions("signature:signaturePayInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(SignaturePayInfo signaturePayInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			signaturePayInfo.setCreateBy(user);
		}
        Page<SignaturePayInfo> page = signaturePayInfoService.find(new Page<SignaturePayInfo>(request, response), signaturePayInfo); 
        model.addAttribute("page", page);
		return "modules/signature/signaturePayInfoList";
	}

	@RequiresPermissions("signature:signaturePayInfo:view")
	@RequestMapping(value = "form")
	public String form(SignaturePayInfo signaturePayInfo, Model model) {
		model.addAttribute("signaturePayInfo", signaturePayInfo);
		return "modules/signature/signaturePayInfoForm";
	}

	@RequiresPermissions("signature:signaturePayInfo:edit")
	@RequestMapping(value = "save")
	public String save(SignaturePayInfo signaturePayInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, signaturePayInfo)){
			return form(signaturePayInfo, model);
		}
		signaturePayInfoService.save(signaturePayInfo);
		//addMessage(redirectAttributes, "保存计费策略模版详情历史'" + signaturePayInfo.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/signature/signaturePayInfo/?repage";
	}
	
	@RequiresPermissions("signature:signaturePayInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		signaturePayInfoService.delete(id);
		addMessage(redirectAttributes, "删除计费策略模版详情历史成功");
		return "redirect:"+Global.getAdminPath()+"/modules/signature/signaturePayInfo/?repage";
	}

}
