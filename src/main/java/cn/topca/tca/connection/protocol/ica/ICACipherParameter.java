package cn.topca.tca.connection.protocol.ica;

import cn.topca.connection.protocol.CipherParameter;

import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * <p>ICA密文传输协议参数接口</p>
 * <p>加密过程：<br/>
 * 1. 根据指定对称密钥密码算法生成对称密钥S<br/>
 * 2. 使用对称密钥S加密消息数据
 * 3. 使用接收者数字证书，根据指定非对称密钥密码算法加密对称密钥S
 * 4. 将密文数据、加密后的对称密钥S和接收者数字证书进行封装并使用本地私钥对封装后的数据进行签名（SignedData）
 * </p>
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-21 11:06
 */
public class ICACipherParameter extends ICABasicParameter implements CipherParameter {
    private Certificate recipientCertificate;
    private PrivateKey nativePrivateKey;
    private Certificate nativeCertificate;
    private String encryptionAlgorithm;
    private String encryptionProvider;

    /**
     * <p>加密ICA协议参数</p>
     *
     * @param version              协议版本（默认3）
     * @param charsetEncoding      协议字符集编码名称（默认GBK）
     * @param recipientCertificate 接收者加密证书
     * @param nativePrivateKey     本地解密私钥
     * @param encryptionAlgorithm  加密算法
     * @param encryptionProvider   加密服务提供者
     * @see ICABasicParameter
     */
    public ICACipherParameter(int version, String charsetEncoding, Certificate recipientCertificate, PrivateKey nativePrivateKey, Certificate nativeCertificate, String encryptionAlgorithm, String encryptionProvider) {
        super(version, charsetEncoding);
        this.recipientCertificate = recipientCertificate;
        this.nativePrivateKey = nativePrivateKey;
        this.nativeCertificate = nativeCertificate;
        if (encryptionAlgorithm == null)
            encryptionAlgorithm = "DESEDE/CBC/PKCS5Padding";
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.encryptionProvider = encryptionProvider;
    }

    /**
     * <p>加密ICA协议参数</p>
     *
     * @param recipientCertificate 接收者证书（基本密钥用法必须包含密钥加密、数据加密、数据签名）
     * @param nativePrivateKey     本地私钥
     * @param nativeCertificate    本地证书（基本密钥用法必须包含密钥加密、数据加密、数据签名）
     * @see ICABasicParameter
     */
    public ICACipherParameter(Certificate recipientCertificate, PrivateKey nativePrivateKey, Certificate nativeCertificate) {
        this(-1, null, recipientCertificate, nativePrivateKey, nativeCertificate, null, null);
    }

    /**
     * 返回消息接收者数字证书。
     * 接收者数字证书用于保护（加密）传输过程中的消息加密密钥，
     * 接收者数字证书如果为X.509格式，则密钥扩展用法必须为“关键项”，且至少包含“密钥加密”，“数据加密”，“数据签名”。
     *
     * @return 消息接收者数字证书
     */
    public Certificate getRecipientCertificate() {
        return recipientCertificate;
    }

    /**
     * 返回本地私钥。
     * 本地私钥用于获取（解密）传输数据中的加密密钥，以及对传输数据进行签名（抗抵赖）。
     *
     * @return 本地私钥
     */
    public PrivateKey getNativePrivateKey() {
        return nativePrivateKey;
    }

    /**
     * 返回本地证书。
     * 本地证书用于提供给接收者验明身份。
     *
     * @return 本地证书
     */
    public Certificate getNativeCertificate() {
        return nativeCertificate;
    }

    /**
     * 返回加密算法名称。
     * 该算法用于保护（加密）消息内容。
     *
     * @return 加密算法名称
     */
    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    /**
     * 返回加密服务提供者名称。
     * 使用指定加密服务提供者对消息内容进行加密。
     *
     * @return 加密服务提供者名称
     */
    public String getEncryptionProvider() {
        return encryptionProvider;
    }
}
