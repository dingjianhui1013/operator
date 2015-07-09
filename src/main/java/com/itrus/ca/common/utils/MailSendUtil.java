package com.itrus.ca.common.utils;

import com.itrus.ca.modules.sys.utils.MailSenderInfo;
import com.itrus.ca.modules.sys.utils.SimpleMailSender;


/**
 * 发送邮件util
 * @author ZhangJingtao
 *
 */
public class MailSendUtil {
	/**
	 * 发送邮件
	 * @param to 
	 * @param title
	 * @param message
	 * @param ssl
	 * @param smtp
	 * @param user
	 * @param pass
	 * @param port
	 * @return
	 */
	public boolean sendMail(String to,String title,String message,boolean ssl,String smtp,String user,String pass,String port,String ccAddress){
		try {
			if (ssl) {
				MailSend mailSend = new MailSend();
				mailSend.setAuth(user, pass);
				mailSend.setServer(smtp);
				String server = new String(smtp);
				String[] ss = server.split("\\.");
				String fromUser = user+"@"+ss[ss.length-2]+"."+ss[ss.length-1];
				mailSend.setSSLSecurity(port);
				mailSend.setForm(fromUser);
				return mailSend.sendMail(to, ccAddress, title, message);
			}else {
				  MailSenderInfo mailInfo = new MailSenderInfo();
				  String server = new String(smtp);
				  String[] ss = server.split("\\.");
				  String fromUser = user+"@"+ss[ss.length-2]+"."+ss[ss.length-1];
				  mailInfo.setMailServerHost(smtp); 
				  mailInfo.setMailServerPort(port); 
				  mailInfo.setValidate(true); 
				  mailInfo.setUserName(user); 
				  mailInfo.setPassword(pass);//您的邮箱密码 
				  mailInfo.setFromAddress(fromUser); 
				  mailInfo.setToAddress(to); 
				  mailInfo.setSubject(title); 
				  mailInfo.setContent(message);
				  SimpleMailSender sms = new SimpleMailSender();
			      return sms.sendTextMail(mailInfo,ccAddress);//发送文体格式 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 测试邮件服务器有效
	 * @param ssl 是否用ssl
	 * @param smtp smtp服务器
	 * @param user 用户名
	 * @param pass 密码
	 * @param port 端口
	 * @return
	 */
	public boolean testMailHostIsValid(boolean ssl,String smtp,String user,String pass,String port){
		String server = new String(smtp);
		String[] ss = server.split("\\.");
		String fromUser = user+"@"+ss[ss.length-2]+"."+ss[ss.length-1];
		return sendMail(fromUser, "测试","测试", ssl, smtp, user, pass, port,null);
	}
}
