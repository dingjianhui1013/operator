package com.itrus.ca.common.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
//	public boolean sendMail(String to,String title,String message,boolean ssl,String smtp,String user,String pass,String port,String ccAddress){
//		try {
//			if (ssl) {
//				MailSend mailSend = new MailSend();
//				mailSend.setAuth(user, pass);
//				mailSend.setServer(smtp);
//				String server = new String(smtp);
//				String[] ss = server.split("\\.");
//				String fromUser = user+"@"+ss[ss.length-2]+"."+ss[ss.length-1];
//				mailSend.setSSLSecurity(port);
//				mailSend.setForm(fromUser);
//				return mailSend.sendMail(to, ccAddress, title, message);
//			}else {
//				  MailSenderInfo mailInfo = new MailSenderInfo();
//				  String server = new String(smtp);
//				  String[] ss = server.split("\\.");
//				  String fromUser = user+"@"+ss[ss.length-2]+"."+ss[ss.length-1];
//				  mailInfo.setMailServerHost(smtp); 
//				  mailInfo.setMailServerPort(port); 
//				  mailInfo.setValidate(true); 
//				  mailInfo.setUserName(user); 
//				  mailInfo.setPassword(pass);//您的邮箱密码 
//				  mailInfo.setFromAddress(fromUser); 
//				  mailInfo.setToAddress(to); 
//				  mailInfo.setSubject(title); 
//				  mailInfo.setContent(message);
//				  SimpleMailSender sms = new SimpleMailSender();
//			      return sms.sendTextMail(mailInfo,ccAddress);//发送文体格式 
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//	/**
//	 * 测试邮件服务器有效
//	 * @param ssl 是否用ssl
//	 * @param smtp smtp服务器
//	 * @param user 用户名
//	 * @param pass 密码
//	 * @param port 端口
//	 * @return
//	 */
//	public boolean testMailHostIsValid(boolean ssl,String smtp,String user,String pass,String port){
//		String server = new String(smtp);
//		String[] ss = server.split("\\.");
//		String fromUser = user+"@"+ss[ss.length-2]+"."+ss[ss.length-1];
//		return sendMail(fromUser, "测试","测试", ssl, smtp, user, pass, port,null);
//	}
	
	private MimeMessage mimeMsg; //MIME邮件对象   
    private Session session; //邮件会话对象   
    private Properties props; //系统属性   
    private boolean needAuth = false; //smtp是否需要认证   
    //smtp认证用户名和密码   
    private String username;   
    private String password;   
    private Multipart mp; //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象   
       
    /** 
     * Constructor 
     * @param smtp 邮件发送服务器 
     */  
    public MailSendUtil(String smtp){   
        setSmtpHost(smtp);   
        createMimeMessage();   
    }  
    public MailSendUtil(){   
    } 
  
    /** 
     * 设置邮件发送服务器 
     * @param hostName String  
     */  
    public void setSmtpHost(String hostName) {   
        System.out.println("设置系统属性：mail.smtp.host = "+hostName);   
        if(props == null)  
            props = System.getProperties(); //获得系统属性对象    
        props.put("mail.smtp.host",hostName); //设置SMTP主机   
    }   
  
  
    /** 
     * 创建MIME邮件对象   
     * @return 
     */  
    public boolean createMimeMessage()   
    {   
        try {   
            System.out.println("准备获取邮件会话对象！");   
            session = Session.getDefaultInstance(props,null); //获得邮件会话对象   
        }   
        catch(Exception e){   
            System.err.println("获取邮件会话对象时发生错误！"+e);   
            return false;   
        }   
        System.out.println("准备创建MIME邮件对象！");   
        try {   
            mimeMsg = new MimeMessage(session); //创建MIME邮件对象   
            mp = new MimeMultipart();   
            return true;   
        } catch(Exception e){   
            System.err.println("创建MIME邮件对象失败！"+e);   
            return false;   
        }   
    }     
      
    /** 
     * 设置SMTP是否需要验证 
     * @param need 
     */  
    public void setNeedAuth(boolean need) {   
        System.out.println("设置smtp身份认证：mail.smtp.auth = "+need);   
        if(props == null) props = System.getProperties();   
        if(need){   
            props.put("mail.smtp.auth","true");   
        }else{   
            props.put("mail.smtp.auth","false");   
        }   
    }   
  
    /** 
     * 设置用户名和密码 
     * @param name 
     * @param pass 
     */  
    public void setNamePass(String name,String pass) {   
        username = name;   
        password = pass;   
    }   
  
    /** 
     * 设置邮件主题 
     * @param mailSubject 
     * @return 
     */  
    public boolean setSubject(String mailSubject) {   
        System.out.println("设置邮件主题！");   
        try{   
            mimeMsg.setSubject(mailSubject);   
            return true;   
        }   
        catch(Exception e) {   
            System.err.println("设置邮件主题发生错误！");   
            return false;   
        }   
    }  
      
    /**  
     * 设置邮件正文 
     * @param mailBody String  
     */   
    public boolean setBody(String mailBody) {   
        try{   
            BodyPart bp = new MimeBodyPart();   
            bp.setContent(""+mailBody,"text/html;charset=GBK");   
            mp.addBodyPart(bp);   
            return true;   
        } catch(Exception e){   
        System.err.println("设置邮件正文时发生错误！"+e);   
        return false;   
        }   
    }   
    /**  
     * 添加附件 
     * @param filename String  
     */   
    public boolean addFileAffix(String filename) {   
      
//        System.out.println("增加邮件附件："+filename);   
        try{   
            BodyPart bp = new MimeBodyPart();   
            FileDataSource fileds = new FileDataSource(filename);   
            bp.setDataHandler(new DataHandler(fileds));   
            bp.setFileName(fileds.getName());   
            mp.addBodyPart(bp);   
            return true;   
        } catch(Exception e){   
            System.err.println("增加邮件附件："+filename+"发生错误！"+e);   
            return false;   
        }   
    }   
      
    /**  
     * 设置发信人 
     * @param from String  
     */   
    public boolean setFrom(String from) {   
        System.out.println("设置发信人！");   
        try{   
            mimeMsg.setFrom(new InternetAddress(from)); //设置发信人   
            return true;   
        } catch(Exception e) {   
            return false;   
        }   
    }   
    /**  
     * 设置收信人 
     * @param to String  
     */   
    public boolean setTo(String to){   
        if(to == null)return false;   
        try{   
            mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));   
            return true;   
        } catch(Exception e) {   
            return false;   
        }     
    }   
      
    /**  
     * 设置抄送人 
     * @param copyto String   
     */   
    public boolean setCopyTo(String copyto)   
    {   
        if(copyto == null)return false;   
        try{   
        //mimeMsg.setRecipients(Message.RecipientType.CC,(Address[])InternetAddress.parse(copyto));   
        return true;   
        }   
        catch(Exception e)   
        { return false; }   
    }   
      
    /**  
     * 发送邮件 
     */   
    public boolean sendOut()   
    {   
        try{   
            mimeMsg.setContent(mp);   
            mimeMsg.saveChanges();   
            System.out.println("正在发送邮件....");   
              
            Session mailSession = Session.getInstance(props,null);   
            Transport transport = mailSession.getTransport("smtp");   
            String aaaString = props.get("mail.smtp.host").toString();
            
//            System.out.println(username);
//            System.out.println(password);
            transport.connect(aaaString,username,password);   
            
            transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));   
            //transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.CC));   
            //transport.send(mimeMsg);   
              
            System.out.println("发送邮件成功！");   
            transport.close();   
              
            return true;   
        } catch(Exception e) {   
            System.err.println("邮件发送失败！"+e);   
            return false;   
        }   
    }   
  
    /** 
     * 调用sendOut方法完成邮件发送 
     * @param smtp 
     * @param from 
     * @param to 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @return boolean 
     */  
    public static boolean send(String smtp,String from,String to,String subject,String content,String username,String password) {  
    	MailSendUtil theMail = new MailSendUtil(smtp);  
        theMail.setNeedAuth(true); //需要验证  
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.setTo(to)) return false;  
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    }  
    
    
    
    public static void main(String[] args) {
		
    	send("smtp.163.com", "azmy_910427_05@163.com", "1272344708@qq.com", "测试哈哈哈", "12312313", "azmy_910427_05@163.com","azmy910427");

    	
	}
    
    
    
      
    /** 
     * 调用sendOut方法完成邮件发送,带抄送 
     * @param smtp 
     * @param from 
     * @param to 
     * @param copyto 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @return boolean 
     */  
    public static boolean sendAndCc(String smtp,String from,String to,String copyto,String subject,String content,String username,String password) {  
    	MailSendUtil theMail = new MailSendUtil(smtp);  
        theMail.setNeedAuth(true); //需要验证  
          
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.setTo(to)) return false;  
        if(!theMail.setCopyTo(copyto)) return false;  
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    }  
      
    /** 
     * 调用sendOut方法完成邮件发送,带附件 
     * @param smtp 
     * @param from 
     * @param to 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @param filename 附件路径 
     * @return 
     */  
    public static boolean send(String smtp,String from,String to,String subject,String content,String username,String password,String filename) {  
    	MailSendUtil theMail = new MailSendUtil(smtp);  
        theMail.setNeedAuth(true); //需要验证  
          
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.addFileAffix(filename)) return false;   
        if(!theMail.setTo(to)) return false;  
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    }  
      
    /** 
     * 调用sendOut方法完成邮件发送,带附件和抄送 
     * @param smtp 
     * @param from 
     * @param to 
     * @param copyto 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @param filename 
     * @return 
     */  
    public static boolean sendAndCc(String smtp,String from,String to,String copyto,String subject,String content,String username,String password,String filename) {  
    	MailSendUtil theMail = new MailSendUtil(smtp);  
        theMail.setNeedAuth(true); //需要验证  
          
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.addFileAffix(filename)) return false;   
        if(!theMail.setTo(to)) return false;  
        if(!theMail.setCopyTo(copyto)) return false;  
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    }  
}
