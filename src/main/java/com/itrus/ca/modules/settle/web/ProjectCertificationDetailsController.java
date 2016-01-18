/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
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
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.constant.WorkDealInfoType;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.entity.KeyUsbKeyInvoice;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.settle.entity.ProjectCertificationDetails;
import com.itrus.ca.modules.settle.entity.Projectcount;
import com.itrus.ca.modules.settle.service.ProjectCertificationDetailsService;
import com.itrus.ca.modules.settle.vo.ProjectCertificationDetailsVo;
import com.itrus.ca.modules.statistic.entity.StatisticAppData;
import com.itrus.ca.modules.statistic.entity.StatisticDayData;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 项目发证明细Controller
 * 
 * @author qt
 * @version 2015-11-18
 */
@Controller
@RequestMapping(value = "${adminPath}/settle/projectCertificationDetails")
public class ProjectCertificationDetailsController extends BaseController {

	@Autowired
	private ProjectCertificationDetailsService projectCertificationDetailsService;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	@Autowired
	private ConfigAppService configAppService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private WorkCertInfoService workCertInfoService;
	
	@Autowired
	private ConfigAppOfficeRelationService configAppOfficeRelationService;
	@ModelAttribute
	public ProjectCertificationDetails get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return projectCertificationDetailsService.get(id);
		} else {
			return new ProjectCertificationDetails();
		}
	}

	@RequiresPermissions("settle:projectCertificationDetails:view")
	@RequestMapping(value = { "list", "" })
	public String list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "alias", required = false) Long alias,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,HttpSession httpSession, Model model) {
			User user = UserUtils.getUser();
		Long officeId = user.getOffice().getId();
		/*// 新增个人一年证书
		int addPersonalYearCertificate = 0;
		// 新增个人两年证书
		int addPersonalTwoYearCertificate = 0;
		// 新增个人四年证书
		int addPersonalFourYearCertificate = 0;
		// 新增企业一年证书
		int addcompanyYearCertificate = 0;
		// 新增企业两年证书
		int addcompanyTwoYearCertificate = 0;
		// 新增企业四年证书
		int addcompanyFourYearCertificate = 0;
		// 更新个人一年证书
		int updatePersonalYearCertificate = 0;
		// 更新个人两年证书
		int updatePersonalTwoYearCertificate = 0;
		// 更新个人四年证书
		int updatePersonalFourYearCertificate = 0;
		// 更新企业一年证书
		int updatecompanyYearCertificate = 0;
		// 更新企业两年证书
		int updatecompanyTwoYearCertificate = 0;
		// 更新企业四年证书
		int updateFourYearCertificate = 0;
		// 遗失补办证书
		int lostCerate = 0;
		// 损坏更换证书
		int damageCertificate = 0;*/
		// User user = UserUtils.getUser();
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("alias", alias);
	
			if (startTime == null) {
				startTime = DateUtils.firstDayOfMonth(new Date());
			}
			if (endTime == null) {
				endTime = new Date();
			}
		
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		if (alias == null) { // 没有选择应用，直接返回查询条件输入页面
			return "modules/settle/projectCertificationDetailsList";
		}

		Page<WorkDealInfo> page = workDealInfoService.findPage4CertList(new Page<WorkDealInfo>(request, response),
				alias, startTime,endTime);
		
		model.addAttribute("page", page);
		Projectcount projectcount=new Projectcount();

		/**
		 * 根据应用、网点、时间、年限生成数据
		 */
		// 新增
		projectcount.setAdd1(workDealInfoService.getCertAppYearInfoCount1(
				startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_ADD_CERT));
		projectcount.setAdd2(workDealInfoService.getCertAppYearInfoCount1(
				startTime,endTime, officeId, 2,alias,
				WorkDealInfoType.TYPE_ADD_CERT));
		projectcount.setAdd4(workDealInfoService.getCertAppYearInfoCount1(
				startTime,endTime, officeId, 4, alias,
				WorkDealInfoType.TYPE_ADD_CERT));
		projectcount.setAdd5(workDealInfoService.getCertAppYearInfoCount1(
				startTime,endTime, officeId, 5, alias,
				WorkDealInfoType.TYPE_ADD_CERT));
		
		projectcount.setRenew1(workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_UPDATE_CERT));
		projectcount.setRenew2(workDealInfoService.getCertAppYearInfoCountOneDeal1(
				startTime,endTime, officeId, 2, alias,
				WorkDealInfoType.TYPE_UPDATE_CERT));
		projectcount.setRenew4(workDealInfoService.getCertAppYearInfoCountOneDeal1(
				startTime,endTime, officeId, 4, alias,
				WorkDealInfoType.TYPE_UPDATE_CERT));
		projectcount.setRenew5(workDealInfoService.getCertAppYearInfoCountOneDeal1(
				startTime,endTime, officeId, 5, alias,
				WorkDealInfoType.TYPE_UPDATE_CERT));
		//变更
		projectcount.setModifyNum(workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId,
			0, alias, WorkDealInfoType.TYPE_INFORMATION_REROUTE));
		//遗失补办
		projectcount.setLostCerate(workDealInfoService.getCertAppYearInfoCountOneDeal1(
				startTime,endTime, officeId, 0,alias,
			WorkDealInfoType.TYPE_LOST_CHILD)
			);
		//损坏更换
		projectcount.setDamageCertificate(workDealInfoService.getCertAppYearInfoCountOneDeal1(
				startTime,endTime, officeId, 0, alias,
			WorkDealInfoType.TYPE_DAMAGED_REPLACED));
		projectcount.setUpdateChangeNum(workDealInfoService.getCertAppYearInfoUpdateChangeNum1(
				startTime,endTime, officeId, 1, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			projectcount.setUpdateChangeNum2(workDealInfoService.getCertAppYearInfoUpdateChangeNum1(
					startTime,endTime, officeId, 2, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			projectcount.setUpdateChangeNum4(workDealInfoService.getCertAppYearInfoUpdateChangeNum1(
					startTime,endTime, officeId, 4, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			projectcount.setUpdateChangeNum5(workDealInfoService.getCertAppYearInfoUpdateChangeNum1(
					startTime,endTime, officeId, 5, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
			
			projectcount.setUpdateLostNum(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
					startTime,endTime, officeId, 1, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
					);
			projectcount.setUpdateLostNum2(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
					startTime,endTime, officeId, 2, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
					);
			projectcount.setUpdateLostNum4(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
					startTime,endTime, officeId, 4, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
					);
			projectcount.setUpdateLostNum5(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
					startTime,endTime, officeId, 5, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
					);
			
			
			projectcount.setUpdateReplaceNum(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
					startTime,endTime, officeId, 1, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			projectcount.setUpdateReplaceNum2(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
					startTime,endTime, officeId, 2, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			projectcount.setUpdateReplaceNum4(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
					startTime,endTime, officeId, 4, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			projectcount.setUpdateReplaceNum5(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
					startTime,endTime, officeId, 5, alias,
					WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
			
			projectcount.setChangeLostNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceNum1(
					startTime,endTime, officeId, 0, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD)
					
					);
			projectcount.setChangeReplaceNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceNum1(
					startTime,endTime, officeId, 0,alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED)
					
					);
			
			projectcount.setChangeUpdateLostNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
					startTime,endTime, officeId, 1, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
			projectcount.setChangeUpdateLostNum2(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
					startTime,endTime, officeId, 2, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
			projectcount.setChangeUpdateLostNum4(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
					startTime,endTime, officeId, 4, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
			projectcount.setChangeUpdateLostNum5(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
					startTime,endTime, officeId, 5, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
			
			
			projectcount.setChangeUpdateReplaceNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
					startTime,endTime, officeId, 1, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
			projectcount.setChangeUpdateReplaceNum2(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
					startTime,endTime, officeId, 2, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
			projectcount.setChangeUpdateReplaceNum4(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
					startTime,endTime, officeId, 4, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
			projectcount.setChangeUpdateReplaceNum5(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
					startTime,endTime, officeId, 5, alias,
					WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
			
//		List<WorkDealInfo> list = workDealInfoService.find4CertList(alias, certInfoList);
//		for (int i = 0; i < list.size(); i++) {
//
//			if (list.get(i).getDealInfoType() != null) {
//				if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).contains("新增")) {
//					if (list.get(i).getConfigProduct().getProductName() != null) {
//						// 企业版
//						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {
//							//
//							// 新增企业有效期1年
//							if (list.get(i).getYear() == 1) {
//								addcompanyYearCertificate++;
//							} // 新增企业有效期2年
//							else if (list.get(i).getYear() == 2) {
//								addcompanyTwoYearCertificate++;
//							} // 新增企业有效期4年
//							else if (list.get(i).getYear() == 4) {
//								addcompanyFourYearCertificate++;
//							}
//							//
//						} else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {
//							//
//							// 新增个人有效期1年
//							if (list.get(i).getYear() == 1) {
//								addPersonalYearCertificate++;
//							} // 新增个人有效期2年
//							else if (list.get(i).getYear() == 2) {
//								addPersonalTwoYearCertificate++;
//							} // 新增个人有效期4年
//							else if (list.get(i).getYear() == 4) {
//								addPersonalFourYearCertificate++;
//							}
//						}
//					}
//				} else if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).contains("更新")) {
//					if (list.get(i).getConfigProduct().getProductName() != null) {
//
//						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {
//
//							if (list.get(i).getYear() == 1) {
//								updatecompanyYearCertificate++;
//							} else if (list.get(i).getYear() == 2) {
//								updatecompanyTwoYearCertificate++;
//							} else if (list.get(i).getYear() == 4) {
//								updateFourYearCertificate++;
//							}
//
//						} // 个人版
//						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {
//
//							if (list.get(i).getYear() == 1) {
//								updatePersonalYearCertificate++;
//							} else if (list.get(i).getYear() == 2) {
//								updatePersonalTwoYearCertificate++;
//							} else if (list.get(i).getYear() == 4) {
//								updatePersonalFourYearCertificate++;
//							}
//						}
//					}
//
//				}
//			} else if (list.get(i).getDealInfoType1() != null) {
//				lostCerate++;
//
//			} else if (list.get(i).getDealInfoType2() != null) {
//				if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType2())).contains("补办")) {
//					lostCerate++;
//				} else if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType2())).contains("更新")) {
//					if (list.get(i).getConfigProduct().getProductName() != null) {
//						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {
//
//							if (list.get(i).getYear() == 1) {
//								updatecompanyYearCertificate++;
//							} else if (list.get(i).getYear() == 2) {
//								updatecompanyTwoYearCertificate++;
//							} else if (list.get(i).getYear() == 4) {
//								updateFourYearCertificate++;
//							}
//
//						} // 个人版
//						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {
//
//							if (list.get(i).getYear() == 1) {
//								updatePersonalYearCertificate++;
//							} else if (list.get(i).getYear() == 2) {
//								updatePersonalTwoYearCertificate++;
//							} else if (list.get(i).getYear() == 4) {
//								updatePersonalFourYearCertificate++;
//							}
//						}
//					}
//				}
//
//			} // 损坏更换
//			else if (list.get(i).getDealInfoType3() != null) {
//				damageCertificate++;
//			}
//		}
//		Projectcount projectcount = new Projectcount();
//		projectcount.setAddPersonalFourYearCertificate(addPersonalFourYearCertificate);
//		projectcount.setAddPersonalTwoYearCertificate(addPersonalTwoYearCertificate);
//		projectcount.setAddPersonalYearCertificate(addPersonalYearCertificate);
//		projectcount.setAddcompanyFourYearCertificate(addcompanyFourYearCertificate);
//		projectcount.setAddcompanyTwoYearCertificate(addcompanyTwoYearCertificate);
//		projectcount.setAddcompanyYearCertificate(addcompanyYearCertificate);
//		projectcount.setDamageCertificate(damageCertificate);
//		projectcount.setLostCerate(lostCerate);
//		projectcount.setUpdatecompanyTwoYearCertificate(updatecompanyTwoYearCertificate);
//		projectcount.setUpdatecompanyYearCertificate(updatecompanyYearCertificate);
//		projectcount.setUpdateFourYearCertificate(updateFourYearCertificate);
//		projectcount.setUpdatePersonalFourYearCertificate(updatePersonalFourYearCertificate);
//		projectcount.setUpdatePersonalTwoYearCertificate(updatePersonalTwoYearCertificate);
//		projectcount.setUpdatePersonalYearCertificate(updatePersonalYearCertificate);
//
		model.addAttribute("projectcount", projectcount);
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);

		return "modules/settle/projectCertificationDetailsList";
	}

	@RequestMapping(value = "export")
	public void export(@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "alias", required = false) Long alias,

			HttpServletRequest request, HttpServletResponse response) {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		User user = UserUtils.getUser();
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);
		// try {
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
		Long officeId = user.getOffice().getId();
		/*if (startTime != null && endTime != null) {
			certInfoList = workCertInfoService.findZhiZhengTime(startTime, endTime);
		}*/
		
		final String fileName = "WorkDealInfos.csv";
		final List<ProjectCertificationDetailsVo> ProjectCertificationDetailsVos = new ArrayList<ProjectCertificationDetailsVo>();
		String dealInfoType = null;
		String dealInfoType1 = null;
		String dealInfoType2 = null;
		String dealInfoType3 = null;

		/*// 新增个人一年证书
		int addPersonalYearCertificate = 0;
		// 新增个人两年证书
		int addPersonalTwoYearCertificate = 0;
		// 新增个人四年证书
		int addPersonalFourYearCertificate = 0;
		// 新增企业一年证书
		int addcompanyYearCertificate = 0;
		// 新增企业两年证书
		int addcompanyTwoYearCertificate = 0;
		// 新增企业四年证书
		int addcompanyFourYearCertificate = 0;
		// 更新个人一年证书
		int updatePersonalYearCertificate = 0;
		// 更新个人两年证书
		int updatePersonalTwoYearCertificate = 0;
		// 更新个人四年证书
		int updatePersonalFourYearCertificate = 0;
		// 更新企业一年证书
		int updatecompanyYearCertificate = 0;
		// 更新企业两年证书
		int updatecompanyTwoYearCertificate = 0;
		// 更新企业四年证书
		int updateFourYearCertificate = 0;
		// 遗失补办证书
		int lostCerate = 0;
		// 损坏更换证书
		int damageCertificate = 0;
		
		// System.out.println(configApp.getAppName());
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getDealInfoType() != null) {
				if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).contains("新增")) {
					if (list.get(i).getConfigProduct().getProductName() != null) {
						// 企业版
						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {
							//
							// 新增企业有效期1年
							if (list.get(i).getYear() == 1) {
								addcompanyYearCertificate++;
							} // 新增企业有效期2年
							else if (list.get(i).getYear() == 2) {
								addcompanyTwoYearCertificate++;
							} // 新增企业有效期4年
							else if (list.get(i).getYear() == 4) {
								addcompanyFourYearCertificate++;
							}
							//
						} else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {
							//
							// 新增个人有效期1年
							if (list.get(i).getYear() == 1) {
								addPersonalYearCertificate++;
							} // 新增个人有效期2年
							else if (list.get(i).getYear() == 2) {
								addPersonalTwoYearCertificate++;
							} // 新增个人有效期4年
							else if (list.get(i).getYear() == 4) {
								addPersonalFourYearCertificate++;
							}
						}
					}
				} else if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).contains("更新")) {
					if (list.get(i).getConfigProduct().getProductName() != null) {

						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {

							if (list.get(i).getYear() == 1) {
								updatecompanyYearCertificate++;
							} else if (list.get(i).getYear() == 2) {
								updatecompanyTwoYearCertificate++;
							} else if (list.get(i).getYear() == 4) {
								updateFourYearCertificate++;
							}

						} // 个人版
						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {

							if (list.get(i).getYear() == 1) {
								updatePersonalYearCertificate++;
							} else if (list.get(i).getYear() == 2) {
								updatePersonalTwoYearCertificate++;
							} else if (list.get(i).getYear() == 4) {
								updatePersonalFourYearCertificate++;
							}
						}
					}

				}
			} else if (list.get(i).getDealInfoType1() != null) {
				lostCerate++;

			} else if (list.get(i).getDealInfoType2() != null) {
				if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType2())).contains("补办")) {
					lostCerate++;
				} else if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType2())).contains("更新")) {
					if (list.get(i).getConfigProduct().getProductName() != null) {
						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {

							if (list.get(i).getYear() == 1) {
								updatecompanyYearCertificate++;
							} else if (list.get(i).getYear() == 2) {
								updatecompanyTwoYearCertificate++;
							} else if (list.get(i).getYear() == 4) {
								updateFourYearCertificate++;
							}

						} // 个人版
						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {

							if (list.get(i).getYear() == 1) {
								updatePersonalYearCertificate++;
							} else if (list.get(i).getYear() == 2) {
								updatePersonalTwoYearCertificate++;
							} else if (list.get(i).getYear() == 4) {
								updatePersonalFourYearCertificate++;
							}
						}
					}
				}

			} // 损坏更换
			else if (list.get(i).getDealInfoType3() != null) {
				damageCertificate++;
			}
		}*/
		ConfigApp configApp = configAppService.get(alias);
		HSSFWorkbook wb = new HSSFWorkbook();// 定义工作簿
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		Cell cell = null;
		HSSFSheet sheet = wb.createSheet(configApp.getAppName() + "项目");
		HSSFRow row = sheet.createRow(0);
		sheet.setDefaultColumnWidth(15);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));// 合并单元格
		HSSFFont font = wb.createFont();

		// 第一行数据
		HSSFRow row0 = sheet.createRow(0);
		cell = row0.createCell(0);
		HSSFFont font0 = wb.createFont();
		HSSFCellStyle style0 = wb.createCellStyle();
		font0.setFontHeightInPoints((short) 18);
		style0.setFont(font0);
		style0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell.setCellStyle(style0);
		cell.setCellValue(configApp.getAppName() + "项目发证明细");
		// 第二行数据
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row1 = sheet.createRow(1);
		cell = row1.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		cell.setCellValue("统计时间：" + DateUtils.formatDate(startTime, "yyyy-MM-dd") + "—"
				+ DateUtils.formatDate(endTime, "yyyy-MM-dd"));
		// 第三行数据
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row2 = sheet.createRow(2);
		cell = row2.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue("新增 一年期证书" + workDealInfoService.getCertAppYearInfoCount1(
				startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_ADD_CERT) + "张； 两年期证书"
				+ workDealInfoService.getCertAppYearInfoCount1(
						startTime,endTime, officeId, 2, alias,
						WorkDealInfoType.TYPE_ADD_CERT) + "张；  四年期证书" + workDealInfoService.getCertAppYearInfoCount1(
								startTime,endTime, officeId, 4, alias,
								WorkDealInfoType.TYPE_ADD_CERT) + "张；五年期证书"+workDealInfoService.getCertAppYearInfoCount1(
										startTime,endTime, officeId, 5, alias,
										WorkDealInfoType.TYPE_ADD_CERT)+"张");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row3 = sheet.createRow(3);
		cell = row3.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue("更新 一年期证书" + workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_UPDATE_CERT) + "张； 两年期证书"
				+ workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId, 2, alias,
						WorkDealInfoType.TYPE_UPDATE_CERT) + "张；  四年期证书" + workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId, 4, alias,
								WorkDealInfoType.TYPE_UPDATE_CERT) + "张；五年期证书"+workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId, 5, alias,
										WorkDealInfoType.TYPE_UPDATE_CERT)+"张");
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row4 = sheet.createRow(4);
		cell = row4.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue("变更信息" + workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId,
				0, alias, WorkDealInfoType.TYPE_INFORMATION_REROUTE) + "张； 遗失补办"
				+ workDealInfoService.getCertAppYearInfoCountOneDeal1(
						startTime,endTime, officeId, 0,alias,
						WorkDealInfoType.TYPE_LOST_CHILD) + "张；  损坏更换" + workDealInfoService.getCertAppYearInfoCountOneDeal1(
								startTime,endTime, officeId, 0, alias,
								WorkDealInfoType.TYPE_DAMAGED_REPLACED) + "张；");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row5 = sheet.createRow(5);
		cell = row5.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue(" 更新+变更 一年期证书" + workDealInfoService.getCertAppYearInfoUpdateChangeNum1(
				startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE) + "张； 两年期证书"
				+ workDealInfoService.getCertAppYearInfoUpdateChangeNum1(
						startTime,endTime, officeId, 2, alias,
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE) + "张；  四年期证书" + workDealInfoService.getCertAppYearInfoUpdateChangeNum1(
								startTime,endTime, officeId, 4, alias,
								WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE) + "张；五年期证书"+workDealInfoService.getCertAppYearInfoUpdateChangeNum1(
										startTime,endTime, officeId, 5, alias,
										WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE)+"张");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row6 = sheet.createRow(6);
		cell = row6.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue(" 更新+遗失补办一年期证书" + workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
				startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD) + "张； 两年期证书"
				+ workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
						startTime,endTime, officeId, 2, alias,
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD) + "张；  四年期证书" + workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
								startTime,endTime, officeId, 4, alias,
								WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD) + "张；五年期证书"+workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
										startTime,endTime, officeId, 5, alias,
										WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)+"张");
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row7 = sheet.createRow(7);
		cell = row6.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue(" 更新+损坏更换 一年期证书" + workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
				startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED) + "张； 两年期证书"
				+ workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
						startTime,endTime, officeId, 2, alias,
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED) + "张；  四年期证书" + workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
								startTime,endTime, officeId, 4, alias,
								WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED) + "张；五年期证书"+workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum1(
										startTime,endTime, officeId, 5, alias,
										WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED)+"张");
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row8 = sheet.createRow(8);
		cell = row6.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue(" 变更+遗失补办 " + workDealInfoService.getCertAppYearInfoChangeLostReplaceNum1(
				startTime,endTime, officeId, 0, alias,
				WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD) + "张； 变更+损坏更换"
				+ workDealInfoService.getCertAppYearInfoChangeLostReplaceNum1(
						startTime,endTime, officeId, 0, alias,
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED) + "张;"
						);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row9= sheet.createRow(9);
		cell = row6.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue(" 更新+变更+遗失补办  一年期证书" + workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
				startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT) + "张； 两年期证书"
				+ workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
						startTime,endTime, officeId, 2, alias,
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT) + "张；  四年期证书" + workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
								startTime,endTime, officeId, 4, alias,
								WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT) + "张；五年期证书"+workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
										startTime,endTime, officeId, 5, alias,
										WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT)+"张");
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row10= sheet.createRow(10);
		cell = row6.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue(" 更新+变更+损坏更换 一年期证书" + workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
				startTime,endTime, officeId, 1, alias,
				WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT) + "张； 两年期证书"
				+ workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
						startTime,endTime, officeId, 2, alias,
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT) + "张；  四年期证书" + workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
								startTime,endTime, officeId, 4, alias,
								WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT) + "张；五年期证书"+workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum1(
										startTime,endTime, officeId, 5, alias,
										WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT)+"张");
		
		List<WorkDealInfo> list = workDealInfoService.find4CertList(alias, startTime,endTime);
		
		HSSFRow row11= sheet.createRow(11);
		row7.createCell(0).setCellValue("ID");
		row7.createCell(1).setCellValue("单位名称");
		row7.createCell(2).setCellValue("业务类型");
		row7.createCell(3).setCellValue("证书类型");
		row7.createCell(4).setCellValue("证书有效期/天");
		row7.createCell(5).setCellValue("制证时间");
		row7.createCell(6).setCellValue("所属区域");
		for (int i = 0; i < list.size(); i++) {

			HSSFRow rown = sheet.createRow(i + 8);
			rown.createCell(0).setCellValue(i + 1);
			if (list.get(i).getWorkCompany().getCompanyName() == null) {

				rown.createCell(1).setCellValue("");
			} else {
				rown.createCell(1).setCellValue(list.get(i).getWorkCompany().getCompanyName());
			}
			rown.createCell(2).setCellValue(
					productType.getProductTypeName(Integer.parseInt(list.get(i).getConfigProduct().getProductName())));
			if (list.get(i).getDealInfoType() != null||list.get(i).getDealInfoType1()!=null||list.get(i).getDealInfoType2()!=null) {
				rown.createCell(3).setCellValue(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())+WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType1())+WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType2()));
			} else if (list.get(i).getDealInfoType1() != null) {
				rown.createCell(3).setCellValue(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType1()));
			} else if (list.get(i).getDealInfoType2() != null) {
				rown.createCell(3).setCellValue(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType2()));
			} else if (list.get(i).getDealInfoType3() != null) {
				rown.createCell(3).setCellValue(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType3()));
			}
			// 证书有效期
			if (list.get(i).getWorkCertInfo().getNotbefore() != null
					&& list.get(i).getWorkCertInfo().getNotafter() != null) {
				if (list.get(i).getAddCertDays() == null) {
					// workDealInfo.year*365+workDealInfo.lastDays
					int EXPDate = (list.get(i).getYear()) * 365 + list.get(i).getLastDays();
					rown.createCell(4).setCellValue(EXPDate);
				} else {
					// workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays
					int EXPDate = (list.get(i).getYear() * 365 + list.get(i).getLastDays()
							+ list.get(i).getAddCertDays());
					rown.createCell(4).setCellValue(EXPDate);
				}
			}
			// 所属区域
			if (list.get(i).getWorkCertInfo().getSignDate() != null) {
				rown.createCell(5).setCellValue(
						DateUtils.formatDate(list.get(i).getWorkCertInfo().getSignDate(), "yyyy-MM-dd HH:mm:ss"));
			} else {
				rown.createCell(5).setCellValue(" ");
			}
			if (list.get(i).getWorkCompany().getDistrict() != null) {
				rown.createCell(6).setCellValue(list.get(i).getWorkCompany().getDistrict());
			} else {
				rown.createCell(6).setCellValue(" ");
			}
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition", "attachment; filename=projectCertificationDetails.xls");
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

	@RequiresPermissions("settle:projectCertificationDetails:view")
	@RequestMapping(value = "form")
	public String form(ProjectCertificationDetails projectCertificationDetails, Model model) {
		model.addAttribute("projectCertificationDetails", projectCertificationDetails);
		return "modules/settle/projectCertificationDetailsForm";
	}

	@RequiresPermissions("settle:projectCertificationDetails:edit")
	@RequestMapping(value = "save")
	public String save(ProjectCertificationDetails projectCertificationDetails, Model model,
			RedirectAttributes redirectAttributes) {

		return "redirect:" + Global.getAdminPath() + "/modules/settle/projectCertificationDetails/?repage";
	}

	@RequiresPermissions("settle:projectCertificationDetails:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		projectCertificationDetailsService.delete(id);
		addMessage(redirectAttributes, "删除项目发证明细成功");
		return "redirect:" + Global.getAdminPath() + "/modules/settle/projectCertificationDetails/?repage";
	}
	/*// 根据date，officeId 生成每日统计数据，并放到Session中，以便后面保存使用
		private void fazhengmingxi(Date startTime,Date endTime,Long officeId, Long alias,HttpSession session) {

			
			
				Projectcount projectcount=new Projectcount();

				*//**
				 * 根据应用、网点、时间、年限生成数据
				 *//*
				// 新增
				projectcount.setAdd1(workDealInfoService.getCertAppYearInfoCount1(
						startTime,endTime, officeId, 1, alias,
						WorkDealInfoType.TYPE_ADD_CERT));
				projectcount.setAdd2(workDealInfoService.getCertAppYearInfoCount1(
						startTime,endTime, officeId, 2,alias,
						WorkDealInfoType.TYPE_ADD_CERT));
				projectcount.setAdd4(workDealInfoService.getCertAppYearInfoCount1(
						startTime,endTime, officeId, 4, alias,
						WorkDealInfoType.TYPE_ADD_CERT));
				projectcount.setAdd5(workDealInfoService.getCertAppYearInfoCount1(
						startTime,endTime, officeId, 5, alias,
						WorkDealInfoType.TYPE_ADD_CERT));
	
				
				projectcount.setRenew1(workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId, 1, alias,
						WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setRenew2(workDealInfoService.getCertAppYearInfoCountOneDeal1(
						startTime,endTime, officeId, 2, alias,
						WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setRenew4(workDealInfoService.getCertAppYearInfoCountOneDeal1(
						startTime,endTime, officeId, 4, alias,
						WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setRenew5(workDealInfoService.getCertAppYearInfoCountOneDeal1(
						startTime,endTime, officeId, 5, alias,
						WorkDealInfoType.TYPE_UPDATE_CERT));
				//变更
				projectcount.setModifyNum(workDealInfoService.getCertAppYearInfoCountOneDeal1(startTime,endTime, officeId,
					0, alias, WorkDealInfoType.TYPE_INFORMATION_REROUTE));
				//遗失补办
				projectcount.setLostCerate(workDealInfoService.getCertAppYearInfoCountOneDeal1(
						startTime,endTime, officeId, 0,alias,
					WorkDealInfoType.TYPE_LOST_CHILD)
					);
				//损坏更换
				projectcount.setDamageCertificate(workDealInfoService.getCertAppYearInfoCountOneDeal1(
						startTime,endTime, officeId, 0, alias,
					WorkDealInfoType.TYPE_DAMAGED_REPLACED));
				projectcount.setUpdateChangeNum(workDealInfoService.getCertAppYearInfoUpdateChangeNum(
					startTime,endTime, officeId, 1, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
				projectcount.setUpdateChangeNum2(workDealInfoService.getCertAppYearInfoUpdateChangeNum(
						startTime,endTime, officeId, 2, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
				projectcount.setUpdateChangeNum4(workDealInfoService.getCertAppYearInfoUpdateChangeNum(
						startTime,endTime, officeId, 4, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
				projectcount.setUpdateChangeNum5(workDealInfoService.getCertAppYearInfoUpdateChangeNum(
						startTime,endTime, officeId, 5, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_INFORMATION_REROUTE));
				
				projectcount.setUpdateLostNum(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
						startTime,endTime, officeId, 1, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
						);
				projectcount.setUpdateLostNum2(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
						startTime,endTime, officeId, 2, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
						);
				projectcount.setUpdateLostNum4(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
						startTime,endTime, officeId, 4, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
						);
				projectcount.setUpdateLostNum5(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
						startTime,endTime, officeId, 5, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_LOST_CHILD)
						);
				
				
				projectcount.setUpdateReplaceNum(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
						startTime,endTime, officeId, 1, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
				projectcount.setUpdateReplaceNum2(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
						startTime,endTime, officeId, 2, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
				projectcount.setUpdateReplaceNum4(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
						startTime,endTime, officeId, 4, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
				projectcount.setUpdateReplaceNum5(workDealInfoService.getCertAppYearInfoUpdateLostReplaceNum(
						startTime,endTime, officeId, 5, app.getId(),
						WorkDealInfoType.TYPE_UPDATE_CERT,WorkDealInfoType.TYPE_DAMAGED_REPLACED));
				
				projectcount.setChangeLostNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceNum(
						startTime,endTime, officeId, 0, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD)
						
						);
				projectcount.setChangeReplaceNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceNum(
						startTime,endTime, officeId, 0, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED)
						
						);
				
				projectcount.setChangeUpdateLostNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
						startTime,endTime, officeId, 1, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setChangeUpdateLostNum2(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
						startTime,endTime, officeId, 2, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setChangeUpdateLostNum4(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
						startTime,endTime, officeId, 4, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setChangeUpdateLostNum5(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
						startTime,endTime, officeId, 5, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_LOST_CHILD,WorkDealInfoType.TYPE_UPDATE_CERT));
				
				
				projectcount.setChangeUpdateReplaceNum(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
						startTime,endTime, officeId, 1, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setChangeUpdateReplaceNum2(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
						startTime,endTime, officeId, 2, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setChangeUpdateReplaceNum4(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
						startTime,endTime, officeId, 4, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
				projectcount.setChangeUpdateReplaceNum5(workDealInfoService.getCertAppYearInfoChangeLostReplaceUpdateNum(
						startTime,endTime, officeId, 5, app.getId(),
						WorkDealInfoType.TYPE_INFORMATION_REROUTE,WorkDealInfoType.TYPE_DAMAGED_REPLACED,WorkDealInfoType.TYPE_UPDATE_CERT));
				
				
				// statisticAppDataService.save(certData);
				session.setAttribute("projectcount", projectcount);
			
		}
*/
}
