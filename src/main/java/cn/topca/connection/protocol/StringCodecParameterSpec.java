package cn.topca.connection.protocol;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * String格式编码协议参数规则
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-25 22:52
 */
public class StringCodecParameterSpec implements CodecParameterSpec{
    private final String spec;

    public StringCodecParameterSpec(String spec) {
        this.spec = spec;
    }

    public String getString() {
        return spec;
    }
}
