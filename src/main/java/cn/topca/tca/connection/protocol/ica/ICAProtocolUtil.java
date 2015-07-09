package cn.topca.tca.connection.protocol.ica;

import cn.topca.security.bc.cms.CMSOperatorUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cert.CertException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Store;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * ICA协议相关工具集
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-26 19:04
 */
public class ICAProtocolUtil {
    /**
     * 加密并签名
     *
     * @param origin              数据包原文
     * @param recipientCert       加密证书
     * @param encryptionAlgorithm 加密算法
     * @param nativeKey           签名私钥
     * @param nativeCert          签名证书
     * @param nativeCertChain     签名证书链
     * @param provider            签名服务提供者
     * @return 已签名密文数据包
     */
    public static byte[] encAndSignToP7(byte[] origin, Certificate recipientCert, ASN1ObjectIdentifier encryptionAlgorithm, PrivateKey nativeKey, Certificate nativeCert, Certificate[] nativeCertChain, Provider provider) throws CMSException, CertificateEncodingException, IOException, OperatorCreationException {
        byte[] data = null;
        byte[] encData = CMSOperatorUtils.generateEnvelopedData(origin, recipientCert, encryptionAlgorithm).getEncoded();
        if (encData != null && encData.length > 0) {
            data = CMSOperatorUtils.generateSignedData(encData, nativeKey, nativeCert, provider).getEncoded();
        }
        return data;
    }

    /**
     * 验签并解密
     *
     * @param signedData 已签名密文数据流
     * @param trustCerts 验签可信证书链
     * @param nativeKey  解密私钥
     * @param provider   解密服务提供者
     * @return 原文数据流
     */
    public static byte[] verifyAndDecFormP7(InputStream signedData, List<X509CertificateHolder> trustCerts, PrivateKey nativeKey, Provider provider) throws CMSException, CertificateException, IOException, OperatorCreationException, CertException {
        Store certStore = null;
        if (trustCerts != null)
            certStore = new CollectionStore(trustCerts);
        CMSOperatorUtils.SignedDataVerifyResult result = CMSOperatorUtils.verifySignedData(signedData, certStore);
        if (!result.getFailed().isEmpty())
            throw new CMSException("verify signed data failed");
        byte[] origin = CMSOperatorUtils.decryptEnvelopedData(new ByteArrayInputStream(result.getPlaintext()), nativeKey, provider);

        return origin;
    }
}
