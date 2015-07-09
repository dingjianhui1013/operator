package cn.topca.connection;

import cn.topca.core.ServiceProvider;

import java.security.Provider;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * ConnectionProvider 连接服务提供者
 * <p/>
 * 通过继承此抽象类实现连接服务提供者，每个连接服务提供者通过{@link ConnectionSpi}类
 * 实现不同连接类型。注册{@link #register}后可使用{@link Connection#getInstance}
 * 方法获取相应类型的连接服务。
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-10 15:54
 */
public abstract class ConnectionProvider extends ServiceProvider {

    /**
     * 构造连接服务提供者
     *
     * @param name    服务提供者唯一标识
     * @param version 服务提供者版本
     * @param info    服务提供者信息
     */
    protected ConnectionProvider(String name, double version, String info) {
        super(name, version, info);
    }

    /* 连接服务名称 */
    public final static String CONNECTION_SERVICE = Connection.SERVICE_TYPE;

    /**
     * Add the service engine implementation
     *
     * @param engineClass the name of the class implementing this service engine
     * @param algorithm   the algorithm name
     * @param aliases     List of aliases or null if algorithm has no aliases
     */
    protected void addConnection(String engineClass, String algorithm, String[] aliases) {
        addServiceEngine(CONNECTION_SERVICE, engineClass, algorithm, aliases);
    }

    /**
     * Returns the native service provider
     *
     * @return the native service provider
     */
    @Override
    protected Provider getProvider() {
        return super.getProvider();
    }
}
