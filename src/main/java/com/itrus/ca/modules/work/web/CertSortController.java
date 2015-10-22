package com.itrus.ca.modules.work.web;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
@Controller
@RequestMapping(value = "/certSort")
public class CertSortController extends BaseController {

	private WorkDealInfoService workDealInfoService = SpringContextHolder.getBean(WorkDealInfoService.class);

	/*
	 * organizationNumber 组织机构代码
	 * conCertNumber 证书持有人身份证号
	 * 1=企业证书
	 * 2=个人证书(企业)
	 * 3=机构证书
	 * 6=个人证书(机构)
	 */
	@RequestMapping(value = "findCertSort")
	@ResponseBody
	public String makeCert(
			@RequestParam(value = "organizationNumber", required = false) String organizationNumber,
			@RequestParam(value = "conCertNumber" , required = false) String conCertNumber,
			@RequestParam(value = "contactName" , required = false) String contactName,
			@RequestParam(value = "companyName" , required = false) String companyName,
			@RequestParam(value = "companyName" , required = false) Integer productTdId
			) {
		JSONObject json = new JSONObject();
		try {
			if (productTdId.equals(1)) {
				if(organizationNumber ==null || organizationNumber.equals("")){
					json.put("status", "fail");
					json.put("certsInSccA", "");
					json.put("errmsg", "企业证书的组织机构代码不能为空");
				}else{
					Integer certSort = workDealInfoService.getCertSortByOrganizationNumber(organizationNumber,productTdId);
					json.put("status", "success");
					json.put("certsInSccA", certSort);
					json.put("errmsg", "");
				}				
			}else if(productTdId.equals(2) || productTdId.equals(6)){
				if (conCertNumber!=null && !conCertNumber.equals("")) {
					Integer certSort = workDealInfoService.getCertSortByConCertNumber(conCertNumber);
					json.put("status", "success");
					json.put("certsInSccA", certSort);
					json.put("errmsg", "");
				}else if (contactName!=null && !contactName.equals("")) {
					Integer certSort = workDealInfoService.getCertSortByContactName(contactName);
					json.put("status", "success");
					json.put("certsInSccA", certSort);
					json.put("errmsg", "");
				}else{
					json.put("status", "fail");
					json.put("certsInSccA", "");
					json.put("errmsg", "个人证书的身份证或持有人姓名不能为空");
					
				}
			}else if(productTdId.equals(3)){
				if(organizationNumber!=null && !organizationNumber.equals("")) {
					Integer certSort = workDealInfoService.getCertSortByOrganizationNumber(organizationNumber,productTdId);
					json.put("status", "success");
					json.put("certsInSccA", certSort);
					json.put("errmsg", "");
				}else if(companyName!=null && !companyName.equals("")){
					Integer certSort = workDealInfoService.getCertSortByCompanyName(companyName,productTdId);
					json.put("status", "success");
					json.put("certsInSccA", certSort);
					json.put("errmsg", "");
				}else{
					json.put("status", "fail");
					json.put("certsInSccA", "");
					json.put("errmsg", "机构证书的组织机构代码或单位名称不能为空");
				}
			}else{
				json.put("status", "fail");
				json.put("certsInSccA", "");
				json.put("errmsg", "没有该产品类型！");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

//多证书编号生成策略
//1：企业证书：以组织机构代码为准，如果888888888这个组织机构代码已经办理了1个当前新增的第二个证书的多证书编号为2 下一个为3，
//2：个人证书：以身份证号为准，如果没有录入身份证号的，以证书持有人姓名为准。
//3：机构证书：首先以组织机构代码为准，如果没有组织机构代码，请以单位名称为准。
//4：新增业务新生成多证书编号，
//5：维护更新、补办、信息变更及组合业务都要延用原来的多证书编号，已经吊销的证书的多证书编号可以继续使用！

	

	
	
}
