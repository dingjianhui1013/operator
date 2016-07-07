/**
 *2016年7月5日 下午6:01:17
 */
package com.itrus.ca.modules.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itrus.ca.common.utils.FileHelper;
import com.itrus.ca.common.utils.PoiExcelHelper;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.task.entity.BasicInfoScca;

/**
 * @author: liubin
 *
 */
public class ProcessExcel {

	private static String file = "/Volumes/Macintosh HD/dbfile/ca/source.xls";

	private static String out = "/Volumes/Macintosh HD/dbfile/ca/source.sql";

	private static void process() {
		PoiExcelHelper poi = new PoiExcelHelper();
		List<ArrayList<String>> content = poi.read(file, 0);
		int flag = 0;
		for (ArrayList<String> e : content) {
			if (flag == 0) {
				flag++;
				continue;
			}
			BasicInfoScca po = new BasicInfoScca();
			po.setId(getMaybeNulltoLong(e.get(0)));
			po.setAppId(getMaybeNulltoLong(e.get(1)));
			po.setProductId(getMaybeNulltoLong(e.get(2)));
			po.setProductLabel(getMaybeNulltoInteger(e.get(3)));
			po.setYear(getMaybeNulltoInteger(e.get(4)));
			po.setCompanyName(getMaybeNulltoString(e.get(5)));
			po.setCompanyType(getMaybeNulltoInteger(e.get(6)));
			po.setOrganizationNumber(getMaybeNulltoString(e.get(7)));
			po.setOrgExpirationTime(getMaybeNulltoString(e.get(8)));
			po.setSelectLv(getMaybeNulltoInteger(e.get(9)));
			po.setComCertificateType(getMaybeNulltoInteger(e.get(10)));
			po.setComCertficateNumber(getMaybeNulltoString(e.get(11)));
			po.setComCertificateTime(getMaybeNulltoString(e.get(12)));
			po.setLegalName(getMaybeNulltoString(e.get(13)));
			po.setSProvince(getMaybeNulltoString(e.get(14)));
			po.setSCity(getMaybeNulltoString(e.get(15)));
			po.setSCounty(getMaybeNulltoString(e.get(16)));
			po.setAddress(getMaybeNulltoString(e.get(17)));
			po.setCompanyMobile(getMaybeNulltoString(e.get(18)));
			po.setContactName(getMaybeNulltoString(e.get(19)));
			po.setConCertType(getMaybeNulltoInteger(e.get(20)));
			po.setConCertNumber(getMaybeNulltoString(e.get(21)));
			po.setContactEmail(getMaybeNulltoString(e.get(22)));
			po.setContactPhone(getMaybeNulltoString(e.get(23)));
			po.setContactTel(getMaybeNulltoString(e.get(24)));
			po.setContactSex(getMaybeNulltoString(e.get(25)));
			po.setName(getMaybeNulltoString(e.get(26)));
			po.setIdCard(getMaybeNulltoString(e.get(27)));
			po.setEmail(getMaybeNulltoString(e.get(28)));
			po.setIssuerDn(getMaybeNulltoString(e.get(29)));
			po.setSubjectDn(getMaybeNulltoString(e.get(30)));
			po.setNotafter(getMaybeNulltoString(e.get(31)));
			po.setNotbefore(getMaybeNulltoString(e.get(32)));
			po.setSerialnumber(getMaybeNulltoString(e.get(33)));
			po.setCertType(getMaybeNulltoString(e.get(34)));
			po.setAppName(getMaybeNulltoString(e.get(35)));
			po.setCertCounts(getMaybeNulltoString(e.get(36)));
			po.setMultiCertSns(getMaybeNulltoInteger(e.get(37)));
			po.setKeyAndUsbSn(getMaybeNulltoString(e.get(38)));
			po.setCertValidDays(getMaybeNulltoInteger(e.get(39)));
			po.setAgentId(getMaybeNulltoLong(e.get(40)));
			po.setOpenTime(getMaybeNulltoString(e.get(41)));
			po.setFirstCertSN(getMaybeNulltoString(e.get(42)));
			po.setProcessTime(getMaybeNulltoString(e.get(43)));
			po.setDealInfoType(getMaybeNulltoString(e.get(44)));
			recordLine(po);
			flag++;
		}
	}

	private static void recordLine(BasicInfoScca po) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO  BASE_INFO_SCCA(");
		sql.append("id,app_id,product_id,product_label,year,company_name,");
		sql.append("company_type,organization_number,org_expiration_time,select_lv,");
		sql.append("com_certificate_type,com_certficate_number,com_certificate_time,");
		sql.append("legal_name,s_province, s_city, s_county, address,");
		sql.append("company_mobile,contact_name,con_cert_type,con_cert_number,contact_email,contact_phone,");
		sql.append("contact_tel,contact_sex,name,id_card,email,issuer_dn,subject_dn,notafter,notbefore,");
		sql.append("serialnumber,cert_Type,app_Name,cert_Counts,multi_Cert_Sns,key_And_Usb_Sn,");
		sql.append("cert_Valid_Days,agent_Id,open_time,FIRST_CERT_SN,PROCESS_TIME,DEAL_INFO_TYPE)");
		sql.append(" VALUES(");
		sql.append(po.getId()).append(",");
		sql.append(po.getAppId()).append(",");
		sql.append(getMaybeNull(po.getProductId(), false)).append(",");
		sql.append(getMaybeNull(po.getProductLabel(), false)).append(",");
		sql.append(getMaybeNull(po.getYear(), false)).append(",");
		sql.append(getMaybeNull(po.getCompanyName(), true)).append(",");
		sql.append(getMaybeNull(po.getCompanyType(), false)).append(",");
		sql.append(getMaybeNull(po.getOrganizationNumber(), true)).append(",");
		sql.append(getMaybeNull(po.getOrgExpirationTime(), true)).append(",");
		sql.append(getMaybeNull(po.getSelectLv(), false)).append(",");
		sql.append(getMaybeNull(po.getComCertificateType(), false)).append(",");
		sql.append(getMaybeNull(po.getComCertficateNumber(), true)).append(",");
		sql.append(getMaybeNull(po.getComCertificateTime(), true)).append(",");
		sql.append(getMaybeNull(po.getLegalName(), true)).append(",");
		sql.append(getMaybeNull(po.getSProvince(), true)).append(",");
		sql.append(getMaybeNull(po.getSCity(), true)).append(",");
		sql.append(getMaybeNull(po.getSCounty(), true)).append(",");
		sql.append(getMaybeNull(po.getAddress(), true)).append(",");
		sql.append(getMaybeNull(po.getCompanyMobile(), true)).append(",");
		sql.append(getMaybeNull(po.getContactName(), true)).append(",");
		sql.append(getMaybeNull(po.getConCertType(), false)).append(",");
		sql.append(getMaybeNull(po.getConCertNumber(), true)).append(",");
		sql.append(getMaybeNull(po.getContactEmail(), true)).append(",");
		sql.append(getMaybeNull(po.getContactPhone(), true)).append(",");
		sql.append(getMaybeNull(po.getContactTel(), true)).append(",");
		sql.append(getMaybeNull(po.getContactSex(), true)).append(",");
		sql.append(getMaybeNull(po.getName(), true)).append(",");
		sql.append(getMaybeNull(po.getIdCard(), true)).append(",");
		sql.append(getMaybeNull(po.getEmail(), true)).append(",");
		sql.append(getMaybeNull(po.getIssuerDn(), true)).append(",");
		sql.append(getMaybeNull(po.getSubjectDn(), true)).append(",");
		sql.append(getMaybeNull(po.getNotafter(), true)).append(",");
		sql.append(getMaybeNull(po.getNotbefore(), true)).append(",");
		sql.append(getMaybeNull(po.getSerialnumber(), true)).append(",");
		sql.append(getMaybeNull(po.getCertType(), true)).append(",");
		sql.append(getMaybeNull(po.getAppName(), true)).append(",");
		sql.append(getMaybeNull(po.getCertCounts(), true)).append(",");
		sql.append(getMaybeNull(po.getMultiCertSns(), false)).append(",");
		sql.append(getMaybeNull(po.getKeyAndUsbSn(), true)).append(",");
		sql.append(getMaybeNull(po.getCertValidDays(), false)).append(",");
		sql.append(po.getAgentId()).append(",");
		sql.append(getTimeString(po.getOpenTime())).append(",");
		sql.append(getMaybeNull(po.getFirstCertSN(), true)).append(",");
		sql.append(getTimeString(po.getProcessTime())).append(",");
		sql.append(getMaybeNull(po.getDealInfoType(), true));
		sql.append(");\r\n");
		System.out.println(sql);
		FileHelper.recordLineToFile(out, sql.toString(), true);
	}

	private static String getMaybeNull(Object o, boolean isString) {
		String res = o == null || o.toString().length() <= 0 ? "NULL" : o
				.toString().trim();
		if (isString && !StringHelper.isNull(res) && !res.equals("NULL"))
			res = "'" + res + "'";
		return res;
	}

	private static String getTimeString(Object o) {
		String res = o == null || o.toString().length() <= 0 ? "" : o
				.toString().trim();
		if (!StringHelper.isNull(res)) {
			res = "'"
					+ StringHelper.getSystime("yyyy/MM/dd HH:mm:ss", new Long(
							res)) + "'";
		}
		return res;
	}

	private static String getMaybeNulltoString(Object o) {
		return o == null ? "NULL" : o.toString().trim();
	}

	private static Long getMaybeNulltoLong(Object o) {
		String s = getMaybeNulltoString(o);
		if (!StringHelper.isNull(s) && s.indexOf(".") > -1) {
			s = s.substring(0, s.indexOf("."));
		}
		return StringHelper.isNull(s) ? null : new Long(s);
	}

	private static Integer getMaybeNulltoInteger(Object o) {
		String s = getMaybeNulltoString(o);
		if (!StringHelper.isNull(s) && s.indexOf(".") > -1) {
			s = s.substring(0, s.indexOf("."));
		}
		return StringHelper.isNull(s) ? null : new Integer(s);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("------ start ------");
		long s = System.currentTimeMillis();
		process();

		System.out.println("------ finish process time:"
				+ ((System.currentTimeMillis() - s) / 1000) + "s ------");
	}
}
