package cn.topca.connection.protocol;

import cn.topca.core.ServiceProvider;

import java.security.Provider;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * The protocol of the connection
 * encode, decode, encrypt, decrypt, etc. for the connection messages
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-20 22:15
 */
public abstract class CodecProtocolProvider extends ServiceProvider {

    /**
     * Constructs a provider with the specified name, version number,
     * information and service name.
     *
     * @param name    the provider name.
     * @param version the provider version number.
     * @param info    a description of the provider and its services.
     */
    protected CodecProtocolProvider(String name, double version, String info) {
        super(name, version, info);
    }

    /* 编码协议服务名称 */
    public final static String CODEC_PROTOCOL_SERVICE = CodecProtocol.SERVICE_TYPE;
    /* 编码协议参数工厂服务名称 */
    public final static String CODEC_PARAMETER_FACTORY_SERVICE = CodecParameterFactory.SERVICE_TYPE;

    /**
     * Add the service engine implementation
     *
     * @param engineClass the name of the class implementing this service engine
     * @param algorithm   the algorithm name
     * @param aliases     List of aliases or null if algorithm has no aliases
     */
    protected void addCodecProtocol(String engineClass, String algorithm, String[] aliases) {
        super.addServiceEngine(CODEC_PROTOCOL_SERVICE, engineClass, algorithm, aliases);
    }

    /**
     * Add the service engine implementation
     *
     * @param engineClass the name of the class implementing this service engine
     * @param algorithm   the algorithm name
     * @param aliases     List of aliases or null if algorithm has no aliases
     */
    protected void addCodecParameterFactory(String engineClass, String algorithm, String[] aliases) {
        super.addServiceEngine(CODEC_PARAMETER_FACTORY_SERVICE, engineClass, algorithm, aliases);
    }

    /**
     * Returns the current provider for the service
     *
     * @return the service provider
     */
    @Override
    protected Provider getProvider() {
        return super.getProvider();
    }
}
