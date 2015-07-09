package cn.topca.connection;

import cn.topca.connection.protocol.CodecParameter;
import cn.topca.connection.protocol.CodecParameterFactory;
import cn.topca.connection.protocol.CodecProtocol;
import cn.topca.connection.protocol.StringCodecParameterSpec;
import cn.topca.core.AbstractService;
import cn.topca.core.NoSuchProvider;
import cn.topca.core.NoSuchServiceAlgorithm;

import java.io.IOException;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 连接服务类
 * 通过连接服务提供者向消费者提供链接服务的类
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-10 15:37
 */
public final class Connection extends AbstractService {
    // 服务类型名称
    public final static String SERVICE_TYPE = Connection.class.getSimpleName();
    // 服务引擎
    private final ConnectionSpi spi;
    // 编码协议
    private CodecProtocol protocol;

    protected Connection(ServiceEngine engine) {
        super(engine);
        this.spi = (ConnectionSpi) getSpi();
    }

    /**
     * 设置协议转换规则
     *
     * @param protocol  协议名称
     * @param parameter 协议参数
     * @throws cn.topca.core.NoSuchServiceAlgorithm
     */
    public void setCodecProtocol(String protocol, String parameter) throws NoSuchServiceAlgorithm {
        this.protocol = CodecProtocol.getInstance(protocol);
        CodecParameterFactory.getInstance(protocol).generate(new StringCodecParameterSpec(parameter));
    }

    /**
     * 设置协议转换规则
     *
     * @param protocol 连接协议
     * @throws InvalidCodecParameterException
     */
    public void setCodecProtocol(CodecProtocol protocol, CodecParameter parameter) throws InvalidCodecParameterException {
        this.protocol = protocol;
        protocol.setParameter(parameter);
    }

    /**
     * 连接指定URL
     *
     * @param url 目标URL
     * @throws IOException
     */
    public void connect(String url) throws IOException {
        spi.engineConnect(url);
    }

    /**
     * 连接指定URL
     *
     * @param url     目标URL
     * @param timeout 连接超时时间
     * @throws IOException
     */
    public void connect(String url, int timeout) throws IOException {
        spi.engineConnect(url, timeout);
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout 超时时间
     */
    public void setConnectTimeout(int timeout) throws IOException {
        spi.engineSetConnectTimeout(timeout);
    }

    /**
     * 获取连接超时时间
     *
     * @return 超时时间
     */
    public int getConnectTimeout() {
        return spi.engineGetConnectTimeout();
    }

    /**
     * 获取连接会话
     *
     * @return 连接会话
     * @throws IOException
     */
    public ConnectionSession getSession() throws IOException {
        return new ConnectionSession(spi.engineGetSession(), protocol);
    }

    /**
     * 关闭连接
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

    /**
     * Returns a Connection object that implements the specified algorithm.
     *
     * @param algorithm the standard name of the algorithm requested.
     * @return the new Connection object.
     * @throws NoSuchServiceAlgorithm if no Provider supports a
     *                                implementation for the
     *                                specified algorithm.
     * @see ConnectionProvider
     */
    public static Connection getInstance(String algorithm)
            throws NoSuchServiceAlgorithm {
        return new Connection(getServiceEngine(SERVICE_TYPE, algorithm));
    }

    /**
     * Returns a Connection object that implements the specified algorithm.
     * <p/>
     * <p> A new Connection object encapsulating the
     * ConnectionSpi implementation from the specified provider
     * is returned.  The specified provider must be registered
     * in the provider list.
     * <p/>
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link java.security.Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the algorithm requested.
     * @param provider  the name of the provider.
     * @return the new Connection object.
     * @throws NoSuchServiceAlgorithm       if a ConnectionSpi implementation
     *                                      for the specified algorithm is not
     *                                      available from the specified provider.
     * @throws cn.topca.core.NoSuchProvider if the specified provider is not
     *                                      registered in the provider list.
     * @throws IllegalArgumentException     if the provider name is null
     *                                      or empty.
     * @see ConnectionProvider
     */
    public static Connection getInstance(String algorithm, String provider)
            throws NoSuchProvider, NoSuchServiceAlgorithm {
        return new Connection(getServiceEngine(SERVICE_TYPE, algorithm, provider));
    }

    /**
     * 使用指定服务提供者实例化指定算法的服务实例
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link java.security.Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the algorithm requested.
     * @param provider  the provider.
     * @return the new Connection object.
     * @throws NoSuchServiceAlgorithm   if a ConnectionSpi implementation
     *                                  for the specified algorithm is not
     *                                  available from the specified provider.
     * @throws IllegalArgumentException if the provider is null
     *                                  or empty.
     * @see ConnectionProvider
     */
    public static Connection getInstance(String algorithm, ConnectionProvider provider)
            throws NoSuchServiceAlgorithm {
        return new Connection(getServiceEngine(SERVICE_TYPE, algorithm, provider));
    }
}
