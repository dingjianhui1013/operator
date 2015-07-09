package cn.topca.security.bc.jcajce.io;

import cn.topca.crypto.Mac;

import java.io.IOException;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 18:03
 */
public class MacOutputStream extends org.bouncycastle.jcajce.io.MacOutputStream {
    private Mac mac;

    public MacOutputStream(Mac mac) {
        super(null);
        this.mac = mac;
    }

    @Override
    public void write(int b) throws IOException {
        mac.update((byte) b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        mac.update(b, off, len);
    }

    @Override
    public byte[] getMac() {
        return mac.doFinal();
    }
}
