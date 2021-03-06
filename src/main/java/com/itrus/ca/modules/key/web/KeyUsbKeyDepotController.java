/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.Role;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.service.FinancePaymentInfoService;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyManufacturerService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;

/**
 * key入库信息Controller
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/key/keyUsbKeyDepot")
public class KeyUsbKeyDepotController extends BaseController {

	static Log log = LogFactory.getLog(KeyUsbKeyController.class);
	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	
	@Autowired
	private FinancePaymentInfoService financePaymentInfoService;
	
	@Autowired
	private KeyUsbKeyDepotService keyUsbKeyDepotService;
	
	@Autowired
	private KeyManufacturerService keyManufacturerService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	@Autowired
	private KeyUsbKeyService keyUsbKeyService;

	@Autowired
	private KeyUsbKeyInvoiceService keyUsbKeyInvoiceService;
	
	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;
	
	@Autowired
	private ConfigSupplierService configSupplierService;
	
	
	@ModelAttribute
	public KeyUsbKeyDepot get(@RequestParam(required=false) Long id) {
		if (id != null){
			return keyUsbKeyDepotService.get(id);
		}else{
			return new KeyUsbKeyDepot();
		}
	}
	
	@RequiresPermissions("key:keyUsbKeyDepot:view")
	@RequestMapping(value = {"list", ""})
	public String list(KeyUsbKeyDepot keyUsbKeyDepot, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			keyUsbKeyDepot.setCreateBy(user);
		}
		
		
		//只有系统管理员才具有分配库房的功能
		model.addAttribute("isSysadmin", false);
		
		List<Role> roleList = user.getRoleList();
		for(Role role:roleList){
			if(role.getId() == 1){
				
				model.addAttribute("isSysadmin", true);
				
				break;
			}
		}
		
		
		
		List<Office> offices =  officeService.getOfficeByType(user, 2);
		
		for (int i = 0; i < offices.size(); i++) {
			log.debug(offices.get(i).getId()+"-");
		}
		
		
        Page<KeyUsbKeyDepot> page = keyUsbKeyDepotService.findByOffices(new Page<KeyUsbKeyDepot>(request, response), keyUsbKeyDepot,offices); 
    
        for (int i = 0; i <  page.getList().size(); i++) {
    	   Long depotId =   page.getList().get(i).getId();//获取库存的id
    	   List<KeyDepotGeneralStatistics> depotGeneStatistics =   keyDepotGeneralStatisticsService.findByDepotId(depotId);
    	   
    	   int inCount = 0;
    	   for (int j = 0; j < depotGeneStatistics.size(); j++) {
    		   inCount += depotGeneStatistics.get(j).getInCount();//计算库存总量
    	   }
    	 page.getList().get(i).setInCount(inCount);
    	   if (depotGeneStatistics.size()>0) {
    		   page.getList().get(i).setKeyDepotGeneralStatisticsList(depotGeneStatistics);
		}
       }
        
        model.addAttribute("page", page);
		return "modules/key/keyUsbKeyDepotList";
	}
	
	
	@RequiresPermissions("key:keyUsbKeyDepot:view")
	@RequestMapping(value = "listCount")
	public String listCount(@RequestParam(required = false)KeyUsbKeyDepot keyUsbKeyDepot, HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "area", required = false) Long area,
			@RequestParam(value = "office", required = false) Long office,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "depotName", required = false) String depotName,
			@RequestParam(value = "keyId", required = false) Long keyId,
			@RequestParam(value = "supplierId", required = false) Long supplierId,
			@RequestParam(value = "que", required = false) Long query,
			Model model) {
		

		  List<KeyUsbKeyDepot> list = keyUsbKeyDepotService.findAll();
		  model.addAttribute("KeyUsbKeyDepot",list);
		  int ruKuZongliang = 0;//入库总量汇总
		  int chuKuZongling = 0;//入库总量汇总
		User user = UserUtils.getUser();
		if (endTime!=null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
		}
		if (!user.isAdmin()){
			if (keyUsbKeyDepot==null) {
				keyUsbKeyDepot = new KeyUsbKeyDepot();
			}
			keyUsbKeyDepot.setCreateBy(user);
		}
		if(null==query){
			
//		if (startTime==null&&endTime==null) {
			 
			
			List<Office> offsList =  officeService.getOfficeByType(user, 1);
    		
			List<Office> offices =  officeService.getOfficeByType(user, 2);
			
			
			if (offices.size()==1) {
				model.addAttribute("offsListSize",1);
				model.addAttribute("offices",offices);
			}
			List<ConfigSupplier> suppliers = configSupplierService.findByKey();
			
		    model.addAttribute("suppliers", suppliers);
			model.addAttribute("offsList",offsList);
//			model.addAttribute("startTime", startTime);
//			model.addAttribute("endTime", endTime);
//			model.addAttribute("ruKuZongliang", ruKuZongliang);
//			model.addAttribute("chuKuZongling",chuKuZongling);
			return "modules/key/keyUsbKeyDepotCountList";
		}else{
			List<Office> offices =  officeService.getOfficeByType(user, 2);
			List<Long> officeIds = new  ArrayList();
			for(Office o:offices){
				officeIds.add(o.getId());
			}
			if (startTime==null&&endTime==null){
				//默认今天数据
				Calendar now = Calendar.getInstance();
				now.set(Calendar.HOUR_OF_DAY, 0);
				now.set(Calendar.MINUTE, 0);
				now.set(Calendar.SECOND, 0);
				startTime = now.getTime();
				now.set(Calendar.HOUR_OF_DAY, 23);
				now.set(Calendar.MINUTE, 59);
				now.set(Calendar.SECOND, 59);
				endTime = now.getTime();
			}
			//汇总
			List<KeyUsbKeyDepot> cangku = keyUsbKeyDepotService.findAllQuery(area,office,depotName,officeIds);
//			ruKuZongliang = keyUsbKeyService.ruKuZongliang(startTime, endTime,cangku,supplierId,keyId);//入库
//			chuKuZongling = keyUsbKeyInvoiceService.chuKuZongliang(startTime, endTime);//出库
			for (int i = 0; i <  cangku.size(); i++) {
				  int in =0;
				  int out = 0;
		    	   Long depotId =   cangku.get(i).getId();//获取库存的id
		    		List<KeyUsbKey> kuksKeys = keyUsbKeyService.findByDepotIdEndTime(depotId,endTime,supplierId,keyId);// 获取该仓库下的入库信息
					List<Long> geneIds = Lists.newArrayList();
					for (int j = 0; j < kuksKeys.size(); j++) {
						geneIds.add(kuksKeys.get(j).getKeyGeneralInfo().getId());   //获取endtime时间下所有入库的key类型信息
					}
					List<KeyDepotGeneralStatistics> statisticList = Lists
							.newArrayList();
					if (geneIds.size() > 0) {
						List<KeyGeneralInfo> geneList = keyGeneralInfoService
								.findByGeneIds(geneIds);// 获取该仓库下的类型
						for (int j = 0; j < geneList.size(); j++) {
							KeyDepotGeneralStatistics sta = new KeyDepotGeneralStatistics();
							sta.setKeyGeneralInfo(geneList.get(j));
							sta.setKeyUsbKeyDepot(cangku.get(i));
							statisticList.add(sta);
						}
						//兼容统计时间只输入一项
						List<KeyUsbKey> kukBefores = new ArrayList();
						List<KeyUsbKeyInvoice> kukiBefores = new ArrayList();
						if(startTime!=null){
							kukBefores = keyUsbKeyService.findByDepotIdStartTime(depotId,startTime,supplierId,keyId);//获取开始时间之前的入库信息
							kukiBefores = keyUsbKeyInvoiceService.findByDepotId(depotId,startTime,supplierId,keyId);//获取开始之前的出库信息
						}
						List<KeyUsbKey> kukBetween = keyUsbKeyService.findByDepotIdStartTimeEndTime(depotId,startTime,endTime,supplierId,keyId);//获取开始时间到结束时间之间的入库信息
						List<KeyUsbKeyInvoice> kukiBetween = keyUsbKeyInvoiceService.findByDepotId(depotId,startTime,endTime,supplierId,keyId);//获取开始之前的出库信息
						for (int j = 0; j < statisticList.size(); j++) {
							int inCount = 0;
							int outCount = 0;
							for (int k = 0; k < kukBetween.size(); k++) {
								if (statisticList.get(j).getKeyGeneralInfo().getId().equals(kukBetween.get(k).getKeyGeneralInfo().getId())) {
									inCount+=kukBetween.get(k).getCount();
								}
							}
							for (int k = 0; k < kukiBetween.size(); k++) {
								if (statisticList.get(j).getKeyGeneralInfo().getId().equals(kukiBetween.get(k).getKeyGeneralInfo().getId())) {
									outCount += kukiBetween.get(k).getDeliveryNum();
								}
							}
							in += inCount;
							out += outCount;
							statisticList.get(j).setInCount(inCount);
							statisticList.get(j).setOutCount(outCount);
						}
			    	   if (statisticList.size()>0) {
			    		   cangku.get(i).setKeyDepotGeneralStatisticsList(statisticList);
			    	   }
					}
					ruKuZongliang += in;
					chuKuZongling += out;
			  }
			
			  Page<KeyUsbKeyDepot> page = keyUsbKeyDepotService.findAllPage(new Page<KeyUsbKeyDepot>(request, response),area,office,depotName,officeIds);
			  for (int i = 0; i <  page.getList().size(); i++) {
				  int total =0;
				  int in =0;
				  int out = 0;
				  int totalEnd = 0;
		    	   Long depotId =   page.getList().get(i).getId();//获取库存的id
		    		List<KeyUsbKey> kuksKeys = keyUsbKeyService.findByDepotIdEndTime(depotId,endTime,supplierId,keyId);// 获取该仓库下的入库信息
					List<Long> geneIds = Lists.newArrayList();
					for (int j = 0; j < kuksKeys.size(); j++) {
						geneIds.add(kuksKeys.get(j).getKeyGeneralInfo().getId());   //获取endtime时间下所有入库的key类型信息
					}
					List<KeyDepotGeneralStatistics> statisticList = Lists
							.newArrayList();
					if (geneIds.size() > 0) {
						List<KeyGeneralInfo> geneList = keyGeneralInfoService
								.findByGeneIds(geneIds);// 获取该仓库下的类型
						for (int j = 0; j < geneList.size(); j++) {
							KeyDepotGeneralStatistics sta = new KeyDepotGeneralStatistics();
							sta.setKeyGeneralInfo(geneList.get(j));
							sta.setKeyUsbKeyDepot(page.getList().get(i));
							statisticList.add(sta);
						}
						//兼容统计时间只输入一项
						List<KeyUsbKey> kukBefores = new ArrayList();
						List<KeyUsbKeyInvoice> kukiBefores = new ArrayList();
						if(startTime!=null){
							kukBefores = keyUsbKeyService.findByDepotIdStartTime(depotId,startTime,supplierId,keyId);//获取开始时间之前的入库信息
							kukiBefores = keyUsbKeyInvoiceService.findByDepotId(depotId,startTime,supplierId,keyId);//获取开始之前的出库信息
						}
						List<KeyUsbKey> kukBetween = keyUsbKeyService.findByDepotIdStartTimeEndTime(depotId,startTime,endTime,supplierId,keyId);//获取开始时间到结束时间之间的入库信息
						List<KeyUsbKeyInvoice> kukiBetween = keyUsbKeyInvoiceService.findByDepotId(depotId,startTime,endTime,supplierId,keyId);//获取开始之前的出库信息
						for (int j = 0; j < statisticList.size(); j++) {
							int totalCount= 0;
							int inCount = 0;
							int outCount = 0;
							int totalEndCount = 0;
							for (int k = 0; k < kukBefores.size(); k++) {
								if (statisticList.get(j).getKeyGeneralInfo().getId().equals(kukBefores.get(k).getKeyGeneralInfo().getId())) {
									totalCount += kukBefores.get(k).getCount();
								}
							}
							for (int k = 0; k < kukiBefores.size(); k++) {
								if (statisticList.get(j).getKeyGeneralInfo().getId().equals(kukiBefores.get(k).getKeyGeneralInfo().getId())) {
									totalCount -= kukiBefores.get(k).getDeliveryNum();
								}
							}
							for (int k = 0; k < kukBetween.size(); k++) {
								if (statisticList.get(j).getKeyGeneralInfo().getId().equals(kukBetween.get(k).getKeyGeneralInfo().getId())) {
									inCount+=kukBetween.get(k).getCount();
								}
							}
							for (int k = 0; k < kukiBetween.size(); k++) {
								if (statisticList.get(j).getKeyGeneralInfo().getId().equals(kukiBetween.get(k).getKeyGeneralInfo().getId())) {
									outCount += kukiBetween.get(k).getDeliveryNum();
								}
							}
							totalEndCount = totalCount+inCount-outCount;
							total += totalCount;
							in += inCount;
							out += outCount;
							totalEnd += totalEndCount;
							statisticList.get(j).setInCount(inCount);
							statisticList.get(j).setOutCount(outCount);
							statisticList.get(j).setTotalCount(totalCount);
							statisticList.get(j).setTotalEndCount(totalEndCount);
						}
			    	   if (statisticList.size()>0) {
			    		   page.getList().get(i).setKeyDepotGeneralStatisticsList(statisticList);
			    	   }
					}
					
					page.getList().get(i).setTotolCount(total);
					page.getList().get(i).setInCount(in);
					page.getList().get(i).setOutCount(out);
					page.getList().get(i).setTotalEndCount(totalEnd);
					page.getList().get(i).setTotolName("合计");
			  }
			  model.addAttribute("page", page);
		}
		
		if (supplierId!=null) {
			 List<KeyGeneralInfo>  keys = keyGeneralInfoService.findBySupplierId(supplierId);
		        model.addAttribute("keys", keys);
		}
		
        List<ConfigSupplier> suppliers = configSupplierService.findByKey();
        model.addAttribute("suppliers", suppliers);
    	
        
    	List<Office> offsList =  officeService.getOfficeByType(user, 1);
    	
		
		List<Office> offices =  officeService.getOfficeByType(user, 2);
		if (offices.size()==1) {
			model.addAttribute("offsListSize",1);
			model.addAttribute("offices",offices);
		}
    	
    	if (area!=null) {
    		model.addAttribute("areaId",area);
    		
    		List<Office> offs = officeService.findByParentId(area);
    		model.addAttribute("offices",offs);
    		if (office!=null) {
    			model.addAttribute("office",office);
    		}
    	}
    	
    	model.addAttribute("offsList",offsList);
    	model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("depotName", depotName);
		model.addAttribute("keyId", keyId);
		model.addAttribute("supplierId", supplierId);
		model.addAttribute("ruKuZongliang", ruKuZongliang);
		model.addAttribute("chuKuZongling",chuKuZongling);
		return "modules/key/keyUsbKeyDepotCountList";
	}  
	
	@RequiresPermissions("key:keyUsbKeyDepot:view")
	@RequestMapping(value = "outList")
	public String outlist(KeyUsbKeyDepot keyUsbKeyDepot, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			keyUsbKeyDepot.setCreateBy(user);
		}
        Page<KeyUsbKeyDepot> page = keyUsbKeyDepotService.find(new Page<KeyUsbKeyDepot>(request, response), keyUsbKeyDepot); 
/*       for (int i = 0; i < page.getList().size(); i++) {
    	   if (page.getList().get(i).getOffice().getParent().getParent().getId()==1) {
			
    		   List<KeyGeneralInfo> generalList =   keyGeneralInfoService.findAll();
    		   page.getList().get(i).setGeneralInfoList(generalList);
		}
       }*/
        model.addAttribute("page", page);
		return "modules/key/keyUsbKeyOutDepotList";
	}
	
	
	
	
	
	@RequiresPermissions("key:keyUsbKeyDepot:view")
	@RequestMapping(value = "form")
	public String form(KeyUsbKeyDepot keyUsbKeyDepot, Model model) {
		List<Office> offices =  officeService.selectAreaList();
		for (int i = 0; i < offices.size();) {
			Office office = offices.get(i);
			if (office.getType().equals("2")) {
				offices.remove(i);
			}else {
				i++;
			}
		}
		Office scca = officeService.get(1L);
		offices.add(scca);
		model.addAttribute("offices", offices);
		model.addAttribute("keyUsbKeyDepot", keyUsbKeyDepot);
		return "modules/key/keyUsbKeyDepotForm";
	}
	
	
	/*@RequiresPermissions("key:keyUsbKeyDepot:view")
	@RequestMapping(value = "check")
	public String check(KeyUsbKeyDepot keyUsbKeyDepot, Model model) {
		List<Office> offices =  officeService.selectAreaList();
		model.addAttribute("offices", offices);
		model.addAttribute("keyUsbKeyDepot", keyUsbKeyDepot);
	    KeyManufacturer keyManufacturer = keyManufacturerService.get(keyUsbKeyDepot.getManufacturerId()) ;
	    model.addAttribute("keyManufacturer", keyManufacturer);
	    List<KeyDepotGeneralStatistics> depotGeneStatistics =   keyDepotGeneralStatisticsService.findByDepotId(keyUsbKeyDepot.getId());
	    if (depotGeneStatistics.size()>0) {
  		  keyUsbKeyDepot.setKeyDepotGeneralStatisticsList(depotGeneStatistics);
  		  }
		return "modules/key/depotCheck";
	}*/

	@RequiresPermissions("key:keyUsbKeyDepot:edit")
	@RequestMapping(value = "save")
	public String save(KeyUsbKeyDepot keyUsbKeyDepot, Model model, RedirectAttributes redirectAttributes,Long officeId) {
		if (!beanValidator(model, keyUsbKeyDepot)){
			return form(keyUsbKeyDepot, model);
		}
		
		List<KeyUsbKeyDepot> depotByOff =  keyUsbKeyDepotService.findByOfficeId(officeId);
		if (depotByOff.size()>0) {
			addMessage(redirectAttributes,depotByOff.get(0).getOffice().getName()+ "网点已添加库房！请勿重复添加库房！");
			return "redirect:"+Global.getAdminPath()+"/key/keyUsbKeyDepot/?repage";
		}
		
		
		if (keyUsbKeyDepot.getId()==null) {
			Office off = officeService.get(officeId);
			String depotName = keyUsbKeyDepot.getDepotName();
			off.setId(officeId);
			keyUsbKeyDepot.setOffice(off);
		List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService.findByName(keyUsbKeyDepot.getDepotName());
		if (depots.size()>0) {
		
			if (keyUsbKeyDepot.getId()==null) {
				
				keyUsbKeyDepot.setDepotName(depotName);
				Office office = officeService.get(officeId);
				List<Office> offids =officeService.findByParentId(office.getParent().getId());
				model.addAttribute("offids", offids);
			}
			List<Office> offices =  officeService.selectAreaList();
			model.addAttribute("offices", offices);
			model.addAttribute("keyUsbKeyDepot", keyUsbKeyDepot);
			model.addAttribute("message", "库存名称已存在，请重新输入！");
			return "modules/key/keyUsbKeyDepotForm";
		}
		}else{
			
			List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService.findByNameId(keyUsbKeyDepot.getDepotName(),keyUsbKeyDepot.getId());
			if (depots.size()>0) {
				List<Office> offices =  officeService.selectAreaList();
				model.addAttribute("offices", offices);
				model.addAttribute("keyUsbKeyDepot", keyUsbKeyDepot);
				model.addAttribute("message", "库存名称已存在，请重新输入！");
				return "modules/key/keyUsbKeyDepotForm";
			}
		}
		keyUsbKeyDepotService.save(keyUsbKeyDepot);
		addMessage(redirectAttributes, "保存key入库信息'" + keyUsbKeyDepot.getDepotName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/key/keyUsbKeyDepot/?repage";
	}
	
	@RequiresPermissions("key:keyUsbKeyDepot:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		
		 List<KeyUsbKey> kuks =  keyUsbKeyService.findByDepotId(id);
		if (kuks.size()>0) {
			addMessage(redirectAttributes, "删除库存信息失败！该库存下已有产品正在使用！");
		}else{
			keyUsbKeyDepotService.delete(id);
			addMessage(redirectAttributes, "删除库存信息成功");
		}
		return "redirect:"+Global.getAdminPath()+"/key/keyUsbKeyDepot/?repage";
	}
	
	
	
	@RequestMapping( value = "checkKey" )
	@ResponseBody
	public String checkKey(String deneName) throws JSONException{
		JSONObject json = new JSONObject();
		try {
			if(deneName.indexOf("软证书")==-1)
			{
				
				List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService.findByOfficeId(UserUtils.getUser().getOffice().getId());
				if (depots.size() > 0) {
					List<KeyDepotGeneralStatistics> depotGeneStatistics = null;
					for (KeyUsbKeyDepot e : depots) {
						depotGeneStatistics = keyDepotGeneralStatisticsService
								.findByDepotIdGenename(e.getId(), deneName);
						if (depotGeneStatistics != null
								&& depotGeneStatistics.size() > 0)
							break;
					}
					
					List<KeyGeneralInfo> typeLst = keyGeneralInfoService.findAll();
					boolean typeIsNull = true;
					for (KeyGeneralInfo e : typeLst) {
						if (e.getName().equals(deneName)) {
							typeIsNull = false;
							break;
						}
					}
					
					if (typeIsNull) {
						json.put("status", "2");
						json.put("msg", "当前网点库房中没有此KYE类型标识！");
					}
					
					if (depotGeneStatistics.size() < 1) {
						// 所属仓库+KEY类型没查到记录，不去查KEY管理的类型标识，直接返回没类型标识
						json.put("status", "4");
						json.put("msg", /*"当前网点库房此类型KEY余量为0"*/"当前库房此类型KEY余量不足");
					} else {
						if (depotGeneStatistics.get(0).getInCount() > 0) {
							// 查到记录，且库存数量大于0，提示正常
							json.put("status", "3");
						} else {
							// 查到记录，且库存小于等于0，提示余量为0
							json.put("status", "4");
							json.put("msg", /*"当前网点库房此类型KEY余量为0"*/"当前库房此类型KEY余量不足");
						}
					}
				}else{
					json.put("status","1");
					json.put("msg", "该网点下目前没有库房！请与上级联系！");
					
				}
			}else
			{
				List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService.findByOfficeId(UserUtils.getUser().getOffice().getId());
				if (depots.size() > 0) {
					List<KeyGeneralInfo> typeLst = keyGeneralInfoService.findAll();
					boolean typeIsNull = true;
					for (KeyGeneralInfo e : typeLst) {
						if (e.getName().equals(deneName)) {
							typeIsNull = false;
							break;
						}
					}
					if (typeIsNull) {
						json.put("status", "2");
						json.put("msg", "当前网点库房中没有此KYE类型标识！");
					}else
					{
						json.put("status", "3");
					}
				}else{
					json.put("status","1");
					json.put("msg", "该网点下目前没有库房！请与上级联系！");
					
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			json.put("status","0");
			json.put("msg", "系统异常");
			e.printStackTrace();
		}
		
		return json.toString();
		
	}
	

		//分配页面
		@RequiresPermissions("key:keyUsbKeyDepot:edit")
		@RequestMapping(value = "assign")
		public String assign(Long id, Model model) {
			
			KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(id);
			
			List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService.getDepotList(depot);
			
			 for (int i = 0; i <  depots.size(); i++) {
		    	   Long depotId =   depots.get(i).getId();//获取库存的id
		    	   List<KeyDepotGeneralStatistics> depotGeneStatistics =   keyDepotGeneralStatisticsService.findByDepotId(depotId);
		    	   
		    	   int inCount = 0;
		    	   for (int j = 0; j < depotGeneStatistics.size(); j++) {
		    		   inCount += depotGeneStatistics.get(j).getInCount();//计算库存总量
		    	   }
		    	   depots.get(i).setInCount(inCount);
		    	   if (depotGeneStatistics.size()>0) {
		    		   depots.get(i).setKeyDepotGeneralStatisticsList(depotGeneStatistics);
				}
		       }
			
			
			model.addAttribute("depot", depot);
			model.addAttribute("depots", depots);
			
			return "modules/key/keyUsbKeyDepotAssign";
		}
		
		
		//分配的iframe
		@RequiresPermissions("key:keyUsbKeyDepot:edit")
		@RequestMapping(value = "depottodepot")
		public String selectDepotToParent(Long id, Model model) {
			KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(id);
			model.addAttribute("depot", depot);
			model.addAttribute("selectIds", keyUsbKeyDepotService.getDepotIds(depot));      //已选择的库房id
			
			
			List<Office> offices =  officeService.getOfficeByType(UserUtils.getUser(), 2);
			
			for (int i = 0; i < offices.size(); i++) {
				log.debug(offices.get(i).getId()+"-");
			}
			
			model.addAttribute("depotList", keyUsbKeyDepotService.findListByOffices(depot,offices));              //所有的库房id(前提是权限内的并且除自己以外)
			model.addAttribute("selectedList", keyUsbKeyDepotService.getDepotList(depot));
			return "modules/key/selectDepotToParent";
		}
		
		
		@RequiresPermissions("key:keyUsbKeyDepot:edit")
		@RequestMapping(value = "assignDepot")
		public String assignDepot(Long id, Long[] idsArr, RedirectAttributes redirectAttributes) {
			
			KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(id);
			
			StringBuilder msg = new StringBuilder();
			int newNum = 0;
			for (int i = 0; i < idsArr.length; i++) {
				KeyUsbKeyDepot child = keyUsbKeyDepotService.get(idsArr[i]);
				if (null != child) {
					
					child.setParent(depot);
					keyUsbKeyDepotService.save(child);
					
					msg.append("<br/>新增下级库房【" + child.getDepotName() + "】！");
					newNum++;
				}
			}
			addMessage(redirectAttributes, "已成功分配 "+newNum+" 个下级库房"+msg);
			return "redirect:"+Global.getAdminPath()+"/key/keyUsbKeyDepot/assign?id="+depot.getId();
		}
	
		
		@RequiresPermissions("key:keyUsbKeyDepot:edit")
		@RequestMapping(value = "outDepot")
		public String outDepot(Long depotId,Long parentId, RedirectAttributes redirectAttributes) {
			KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);
			depot.setParent(null);
			keyUsbKeyDepotService.save(depot);
			
				
			addMessage(redirectAttributes, "下级库房【" + depot.getDepotName() + "】移除成功！");
				
			
			return "redirect:"+Global.getAdminPath()+"/key/keyUsbKeyDepot/assign?id="+parentId;
		}
}
