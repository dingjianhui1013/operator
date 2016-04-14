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
import com.itrus.ca.modules.signature.entity.SignatureAgentBoundConfigApp;
import com.itrus.ca.modules.signature.service.SignatureAgentBoundConfigAppService;

/**
 * 计费策略模版详情历史Controller
 * @author HuHao
 * @version 2016-03-20
 */
@Controller
@RequestMapping(value = "${adminPath}/signature/signatureAgentBoundConfigApp")
public class SignatureAgentBoundConfigAppController extends BaseController {

	@Autowired
	private SignatureAgentBoundConfigAppService signatureAgentBoundConfigAppService;
	
	@ModelAttribute
	public SignatureAgentBoundConfigApp get(@RequestParam(required=false) Long id) {
		if (id != null){
			return signatureAgentBoundConfigAppService.get(id);
		}else{
			return new SignatureAgentBoundConfigApp();
		}
	}
	
	@RequiresPermissions("signature:signatureAgentBoundConfigApp:view")
	@RequestMapping(value = {"list", ""})
	public String list(SignatureAgentBoundConfigApp signatureAgentBoundConfigApp, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			signatureAgentBoundConfigApp.setCreateBy(user);
		}
        Page<SignatureAgentBoundConfigApp> page = signatureAgentBoundConfigAppService.find(new Page<SignatureAgentBoundConfigApp>(request, response), signatureAgentBoundConfigApp); 
        model.addAttribute("page", page);
		return "modules/signature/signatureAgentBoundConfigAppList";
	}

	@RequiresPermissions("signature:signatureAgentBoundConfigApp:view")
	@RequestMapping(value = "form")
	public String form(SignatureAgentBoundConfigApp signatureAgentBoundConfigApp, Model model) {
		model.addAttribute("signatureAgentBoundConfigApp", signatureAgentBoundConfigApp);
		return "modules/signature/signatureAgentBoundConfigAppForm";
	}

	@RequiresPermissions("signature:signatureAgentBoundConfigApp:edit")
	@RequestMapping(value = "save")
	public String save(SignatureAgentBoundConfigApp signatureAgentBoundConfigApp, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, signatureAgentBoundConfigApp)){
			return form(signatureAgentBoundConfigApp, model);
		}
		signatureAgentBoundConfigAppService.save(signatureAgentBoundConfigApp);
		return "redirect:"+Global.getAdminPath()+"/modules/signature/signatureAgentBoundConfigApp/?repage";
	}
	
	@RequiresPermissions("signature:signatureAgentBoundConfigApp:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		signatureAgentBoundConfigAppService.delete(id);
		addMessage(redirectAttributes, "删除计费策略模版详情历史成功");
		return "redirect:"+Global.getAdminPath()+"/modules/signature/signatureAgentBoundConfigApp/?repage";
	}

}
