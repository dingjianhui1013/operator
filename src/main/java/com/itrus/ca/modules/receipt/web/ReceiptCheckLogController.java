/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.key.entity.KeyCheckLog;
import com.itrus.ca.modules.key.entity.KeyTypeObj;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.receipt.entity.ReceiptCheckLog;
import com.itrus.ca.modules.receipt.entity.ReceiptCheckLogDetail;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotTypeStatistics;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.entity.ReceiptType;
import com.itrus.ca.modules.receipt.service.ReceiptCheckLogDetailService;
import com.itrus.ca.modules.receipt.service.ReceiptCheckLogService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotTypeStatisticsService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.receipt.service.ReceiptTypeService;

/**
 * 盘点信息Controller
 * @author WHW
 * @version 2014-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/receipt/receiptCheckLog")
public class ReceiptCheckLogController extends BaseController {

	private LogUtil logUtil = new LogUtil();
	
	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;
	
	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;
	
	@Autowired
	private ReceiptCheckLogService receiptCheckLogService;
	
	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;
	
	@Autowired
	private OfficeService officeService;
	
	
	@Autowired
	private ReceiptTypeService receiptTypeService;
	
	@Autowired
	private ReceiptCheckLogDetailService receiptCheckLogDetailService;
	
	
	@Autowired 
	private ReceiptDepotTypeStatisticsService receiptDepotTypeStatisticsService;
	
	
	@ModelAttribute
	public ReceiptCheckLog get(@RequestParam(required=false) Long id) {
		if (id != null){
			return receiptCheckLogService.get(id);
		}else{
			return new ReceiptCheckLog();
		}
	}
	
	@RequiresPermissions("receipt:receiptCheckLog:view")
	@RequestMapping(value = "li")
	public String li(ReceiptDepotInfo receiptDepotInfo, HttpServletRequest request, HttpServletResponse response, Model model){
		User user = UserUtils.getUser();
		List<Office> offices =  officeService.getOfficeByType(user, 2);
        Page<ReceiptDepotInfo> page = receiptDepotInfoService.find(new Page<ReceiptDepotInfo>(request, response), receiptDepotInfo,offices); 
        List<ReceiptDepotInfo> depotInfos = new ArrayList<ReceiptDepotInfo>();
        for(ReceiptDepotInfo depotInfon:page.getList()){
        	int i = receiptCheckLogService.selectByDepotInfoF(depotInfon).size();
        	if(i>0){
        		depotInfon.setReceiptType(0);
        	}
        	depotInfos.add(depotInfon);
        }
        page.setList(depotInfos);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("page", page);
		try {
			model.addAttribute("pDate",simpleDateFormat.parse(simpleDateFormat.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "modules/receipt/receiptCheckLogList";
	}
	
	@RequiresPermissions("receipt:receiptCheckLog:pand")
	@RequestMapping("checkLogFrom")
	public String checkLogFrom(Long id,ReceiptCheckLog receiptCheckLog, 
			Date time ,
			HttpServletRequest request, HttpServletResponse response, 
			Model model,RedirectAttributes redirectAttributes) throws Exception{
		

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(df.format(time).equals(df.format(new Date()))){//盘点日为当天
			time = simpleDateFormat.parse(simpleDateFormat.format(new Date()));//精确到当前的时间
		}else{
			time.setHours(23);
			time.setMinutes(59);
			time.setSeconds(59);
		}
		ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService.get(id); 
		List<ReceiptCheckLog> checkLogs = receiptCheckLogService.selectByDepotInfoF(receiptDepotInfo);
		if(checkLogs.size()>0){
			addMessage(redirectAttributes, "存在尚未复位的盘点信息，请对其进行复位操作");
			return "redirect:"+Global.getAdminPath()+"/receipt/receiptCheckLog/li";
		}
		List<ReceiptType> types = receiptTypeService.getAll();
		HashMap<Integer, Integer>  lastTypes = new HashMap<Integer,Integer>();
		//当前库房信息
		model.addAttribute("receiptDepotInfo", receiptDepotInfo);
		
		
		
		ReceiptCheckLog checkLogOne = receiptCheckLogService.findLestMoney(receiptDepotInfo);
		if (checkLogOne==null) {
			receiptCheckLog = new ReceiptCheckLog();
		}else {
			receiptCheckLog = checkLogOne;
		}
		
		
		if (receiptDepotInfo.getId()==KeyDepotId.RECEIPT_DEPOT_ID) {
			//计算总库上一次余额与类型数量
			
			if (checkLogOne==null) {
				for (int i = 0; i < types.size(); i++) {
					lastTypes.put(types.get(i).getTypeName(), 0);
				}
				model.addAttribute("lastMoney", 0);
				model.addAttribute("lastTypes", lastTypes);
			}else{
				for (int i = 0; i < types.size(); i++) {
					
					ReceiptCheckLogDetail detail = receiptCheckLogDetailService.findByCheckType(checkLogOne.getId(), types.get(i).getId());
					if (detail==null) {
						lastTypes.put(types.get(i).getTypeName(), 0);
					}else{
						lastTypes.put(types.get(i).getTypeName(),detail.getCount());
					}
				}
				model.addAttribute("lastMoney", checkLogOne.getAfterResidue());
				model.addAttribute("lastTypes", lastTypes);
			}
			//计算总库入库余额与类型数量
			List<ReceiptEnterInfo> enterInfos = receiptEnterInfoService.findEnterInfoList(receiptDepotInfo,checkLogOne!=null?checkLogOne.getEndDate():null,time);
			HashMap<Integer, Integer>  inTypes = new HashMap<Integer,Integer>();
			double numMoney = 0;
			for (int i = 0; i < types.size(); i++) {
				int count = 0;
				for (int j = 0; j < enterInfos.size(); j++) {
					if (enterInfos.get(j).getType().getId().equals(types.get(i).getId())) {
						count+=enterInfos.get(j).getCount();
						numMoney += enterInfos.get(j).getNow_Money();
					}
				}
				inTypes.put(types.get(i).getTypeName(),count);
			}
			
			model.addAttribute("numMoney", numMoney);//本期入库
			model.addAttribute("inTypes", inTypes);
			
			//计算总库出库余额与类型数量
			List<ReceiptInvoice> invoices =  receiptInvoiceService.findByInvoice(staDate(receiptDepotInfo),time,receiptDepotInfo);
			HashMap<Integer, Integer>  outTypes = new HashMap<Integer,Integer>();
			double yikaiMoney = 0;
			for (int i = 0; i < types.size(); i++) {
				int count = 0;
				for (int j = 0; j < invoices.size(); j++) {
					if (invoices.get(j).getType()!=null) {
						if (invoices.get(j).getType().getId().equals(types.get(i).getId())) {
							if (invoices.get(j).getCount()!=null) {
								count+=invoices.get(j).getCount();
							}
							yikaiMoney += invoices.get(j).getReceiptMoney();
						}
					}
				}
				outTypes.put(types.get(i).getTypeName(),count);
			}
			
			model.addAttribute("yikaiMoney", yikaiMoney);//本期出库
			model.addAttribute("outTypes", outTypes);
			
			
			//计算总库余额
			HashMap<Integer, Integer>  yuTypes = new HashMap<Integer,Integer>();
			for (int i = 0; i < types.size(); i++) {
				int last = lastTypes.get(types.get(i).getTypeName());
				int in =  inTypes.get(types.get(i).getTypeName());
				int out = outTypes.get(types.get(i).getTypeName());
				yuTypes.put(types.get(i).getTypeName(), last+in-out);
			}
			model.addAttribute("yuTypes", yuTypes);//余额
			
			
		}else {
			double lastMoney = lastResidue(receiptDepotInfo);
			model.addAttribute("lastMoney", lastMoney);
			//总金额
			double numMoney = zhongMondy(receiptDepotInfo,time);
			model.addAttribute("numMoney", numMoney);//本期入库
			double outMoney =  yikaiMoney(staDate(receiptDepotInfo),time,receiptDepotInfo);
			model.addAttribute("yikaiMoney",outMoney);//本期出库
			
			//余额
			model.addAttribute("yuMoney",lastMoney+numMoney-outMoney);
		}
		
		
	
		//开始时间
		model.addAttribute("staDate",staDate(receiptDepotInfo));
		//结束时间  
		//model.addAttribute("endDate",getDateString());
		
		
		model.addAttribute("endDate",time);
		//已开金额
		
		receiptCheckLog.setReceiptDepotInfo(receiptDepotInfo);
		Page<ReceiptCheckLog> page = receiptCheckLogService.find(new Page<ReceiptCheckLog>(request, response), receiptCheckLog); 
        model.addAttribute("page", page);
        List<ReceiptCheckLog> list = page.getList();
        for(ReceiptCheckLog checkLog :list){
        	if(checkLog.getStatus()==0){
        		model.addAttribute("status",1);
        	}
        }
        return "modules/receipt/receiptCheckLogListT";
	}
	
	@RequestMapping("checkLogFromF")
	public String checkLogFromF(Long id,ReceiptCheckLog receiptCheckLog, HttpServletRequest request, HttpServletResponse response, Model model){
		receiptCheckLog = new ReceiptCheckLog();
		ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService.get(id); 
		receiptCheckLog.setReceiptDepotInfo(receiptDepotInfo);
		//当前库房信息
		model.addAttribute("receiptDepotInfo", receiptDepotInfo);
		Page<ReceiptCheckLog> page = receiptCheckLogService.find(new Page<ReceiptCheckLog>(request, response), receiptCheckLog); 
		for (int i = 0; i < page.getList().size(); i++) {
			List<ReceiptCheckLogDetail> lastsStrings = new ArrayList<ReceiptCheckLogDetail>();
			List<ReceiptCheckLogDetail> inStrings = new ArrayList<ReceiptCheckLogDetail>();
			List<ReceiptCheckLogDetail> outStrings = new ArrayList<ReceiptCheckLogDetail>();
			List<ReceiptCheckLogDetail> yuStrings = new ArrayList<ReceiptCheckLogDetail>();
			List<ReceiptCheckLogDetail> afterStrings = new ArrayList<ReceiptCheckLogDetail>();
			lastsStrings = receiptCheckLogDetailService.findByType(1, page.getList().get(i).getId());
			inStrings = receiptCheckLogDetailService.findByType(2, page.getList().get(i).getId());
			outStrings = receiptCheckLogDetailService.findByType(3, page.getList().get(i).getId());
			yuStrings = receiptCheckLogDetailService.findByType(4,page.getList().get(i).getId());
			afterStrings = receiptCheckLogDetailService.findByType(5, page.getList().get(i).getId());
			
			page.getList().get(i).setLastsStrings(lastsStrings);
			page.getList().get(i).setInStrings(inStrings);
			page.getList().get(i).setOutStrings(outStrings);
			page.getList().get(i).setYuStrings(yuStrings);
			page.getList().get(i).setAfterStrings(afterStrings);
		}
		model.addAttribute("page", page);
        List<ReceiptCheckLog> list = page.getList();
        for(ReceiptCheckLog checkLog :list){
        	if(checkLog.getStatus()==0){
        		model.addAttribute("status",1);
        	}
        }
        return "modules/receipt/receiptCheckLogListF";
	}
	
	@RequiresPermissions("receipt:receiptCheckLog:view")
	@RequestMapping(value = "list")
	public String list(Long id,ReceiptCheckLog receiptCheckLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		model.addAttribute("user", user); //当前管理员信息
		model.addAttribute("office",user.getOffice());
		model.addAttribute("receiptDepotInfo", getReceiptDepotInfo()); //当前库房信息
		model.addAttribute("yuMoney",getReceiptDepotInfo().getReceiptResidue());
		Page<ReceiptCheckLog> page = receiptCheckLogService.find(new Page<ReceiptCheckLog>(request, response), receiptCheckLog); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptCheckLogListT";
	}
	
	@RequiresPermissions("receipt:receiptCheckLog:success")
	@RequestMapping("checkLogFwFrom")
	public String checkLogFwFrom(ReceiptDepotInfo receiptDepotInfo,Model model){
		List<ReceiptCheckLog> receiptCheckLogs = receiptCheckLogService.selectByDepotInfoF(receiptDepotInfo);
		
		
		List<ReceiptCheckLogDetail> lastsStrings = new ArrayList<ReceiptCheckLogDetail>();
		List<ReceiptCheckLogDetail> inStrings = new ArrayList<ReceiptCheckLogDetail>();
		List<ReceiptCheckLogDetail> outStrings = new ArrayList<ReceiptCheckLogDetail>();
		List<ReceiptCheckLogDetail> yuStrings = new ArrayList<ReceiptCheckLogDetail>();
		List<ReceiptCheckLogDetail> afterStrings = new ArrayList<ReceiptCheckLogDetail>();
		lastsStrings = receiptCheckLogDetailService.findByType(1, receiptCheckLogs.get(0).getId());
		inStrings = receiptCheckLogDetailService.findByType(2, receiptCheckLogs.get(0).getId());
		outStrings = receiptCheckLogDetailService.findByType(3, receiptCheckLogs.get(0).getId());
		yuStrings = receiptCheckLogDetailService.findByType(4, receiptCheckLogs.get(0).getId());
		afterStrings = receiptCheckLogDetailService.findByType(5, receiptCheckLogs.get(0).getId());
		
		
		model.addAttribute("lastsStrings",lastsStrings);
		model.addAttribute("inStrings",inStrings);
		model.addAttribute("outStrings",outStrings);
		model.addAttribute("yuStrings",yuStrings);
		model.addAttribute("afterStrings",afterStrings);
		model.addAttribute("receiptCheckLog", receiptCheckLogs.get(0));
		return "modules/receipt/receiptCheckLogFw";
	}
	
	@RequiresPermissions("receipt:receiptCheckLog:view")
	@RequestMapping("listT")
	public String listT(ReceiptCheckLog receiptCheckLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
//		if (!user.isAdmin()){
//			receiptCheckLog.setCreateBy(user);
//		}
        Page<ReceiptCheckLog> page = receiptCheckLogService.find(new Page<ReceiptCheckLog>(request, response), receiptCheckLog); 
        model.addAttribute("page", page);
		return "modules/receipt/receiptCheckLogListT";
	}
	
	@RequiresPermissions("receipt:receiptCheckLog:view")
	@RequestMapping(value = "form")
	public String form(ReceiptCheckLog receiptCheckLog, Model model) {
		model.addAttribute("receiptCheckLog", receiptCheckLog);
		return "modules/receipt/receiptCheckLogForm";
	}

	@RequestMapping("fuwei")
	public String fuwei(ReceiptCheckLog receiptCheckLog, HttpServletRequest request, HttpServletResponse response, Model model){
		ReceiptDepotInfo receiptDepotInfo = receiptCheckLog.getReceiptDepotInfo();//修改余额
		if (receiptDepotInfo.getId().equals(KeyDepotId.RECEIPT_DEPOT_ID)) {
			if( receiptCheckLog.getAfterResidue().doubleValue() != receiptCheckLog.getBeforeResidue().doubleValue() ){
				Double betweenPrice =0d;
//				if ( receiptCheckLog.getAfterResidue()>receiptCheckLog.getBeforeResidue()) {//入库
//					
//					
////					betweenPrice = receiptCheckLog.getAfterResidue() - receiptCheckLog.getBeforeResidue();
////					receiptDepotInfo.setReceiptResidue(betweenPrice+receiptDepotInfo.getReceiptResidue()  );
////					receiptDepotInfo.setReceiptTotal(betweenPrice+receiptDepotInfo.getReceiptTotal());
//					double ycMoney = new Double(receiptCheckLog.getAfterResidue())- new Double(receiptCheckLog.getBeforeResidue());
//					receiptCheckLog.setBeforeTotal(receiptCheckLog.getBeforeTotal()+ycMoney);
//				}else {
//					//出库
////					betweenPrice = receiptCheckLog.getBeforeResidue() - receiptCheckLog.getAfterResidue();
////					receiptDepotInfo.setReceiptOut(receiptDepotInfo.getReceiptOut()+betweenPrice);
////					receiptDepotInfo.setReceiptTotal(receiptDepotInfo.getReceiptTotal()-betweenPrice);
//
//					double ycMoney = new Double(receiptCheckLog.getBeforeResidue())-new Double(receiptCheckLog.getAfterResidue());
//					receiptCheckLog.setBeforeOut(receiptCheckLog.getBeforeOut()+ycMoney);
//				}
				List<ReceiptType> types =  receiptTypeService.getAll();
				for (int i = 0; i < types.size(); i++) {
					ReceiptCheckLogDetail yuDetail = receiptCheckLogDetailService.findByTypeCheckType(4, receiptCheckLog.getId(), types.get(i).getId());
					ReceiptCheckLogDetail afterDetail = receiptCheckLogDetailService.findByTypeCheckType(5, receiptCheckLog.getId(), types.get(i).getId());
					if (yuDetail.getCount()>afterDetail.getCount()) {
						Integer betweenCount = yuDetail.getCount() - afterDetail.getCount();
						ReceiptInvoice invoice = new ReceiptInvoice();
						invoice.setCount(betweenCount);
						invoice.setType(types.get(i));
						invoice.setReceiptType(2);//异常出库
						invoice.setReceiptDepotInfo(receiptDepotInfo);
						Integer priceTotal = betweenCount*types.get(i).getTypeName();
						invoice.setReceiptMoney(Double.parseDouble(priceTotal.toString()));
						receiptInvoiceService.save(invoice);
						logUtil.saveSysLog("发票管理", "库房"+receiptDepotInfo.getReceiptName()+"添加出库信息成功", "");
						
						
						
						//betweenPrice = receiptCheckLog.getBeforeResidue() - receiptCheckLog.getAfterResidue();
						receiptDepotInfo.setReceiptOut(receiptDepotInfo.getReceiptOut()+priceTotal);
						receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue()-priceTotal);
					//	receiptDepotInfo.setReceiptTotal(receiptDepotInfo.getReceiptTotal()-priceTotal);

//						double ycMoney = new Double(receiptCheckLog.getBeforeResidue())-new Double(receiptCheckLog.getAfterResidue());
//						receiptCheckLog.setBeforeOut(receiptCheckLog.getBeforeOut()+ycMoney);
						
						
						
						ReceiptDepotTypeStatistics seceiptDepotTypeStatistics = receiptDepotTypeStatisticsService.findByTypeDepot(invoice.getReceiptDepotInfo().getId(), types.get(i).getId());
						if (seceiptDepotTypeStatistics!=null) {
							ReceiptDepotTypeStatistics stas =seceiptDepotTypeStatistics;
							Integer outTotal = stas.getOutCount();
							Integer total = stas.getTotalEndCount();
							stas.setOutCount(outTotal+betweenCount);
							stas.setTotalEndCount(total-betweenCount);
							receiptDepotTypeStatisticsService.save(stas);
						}else {
							ReceiptDepotTypeStatistics stas = new ReceiptDepotTypeStatistics();
							stas.setReceiptDepotInfo(receiptDepotInfo);
							stas.setOutCount(betweenCount);
							stas.setTotalEndCount(betweenCount);
							stas.setInCount(0);
							stas.setReceiptType(types.get(i));
							receiptDepotTypeStatisticsService.save(stas);
						}
					}else if (yuDetail.getCount()<afterDetail.getCount()) {
						Integer betweenCount = afterDetail.getCount()-yuDetail.getCount();
						ReceiptEnterInfo enter = new ReceiptEnterInfo();
						enter.setReceiptType(2);//异常入库
						enter.setType(types.get(i));
						enter.setReceiptDepotInfo(receiptDepotInfo);
						enter.setCount(betweenCount);
						
						Integer priceTotal = betweenCount*types.get(i).getTypeName();
						enter.setNow_Money(Double.parseDouble(priceTotal.toString()));
						receiptEnterInfoService.save(enter);
						logUtil.saveSysLog("发票管理", "库房"+receiptDepotInfo.getReceiptName()+"添加入库信息成功", "");
						
						receiptDepotInfo.setReceiptResidue(priceTotal+receiptDepotInfo.getReceiptResidue()  );
						receiptDepotInfo.setReceiptTotal(priceTotal+receiptDepotInfo.getReceiptTotal());
						
						
						
						ReceiptDepotTypeStatistics seceiptDepotTypeStatistics = receiptDepotTypeStatisticsService.findByTypeDepot(enter.getReceiptDepotInfo().getId(), types.get(i).getId());
						if (seceiptDepotTypeStatistics!=null) {
							ReceiptDepotTypeStatistics stas =seceiptDepotTypeStatistics;
							Integer inTotal = stas.getInCount();
							Integer total = stas.getTotalEndCount();
							stas.setInCount(inTotal+betweenCount);
							stas.setTotalEndCount(total+betweenCount);
							receiptDepotTypeStatisticsService.save(stas);
						}else {
							ReceiptDepotTypeStatistics stas = new ReceiptDepotTypeStatistics();
							stas.setReceiptDepotInfo(receiptDepotInfo);
							stas.setInCount(betweenCount);
							stas.setTotalEndCount(betweenCount);
							stas.setOutCount(0);
							stas.setReceiptType(types.get(i));
							receiptDepotTypeStatisticsService.save(stas);
						}
						
					}
				}
					
			//	receiptDepotInfo.setReceiptResidue(betweenPrice+receiptDepotInfo.getReceiptResidue()  );
			//	receiptDepotInfo.setReceiptTotal(betweenPrice+receiptDepotInfo.getReceiptTotal());
				receiptDepotInfoService.save(receiptDepotInfo);
				receiptCheckLog.setStatus(1);
//				double ycMoney = new Double(receiptCheckLog.getAfterResidue())- new Double(receiptCheckLog.getBeforeResidue());
//				receiptCheckLog.setBeforeTotal(receiptCheckLog.getBeforeTotal()+ycMoney);
				receiptCheckLogService.save(receiptCheckLog);
				logUtil.saveSysLog("发票管理","库房"+receiptDepotInfo.getReceiptName()+"盘点复位成功","");
			}
			
		}else{
			
			if(new Double(receiptCheckLog.getAfterResidue())> new Double(receiptCheckLog.getBeforeResidue()) ){
				//当实际余额 大约 库存余额
			//	ReceiptDepotInfo receiptDepotInfo = receiptCheckLog.getReceiptDepotInfo();
				//修改余额
			
				// 创建入库信息
				ReceiptEnterInfo receiptEnterInfo = new ReceiptEnterInfo();
				receiptEnterInfo.setReceiptDepotInfo(receiptDepotInfo);
			//	receiptEnterInfo.setReceiptMoney(receiptCheckLog.getAfterResidue()-receiptCheckLog.getBeforeResidue());
			//	receiptEnterInfo.setBeforMoney(receiptEnterInfo.getReceiptDepotInfo().getReceiptResidue()-receiptEnterInfo.getReceiptMoney());
				receiptEnterInfo.setNow_Money(receiptCheckLog.getAfterResidue()-receiptCheckLog.getBeforeResidue());
				receiptEnterInfo.setReceiptType(2);//异常入库
				receiptEnterInfoService.save(receiptEnterInfo);
				Double betweenDouble = receiptCheckLog.getAfterResidue()-receiptCheckLog.getBeforeResidue();
				receiptDepotInfo.setReceiptResidue(betweenDouble+receiptDepotInfo.getReceiptResidue());
				receiptDepotInfo.setReceiptTotal( receiptDepotInfo.getReceiptTotal()+betweenDouble);
				
				
				logUtil.saveSysLog("发票管理", "库房"+receiptDepotInfo.getReceiptName()+"添加入库信息成功", "");
				receiptDepotInfoService.save(receiptDepotInfo);
				logUtil.saveSysLog("发票管理","修改库房"+receiptDepotInfo.getReceiptName()+"发票余额，异常入库","");
				receiptCheckLog.setStatus(1);
				double ycMoney = new Double(receiptCheckLog.getAfterResidue())- new Double(receiptCheckLog.getBeforeResidue());
				receiptCheckLog.setBeforeTotal(receiptCheckLog.getBeforeTotal()+ycMoney);
				receiptCheckLogService.save(receiptCheckLog);
				logUtil.saveSysLog("发票管理","库房"+receiptDepotInfo.getReceiptName()+"盘点复位成功","");
			}else if(new Double(receiptCheckLog.getAfterResidue())<new Double(receiptCheckLog.getBeforeResidue())){
				//当实际金额 小于库存余额
			//	ReceiptDepotInfo receiptDepotInfo = receiptCheckLog.getReceiptDepotInfo();
				//修改余额
			
				//创建出库信息
				ReceiptInvoice receiptInvoice = new ReceiptInvoice();
				receiptInvoice.setReceiptDepotInfo(receiptDepotInfo);
				receiptInvoice.setCompanyName(receiptDepotInfo.getReceiptName());
				receiptInvoice.setReceiptMoney(receiptCheckLog.getBeforeResidue()-receiptCheckLog.getAfterResidue());
				receiptInvoice.setReceiptType(2);//异常出库
				receiptInvoiceService.save(receiptInvoice);
				
				
				
				Double betweenDouble = receiptCheckLog.getBeforeResidue()-receiptCheckLog.getAfterResidue();
				receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue() - betweenDouble);
				receiptDepotInfo.setReceiptOut(betweenDouble+receiptDepotInfo.getReceiptOut());
				
				
				
				
				logUtil.saveSysLog("发票管理", "库房"+receiptDepotInfo.getReceiptName()+"添加出库库信息成功", "");
				receiptDepotInfoService.save(receiptDepotInfo);
				logUtil.saveSysLog("发票管理","修改库房"+receiptDepotInfo.getReceiptName()+"发票余额，异常出库","");
				receiptCheckLog.setStatus(1);
				double ycMoney = new Double(receiptCheckLog.getBeforeResidue())-new Double(receiptCheckLog.getAfterResidue());
				receiptCheckLog.setBeforeOut(receiptCheckLog.getBeforeOut()+ycMoney);
				receiptCheckLogService.save(receiptCheckLog);
				logUtil.saveSysLog("发票管理","库房"+receiptDepotInfo.getReceiptName()+"盘点复位成功","");
			}else{
				receiptCheckLog.setStatus(1);
				receiptCheckLogService.save(receiptCheckLog);
			}
		}
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptCheckLog/li";
	}
	
	@RequiresPermissions("receipt:receiptCheckLog:success")
	@RequestMapping(value = "save")
	public String save(ReceiptCheckLog receiptCheckLog,String endDa, Model model, RedirectAttributes redirectAttributes,
			String lastCount , String inCount , String outCount , String yuCount , String afterCount,String typeDesc
			) {
		if (!beanValidator(model, receiptCheckLog)){
			return form(receiptCheckLog, model);
		}
		//
		if(new Double(receiptCheckLog.getBeforeResidue()).equals(new Double(receiptCheckLog.getAfterResidue()))){
			receiptCheckLog.setStatus(1);
		}else {
			receiptCheckLog.setStatus(0);
		}
		
		receiptCheckLog.setEndDate(getDateString(endDa));
		receiptCheckLog.setStartDate(staDate(receiptCheckLog.getReceiptDepotInfo()));
		receiptCheckLog.setTimes(receiptCheckLogService.selectByDepotInfo(receiptCheckLog.getReceiptDepotInfo()).size()+1);
		receiptCheckLogService.save(receiptCheckLog);
		
		if (receiptCheckLog.getReceiptDepotInfo().getId().equals(KeyDepotId.RECEIPT_DEPOT_ID)) {
			String[] lastsStrings = lastCount.split(",");
			String[] inStrings = inCount.split(",");
			String[] outStrings = outCount.split(",");
			String[] yuStrings = yuCount.split(",");
			String[] afterStrings =  afterCount.split(",");
			String[] typeDescsStrings = typeDesc.split(",");
			
			for (int i = 0; i < typeDescsStrings.length; i++) {
				
				Integer typeIdLong = Integer.parseInt(typeDescsStrings[i]);
				ReceiptType type =  receiptTypeService.findByName(typeIdLong);
				ReceiptCheckLogDetail lastDetail = new ReceiptCheckLogDetail();
				lastDetail.setReceiptCheckLog(receiptCheckLog);
				lastDetail.setReceiptType(type);
				lastDetail.setType(1);
				lastDetail.setCount(new Integer(lastsStrings[i]));
				lastDetail.setCountPrice(type.getTypeName()*new Integer(lastsStrings[i]));
				receiptCheckLogDetailService.save(lastDetail);
				
				ReceiptCheckLogDetail inDetail = new ReceiptCheckLogDetail();
				inDetail.setReceiptCheckLog(receiptCheckLog);
				inDetail.setReceiptType(type);
				inDetail.setType(2);
				inDetail.setCount(new Integer(inStrings[i]));
				inDetail.setCountPrice(type.getTypeName()*new Integer(inStrings[i]));
				receiptCheckLogDetailService.save(inDetail);
				
				ReceiptCheckLogDetail outDetail = new ReceiptCheckLogDetail();
				outDetail.setReceiptCheckLog(receiptCheckLog);
				outDetail.setReceiptType(type);
				outDetail.setType(3);
				outDetail.setCount(new Integer(outStrings[i]));
				outDetail.setCountPrice(type.getTypeName()*new Integer(outStrings[i]));
				receiptCheckLogDetailService.save(outDetail);
				
				ReceiptCheckLogDetail yuDetail = new ReceiptCheckLogDetail();
				yuDetail.setReceiptCheckLog(receiptCheckLog);
				yuDetail.setReceiptType(type);
				yuDetail.setType(4);
				yuDetail.setCount(new Integer(yuStrings[i]));
				yuDetail.setCountPrice(type.getTypeName()*new Integer(yuStrings[i]));
				receiptCheckLogDetailService.save(yuDetail);
				
				ReceiptCheckLogDetail afterDetail = new ReceiptCheckLogDetail();
				afterDetail.setReceiptCheckLog(receiptCheckLog);
				afterDetail.setReceiptType(type);
				afterDetail.setType(5);
				afterDetail.setCount(new Integer(afterStrings[i]));
				afterDetail.setCountPrice(type.getTypeName()*new Integer(afterStrings[i]));
				receiptCheckLogDetailService.save(afterDetail);
			}
		}
		
		logUtil.saveSysLog("发票管理","对库房"+receiptCheckLog.getReceiptDepotInfo().getReceiptName()+"保存盘点信息成功","");
//		addMessage(redirectAttributes, "保存盘点信息'" + receiptCheckLog.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/receipt/receiptCheckLog/li";
	}

	
	
	
//	@RequiresPermissions("receipt:receiptCheckLog:edit")
//	@RequestMapping(value = "delete")
//	public String delete(Long id, RedirectAttributes redirectAttributes) {
//		receiptCheckLogService.delete(id);
//		addMessage(redirectAttributes, "删除盘点信息成功");
//		return "redirect:"+Global.getAdminPath()+"/modules/receipt/receiptCheckLog/?repage";
//	}

	/**
	 * 获得当前登录用户的库房内容
	 * @return
	 */
	private ReceiptDepotInfo getReceiptDepotInfo(){
		User user = UserUtils.getUser();
		List<ReceiptDepotInfo> depotInfos =  receiptDepotInfoService.findDepotByOffice(user.getOffice());
		if(depotInfos.size()>0){
			return depotInfos.get(0);
		}
		return null;
	}
	
	/**
	 * 获得当前时间的String类型
	 * @return
	 */
	private String getDateString(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date()).toString();
	}
	
	/**
	 * 获得当前时间的String类型
	 * @return
	 */
	private Date getDateString(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 获取上次盘点余额
	 */
	private double lastResidue(ReceiptDepotInfo receiptDepotInfo){
		double numMoney =0;
		ReceiptCheckLog receiptCheckLog = receiptCheckLogService.findLestMoney(receiptDepotInfo);
		//receiptCheckLog.getEndDate(); 下次盘点的开始时间
		if(receiptCheckLog!=null){
			numMoney+=receiptCheckLog.getAfterResidue();
		}
		return numMoney ;
	}
	
	/**
	 * 获取当前盘点金额总量
	 */
	private double zhongMondy(ReceiptDepotInfo receiptDepotInfo,Date lastDate){
		double numMoney =0;
		ReceiptCheckLog receiptCheckLog = receiptCheckLogService.findLestMoney(receiptDepotInfo);
		List<ReceiptEnterInfo> enterInfos = receiptEnterInfoService.findEnterInfoList(receiptDepotInfo,receiptCheckLog!=null?receiptCheckLog.getEndDate():null,lastDate);
		for(ReceiptEnterInfo info:enterInfos){
			if(info.getNow_Money()!=null){
				numMoney+=info.getNow_Money();
			}
		}
		return numMoney;
	}
	
	private Date staDate(ReceiptDepotInfo receiptDepotInfo){
		ReceiptCheckLog receiptCheckLog = receiptCheckLogService.findLestMoney(receiptDepotInfo);
		if(receiptCheckLog!=null){
			return receiptCheckLog.getEndDate();
		}
		receiptDepotInfo = receiptDepotInfoService.get(receiptDepotInfo.getId());
		return receiptDepotInfo.getCreateDate();
	}
	
	
	/**
	 * 获取已开金额
	 */
	private double yikaiMoney(Date staDate ,Date endDate,ReceiptDepotInfo receiptDepotInfo){
		double money = receiptInvoiceService.findInvoiceByDepotInfo(staDate,endDate,receiptDepotInfo);
		return money;
	}
	
	
	@RequestMapping(value="checkTime")
	@ResponseBody
	public String checkTime(Long depotId,Date endDate) throws JSONException{
		JSONObject json = new JSONObject();
		try {

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(df.format(endDate).equals(df.format(new Date()))){//盘点日为当天
				endDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));//精确到当前的时间
			}else{
				endDate.setHours(23);
				endDate.setMinutes(59);
				endDate.setSeconds(59);
			}
			ReceiptDepotInfo receiptDepotInfo = receiptDepotInfoService.get(depotId); 
			ReceiptCheckLog checkLogOne = receiptCheckLogService.findLestMoney(receiptDepotInfo);
			Date startDate = null;
			if (checkLogOne==null) {
				startDate = receiptDepotInfo.getCreateDate();
			}else {
				
				startDate = checkLogOne.getEndDate();
			}
			
			String time =df.format(startDate);
			
			if (endDate.after(startDate)) {
				json.put("after", 1);
			}else {
				json.put("after", -1);
				json.put("time", time);
			}
			json.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", -1);
		}
		return json.toString();
		
	}
	
	
	
	
	
	
	
}
