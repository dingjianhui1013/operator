package cn.topca.security.bc.cms;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSTypedData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 13:56
 * @see org.bouncycastle.cms.CMSProcessableByteArray
 */
public class CMSProcessableByteArray implements CMSTypedData, CMSReadable {
    private final ASN1ObjectIdentifier type;
    private final byte[] bytes;

    public CMSProcessableByteArray(
            byte[] bytes) {
        this(new ASN1ObjectIdentifier(CMSObjectIdentifiers.data.getId()), bytes);
    }

    public CMSProcessableByteArray(
            ASN1ObjectIdentifier type,
            byte[] bytes) {
        this.type = type;
        this.bytes = bytes;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    public void write(OutputStream zOut)
            throws IOException, CMSException {
        zOut.write(bytes);
    }

    public Object getContent() {
        return bytes.clone();
    }

    public ASN1ObjectIdentifier getContentType() {
        return type;
    }
}