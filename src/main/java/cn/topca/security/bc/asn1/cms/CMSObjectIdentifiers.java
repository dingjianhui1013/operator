package cn.topca.security.bc.asn1.cms;

import cn.topca.security.bc.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-27 06:15
 */
public interface CMSObjectIdentifiers extends org.bouncycastle.asn1.cms.CMSObjectIdentifiers {
    static final ASN1ObjectIdentifier gm_data = GMObjectIdentifiers.data;
    static final ASN1ObjectIdentifier gm_signedData = GMObjectIdentifiers.signedData;
    static final ASN1ObjectIdentifier gm_envelopedData = GMObjectIdentifiers.envelopedData;
    static final ASN1ObjectIdentifier gm_signedAndEnvelopedData = GMObjectIdentifiers.signedAndEnvelopedData;
    static final ASN1ObjectIdentifier gm_digestedData = GMObjectIdentifiers.digestedData;
    static final ASN1ObjectIdentifier gm_encryptedData = GMObjectIdentifiers.encryptedData;
}
