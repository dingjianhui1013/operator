/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkFinancePayInfoRelation;
import com.itrus.ca.modules.work.entity.WorkPayInfo;
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
		
		List<Office> offices =  officeService.getOfficeByType(user, 2);
		
		for (int i = 0; i < offices.size(); i++) {
			System.out.print(offices.get(i).getId()+"-");
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
			Model model) {
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
		
		if (startTime==null&&endTime==null) {
		
			
			List<Office> offsList =  officeService.getOfficeByType(user, 1);
    		
			List<Office> offices =  officeService.getOfficeByType(user, 2);
			if (offices.size()==1) {
				model.addAttribute("offsListSize",1);
				model.addAttribute("offices",offices);
			}
			List<ConfigSupplier> suppliers = configSupplierService.findByKey();
		    model.addAttribute("suppliers", suppliers);
			model.addAttribute("offsList",offsList);
			return "modules/key/keyUsbKeyDepotCountList";
		}else{
			  Page<KeyUsbKeyDepot> page = keyUsbKeyDepotService.findAll(new Page<KeyUsbKeyDepot>(request, response),area,office,depotName); 
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
						List<KeyUsbKey> kukBefores = keyUsbKeyService.findByDepotIdStartTime(depotId,startTime,supplierId,keyId);//获取开始时间之前的入库信息
						List<KeyUsbKeyInvoice> kukiBefores = keyUsbKeyInvoiceService.findByDepotId(depotId,startTime,supplierId,keyId);//获取开始之前的出库信息
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
			addMessage(redirectAttributes,depotByOff.get(0).getOffice().getName()+ "网点已添加库存！请勿重复添加库存！");
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
			
			List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService.findByOfficeId(UserUtils.getUser().getOffice().getId());
			if (depots.size()>0) {
				List<KeyDepotGeneralStatistics> depotGeneStatistics =   keyDepotGeneralStatisticsService.findByDepotIdGenename(depots.get(0).getId(),deneName);
				if (depotGeneStatistics.size()<1) {
					json.put("status","2");
					json.put("msg", "当前网点库房中没有此KYE类型标识！");
				}else{
					if (depotGeneStatistics.get(0).getInCount()>0) {
						json.put("status","3");
					}else{
						json.put("status","4");
						json.put("msg", "当前网点库房KEY余量为0");
					}
				}
			}else{
				json.put("status","1");
				json.put("msg", "该网点下目前没有库房！请与上级联系！");
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			json.put("status","0");
			json.put("msg", "系统异常");
			e.printStackTrace();
		}
		
		return json.toString();
		
	}
	

}
