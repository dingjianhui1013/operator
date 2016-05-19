package cn.topca.core;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 所有服务类的父类，实现了大部分可以复用的基础方法，如：
 * <li>根据名称获取服务提供者</li>
 * <li>根据服务名称和算法名称获取服务提供者</li>
 * <li>使用指定服务提供者实例化指定算法的服务引擎</li>
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-15 17:54
 */
public abstract class AbstractService {
    private final ServiceEngine engine;

    protected AbstractService(ServiceEngine engine) {
        this.engine = engine;
    }

    protected ServiceProviderInterface getSpi() {
        return engine.getSpi();
    }

    public double getVersion() {
        return engine.getVersion();
    }

    /**
     * 获取服务算法
     *
     * @return 服务算法
     */
    public String getAlgorithm() {
        return engine.getAlgorithm();
    }

    protected Provider getProvider() {
        return engine.getProvider();
    }

    /**
     * Returns the provider installed with the specified name.
     *
     * @param name the name of the provider to get.
     * @return the provider of the specified name.
     * @throws NoSuchProvider if the specified provider is not
     *                        registered in the service provider list.
     */
    private static ServiceProvider.Delegate getProvider(String name) throws NoSuchProvider {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("missing provider");
        }
        Provider p = Security.getProvider(name);
        if (p == null || !(p instanceof ServiceProvider.Delegate)) {
            throw new NoSuchProvider("no such provider: " + name);
        }
        return (ServiceProvider.Delegate) p;
    }

    /**
     * Returns the most preferred provider in the list of registered providers
     * that satisfy the specified service name and the specified algorithm.
     *
     * @param service   the name of the service.
     * @param algorithm the standard name of the algorithm requested.
     * @return the first provider in the list
     * @throws NoSuchAlgorithmException
     */
    /*private static Provider getFirstProvider(String service, String algorithm) throws NoSuchAlgorithmException {
        Provider[] providers = Security.getProviders(service + "." + algorithm);
        if (providers != null && providers.length > 0) {
            for (Provider p : providers) {
                //if (p instanceof ServiceProvider.Delegate)
                    return p;
            }
        }
        throw new NoSuchAlgorithmException(algorithm + " " + service + " not available");
    }*/
    
    private static ServiceProvider.Delegate getFirstProvider(String service, String algorithm) throws NoSuchAlgorithmException {
        Provider[] providers = Security.getProviders(service + "." + algorithm);
        if (providers != null && providers.length > 0) {
            for (Provider p : providers) {
                if (p instanceof ServiceProvider.Delegate)
                    return (ServiceProvider.Delegate) p;
            }
        }
        throw new NoSuchAlgorithmException(algorithm + " " + service + " not available");
    }

    /**
     * Returns a ServiceEngine of the specified service
     * algorithm.
     * <p/>
     * <p> A new ServiceEngine implementation from the specified provider
     * is returned.  The specified provider must be registered
     * in the service provider list.
     *
     * @param service   the name of the service.
     * @param algorithm the standard name of the algorithm requested.
     * @return the new ServiceEngine object.
     * @throws NoSuchServiceAlgorithm if a {@link ServiceProviderInterface} implementation
     *                                for the specified algorithm is not
     *                                available from the specified provider.
     * @see ServiceProviderInterface
     */
    protected static ServiceEngine getServiceEngine(String service, String algorithm) throws NoSuchServiceAlgorithm {
        try {
            return getServiceEngine(service, algorithm, getFirstProvider(service, algorithm));
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchServiceAlgorithm(e.getMessage(), e);
        }
    }

    /**
     * Returns a ServiceEngine of the specified service
     * algorithm.
     * <p/>
     * <p> A new ServiceEngine implementation from the specified provider
     * is returned.  The specified provider must be registered
     * in the service provider list.
     *
     * @param service   the name of the service.
     * @param algorithm the standard name of the algorithm requested.
     * @param provider  the service provider.
     * @return the new ServiceEngine object.
     * @throws NoSuchServiceAlgorithm   if a {@link ServiceProviderInterface} implementation
     *                                  for the specified algorithm is not
     *                                  available from the specified provider.
     * @throws NoSuchProvider           if the specified provider is not
     *                                  registered in the provider list.
     * @throws IllegalArgumentException if the service provider is null
     *                                  or empty.
     * @see ServiceProviderInterface
     */
    protected static ServiceEngine getServiceEngine(String service, String algorithm, String provider) throws NoSuchServiceAlgorithm, NoSuchProvider {
        try {
            return getServiceEngine(service, algorithm, getProvider(provider));
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchServiceAlgorithm(e.getMessage(), e);
        }

    }

    /**
     * Returns a ServiceEngine of the specified service
     * algorithm.
     * <p/>
     * <p> A new ServiceEngine implementation from the specified provider
     * is returned.  The specified provider must be registered
     * in the service provider list.
     *
     * @param service   the name of the service.
     * @param algorithm the standard name of the algorithm requested.
     * @param provider  the service provider.
     * @return the new ServiceEngine object.
     * @throws NoSuchServiceAlgorithm   if a {@link ServiceProviderInterface} implementation
     *                                  for the specified algorithm is not
     *                                  available from the specified provider.
     * @throws IllegalArgumentException if the service provider is null
     *                                  or empty.
     * @see ServiceProviderInterface
     */
    protected static ServiceEngine getServiceEngine(String service, String algorithm, ServiceProvider provider) throws NoSuchServiceAlgorithm {
        try {
            return getServiceEngine(service, algorithm, provider.getProvider());
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchServiceAlgorithm(e.getMessage(), e);
        }
    }

    /**
     * Returns a ServiceEngine object that implements the specified service
     * algorithm.
     * <p/>
     * <p> A new {@link ServiceProviderInterface} implementation from the specified provider
     * is returned.  The specified provider must be registered
     * in the service provider list.
     *
     * @param service   the name of the service.
     * @param algorithm the standard name of the algorithm requested.
     * @param provider  the service provider {@link java.security.Provider}.
     * @return the new ServiceEngine object.
     * @throws NoSuchAlgorithmException if a {@link ServiceProviderInterface} implementation
     *                                  for the specified algorithm is not
     *                                  available from the specified provider.
     * @throws IllegalArgumentException if the service provider is null
     *                                  or empty.
     * @see ServiceProviderInterface
     */
    private static ServiceEngine getServiceEngine(String service, String algorithm, Provider provider) throws NoSuchAlgorithmException {
        Provider.Service s = getProviderService(service, algorithm, provider);
        Object obj = s.newInstance(null);
        ServiceProviderInterface spi = (ServiceProviderInterface) obj;
        return new ServiceEngine(spi, provider.getVersion(), s.getAlgorithm(), provider);
    }

    private static Provider.Service getProviderService(String service, String algorithm, Provider provider) throws NoSuchAlgorithmException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        Provider.Service s = provider.getService(service, algorithm);
        if (s == null)
            throw new NoSuchAlgorithmException("no such " + algorithm + " for " + provider.getName());
        return s;
    }

    protected static class ServiceEngine {
        private final ServiceProviderInterface spi;
        private final double version;
        private final String algorithm;
        private final Provider provider;

        public ServiceEngine(ServiceProviderInterface spi, double version, String algorithm, Provider provider) {
            this.spi = spi;
            this.version = version;
            this.algorithm = algorithm;
            this.provider = provider;
        }

        public ServiceProviderInterface getSpi() {
            return spi;
        }

        public double getVersion() {
            return version;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public Provider getProvider() {
            return provider;
        }
    }
}
