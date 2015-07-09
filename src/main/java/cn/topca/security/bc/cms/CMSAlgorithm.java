package cn.topca.security.bc.cms;

import cn.topca.security.bc.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-26 21:53
 */
public class CMSAlgorithm extends org.bouncycastle.cms.CMSAlgorithm {
    public static final ASN1ObjectIdentifier SM4 = GMObjectIdentifiers.sm4;
}