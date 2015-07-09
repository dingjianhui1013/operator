/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.settle.entity.SettleVTN;
import com.itrus.ca.modules.settle.service.SettleVTNService;

/**
 * VTN管理Controller
 * @author HuHao
 * @version 2015-03-12
 */
@Controller
@RequestMapping(value = "${adminPath}/settlevtn/settleVTN")
public class SettleVTNController extends BaseController {

	@Autowired
	private SettleVTNService settleVTNService;
	
	@Autowired
	private ConfigSupplierService configSupplierService;
	
	@Autowired
	private ConfigAppService configAppService;
	
	@ModelAttribute
	public SettleVTN get(@RequestParam(required=false) Long id) {
		if (id != null){
			return settleVTNService.get(id);
		}else{
			return new SettleVTN();
		}
	}
	
	@RequiresPermissions("settlevtn:settleVTN:view")
	@RequestMapping(value = {"list", ""})
	public String list(SettleVTN settleVTN, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model,
			Date startTime,
			Date endTime
			) {
		//User user = UserUtils.getUser();
//		if (!user.isAdmin()){
//			settleVTN.setCreateBy(user);
//		}
		if (endTime!=null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
		}
        Page<SettleVTN> page = settleVTNService.find(new Page<SettleVTN>(request, response), settleVTN,startTime,endTime); 
        model.addAttribute("page", page);
        model.addAttribute("startTime", startTime==null? new Date(System.currentTimeMillis()- 30L*24*60*60*1000): startTime);   //开始时间为空，则为当前时间前30天
		model.addAttribute("endTime", endTime ==null? new Date(): endTime); //结束时间，默认当前时间
		return "modules/settle/settleVTNList";
	}

	//@RequiresPermissions("settlevtn:settleVTN:view")
	@RequestMapping(value = "form")
	public String form(SettleVTN settleVTN, Model model) {
		
		List<ConfigSupplier> configSuppliers = configSupplierService.findByCert();
		List<ConfigApp> apps = configAppService.findAllConfigApp();
		
		
		model.addAttribute("apps",apps);
		model.addAttribute("configSuppliers",configSuppliers);
		model.addAttribute("supplierSize",configSuppliers.size());
		model.addAttribute("settleVTN", settleVTN);
		return "modules/settle/settleVTNForm";
	}
	
	@RequestMapping(value = "update")
	public String Update(SettleVTN settleVTN, Model model) {
		
		List<ConfigSupplier> configSuppliers = configSupplierService.findByCert();
		List<ConfigApp> apps = configAppService.findAllConfigApp();
		
		
		ConfigApp app = configAppService.findByAppname(settleVTN.getAppName());
		if (app!=null) {
			model.addAttribute("appType",0);
		}else {
			model.addAttribute("appType",1);
		}
		
		
		model.addAttribute("apps",apps);
		model.addAttribute("configSuppliers",configSuppliers);
		model.addAttribute("supplierSize",configSuppliers.size());
		model.addAttribute("settleVTN", settleVTN);
		return "modules/settle/settleVTNFormUpdate";
	}
	
	@RequiresPermissions("settlevtn:settleVTN:edit")
	@RequestMapping(value = "saveUpdate")
	public String saveUpdate(
			SettleVTN settleVTN, 
			Model model,
			RedirectAttributes redirectAttributes
			) {
		if (!beanValidator(model, settleVTN)){
			return form(settleVTN, model);
		}
		
		String[] names = settleVTN.getAppName().split(",");
		String appName = "";
		for (int i = 0; i < names.length; i++) {
			if (!names[i].equals("0")&&!names[i].equals("")) {
				appName = names[i];
			}
		}
		settleVTN.setAppName(appName);
		settleVTN.setCountPrice(settleVTN.getCount()*settleVTN.getPrice());
		try{	
				settleVTNService.save(settleVTN);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addMessage(redirectAttributes, "修改VTN信息失败");
		}
		addMessage(redirectAttributes, "修改VTN信息成功");
		return "redirect:"+ Global.getAdminPath()+"/settlevtn/settleVTN/?repage";
	}
	
	
	

	@RequiresPermissions("settlevtn:settleVTN:edit")
	@RequestMapping(value = "save")
	public String save(
			SettleVTN settleVTN, 
			Model model,
			RedirectAttributes redirectAttributes,
			Long configSupplierId,
			String productName,
			String appName,
			String count,
			String price
			) {
		if (!beanValidator(model, settleVTN)){
			return form(settleVTN, model);
		}
		String[] products= productName.split(",");
		String[] counts = count.split(",");
		String[] prices = price.split(",");
		String[] apps= appName.split(",");
		try {
			List<String> appsList = new ArrayList<String>();
			for (int i = 0; i < apps.length; i++) {
				if (!apps[i].equals("0")&&!apps[i].equals("")) {
					appsList.add(apps[i]);
				}
			}
			
			for (int i = 0; i < appsList.size(); i++) {
				SettleVTN vtn = new SettleVTN();
				vtn.setAppName(appsList.get(i));
				vtn.setProductName(products[i]);
				vtn.setCount(Integer.parseInt(counts[i]));
				vtn.setPrice(Double.parseDouble(prices[i]));
				vtn.setConfigSupplier(configSupplierService.get(configSupplierId));
				vtn.setCreateTime(new Date());
				vtn.setCountPrice(Double.parseDouble(counts[i])*Double.parseDouble(prices[i]));
				
				settleVTNService.save(vtn);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addMessage(redirectAttributes, "保存VTN信息失败");
		}
	
		addMessage(redirectAttributes, "保存VTN信息成功");
		return "redirect:"+ Global.getAdminPath()+"/settlevtn/settleVTN/?repage";
	}
	
	@RequiresPermissions("settlevtn:settleVTN:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		settleVTNService.delete(id);
		addMessage(redirectAttributes, "删除VTN信息成功");
		return "redirect:"+Global.getAdminPath()+"/settlevtn/settleVTN/?repage";
	}

}
