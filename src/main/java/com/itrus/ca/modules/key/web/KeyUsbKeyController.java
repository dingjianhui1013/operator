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

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;

/**
 * 入库管理Controller
 * 
 * @author HUHAO
 * @version 2014-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/key/keyUsbKey")
public class KeyUsbKeyController extends BaseController {

	@Autowired
	private KeyUsbKeyService keyUsbKeyService;

	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;

	@Autowired
	private KeyUsbKeyDepotService keyUsbKeyDepotService;

	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;

	@ModelAttribute
	public KeyUsbKey get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return keyUsbKeyService.get(id);
		} else {
			return new KeyUsbKey();
		}
	}

	@RequiresPermissions("key:keyUsbKey:view")
	@RequestMapping(value = { "list", "" })
	public String list(KeyUsbKey keyUsbKey, HttpServletRequest request,
			HttpServletResponse response, Model model, Long depotId) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			keyUsbKey.setCreateBy(user);
		}
		KeyUsbKeyDepot keyUsbKeyDepot = new KeyUsbKeyDepot();
		keyUsbKeyDepot.setId(depotId);
		keyUsbKey.setKeyUsbKeyDepot(keyUsbKeyDepot);
		Page<KeyUsbKey> page = keyUsbKeyService.find(new Page<KeyUsbKey>(
				request, response), keyUsbKey);
		if (page.getList().size() > 0) {
			for (int i = 0; i < page.getList().size(); i++) {
				int inReason = page.getList().get(i).getInReason();
				switch (inReason) {
				case 1:
					page.getList().get(i).setInReasonName("调拨入库");
					break;
				case 2:
					page.getList().get(i).setInReasonName("采购入库");
					break;
				case 3:
					page.getList().get(i).setInReasonName("维护品入库");
					break;
				case 4:
					page.getList().get(i).setInReasonName("坏key入库");
					break;
				case 5:
					page.getList().get(i).setInReasonName("盘点异常入库");
					break;
				case 6:
					page.getList().get(i).setInReasonName("退费入库");
					break;
				}
			}
		}
		KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);
		model.addAttribute("depot", depot);
		model.addAttribute("depotId", depotId);
		model.addAttribute("page", page);
		boolean flag = false;
		if (depot.getOffice().getId().equals(34L)) {//34L是总网点的ID
			model.addAttribute("addEnable", true);
		}else {
			model.addAttribute("addEnable", false);
		}
		return "modules/key/keyUsbKeyList";
	}

	@RequiresPermissions("key:keyUsbKey:edit")
	@RequestMapping(value = "form")
	public String form(KeyUsbKey keyUsbKey, Model model, Long depotId) {

		List<KeyGeneralInfo> generalInfos = keyGeneralInfoService.findAll();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		// String time = sdf.format(new Date());
		for (int i = 0; i < generalInfos.size(); i++) {
			String keyName = generalInfos.get(i).getName();
			String supplierName = generalInfos.get(i).getConfigSupplier().getSupplierName();
			String name = supplierName + "-" + keyName;
			generalInfos.get(i).setManuKeyName(name);
		}
		model.addAttribute("depotId", depotId);
		model.addAttribute("generalInfos", generalInfos);
		model.addAttribute("time", new Date());
		model.addAttribute("keyUsbKey", keyUsbKey);
		return "modules/key/keyUsbKeyForm";
	}

	@RequiresPermissions("key:keyUsbKey:edit")
	@RequestMapping(value = "save")
	public String save(KeyUsbKey keyUsbKey, Model model,
			RedirectAttributes redirectAttributes, String time,
			Long keyGeneralInfoId, Long depotId) throws ParseException {
		if (!beanValidator(model, keyUsbKey)) {
			return form(keyUsbKey, model, depotId);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d = (Date) sdf.parseObject(time);
		keyUsbKey.setCreateDate(d);
		KeyUsbKeyDepot keyUsbKeyDepot = new KeyUsbKeyDepot();
		keyUsbKeyDepot.setId(depotId);
		KeyGeneralInfo keyGeneralInfo = new KeyGeneralInfo();
		keyGeneralInfo.setId(keyGeneralInfoId);
		keyUsbKey.setKeyUsbKeyDepot(keyUsbKeyDepot);
		keyUsbKey.setKeyGeneralInfo(keyGeneralInfo);
		keyUsbKey.setStartDate(new Date());
		keyUsbKeyService.save(keyUsbKey);

		List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
				.findByDepotIdAndGeneId(depotId, keyGeneralInfoId);
		if (staList.size() > 0) {

			KeyDepotGeneralStatistics sta = staList.get(0);
			int total = sta.getTotalCount();
			int intotal = sta.getInCount();
			sta.setTotalCount(total + keyUsbKey.getCount());
			sta.setInCount(intotal + keyUsbKey.getCount());
			keyDepotGeneralStatisticsService.save(sta);

		} else {
			KeyDepotGeneralStatistics statistic = new KeyDepotGeneralStatistics();
			statistic.setKeyUsbKeyDepot(keyUsbKeyDepot);
			statistic.setKeyGeneralInfo(keyGeneralInfo);
			statistic.setTotalCount(keyUsbKey.getCount());
			statistic.setInCount(keyUsbKey.getCount());
			statistic.setOutCount(0);
			keyDepotGeneralStatisticsService.save(statistic);
		}
		addMessage(redirectAttributes, "保存入库信息成功");
		return "redirect:" + Global.getAdminPath()
				+ "/key/keyUsbKey/list?repage&depotId=" + depotId;
	}
	
	@RequiresPermissions("key:keyUsbKey:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		keyUsbKeyService.delete(id);
		addMessage(redirectAttributes, "删除入库管理成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/key/keyUsbKey/?repage";
	}

	@RequestMapping(value = "depotCountInList")
	public String depotCountInList(KeyUsbKey keyUsbKey,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "keyId", required = false) Long keyId,
			@RequestParam(value = "supplierId", required = false) Long supplierId,
			Model model, Long depotId) throws ParseException {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			keyUsbKey.setCreateBy(user);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//兼容统计时间只输入一项
		Date start = null;
		Date end = null;
		if(!"00:00:00".equals(startTime)){
			start = (Date) sdf.parseObject(startTime);
		}
		if(!"23:59:59".equals(endTime)){
			end  = (Date) sdf.parseObject(endTime);
		}
		KeyUsbKeyDepot keyUsbKeyDepot = new KeyUsbKeyDepot();
		keyUsbKeyDepot.setId(depotId);
		keyUsbKey.setKeyUsbKeyDepot(keyUsbKeyDepot);
		Page<KeyUsbKey> page = keyUsbKeyService.findByStartEnd(new Page<KeyUsbKey>(
				request, response), keyUsbKey,start,end,supplierId,keyId);
		if (page.getList().size() > 0) {
			for (int i = 0; i < page.getList().size(); i++) {
				int inReason = page.getList().get(i).getInReason();
				switch (inReason) {
				case 1:
					page.getList().get(i).setInReasonName("调拨入库");
					break;
				case 2:
					page.getList().get(i).setInReasonName("采购入库");
					break;
				case 3:
					page.getList().get(i).setInReasonName("维护品入库");
					break;
				case 4:
					page.getList().get(i).setInReasonName("坏key入库");
					break;
				case 5:
					page.getList().get(i).setInReasonName("盘点异常入库");
					break;
				case 6:
					page.getList().get(i).setInReasonName("退费入库");
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
		return "modules/key/keyUsbKeyDepotCountInList";
	}

}
