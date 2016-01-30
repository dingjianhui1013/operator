/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.userenroll.web;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.userenroll.entity.KeyUnlock;
import com.itrus.ca.modules.userenroll.service.KeyUnlockService;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 解锁审批Controller
 * @author ZhangJingtao
 * @version 2014-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/userenroll/keyUnlock")
public class KeyUnlockController extends BaseController {

	@Autowired
	private KeyUnlockService keyUnlockService;
	
	@Autowired
	private WorkCertInfoService certInfoService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@ModelAttribute
	public KeyUnlock get(@RequestParam(required=false) Long id) {
		if (id != null){
			return keyUnlockService.get(id);
		}else{
			return new KeyUnlock();
		}
	}
	
	@RequiresPermissions("userenroll:keyUnlock:view")
	@RequestMapping(value = {"list", ""})
	public String list(KeyUnlock keyUnlock, HttpServletRequest request, HttpServletResponse response, Model model,String companyString , String attnName ) {
//		if (!user.isAdmin()){
//			keyUnlock.setCreateBy(user);
//		}
		
//		List<WorkDealInfo> workDealInfos =  workDealInfoService.findByCompanyAttn(companyString,attnName);
//		List<String> certSns = new ArrayList<String>();
//		
//		if (workDealInfos.size()>0) {
//			for (int i = 0; i < workDealInfos.size(); i++) {
//				certSns.add(workDealInfos.get(i).getCertSn());
//			}
//		}
        Page<KeyUnlock> page = keyUnlockService.find(new Page<KeyUnlock>(request, response), keyUnlock,companyString,attnName); 
        
        for (int i = 0; i < page.getList().size(); i++) {
        	 KeyUnlock unlock = keyUnlockService.get(page.getList().get(i).getId());
        	 WorkCertInfo certInfo = certInfoService.getByCertSn(unlock.getCertCn());
        	 List<WorkDealInfo> workDealInfoss =  workDealInfoService.findByCertSnKeySn(certInfo.getSerialnumber(),unlock.getKeySn());
        	 
        	 if (workDealInfoss.size()>0) {
        		 page.getList().get(i).setAttnName(workDealInfoss.get(0).getWorkUser().getContactName());
        		 page.getList().get(i).setCompanyName(workDealInfoss.get(0).getWorkCompany().getCompanyName());
			}
		}
        
        model.addAttribute("companyString", companyString);
        model.addAttribute("attnName", attnName);
        model.addAttribute("page", page);
		return "modules/userenroll/keyUnlockList";
	}

	@RequiresPermissions("userenroll:keyUnlock:view")
	@RequestMapping(value = "form")
	public String form(KeyUnlock keyUnlock, Model model) {
		model.addAttribute("keyUnlock", keyUnlock);
		return "modules/userenroll/keyUnlockForm";
	}

	@RequiresPermissions("userenroll:keyUnlock:edit")
	@RequestMapping(value = "save")
	public String save(KeyUnlock keyUnlock, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, keyUnlock)){
			return form(keyUnlock, model);
		}
		keyUnlockService.save(keyUnlock);
		return "redirect:"+Global.getAdminPath()+"/modules/userenroll/keyUnlock/?repage";
	}
	
	@RequiresPermissions("userenroll:keyUnlock:edit")
	@RequestMapping(value = "audit")
	@ResponseBody
	public String audit(Long id,@RequestParam(defaultValue="0")Integer agree,String msg, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("status", -1);
		try {
			if (msg!=null) {
				msg = URLDecoder.decode(msg, "UTF-8");
			}
			boolean result = keyUnlockService.audit(id, agree,msg,request);
			json.put("status", result? -1:1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toJSONString();
	}
	
	@RequestMapping(value = "getUnlockInfo")
	@ResponseBody
	public String getUnlockInfo(Long id) {
		JSONObject json = new JSONObject();
		json.put("status", -1);
		try {
			KeyUnlock unlock = keyUnlockService.get(id);
			WorkCertInfo certInfo = certInfoService.getByCertSn(unlock.getCertCn());
			if (certInfo!=null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				json.put("keysn", unlock.getKeySn());
				json.put("issuer", certInfo.getIssuerDn());
				json.put("subject", certInfo.getSubjectDn());
				json.put("notbefore", sdf.format(certInfo.getNotbefore()));
				json.put("notafter", sdf.format(certInfo.getNotafter()));
				json.put("sn", certInfo.getSerialnumber());
				json.put("status", 1);
				List<WorkDealInfo> list = workDealInfoService.findByCertSn(certInfo.getSerialnumber());
				if (list.size()>0) {
					json.put("appName", list.get(0).getConfigApp().getAppName());
					json.put("productName", ProductType.getProductTypeName(Integer.parseInt(list.get(0).getConfigProduct().getProductName())));
					json.put("dealInfoType", WorkDealInfoType.getDealInfoTypeName(list.get(0).getDealInfoType()));
				}
			}else {
				json.put("status", -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toJSONString();
	}

}
