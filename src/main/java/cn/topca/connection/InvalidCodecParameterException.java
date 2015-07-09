package cn.topca.connection;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 当前协议无法识别协议参数或传入的协议参数不能满足协议要求时使用该异常
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-20 21:21
 */
public class InvalidCodecParameterException extends Exception {
    /**
     * 当前协议无法识别协议参数或传入的协议参数不能满足协议要求时使用该异常
     *
     * @param message 错误提示信息
     */
    public InvalidCodecParameterException(String message) {
        super(message);
    }

    /**
     * 当前协议无法识别协议参数或传入的协议参数不能满足协议要求时使用该异常
     *
     * @param message 错误提示信息
     * @param cause   触发该异常的异常
     * @since 1.4
     */
    public InvalidCodecParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
