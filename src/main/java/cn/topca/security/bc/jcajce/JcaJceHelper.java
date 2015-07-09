package cn.topca.security.bc.jcajce;

import cn.topca.crypto.Cipher;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.AsymmetricKeyUnwrapper;
import org.bouncycastle.operator.SymmetricKeyUnwrapper;

import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-26 20:34
 */
public interface JcaJceHelper {
    Cipher createCipher(
            String algorithm)
            throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException;

    Mac createMac(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    KeyAgreement createKeyAgreement(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    AlgorithmParameterGenerator createAlgorithmParameterGenerator(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    AlgorithmParameters createAlgorithmParameters(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    KeyGenerator createKeyGenerator(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    KeyFactory createKeyFactory(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    KeyPairGenerator createKeyPairGenerator(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    MessageDigest createDigest(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    Signature createSignature(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException;

    CertificateFactory createCertificateFactory(String algorithm)
            throws NoSuchAlgorithmException, NoSuchProviderException, CertificateException;

    AsymmetricKeyUnwrapper createAsymmetricUnwrapper(AlgorithmIdentifier keyEncryptionAlgorithm, PrivateKey keyEncryptionKey);

    SymmetricKeyUnwrapper createSymmetricUnwrapper(AlgorithmIdentifier keyEncryptionAlgorithm, SecretKey keyEncryptionKey);

}
