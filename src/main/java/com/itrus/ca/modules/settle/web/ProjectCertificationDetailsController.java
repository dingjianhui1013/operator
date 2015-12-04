/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.settle.entity.ProjectCertificationDetails;
import com.itrus.ca.modules.settle.entity.Projectcount;
import com.itrus.ca.modules.settle.service.ProjectCertificationDetailsService;
import com.itrus.ca.modules.settle.vo.ProjectCertificationDetailsVo;
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
	public String list(ProjectCertificationDetails projectCertificationDetails, WorkDealInfo workDealInfo,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "alias", required = false) Long alias,
			@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime, Model model) {
		// 新增个人一年证书
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
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());
		List<ConfigApp> configAppList = configAppService.selectAll();
		model.addAttribute("configAppList", configAppList);
		model.addAttribute("alias", alias);
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
		if (startTime != null && endTime != null) {
			certInfoList = workCertInfoService.findZhiZhengTime(startTime, endTime);
		}
		Page<WorkDealInfo> page = workDealInfoService.find5Apply(new Page<WorkDealInfo>(request, response),
				workDealInfo, alias, certInfoList);
		model.addAttribute("page", page);

		List<WorkDealInfo> list = workDealInfoService.find5ApplyIsIxin(workDealInfo, alias, certInfoList);
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getDealInfoType() != null) {
				if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).contains("新增")) {
					if (list.get(i).getConfigProduct().getProductName() != null) {
						// 企业版
						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {
							// 有效期为空
							if (list.get(i).getAddCertDays() == null) {
								// 新增企业有效期1年
								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									addcompanyYearCertificate++;
								} // 新增企业有效期2年
								else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									addcompanyTwoYearCertificate++;
								} // 新增企业有效期4年
								else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									addcompanyFourYearCertificate++;
								}
							} // 有效期不为空
							else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									addcompanyYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									addcompanyTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									addcompanyFourYearCertificate++;
								}
							}
						} // 个人版
						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {
							// 有效期为空
							if (list.get(i).getAddCertDays() == null) {
								// 新增个人有效期1年
								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									addPersonalYearCertificate++;
								} // 新增个人有效期2年
								else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									addPersonalTwoYearCertificate++;
								} // 新增个人有效期4年
								else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									addPersonalFourYearCertificate++;
								}
							} // 有效期不为空
							else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									addPersonalYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									addPersonalTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									addPersonalFourYearCertificate++;
								}
							}

						}
					}
				} else if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).contains("更新")) {
					if (list.get(i).getConfigProduct().getProductName() != null) {

						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {

							if (list.get(i).getAddCertDays() == null) {

								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									updatecompanyYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									updatecompanyTwoYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									updateFourYearCertificate++;
								}
							} else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									updatecompanyYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									updatecompanyTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									updateFourYearCertificate++;
								}
							}
						} // 个人版
						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {

							if (list.get(i).getAddCertDays() == null) {

								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									updatePersonalYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									updatePersonalTwoYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									updatePersonalFourYearCertificate++;
								}
							} else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									updatePersonalYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									updatePersonalTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									updatePersonalFourYearCertificate++;
								}
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

							if (list.get(i).getAddCertDays() == null) {

								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									updatecompanyYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									updatecompanyTwoYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									updateFourYearCertificate++;
								}
							} else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									updatecompanyYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									updatecompanyTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									updateFourYearCertificate++;
								}
							}
						} // 个人版
						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {

							if (list.get(i).getAddCertDays() == null) {

								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									updatePersonalYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									updatePersonalTwoYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									updatePersonalFourYearCertificate++;
								}
							} else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									updatePersonalYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									updatePersonalTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									updatePersonalFourYearCertificate++;
								}
							}

						}
					}
				}

			} // 损坏更换
			else if (list.get(i).getDealInfoType3() != null) {
				damageCertificate++;
			}
		}
		Projectcount projectcount = new Projectcount();
		projectcount.setAddPersonalFourYearCertificate(addPersonalFourYearCertificate);
		projectcount.setAddPersonalTwoYearCertificate(addPersonalTwoYearCertificate);
		projectcount.setAddPersonalYearCertificate(addPersonalYearCertificate);
		projectcount.setAddcompanyFourYearCertificate(addcompanyFourYearCertificate);
		projectcount.setAddcompanyTwoYearCertificate(addcompanyTwoYearCertificate);
		projectcount.setAddcompanyYearCertificate(addcompanyYearCertificate);
		projectcount.setDamageCertificate(damageCertificate);
		projectcount.setLostCerate(lostCerate);
		projectcount.setUpdatecompanyTwoYearCertificate(updatecompanyTwoYearCertificate);
		projectcount.setUpdatecompanyYearCertificate(updatecompanyYearCertificate);
		projectcount.setUpdateFourYearCertificate(updateFourYearCertificate);
		projectcount.setUpdatePersonalFourYearCertificate(updatePersonalFourYearCertificate);
		projectcount.setUpdatePersonalTwoYearCertificate(updatePersonalTwoYearCertificate);
		projectcount.setUpdatePersonalYearCertificate(updatePersonalYearCertificate);

		model.addAttribute("projectcount", projectcount);
		model.addAttribute("workType", workDealInfo.getDealInfoStatus());
		model.addAttribute("proType", ProductType.productTypeStrMap);
		model.addAttribute("wdiType", WorkDealInfoType.WorkDealInfoTypeMap);
		model.addAttribute("wdiStatus", WorkDealInfoStatus.WorkDealInfoStatusMap);

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "modules/settle/projectCertificationDetailsList";
	}

	@RequestMapping(value = "export")
	public void export(@RequestParam(value = "startTime", required = false) Date startTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "alias", required = false) Long alias,

			HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WorkDealInfo workDealInfo = new WorkDealInfo();
		User user = UserUtils.getUser();
		workDealInfo.setCreateBy(user.getCreateBy());
		ProductType productType = new ProductType();
		WorkDealInfoType workDealInfoType = new WorkDealInfoType();
		List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);
		// try {
		List<WorkCertInfo> certInfoList = new ArrayList<WorkCertInfo>();
		if (startTime != null && endTime != null) {
			certInfoList = workCertInfoService.findZhiZhengTime(startTime, endTime);
		}
		List<WorkDealInfo> list = workDealInfoService.find5ApplyIsIxin(workDealInfo, alias, certInfoList);

		final String fileName = "WorkDealInfos.csv";
		final List<ProjectCertificationDetailsVo> ProjectCertificationDetailsVos = new ArrayList<ProjectCertificationDetailsVo>();
		String dealInfoType = null;
		String dealInfoType1 = null;
		String dealInfoType2 = null;
		String dealInfoType3 = null;

		// 新增个人一年证书
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

		workDealInfo.setId(alias);
		ConfigApp configApp = configAppService.get(alias);
		// System.out.println(configApp.getAppName());
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getDealInfoType() != null) {
				if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).contains("新增")) {
					if (list.get(i).getConfigProduct().getProductName() != null) {
						// 企业版
						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {
							// 有效期为空
							if (list.get(i).getAddCertDays() == null) {
								// 新增企业有效期1年
								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									addcompanyYearCertificate++;
								} // 新增企业有效期2年
								else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									addcompanyTwoYearCertificate++;
								} // 新增企业有效期4年
								else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									addcompanyFourYearCertificate++;
								}
							} // 有效期不为空
							else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									addcompanyYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									addcompanyTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									addcompanyFourYearCertificate++;
								}
							}
						} // 个人版
						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {
							// 有效期为空
							if (list.get(i).getAddCertDays() == null) {
								// 新增个人有效期1年
								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									addPersonalYearCertificate++;
								} // 新增个人有效期2年
								else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									addPersonalTwoYearCertificate++;
								} // 新增个人有效期4年
								else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									addPersonalFourYearCertificate++;
								}
							} // 有效期不为空
							else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									addPersonalYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									addPersonalTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									addPersonalFourYearCertificate++;
								}
							}

						}
					}
				} else if ((WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType())).contains("更新")) {
					if (list.get(i).getConfigProduct().getProductName() != null) {

						if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 1) {

							if (list.get(i).getAddCertDays() == null) {

								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									updatecompanyYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									updatecompanyTwoYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									updateFourYearCertificate++;
								}
							} else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									updatecompanyYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									updatecompanyTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									updateFourYearCertificate++;
								}
							}
						} // 个人版
						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {

							if (list.get(i).getAddCertDays() == null) {

								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									updatePersonalYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									updatePersonalTwoYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									updatePersonalFourYearCertificate++;
								}
							} else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									updatePersonalYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									updatePersonalTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									updatePersonalFourYearCertificate++;
								}
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

							if (list.get(i).getAddCertDays() == null) {

								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									updatecompanyYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									updatecompanyTwoYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									updateFourYearCertificate++;
								}
							} else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									updatecompanyYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									updatecompanyTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									updateFourYearCertificate++;
								}
							}
						} // 个人版
						else if (Integer.parseInt(list.get(i).getConfigProduct().getProductName()) == 2) {

							if (list.get(i).getAddCertDays() == null) {

								if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() > 0
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 730) {
									updatePersonalYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 730
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1095) {
									updatePersonalTwoYearCertificate++;
								} else if ((list.get(i).getYear()) * 365 + list.get(i).getLastDays() >= 1460
										&& (list.get(i).getYear()) * 365 + list.get(i).getLastDays() < 1825) {
									updatePersonalFourYearCertificate++;
								}
							} else {
								if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() > 0
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 730) {
									updatePersonalYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 730
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1095) {
									updatePersonalTwoYearCertificate++;
								} else if (list.get(i).getYear() * 365 + list.get(i).getLastDays()
										+ list.get(i).getAddCertDays() >= 1460
										&& list.get(i).getYear() * 365 + list.get(i).getLastDays()
												+ list.get(i).getAddCertDays() < 1825) {
									updatePersonalFourYearCertificate++;
								}
							}

						}
					}
				}

			} // 损坏更换
			else if (list.get(i).getDealInfoType3() != null) {
				damageCertificate++;
			}
		}

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
		cell.setCellValue("新增：       个人证书 一年期证书" + addPersonalYearCertificate + "张； 两年期证书"
				+ addPersonalTwoYearCertificate + "张；  四年期证书" + addPersonalFourYearCertificate + "张；");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row3 = sheet.createRow(3);
		cell = row3.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue("        企业证书 一年期证书" + addcompanyYearCertificate + "张； 两年期证书" + addcompanyTwoYearCertificate
				+ "张；  四年期证书" + addcompanyFourYearCertificate + "张；");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row4 = sheet.createRow(4);
		cell = row4.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue("更新：    个人证书 一年期证书" + updatePersonalYearCertificate + "张； 两年期证书"
				+ updatePersonalTwoYearCertificate + "张；  四年期证书" + updatePersonalFourYearCertificate + "张；");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row5 = sheet.createRow(5);
		cell = row5.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue("       企业证书 一年期证书" + updatecompanyYearCertificate + "张； 两年期证书"
				+ updatecompanyTwoYearCertificate + "张；  四年期证书" + updateFourYearCertificate + "张；");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 14));
		HSSFRow row6 = sheet.createRow(6);
		cell = row6.createCell(1);
		font.setFontHeightInPoints((short) 14);
		style.setFont(font);
		cell.setCellStyle(style);
		// cell.setCellValue("aa");
		cell.setCellValue("遗失补办： 证书" + lostCerate + "张                         损坏更换：   证书" + damageCertificate + "张");

		HSSFRow row7 = sheet.createRow(7);
		row7.createCell(0).setCellValue("ID");
		row7.createCell(1).setCellValue("单位名称");
		row7.createCell(2).setCellValue("业务类型");
		row7.createCell(3).setCellValue("证书类型");
		row7.createCell(4).setCellValue("证书有效期/天");
		row7.createCell(5).setCellValue("制证时间");

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
			if (list.get(i).getDealInfoType() != null) {
				rown.createCell(3).setCellValue(WorkDealInfoType.getDealInfoTypeName(list.get(i).getDealInfoType()));
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
			// 制证时间
			if (list.get(i).getWorkCertInfo().getSignDate() != null) {
				rown.createCell(5).setCellValue(
						DateUtils.formatDate(list.get(i).getWorkCertInfo().getSignDate(), "yyyy-MM-dd HH:mm:ss"));
			} else {
				rown.createCell(5).setCellValue(" ");
			}
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType(response.getContentType());
			response.setHeader("Content-disposition", "attachment; filename=keyPurchaseRecord.xls");
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
			System.out.println("+++++++++++++");
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

}
