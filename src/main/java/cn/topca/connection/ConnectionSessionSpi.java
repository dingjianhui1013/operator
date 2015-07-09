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
 * @version 2014-04-26 07:30
 */
public abstract class ConnectionSessionSpi {
    protected ConnectionSessionSpi() {
    }

    /**
     * 设置会话超时时间
     *
     * @param timeout 超时时间
     * @throws IOException
     */
    protected abstract void engineSetSessionTimeout(int timeout) throws IOException;

    /**
     * 获取读取超时时间
     *
     * @return 超时时间
     */
    protected abstract int engineGetSessionTimeout();
    
    
    /**
     * 通过链接向服务发送请求消息，并接收响应消息
     *
     * @param message 请求消息
     * @return 响应消息
     * @throws IOException
     */
    protected ResponseMessage engineDoRequest(RequestMessage message, CodecProtocol protocol) throws IOException, ResponseException {
        protocol.encode(message, engineGetOutputStream());
        return (ResponseMessage) protocol.decode(engineGetInputStream());
    }

    /**
     * 获取用于写入数据的输出流
     *
     * @return OutputStream 输出流
     * @throws IOException
     * @see OutputStream
     */
    protected abstract OutputStream engineGetOutputStream() throws IOException;

    /**
     * 获取用于读取数据的输入流
     *
     * @return InputStream 输入流
     * @throws IOException
     * @see InputStream
     */
    protected abstract InputStream engineGetInputStream() throws IOException;

    /**
     * 关闭会话
     *
     * @param immediately {@code true}立即关闭
     * @throws IOException
     */
    public abstract void engineClose(boolean immediately) throws IOException;

    /**
     * 获取会话关闭标记
     *
     * @return {@code true}会话已关闭
     * @throws IOException
     */
    public abstract boolean engineIsClosed() throws IOException;
}
