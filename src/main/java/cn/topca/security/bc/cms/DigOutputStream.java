package cn.topca.security.bc.cms;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 19:18
 */
class DigOutputStream extends OutputStream {
    private final MessageDigest dig;

    DigOutputStream(MessageDigest dig) {
        this.dig = dig;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        dig.update(b, off, len);
    }

    public void write(int b) throws IOException {
        dig.update((byte) b);
    }
}