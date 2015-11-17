/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.Region;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyManufacturerService;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.settle.entity.KeyPurchase;
import com.itrus.ca.modules.settle.entity.KeySettle;
import com.itrus.ca.modules.settle.entity.SettleKey;
import com.itrus.ca.modules.settle.service.KeyPurchaseService;
import com.itrus.ca.modules.settle.service.KeySettleService;
import com.itrus.ca.modules.settle.service.SettleKeyService;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * key结算功能Controller
 * @author qt
 * @version 2015-11-12
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/keySettle")
public class KeySettleController extends BaseController {

	@Autowired
	private KeySettleService keySettleService;
	
	@Autowired
	private KeyManufacturerService keyManufacturerService;
	
	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;
	
	@Autowired
	private SettleKeyService settleKeyService;
	
	@Autowired
	private ConfigSupplierService configSupplierService;
	
	@Autowired
	private KeyPurchaseService keyPurchaseService;
	
	@Value(value = "${deploy.path}")
	public String deployPath;
	
	@ModelAttribute
	public KeySettle get(@RequestParam(required=false) Long id) {
		if (id != null){
			return keySettleService.get(id);
		}else{
			return new KeySettle();
		}
	}
	
	@RequiresPermissions("settle:keySettle:view")
	@RequestMapping(value = {"list", ""})
	public String list(KeySettle keySettle, KeyPurchase keypurchase,
			@RequestParam(value = "supplierId", required = false) Long supplierId,
			@RequestParam(value = "keyId", required = false) Long keyId,
//			@RequestParam(value = "purchaseId", required = false) Long purchaseId,
			@RequestParam(value = "KeySubtotal", required = false) Double keySubtotal,
			@RequestParam(value = "KeyTotalQuantity", required = false) Long keyTotalQuantity,
			@RequestParam(value = "KeyTotalAmount", required = false) Double keyTotalAmount,
			HttpServletRequest request, HttpServletResponse response, Model model)
			 {
//		 Page<KeySettle> page = keySettleService.find(new Page<KeySettle>(request, response), keySettle, supplierId, keyId,purchaseId, keySubtotal, keyTotalQuantity, keyTotalAmount);
//		 model.addAttribute("page", page);
		 User user = UserUtils.getUser();
			if (!user.isAdmin()){
				keySettle.setCreateBy(user);
			}
			List<ConfigSupplier> suppliers = configSupplierService.findByKey();
			model.addAttribute("configSupplierId", supplierId);
	    	model.addAttribute("keyId", keyId);
			model.addAttribute("suppliers",suppliers);
			if (supplierId!=null) {
				 List<KeyGeneralInfo>  keys = keyGeneralInfoService.findBySupplierId(supplierId);
			        model.addAttribute("keys", keys);
			}
//	    	List<KeyPurchase> keyPurchase =keyPurchaseService.findByKey();
//	    	System.out.println(keyPurchase);
//	    	model.addAttribute("keyPurchase", keyPurchase);
			Page<KeyPurchase> page=keyPurchaseService.findByKey(new Page<KeyPurchase>(request, response), keypurchase);
			model.addAttribute("page", page);
			model.addAttribute("zs", page.getList().size());
		 return "modules/settle/keySettleList";
	}
	@RequestMapping(value = "export")
	public void export(HttpServletRequest request,
			HttpServletResponse response,Long supplierId,Long keyId,Date startTime,Date endTime)
	{
		System.out.println("lailemei ");
		try {
			HSSFWorkbook wb=new HSSFWorkbook();
			HSSFSheet sheet=wb.createSheet("key结算统计");
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
			cell.setCellValue("key结算统计");
			HSSFRow   row1=sheet.createRow(1);
			row1.createCell(0).setCellValue("入库时间");;
			row1.createCell(1).setCellValue("产品名称");
			row1.createCell(2).setCellValue("key起始码");
			row1.createCell(3).setCellValue("key截止码");
			row1.createCell(4).setCellValue("数量");
			row1.createCell(5).setCellValue("单价");
			row1.createCell(6).setCellValue("小计/元");
			row1.createCell(7).setCellValue("状态");
			row1.createCell(8).setCellValue("备注");
			KeyPurchase keyPurchase=new KeyPurchase();
			
			List<KeyPurchase> list = keyPurchaseService.find(keyPurchase);
			int last=0;
			int zj=0;
			for(int i=0;i<list.size();i++)
			{
				HSSFRow rown=sheet.createRow(i+2);
				rown.createCell(0).setCellValue(DateUtils.formatDate(list.get(i).getStorageDate(), "yyyy-MM-dd") );
				rown.createCell(1).setCellValue(list.get(i).getAppName());
				rown.createCell(2).setCellValue(list.get(i).getStartCode()+"");
				rown.createCell(3).setCellValue(list.get(i).getEndCode()+"");
				rown.createCell(4).setCellValue(list.get(i).getCount());
				rown.createCell(5).setCellValue(list.get(i).getMoney());
				rown.createCell(6).setCellValue(list.get(i).getCount()*list.get(i).getMoney());
				rown.createCell(7).setCellValue(list.get(i).getStatus());
				rown.createCell(8).setCellValue(list.get(i).getRemarks());
				last = i+2;
				zj+=(list.get(i).getCount()*list.get(i).getMoney());
			}
			HSSFRow  cell1=sheet.createRow(last+1);
			HSSFCell cell2=cell1.createCell(7);
			cell2.setCellValue("总计："+list.size()+"个"+"总金额："+zj+"元");
			
			
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
	

	@RequiresPermissions("settle:keySettle:view")
	@RequestMapping(value = "form")
	public String form(KeySettle keySettle, Model model) {
		model.addAttribute("keySettle", keySettle);
		return "modules/settle/keySettleForm";
	}

	@RequiresPermissions("settle:keySettle:edit")
	@RequestMapping(value = "save")
	public String save(KeySettle keySettle, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, keySettle)){
			return form(keySettle, model);
		}
		return "redirect:"+Global.getAdminPath()+"/modules/settle/keySettle/?repage";
	}
	
	@RequiresPermissions("settle:keySettle:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		keySettleService.delete(id);
		addMessage(redirectAttributes, "删除key结算功能成功");
		return "redirect:"+Global.getAdminPath()+"/modules/settle/keySettle/?repage";
	}

}
