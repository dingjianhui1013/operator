package com.itrus.ca.modules.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.User;

@Repository
public class ItrustProxyUtil {

	/**
	 * 模拟POST请求
	 * 
	 * @param client
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JSONException
	 */
	public String postIxin(HttpClient client, String url,
			HttpServletRequest request, List<ConfigApp> apps, User user,
			String fileUploadPath, String key)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException, JSONException {
		boolean isFileUpload = false;
		if (request instanceof MultipartHttpServletRequest) {
			isFileUpload = true;
		}

		HttpPost post = new HttpPost(url);
		String ss = "";
		// 创建表单参数列表
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		Map<Object, String[]> formValue = request.getParameterMap();
		// 填充表单
		for (Entry<Object, String[]> obj : formValue.entrySet()) {
			if(!obj.getKey().toString().equalsIgnoreCase("project")){
				qparams.add(new BasicNameValuePair(obj.getKey().toString(), obj
						.getValue()[0]));
			}
		}

		for (Entry<Object, String[]> obj : formValue.entrySet()) {
			if(obj.getKey().toString().equalsIgnoreCase("project")){
				for (int i = 0; i < obj.getValue().length; i++) {
					qparams.add(new BasicNameValuePair(obj.getKey().toString(),obj.getValue()[i]));
					ss =  this.getfation(apps, qparams, isFileUpload, request,
							fileUploadPath, client, post, user, key);
					for (int j = 0; j <qparams.size() ; j++) {
						if(qparams.get(j).getName().equalsIgnoreCase("project")){
							qparams.remove(j);
							break;
						}
					}
				}
			}
		}
		if(ss.equalsIgnoreCase("")){
			return this.getfation(apps, qparams, isFileUpload, request,
					fileUploadPath, client, post, user, key);
		}else{
			return ss;
		}
		
	}

	private String getfation(List<ConfigApp> apps, List<NameValuePair> qparams,
			boolean isFileUpload, HttpServletRequest request,
			String fileUploadPath, HttpClient client, HttpPost post, User user,
			String key) throws UnsupportedEncodingException, IOException,
			ClientProtocolException, JSONException {
		Map<Long, Map<String, Object>> projectMap = new HashMap<Long, Map<String, Object>>();
		for (ConfigApp app : apps) {
			Map<String, Object> project = new HashMap<String, Object>();
			project.put("id", app.getId());
			project.put("name", app.getAppName());
			project.put("supportCommon", app.getSupportCommon());
			projectMap.put(app.getId(), project);
		}
		StringBuffer html = new StringBuffer();

		String value = new JSONObject(projectMap).toString();

		qparams.add(new BasicNameValuePair("aopProjects", value));

		if (isFileUpload) {// 文件上传 特殊处理
			MultipartEntity reqEntity = new MultipartEntity();

			MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
			Iterator<String> fileNames = fileRequest.getFileNames();
			Map<String, MultipartFile> files = fileRequest.getFileMap();
			while (fileNames.hasNext()) {
				String name = fileNames.next();
				MultipartFile file = files.get(name);
				File uploadFile = new File(fileUploadPath
						+ "/"
						+ System.currentTimeMillis()
						+ "."
						+ file.getOriginalFilename()
								.substring(
										file.getOriginalFilename().lastIndexOf(
												".") + 1));
				file.transferTo(uploadFile);
				FileBody fileBody = new FileBody(uploadFile);
				reqEntity.addPart(name, fileBody);
			}
			for (NameValuePair nameValuePair : qparams) {
				StringBody sb = new StringBody(nameValuePair.getValue());
				reqEntity.addPart(nameValuePair.getName(), sb);
			}
			post.setEntity(reqEntity);
		} else {
			post.setEntity(new UrlEncodedFormEntity(qparams, "UTF-8"));
		}

		post.setHeader("02iadmin", user.getName());
		post.setHeader("User-Agent", request.getHeader("User-Agent"));
		post.setHeader("Accept", request.getHeader("Accept"));
		post.setHeader("Accept-Language", "zh-cn");
		post.setHeader("restSecHeader", key);

		HttpResponse response2 = client.execute(post);
		int statusCode = response2.getStatusLine().getStatusCode();
		System.out.println("statusCode:" + statusCode);
		if (statusCode == 200) {
			HttpEntity entity2 = response2.getEntity();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					entity2.getContent(), "UTF-8"));
			String buffer2;
			while ((buffer2 = reader2.readLine()) != null) {
				html.append(buffer2 + "\n");
			}
			return html.toString();
		} else {
			return "";
		}

	}

	class AopProject {
		private Long id;
		private String name;
		private Boolean supportCommon;

		public AopProject(Long id, String name, Boolean supportCommon) {
			this.id = id;
			this.name = name;
			this.supportCommon = supportCommon;
		}

		public Boolean getSupportCommon() {
			return supportCommon;
		}

		public void setSupportCommon(Boolean supportCommon) {
			this.supportCommon = supportCommon;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 读回流的时候用指定字符集
	 * 
	 * @param destURL
	 * @param chacter
	 * @return String
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static HttpResponse sendGet(HttpClient client, String destURL,
			String chacter, HttpServletRequest request, String key)
			throws MalformedURLException, IOException {
		HttpGet get = new HttpGet(destURL);
		System.out.println("=================");
		System.out.println(request.getHeader("User-Agent"));
		System.out.println(request.getHeader("Accept"));
		System.out.println(request.getHeader("Content-Type"));
		System.out.println("=================");
		get.setHeader("User-Agent", request.getHeader("User-Agent"));
		get.setHeader("Accept", request.getHeader("Accept"));
		get.setHeader("Accept-Encoding", request.getHeader("Accept-Encoding"));
		get.setHeader("Connection", request.getHeader("Connection"));
		get.setHeader("Accept-Language", "zh-cn");
		get.setHeader("restSecHeader", key);

		HttpResponse response = client.execute(get);
		return response;
	}

	public static String sendPost(HttpClient client, String destURL,
			Map<String, String> params, HttpServletRequest request,
			String secret) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(destURL);

		// 创建表单参数列表
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		Map<Long, Map<String, Object>> projectMap = new HashMap<Long, Map<String, Object>>();
		StringBuffer html = new StringBuffer();
		String value = new JSONObject(projectMap).toString();
		if (params != null) {
			for (String key : params.keySet()) {
				qparams.add(new BasicNameValuePair(key, params.get(key)));
			}
		}

		post.setEntity(new UrlEncodedFormEntity(qparams, "UTF-8"));
		post.setHeader("Accept", "text/html");
		post.setHeader("Accept-Language", "zh-cn");
		post.setHeader("restSecHeader", secret);

		HttpResponse response2 = client.execute(post);
		int statusCode = response2.getStatusLine().getStatusCode();
		System.out.println("statusCode:" + statusCode);
		if (statusCode == 200) {
			HttpEntity entity2 = response2.getEntity();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(
					entity2.getContent(), "UTF-8"));
			String buffer2;
			while ((buffer2 = reader2.readLine()) != null) {
				html.append(buffer2);
			}
			return html.toString();
		} else {
			return "";
		}
	}
}
