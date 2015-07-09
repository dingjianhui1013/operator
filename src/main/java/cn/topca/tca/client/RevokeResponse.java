package cn.topca.tca.client;

import cn.topca.connection.Message;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 证书吊销响应数据
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-03 12:54
 */
public class RevokeResponse implements Message {
    private String certSerialNumber;
    private String certRevokeDate;
    private String certRevokeReason;

    public String getCertSerialNumber() {
        return certSerialNumber;
    }

    public void setCertSerialNumber(String certSerialNumber) {
        this.certSerialNumber = certSerialNumber;
    }

    public String getCertRevokeDate() {
        return certRevokeDate;
    }

    public void setCertRevokeDate(String certRevokeDate) {
        this.certRevokeDate = certRevokeDate;
    }

    public String getCertRevokeReason() {
        return certRevokeReason;
    }

    public void setCertRevokeReason(String certRevokeReason) {
        this.certRevokeReason = certRevokeReason;
    }
}
