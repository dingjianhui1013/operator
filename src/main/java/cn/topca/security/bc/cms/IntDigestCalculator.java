package cn.topca.security.bc.cms;

import java.security.NoSuchAlgorithmException;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 19:12
 */
interface IntDigestCalculator {
    byte[] getDigest()
            throws NoSuchAlgorithmException;
}