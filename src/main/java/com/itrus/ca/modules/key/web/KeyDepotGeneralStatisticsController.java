/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.web;

import java.util.List;

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
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.key.entity.KeyAllocateApply;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;

/**
 * 库存统计Controller
 * 
 * @author HUHAO
 * @version 2014-06-30
 */
@Controller
@RequestMapping(value = "${adminPath}/key/keyDepotGeneralStatistics")
public class KeyDepotGeneralStatisticsController extends BaseController {

	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;

	@ModelAttribute
	public KeyDepotGeneralStatistics get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return keyDepotGeneralStatisticsService.get(id);
		} else {
			return new KeyDepotGeneralStatistics();
		}
	}

	@RequiresPermissions("key:keyDepotGeneralStatistics:view")
	@RequestMapping(value = { "list", "" })
	public String list(KeyDepotGeneralStatistics keyDepotGeneralStatistics,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			// keyDepotGeneralStatistics.setCreateBy(user);
		}
		Page<KeyDepotGeneralStatistics> page = keyDepotGeneralStatisticsService
				.find(new Page<KeyDepotGeneralStatistics>(request, response),
						keyDepotGeneralStatistics);
		model.addAttribute("page", page);
		return "modules/key/keyDepotGeneralStatisticsList";
	}

	@RequestMapping(value = "alarmForm")
	public String alarmForm(
			KeyDepotGeneralStatistics keyDepotGeneralStatistics,
			HttpServletRequest request, HttpServletResponse response,
			Model model, Long depotId) {
		List<KeyDepotGeneralStatistics> page = keyDepotGeneralStatisticsService
				.findByDepotId(depotId);
		model.addAttribute("depotId", depotId);
		model.addAttribute("page", page);
		return "modules/key/alarmValueForm";
	}
	
	
	@RequestMapping(value = "saveAlarm")
	@ResponseBody
	public String badKeyInSave(KeyDepotGeneralStatistics keyDepotGeneralStatistics,RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response,
			Model model, Long depotId,String alarm) throws JSONException {
		if (!beanValidator(model, keyDepotGeneralStatistics)){
			return form(keyDepotGeneralStatistics, model);
		}
		JSONObject json = new JSONObject();
		try {
			List<KeyDepotGeneralStatistics> page = keyDepotGeneralStatisticsService
					.findByDepotId(depotId);
//			String alarm[] = spli
			if (page.size()>0) {
				String[] alar= alarm.split(",");
				
				for (int i = 0; i < page.size(); i++) {
					if (!alar[i].equals("")) {
						page.get(i).setAlarmValue(Integer.parseInt(alar[i]));
					}
				}
			}
			keyDepotGeneralStatisticsService.saveList(page);
			addMessage(redirectAttributes, "保存成功！");
			json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "保存失败！");
			json.put("status", "0");
		}
		return json.toString();
	}
	
	
	
	
	
	
	
	

	@RequiresPermissions("key:keyDepotGeneralStatistics:view")
	@RequestMapping(value = "form")
	public String form(KeyDepotGeneralStatistics keyDepotGeneralStatistics,
			Model model) {
		model.addAttribute("keyDepotGeneralStatistics",
				keyDepotGeneralStatistics);
		return "modules/key/keyDepotGeneralStatisticsForm";
	}

	@RequiresPermissions("key:keyDepotGeneralStatistics:edit")
	@RequestMapping(value = "save")
	public String save(KeyDepotGeneralStatistics keyDepotGeneralStatistics,
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, keyDepotGeneralStatistics)) {
			return form(keyDepotGeneralStatistics, model);
		}
		keyDepotGeneralStatisticsService.save(keyDepotGeneralStatistics);
		// addMessage(redirectAttributes, "保存库存统计'" +
		// keyDepotGeneralStatistics.getName() + "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/key/keyDepotGeneralStatistics/?repage";
	}

	@RequiresPermissions("key:keyDepotGeneralStatistics:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		keyDepotGeneralStatisticsService.delete(id);
		addMessage(redirectAttributes, "删除库存统计成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/key/keyDepotGeneralStatistics/?repage";
	}

}
