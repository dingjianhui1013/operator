package cn.topca.security.bc.cms;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cms.CMSException;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 20:13
 */
interface CMSSecureReadable
{
    AlgorithmIdentifier getAlgorithm();
    Object getCryptoObject();
    CMSReadable getReadable(SecretKey key, Provider provider)
            throws CMSException;
    InputStream getInputStream()
            throws IOException, CMSException;
}