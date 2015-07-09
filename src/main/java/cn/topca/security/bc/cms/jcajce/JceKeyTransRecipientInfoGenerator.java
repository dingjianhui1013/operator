package cn.topca.security.bc.cms.jcajce;

import cn.topca.security.bc.operator.jcajce.JceAsymmetricKeyWrapper;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.KeyTransRecipientInfoGenerator;
import org.bouncycastle.operator.OperatorCreationException;

import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-26 19:24
 */
public class JceKeyTransRecipientInfoGenerator
        extends KeyTransRecipientInfoGenerator {
    public JceKeyTransRecipientInfoGenerator(X509Certificate recipientCert)
            throws CertificateEncodingException {
        super(new JcaX509CertificateHolder(recipientCert).getIssuerAndSerialNumber(), new JceAsymmetricKeyWrapper(recipientCert.getPublicKey()));
    }

    public JceKeyTransRecipientInfoGenerator(byte[] subjectKeyIdentifier, PublicKey publicKey) {
        super(subjectKeyIdentifier, new JceAsymmetricKeyWrapper(publicKey));
    }

    public JceKeyTransRecipientInfoGenerator setProvider(String providerName)
            throws OperatorCreationException {
        ((JceAsymmetricKeyWrapper) this.wrapper).setProvider(providerName);
        return this;
    }

    public JceKeyTransRecipientInfoGenerator setProvider(Provider provider)
            throws OperatorCreationException {
        ((JceAsymmetricKeyWrapper) this.wrapper).setProvider(provider);
        return this;
    }
}
