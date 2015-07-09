package cn.topca.tca.client;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 目前CAClient仅支持ICA编码协议
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-03 13:35
 */
public class UnSupportedProtocolException extends RuntimeException {
    public UnSupportedProtocolException(String algorithm) {
        super(algorithm);
    }

    public UnSupportedProtocolException(String algorithm, Throwable cause) {
        super(algorithm, cause);
    }
}
