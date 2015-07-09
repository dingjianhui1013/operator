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
import com.itrus.ca.modules.receipt.entity.ReceiptDepotTypeStatistics;
import com.itrus.ca.modules.receipt.service.ReceiptDepotTypeStatisticsService;

/**
 * 发票库存详情Controller
 * @author HuHao
 * @version 2015-03-18
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptDepotTypeStatistics")
public class ReceiptDepotTypeStatisticsController extends BaseController {

	@Autowired
	private ReceiptDepotTypeStatisticsService receiptDepotTypeStatisticsService;
	
	@ModelAttribute
	public ReceiptDepotTypeStatistics get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptDepotTypeStatisticsService.get(id);
		}else{
			return new ReceiptDepotTypeStatistics();
		}
	}
	
	@RequiresPermissions("receipt:receiptDepotTypeStatistics:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptDepotTypeStatistics receiptDepotTypeStatistics, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			receiptDepotTypeStatistics.setCreateBy(user);
		}
        Page<ReceiptDepotTypeStatistics> page = receiptDepotTypeStatisticsService.find(new Page<ReceiptDepotTypeStatistics>(request, response), receiptDepotTypeStatistics); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptDepotTypeStatisticsList";
	}

	@RequiresPermissions("receipt:receiptDepotTypeStatistics:view")
	@RequestMapping(value = "form")
	public String form(ReceiptDepotTypeStatistics receiptDepotTypeStatistics, Model model) {
		model.addAttribute("receiptDepotTypeStatistics", receiptDepotTypeStatistics);
		return "modules/receipt/receiptDepotTypeStatisticsForm";
	}

	@RequiresPermissions("receipt:receiptDepotTypeStatistics:edit")
	@RequestMapping(value = "save")
	public String save(ReceiptDepotTypeStatistics receiptDepotTypeStatistics, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptDepotTypeStatistics)){
			return form(receiptDepotTypeStatistics, model);
		}
		receiptDepotTypeStatisticsService.save(receiptDepotTypeStatistics);
		//addMessage(redirectAttributes, "保存发票库存详情'" + receiptDepotTypeStatistics.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptDepotTypeStatistics/?repage";
	}
	
	@RequiresPermissions("receipt:receiptDepotTypeStatistics:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		receiptDepotTypeStatisticsService.delete(id);
		addMessage(redirectAttributes, "删除发票库存详情成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptDepotTypeStatistics/?repage";
	}

}
