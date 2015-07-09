package cn.topca.core;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 框架内异常继承此类型
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-06 16:53
 */
public class GeneralFrameworkException extends Exception {
    public GeneralFrameworkException() {
    }

    public GeneralFrameworkException(String message) {
        super(message);
    }

    public GeneralFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralFrameworkException(Throwable cause) {
        super(cause);
    }
}
