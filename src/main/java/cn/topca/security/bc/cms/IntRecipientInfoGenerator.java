package cn.topca.security.bc.cms;

import org.bouncycastle.asn1.cms.RecipientInfo;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.Provider;
import java.security.SecureRandom;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 14:06
 */
interface IntRecipientInfoGenerator {
    /**
     * Generate a RecipientInfo object for the given key.
     *
     * @param contentEncryptionKey the <code>SecretKey</code> to encrypt
     * @param random               a source of randomness
     * @param prov                 the default provider to use
     * @return a <code>RecipientInfo</code> object for the given key
     * @throws java.security.GeneralSecurityException
     */
    RecipientInfo generate(SecretKey contentEncryptionKey, SecureRandom random,
                           Provider prov) throws GeneralSecurityException;
}