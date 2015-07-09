package com.itrus.ca.common.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.JCEMac.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


/**
 * 短信发送
 * 
 * @author ZhangJingtao
 * 
 */
@Repository
public class SendSMSService {
	
	Logger logger = Logger.getLogger(SendSMSService.class);

	/**
	 * 发送手机短信
	 * @param to   收件人
	 * @param title  标题
	 * @param message 内容
	 * @param source 来源
	 * @return
	 */
	public boolean sendSMS(String mobile, String content,Integer company) {
//		logger.info("发送短信");
//		String user = cacheCustomer.getSysConfig(ConfigConstant.SMS_USER_NAME1).getConfig();
//		String pass = cacheCustomer.getSysConfig(ConfigConstant.SMS_USER_PASS1).getConfig();
//		if (company==1) {//
//
//		}
//		if (company==0) {//北京创世漫道科技有限公司
//			try {
//				return sendSMSLINK(mobile, content,user,pass);
//			} catch (Exception e) {
//				return false;
//			}
//		}
//		
		return true;
	}
	
	
	
	public static boolean sendSMSLINK(String Mobile,String Content,String user,String pass) throws MalformedURLException, UnsupportedEncodingException {
		URL url = null;
		String CorpID=user;//账户名
		String Pwd=pass;//密码
		String send_content=URLEncoder.encode(Content.replaceAll("<br/>", " "), "GBK");//发送内容
		url = new URL("http://inolink.com/WS/BatchSend.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+Mobile+"&Content="+send_content);
		BufferedReader in = null;
		int inputLine = 0;
		try {
			System.out.println("开始发送短信手机号码为 ："+Mobile);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			System.out.println("网络异常,发送短信失败！");
			inputLine=-2;
			return false;
		}
		return true;
	}
	
	
	public static int sendSMSCS(String content,String mobile,String user,String pass){
//		String password = new .MD5Encrpytion(user+pass);
		return 1;
	}
	
	public String getSMSurl(Integer company){
		String url = new String();
		if (company==1) {//创世漫道
			url = new String("http://sdk2.entinfo.cn:8061/mdgxsend.ashx");
		}
		if (company==2) {
			url = new String("http://inolink.com/WS/BatchSend.aspx");
		}
		return url;
	}
	
	public String getSignUrl(Integer company,String sn,String pass){
		String url = new String();
		if (company==1) {//创世漫道
			url = new String("http://sdk.entinfo.cn:8060/webservice.asmx/Register?sn="+sn+"&pwd="+pass+"&province=%E5%8C%97%E4%BA%AC&city=%E5%8C%97%E4%BA%AC&trade=%E6%B5%8B%E8%AF%95&entname=%E6%B5%8B%E8%AF%95&linkman=%E6%B5%8B%E8%AF%95&phone=12345678&mobile=18610021021&email=610039879@qq.com&fax=xxxx&address=xxxxx&postcode=100001&sign=%E3%80%90%E5%A4%A9%E5%A8%81%E8%AF%9A%E4%BF%A1%E3%80%91");
			return url;
		}
		if (company==2) {
			url = new String();
			return url;
		}
		return url;
	}
	
	public static void main(String[] args) throws MalformedURLException, UnsupportedEncodingException {
		System.out.println("开始发送");
//		int i = sendSMSLINK("18610021021", "尊敬的ffff你好，证书已被管理员吊销", "", "tclkj02949", "852123");
//		System.out.println("发送完毕:"+i);
	}
}
