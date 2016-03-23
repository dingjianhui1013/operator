package com.itrus.ca.modules.message.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.tempuri.ArrayOfString;
import org.tempuri.SendMessage;
import org.tempuri.Service;
import org.tempuri.ServiceSoap;

import sun.util.logging.resources.logging;


@org.springframework.stereotype.Service
public class SmsService {
	  private static final Log LOGGER = LogFactory.getLog(SmsService.class);
	  @Value("${sms.username}")
	   private String uid;

	    @Value("${sms.password}")
	   private String upass;
	  
	    @Value("${sms.pid}")
	    private int pid;
	   
	  
	    
	    @Value("${sms.endpoint}")
	    private String smsEndpoint;
	    
	   private Service service;
	   private ServiceSoap serviceSoap;
//	   @PostConstruct
	    private void init(){

	        
	            // sendServiceSoap = new
	            // FocusSendWebService(EmailProvider.class.getResource("FocussendWebservice.wsdl")).getFocusSendWebServiceSoap();
	        	try {
					service = new Service(new URL(smsEndpoint));
					serviceSoap= service.getServiceSoap();
	        	} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					LOGGER.error("Error while initialize serviceSoap", e);
				}
	        	
	    

	   }
	   
	
	
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public void setUpass(String upass) {
		this.upass = upass;
	}
	
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	
//	鍗曚釜鎵嬫満鍙风爜鍙戦��
	public String sendSms( String messid,String phone,String message) {
//		SendMessage senMessage= new SendMessage();
//		 senMessage.setUid(uid);
//		 senMessage.setUpass(upass);;
//		 senMessage.setMessid(messid);;
//		 senMessage.setPid(pid);
//		 senMessage.setPhone(phone);;
//		 senMessage.setMessage(message);
		try {
			service = new Service(new URL(smsEndpoint));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serviceSoap= service.getServiceSoap();
		  
		//String result = serviceSoap.sendMessage("05", "123456", messid, 4, phone, message);
	       return serviceSoap.sendMessage(uid, upass, messid, pid, phone, message);
	    }   
//	public boolean mutilSendMessage(ArrayOfString messageFile){
//		try {
//			service = new Service(new URL("http://221.237.182.5:8068/Service.asmx?WSDL"));
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		serviceSoap= service.getServiceSoap();
//		  
//		 String result = serviceSoap.mutilSendMessage("05", "123456", 4, messageFile);
//		 if (!result.equals("1")) {
//	            LOGGER.warn(String.format("sms sending failed: sms messageFile= %s and result = %s",messageFile, result));
//	            return false;
//	        }
//	        System.out.println("鍙戦�佹垚鍔�");
//	        return true;
//	}
}
