/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.web;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.utils.RaAccountUtil;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.key.entity.KeyAllocateApply;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentBoundConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentBoundConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.profile.service.ConfigRaAccountExtendInfoService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 ************************************************************** 
 ** 
 ** @author huhao
 ** 
 **         2016年1月13日21:59:12
 ** 
 ** @describe:业务办理controller
 ** 
 ************************************************************** 
 */
@Controller
@RequestMapping(value = "${adminPath}/work/workDealInfoSed")
public class WorkDealInfoSedController extends BaseController {

	@Autowired
	private ConfigProductService configProductService;
	@Autowired
	private ConfigChargeAgentBoundConfigProductService configChargeAgentBoundConfigProductService;
	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;
	@Autowired
	private ConfigRaAccountExtendInfoService configRaAccountExtendInfoService;
	@Autowired
	private WorkDealInfoService workDealInfoService;
	

	@RequestMapping(value = "showAgentProduct")
	@ResponseBody
	public String showAgentProduct(Long app, String productName, Integer lable, Integer infoType) {
		JSONObject json = new JSONObject();
		try {
			ConfigProduct configProduct = configProductService.findByIdOrLable(app, productName, lable);

			JSONArray array = new JSONArray();

			List<ConfigChargeAgentBoundConfigProduct> boundList = configChargeAgentBoundConfigProductService
					.findByProIdAll(configProduct.getId());

			Set<String> nameSet = new HashSet<String>();
			for (int i = 0; i < boundList.size(); i++) {
				nameSet.add(boundList.get(i).getAgent().getTempStyle());
			}
			if (nameSet.size() > 0) {
				json.put("tempStyle", 1);
				Iterator<String> itName = nameSet.iterator();
				while (itName.hasNext()) {
					JSONObject iter = new JSONObject();
					String str = itName.next();
					if (str.endsWith("1")) {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "标准");
						array.put(iter);
					} else if (str.endsWith("2")) {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "政府统一采购");
						array.put(iter);
					} else {
						iter = new JSONObject();
						iter.put("id", str);
						iter.put("name", "合同采购");
						array.put(iter);
					}
				}

				JSONObject jsonMap = (JSONObject) array.get(0);
				List<ConfigChargeAgentBoundConfigProduct> boundStyleList = configChargeAgentBoundConfigProductService
						.findByProIdAllByStyle(configProduct.getId(), jsonMap.get("id").toString());

				JSONArray iterList2 = new JSONArray();

				for (int i = 0; i < boundStyleList.size(); i++) {
					JSONObject iter2 = new JSONObject();
					iter2.put("id", boundStyleList.get(i).getId());
					iter2.put("name", boundStyleList.get(i).getAgent().getTempName());
					iterList2.put(iter2);
				}

				json.put("boundStyleList", iterList2);

				json.put("typeMap", array);

			} else {

				json.put("tempStyle", -1);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}

	@RequestMapping(value = "showYearNew")
	@ResponseBody
	public String showYearNew(Long boundId, Integer infoType) {
		JSONObject json = new JSONObject();
		try {

			ConfigChargeAgentBoundConfigProduct bound = configChargeAgentBoundConfigProductService.get(boundId);

			// ConfigProduct configProduct =
			// configProductService.findByIdOrLable(
			// app, productName, lable);
			if (infoType != null) {
				String[] years = configChargeAgentDetailService.getChargeAgentYears(bound.getAgent().getId(), infoType);
				for (int i = 0; i < years.length; i++) {
					switch (years[i]) {
					case "1":
						json.put("year1", true);
						break;
					case "2":
						json.put("year2", true);
						break;
					case "4":
						json.put("year4", true);
						break;
					case "5":
						json.put("year5", true);
						break;
					}
				}
			}
			ConfigRaAccountExtendInfo configRaAccountExtendInfo = configRaAccountExtendInfoService
					.get(bound.getProduct().getRaAccountExtedId());
			RaAccountUtil.getExtendInfo(json, configRaAccountExtendInfo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return json.toString();
	}

	@RequestMapping(value = "showDealInfoAddMany")
	public String showDealInfoAddMany(HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("proList", ProductType.getProductTypeList());
		return "modules/work/workDealInfoAddMany";
	}

	// 批量导入
	@RequestMapping("addAttach")
	@ResponseBody
	public String importFile(
			@RequestParam(value = "appId", required = true) Long appId,
			@RequestParam(value = "fileName", required = true) MultipartFile file)
			throws IllegalStateException, IOException, JSONException {
		
		
		JSONObject json = new JSONObject();
		String ifExcel = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));// 截取.xlsx或.xls
		if (!(ifExcel.equals(".xls") || ifExcel.equals(".xlsx"))) {
			json.put("status", -1);
			json.put("msg", "模板必须为Excel文件");
			return json.toString();
		}
		try {
			json = workDealInfoService.saveExcelDateSed(file, ifExcel);// 解析存储excel文件
		} catch (Exception ex) {
			json.put("status", -1);
			json.put("msg", json.toString());
			return json.toString();
		}
		return json.toString();
	}
	
	
	
	

}
