package cn.topca.core;

import java.security.Provider;
import java.security.Security;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * ServiceProvider 服务提供者
 * <p/>
 * 在“JAVA Security API”之上扩展而来
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-15 15:15
 */
public abstract class ServiceProvider {
    private final Provider p;
    private transient boolean changed = false;

    /**
     * Constructs a provider with the specified name, version number,
     * and information.
     *
     * @param name    the provider name.
     * @param version the provider version number.
     * @param info    a description of the provider and its services.
     */
    protected ServiceProvider(String name, double version, String info) {
        p = new Delegate(name, version, info);
    }

    /**
     * Add the service engine implementation
     *
     * @param serviceName the name of the service
     * @param engineClass the name of the class implementing this service engine
     * @param algorithm   the algorithm name
     * @param aliases     List of aliases or null if algorithm has no aliases
     */
    protected void addServiceEngine(String serviceName, String engineClass, String algorithm, String[] aliases) {
        p.put(serviceName + "." + algorithm, engineClass);
        if (aliases != null)
            for (String alias : aliases)
                p.put("Alg.Alias." + serviceName + "." + alias, algorithm);
        changed = true;
    }

    /**
     * Register the provider to the next position available.
     *
     * @return the registered provider
     */
    public synchronized ServiceProvider register() {
        if (changed) {
            Security.removeProvider(p.getName());
        }
        if (Security.getProvider(p.getName()) == null) {
            Security.addProvider(p);
            changed = false;
        }
        return this;
    }

    /**
     * Returns the current provider for the service
     *
     * @return the service provider
     */
    protected Provider getProvider() {
        return p;
    }

    protected static class Delegate extends Provider {
        protected Delegate(String name, double version, String info) {
            super(name, version, info);
        }
    }
}
