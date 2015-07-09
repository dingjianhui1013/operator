/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.openxmlformats.schemas.drawingml.x2006.chart.STScatterStyle;
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
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotTypeStatistics;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotTypeStatisticsService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptTypeService;

/**
 * 入库信息Controller
 * @author WHW
 * @version 2014-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptEnterInfo")
public class ReceiptEnterInfoController extends BaseController {

	private LogUtil logUtil = new LogUtil();
	
	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	
	@Autowired
	private ReceiptTypeService receiptTypeService;
	
	@Autowired
	private ReceiptDepotTypeStatisticsService receiptDepotTypeStatisticsService;
	
	@ModelAttribute
	public ReceiptEnterInfo get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptEnterInfoService.get(id);
		}else{
			return new ReceiptEnterInfo();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(ReceiptEnterInfo receiptEnterInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			receiptEnterInfo.setCreateBy(user);
		}
        Page<ReceiptEnterInfo> page = receiptEnterInfoService.find(new Page<ReceiptEnterInfo>(request, response), receiptEnterInfo); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptEnterInfoList";
	}

	@RequiresPermissions("receipt:receiptEnterInfo:view")
	@RequestMapping(value = "form")
	public String form(ReceiptEnterInfo receiptEnterInfo, Model model) {
		model.addAttribute("receiptEnterInfo", receiptEnterInfo);
		return "modules/receipt/receiptEnterInfoForm";
	}

	@RequiresPermissions("receipt:receiptEnterInfo:edit")
	@RequestMapping(value = "save")
	public String save(ReceiptEnterInfo receiptEnterInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptEnterInfo)){
			return form(receiptEnterInfo, model);
		}
		receiptEnterInfo.setBeforMoney(receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue());
		receiptEnterInfo.setNow_Money(receiptEnterInfo.getReceiptMoney()+receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue());
		receiptEnterInfoService.save(receiptEnterInfo);
		logUtil.saveSysLog("发票管理", "总库添加入库信息成功，入库金额为："+receiptEnterInfo.getReceiptMoney(),"");
//		addMessage(redirectAttributes, "保存入库信息'" + receiptEnterInfo.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptEnterInfo/?repage";
	}
	
	
	@RequestMapping(value = "saveRK")
	public String saveRK(ReceiptEnterInfo receiptEnterInfo, Model model, RedirectAttributes redirectAttributes,
			String receiptType,String applyNum
			) {
		if (!beanValidator(model, receiptEnterInfo)){
			return form(receiptEnterInfo, model);
		}
		String[] types =  receiptType.split(",");
		String[] count =  applyNum.split(",");
		ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService.get(receiptEnterInfo.getReceiptDepotInfo().getId());
		for (int i = 0; i < types.length; i++) {
			ReceiptEnterInfo enter = new ReceiptEnterInfo();
			enter.setReceiptType(0);//1为采购入库
			enter.setType(receiptTypeService.get(Long.parseLong(types[i])));
			enter.setReceiptDepotInfo(receiptDepotInfo);
			enter.setRemarks(receiptEnterInfo.getRemarks());
			enter.setCount(Integer.parseInt(count[i]));
			Integer priceTotal = Integer.parseInt(count[i])*receiptTypeService.get(Long.parseLong(types[i])).getTypeName();
			enter.setNow_Money(Double.parseDouble(priceTotal.toString()));
			receiptEnterInfoService.save(enter);
		}
		
		for (int i = 0; i < types.length; i++) {
			
			ReceiptDepotTypeStatistics seceiptDepotTypeStatistics = receiptDepotTypeStatisticsService.findByTypeDepot(receiptEnterInfo.getReceiptDepotInfo().getId(), Long.parseLong(types[i]));
			if (seceiptDepotTypeStatistics!=null) {
				ReceiptDepotTypeStatistics stas =seceiptDepotTypeStatistics;
				Integer inTotal = stas.getInCount();
				Integer total = stas.getTotalEndCount();
				stas.setInCount(inTotal+Integer.parseInt(count[i]));
				stas.setTotalEndCount(total+Integer.parseInt(count[i]));
				receiptDepotTypeStatisticsService.save(stas);
			}else {
				
				ReceiptDepotTypeStatistics stas = new ReceiptDepotTypeStatistics();
				stas.setReceiptDepotInfo(receiptDepotInfo);
				stas.setInCount(Integer.parseInt(count[i]));
				stas.setTotalEndCount(Integer.parseInt(count[i]));
				stas.setOutCount(0);
				stas.setReceiptType(receiptTypeService.get(Long.parseLong(types[i])));
				receiptDepotTypeStatisticsService.save(stas);
			}
		}
		
		logUtil.saveSysLog("发票管理", "总库添加入库信息成功，入库金额为："+receiptEnterInfo.getReceiptMoney(),"");
		
		receiptDepotInfo.setReceiptTotal(receiptEnterInfo.getReceiptMoney()+(receiptDepotInfo.getReceiptTotal()==null?0:receiptDepotInfo.getReceiptTotal()));
		receiptDepotInfo.setReceiptResidue(receiptEnterInfo.getReceiptMoney()+(receiptDepotInfo.getReceiptResidue()==null?0:receiptDepotInfo.getReceiptResidue()));
		receiptDepotInfoService.save(receiptDepotInfo);
		logUtil.saveSysLog("发票管理", "总库库房修改库房余额，余额为："+receiptDepotInfo.getReceiptResidue(),"");
		addMessage(redirectAttributes, "入库成功");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptDepotInfo/list";
	}
	
	@RequiresPermissions("receipt:receiptEnterInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		receiptEnterInfoService.delete(id);
		addMessage(redirectAttributes, "删除入库信息成功");
		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptEnterInfo/?repage";
	}

}
