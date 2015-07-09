package cn.topca.security.bc.cms;

import cn.topca.security.JCAJCEUtils;
import cn.topca.security.bc.asn1.cms.CMSObjectIdentifiers;
import cn.topca.security.bc.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import cn.topca.security.bc.cms.jcajce.JceCMSContentEncryptorBuilder;
import cn.topca.security.bc.cms.jcajce.JceKeyTransEnvelopedRecipient;
import cn.topca.security.bc.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import cn.topca.security.bc.operator.jcajce.JcaContentSignerBuilder;
import cn.topca.security.bc.operator.jcajce.JcaContentVerifierProviderBuilder;
import cn.topca.security.bc.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.cert.CertException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Store;
import org.bouncycastle.x509.X509CertStoreSelector;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 基于BouncyCastle的CMS相关的工具集
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-26 21:56
 */
public class CMSOperatorUtils {
    /**
     * 将数据加密为PKCS#7格式规范的EnvelopedData
     *
     * @param origin              原文
     * @param recipientCert       接收者证书
     * @param encryptionAlgorithm 对称加密算法
     * @return PKCS#7格式加密数据
     * @throws CertificateEncodingException
     * @throws CMSException
     */
    public static CMSEnvelopedData generateEnvelopedData(byte[] origin, Certificate recipientCert, ASN1ObjectIdentifier encryptionAlgorithm) throws CertificateEncodingException, CMSException {
        CMSEnvelopedDataGenerator envelopedDataGenerator = new CMSEnvelopedDataGenerator();
        envelopedDataGenerator.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator((X509Certificate) recipientCert));
        CMSTypedData typedData = new CMSProcessableByteArray(origin);
        OutputEncryptor outputEncryptor = new JceCMSContentEncryptorBuilder(encryptionAlgorithm).build();
        CMSEnvelopedData envelopedData = envelopedDataGenerator.generate(typedData, outputEncryptor);
        // 如果为国产算法则使用国产OID封装数据结构
        if (JCAJCEUtils.isGMAlgorithm(recipientCert.getPublicKey().getAlgorithm())) {
            ContentInfo contentinfo = new ContentInfo(CMSObjectIdentifiers.gm_envelopedData, envelopedData.getContentInfo().getContent());
            envelopedData = new CMSEnvelopedData(contentinfo);
        }
        return envelopedData;
    }

    /**
     * PKCS#7格式数据签名（含明文及签名证书）
     *
     * @param plaintext  明文
     * @param privateKey 签名密钥
     * @param signCert   签名证书
     * @param provider   签名服务提供者
     * @return PKCS#7格式签名数据
     * @throws OperatorCreationException
     * @throws CMSException
     * @throws CertificateEncodingException
     */
    public static CMSSignedData generateSignedData(byte[] plaintext, PrivateKey privateKey, Certificate signCert, Provider provider) throws OperatorCreationException, CMSException, CertificateEncodingException {
        X509CertificateStructure x509 = X509CertificateStructure.getInstance(signCert.getEncoded());
        TBSCertificateStructure x509tbs = x509.getTBSCertificate();
        CMSTypedData msgContent = new CMSProcessableByteArray(plaintext);
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(x509tbs.getIssuer());
        v.add(x509tbs.getSerialNumber());
        IssuerAndSerialNumber id = new IssuerAndSerialNumber(new DERSequence(v));
        SignerIdentifier signerIdentifier = new SignerIdentifier(id);
        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(x509tbs.getSignature().getAlgorithm().getId());
        if (provider != null)
            contentSignerBuilder.setProvider(provider);
        ContentSigner contentSigner = contentSignerBuilder.build(privateKey);
        DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder().build();
        SignerInfoGenerator infoGen = new SignerInfoGenerator(signerIdentifier, contentSigner, digestCalculatorProvider, true);
        gen.addSignerInfoGenerator(infoGen);
        List<X509CertificateHolder> certs = new ArrayList<X509CertificateHolder>();
        X509CertificateHolder certHolder = new X509CertificateHolder(x509);
        certs.add(certHolder);
        Store certStore = new CollectionStore(certs);
        gen.addCertificates(certStore);
        return gen.generate(msgContent, true);
    }

    /**
     * 验证PKCS#7格式数字签名
     *
     * @param signedData PKCS#7格式签名数据输入流
     * @param trustCerts 可信颁发者
     * @return 签名明文数据
     * @throws CMSException
     * @throws CertificateException
     * @throws OperatorCreationException
     * @throws IOException
     * @throws CertException
     */
    public static SignedDataVerifyResult verifySignedData(InputStream signedData, Store trustCerts) throws CMSException, IOException, CertificateException, OperatorCreationException, CertException {
        CMSSignedData cmsSignedData = new CMSSignedData(signedData);
        byte[] plaintext = (byte[]) cmsSignedData.getSignedContent().getContent();
        SignerInformationStore signerInfos = cmsSignedData.getSignerInfos();
        Store certs = cmsSignedData.getCertificates();
        Collection signers = signerInfos.getSigners();
        Iterator it = signers.iterator();
        SignedDataVerifyResult result = new SignedDataVerifyResult();
        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            Collection certCollection = certs.getMatches(signer.getSID());
            Iterator certIt = certCollection.iterator();
            X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
            if (trustCerts != null) {
                X509CertStoreSelector caSelector = new X509CertStoreSelector();
                caSelector.setSubject(cert.getIssuer().getEncoded());
                Collection caCollection = trustCerts.getMatches(caSelector);
                Iterator caIt = caCollection.iterator();
                X509CertificateHolder caCert = (X509CertificateHolder) caIt.next();
                ContentVerifierProvider verifierProvider = new JcaContentVerifierProviderBuilder().build(caCert);
                if (!cert.isSignatureValid(verifierProvider)) {
                    result.setFailed(signer.getSID(), VerifyStatus.UnTrusted);
                    continue;
                }
            }
            if (!signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(cert))) {
                result.setFailed(signer.getSID(), VerifyStatus.Failed);
                continue;
            }
            result.setSuccess(signer.getSID());
        }
        result.setPlaintext(plaintext);
        return result;
    }

    /**
     * 解包（解密）PKCS#7格式数据包
     *
     * @param envelopedData 已封装（加密）的PKCS#7数据包输入流
     * @param privateKey    接收者（解密）私钥
     * @param provider      解密服务提供者
     * @return 原文数据
     * @throws CMSException
     * @throws IOException
     */
    public static byte[] decryptEnvelopedData(InputStream envelopedData, PrivateKey privateKey, Provider provider) throws CMSException, IOException {
        CMSEnvelopedData cmsEnvelopedData = new CMSEnvelopedData(envelopedData);

        RecipientInformationStore recipients = cmsEnvelopedData.getRecipientInfos();

        Collection c = recipients.getRecipients();
        Iterator it = c.iterator();

        if (it.hasNext()) {
            RecipientInformation recipient = (RecipientInformation) it.next();
            JceKeyTransEnvelopedRecipient keyTransEnvelopedRecipient = new JceKeyTransEnvelopedRecipient(privateKey);
            if (provider != null)
                keyTransEnvelopedRecipient.setProvider(provider);
            return recipient.getContent(keyTransEnvelopedRecipient);
        }
        return null;
    }

    private static String toStringSerialNumber(BigInteger serialnumber) {
        String sn = serialnumber.toString(16).toUpperCase();
        if (sn.length() % 2 == 1)
            sn = "0" + sn;
        return sn;
    }

    public final static class SignedDataVerifyResult {
        private byte[] plaintext;
        private Set<SignerId> success = new HashSet<SignerId>();
        private Map<SignerId, VerifyStatus> failed = new HashMap<SignerId, VerifyStatus>();

        SignedDataVerifyResult() {
        }

        void setPlaintext(byte[] plaintext) {
            this.plaintext = plaintext;
        }

        void setSuccess(SignerId sid) {
            success.add(sid);
        }

        void setFailed(SignerId sid, VerifyStatus unTrusted) {
            failed.put(sid, unTrusted);
        }

        public byte[] getPlaintext() {
            return this.plaintext;
        }

        public Set<SignerId> getSuccess() {
            return success;
        }

        public Map<SignerId, VerifyStatus> getFailed() {
            return failed;
        }

    }

    public static enum VerifyStatus {
        Success, Failed, UnTrusted
    }

}
