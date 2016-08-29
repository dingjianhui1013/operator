/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;
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

import cn.emay.sdk.common.DateUtil;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.web.KeyUsbKeyController;
import com.itrus.ca.modules.profile.entity.ConfigChargeSupplierDetail;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.service.ConfigChargeSupplierDetailService;
import com.itrus.ca.modules.profile.service.ConfigSupplierProductRelationService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.settle.entity.KeyPurchase;
import com.itrus.ca.modules.settle.service.KeyPurchaseService;

import freemarker.template.SimpleDate;

/**
 * key采购记录Controller
 * @author DingJianHui
 * @version 2015-11-09
 */


@Controller
@RequestMapping(value = "${adminPath}/settle/keyPurchase")
public class KeyPurchaseController extends BaseController {
	static Log log = LogFactory.getLog(KeyPurchaseController.class);
	@Autowired
	private KeyPurchaseService keyPurchaseService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	@Autowired
	private ConfigChargeSupplierDetailService configChargeSupplierDetailService;
	
	@Autowired
	private ConfigSupplierProductRelationService configSupplierProductRelationService;
	@ModelAttribute
	public KeyPurchase get(@RequestParam(required=false) Long id) {
		if (id != null){
			return keyPurchaseService.get(id);
		}else{
			return new KeyPurchase();
		}
	}
	
	@RequiresPermissions("settle:keyPurchase:view")
	@RequestMapping(value = {"list", ""})
	public String list(KeyPurchase keyPurchase, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			keyPurchase.setCreateBy(user);
		}
        Page<KeyPurchase> page = keyPurchaseService.find(new Page<KeyPurchase>(request, response), keyPurchase); 
        model.addAttribute("page", page);
        
        List<KeyGeneralInfo> generalInfos = keyGeneralInfoService.findAll();
		for (int i = 0; i < generalInfos.size(); i++) {
			String keyName = generalInfos.get(i).getName();
			String supplierName = generalInfos.get(i).getConfigSupplier().getSupplierName();
			String name = supplierName + "-" + keyName;
			generalInfos.get(i).setManuKeyName(name);
		}
		model.addAttribute("storageDate", keyPurchase.getStorageDate() == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(keyPurchase.getStorageDate()));
		model.addAttribute("generalInfos", generalInfos);
		return "modules/settle/settleKeyPurchaseList";
	}

	@RequiresPermissions("settle:keyPurchase:view")
	@RequestMapping(value = "form")
	public String form(KeyPurchase keyPurchase, Model model) {
		model.addAttribute("keyPurchase", keyPurchase);
		List<KeyGeneralInfo> generalInfos = keyGeneralInfoService.findAll();
		for (int i = 0; i < generalInfos.size(); i++) {
			String keyName = generalInfos.get(i).getName();
			String supplierName = generalInfos.get(i).getConfigSupplier().getSupplierName();
			String name = supplierName + "-" + keyName;
			generalInfos.get(i).setManuKeyName(name);
		}
		model.addAttribute("generalInfos", generalInfos);
		return "modules/settle/settleKeyInput";
	}

	@RequiresPermissions("settle:keyPurchase:edit")
	@RequestMapping(value = "save")
	public String save(KeyPurchase keyPurchase, Model model, RedirectAttributes redirectAttributes,Double money) {
		if (!beanValidator(model, keyPurchase)){
			return form(keyPurchase, model);
		}
		if(money!=null && !"".equals(money))
		{
			keyPurchase.setMoney(money);
			
		}
		if(keyPurchase.getAppName()!=null && !"".equals(keyPurchase.getAppName()))
		{
			KeyGeneralInfo kg= keyGeneralInfoService.findById(Long.parseLong(keyPurchase.getAppName()));
			String keyName =kg.getName();
			String supplierName = kg.getConfigSupplier().getSupplierName();
			String name = supplierName +"-"+keyName; 
			keyPurchase.setAppName(name);
		}
		keyPurchaseService.save(keyPurchase);
		addMessage(redirectAttributes, "保存key采购记录'" + keyPurchase.getAppName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/settle/keyPurchase/?repage";
	}
	
	@RequiresPermissions("settle:keyPurchase:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		keyPurchaseService.delete(id);
		addMessage(redirectAttributes, "删除key采购记录成功");
		return "redirect:"+Global.getAdminPath()+"/settle/keyPurchase/?repage";
	}
	
	@RequestMapping(value = "changeKey")
	@ResponseBody
	public String change(Integer moneyType) throws JSONException
	{
		JSONObject json=new JSONObject();
		List<ConfigSupplierProductRelation> list= configSupplierProductRelationService.findByGenanID((long)moneyType);
		if(!list.isEmpty())
		{
			List<ConfigChargeSupplierDetail>  ccsd=configChargeSupplierDetailService.findByconfigSupplierId(list.get(0).getId());
			for (ConfigChargeSupplierDetail ccsds : ccsd) {
				json.put("money", ccsds.getMoney());
			}
		}
		KeyGeneralInfo kg= keyGeneralInfoService.findById((long)moneyType);
		String keyName =kg.getName();
		String supplierName = kg.getConfigSupplier().getSupplierName();
		String name = supplierName +"-"+keyName; 
		List<KeyPurchase> startCode=keyPurchaseService.findByKeyName(name);
		if(startCode.size()>0)
		{
			Long endcode=startCode.get(0).getEndCode();
			json.put("endcode", endcode);
		}else
		{
			json.put("endcode", 0);
		}
		return json.toString();
	}
	@RequestMapping(value = "export")
	public void export(HttpServletRequest request,
			HttpServletResponse response,String appName,Integer status,Date storageDate)
	{
		try {
			HSSFWorkbook wb=new HSSFWorkbook();
			HSSFSheet sheet=wb.createSheet("key采购记录");
			HSSFCellStyle style=wb.createCellStyle();
			HSSFFont font=wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short)18);
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			sheet.addMergedRegion(new Region(0, (short)0, 0, (short)7));
			HSSFRow   row=sheet.createRow(0);
			row.setHeightInPoints((short)20);
			HSSFCell cell=row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("key采购记录");
			HSSFRow   row1=sheet.createRow(1);
			row1.createCell(0).setCellValue("产品名称");;
			row1.createCell(1).setCellValue("入库时间");
			row1.createCell(2).setCellValue("key起始码");
			row1.createCell(3).setCellValue("key截止码");
			row1.createCell(4).setCellValue("数量");
			row1.createCell(5).setCellValue("单价");
			row1.createCell(6).setCellValue("状态");
			row1.createCell(7).setCellValue("备注");
			KeyPurchase keyPurchase=new KeyPurchase();
			keyPurchase.setAppName(appName);
			keyPurchase.setStorageDate(storageDate);
			keyPurchase.setStatus(status);
			List<KeyPurchase> list = keyPurchaseService.find(keyPurchase);
			for(int i=0;i<list.size();i++)
			{
				HSSFRow rown=sheet.createRow(i+2);
				rown.createCell(0).setCellValue(list.get(i).getAppName());
				rown.createCell(1).setCellValue(DateUtils.formatDate(list.get(i).getStorageDate(), "yyyy-MM-dd") );
				rown.createCell(2).setCellValue(list.get(i).getStartCode()+"");
				rown.createCell(3).setCellValue(list.get(i).getEndCode()+"");
				rown.createCell(4).setCellValue(list.get(i).getCount());
				if(list.get(i).getMoney()==null)
				{
					rown.createCell(5).setCellValue("");
				}else
				{
					rown.createCell(5).setCellValue(list.get(i).getMoney());
				}
				rown.createCell(6).setCellValue(list.get(i).getStatus());
				rown.createCell(7).setCellValue(list.get(i).getRemarks());
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=keyPurchaseRecord.xls");
			wb.write(baos);
			byte[] bytes = baos.toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(bytes);
			bos.close();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequiresPermissions("settle:keyPurchase:edit")
	@RequestMapping(value = "updateStatus")
	public String updateStatus(Long keyID, String remarks,RedirectAttributes redirectAttributes) {
		KeyPurchase kp= keyPurchaseService.findById(keyID);
		kp.setStatus(1);
		kp.setRemarks(remarks);
		log.debug(remarks);
		keyPurchaseService.save(kp);
		addMessage(redirectAttributes, "修改key采购记录成功");
		return "redirect:"+Global.getAdminPath()+"/settle/keyPurchase/?repage";
	}
}
