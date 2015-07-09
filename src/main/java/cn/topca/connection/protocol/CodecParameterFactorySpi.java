package cn.topca.connection.protocol;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 编码协议参数工厂服务引擎接口
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-25 22:49
 */
public abstract class CodecParameterFactorySpi {
    protected CodecParameterFactorySpi() {
    }

    /**
     * 根据规则构造编码协议参数实例
     * @param spec
     * @return
     */
    protected abstract CodecParameter generate(CodecParameterSpec spec);
}
