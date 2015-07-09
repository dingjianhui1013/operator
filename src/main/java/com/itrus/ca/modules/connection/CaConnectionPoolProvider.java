package com.itrus.ca.modules.connection;

import cn.topca.connection.ConnectionProvider;

/**
 * 定义了新的提供者 ConnectionPool
 * @author ZhangJingtao
 *
 */
public class CaConnectionPoolProvider extends ConnectionProvider {
    // 连接服务提供者唯一标识
    public final static String PROVIDER_NAME = "Connection Pool Provider";
    // 连接服务提供者版本
    private final static double PROVIDER_VERSION = 0.1;
    // 连接服务提供者信息
    private final static String PROVIDER_INFO = "Connection Pool for ConnectionProvider";
    // 连接服务实例
    private static CaConnectionPoolProvider instance;

    /**
     * 这是一个构建连接服务提供者的例子
     */
    private CaConnectionPoolProvider() {
        super(PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_INFO);
        String engineClass = CaConnection.class.getName();
        String algorithm = "ConnectionPool";
        String[] algorithmAliases = new String[]{"Http", "HTTP"};
        addConnection(engineClass, algorithm, algorithmAliases);
    }

    /**
     * 获取连接服务提供者实例
     *
     * @return 连接服务提供者
     */
    public static CaConnectionPoolProvider getInstance() {
        if (instance == null)
            instance = new CaConnectionPoolProvider();
        return instance;
    }

}
