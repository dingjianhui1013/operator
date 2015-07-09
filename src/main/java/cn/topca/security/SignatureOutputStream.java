package cn.topca.security;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-03 09:53
 */
public class SignatureOutputStream extends OutputStream {
    private final Signature sig;

    public SignatureOutputStream(Signature sig) {
        this.sig = sig;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        try {
            sig.update(b, off, len);
        } catch (SignatureException e) {
            throw new IOException("signature problem: " + e, e);
        }
    }

    public void write(int b) throws IOException {
        try {
            sig.update((byte) b);
        } catch (SignatureException e) {
            throw new IOException("signature problem: " + e, e);
        }
    }
}
