package com.itrus.ca.modules.connection;

import cn.topca.connection.ConnectionSessionSpi;
import cn.topca.connection.ConnectionSpi;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.CoreConnectionPNames;
/**
 * 
 * 使用HttpClient {@link CAhttpClient}作连接
 * @author ZhangJingtao
 * 
 */
public class CaConnection extends ConnectionSpi {
//    private HttpURLConnection conn;
	  private CAhttpClient client;
	  private HttpResponse response;
	  private HttpPost post;
	  private ByteArrayOutputStream out;
	  private ByteArrayInputStream in;
	  
    /**
     * 连接指定URL
     *
     * @param url 目标URL
     * @throws IOException
     */
    @Override
    public void engineConnect(String url) throws IOException {
    	this.client = HttpConnectionManager.getHttpClient();
    	client.getParams().setParameter("Content-Type","application/octet-stream");    	
    	this.post = new HttpPost(url);
    	client.setHttpPost(this.post);
    	client.setOutputStream(new ByteArrayOutputStream());
    	client.setInputStream(new ByteArrayInputStream(new String("").getBytes()));
    }
    
    /**
     * 连接指定URL
     *
     * @param url     目标URL
     * @param timeout 超时时间
     * @throws IOException
     */
    @Override
    public void engineConnect(String url, int timeout) throws IOException {
        engineConnect(url);
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout 超时时间
     */
    @Override
    public void engineSetConnectTimeout(int timeout) throws IOException {
//        conn.setConnectTimeout(timeout);
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    /**
     * 设置读取超时时间
     *
     * @param timeout 超时时间
     */
    @Override
    public void engineSetReadTimeout(int timeout) throws IOException {
//        conn.setReadTimeout(timeout);
        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
    }

    /**
     * 获取连接会话服务引擎
     *
     * @return InputStream 连接会话服务引擎
     * @throws IOException
     */
    @Override
    protected ConnectionSessionSpi engineGetSession() throws IOException {
        return new ConnectionPoolSession(client);
    }

    /**
     * 关闭连接
     *
     * @param immediately {@code true}立即关闭
     * @throws IOException
     */
    @Override
    public void engineClose(boolean immediately) throws IOException {
        if (!immediately)
        	sleep(ONE_SECOND);
        client.getConnectionManager().shutdown();
//        conn.getInputStream().close();
//        conn.getOutputStream().close();
    }

    private final static long ONE_SECOND = 1000;

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }

    /**
     * 获取连接超时时间
     *
     * @return 超时时间
     */
    @Override
    public int engineGetConnectTimeout() {
        return client.getParams().getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HttpConnectionManager.CONNECT_TIMEOUT);
    }


}
