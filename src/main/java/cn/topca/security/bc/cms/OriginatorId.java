package cn.topca.security.bc.cms;

import org.bouncycastle.util.Arrays;

import java.security.cert.X509CertSelector;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 19:45
 */
class OriginatorId
        extends X509CertSelector {
    public int hashCode() {
        int code = Arrays.hashCode(this.getSubjectKeyIdentifier());

        if (this.getSerialNumber() != null) {
            code ^= this.getSerialNumber().hashCode();
        }

        if (this.getIssuerAsString() != null) {
            code ^= this.getIssuerAsString().hashCode();
        }

        return code;
    }

    public boolean equals(
            Object o) {
        if (!(o instanceof OriginatorId)) {
            return false;
        }

        OriginatorId id = (OriginatorId) o;

        return Arrays.areEqual(this.getSubjectKeyIdentifier(), id.getSubjectKeyIdentifier())
                && equalsObj(this.getSerialNumber(), id.getSerialNumber())
                && equalsObj(this.getIssuerAsString(), id.getIssuerAsString());
    }

    private boolean equalsObj(Object a, Object b) {
        return (a != null) ? a.equals(b) : b == null;
    }
}