package cn.topca.tca.connection.protocol;

import cn.topca.connection.protocol.CodecProtocolProvider;
import cn.topca.tca.connection.protocol.ica.ICACodecProtocol;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * TopCA 通信协议提供者
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-15 11:42
 */
public class TCAProtocolProvider extends CodecProtocolProvider {
    public final static String PROVIDER_NAME = "TopCA";
    private final static double PROVIDER_VERSION = 0.1;
    private final static String PROVIDER_INFO = "The TopCA Connection Protocol Provider";
    private static TCAProtocolProvider instance;

    private TCAProtocolProvider() {
        super(PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_INFO);
        String engineClass = ICACodecProtocol.class.getName();
        String algorithm = "ICA";
        String[] aliases = new String[]{"Ica", "ica", "crs", "CRS"};
        addCodecProtocol(engineClass, algorithm, aliases);
    }

    /**
     * 获取通信协议服务提供者实例
     *
     * @return 通信协议服务提供者
     */
    public static TCAProtocolProvider getInstance() {
        if (instance == null)
            instance = new TCAProtocolProvider();
        return instance;
    }
}
