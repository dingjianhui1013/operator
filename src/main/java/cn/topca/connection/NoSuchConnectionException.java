package cn.topca.connection;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <support_topca@itrus.com.cn>
 * <p/>
 * 当指定算法的连接服务不存在时抛出该异常
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-10 15:56
 */
public class NoSuchConnectionException extends Exception {
    public NoSuchConnectionException(String message) {
        super(message);
    }
}
