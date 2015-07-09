package cn.topca.tca.client;

import cn.topca.connection.Message;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 证书吊销请求数据
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-03 12:53
 */
public class RevokeRequest implements Message {
    public static final String unspecified = "unspecified";
    public static final String keyCompromise = "keyCompromise";
    public static final String caCompromise = "caCompromise";
    public static final String affiliationChanged = "affiliationChanged";
    public static final String superseded = "superseded";
    public static final String cessationOfOperation  = "cessationOfOperation";
    public static final String certificateHold = "certificateHold";
    public static final String unknown = "unknown";
    public static final String removeFromCRL = "removeFromCRL";
    public static final String privilegeWithdrawn = "privilegeWithdrawn";
    public static final String aACompromise = "aACompromise";

    private String certSerialNumber;
    private String certRevokeReason;
    private String certReqChallenge;
    private String accountHash;

    public String getCertSerialNumber() {
        return certSerialNumber;
    }

    public void setCertSerialNumber(String certSerialNumber) {
        this.certSerialNumber = certSerialNumber;
    }

    public String getCertRevokeReason() {
        return certRevokeReason;
    }

    public void setCertRevokeReason(String certRevokeReason) {
        this.certRevokeReason = certRevokeReason;
    }

    public String getCertReqChallenge() {
        return certReqChallenge;
    }

    public void setCertReqChallenge(String certReqChallenge) {
        this.certReqChallenge = certReqChallenge;
    }

    public String getAccountHash() {
        return accountHash;
    }

    public void setAccountHash(String accountHash) {
        this.accountHash = accountHash;
    }
}
