/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.web;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.receipt.service.ReceiptTypeService;

/**
 * 发票统计Controller
 * 
 * @author WHW
 * @version 2014-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/tong")
public class ReceiptTongController extends BaseController {

	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;

	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;

	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;

	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private ReceiptTypeService receiptTypeService;

	@ModelAttribute
	public ReceiptInvoice get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return receiptInvoiceService.get(id);
		} else {
			return new ReceiptInvoice();
		}
	}

	@RequestMapping(value = { "list", "" })
	public String list(ReceiptInvoice receiptInvoice,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			receiptInvoice.setCreateBy(user);
		}

		Page<ReceiptInvoice> page = receiptInvoiceService.find(
				new Page<ReceiptInvoice>(request, response), receiptInvoice);
		model.addAttribute("page", page);
		return "modules/receipt/receiptInvoiceList";
	}

	@RequestMapping("litj")
	public String litj(ReceiptDepotInfo receiptDepotInfo, 
			HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "comName", required = false) String comName,
			 Model model) {
		ReceiptDepotInfo depotInfo = receiptDepotInfoService
				.get(receiptDepotInfo.getId());
		// 获取库信息
		ReceiptInvoice receiptInvoice = new ReceiptInvoice();
		// 获取开始时间与结束时间
//		Date stad = null;
//		Date endd = null;
//		if (staDate != null && !staDate.equals("")) {
//			stad = getDate(staDate);
//			model.addAttribute("staDate", staDate);
//		}
//		if (endDate != null && !endDate.equals("")) {
//			endd = getDate(endDate);
//			model.addAttribute("endDate", endDate);
//		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE,1);
		endTime = calendar.getTime();
		// 获取入库总金额
		model.addAttribute("numMoney",
				getNumMoney(receiptDepotInfo, startTime, endTime));
		// 获取出库总金额
		model.addAttribute("chuku", getchuku(receiptDepotInfo, startTime, endTime));
		receiptInvoice.setCompanyName(comName);
		receiptInvoice.setReceiptDepotInfo(depotInfo);
		Page<ReceiptInvoice> page = receiptInvoiceService.findByDepotInfo(
				new Page<ReceiptInvoice>(request, response), receiptInvoice,
				startTime, endTime);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("page", page);
		model.addAttribute("receiptDepotInfo", depotInfo);
		model.addAttribute("zkid", KeyDepotId.RECEIPT_DEPOT_ID);
		return "modules/receipt/receiptInvoiceListTJ";
	}

	@RequestMapping("listTJ")
	public String listTJ(ReceiptDepotInfo receiptDepotInfo,Integer skey,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "areaId", required = false) Long areaId,
			@RequestParam(value = "officeId", required = false) Long officeId,
			@RequestParam(value = "receiptName", required = false) String receiptName,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			Model model) {
		List<Office> ooo = officeService.getOfficeByType(UserUtils.getUser(), 2);
		Page<ReceiptDepotInfo> page =null;
		if(skey!=null){
			page = receiptDepotInfoService
					.findByAreaOfficeName(new Page<ReceiptDepotInfo>(request, response),
							receiptDepotInfo,areaId,officeId,startTime,endTime,ooo);
			
			for (int i = 0; i < page.getList().size(); i++) {
				if (page.getList().get(i).getId().equals(KeyDepotId.RECEIPT_DEPOT_ID)) {
					List<ReceiptType> types = receiptTypeService.getAll();
					HashMap<Integer,Integer> beforeList = new HashMap<Integer, Integer>();
					HashMap<Integer,Integer> inList = new HashMap<Integer, Integer>();
					HashMap<Integer,Integer> outList = new HashMap<Integer, Integer>();
					HashMap<Integer,Integer> afterList = new HashMap<Integer, Integer>();
					for (int j = 0; j < types.size(); j++) {
						List<ReceiptEnterInfo> enterInfos =  receiptEnterInfoService.selectByDepotType(page.getList().get(i),types.get(j).getId());
						int beforeInTotal = 0;
						int afterInTotal = 0;
						int beforeOutTotal = 0;
						int afterOutTotal = 0;
						for (int k = 0; k < enterInfos.size(); k++) {
							if (enterInfos.get(k).getCreateDate().before(startTime)) {
								beforeInTotal +=  enterInfos.get(k).getCount();
							}else if(!enterInfos.get(k).getCreateDate().after(endTime)){
								afterInTotal +=  enterInfos.get(k).getCount();
							}
						}
						List<ReceiptInvoice> invoices = receiptInvoiceService.selectByDepotType(page.getList().get(i),types.get(j).getId());
						for (int k = 0; k < invoices.size(); k++) {
							if (invoices.get(k).getCreateDate().before(startTime)) {
								beforeOutTotal +=  invoices.get(k).getCount();
							}else if(!invoices.get(k).getCreateDate().after(endTime)){
								afterOutTotal +=  invoices.get(k).getCount();
							}
						}
						beforeList.put(types.get(j).getTypeName(), beforeInTotal-beforeOutTotal);
						inList.put(types.get(j).getTypeName(), afterInTotal);
						outList.put(types.get(j).getTypeName(), afterOutTotal);
						afterList.put(types.get(j).getTypeName(), beforeInTotal-beforeOutTotal+afterInTotal-afterOutTotal);
					}
					page.getList().get(i).setBeforeList(beforeList);
					page.getList().get(i).setInList(inList);
					page.getList().get(i).setOutList(outList);
					page.getList().get(i).setAfterList(afterList);
				}
				
			}
			
			
			
			
		}
		List<Office> offsList =  officeService.getOfficeByType(UserUtils.getUser(), 1);
		for (int i = 0; i < offsList.size();) {
			Office office = offsList.get(i);
			if (office.getType().equals("2")) {
				offsList.remove(i);
			}else {
				i++;
			}
		}
		if (areaId != null) {
			List<Office> offices = officeService.findByParentId(areaId);
			model.addAttribute("offices", offices);
		}
		
		List<ReceiptDepotInfo> receipts=receiptDepotInfoService.findAllDepot();
		model.addAttribute("receipts",receipts);
		model.addAttribute("page", page);
		model.addAttribute("officeId", officeId);
		model.addAttribute("areaId", areaId);
		model.addAttribute("receiptName", receiptName);
		model.addAttribute("offsList", offsList);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "modules/receipt/receiptDepotInfoListTJ";
	}

	@RequiresPermissions("receipt:receiptInvoice:view")
	@RequestMapping(value = "form")
	public String form(ReceiptInvoice receiptInvoice, Model model) {
		model.addAttribute("receiptInvoice", receiptInvoice);
		return "modules/receipt/receiptInvoiceForm";
	}

	@RequiresPermissions("receipt:receiptInvoice:edit")
	@RequestMapping(value = "save")
	public String save(ReceiptInvoice receiptInvoice, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receiptInvoice)) {
			return form(receiptInvoice, model);
		}
		receiptInvoiceService.save(receiptInvoice);
		// addMessage(redirectAttributes, "保存出库信息'" + receiptInvoice.getName() +
		// "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/receipt/receiptInvoice/?repage";
	}

	@RequiresPermissions("receipt:receiptInvoice:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		receiptInvoiceService.delete(id);
		addMessage(redirectAttributes, "删除出库信息成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/receipt/receiptInvoice/?repage";
	}


	/**
	 * 获取入库总金额
	 */
	private double getNumMoney(ReceiptDepotInfo receiptDepotInfo, Date staDate,
			Date endDate) {
		double numMoney = receiptEnterInfoService.getNumMoney(receiptDepotInfo,
				staDate, endDate);
		return numMoney;
	}

	/**
	 * 获取出库总金额
	 */
	private double getchuku(ReceiptDepotInfo receiptDepotInfo, Date staDate,
			Date endDate) {
		double money = receiptInvoiceService.findInvoiceByDepotInfo(staDate,
				endDate, receiptDepotInfo);
		return money;
	}
}
