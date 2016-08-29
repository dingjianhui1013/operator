package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.web.KeyUsbKeyController;
import com.itrus.ca.modules.profile.entity.ConfigAgentAppRelation;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigCommercialAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigCommercialAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.settle.vo.PayableDetailVo;
import com.itrus.ca.modules.settle.vo.SettleCollectVO;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 年限结算表
 * 
 * @author huhao
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/settlePayableDetail")
public class SettlePayableDetailController extends BaseController {
	static Log log = LogFactory.getLog(SettlePayableDetailController.class);
	@Autowired
	private ConfigCommercialAgentService configCommercialAgentService;

	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigChargeAgentService configChargeAgentService;

	@Autowired
	private ConfigProductService configProductService;

	@RequiresPermissions("work:settlePayableDetail:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String settlePayableDetailList(Model model) {
		List<ConfigCommercialAgent> comAgents = configCommercialAgentService
				.findAllNameByType(1);
		model.addAttribute("comAgents", comAgents);
		model.addAttribute("startTime", DateUtils.firstDayOfMonth(new Date()));
		model.addAttribute("endTime", new Date());

		return "modules/settle/settlePayableDetail";
	}

	@RequiresPermissions("work:settlePayableDetail:view")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public String settlePayableDetailList(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime

	) {
		List<ConfigCommercialAgent> comAgents = configCommercialAgentService
				.findAllNameByType(1);
		model.addAttribute("comAgents", comAgents);
		if (comAgentId == null) {

			return "modules/settle/settlePayableDetail";
		}
		model.addAttribute("appId", appId);
		model.addAttribute("productIds", productIds);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("comAgentId", comAgentId);
		model.addAttribute("proType", ProductType.productTypeStrMap);

		List<ConfigAgentAppRelation> relationByComAgentId = configAgentAppRelationService
				.findByComAgentId(comAgentId);
		model.addAttribute("relationByComAgentId", relationByComAgentId);

		if (appId != null) {
			List<ConfigProduct> products = configProductService
					.findByAppId(appId);
			model.addAttribute("products", products);
		}

		ConfigCommercialAgent comAgent = configCommercialAgentService
				.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId == null) {
			List<ConfigAgentAppRelation> relation = configAgentAppRelationService
					.findByComAgentId(comAgentId);
			for (int i = 0; i < relation.size(); i++) {
				appIds.add(relation.get(i).getConfigApp().getId());
			}
		}
		List<Long> productIdList = new ArrayList<Long>();

		if (productIds == null || productIds.equals("")) {
			List<ConfigProduct> products = configProductService
					.findByAppId(appId);
			for (int i = 0; i < products.size(); i++) {
				productIdList.add(products.get(i).getId());
			}
		} else {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				productIdList.add(Long.parseLong(products[i].toString()));
			}
		}

		model.addAttribute("productIdList", productIdList);

		/**
		 * 重新修改结算方法
		 * 
		 */
		List<SettleCollectVO> collect = Lists.newArrayList();
		for (Long productId : productIdList) {
			ConfigProduct product = configProductService.get(productId);
			SettleCollectVO vo = new SettleCollectVO(product.getProductName(),
					product.getProductLabel());
			vo.setProductName(ProductType.productTypeStrMap.get(product
					.getProductName())
					+ "["
					+ (product.getProductLabel() == 0 ? "通用" : "专用") + "]");
			collect.add(vo);
		}

		// 先得到业务办理时间范围内所有的新增和更新 然后找到每个业务链的头部，和代理商合同有效期比较，符合条件放入list
		List<WorkDealInfo> dealInfos = workDealInfoService.findDealInfoByAdd(
				appId, appIds, productIdList, startTime, endTime, new Date(
						comAgent.getAgentContractStart().getTime()), new Date(
						comAgent.getAgentContractEnd().getTime()));

		// 得到此时间段内更新的业务
		List<WorkDealInfo> dealInfoUpdates = workDealInfoService
				.findDealInfoByUpdate(appId, appIds, productIdList, startTime,
						endTime);

		// 循环更新业务,如果更新业务所在业务链的首条业务在合同有效期范围之内，则放入set
		for (WorkDealInfo info : dealInfoUpdates) {

			WorkDealInfo first = workDealInfoService
					.findFirstByFirstCertSN(info.getFirstCertSN());
			if (first == null) {
				continue;
			}
			if (first.getBusinessCardUserDate().after(
					new Date(comAgent.getAgentContractStart().getTime()))
					&& first.getBusinessCardUserDate().before(
							new Date(comAgent.getAgentContractEnd().getTime()))) {
				dealInfos.add(first);
			}
		}

		Integer lenth = 0;

		for (int i = 0; i < dealInfos.size(); i++) {
			WorkDealInfo dealInfo = dealInfos.get(i);
			// 结算年限
			int totalAgentYear = comAgent.getSettlementPeriod();

			Calendar firstCalendar = Calendar.getInstance();
			firstCalendar.setTime(dealInfo.getBusinessCardUserDate());
			firstCalendar.set(Calendar.HOUR_OF_DAY, 0);
			firstCalendar.set(Calendar.SECOND, 0);
			firstCalendar.set(Calendar.MINUTE, 0);

			Date firstDealBusiness = firstCalendar.getTime();

			// endLastDate 最终截止时间:业务链首条的办理时间+代理商的结算年限
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(firstDealBusiness);
			calendar.add(Calendar.DAY_OF_YEAR, totalAgentYear * 365);
			Date endLastDate = calendar.getTime();

			// 找到整个业务链
			List<WorkDealInfo> infos = workDealInfoService
					.findChainByFirstCertSN(dealInfo.getFirstCertSN(), endTime,
							endLastDate);

			List<PayableDetailVo> detailList = new ArrayList<PayableDetailVo>();

			int yjNum = 0; // 已结算
			int lastNum = 0; // 本次结算
			int waitNum = 0; // 待结算 待结算就用最后一笔业务的到期时间和最终截止时间作比较

			// 循环整个业务链 除了最后一条 最后一条是本期结算的数据所以单算
			for (int j = 0; j < infos.size() - 1; j++) {
				WorkDealInfo prvedDealInfo = infos.get(j);
				if (infos.get(j).getNotafter() == null) {
					continue;
				}

				Calendar preNotAfterCalendar = Calendar.getInstance();
				preNotAfterCalendar.setTime(prvedDealInfo.getNotafter());
				preNotAfterCalendar.set(Calendar.HOUR_OF_DAY, 0);
				preNotAfterCalendar.set(Calendar.SECOND, 0);
				preNotAfterCalendar.set(Calendar.MINUTE, 0);

				Date prvedDealNotAfter = preNotAfterCalendar.getTime();

				PayableDetailVo detailVo = new PayableDetailVo();

				// 通过业务绑定的计费策略模板得到是标准,政府统一采购还是合同采购

				ConfigChargeAgent agent = configChargeAgentService
						.get(prvedDealInfo.getConfigChargeAgentId());
				if (agent != null) {
					detailVo.setMethod(Integer.parseInt(agent.getTempStyle()));
				}

				// 得到业务类型
				String dealInfoType = "";
				if (prvedDealInfo.getDealInfoType() != null) {
					dealInfoType = WorkDealInfoType.WorkDealInfoTypeMapNew
							.get(prvedDealInfo.getDealInfoType()) + " ";
				}
				if (prvedDealInfo.getDealInfoType1() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
							.get(prvedDealInfo.getDealInfoType1()) + " ";
				}
				if (prvedDealInfo.getDealInfoType2() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
							.get(prvedDealInfo.getDealInfoType2()) + " ";
				}
				if (prvedDealInfo.getDealInfoType3() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
							.get(prvedDealInfo.getDealInfoType3());
				}
				detailVo.setDealInfoType(dealInfoType); // 业务类型
				detailVo.setStartDate(prvedDealInfo.getBusinessCardUserDate()); // 起始时间
				detailVo.setEndDate(prvedDealInfo.getNotafter()); // 到期时间

				// 如果业务类型dealInfoType不为空 结算年限为0
				if (prvedDealInfo.getDealInfoType() == null) {
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}

				// 如果业务类型dealInfoType不为空 不是新增或更新 结算年限为0
				if (!prvedDealInfo.getDealInfoType().equals(1)
						&& !prvedDealInfo.getDealInfoType().equals(0)) {
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}

				// 如果模板类型不是标准, 则结算年限为0
				if (!agent.getTempStyle().equals("1")) {
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}

				// 如果业务到期时间在最终截止日期之内,则参与结算 结算年限为证书办理年限
				if (prvedDealNotAfter.getTime() < endLastDate.getTime()) {
					yjNum += prvedDealInfo.getYear();
					detailVo.setSettleYear(prvedDealInfo.getYear().toString());
					detailList.add(detailVo);
				}

			}

			// 本期需要结算的 首先肯定是新增或者更新的
			WorkDealInfo currentDealInfo = infos.get(infos.size() - 1);

			Calendar currentBussinessCalendar = Calendar.getInstance();
			currentBussinessCalendar.setTime(currentDealInfo
					.getBusinessCardUserDate());
			currentBussinessCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentBussinessCalendar.set(Calendar.SECOND, 0);
			currentBussinessCalendar.set(Calendar.MINUTE, 0);

			Date currentDealBusiness = currentBussinessCalendar.getTime();

			Calendar currentNotAfterCalendar = Calendar.getInstance();
			currentNotAfterCalendar.setTime(currentDealInfo.getNotafter());
			currentNotAfterCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentNotAfterCalendar.set(Calendar.SECOND, 0);
			currentNotAfterCalendar.set(Calendar.MINUTE, 0);

			Date currentDealNotAfter = currentNotAfterCalendar.getTime();

			PayableDetailVo detailVo = new PayableDetailVo();
			ConfigChargeAgent agent = configChargeAgentService
					.get(currentDealInfo.getConfigChargeAgentId());

			if (agent != null) {
				detailVo.setMethod(Integer.parseInt(agent.getTempStyle()));
			}

			// 得到业务类型
			String dealInfoType = "";
			if (currentDealInfo.getDealInfoType() != null) {
				dealInfoType = WorkDealInfoType.WorkDealInfoTypeMapNew
						.get(currentDealInfo.getDealInfoType()) + " ";
			}
			if (currentDealInfo.getDealInfoType1() != null) {
				dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
						.get(currentDealInfo.getDealInfoType1()) + " ";
			}
			if (currentDealInfo.getDealInfoType2() != null) {
				dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
						.get(currentDealInfo.getDealInfoType2()) + " ";
			}
			if (currentDealInfo.getDealInfoType3() != null) {
				dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
						.get(currentDealInfo.getDealInfoType3());
			}
			detailVo.setDealInfoType(dealInfoType); // 业务类型
			detailVo.setStartDate(currentDealInfo.getBusinessCardUserDate()); // 起始时间
			detailVo.setEndDate(currentDealInfo.getNotafter()); // 到期时间

			// 如果模板类型不是标准, 则结算年限为0
			if (!agent.getTempStyle().equals("1")) {

				if (currentDealBusiness != null) {
					if (currentDealNotAfter.getTime() < endLastDate.getTime()) {
						long between = endLastDate.getTime()
								- currentDealNotAfter.getTime();
						long a = between / 31536000000L;
						waitNum += (int) Math.ceil(a + 1);
					} else if (currentDealBusiness.getTime() < endLastDate
							.getTime()
							&& currentDealNotAfter.getTime() >= endLastDate
									.getTime()) {
						waitNum = 0;
					}

					detailVo.setSettleYear("0");
					detailList.add(detailVo);
				}

			}
			// 如果业务到期时间在最终截止日期之内,则参与结算 结算年限为证书办理年限
			else if (currentDealNotAfter.getTime() < endLastDate.getTime()) {
				long between = endLastDate.getTime()
						- currentDealNotAfter.getTime();
				long a = between / 31536000000L;
				waitNum += (int) Math.ceil(a + 1);
				lastNum += currentDealInfo.getYear();
				detailVo.setSettleYear(currentDealInfo.getYear().toString());
				detailList.add(detailVo);
			}

			// 如果业务制证时间在最终截止日期之内而业务到期时间在最终截止日期之外,结算年限为
			else if (currentDealBusiness.getTime() < endLastDate.getTime()
					&& currentDealNotAfter.getTime() >= endLastDate.getTime()) {
				long between = endLastDate.getTime()
						- currentDealBusiness.getTime();
				long a = between / 31536000000L;
				int yy = (int) Math.ceil(a + 1);
				lastNum += yy;
				waitNum = 0;
				detailVo.setSettleYear(yy + "");
				detailList.add(detailVo);
			}

			// 汇总
			for (SettleCollectVO vo : collect) {
				if (currentDealInfo.getConfigProduct().getProductName()
						.equals(vo.getProductId())
						&& currentDealInfo.getConfigProduct().getProductLabel() == vo
								.getProductLabel()) {

					if (currentDealInfo.getDealInfoType().equals(
							WorkDealInfoType.TYPE_ADD_CERT)) {
						if (lastNum == 1) {
							vo.setAdd1(vo.getAdd1() + 1);
						}
						if (lastNum == 2) {
							vo.setAdd2(vo.getAdd2() + 1);
						}
						if (lastNum == 3) {
							vo.setAdd3(vo.getAdd3() + 1);
						}
						if (lastNum == 4) {
							vo.setAdd4(vo.getAdd4() + 1);
						}
						if (lastNum == 5) {
							vo.setAdd5(vo.getAdd5() + 1);
						}
					} else if (currentDealInfo.getDealInfoType().equals(
							WorkDealInfoType.TYPE_UPDATE_CERT)) {
						if (lastNum == 1) {
							vo.setUpdate1(vo.getUpdate1() + 1);
						}
						if (lastNum == 2) {
							vo.setUpdate2(vo.getUpdate2() + 1);
						}
						if (lastNum == 3) {
							vo.setUpdate3(vo.getUpdate3() + 1);
						}
						if (lastNum == 4) {
							vo.setUpdate4(vo.getUpdate4() + 1);
						}
						if (lastNum == 5) {
							vo.setUpdate5(vo.getUpdate5() + 1);
						}
					}

				}
			}

			dealInfos.get(i).setTotalNum(totalAgentYear); // 总年限

			dealInfos.get(i).setWaitNum(waitNum); // 待结算年限

			dealInfos.get(i).setYyNum(yjNum); // 已结算年限

			dealInfos.get(i).setLastNum(lastNum); // 本次结算年限

			dealInfos.get(i).setDetailList(detailList);

		}
		for (int k = dealInfos.size() - 1; k >= 0; k--) {
			WorkDealInfo deal = dealInfos.get(k);
			List<PayableDetailVo> detailVos = deal.getDetailList();
			int payType = 0;
			if (detailVos.size() < 1) {
				dealInfos.remove(k);
			} else {
				for (int j = detailVos.size() - 1; j >= 0; j--) {
					if (detailVos.get(j).getMethod().equals(1)) {
						payType = 1;
						break;
					}
				}
			}
			if (payType == 0) {
				dealInfos.remove(k);
			}
		}

		for (WorkDealInfo info : dealInfos) {
			if (info.getDetailList().size() > lenth) {
				lenth = info.getDetailList().size();
			}
		}

		model.addAttribute("dealInfos", dealInfos);
		model.addAttribute("collect", collect);
		model.addAttribute("lenth", lenth);
		return "modules/settle/settlePayableDetail";
	}

	@RequestMapping(value = "dcPayableDetail")
	public void dcPayableDetail(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "comAgentId", required = false) Long comAgentId,
			@RequestParam(value = "appId", required = false) Long appId,
			@RequestParam(value = "productIds", required = false) String productIds,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime) {

		ConfigCommercialAgent comAgent = configCommercialAgentService
				.get(comAgentId);
		List<Long> appIds = new ArrayList<Long>();
		if (appId == null) {
			List<ConfigAgentAppRelation> relation = configAgentAppRelationService
					.findByComAgentId(comAgentId);
			for (int i = 0; i < relation.size(); i++) {
				appIds.add(relation.get(i).getConfigApp().getId());
			}
		}

		List<Long> productIdList = new ArrayList<Long>();

		if (productIds == null || productIds.equals("")) {
			List<ConfigProduct> products = configProductService
					.findByAppId(appId);
			for (int i = 0; i < products.size(); i++) {
				productIdList.add(products.get(i).getId());
			}
		} else {
			String[] products = productIds.split(",");
			for (int i = 0; i < products.length; i++) {
				productIdList.add(Long.parseLong(products[i].toString()));
			}
		}
		model.addAttribute("productIdList", productIdList);

		List<SettleCollectVO> collect = Lists.newArrayList();
		for (Long productId : productIdList) {
			ConfigProduct product = configProductService.get(productId);
			SettleCollectVO vo = new SettleCollectVO(product.getProductName(),
					product.getProductLabel());
			vo.setProductName(ProductType.productTypeStrMap.get(product
					.getProductName())
					+ "["
					+ (product.getProductLabel() == 0 ? "通用" : "专用") + "]");
			collect.add(vo);
		}
		// 先得到业务办理时间范围内所有的新增和更新 然后找到每个业务链的头部，和代理商合同有效期比较，符合条件放入list
		List<WorkDealInfo> dealInfos = workDealInfoService.findDealInfoByAdd(
				appId, appIds, productIdList, startTime, endTime, new Date(
						comAgent.getAgentContractStart().getTime()), new Date(
						comAgent.getAgentContractEnd().getTime()));

		// 得到此时间段内更新的业务
		List<WorkDealInfo> dealInfoUpdates = workDealInfoService
				.findDealInfoByUpdate(appId, appIds, productIdList, startTime,
						endTime);

		// 循环更新业务,如果更新业务所在业务链的首条业务在合同有效期范围之内，则放入set
		for (WorkDealInfo info : dealInfoUpdates) {

			WorkDealInfo first = workDealInfoService
					.findFirstByFirstCertSN(info.getFirstCertSN());

			if (first.getBusinessCardUserDate().after(
					new Date(comAgent.getAgentContractStart().getTime()))
					&& first.getBusinessCardUserDate().before(
							new Date(comAgent.getAgentContractEnd().getTime()))) {
				dealInfos.add(first);
			}
		}

		Integer lenth = 0;

		for (int i = 0; i < dealInfos.size(); i++) {
			WorkDealInfo dealInfo = dealInfos.get(i);
			// 结算年限
			int totalAgentYear = comAgent.getSettlementPeriod();

			Calendar firstCalendar = Calendar.getInstance();
			firstCalendar.setTime(dealInfo.getBusinessCardUserDate());
			firstCalendar.set(Calendar.HOUR_OF_DAY, 0);
			firstCalendar.set(Calendar.SECOND, 0);
			firstCalendar.set(Calendar.MINUTE, 0);

			Date firstDealBusiness = firstCalendar.getTime();

			// endLastDate 最终截止时间:业务链首条的办理时间+代理商的结算年限
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(firstDealBusiness);
			calendar.add(Calendar.DAY_OF_YEAR, totalAgentYear * 365);
			Date endLastDate = calendar.getTime();

			// 找到整个业务链
			List<WorkDealInfo> infos = workDealInfoService
					.findChainByFirstCertSN(dealInfo.getFirstCertSN(), endTime,
							endLastDate);

			List<PayableDetailVo> detailList = new ArrayList<PayableDetailVo>();

			int yjNum = 0; // 已结算
			int lastNum = 0; // 本次结算
			int waitNum = 0; // 待结算 待结算就用最后一笔业务的到期时间和最终截止时间作比较

			// 循环整个业务链 除了最后一条 最后一条是本期结算的数据所以单算
			for (int j = 0; j < infos.size() - 1; j++) {
				WorkDealInfo prvedDealInfo = infos.get(j);
				if (infos.get(j).getNotafter() == null) {
					continue;
				}

				Calendar preNotAfterCalendar = Calendar.getInstance();
				preNotAfterCalendar.setTime(prvedDealInfo.getNotafter());
				preNotAfterCalendar.set(Calendar.HOUR_OF_DAY, 0);
				preNotAfterCalendar.set(Calendar.SECOND, 0);
				preNotAfterCalendar.set(Calendar.MINUTE, 0);

				Date prvedDealNotAfter = preNotAfterCalendar.getTime();

				PayableDetailVo detailVo = new PayableDetailVo();

				// 通过业务绑定的计费策略模板得到是标准,政府统一采购还是合同采购

				ConfigChargeAgent agent = configChargeAgentService
						.get(prvedDealInfo.getConfigChargeAgentId());
				if (agent != null) {
					detailVo.setMethod(Integer.parseInt(agent.getTempStyle()));
				}

				// 得到业务类型
				String dealInfoType = "";
				if (prvedDealInfo.getDealInfoType() != null) {
					dealInfoType = WorkDealInfoType.WorkDealInfoTypeMapNew
							.get(prvedDealInfo.getDealInfoType()) + " ";
				}
				if (prvedDealInfo.getDealInfoType1() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
							.get(prvedDealInfo.getDealInfoType1()) + " ";
				}
				if (prvedDealInfo.getDealInfoType2() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
							.get(prvedDealInfo.getDealInfoType2()) + " ";
				}
				if (prvedDealInfo.getDealInfoType3() != null) {
					dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
							.get(prvedDealInfo.getDealInfoType3());
				}
				detailVo.setDealInfoType(dealInfoType); // 业务类型
				detailVo.setStartDate(prvedDealInfo.getBusinessCardUserDate()); // 起始时间
				detailVo.setEndDate(prvedDealInfo.getNotafter()); // 到期时间

				// 如果业务类型dealInfoType不为空 结算年限为0
				if (prvedDealInfo.getDealInfoType() == null) {
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}

				// 如果业务类型dealInfoType不为空 不是新增或更新 结算年限为0
				if (!prvedDealInfo.getDealInfoType().equals(1)
						&& !prvedDealInfo.getDealInfoType().equals(0)) {
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}

				// 如果模板类型不是标准, 则结算年限为0
				if (!agent.getTempStyle().equals("1")) {
					detailVo.setSettleYear("0");
					detailList.add(detailVo);
					continue;
				}

				// 如果业务到期时间在最终截止日期之内,则参与结算 结算年限为证书办理年限
				if (prvedDealNotAfter.getTime() < endLastDate.getTime()) {
					yjNum += prvedDealInfo.getYear();
					detailVo.setSettleYear(prvedDealInfo.getYear().toString());
					detailList.add(detailVo);
				}

			}

			// 本期需要结算的 首先肯定是新增或者更新的
			WorkDealInfo currentDealInfo = infos.get(infos.size() - 1);

			Calendar currentBussinessCalendar = Calendar.getInstance();
			currentBussinessCalendar.setTime(currentDealInfo
					.getBusinessCardUserDate());
			currentBussinessCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentBussinessCalendar.set(Calendar.SECOND, 0);
			currentBussinessCalendar.set(Calendar.MINUTE, 0);

			Date currentDealBusiness = currentBussinessCalendar.getTime();

			Calendar currentNotAfterCalendar = Calendar.getInstance();
			currentNotAfterCalendar.setTime(currentDealInfo.getNotafter());
			currentNotAfterCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currentNotAfterCalendar.set(Calendar.SECOND, 0);
			currentNotAfterCalendar.set(Calendar.MINUTE, 0);

			Date currentDealNotAfter = currentNotAfterCalendar.getTime();

			PayableDetailVo detailVo = new PayableDetailVo();
			ConfigChargeAgent agent = configChargeAgentService
					.get(currentDealInfo.getConfigChargeAgentId());

			if (agent != null) {
				detailVo.setMethod(Integer.parseInt(agent.getTempStyle()));
			}

			// 得到业务类型
			String dealInfoType = "";
			if (currentDealInfo.getDealInfoType() != null) {
				dealInfoType = WorkDealInfoType.WorkDealInfoTypeMapNew
						.get(currentDealInfo.getDealInfoType()) + " ";
			}
			if (currentDealInfo.getDealInfoType1() != null) {
				dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
						.get(currentDealInfo.getDealInfoType1()) + " ";
			}
			if (currentDealInfo.getDealInfoType2() != null) {
				dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
						.get(currentDealInfo.getDealInfoType2()) + " ";
			}
			if (currentDealInfo.getDealInfoType3() != null) {
				dealInfoType += WorkDealInfoType.WorkDealInfoTypeMapNew
						.get(currentDealInfo.getDealInfoType3());
			}
			detailVo.setDealInfoType(dealInfoType); // 业务类型
			detailVo.setStartDate(currentDealInfo.getBusinessCardUserDate()); // 起始时间
			detailVo.setEndDate(currentDealInfo.getNotafter()); // 到期时间

			// 如果模板类型不是标准, 则结算年限为0
			if (!agent.getTempStyle().equals("1")) {

				if (currentDealBusiness != null) {
					if (currentDealNotAfter.getTime() < endLastDate.getTime()) {
						long between = endLastDate.getTime()
								- currentDealNotAfter.getTime();
						long a = between / 31536000000L;
						waitNum += (int) Math.ceil(a + 1);
					} else if (currentDealBusiness.getTime() < endLastDate
							.getTime()
							&& currentDealNotAfter.getTime() >= endLastDate
									.getTime()) {
						waitNum = 0;
					}

					detailVo.setSettleYear("0");
					detailList.add(detailVo);
				}

			}
			// 如果业务到期时间在最终截止日期之内,则参与结算 结算年限为证书办理年限
			else if (currentDealNotAfter.getTime() < endLastDate.getTime()) {
				long between = endLastDate.getTime()
						- currentDealNotAfter.getTime();
				long a = between / 31536000000L;
				waitNum += (int) Math.ceil(a + 1);
				lastNum += currentDealInfo.getYear();
				detailVo.setSettleYear(currentDealInfo.getYear().toString());
				detailList.add(detailVo);
			}

			// 如果业务制证时间在最终截止日期之内而业务到期时间在最终截止日期之外,结算年限为
			else if (currentDealBusiness.getTime() < endLastDate.getTime()
					&& currentDealNotAfter.getTime() >= endLastDate.getTime()) {
				long between = endLastDate.getTime()
						- currentDealBusiness.getTime();
				long a = between / 31536000000L;
				int yy = (int) Math.ceil(a + 1);
				lastNum += yy;
				waitNum = 0;
				detailVo.setSettleYear(yy + "");
				detailList.add(detailVo);
			}

			// 汇总
			for (SettleCollectVO vo : collect) {
				if (currentDealInfo.getConfigProduct().getProductName()
						.equals(vo.getProductId())
						&& currentDealInfo.getConfigProduct().getProductLabel() == vo
								.getProductLabel()) {

					if (currentDealInfo.getDealInfoType().equals(
							WorkDealInfoType.TYPE_ADD_CERT)) {
						if (lastNum == 1) {
							vo.setAdd1(vo.getAdd1() + 1);
						}
						if (lastNum == 2) {
							vo.setAdd2(vo.getAdd2() + 1);
						}
						if (lastNum == 3) {
							vo.setAdd3(vo.getAdd3() + 1);
						}
						if (lastNum == 4) {
							vo.setAdd4(vo.getAdd4() + 1);
						}
						if (lastNum == 5) {
							vo.setAdd5(vo.getAdd5() + 1);
						}
					} else if (currentDealInfo.getDealInfoType().equals(
							WorkDealInfoType.TYPE_UPDATE_CERT)) {
						if (lastNum == 1) {
							vo.setUpdate1(vo.getUpdate1() + 1);
						}
						if (lastNum == 2) {
							vo.setUpdate2(vo.getUpdate2() + 1);
						}
						if (lastNum == 3) {
							vo.setUpdate3(vo.getUpdate3() + 1);
						}
						if (lastNum == 4) {
							vo.setUpdate4(vo.getUpdate4() + 1);
						}
						if (lastNum == 5) {
							vo.setUpdate5(vo.getUpdate5() + 1);
						}
					}

				}
			}

			dealInfos.get(i).setTotalNum(totalAgentYear); // 总年限

			dealInfos.get(i).setWaitNum(waitNum); // 待结算年限

			dealInfos.get(i).setYyNum(yjNum); // 已结算年限

			dealInfos.get(i).setLastNum(lastNum); // 本次结算年限

			dealInfos.get(i).setDetailList(detailList);

		}

		for (int k = dealInfos.size() - 1; k >= 0; k--) {
			WorkDealInfo deal = dealInfos.get(k);
			List<PayableDetailVo> detailVos = deal.getDetailList();
			int payType = 0;
			if (detailVos.size() < 1) {
				dealInfos.remove(k);
			} else {
				for (int j = detailVos.size() - 1; j >= 0; j--) {
					if (detailVos.get(j).getMethod().equals(1)) {
						payType = 1;
						break;
					}
				}
			}
			if (payType == 0) {
				dealInfos.remove(k);
			}
		}

		for (WorkDealInfo info : dealInfos) {
			if (info.getDetailList().size() > lenth) {
				lenth = info.getDetailList().size();
			}
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("年限结算表");

		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 20);
		style.setFont(font);

		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0,
				(short) (5 * lenth + 4 + 2)));
		cell0.setCellValue("统计周期:" + format.format(startTime) + "-"
				+ format.format(endTime));

		cell0.setCellStyle(style);

		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("结算时间:" + format.format(new Date()));

		HSSFRow row2 = sheet.createRow(2);
		row2.createCell(0).setCellValue("业务类型");
		row2.createCell(1).setCellValue("新增");
		row2.createCell(6).setCellValue("更新");

		HSSFRow row3 = sheet.createRow(3);
		row3.createCell(0).setCellValue("结算年限");
		row3.createCell(1).setCellValue("1年");
		row3.createCell(2).setCellValue("2年");
		row3.createCell(3).setCellValue("3年");
		row3.createCell(4).setCellValue("4年");
		row3.createCell(5).setCellValue("5年");
		row3.createCell(6).setCellValue("1年");
		row3.createCell(7).setCellValue("2年");
		row3.createCell(8).setCellValue("3年");
		row3.createCell(9).setCellValue("4年");
		row3.createCell(10).setCellValue("5年");

		for (int i = 4; i < collect.size() + 4; i++) {
			HSSFRow rowi = sheet.createRow(i);
			rowi.createCell(0)
					.setCellValue(collect.get(i - 4).getProductName());
			rowi.createCell(1).setCellValue(collect.get(i - 4).getAdd1());
			rowi.createCell(2).setCellValue(collect.get(i - 4).getAdd2());
			rowi.createCell(3).setCellValue(collect.get(i - 4).getAdd3());
			rowi.createCell(4).setCellValue(collect.get(i - 4).getAdd4());
			rowi.createCell(5).setCellValue(collect.get(i - 4).getAdd5());

			rowi.createCell(6).setCellValue(collect.get(i - 4).getUpdate1());
			rowi.createCell(7).setCellValue(collect.get(i - 4).getUpdate2());
			rowi.createCell(8).setCellValue(collect.get(i - 4).getUpdate3());
			rowi.createCell(9).setCellValue(collect.get(i - 4).getUpdate4());
			rowi.createCell(10).setCellValue(collect.get(i - 4).getUpdate5());
		}

		HSSFRow row = sheet.createRow(collect.size() + 4 + 1);
		row.createCell(0).setCellValue("本期结算证书年限(本次结算年数总数)");

		HSSFRow rowx = sheet.createRow(collect.size() + 4 + 2);
		rowx.createCell(0).setCellValue("序号");
		rowx.createCell(1).setCellValue("单位名称");
		rowx.createCell(2).setCellValue("经办人姓名");
		rowx.createCell(3).setCellValue("产品名称");

		for (int i = 0; i < lenth; i++) {
			rowx.createCell(4 + 5 * i).setCellValue("第" + (i + 1) + "次结算");
		}

		rowx.createCell(5 * lenth + 4).setCellValue("结算年限统计");

		HSSFRow rowy = sheet.createRow(collect.size() + 4 + 3);

		for (int i = 0; i < lenth; i++) {
			rowy.createCell(i * 5 + 4).setCellValue("缴费类型");
			rowy.createCell(i * 5 + 4 + 1).setCellValue("起始时间");
			rowy.createCell(i * 5 + 4 + 2).setCellValue("结束时间");
			rowy.createCell(i * 5 + 4 + 3).setCellValue("业务类型");
			rowy.createCell(i * 5 + 4 + 4).setCellValue("结算(年)");
		}

		rowy.createCell(5 * lenth + 4).setCellValue("已结算(年)");
		rowy.createCell(5 * lenth + 4 + 1).setCellValue("本期结算(年)");
		rowy.createCell(5 * lenth + 4 + 2).setCellValue("剩余结算(年)");

		int startrow = collect.size() + 4 + 4;

		for (int i = startrow; i < dealInfos.size() + startrow; i++) {
			HSSFRow rowi = sheet.createRow(i);

			rowi.createCell(0).setCellValue(i - startrow + 1);
			rowi.createCell(1).setCellValue(
					dealInfos.get(i - startrow).getWorkCompany()
							.getCompanyName());
			rowi.createCell(2).setCellValue(
					dealInfos.get(i - startrow).getWorkCertInfo()
							.getWorkCertApplyInfo().getName());
			rowi.createCell(3).setCellValue(
					ProductType.productTypeStrMap.get(dealInfos
							.get(i - startrow).getConfigProduct()
							.getProductName()));
			for (int j = 0; j < dealInfos.get(i - startrow).getDetailList()
					.size(); j++) {

				if (dealInfos.get(i - startrow).getDetailList().get(j)
						.getMethod().toString().equals("1")) {
					rowi.createCell(j * 5 + 4).setCellValue("标准");
				}
				if (dealInfos.get(i - startrow).getDetailList().get(j)
						.getMethod().toString().equals("2")) {
					rowi.createCell(j * 5 + 4).setCellValue("政府统一采购");
				}
				if (dealInfos.get(i - startrow).getDetailList().get(j)
						.getMethod().toString().equals("3")) {
					rowi.createCell(j * 5 + 4).setCellValue("合同采购");
				}
				if (dealInfos.get(i - startrow).getDetailList().get(j)
						.getMethod() == null) {
					rowi.createCell(j * 5 + 4).setCellValue("123");
				}
				rowi.createCell(j * 5 + 4 + 1).setCellValue(
						dealInfos.get(i - startrow).getDetailList().get(j)
								.getStartDate() == null ? "" : dealInfos
								.get(i - startrow).getDetailList().get(j)
								.getStartDate().toString());
				rowi.createCell(j * 5 + 4 + 2).setCellValue(
						dealInfos.get(i - startrow).getDetailList().get(j)
								.getEndDate() == null ? "" : dealInfos
								.get(i - startrow).getDetailList().get(j)
								.getEndDate().toString());
				rowi.createCell(j * 5 + 4 + 3)
						.setCellValue(
								dealInfos.get(i - startrow).getDetailList()
										.get(j).getDealInfoType() == null
										|| dealInfos.get(i - startrow)
												.getDetailList().get(j)
												.getDealInfoType().equals("") ? ""
										: dealInfos.get(i - startrow)
												.getDetailList().get(j)
												.getDealInfoType());
				rowi.createCell(j * 5 + 4 + 4).setCellValue(
						dealInfos.get(i - startrow).getDetailList().get(j)
								.getSettleYear() == null ? "" : dealInfos
								.get(i - startrow).getDetailList().get(j)
								.getSettleYear());
			}

			rowi.createCell(5 * lenth + 4).setCellValue(
					dealInfos.get(i - startrow).getYyNum());
			rowi.createCell(5 * lenth + 4 + 1).setCellValue(
					dealInfos.get(i - startrow).getLastNum());
			rowi.createCell(5 * lenth + 4 + 2).setCellValue(
					dealInfos.get(i - startrow).getWaitNum());
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition",
					"attachment; filename=dcPayableDetail.xls");
			wb.write(baos);
			byte[] bytes = baos.toByteArray();
			response.setHeader("Content-Length", String.valueOf(bytes.length));
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(bytes);
			bos.close();
			baos.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@RequestMapping(value = "setApps")
	@ResponseBody
	public String setApps(Long comAgentId) throws JSONException {
		List<ConfigAgentAppRelation> relation = configAgentAppRelationService
				.findByComAgentId(comAgentId);

		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < relation.size(); i++) {
			json = new JSONObject();
			json.put("id", relation.get(i).getConfigApp().getId());
			json.put("name", relation.get(i).getConfigApp().getAppName());
			array.put(json);
		}
		return array.toString();
	}

	@RequestMapping(value = "setProducts")
	@ResponseBody
	public String setProducts(Long appId) throws JSONException {
		List<ConfigProduct> products = configProductService.findByAppId(appId);
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < products.size(); i++) {
			json = new JSONObject();
			json.put("id", products.get(i).getId());
			log.debug(products.get(i).getId()
					+ "===="
					+ ProductType.productTypeStrMap.get(products.get(i)
							.getProductName()));
			json.put(
					"name",
					ProductType.productTypeStrMap.get(products.get(i)
							.getProductName())
							+ "["
							+ (products.get(i).getProductLabel() == 0 ? "通用"
									: "专用") + "]");
			array.put(json);
		}
		return array.toString();
	}

}
