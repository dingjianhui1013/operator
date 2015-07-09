package cn.topca.security.bc.asn1.gm;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 国密算法ASN1ObjectIdentifier对象集合
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 07:58
 */
public interface GMObjectIdentifiers {
    //
    // 中国商用密码算法 OBJECT IDENTIFIER ::= {
    //       iso(1) member-body(2) cn(156) 国家密码管理局(10197) 密码算法(1) }
    //
    static final ASN1ObjectIdentifier gmAlgorithm = new ASN1ObjectIdentifier("1.2.156.10197.1");


    // CA代码对象标识符
    static final ASN1ObjectIdentifier ca = new ASN1ObjectIdentifier("1.2.156.10197.4.3");

    // 分组密码算法
    static final ASN1ObjectIdentifier blockEncryptionAlgorithm = gmAlgorithm.branch("100");
    static final ASN1ObjectIdentifier sm6 = gmAlgorithm.branch("101");
    static final ASN1ObjectIdentifier sm1 = gmAlgorithm.branch("102");
    static final ASN1ObjectIdentifier ssf33 = gmAlgorithm.branch("103");
    static final ASN1ObjectIdentifier sm4 = gmAlgorithm.branch("104");
    static final ASN1ObjectIdentifier sm7 = gmAlgorithm.branch("105");
    static final ASN1ObjectIdentifier sm8 = gmAlgorithm.branch("106");

    // 序列密码算法
    static final ASN1ObjectIdentifier streamEncryptionAlgorithm = gmAlgorithm.branch("200");
    static final ASN1ObjectIdentifier sm5 = gmAlgorithm.branch("201");

    // 公钥密码算法
    static final ASN1ObjectIdentifier publicKeyAlgorithm = gmAlgorithm.branch("300");
    static final ASN1ObjectIdentifier sm2 = gmAlgorithm.branch("301");
    static final ASN1ObjectIdentifier sm9 = gmAlgorithm.branch("302");
    static final ASN1ObjectIdentifier rsa = gmAlgorithm.branch("310");

    // 杂凑算法
    static final ASN1ObjectIdentifier digestAlgorithm = gmAlgorithm.branch("400");
    static final ASN1ObjectIdentifier sm3 = gmAlgorithm.branch("401");
    static final ASN1ObjectIdentifier sha1 = gmAlgorithm.branch("410");
    static final ASN1ObjectIdentifier sha256 = gmAlgorithm.branch("411");

    // 组合运算算法
    static final ASN1ObjectIdentifier combinedAlgorithm = gmAlgorithm.branch("500");
    static final ASN1ObjectIdentifier sm3withSM2 = gmAlgorithm.branch("501");
    static final ASN1ObjectIdentifier sha1withSM2 = gmAlgorithm.branch("502");
    static final ASN1ObjectIdentifier sha256withSM2 = gmAlgorithm.branch("503");
    static final ASN1ObjectIdentifier sm3withRSA = gmAlgorithm.branch("504");
    static final ASN1ObjectIdentifier sha1withRSA = gmAlgorithm.branch("505");
    static final ASN1ObjectIdentifier sha256withRSA = gmAlgorithm.branch("506");

    // 加密消息语法(CMS)
    static final ASN1ObjectIdentifier gmCms = new ASN1ObjectIdentifier("1.2.156.10197.6.1.4.2");
    static final ASN1ObjectIdentifier data = new ASN1ObjectIdentifier(gmCms + ".1");
    static final ASN1ObjectIdentifier signedData = new ASN1ObjectIdentifier(gmCms + ".2");
    static final ASN1ObjectIdentifier envelopedData = new ASN1ObjectIdentifier(gmCms + ".3");
    static final ASN1ObjectIdentifier signedAndEnvelopedData = new ASN1ObjectIdentifier(gmCms + ".4");
    static final ASN1ObjectIdentifier digestedData = new ASN1ObjectIdentifier(gmCms + ".5");
    static final ASN1ObjectIdentifier encryptedData = new ASN1ObjectIdentifier(gmCms + ".6");

}
