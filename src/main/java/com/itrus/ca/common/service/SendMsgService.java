package com.itrus.ca.common.service;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itrus.ca.common.utils.MailSendUtil;
import com.itrus.ca.modules.profile.service.ConfigMsgService;
import com.itrus.ca.modules.sys.service.SysConfigService;
import com.itrus.ca.modules.sys.utils.ConfigConstant;

/**
 * 邮件、短信发送 具体看配置
 * 
 * @author ZhangJingtao
 * 
 */
@Repository
public class SendMsgService {

	Logger logger = Logger.getLogger(SendMsgService.class);

	@Autowired
	SysConfigService sysConfigService;
	
	@Autowired
	ConfigMsgService configMsgService;

	/**
	 * 发送消息
	 * 
	 * @param to
	 *            收件人
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param source
	 *            来源
	 * @param type 0 key库存 1发票库存
	 * @return
	 * @throws Exception
	 */
	public boolean sendMail(String to, String title, HashMap<String, String> params,Integer type) {
		HashMap<String, String> config = new HashMap<String, String>();
		config = sysConfigService.selectEmailConfig();

		String content = new String();
		try {
			if (validateEmail(to)) {
				content = configMsgService.gentContent(type, 0);
				for (String key : params.keySet()) {
					content = content.replace("${"+key+"}", params.get(key));
				}
				boolean ssl = config.get(ConfigConstant.IS_NEED_SSL).equals("true")? true:false;
				String smtp = config.get(ConfigConstant.EMAIL_URL);
				String user = config.get(ConfigConstant.EMAIL_USER_NAME);
				String pass = config.get(ConfigConstant.EMAIL_USER_PASS);
				String port = config.get(ConfigConstant.PORT);

				MailSendUtil util = new MailSendUtil();
				boolean result = util.sendMail(to, title, content, ssl, smtp,
						user, pass, port,null);
				logger.info("发送邮件成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean validateEmail(String email) {
		String check = new String("[a-zA-Z0-9-_]+@+[a-zA-Z0-9]+.+[a-zA-Z0-9]");
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email.trim());
		boolean isMatched = matcher.matches();
		if (isMatched) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern
					.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}
