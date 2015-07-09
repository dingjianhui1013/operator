package cn.topca.connection.examples;

import cn.topca.connection.ConnectionSessionSpi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 连接会话示例
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-26 08:03
 */
public class SampleConnectionSession extends ConnectionSessionSpi {
    private URLConnection connection;
    private boolean closed;

    protected SampleConnectionSession(URLConnection connection) throws IOException {
        if(connection==null)
            throw new IOException("please connect first");
        this.connection = connection;
    }

    /**
     * 设置会话超时时间
     *
     * @param timeout 超时时间
     * @throws java.io.IOException
     */
    @Override
    protected void engineSetSessionTimeout(int timeout) throws IOException {
        connection.setReadTimeout(timeout);
    }

    /**
     * 获取读取超时时间
     *
     * @return 超时时间
     */
    @Override
    protected int engineGetSessionTimeout() {
        return connection.getReadTimeout();
    }

    /**
     * 获取用于写入数据的输出流
     *
     * @return OutputStream 输出流
     * @throws java.io.IOException
     * @see java.io.OutputStream
     */
    @Override
    protected OutputStream engineGetOutputStream() throws IOException {
        if (closed)
            throw new IOException("Session closed");
        return connection.getOutputStream();
    }

    /**
     * 获取用于读取数据的输入流
     *
     * @return InputStream 输入流
     * @throws java.io.IOException
     * @see java.io.InputStream
     */
    @Override
    protected InputStream engineGetInputStream() throws IOException {
        if (closed)
            throw new IOException("Session closed");
        return connection.getInputStream();
    }

    /**
     * 关闭会话
     *
     * @param immediately {@code true}立即关闭
     * @throws java.io.IOException
     */
    @Override
    public void engineClose(boolean immediately) throws IOException {
        if (!immediately)
            while (connection.getInputStream().available() > 0) {
                sleep(ONE_SECOND);
            }
        connection.getInputStream().reset();
        closed = true;
    }

    private final static long ONE_SECOND = 1000;

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }

    /**
     * 获取会话关闭标记
     *
     * @return {@code true}会话已关闭
     * @throws java.io.IOException
     */
    @Override
    public boolean engineIsClosed() throws IOException {
        return closed;
    }
}
