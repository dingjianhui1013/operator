package com.itrus.ca.modules.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Date;




import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * i信代理访问
 * 
 * @author ZhangJingtao
 * 
 */
@RequestMapping("${adminPath}/${ixin.project}")
@Controller
public class IxinController {

	@Autowired
	ItrustService itrustService;

	@Value(value = "${ixin.url}")
	String ixinURL;
	
	@Value(value = "${ixin.key}")
	String key;

	@Value(value = "${uploadFile.path}")
	String fileUploadPath;
	
	@Value(value = "${ixin.project}")
	String ixinProjectName;

	@Autowired
	OfficeService officeService;
	@Autowired
	ConfigAppService configAppService;
	Logger log = Logger.getLogger(IxinController.class);
	
	@ResponseBody
	@RequestMapping(value = "/getCrlContextJson")
	public String getCrlContext(HttpServletRequest request,
			HttpServletResponse response) throws JSONException{
		String url = ixinURL + "/crlcontext/listjson?"+new Date().getTime()+"=&page=1&size=100";
		HttpClient client = new DefaultHttpClient();
		HttpResponse response2 = null;
		try {
			response2 = new ItrustProxyUtil().sendGet(client, url,
					"", request,key);
			HttpEntity entity2 = response2.getEntity();
			BufferedReader reader2 = new BufferedReader(
					new InputStreamReader(entity2.getContent(), "UTF-8"));
			String buffer2;
			StringBuffer content = new StringBuffer();
			while ((buffer2 = reader2.readLine()) != null) {
				content.append(buffer2 + "\n");
			}
			return content.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/**")
	public String ixin(Model uiModel, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException,
			ClientProtocolException, IOException, JSONException {
		String url = request.getRequestURI() + "?";
		
		/*System.out.print(request.getParameterMap());
		
		Map<Object, String[]> formValue = request.getParameterMap();
		String project = "";
		for (Entry<Object, String[]> obj : formValue.entrySet()) {
			if(obj.getKey().toString().equalsIgnoreCase("project")){
				for (int i = 0; i < obj.getValue().length; i++) {
//					project += obj.getValue()[i];
					project+=configAppService.findByAppId(Long.parseLong(obj.getValue()[i])).getAppName()+" ";
				
				}
			}
		}*/
		
		if (request.getMethod().toUpperCase().equals("GET")) {
		
			
			Map<String, String[]> paramMap = request.getParameterMap();
			for (Entry<String, String[]> obj : paramMap.entrySet()) {
				url += obj.getKey();
				if (obj.getValue()[0] != null) {
					url += "=" + obj.getValue()[0].toString();
				}
				url += "&";
			}
			url = url.substring(0, url.length() - 1);
		}
		String contextPath = request.getContextPath();
		url = url.replace(contextPath, "").replace("/"+ixinProjectName, "");
		url = ixinURL + url;
	
		
		log.debug("url:" + url);
		
		HttpClient client = new DefaultHttpClient();
		List<ConfigApp> apps = new ArrayList<ConfigApp>();
		List<Office> offices = officeService.getOfficeByType(UserUtils.getUser(), 2);
		apps = itrustService.getUserAppsByOfficeId(offices);// UserUtils.getUser().getOffice().getId()
		String content = new String();
		if (request.getMethod().toUpperCase().equals("GET")) {
			HttpResponse response2 = new ItrustProxyUtil().sendGet(client, url,
					"", request,key);
			int statusCode = response2.getStatusLine().getStatusCode();
			System.out.println("statusCode:" + statusCode);
			StringBuffer html = new StringBuffer();
			if (statusCode == 200) {
				String responseContextType = response2.getEntity()
						.getContentType().getValue();
				
				System.out.print(responseContextType);
				
				
				if (!responseContextType.contains("html")
						&& !responseContextType.contains("xml")
						&& !responseContextType.contains("json")) {// 不包含这些图片or文件
					InputStream is = response2.getEntity().getContent();
					for (Header h : response2.getAllHeaders()) {
						if (h.getName().equals("Content-Disposition")) {
							response.setHeader("Content-disposition", h.getValue());
							break;
						}
					}
					ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
					int ch;
					while ((ch = is.read()) != -1) {
						bytestream.write(ch);
					}
					byte imgdata[] = bytestream.toByteArray();
					String len = new String(imgdata);
					
					response.setContentType(responseContextType);
					
//					response.setHeader("Content-Length", String.valueOf(len.length()*2));

					BufferedOutputStream bos = null;
					bos = new BufferedOutputStream(response.getOutputStream());
					bos.write(imgdata);
					bos.close();
					bytestream.close();
					return null;
				}
				HttpEntity entity2 = response2.getEntity();
				BufferedReader reader2 = new BufferedReader(
						new InputStreamReader(entity2.getContent(), "UTF-8"));
				String buffer2;
				while ((buffer2 = reader2.readLine()) != null) {
					html.append(buffer2 + "\n");
				}
			}
			content = html.toString();
		} else {
			content = new ItrustProxyUtil().postIxin(client, url, request,
					apps, UserUtils.getUser(), fileUploadPath,key);
		}
		try {
			JSONObject json = new JSONObject(content);
			com.itrus.ca.modules.sys.utils.JsonUtils.writeJson4AjaxSubmit(response,
					json.toString());
			return null;
		} catch (Exception e) {
			// TODO: handle exception
		}
		// new ItrustProxyUtil().postIxin(client, url, request,
		// apps,UserUtils.getUser());
		client.getConnectionManager().shutdown();
		// content);//回写内容
		
		
//		content=project;
		uiModel.addAttribute("content", content);
		return "/modules/temp/page";
	}

	public ItrustService getItrustService() {
		return itrustService;
	}

	public void setItrustService(ItrustService itrustService) {
		this.itrustService = itrustService;
	}
	
//	public static CloseableHttpClient createSSLClientDefault(){
//		try {
//		             SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//		                 //信任所有
//		                 public boolean isTrusted(X509Certificate[] chain,
//		                                 String authType) throws CertificateException {
//		                     return true;
//		                 }
//		             }).build();
//		             SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
//		             return HttpClients.custom().setSSLSocketFactory(sslsf).build();
//		         } catch (KeyManagementException e) {
//		             e.printStackTrace();
//		         } catch (NoSuchAlgorithmException e) {
//		             e.printStackTrace();
//		         } catch (KeyStoreException e) {
//		             e.printStackTrace();
//		         }
//		         return  HttpClients.createDefault();
//		}
}
