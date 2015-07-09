package cn.topca.connection.examples;

import cn.topca.connection.ConnectionSessionSpi;
import cn.topca.connection.ConnectionSpi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-15 10:03
 */
public class SampleConnection extends ConnectionSpi {
    private HttpURLConnection conn;

    /**
     * 连接指定URL
     *
     * @param url 目标URL
     * @throws IOException
     */
    @Override
    public void engineConnect(String url) throws IOException {
        this.conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/octet-stream");
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
        conn.setConnectTimeout(timeout);
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout 超时时间
     */
    @Override
    public void engineSetConnectTimeout(int timeout) throws IOException {
        conn.setConnectTimeout(timeout);
    }

    /**
     * 设置读取超时时间
     *
     * @param timeout 超时时间
     */
    @Override
    public void engineSetReadTimeout(int timeout) throws IOException {
        conn.setReadTimeout(timeout);
    }

    /**
     * 获取连接会话服务引擎
     *
     * @return InputStream 连接会话服务引擎
     * @throws IOException
     */
    @Override
    protected ConnectionSessionSpi engineGetSession() throws IOException {
        return new SampleConnectionSession(conn);
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
            while (conn.getInputStream().available() > 0) {
                sleep(ONE_SECOND);
            }
        conn.getInputStream().close();
        conn.getOutputStream().close();
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
        return conn.getConnectTimeout();
    }


}
