/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;

/**
 * 出库管理Controller
 * 
 * @author HUHAO
 * @version 2014-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/key/keyUsbKeyInvoice")
public class KeyUsbKeyInvoiceController extends BaseController {

	@Autowired
	private KeyUsbKeyInvoiceService keyUsbKeyInvoiceService;

	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;

	@Autowired
	private KeyUsbKeyDepotService keyUsbKeyDepotService;

	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;

	@ModelAttribute
	public KeyUsbKeyInvoice get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return keyUsbKeyInvoiceService.get(id);
		} else {
			return new KeyUsbKeyInvoice();
		}
	}

	@RequiresPermissions("key:keyUsbKeyInvoice:view")
	@RequestMapping(value = { "list", "" })
	public String list(KeyUsbKeyInvoice keyUsbKeyInvoice,
			HttpServletRequest request, HttpServletResponse response,
			Model model, Long depotId) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			keyUsbKeyInvoice.setCreateBy(user);
		}
		KeyUsbKeyDepot keyUsbKeyDepot = new KeyUsbKeyDepot();
		keyUsbKeyDepot.setId(depotId);
		keyUsbKeyInvoice.setKeyUsbKeyDepot(keyUsbKeyDepot);
		Page<KeyUsbKeyInvoice> page = keyUsbKeyInvoiceService
				.find(new Page<KeyUsbKeyInvoice>(request, response),
						keyUsbKeyInvoice);
		
		List<KeyUsbKeyInvoice> keyInvoices = keyUsbKeyInvoiceService.find(keyUsbKeyInvoice);
		
		if (page.getList().size() > 0) {
			for (int i = 0; i < page.getList().size(); i++) {
				int inReason = page.getList().get(i).getOutReason();
				switch (inReason) {
				case 1:
					page.getList().get(i).setOutReasonName("维修品出库");
					break;
				case 2:
					page.getList().get(i).setOutReasonName("调拨出库");
					break;
				case 3:
					page.getList().get(i).setOutReasonName("坏key出库");
					break;
				case 4:
					page.getList().get(i).setOutReasonName("销售出库");
					break;
				case 5:
					page.getList().get(i).setOutReasonName("盘点异常出库");
					break;
				case 6:
					page.getList().get(i).setOutReasonName("其他");
					break;
				}
			}
		}
		Integer count=0;
		int dayCount=0;
		for(int i=0;i<keyInvoices.size();i++)
		{
			count+=((int)keyInvoices.get(i).getDeliveryNum());
			String oldDate=new SimpleDateFormat("yyyy-MM-dd").format(keyInvoices.get(i).getStartDate());
			String newDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
			if(oldDate.indexOf(newDate)>-1)
			{
				dayCount+=keyInvoices.get(i).getDeliveryNum();
			}
			
		}
		model.addAttribute("dayCount",dayCount);
		model.addAttribute("count",count);
		KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);
		model.addAttribute("depot", depot);
		model.addAttribute("depotId", depotId);
		model.addAttribute("page", page);
		return "modules/key/keyUsbKeyInvoiceList";
	}

	@RequestMapping(value = "depotCountOutList")
	public String depotCountOutList(KeyUsbKeyInvoice keyUsbKeyInvoice,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "keyId", required = false) Long keyId,
			@RequestParam(value = "supplierId", required = false) Long supplierId,
			Model model, Long depotId) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date start = (Date) sdf.parseObject(startTime);
		Date end  = (Date) sdf.parseObject(endTime);
		KeyUsbKeyDepot keyUsbKeyDepot = new KeyUsbKeyDepot();
		keyUsbKeyDepot.setId(depotId);
		keyUsbKeyInvoice.setKeyUsbKeyDepot(keyUsbKeyDepot);
		Page<KeyUsbKeyInvoice> page = keyUsbKeyInvoiceService
				.findStartEnd(new Page<KeyUsbKeyInvoice>(request, response),
						keyUsbKeyInvoice,start,end,supplierId,keyId);
		if (page.getList().size() > 0) {
			for (int i = 0; i < page.getList().size(); i++) {
				int inReason = page.getList().get(i).getOutReason();
				switch (inReason) {
				case 1:
					page.getList().get(i).setOutReasonName("维修出库");
					break;
				case 2:
					page.getList().get(i).setOutReasonName("调拨出库");
					break;
				case 3:
					page.getList().get(i).setOutReasonName("坏key出库");
					break;
				case 4:
					page.getList().get(i).setOutReasonName("销售出库");
					break;
				case 5:
					page.getList().get(i).setOutReasonName("盘点异常出库");
					break;
				case 6:
					page.getList().get(i).setOutReasonName("其他");
					break;
				}
			}
		}
		model.addAttribute("startTime",startTime );
		model.addAttribute("endTime", endTime);
		model.addAttribute("keyId", keyId);
		model.addAttribute("supplierId", supplierId);
		model.addAttribute("depotId", depotId);
		model.addAttribute("page", page);
		return "modules/key/keyUsbKeyDepotCountOutList";
	}

	@RequiresPermissions("key:keyUsbKeyInvoice:view")
	@RequestMapping(value = "form")
	public String form(KeyUsbKeyInvoice keyUsbKeyInvoice, Model model,
			Long depotId) {

		
		List<KeyDepotGeneralStatistics> statistics = keyDepotGeneralStatisticsService.findByDepotId(depotId);
		List<KeyGeneralInfo> generalInfos = Lists.newArrayList();
		if (statistics.size()>0) {
			for (int i = 0; i < statistics.size(); i++) {
				generalInfos.add(statistics.get(i).getKeyGeneralInfo());
			}
			for (int i = 0; i < generalInfos.size(); i++) {
				String keyName = generalInfos.get(i).getName();
				String manuName = generalInfos.get(i).getConfigSupplier().getSupplierName();
				String name = manuName + "-" + keyName;
				generalInfos.get(i).setManuKeyName(name);
			}
			
		}
		model.addAttribute("genesSize",generalInfos.size() );
		KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);
		model.addAttribute("depot", depot);
		model.addAttribute("depotId", depotId);
		model.addAttribute("generalInfos", generalInfos);
		model.addAttribute("time", new Date());
		model.addAttribute("keyUsbKeyInvoice", keyUsbKeyInvoice);
		return "modules/key/keyUsbKeyInvoiceForm";
	}

	@RequiresPermissions("key:keyUsbKeyInvoice:edit")
	@RequestMapping(value = "save")
	public String save(KeyUsbKeyInvoice keyUsbKeyInvoice, Model model,
			RedirectAttributes redirectAttributes, String time,
			Long keyGeneralInfoId, Long depotId) throws ParseException {
		if (!beanValidator(model, keyUsbKeyInvoice)) {
			return form(keyUsbKeyInvoice, model, depotId);
		}
		
	
		List<KeyDepotGeneralStatistics> statistics = keyDepotGeneralStatisticsService.findByDepotIdAndGeneId(depotId, keyGeneralInfoId);
		if (statistics.get(0).getInCount()<keyUsbKeyInvoice.getDeliveryNum()) {
			List<KeyDepotGeneralStatistics> statics = keyDepotGeneralStatisticsService.findByDepotId(depotId);
			List<KeyGeneralInfo> generalInfos = Lists.newArrayList();
			if (statics.size()>0) {
				for (int i = 0; i < statics.size(); i++) {
					generalInfos.add(statics.get(i).getKeyGeneralInfo());
				}
				for (int i = 0; i < generalInfos.size(); i++) {
					String keyName = generalInfos.get(i).getName();
					String manuName = generalInfos.get(i).getConfigSupplier().getSupplierName();
					String name = manuName + "-" + keyName;
					generalInfos.get(i).setManuKeyName(name);
				}
				
			}
			model.addAttribute("genesSize",generalInfos.size() );
			KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);
			model.addAttribute("depot", depot);
			model.addAttribute("depotId", depotId);
			model.addAttribute("generalInfos", generalInfos);
			model.addAttribute("time", new Date());
			model.addAttribute("keyGeneralInfoId", keyGeneralInfoId);
			model.addAttribute("keyUsbKeyInvoice", keyUsbKeyInvoice);
			model.addAttribute("message","您录入的出库数量大于库存中的余量，请检查！" );
			return "modules/key/keyUsbKeyInvoiceForm";
		
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d = (Date) sdf.parseObject(time);
		keyUsbKeyInvoice.setCreateDate(d);
		KeyUsbKeyDepot keyUsbKeyDepot = new KeyUsbKeyDepot();
		keyUsbKeyDepot.setId(depotId);
		KeyGeneralInfo keyGeneralInfo = new KeyGeneralInfo();
		keyGeneralInfo.setId(keyGeneralInfoId);
		keyUsbKeyInvoice.setKeyUsbKeyDepot(keyUsbKeyDepot);
		keyUsbKeyInvoice.setKeyGeneralInfo(keyGeneralInfo);
		keyUsbKeyInvoice.setStartDate(new Date());
		keyUsbKeyInvoiceService.save(keyUsbKeyInvoice);

		List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
				.findByDepotIdAndGeneId(depotId, keyGeneralInfoId);
		if (staList.size() > 0) {
			KeyDepotGeneralStatistics sta = staList.get(0);
			int outtotal = sta.getOutCount();
			int intotal = sta.getInCount();
			sta.setOutCount(outtotal + keyUsbKeyInvoice.getDeliveryNum());
			sta.setInCount(intotal - keyUsbKeyInvoice.getDeliveryNum());
			keyDepotGeneralStatisticsService.save(sta);
		}
		addMessage(redirectAttributes, "保存出库信息成功");
		return "redirect:" + Global.getAdminPath()
				+ "/key/keyUsbKeyInvoice/list?repage&depotId=" + depotId;
	}

	@RequiresPermissions("key:keyUsbKeyInvoice:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		keyUsbKeyInvoiceService.delete(id);
		addMessage(redirectAttributes, "删除出库管理成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/key/keyUsbKeyInvoice/?repage";
	}

}
