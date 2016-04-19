package com.itrus.ca.modules.sys.web;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.modules.constant.SignatureInfoStatus;
import com.itrus.ca.modules.constant.SignatureInfoType;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptEnterInfo;
import com.itrus.ca.modules.receipt.entity.ReceiptInvoice;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.signature.entity.SignatureInfo;
import com.itrus.ca.modules.signature.entity.SignaturePayInfo;
import com.itrus.ca.modules.signature.service.SignatureInfoService;
import com.itrus.ca.modules.signature.service.SignaturePayInfoService;
import com.itrus.ca.modules.sys.entity.Office;
import com.sun.jmx.snmp.Timestamp;

@Controller
@RequestMapping(value = "${adminPath}/sys/dzqzpt")
public class SignatureInterfaceController {

	@Autowired
	private SignatureInfoService signatureInfoService;

	@Autowired
	private ReceiptDepotInfoService receiptDepotInfoService;

	@Autowired
	private ReceiptInvoiceService receiptInvoiceService;

	@Autowired
	private SignaturePayInfoService signaturePayInfoService;

	@Autowired
	private ReceiptEnterInfoService receiptEnterInfoService;

	@RequestMapping(value = "/getdata", method = RequestMethod.POST, headers = { "content-type=application/json" })
	@ResponseBody
	public String getdata(@RequestBody String jsonString) {
		JSONObject json = new JSONObject();

		try {
			json.put("status", -1);
			JSONObject jsonObj = new JSONObject(jsonString);

			// 对头参数进行验证
			String msgHead = validateParamHead(jsonString, jsonObj, 1);
			if (!msgHead.isEmpty()) {
				json.put("msg", msgHead);
				return json.toString();
			}

			// 得到四个头参数
			long reqTime = jsonObj.getLong("timestamp");
			long reqId = jsonObj.getLong("reqId");
			String method = jsonObj.getString("method").trim();
			JSONObject paramObj = jsonObj.getJSONObject("params");

			// 如果请求时间到服务器接收时间大于30s,则认为本次请求不正常

			/*
			 * long accTime = new Timestamp().getDateTime();
			 * if(accTime-reqTime>30*1000){ json.put("msg", "您的请求超时，请重新发送!");
			 * json.put("respId", reqId); return json.toString(); }
			 */

			// 对业务参数进行验证
			String msgBody = validateParamBody(method, paramObj, 1);
			if (!msgBody.isEmpty()) {
				json.put("msg", msgBody);
				return json.toString();
			}

			// 新增业务处理
			if (method.equals("new")) {

				String serialNumber = paramObj.getString("serialNumber").trim().replace(" ", "").toUpperCase();

				// 找到相应印章参数
				SignatureInfo signatureInfo = signatureInfoService.getNewByCertSn(serialNumber);
				if (signatureInfo == null) {
					json.put("msg", "未找到相应业务数据，无法进行印章新增业务!");
					json.put("respId", reqId);
					return json.toString();
				}
				JSONObject dataObj = new JSONObject();

				dataObj.put("sealId", signatureInfo.getId().toString());
				dataObj.put("sealType", SignatureInfoType.getSignatureTypeName(signatureInfo.getSignatureType()));
				dataObj.put("sealName", signatureInfo.getWorkDealInfo().getWorkCompany().getCompanyName());
				dataObj.put("sealYear", Integer.parseInt(signatureInfo.getYear()));
				dataObj.put("applyUser", signatureInfo.getWorkDealInfo().getWorkUser().getContactName());
				dataObj.put("applyPhone", signatureInfo.getWorkDealInfo().getWorkUser().getContactPhone());
				dataObj.put("serialNumber", signatureInfo.getWorkCertInfo().getSerialnumber());
				dataObj.put("comment", "");

				json.put("msg", "业务数据支持办理新增业务!");

				json.putOnce("data", dataObj);

				// 变更业务处理
			} else if (method.equals("change")) {

				String oldSerialNumber = paramObj.getString("oldSerialNumber").trim().replace(" ", "").toUpperCase();
				String newSerialNumber = paramObj.getString("newSerialNumber").trim().replace(" ", "").toUpperCase();
				String sealId = paramObj.getString("sealId").trim();
				SignatureInfo signatureInfo = signatureInfoService.getChangeBySignatureId(oldSerialNumber,
						newSerialNumber, Long.parseLong(sealId));
				if (signatureInfo == null) {
					json.put("msg", "未找到相应业务数据，无法进行印章变更业务!");
					json.put("respId", reqId);

					return json.toString();
				}

				JSONObject dataObj = new JSONObject();
				dataObj.put("sealId", signatureInfo.getId().toString());
				json.put("msg", "业务数据支持办理变更业务!");
				json.putOnce("data", dataObj);

				// 更新业务处理
			} else if (method.equals("update")) {

				String sealId = paramObj.getString("sealId").trim();
				SignatureInfo signatureInfo = signatureInfoService.getUpdateBySignatureId(Long.parseLong(sealId));
				if (signatureInfo == null) {
					json.put("msg", "未找到相应业务数据，无法进行印章续期业务!");
					json.put("respId", reqId);
					return json.toString();
				}

				JSONObject dataObj = new JSONObject();

				dataObj.put("sealId", signatureInfo.getId().toString());
				dataObj.put("updateYear", Long.parseLong(signatureInfo.getYear()));

				json.put("msg", "业务数据支持办理续期业务!");

				json.putOnce("data", dataObj);

				// 方法名参数错误处理
			} else {
				json.put("msg", "method参数值必须为‘new’,'change','update'其中之一!");
				json.put("respId", reqId);
				return json.toString();
			}

			json.put("status", 1);
			json.put("respId", reqId);

		} catch (JSONException e0) {
			try {
				e0.printStackTrace();
				json.put("msg", "JSON解析发生错误 " + e0.getMessage());
				return json.toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				json.put("msg", e.getMessage());
				return json.toString();
			} catch (Exception e2) {

			}
			e.printStackTrace();

		}

		return json.toString();
	}

	@RequestMapping(value = "/pushdata", method = RequestMethod.POST, headers = { "content-type=application/json" })
	@ResponseBody
	public String pushdata(@RequestBody String jsonString) throws ParseException {
		JSONObject json = new JSONObject();
		try {
			json.put("status", -1);
			JSONObject jsonObj = new JSONObject(jsonString);

			// 对头参数进行验证
			String msgHead = validateParamHead(jsonString, jsonObj,2);
			if (!msgHead.isEmpty()) {
				json.put("msg", msgHead);
				return json.toString();
			}
			
			//得到三个头参数
			long reqTime = jsonObj.getLong("timestamp");
			long reqId = jsonObj.getLong("reqId");
			JSONObject paramObj = jsonObj.getJSONObject("params");
			
			//如果请求时间到服务器接收时间大于30s,则认为本次请求不正常
			long accTime = new Timestamp().getDateTime();
			
			/*if(accTime-reqTime>30*1000){ 
			json.put("msg", "您的请求超时，请重新发送!");
			json.put("respId", reqId); 
			return json.toString(); 
			}*/
			
			// 对业务参数进行验证
			String msgBody = validateParamBody(null, paramObj,2);
			if (!msgBody.isEmpty()) {
				json.put("msg", msgBody);
				return json.toString();
			}
			
			//得到两个业务参数
			String serialId = paramObj.getString("serialId");
			Integer serialStatus = paramObj.getInt("serialStatus");

			//查找到相关业务，如果为空返回
			SignatureInfo signatureInfo = signatureInfoService.get(Long.parseLong(serialId));
			if (signatureInfo == null) {
				json.put("msg", "未找到该业务信息!请确认参数正确!");
				json.put("respId", reqId);
				return json.toString();
			}
			SignatureInfo nextSignatureInfo = signatureInfoService.getByPreId(Long.parseLong(serialId));
			
			if(nextSignatureInfo!=null){
				if(serialStatus == 1 || serialStatus == 2){
					if(!nextSignatureInfo.getSignatureInfoStatus().equals(SignatureInfoStatus.STATUS_REVOKE_USER)){
						json.put("msg", "该条业务已有后续业务，无法操作该业务！");
						json.put("respId", reqId);
						return json.toString();	
					}else{
						json.put("msg", "该条业务已被吊销，无法操作该业务！");
						json.put("respId", reqId);
						return json.toString();
					}
					
				}
				
				if(serialStatus == 3){
					if(nextSignatureInfo.getSignatureInfoStatus().equals(SignatureInfoStatus.STATUS_REVOKE_USER)){
						json.put("msg", "该条业务已被吊销，无法再次吊销！");
						json.put("respId", reqId);
						return json.toString();
					}else{
						json.put("msg", "该条业务已有后续业务，无法吊销该业务！");
						json.put("respId", reqId);
						return json.toString();
					}
				}
			}
			
		
			
			//签章完成
			if (serialStatus == 1) {
				
				if(signatureInfo.getSignatureInfoStatus().equals(SignatureInfoStatus.STATUS_ABNORMAL_USER)){
					json.put("msg", "该签章业务已办理成功,无法再次办理!");
					json.put("respId", reqId);
					return json.toString();
				}
				
				signatureInfo.setManageDate(new Date());
				signatureInfo.setSignatureInfoStatus(serialStatus.toString());
				if (signatureInfo.getSignatureInfoType().equals(SignatureInfoType.TYPE_ADD_SIGNATURE)) {
					signatureInfo.setStatus(SignatureInfoStatus.STATUS_START);
				} else if (signatureInfo.getSignatureInfoType().equals(SignatureInfoType.TYPE_CHANGE_SIGNATURE)) {
					signatureInfo.setStatus(SignatureInfoStatus.STATUS_CHANGE);
				} else if (signatureInfo.getSignatureInfoType().equals(SignatureInfoType.TYPE_UPDATE_SIGNATURE)) {
					signatureInfo.setStatus(SignatureInfoStatus.STATUS_UPDATE);
				}
				
				SignaturePayInfo pay = signaturePayInfoService.findBySignatureInfo(signatureInfo);
				
				if(pay.getIsReceipt()==true){
					ReceiptInvoice receiptInvoice = pay.getReceiptInvoice();
					receiptInvoice.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
					ReceiptDepotInfo receiptDepotInfo = receiptInvoice.getReceiptDepotInfo();
					receiptDepotInfo.setReceiptOut(receiptDepotInfo.getReceiptOut()+receiptInvoice.getReceiptMoney());
					receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue()-receiptInvoice.getReceiptMoney());
					receiptDepotInfoService.save(receiptDepotInfo);
				}
				
				
				json.put("msg", "该印章业务已标记为成功！");
						
			//签章失败	
			} else if (serialStatus == 2) {
				
				if(signatureInfo.getSignatureInfoStatus().equals(SignatureInfoStatus.STATUS_ABNORMAL_USER)){
					json.put("msg", "该签章业务已办理成功,无法使其签章失败!");
					json.put("respId", reqId);
					return json.toString();
				}
				
				signatureInfo.setSignatureInfoStatus(serialStatus.toString());
				
				json.put("msg", "该印章业务已标记为失败！");
				
			//签章吊销，生成一条新的signatureInfo	
			} else if (serialStatus == 3) {
				
				if(!signatureInfo.getSignatureInfoStatus().equals(SignatureInfoStatus.STATUS_ABNORMAL_USER)){
					json.put("msg", "该签章业务还未办理成功,无法吊销!");
					json.put("respId", reqId);
					return json.toString();
				}
				
				SignatureInfo signatureInfoNew = new SignatureInfo(signatureInfo.getWorkDealInfo(),
						signatureInfo.getSignatureAgent(), SignatureInfoType.TYPE_REVOKE_SIGNATURE,
						signatureInfo.getSignatureType(), signatureInfo.getSignatureAppName(), signatureInfo.getYear(),
						signatureInfo.getSvn(), signatureInfo.getWorkCertInfo(), null,
						SignatureInfoStatus.STATUS_REVOKE_USER, SignatureInfoStatus.STATUS_CANCEL,
						signatureInfo.getStartDate(), signatureInfo.getEndDate(), null, null, null, null,
						signatureInfo.getConfigApp(), signatureInfo.getId(),
						signatureInfo.getFirstId() == null ? signatureInfo.getId() : signatureInfo.getFirstId(),
						signatureInfo.getOfficeId(), signatureInfo.getAreaId());
				
				signatureInfoNew.setCreateBy(signatureInfo.getCreateBy());
				signatureInfoNew.setUpdateBy(signatureInfo.getUpdateBy());
				signatureInfoNew.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
				signatureInfoNew.setCreateDate(new Date());
				signatureInfoNew.setUpdateDate(new Date());
				
				signatureInfo.setDelFlag(BaseEntity.DEL_FLAG_DELETE);

				signatureInfoService.save(signatureInfoNew);
				
				SignaturePayInfo pay = signaturePayInfoService.findBySignatureInfo(signatureInfo);
				if(pay.getIsReceipt()==true){
					//如果是录入当天吊销,则发票退库
					if(signatureInfo.getEnterDate().getDay()==new Date().getDay()){
						ReceiptInvoice receiptInvoice = pay.getReceiptInvoice();
						ReceiptDepotInfo receiptDepotInfo = receiptInvoice.getReceiptDepotInfo();
						receiptDepotInfo.setReceiptResidue(receiptDepotInfo.getReceiptResidue()+receiptInvoice.getReceiptMoney());
						receiptDepotInfo.setReceiptOut(receiptDepotInfo.getReceiptOut()-receiptInvoice.getReceiptMoney());
						receiptDepotInfoService.save(receiptDepotInfo);
						ReceiptEnterInfo enterInfo =  new ReceiptEnterInfo();
						enterInfo.setReceiptDepotInfo(receiptDepotInfo);
						enterInfo.setNow_Money(receiptInvoice.getReceiptMoney());
						enterInfo.setBeforMoney(enterInfo
								.getReceiptDepotInfo().getReceiptResidue()
								- Double.valueOf(receiptInvoice.getReceiptMoney()));
						enterInfo.setReceiptMoney(enterInfo
								.getReceiptDepotInfo().getReceiptResidue());
						enterInfo.setReceiptType(6);// 吊销回退入库
						
						
						enterInfo.setCreateBy(receiptDepotInfo.getCreateBy());
						enterInfo.setUpdateBy(receiptDepotInfo.getUpdateBy());
						enterInfo.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
						enterInfo.setCreateDate(new Date());
						enterInfo.setUpdateDate(new Date());
						
						receiptEnterInfoService.save(enterInfo);
						
					}	
				}
				
				json.put("msg", "该印章业务已标记为吊销！");
				
			}
			signatureInfoService.save(signatureInfo);
			json.put("status", 1);
			json.put("respId", reqId);
		} catch (JSONException e0){
			try {
				e0.printStackTrace();
				json.put("msg", "JSON解析发生错误 "+e0.getMessage());
				return json.toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				json.put("msg", e.getMessage());
				return json.toString();
			} catch (Exception e2) {

			}
			e.printStackTrace();

		}
		return json.toString();
	}

	/*
	 * 对头参数进行验证
	 * 
	 * @param jsonString
	 * 
	 * @param jsonObj
	 * 
	 * @param status 1代表getdata 2代表pushdata
	 * 
	 * @return
	 */
	public String validateParamHead(String jsonString, JSONObject jsonObj, Integer status) {
		StringBuffer msg = new StringBuffer();
		try {
			if (jsonString.indexOf("timestamp") <= -1 || jsonObj.isNull("timestamp")) {
				msg.append("参数有误,未包含'timestamp'");
			}

			if (jsonString.indexOf("reqId") <= -1 || jsonObj.isNull("reqId")) {
				if (msg.length() > 0) {
					msg.append(",'reqId'");
				} else {
					msg.append("参数有误,未包含'reqId'");
				}

			}

			if (jsonString.indexOf("params") <= -1 || jsonObj.isNull("params")) {
				if (msg.length() > 0) {
					msg.append(",'params'");
				} else {
					msg.append("参数有误,未包含'params'");
				}
			}

			if (status == 1) {
				if (jsonString.indexOf("method") <= -1 || jsonObj.isNull("method")) {
					if (msg.length() > 0) {
						msg.append(",'method'");
					} else {
						msg.append("参数有误,未包含'method'");
					}
				}
			}

			if (msg.length() > 0) {
				msg.append(" 请检查参数！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg.toString();
	}

	/*
	 * 对业务参数进行验证
	 * 
	 * @param method
	 * 
	 * @param jsonObj
	 * 
	 * @param status 1代表getdata 2代表pushdata
	 * 
	 * @return
	 */
	public String validateParamBody(String method, JSONObject jsonObj, Integer status) {
		StringBuffer msg = new StringBuffer();
		try {

			if (status == 1) {
				if (method.equals("new")) {
					if (jsonObj.isNull("serialNumber")) {
						msg.append("参数有误,未包含'serialNumber' 请检查参数!");
					}
				} else if (method.equals("change")) {
					if (jsonObj.isNull("oldSerialNumber")) {
						msg.append("参数有误,未包含'oldSerialNumber'");
					}
					if (jsonObj.isNull("newSerialNumber")) {
						if (msg.length() > 0) {
							msg.append(",'newSerialNumber'");
						} else {
							msg.append("参数有误,未包含‘newSerialNumber’");
						}
					}
					if (jsonObj.isNull("sealId")) {
						if (msg.length() > 0) {
							msg.append(",'sealId'");
						} else {
							msg.append("参数有误,未包含'sealId'");
						}
					}

					if (msg.length() > 0) {
						msg.append(" 请检查参数！");
					}
				} else if (method.equals("update")) {
					if (jsonObj.isNull("sealId")) {
						msg.append("参数有误,未包含'sealId' 请检查参数!");
					}
				} else {
					msg.append("参数值错误,method参数值必须为'new','change','update'其中之一!");
				}
			} else if (status == 2) {
				if (jsonObj.isNull("serialId")) {
					msg.append("参数有误,未包含'serialId'");
				}
				if (jsonObj.isNull("serialStatus")) {
					if (msg.length() > 0) {
						msg.append(",'serialStatus'");
					} else {
						msg.append("参数有误,未包含'serialStatus'");
					}
				}

				if (msg.length() > 0) {
					msg.append(" 请检查参数！");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg.toString();
	}
}
