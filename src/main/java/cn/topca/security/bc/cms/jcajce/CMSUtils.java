package cn.topca.security.bc.cms.jcajce;

import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.operator.GenericKey;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 22:25
 */
class CMSUtils {
    static TBSCertificateStructure getTBSCertificateStructure(
            X509Certificate cert)
            throws CertificateEncodingException {
        return TBSCertificateStructure.getInstance(cert.getTBSCertificate());
    }

    static Key getJceKey(GenericKey key) {
        if (key.getRepresentation() instanceof Key) {
            return (Key) key.getRepresentation();
        }

        if (key.getRepresentation() instanceof byte[]) {
            return new SecretKeySpec((byte[]) key.getRepresentation(), "ENC");
        }

        throw new IllegalArgumentException("unknown generic key type");
    }

    static IssuerAndSerialNumber getIssuerAndSerialNumber(X509Certificate cert)
            throws CertificateEncodingException {
        X509CertificateStructure certStruct = X509CertificateStructure.getInstance(cert.getEncoded());

        return new IssuerAndSerialNumber(certStruct.getIssuer(), certStruct.getSerialNumber());
    }
}