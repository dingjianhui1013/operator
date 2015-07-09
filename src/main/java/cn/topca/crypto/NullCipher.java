package cn.topca.crypto;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * Crypto代理，用于兼容未签名的JCE
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-26 21:14
 */
public class NullCipher extends Cipher {
    public NullCipher() {
        super(new NullCipherSpi(), null, null);
    }
}
