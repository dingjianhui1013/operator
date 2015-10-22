package com.itrus.ca.common.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientUtil {
	private static MultiThreadedHttpConnectionManager conmgr = null;
	private static HttpClient client = null;

	static {
		conmgr = new MultiThreadedHttpConnectionManager();
		conmgr.getParams().setDefaultMaxConnectionsPerHost(200);
		conmgr.getParams().setMaxTotalConnections(350);
		client = new HttpClient(conmgr);
	}

	public static HttpClient getClient() {
		return client;
	}

	public static String post(String url, Map<String, String> params)
			throws HttpException, IOException {
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		if (params != null) {
			Iterator<Entry<String, String>> p = params.entrySet().iterator();
			Entry<String, String> e;
			while (p.hasNext()) {
				e = p.next();
				postMethod.addParameter(e.getKey(), e.getValue());
			}
		}
		String result = "";

		int status = client.executeMethod(postMethod);
		result = postMethod.getResponseBodyAsString();

		postMethod.releaseConnection();

		return result;
	}

	public static String postContent(String url, String xmlInfo,
			boolean hasReturnContent) throws IllegalAccessException {
		PostMethod postMethod = new PostMethod(url);
		String result = "";
		if (xmlInfo != null) {
			try {
				postMethod.getParams().setParameter(
						HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
				postMethod.setRequestEntity(new StringRequestEntity(xmlInfo,
						"application/x-www-form-urlencoded", "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			postMethod.setRequestBody(xmlInfo);// 这里添加xml字符串

		}
		try {
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				if (!hasReturnContent) {
					result = postMethod.getResponseBodyAsString();
				} else {
					BufferedInputStream bis = new BufferedInputStream(
							postMethod.getResponseBodyAsStream());
					byte[] bytes = new byte[1024];
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int count = 0;
					while ((count = bis.read(bytes)) != -1) {
						bos.write(bytes, 0, count);
					}
					byte[] strByte = bos.toByteArray();
					result = new String(strByte, 0, strByte.length, "utf-8");
					bos.close();
					bis.close();
				}

			} else {
				System.out.println(url + ",POST," + status + ","
						+ postMethod.getStatusLine().getReasonPhrase());
			}
		} catch (IOException e) {
			result = "";
			System.out.println(url + ",POST " + e);
			throw new IllegalAccessException("http request exception : "
					+ StringHelper.getStackInfo(e));
		} finally {
			postMethod.releaseConnection();
		}
		return result;
	}

	public static String get(String url, boolean hashReturnXml) {
		GetMethod getMethod = new GetMethod(url);
		String result = "";
		try {
			int status = client.executeMethod(getMethod);
			if (status == HttpStatus.SC_OK) {
				if (!hashReturnXml) {
					result = getMethod.getResponseBodyAsString();
				} else {
					BufferedInputStream bis = new BufferedInputStream(
							getMethod.getResponseBodyAsStream());
					byte[] bytes = new byte[1024];
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int count = 0;
					while ((count = bis.read(bytes)) != -1) {
						bos.write(bytes, 0, count);
					}
					byte[] strByte = bos.toByteArray();
					result = new String(strByte, 0, strByte.length, "utf-8");
					bos.close();
					bis.close();
				}

			} else {
				System.out.println(url + ",GET," + status + ","
						+ getMethod.getStatusLine().getReasonPhrase());
			}
		} catch (HttpException e) {
			System.out.println(url + ",GET " + e);
		} catch (IOException e) {
			System.out.println(url + ",GET " + e);
		} finally {
			getMethod.releaseConnection();
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("param1", "按单位组织机构代码(含企业和机构证书)查单位证书");
		params.put("param2", "123456789");
		
		System.out.println(post(
						"http://ca.scca.com.cn/sccaDataQuery/GetMultiNumberFromSCCADBAction.action",
						params
						));

	}

}
