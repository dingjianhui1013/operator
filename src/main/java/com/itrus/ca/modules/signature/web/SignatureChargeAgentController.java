/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.signature.entity.SignatureAgentBoundConfigApp;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgent;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgentDetail;
import com.itrus.ca.modules.signature.entity.SignatureInfo;
import com.itrus.ca.modules.signature.service.SignatureAgentBoundConfigAppService;
import com.itrus.ca.modules.signature.service.SignatureConfigChargeAgentDetailService;
import com.itrus.ca.modules.signature.service.SignatureConfigChargeAgentService;
import com.itrus.ca.modules.signature.service.SignatureInfoService;
import com.itrus.ca.modules.work.entity.WorkDealInfo;


/**
 * 签章计费策略Controller
 * @author CYC
 * @version 2016-02-22
 */
@Controller
@RequestMapping(value = "${adminPath}/signature/signatureChargeAgent")
public class SignatureChargeAgentController extends BaseController {

	@Autowired
	private SignatureConfigChargeAgentService signatureConfigChargeAgentService;
	
	@Autowired
	private ConfigAppService configAppService;
	
	@Autowired
	private SignatureConfigChargeAgentDetailService signatureConfigChargeAgentDetailService; 
	
	@Autowired
	private SignatureAgentBoundConfigAppService signatureAgentBoundConfigAppService;
	
	@Autowired
	private SignatureInfoService signatureInfoService;
	
	
	private LogUtil logUtil = new LogUtil();

	@RequiresPermissions("signature:signatureChargeAgent:view")
	@RequestMapping(value = "form")
	public String form(Long id,String view ,Model model)
	{
		if(id==null)
		{
			return "modules/signature/signatureChargeAgentForm";
		}else
		{
			
			List<SignatureConfigChargeAgent> list = signatureConfigChargeAgentService.findById(id);
			if (list.size() > 0) {
				model.addAttribute("id",list.get(0).getId());
				model.addAttribute("signatureConfigChargeAgent", list.get(0));
				List<SignatureConfigChargeAgentDetail> list2 = signatureConfigChargeAgentDetailService.findBySConfigChargeAgent(list.get(0));
				for (SignatureConfigChargeAgentDetail d : list2) {
					if (d.getWorkType().equals(WorkType.TYPE_CHANGE)) {
						model.addAttribute("th", d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_UPDATE)) {
						model.addAttribute("gx" + d.getChargeYear(), d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_ADD)) {
						model.addAttribute("xz" + d.getChargeYear(), d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_RENEW)) {
						model.addAttribute("xf" + d.getChargeYear(), d.getMoney());
					}

				}
			}
			model.addAttribute("productTypeMap", ProductType.productTypeStrMap);
			model.addAttribute("Id", id);
			if("1".equals(view)){
				return "modules/signature/signatureChargeAgentTemplateForView";
			}else{
				
				return "modules/signature/signatureChargeAgentForms";
			}
		}
	}

	
	@RequiresPermissions("signature:signatureChargeAgent:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigApp configApp, HttpServletRequest request, HttpServletResponse response, Model model) {
	    Page<ConfigApp> page = configAppService.find(new Page<ConfigApp>(request, response), configApp, new ArrayList<Long>(), null);
	    for (int i = 0; i < page.getList().size(); i++) {
	    	  List<SignatureAgentBoundConfigApp> bounds = signatureAgentBoundConfigAppService.findByApp(page.getList().get(i).getId());
	    	  page.getList().get(i).setBounds(bounds);
	    }
	    model.addAttribute("page", page);
		return "modules/profile/signatureChargeAgentList";
	}
	
	
	
	
	/**
	 * 显示绑定模版的数据
	 * @param configChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("signature:signatureChargeAgent:view")
	@RequestMapping(value = "bindListNew")
	public String bindListNew(SignatureConfigChargeAgent signatureConfigChargeAgent,Long appId,
						   HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SignatureConfigChargeAgent> page = signatureConfigChargeAgentService.find(new Page<SignatureConfigChargeAgent>(request, response), signatureConfigChargeAgent);
		for (int i = 0; i < page.getList().size(); i++) {
			List<SignatureAgentBoundConfigApp> bounds = signatureAgentBoundConfigAppService.findByAppAgent(appId, page.getList().get(i).getId());
			if (bounds.size()<1) {
				page.getList().get(i).setIsBind("1");
			}else{
				page.getList().get(i).setIsBind("2");
				page.getList().get(i).setBoundId(bounds.get(0).getId());
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("appId", appId);
		return "modules/signature/signatureConfigChargeAgentTemplateBindNewList";
	}
	
	
	
	
	/**
	 * 点击"签章计费策略模板" 跳转到签章计费策略模板 界面
	 * @param signatureConfigChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("signature:signatureChargeAgent:view")
	@RequestMapping(value="getSignatureChargeAgentList")
	public String signatureChargeAgentList(SignatureConfigChargeAgent signatureConfigChargeAgent,HttpServletRequest request ,
										 HttpServletResponse response, Model model){
		Page<SignatureConfigChargeAgent> page = signatureConfigChargeAgentService.find(new Page<SignatureConfigChargeAgent>(request,response),signatureConfigChargeAgent);
		/*List<SignatureConfigChargeAgent> agents =  signatureConfigChargeAgentService.findByNullUpdateNum();
		model.addAttribute("isHaveOld", agents.size());*/
		model.addAttribute("page",page);
		return "modules/signature/signatureConfigChargeAgentTemplateList";
	}

	
	
	
	@RequiresPermissions("signature:signatureChargeAgent:edit")
	@RequestMapping(value = "save")
	public String save(
			Long signatureConfigChargeAgentId,
			Boolean chargeMethodPos, Boolean chargeMethodMoney,
			Model model, RedirectAttributes redirectAttributes,Double[] addMoney,
			Double[] updateMoney,Double changeMoney,Double[] renewMoney,String tempName
			) {
	
		SignatureConfigChargeAgent signatureConfigChargeAgent = new SignatureConfigChargeAgent();
		signatureConfigChargeAgentDetailService.deleteByAgentId(signatureConfigChargeAgentId);
		signatureConfigChargeAgent.setId(signatureConfigChargeAgentId);
		signatureConfigChargeAgent.setTempName(tempName);//模板名称
		
			if (chargeMethodPos!=null) {
				signatureConfigChargeAgent.setChargeMethodPos(true);
			} else {
				signatureConfigChargeAgent.setChargeMethodPos(false);
			}
			if (chargeMethodMoney!=null) {
				signatureConfigChargeAgent.setChargeMethodMoney(true);
			} else {
				signatureConfigChargeAgent.setChargeMethodMoney(false);
			}

			String detail = "";
			if(signatureConfigChargeAgentId==null){
				detail = "添加应用"+signatureConfigChargeAgent.getTempName()+"下的计费策略成功";
			}else{
				detail = "更新应用"+signatureConfigChargeAgent.getTempName()+"下的计费策略成功";
			}
		signatureConfigChargeAgentService.save(signatureConfigChargeAgent);
		
		//保存详情
		int year = 1;
		for (Double double1 : updateMoney) {
			if (double1==null) {
				year++;
				continue;
			}
			SignatureConfigChargeAgentDetail sConfigChargeAgentDetail = new SignatureConfigChargeAgentDetail();
			sConfigChargeAgentDetail.setChargeYear(year);
			sConfigChargeAgentDetail.setSignatureConfigChargeAgent(signatureConfigChargeAgent);
			sConfigChargeAgentDetail.setMoney(double1);
			sConfigChargeAgentDetail.setWorkType(WorkType.TYPE_UPDATE);
			signatureConfigChargeAgentDetailService.save(sConfigChargeAgentDetail);
			logUtil.saveSysLog("业务配置 签章计费策略", "更新"+year+"年计费策略成功", null);
			year++;
		}
		year = 1;
		for (Double double1 : addMoney) {
			if (double1==null) {
				year++;
				continue;
			}
			SignatureConfigChargeAgentDetail sConfigChargeAgentDetail = new SignatureConfigChargeAgentDetail();
			sConfigChargeAgentDetail.setChargeYear(year);
			sConfigChargeAgentDetail.setSignatureConfigChargeAgent(signatureConfigChargeAgent);
			sConfigChargeAgentDetail.setMoney(double1);
			sConfigChargeAgentDetail.setWorkType(WorkType.TYPE_ADD);
			signatureConfigChargeAgentDetailService.save(sConfigChargeAgentDetail);
			logUtil.saveSysLog("业务配置 签章计费策略", "添加"+year+"年计费策略成功", null);
			year++;
		}
		

		year = 1;
		if (changeMoney!=null) {
			SignatureConfigChargeAgentDetail sConfigChargeAgentDetail = new SignatureConfigChargeAgentDetail();
			sConfigChargeAgentDetail.setChargeYear(0);
			sConfigChargeAgentDetail.setSignatureConfigChargeAgent(signatureConfigChargeAgent);
			sConfigChargeAgentDetail.setMoney(changeMoney);
			sConfigChargeAgentDetail.setWorkType(WorkType.TYPE_CHANGE);
			signatureConfigChargeAgentDetailService.save(sConfigChargeAgentDetail);
			logUtil.saveSysLog("业务配置", "保存计费策略变更成功", null);
		}
		
		
		addMessage(redirectAttributes, "保存签章计费策略模板成功");
		return "redirect:"+Global.getAdminPath()+"/signature/signatureChargeAgent/getSignatureChargeAgentList";
	}

	
	
	@RequestMapping(value = "boundAgentApp")
	@ResponseBody
	public String boundAgentApp(Long appId, Long agentId) throws JSONException {
		JSONObject json = new JSONObject();

		try {
			SignatureAgentBoundConfigApp signatureAgentBoundConfigApp = new SignatureAgentBoundConfigApp();
			ConfigApp app =  configAppService.get(appId);
			SignatureConfigChargeAgent signatureConfigChargeAgent = signatureConfigChargeAgentService.get(agentId);
			signatureAgentBoundConfigApp.setApp(app);
			signatureAgentBoundConfigApp.setAgent(signatureConfigChargeAgent);
			signatureAgentBoundConfigAppService.save(signatureAgentBoundConfigApp);
			json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
	}
	
	@RequestMapping(value = "unBoundAgentApp")
	@ResponseBody
	public String unBoundAgentApp(Long boundId) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", "-1");
			
			SignatureAgentBoundConfigApp agentBoundApp = signatureAgentBoundConfigAppService.get(boundId); 
			
			List<SignatureInfo> signatureInfos =  signatureInfoService.findByAgentIdProductId(agentBoundApp.getApp(),agentBoundApp.getAgent());
			if(signatureInfos.size()>0){
				return json.toString();
			}
			signatureAgentBoundConfigAppService.delete(boundId);
			json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
	}
	
	
	@RequestMapping(value = "checkUsed")
	@ResponseBody
	public String checkUsed(Long agentId) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			List<SignatureAgentBoundConfigApp> bounds = signatureAgentBoundConfigAppService.findByAgent(agentId);
			if (bounds.size()>0) {
				json.put("status", "1");
			}else{
				json.put("status", "2");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
	}
	
	
	@RequestMapping(value = "deleteByAgentId")
	public String deleteByAgentId(Long agentId,RedirectAttributes redirectAttributes,Model model) {
		signatureConfigChargeAgentService.unDeleteById(agentId);
		addMessage(redirectAttributes, "删除签章计费策略模板成功");
		return "redirect:"+Global.getAdminPath()+"/signature/signatureChargeAgent/getSignatureChargeAgentList";
	}

	@RequestMapping(value = "checkName")
	@ResponseBody
	public String checkName(String tempName,Long Id) throws JSONException
	{
		JSONObject json = new JSONObject();
		List<SignatureConfigChargeAgent> list = signatureConfigChargeAgentService.checkName(tempName,Id);
		try {
			if(list.size()>0)
			{
				json.put("status", 1);
			}else {
				json.put("status", 0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
	}
	
	
	
	
	
	

}
