package cn.topca.connection.protocol;

import cn.topca.core.AbstractService;
import cn.topca.core.NoSuchProvider;
import cn.topca.core.NoSuchServiceAlgorithm;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 编码协议参数生产工厂
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-25 22:48
 */
public class CodecParameterFactory extends AbstractService {
    // 服务类型名称
    public static String SERVICE_TYPE = CodecParameterFactory.class.getSimpleName();
    // 服务引擎
    private final CodecParameterFactorySpi spi;

    private CodecParameterFactory(ServiceEngine engine) {
        super(engine);
        this.spi = (CodecParameterFactorySpi) getSpi();
    }

    /**
     * 根据指定的规则生产{@link CodecParameter}
     *
     * @param spec
     * @return
     */
    public CodecParameter generate(CodecParameterSpec spec) {
        return spi.generate(spec);
    }

    /**
     * Returns a CodecParameterFactory object that implements the specified algorithm.
     *
     * @param algorithm the standard name of the algorithm requested.
     * @return the new CodecParameterFactory object.
     * @throws NoSuchServiceAlgorithm if no Provider supports a
     *                                implementation for the
     *                                specified algorithm.
     * @see CodecProtocolProvider
     */
    public static CodecParameterFactory getInstance(String algorithm)
            throws NoSuchServiceAlgorithm {
        return new CodecParameterFactory(getServiceEngine(SERVICE_TYPE, algorithm));
    }

    /**
     * Returns a CodecParameterFactory object that implements the specified algorithm.
     * <p/>
     * <p> A new CodecParameterFactory object encapsulating the
     * CodecParameterFactorySpi implementation from the specified provider
     * is returned.  The specified provider must be registered
     * in the provider list.
     * <p/>
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link java.security.Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the algorithm requested.
     * @param provider  the name of the provider.
     * @return the new CodecParameterFactory object.
     * @throws NoSuchServiceAlgorithm       if a CodecParameterFactorySpi implementation
     *                                      for the specified algorithm is not
     *                                      available from the specified provider.
     * @throws cn.topca.core.NoSuchProvider if the specified provider is not
     *                                      registered in the provider list.
     * @throws IllegalArgumentException     if the provider name is null
     *                                      or empty.
     * @see CodecProtocolProvider
     */
    public static CodecParameterFactory getInstance(String algorithm, String provider)
            throws NoSuchProvider, NoSuchServiceAlgorithm {
        return new CodecParameterFactory(getServiceEngine(SERVICE_TYPE, algorithm, provider));
    }

    /**
     * 使用指定服务提供者实例化指定算法的服务实例
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link java.security.Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the algorithm requested.
     * @param provider  the provider.
     * @return the new CodecParameterFactory object.
     * @throws NoSuchServiceAlgorithm   if a CodecParameterFactorySpi implementation
     *                                  for the specified algorithm is not
     *                                  available from the specified provider.
     * @throws IllegalArgumentException if the provider is null
     *                                  or empty.
     * @see CodecProtocolProvider
     */
    public static CodecParameterFactory getInstance(String algorithm, CodecProtocolProvider provider)
            throws NoSuchServiceAlgorithm {
        return new CodecParameterFactory(getServiceEngine(SERVICE_TYPE, algorithm, provider));
    }
}
