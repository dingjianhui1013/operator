/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Element;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avro.data.Json;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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

import antlr.Utils;

import com.google.common.collect.Lists;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.constant.KeyDepotId;
import com.itrus.ca.modules.key.entity.KeyAllocateApply;
import com.itrus.ca.modules.key.entity.KeyAllocateApplyDetailed;
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.key.service.KeyAllocateApplyDetailedService;
import com.itrus.ca.modules.key.service.KeyAllocateApplyService;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;

/**
 * 调拨管理Controller
 * 
 * @author HUHAO
 * @version 2014-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/key/keyAllocateApply")
public class KeyAllocateApplyController extends BaseController {

	@Autowired
	private KeyAllocateApplyService keyAllocateApplyService;

	@Autowired
	private KeyAllocateApplyDetailedService keyAllocateApplyDetailedService;

	@Autowired
	private KeyUsbKeyDepotService keyUsbKeyDepotService;

	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;

	@Autowired
	private KeyUsbKeyService keyUsbKeyService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private KeyUsbKeyInvoiceService keyUsbKeyInvoiceService;

	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;
	
	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public KeyAllocateApply get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return keyAllocateApplyService.get(id);
		} else {
			return new KeyAllocateApply();
		}
	}

	@RequiresPermissions("key:keyAllocateApply:view")
	@RequestMapping(value = { "list", "" })
	public String list(
			KeyAllocateApply keyAllocateApply,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			keyAllocateApply.setCreateBy(user);
		}
		model.addAttribute("state", state);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		keyAllocateApply.setState(state);
		Page<KeyAllocateApply> page = keyAllocateApplyService.findByDepotName(
				new Page<KeyAllocateApply>(request, response),
				keyAllocateApply, startTime, endTime);

		for (int i = 0; i < page.getList().size(); i++) {
			Long applyId = page.getList().get(i).getId();
			List<KeyAllocateApplyDetailed> keyApplyDatialed = keyAllocateApplyDetailedService
					.findByApplyId(applyId);
			page.getList().get(i).setKeyApplyDetails(keyApplyDatialed);

			int lea = page.getList().get(i).getLeavel();
			switch (lea) {
			case 1:
				page.getList().get(i).setLeavelName("一般");
				break;
			case 2:
				page.getList().get(i).setLeavelName("紧急");
				break;
			}

			int sta = page.getList().get(i).getState();
			switch (sta) {
			case 1:
				page.getList().get(i).setStateName("待审核");
				break;
			case 2:
				page.getList().get(i).setStateName("同意");
				break;
			case 3:
				page.getList().get(i).setStateName("拒绝");
				break;
			case 4:
				page.getList().get(i).setStateName("已到货");
				break;
			default:
				;
			}

		}
		model.addAttribute("page", page);
		return "modules/key/keyAllocateApplyList";
	}

	@RequiresPermissions("key:keyAllocateApply:approval")
	@RequestMapping(value = "assessmentList")
	public String assessmentList(KeyAllocateApply keyAllocateApply,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "depotName", required = false) String depotName,
			@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "office", required = false) Long office,
			Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			keyAllocateApply.setCreateBy(user);
		}
		
		List<Office> officeList = Lists.newArrayList();
		
		officeList.add(user.getOffice());
		List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService
				.findByOfficeIds(officeList);
		
		
		model.addAttribute("state", state);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("officeId", office);
		
		List<Office> offices = officeService.getOfficeByType(UserUtils.getUser(),2);
		model.addAttribute("offices", offices);
		keyAllocateApply.setState(state);
		Page<KeyAllocateApply> page = keyAllocateApplyService
				.assessmentFind(new Page<KeyAllocateApply>(request, response),
						keyAllocateApply, startTime, endTime, office,depots.get(0));

		for (int i = 0; i < page.getList().size(); i++) {
			Long applyId = page.getList().get(i).getId();
			List<KeyAllocateApplyDetailed> keyApplyDatialed = keyAllocateApplyDetailedService
					.findByApplyId(applyId);
			page.getList().get(i).setKeyApplyDetails(keyApplyDatialed);

			int lea = page.getList().get(i).getLeavel();
			switch (lea) {
			case 1:
				page.getList().get(i).setLeavelName("一般");
				break;
			case 2:
				page.getList().get(i).setLeavelName("紧急");
				break;
			}

			int sta = page.getList().get(i).getState();
			switch (sta) {
			case 1:
				page.getList().get(i).setStateName("待审核");
				break;
			case 2:
				page.getList().get(i).setStateName("同意");
				break;
			case 3:
				page.getList().get(i).setStateName("拒绝");
				break;
			case 4:
				page.getList().get(i).setStateName("已到货");
				break;
			default:
				;

			}

		}

		model.addAttribute("depotName", depotName);
		model.addAttribute("page", page);
		return "modules/key/keyAllocateApplyAssessmentList";
	}

	@RequestMapping(value = "listModel")
	public String listConsumerModel(KeyAllocateApply keyAllocateApply,
			Long applyId, Model model) {
		model.addAttribute("applyId", applyId);

		model.addAttribute("keyAllocateApply", keyAllocateApply);
		return "modules/key/AssessmentForm";
	}

	@RequestMapping(value = "badKeyInForm")
	public String badKeyInForm(KeyAllocateApply keyAllocateApply, Long applyId,
			Model model) {
		KeyAllocateApply apply = keyAllocateApplyService.get(applyId);
		List<KeyAllocateApplyDetailed> applyDetials = keyAllocateApplyDetailedService
				.findByApplyId(applyId);
		KeyDepotId badDepot = new KeyDepotId();
		KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(badDepot.BAD_DEPOT_ID);

		model.addAttribute("applyDetials", applyDetials);

		model.addAttribute("apply", apply);
		model.addAttribute("depot", depot);
		return "modules/key/badKeyInForm";
	}

	@RequestMapping(value = "showRefusalReason ")
	public String showRefusalReason(KeyAllocateApply keyAllocateApply,
			Long applyId, Model model) {
		KeyAllocateApply apply = keyAllocateApplyService.get(applyId);
		model.addAttribute("apply", apply);
		return "modules/key/showRefusalReasonForm";
	}

	@RequestMapping(value = "AllocateApplyArrivalShow")
	public String showAllocateApplyArrival(KeyAllocateApply keyAllocateApply,
			Long applyId, Model model) {

		List<KeyAllocateApplyDetailed> kaads = keyAllocateApplyDetailedService
				.findByApplyId(applyId);
		model.addAttribute("kaads", kaads);
		return "modules/key/keyAllocateApplyArrivalShow";
	}

	@RequestMapping(value = "assessmentSave")
	@ResponseBody
	public String save(KeyAllocateApply keyAllocateApply, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response, Long applyId) throws JSONException {
		if (!beanValidator(model, keyAllocateApply)) {
			return form(keyAllocateApply, model,redirectAttributes);
		}
		JSONObject json = new JSONObject();
		try {

			KeyAllocateApply apply = keyAllocateApplyService.get(applyId);
			List<KeyAllocateApplyDetailed> applyDetials = keyAllocateApplyDetailedService
					.findByApplyId(applyId);
			//获得总网点depotId
			if (keyAllocateApply.getApprovalOpinion().equals(0)) {
				for (int i = 0; i < applyDetials.size(); i++) {
					List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
							.findByDepotIdAndGeneId(KeyDepotId.MAIN_DEPOT_ID, applyDetials.get(i)
									.getKeyGeneralInfo().getId());
					if (staList.size() > 0) {
						KeyDepotGeneralStatistics sta = staList.get(0);
						if (sta.getInCount() > applyDetials.get(i)
								.getApplyNewNum()) {
						}else if(sta.getInCount().equals( applyDetials.get(i)
								.getApplyNewNum())){

							json.put("status", "3");
							return json.toString();
						} else {
							addMessage(redirectAttributes,
									"审批失败，请采购网点所需要的Key类型产品库存不足，请采购！");
							json.put("status", "2");
							return json.toString();
						}
					} else {
						addMessage(redirectAttributes, "审批失败，请采购网点所需要的Key类型产品！");
						json.put("status", "0");
						return json.toString();
					}
				}
			} else {
				apply.setRefusalReason(keyAllocateApply.getRefusalReason());
				apply.setApprovalOpinion(keyAllocateApply.getApprovalOpinion());
				apply.setState(3);
				keyAllocateApplyService.save(apply);

				addMessage(redirectAttributes, "审批成功！");
				json.put("status", "1");
				return json.toString();
			}

			for (int i = 0; i < applyDetials.size(); i++) {
				List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
						.findByDepotIdAndGeneId(KeyDepotId.MAIN_DEPOT_ID, applyDetials.get(i)
								.getKeyGeneralInfo().getId());

				KeyDepotGeneralStatistics sta = staList.get(0);

				if (i == applyDetials.size() - 1) {
					apply.setState(2);
					apply.setRemarks(keyAllocateApply.getRemarks());
					apply.setApprovalOpinion(keyAllocateApply
							.getApprovalOpinion());
					keyAllocateApplyService.save(apply);
				}

				KeyUsbKeyInvoice out = new KeyUsbKeyInvoice();// 创建出库对象
				out.setDeliveryNum(applyDetials.get(i).getApplyNewNum());// 获取申请的数量
				out.setOutReason(2);// 赋予出库原因
				out.setKeyGeneralInfo(applyDetials.get(i).getKeyGeneralInfo());// 申请的置换类型
				
				KeyDepotId mainDepot = new KeyDepotId();
				
				KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(mainDepot.MAIN_DEPOT_ID);
				out.setKeyUsbKeyDepot(depot);
				out.setKeyUsbKeyDepotReceive(apply.getKeyUsbKeyDepot());
				out.setStartDate(new Date());
				keyUsbKeyInvoiceService.save(out);

				int outcount = sta.getOutCount()
						+ applyDetials.get(i).getApplyNewNum();
				sta.setOutCount(outcount);
				sta.setInCount(sta.getTotalCount() - outcount);
				keyDepotGeneralStatisticsService.save(sta);

				if (apply.getApplyType().equals(2)) {

					KeyUsbKeyInvoice outwd = new KeyUsbKeyInvoice();
					outwd.setDeliveryNum(applyDetials.get(i).getBadKeyNum());
					outwd.setOutReason(3);
					outwd.setKeyGeneralInfo(applyDetials.get(i)
							.getBadKeyGeneralInfo());
					outwd.setKeyUsbKeyDepot(apply.getKeyUsbKeyDepot());
					outwd.setKeyUsbKeyDepotReceive(keyUsbKeyDepotService
							.get(KeyDepotId.BAD_DEPOT_ID));
					outwd.setStartDate(new Date());
					keyUsbKeyInvoiceService.save(outwd);

					Long ii = apply.getKeyUsbKeyDepot().getId();
					Long jj = applyDetials.get(i).getBadKeyGeneralInfo().getId();
					List<KeyDepotGeneralStatistics> staofficeList = keyDepotGeneralStatisticsService
							.findByDepotIdAndGeneId(apply.getKeyUsbKeyDepot()
									.getId(), applyDetials.get(i)
									.getBadKeyGeneralInfo().getId());
					KeyDepotGeneralStatistics staoffice = staofficeList.get(0);

					int outNum = staoffice.getOutCount()
							+ applyDetials.get(i).getBadKeyNum();
					staoffice.setOutCount(outNum);
					staoffice.setInCount(staoffice.getTotalCount() - outNum);
					keyDepotGeneralStatisticsService.save(staoffice);

				}

			}

			addMessage(redirectAttributes, "审批成功！");
			json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "审批失败！");
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequestMapping(value = "badKeyInSave")
	@ResponseBody
	public String badKeyInSave(KeyAllocateApply keyAllocateApply, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response, Long applyId) throws JSONException {
		if (!beanValidator(model, keyAllocateApply)) {
			return form(keyAllocateApply, model,redirectAttributes);
		}
		JSONObject json = new JSONObject();
		try {
			KeyAllocateApply apply = keyAllocateApplyService.get(applyId);

			List<KeyAllocateApplyDetailed> applyDetials = keyAllocateApplyDetailedService
					.findByApplyId(applyId);

			for (int i = 0; i < applyDetials.size(); i++) {
				KeyUsbKey kuk = new KeyUsbKey();
				kuk.setInReason(4);
				KeyDepotId badDepot = new KeyDepotId();
				
				KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(badDepot.BAD_DEPOT_ID);
				kuk.setKeyUsbKeyDepot(depot);
				kuk.setCount(applyDetials.get(i).getBadKeyNum());
				kuk.setKeyGeneralInfo(applyDetials.get(i)
						.getBadKeyGeneralInfo());
				
				kuk.setFromkeyUsbKeyDepot(apply.getKeyUsbKeyDepot());
				
				kuk.setStartDate(new Date());
				keyUsbKeyService.save(kuk);
				apply.setIsBadKeyIn(0);
				keyAllocateApplyService.save(apply);
				List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
						.findByDepotIdAndGeneId(KeyDepotId.BAD_DEPOT_ID, applyDetials.get(i)
								.getBadKeyGeneralInfo().getId());
				if (staList.size() > 0) {
					KeyDepotGeneralStatistics sta = staList.get(0);
					int total = sta.getTotalCount();
					int intotal = sta.getInCount();
					sta.setTotalCount(total
							+ applyDetials.get(i).getBadKeyNum());
					sta.setInCount(intotal + applyDetials.get(i).getBadKeyNum());
					keyDepotGeneralStatisticsService.save(sta);
				} else {
					KeyDepotGeneralStatistics statistic = new KeyDepotGeneralStatistics();
					statistic.setKeyUsbKeyDepot(depot);
					statistic.setKeyGeneralInfo(applyDetials.get(i)
							.getBadKeyGeneralInfo());
					statistic.setTotalCount(applyDetials.get(i).getBadKeyNum());
					statistic.setInCount(applyDetials.get(i).getBadKeyNum());
					statistic.setOutCount(0);
					keyDepotGeneralStatisticsService.save(statistic);
				}
			}

			addMessage(redirectAttributes, "坏key入库成功！");
			logUtil.saveSysLog("库存管理", "坏Key入库成功", "");
			json.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();

			addMessage(redirectAttributes, "坏key入库失败！");
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequiresPermissions("key:keyAllocateApply:view")
	@RequestMapping(value = "form")
	public String form(KeyAllocateApply keyAllocateApply, Model model,RedirectAttributes redirectAttributes) {

		User user = UserUtils.getUser();
		/*List<Office> offices = officeService.getOfficeByType(user, 2);
		List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService
				.findByOfficeIds(offices);*/
		
		//改:之前是user所能看到的网点，现在改为user所属网点
		
		List<Office> offices = Lists.newArrayList();
		
		offices.add(user.getOffice());
		
		List<KeyUsbKeyDepot> depots = keyUsbKeyDepotService
				.findByOfficeIds(offices);
		
		if (depots.size() < 1) {
			model.addAttribute("message", "您所属的网点没有仓库，请您添加仓库后，再进行调拨申请！");
		} else {
			model.addAttribute("depots", depots);
			model.addAttribute("depotId", depots.get(0).getId());
		}

		//实际情况是 一个网点下只有一个库房，所以我们可以得到调拨申请的库房
		KeyUsbKeyDepot depot = depots.get(0);
		if(depot.getParent()==null){
			addMessage(redirectAttributes, "所在库房【"+depot.getDepotName()+"】没有上级库房,无法进行调拨!");
			return "redirect:" + Global.getAdminPath() + "/key/keyAllocateApply/?repage";
		}
		
		model.addAttribute("parentDepot", depot.getParent());
		
		List<Long> depotIdList = Lists.newArrayList();
		/*depotIdList.add(KeyDepotId.MAIN_DEPOT_ID);*/

		//改：之前所有调拨都从四川CA总库房调拨，现在改为所在网点库房从上级库房调拨
		depotIdList.add(depot.getParent().getId());
		
		List<KeyUsbKey> kuksKeys = keyUsbKeyService.findByDepotIds(depotIdList);
		List<Long> geneIds = Lists.newArrayList();
		for (int i = 0; i < kuksKeys.size(); i++) {
			geneIds.add(kuksKeys.get(i).getKeyGeneralInfo().getId());
		}
		List<KeyGeneralInfo> geneList = new ArrayList<KeyGeneralInfo>();
		if (kuksKeys.size() != 0) {
			geneList = keyGeneralInfoService.findByGeneIds(geneIds);
		}
		model.addAttribute("geneList", geneList);
		model.addAttribute("office", user.getOffice());
		model.addAttribute("keyAllocateApply", keyAllocateApply);
		return "modules/key/keyAllocateApplyForm";
	}

	@RequestMapping(value = "arrivalSave")
	@ResponseBody
	public String arrivalSave(Long applyId) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			KeyAllocateApply apply = keyAllocateApplyService.get(applyId);
			apply.setState(4);
			keyAllocateApplyService.save(apply);
			List<KeyAllocateApplyDetailed> applyDetials = keyAllocateApplyDetailedService
					.findByApplyId(applyId);
			for (int i = 0; i < applyDetials.size(); i++) {
				KeyUsbKey kuk = new KeyUsbKey();
				kuk.setInReason(1);
				kuk.setKeyUsbKeyDepot(apply.getKeyUsbKeyDepot());
				kuk.setCount(applyDetials.get(i).getApplyNewNum());
				kuk.setKeyGeneralInfo(applyDetials.get(i).getKeyGeneralInfo());
				kuk.setStartDate(new Date());
				keyUsbKeyService.save(kuk);

				List<KeyDepotGeneralStatistics> staList = keyDepotGeneralStatisticsService
						.findByDepotIdAndGeneId(apply.getKeyUsbKeyDepot()
								.getId(), applyDetials.get(i)
								.getKeyGeneralInfo().getId());
				if (staList.size() > 0) {
					KeyDepotGeneralStatistics sta = staList.get(0);
					int total = sta.getTotalCount();
					int intotal = sta.getInCount();
					sta.setTotalCount(total
							+ applyDetials.get(i).getApplyNewNum());
					sta.setInCount(intotal
							+ applyDetials.get(i).getApplyNewNum());
					keyDepotGeneralStatisticsService.save(sta);

				} else {
					KeyDepotGeneralStatistics statistic = new KeyDepotGeneralStatistics();
					statistic.setKeyUsbKeyDepot(apply.getKeyUsbKeyDepot());
					statistic.setKeyGeneralInfo(applyDetials.get(i)
							.getKeyGeneralInfo());
					statistic.setTotalCount(applyDetials.get(i)
							.getApplyNewNum());
					statistic.setInCount(applyDetials.get(i).getApplyNewNum());
					statistic.setOutCount(0);
					keyDepotGeneralStatisticsService.save(statistic);
				}
			}
			logUtil.saveSysLog("库存管理", apply.getLeavelName()+"确认到货成功", "");
			json.put("status", "1");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
	}

	@RequiresPermissions("key:keyAllocateApply:edit")
	@RequestMapping(value = "save")
	public String save(KeyAllocateApply keyAllocateApply, Model model,
			RedirectAttributes redirectAttributes, Long depotId,
			String badKeyType, String applyNewNum, Integer applyType,
			String applyKeyType) {
		if (!beanValidator(model, keyAllocateApply)) {
			// return form(keyAllocateApply, model);
		}
		User user = UserUtils.getUser();
		Office office = user.getOffice();
		keyAllocateApply.setSysOffice(office);
		keyAllocateApply.setApplyType(applyType);

		String[] applyNewNums = applyNewNum.split(",");
		String[] applyKeyTypes = applyKeyType.split(",");
		String[] badKeyTypeYZs =null;
		if (badKeyType!=null) {
			badKeyTypeYZs = badKeyType.split(",");
		}
		if (applyType.equals(2)) {
			for (int i = 0; i < applyNewNums.length; i++) {
				
				List<KeyDepotGeneralStatistics> statistics =	keyDepotGeneralStatisticsService.findByDepotIdAndGeneId(depotId, Long.parseLong(badKeyTypeYZs[i]));
				if(statistics.size()<1){
					addMessage(redirectAttributes, "保存调拨管理失败，该网点库存中没有该key类型请先调拨申请！");
					model.addAttribute("keyAllocateApply", keyAllocateApply);
					model.addAttribute("depotId", depotId);
					return "redirect:" + Global.getAdminPath()+ "/key/keyAllocateApply/form";
				}
					if (statistics.get(0).getInCount()<Integer.parseInt(applyNewNums[i])) {
						addMessage(redirectAttributes, "保存调拨管理失败，坏KEY置换的类型'"+statistics.get(0).getKeyGeneralInfo().getName()+"'的数量超过库存剩余量，请重新填写坏key置换调拨申请！");
						model.addAttribute("keyAllocateApply", keyAllocateApply);
						model.addAttribute("depotId", depotId);
						return "redirect:" + Global.getAdminPath()+ "/key/keyAllocateApply/form";
					}
				
			}
			
			
		}
		if (applyType.equals(2)) {
			KeyGeneralInfo badGene = new KeyGeneralInfo();
			// badGene.setId(applyKeyType);
			// keyAllocateApply.setBadKeyGeneralInfo(badGene);
			keyAllocateApply.setIsBadKeyIn(1);

		}

		
		
		KeyUsbKeyDepot depot = keyUsbKeyDepotService.get(depotId);
		keyAllocateApply.setKeyUsbKeyDepot(depot);
		keyAllocateApply.setAuditKeyDepotId(depot.getParent().getId());

		keyAllocateApply.setState(1);
		
		keyAllocateApply.setAreaId(UserUtils.getUser().getOffice().getParent().getId());
		keyAllocateApply.setOfficeId(UserUtils.getUser().getOffice().getId());
		
		keyAllocateApplyService.save(keyAllocateApply);

		

		String[] badKeyTypes = null;
		if (applyType.equals(2)) {

			badKeyTypes = badKeyType.split(",");
		}

		for (int i = 0; i < applyNewNums.length; i++) {
			KeyAllocateApplyDetailed allocateApplyDetailed = new KeyAllocateApplyDetailed();

			Integer num = Integer.parseInt(applyNewNums[i]);
			allocateApplyDetailed.setApplyNewNum(num);

			Long type = Long.parseLong(applyKeyTypes[i]);
			KeyGeneralInfo keyGeneralInfo = keyGeneralInfoService.get(type);
			allocateApplyDetailed.setKeyGeneralInfo(keyGeneralInfo);

			if (applyType.equals(2)) {

				allocateApplyDetailed.setBadKeyNum(num);

				Long badType = Long.parseLong(badKeyTypes[i]);
				KeyGeneralInfo badKeyInfo = keyGeneralInfoService.get(badType);
				allocateApplyDetailed.setBadKeyGeneralInfo(badKeyInfo);

			}

			allocateApplyDetailed.setKeyAllocateApply(keyAllocateApply);

			keyAllocateApplyDetailedService.save(allocateApplyDetailed);
		}

		// addMessage(redirectAttributes, "保存调拨管理'" + keyAllocateApply.getName()
		// + "'成功");
		logUtil.saveSysLog("库存管理", "保存调拨管理成功", "");
		return "redirect:" + Global.getAdminPath()
				+ "/key/keyAllocateApply/?repage";
	}

	@RequiresPermissions("key:keyAllocateApply:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		keyAllocateApplyService.delete(id);
		addMessage(redirectAttributes, "删除调拨管理成功");
		return "redirect:" + Global.getAdminPath()
				+ "/modules/key/keyAllocateApply/?repage";
	}

	@RequestMapping(value = "checkSP")
	@ResponseBody
	public String checkSP(Long depotId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject json = new JSONObject();
		try {
			List<KeyAllocateApply> applys = keyAllocateApplyService
					.findByDepotId(depotId);
			int state = 0;
			for (int i = 0; i < applys.size(); i++) {

				if (!applys.get(i).getState().equals(4)) {
					state = 1;
					break;
				}

			}
			if (state == 0) {
				json.put("status", "1");
			} else {
				json.put("status", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return json.toString();
	}

}
