package cn.topca.connection;

import cn.topca.connection.protocol.CodecProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 连接会话
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-25 22:28
 */
public final class ConnectionSession {
    // 服务引擎
    private final ConnectionSessionSpi spi;
    // 编码协议
    private CodecProtocol protocol;


    protected ConnectionSession(ConnectionSessionSpi spi, CodecProtocol protocol) {
        this.spi = spi;
        this.protocol = protocol;
    }

    /**
     * 设置会话超时时间
     *
     * @param timeout 超时时间
     */
    public void setSessionTimeout(int timeout) throws IOException {
        spi.engineSetSessionTimeout(timeout);
    }

    /**
     * 获取读取超时时间
     *
     * @return 超时时间
     */
    public int getSessionTimeout() {
        return spi.engineGetSessionTimeout();
    }

    /**
     * 通过链接向服务发送请求消息，并接收响应消息
     *
     * @param message 请求消息
     * @return 响应消息
     * @throws IOException
     */
    public ResponseMessage doRequest(RequestMessage message) throws IOException, ResponseException {
        return spi.engineDoRequest(message, protocol);
    }

    /**
     * 获取用于写入数据的输出流
     *
     * @return OutputStream 输出流
     * @throws IOException
     * @see OutputStream
     */
    public OutputStream getOutputStream() throws IOException {
        return spi.engineGetOutputStream();
    }

    /**
     * 获取用于读取数据的输入流
     *
     * @return InputStream 输入流
     * @throws IOException
     * @see InputStream
     */
    public InputStream getInputStream() throws IOException {
        return spi.engineGetInputStream();
    }

    /**
     * 关闭会话
     *
     * @param immediately {@code true}立即关闭
     * @throws IOException
     */
    public void close(boolean immediately) throws IOException {
        spi.engineClose(immediately);
    }

    /**
     * 获取编码协议
     *
     * @return 编码协议
     */
    public CodecProtocol getProtocol() {
        return this.protocol;
    }
}
