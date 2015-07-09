package cn.topca.security.bc.cms;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.util.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 19:38
 */
class CMSProcessableInputStream implements CMSProcessable, CMSReadable {
    private InputStream input;
    private boolean used = false;

    public CMSProcessableInputStream(
            InputStream input) {
        this.input = input;
    }

    public InputStream getInputStream() {
        checkSingleUsage();

        return input;
    }

    public void write(OutputStream zOut)
            throws IOException, CMSException {
        checkSingleUsage();

        Streams.pipeAll(input, zOut);
        input.close();
    }

    public Object getContent() {
        return getInputStream();
    }

    private synchronized void checkSingleUsage() {
        if (used) {
            throw new IllegalStateException("CMSProcessableInputStream can only be used once");
        }

        used = true;
    }
}
