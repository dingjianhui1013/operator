/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.itrus.ca.modules.bean.StaticMonth;
import com.itrus.ca.modules.bean.StaticProductMonth;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.statistic.entity.StatisticCertDataProduct;
import com.itrus.ca.modules.statistic.service.StatisticCertDataProductService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.mysql.jdbc.Util;

/**
 * 证书发放统计Controller
 * @author ZhangJingtao
 * @version 2014-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/statistic/statisticCertDataProduct")
public class StatisticCertDataProductController extends BaseController {

	@Autowired
	private StatisticCertDataProductService statisticCertDataProductService;
	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public StatisticCertDataProduct get(@RequestParam(required=false) Long id) {
		if (id != null){
			return statisticCertDataProductService.get(id);
		}else{
			return new StatisticCertDataProduct();
		}
	}
	
	@RequiresPermissions("statistic:statisticCertDataProduct:view")
	@RequestMapping(value = {"list", ""})
	public String list(StatisticCertDataProduct statisticCertDataProduct,Long areaId,
			Long officeId, String startDate, String endDate,
			HttpServletRequest request, HttpServletResponse response, Model model) {
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
		model.addAttribute("offsList", offsList);
		model.addAttribute("areaId", areaId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("officeId", officeId);
		
		if (startDate == null&& endDate == null) {
			return "modules/statistic/statisticCertDataProductListF";
		}
		Office office = null;
		if (officeId!=null) {
			office = officeService.get(officeId);
		}
		
		if (startDate==null||startDate.equals("")) {
			startDate = "2013-01";
		}
		if (endDate==null||endDate.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			endDate = df.format(new Date());
		}
		List<String> monthList = getMonthList(startDate+"-01", endDate+"-01");
		List<StaticProductMonth> sumList = new ArrayList<StaticProductMonth>();
		for (int i = 1; i < ProductType.productTypeMap.size()+1; i++) {
			StaticProductMonth month = new StaticProductMonth();
			month.setProductType(i);
			List<StaticMonth> smList = new ArrayList<StaticMonth>();
			for (String s : monthList) {
				StaticMonth sm = new StaticMonth();
				try {
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					Date start=sdf.parse(s);
					Calendar rightNow = Calendar.getInstance();
					rightNow.setTime(start);
					rightNow.add(Calendar.MONTH, 1);
					Date end = rightNow.getTime();
					List<StatisticCertDataProduct> list = statisticCertDataProductService.getSum(i, office, start, end);
					sm.setMonth(start);
					Integer oneSum = 0;
					Integer twoSum = 0;
					Integer fourSum = 0;
					Integer fiveSum = 0;
					
					for (StatisticCertDataProduct statisticCertDataProduct2 : list) {
						oneSum += statisticCertDataProduct2.getYear1();
						twoSum += statisticCertDataProduct2.getYear2();
						fourSum += statisticCertDataProduct2.getYear4();
						fiveSum += statisticCertDataProduct2.getYear5();
					}
					sm.setCount1(oneSum);
					sm.setCount2(twoSum);
					sm.setCount4(fourSum);
					sm.setCount5(fiveSum);
					if (i==1) {
						sm.setPro1Sum(oneSum+twoSum+fourSum+fiveSum);
					}
					if (i==2) {
						sm.setPro2Sum(oneSum+twoSum+fourSum+fiveSum);
					}
					if (i==3) {
						sm.setPro3Sum(oneSum+twoSum+fourSum+fiveSum);
					}
					if (i==4) {
						sm.setPro4Sum(oneSum+twoSum+fourSum+fiveSum);
					}
					if (i==5) {
						sm.setPro5Sum(oneSum+twoSum+fourSum+fiveSum);
					}
					if (i==6) {
						sm.setPro6Sum(oneSum+twoSum+fourSum+fiveSum);
					}
					smList.add(sm);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			month.setMonths(smList);
			sumList.add(month);
		}
		
		model.addAttribute("sumList", sumList);
		model.addAttribute("pro", ProductType.productTypeMap);
		model.addAttribute("monthList", getMoList(startDate+"-01", endDate+"-01"));
		
		return "modules/statistic/statisticCertDataProductListF";
	}

	@RequiresPermissions("statistic:statisticCertDataProduct:view")
	@RequestMapping(value = "form")
	public String form(StatisticCertDataProduct statisticCertDataProduct, Model model) {
		model.addAttribute("statisticCertDataProduct", statisticCertDataProduct);
		return "modules/statistic/statisticCertDataProductForm";
	}

	@RequiresPermissions("statistic:statisticCertDataProduct:edit")
	@RequestMapping(value = "save")
	public String save(StatisticCertDataProduct statisticCertDataProduct, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, statisticCertDataProduct)){
			return form(statisticCertDataProduct, model);
		}
		statisticCertDataProductService.save(statisticCertDataProduct);
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticCertDataProduct/?repage";
	}
	
	@RequiresPermissions("statistic:statisticCertDataProduct:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		statisticCertDataProductService.delete(id);
		addMessage(redirectAttributes, "删除证书发放统计成功");
		return "redirect:"+Global.getAdminPath()+"/modules/statistic/statisticCertDataProduct/?repage";
	}

	
	
	public List<String> getMonthList(String beginTime, String endTime) {  
        List<String> monthList = new ArrayList<String>();  
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd");  
        try {  
        	Date begin = monthFormat.parse(beginTime);
        	Date end = monthFormat.parse(endTime);
            int months = (end.getYear() - begin.getYear()) * 12  
                    + (end.getMonth() - begin.getMonth());  
              
            for (int i = 0; i <= months; i++) {  
                Calendar calendar = Calendar.getInstance();    
                calendar.setTime(begin);    
                calendar.add(Calendar.MONTH, i);  
                monthList.add(monthFormat.format(calendar.getTime()));  
            }  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return monthList;  
    }
	
	public List<String> getMoList(String beginTime, String endTime) {  
        List<String> monthList = new ArrayList<String>();  
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");  
        try {  
        	Date begin = monthFormat.parse(beginTime);
        	Date end = monthFormat.parse(endTime);
            int months = (end.getYear() - begin.getYear()) * 12  
                    + (end.getMonth() - begin.getMonth());  
              
            for (int i = 0; i <= months; i++) {  
                Calendar calendar = Calendar.getInstance();    
                calendar.setTime(begin);    
                calendar.add(Calendar.MONTH, i);  
                monthList.add(monthFormat.format(calendar.getTime()));  
            }  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return monthList;  
    }
}
