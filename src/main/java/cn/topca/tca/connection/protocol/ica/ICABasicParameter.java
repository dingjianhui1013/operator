package cn.topca.tca.connection.protocol.ica;

import cn.topca.connection.protocol.CharsetParameter;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * ICA通信协议参数接口
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-21 10:55
 */
public class ICABasicParameter implements CharsetParameter {
    private int version;
    private String charsetEncoding;

    /**
     * <p>基本ICA协议参数</p>
     *
     * @param version         协议版本（默认3）
     * @param charsetEncoding 协议字符集编码名称（默认GBK）
     */
    public ICABasicParameter(int version, String charsetEncoding) {
        if (version < 1)
            version = 3;
        this.version = version;

        if (charsetEncoding == null)
            charsetEncoding = "GBK";
        this.charsetEncoding = charsetEncoding;

    }

    /**
     * 使用指定字符集编码进行协议编解码
     *
     * @param charsetEncoding 协议字符集编码名称
     */
    public ICABasicParameter(String charsetEncoding) {
        this(-1, charsetEncoding);
    }

    /**
     * 默认ICA协议参数
     *
     * @see #ICABasicParameter(int, String)
     */
    public ICABasicParameter() {
        this(-1, null);
    }

    /**
     * 返回ICA协议版本
     * 默认版本为3
     *
     * @return ICA协议版本
     */
    public int getVersion() {
        return version;
    }

    /**
     * 返回协议传输使用的字符集编码名称。
     *
     * @return 字符集编码名称
     */
    public String getCharsetEncoding() {
        return charsetEncoding;
    }

}
