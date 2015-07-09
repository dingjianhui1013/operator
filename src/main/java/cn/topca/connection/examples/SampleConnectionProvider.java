package cn.topca.connection.examples;

import cn.topca.connection.ConnectionProvider;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 这是一个实现连接服务提供者的例子
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-15 09:59
 */
public class SampleConnectionProvider extends ConnectionProvider {
    // 连接服务提供者唯一标识
    public final static String PROVIDER_NAME = "Sample Connection Provider";
    // 连接服务提供者版本
    private final static double PROVIDER_VERSION = 0.1;
    // 连接服务提供者信息
    private final static String PROVIDER_INFO = "the example for ConnectionProvider";
    // 连接服务实例
    private static SampleConnectionProvider instance;

    /**
     * 这是一个构建连接服务提供者的例子
     */
    private SampleConnectionProvider() {
        super(PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_INFO);
        String engineClass = SampleConnection.class.getName();
        String algorithm = "SampleHttp";
        String[] algorithmAliases = new String[]{"Http", "HTTP"};
        addConnection(engineClass, algorithm, algorithmAliases);
    }

    /**
     * 获取连接服务提供者实例
     *
     * @return 连接服务提供者
     */
    public static SampleConnectionProvider getInstance() {
        if (instance == null)
            instance = new SampleConnectionProvider();
        return instance;
    }

}
