/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javassist.expr.NewArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.itrus.ca.modules.sys.entity.Area;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.Role;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.service.ReceiptAllocateApplyService;
import com.itrus.ca.modules.receipt.service.ReceiptCheckLogService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.receipt.service.ReceiptTypeService;

/**
 * 发票信息Controller
 * @author ZhangJingtao
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptDepotInfo")
public class ReceiptDepotInfoController extends BaseController {
	
	private LogUtil logUtil = new LogUtil();

	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;
	
	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	
	@Autowired
	private ReceiptAllocateApplyService receiptAllocateApplyService;
	
	@Autowired
	private ReceiptCheckLogService receiptCheckLogService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ReceiptTypeService receiptTypeService;
	
	@ModelAttribute
	public ReceiptDepotInfo get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptDepotInfoService.get(id);
		}else{
			return new ReceiptDepotInfo();
		}
	}
	
	@RequiresPermissions("receipt:receiptDepotInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptDepotInfo receiptDepotInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		
		List<Office> offices =  officeService.getOfficeByType(user, 2);
		
		model.addAttribute("isSysadmin", false);
		
		List<Role> roleList = user.getRoleList();
		for(Role role:roleList){
			if(role.getId()==1){
				model.addAttribute("isSysadmin", true);
				break;
			}
		}
		
        Page<ReceiptDepotInfo> page = receiptDepotInfoService.find(new Page<ReceiptDepotInfo>(request, response), receiptDepotInfo,offices); 
        model.addAttribute("zkid",KeyDepotId.RECEIPT_DEPOT_ID);
        model.addAttribute("page", page);
		return "modules/receipt/receiptDepotInfoList";
	}

	@RequiresPermissions("receipt:receiptDepotInfo:addKF")
	@RequestMapping(value = "form")
	public String form(ReceiptDepotInfo receiptDepotInfo, Model model) {
		model.addAttribute("receiptDepotInfo", receiptDepotInfo);
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
		return "modules/receipt/receiptDepotInfoForm";
	}

	@RequestMapping("updateFrom")
	public String insertFrom(ReceiptDepotInfo receiptDepotInfo, Model model){
		model.addAttribute("receiptDepotInfo", receiptDepotInfo);
		return "modules/receipt/receiptDepotInfoUpdate";
	}
	
	@RequiresPermissions("receipt:receiptDepotInfo:view")
	@RequestMapping(value = "save")
	public String save(ReceiptDepotInfo receiptDepotInfo, Long officeId,Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptDepotInfo)){
			return form(receiptDepotInfo, model);
		}
		if(officeId==null || officeId.equals(0L)){
			addMessage(redirectAttributes, "新建" + receiptDepotInfo.getReceiptName() + "失败，网点名称不可为空");
			return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/?repage";
		}
		Office office = officeService.get(officeId);
		if(receiptDepotInfo.getId()==null&&receiptDepotInfoService.findDepotByOffice(office).size()>0){
			addMessage(redirectAttributes, office.getName()+"该网点下已存在发票仓库，不可再次添加！");
			return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/?repage";
		}
		String msg= receiptDepotInfo.getId()!=null?("修改"+ receiptDepotInfo.getReceiptName() + "成功"):("保存"+ receiptDepotInfo.getReceiptName()+"成功");
		receiptDepotInfo.setOffice(office);
		receiptDepotInfo.setArea(office.getParent());
		if(receiptDepotInfo.getReceiptOut()==null){
			receiptDepotInfo.setReceiptOut(0.0);
		}
		if(receiptDepotInfo.getReceiptTotal()==null){
			receiptDepotInfo.setReceiptTotal(0.0);
		}
		if(receiptDepotInfo.getReceiptResidue()==null){
			receiptDepotInfo.setReceiptResidue(0.0);
		}
		receiptDepotInfoService.save(receiptDepotInfo);
		logUtil.saveSysLog("发票管理",msg, "");
		addMessage(redirectAttributes, "保存" + receiptDepotInfo.getReceiptName() + "成功");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/?repage";
	}
	
	@RequiresPermissions("receipt:receiptDepotInfo:view")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService.get(id);
		if(id.equals(KeyDepotId.RECEIPT_DEPOT_ID)){
			return getZK(redirectAttributes);
		}
		if(receiptInvoiceService.selectByDepotInfo(receiptDepotInfo).size()>0){
			return getUrl(redirectAttributes);
		}
		if(receiptEnterInfoService.selectByDepotInfo(receiptDepotInfo).size()>0){
			return getUrl(redirectAttributes);
		}
		if(receiptAllocateApplyService.selectByDepotInfo(receiptDepotInfo).size()>0){
			return getUrl(redirectAttributes);
		}
		if(receiptCheckLogService.selectByDepotInfo(receiptDepotInfo).size()>0){
			return getUrl(redirectAttributes);
		}
		receiptDepotInfoService.delete(id);
		logUtil.saveSysLog("发票管理","删除发票库房"+receiptDepotInfo.getReceiptName()+"成功", "");
		addMessage(redirectAttributes, "删除发票库房成功");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/?repage";
	}

	/**
	 * 提示信息并返回
	 * @return
	 */
	private String getUrl(RedirectAttributes redirectAttributes){
		addMessage(redirectAttributes, "此库房存在办理过的业务,不可被删除");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/?repage";
	}
	
	
	/**
	 * 提示信息并返回
	 * @return
	 */
	private String getZK(RedirectAttributes redirectAttributes){
		addMessage(redirectAttributes, "总库房不可被删除");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/?repage";
	}
	/**
	 * 弹出入库窗口
	 * @return
	 *//*
	@RequestMapping(value = "alarmForm")
	public String alarmForm(HttpServletRequest request, HttpServletResponse response,
			Model model, Long depotId) {
		model.addAttribute("now_date", getDateString());
		model.addAttribute("depotId", depotId);
		return "modules/receipt/alarmValueForm";
	}*/
	
	private String getDateString(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date()).toString();
	}
	
	@RequestMapping("rukuList")
	public String rukuList(ReceiptDepotInfo receiptDepotInfo, HttpServletRequest request, HttpServletResponse response, Model model){
		ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
		receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
        Page<ReceiptEnterInfo> page = receiptEnterInfoService.find(new Page<ReceiptEnterInfo>(request, response), receiptEnterInfo); 
        if(receiptDepotInfo.getId().equals(KeyDepotId.RECEIPT_DEPOT_ID)){
        	model.addAttribute("state",1);
        }
        model.addAttribute("zkid", KeyDepotId.RECEIPT_DEPOT_ID);
        model.addAttribute("page", page);
		return "modules/receipt/receiptEnterInfoListFP";
	}
	
	@RequestMapping("chukuList")
	public String chukuList(ReceiptDepotInfo receiptDepotInfo, HttpServletRequest request, HttpServletResponse response, Model model){
		ReceiptInvoice receiptInvoice = new ReceiptInvoice();
		receiptInvoice.setReceiptDepotInfo(receiptDepotInfo);
        Page<ReceiptInvoice> page = receiptInvoiceService.find(new Page<ReceiptInvoice>(request, response), receiptInvoice); 
        model.addAttribute("page", page);
        model.addAttribute("zkid", KeyDepotId.RECEIPT_DEPOT_ID);
        model.addAttribute("recriptId", receiptDepotInfo.getId());
        
        
        List<ReceiptInvoice> invoices =  receiptInvoiceService.findAllReceiptInvoice(receiptDepotInfo);
        Double count = 0d;
        Double now = 0d;
        for (int i = 0; i < invoices.size(); i++) {
        	count += invoices.get(i).getReceiptMoney();
        	String oldDate=new SimpleDateFormat("yyyy-MM-dd").format(invoices.get(i).getCreateDate());
			String newDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
			if(oldDate.indexOf(newDate)>-1){
				now += invoices.get(i).getReceiptMoney();
			}
		}
        
        model.addAttribute("count", count);
        model.addAttribute("now", now);
        
		return "modules/receipt/receiptInvoiceListFP";
	}
	
	@RequiresPermissions("receipt:receiptDepotInfo:show")
	@RequestMapping("rukuFrom")
	public String rukuFrom(ReceiptDepotInfo receiptDepotInfo, HttpServletRequest request, HttpServletResponse response, Model model){
		List<ReceiptType> types = receiptTypeService.getAll();	
		model.addAttribute("types",types);
		model.addAttribute("receiptDepotInfo", receiptDepotInfo);
		model.addAttribute("now_date",getDateString());
		model.addAttribute("userName", UserUtils.getUser().getName());
		return "modules/receipt/receiptEnterInfoRK";
	}
	@RequiresPermissions("receipt:receiptDepotInfo:show")
	@RequestMapping("addchuku")
	public String addchuku(ReceiptInvoice receiptInvoice,HttpServletRequest request, HttpServletResponse response, Model model){
		
		List<ReceiptType> receiptType=receiptTypeService.getAll();
		model.addAttribute("receiptType", receiptType);
		model.addAttribute("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		model.addAttribute("typeSize", receiptType.size());
		return "modules/receipt/receiptDepotInfoF";
	}
	@RequestMapping(value= "saveZong")
	public String saveZong(ReceiptInvoice receiptInvoice, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes)
	{
		
		ReceiptDepotInfo receipt= receiptDepotInfoService.get(receiptInvoice.getId());
		double count= receipt.getReceiptResidue()-(((double)receiptInvoice.getType().getTypeName())*receiptInvoice.getCount());
		if(count>=0)
		{
			ReceiptDepotInfo receiptDepotInfo=new ReceiptDepotInfo();
			receiptDepotInfo.setId(receiptInvoice.getId());
			ReceiptInvoice ri=new ReceiptInvoice();
			ri.setReceiptDepotInfo(receiptDepotInfo);
			ri.setCreateDate(receiptInvoice.getCreateDate());
			ri.setCount(receiptInvoice.getCount());
			ri.setReceiptType(receiptInvoice.getReceiptType());
			ReceiptType receiptType=receiptTypeService.findByName(receiptInvoice.getType().getTypeName());
			ri.setType(receiptType);
			ri.getType().setTypeName(receiptInvoice.getType().getTypeName());
			ri.setReceiptMoney(receiptInvoice.getCount()*(double)receiptInvoice.getType().getTypeName());
			ri.setRemarks(receiptInvoice.getRemarks());
			receiptInvoiceService.save(ri);
			receipt.setReceiptResidue(count);
			receiptDepotInfoService.save(receipt);
			addMessage(redirectAttributes,"添加出库信息成功");
		}else
		{
			addMessage(redirectAttributes,"您录入的出库总钱数大于库存中的余量总钱数，请检查！");
		}
	
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/chukuList?id="+1;
	}
	
	@RequestMapping(value = "alarmForm")
	public String alarmForm(
			ReceiptDepotInfo receiptDepotInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model, Long depotId) {
		ReceiptDepotInfo depotInfo= receiptDepotInfoService
				.get(depotId);
		model.addAttribute("depotId", depotId);
		model.addAttribute("depotInfo", depotInfo);
		return "modules/receipt/alarmValueForm";
	}
	
	
	@RequestMapping(value = "saveAlarm")
	@ResponseBody
	public String saveAlarm(ReceiptDepotInfo receiptDepotInfo,RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response,
			Model model, Long depotId,String alarm) throws JSONException {
		if (!beanValidator(model, receiptDepotInfo)){
		//	return form(keyDepotGeneralStatistics, model);
		}
		JSONObject json = new JSONObject();
		ReceiptDepotInfo depotInfo= null;
		try {
			depotInfo= receiptDepotInfoService
					.get(depotId);
			if(depotInfo!=null){
				depotInfo.setPrewarning(Double.parseDouble(alarm));
			}
			receiptDepotInfoService.save(depotInfo);
			logUtil.saveSysLog("发票管理", "保存"+depotInfo.getReceiptName()+"成功", "");
			addMessage(redirectAttributes, "保存成功！");
			json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.saveSysLog("发票管理", "保存"+depotInfo.getReceiptName()+"失败", "");
			addMessage(redirectAttributes, "保存失败！");
			json.put("status", "0");
		}
		return json.toString();
	}
	
	
	
			//分配页面
			@RequiresPermissions("receipt:receiptDepotInfo:show")
			@RequestMapping(value = "assign")
			public String assign(Long id, Model model) {
				
				ReceiptDepotInfo depot = receiptDepotInfoService.get(id);
				
				List<ReceiptDepotInfo> depots = receiptDepotInfoService.getDepotList(depot);
				
				
				model.addAttribute("depot", depot);
				model.addAttribute("depots", depots);
				
				return "modules/receipt/receiptDepotInfoAssign";
			}
			
			//分配的iframe
			@RequiresPermissions("receipt:receiptDepotInfo:show")
			@RequestMapping(value = "depottodepot")
			public String selectDepotToParent(Long id, Model model) {
				ReceiptDepotInfo depot = receiptDepotInfoService.get(id);
				model.addAttribute("depot", depot);
				model.addAttribute("selectIds", receiptDepotInfoService.getDepotIds(depot));      //已选择的库房id
				
				
				List<Office> offices =  officeService.getOfficeByType(UserUtils.getUser(), 2);
				
				model.addAttribute("depotList", receiptDepotInfoService.findListByOffices(depot,offices));              //所有的库房id(前提是权限内的并且除自己以外)
				model.addAttribute("selectedList", receiptDepotInfoService.getDepotList(depot));
				return "modules/receipt/selectDepotToParent";
			}
			
			
			@RequiresPermissions("receipt:receiptDepotInfo:show")
			@RequestMapping(value = "assignDepot")
			public String assignDepot(Long id, Long[] idsArr, RedirectAttributes redirectAttributes) {
				
				ReceiptDepotInfo depot = receiptDepotInfoService.get(id);
				
				StringBuilder msg = new StringBuilder();
				int newNum = 0;
				for (int i = 0; i < idsArr.length; i++) {
					ReceiptDepotInfo child = receiptDepotInfoService.get(idsArr[i]);
					if (null != child) {
						
						child.setParent(depot);
						receiptDepotInfoService.save(child);
						
						msg.append("<br/>新增下级库房【" + child.getReceiptName() + "】！");
						newNum++;
					}
				}
				addMessage(redirectAttributes, "已成功分配 "+newNum+" 个下级库房"+msg);
				return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/assign?id="+depot.getId();
			}
			
			
			@RequiresPermissions("receipt:receiptDepotInfo:show")
			@RequestMapping(value = "outDepot")
			public String outDepot(Long depotId,Long parentId, RedirectAttributes redirectAttributes) {
				ReceiptDepotInfo depot = receiptDepotInfoService.get(depotId);
				depot.setParent(null);
				receiptDepotInfoService.save(depot);
				
					
				addMessage(redirectAttributes, "下级库房【" + depot.getReceiptName() + "】移除成功！");
					
				
				return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/assign?id="+parentId;
			}
			
	
}
