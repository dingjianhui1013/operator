/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.web;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.receipt.entity.ReceiptAllocateApply;
import com.itrus.ca.modules.receipt.entity.ReceiptAllocateApplyDetail;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotTypeStatistics;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.service.ReceiptAllocateApplyDetailService;
import com.itrus.ca.modules.receipt.service.ReceiptAllocateApplyService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotTypeStatisticsService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.receipt.service.ReceiptTypeService;

/**
 * 调拨信息Controller
 * @author WHW
 * @version 2014-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptAllocateApply")
public class ReceiptAllocateApplyController extends BaseController {

	private LogUtil logUtil = new LogUtil();
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	
	@Autowired
	private ReceiptAllocateApplyService receiptAllocateApplyService;
	
	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;
	
	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ReceiptTypeService receiptTypeService;
	
	@Autowired
	private ReceiptAllocateApplyDetailService receiptAllocateApplyDetailService;
	
	@Autowired
	private ReceiptDepotTypeStatisticsService receiptDepotTypeStatisticsService;

	
	@ModelAttribute
	public ReceiptAllocateApply get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptAllocateApplyService.get(id);
		}else{
			return new ReceiptAllocateApply();
		}
	}
	
	@RequiresPermissions("receipt:receiptAllocateApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptAllocateApply receiptAllocateApply, HttpServletRequest request, HttpServletResponse response, Model model,
			Date startTime, Date endTime, Integer state) {
		model.addAttribute("startTime", startTime);
		model.addAttribute("state", state);
		model.addAttribute("endTime", endTime);
		receiptAllocateApply.setState(state);
        Page<ReceiptAllocateApply> page = receiptAllocateApplyService.find(new Page<ReceiptAllocateApply>(request, response), receiptAllocateApply, startTime, endTime); 
        for (int i = 0; i < page.getList().size(); i++) {
        	List<ReceiptAllocateApplyDetail> details = receiptAllocateApplyDetailService.getByApplyId(page.getList().get(i).getId());
        	page.getList().get(i).setReceiptApplyDetails(details);
		}
        
        model.addAttribute("page", page);
		return "modules/receipt/receiptAllocateApplyList";
	}

	/**
	 * 审批列表
	 * @param receiptAllocateApply
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("receipt:receiptAllocateApply:edit")
	@RequestMapping("listT")
	public String listT(ReceiptAllocateApply receiptAllocateApply, HttpServletRequest request, HttpServletResponse response, Model model,
			Date startTime, Date endTime, Integer state) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			receiptAllocateApply.setCreateBy(user);
		}
		List<Office> offices = officeService.getOfficeByType(UserUtils.getUser(),2);
		List<Long> officeids = Lists.newArrayList();
		if (offices.size() > 0) {
			for (int i = 0; i < offices.size(); i++) {
				officeids.add(offices.get(i).getId());
			}
		}
		
		model.addAttribute("offices", offices);
		model.addAttribute("startTime", startTime);
		model.addAttribute("state", state);
		model.addAttribute("endTime", endTime);
		receiptAllocateApply.setState(state);
        Page<ReceiptAllocateApply> page = receiptAllocateApplyService.findT(new Page<ReceiptAllocateApply>(request, response), receiptAllocateApply, officeids,startTime, endTime); 
        for (int i = 0; i < page.getList().size(); i++) {
        	List<ReceiptAllocateApplyDetail> details = receiptAllocateApplyDetailService.getByApplyId(page.getList().get(i).getId());
        	page.getList().get(i).setReceiptApplyDetails(details);
		}
        
        
        model.addAttribute("page", page);
		return "modules/receipt/receiptAllocateApplyListT";
	}
	
	
	@RequiresPermissions("receipt:receiptAllocateApply:view")
	@RequestMapping(value = "form")
	public String form(ReceiptAllocateApply receiptAllocateApply, Model model) {
		List<Office> offices = officeService.getOfficeByType(UserUtils.getUser(), 2);
		//List<ReceiptType> types = receiptTypeService.getAll();
		List<ReceiptType> types = new ArrayList<ReceiptType>();
		List<ReceiptDepotTypeStatistics> stas = receiptDepotTypeStatisticsService.findByDepot(KeyDepotId.RECEIPT_DEPOT_ID);
		for (int i = 0; i < stas.size(); i++) {
			types.add(stas.get(i).getReceiptType());
		}
		model.addAttribute("types",types);
		model.addAttribute("offices", offices);
		model.addAttribute("receiptAllocateApply", receiptAllocateApply);
		return "modules/receipt/receiptAllocateApplyForm";
	}

	/**
	 * 确认收货
	 * @param receiptAllocateApply
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("receipt:receiptAllocateApply:view")
	@RequestMapping(value = "formQR")
	public String formQR(ReceiptAllocateApply receiptAllocateApply, Model model, RedirectAttributes redirectAttributes) {
		ReceiptDepotInfo receiptDepotInfo = receiptAllocateApply.getReceiptDepotInfo();
		receiptAllocateApply.setState(3);
		receiptAllocateApplyService.save(receiptAllocateApply);
		logUtil.saveSysLog("发票管理", "修改调拨信息(状态为：完成)成功,id为"+receiptAllocateApply.getId(), "");
		
		receiptDepotInfo.setReceiptTotal(receiptAllocateApply.getMoney()+(receiptDepotInfo.getReceiptTotal()==null?0:receiptDepotInfo.getReceiptTotal()));
		receiptDepotInfo.setReceiptResidue(receiptAllocateApply.getMoney()+(receiptDepotInfo.getReceiptResidue()==null?0:receiptDepotInfo.getReceiptResidue()));
		receiptDepotInfoService.save(receiptDepotInfo);
		logUtil.saveSysLog("发票管理", "修改库房金额成功,库房为"+receiptDepotInfo.getReceiptName(), "");
		
		List<ReceiptAllocateApplyDetail> applyDetails =  receiptAllocateApplyDetailService.getByApplyId(receiptAllocateApply.getId());
		for (int i = 0; i < applyDetails.size(); i++) {
			
			ReceiptDepotTypeStatistics seceiptDepotTypeStatistics = receiptDepotTypeStatisticsService.findByTypeDepot(receiptDepotInfo.getId(),applyDetails.get(i).getReceiptType().getId());
			if (seceiptDepotTypeStatistics!=null) {
				ReceiptDepotTypeStatistics stas =seceiptDepotTypeStatistics;
				Integer inTotal = stas.getInCount();
				Integer total = stas.getTotalEndCount();
				stas.setInCount(inTotal+applyDetails.get(i).getApplyNum());
				stas.setTotalEndCount(total+applyDetails.get(i).getApplyNum());
				receiptDepotTypeStatisticsService.save(stas);
				logUtil.saveSysLog("发票管理", "修改库房详情成功,库房为"+stas.getReceiptDepotInfo().getReceiptName(), "");
			}else {
				
				ReceiptDepotTypeStatistics stas = new ReceiptDepotTypeStatistics();
				stas.setReceiptDepotInfo(receiptDepotInfo);
				stas.setInCount(applyDetails.get(i).getApplyNum());
				stas.setTotalEndCount(applyDetails.get(i).getApplyNum());
				stas.setOutCount(0);
				stas.setReceiptType(applyDetails.get(i).getReceiptType());
				receiptDepotTypeStatisticsService.save(stas);
				logUtil.saveSysLog("发票管理", "保存库房详情成功,库房为"+stas.getReceiptDepotInfo().getReceiptName(), "");
			}
			
			ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
			receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
			receiptEnterInfo.setCount(applyDetails.get(i).getApplyNum());
			receiptEnterInfo.setReceiptType(1);//调拨入库
			receiptEnterInfo.setType(applyDetails.get(i).getReceiptType());
			receiptEnterInfo.setRemarks(receiptAllocateApply.getRemarks());
			
			Integer priceTotal = applyDetails.get(i).getApplyNum()*applyDetails.get(i).getReceiptType().getTypeName();
			receiptEnterInfo.setNow_Money(new Double(priceTotal));
			receiptEnterInfoService.save(receiptEnterInfo);
			logUtil.saveSysLog("发票管理", "库房"+receiptDepotInfo.getReceiptName()+"添加入库信息成功", "");
		}
		addMessage(redirectAttributes, "收货成功");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptAllocateApply/list";
	}
	
	@RequestMapping("spFrom")
	public String spFrom(ReceiptAllocateApply receiptAllocateApply, Model model){
		model.addAttribute("receiptAllocateApply", receiptAllocateApply);
		return "modules/receipt/receiptAllocateApplySP";
	}

	@RequiresPermissions("receipt:receiptAllocateApply:view")
	@RequestMapping(value = "save")
	public String save(String reqDate, ReceiptAllocateApply receiptAllocateApply,
			Long officeId, Model model, RedirectAttributes redirectAttributes,
			String receiptType,String applyNum,Double money
			) {
		if (!beanValidator(model, receiptAllocateApply)){
			return form(receiptAllocateApply, model);
		}
		ReceiptDepotInfo receiptDepotInfo = getDeoptByOfficeId(officeId);
		if(receiptDepotInfo!=null){
			receiptAllocateApply.setState(0);
			receiptAllocateApply.setReceiptDepotInfo(receiptDepotInfo);
			receiptAllocateApply.setOffice(receiptDepotInfo.getOffice());
			receiptAllocateApply.setWarehouseDate(getTimestamp(reqDate));
			receiptAllocateApply.setMoney(money);
			receiptAllocateApply.setReceiptType(null);
			
			receiptAllocateApply.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
			receiptAllocateApply.setOfficeId(UserUtils.getUser().getOffice().getId());
			
			receiptAllocateApplyService.save(receiptAllocateApply);
			logUtil.saveSysLog("发票管理","库房"+receiptDepotInfo.getReceiptName()+"添加调拨申请成功，申请金额为"+receiptAllocateApply.getMoney(), "");
			
			String[] receiptTypes = receiptType.split(",");
			String[] applyNums = applyNum.split(",");
			for (int i = 0; i < receiptTypes.length; i++) {
				ReceiptAllocateApplyDetail receiptAllocateApplyDetail = new ReceiptAllocateApplyDetail();
				receiptAllocateApplyDetail.setApplyNum(Integer.parseInt(applyNums[i]));
				ReceiptType type = receiptTypeService.get(Long.parseLong(receiptTypes[i]));
				receiptAllocateApplyDetail.setReceiptType(type);
				receiptAllocateApplyDetail.setReceiptAllocateApply(receiptAllocateApply);
				receiptAllocateApplyDetailService.save(receiptAllocateApplyDetail);
			}
			addMessage(redirectAttributes, "调拨申请已提交，等待审批！");
		}else{
			addMessage(redirectAttributes, "未查询到该网点下库房信息，调拨失败");
		}
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptAllocateApply/?repage";
	}
	
	/**
	 * 调拨审批
	 * @param reqDate
	 * @param receiptAllocateApply
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveSP")
	@ResponseBody
	public String saveSP(String reqDate, ReceiptAllocateApply receiptAllocateApply) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(receiptAllocateApply.getState()==1){
			ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService.get(KeyDepotId.RECEIPT_DEPOT_ID);
			List<ReceiptAllocateApplyDetail> details = receiptAllocateApplyDetailService.getByApplyId(receiptAllocateApply.getId());
			String mesString = "";
			for (int i = 0; i < details.size(); i++) {
				ReceiptDepotTypeStatistics  stas = receiptDepotTypeStatisticsService.findByTypeDepot(receiptDepotInfo.getId(), details.get(i).getReceiptType().getId());
				if (stas.getTotalEndCount()<details.get(i).getApplyNum()) {
					mesString += stas.getReceiptType().getTypeName()+"元";
					if (i<details.size()-1) {
						mesString +="、";
					}
				}
			}
			if(!mesString.equals("")){
				map.put("status", 2);
				map.put("msg","出库失败，总库发票类型（"+mesString+"）不足");
//				receiptAllocateApply.setState(0);
//				logUtil.saveSysLog("发票管理","总库调拨出库失败(总库余额不足)","");
				return new JSONObject(map).toString();
			}else{
				
				for (int i = 0; i < details.size(); i++) {
					ReceiptInvoice receiptInvoice = new ReceiptInvoice();
					receiptInvoice.setReceiptDepotInfo(receiptDepotInfo);
					receiptInvoice.setReceiptType(1);//调拨出库
					receiptInvoice.setCompanyName(receiptAllocateApply.getReceiptDepotInfo().getReceiptName());
					receiptInvoice.setCount(details.get(i).getApplyNum());
					receiptInvoice.setType(details.get(i).getReceiptType());
					
					Integer totalInteger =details.get(i).getApplyNum()*details.get(i).getReceiptType().getTypeName();
					receiptInvoice.setReceiptMoney(new Double(totalInteger));
					receiptInvoiceService.save(receiptInvoice);
					logUtil.saveSysLog("发票管理","总库调拨出库成功(接受库房为"+receiptDepotInfo.getReceiptName()+",金额为"+receiptInvoice.getReceiptMoney()+")","");
					
					ReceiptDepotTypeStatistics  stas = receiptDepotTypeStatisticsService.findByTypeDepot(receiptDepotInfo.getId(), details.get(i).getReceiptType().getId());
					stas.setTotalEndCount(stas.getTotalEndCount()+receiptInvoice.getCount());
					stas.setOutCount(stas.getOutCount()+receiptInvoice.getCount());
					receiptDepotTypeStatisticsService.save(stas);
					receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue()-receiptInvoice.getReceiptMoney());
					receiptDepotInfo.setReceiptOut(receiptDepotInfo.getReceiptOut()+receiptInvoice.getReceiptMoney());
					receiptDepotInfoService.save(receiptDepotInfo);
					logUtil.saveSysLog("发票管理","总库发票余额更改修,余额为"+receiptDepotInfo.getReceiptResidue(),"");
				}				
			}
		}
		String msg = receiptAllocateApply.getState()!=1?("修改调拨信息状态成功(状态为：拒绝),id为"+receiptAllocateApply.getId()):("修改调拨信息状态成功(状态为：批准),id为"+receiptAllocateApply.getId());
		receiptAllocateApplyService.save(receiptAllocateApply);
		logUtil.saveSysLog("发票管理",msg,"");
		map.put("status", 1);
		map.put("msg","调拨审批成功");
		return new JSONObject(map).toString();
	}
	
	@RequiresPermissions("receipt:receiptAllocateApply:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		receiptAllocateApplyService.delete(id);
		addMessage(redirectAttributes, "删除调拨信息成功");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptAllocateApply/?repage";
	}
	
	@RequestMapping("selectDepotByOfficeId")
	@ResponseBody
	public String selectDepotByOfficeId(Long officeId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<ReceiptDepotInfo> receiptDepotInfos = null;
		if(officeId!=null&&officeId>0){
			receiptDepotInfos = receiptDepotInfoService.findDepotByOffice(officeService.get(officeId));
			if(receiptDepotInfos.size()>0){
				map.put("status", receiptDepotInfos.get(0).getReceiptName());
				return new JSONObject(map).toString();
			}
		}
		map.put("status", "此网点没有库房");
		return new JSONObject(map).toString();
	}
	
	/**
	 * 通过网点ID获取仓库
	 */
	private ReceiptDepotInfo getDeoptByOfficeId(Long officeId){
		List<ReceiptDepotInfo> receiptDepotInfos = null;
		if(officeId!=null&&officeId>0){
			receiptDepotInfos = receiptDepotInfoService.findDepotByOffice(officeService.get(officeId));
			if(receiptDepotInfos.size()>0){
				return receiptDepotInfos.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 获取当前登录用户的库房
	 */
	private ReceiptDepotInfo getReceiptDepotInfo(){
		User user = UserUtils.getUser();
		List<ReceiptDepotInfo> depotInfos = receiptDepotInfoService.findDepotByOffice(user.getOffice());
		return depotInfos.get(0);
	}
	
	/**
	 * 转换时间格式 转换成Timestamp
	 * @param date
	 * @return
	 */
	private Timestamp getTimestamp(String date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp timestamp = null;
		try {
			timestamp = new Timestamp(dateFormat.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	
	
	@RequestMapping(value = "showRefusalReason")
	public String showRefusalReason(ReceiptAllocateApply receiptAllocateApply,
			Long applyId, Model model) {
		ReceiptAllocateApply apply = receiptAllocateApplyService.get(applyId);
		model.addAttribute("apply", apply);
		return "modules/receipt/showRefusalReasonForm";
	}
	
}
