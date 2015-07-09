package com.itrus.ca.modules.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.RequestProxyAuthentication;
import org.apache.http.client.protocol.RequestTargetAuthentication;
import org.apache.http.client.protocol.ResponseAuthCache;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.VersionInfo;
/**
 * 申请证书HttpClient
 * 继承后增加了 两个流和一个post，目的是使HttpClient能够被Api赋值后再提交post请求
 * @author ZhangJingtao
 *
 */
public class CAhttpClient extends AbstractHttpClient {
	
	private ByteArrayOutputStream outputStream;//提交的参数最后被保存到httpPost中的entity中
	private InputStream inputStream;//返回的证书流
	private HttpPost httpPost;//保存url和entity
	

	protected CAhttpClient(ClientConnectionManager conman, HttpParams params) {
		super(conman, params);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected HttpParams createHttpParams() {
		HttpParams params = new SyncBasicHttpParams();
		setDefaultHttpParams(params);
		return params;
	}

	@Override
	protected BasicHttpProcessor createHttpProcessor() {
		BasicHttpProcessor httpproc = new BasicHttpProcessor();
		httpproc.addInterceptor(new RequestDefaultHeaders());
		// Required protocol interceptors
		httpproc.addInterceptor(new RequestContent());
		httpproc.addInterceptor(new RequestTargetHost());
		// Recommended protocol interceptors
		httpproc.addInterceptor(new RequestClientConnControl());
		httpproc.addInterceptor(new RequestUserAgent());
		httpproc.addInterceptor(new RequestExpectContinue());
		// HTTP state management interceptors
		httpproc.addInterceptor(new RequestAddCookies());
		httpproc.addInterceptor(new ResponseProcessCookies());
		// HTTP authentication interceptors
		httpproc.addInterceptor(new RequestAuthCache());
		httpproc.addInterceptor(new ResponseAuthCache());
		httpproc.addInterceptor(new RequestTargetAuthentication());
		httpproc.addInterceptor(new RequestProxyAuthentication());
		return httpproc;
	}

	public static void setDefaultHttpParams(HttpParams params) {
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		// determine the release version from packaged version info
		final VersionInfo vi = VersionInfo.loadVersionInfo(
				"org.apache.http.client",
				DefaultHttpClient.class.getClassLoader());
		final String release = (vi != null) ? vi.getRelease()
				: VersionInfo.UNAVAILABLE;
		HttpProtocolParams.setUserAgent(params, "Apache-HttpClient/" + release
				+ " (java 1.5)");
		
	}

	public ByteArrayOutputStream getOutputStream() {
		if (this.outputStream==null) {
			return new ByteArrayOutputStream();
		}
		return outputStream;
	}

	public void setOutputStream(ByteArrayOutputStream outputStream) {
		this.httpPost.setEntity(new ByteArrayEntity(outputStream.toByteArray()));//甚至请求内容
		this.outputStream = outputStream;
	}

	public InputStream getInputStream() {
		if (this.inputStream==null) {
			return new ByteArrayInputStream("".getBytes());
		}
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public HttpPost getHttpPost() {
		return httpPost;
	}

	public void setHttpPost(HttpPost httpPost) {
		this.httpPost = httpPost;
	}

}
