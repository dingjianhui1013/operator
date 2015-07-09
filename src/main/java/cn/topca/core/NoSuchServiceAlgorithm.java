package cn.topca.core;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 当指定服务算法不存在时抛出该异常
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-06 16:49
 */
public class NoSuchServiceAlgorithm extends GeneralFrameworkException {
    public NoSuchServiceAlgorithm(String message) {
        super(message);
    }

    public NoSuchServiceAlgorithm(String message, Throwable cause) {
        super(message, cause);
    }
}
