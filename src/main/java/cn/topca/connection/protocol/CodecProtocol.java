package cn.topca.connection.protocol;

import cn.topca.connection.InvalidCodecParameterException;
import cn.topca.connection.Message;
import cn.topca.core.AbstractService;
import cn.topca.core.NoSuchProvider;
import cn.topca.core.NoSuchServiceAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * 表示成协议，表示层协议一般用于应用消息的编码解码以及加密解密
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-15 10:50
 */
public class CodecProtocol extends AbstractService {

    // 服务类型名称
    public final static String SERVICE_TYPE = CodecProtocol.class.getSimpleName();
    // 服务引擎
    private final CodecProtocolSpi spi;

    private CodecProtocol(ServiceEngine engine) {
        super(engine);
        this.spi = (CodecProtocolSpi) engine.getSpi();
    }

    /**
     * 设置并应用协议参数
     *
     * @param parameter 协议对信息进行封装、解析过程中需要的参数信息
     * @throws cn.topca.connection.InvalidCodecParameterException 当前协议无法识别协议参数或传入的协议参数不能满足协议要求时抛出异常
     */
    public void setParameter(CodecParameter parameter) throws InvalidCodecParameterException {
        spi.engineSetParameter(parameter);
    }

    /**
     * 获取当前协议参数
     *
     * @return 协议参数
     */
    public CodecParameter getParameter() {
        return spi.engineGetParameter();
    }


    /**
     * 对消息进行编码
     *
     * @param message 消息对象
     * @param out     通信输出流
     * @throws java.io.IOException
     */
    public void encode(Message message, OutputStream out) throws IOException {
        spi.engineEncode(message, out);
    }

    /**
     * 对消息进行编码
     *
     * @param message 通信消息
     * @return 二进制编码
     * @throws IOException
     */
    public byte[] encode(Message message) throws IOException {
        return spi.engineEncode(message);
    }

    /**
     * 对消息进行解码
     *
     * @param in 通信输入流
     * @return 消息对象
     * @throws IOException
     */
    public Message decode(InputStream in) throws IOException {
        return spi.engineDecode(in);
    }

    /**
     * 对消息进行解码
     *
     * @param encoded 消息二进制编码
     * @return 消息对象
     * @throws IOException
     */
    public Message decode(byte[] encoded) throws IOException {
        return spi.engineDecode(encoded);
    }

    /**
     * Returns a Connection object that implements the specified algorithm.
     *
     * @param algorithm the standard name of the algorithm requested.
     * @return the new Connection object.
     * @throws NoSuchServiceAlgorithm if no Provider supports a
     *                                implementation for the
     *                                specified algorithm.
     * @see CodecProtocolProvider
     */
    public static CodecProtocol getInstance(String algorithm)
            throws NoSuchServiceAlgorithm {
        return new CodecProtocol(getServiceEngine(SERVICE_TYPE, algorithm));
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
     * @throws NoSuchServiceAlgorithm       if a ConnectionProtocolSpi implementation
     *                                      for the specified algorithm is not
     *                                      available from the specified provider.
     * @throws cn.topca.core.NoSuchProvider if the specified provider is not
     *                                      registered in the provider list.
     * @throws IllegalArgumentException     if the provider name is null
     *                                      or empty.
     * @see CodecProtocolProvider
     */
    public static CodecProtocol getInstance(String algorithm, String provider)
            throws NoSuchServiceAlgorithm, NoSuchProvider {
        return new CodecProtocol(getServiceEngine(SERVICE_TYPE, algorithm, provider));
    }

    /**
     * 使用指定服务提供者实例化指定算法的服务实例
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link java.security.Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the algorithm requested.
     * @param provider  the provider.
     * @return the new Connection object.
     * @throws NoSuchServiceAlgorithm   if a ConnectionProtocolSpi implementation
     *                                  for the specified algorithm is not
     *                                  available from the specified provider.
     * @throws IllegalArgumentException if the provider is null
     *                                  or empty.
     * @see CodecProtocolProvider
     */
    public static CodecProtocol getInstance(String algorithm, CodecProtocolProvider provider)
            throws NoSuchServiceAlgorithm {
        return new CodecProtocol(getServiceEngine(SERVICE_TYPE, algorithm, provider));
    }

}
