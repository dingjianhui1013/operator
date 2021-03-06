package com.itrus.ca.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.work.entity.WorkCertApplyInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkUser;

import cn.topca.tca.client.CertificateRequest;

/**
 * 根据业务信息生成证书相关信息
 * 
 * @author ZhangJingtao
 * 
 */
public class RaAccountUtil {

	/**
	 * 根据request信息生成request用于申请证书
	 * 
	 * @param company
	 * @param uesr
	 * @param applyInfo
	 * @param extendInfo
	 * @return
	 */
	public static CertificateRequest outRequest(WorkDealInfo dealInfo,
			ConfigRaAccountExtendInfo extendInfo) {
		CertificateRequest request = new CertificateRequest();
		WorkCompany company = dealInfo.getWorkCompany();
		WorkUser user = dealInfo.getWorkUser();
		WorkCertApplyInfo applyInfo = dealInfo.getWorkCertInfo()
				.getWorkCertApplyInfo();
		ConfigApp app = dealInfo.getConfigApp();

		String flag = new String();
		// 取出所有信息
		String c_name = company.getCompanyName();
		String c_name2 = company.getTwoLevelCompanyName();
		String c_orgNum = company.getOrganizationNumber();
		String c_certNum = company.getComCertficateNumber();

		String u_contactName = user.getContactName();
		String u_email = user.getContactEmail();
		String u_conCertNum = user.getConCertNumber();
		String u_uid = user.getContactTel();

		String u_province = getProvince(u_conCertNum);
		
		
		String a_name = applyInfo.getName();
		String a_idCard = applyInfo.getIdCard();
		String a_email = applyInfo.getEmail();

		String appName = app.getAppName();
		String certSort = dealInfo.getCertSort().toString();
		String productName = ProductType.productTypeEnglishMap.get(Integer
				.valueOf(dealInfo.getConfigProduct().getProductName()));


		if (!extendInfo.getCommonNameDisplayName().equals("-1")) {
			flag = extendInfo.getCommonNameDisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = c_name;
			} else if (flag.endsWith("1")) {
				value = u_contactName;
			} else if (flag.endsWith("2")) {
				value = a_name;
			} else if (flag.endsWith("3")) {
				value = u_contactName+"_"+c_name;
			} else if (flag.endsWith("4")){
				value = u_contactName+"_"+c_name2;
			}
			request.setUserName(value);
		}

		if (!extendInfo.getNameDisplayName().equals("-1")) {
			flag = extendInfo.getNameDisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = c_name;
			} else if (flag.endsWith("1")) {
				value = u_contactName;
			} else if (flag.endsWith("2")) {
				value = a_name;
			} else if (flag.endsWith("3")) {
				value = c_orgNum;
			} else if (flag.endsWith("4")) {
				value = u_conCertNum;
			}
			request.setUserSurname(value);
			
		}
		
		
		if(!extendInfo.getOrganizationDisplayName().equals("-1")){
			flag = extendInfo.getOrganizationDisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = c_name;
			} else if (flag.endsWith("1")) {
				value = u_province;
			} 
			request.setUserOrganization(value);
		}
		
		
		if (!extendInfo.getEmailDisplayName().equals("-1")) {
			flag = extendInfo.getEmailDisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = u_email;
			} else if (flag.endsWith("1")) {
				value = a_email;
			}
			request.setUserEmail(value);
		}
		if (!extendInfo.getOrgunitDisplayName().equals("-1")) {
			flag = extendInfo.getOrgunitDisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = appName;
			}
			request.setUserOrgunit(value);
		}

		if (!extendInfo.getAddtionalField1DisplayName().equals("-1")) {
			flag = extendInfo.getAddtionalField1DisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = certSort;
			} else if (flag.equals("1")) {
				value = c_name;
			} else if (flag.equals("2")) {
				value = appName;
			} else if (flag.equals("3")) {
				value = u_conCertNum;
			} else if (flag.equals("4")) {
				value = c_orgNum;
			} else if (flag.equals("5")) {
				value = a_idCard;
			} else if (flag.equals("6")) {
				value = productName;
			} else if (flag.equals("7")) {
				value = c_name2;
			}
			request.setUserAdditionalField1(value);
		}

		if (!extendInfo.getAddtionalField2DisplayName().equals("-1")) {
			flag = extendInfo.getAddtionalField2DisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = certSort;
			} else if (flag.equals("1")) {
				value = c_name;
			} else if (flag.equals("2")) {
				value = u_uid;
			} else if (flag.equals("3")) {
				value = productName;
			} else if (flag.equals("4")) {
				value = u_conCertNum;
			} else if (flag.equals("5")) {
				value = c_orgNum;
			} else if (flag.equals("6")) {
				value = a_idCard;
			}
			request.setUserAdditionalField2(value);
		}

		if (!extendInfo.getAddtionalField3DisplayName().equals("-1")) {
			flag = extendInfo.getAddtionalField3DisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = certSort;
			} else if (flag.equals("1")) {
				value = appName;
			} else if (flag.equals("2")) {
				value = u_uid;
			}
			request.setUserAdditionalField3(value);
		}

		if (!extendInfo.getAddtionalField4DisplayName().equals("-1")) {
			flag = extendInfo.getAddtionalField4DisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = c_certNum;
			} else if (flag.equals("1")) {
				value = u_uid;
			} else if (flag.equals("2")) {
				value = u_conCertNum;
			} else if (flag.equals("3")) {
				value = certSort;
			} else if (flag.equals("4")) {
				value = a_idCard;
			}
			request.setUserAdditionalField4(value);
		}

		if (!extendInfo.getAddtionalField5DisplayName().equals("-1")) {
			flag = extendInfo.getAddtionalField5DisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = certSort;
			} else if (flag.equals("1")) {
				value = productName;
			}
			request.setUserAdditionalField5(value);
		}

		if (!extendInfo.getAddtionalField6DisplayName().equals("-1")) {
			flag = extendInfo.getAddtionalField6DisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = c_orgNum;
			}
			request.setUserAdditionalField6(value);
		}

		if (!extendInfo.getAddtionalField7DisplayName().equals("-1")) {
			flag = extendInfo.getAddtionalField7DisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = c_certNum;
			}
			request.setUserAdditionalField7(value);
		}

		if (!extendInfo.getAddtionalField8DisplayName().equals("-1")) {
			flag = extendInfo.getAddtionalField8DisplayName();
			String value = new String();
			if (flag.equals("0")) {
				value = certSort;
			} else if (flag.equals("1")) {
				value = c_name;
			} else if (flag.equals("2")) {
				value = appName;
			} else if (flag.equals("3")) {
				value = u_conCertNum;
			} else if (flag.equals("4")) {
				value = productName;
			} else if (flag.equals("5")) {
				value = c_orgNum;
			} else if (flag.equals("6")) {
				value = u_uid;
			} else if (flag.equals("7")) {
				value = a_idCard;
			}
			request.setUserAdditionalField8(value);
		}

		return request;
	}

	/**
	 * RA配置生成制证页面的列表
	 * 
	 * @param company
	 * @param uesr
	 * @param applyInfo
	 * @param extendInfo
	 * @return 数组 0 名称 1 值
	 */
	public static List<String[]> outPageLine(WorkDealInfo dealInfo,
			ConfigRaAccountExtendInfo extendInfo) {
		List<String[]> result = new ArrayList<String[]>();
		try {
			WorkCompany company = dealInfo.getWorkCompany();
			WorkUser user = dealInfo.getWorkUser();
			WorkCertApplyInfo applyInfo = dealInfo.getWorkCertInfo()
					.getWorkCertApplyInfo();
			ConfigApp app = dealInfo.getConfigApp();

			String flag = new String();
			// 取出所有信息
			String c_name = company.getCompanyName();
			String c_orgNum = company.getOrganizationNumber();
			String c_certNum = company.getComCertficateNumber();

			String u_contactName = user.getContactName();
			String u_email = user.getContactEmail();
			String u_conCertNum = user.getConCertNumber();
			String u_uid = user.getContactTel();

			String a_name = applyInfo.getName();
			String a_idCard = applyInfo.getIdCard();
			String a_email = applyInfo.getEmail();

			String appName = app.getAppName();
//			String certSort = dealInfo.getCertSort().toString();
			String productName = ProductType.productTypeEnglishMap.get(Integer
					.valueOf(dealInfo.getConfigProduct().getProductName()));
			if (extendInfo!=null&&!"-1".equals(extendInfo.getNameDisplayName())) {
				flag = extendInfo.getNameDisplayName();
				String value = new String();
				String name = new String();
				if (flag.equals("0")) {
					value = c_name;
					name = "单位名称";
				} else if (flag.endsWith("1")) {
					value = u_contactName;
					name = "经办人姓名";
				} else if (flag.endsWith("2")) {
					value = a_name;
					name = "证书申请人姓名";
				} else if (flag.endsWith("3")) {
					value = c_orgNum;
					name = "组织机构代码";
				} else if (flag.endsWith("4")) {
					value = u_conCertNum;
					name = "经办人身份证号";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}
			if (extendInfo!=null&&!extendInfo.getOrganizationDisplayName().equals("-1")) {
				flag = extendInfo.getOrganizationDisplayName();
				String value = new String();
				String name = new String();
				if (flag.equals("0")) {
					name = "组织机构代码";
					value = c_orgNum;
				} else if (flag.endsWith("1")) {
					value = u_conCertNum;
					name = "经办人身份证号";
				} else if (flag.endsWith("2")) {
					value = a_idCard;
					name = "证书申请人身份证号";
				} else if (flag.endsWith("3")) {
					value = c_name;
					name = "单位名称";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getEmailDisplayName().equals("-1")) {
				flag = extendInfo.getEmailDisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
					value = u_email;
					name = "经办人邮箱";
				} else if (flag.endsWith("1")) {
					value = a_email;
					name = "证书申请人邮箱";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}
			if (extendInfo!=null&&!extendInfo.getOrgunitDisplayName().equals("-1")) {
				flag = extendInfo.getOrgunitDisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
					value = appName;
					name = "应用名称";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField1DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField1DisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
//					value = certSort;
					name = "多证书编号";
				} else if (flag.equals("1")) {
					value = c_name;
					name = "单位名称";
				} else if (flag.equals("2")) {
					value = appName;
					name = "应用名称";
				} else if (flag.equals("3")) {
					value = u_conCertNum;
					name = "经办人身份证号";
				} else if (flag.equals("4")) {
					value = c_orgNum;
					name = "组织机构代码";
				} else if (flag.equals("5")) {
					value = a_idCard;
					name = "证书申请人身份证号";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField2DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField2DisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
//					value = certSort;
					name = "多证书编号";
				} else if (flag.equals("1")) {
					value = c_name;
					name = "单位名称";
				} else if (flag.equals("2")) {
					value = u_uid;
					name = "业务系统UID";
				} else if (flag.equals("3")) {
					value = productName;
					name = "证书类型";
				} else if (flag.equals("4")) {
					value = u_conCertNum;
					name = "经办人身份证号";
				} else if (flag.equals("5")) {
					value = c_orgNum;
					name = "组织机构代码";
				} else if (flag.equals("6")) {
					value = a_idCard;
					name = "证书申请人身份证号 ";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField3DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField3DisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
//					value = certSort;
					name = "多证书编号";
				} else if (flag.equals("1")) {
					value = appName;
					name = "应用名称";
				} else if (flag.equals("2")) {
					value = u_uid;
					name = "业务系统UID";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField4DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField4DisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
					value = c_certNum;
					name = "工商营业执照注册号";
				} else if (flag.equals("1")) {
					value = u_uid;
					name = "业务系统UID";
				} else if (flag.equals("2")) {
					value = u_conCertNum;
					name = "经办人身份证号";
				} else if (flag.equals("3")) {
//					value = certSort;
					name = "多证书编号";
				} else if (flag.equals("4")) {
					value = a_idCard;
					name = "证书申请人身份证号";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField5DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField5DisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
//					value = certSort;
					name = "多证书编号";
				} else if (flag.equals("1")) {
					value = productName;
					name = "证书类型";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField6DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField6DisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
					value = c_orgNum;
					name = "组织机构代码";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField7DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField7DisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
					value = c_certNum;
					name = "工商营业执照注册号";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField8DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField8DisplayName();
				String value = new String();
				String name = new String();

				if (flag.equals("0")) {
//					value = certSort;
					name = "多证书编号";
				} else if (flag.equals("1")) {
					value = c_name;
					name = "单位名称";
				} else if (flag.equals("2")) {
					value = appName;
					name = "应用名称";
				} else if (flag.equals("3")) {
					value = u_conCertNum;
					name = "经办人身份证号";
				} else if (flag.equals("4")) {
					value = productName;
					name = "证书类型";
				} else if (flag.equals("5")) {
					value = c_orgNum;
					name = "组织机构代码";
				} else if (flag.equals("6")) {
					value = u_uid;
					name = "业务系统UID";
				} else if (flag.equals("7")) {
					value = a_idCard;
					name = "证书申请人身份证号";
				}
				String[] s = new String[] { name, value };
				result.add(s);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * 
	* @Title: getExtendInfo
	* @Description: TODO(返回证书模版项对应证书新增页面name)
	* @param @param json
	* @param @param extendInfo
	* @param @return    设定文件
	* @return JSONObject    返回类型
	* @throws
	 */
	public static void getExtendInfo(JSONObject json, ConfigRaAccountExtendInfo extendInfo) {
		try {
			String flag = new String();
			if (extendInfo!=null&&!"-1".equals(extendInfo.getNameDisplayName())) {
				flag = extendInfo.getNameDisplayName();
				String value = new String();
				if (flag.equals("0")) {
					value = "companyName";
				} else if (flag.endsWith("1")) {
					value = "contactName";
				} else if (flag.endsWith("2")) {
					value = "pName";
				}else if (flag.endsWith("3")) {
					value = "organizationNumber";
				}else if (flag.endsWith("4")) {
					value = "conCertNumber";
				}
				json.put("nameDisplayName", value);
			}
			if (extendInfo!=null&&!"-1".equals(extendInfo.getCommonNameDisplayName())) {
				flag = extendInfo.getCommonNameDisplayName();
				String value = new String();
				String value1 = new String();
				if (flag.equals("0")) {
					value = "companyName";
				} else if (flag.endsWith("1")) {
					value = "contactName";
				} else if (flag.endsWith("2")) {
					value = "pName";
				} else if (flag.endsWith("3")){
					value = "contactName";
					value1 = "companyName";
				}else if(flag.endsWith("4")){
					value = "contactName";
					value1 = "twoLevelCompanyName";
				}
				json.put("commonNameDisplayName", value);
				
				if(!value1.equals("")){
					json.put("commonNameDisplayName2", value1);
				}
			}
			if (extendInfo!=null&&!extendInfo.getOrganizationDisplayName().equals("-1")) {
				flag = extendInfo.getOrganizationDisplayName();
				String value = new String();
				if (flag.equals("0")) {
					value = "companyName";
				} else if (flag.endsWith("1")) {
					value = "conCertNumber";
				} 
				json.put("organizationDisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getEmailDisplayName().equals("-1")) {
				flag = extendInfo.getEmailDisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "contacEmail";
				} else if (flag.endsWith("1")) {
					value = "pEmail";
				}
				json.put("emailDisplayName", value);
			}
			if (extendInfo!=null&&!extendInfo.getOrgunitDisplayName().equals("-1")) {
				flag = extendInfo.getOrgunitDisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "configApp";
				}
				json.put("orgunitDisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField1DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField1DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "多证书编号";
				} else if (flag.equals("1")) {
					value = "companyName";
				} else if (flag.equals("2")) {
					value = "configApp";
				} else if (flag.equals("3")) {
					value = "conCertNumber";
				} else if (flag.equals("4")) {
					value = "organizationNumber";
				} else if (flag.equals("5")) {
					value = "pIDCard";
				} else if (flag.equals("6")){
					value = "product";
				} else if (flag.equals("7")){
					value = "twoLevelCompanyName";
				}
				json.put("addtionalField1DisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField2DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField2DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "多证书编号";
				} else if (flag.equals("1")) {
					value = "companyName";
				} else if (flag.equals("2")) {
					value = "contactTel";
				} else if (flag.equals("3")) {
					value = "product";
				} else if (flag.equals("4")) {
					value = "conCertNumber";
				} else if (flag.equals("5")) {
					value = "organizationNumber";
				} else if (flag.equals("6")) {
					value = "pIDCard";
				}
				json.put("addtionalField2DisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField3DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField3DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "多证书编号";
				} else if (flag.equals("1")) {
					value = "configApp";
				} else if (flag.equals("2")) {
					value = "contactTel";
				}
				json.put("addtionalField3DisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField4DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField4DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "comCertficateNumber";
				} else if (flag.equals("1")) {
					value = "contactTel";
				} else if (flag.equals("2")) {
					value = "conCertNumber";
				} else if (flag.equals("3")) {
					value = "多证书编号";
				} else if (flag.equals("4")) {
					value = "pIDCard";
				}
				json.put("addtionalField4DisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField5DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField5DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "多证书编号";
				} else if (flag.equals("1")) {
					value = "product";
				}
				json.put("addtionalField5DisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField6DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField6DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "organizationNumber";
				}
				json.put("addtionalField6DisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField7DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField7DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "comCertficateNumber";
				}
				json.put("addtionalField7DisplayName", value);
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField8DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField8DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					value = "多证书编号";
				} else if (flag.equals("1")) {
					value = "companyName";
				} else if (flag.equals("2")) {
					value = "configApp";
				} else if (flag.equals("3")) {
					value = "conCertNumber";
				} else if (flag.equals("4")) {
					value = "product";
				} else if (flag.equals("5")) {
					value = "organizationNumber";
				} else if (flag.equals("6")) {
					value = "contactTel";
				} else if (flag.equals("7")) {
					value = "pIDCard";
				}
				json.put("addtionalField8DisplayName", value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	* @Title: getExtendInfo
	* @Description: TODO(返回证书模版项对应证书新增页面name)
	* @param @param json
	* @param @param extendInfo
	* @param @return    设定文件
	* @return JSONObject    返回类型
	* @throws
	 */
	public static Set<String> getExtendInfos(ConfigRaAccountExtendInfo extendInfo) {
		Set<String> fields = new HashSet<String>();
		try {
			String flag = new String();
			if (extendInfo!=null&&!"-1".equals(extendInfo.getCommonNameDisplayName())) {
				flag = extendInfo.getCommonNameDisplayName();
				if (flag.equals("0")) {
					fields.add("companyName");//单位名称
				} else if (flag.endsWith("1")) {
					fields.add("contactName");//证书持有人名称
				} else if (flag.endsWith("2")) {
					fields.add("pName");//经办人名称
				}
			}
			
			if (extendInfo!=null&&!"-1".equals(extendInfo.getNameDisplayName())) {
				flag = extendInfo.getNameDisplayName();
				if (flag.equals("0")) {
					fields.add("companyName");//单位名称
				} else if (flag.endsWith("1")) {
					fields.add("contactName");//证书持有人名称
				} else if (flag.endsWith("2")) {
					fields.add("pName");//经办人名称
				}else if (flag.endsWith("3")) {
					fields.add("organizationNumber");//组织机构代码
				}else if (flag.endsWith("4")) {
					fields.add("conCertNumber");//证书持有人身份证号
				}
			}
			if (extendInfo!=null&&!extendInfo.getEmailDisplayName().equals("-1")) {
				flag = extendInfo.getEmailDisplayName();
				if (flag.equals("0")) {
					fields.add("contacEmail");//证书持有人邮箱
				} 
			}
			

			if (extendInfo!=null&&!extendInfo.getAddtionalField1DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField1DisplayName();
				if (flag.equals("1")) {
					fields.add("companyName");//单位名称
				}else if (flag.equals("3")) {
					fields.add("conCertNumber");//证书持有人身份证号
				} else if (flag.equals("4")) {
					fields.add("organizationNumber");//组织机构代码
				} else if (flag.equals("5")) {
					fields.add("pIDCard");//经办人身份证号
				}
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField2DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField2DisplayName();
				 if (flag.equals("1")) {
					fields.add("companyName");//单位名称
				} else if (flag.equals("2")) {
					fields.add("contactTel");//业务系统UID
				} else if (flag.equals("4")) {
					fields.add("conCertNumber");//证书持有人身份证号
				} else if (flag.equals("5")) {
					fields.add("organizationNumber");//组织机构代码
				} else if (flag.equals("6")) {
					fields.add("pIDCard");//经办人身份证号
				}
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField3DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField3DisplayName();
				 if (flag.equals("2")) {
					fields.add("contactTel");//业务系统UID
				}
			}
			
			if (extendInfo!=null&&!extendInfo.getAddtionalField4DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField4DisplayName();
				String value = new String();

				if (flag.equals("0")) {
					fields.add("comCertficateNumber");//证件号
				} else if (flag.equals("1")) {
					fields.add("contactTel");//业务系统UID
				} else if (flag.equals("2")) {
					fields.add("conCertNumber");//证书持有人身份证号
				}else if (flag.equals("4")) {
					fields.add("pIDCard");//经办人身份证号
				}
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField6DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField6DisplayName();
				if (flag.equals("0")) {
					fields.add("organizationNumber");//组织机构代码
				}
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField7DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField7DisplayName();
				if (flag.equals("0")) {
					fields.add("comCertficateNumber");//证件号
				}
			}

			if (extendInfo!=null&&!extendInfo.getAddtionalField8DisplayName().equals("-1")) {
				flag = extendInfo.getAddtionalField8DisplayName();
				String value = new String();

				if (flag.equals("1")) {
					fields.add("companyName");//单位名称
				} else if (flag.equals("3")) {
					fields.add("conCertNumber");//证书持有人身份证号
				} else if (flag.equals("5")) {
					fields.add("organizationNumber");//组织机构代码
				} else if (flag.equals("6")) {
					fields.add("contactTel");//业务系统UID
				} else if (flag.equals("7")) {
					fields.add("pIDCard");//经办人身份证号
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fields;
	}
	
	
	
	
	public static String getProvince(String conCertNum){
		
		String province = "";
		
		String first2Num = conCertNum.trim().substring(0, 2);
		
		if(first2Num.equals("11")){
			province = "北京市";
		}else if(first2Num.equals("12")){
			province = "天津市";
		}else if(first2Num.equals("13")){
			province = "河北省";
		}else if(first2Num.equals("14")){
			province = "山西省";
		}else if(first2Num.equals("15")){
			province = "内蒙古自治区";
		}else if(first2Num.equals("21")){
			province = "辽宁省";
		}else if(first2Num.equals("22")){
			province = "吉林省";
		}else if(first2Num.equals("23")){
			province = "黑龙江省";
		}else if(first2Num.equals("31")){
			province = "上海市";
		}else if(first2Num.equals("32")){
			province = "江苏省";
		}else if(first2Num.equals("33")){
			province = "浙江省";
		}else if(first2Num.equals("34")){
			province = "安徽省";
		}else if(first2Num.equals("35")){
			province = "福建省";
		}else if(first2Num.equals("36")){
			province = "江西省";
		}else if(first2Num.equals("37")){
			province = "山东省";
		}else if(first2Num.equals("41")){
			province = "河南省";
		}else if(first2Num.equals("42")){
			province = "湖北省";
		}else if(first2Num.equals("43")){
			province = "湖南省";
		}else if(first2Num.equals("44")){
			province = "广东省";
		}else if(first2Num.equals("45")){
			province = "广西壮族自治区";
		}else if(first2Num.equals("46")){
			province = "海南省";
		}else if(first2Num.equals("50")){
			province = "重庆市";
		}else if(first2Num.equals("51")){
			province = "四川省";
		}else if(first2Num.equals("52")){
			province = "贵州省";
		}else if(first2Num.equals("53")){
			province = "云南省";
		}else if(first2Num.equals("54")){
			province = "西藏自治区";
		}else if(first2Num.equals("61")){
			province = "陕西省";
		}else if(first2Num.equals("62")){
			province = "甘肃省";
		}else if(first2Num.equals("63")){
			province = "青海省";
		}else if(first2Num.equals("64")){
			province = "宁夏回族自治区";
		}else if(first2Num.equals("65")){
			province = "新疆维吾尔自治区";
		}else if(first2Num.equals("71")){
			province = "台湾省";
		}else if(first2Num.equals("81")){
			province = "香港特别行政区";
		}else if(first2Num.equals("82")){
			province = "澳门特别行政区";
		}else {
			province = "未知省份";
		}
	
	
		return province;

	}
}
