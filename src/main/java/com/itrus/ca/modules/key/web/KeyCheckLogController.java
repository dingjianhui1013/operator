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

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.key.entity.KeyAllocateApply;
import com.itrus.ca.modules.key.entity.KeyCheckLog;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyCheckLogService;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyManufacturerService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;

/**
 * 库存统计Controller
 * 
 * @author HUHAO
 * @version 2014-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/key/keyCheckLog")
public class KeyCheckLogController extends BaseController {

	@Autowired
	private KeyCheckLogService keyCheckLogService;

	@Autowired
	private KeyUsbKeyDepotService keyUsbKeyDepotService;

	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;

	@Autowired
	private KeyUsbKeyService keyUsbKeyService;

	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;

	@Autowired
	private KeyUsbKeyInvoiceService keyUsbKeyInvoiceService;

	@Autowired
	private KeyManufacturerService keyManufacturerService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private ConfigSupplierService configSupplierService;
	
	
	@ModelAttribute
	public KeyCheckLog get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return keyCheckLogService.get(id);
		} else {
			return new KeyCheckLog();
		}
	}

	@RequiresPermissions("key:keyCheckLog:view")
	@RequestMapping(value = { "list", "" })
	public String list(KeyCheckLog keyCheckLog, HttpServletRequest request,
			@RequestParam(value = "depotName", required = false) String depotName,
			HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			keyCheckLog.setCreateBy(user);
		}
		
		List<Office> offices =  officeService.getOfficeByType(user, 2);
		
		Page<KeyUsbKeyDepot> depots = keyUsbKeyDepotService
				.findAllDepot(new Page<KeyUsbKeyDepot>(request, response),depotName,offices);

		for (int i = 0; i < depots.getList().size(); i++) {
			List<KeyCheckLog> checkLog = keyCheckLogService
					.findOrderByCheckNumber(depots.getList().get(i).getId());// 获取该仓库下的最后一次复位信息
			int isReset = 0;
			int totalCount = 0;
			int inCount = 0;
			int outCount = 0;
			int totalEndCount = 0;
			if (checkLog.size() > 0) {
				List<KeyCheckLog> checkLogsByCheckNumbe = keyCheckLogService
						.findByCheckNumber(depots.getList().get(i).getId(),
								checkLog.get(0).getCheckNumber());// 获取最后一次复位的所有信息
				for (int j = 0; j < checkLogsByCheckNumbe.size(); j++) {
					totalCount += checkLogsByCheckNumbe.get(j).getBeforeTotal();
					inCount += checkLogsByCheckNumbe.get(j).getBeforeResidue();
					outCount += checkLogsByCheckNumbe.get(j).getBeforeOut();
					totalEndCount += checkLogsByCheckNumbe.get(j)
							.getAfterResidue();
					if (checkLogsByCheckNumbe.get(j).getIsReset()) {
						isReset = 1;
					}
				}
				depots.getList()
						.get(i)
						.setStartDate(
								checkLogsByCheckNumbe.get(0).getStartDate());
				depots.getList().get(i)
						.setEndDate(checkLogsByCheckNumbe.get(0).getEndDate());
				
				List<KeyUsbKey> kukKeysYC = 	keyUsbKeyService.findByStartEnd(checkLogsByCheckNumbe.get(0).getStartDate());
				if (kukKeysYC.size()>0) {
					for (int j = 0; j < kukKeysYC.size(); j++) {
						inCount+=kukKeysYC.get(j).getCount();
					}
				}
				List<KeyUsbKeyInvoice> kukInvoice = keyUsbKeyInvoiceService.findByStartEnd(checkLogsByCheckNumbe.get(0).getStartDate());
				if (kukInvoice.size()>0) {
					for (int j = 0; j < kukInvoice.size(); j++) {
						outCount+=kukInvoice.get(j).getDeliveryNum();
					}
				}
			}
			
			
			depots.getList().get(i).setIsReset(isReset);
			depots.getList().get(i).setTotolCount(totalCount);
			depots.getList().get(i).setInCount(inCount);
			depots.getList().get(i).setOutCount(outCount);
			depots.getList().get(i).setTotalEndCount(totalEndCount);
		}
		model.addAttribute("depotName", depotName);
		model.addAttribute("page", depots);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			model.addAttribute("pDate",simpleDateFormat.parse(simpleDateFormat.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "modules/key/keyCheckLogList";
	}

	@RequiresPermissions("key:keyCheckLog:view")
	@RequestMapping(value = "checkList")
	public String checkList(KeyCheckLog keyCheckLog,
			Date time ,
			HttpServletRequest request, HttpServletResponse response,
			Model model, Long depotId) throws Exception{
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			keyCheckLog.setCreateBy(user);
		}
		// KeyUsbKeyDepot depot= keyUsbKeyDepotService.get(depotId);//获取仓库信息

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(df.format(time).equals(df.format(new Date()))){//盘点日为当天
			time = simpleDateFormat.parse(simpleDateFormat.format(new Date()));//精确到当前的时间
		}else{
			time.setHours(23);
			time.setMinutes(59);
			time.setSeconds(59);
		}
		List<KeyCheckLog> checkLog = keyCheckLogService
				.findOrderByCheckNumber(depotId);// 获取该仓库下的最后一次复位信息
		Date date = time;
		if (checkLog.size() > 0) {// 盘点过
			List<KeyCheckLog> checkLogsByCheckNumbe = keyCheckLogService
					.findByCheckNumber(depotId, checkLog.get(0)
							.getCheckNumber());// 获取最后一次复位的所有信息
				Date startDate = checkLogsByCheckNumbe.get(0).getStartDate();
				int checkNumber = checkLog.get(0).getCheckNumber() + 1;// 这次盘点次数
				Date start = checkLog.get(0).getEndDate();// 上次盘点的开始时间
				Date end = date;// 这次盘点的开始时间
				List<KeyUsbKey> kuksKeys = keyUsbKeyService.findByDepotIdDate(
						depotId, start, end);// 获取上一次盘点开始时间到这一次的结束时间的入库信息
				List<Long> geneIds = Lists.newArrayList();// 创建装key类型信息的ID
				List<KeyDepotGeneralStatistics> statisticList = Lists
						.newArrayList();// 创建一个statistics的集合
				for (int i = 0; i < kuksKeys.size(); i++) {
					geneIds.add(kuksKeys.get(i).getKeyGeneralInfo().getId());
				}
				if (geneIds.size() > 0) {
					List<KeyGeneralInfo> geneList = keyGeneralInfoService
							.findByGeneIds(geneIds);// 获取该仓库下的类型
					for (int i = 0; i < geneList.size(); i++) {
						KeyDepotGeneralStatistics sta = new KeyDepotGeneralStatistics();
						Long geneId = geneList.get(i).getId();
						int total = 0;
						int in = 0;
						int out = 0;
						for (int j = 0; j < kuksKeys.size(); j++) {
							if (kuksKeys.get(j).getKeyGeneralInfo().getId().equals(geneId)) {
								in += kuksKeys.get(j).getCount();
							}
						}
						sta.setKeyGeneralInfo(geneList.get(i));
						sta.setTotalCount(total);
						sta.setInCount(in);
						sta.setOutCount(out);
						int totalEndCount = in - out;
						sta.setTotalEndCount(totalEndCount);
						statisticList.add(sta);
					}
				}

				if (statisticList.size() > 0) {
					for (int i = 0; i < statisticList.size(); i++) {// 添加之前没有的出入库操作
						for (int j = checkLogsByCheckNumbe.size() - 1; j >= 0; j--) {
							if (statisticList.get(i).getKeyGeneralInfo()
									.getId().equals(checkLogsByCheckNumbe.get(j)
									.getKeyGeneralInfo().getId())) {
								int afterTotal = checkLogsByCheckNumbe.get(j)
										.getAfterResidue();// 上期结余
								statisticList.get(i).setTotalCount(afterTotal);
								int inCount = statisticList.get(i).getInCount();
								statisticList.get(i).setTotalEndCount(
										afterTotal + inCount);
								checkLogsByCheckNumbe.remove(j);
							}
						}
					}
				}
				for (int i = 0; i < checkLogsByCheckNumbe.size(); i++) {
					KeyDepotGeneralStatistics sta = new KeyDepotGeneralStatistics();
					sta.setKeyGeneralInfo(checkLogsByCheckNumbe.get(i)
							.getKeyGeneralInfo());
					sta.setKeyUsbKeyDepot(checkLogsByCheckNumbe.get(i)
							.getKeyUsbKeyDepot());
					int totalCount = checkLogsByCheckNumbe.get(i)
							.getAfterResidue();
					sta.setTotalCount(totalCount);
					sta.setInCount(0);
					sta.setOutCount(0);
					sta.setTotalEndCount(totalCount);
					statisticList.add(sta);
				}

				List<KeyUsbKeyInvoice> invoices = keyUsbKeyInvoiceService
						.findByDepotId(depotId, start, end);
				if (invoices.size() > 0) {
					for (int i = 0; i < statisticList.size(); i++) {
						for (int j = 0; j < invoices.size(); j++) {
							if (statisticList.get(i).getKeyGeneralInfo().getId().equals(invoices.get(j).getKeyGeneralInfo().getId())) {
								
								int out = invoices.get(j).getDeliveryNum();
								int totalEndCount = statisticList.get(i)
										.getTotalEndCount() - out;
								int endOut =  statisticList.get(i).getOutCount()+out;
								
								statisticList.get(i).setOutCount(endOut);
								statisticList.get(i).setTotalEndCount(
										totalEndCount);
							}
						}
					}
				}

				KeyDepotGeneralStatistics statis = new KeyDepotGeneralStatistics();
				int depotTotal = 0;
				int depotIn = 0;
				int depotOut = 0;
				int endTotal = 0;
				for (int i = 0; i < statisticList.size(); i++) {
					depotTotal += statisticList.get(i).getTotalCount();
					depotIn += statisticList.get(i).getInCount();
					depotOut += statisticList.get(i).getOutCount();
					endTotal += statisticList.get(i).getTotalEndCount();
				}
				
//				List<KeyUsbKey> kukKeysYC = 	keyUsbKeyService.findByStartEnd(startDate);
//				if (kukKeysYC.size()>0) {
//					for (int j = 0; j < kukKeysYC.size(); j++) {
//						//depotTotal += kukKeysYC.get(j).getCount();
//						depotIn+=kukKeysYC.get(j).getCount();
//					}
//				}
//				List<KeyUsbKeyInvoice> kukInvoice = keyUsbKeyInvoiceService.findByStartEnd(startDate);
//				if (kukInvoice.size()>0) {
//					for (int j = 0; j < kukInvoice.size(); j++) {
//						//depotTotal -= kukInvoice.get(j).getDeliveryNum();
//						depotOut+=kukInvoice.get(j).getDeliveryNum();
//					}
//				}
				
				
				
				
				KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);// 获取当前库存的信息
				statis.setKeyUsbKeyDepot(depot);
				statis.setOutCount(depotOut);
				statis.setInCount(depotIn);
				statis.setTotalCount(depotTotal);
				statis.setTotalEndCount(endTotal);
				model.addAttribute("statis", statis);

				model.addAttribute("time", start);
				model.addAttribute("checkNumber", checkNumber);
				model.addAttribute("statisticList", statisticList);
				model.addAttribute("len", statisticList.size());
			
		} else {// 没有盘点过
			int checkNumber = 1;

			List<KeyUsbKey> kuksKeys = keyUsbKeyService.findByDepotId(depotId);// 获取该仓库下的入库信息
			List<Long> geneIds = Lists.newArrayList();
			KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);// 获取当前库存的信息
			for (int i = 0; i < kuksKeys.size(); i++) {
				geneIds.add(kuksKeys.get(i).getKeyGeneralInfo().getId());
			}
			KeyDepotGeneralStatistics statis = new KeyDepotGeneralStatistics();
			int depotTotal = 0;
			int depotIn = 0;
			int depotOut = 0;
			int depotEndTotal = 0;
			if (geneIds.size() > 0) {

				List<KeyGeneralInfo> geneList = keyGeneralInfoService
						.findByGeneIds(geneIds);// 获取该仓库下的类型
				List<KeyDepotGeneralStatistics> statisticList = Lists
						.newArrayList();

				for (int i = 0; i < geneList.size(); i++) {
					KeyDepotGeneralStatistics sta = new KeyDepotGeneralStatistics();
					Long geneId = geneList.get(i).getId();
					int total = 0;
					int out = 0;
					int in = 0;
					for (int j = 0; j < kuksKeys.size(); j++) {
						if (kuksKeys.get(j).getKeyGeneralInfo().getId().equals(geneId)) {
							in += kuksKeys.get(j).getCount();
							depotIn += kuksKeys.get(j).getCount();
						}
					}
					List<KeyUsbKeyInvoice> invoices = keyUsbKeyInvoiceService
							.findByDepotId(depotId);
					for (int j = 0; j < invoices.size(); j++) {
						if (invoices.get(j).getKeyGeneralInfo().getId().equals(geneId)) {
							out += invoices.get(j).getDeliveryNum();
							depotOut += invoices.get(j).getDeliveryNum();
						}
					}
					sta.setKeyGeneralInfo(geneList.get(i));
					sta.setTotalCount(total);
					sta.setInCount(in);
					sta.setOutCount(out);
					int totalEndCount = in - out;
					sta.setTotalEndCount(totalEndCount);
					sta.setKeyUsbKeyDepot(depot);
					statisticList.add(sta);
				}
				model.addAttribute("checkNumber", checkNumber);
				model.addAttribute("statisticList", statisticList);
				model.addAttribute("len", statisticList.size());
				
			}else{

				model.addAttribute("len", 0);
			}

			
			statis.setKeyUsbKeyDepot(depot);
			statis.setOutCount(depotOut);
			statis.setInCount(depotIn);
			statis.setTotalCount(depotTotal);
			depotEndTotal = depotIn - depotOut;
			statis.setTotalEndCount(depotEndTotal);
			model.addAttribute("statis", statis);
			model.addAttribute("time", depot.getCreateDate());
		}


		model.addAttribute("end", date);
		model.addAttribute("depotId", depotId);
		return "modules/key/keyInventoryList";
	}

	@RequiresPermissions("key:keyCheckLog:reset")
	@RequestMapping(value = "resetList")
	public String resetList(KeyCheckLog keyCheckLog,
			HttpServletRequest request, HttpServletResponse response,
			Model model, Long depotId) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			keyCheckLog.setCreateBy(user);
		}
		List<KeyCheckLog> checkLog = keyCheckLogService
				.findOrderByCheckNumber(depotId);// 获取该仓库下的最后一次复位信息

		List<KeyCheckLog> checkByNum = keyCheckLogService.findByCheckNumber(
				depotId, checkLog.get(0).getCheckNumber());

		Date date = new Date();
		List<KeyCheckLog> checkLogsByCheckNumbeIsReset = keyCheckLogService
				.findByCheckNumberIsReset(depotId, checkLog.get(0)
						.getCheckNumber());

		KeyDepotGeneralStatistics statis = new KeyDepotGeneralStatistics();
		int depotTotal = 0;
		int depotIn = 0;
		int depotOut = 0;
		int endTotal = 0;
		for (int i = 0; i < checkByNum.size(); i++) {
			depotTotal += checkLog.get(i).getBeforeTotal();
			depotIn += checkLog.get(i).getBeforeResidue();
			depotOut += checkLog.get(i).getBeforeOut();
			endTotal += checkLog.get(i).getAfterResidue();
		}
		KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);// 获取当前库存的信息
		statis.setKeyUsbKeyDepot(depot);
		statis.setOutCount(depotOut);
		statis.setInCount(depotIn);
		statis.setTotalCount(depotTotal);
		statis.setTotalEndCount(endTotal);
		model.addAttribute("statis", statis);
		model.addAttribute("checkLogsByCheckNumbe",
				checkLogsByCheckNumbeIsReset);
		return "modules/key/keyResetList";
	}

	@RequiresPermissions("key:keyCheckLog:view")
	@RequestMapping(value = "form")
	public String form(KeyCheckLog keyCheckLog, Model model) {
		model.addAttribute("keyCheckLog", keyCheckLog);
		return "modules/key/keyCheckLogForm";
	}

	@RequiresPermissions("key:keyCheckLog:edit")
	@RequestMapping(value = "save")
	public String save(KeyCheckLog keyCheckLog, Model model,
			String keyGeneralInfoId, String total, String out, String in,
			String afterTotal, String inventorySurplus,
			String differenceReason, Long depotId, String time,String end,
			Integer checkNumber, RedirectAttributes redirectAttributes)
			throws ParseException {
		if (!beanValidator(model, keyCheckLog)) {
			return form(keyCheckLog, model);
		}
		KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);
		// int checkNum = 0;
		// if (depot.size()>0) {
		// checkNum = depot.get(0).get
		// }
		String[] geneIds = keyGeneralInfoId.split(",");
		String[] totals = total.split(",");
		String[] outs = out.split(",");
		String[] ins = in.split(",");
		String[] afterTotals = afterTotal.split(",");
		String[] inventorySurplusValue = inventorySurplus.split(",");
		String[] differenceReasonVasue = differenceReason.split(",");
		//Date date = new Date();

		for (int i = 0; i < geneIds.length; i++) {
			KeyCheckLog check = new KeyCheckLog();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = sdf.parse(time);
			Date e = sdf.parse(end);
			check.setStartDate(d);
			check.setEndDate(e);

			check.setCheckNumber(checkNumber);
			Long geneId = Long.parseLong(geneIds[i]);
			KeyGeneralInfo gene = keyGeneralInfoService.get(geneId);
			check.setKeyGeneralInfo(gene);// 设置Key类型
			check.setKeyUsbKeyDepot(depot);// 设置库存
			int totalInt = Integer.parseInt(totals[i]);
			check.setBeforeTotal(totalInt);// 入库数量
			int outInt = Integer.parseInt(outs[i]);
			check.setBeforeOut(outInt);// 出库数量
			int inInt = Integer.parseInt(ins[i]);
			check.setBeforeResidue(inInt);// 余量
			int inventory = Integer.parseInt(inventorySurplusValue[i]);
			check.setAfterResidue(inventory);// 盘点数量

			int afterTotalInt = Integer.parseInt(afterTotals[i]);
			check.setAfterTotal(afterTotalInt);

			if (inventory == afterTotalInt) {
				check.setIsReset(false);
			} else {
				check.setIsReset(true);
				check.setFixRemark(differenceReasonVasue[i]);
			}
			keyCheckLogService.save(check);
		}

		// addMessage(redirectAttributes, "保存库存统计'" + keyCheckLog.getName() +
		// "'成功");
		return "redirect:" + Global.getAdminPath()
				+ "/key/keyCheckLog/list?repage";
	}

	@RequiresPermissions("key:keyCheckLog:edit")
	@RequestMapping(value = "saveReset")
	public String saveReset(KeyCheckLog keyCheckLog, String checkLogId,
			Model model, RedirectAttributes redirectAttributes)
			throws ParseException {
		if (!beanValidator(model, keyCheckLog)) {
			return form(keyCheckLog, model);
		}
		String[] checkId = checkLogId.split(",");
		for (int i = 0; i < checkId.length; i++) {
			Long id = Long.parseLong(checkId[i]);
			KeyCheckLog check = keyCheckLogService.get(id);
			KeyUsbKeyDepot keyUsbKeyDepot = check.getKeyUsbKeyDepot();
			KeyGeneralInfo keyGeneralInfo = check.getKeyGeneralInfo();
			if (check.getAfterResidue() > check.getAfterTotal()) {// 如果盘点值大于库存的值，进行入库操作

				KeyUsbKey keyUsbKey = new KeyUsbKey();
				keyUsbKey.setKeyUsbKeyDepot(keyUsbKeyDepot);
				keyUsbKey.setKeyGeneralInfo(keyGeneralInfo);
				keyUsbKey.setCount(check.getAfterResidue()
						- check.getAfterTotal());
				keyUsbKey.setInReason(5);
				
				keyUsbKey.setStartDate(check.getStartDate());
				
				keyUsbKeyService.save(keyUsbKey);

				List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
						.findByDepotIdAndGeneId(check.getKeyUsbKeyDepot()
								.getId(), check.getKeyGeneralInfo().getId());
				if (staList.size() > 0) {

					KeyDepotGeneralStatistics sta = staList.get(0);
					int total = sta.getTotalCount();
					int intotal = sta.getInCount();
					sta.setTotalCount(total + keyUsbKey.getCount());
					sta.setInCount(intotal + keyUsbKey.getCount());
					keyDepotGeneralStatisticsService.save(sta);
				}
				check.setIsReset(false);
				keyCheckLogService.save(check);
			} else {// 如果盘点值小于库存的值，进行出库操作

				KeyUsbKeyInvoice keyUsbKeyInvoice = new KeyUsbKeyInvoice();
				keyUsbKeyInvoice.setKeyUsbKeyDepot(keyUsbKeyDepot);
				keyUsbKeyInvoice.setKeyGeneralInfo(keyGeneralInfo);
				keyUsbKeyInvoice.setDeliveryNum(check.getAfterTotal()
						- check.getAfterResidue());
				keyUsbKeyInvoice.setOutReason(5);
				keyUsbKeyInvoice.setCreateDate(check.getStartDate());
				keyUsbKeyInvoice.setStartDate(check.getStartDate());
				keyUsbKeyInvoiceService.save(keyUsbKeyInvoice);

				List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
						.findByDepotIdAndGeneId(check.getKeyUsbKeyDepot()
								.getId(), check.getKeyGeneralInfo().getId());
				if (staList.size() > 0) {
					KeyDepotGeneralStatistics sta = staList.get(0);
					int outtotal = sta.getOutCount();
					int intotal = sta.getInCount();
					sta.setOutCount(outtotal
							+ keyUsbKeyInvoice.getDeliveryNum());
					sta.setInCount(intotal - keyUsbKeyInvoice.getDeliveryNum());
					keyDepotGeneralStatisticsService.save(sta);
				}
				check.setIsReset(false);
				keyCheckLogService.save(check);
			}

		}

		addMessage(redirectAttributes, "复位成功!");
		return "redirect:" + Global.getAdminPath()
				+ "/key/keyCheckLog/list?repage";
	}

	@RequiresPermissions("key:keyCheckLog:view")
	@RequestMapping(value = "checkLogShowList")
	public String checkLogShow(
			KeyCheckLog keyCheckLog,
			HttpServletRequest request,
			HttpServletResponse response,

			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "supplierId", required = false) Long supplierId,
			@RequestParam(value = "geneId", required = false) Long geneId,

			Model model, Long depotId) {
		User user = UserUtils.getUser();
		if (endTime!=null) {
			endTime.setHours(23);
			endTime.setMinutes(59);
			endTime.setSeconds(59);
		}
		if (!user.isAdmin()) {
			keyCheckLog.setCreateBy(user);
		}

		Page<KeyCheckLog> checkLogs = keyCheckLogService.findBydepotIdForShow(
				new Page<KeyCheckLog>(request, response), depotId, startTime,
				endTime,supplierId,geneId);

		//List<KeyManufacturer> manus = keyManufacturerService.findAll();
		List<ConfigSupplier> suppliers = configSupplierService.findAllKeySupplier();
		model.addAttribute("suppliers", suppliers);

		List<KeyGeneralInfo> genes = 	keyGeneralInfoService.findBySupplierId(supplierId);
		model.addAttribute("genes", genes);

		model.addAttribute("supplierId", supplierId);
		model.addAttribute("geneId", geneId);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
	
		model.addAttribute("depotId", depotId);
		model.addAttribute("page", checkLogs);
		return "modules/key/keyCheckLogListShow";
	}

	@RequiresPermissions("key:keyCheckLog:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		keyCheckLogService.delete(id);
		addMessage(redirectAttributes, "删除库存统计成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/key/keyCheckLog/?repage";
	}
	
	
	@RequestMapping(value = "checkFW")
	@ResponseBody
	public String checkFW(Long depotId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject json = new JSONObject();
		try {
			List<KeyCheckLog> checkLog = keyCheckLogService
					.findOrderByCheckNumber(depotId);// 获取该仓库下的最后一次复位信息
			json.put("status", "0");
			if (checkLog.size()>0) {
				List<KeyCheckLog> checkByNum = keyCheckLogService.findByCheckNumber(
						depotId, checkLog.get(0).getCheckNumber());
			
				for (int i = 0; i < checkByNum.size(); i++) {
					if (checkByNum.get(i).getIsReset()) {
						json.put("status", "1");
						break;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
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
			List<KeyCheckLog> checkLog = keyCheckLogService
					.findOrderByCheckNumber(depotId);// 获取该仓库下的最后一次复位信息
			Date startDate = null;
			if (checkLog.size()>0) {
				List<KeyCheckLog> checkLogsByCheckNumbe = keyCheckLogService
						.findByCheckNumber(depotId, checkLog.get(0)
								.getCheckNumber());// 获取最后一次复位的所有信息
			  startDate = checkLogsByCheckNumbe.get(0).getStartDate();
			}else {
				KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);// 获取当前库存的信息
				startDate = depot.getCreateDate();
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
