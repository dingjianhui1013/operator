package cn.topca.connection;

import cn.topca.core.ServiceProviderInterface;

import java.io.IOException;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 连接服务提供者引擎
 * <p/>
 * 连接服务引擎用于实现特定算法类型的连接。所有连接算法实现需要通过连接服务提供者（{@link ConnectionProvider}）注册使用。
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-10 15:46
 * @see Connection
 * @see ConnectionProvider
 */
public abstract class ConnectionSpi implements ServiceProviderInterface {
    protected ConnectionSpi() {
    }

    /**
     * 连接指定URL
     *
     * @param url 目标URL
     * @throws IOException
     */
    protected abstract void engineConnect(String url) throws IOException;

    /**
     * 连接指定URL
     *
     * @param url     目标URL
     * @param timeout 连接超时时间
     * @throws IOException
     */
    protected abstract void engineConnect(String url, int timeout) throws IOException;

    /**
     * 设置连接超时时间
     *
     * @param timeout 超时时间
     * @throws IOException
     */
    protected abstract void engineSetConnectTimeout(int timeout) throws IOException;

    /**
     * 获取连接超时时间
     *
     * @return 超时时间
     */
    protected abstract int engineGetConnectTimeout();

    /**
     * 设置读取超时时间
     *
     * @param timeout 超时时间
     */
    protected abstract void engineSetReadTimeout(int timeout) throws IOException;

    /**
     * 获取连接会话
     *
     * @return 连接会话
     * @throws IOException
     */
    protected abstract ConnectionSessionSpi engineGetSession() throws IOException;

    /**
     * 关闭连接
     *
     * @param immediately {@code true}立即关闭
     * @throws IOException
     */
    public abstract void engineClose(boolean immediately) throws IOException;

}
