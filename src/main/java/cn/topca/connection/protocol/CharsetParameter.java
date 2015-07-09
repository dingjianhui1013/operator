package cn.topca.connection.protocol;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * <p>The charset encoding parameter of connection protocol</p>
 * <p>通信协议字符集编码参数接口</p>
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-21 10:42
 */
public interface CharsetParameter extends CodecParameter {

    /**
     * 返回字符集编码名称
     *
     * @return 字符集编码名称
     */
    public String getCharsetEncoding();

}
