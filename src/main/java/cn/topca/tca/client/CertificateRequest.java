package cn.topca.tca.client;

import cn.topca.connection.Message;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 证书颁发请求数据
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-03 12:52
 */
public class CertificateRequest implements Message {

    private String userName;
    private String userSurname;
    private String userSerialnumber;
    private String userEmail;
    private String userOrganization;
    private String userOrgunit;
    private String userCountry;
    private String userState;
    private String userLocality;
    private String userStreet;
    private String userDns;
    private String userIp;
    private String userTitle;
    private String userMobilePhone;
    private String userDescription;
    private String userAdditionalField1;
    private String userAdditionalField2;
    private String userAdditionalField3;
    private String userAdditionalField4;
    private String userAdditionalField5;
    private String userAdditionalField6;
    private String userAdditionalField7;
    private String userAdditionalField8;
    private String userAdditionalField9;
    private String userAdditionalField10;

    private String certReqBuf;
    private String accountHash;
    private String origCertSerialnumber;
    private String certReqChallenge;
    private String certNotBefore;
    private String certNotAfter;

    // 加密证书使用原私钥更新
    private String useExistingKeySet;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserSerialnumber() {
        return userSerialnumber;
    }

    public void setUserSerialnumber(String userSerialnumber) {
        this.userSerialnumber = userSerialnumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserOrganization() {
        return userOrganization;
    }

    public void setUserOrganization(String userOrganization) {
        this.userOrganization = userOrganization;
    }

    public String getUserOrgunit() {
        return userOrgunit;
    }

    public void setUserOrgunit(String userOrgunit) {
        this.userOrgunit = userOrgunit;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserLocality() {
        return userLocality;
    }

    public void setUserLocality(String userLocality) {
        this.userLocality = userLocality;
    }

    public String getUserStreet() {
        return userStreet;
    }

    public void setUserStreet(String userStreet) {
        this.userStreet = userStreet;
    }

    public String getUserDns() {
        return userDns;
    }

    public void setUserDns(String userDns) {
        this.userDns = userDns;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getUserMobilePhone() {
        return userMobilePhone;
    }

    public void setUserMobilePhone(String userMobilePhone) {
        this.userMobilePhone = userMobilePhone;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserAdditionalField1() {
        return userAdditionalField1;
    }

    public void setUserAdditionalField1(String userAdditionalField1) {
        this.userAdditionalField1 = userAdditionalField1;
    }

    public String getUserAdditionalField2() {
        return userAdditionalField2;
    }

    public void setUserAdditionalField2(String userAdditionalField2) {
        this.userAdditionalField2 = userAdditionalField2;
    }

    public String getUserAdditionalField3() {
        return userAdditionalField3;
    }

    public void setUserAdditionalField3(String userAdditionalField3) {
        this.userAdditionalField3 = userAdditionalField3;
    }

    public String getUserAdditionalField4() {
        return userAdditionalField4;
    }

    public void setUserAdditionalField4(String userAdditionalField4) {
        this.userAdditionalField4 = userAdditionalField4;
    }

    public String getUserAdditionalField5() {
        return userAdditionalField5;
    }

    public void setUserAdditionalField5(String userAdditionalField5) {
        this.userAdditionalField5 = userAdditionalField5;
    }

    public String getUserAdditionalField6() {
        return userAdditionalField6;
    }

    public void setUserAdditionalField6(String userAdditionalField6) {
        this.userAdditionalField6 = userAdditionalField6;
    }

    public String getUserAdditionalField7() {
        return userAdditionalField7;
    }

    public void setUserAdditionalField7(String userAdditionalField7) {
        this.userAdditionalField7 = userAdditionalField7;
    }

    public String getUserAdditionalField8() {
        return userAdditionalField8;
    }

    public void setUserAdditionalField8(String userAdditionalField8) {
        this.userAdditionalField8 = userAdditionalField8;
    }

    public String getUserAdditionalField9() {
        return userAdditionalField9;
    }

    public void setUserAdditionalField9(String userAdditionalField9) {
        this.userAdditionalField9 = userAdditionalField9;
    }

    public String getUserAdditionalField10() {
        return userAdditionalField10;
    }

    public void setUserAdditionalField10(String userAdditionalField10) {
        this.userAdditionalField10 = userAdditionalField10;
    }

    public String getCertReqBuf() {
        return certReqBuf;
    }

    public void setCertReqBuf(String certReqBuf) {
        this.certReqBuf = certReqBuf;
    }

    public String getAccountHash() {
        return accountHash;
    }

    public void setAccountHash(String accountHash) {
        this.accountHash = accountHash;
    }

    public String getOrigCertSerialnumber() {
        return origCertSerialnumber;
    }

    public void setOrigCertSerialnumber(String origCertSerialnumber) {
        this.origCertSerialnumber = origCertSerialnumber;
    }

    public String getCertReqChallenge() {
        return certReqChallenge;
    }

    public void setCertReqChallenge(String certReqChallenge) {
        this.certReqChallenge = certReqChallenge;
    }

    public String getCertNotBefore() {
        return certNotBefore;
    }

    public void setCertNotBefore(String certNotBefore) {
        this.certNotBefore = certNotBefore;
    }

    public String getCertNotAfter() {
        return certNotAfter;
    }

    public void setCertNotAfter(String certNotAfter) {
        this.certNotAfter = certNotAfter;
    }

    public String getUseExistingKeySet() {
        return useExistingKeySet;
    }

    public void setUseExistingKeySet(String useExistingKeySet) {
        this.useExistingKeySet = useExistingKeySet;
    }
}
