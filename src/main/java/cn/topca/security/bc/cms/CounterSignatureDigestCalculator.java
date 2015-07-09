package cn.topca.security.bc.cms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 19:16
 */
class CounterSignatureDigestCalculator
        implements IntDigestCalculator {
    private final String alg;
    private final Provider provider;
    private final byte[] data;

    CounterSignatureDigestCalculator(String alg, Provider provider, byte[] data) {
        this.alg = alg;
        this.provider = provider;
        this.data = data;
    }

    public byte[] getDigest()
            throws NoSuchAlgorithmException {
        MessageDigest digest = CMSSignedHelper.INSTANCE.getDigestInstance(alg, provider);

        return digest.digest(data);
    }
}