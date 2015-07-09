package cn.topca.security.bc.cms;

import org.bouncycastle.asn1.ASN1Set;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 19:25
 */
interface AuthAttributesProvider {
    ASN1Set getAuthAttributes();
}