/**
 *2016年8月25日 下午6:31:15
 */
package com.itrus.ca.modules.task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.itrus.ca.common.utils.FileHelper;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.task.entity.BasicInfoScca;

/**
 * @author: liubin
 *
 */
public class ProcessTxt {

	private static String file = "/Volumes/Macintosh HD/dbfile/ca/sss.txt";

	private static String out = "/Volumes/Macintosh HD/dbfile/ca/sss.sql";

	private static void process() throws FileNotFoundException, IOException {
		String[] content = FileHelper.getLine(file);

		int flag = 0;
		for (String e : content) {

			String[] column = StringHelper.splitStr(e, "#");

			BasicInfoScca po = new BasicInfoScca();
			po.setId(getMaybeNulltoLong(column[1].replaceAll("\"", "")));
			po.setAppId(getMaybeNulltoLong(column[2].replaceAll("\"", "")));
			po.setProductId(getMaybeNulltoLong(column[3].replaceAll("\"", "")));
			po.setProductLabel(getMaybeNulltoInteger(column[4].replaceAll("\"",
					"")));
			po.setYear(getMaybeNulltoInteger(column[5].replaceAll("\"", "")));
			po.setCompanyName(getMaybeNulltoString(column[6].replaceAll("\"",
					"")));
			po.setCompanyType(getMaybeNulltoInteger(column[7].replaceAll("\"",
					"")));
			po.setOrganizationNumber(getMaybeNulltoString(column[8].replaceAll(
					"\"", "")));
			po.setOrgExpirationTime(getMaybeNulltoString(column[9].replaceAll(
					"\"", "")));
			po.setSelectLv(getMaybeNulltoInteger(column[10]
					.replaceAll("\"", "")));
			po.setComCertificateType(getMaybeNulltoInteger(column[11]
					.replaceAll("\"", "")));
			po.setComCertficateNumber(getMaybeNulltoString(column[12]
					.replaceAll("\"", "")));
			po.setComCertificateTime(getMaybeNulltoString(column[13]
					.replaceAll("\"", "")));
			po.setLegalName(getMaybeNulltoString(column[14]
					.replaceAll("\"", "")));
			po.setSProvince(getMaybeNulltoString(column[15]
					.replaceAll("\"", "")));
			po.setSCity(getMaybeNulltoString(column[16].replaceAll("\"", "")));
			po.setSCounty(getMaybeNulltoString(column[17].replaceAll("\"", "")));
			po.setAddress(getMaybeNulltoString(column[18].replaceAll("\"", "")));
			po.setCompanyMobile(getMaybeNulltoString(column[19].replaceAll(
					"\"", "")));
			po.setContactName(getMaybeNulltoString(column[20].replaceAll("\"",
					"")));
			po.setConCertType(getMaybeNulltoInteger(column[21].replaceAll("\"",
					"")));
			po.setConCertNumber(getMaybeNulltoString(column[22].replaceAll(
					"\"", "")));
			po.setContactEmail(getMaybeNulltoString(column[23].replaceAll("\"",
					"")));
			po.setContactPhone(getMaybeNulltoString(column[24].replaceAll("\"",
					"")));
			po.setContactTel(getMaybeNulltoString(column[25].replaceAll("\"",
					"")));
			po.setContactSex(getMaybeNulltoString(column[26].replaceAll("\"",
					"")));
			po.setName(getMaybeNulltoString(column[27].replaceAll("\"", "")));
			po.setIdCard(getMaybeNulltoString(column[28].replaceAll("\"", "")));
			po.setEmail(getMaybeNulltoString(column[29].replaceAll(
					"emailAddress=", "").replaceAll("\"", "")));
			po.setIssuerDn(getMaybeNulltoString(column[30].replaceAll("\"", "")));
			po.setSubjectDn(getMaybeNulltoString(column[31]
					.replaceAll("\"", "")));
			po.setNotafter(getMaybeNulltoString(column[32].replaceAll("\"", "")));
			po.setNotbefore(getMaybeNulltoString(column[33]
					.replaceAll("\"", "")));
			po.setSerialnumber(getMaybeNulltoString(column[34].replaceAll("\"",
					"")));
			po.setAgentId(getMaybeNulltoLong(column[35].replaceAll("\"", "")));

			po.setCertType(getMaybeNulltoString(column[37].replaceAll("\"", "")));
			po.setAppName(getMaybeNulltoString(column[38].replaceAll("\"", "")));
			po.setCertCounts(getMaybeNulltoString(column[39].replaceAll("\"",
					"")));
			po.setMultiCertSns(getMaybeNulltoInteger(column[40].replaceAll(
					"\"", "")));
			po.setKeyAndUsbSn(getMaybeNulltoString(column[41].replaceAll("\"",
					"")));
			po.setCertValidDays(getMaybeNulltoInteger(column[42].replaceAll(
					"\"", "")));
			po.setOpenTime(getMaybeNulltoString(column[54].replaceAll("\"", "")));
			po.setFirstCertSN(getMaybeNulltoString(column[53].replaceAll("\"",
					"")));
			po.setProcessTime(getMaybeNulltoString(column[55].replaceAll("\"",
					"")));
			po.setDealInfoType(getMaybeNulltoString(column[52].replaceAll("\"",
					"")));
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
			try {
				long t = StringHelper.getTime(res, "yyyy/MM/dd HH:mm:ss");

				res = "'" + StringHelper.getSystime("yyyy/MM/dd HH:mm:ss", t)
						+ "'";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
