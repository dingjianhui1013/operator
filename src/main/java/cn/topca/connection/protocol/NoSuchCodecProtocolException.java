package cn.topca.connection.protocol;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <support_topca@itrus.com.cn>
 * <p/>
 * 当指定的协议未在系统中注册时抛出此异常
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-15 10:53
 */
public class NoSuchCodecProtocolException extends Exception {
    /**
     * 指定的协议在系统中未注册
     *
     * @param message 异常信息（一般为引发该异常的协议名称）
     */
    public NoSuchCodecProtocolException(String message) {
        super(message);
    }
}
