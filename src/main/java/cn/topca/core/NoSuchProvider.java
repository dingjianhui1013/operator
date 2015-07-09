package cn.topca.core;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 当指定服务提供者不存在时抛出该异常
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-10 16:22
 */
public class NoSuchProvider extends GeneralFrameworkException {
    public NoSuchProvider(String message) {
        super(message);
    }

    public NoSuchProvider(String message, Throwable cause) {
        super(message, cause);
    }
}
