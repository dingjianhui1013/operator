/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkType;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetailHistory;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentHistory;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailHistoryService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentHistoryService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;

/**
 * 代理商应用计费策略Controller
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/profile/configChargeAgent")
public class ConfigChargeAgentController extends BaseController {

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;
	
	@Autowired
	private ConfigProductService configProductService;
	
	@Autowired
	private ConfigAppService configAppService;
	
	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;
	
	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ConfigChargeAgentHistoryService configChargeAgentHistoryService;
	
	@Autowired
	private ConfigChargeAgentDetailHistoryService configChargeAgentDetailHistoryService;
	
	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public ConfigChargeAgent get(@RequestParam(required=false) Long id) {
		if (id != null){
			return configChargeAgentService.get(id);
		}else{
			return new ConfigChargeAgent();
		}
	}
	
	@RequiresPermissions("profile:configChargeAgent:view")
	@RequestMapping(value = {"list", ""})
	public String list(ConfigProduct configProduct, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ConfigProduct> page = configProductService.find(new Page<ConfigProduct>(request, response), configProduct);
        model.addAttribute("page", page);
        model.addAttribute("typeMap", ProductType.productTypeStrMap);
		return "modules/profile/configChargeAgentList";
	}

	/**
	 * 显示待绑定的计费策略模板
	 * @param configChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("profile:configChargeAgent:view")
	@RequestMapping(value = "bindList")
	public String bindList(ConfigChargeAgent configChargeAgent,long productId,
						   HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ConfigChargeAgent> page = configChargeAgentService.find(new Page<ConfigChargeAgent>(request, response), configChargeAgent);
		model.addAttribute("page", page);
		model.addAttribute("productId", productId);
		return "modules/profile/configChargeAgentTemplateBindList";
	}

	
	/**
	 * 显示绑定模版的数据
	 * @param configChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("profile:configChargeAgent:view")
	@RequestMapping(value = "bindListNew")
	public String bindListNew(ConfigChargeAgent configChargeAgent,Long productId,
						   HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ConfigChargeAgent> page = configChargeAgentService.find(new Page<ConfigChargeAgent>(request, response), configChargeAgent);
		for (int i = 0; i < page.getList().size(); i++) {
			ConfigChargeAgentBoundConfigProduct bound =	configChargeAgentBoundConfigProductService.findByAgentIdProductId(page.getList().get(i).getId(),productId);
			if (bound==null) {
				page.getList().get(i).setIsBind("1");
			}else{
				page.getList().get(i).setIsBind("2");
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("productId", productId);
		return "modules/profile/configChargeAgentTemplateBindNewList";
	}
	
	
	/**
	 * 产品和计费策略绑定
	 * @param configChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bindingNew")
	public String bindingNew(Long agentId,Long productId,
						   HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		
		ConfigProduct product = configProductService.get(productId);
	    ConfigChargeAgent agent = configChargeAgentService.get(agentId);
		
		ConfigChargeAgentBoundConfigProduct bound = new ConfigChargeAgentBoundConfigProduct();
		
		bound.setAgent(agent);
		bound.setProduct(product);
		configChargeAgentBoundConfigProductService.save(bound);
	
		addMessage(redirectAttributes, "绑定计费策略成功！");
		return "redirect:" + Global.getAdminPath()+"/profile/configChargeAgent/bindListNew?productId="+productId;
		
	}
	
	
	/**
	 * 取消绑定计费策略
	 * @param configChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "deleteBindingNew")
	public String deleteBindingNew(Long agentId,Long productId,
						   HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		try {
			configChargeAgentBoundConfigProductService.deleteByAgentId(agentId);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		addMessage(redirectAttributes, "取消绑定计费策略成功！");
		return "redirect:" + Global.getAdminPath()+"/profile/configChargeAgent/bindListNew?productId="+productId;
		
	}
	
	
	
	
	/**
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value="checkBound")
	@ResponseBody
	public String checkBound(Long proId , Long agentId){
		JSONObject jsonObject = new JSONObject();
		try {
			 List<ConfigChargeAgent>  agentList = configChargeAgentService.findById(agentId);
			 jsonObject.put("isBZ","1");
			 if (agentList.get(0).getTempStyle().equals("1")) {
				 List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService.findByProId(proId);
				 if (boundList.size()>0) {
					jsonObject.put("isBZ","0");
					jsonObject.put("msg","该产品已绑定类型为标准的计费策略，不能重复绑定类型为标准的计费策略！");
					return jsonObject.toJSONString();
				}
			}
			 
			if (agentList.get(0).getTempStyle().equals("1")) {
				 
				jsonObject.put("isNum","1");
				 
			}else{
				if (agentList.get(0).getSurplusNum().equals(0)) {
					jsonObject.put("isNum","0");
					jsonObject.put("msg","该计费策略剩余数量为0，绑定计费策略失败！");
				}else {
					jsonObject.put("isNum","1");
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return jsonObject.toJSONString();
	}
	
	
	
	
	
	
	
	
	/**
	 * 点击“计费策略模板” 跳转计费策略模板 界面
	 * @param configChargeAgent
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getChargeAgentList")
	public String chargeAgentList(ConfigChargeAgent configChargeAgent,HttpServletRequest request ,
										 HttpServletResponse response, Model model){
		Page<ConfigChargeAgent> page = configChargeAgentService.find(new Page<ConfigChargeAgent>(request,response),configChargeAgent);
		model.addAttribute("page",page);
		return "modules/profile/configChargeAgentTemplateList";
	}

	
	
	@RequestMapping(value="changeChargeAgentInfoList")
	public String changeChargeAgentInfoList(Long agentHisId,HttpServletRequest request ,
										 HttpServletResponse response, Model model){
	//	Page<ConfigChargeAgent> page = configChargeAgentService.find(new Page<ConfigChargeAgent>(request,response),configChargeAgent);
	
		Page<ConfigChargeAgentHistory> page = configChargeAgentHistoryService.findByAgentId(new Page<ConfigChargeAgentHistory>(request,response),agentHisId);
		
		for (int i = 0; i < page.getList().size(); i++) {
			
			List<ConfigChargeAgentDetailHistory> agentDetailHistory = configChargeAgentDetailHistoryService.findByAgentHistory(page.getList().get(i).getId());
			for (ConfigChargeAgentDetailHistory d : agentDetailHistory) {
				if (d.getWorkType().equals(WorkType.TYPE_OPEN)) {
					//model.addAttribute("khf", d.getMoney());
					
					page.getList().get(i).getMap().put("khf", d.getMoney().toString());
				}
				if (d.getWorkType().equals(WorkType.TYPE_CHANGE)) {
					//model.addAttribute("th", d.getMoney());
					page.getList().get(i).getMap().put("th", d.getMoney().toString());
				}
				if (d.getWorkType().equals(WorkType.TYPE_REISSUE)) {
					//model.addAttribute("bb0", d.getMoney());
					page.getList().get(i).getMap().put("bb0", d.getMoney().toString());
				}
				if (d.getWorkType().equals(WorkType.TYPE_UPDATE)) {
					//model.addAttribute("gx" + d.getChargeYear(), d.getMoney());
					page.getList().get(i).getMap().put("gx" + d.getChargeYear(), d.getMoney().toString());
				}
				if (d.getWorkType().equals(WorkType.TYPE_ADD)) {
					//model.addAttribute("xz" + d.getChargeYear(), d.getMoney());
					page.getList().get(i).getMap().put("xz" + d.getChargeYear(), d.getMoney().toString());
				}
				if (d.getWorkType().equals(WorkType.TYPE_TRUST)) {
					///model.addAttribute("trustDevice" + d.getChargeYear(), d.getMoney());
					page.getList().get(i).getMap().put("trustDevice" + d.getChargeYear(), d.getMoney().toString());
				}
				if (d.getWorkType().equals(WorkType.TYPE_DAMAGE)) {
					//model.addAttribute("bb1", d.getMoney());
					page.getList().get(i).getMap().put("bb1", d.getMoney().toString());
				}

			}
			
		}
		
		model.addAttribute("agentHisId",agentHisId);
		model.addAttribute("page",page);
		return "modules/profile/configChargeAgentHistoryList";
	}
	
	
	
	
	
	
	
	
	/**
	 * 新增和修改计费策略 跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("profile:configChargeAgent:view")
	@RequestMapping(value = "form")
	public String form(Long id, String view ,Model model) {
		if(id ==null){//新增
			model.addAttribute("configChargeAgent",new ConfigChargeAgent());
			return "modules/profile/configChargeAgentTemplateForm";
		}else {//查看 更新 展示

			//ConfigProduct configProduct = configProductService.get(id);
			//List<ConfigChargeAgent> list = configChargeAgentService.findByProductId(id);
			List<ConfigChargeAgent> list = configChargeAgentService.findById(id);
			if (list.size() > 0) {
				model.addAttribute("id",list.get(0).getId());
				model.addAttribute("configChargeAgent", list.get(0));
				List<ConfigChargeAgentDetail> list2 = configChargeAgentDetailService.findByConfigChargeAgent(list.get(0));
				for (ConfigChargeAgentDetail d : list2) {
					if (d.getWorkType().equals(WorkType.TYPE_OPEN)) {
						model.addAttribute("khf", d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_CHANGE)) {
						model.addAttribute("th", d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_REISSUE)) {
						model.addAttribute("bb0", d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_UPDATE)) {
						model.addAttribute("gx" + d.getChargeYear(), d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_ADD)) {
						model.addAttribute("xz" + d.getChargeYear(), d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_TRUST)) {
						model.addAttribute("trustDevice" + d.getChargeYear(), d.getMoney());
					}
					if (d.getWorkType().equals(WorkType.TYPE_DAMAGE)) {
						model.addAttribute("bb1", d.getMoney());
					}

				}
			}
			model.addAttribute("productTypeMap", ProductType.productTypeStrMap);
			//model.addAttribute("configProduct", configProduct);
			if("1".equals(view)){
				return "modules/profile/configChargeAgentTemplateForView";

			}else{
			return "modules/profile/configChargeAgentTemplateForm";
		}
	}
	}

	/**
	 * 绑定计费策略
	 * @param productId 产品id
	 * @param chargeAgentId 计费策略模板id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "bindSave")
	public String bindSave(Long productId, Long chargeAgentId,RedirectAttributes redirectAttributes)
	{
		ConfigProduct configProduct=configProductService.get(productId);
		configProduct.setChargeAgentId(chargeAgentId);
		configProductService.save(configProduct);
		logUtil.saveSysLog("计费策略绑定","绑定id为"+chargeAgentId+"计费策略模板成功",null);
		addMessage(redirectAttributes, "计费策略绑定成功");
		return "redirect:" + Global.getAdminPath()+"/profile/configChargeAgent/bindList?productId="+productId;
	}

	/**
	 * 解除计费策略的绑定
	 * @param productId
	 * @return
	 */
	@RequestMapping(value="unBindSave")
	public String unBindSave(Long productId ,RedirectAttributes redirectAttributes){
		ConfigProduct configProduct = configProductService.get(productId);
		Long chargeAgentId = configProduct.getChargeAgentId();
		configProduct.setChargeAgentId(null);
		configProductService.save(configProduct);
		logUtil.saveSysLog("计费策略解除绑定","产品id为"+productId+"解除绑定chargeAgentId为"+chargeAgentId+"的计费策略模板",null);
		addMessage(redirectAttributes,"计费策略解除绑定成功");
		return "redirect:" + Global.getAdminPath()+"/profile/configChargeAgent/?repage";
	}
	@RequiresPermissions("profile:configChargeAgent:edit")
	@RequestMapping(value = "save")
	public String save(Long productId,Long configChargeAgentDetailId,
			Long appId,Integer productLabel,Long configChargeAgentId,
			Boolean chargeMethodPos, Boolean chargeMethodMoney,
			Boolean chargeMethodBank, Boolean chargeMethodAlipay,
			Boolean chargeMethodGov, Boolean chargeMethodContract,
			Model model, RedirectAttributes redirectAttributes,Double[] addMoney,Double reissueMoney0,Double reissueMoney1,
			Double[] updateMoney,Double changeMoney,Double openAccountMoney,Double[] trustDeviceMoney,String tempName,String tempStyle,Integer configureNum) {
		
		Integer conNum =0;
		Integer surNum =0;
		Integer avaNum =0;
		if (configChargeAgentId!=null && configureNum!=null) {	
			ConfigChargeAgent agent = configChargeAgentService.get(configChargeAgentId);
			conNum = configureNum;
			surNum = agent.getSurplusNum() - agent.getConfigureNum() + configureNum;
			avaNum = agent.getAvailableNum();
		}
		
		ConfigChargeAgent configChargeAgent = new ConfigChargeAgent();
		configChargeAgentDetailService.deleteByAgentId(configChargeAgentId);
		configChargeAgent.setId(configChargeAgentId);
			configChargeAgent.setTempName(tempName);//模板名称
			configChargeAgent.setTempStyle(tempStyle);//模板类型
		//if("1".equals(tempStyle)){//通用
			if (chargeMethodPos!=null) {
				configChargeAgent.setChargeMethodPos(true);
			} else {
				configChargeAgent.setChargeMethodPos(false);
			}
			if (chargeMethodMoney!=null) {
				configChargeAgent.setChargeMethodMoney(true);
			} else {
				configChargeAgent.setChargeMethodMoney(false);
			}
			if (chargeMethodBank!=null) {
				configChargeAgent.setChargeMethodBank(true);
			} else {
				configChargeAgent.setChargeMethodBank(false);
			}
			if (chargeMethodAlipay!=null) {
				configChargeAgent.setChargeMethodAlipay(true);
			} else {
				configChargeAgent.setChargeMethodAlipay(false);
			}
			if (chargeMethodGov!=null) {
				configChargeAgent.setChargeMethodGov(true);
			} else {
				configChargeAgent.setChargeMethodGov(false);
			}
			if (chargeMethodContract!=null) {
				configChargeAgent.setChargeMethodContract(true);
			} else {
				configChargeAgent.setChargeMethodContract(false);
			}
	
			if(configChargeAgentId == null){
				if(configureNum!=null){
					configChargeAgent.setConfigureNum(configureNum);
					configChargeAgent.setSurplusNum(configureNum);
					configChargeAgent.setAvailableNum(0);
				}else{
					configChargeAgent.setConfigureNum(0);
					configChargeAgent.setSurplusNum(0);
					configChargeAgent.setAvailableNum(0);
				}
				
			}else{
					configChargeAgent.setConfigureNum(conNum);
					configChargeAgent.setSurplusNum(surNum);
					configChargeAgent.setAvailableNum(avaNum);
			}
			
			
			
//		}
//		else{
//			configChargeAgent.setChargeMethodPos(false);
//			configChargeAgent.setChargeMethodMoney(false);
//			configChargeAgent.setChargeMethodBank(false);
//
//		}

//		ConfigProduct product = new ConfigProduct();
//
//		product.setId(productId);
//		configChargeAgent.setConfigProduct(product);
//		ConfigApp configApp = new ConfigApp();
//		configApp.setId(appId);
//		ConfigCommercialAgent configCommercialAgent= configAgentAppRelationService.findAgentByApp(configApp);
//		configChargeAgent.setConfigCommercialAgent(configCommercialAgent);
//
//		configChargeAgent.setConfigApp(configApp);
		
		String detail = "";
		if(configChargeAgentId==null){
			//detail = "添加应用"+configApp.getAppName()+"下的计费策略成功";
		}else{
			//detail = "更新应用"+configApp.getAppName()+"下的计费策略成功";
		}
		
		
		
		configChargeAgentService.save(configChargeAgent);
		
		ConfigChargeAgentHistory agentHistory = new ConfigChargeAgentHistory();
		agentHistory.setSuperiorId(configChargeAgent.getId());
		
		agentHistory.setChargeMethodBank(configChargeAgent.getChargeMethodBank());
		agentHistory.setChargeMethodMoney(configChargeAgent.getChargeMethodMoney());
		agentHistory.setChargeMethodPos(configChargeAgent.getChargeMethodPos());
		agentHistory.setConfigureNum(configChargeAgent.getConfigureNum());
		agentHistory.setSurplusNum(configChargeAgent.getSurplusNum());
		agentHistory.setAvailableNum(configChargeAgent.getAvailableNum());
		agentHistory.setTempName(configChargeAgent.getTempName());
		agentHistory.setTempStyle(configChargeAgent.getTempStyle());
		
		configChargeAgentHistoryService.save(agentHistory);//保存主表的信息
		
		
		
		
		
		logUtil.saveSysLog("业务配置", detail, null);
		
		
		//保存详情
		int year = 1;
		for (Double double1 : updateMoney) {
			if (double1==null) {
				year++;
				continue;
			}
			ConfigChargeAgentDetail configChargeAgentDetail = new ConfigChargeAgentDetail();
			if(year==3){
				configChargeAgentDetail.setChargeYear(4);
			}else if(year == 4){
				configChargeAgentDetail.setChargeYear(5);
			}else{
				configChargeAgentDetail.setChargeYear(year);
			}

			configChargeAgentDetail.setConfigChargeAgent(configChargeAgent);
			configChargeAgentDetail.setMoney(double1);
			configChargeAgentDetail.setWorkType(WorkType.TYPE_UPDATE);
			//configChargeAgentDetail.setProductType(productLabel);
			configChargeAgentDetailService.save(configChargeAgentDetail);
			
			ConfigChargeAgentDetailHistory agentDetailHistory = new ConfigChargeAgentDetailHistory();
			agentDetailHistory.setAgent(agentHistory);
			agentDetailHistory.setChargeYear(configChargeAgentDetail.getChargeYear());
			agentDetailHistory.setMoney(configChargeAgentDetail.getMoney());
			agentDetailHistory.setProductType(configChargeAgentDetail.getProductType());
			agentDetailHistory.setWorkType(configChargeAgentDetail.getWorkType());
			configChargeAgentDetailHistoryService.save(agentDetailHistory);
			
			logUtil.saveSysLog("业务配置", "更新"+year+"年计费策略成功", null);
			year++;
		}
		year = 1;
		for (Double double1 : addMoney) {
			if (double1==null) {
				year++;
				continue;
			}
			ConfigChargeAgentDetail configChargeAgentDetail = new ConfigChargeAgentDetail();
			if(year==3){
				configChargeAgentDetail.setChargeYear(4);
			}else if(year == 4){
				configChargeAgentDetail.setChargeYear(5);
			}else{
				configChargeAgentDetail.setChargeYear(year);
			}
			configChargeAgentDetail.setConfigChargeAgent(configChargeAgent);
			configChargeAgentDetail.setMoney(double1);
			configChargeAgentDetail.setWorkType(WorkType.TYPE_ADD);
			//configChargeAgentDetail.setProductType(productLabel);
			configChargeAgentDetailService.save(configChargeAgentDetail);
			
			ConfigChargeAgentDetailHistory agentDetailHistory = new ConfigChargeAgentDetailHistory();
			agentDetailHistory.setAgent(agentHistory);
			agentDetailHistory.setChargeYear(configChargeAgentDetail.getChargeYear());
			agentDetailHistory.setMoney(configChargeAgentDetail.getMoney());
			agentDetailHistory.setProductType(configChargeAgentDetail.getProductType());
			agentDetailHistory.setWorkType(configChargeAgentDetail.getWorkType());
			configChargeAgentDetailHistoryService.save(agentDetailHistory);
			
			
			logUtil.saveSysLog("业务配置", "添加"+year+"年计费策略成功", null);
			year++;
		}
		
		if (reissueMoney0!=null) {
			ConfigChargeAgentDetail configChargeAgentDetail = new ConfigChargeAgentDetail();
			configChargeAgentDetail.setChargeYear(1);
			configChargeAgentDetail.setConfigChargeAgent(configChargeAgent);
			configChargeAgentDetail.setMoney(reissueMoney0);
			configChargeAgentDetail.setWorkType(WorkType.TYPE_REISSUE);
			//configChargeAgentDetail.setProductType(productLabel);
			configChargeAgentDetailService.save(configChargeAgentDetail);
			ConfigChargeAgentDetailHistory agentDetailHistory = new ConfigChargeAgentDetailHistory();
			agentDetailHistory.setAgent(agentHistory);
			agentDetailHistory.setChargeYear(configChargeAgentDetail.getChargeYear());
			agentDetailHistory.setMoney(configChargeAgentDetail.getMoney());
			agentDetailHistory.setProductType(configChargeAgentDetail.getProductType());
			agentDetailHistory.setWorkType(configChargeAgentDetail.getWorkType());
			configChargeAgentDetailHistoryService.save(agentDetailHistory);
		}
		
		if (reissueMoney1!=null) {
			ConfigChargeAgentDetail configChargeAgentDetail = new ConfigChargeAgentDetail();
			configChargeAgentDetail.setChargeYear(1);
			configChargeAgentDetail.setConfigChargeAgent(configChargeAgent);
			configChargeAgentDetail.setMoney(reissueMoney1);
			configChargeAgentDetail.setWorkType(WorkType.TYPE_DAMAGE);
			//configChargeAgentDetail.setProductType(productLabel);
			configChargeAgentDetailService.save(configChargeAgentDetail);
			ConfigChargeAgentDetailHistory agentDetailHistory = new ConfigChargeAgentDetailHistory();
			agentDetailHistory.setAgent(agentHistory);
			agentDetailHistory.setChargeYear(configChargeAgentDetail.getChargeYear());
			agentDetailHistory.setMoney(configChargeAgentDetail.getMoney());
			agentDetailHistory.setProductType(configChargeAgentDetail.getProductType());
			agentDetailHistory.setWorkType(configChargeAgentDetail.getWorkType());
			configChargeAgentDetailHistoryService.save(agentDetailHistory);
		}
		
		year = 1;
		if (changeMoney!=null) {
			ConfigChargeAgentDetail configChargeAgentDetail = new ConfigChargeAgentDetail();
			configChargeAgentDetail.setChargeYear(0);
			configChargeAgentDetail.setConfigChargeAgent(configChargeAgent);
			configChargeAgentDetail.setMoney(changeMoney);
			configChargeAgentDetail.setWorkType(WorkType.TYPE_CHANGE);
			//configChargeAgentDetail.setProductType(productLabel);
			configChargeAgentDetailService.save(configChargeAgentDetail);
			ConfigChargeAgentDetailHistory agentDetailHistory = new ConfigChargeAgentDetailHistory();
			agentDetailHistory.setAgent(agentHistory);
			agentDetailHistory.setChargeYear(configChargeAgentDetail.getChargeYear());
			agentDetailHistory.setMoney(configChargeAgentDetail.getMoney());
			agentDetailHistory.setProductType(configChargeAgentDetail.getProductType());
			agentDetailHistory.setWorkType(configChargeAgentDetail.getWorkType());
			configChargeAgentDetailHistoryService.save(agentDetailHistory);
			logUtil.saveSysLog("业务配置", "保存计费策略变更成功", null);
		}
		
		if (openAccountMoney!=null) {
			ConfigChargeAgentDetail configChargeAgentDetail = new ConfigChargeAgentDetail();
			configChargeAgentDetail.setChargeYear(0);
			configChargeAgentDetail.setConfigChargeAgent(configChargeAgent);
			configChargeAgentDetail.setMoney(openAccountMoney);
			configChargeAgentDetail.setWorkType(WorkType.TYPE_OPEN);
			//configChargeAgentDetail.setProductType(productLabel);
			configChargeAgentDetailService.save(configChargeAgentDetail);
			ConfigChargeAgentDetailHistory agentDetailHistory = new ConfigChargeAgentDetailHistory();
			agentDetailHistory.setAgent(agentHistory);
			agentDetailHistory.setChargeYear(configChargeAgentDetail.getChargeYear());
			agentDetailHistory.setMoney(configChargeAgentDetail.getMoney());
			agentDetailHistory.setProductType(configChargeAgentDetail.getProductType());
			agentDetailHistory.setWorkType(configChargeAgentDetail.getWorkType());
			configChargeAgentDetailHistoryService.save(agentDetailHistory);
			logUtil.saveSysLog("业务配置", "保存计费策略开户费成功", null);
		}
		
		int trustYear = 0;
		for (Double trustDouble : trustDeviceMoney) {
			if (trustDouble!=null) {
				ConfigChargeAgentDetail configChargeAgentDetail = new ConfigChargeAgentDetail();
				configChargeAgentDetail.setChargeYear(trustYear);
				configChargeAgentDetail.setConfigChargeAgent(configChargeAgent);
				configChargeAgentDetail.setMoney(trustDouble);
				configChargeAgentDetail.setWorkType(WorkType.TYPE_TRUST);
				//configChargeAgentDetail.setProductType(productLabel);
				configChargeAgentDetailService.save(configChargeAgentDetail);
				ConfigChargeAgentDetailHistory agentDetailHistory = new ConfigChargeAgentDetailHistory();
				agentDetailHistory.setAgent(agentHistory);
				agentDetailHistory.setChargeYear(configChargeAgentDetail.getChargeYear());
				agentDetailHistory.setMoney(configChargeAgentDetail.getMoney());
				agentDetailHistory.setProductType(configChargeAgentDetail.getProductType());
				agentDetailHistory.setWorkType(configChargeAgentDetail.getWorkType());
				configChargeAgentDetailHistoryService.save(agentDetailHistory);
				trustYear++;
			}else {
				trustYear++;
			}
		}
		addMessage(redirectAttributes, "保存计费策略模板成功");
		return "redirect:"+Global.getAdminPath()+"/profile/configChargeAgent/getChargeAgentList";
	}
	
	@RequiresPermissions("profile:configChargeAgent:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		configChargeAgentService.deleteById(id);
		logUtil.saveSysLog("业务配置", "删除id为"+id+"的计代理商应用计费策略成功", null);
		addMessage(redirectAttributes, "删除代理商应用计费策略成功");
		return "redirect:"+Global.getAdminPath()+"/profile/configChargeAgent/getChargeAgentList";
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value="checkUsed")
	@ResponseBody
	public String checkUsed(Long id){
		JSONObject jsonObject = new JSONObject();
		//Boolean checkUsed = configChargeAgentService.checkUsed(id);
		 List<WorkDealInfo> dealList = workDealInfoService.findByAgentId(id);
		if(dealList.size()>0){
			jsonObject.put("status","0");
			jsonObject.put("msg","该计费策略已经被使用，请解除绑定后再删除。");

		}else{
			jsonObject.put("status","1");
		}
		return jsonObject.toJSONString();
	}

	/**
	 *  计费模板 添加 更新前检测
	 * @param tempId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="checkChargeAgent")
	@ResponseBody
	public String checkChargeAgent(Long tempId ,HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject();
		try{
			String tempName = request.getParameter("tempName");
			List<ConfigChargeAgent> list = configChargeAgentService.findByName(tempName);
			if(list.size() != 0 ){
				if(tempId == null ){
					jsonObject.put("status","0");
					jsonObject.put("msg","计费策略模板名称已存在,添加失败");
					return jsonObject.toString();
				}else{
					ConfigChargeAgent configChargeAgent = list.get(0);
					if(tempId.longValue() != configChargeAgent.getId().longValue()){//更新重名
						jsonObject.put("status","0");
						jsonObject.put("msg","计费策略模板名称已存在,更新失败");
						return jsonObject.toString();
					}
					if(configChargeAgentService.checkUsed(tempId)){
						jsonObject.put("status","1");
						jsonObject.put("msg","该计费策略模板已被使用，是否继续进行修改？");
						return jsonObject.toString();
					}
					jsonObject.put("status","2");
				}
			}else{
				jsonObject.put("status","2");
				return jsonObject.toString();
			}

		}catch(Exception e){

			e.printStackTrace();
		}

		return jsonObject.toString();
	}

}
