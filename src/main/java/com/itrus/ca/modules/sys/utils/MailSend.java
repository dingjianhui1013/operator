package com.itrus.ca.modules.sys.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Length;


public class MailSend {
	// 日志
	private static Logger log = Logger.getLogger(MailSend.class);
	// 保存参数
	private Properties props = new Properties();

	// 发送邮件服务器
	private String server;
	// 发送邮件地址
	private String from;

	// 认证信息
	private Authenticator auth = null;

	public void setServer(String server) {
		this.server = server;
		props.put("mail.smtp.host", server);
	}

	public void setForm(String from) {
		this.from = from;
	}

	public void setSSLSecurity(String port) {
		props.put("mail.smtp.socketFactory.port", port);
		// props.put("mail.smtp.socketFactory.class",
		// "javax.net.ssl.SSLSocketFactory");
		// props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.starttls.enable", "true");
	}

	// set security user name and password
	public void setAuth(String userName, String userPassword) {
		auth = new MailAuthenticator(userName, userPassword);
		props.put("mail.smtp.auth", "true");
	}
	/**
	 * 发送邮件
	 * @param toAddr 收件人
	 * @param ccAddr 抄送？
	 * @param subject 主题
	 * @param message 内容
	 * @return
	 */
	public boolean sendMail(String toAddr, String ccAddr, String subject,
			String message) {
		try {
			Session session = Session.getInstance(props, auth);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] tos = InternetAddress.parse(toAddr);
			msg.setRecipients(Message.RecipientType.TO, tos);
			if (ccAddr != null) {
				InternetAddress[] cc = InternetAddress.parse(ccAddr);
				msg.setRecipients(Message.RecipientType.CC, cc);
			}
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
			return true;
		} catch (AddressException e) {
			log.error(e.getMessage(), e);
		} catch (MessagingException e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}
	
	public static void main(String[] args) {
		MailSend mailSend = new MailSend();
		mailSend.setAuth("wang_xiaoxue", "22222222");
		mailSend.setServer("mail.itrus.com.cn");
		String server = new String("mail.itrus.com.cn");
		String[] ss = server.split("\\.");
		String fromUser = "wang_xiaoxue@"+ss[ss.length-3]+"."+ss[ss.length-2]+"."+ss[ss.length-1];
		mailSend.setSSLSecurity("25");
		mailSend.setForm(fromUser);
		mailSend.sendMail("wang_xiaoxue@itrus.com.cn", null, "测试ssl", "明天下午出去玩啊");
//		mailSend.set
	}
}
